package ru.prism.module.impl.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import ru.prism.manager.event_impl.EventJump;
import ru.prism.manager.event_impl.EventRender3D;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.event_impl.WorldLoadEvent;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.preview.ModulePreview;
import ru.prism.module.api.preview.PreviewContext;
import ru.prism.module.api.preview.PreviewSettings;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.ButtonSetting;
import ru.prism.module.api.settings.impl.ColorSetting;
import ru.prism.module.api.settings.impl.ModeSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.animation.Animation;
import ru.prism.utils.animation.Easings;
import ru.prism.utils.colors.ColorUtil;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.*;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

@ModuleInfo(
        name = "Jump Circle",
        desc = "Рисует кружок под тобой при прыжке, как у наркомана.",
        category = Category.VISUALS
)
public class JumpCircle extends Module implements ModulePreview {
    private final List<Circle> circles = new ArrayList<>();

    public ButtonSetting previewButton = PreviewSettings.button(this);

    public ModeSetting mode = new ModeSetting(this, "Режим", "Кружок", "Куб");

    public SliderSetting size_c = new SliderSetting(this,"Размер",1,0.2F,3,0.1F).setVisible(() -> mode.is("Кружок"));
    public BooleanSetting glow = new BooleanSetting(this, "Свечение", true).setVisible(() -> mode.is("Кружок"));

    public ModeSetting typeColor = new ModeSetting(this, "Режим цвета", "Тема", "Свой").setVisible(() -> mode.is("Куб"));
    public ColorSetting tintColor = new ColorSetting(this, "Цвет", 0xFF00FFFF)
            .setVisible(() -> mode.is("Куб") && typeColor.is("Свой"));
    public SliderSetting count = new SliderSetting(this, "Кубиков", 3, 1, 10, 1).setVisible(() -> mode.is("Куб"));
    public SliderSetting size  = new SliderSetting(this, "Размер",  0.18F, 0.05F, 0.40F, 0.01F).setVisible(() -> mode.is("Куб"));

    private final PreviewSettings previewSettings = PreviewSettings.of(this, 3.5F, 0F, 2F);

    BufferAllocator allocator = new BufferAllocator(1 << 18);
    private final BufferAllocator cubeAllocator = new BufferAllocator(1 << 16);

    private static final int QUAD_BUFFER_SIZE_BYTES = 1 << 10;
    private static final RenderPipeline TEXTURED_QUADS_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_TEX_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "pipeline/world/textured_quads"))
                    .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS)
                    .withCull(false)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withBlend(BlendFunction.LIGHTNING)
                    .build()
    );

    // кубики живут вокруг самого игрока и прыгают без остановки — настраивать нечего

    // ── константы из DashCubes ────────────────────────────────────────────────
    private static final int    RES_PX         = 16;       // сетка 1/16 блока
    private static final long   LIFETIME_MS    = 2000L;
    private static final double SPAWN_MIN_DST  = 0.6;
    private static final double SPAWN_MAX_DST  = 2.6;
    private static final double MAX_OWNER_DIST = 6.0;
    private static final long   RANGE_FADE_MS  = 400L;
    private static final int[]  JUMP_YAWS      = {0, 90, 180, 270};

    private final Random          random    = new Random();
    private final List<Cube>      cubes     = new ArrayList<>();
    private final List<Cube>      visible   = new ArrayList<>();

    @Override
    protected void onDisable() {
        circles.clear();
        cubes.clear();
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        circles.clear();
        cubes.clear();
    }

    @EventHandler
    public void onJump(EventJump e) {
        if (mode.is("Кружок")) {
            circles.add(new Circle(mc.player.getEntityPos().add(0, 0.05, 0)));
        } else {
            for (Cube c : cubes) c.tryStartJump();
        }
    }

    // ───────────────────────────── предпоказ ─────────────────────────────

    @Override
    public PreviewSettings previewSettings() {
        return previewSettings;
    }

    @Override
    public void previewSpawn(PreviewContext ctx) {
        circles.add(new Circle(ctx.anchor().add(0, 0.05, 0)));
    }

    /** Кубики спавнятся сами, а прыжок перезапускаем сразу после предыдущего — чтобы не замирали. */
    @Override
    public void previewTick(PreviewContext ctx) {
        for (Cube c : cubes) c.tryStartJump();
    }

    @Override
    public void previewStop() {
        circles.clear();
        cubes.clear();
    }

    @EventHandler
    public void onTick(EventTick e) {
        if (!mode.is("Куб")) return;
        if (mc.player == null || mc.world == null) return;

        Vec3d playerPos = mc.player.getEntityPos();

        // спавним count новых кубиков в тик — точно как DashCubes
        List<BlockPos> candidates = getPlaceableAround(playerPos, SPAWN_MIN_DST, SPAWN_MAX_DST, 3);
        int toSpawn = count.getValue().intValue();
        for (int i = 0; i < toSpawn && !candidates.isEmpty(); i++) {
            int idx = random.nextInt(candidates.size());
            Vec3d sp = findSpawnPoint(candidates.get(idx), playerPos);
            if (sp != null) cubes.add(new Cube(sp));
        }

        cubes.forEach(Cube::updateLogic);
        cubes.removeIf(Cube::isDead);
    }

    @EventHandler
    public void onRender(EventRender3D e) {
        if (mode.is("Кружок")) {
            renderCircles(e);
        } else {
            renderCubes(e);
        }
    }

    private void renderCircles(EventRender3D e) {
        if (circles.isEmpty()) {
            return;
        }


        circles.removeIf(c -> System.currentTimeMillis() - c.time > 2500);

        if (circles.isEmpty()) {
            return;
        }
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(allocator);
        MatrixStack pose = e.getMatrixStack();

        int color = ColorUtil.fade(1);
        int alpha = 255;

        for (Circle c : circles) {
            if (System.currentTimeMillis() - c.time > 800 && !c.isBack) {
                c.animation.run(0, 0.5F, Easings.SINE_OUT);
                c.animation2.run(2, 0.5F, Easings.SINE_OUT);
                c.isBack = true;
            }

            c.animation.update();
            c.animation2.update();
            float rad = (float) c.animation2.getValue();

            double posX = c.vector3d.x - mc.gameRenderer.getCamera().getCameraPos().x;
            double posY = c.vector3d.y - mc.gameRenderer.getCamera().getCameraPos().y;
            double posZ = c.vector3d.z - mc.gameRenderer.getCamera().getCameraPos().z;

            float size = size_c.getValue() * rad;

            pose.push();
            pose.translate(posX, posY, posZ);
            pose.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90 ));

            MatrixStack.Entry entry = pose.peek();
            Matrix4f matrix4f = entry.getPositionMatrix();
            Matrix3f normalMatrix = entry.getNormalMatrix();
            VertexConsumer buffer = immediate.getBuffer(ROMB_ESP.apply(Identifier.of("client",
                    "textures/visuals/jump_new.png")));

            drawTexturedQuad(buffer, matrix4f, normalMatrix, -size / 2f, -size / 2f, size, size, new int[]{
                    ColorUtil.fade(0),
                    ColorUtil.fade(90),
                    ColorUtil.fade(180)
                    ,ColorUtil.fade(360)
            }, (int) (alpha * c.animation.get()));

            pose.pop();

            if (glow.getValue()) {
                pose.push();
                pose.translate(posX, posY, posZ);

                drawGlowLayers(immediate, pose, size , (float) c.animation.get());
                pose.pop();
            }
        }

        immediate.draw();

    }

    private void renderCubes(EventRender3D e) {
        if (mc.player == null || mc.world == null || cubes.isEmpty()) return;

        float pt  = e.getTickDelta();
        Vec3d cam = mc.gameRenderer.getCamera().getCameraPos();
        MatrixStack pose = e.getMatrixStack();
        float s = size.getValue();

        int base = typeColor.is("Тема") ? ColorUtil.getClientColor1(1) : tintColor.getValue();
        int cr = (base >> 16) & 0xFF;
        int cg = (base >>  8) & 0xFF;
        int cb =  base        & 0xFF;

        // предвычисляем матрицы всех видимых кубиков
        visible.clear();
        for (Cube c : cubes) {
            if (c.isDead()) continue;
            float alpha = c.getAlpha();
            if (alpha < 0.01f) continue;

            double wx = c.spawnPos.x - cam.x;
            double wy = c.spawnPos.y + c.getJumpYOffset(pt) - cam.y;
            double wz = c.spawnPos.z - cam.z;

            // куб: поворот при прыжке + масштаб по alpha (вырастает/исчезает)
            pose.push();
            pose.translate(wx, wy, wz);
            if (c.jumpTicksMax > 0) {
                // субтик-интерполяция: lerp между предыдущим и текущим значением jumpTicks
                float tLerped = MathHelper.lerp(pt, (float) c.prevJumpTicks, (float) c.jumpTicks);
                float phase   = 1.0f - tLerped / (float) c.jumpTicksMax;
                pose.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(c.jumpYaw));
                pose.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f * phase));
            }
            pose.scale(alpha, alpha, alpha);
            c.cubeMatrix.set(pose.peek().getPositionMatrix());
            pose.pop();

            // billboard для glow
            pose.push();
            pose.translate(wx, wy, wz);
            pose.multiply(mc.gameRenderer.getCamera().getRotation());
            pose.scale(alpha, alpha, alpha);
            c.glowMatrix.set(pose.peek().getPositionMatrix());
            pose.pop();

            c.cachedAlpha = alpha;
            visible.add(c);
        }

        if (visible.isEmpty()) return;

        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(cubeAllocator);


        VertexConsumer buf = immediate.getBuffer(FILL_LAYER);
        for (Cube c : visible) {
            int fa = (int) (c.cachedAlpha * 0.75f * 255);
            if (fa > 0) drawCubeFill(buf, c.cubeMatrix, s, cr, cg, cb, fa);
        }


        buf = immediate.getBuffer(LINE_LAYER);
        for (Cube c : visible) {
            int ea = (int) (c.cachedAlpha  * 255);
            if (ea > 0) drawCubeEdges(buf, c.cubeMatrix, s, cr, cg, cb, ea);
        }

        buf = immediate.getBuffer(GLOW_LAYER_BIG);
        for (Cube c : visible) drawGlow(buf, c.glowMatrix, cr, cg, cb, (int)(35  * c.cachedAlpha), s * 5.5f);

        buf = immediate.getBuffer(GLOW_LAYER_SMALL);
        for (Cube c : visible) drawGlow(buf, c.glowMatrix, cr, cg, cb, (int)(40 * c.cachedAlpha), s * 1.8f);

        immediate.draw();
    }

    public static final Function<Identifier, RenderLayer> ROMB_ESP =
            Util.memoize(texture -> {
                RenderSetup setup = RenderSetup.builder(TEXTURED_QUADS_PIPELINE)
                        .texture("Sampler0", texture)
                        .translucent()
                        .expectedBufferSize(1536)
                        .build();
                return RenderLayer.of("wtex", setup);
            });

    private void drawGlowLayers(VertexConsumerProvider.Immediate immediate, MatrixStack pose, float radius, float alpha) {
        VertexConsumer buffer = immediate.getBuffer(ROMB_ESP.apply(Identifier.of("client",
                "textures/visuals/jump_new.png")));
        int layers = 20;
        float maxHeight = radius * 0.15f;
        float expand   = radius * 0.25f;

        Vector3f normal = new Vector3f(0, 1, 0);
        pose.peek().getNormalMatrix().transform(normal);
        normal.normalize();

        for (int i = 0; i < layers; i++) {
            float progress   = i / (float) layers;
            float yOff       = maxHeight * progress;
            float layerAlpha = alpha * (1f - progress) * 0.15f;
            if (layerAlpha <= 0.004f) continue;
            float r    = radius + expand * progress;
            float half = r / 2f;
            int   a    = (int) (layerAlpha * 255);

            Matrix4f matrix = pose.peek().getPositionMatrix();

            int c0   = ColorUtil.fade(0);
            int c90  = ColorUtil.fade(90);
            int c180 = ColorUtil.fade(180);
            int c270 = ColorUtil.fade(270);

            buffer.vertex(matrix, -half, yOff, -half).color((c0   >> 16) & 0xFF, (c0   >> 8) & 0xFF, c0   & 0xFF, a).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(normal.x, normal.y, normal.z);
            buffer.vertex(matrix, -half, yOff,  half).color((c90  >> 16) & 0xFF, (c90  >> 8) & 0xFF, c90  & 0xFF, a).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(normal.x, normal.y, normal.z);
            buffer.vertex(matrix,  half, yOff,  half).color((c180 >> 16) & 0xFF, (c180 >> 8) & 0xFF, c180 & 0xFF, a).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(normal.x, normal.y, normal.z);
            buffer.vertex(matrix,  half, yOff, -half).color((c270 >> 16) & 0xFF, (c270 >> 8) & 0xFF, c270 & 0xFF, a).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(normal.x, normal.y, normal.z);
        }
    }

    private void drawTexturedQuad(VertexConsumer buffer, Matrix4f matrix, Matrix3f normalMatrix, float x, float y,
                                  float width, float height,  int[] ints, int alpha) {


        Vector3f normal = new Vector3f(0, 0, 1);
        normalMatrix.transform(normal);
        normal.normalize();

        float x1 = x;
        float y1 = y;
        float x2 = x + width;
        float y2 = y + height;

        buffer.vertex(matrix, x1, y1, 0.0f).color(ColorUtil.replAlpha(ints[0],alpha)).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(normal.x, normal.y, normal.z);
        buffer.vertex(matrix, x2, y1, 0.0f).color(ColorUtil.replAlpha(ints[1],alpha)).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(normal.x, normal.y, normal.z);
        buffer.vertex(matrix, x2, y2, 0.0f).color(ColorUtil.replAlpha(ints[2],alpha)).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(normal.x, normal.y, normal.z);
        buffer.vertex(matrix, x1, y2, 0.0f).color(ColorUtil.replAlpha(ints[3],alpha)).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(normal.x, normal.y, normal.z);
    }

    // ── спавн ────────────────────────────────────────────────────────────────

    private List<BlockPos> getPlaceableAround(Vec3d center, double minDst, double maxDst, int offsetDown) {
        List<BlockPos> result = new ArrayList<>();
        if (mc.world == null) return result;
        double xE = center.x, yE = center.y + 1.0, zE = center.z;
        for (double x = xE - maxDst; x < xE + maxDst; x++) {
            for (double z = zE - maxDst; z < zE + maxDst; z++) {
                for (double y = yE - offsetDown; y < yE; y++) {
                    BlockPos pos = BlockPos.ofFloored(x, y, z);
                    if (canPlace(pos) && !result.contains(pos)) result.add(pos);
                }
            }
        }
        return result;
    }

    private boolean canPlace(BlockPos pos) {
        if (mc.world == null) return false;
        if (!mc.world.isAir(pos)) return false;
        if (mc.world.isAir(pos.down())) return false;
        return mc.world.getOtherEntities(null, new Box(pos)).isEmpty();
    }

    // выбираем случайную точку внутри блока и прищёлкиваем к сетке 1/RES_PX
    private Vec3d findSpawnPoint(BlockPos pos, Vec3d playerPos) {
        double resOff = 1.0 / RES_PX;
        double half   = resOff / 2.0;
        for (int att = 64; att > 0; att--) {
            Vec3d v = new Vec3d(
                    pos.getX() + random.nextDouble(),
                    pos.getY() + random.nextDouble(),
                    pos.getZ() + random.nextDouble()
            );
            double dst = v.distanceTo(playerPos);
            if (dst >= SPAWN_MIN_DST && dst <= SPAWN_MAX_DST) {
                double sx = Math.floor(v.x * RES_PX) / RES_PX + half;
                double sy = Math.floor(v.y)           + half - resOff;
                double sz = Math.floor(v.z * RES_PX) / RES_PX + half;
                return new Vec3d(sx, sy, sz);
            }
        }
        return null;
    }

    // ── рендер-хелперы ───────────────────────────────────────────────────────

    private static final float[] CUBE_FACE_SHADE = {0.5F, 1.0F, 0.7F, 0.95F, 0.65F, 0.85F};
    private static final float[][] CUBE_FILL = {
            {-1, -1, -1}, {1, -1, -1}, {1, -1, 1}, {-1, -1, 1},
            {-1, 1, -1}, {1, 1, -1}, {1, 1, 1}, {-1, 1, 1},
            {-1, -1, -1}, {1, -1, -1}, {1, 1, -1}, {-1, 1, -1},
            {-1, -1, 1}, {1, -1, 1}, {1, 1, 1}, {-1, 1, 1},
            {-1, -1, -1}, {-1, -1, 1}, {-1, 1, 1}, {-1, 1, -1},
            {1, -1, -1}, {1, -1, 1}, {1, 1, 1}, {1, 1, -1}
    };

    private static int shade(int c, float f) {
        int v = (int) (c * f);
        return v > 255 ? 255 : v;
    }

    private static void drawCubeFill(VertexConsumer b, Matrix4f m, float s,
                                     int r, int g, int bl, int a) {
        float h = s / 2f;
        for (int f = 0; f < 6; f++) {
            int cr = shade(r, CUBE_FACE_SHADE[f]);
            int cg = shade(g, CUBE_FACE_SHADE[f]);
            int cb = shade(bl, CUBE_FACE_SHADE[f]);
            float[] q = CUBE_FILL[f];
            for (int k = 0; k < 24; k += 3) {
                b.vertex(m, q[k] * h, q[k + 1] * h, q[k + 2] * h).color(cr, cg, cb, a);
            }
        }
    }

    private static void drawCubeEdges(VertexConsumer b, Matrix4f m, float s,
                                      int r, int g, int bl, int a) {
        float h = s / 2f;
        line(b,m, -h,-h,-h,  h,-h,-h, r,g,bl,a); line(b,m,  h,-h,-h,  h,-h, h, r,g,bl,a);
        line(b,m,  h,-h, h, -h,-h, h, r,g,bl,a); line(b,m, -h,-h, h, -h,-h,-h, r,g,bl,a);
        line(b,m, -h, h,-h,  h, h,-h, r,g,bl,a); line(b,m,  h, h,-h,  h, h, h, r,g,bl,a);
        line(b,m,  h, h, h, -h, h, h, r,g,bl,a); line(b,m, -h, h, h, -h, h,-h, r,g,bl,a);
        line(b,m, -h,-h,-h, -h, h,-h, r,g,bl,a); line(b,m,  h,-h,-h,  h, h,-h, r,g,bl,a);
        line(b,m,  h,-h, h,  h, h, h, r,g,bl,a); line(b,m, -h,-h, h, -h, h, h, r,g,bl,a);
    }

    private static void line(VertexConsumer b, Matrix4f m,
                              float x1, float y1, float z1,
                              float x2, float y2, float z2,
                              int r, int g, int bl, int a) {
        b.vertex(m, x1, y1, z1).color(r, g, bl, a);
        b.vertex(m, x2, y2, z2).color(r, g, bl, a);
    }

    private static void drawGlow(VertexConsumer buf, Matrix4f m,
                                  int r, int g, int b, int alpha, float s) {
        if (alpha <= 0) return;
        buf.vertex(m,-s,-s,0).color(r,g,b,alpha).texture(0,1).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0,0,1);
        buf.vertex(m, s,-s,0).color(r,g,b,alpha).texture(1,1).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0,0,1);
        buf.vertex(m, s, s,0).color(r,g,b,alpha).texture(1,0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0,0,1);
        buf.vertex(m,-s, s,0).color(r,g,b,alpha).texture(0,0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0,0,1);
    }

    private class Circle {

        private final Vec3d vector3d;

        private final long time;
        private final Animation animation = new Animation();
        private final Animation animation2 = new Animation();
        private boolean isBack;

        public Circle(Vec3d vector3d) {
            this.vector3d = vector3d;
            time = System.currentTimeMillis();
            animation.run(1, 0.5F, Easings.SINE_OUT);
            animation2.run(1, 0.5F, Easings.SINE_OUT);
        }

    }

    // ── Cube ─────────────────────────────────────────────────────────────────

    private class Cube {
        final Vec3d  spawnPos;
        final long   spawnTime = System.currentTimeMillis();
        float        cachedAlpha;

        // jump-анимация (порт из DashCubes)
        int    jumpTicks     = 0;
        int    prevJumpTicks = 0;
        int    jumpTicksMax  = 0;
        int    jumpYaw       = 0;
        double jumpHeight    = 0;

        // out-of-range fade
        boolean outOfRange      = false;
        long    outOfRangeStart = 0L;

        final Matrix4f cubeMatrix = new Matrix4f();
        final Matrix4f glowMatrix = new Matrix4f();

        Cube(Vec3d spawnPos) { this.spawnPos = spawnPos; }

        void updateLogic() {
            prevJumpTicks = jumpTicks;
            if (jumpTicks > 0) jumpTicks--;

            if (mc.player != null && mc.player.getEntityPos().distanceTo(spawnPos) > MAX_OWNER_DIST) {
                if (!outOfRange) { outOfRange = true; outOfRangeStart = System.currentTimeMillis(); }
            }
        }

        void tryStartJump() {
            if (jumpTicks <= 0) {
                jumpTicksMax = jumpTicks = (int) (14.0f * (0.5f + 0.5f * random.nextFloat())); // 7-14 тиков
                jumpHeight   = (2 + random.nextInt(11)) / 16.0;      // 2-12 пикселей в блоках
                jumpYaw      = JUMP_YAWS[random.nextInt(JUMP_YAWS.length)];
            }
        }

        float getTimePc() {
            return MathHelper.clamp((float)(System.currentTimeMillis() - spawnTime) / LIFETIME_MS, 0f, 1f);
        }

        float getRangeFade() {
            if (!outOfRange) return 1f;
            return MathHelper.clamp(1f - (float)(System.currentTimeMillis() - outOfRangeStart) / RANGE_FADE_MS, 0f, 1f);
        }

        // fade-in первые 10% жизни, fade-out последние 20%
        float getAlpha() {
            float t = getTimePc();
            float a = t < 0.1f ? t / 0.1f
                    : t > 0.8f ? 1f - (t - 0.8f) / 0.2f
                    : 1f;
            return MathHelper.clamp(a, 0f, 1f) * getRangeFade();
        }

        // треугольная волна: куб взлетает в середине анимации и возвращается
        double getJumpYOffset(float pt) {
            if (jumpTicksMax <= 0) return 0.0;
            float t = MathHelper.lerp(pt, (float) prevJumpTicks, (float) jumpTicks) / (float) jumpTicksMax;
            if (t > 0.5f) t = 1.0f - t;
            return t * 2.0 * jumpHeight;
        }

        boolean isDead() {
            return getTimePc() >= 1f || (outOfRange && getRangeFade() <= 0.01f);
        }
    }

    // ── render pipelines ─────────────────────────────────────────────────────

    private static final Identifier GLOW_TEX_BIG   = Identifier.of("client", "textures/visuals/particles_1.png");
    private static final Identifier GLOW_TEX_SMALL  = Identifier.of("client", "textures/visuals/particles_2.png");

    private static final RenderPipeline FILL_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "jump_cube_fill"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
                    .withCull(false).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .withDepthWrite(false).withBlend(BlendFunction.LIGHTNING).build()
    );
    private static final RenderLayer FILL_LAYER = RenderLayer.of(
            "jump_cube_fill", RenderSetup.builder(FILL_PIPELINE).expectedBufferSize(1 << 13).build()
    );

    private static final RenderPipeline LINE_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "jump_cube_lines"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.DEBUG_LINES)
                    .withCull(false).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .withDepthWrite(false).withBlend(BlendFunction.LIGHTNING).build()
    );
    private static final RenderLayer LINE_LAYER = RenderLayer.of(
            "jump_cube_lines", RenderSetup.builder(LINE_PIPELINE).expectedBufferSize(1 << 12).build()
    );

    private static final RenderPipeline GLOW_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_TEX_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "jump_cube_glow"))
                    .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS)
                    .withCull(false).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .withDepthWrite(false).withBlend(BlendFunction.LIGHTNING).build()
    );
    private static final RenderLayer GLOW_LAYER_BIG = RenderLayer.of(
            "jump_cube_glow_big",
            RenderSetup.builder(GLOW_PIPELINE).texture("Sampler0", GLOW_TEX_BIG).translucent().expectedBufferSize(1 << 12).build()
    );
    private static final RenderLayer GLOW_LAYER_SMALL = RenderLayer.of(
            "jump_cube_glow_small",
            RenderSetup.builder(GLOW_PIPELINE).texture("Sampler0", GLOW_TEX_SMALL).translucent().expectedBufferSize(1 << 12).build()
    );
}

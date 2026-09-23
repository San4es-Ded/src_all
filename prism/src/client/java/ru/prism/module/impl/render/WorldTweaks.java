package ru.prism.module.impl.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.*;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import ru.prism.manager.event_impl.AttackEvent;
import ru.prism.manager.event_impl.EventPacket;
import ru.prism.manager.event_impl.EventRender3D;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.event_impl.FogEvent;
import ru.prism.manager.event_impl.WorldLoadEvent;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.ColorSetting;
import ru.prism.module.api.settings.impl.ModeSetting;
import ru.prism.module.api.settings.impl.MultiBooleanSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.animation.Animation;
import ru.prism.utils.animation.Easings;
import ru.prism.utils.colors.ColorUtil;
import ru.prism.utils.other.Instance;
import ru.prism.utils.render.shader.ScanWorldRenderer;

import java.awt.*;
import java.util.function.Function;

import static net.minecraft.client.gl.RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET;

@ModuleInfo(
        name = "World Tweaks",
        desc = "Подкручивает мир под себя, всякая мелочь для глаз.",
        category = Category.WORLD
)
public class WorldTweaks extends Module {

    public static WorldTweaks get() {
        return Instance.get(WorldTweaks.class);
    }

    public MultiBooleanSetting items = new MultiBooleanSetting(this, "Функции",
            new BooleanSetting("World Cubes", true), new BooleanSetting("Scan World", true));

    public BooleanSetting times = new BooleanSetting(this,"Менять время",true);
    public BooleanSetting fogs = new BooleanSetting(this,"Менять туман",true);
    public SliderSetting time = new SliderSetting(this,"Время", 12,0,24,1).setVisible(() -> times.getValue());
    public SliderSetting fog = new SliderSetting(this,"Дистанция тумана",100, 2,200,1).setVisible(() -> fogs.getValue());
    public ModeSetting typeColor = new ModeSetting(this,"Режим цвета","Тема","Свой");

    public ColorSetting tintColor = new ColorSetting(this, "Цвет", 0xFF00FFFF).setVisible(() -> typeColor.is("Свой"));

    public int getColor() {

        if(typeColor.is("Тема")) {
            return ColorUtil.getClientColor1(1);
        }

        return tintColor.getValue();
    }

    public ModeSetting cubesTypeColor = new ModeSetting(this,"Режим цвета","Тема","Свой")
            .setVisible(() -> items.getValue("World Cubes"));

    public ColorSetting cubesTintColor = new ColorSetting(this, "Цвет", 0xFF00FFFF)
            .setVisible(() -> items.getValue("World Cubes") && cubesTypeColor.is("Свой"));

    public int getCubesColor() {

        if(cubesTypeColor.is("Тема")) {
            return ColorUtil.getClientColor1(1);
        }

        return cubesTintColor.getValue();
    }
    public SliderSetting range       = new SliderSetting(this, "Радиус (ячейки)", 4, 1, 30, 1)
            .setVisible(() -> items.getValue("World Cubes"));
    public SliderSetting vRange      = new SliderSetting(this, "Высота (ячейки)", 2, 1, 20, 1)
            .setVisible(() -> items.getValue("World Cubes"));
    public SliderSetting lineAlphaS  = new SliderSetting(this, "Прозрачность линий", 0.22f, 0.04f, 1.0f, 0.02f)
            .setVisible(() -> items.getValue("World Cubes"));

    public BooleanSetting showGlow   = new BooleanSetting(this, "Свечение в углах", true)
            .setVisible(() -> items.getValue("World Cubes"));
    public SliderSetting  glowSize   = new SliderSetting(this, "Размер свечения", 0.08f, 0.02f, 1.0f, 0.02f)
            .setVisible(() -> items.getValue("World Cubes") && showGlow.getValue());
    public SliderSetting  glowBright = new SliderSetting(this, "Яркость свечения", 1.8f, 0.5f, 4.0f, 0.1f)
            .setVisible(() -> items.getValue("World Cubes") && showGlow.getValue());


    private final Animation shiftAnim = new Animation();
    private int  currOx, currOy, currOz;
    private int  prevOx, prevOy, prevOz;
    private boolean firstFrame = true;

    private static final int CELL = 12;
    private final BufferAllocator allocator = new BufferAllocator(1 << 18);

    public ModeSetting scanTypeColor = new ModeSetting(this, "Режим цвета", "Тема", "Свой")
            .setVisible(() -> items.getValue("Scan World"));
    public ColorSetting scanTintColor = new ColorSetting(this, "Цвет", 0xFF50DCFF)
            .setVisible(() -> items.getValue("Scan World") && scanTypeColor.is("Свой"));

    public BooleanSetting periodic = new BooleanSetting(this, "Периодически", true)
            .setVisible(() -> items.getValue("Scan World"));
    public SliderSetting interval  = new SliderSetting(this, "Интервал", 3.0f, 1.0f, 15.0f, 0.5f)
            .setVisible(() -> items.getValue("Scan World") && periodic.getValue());
    public BooleanSetting onTotem  = new BooleanSetting(this, "При тотеме", true)
            .setVisible(() -> items.getValue("Scan World"));
    public BooleanSetting onKill   = new BooleanSetting(this, "При убийстве", true)
            .setVisible(() -> items.getValue("Scan World"));

    public SliderSetting duration  = new SliderSetting(this, "Длительность", 2.5f, 0.5f, 8.0f, 0.1f)
            .setVisible(() -> items.getValue("Scan World"));
    public SliderSetting width     = new SliderSetting(this, "Ширина", 10.0f, 1.0f, 32.0f, 1.0f)
            .setVisible(() -> items.getValue("Scan World"));
    public SliderSetting maxRadius = new SliderSetting(this, "Радиус", 80.0f, 10.0f, 256.0f, 2.0f)
            .setVisible(() -> items.getValue("Scan World"));

    private static final byte DEATH_STATUS = 3;
    private static final byte TOTEM_STATUS = 35;
    private static final long KILL_WINDOW_MS = 6500L;

    private long lastTriggerTime;
    private int lastTargetId = -1;
    private long lastAttackTime;

    public int getScanColor() {
        if (scanTypeColor.is("Тема")) {
            return ColorUtil.getClientColor1(1);
        }
        return scanTintColor.getValue();
    }

    @Override
    protected void onEnable() {
        if (items.getValue("World Cubes")) {
            firstFrame = true;
        }
        if (items.getValue("Scan World")) {
            lastTargetId = -1;
            if (mc.player != null) {
                lastTriggerTime = System.currentTimeMillis();
                ScanWorldRenderer.getInstance().startScan(mc.player.getEntityPos());
            }
        }
    }

    @Override
    protected void onDisable() {
        if (items.getValue("Scan World")) {
            ScanWorldRenderer.getInstance().stopScan();
        }
    }

    @EventHandler
    public void onFog(FogEvent e) {
        if(fogs.getValue()) {
            e.setDistance(fog.getValue());
            e.setColor(getColor());
            e.cancel();
        }

    }

    @EventHandler
    public void onWorldCubesRender3D(EventRender3D e) {
        if (!items.getValue("World Cubes")) return;
        if (mc.player == null || mc.world == null) return;


        int ox = snapDown(mc.player.getX());
        int oy = snapDown(mc.player.getY());
        int oz = snapDown(mc.player.getZ());

        if (firstFrame) {
            currOx = ox; currOy = oy; currOz = oz;
            prevOx = ox; prevOy = oy; prevOz = oz;
            shiftAnim.set(1.0);
            firstFrame = false;
        } else if (ox != currOx || oy != currOy || oz != currOz) {

            prevOx = currOx; prevOy = currOy; prevOz = currOz;
            currOx = ox;     currOy = oy;     currOz = oz;
            shiftAnim.set(0.0);
            shiftAnim.run(1.0, 0.25, Easings.QUAD_OUT);
        }
        shiftAnim.update();
        float t = shiftAnim.get();

        int col = getCubesColor();
        int cr  = (col >> 16) & 0xFF;
        int cg  = (col >> 8)  & 0xFF;
        int cb  =  col        & 0xFF;

        float baseAlpha = lineAlphaS.getValue() * 255;

        int rI = range.getValue().intValue();
        int vI = vRange.getValue().intValue();

        Vec3d cam   = mc.gameRenderer.getCamera().getCameraPos();
        MatrixStack stack = e.getMatrixStack();
        Matrix4f mat = stack.peek().getPositionMatrix();

        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(allocator);
        VertexConsumer lb = immediate.getBuffer(LINE_LAYER);


        if (t < 0.999f) {
            int oldA = (int)(baseAlpha * (1f - t));
            drawLines(lb, mat, cam, prevOx, prevOy, prevOz, cr, cg, cb, oldA, rI, vI);
        }

        int newA = (int)(baseAlpha * t);
        drawLines(lb, mat, cam, currOx, currOy, currOz, cr, cg, cb, newA, rI, vI);


        if (showGlow.getValue()) {
            float gs    = glowSize.getValue();
            float brite = glowBright.getValue();

            int newGlowA = Math.min(255, (int)(15 * t * brite));
            drawGlows(immediate, ROMB_ESP.apply(Particles.ParticleType.BLOOM.texture()), mat, cam, currOx, currOy, currOz,
                    cr, cg, cb, newGlowA, gs * 2, rI, vI);

             newGlowA = Math.min(255, (int)(255 * t * brite));
            drawGlows(immediate, ROMB_ESP.apply(Particles.ParticleType.CIRCLE.texture()), mat, cam, currOx, currOy, currOz,
                      cr, cg, cb, newGlowA, gs, rI, vI);
        }

        immediate.draw();
    }


    private void drawLines(VertexConsumer lb, Matrix4f mat, Vec3d cam,
                           int ox, int oy, int oz,
                           int cr, int cg, int cb, int alpha,
                           int rI, int vI) {
        if (alpha <= 0) return;

        int xMin = ox - rI * CELL,  xMax = ox + rI * CELL;
        int zMin = oz - rI * CELL,  zMax = oz + rI * CELL;
        int yMin = oy - (vI - 1) * CELL;
        int yMax = oy +  vI      * CELL;


        for (int x = xMin; x <= xMax; x += CELL) {
            for (int z = zMin; z <= zMax; z += CELL) {
                float rx = (float)(x - cam.x), rz = (float)(z - cam.z);
                lb.vertex(mat, rx, (float)(yMin - cam.y), rz).color(cr, cg, cb, alpha);
                lb.vertex(mat, rx, (float)(yMax - cam.y), rz).color(cr, cg, cb, alpha);
            }
        }

        for (int y = yMin; y <= yMax; y += CELL) {
            float ry = (float)(y - cam.y);
            for (int z = zMin; z <= zMax; z += CELL) {
                float rz = (float)(z - cam.z);
                lb.vertex(mat, (float)(xMin - cam.x), ry, rz).color(cr, cg, cb, alpha);
                lb.vertex(mat, (float)(xMax - cam.x), ry, rz).color(cr, cg, cb, alpha);
            }
        }
        for (int y = yMin; y <= yMax; y += CELL) {
            float ry = (float)(y - cam.y);
            for (int x = xMin; x <= xMax; x += CELL) {
                float rx = (float)(x - cam.x);
                lb.vertex(mat, rx, ry, (float)(zMin - cam.z)).color(cr, cg, cb, alpha);
                lb.vertex(mat, rx, ry, (float)(zMax - cam.z)).color(cr, cg, cb, alpha);
            }
        }
    }

    private void drawGlows(VertexConsumerProvider.Immediate immediate, RenderLayer glowLayer,
                           Matrix4f mat, Vec3d cam,
                           int ox, int oy, int oz,
                           int cr, int cg, int cb, int alpha,
                           float gs, int rI, int vI) {
        if (alpha <= 0) return;

        int xMin = ox - rI * CELL, xMax = ox + rI * CELL;
        int zMin = oz - rI * CELL, zMax = oz + rI * CELL;
        int yMin = oy - (vI - 1) * CELL;
        int yMax = oy +  vI      * CELL;

        Camera camera = mc.gameRenderer.getCamera();

        // биллборд одинаково повёрнут к камере во всех ячейках —
        // углы квада считаем один раз, дальше только сложение с позицией ячейки
        Quaternionf q = camera.getRotation();
        Vector3f c0 = new Vector3f(-gs, -gs, 0).rotate(q);
        Vector3f c1 = new Vector3f( gs, -gs, 0).rotate(q);
        Vector3f c2 = new Vector3f( gs,  gs, 0).rotate(q);
        Vector3f c3 = new Vector3f(-gs,  gs, 0).rotate(q);

        // направление взгляда — ячейки за спиной в буфер не пишем
        float yawRad   = (float) Math.toRadians(camera.getYaw());
        float pitchRad = (float) Math.toRadians(camera.getPitch());
        float cosPitch = (float) Math.cos(pitchRad);
        float lx = (float) (-Math.sin(yawRad) * cosPitch);
        float ly = (float) -Math.sin(pitchRad);
        float lz = (float) (Math.cos(yawRad) * cosPitch);

        VertexConsumer gb = immediate.getBuffer(glowLayer);

        for (int x = xMin; x <= xMax; x += CELL) {
            for (int y = yMin; y <= yMax; y += CELL) {
                for (int z = zMin; z <= zMax; z += CELL) {
                    float rx = (float) (x - cam.x);
                    float ry = (float) (y - cam.y);
                    float rz = (float) (z - cam.z);

                    if (rx * lx + ry * ly + rz * lz < -gs * 2) continue;

                    gb.vertex(mat, rx + c0.x, ry + c0.y, rz + c0.z).color(cr, cg, cb, alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
                    gb.vertex(mat, rx + c1.x, ry + c1.y, rz + c1.z).color(cr, cg, cb, alpha).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
                    gb.vertex(mat, rx + c2.x, ry + c2.y, rz + c2.z).color(cr, cg, cb, alpha).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
                    gb.vertex(mat, rx + c3.x, ry + c3.y, rz + c3.z).color(cr, cg, cb, alpha).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
                }
            }
        }
    }


    public static final RenderPipeline ROMB_ESP_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(TRANSFORMS_AND_PROJECTION_SNIPPET)
                    .withLocation("pipeline/wtex")
                    .withVertexShader("core/position_tex_color")
                    .withFragmentShader("core/position_tex_color")
                    .withSampler("Sampler0")
                    .withBlend(BlendFunction.LIGHTNING)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withCull(false)
                    .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS)
                    .build()
    );

    public static final Function<Identifier, RenderLayer> ROMB_ESP =
            Util.memoize(texture -> {
                RenderSetup setup = RenderSetup.builder(ROMB_ESP_PIPELINE)
                        .texture("Sampler0", texture)
                        .translucent()
                        .expectedBufferSize(1536)
                        .build();
                return RenderLayer.of("wtex", setup);
            });

    private static int snapDown(double v) {
        return (int) Math.floor(v / CELL) * CELL;
    }

    private static final RenderPipeline LINE_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "world_cubes_grid"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.DEBUG_LINES)
                    .withCull(false)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withBlend(BlendFunction.LIGHTNING)
                    .build()
    );

    private static final RenderLayer LINE_LAYER = RenderLayer.of(
            "world_cubes_lines",
            RenderSetup.builder(LINE_PIPELINE).expectedBufferSize(1 << 14).build()
    );

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        if (!items.getValue("Scan World")) return;
        lastTargetId = -1;
        ScanWorldRenderer.getInstance().stopScan();
    }

    @EventHandler
    public void onTick(EventTick e) {
        if (!items.getValue("Scan World")) return;
        if (mc.player == null || mc.world == null) return;
        if (!periodic.getValue()) return;

        long now = System.currentTimeMillis();
        long intervalMs = (long) (interval.getValue() * 1000.0f);

        if (now - lastTriggerTime >= intervalMs) {
            Vec3d center = mc.player.getEntityPos();
            ScanWorldRenderer.getInstance().startScan(center);
            lastTriggerTime = now;
        }
    }

    @EventHandler
    public void onAttack(AttackEvent e) {
        if (!items.getValue("Scan World")) return;
        if (!(e.getTarget() instanceof LivingEntity living) || living == mc.player) return;
        lastTargetId = living.getId();
        lastAttackTime = System.currentTimeMillis();
    }

    @EventHandler
    public void onPacket(EventPacket e) {
        if (!items.getValue("Scan World")) return;
        if (!(e.getPacket() instanceof EntityStatusS2CPacket packet)) return;
        if (mc.world == null) return;

        if (packet.getStatus() == TOTEM_STATUS && onTotem.getValue()) {
            Entity entity = packet.getEntity(mc.world);
            if (entity == null) return;
            ScanWorldRenderer.getInstance().startScan(entity.getEntityPos());
        } else if (packet.getStatus() == DEATH_STATUS && onKill.getValue()) {
            Entity entity = packet.getEntity(mc.world);
            if (!(entity instanceof LivingEntity living)) return;
            // волна только на нашем килле: цель недавно атакована нами
            if (living.getId() != lastTargetId) return;
            if (System.currentTimeMillis() - lastAttackTime > KILL_WINDOW_MS) return;
            lastTargetId = -1;
            ScanWorldRenderer.getInstance().startScan(living.getEntityPos());
        }
    }

    @EventHandler
    public void onScanWorldRender3D(EventRender3D e) {
        if (!items.getValue("Scan World")) return;
        if (mc.player == null || mc.world == null) return;

        int col = getScanColor();
        int r = (col >> 16) & 0xFF;
        int g = (col >> 8) & 0xFF;
        int b = col & 0xFF;

        ScanWorldRenderer renderer = ScanWorldRenderer.getInstance();
        renderer.setDuration(duration.getValue());
        renderer.setWidth(width.getValue());
        renderer.setMaxRadius(maxRadius.getValue());
        renderer.setOuterColor(new Color(r, g, b, 90));
        renderer.setMidColor(new Color(r, g, b, 90));
        renderer.setInnerColor(new Color(r, g, b, 180));
        renderer.setScanlineColor(new Color(r, g, b, 90));

        renderer.render(
                e.getMatrixStack().peek().getPositionMatrix(),
                mc.gameRenderer.getBasicProjectionMatrix(mc.options.getFov().getValue().floatValue())
        );
    }
}

package ru.prism.module.impl.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Box;
import org.joml.Quaternionf;
import ru.prism.manager.event_impl.*;
import ru.prism.manager.event_impl.MotionEvent;
import ru.prism.manager.event_impl.WorldLoadEvent;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.ColorSetting;
import ru.prism.module.api.settings.impl.ModeSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.animation.Animation;
import ru.prism.utils.animation.Easings;
import ru.prism.utils.colors.ColorUtil;
import ru.prism.utils.math.MathUtil;
import ru.prism.utils.taskript.StopWatch;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.*;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

@ModuleInfo(
        name = "Lumen",
        desc = "Кружит вокруг тебя светящуюся живность — светлячки или кубы, выбирай.",
        category = Category.WORLD
)
public class Lumen extends Module {

    public ModeSetting mode = new ModeSetting(this, "Режим", "Светлячки", "Кубы");

    private static final long MAX_PART_ALIVE_TIME = 5500L;

    private static final RenderPipeline FLY_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET)
                    .withLocation(Identifier.of("mytheria", "pipeline/fireflies"))
                    .withVertexShader("core/position_tex_color")
                    .withFragmentShader("core/position_tex_color")
                    .withSampler("Sampler0")
                    .withBlend(BlendFunction.LIGHTNING)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withCull(false)
                    .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS)
                    .build()
    );

    private static final RenderPipeline TRAIL_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET)
                    .withLocation(Identifier.of("mytheria", "pipeline/fireflies_trail"))
                    .withVertexShader("core/position_color")
                    .withFragmentShader("core/position_color")
                    .withBlend(BlendFunction.LIGHTNING)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withCull(false)
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
                    .build()
    );

    private static final RenderPipeline SPARK_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET)
                    .withLocation(Identifier.of("mytheria", "pipeline/fireflies_spark"))
                    .withVertexShader("core/position_color")
                    .withFragmentShader("core/position_color")
                    .withBlend(BlendFunction.LIGHTNING)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withCull(false)
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
                    .build()
    );

    private static final Identifier FIRE_PART_TEX = Identifier.of("client", "textures/particles/circle.png");
    private static final Identifier BLOOM_TEX = Identifier.of("client", "textures/particles/glow.png");

    private static final RenderLayer FLY_LAYER = RenderLayer.of(
            "mytheria_fireflies",
            RenderSetup.builder(FLY_PIPELINE)
                    .texture("Sampler0", FIRE_PART_TEX)
                    .translucent()
                    .expectedBufferSize(8192)
                    .build()
    );

    private static final RenderLayer BLOOM_LAYER = RenderLayer.of(
            "mytheria_fireflies_bloom",
            RenderSetup.builder(FLY_PIPELINE)
                    .texture("Sampler0", BLOOM_TEX)
                    .translucent()
                    .expectedBufferSize(8192)
                    .build()
    );

    private static final RenderLayer TRAIL_LAYER = RenderLayer.of(
            "mytheria_fireflies_trail",
            RenderSetup.builder(TRAIL_PIPELINE)
                    .translucent()
                    .expectedBufferSize(16384)
                    .build()
    );

    private static final RenderLayer SPARK_LAYER = RenderLayer.of(
            "mytheria_fireflies_spark",
            RenderSetup.builder(SPARK_PIPELINE)
                    .translucent()
                    .expectedBufferSize(16384)
                    .build()
    );

    private static final Identifier SPARK_GLOW_TEX =
            Identifier.of("client", "textures/particles/glow.png");

    private static final RenderLayer SPARK_GLOW_LAYER = RenderLayer.of(
            "mytheria_fireflies_spark_glow",
            RenderSetup.builder(FLY_PIPELINE)
                    .texture("Sampler0", SPARK_GLOW_TEX)
                    .translucent()
                    .expectedBufferSize(8192)
                    .build()
    );

    public ModeSetting colorMode = new ModeSetting(this,"Цвет", "Европа", "Свой", "Тема")
            .setVisible(() -> mode.is("Светлячки"));
    public ColorSetting pickColor = new ColorSetting(this,"Свой цвет", ColorUtil.getColor(255, 70, 0, 255))
            .setVisible(() -> mode.is("Светлячки") && this.colorMode.is("Свой"));
    public BooleanSetting darkImprint = new BooleanSetting(this,"Тёмный след", false)
            .setVisible(() -> mode.is("Светлячки"));
    public BooleanSetting lighting = new BooleanSetting(this,"Свечение", false)
            .setVisible(() -> mode.is("Светлячки"));
    public SliderSetting spawnDelay = new SliderSetting(this,"Задержка спавна", 3.0F, 1.0F, 10.0F, 1.0F)
            .setVisible(() -> mode.is("Светлячки"));

    private final List<FirePart> fireParts = new ArrayList<>();

    private static final Identifier GLOW_TEXTURE_BIG = Identifier.of("client", "textures/particles/glow.png");
    private static final Identifier GLOW_TEXTURE_SMALL = Identifier.of("client", "textures/visuals/particles_2.png");

    private static final RenderPipeline FILL_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "svetoch_fill"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
                    .withCull(false)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withBlend(BlendFunction.LIGHTNING)
                    .build()
    );

    private static final RenderLayer FILL_LAYER = RenderLayer.of(
            "svetoch_fill",
            RenderSetup.builder(FILL_PIPELINE).expectedBufferSize(1 << 13).build()
    );

    private static final RenderPipeline LINE_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "svetoch_lines"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.DEBUG_LINES)
                    .withCull(false)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withBlend(BlendFunction.LIGHTNING)
                    .build()
    );

    private static final RenderLayer LINE_LAYER = RenderLayer.of(
            "svetoch_lines",
            RenderSetup.builder(LINE_PIPELINE).expectedBufferSize(1 << 13).build()
    );

    private static final RenderPipeline GLOW_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_TEX_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "svetoch_glow"))
                    .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS)
                    .withCull(false)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withBlend(BlendFunction.LIGHTNING)
                    .build()
    );

    private static final RenderLayer GLOW_LAYER_BIG = RenderLayer.of(
            "svetoch_glow_big",
            RenderSetup.builder(GLOW_PIPELINE)
                    .texture("Sampler0", GLOW_TEXTURE_BIG)
                    .translucent()
                    .expectedBufferSize(1 << 12)
                    .build()
    );

    private static final RenderLayer GLOW_LAYER_SMALL = RenderLayer.of(
            "svetoch_glow_small",
            RenderSetup.builder(GLOW_PIPELINE)
                    .texture("Sampler0", GLOW_TEXTURE_SMALL)
                    .translucent()
                    .expectedBufferSize(1 << 12)
                    .build()
    );

    public ModeSetting typeColor = new ModeSetting(this, "Режим цвета", "Тема", "Свой")
            .setVisible(() -> mode.is("Кубы"));
    public ColorSetting tintColor = new ColorSetting(this, "Цвет", 0xFF00FFFF)
            .setVisible(() -> mode.is("Кубы") && typeColor.is("Свой"));

    public SliderSetting count = new SliderSetting(this, "Кол кубиков", 100, 50, 300, 1)
            .setVisible(() -> mode.is("Кубы"));
    public SliderSetting cubeSize = new SliderSetting(this, "Размер куба", 0.26F, 0.1F, 0.6F, 0.02F)
            .setVisible(() -> mode.is("Кубы"));

    public int getColor() {
        if (typeColor.is("Тема")) {
            return ColorUtil.getClientColor1(1);
        }
        return tintColor.getValue();
    }

    // тайминги как у мировых партиклов: гаснуть с 1.5с, полное угасание ~3.5с
    private static final long LIFETIME = 1500L;
    private static final double CULL_DIST_SQ = 96 * 96;

    // куб: 8 углов как знаки полуразмера, грани и рёбра — статические таблицы индексов
    private static final float[][] CORNERS = {
            {-1, -1, -1}, {1, -1, -1}, {1, -1, 1}, {-1, -1, 1},
            {-1, 1, -1}, {1, 1, -1}, {1, 1, 1}, {-1, 1, 1}
    };
    private static final int[][] FACES = {
            {0, 1, 2, 3}, {4, 5, 6, 7},
            {0, 1, 5, 4}, {2, 3, 7, 6},
            {1, 2, 6, 5}, {3, 0, 4, 7}
    };
    private static final int[][] EDGES = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0},
            {4, 5}, {5, 6}, {6, 7}, {7, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7}
    };

    private final List<Particle> particles = new ArrayList<>();
    private final List<Particle> visible = new ArrayList<>();
    private final BufferAllocator allocator = new BufferAllocator(1 << 16);
    private long lastFrameTime = System.nanoTime();

    @Override
    public void toggle() {
        super.toggle();
        particles.clear();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        fireParts.clear();
        particles.clear();
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        fireParts.clear();
        particles.clear();
    }

    @EventHandler
    public void onTick(EventTick e) {
        if (mode.is("Светлячки")) fireFliesTick();
    }

    @EventHandler
    public void onUpdate(MotionEvent e) {
        if (mode.is("Кубы")) svetochUpdate();
    }

    @EventHandler
    public void onRender(EventRender3D e) {
        if (mode.is("Светлячки")) {
            fireFliesRender(e);
        } else if (mode.is("Кубы")) {
            svetochRender(e);
        }
    }

    private void fireFliesTick() {
        if (mc.player == null || mc.world == null) return;

        if (mc.player.age == 1) {
            fireParts.forEach(FirePart::setToRemove);
        }

        fireParts.forEach(FirePart::updatePart);
        fireParts.removeIf(FirePart::isToRemove);

        if (mc.player.age % ((int) spawnDelay.getValue().intValue() + 1) == 0) {
            int color = getPartColor();
            fireParts.add(new FirePart(generateVecForPart(10.0, 4.0), MAX_PART_ALIVE_TIME, color));
            fireParts.add(new FirePart(generateVecForPart(6.0, 5.0), MAX_PART_ALIVE_TIME, color));
        }
    }

    private int getPartColor() {
        if (this.colorMode.is("Европа")) {
            return Color.getHSBColor((float) Math.random(), 1.0F, 1.0F).getRGB();
        }
        if (this.colorMode.is("Тема")) {
            return ColorUtil.client();
        }
        return this.pickColor.getValue();
    }

    private static float getRandom(double min, double max) {
        return (float) ThreadLocalRandom.current().nextDouble(min, Math.max(max, min + 1.0E-6));
    }

    private Vec3d generateVecForPart(double rangeXZ, double rangeY) {
        Vec3d pos = randomAround(rangeXZ, rangeY);
        for (int i = 0; i < 30; i++) {
            double dx = pos.x - mc.player.getX();
            double dz = pos.z - mc.player.getZ();
            if (posBlock(pos.x, pos.y, pos.z) || Math.sqrt(dx * dx + dz * dz) < rangeXZ / 3.0) {
                pos = randomAround(rangeXZ, rangeY);
            }
        }
        return pos;
    }

    private Vec3d randomAround(double rangeXZ, double rangeY) {
        return mc.player.getEntityPos().add(
                getRandom(-rangeXZ, rangeXZ),
                getRandom(-rangeY / 3.0, rangeY),
                getRandom(-rangeXZ, rangeXZ)
        );
    }

    private boolean posBlock(double x, double y, double z) {
        BlockState state = mc.world.getBlockState(BlockPos.ofFloored(x, y, z));
        return !state.isAir() && state.getFluidState().isEmpty() && !state.isReplaceable();
    }

    private void fireFliesRender(EventRender3D e) {
        if (mc.player == null || mc.world == null || fireParts.isEmpty()) return;

        float pTicks = e.getTickDelta();
        Vec3d cameraPos = mc.gameRenderer.getCamera().getCameraPos();
        Quaternionf cameraRotation = mc.gameRenderer.getCamera().getRotation();
        Vector3f right = cameraRotation.transform(new Vector3f(1, 0, 0));
        Vector3f up = cameraRotation.transform(new Vector3f(0, 1, 0));
        Vector3f forward = cameraRotation.transform(new Vector3f(0, 0, -1));

        MatrixStack matrices = e.getMatrixStack();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        VertexConsumerProvider.Immediate consumers = this.mc.getBufferBuilders().getEntityVertexConsumers();

        // trails (translucent)
        VertexConsumer trailConsumer = consumers.getBuffer(TRAIL_LAYER);
        for (FirePart part : fireParts) {
            if (isBehindCamera(part, cameraPos, forward)) continue;
            drawTrail(trailConsumer, matrix, part, cameraPos);
        }
        consumers.draw(TRAIL_LAYER);


        // sparks (additive)
        if (lighting.getValue()) {

            VertexConsumer glowConsumer = consumers.getBuffer(SPARK_GLOW_LAYER);
            for (FirePart part : fireParts) {
                if (isBehindCamera(part, cameraPos, forward)) continue;
                drawSparkGlow(glowConsumer, matrix, part, cameraPos, right, up, pTicks);
            }
            consumers.draw(SPARK_GLOW_LAYER);

        }
        VertexConsumer sparkConsumer = consumers.getBuffer(SPARK_LAYER);
        for (FirePart part : fireParts) {
            if (isBehindCamera(part, cameraPos, forward)) continue;
            drawSparks(sparkConsumer, matrix, part, cameraPos, right, up, pTicks);
        }
        consumers.draw(SPARK_LAYER);


        // firefly bodies (additive, textured)
        VertexConsumer flyConsumer = consumers.getBuffer(FLY_LAYER);
        for (FirePart part : fireParts) {
            if (isBehindCamera(part, cameraPos, forward)) continue;
            drawBody(flyConsumer, matrix, part, cameraPos, right, up, pTicks, false);
        }
        consumers.draw(FLY_LAYER);

        if (lighting.getValue()) {
            VertexConsumer bloomConsumer = consumers.getBuffer(BLOOM_LAYER);
            for (FirePart part : fireParts) {
                if (isBehindCamera(part, cameraPos, forward)) continue;
                drawBody(bloomConsumer, matrix, part, cameraPos, right, up, pTicks, true);
            }
            consumers.draw(BLOOM_LAYER);
        }
    }

    private boolean isBehindCamera(FirePart part, Vec3d cameraPos, Vector3f forward) {
        float px = (float) (part.pos.x - cameraPos.x);
        float py = (float) (part.pos.y - cameraPos.y);
        float pz = (float) (part.pos.z - cameraPos.z);
        return px * forward.x + py * forward.y + pz * forward.z < -2.0f;
    }

    private void drawSparkGlow(VertexConsumer consumer,
                               Matrix4f matrix,
                               FirePart part,
                               Vec3d cameraPos,
                               Vector3f right,
                               Vector3f up,
                               float pTicks) {

        if (part.sparkParts.isEmpty()) return;

        double dist = mc.player.squaredDistanceTo(part.pos.x, part.pos.y + 1.6, part.pos.z);

        float size = 0.035f + 0.02f *
                MathHelper.clamp(
                        1.0f - ((float) Math.sqrt(dist) - 3.0f) / 10.0f,
                        0.0f,
                        1.0f);

        for (SparkPart spark : part.sparkParts) {

            float life = (float) spark.timePC();

            int color = setAlphaF(
                    part.color,
                    alphaOf(part.color) * (1.0f - life) * 0.3f
            );

            Vec3d pos = new Vec3d(
                    spark.getRenderPosX(pTicks),
                    spark.getRenderPosY(pTicks),
                    spark.getRenderPosZ(pTicks)
            );

            quad(
                    consumer,
                    matrix,
                    pos,
                    cameraPos,
                    right,
                    up,
                    size * (1.4f - life),
                    color
            );
        }
    }
    private void drawBody(VertexConsumer consumer, Matrix4f matrix, FirePart part,
                          Vec3d cameraPos, Vector3f right, Vector3f up, float pTicks, boolean bloomPass) {
        float alphaPC = part.getAlphaPC();
        if (alphaPC <= 0.004f) return;

        int color = setAlphaF(part.color, alphaOf(part.color) * alphaPC);
        // blend toward white by brightness, like the original core highlight
        float brightness = brightnessOf(color);
        int bodyColor = lerpToWhiteKeepAlpha(color, alphaOf(color) / 255f * brightness * 0.3f);

        Vec3d pos = part.getRenderPosVec(pTicks);
        float half = 0.04f * alphaPC; // original: 5 * alphaPC scaled by 0.1, halved

        if (bloomPass) {

            bodyColor = setAlphaF(bodyColor, alphaOf(bodyColor) * 0.3f);
            half *= 5.0f;
        }

        quad(consumer, matrix, pos, cameraPos, right, up, half, bodyColor);
    }

    private void drawTrail(VertexConsumer consumer, Matrix4f matrix, FirePart part, Vec3d cameraPos) {
        if (part.trailParts.size() < 2) return;

        int baseColor = setAlphaF(part.color, alphaOf(part.color) * part.getAlphaPC());
        if (darkImprint.getValue()) {
            baseColor = darken(baseColor, 0.3f);
        }

        double distToPart = mc.player.squaredDistanceTo(part.pos.x, part.pos.y + 1.6, part.pos.z);
        float widthFactor = MathHelper.clamp(1.0f - ((float) Math.sqrt(distToPart) - 3.0f) / 20.0f, 0.0f, 1.0f);
        float halfWidth = 0.012f * widthFactor + 1.0E-4f;

        int pointsCount = part.trailParts.size();
        for (int i = 0; i < pointsCount - 1; i++) {
            TrailPart a = part.trailParts.get(i);
            TrailPart b = part.trailParts.get(i + 1);

            float sizePC = (float) i / (float) pointsCount;
            float sizePC2 = (float) (i + 1) / (float) pointsCount;
            int c1 = trailColor(baseColor, part.getAlphaPC(), sizePC);
            int c2 = trailColor(baseColor, part.getAlphaPC(), sizePC2);

            emitRibbonSegment(consumer, matrix, a, b, cameraPos, halfWidth, c1, c2);
        }
    }

    private int trailColor(int baseColor, float alphaPC, float sizePC) {
        float wave = easeInOutQuadWave(sizePC);
        int c = setAlphaF(baseColor, alphaOf(baseColor) * alphaPC * wave);
        return lerpToWhiteKeepAlpha(c, alphaOf(c) / 255f * brightnessOf(c) * 0.25f * sizePC);
    }

    private void emitRibbonSegment(VertexConsumer consumer, Matrix4f matrix, TrailPart a, TrailPart b,
                                   Vec3d cameraPos, float halfWidth, int c1, int c2) {
        float ax = (float) (a.x - cameraPos.x), ay = (float) (a.y - cameraPos.y), az = (float) (a.z - cameraPos.z);
        float bx = (float) (b.x - cameraPos.x), by = (float) (b.y - cameraPos.y), bz = (float) (b.z - cameraPos.z);

        // perpendicular in view space: segment dir x direction-to-camera
        float dx = bx - ax, dy = by - ay, dz = bz - az;
        float cx = ay * dz - az * dy;
        float cy = az * dx - ax * dz;
        float cz = ax * dy - ay * dx;
        float len = (float) Math.sqrt(cx * cx + cy * cy + cz * cz);
        if (len < 1.0E-6f) return;
        cx = cx / len * halfWidth;
        cy = cy / len * halfWidth;
        cz = cz / len * halfWidth;

        vertex(consumer, matrix, ax - cx, ay - cy, az - cz, c1);
        vertex(consumer, matrix, ax + cx, ay + cy, az + cz, c1);
        vertex(consumer, matrix, bx + cx, by + cy, bz + cz, c2);
        vertex(consumer, matrix, bx - cx, by - cy, bz - cz, c2);
    }

    private void drawSparks(VertexConsumer consumer, Matrix4f matrix, FirePart part,
                            Vec3d cameraPos, Vector3f right, Vector3f up, float pTicks) {
        if (part.sparkParts.size() < 2) return;

        double distToPart = mc.player.squaredDistanceTo(part.pos.x, part.pos.y + 1.6, part.pos.z);
        float pointsScale = 0.25f + 5.0f * MathHelper.clamp(1.0f - ((float) Math.sqrt(distToPart) - 3.0f) / 10.0f, 0.0f, 1.0f);
        float half = pointsScale * 0.0012f;

        float alphaPC = part.getAlphaPC();
        for (SparkPart spark : part.sparkParts) {
            float timePC = (float) spark.timePC();
            int c = lerpKeepNone(0xFFFFFFFF, part.color, timePC);
            c = setAlphaF(c, alphaOf(part.color) * alphaPC * (1.0f - timePC * timePC * timePC));

            Vec3d pos = new Vec3d(
                    spark.getRenderPosX(pTicks),
                    spark.getRenderPosY(pTicks),
                    spark.getRenderPosZ(pTicks)
            );
            quadNoTex(consumer, matrix, pos, cameraPos, right, up, half, c);
        }
    }

    private void quadNoTex(VertexConsumer consumer, Matrix4f matrix, Vec3d pos, Vec3d cameraPos,
                           Vector3f right, Vector3f up, float half, int color) {
        float px = (float) (pos.x - cameraPos.x);
        float py = (float) (pos.y - cameraPos.y);
        float pz = (float) (pos.z - cameraPos.z);
        float rx = right.x * half, ry = right.y * half, rz = right.z * half;
        float ux = up.x * half, uy = up.y * half, uz = up.z * half;

        vertex(consumer, matrix, px - rx - ux, py - ry - uy, pz - rz - uz, color);
        vertex(consumer, matrix, px - rx + ux, py - ry + uy, pz - rz + uz, color);
        vertex(consumer, matrix, px + rx + ux, py + ry + uy, pz + rz + uz, color);
        vertex(consumer, matrix, px + rx - ux, py + ry - uy, pz + rz - uz, color);
    }

    private void quad(VertexConsumer consumer, Matrix4f matrix, Vec3d pos, Vec3d cameraPos,
                      Vector3f right, Vector3f up, float half, int color) {
        float px = (float) (pos.x - cameraPos.x);
        float py = (float) (pos.y - cameraPos.y);
        float pz = (float) (pos.z - cameraPos.z);
        float rx = right.x * half, ry = right.y * half, rz = right.z * half;
        float ux = up.x * half, uy = up.y * half, uz = up.z * half;

        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        float a = ((color >>> 24) & 0xFF) / 255f;

        consumer.vertex(matrix, px - rx - ux, py - ry - uy, pz - rz - uz).texture(0, 0).color(r, g, b, a);
        consumer.vertex(matrix, px - rx + ux, py - ry + uy, pz - rz + uz).texture(0, 1).color(r, g, b, a);
        consumer.vertex(matrix, px + rx + ux, py + ry + uy, pz + rz + uz).texture(1, 1).color(r, g, b, a);
        consumer.vertex(matrix, px + rx - ux, py + ry - uy, pz + rz - uz).texture(1, 0).color(r, g, b, a);
    }

    private void vertex(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z, int color) {
        consumer.vertex(matrix, x, y, z).color(
                ((color >> 16) & 0xFF) / 255f,
                ((color >> 8) & 0xFF) / 255f,
                (color & 0xFF) / 255f,
                ((color >>> 24) & 0xFF) / 255f
        );
    }

    // ---------- color helpers (ported semantics) ----------

    private static float alphaOf(int color) {
        return (color >>> 24) & 0xFF;
    }

    private static int setAlphaF(int color, float alpha) {
        int a = MathHelper.clamp(Math.round(alpha), 0, 255);
        return (color & 0x00FFFFFF) | (a << 24);
    }

    private static float brightnessOf(int color) {
        float[] hsb = Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, null);
        return hsb[2];
    }

    private static int darken(int color, float factor) {
        int r = (int) (((color >> 16) & 0xFF) * factor);
        int g = (int) (((color >> 8) & 0xFF) * factor);
        int b = (int) ((color & 0xFF) * factor);
        return (color & 0xFF000000) | (r << 16) | (g << 8) | b;
    }

    private static int lerpKeepNone(int from, int to, float t) {
        t = MathHelper.clamp(t, 0.0f, 1.0f);
        int r = (int) MathHelper.lerp(t, (from >> 16) & 0xFF, (to >> 16) & 0xFF);
        int g = (int) MathHelper.lerp(t, (from >> 8) & 0xFF, (to >> 8) & 0xFF);
        int b = (int) MathHelper.lerp(t, from & 0xFF, to & 0xFF);
        int a = (int) MathHelper.lerp(t, (from >>> 24) & 0xFF, (to >>> 24) & 0xFF);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int lerpToWhiteKeepAlpha(int color, float t) {
        int a = (color >>> 24) & 0xFF;
        int mixed = lerpKeepNone(color, 0xFFFFFFFF, MathHelper.clamp(t, 0.0f, 1.0f));
        return (mixed & 0x00FFFFFF) | (a << 24);
    }

    private static float easeInOutQuadWave(float t) {
        float v = 2.0f * t - 1.0f;
        return 1.0f - v * v;
    }

    // ---------- particles ----------

    private class FirePart {
        final List<TrailPart> trailParts = new ArrayList<>();
        final List<SparkPart> sparkParts = new ArrayList<>();
        Vec3d prevPos;
        Vec3d pos;
        float alphaAnim = 0.0f;
        float alphaTarget = 1.0f;
        int msChangeSideRate = getMsChangeSideRate();
        final int color;
        float moveYawSet = getRandom(0.0, 360.0);
        float speed = getRandom(0.08, 0.175);
        float yMotion = getRandom(-0.075, 0.1);
        float moveYaw;
        final float maxAlive;
        final long startTime;
        long rateTimer;
        boolean toRemove;

        FirePart(Vec3d pos, float maxAlive, int color) {
            this.moveYaw = this.moveYawSet;
            this.startTime = System.currentTimeMillis();
            this.rateTimer = this.startTime;
            this.color = color;
            this.pos = pos;
            this.prevPos = pos;
            this.maxAlive = maxAlive;
        }

        float getTimePC() {
            return MathHelper.clamp((System.currentTimeMillis() - this.startTime) / this.maxAlive, 0.0f, 1.0f);
        }

        float getAlphaPC() {
            return this.alphaAnim;
        }

        Vec3d getRenderPosVec(float pTicks) {
            return this.pos.add(
                    -(this.prevPos.x - this.pos.x) * pTicks,
                    -(this.prevPos.y - this.pos.y) * pTicks,
                    -(this.prevPos.z - this.pos.z) * pTicks
            );
        }

        void updatePart() {
            // alpha animation: step toward target like the original AnimationUtils(0, 1, 0.02)
            this.alphaAnim += MathHelper.clamp(this.alphaTarget - this.alphaAnim, -0.02f, 0.02f) * 2.0f;
            this.alphaAnim = MathHelper.clamp(this.alphaAnim, 0.0f, 1.0f);

            if (System.currentTimeMillis() - this.rateTimer >= this.msChangeSideRate) {
                this.msChangeSideRate = getMsChangeSideRate();
                this.rateTimer = System.currentTimeMillis();
                this.moveYawSet = getRandom(0.0, 360.0);
            }

            this.moveYaw = MathHelper.lerp(0.065f, this.moveYaw, this.moveYawSet);
            this.speed /= 1.005f;
            float motionX = -MathHelper.sin((float) Math.toRadians(this.moveYaw)) * this.speed;
            float motionZ = MathHelper.cos((float) Math.toRadians(this.moveYaw)) * this.speed;
            this.prevPos = this.pos;

            float scaleBox = 0.1f;
            boolean colliding = !mc.world.isSpaceEmpty(new Box(
                    this.pos.x - scaleBox / 2.0f, this.pos.y, this.pos.z - scaleBox / 2.0f,
                    this.pos.x + scaleBox / 2.0f, this.pos.y + scaleBox, this.pos.z + scaleBox / 2.0f
            ));
            float delente = colliding ? 0.3f : 1.0f;

            this.yMotion /= 1.02f;
            this.pos = this.pos.add(motionX / delente, this.yMotion / delente, motionZ / delente);

            if (getTimePC() >= 1.0f) {
                this.alphaTarget = 0.0f;
                if (getAlphaPC() < 0.004f) {
                    setToRemove();
                }
            }

            this.trailParts.add(new TrailPart(this, 500));
            this.trailParts.removeIf(TrailPart::toRemove);

            for (int i = 0; i < 2; i++) {
                this.sparkParts.add(new SparkPart(this, 600));
            }
            this.sparkParts.forEach(SparkPart::motionSparkProcess);
            this.sparkParts.removeIf(SparkPart::toRemove);
        }

        void setToRemove() {
            this.toRemove = true;
        }

        boolean isToRemove() {
            return this.toRemove;
        }

        int getMsChangeSideRate() {
            return (int) getRandom(300.5, 900.5);
        }
    }

    private static class TrailPart {
        final double x, y, z;
        final long startTime = System.currentTimeMillis();
        final int maxTime;

        TrailPart(FirePart part, int maxTime) {
            this.maxTime = maxTime;
            this.x = part.pos.x;
            this.y = part.pos.y;
            this.z = part.pos.z;
        }

        boolean toRemove() {
            return System.currentTimeMillis() - this.startTime >= this.maxTime;
        }
    }

    private static class SparkPart {
        double posX, posY, posZ;
        double prevPosX, prevPosY, prevPosZ;
        double speed = Math.random() / 50.0;
        final float radianYaw = (float) Math.random() * 360.0f;
        final float radianPitch = -45.0f + (float) Math.random() * 90.0f;
        final long startTime = System.currentTimeMillis();
        final int maxTime;

        SparkPart(FirePart part, int maxTime) {
            this.maxTime = maxTime;
            this.posX = part.pos.x;
            this.posY = part.pos.y;
            this.posZ = part.pos.z;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
        }

        double timePC() {
            return MathHelper.clamp((System.currentTimeMillis() - this.startTime) / (float) this.maxTime, 0.0f, 1.0f);
        }

        boolean toRemove() {
            return this.timePC() >= 1.0;
        }

        void motionSparkProcess() {
            float radYaw = (float) Math.toRadians(this.radianYaw);
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.posX += MathHelper.sin(radYaw) * this.speed;
            this.posY += MathHelper.cos((float) Math.toRadians(this.radianPitch - 90.0f)) * this.speed;
            this.posZ += MathHelper.cos(radYaw) * this.speed;
            this.speed /= 1.2;
        }

        double getRenderPosX(float pTicks) {
            return this.prevPosX + (this.posX - this.prevPosX) * pTicks;
        }

        double getRenderPosY(float pTicks) {
            return this.prevPosY + (this.posY - this.prevPosY) * pTicks;
        }

        double getRenderPosZ(float pTicks) {
            return this.prevPosZ + (this.posZ - this.prevPosZ) * pTicks;
        }
    }

    private void svetochUpdate() {
        if (mc.player == null || mc.world == null) return;

        // спавн как у обычных партиклов "В мире": пачкой каждый тик вокруг игрока,
        // с привязкой к рельефу и подъёмом из блоков
        int max = count.getValue().intValue();
        int r = 24;
        for (int i = 0; i < 3 && particles.size() < max; i++) {
            Vec3d additional = mc.player.getEntityPos().add(
                    MathUtil.randomValue(-r, r),
                    0,
                    MathUtil.randomValue(-r, r)
            );

            BlockPos topPos = mc.world.getTopPosition(
                    Heightmap.Type.MOTION_BLOCKING,
                    BlockPos.ofFloored(additional)
            );

            double x = topPos.getX() + MathUtil.randomValue(-3, 3);
            double z = topPos.getZ() + MathUtil.randomValue(-3, 3);
            double y = mc.player.getY() + MathUtil.randomValue(mc.player.getHeight(), r);

            Vec3d spawnPos = new Vec3d(x, y, z);
            while (!mc.world.isAir(BlockPos.ofFloored(spawnPos)) && spawnPos.y < mc.world.getTopYInclusive()) {
                spawnPos = spawnPos.add(0, 1, 0);
            }

            Vec3d velocity = new Vec3d(
                    mc.player.getVelocity().x + MathUtil.randomValue(-0.6F, 0.6F),
                    MathUtil.randomValue(-0.01, 0.44),
                    mc.player.getVelocity().z + MathUtil.randomValue(-0.6F, 0.6F)
            );

            particles.add(new Particle(spawnPos, velocity));
        }
    }

    private void svetochRender(EventRender3D e) {
        if (particles.isEmpty() || mc.player == null || mc.world == null) return;

        long now = System.currentTimeMillis();
        long nanoNow = System.nanoTime();
        // физика независима от FPS: всё нормировано к 60 кадрам/с
        float ticks = (float) Math.min((nanoNow - lastFrameTime) / 1_000_000_000.0, 0.1) * 60F;
        lastFrameTime = nanoNow;

        MatrixStack matrices = e.getMatrixStack();
        Vec3d cam = mc.gameRenderer.getCamera().getCameraPos();
        Matrix4f base = matrices.peek().getPositionMatrix();
        Quaternionf camRotation = mc.gameRenderer.getCamera().getRotation();

        // общий поворот на все кубы — одна тригонометрия на кадр вместо одной на куб
        float rotation = (now % 9000L) / 9000F * 360F;
        float rotXRad = (float) Math.toRadians(rotation * 0.5F);

        int baseColor = getColor();
        int cr = (baseColor >> 16) & 0xFF;
        int cg = (baseColor >> 8) & 0xFF;
        int cb = baseColor & 0xFF;

        float size = cubeSize.getValue();

        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(allocator);

        // ВАЖНО: immediate с одним аллокатором держит активным только один слой —
        // при getBuffer() другого слоя предыдущий флушится. Поэтому рендер идёт
        // проходами по слоям, а матрицы считаются один раз и кэшируются в частице.
        visible.clear();
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.update(now, ticks);
            if (p.shouldRemove()) {
                it.remove();
                continue;
            }

            if (p.alphaValue <= 0.01F) continue;

            double dx = p.x - cam.x;
            double dy = p.y - cam.y;
            double dz = p.z - cam.z;
            // дальние кубы не пишем в буферы вообще
            if (dx * dx + dy * dy + dz * dz > CULL_DIST_SQ) continue;

            // матрицы собираются напрямую (JOML), без push/pop стека
            // и без аллокаций кватернионов RotationAxis на каждый куб
            p.cubeMatrix.set(base)
                    .translate((float) dx, (float) dy, (float) dz)
                    .rotateY((float) Math.toRadians(rotation + p.phase))
                    .rotateX(rotXRad);

            p.glowMatrix.set(base)
                    .translate((float) dx, (float) dy, (float) dz)
                    .rotate(camRotation);

            visible.add(p);
        }

        if (visible.isEmpty()) return;

        // проход 1: грани — один слой, один буфер
        VertexConsumer buf = immediate.getBuffer(FILL_LAYER);
        for (Particle p : visible) {
            int faceAlpha = (int) (p.alphaValue * 0.2F * 255);
            if (faceAlpha <= 0) continue;
            for (int[] face : FACES) {
                for (int idx : face) {
                    float[] c = CORNERS[idx];
                    buf.vertex(p.cubeMatrix, c[0] * size, c[1] * size, c[2] * size).color(cr, cg, cb, faceAlpha);
                }
            }
        }

        // проход 2: оутлайн
        buf = immediate.getBuffer(LINE_LAYER);
        for (Particle p : visible) {
            int edgeAlpha = (int) (p.alphaValue  * 255);
            if (edgeAlpha <= 0) continue;
            for (int[] edge : EDGES) {
                float[] a = CORNERS[edge[0]];
                float[] b = CORNERS[edge[1]];
                buf.vertex(p.cubeMatrix, a[0] * size, a[1] * size, a[2] * size).color(cr, cg, cb, edgeAlpha);
                buf.vertex(p.cubeMatrix, b[0] * size, b[1] * size, b[2] * size).color(cr, cg, cb, edgeAlpha);
            }
        }


        buf = immediate.getBuffer(GLOW_LAYER_BIG);
        for (Particle p : visible) {
            drawGlow(buf, p.glowMatrix, cr, cg, cb, (int) (80 * p.alphaValue), size * 6);
        }




        immediate.draw();
    }

    private static void drawGlow(VertexConsumer buf, Matrix4f m, int r, int g, int b, int alpha, float s) {
        if (alpha <= 0) return;
        buf.vertex(m, -s, -s, 0).color(r, g, b, alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
        buf.vertex(m, s, -s, 0).color(r, g, b, alpha).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
        buf.vertex(m, s, s, 0).color(r, g, b, alpha).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
        buf.vertex(m, -s, s, 0).color(r, g, b, alpha).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
    }

    private static class Particle {
        // физика как у Particles: скорость спавна умножается на BASE_VELOCITY,
        // гравитация и отскок от блоков с теми же коэффициентами
        private static final double BASE_VELOCITY = 0.05;
        private static final double BOUNCINESS = 0.85;
        private static final double GRAVITY = 0.0001;

        double x, y, z, mX, mY, mZ;
        final long start;
        final float phase;
        boolean fadingOut;
        float alphaValue;
        final Animation alpha = new Animation();
        final Matrix4f cubeMatrix = new Matrix4f();
        final Matrix4f glowMatrix = new Matrix4f();

        Particle(Vec3d pos, Vec3d velocity) {
            this.start = System.currentTimeMillis();
            this.phase = (float) (Math.random() * 100.0);
            this.x = pos.x;
            this.y = pos.y;
            this.z = pos.z;
            this.mX = velocity.x * BASE_VELOCITY;
            this.mY = velocity.y * BASE_VELOCITY;
            this.mZ = velocity.z * BASE_VELOCITY;
            alpha.set(0);
            alpha.run(1, 0.5, Easings.LINEAR, true);
        }

        void update(long now, float ticks) {
            double nextX = x + mX * ticks;
            double nextY = y + mY * ticks;
            double nextZ = z + mZ * ticks;

            // шаг за кадр крошечный: почти всегда все три оси остаются в текущем блоке —
            // тогда вместо трёх лукапов состояния блока хватает одного
            if (Math.floor(nextX) == Math.floor(x)
                    && Math.floor(nextY) == Math.floor(y)
                    && Math.floor(nextZ) == Math.floor(z)) {
                if (isHit(nextX, nextY, nextZ)) {
                    mX = -mX * BOUNCINESS;
                    mY = -mY * BOUNCINESS;
                    mZ = -mZ * BOUNCINESS;
                } else {
                    x = nextX;
                    y = nextY;
                    z = nextZ;
                }
            } else {
                if (isHit(nextX, y, z)) mX = -mX * BOUNCINESS;
                else x = nextX;
                if (isHit(x, nextY, z)) mY = -mY * BOUNCINESS;
                else y = nextY;
                if (isHit(x, y, nextZ)) mZ = -mZ * BOUNCINESS;
                else z = nextZ;
            }

            mY -= GRAVITY * ticks;

            if (!fadingOut && now - start > LIFETIME) {
                fadingOut = true;
                alpha.run(0, 2, Easings.LINEAR, true);
            }

            alpha.update();
            alphaValue = alpha.get();
        }

        boolean shouldRemove() {
            return fadingOut && alphaValue <= 0.01F;
        }

        private static boolean isHit(double px, double py, double pz) {
            BlockPos pos = BlockPos.ofFloored(px, py, pz);
            return mc.world.getBlockState(pos).isFullCube(mc.world, pos);
        }
    }
}

package ru.prism.module.impl.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import ru.prism.manager.event_impl.EventRender3D;
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
import ru.prism.module.api.settings.impl.ModeSetting;
import ru.prism.module.api.settings.impl.SliderSetting;

import ru.prism.utils.animation.Animation;
import ru.prism.utils.animation.Easings;
import ru.prism.utils.colors.ColorUtil;
import ru.prism.utils.math.MathUtil;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.*;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static net.minecraft.client.gl.RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET;

@ModuleInfo(
        name = "Target Esp",
        desc = "Подсвечивает того, кого ты щас будешь хуярить.",
        category = Category.COMBAT
)
public class TargetEsp extends Module implements ModulePreview {

    public ButtonSetting previewButton = PreviewSettings.button(this);

    public ModeSetting type = new ModeSetting(this,"Режим","Призраки","Кольцо","Кубики","Ленты","Эмблема");

    public ModeSetting typeGhost = new ModeSetting(this,"Тип призраков","1","2","3","4").setVisible(() -> type.is("Призраки"));;

    public SliderSetting speed = new SliderSetting(this,"Скорость призраков",900,400,1500,50).setVisible(() -> type.is("Призраки"));
    public SliderSetting sizeGlow = new SliderSetting(this,"Сила свечения",0.35F,0.1F,0.75F,0.05F).setVisible(() -> type.is("Призраки"));
    public SliderSetting sizeGlowOFF = new SliderSetting(this,"Размер свечения",0.7F,0.1F,2F,0.05F).setVisible(() -> type.is("Призраки"));

    public SliderSetting ringSpeed = new SliderSetting(this,"Скорость кольца",1800,600,4000,100).setVisible(() -> type.is("Кольцо"));
    public SliderSetting ringRadius = new SliderSetting(this,"Радиус кольца",0.35F,0.1F,1.5F,0.05F).setVisible(() -> type.is("Кольцо"));
    public SliderSetting ringGlow = new SliderSetting(this,"Яркость кольца",1.0F,0.2F,2.0F,0.05F).setVisible(() -> type.is("Кольцо"));

    public SliderSetting cubeCount = new SliderSetting(this,"Количество кубов",12,4,24,1).setVisible(() -> type.is("Кубики"));
    public SliderSetting cubeRadius = new SliderSetting(this,"Радиус кубов",1.5F,0.5F,3.0F,0.1F).setVisible(() -> type.is("Кубики"));
    public SliderSetting cubeSize = new SliderSetting(this,"Размер кубов",0.06F,0.02F,0.2F,0.01F).setVisible(() -> type.is("Кубики"));
    public SliderSetting cubeSpin = new SliderSetting(this,"Скорость вращения",2000,500,8000,100).setVisible(() -> type.is("Кубики"));
    public SliderSetting cubeWave = new SliderSetting(this,"Высота волны",1.0F,0.0F,1.5F,0.05F).setVisible(() -> type.is("Кубики"));
    public SliderSetting cubeGlow = new SliderSetting(this,"Сила свечения кубов",1.0F,0.0F,2.0F,0.05F).setVisible(() -> type.is("Кубики"));
    public BooleanSetting cubeOutline = new BooleanSetting(this,"Обводка кубов",true).setVisible(() -> type.is("Кубики"));
    public BooleanSetting cubeSprites = new BooleanSetting(this,"Спрайты кубов",true).setVisible(() -> type.is("Кубики"));








    public SliderSetting ribbonsSpread = new SliderSetting(this,"Разброс",0.8F,0.2F,2.0F,0.1F).setVisible(() -> type.is("Ленты"));
    public SliderSetting ribbonsSpeed = new SliderSetting(this,"Скорость",1.5F,1.0F,15.0F,0.5F).setVisible(() -> type.is("Ленты"));
    public SliderSetting ribbonsCount = new SliderSetting(this,"Количество лент",7,3,12,1).setVisible(() -> type.is("Ленты"));
    public SliderSetting ribbonsHeight = new SliderSetting(this,"Высота",1.0F,0.5F,2.5F,0.1F).setVisible(() -> type.is("Ленты"));






    public ModeSetting emblemVariant = new ModeSetting(this,"Вариант эмблемы","Кристалл","Контур","Завитки","Блик").setVisible(() -> type.is("Эмблема"));
    public SliderSetting emblemSize = new SliderSetting(this,"Размер эмблемы",1.0F,0.5F,3.0F,0.1F).setVisible(() -> type.is("Эмблема"));
    public SliderSetting emblemSpeed = new SliderSetting(this,"Скорость эмблемы",1.0F,0.5F,3.0F,0.1F).setVisible(() -> type.is("Эмблема"));

    private final PreviewSettings previewSettings = PreviewSettings.of(this, 4F, 0F, 2F);

    private double ghostPeriod() {
        return 900.0 * (900.0 / Math.max(1.0, speed.getValue()));
    }

    private double ringPeriod() {
        return 1800.0 * (1800.0 / Math.max(1.0, ringSpeed.getValue()));
    }

    private double cubePeriod() {
        return 2000.0 * (2000.0 / Math.max(1.0, cubeSpin.getValue()));
    }

    private final BufferAllocator boxAllocator = new BufferAllocator(1 << 18);
    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        boxAllocator.clear();
        orbitGhostTrails.clear();
        orbitGhostLast = 0L;
        oldCubeParticles.clear();
        oldCubeTargetId = Integer.MIN_VALUE;
        oldCubeLastSpawn = 0L;
        particleTrail.clear();
        particleTrailLast = 0L;
        cubeSparkParticles.clear();
        cubeWasSwinging = false;
        sparkLastTime = 0L;
        phantomTime = 0L;
        phantomAngle = 0F;
        markerAngle = 0F;
        markerDir = 1F;
    }

    @Override
    protected void onDisable() {
        oldCubeParticles.clear();
        oldCubeTargetId = Integer.MIN_VALUE;
        oldCubeLastSpawn = 0L;
        particleTrail.clear();
        particleTrailLast = 0L;
        cubeSparkParticles.clear();
        cubeWasSwinging = false;
        sparkLastTime = 0L;
        phantomTime = 0L;
        phantomAngle = 0F;
        markerAngle = 0F;
        markerDir = 1F;
    }

    /** Пока открыт предпоказ, таргетом считается болванчик, а не цель ауры. */
    private LivingEntity previewTarget;

    public LivingEntity target = null;
    public Animation alpha = new Animation();
    public Animation alpha_2 = new Animation();

    private float animationNurik = 0.0F;
    private long currentTimeSpirits = 0;

    private final List<List<Vec3d>> orbitGhostTrails = new ArrayList<>();
    private float orbitGhostAngle = 0F;
    private long orbitGhostLast = 0L;

    private float markerAngle = 0F;
    private float markerDir = 1F;

    private float phantomAngle = 0F;
    private long phantomTime = 0L;

    private final List<OldCubeParticle> oldCubeParticles = new ArrayList<>();
    private long oldCubeLastSpawn = 0L;
    private int oldCubeTargetId = Integer.MIN_VALUE;

    private final List<TrailPoint> particleTrail = new ArrayList<>();
    private long particleTrailLast = 0L;

    private final List<SparkParticle> cubeSparkParticles = new ArrayList<>();
    private boolean cubeWasSwinging = false;
    private long sparkLastTime = 0L;

    private float jelloSpin = 0F;

    private static final float[][] BLADE_GLOW_POINTS = new float[27][5];
    private static final float[] BLADE_COEF_A = {0.55F, 0.72F, 0.48F, 0.65F, 0.58F, 0.8F};
    private static final float[] BLADE_COEF_B = {0.0F, 1.2F, 2.8F, 4.1F, 5.5F, 0.7F};
    private static final float[] BLADE_COEF_C = {50.0F, 65.0F, 40.0F, 60.0F, 55.0F, 70.0F};
    private static final float[] BLADE_COEF_D = {0.35F, 0.5F, 0.42F, 0.6F, 0.38F, 0.55F};
    private static final float[] BLADE_COEF_E = {1.0F, 3.5F, 0.5F, 2.2F, 4.8F, 1.8F};
    private static final float[] BLADE_COEF_F = {20.0F, 30.0F, 25.0F, 35.0F, 22.0F, 28.0F};
    private static final float[] BLADE_COEF_G = {0.4F, 0.55F, 0.35F, 0.5F, 0.45F, 0.62F};
    private static final float[] BLADE_COEF_H = {0.5F, 2.0F, 3.8F, 1.3F, 4.5F, 0.2F};
    private static final float[] BLADE_COEF_I = {0.08F, 0.12F, 0.07F, 0.1F, 0.09F, 0.11F};

    private static final Vec3d[] SHAPE_POSITIONS = new Vec3d[]{
            new Vec3d(0.0, 0.8500003162575299, 0.8000000015134241),
            new Vec3d(0.20000001585804067, 0.8500003162575299, -0.6750000685489941),
            new Vec3d(0.6000003159631385, 1.3499998137357587, 0.6000003159631385),
            new Vec3d(-0.7400002388842586, 1.0499995624093656, 0.39999995534312804),
            new Vec3d(0.7400000600814527, 0.950000007956495, -0.40000007458364306),
            new Vec3d(-0.475000060783353, 0.8500003162575299, -0.375),
            new Vec3d(0.0, 1.3499998137357587, -0.6000002870957047),
            new Vec3d(0.8500003162575299, 0.7000001202807937, 0.09999998819572534),
            new Vec3d(-0.7000000376259103, 1.3499998137357587, -0.30000000195726123),
            new Vec3d(-0.30000000195726123, 1.3499998137357587, 0.550000008949574),
            new Vec3d(-0.5, 0.7000001202807937, 0.7000001202807937),
            new Vec3d(0.5, 0.7000001202807937, 0.7000001202807937),
            new Vec3d(-0.7000000376259103, 0.75, 0.0),
            new Vec3d(-0.20000003729295496, 0.6499997319148408, -0.7000000376259103)
    };
    private static final Vec3d[] SHAPE_ROTATIONS = new Vec3d[]{
            new Vec3d(-49.0, 0.0, 40.0),
            new Vec3d(35.0, 0.0, -30.0),
            new Vec3d(-30.0, 0.0, 35.0),
            new Vec3d(-25.0, 0.0, -30.0),
            new Vec3d(0.0, 0.0, 0.0),
            new Vec3d(30.0, 0.0, -25.0),
            new Vec3d(45.0, 0.0, 0.0),
            new Vec3d(-30.0, 0.0, 30.0),
            new Vec3d(0.0, 0.0, 0.0),
            new Vec3d(0.0, 0.0, 0.0),
            new Vec3d(0.0, 0.0, 0.0),
            new Vec3d(0.0, 0.0, 0.0),
            new Vec3d(0.0, 0.0, 0.0),
            new Vec3d(0.0, 0.0, 0.0)
    };

    static {
        int k = 0;
        BLADE_GLOW_POINTS[k++] = new float[]{0.0F, 0.0F, -0.25F, 0.22F, 1.0F};
        for (int i = 1; i <= 12; i++) {
            float t = (float) i / 13.0F;
            float x = -0.5F * t;
            float z = -0.25F + (0.2F - -0.25F) * t;
            float half = 0.18F + (0.14F - 0.18F) * t;
            float af = 0.9F + (0.7F - 0.9F) * t;
            BLADE_GLOW_POINTS[k++] = new float[]{x, 0.0F, z, half, af};
        }
        BLADE_GLOW_POINTS[k++] = new float[]{-0.5F, 0.0F, 0.2F, 0.2F, 0.9F};
        for (int i = 1; i <= 12; i++) {
            float t = (float) i / 13.0F;
            float x = 0.5F * t;
            float z = -0.25F + (0.2F - -0.25F) * t;
            float half = 0.18F + (0.14F - 0.18F) * t;
            float af = 0.9F + (0.7F - 0.9F) * t;
            BLADE_GLOW_POINTS[k++] = new float[]{x, 0.0F, z, half, af};
        }
        BLADE_GLOW_POINTS[k] = new float[]{0.5F, 0.0F, 0.2F, 0.2F, 0.9F};
    }

    // ───────────────────────────── предпоказ ─────────────────────────────

    @Override
    public PreviewSettings previewSettings() {
        return previewSettings;
    }

    @Override
    public boolean previewNeedsDummy() {
        return true;
    }

    @Override
    public void previewStart(PreviewContext ctx) {
        previewTarget = ctx.dummy();
    }

    @Override
    public void previewTick(PreviewContext ctx) {
        previewTarget = ctx.dummy();
    }

    /** Цикл показа — вспышка урона: по ней видно, как эффект краснеет и сжимается. */
    @Override
    public void previewSpawn(PreviewContext ctx) {
        if (ctx.dummy() != null) ctx.dummy().hurtTime = 10;
    }

    @Override
    public void previewStop() {
        previewTarget = null;
        target = null;
    }

    @EventHandler
    public void onRender(EventRender3D e) {
        alpha.update();

        LivingEntity currentTarget = previewTarget;

        if (currentTarget == null && mc.player != null && mc.targetedEntity instanceof LivingEntity looked
                && looked != mc.player) {
            currentTarget = looked;
        }

        if (currentTarget != null) {
            target = currentTarget;
        } else if (target != null && (target.isRemoved() || !target.isAlive())) {
            target = null;
        }

        if (mc.world == null || mc.player == null) return;

        alpha.run(currentTarget != null ? 1 : 0, 0.15F, Easings.SINE_OUT);
        float alphaPC = alpha.get();



        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(boxAllocator);
        if (alphaPC > 0.001f && target != null && type.is("Призраки") && typeGhost.is("2")) {
            long currentTime = System.currentTimeMillis();
            if (currentTimeSpirits == 0) {
                currentTimeSpirits = currentTime;
            }

            long timeDiff = currentTime - currentTimeSpirits;
            if (timeDiff > 0) {
            animationNurik += (float) (5L * timeDiff) / (long) ghostPeriod();
            }
            currentTimeSpirits = currentTime;

            MatrixStack matrices = e.getMatrixStack();


            Vec3d lerpedPos = target.getLerpedPos(e.getTickDelta());
            Vec3d cameraPos = mc.gameRenderer.getCamera().getCameraPos();

            double x = lerpedPos.x - cameraPos.x;
            double y = lerpedPos.y  - cameraPos.y;
            double z = lerpedPos.z - cameraPos.z;

            alphaPC = (float) alpha.getValue();

            alpha_2.update();
            int hurtTicks = target.hurtTime;
            float hurtPC = (float) Math.sin((double) hurtTicks * (Math.PI / 20D));
            alpha_2.run(hurtPC,0.1F,Easings.SINE_OUT);

            float atts = alpha_2.get();

            int fadeColor = ColorUtil.fade(1);
            int redColor = ColorUtil.getColor(200, 70, 70, (int) (255.0F * alphaPC));
            int baseColor = ColorUtil.overCol(ColorUtil.multAlpha(fadeColor, alphaPC), redColor, atts);


            int n2 = 3;
            int n3 = 12;
            int n4 = 3 * n2;

            matrices.push();

            Camera camera = mc.gameRenderer.getCamera();

            for (int i = 0; i < n4; i += n2) {
                for (int j = 0; j < n3; j++) {
                    float f2 = animationNurik + (float) j * 0.1F;
                    float f3 = 0.6F;
                    float f4 = 0.4F;
                    int n5 = (int) Math.pow((double) i, 2.0F);

                    matrices.push();

                    double particleX = x + (double) (f3 * Math.sin(f2 + (float) n5));
                    double particleY = y + (double) f4 + (double) (0.3F * Math.sin(animationNurik + (float) j * 0.2F))
                            + (double) (0.2F * (float) i);
                    double particleZ = z + (double) (f3 * Math.cos(f2 - (float) n5));

                    matrices.translate(particleX, particleY, particleZ);

                    float scale =  (0.006F + (float) j / 2000.0F ) * alphaPC;
                    matrices.scale(scale, scale, scale);

                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));

                    Matrix4f matrix = matrices.peek().getPositionMatrix();
                    VertexConsumer consumer = immediate.getBuffer(ROMB_ESP.apply(Identifier.of("client", "textures/visuals/particles_3.png")));

                    int color = baseColor;
 

                    int n7 = -20;
                    int n8 = 35;

                    consumer.vertex(matrix, (float) n7, (float) (n7 + n8), 0.0f)
                            .color(baseColor)
                            .texture(0.0F, 1.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);

                    consumer.vertex(matrix, (float) (n7 + n8), (float) (n7 + n8), 0.0f)
                            .color(baseColor)
                            .texture(1.0F, 1.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);

                    consumer.vertex(matrix, (float) (n7 + n8), (float) n7, 0.0f)
                            .color(baseColor)
                            .texture(1.0F, 0.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);

                    consumer.vertex(matrix, (float) n7, (float) n7, 0.0f)
                            .color(baseColor)
                            .texture(0.0F, 0.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);



                     n7 = (int) (-20  - 20 * sizeGlowOFF.getValue());
                     n8 = (int) (35 + 40 * sizeGlowOFF.getValue());

                    consumer.vertex(matrix, (float) n7, (float) (n7 + n8), 0.0f)
                            .color(ColorUtil.replAlpha(baseColor,alphaPC * sizeGlow.getValue()))
                            .texture(0.0F, 1.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);

                    consumer.vertex(matrix, (float) (n7 + n8), (float) (n7 + n8), 0.0f)
                            .color(ColorUtil.replAlpha(baseColor,alphaPC * sizeGlow.getValue()))
                            .texture(1.0F, 1.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);

                    consumer.vertex(matrix, (float) (n7 + n8), (float) n7, 0.0f)
                            .color(ColorUtil.replAlpha(baseColor,alphaPC * sizeGlow.getValue()))
                            .texture(1.0F, 0.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);

                    consumer.vertex(matrix, (float) n7, (float) n7, 0.0f)
                            .color(ColorUtil.replAlpha(baseColor,alphaPC * sizeGlow.getValue()))
                            .texture(0.0F, 0.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);

                    matrices.pop();
                }
            }

            matrices.pop();
        }
        if (alphaPC > 0.001f && target != null && type.is("Призраки") && typeGhost.is("4")) {
            long currentTime = System.currentTimeMillis();
            if (currentTimeSpirits == 0) {
                currentTimeSpirits = currentTime;
            }

            long timeDiff = currentTime - currentTimeSpirits;
            if (timeDiff > 0) {
            animationNurik += (float) (5L * timeDiff) / (long) ghostPeriod();
            }
            currentTimeSpirits = currentTime;

            MatrixStack matrices = e.getMatrixStack();


            Vec3d lerpedPos = target.getLerpedPos(e.getTickDelta());
            Vec3d cameraPos = mc.gameRenderer.getCamera().getCameraPos();

            double x = lerpedPos.x - cameraPos.x;
            double y = lerpedPos.y  - cameraPos.y;
            double z = lerpedPos.z - cameraPos.z;

            alphaPC = (float) alpha.getValue();

            alpha_2.update();
            int hurtTicks = target.hurtTime;
            float hurtPC = (float) Math.sin((double) hurtTicks * (Math.PI / 20D));
            alpha_2.run(hurtPC,0.1F,Easings.SINE_OUT);

            float atts = alpha_2.get();

            int fadeColor = ColorUtil.fade(1);
            int redColor = ColorUtil.getColor(200, 70, 70, (int) (255.0F * alphaPC));
            int baseColor = ColorUtil.overCol(ColorUtil.multAlpha(fadeColor, alphaPC), redColor, atts);


            int n2 = 3;
            int n3 = 24;
            int n4 = 3 * n2;

            matrices.push();

            Camera camera = mc.gameRenderer.getCamera();

            for (int i = 0; i < n4; i += n2) {
                for (int j = 0; j < n3; j++) {
                    float f2 = animationNurik + (float) j * 0.05F;
                    float f3 = target.getWidth();
                    float f4 = 0.45F;
                    int n5 = (int) Math.pow((double) i, 2.0F);

                    matrices.push();

                    double particleX = x + (double) (f3 * Math.sin(f2 + (float) n5));
                    double particleY = y + (double) f4 + (double) (0.1F * Math.sin(animationNurik + (float) j * 0.1F))
                            + (double) (0.2F * (float) i);
                    double particleZ = z + (double) (f3 * Math.cos(f2 - (float) n5));

                    matrices.translate(particleX, particleY, particleZ);

                    float scale =  (0.009F + (float) j / 2000.0F ) * alphaPC;
                    matrices.scale(scale, scale, scale);

                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));

                    Matrix4f matrix = matrices.peek().getPositionMatrix();
                    VertexConsumer consumer = immediate.getBuffer(ROMB_ESP.apply(Identifier.of("client", "textures/visuals/particles_3.png")));

                    int color = baseColor;


                    int n7 = -12;
                    int n8 = 16;

                    consumer.vertex(matrix, (float) n7, (float) (n7 + n8), 0.0f)
                            .color(baseColor)
                            .texture(0.0F, 1.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);

                    consumer.vertex(matrix, (float) (n7 + n8), (float) (n7 + n8), 0.0f)
                            .color(baseColor)
                            .texture(1.0F, 1.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);

                    consumer.vertex(matrix, (float) (n7 + n8), (float) n7, 0.0f)
                            .color(baseColor)
                            .texture(1.0F, 0.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);

                    consumer.vertex(matrix, (float) n7, (float) n7, 0.0f)
                            .color(baseColor)
                            .texture(0.0F, 0.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);



                    n7 = (int) (-12  - 20 * sizeGlowOFF.getValue());
                    n8 = (int) (16 + 40 * sizeGlowOFF.getValue());

                    consumer.vertex(matrix, (float) n7, (float) (n7 + n8), 0.0f)
                            .color(ColorUtil.replAlpha(baseColor,alphaPC * sizeGlow.getValue()))
                            .texture(0.0F, 1.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);

                    consumer.vertex(matrix, (float) (n7 + n8), (float) (n7 + n8), 0.0f)
                            .color(ColorUtil.replAlpha(baseColor,alphaPC * sizeGlow.getValue()))
                            .texture(1.0F, 1.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);

                    consumer.vertex(matrix, (float) (n7 + n8), (float) n7, 0.0f)
                            .color(ColorUtil.replAlpha(baseColor,alphaPC * sizeGlow.getValue()))
                            .texture(1.0F, 0.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);

                    consumer.vertex(matrix, (float) n7, (float) n7, 0.0f)
                            .color(ColorUtil.replAlpha(baseColor,alphaPC * sizeGlow.getValue()))
                            .texture(0.0F, 0.0F)
                            .overlay(OverlayTexture.DEFAULT_UV)
                            .light(0xF000F0)
                            .normal(0, 0, 1);

                    matrices.pop();
                }
            }

            matrices.pop();
        }
        if (alphaPC > 0.001f && target != null && type.is("Призраки") && typeGhost.is("1")) {

            long speed_f = (long) ghostPeriod();

            long currentTime = System.currentTimeMillis();
            if (currentTimeSpirits == 0) {
                currentTimeSpirits = currentTime;
            }

            long timeDiff = currentTime - currentTimeSpirits;
            if (timeDiff > 0) {
                animationNurik += (float) (5L * timeDiff) / speed_f;
            }
            currentTimeSpirits = currentTime;

            int hurtTicks = target.hurtTime;
            float hurtPC = (float) Math.sin((double) hurtTicks * (Math.PI / 20D));
            int redColor = ColorUtil.getColor(255, 100, 100, (int) (255.0F * alphaPC));
            int color = ColorUtil.overCol(ColorUtil.multAlpha(ColorUtil.fade(0), alphaPC), redColor, hurtPC);
            int color2 = ColorUtil.overCol(ColorUtil.multAlpha(ColorUtil.fade(90), alphaPC), redColor,  hurtPC);
            int color3 = ColorUtil.overCol(ColorUtil.multAlpha(ColorUtil.fade(180), alphaPC), redColor, hurtPC);
            int color4 = ColorUtil.overCol(ColorUtil.multAlpha(ColorUtil.fade(360), alphaPC), redColor, hurtPC);



            MatrixStack matrices = e.getMatrixStack();

            Vec3d lerpedPos = target.getLerpedPos(e.getTickDelta());
            Vec3d cameraPos = mc.gameRenderer.getCamera().getCameraPos();

            long time = System.currentTimeMillis();
            double speed = (double) time / speed_f;
            float radius = target.getWidth()  + 0.25F - 0.25F * alphaPC;
            int ghostCount = 3;
            int cound = 10;

            for (int i = 0; i < ghostCount; i++) {
                for (int s = 0; s < cound; s++) {
                    matrices.push();

                    float f2 = animationNurik + (float) s * 0.1F;

                    double angle = speed + (i * (Math.PI * 2 / ghostCount));

                    double offX = Math.cos(angle + f2) * radius;
                    double offZ = Math.sin(angle + f2) * radius;

                    double offY = Math.sin(speed + i) * 0.6 + 0.15  * Math.sin(speed + (float) s * 0.11F);


                    matrices.translate(
                            lerpedPos.x - cameraPos.x + offX,
                            lerpedPos.y - cameraPos.y + (target.getHeight() / 2.0F) + offY,
                            lerpedPos.z - cameraPos.z + offZ
                    );


                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-mc.gameRenderer.getCamera().getYaw()));
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(mc.gameRenderer.getCamera().getPitch()));


                    float scale =  ((0.12F + (float) s /40.0F) * 1.25F) * alphaPC;
                    matrices.scale(scale, scale, scale);

                    Matrix4f matrix = matrices.peek().getPositionMatrix();
                    VertexConsumer consumer = immediate.getBuffer(ROMB_ESP.apply(Identifier.of("client", "textures/visuals/particles_3.png")));


                    drawGradientQuad(consumer, matrix, color,color2,color3,color4, (int) (255 * alphaPC));

                    scale = ((0.12F + (float) s /40.0F) * (100 * sizeGlowOFF.getValue()) / 10) * alphaPC;
                    matrices.scale(scale, scale, scale);
                    VertexConsumer consumer2 = immediate.getBuffer(ROMB_ESP.apply(Identifier.of("client", "textures/visuals/particles_2.png")));

                    drawGradientQuad(consumer2, matrix, color,color2,color3,color4, (int) ((255 * sizeGlow.getValue() * 1) * alphaPC));


                    matrices.pop();
                }
            }
        }
        if (alphaPC > 0.001f && target != null && type.is("Призраки") && typeGhost.is("3")) {

            long speed_f = (long) ghostPeriod();

            long currentTime = System.currentTimeMillis();
            if (currentTimeSpirits == 0) {
                currentTimeSpirits = currentTime;
            }

            long timeDiff = currentTime - currentTimeSpirits;
            if (timeDiff > 0) {
                animationNurik += (float) (5L * timeDiff) / speed_f;
            }
            currentTimeSpirits = currentTime;

            int hurtTicks = target.hurtTime;
            float hurtPC = (float) Math.sin((double) hurtTicks * (Math.PI / 20D));
            int redColor = ColorUtil.getColor(255, 100, 100, (int) (255.0F * alphaPC));
            int color = ColorUtil.overCol(ColorUtil.multAlpha(ColorUtil.fade(0), alphaPC), redColor, hurtPC);
            int color2 = ColorUtil.overCol(ColorUtil.multAlpha(ColorUtil.fade(90), alphaPC), redColor,  hurtPC);
            int color3 = ColorUtil.overCol(ColorUtil.multAlpha(ColorUtil.fade(180), alphaPC), redColor, hurtPC);
            int color4 = ColorUtil.overCol(ColorUtil.multAlpha(ColorUtil.fade(360), alphaPC), redColor, hurtPC);



            MatrixStack matrices = e.getMatrixStack();

            Vec3d lerpedPos = target.getLerpedPos(e.getTickDelta());
            Vec3d cameraPos = mc.gameRenderer.getCamera().getCameraPos();

            long time = System.currentTimeMillis();
            double speed = (double) time / speed_f;
            float radius = target.getWidth()  + 0.25F - 0.25F * alphaPC;
            int ghostCount = 3;
            int cound = 9;

            for (int i = 0; i < ghostCount; i++) {
                for (int s = 0; s < cound; s++) {
                    matrices.push();

                    float f2 = animationNurik + (float) s * 0.1F;

                    double angle = speed + (i * (Math.PI * 2 / ghostCount));

                    double offX = Math.cos(angle + f2) * radius;
                    double offZ = Math.sin(angle + f2) * radius;

                    double offY = Math.sin(speed ) * 0.7  + 0.2  * Math.sin(speed + (float) s * 0.11F);


                    matrices.translate(
                            lerpedPos.x - cameraPos.x + offX,
                            lerpedPos.y - cameraPos.y + (target.getHeight() / 2.0F) + offY,
                            lerpedPos.z - cameraPos.z + offZ
                    );


                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-mc.gameRenderer.getCamera().getYaw()));
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(mc.gameRenderer.getCamera().getPitch()));


                    float scale =  (0.12F + (float) s /50.0F) * 1.5F;
                    matrices.scale(scale, scale, scale);

                    Matrix4f matrix = matrices.peek().getPositionMatrix();
                    VertexConsumer consumer = immediate.getBuffer(ROMB_ESP.apply(Identifier.of("client", "textures/visuals/particles_3.png")));


                    drawGradientQuad(consumer, matrix, color,color2,color3,color4, (int) (255 * alphaPC));

                    scale = (0.12F + (float) s /50.0F) * (100 * sizeGlowOFF.getValue()) / 10;
                    matrices.scale(scale, scale, scale);
                    VertexConsumer consumer2 = immediate.getBuffer(ROMB_ESP.apply(Identifier.of("client", "textures/visuals/particles_2.png")));

                    drawGradientQuad(consumer2, matrix, color,color2,color3,color4, (int) ((255 * sizeGlow.getValue() * 1) * alphaPC));


                    matrices.pop();
                }
            }
        }



        if (alphaPC > 0.001f && target != null && type.is("Кольцо")) {
            int hurtTicks = target.hurtTime;
            float hurtPC = (float) Math.sin(hurtTicks * (Math.PI / 10.0));
            int redColor = ColorUtil.getColor(255, 100, 100, (int)(255.0F * alphaPC));

            double duration = ringPeriod();
            double elapsed  = System.currentTimeMillis() % duration;
            boolean side    = elapsed > duration / 2.0;
            double raw      = elapsed / (duration / 2.0);
            raw = side ? (raw - 1.0) : 1.0 - raw;
            double progress = raw < 0.5
                    ? 2.0 * raw * raw
                    : 1.0 - Math.pow(-2.0 * raw + 2.0, 2.0) / 2.0;

            float height2 = target.getHeight() ;
            double eased  = (height2 / 1.7) * (progress > 0.5 ? 1.0 - progress : progress) * (side ? -1 : 1) /0.7;

            MatrixStack matrices = e.getMatrixStack();
            Vec3d lerpedPos = target.getLerpedPos(e.getTickDelta());
            Vec3d cameraPos = mc.gameRenderer.getCamera().getCameraPos();

            matrices.push();
            matrices.translate(lerpedPos.x - cameraPos.x, lerpedPos.y - cameraPos.y, lerpedPos.z - cameraPos.z);
            Matrix4f matrix = matrices.peek().getPositionMatrix();

            float ringRad = ringRadius.getValue();
            float radius = (target.getWidth() - 0.1F) + ringRad - ringRad * alphaPC;
            float yBase  = (float)(height2 * progress);
            float yTop   = (float)(height2 * progress + eased);


            VertexConsumer fillBuf = immediate.getBuffer(RING_FILL_LAYER);
            for (int seg = 0; seg < 360; seg++) {
                float a0 = (float) Math.toRadians(seg);
                float a1 = (float) Math.toRadians(seg + 1);
                int c0 = ColorUtil.overCol(ColorUtil.multAlpha(ColorUtil.fade(seg ), alphaPC), redColor, hurtPC);
                int c1 = ColorUtil.overCol(ColorUtil.multAlpha(ColorUtil.fade((seg  )), alphaPC), redColor, hurtPC);
                float x0 = (float)(Math.cos(a0) * radius), z0 = (float)(Math.sin(a0) * radius);
                float x1 = (float)(Math.cos(a1) * radius), z1 = (float)(Math.sin(a1) * radius);
                fillBuf.vertex(matrix, x0, yBase, z0).color(ColorUtil.replAlpha(c0, (int)(120 * alphaPC * ringGlow.getValue())));
                fillBuf.vertex(matrix, x1, yBase, z1).color(ColorUtil.replAlpha(c1, (int)(120 * alphaPC * ringGlow.getValue())));
                fillBuf.vertex(matrix, x1, yTop,  z1).color(ColorUtil.replAlpha(c1, 0));
                fillBuf.vertex(matrix, x0, yTop,  z0).color(ColorUtil.replAlpha(c0, 0));
            }

            VertexConsumer lineBuf = immediate.getBuffer(RING_LINE_LAYER);
            for (int seg = 0; seg < 360; seg++) {
                float a0 = (float) Math.toRadians(seg);
                float a1 = (float) Math.toRadians(seg + 1);
                int c =ColorUtil.multAlpha(ColorUtil.overCol(ColorUtil.multBright(ColorUtil.fade(1),0.7F),redColor,hurtPC), alphaPC);
                lineBuf.vertex(matrix, (float)(Math.cos(a0) * radius), yBase, (float)(Math.sin(a0) * radius)).color(ColorUtil.replAlpha(c, (int)(150 * alphaPC * ringGlow.getValue())));
                lineBuf.vertex(matrix, (float)(Math.cos(a1) * radius), yBase, (float)(Math.sin(a1) * radius)).color(ColorUtil.replAlpha(c, (int)(150 * alphaPC * ringGlow.getValue())));
            }

            matrices.pop();
        }

        if (alphaPC > 0.001f && target != null && type.is("Кубики")) {
            int hurtTicks = target.hurtTime;
            float hurtPC = (float) Math.sin(hurtTicks * (Math.PI / 10.0));

            alpha_2.update();
            alpha_2.run(hurtPC,0.1F,Easings.SINE_OUT);

            int redColor = ColorUtil.getColor(255, 100, 100, (int)(255.0f * alphaPC));
            int color = ColorUtil.overCol(ColorUtil.multAlpha(ColorUtil.fade(1), alphaPC), redColor, alpha_2.get());

            MatrixStack matrices = e.getMatrixStack();
            Vec3d cameraPos = mc.gameRenderer.getCamera().getCameraPos();
            Vec3d targetPos = target.getLerpedPos(e.getTickDelta());

            float time = (float) ((Math.cos(System.currentTimeMillis() / cubePeriod()) )) * 360 + (alpha_2.get() * 20);
            int cound = Math.max(1, cubeCount.getValue().intValue());
            float width = target.getWidth() * cubeRadius.getValue();
            float sizeFI = (1f - 0.3F * alpha_2.get()) * alphaPC;

            Camera camera = mc.gameRenderer.getCamera();

            for (int i = 0; i < 360; i += cound) {
                float val = 1.2f - 0.5f ;
                float sin = (float)(Math.sin((float) Math.toRadians(i + time)) * width * val);
                float cos = (float)(Math.cos((float) Math.toRadians(i + time)) * width * val);

                double x = targetPos.x + sin;
                double z = targetPos.z + cos;
                double y = targetPos.y + target.getHeight() * cubeWave.getValue() * Math.abs(MathUtil.sin(i));

                matrices.push();
                matrices.translate(x - cameraPos.x, y - cameraPos.y, z - cameraPos.z);
                matrices.multiply(camera.getRotation());
                float gs = 0.6f * sizeFI;
                matrices.scale(gs, gs, gs);
                if (cubeSprites.getValue())
                drawGradientQuad(immediate.getBuffer(ROMB_ESP.apply(Identifier.of("client", "textures/visuals/particles_1.png"))),
                        matrices.peek().getPositionMatrix(),
                        ColorUtil.multAlpha(color, 0.3f), ColorUtil.multAlpha(color, 0.3f),
                        ColorUtil.multAlpha(color, 0.3f), ColorUtil.multAlpha(color, 0.3f),
                        (int)(alphaPC * 0.35f * 255));
                matrices.pop();
            }

            // Pass 2: cube fills
            for (int i = 0; i < 360; i += cound) {
                float val = 1.2f - 0.5f ;
                float sin = (float)(Math.sin((float) Math.toRadians(i + time)) * width * val);
                float cos = (float)(Math.cos((float) Math.toRadians(i + time)) * width * val);

                double x = targetPos.x + sin;
                double z = targetPos.z + cos;
                double y = targetPos.y + target.getHeight() * cubeWave.getValue() * Math.abs(MathUtil.sin(i));

                Vec3d cubePos = new Vec3d(x, y, z);
                Vector3f directionToTarget = new Vector3f(
                        (float)(targetPos.x - cubePos.x),
                        (float)(targetPos.y - cubePos.y),
                        (float)(targetPos.z - cubePos.z)
                ).normalize();

                matrices.push();
                matrices.translate(x - cameraPos.x, y - cameraPos.y, z - cameraPos.z);
                matrices.multiply(new Quaternionf().rotationTo(new Vector3f(0, 1, 0), directionToTarget));
                Matrix4f matrix = matrices.peek().getPositionMatrix();
                float size = cubeSize.getValue() * sizeFI;
                VertexConsumer fillBuf = immediate.getBuffer(RING_FILL_LAYER);
                for (int shell = 3; shell >= 1; shell--) {
                    drawCubeFillTESP(fillBuf, matrix, size * (1.0f + shell * 0.45f),
                            ColorUtil.replAlpha(color, (int)(alphaPC * 255 * (0.16f / shell) * cubeGlow.getValue())));
                }
                drawCubeFillTESP(fillBuf, matrix, size,
                        ColorUtil.replAlpha(color, (int)(alphaPC * 0.9f * 255)));
                matrices.pop();
            }

            // Pass 3: cube outlines
            for (int i = 0; i < 360; i += cound) {
                float val = 1.2f - 0.5f ;
                float sin = (float)(Math.sin((float) Math.toRadians(i + time)) * width * val);
                float cos = (float)(Math.cos((float) Math.toRadians(i + time)) * width * val);

                double x = targetPos.x + sin;
                double z = targetPos.z + cos;
                double y = targetPos.y + target.getHeight() * cubeWave.getValue() * Math.abs(MathUtil.sin(i));

                Vec3d cubePos = new Vec3d(x, y, z);
                Vector3f directionToTarget = new Vector3f(
                        (float)(targetPos.x - cubePos.x),
                        (float)(targetPos.y - cubePos.y),
                        (float)(targetPos.z - cubePos.z)
                ).normalize();

                matrices.push();
                matrices.translate(x - cameraPos.x, y - cameraPos.y, z - cameraPos.z);
                matrices.multiply(new Quaternionf().rotationTo(new Vector3f(0, 1, 0), directionToTarget));
                Matrix4f matrix = matrices.peek().getPositionMatrix();
                float size = cubeSize.getValue() * sizeFI;
                if (cubeOutline.getValue())
                drawCubeOutlineTESP(immediate.getBuffer(RING_LINE_LAYER), matrix, size,
                        ColorUtil.replAlpha(color, (int)(alphaPC * 255)));
                matrices.pop();
            }
        }













        if (alphaPC > 0.001f && target != null && type.is("Ленты")) {
            int hurtTicks = target.hurtTime;
            float hurtPC = (float) Math.sin(hurtTicks * Math.PI / 20);
            int redColor = ColorUtil.getColor(255, 100, 100, (int) (255F * alphaPC));

            MatrixStack matrices = e.getMatrixStack();
            Vec3d lerpedPos = target.getLerpedPos(e.getTickDelta());
            Vec3d cameraPos = mc.gameRenderer.getCamera().getCameraPos();

            float spread = ribbonsSpread.getValue() * alphaPC;
            float height = target.getHeight() * ribbonsHeight.getValue();
            float spin = (float) ((System.currentTimeMillis() % 600000L) / 1000.0D * (double) ribbonsSpeed.getValue() * 1.667D);
            int baseColor = ColorUtil.overCol(ColorUtil.multAlpha(ColorUtil.fade(0), alphaPC), redColor, hurtPC);

            matrices.push();
            matrices.translate(lerpedPos.x - cameraPos.x, lerpedPos.y - cameraPos.y, lerpedPos.z - cameraPos.z);
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            VertexConsumer lineBuf = immediate.getBuffer(RING_LINE_LAYER);

            float[][] bands = {{0.06F, 0.18F}, {0.14F, 0.07F}, {0.26F, 0.03F}};
            float[][] dirs = {{1F, 0F}, {-1F, 0F}, {0F, 1F}, {0F, -1F}};

            for (float[] band : bands) {
                for (float[] dir : dirs) {
                    for (int m = 0; m < ribbonsCount.getValue().intValue(); m++) {
                        float ox = dir[0] * band[0];
                        float oz = dir[1] * band[0];
                        float[] prev = null;
                        for (int k = 0; k <= 80; k += 4) {
                            float t = k / 80.0F;
                            float[] p = ribbonPoint(spread, height, spin, m, ox, oz, t);
                            if (prev != null) {
                                int alpha = (int) (255F * alphaPC * Math.abs(MathHelper.sin(t * MathHelper.PI)) * band[1]);
                                int col = ColorUtil.replAlpha(baseColor, alpha);
                                lineBuf.vertex(matrix, prev[0], prev[1], prev[2]).color(col);
                                lineBuf.vertex(matrix, p[0], p[1], p[2]).color(col);
                            }
                            prev = p;
                        }
                    }
                }
            }

            for (int m = 0; m < ribbonsCount.getValue().intValue(); m++) {
                float[] prev = null;
                for (int k = 0; k <= 80; k++) {
                    float t = k / 80.0F;
                    float[] p = ribbonPoint(spread, height, spin, m, 0F, 0F, t);
                    if (prev != null) {
                        int alpha = (int) (255F * alphaPC * Math.abs(MathHelper.sin(t * MathHelper.PI)) * 0.85F);
                        int col = ColorUtil.replAlpha(baseColor, alpha);
                        lineBuf.vertex(matrix, prev[0], prev[1], prev[2]).color(col);
                        lineBuf.vertex(matrix, p[0], p[1], p[2]).color(col);
                    }
                    prev = p;
                }

                prev = null;
                for (int k = 0; k <= 80; k += 2) {
                    float t = k / 80.0F;
                    float[] p = ribbonPoint(spread, height, spin, m, 0.02F, 0F, t);
                    if (prev != null) {
                        int alpha = (int) (255F * alphaPC * Math.abs(MathHelper.sin(t * MathHelper.PI)) * 0.2F);
                        int col = ColorUtil.replAlpha(baseColor, alpha);
                        lineBuf.vertex(matrix, prev[0], prev[1], prev[2]).color(col);
                        lineBuf.vertex(matrix, p[0], p[1], p[2]).color(col);
                    }
                    prev = p;
                }
            }

            matrices.pop();
        }






        if (alphaPC > 0.001f && target != null && type.is("Эмблема")) {
            int hurtTicks = target.hurtTime;
            float hurtPC = (float) Math.sin(hurtTicks * Math.PI / 20);
            int redColor = ColorUtil.getColor(200, 70, 70, (int) (255F * alphaPC));

            MatrixStack matrices = e.getMatrixStack();
            Vec3d lerpedPos = target.getLerpedPos(e.getTickDelta());
            Vec3d cameraPos = mc.gameRenderer.getCamera().getCameraPos();
            Camera camera = mc.gameRenderer.getCamera();

            if (emblemVariant.is("Блик")) {
                markerAngle += 0.01F * markerDir;
                if (markerAngle >= 2.3F) {
                    markerAngle = 2.3F;
                    markerDir = -1F;
                }
                if (markerAngle <= -2.3F) {
                    markerAngle = -2.3F;
                    markerDir = 1F;
                }
                int markerColor = ColorUtil.overCol(ColorUtil.multAlpha(ColorUtil.fade(0), alphaPC), redColor, hurtPC);
                float markerScale = 0.15F * alphaPC * emblemSize.getValue();
                matrices.push();
                matrices.translate(lerpedPos.x - cameraPos.x, lerpedPos.y - cameraPos.y + target.getHeight() / 2.0, lerpedPos.z - cameraPos.z);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
                matrices.scale(8F * markerScale, 8F * markerScale, 8F * markerScale);
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(markerAngle * 57.29578F));
                drawGradientQuad(immediate.getBuffer(ROMB_ESP.apply(Identifier.of("client", "textures/visuals/marker.png"))), matrices.peek().getPositionMatrix(), markerColor, markerColor, markerColor, markerColor, (int) (255F * alphaPC));
                matrices.pop();
            } else {
                float period = (emblemVariant.is("Контур") ? 1600F : emblemVariant.is("Завитки") ? 1000F : 900F) / emblemSpeed.getValue();
                float baseScale = (emblemVariant.is("Контур") ? 1.5F : emblemVariant.is("Завитки") ? 1.25F : 1.7F) * emblemSize.getValue();
                Identifier emblemTex = emblemVariant.is("Контур")
                        ? Identifier.of("client", "textures/visuals/emblem_client.png")
                        : emblemVariant.is("Завитки")
                        ? Identifier.of("client", "textures/visuals/emblem_rhombus2.png")
                        : Identifier.of("client", "textures/visuals/emblem_rhombus.png");

                float angle = (float) (((Math.sin(System.currentTimeMillis() / (double) period) + 1.0D) / 2.0D) * 720.0D);
                angle = MathHelper.clamp(angle, 0F, 720F);
                int color = ColorUtil.overCol(ColorUtil.fade(0), redColor, hurtPC);
                float scale = baseScale - 0.9F * alphaPC + (0.35F - 0.35F * hurtPC);

                matrices.push();
                matrices.translate(lerpedPos.x - cameraPos.x, lerpedPos.y - cameraPos.y + target.getHeight() / 1.75, lerpedPos.z - cameraPos.z);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));
                matrices.scale(scale, scale, 1.0F);
                drawGradientQuad(immediate.getBuffer(ROMB_ESP.apply(emblemTex)), matrices.peek().getPositionMatrix(), color, color, color, color, (int) (255F * alphaPC));
                matrices.pop();
            }
        }

        immediate.draw();
    }

    private static final RenderPipeline RING_FILL_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "ring_esp_fill"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
                    .withCull(false)
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withBlend(BlendFunction.LIGHTNING)
                    .build()
    );
    private static final RenderPipeline RING_LINE_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "ring_esp_line"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.DEBUG_LINES)
                    .withCull(false)
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withBlend(BlendFunction.LIGHTNING)
                    .build()
    );
    private static final RenderLayer RING_FILL_LAYER = RenderLayer.of("ring_esp_fill",
            RenderSetup.builder(RING_FILL_PIPELINE).expectedBufferSize(1 << 16).build());
    private static final RenderLayer RING_LINE_LAYER = RenderLayer.of("ring_esp_line",
            RenderSetup.builder(RING_LINE_PIPELINE).expectedBufferSize(1 << 14).build());

    public static final RenderPipeline ROMB_ESP_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(TRANSFORMS_AND_PROJECTION_SNIPPET)
                    .withLocation("pipeline/wtex")
                    .withVertexShader("core/position_tex_color")
                    .withFragmentShader("core/position_tex_color")
                    .withSampler("Sampler0")
                    .withBlend(BlendFunction.LIGHTNING)
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
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

    private static void drawGradientQuad(VertexConsumer buffer, Matrix4f matrix, int color, int alpha) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;


    }


    private static void drawGradientQuad(VertexConsumer buffer, Matrix4f matrix,int color,int color2,int color3,int color4, int alpha) {

        buffer.vertex(matrix, -0.5f, -0.5f, 0.0f).color(ColorUtil.replAlpha(color, alpha)).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
        buffer.vertex(matrix, 0.5f, -0.5f, 0.0f).color(ColorUtil.replAlpha(color2, alpha)).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
        buffer.vertex(matrix, 0.5f, 0.5f, 0.0f).color(ColorUtil.replAlpha(color3, alpha)).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
        buffer.vertex(matrix, -0.5f, 0.5f, 0.0f).color(ColorUtil.replAlpha(color4, alpha)).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
    }

    private static final float[] CUBE_FACE_SHADE_TESP = {1.0F, 0.5F, 0.85F, 0.65F, 0.95F, 0.7F};
    private static final float[][] CUBE_VERTS_TESP = {
            {-1, 1, -1}, {1, 1, -1}, {1, 1, 1}, {-1, 1, 1},
            {-1, -1, 1}, {1, -1, 1}, {1, -1, -1}, {-1, -1, -1},
            {1, -1, -1}, {1, -1, 1}, {1, 1, 1}, {1, 1, -1},
            {-1, -1, 1}, {-1, -1, -1}, {-1, 1, -1}, {-1, 1, 1},
            {-1, -1, 1}, {1, -1, 1}, {1, 1, 1}, {-1, 1, 1},
            {1, -1, -1}, {-1, -1, -1}, {-1, 1, -1}, {1, 1, -1}
    };

    private static void drawCubeFillTESP(VertexConsumer buf, Matrix4f m, float s, int color) {
        int a = (color >>> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        for (int f = 0; f < 6; f++) {
            float sh = CUBE_FACE_SHADE_TESP[f];
            int faceColor = (a << 24) | ((int) (r * sh) << 16) | ((int) (g * sh) << 8) | (int) (b * sh);
            for (int k = 0; k < 4; k++) {
                float[] p = CUBE_VERTS_TESP[f * 4 + k];
                buf.vertex(m, p[0] * s, p[1] * s, p[2] * s).color(faceColor);
            }
        }
    }

    private static void drawCubeOutlineTESP(VertexConsumer buf, Matrix4f m, float s, int color) {
        // bottom ring
        buf.vertex(m, -s, -s, -s).color(color); buf.vertex(m,  s, -s, -s).color(color);
        buf.vertex(m,  s, -s, -s).color(color); buf.vertex(m,  s, -s,  s).color(color);
        buf.vertex(m,  s, -s,  s).color(color); buf.vertex(m, -s, -s,  s).color(color);
        buf.vertex(m, -s, -s,  s).color(color); buf.vertex(m, -s, -s, -s).color(color);
        // top ring
        buf.vertex(m, -s,  s, -s).color(color); buf.vertex(m,  s,  s, -s).color(color);
        buf.vertex(m,  s,  s, -s).color(color); buf.vertex(m,  s,  s,  s).color(color);
        buf.vertex(m,  s,  s,  s).color(color); buf.vertex(m, -s,  s,  s).color(color);
        buf.vertex(m, -s,  s,  s).color(color); buf.vertex(m, -s,  s, -s).color(color);
        // verticals
        buf.vertex(m, -s, -s, -s).color(color); buf.vertex(m, -s,  s, -s).color(color);
        buf.vertex(m,  s, -s, -s).color(color); buf.vertex(m,  s,  s, -s).color(color);
        buf.vertex(m,  s, -s,  s).color(color); buf.vertex(m,  s,  s,  s).color(color);
        buf.vertex(m, -s, -s,  s).color(color); buf.vertex(m, -s,  s,  s).color(color);
    }

    private static final int[][] CUBE_FACES_TESP = {
            {4, 5, 7, 6}, {0, 2, 3, 1}, {2, 3, 7, 6}, {0, 1, 5, 4}, {1, 3, 7, 5}, {0, 2, 6, 4}
    };
    private static final int[][] CUBE_EDGES_TESP = {
            {0, 1}, {2, 3}, {4, 5}, {6, 7}, {0, 2}, {1, 3}, {4, 6}, {5, 7}, {0, 4}, {1, 5}, {2, 6}, {3, 7}
    };

    private static void vertexTESP(VertexConsumer buf, Matrix4f m, float x, float y, float z, int color) {
        buf.vertex(m, x, y, z).color(color);
    }

    private static void fillCubeCornersTESP(float s, Vector3f up, Vector3f side, Vector3f forward, float[] cx, float[] cy, float[] cz) {
        int idx = 0;
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                for (int k = -1; k <= 1; k += 2) {
                    cx[idx] = (up.x * i + side.x * j + forward.x * k) * s;
                    cy[idx] = (up.y * i + side.y * j + forward.y * k) * s;
                    cz[idx] = (up.z * i + side.z * j + forward.z * k) * s;
                    idx++;
                }
            }
        }
    }

    private static void drawArrowTESP(VertexConsumer buf, Matrix4f m, float s, Vector3f up, Vector3f side, Vector3f forward, int color) {
        float ax = forward.x * s * 0.75F, ay = forward.y * s * 0.75F, az = forward.z * s * 0.75F;
        float px = forward.x * s * 1.5F + ax, py = forward.y * s * 1.5F + ay, pz = forward.z * s * 1.5F + az;
        float[] xs = {up.x * s, side.x * s, -up.x * s, -side.x * s};
        float[] ys = {up.y * s, side.y * s, -up.y * s, -side.y * s};
        float[] zs = {up.z * s, side.z * s, -up.z * s, -side.z * s};
        for (int i = 0; i < 4; i++) {
            int j = (i + 1) % 4;
            vertexTESP(buf, m, px, py, pz, color);
            vertexTESP(buf, m, xs[i] + ax, ys[i] + ay, zs[i] + az, color);
            vertexTESP(buf, m, xs[j] + ax, ys[j] + ay, zs[j] + az, color);
            vertexTESP(buf, m, px, py, pz, color);
        }
    }

    private static void drawDiamondTESP(VertexConsumer buf, Matrix4f m, float s, Vector3f up, Vector3f side, Vector3f forward, int color) {
        float tx = forward.x * s * 1.2F, ty = forward.y * s * 1.2F, tz = forward.z * s * 1.2F;
        float[] xs = {up.x * s * 0.6F, side.x * s * 0.6F, -up.x * s * 0.6F, -side.x * s * 0.6F};
        float[] ys = {up.y * s * 0.6F, side.y * s * 0.6F, -up.y * s * 0.6F, -side.y * s * 0.6F};
        float[] zs = {up.z * s * 0.6F, side.z * s * 0.6F, -up.z * s * 0.6F, -side.z * s * 0.6F};
        for (int i = 0; i < 4; i++) {
            int j = (i + 1) % 4;
            vertexTESP(buf, m, tx, ty, tz, color);
            vertexTESP(buf, m, xs[i], ys[i], zs[i], color);
            vertexTESP(buf, m, xs[j], ys[j], zs[j], color);
            vertexTESP(buf, m, tx, ty, tz, color);
            vertexTESP(buf, m, -tx, -ty, -tz, color);
            vertexTESP(buf, m, xs[j], ys[j], zs[j], color);
            vertexTESP(buf, m, xs[i], ys[i], zs[i], color);
            vertexTESP(buf, m, -tx, -ty, -tz, color);
        }
    }

    private static void drawCubeOrientedFillTESP(VertexConsumer buf, Matrix4f m, float s, Vector3f up, Vector3f side, Vector3f forward, int color) {
        float[] cx = new float[8], cy = new float[8], cz = new float[8];
        fillCubeCornersTESP(s, up, side, forward, cx, cy, cz);
        for (int[] face : CUBE_FACES_TESP) {
            for (int idx : face) {
                vertexTESP(buf, m, cx[idx], cy[idx], cz[idx], color);
            }
        }
    }

    private static void drawCubeOrientedOutlineTESP(VertexConsumer buf, Matrix4f m, float s, Vector3f up, Vector3f side, Vector3f forward, int color) {
        float[] cx = new float[8], cy = new float[8], cz = new float[8];
        fillCubeCornersTESP(s, up, side, forward, cx, cy, cz);
        for (int[] edge : CUBE_EDGES_TESP) {
            vertexTESP(buf, m, cx[edge[0]], cy[edge[0]], cz[edge[0]], color);
            vertexTESP(buf, m, cx[edge[1]], cy[edge[1]], cz[edge[1]], color);
        }
    }

    private static void applyCubeRotation(MatrixStack matrices, int index, float rotation) {
        if (index < 0) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
        } else if (index % 3 == 0) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation));
        } else if (index % 3 == 1) {
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
        } else {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
        }
    }

    private static float easeOutBack(float x) {
        float c1 = 1.70158F;
        float c3 = c1 + 1F;
        float x1 = x - 1F;
        return 1F + c3 * x1 * x1 * x1 + c1 * x1 * x1;
    }

    private static float steppedAngle(float v) {
        int base = (int) Math.floor(v);
        float frac = v - base;
        float ease = frac < 0.6F ? easeOutBack(frac / 0.6F) : 1.0F;
        return (base % 4 + ease) * 90.0F;
    }

    private static void bladeTriangle(VertexConsumer buf, Matrix4f m, int baseColor) {
        bladeTri(buf, m, 0F, 0F, -0.25F, 255, -0.06F, 0F, 0F, 240, -0.5F, 0F, 0.2F, 200, baseColor);
        bladeTri(buf, m, 0F, 0F, -0.25F, 255, 0F, 0F, 0F, 240, -0.06F, 0F, 0F, 240, baseColor);
        bladeTri(buf, m, 0F, 0F, -0.25F, 255, 0.5F, 0F, 0.2F, 200, 0.06F, 0F, 0F, 240, baseColor);
        bladeTri(buf, m, 0F, 0F, -0.25F, 255, 0.06F, 0F, 0F, 240, 0F, 0F, 0F, 240, baseColor);
    }

    private static void bladeTri(VertexConsumer buf, Matrix4f m, float ax, float ay, float az, int ca, float bx, float by, float bz, int cb, float cx, float cy, float cz, int cc, int baseColor) {
        vertexTESP(buf, m, ax, ay, az, ColorUtil.replAlpha(baseColor, ca));
        vertexTESP(buf, m, bx, by, bz, ColorUtil.replAlpha(baseColor, cb));
        vertexTESP(buf, m, cx, cy, cz, ColorUtil.replAlpha(baseColor, cc));
        vertexTESP(buf, m, cx, cy, cz, ColorUtil.replAlpha(baseColor, cc));
    }

    private static void drawCornerBoxes(VertexConsumer buf, Matrix4f m, float size, float bevel, int color) {
        float b = 0.3F;
        for (int i = 0; i < 8; i++) {
            float cx = (i & 1) != 0 ? size : -size;
            float cy = (i & 2) != 0 ? size : -size;
            float cz = (i & 4) != 0 ? size : -size;
            drawCornerBox(buf, m, cx, cy, cz, cx - 2F * cx * b, cy, cz, bevel, color);
            drawCornerBox(buf, m, cx, cy, cz, cx, cy - 2F * cy * b, cz, bevel, color);
            drawCornerBox(buf, m, cx, cy, cz, cx, cy, cz - 2F * cz * b, bevel, color);
        }
    }

    private static void drawCornerBox(VertexConsumer buf, Matrix4f m, float x1, float y1, float z1, float x2, float y2, float z2, float bevel, int color) {
        float v11, v12, v13, v14, v15, v16;
        if (x1 != x2) {
            v11 = 0F;
            v12 = bevel;
            v13 = 0F;
            v14 = 0F;
            v15 = 0F;
            v16 = bevel;
        } else if (y1 != y2) {
            v11 = bevel;
            v12 = 0F;
            v13 = 0F;
            v14 = 0F;
            v15 = 0F;
            v16 = bevel;
        } else {
            v11 = bevel;
            v12 = 0F;
            v13 = 0F;
            v14 = 0F;
            v15 = bevel;
            v16 = 0F;
        }

        float a17 = x1 - v11 - v14, a18 = y1 - v12 - v15, a19 = z1 - v13 - v16;
        float a20 = x1 + v11 - v14, a21 = y1 + v12 - v15, a22 = z1 + v13 - v16;
        float a23 = x1 + v11 + v14, a24 = y1 + v12 + v15, a25 = z1 + v13 + v16;
        float a26 = x1 - v11 + v14, a27 = y1 - v12 + v15, a28 = z1 - v13 + v16;
        float b29 = x2 - v11 - v14, b30 = y2 - v12 - v15, b31 = z2 - v13 - v16;
        float b32 = x2 + v11 - v14, b33 = y2 + v12 - v15, b34 = z2 + v13 - v16;
        float b35 = x2 + v11 + v14, b36 = y2 + v12 + v15, b37 = z2 + v13 + v16;
        float b38 = x2 - v11 + v14, b39 = y2 - v12 + v15, b40 = z2 - v13 + v16;

        quadTESP(buf, m, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, a27, a28, color);
        quadTESP(buf, m, b29, b30, b31, b38, b39, b40, b35, b36, b37, b32, b33, b34, color);
        quadTESP(buf, m, a17, a18, a19, b29, b30, b31, b32, b33, b34, a20, a21, a22, color);
        quadTESP(buf, m, a20, a21, a22, b32, b33, b34, b35, b36, b37, a23, a24, a25, color);
        quadTESP(buf, m, a23, a24, a25, b35, b36, b37, b38, b39, b40, a26, a27, a28, color);
        quadTESP(buf, m, a26, a27, a28, b38, b39, b40, b29, b30, b31, a17, a18, a19, color);
    }

    private static void quadTESP(VertexConsumer buf, Matrix4f m, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, int color) {
        vertexTESP(buf, m, x1, y1, z1, color);
        vertexTESP(buf, m, x2, y2, z2, color);
        vertexTESP(buf, m, x3, y3, z3, color);
        vertexTESP(buf, m, x4, y4, z4, color);
    }

    private static void drawSphereRing(VertexConsumer buf, Matrix4f m, float radius, int segments, int color) {
        for (int i = 0; i < segments; i++) {
            double a0 = (Math.PI * 2) / segments * i;
            double a1 = (Math.PI * 2) / segments * (i + 1);
            vertexTESP(buf, m, (float) (Math.cos(a0) * radius), 0F, (float) (Math.sin(a0) * radius), color);
            vertexTESP(buf, m, (float) (Math.cos(a1) * radius), 0F, (float) (Math.sin(a1) * radius), color);
        }
    }

    private static float[] ribbonPoint(float spread, float height, float spin, int m, float ox, float oz, float t) {
        float half = spin * 0.5F;
        float a = 1F + m * 0.3F;
        float b = 0.7F + m * 0.2F;
        float c = 1.2F + m * 0.25F;
        float px = spread * MathHelper.sin(half * a + t * 6F + m * 1.7F) * (0.5F + 0.5F * MathHelper.cos(t * 3F + half * 0.3F)) + ox;
        float py = height * 0.5F + spread * 0.8F * MathHelper.sin(half * b + t * 4F + m * 2.3F);
        float pz = spread * MathHelper.cos(half * c + t * 5F + m * 1.1F) * (0.5F + 0.5F * MathHelper.sin(t * 2F + half * 0.4F)) + oz;
        return new float[]{px, py, pz};
    }

    private static void drawParticleCloud(MatrixStack matrices, VertexConsumer buf, Camera camera, double bx, double by, double bz, float spin, float spread, int baseColor, float alphaPC) {
        if (spread <= 0.0001F || alphaPC <= 0.001F) {
            return;
        }
        for (int n = 0; n < 36; n++) {
            int arm = n / 12;
            int j = n % 12;
            float ang = j * ((float) (Math.PI * 2) / 12);
            float phase = spin + ang;
            float dx = spread * MathHelper.sin(phase + arm * arm);
            float dz = spread * MathHelper.cos(phase - arm * arm);
            float dy = 0.5F + 0.3F * alphaPC * MathHelper.sin(spin + ang * 2F) + 0.2F * arm;
            float size = (0.005F + j / 2000.0F) * alphaPC;
            matrices.push();
            matrices.translate(bx + dx, by + dy, bz + dz);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
            matrices.scale(size * 50F, size * 50F, size * 50F);
            drawGradientQuad(buf, matrices.peek().getPositionMatrix(), baseColor, baseColor, baseColor, baseColor, (int) (255F * alphaPC));
            matrices.pop();
        }
    }

    private static void shapeTri(VertexConsumer buf, Matrix4f m, float[] a, float[] b, float[] c, int color) {
        vertexTESP(buf, m, a[0], a[1], a[2], color);
        vertexTESP(buf, m, b[0], b[1], b[2], color);
        vertexTESP(buf, m, c[0], c[1], c[2], color);
        vertexTESP(buf, m, c[0], c[1], c[2], color);
    }

    private static void shapeLine(VertexConsumer buf, Matrix4f m, float[] a, float[] b, int color) {
        vertexTESP(buf, m, a[0], a[1], a[2], color);
        vertexTESP(buf, m, b[0], b[1], b[2], color);
    }

    private static void drawShape(VertexConsumer fillBuf, VertexConsumer lineBuf, Matrix4f m, String shape, boolean fill, int color) {
        if ("Куб".equals(shape)) {
            drawCubeShape(fillBuf, lineBuf, m, fill, color);
        } else if ("Икосаэдр".equals(shape)) {
            drawIcosahedron(fillBuf, lineBuf, m, fill, color);
        } else if ("Звезда".equals(shape)) {
            drawStarShape(fillBuf, lineBuf, m, fill, color);
        } else {
            drawOctahedron(fillBuf, lineBuf, m, fill, color);
        }
    }

    private static void drawOctahedron(VertexConsumer fb, VertexConsumer lb, Matrix4f m, boolean fill, int color) {
        float r = 0.075F;
        float[] pY = {0F, r, 0F};
        float[] nY = {0F, -r, 0F};
        float[] xp = {r, 0F, 0F};
        float[] xn = {-r, 0F, 0F};
        float[] zp = {0F, 0F, r};
        float[] zn = {0F, 0F, -r};
        if (fill) {
            shapeTri(fb, m, pY, xp, zp, color);
            shapeTri(fb, m, pY, zp, xn, color);
            shapeTri(fb, m, pY, xn, zn, color);
            shapeTri(fb, m, pY, zn, xp, color);
            shapeTri(fb, m, nY, zp, xp, color);
            shapeTri(fb, m, nY, xn, zp, color);
            shapeTri(fb, m, nY, zn, xn, color);
            shapeTri(fb, m, nY, xp, zn, color);
        } else {
            float[][] v = {pY, nY, xp, xn, zp, zn};
            int[][] edges = {{0, 2}, {0, 3}, {0, 4}, {0, 5}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {2, 4}, {4, 3}, {3, 5}, {5, 2}};
            for (int[] e : edges) {
                shapeLine(lb, m, v[e[0]], v[e[1]], color);
            }
        }
    }

    private static void drawCubeShape(VertexConsumer fb, VertexConsumer lb, Matrix4f m, boolean fill, int color) {
        float s = 0.05F;
        float[][] v = {
                {-s, -s, -s}, {s, -s, -s}, {s, s, -s}, {-s, s, -s},
                {-s, -s, s}, {s, -s, s}, {s, s, s}, {-s, s, s}
        };
        int[][] faces = {{0, 1, 2, 3}, {5, 4, 7, 6}, {1, 5, 6, 2}, {4, 0, 3, 7}, {3, 2, 6, 7}, {4, 5, 1, 0}};
        if (fill) {
            for (int[] f : faces) {
                shapeTri(fb, m, v[f[0]], v[f[1]], v[f[2]], color);
                shapeTri(fb, m, v[f[0]], v[f[2]], v[f[3]], color);
            }
        } else {
            int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 0}, {4, 5}, {5, 6}, {6, 7}, {7, 4}, {0, 4}, {1, 5}, {2, 6}, {3, 7}};
            for (int[] e : edges) {
                shapeLine(lb, m, v[e[0]], v[e[1]], color);
            }
        }
    }

    private static void drawIcosahedron(VertexConsumer fb, VertexConsumer lb, Matrix4f m, boolean fill, int color) {
        float r = 0.06F;
        float phi = (1F + (float) Math.sqrt(5.0)) / 2F;
        float a = r;
        float b = r * phi;
        float[][] v = {
                {-a, b, 0}, {a, b, 0}, {-a, -b, 0}, {a, -b, 0},
                {0, -a, b}, {0, a, b}, {0, -a, -b}, {0, a, -b},
                {b, 0, -a}, {b, 0, a}, {-b, 0, -a}, {-b, 0, a}
        };
        int[][] faces = {
                {0, 11, 5}, {0, 5, 1}, {0, 1, 7}, {0, 7, 10}, {0, 10, 11},
                {1, 5, 9}, {5, 11, 4}, {11, 10, 2}, {10, 7, 6}, {7, 1, 8},
                {3, 9, 4}, {3, 4, 2}, {3, 2, 6}, {3, 6, 8}, {3, 8, 9},
                {4, 9, 5}, {2, 4, 11}, {6, 2, 10}, {8, 6, 7}, {9, 8, 1}
        };
        if (fill) {
            for (int[] f : faces) {
                shapeTri(fb, m, v[f[0]], v[f[1]], v[f[2]], color);
            }
        } else {
            for (int[] f : faces) {
                shapeLine(lb, m, v[f[0]], v[f[1]], color);
                shapeLine(lb, m, v[f[1]], v[f[2]], color);
                shapeLine(lb, m, v[f[2]], v[f[0]], color);
            }
        }
    }

    private static void drawStarShape(VertexConsumer fb, VertexConsumer lb, Matrix4f m, boolean fill, int color) {
        float tip = 0.125F;
        float cr = 0.05F;
        float[][] tips = {
                {tip, 0, 0}, {-tip, 0, 0}, {0, tip, 0}, {0, -tip, 0}, {0, 0, tip}, {0, 0, -tip}
        };
        float[][] corners = {
                {cr, cr, cr}, {cr, cr, -cr}, {cr, -cr, cr}, {cr, -cr, -cr},
                {-cr, cr, cr}, {-cr, cr, -cr}, {-cr, -cr, cr}, {-cr, -cr, -cr}
        };
        int[][] faces = {
                {0, 0, 1}, {0, 1, 3}, {0, 3, 2}, {0, 2, 0},
                {1, 4, 5}, {1, 5, 7}, {1, 7, 6}, {1, 6, 4},
                {2, 0, 1}, {2, 1, 5}, {2, 5, 4}, {2, 4, 0},
                {3, 2, 3}, {3, 3, 7}, {3, 7, 6}, {3, 6, 2},
                {4, 0, 2}, {4, 2, 6}, {4, 6, 4}, {4, 4, 0},
                {5, 1, 3}, {5, 3, 7}, {5, 7, 5}, {5, 5, 1}
        };
        if (fill) {
            for (int[] f : faces) {
                shapeTri(fb, m, tips[f[0]], corners[f[1]], corners[f[2]], color);
            }
        } else {
            for (int ti = 0; ti < tips.length; ti++) {
                for (int ci = 0; ci < corners.length; ci++) {
                    float dx = tips[ti][0] - corners[ci][0];
                    float dy = tips[ti][1] - corners[ci][1];
                    float dz = tips[ti][2] - corners[ci][2];
                    if (Math.sqrt(dx * dx + dy * dy + dz * dz) < tip * 1.5F) {
                        shapeLine(lb, m, tips[ti], corners[ci], color);
                    }
                }
            }
        }
    }

    private static class OldCubeParticle {
        final double x;
        final double y;
        final double z;
        final double velocityY;
        final long bornAt;
        final float rotation;

        OldCubeParticle(double x, double y, double z, double velocityY, long bornAt, float rotation) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.velocityY = velocityY;
            this.bornAt = bornAt;
            this.rotation = rotation;
        }

        float alpha(long now) {
            long age = now - bornAt;
            if (age < 0L) {
                return 0F;
            }
            if (age <= 500L) {
                return age / 500.0F;
            }
            if (age >= 800L) {
                return Math.max(0F, 1.0F - (age - 800.0F) / 200.0F);
            }
            return 1F;
        }
    }

    private static class TrailPoint {
        double x;
        double y;
        double z;
        float alpha = 1F;

        TrailPoint(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private static class SparkParticle {
        double x;
        double y;
        double z;
        double vx;
        double vy;
        double vz;
        float life;
        float maxLife;

        SparkParticle(double x, double y, double z, double vx, double vy, double vz, float maxLife) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
            this.maxLife = maxLife;
            this.life = maxLife;
        }
    }

}

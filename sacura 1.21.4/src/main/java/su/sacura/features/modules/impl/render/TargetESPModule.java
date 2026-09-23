package su.sacura.features.modules.impl.render;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import su.sacura.events.render.EventRender3D;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;
import su.sacura.features.modules.settings.impl.ModeSetting;
import su.sacura.features.modules.settings.impl.SliderSetting;
import su.sacura.util.impl.math.impl.EaseInOutQuad;
import su.sacura.util.impl.render.RenderHelper;
import su.sacura.util.impl.render.providers.ColorProvider;
import su.sacura.util.impl.system.RayTraceUtil;
import su.sacura.util.type.ISetting;

@ModuleAnnotations(name = "Target ESP", category = Category.RENDER)
public class TargetESPModule extends Module {
    private final ModeSetting mode = (new ModeSetting("Режим", "Призраки", new String[] { "Маркер", "Призраки", "Кружок" })).setDescription("Красивая визуализация цели");
    private final SliderSetting ghostSize = (new SliderSetting("Размер призраков", 1.0F, 0.1F, 3.0F, 0.1F))
            .setVisible(() -> Boolean.valueOf(this.mode.is("Призраки")))
            .setDescription("Размер самих призраков");
    private final SliderSetting ghostRadius = (new SliderSetting("Радиус", 1.8F, 0.5F, 5.0F, 0.1F))
            .setVisible(() -> Boolean.valueOf(this.mode.is("Призраки")))
            .setDescription("Радиус круга призраков");
    private final SliderSetting ghostSpeed = (new SliderSetting("Скорость", 8.0F, 1.0F, 20.0F, 0.5F))
            .setVisible(() -> Boolean.valueOf(this.mode.is("Призраки")))
            .setDescription("Скорость вращения призраков");
    private final SliderSetting ghostAmplitude = (new SliderSetting("Амплитуда", 3.0F, 1.0F, 10.0F, 0.5F))
            .setVisible(() -> Boolean.valueOf(this.mode.is("Призраки")))
            .setDescription("Амплитуда покачивания (физика)");
    private final SliderSetting ghostShaking = (new SliderSetting("Тряска", 1.8F, 0.5F, 5.0F, 0.1F))
            .setVisible(() -> Boolean.valueOf(this.mode.is("Призраки")))
            .setDescription("Интенсивность тряски (физика)");
    private final SliderSetting ghostLength = (new SliderSetting("Длина хвоста", 14.0F, 5.0F, 30.0F, 1.0F))
            .setVisible(() -> Boolean.valueOf(this.mode.is("Призраки")))
            .setDescription("Количество призраков в одном хвосте");
    private final SliderSetting ghostTilt = (new SliderSetting("Наклон", 0.0F, -180.0F, 180.0F, 5.0F))
            .setVisible(() -> Boolean.valueOf(this.mode.is("Призраки")))
            .setDescription("Угол наклона всего эффекта");
    private final EaseInOutQuad animation = new EaseInOutQuad(800, 1.0D);
    private Entity lastTarget = null;
    private double scale = 0.0D;

    private static final int TRAIL_MAX = 25;
    private static final long TRAIL_LIFETIME = 350L;
    private static final int TRAIL_SEGMENTS = 32;
    private final List<TrailSample> circleTrail = new ArrayList<>();
    private double circleY = 0.0D;
    private double circleVelocityY = 0.0D;
    private java.util.UUID circleTrailOwner = null;

    private static final long MARKER_PERIOD = 1500L; // медленно; уменьши до 1200, если хочется быстрее
    private final Random markerRng = new Random();
    private float markerCurrentAngle = 0.0F;
    private float markerStartAngle = 0.0F;
    private float markerTargetAngle = 0.0F;
    private long markerCycleStartMs = 0L;
    private boolean markerNextSignPositive = true;

    // ── КЭШ РАЗМЕРОВ: точно как в оригинале ──
    // SCALE_CACHE[i] = max(0.28 * (i/100), 0.2)
    // Индекс 0 = конец хвоста (маленький), 100 = голова (большой)
    private final float[] SCALE_CACHE = new float[101];

    public TargetESPModule() {
        addSettings(new ISetting[] { (ISetting)this.mode, (ISetting)this.ghostSize, (ISetting)this.ghostRadius, (ISetting)this.ghostSpeed, (ISetting)this.ghostAmplitude, (ISetting)this.ghostShaking, (ISetting)this.ghostLength, (ISetting)this.ghostTilt });
        for (int i = 0; i <= 100; ++i) {
            this.SCALE_CACHE[i] = Math.max(0.28f * ((float)i / 100.0f), 0.2f);
        }
    }

    @Subscribe
    public void onEvent(EventRender3D e) {
        Entity currentTarget = null;
        if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            currentTarget = ((EntityHitResult)mc.crosshairTarget).getEntity();
            if (currentTarget.isInvisible())
                currentTarget = null;
        }
        if (currentTarget != null) {
            this.animation.setDirection(Direction.AxisDirection.POSITIVE);
            this.lastTarget = currentTarget;
        } else if (this.lastTarget != null) {
            this.animation.setDirection(Direction.AxisDirection.NEGATIVE);
            currentTarget = this.lastTarget;
            if (this.animation.getOutput() <= 0.01D)
                this.lastTarget = null;
        }
        if (currentTarget != null)
            if (this.mode.is("Маркер")) {
                render(currentTarget);
            } else if (this.mode.is("Призраки")) {
                renderGhosts(currentTarget);
            } else if (this.mode.is("Кружок")) {
                cicle(currentTarget, e.getMatrixStack(), e.getDeltatick().getTickDelta(true));
            }
    }

    public void onDisable() {
        this.circleTrail.clear();
        this.circleTrailOwner = null;
        this.markerCycleStartMs = 0L;
        this.markerCurrentAngle = 0.0F;
        super.onDisable();
    }

    // ============================================================
    //  ПРИЗРАКИ — точная анимация из оригинала,
    //  но остаточные следуют за главным с задержкой
    // ============================================================
    public void renderGhosts(Entity target) {
        if (target == null) return;

        Camera camera = mc.gameRenderer.getCamera();
        if (camera == null) return;

        int espLength = ((Float) this.ghostLength.get()).intValue();
        float factor = ((Float) this.ghostSpeed.get()).floatValue();
        float shaking = ((Float) this.ghostShaking.get()).floatValue();
        float amplitude = ((Float) this.ghostAmplitude.get()).floatValue();
        float radiusMultiplier = ((Float) this.ghostRadius.get()).floatValue();
        float sizeMultiplier = ((Float) this.ghostSize.get()).floatValue();
        float tilt = ((Float) this.ghostTilt.get()).floatValue();

        float hitProgress = RayTraceUtil.getHitProgress(target);
        float delta = mc.getRenderTickCounter().getTickDelta(true);
        Vec3d camPos = camera.getPos();

        double tX = interpolate(target.prevX, target.getX(), delta).doubleValue() - camPos.x;
        double tY = interpolate(target.prevY, target.getY(), delta).doubleValue() - camPos.y;
        double tZ = interpolate(target.prevZ, target.getZ(), delta).doubleValue() - camPos.z;

        float age = interpolateFloat(target.age - 1, target.age, delta);

        boolean canSee = mc.player.canSee(target);

        RenderHelper.enable(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.setShaderTexture(0, Identifier.of("sacura/images/particles/firefly.png"));
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);

        if (canSee) {
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(false);
        } else {
            RenderSystem.disableDepthTest();
        }

        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        float pitch = camera.getPitch();
        float yaw = camera.getYaw();
        float ghostAlpha = (float) this.animation.getOutput();

        float maxDelay = espLength * 1.5F;
        float globalAlphaMult = 0.6F;

        for (int j = 0; j < 3; ++j) {
            for (int i = 0; i <= espLength; ++i) {
                float offset = (float) i / (float) espLength;
                boolean isHead = (i == 0);

                // Задержка — только для остаточных
                float delay = offset * maxDelay;
                float pastAge = age - delay;

                // ── ОРБИТА: ──
                // Голова и остаточные — ОДНА скорость вращения (factor)
                // Остаточные просто смотрят в прошлое
                double radians = Math.toRadians((((float) i / 1.5f + pastAge) * factor + (float)(j * 120)) % (factor * 360.0f));

                // ── ВЕРТИКАЛЬ: ──
                // Голова и остаточные — ОДНА скорость покачивания
                // Одинаковая формула, одинаковая скорость
                double sinQuad = Math.sin(Math.toRadians(pastAge * 2.5f + (float)(i * (j + 1))) * (double) amplitude) / (double) shaking;

                MatrixStack matrices = new MatrixStack();
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw + 180.0F));
                matrices.translate(
                        tX + Math.cos(radians) * (double) target.getWidth() * (double) radiusMultiplier,
                        tY + 1.0 + sinQuad,
                        tZ + Math.sin(radians) * (double) target.getWidth() * (double) radiusMultiplier
                );
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-yaw));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));

                if (tilt != 0.0F) {
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(tilt));
                }

                Matrix4f matrix = matrices.peek().getPositionMatrix();

                // ── ЦВЕТ ──
                int baseColor = hitProgress > 0.0F
                        ? Color.RED.getRGB()
                        : ColorProvider.getColorStyle((int)(180.0F * offset));

                // ── РАЗМЕР: ──
                // Голова — больше (1.5x)
                // Остаточные — от 100% до 50%
                float sizeFade;
                if (isHead) {
                    sizeFade = 1.2F;
                } else {
                    sizeFade = 1.0F - offset * 0.5F;
                }
                float spriteScale = sizeMultiplier * sizeFade * 0.25F;

                // ── ПРОЗРАЧНОСТЬ: ──
                // Голова — максимально непрозрачная (100% * globalAlphaMult)
                // Остаточные — квадратичное затухание
                float alphaFade;
                if (isHead) {
                    alphaFade = 1.0F * globalAlphaMult * ghostAlpha;
                } else {
                    alphaFade = (1.0F - offset * offset) * globalAlphaMult * ghostAlpha;
                }
                int color = ColorProvider.applyOpacity(baseColor, alphaFade);

                buffer.vertex(matrix, -spriteScale, spriteScale, 0.0F).texture(0.0F, 1.0F).color(color);
                buffer.vertex(matrix, spriteScale, spriteScale, 0.0F).texture(1.0F, 1.0F).color(color);
                buffer.vertex(matrix, spriteScale, -spriteScale, 0.0F).texture(1.0F, 0.0F).color(color);
                buffer.vertex(matrix, -spriteScale, -spriteScale, 0.0F).texture(0.0F, 0.0F).color(color);
            }
        }

        RenderHelper.end(buffer);

        if (canSee) {
            RenderSystem.depthMask(true);
            RenderSystem.disableDepthTest();
        } else {
            RenderSystem.enableDepthTest();
        }
        RenderSystem.disableBlend();
    }

    // ============================================================
    //  КРУЖОК
    // ============================================================
    private void cicle(Entity target, MatrixStack matrices, float tickDelta) {
        Vec3d camPos = mc.gameRenderer.getCamera().getPos();
        double x = MathHelper.lerp(tickDelta, target.lastRenderX, target.getX()) - camPos.x;
        double z = MathHelper.lerp(tickDelta, target.lastRenderZ, target.getZ()) - camPos.z;
        double baseY = MathHelper.lerp(tickDelta, target.lastRenderY, target.getY()) - camPos.y;

        if (this.circleTrailOwner == null || !this.circleTrailOwner.equals(target.getUuid())) {
            this.circleTrail.clear();
            this.circleTrailOwner = target.getUuid();
            this.circleY = target.getHeight() * 0.5D;
            this.circleVelocityY = 0.0D;
        }

        long now = System.currentTimeMillis();
        double height = target.getHeight();
        double targetLocalY = (Math.sin(now / 400.0D) * 0.5D + 0.5D) * height;

        double stiffness = 130.0D;
        double damping = 11.0D;
        double dt = 1.0D / 60.0D;
        double force = (targetLocalY - this.circleY) * stiffness - this.circleVelocityY * damping;
        this.circleVelocityY += force * dt;
        this.circleY += this.circleVelocityY * dt;
        if (this.circleY < 0.0D) {
            this.circleY = 0.0D;
            if (this.circleVelocityY < 0.0D) this.circleVelocityY = 0.0D;
        }
        if (this.circleY > height) {
            this.circleY = height;
            if (this.circleVelocityY > 0.0D) this.circleVelocityY = 0.0D;
        }

        this.circleTrail.add(0, new TrailSample(this.circleY, now));
        while (this.circleTrail.size() > TRAIL_MAX) {
            this.circleTrail.remove(this.circleTrail.size() - 1);
        }
        this.circleTrail.removeIf(s -> now - s.time > TRAIL_LIFETIME);

        disableDepth();
        RenderHelper.enable(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

        int baseColor = ColorProvider.getColorStyle(360.0F);
        float r = (baseColor >> 16 & 0xFF) / 255.0F;
        float g = (baseColor >> 8 & 0xFF) / 255.0F;
        float b = (baseColor & 0xFF) / 255.0F;
        float alpha = (float)this.animation.getOutput();
        float radius = target.getWidth() * 0.8F;
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        if (this.circleTrail.size() >= 2 && alpha > 0.01F) {
            BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            int lastIdx = this.circleTrail.size() - 1;
            for (int i = 0; i < lastIdx; i++) {
                TrailSample s1 = this.circleTrail.get(i);
                TrailSample s2 = this.circleTrail.get(i + 1);
                float fade1 = (1.0F - (float)i / (float)lastIdx) * alpha;
                float fade2 = (1.0F - (float)(i + 1) / (float)lastIdx) * alpha;
                if (fade1 <= 0.005F && fade2 <= 0.005F) continue;
                double y1 = baseY + s1.y;
                double y2 = baseY + s2.y;
                for (int seg = 0; seg < TRAIL_SEGMENTS; seg++) {
                    double a1 = Math.PI * 2.0D * seg / TRAIL_SEGMENTS;
                    double a2 = Math.PI * 2.0D * (seg + 1) / TRAIL_SEGMENTS;
                    float x1 = (float)(x + radius * Math.cos(a1));
                    float z1 = (float)(z + radius * Math.sin(a1));
                    float x2 = (float)(x + radius * Math.cos(a2));
                    float z2 = (float)(z + radius * Math.sin(a2));
                    buffer.vertex(matrix, x1, (float)y1, z1).color(r, g, b, fade1);
                    buffer.vertex(matrix, x2, (float)y1, z2).color(r, g, b, fade1);
                    buffer.vertex(matrix, x2, (float)y2, z2).color(r, g, b, fade2);
                    buffer.vertex(matrix, x1, (float)y2, z1).color(r, g, b, fade2);
                }
            }
            RenderHelper.end(buffer);
        }

        endRender();
    }

    private static void disableDepth() {
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
    }

    private static void endRender() {
        RenderHelper.disable();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    // ============================================================
    //  МАРКЕР
    // ============================================================
    private void render(Entity target) {
        Camera camera = mc.gameRenderer.getCamera();
        if (camera == null)
            return;
        this.scale = this.animation.getOutput();
        if (this.scale == 0.0D)
            return;
        float delta = mc.getRenderTickCounter().getTickDelta(true);
        float hitProgress = RayTraceUtil.getHitProgress(target);
        Vec3d camPos = camera.getPos();
        double tX = interpolate(target.prevX, target.getX(), delta).doubleValue() - camPos.x;
        double tY = interpolate(target.prevY, target.getY(), delta).doubleValue() - camPos.y;
        double tZ = interpolate(target.prevZ, target.getZ(), delta).doubleValue() - camPos.z;
        MatrixStack matrices = setupMatrices(camera, target, delta, tX, tY, tZ);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        disableDepth();
        RenderHelper.enable(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        if (this.mode.is("Маркер"))
            RenderSystem.setShaderTexture(0, Identifier.of("sacura/images/target/target2.png"));
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        float alpha = (float)this.animation.getOutput();
        int[] baseColors;
        if (hitProgress > 0.0F) {
            baseColors = new int[] { Color.RED.getRGB(), ColorProvider.getColorStyle(0.0F), Color.RED.getRGB(), ColorProvider.getColorStyle(270.0F) };
        } else {
            baseColors = new int[] { ColorProvider.getColorStyle(90.0F), ColorProvider.getColorStyle(0.0F), ColorProvider.getColorStyle(180.0F), ColorProvider.getColorStyle(270.0F) };
        }
        drawQuad(matrix, applyAlphaToColors(baseColors, alpha));
        endRender();
    }

    private MatrixStack setupMatrices(Camera camera, Entity target, float delta, double tX, double tY, double tZ) {
        MatrixStack matrices = new MatrixStack();
        float pitch = camera.getPitch();
        float yaw = camera.getYaw();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw + 180.0F));
        matrices.translate(tX, tY + (target.getEyeHeight(target.getPose()) / 2.0F), tZ);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-yaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));

        long now = System.currentTimeMillis();

        // Старт нового цикла
        if (this.markerCycleStartMs == 0L || now - this.markerCycleStartMs >= MARKER_PERIOD) {
            this.markerStartAngle = this.markerCurrentAngle;

            float angleDelta = 90.0F + this.markerRng.nextFloat() * 270.0F; // 90..360
            if (!this.markerNextSignPositive) angleDelta = -angleDelta;
            this.markerTargetAngle = this.markerStartAngle + angleDelta;

            this.markerNextSignPositive = !this.markerNextSignPositive; // чередуем сторону
            this.markerCycleStartMs = now;
        }

        // Прогресс цикла + ease-in-out (velocity 0 в начале и в конце, максимум в середине)
        float t = (float)(now - this.markerCycleStartMs) / (float)MARKER_PERIOD;
        if (t > 1.0F) t = 1.0F;
        if (t < 0.0F) t = 0.0F;
        float eased = t * t * (3.0F - 2.0F * t); // smoothstep

        this.markerCurrentAngle = this.markerStartAngle
                + (this.markerTargetAngle - this.markerStartAngle) * eased;

        // Сначала сдвигаем квад так, чтобы его центр оказался в (0,0),
        // затем вращаем вокруг Z — пивот ровно по центру маркера.
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(this.markerCurrentAngle));
        matrices.translate(-0.5D, -0.5D, -0.01D);

        return matrices;
    }

    private int[] applyAlphaToColors(int[] colors, float alpha) {
        int[] out = new int[colors.length];
        for (int i = 0; i < colors.length; i++) {
            Color color = new Color(colors[i]);
            out[i] = (new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha() * alpha))).getRGB();
        }
        return out;
    }

    private void drawQuad(Matrix4f matrix, int[] colors) {
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(matrix, 0.0F, 1.0F, 0.0F).texture(0.0F, 1.0F).color(colors[0]);
        buffer.vertex(matrix, 1.0F, 1.0F, 0.0F).texture(1.0F, 1.0F).color(colors[1]);
        buffer.vertex(matrix, 1.0F, 0.0F, 0.0F).texture(1.0F, 0.0F).color(colors[2]);
        buffer.vertex(matrix, 0.0F, 0.0F, 0.0F).texture(0.0F, 0.0F).color(colors[3]);
        RenderHelper.end(buffer);
    }

    public static float interpolateFloat(float old, float value, float interpolation) {
        return old + (value - old) * interpolation;
    }

    public static Double interpolate(double old, double value, double interpolation) {
        return Double.valueOf(old + (value - old) * interpolation);
    }

    private static class TrailSample {
        final double y;
        final long time;
        TrailSample(double y, long time) {
            this.y = y;
            this.time = time;
        }
    }
}
package wtf.wyvern.client.modules.impl.render;

import wtf.wyvern.core.eventbus.EventTarget;
import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.events.impl.player.EventAttack;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.core.events.impl.render.EventRender3D;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.ColorSetting;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.base.color.ColorUtil;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "KillEffect", category = Category.RENDER, description = "Визуальный эффект при убийстве цели")
public final class KillEffect extends Module {
    public static final KillEffect INSTANCE = new KillEffect();

    private static final Identifier GLOW_TEXTURE = Identifier.of("wyvern", "icons/glow.png");

    private static final String MODE_PARTICLES = "Частицы";
    private static final String MODE_LIGHTNING = "Молния";
    private static final String MODE_GLOW = "Сияние";
    private static final String MODE_NOVA = "Сверхновая";
    private static final String COLOR_RAINBOW = "Радуга";
    private static final String COLOR_CLIENT = "Клиент";
    private static final String COLOR_CUSTOM = "Свой";

    private static final long TRACK_TARGET_MS = 2500L;
    private static final long SAME_ENTITY_COOLDOWN_MS = 900L;
    private static final int MAX_CAPTURED_VERTICES = 24000;
    private static final int FULL_BRIGHT = 0x00F000F0;

    private static final float GLOW_DURATION = 2.8f;
    private static final float GLOW_Y_OFFSET = -0.42f;
    private static final float GLOW_DISC_RADIUS = 2.15f;
    private static final float GLOW_SLICE_STEP = 0.035f;
    private static final float GLOW_APPEAR_END = 0.30f;
    private static final float GLOW_BEAM_DISAPPEAR_START = 0.34f;
    private static final float GLOW_BEAM_DISAPPEAR_END = 0.54f;
    private static final float GLOW_DISC_FADE_START = 0.40f;
    private static final float NOVA_DURATION = 2.2f;

    private final Random random = new Random();

    private final ModeSetting effectMode = new ModeSetting("Режим", MODE_LIGHTNING, MODE_GLOW);
    private final SliderSetting particles = new SliderSetting("Частицы", 280.0f, 80.0f, 650.0f, 10.0f,
            () -> effectMode.is(MODE_PARTICLES));
    private final SliderSetting particleSize = new SliderSetting("Размер частиц", 0.115f, 0.045f, 0.22f, 0.005f,
            () -> effectMode.is(MODE_PARTICLES));
    private final SliderSetting deathHold = new SliderSetting("Задержка", 200.0f, 0.0f, 500.0f, 10.0f,
            () -> effectMode.is(MODE_PARTICLES));
    private final SliderSetting evaporation = new SliderSetting("Испарение", 1050.0f, 250.0f, 2400.0f, 25.0f,
            () -> effectMode.is(MODE_PARTICLES));
    private final SliderSetting riseHeight = new SliderSetting("Подъем", 1.75f, 0.35f, 4.0f, 0.05f,
            () -> effectMode.is(MODE_PARTICLES));
    private final SliderSetting chaos = new SliderSetting("Хаос", 0.75f, 0.0f, 1.75f, 0.05f,
            () -> effectMode.is(MODE_PARTICLES));
    private final BooleanSetting throughWalls = new BooleanSetting("Через стены", false,
            () -> effectMode.is(MODE_PARTICLES));
    private final ModeSetting colorMode = new ModeSetting("Цвет",
            () -> effectMode.is(MODE_PARTICLES), COLOR_RAINBOW, COLOR_CLIENT, COLOR_CUSTOM);
    private final BooleanSetting secondColor = new BooleanSetting("Второй цвет", false,
            () -> effectMode.is(MODE_PARTICLES) && colorMode.is(COLOR_CUSTOM));
    private final ColorSetting color = new ColorSetting("Цвет 1", new ColorRGBA(255, 255, 255, 255),
            () -> effectMode.is(MODE_PARTICLES) && colorMode.is(COLOR_CUSTOM));
    private final ColorSetting color2 = new ColorSetting("Цвет 2", new ColorRGBA(70, 70, 70, 170),
            () -> effectMode.is(MODE_PARTICLES) && colorMode.is(COLOR_CUSTOM) && secondColor.isEnabled());
    private final SliderSetting glowWidth = new SliderSetting("Ширина глоу", 0.15f, 0.05f, 0.45f, 0.01f,
            () -> effectMode.is(MODE_GLOW));
    private final SliderSetting glowHeight = new SliderSetting("Высота глоу", 4.0f, 1.5f, 15.0f, 0.1f,
            () -> effectMode.is(MODE_GLOW));

    private final CopyOnWriteArrayList<KillParticle> activeParticles = new CopyOnWriteArrayList<>();
    private final List<RecentKill> recentKills = new ArrayList<>();
    private final List<GlowEffect> glowEffects = new ArrayList<>();
    private final List<NovaEffect> novaEffects = new ArrayList<>();

    private int trackedEntityId = Integer.MIN_VALUE;
    private long trackedAtMs;
    private Vec3d lastTrackedPos;
    private float lastTrackedWidth;
    private float lastTrackedHeight;
    private float lastTrackedYaw;
    private long lastStaticEffectMs;

    @FastNative
    @EventTarget
    private void onAttack(EventAttack event) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        Entity entity = event.getTarget();
        if (!(entity instanceof LivingEntity) || entity == mc.player) {
            return;
        }

        trackedEntityId = entity.getId();
        trackedAtMs = System.currentTimeMillis();
        updateLastTracked(entity);
    }

    @FastNative
    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (mc.player == null || mc.world == null) {
            clearState();
            return;
        }

        if (trackedEntityId == Integer.MIN_VALUE) {
            cleanupRecentKills(System.currentTimeMillis());
            return;
        }

        long nowMs = System.currentTimeMillis();
        if (nowMs - trackedAtMs > TRACK_TARGET_MS) {
            resetTrackedEntity();
            return;
        }

        Entity tracked = mc.world.getEntityById(trackedEntityId);
        if (tracked == null) {
            spawnFromLastKnownPosition(nowMs);
            resetTrackedEntity();
            return;
        }

        if (!(tracked instanceof LivingEntity living) || tracked == mc.player) {
            resetTrackedEntity();
            return;
        }

        if (isDeadOrRemoved(living)) {
            if (!isRecentlySpawned(tracked.getId(), nowMs)) {
                recentKills.add(new RecentKill(tracked.getId(), nowMs));
                spawnFromEntity(living, nowMs);
            }
            resetTrackedEntity();
            return;
        }

        updateLastTracked(tracked);
    }

    @EventTarget
    private void onRender3D(EventRender3D event) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        renderParticles(event);
        renderGlowEffects(event);
        renderNovaEffects(event);
    }

    @FastNative
    private void updateLastTracked(Entity entity) {
        lastTrackedPos = entity.getPos();
        lastTrackedWidth = entity.getWidth();
        lastTrackedHeight = entity.getHeight();
        lastTrackedYaw = entity.getYaw();
    }

    @FastNative
    private void spawnFromLastKnownPosition(long nowMs) {
        if (lastTrackedPos == null || isRecentlySpawned(trackedEntityId, nowMs)) {
            return;
        }

        recentKills.add(new RecentKill(trackedEntityId, nowMs));
        if (effectMode.is(MODE_PARTICLES)) {
            boolean playerLike = lastTrackedWidth <= 0.7f && lastTrackedHeight > 1.4f;
            ParticleModel model = makeFallbackModel(
                    lastTrackedPos,
                    lastTrackedWidth,
                    lastTrackedHeight,
                    lastTrackedYaw,
                    playerLike
            );
            spawnParticles(model, nowMs);
        } else {
            spawnStaticEffect(lastTrackedPos);
        }
    }

    @FastNative
    private void spawnFromEntity(LivingEntity entity, long nowMs) {
        if (effectMode.is(MODE_PARTICLES)) {
            ParticleModel model = captureModel(entity, mc.getRenderTickCounter().getTickDelta(false));
            if (model.isEmpty()) {
                model = makeFallbackModel(
                        entity.getPos(),
                        entity.getWidth(),
                        entity.getHeight(),
                        entity.getYaw(),
                        entity instanceof PlayerEntity
                );
            }
            spawnParticles(model, nowMs);
            return;
        }

        spawnStaticEffect(entity.getPos());
    }

    @FastNative
    private ParticleModel captureModel(LivingEntity entity, float partialTicks) {
        List<Vec3d> capturedVertices = EntityModelCapture.capture(entity, partialTicks, MAX_CAPTURED_VERTICES);
        return ParticleModel.fromVertices(capturedVertices);
    }

    @FastNative
    private void spawnParticles(ParticleModel model, long nowMs) {
        if (model.isEmpty()) {
            return;
        }

        int count = Math.max(1, Math.round(particles.getCurrent()));
        float chaosValue = chaos.getCurrent();

        for (int i = 0; i < count; ++i) {
            Vec3d worldPoint = model.randomPoint(random);
            Vec3d away = worldPoint.subtract(model.center);
            if (away.length() < 1.0E-5) {
                away = new Vec3d(
                        random.nextDouble() - 0.5,
                        random.nextDouble() * 0.4,
                        random.nextDouble() - 0.5
                );
            }

            Vec3d drift = away.normalize()
                    .multiply((0.32 + random.nextDouble() * 0.58) * chaosValue)
                    .add(
                            (random.nextDouble() - 0.5) * 0.34 * chaosValue,
                            random.nextDouble() * 0.38 * chaosValue,
                            (random.nextDouble() - 0.5) * 0.34 * chaosValue
                    );

            activeParticles.add(new KillParticle(
                    worldPoint,
                    drift,
                    nowMs,
                    random.nextInt(1440) * (random.nextBoolean() ? 1 : -1),
                    random.nextFloat() * 360.0f,
                    (random.nextFloat() - 0.5f) * 210.0f,
                    random.nextFloat() * (float) Math.PI * 2.0f,
                    0.72f + random.nextFloat() * 0.65f
            ));
        }
    }

    private void renderParticles(EventRender3D event) {
        if (activeParticles.isEmpty()) {
            return;
        }

        long nowMs = System.currentTimeMillis();
        long holdMs = Math.max(0L, Math.round(deathHold.getCurrent()));
        long fadeMs = Math.max(1L, Math.round(evaporation.getCurrent()));
        long maxLifeMs = holdMs + fadeMs;

        activeParticles.removeIf(particle -> particle.isDead(nowMs, maxLifeMs));
        if (activeParticles.isEmpty()) {
            return;
        }

        Camera camera = mc.getEntityRenderDispatcher().camera;
        if (camera == null) {
            return;
        }

        Vec3d cameraPos = camera.getPos();
        MatrixStack matrices = event.getMatrix();

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
        if (throughWalls.isEnabled()) {
            RenderSystem.disableDepthTest();
        } else {
            RenderSystem.enableDepthTest();
        }
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture(0, GLOW_TEXTURE);

        BufferBuilder buffer = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        for (KillParticle particle : activeParticles) {
            float alpha = particle.getAlpha(nowMs, holdMs, fadeMs);
            if (alpha <= 1.0f / 255.0f) {
                continue;
            }

            Vec3d pos = particle.getPosition(nowMs, holdMs, fadeMs, chaos.getCurrent(), riseHeight.getCurrent());
            float size = particle.getSize(nowMs, holdMs, fadeMs, particleSize.getCurrent());
            int argb = colorWithAlpha(getParticleColor(particle.getColorSeed()), alpha);

            emitBillboard(matrices, buffer, camera, pos, cameraPos, size * 0.5f, particle.getRotation(nowMs), argb);
        }

        drawBuffer(buffer);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    @FastNative
    private int getParticleColor(int seed) {
        if (colorMode.is(COLOR_RAINBOW)) {
            float hue = (float) Math.floorMod(System.currentTimeMillis() / 12L + seed, 360L) / 360.0f;
            return Color.HSBtoRGB(hue, 1.0f, 1.0f) | 0xFF000000;
        }

        int firstColor;
        int secondColorValue;
        if (colorMode.is(COLOR_CLIENT)) {
            firstColor = colorWithAlpha(clientColor(), 1.0f);
            secondColorValue = firstColor;
        } else {
            firstColor = color.getIntColor();
            secondColorValue = secondColor.isEnabled() ? color2.getIntColor() : color.getIntColor();
        }

        int angle = (int) Math.floorMod(System.currentTimeMillis() / 8L + seed, 360L);
        float progress = angle > 180 ? (float) (360 - angle) / 180.0f : (float) angle / 180.0f;
        return lerpColor(firstColor, secondColorValue, progress);
    }

    private void emitBillboard(MatrixStack matrices, BufferBuilder buffer, Camera camera, Vec3d worldPos,
                              Vec3d cameraPos, float halfSize, float angleDeg, int argb) {
        matrices.push();
        matrices.translate(worldPos.x - cameraPos.x, worldPos.y - cameraPos.y, worldPos.z - cameraPos.z);
        matrices.multiply(camera.getRotation());
        if (angleDeg != 0.0f) {
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angleDeg));
        }

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        buffer.vertex(matrix, -halfSize, -halfSize, 0.0f).texture(1.0f, 1.0f).color(argb);
        buffer.vertex(matrix, -halfSize, halfSize, 0.0f).texture(1.0f, 0.0f).color(argb);
        buffer.vertex(matrix, halfSize, halfSize, 0.0f).texture(0.0f, 0.0f).color(argb);
        buffer.vertex(matrix, halfSize, -halfSize, 0.0f).texture(0.0f, 1.0f).color(argb);

        matrices.pop();
    }

    @FastNative
    private ParticleModel makeFallbackModel(Vec3d origin, float width, float height, float yaw, boolean playerLike) {
        if (!(width > 0.0f) || !(height > 0.0f)) {
            return ParticleModel.empty();
        }

        ArrayList<Vec3d> vertices = new ArrayList<>();
        if (playerLike) {
            addBox(vertices, origin, yaw, -0.25, 1.22, -0.25, 0.25, 1.72, 0.25);
            addBox(vertices, origin, yaw, -0.25, 0.72, -0.125, 0.25, 1.22, 0.125);
            addBox(vertices, origin, yaw, -0.43, 0.72, -0.105, -0.25, 1.22, 0.105);
            addBox(vertices, origin, yaw, 0.25, 0.72, -0.105, 0.43, 1.22, 0.105);
            addBox(vertices, origin, yaw, -0.24, 0.0, -0.105, -0.02, 0.72, 0.105);
            addBox(vertices, origin, yaw, 0.02, 0.0, -0.105, 0.24, 0.72, 0.105);
        } else {
            double half = Math.max(0.05, width * 0.5);
            double entityHeight = Math.max(0.1, height);
            addBox(vertices, origin, yaw, -half, 0.0, -half, half, entityHeight, half);
        }

        return ParticleModel.fromVertices(vertices);
    }

    @FastNative
    private void addBox(List<Vec3d> vertices, Vec3d origin, float yaw,
                        double minX, double minY, double minZ,
                        double maxX, double maxY, double maxZ) {
        Vec3d nnn = transform(origin, yaw, minX, minY, minZ);
        Vec3d pnn = transform(origin, yaw, maxX, minY, minZ);
        Vec3d ppn = transform(origin, yaw, maxX, maxY, minZ);
        Vec3d npn = transform(origin, yaw, minX, maxY, minZ);
        Vec3d nnp = transform(origin, yaw, minX, minY, maxZ);
        Vec3d pnp = transform(origin, yaw, maxX, minY, maxZ);
        Vec3d ppp = transform(origin, yaw, maxX, maxY, maxZ);
        Vec3d npp = transform(origin, yaw, minX, maxY, maxZ);

        addQuad(vertices, nnn, pnn, ppn, npn);
        addQuad(vertices, pnp, nnp, npp, ppp);
        addQuad(vertices, nnp, nnn, npn, npp);
        addQuad(vertices, pnn, pnp, ppp, ppn);
        addQuad(vertices, npn, ppn, ppp, npp);
        addQuad(vertices, nnp, pnp, pnn, nnn);
    }

    @FastNative
    private void addQuad(List<Vec3d> vertices, Vec3d a, Vec3d b, Vec3d c, Vec3d d) {
        vertices.add(a);
        vertices.add(b);
        vertices.add(c);
        vertices.add(d);
    }

    @FastNative
    private Vec3d transform(Vec3d origin, float yaw, double x, double y, double z) {
        double radians = Math.toRadians(-yaw);
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);
        double rotatedX = x * cos - z * sin;
        double rotatedZ = x * sin + z * cos;
        return origin.add(rotatedX, y, rotatedZ);
    }

    @FastNative
    private void spawnStaticEffect(Vec3d pos) {
        if (pos == null || !Double.isFinite(pos.x) || !Double.isFinite(pos.y) || !Double.isFinite(pos.z)) {
            return;
        }

        long now = System.currentTimeMillis();
        if (now - lastStaticEffectMs < 250L) {
            return;
        }
        lastStaticEffectMs = now;

        if (effectMode.is(MODE_LIGHTNING)) {
            spawnLightning(pos.x, pos.y, pos.z);
        } else if (effectMode.is(MODE_GLOW)) {
            glowEffects.add(new GlowEffect(pos, now));
        } else if (effectMode.is(MODE_NOVA)) {
            novaEffects.add(new NovaEffect(pos, now, random.nextFloat() * MathHelper.TAU));
        }
    }

    /**
     * A compact celestial burst: an expanding ground shockwave, a floating core
     * and small theme-colored stars that spiral away from the defeated player.
     */
    private void renderNovaEffects(EventRender3D event) {
        if (novaEffects.isEmpty()) return;

        Camera camera = mc.getEntityRenderDispatcher().camera;
        if (camera == null) return;

        long now = System.currentTimeMillis();
        Vec3d cameraPos = camera.getPos();
        MatrixStack matrices = event.getMatrix();
        int primary = clientColor();
        int secondary = Wyvern.INSTANCE.getThemeManager().getCurrentTheme().getSecondColor().getRGB();

        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture(0, GLOW_TEXTURE);

        BufferBuilder buffer = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        Iterator<NovaEffect> iterator = novaEffects.iterator();
        while (iterator.hasNext()) {
            NovaEffect effect = iterator.next();
            float progress = (now - effect.startTime) / (NOVA_DURATION * 1000.0f);
            if (progress >= 1.0f) {
                iterator.remove();
                continue;
            }

            float appear = smoothStep(progress / 0.12f);
            float fade = 1.0f - smoothStep((progress - 0.34f) / 0.66f);
            float expansion = easeOutCubic(progress);
            float pulse = 0.90f + 0.10f * MathHelper.sin(progress * 42.0f);
            float alpha = appear * fade;
            Vec3d core = effect.position.add(0.0, 0.85 + expansion * 0.65, 0.0);

            emitBillboard(matrices, buffer, camera, core, cameraPos,
                    0.92f * pulse, progress * 80.0f, colorWithAlpha(primary, alpha * 0.36f));
            emitBillboard(matrices, buffer, camera, core, cameraPos,
                    0.48f * pulse, -progress * 120.0f, colorWithAlpha(secondary, alpha * 0.78f));
            emitBillboard(matrices, buffer, camera, core, cameraPos,
                    0.19f, 0.0f, colorWithAlpha(0xFFFFFFFF, alpha));

            float shockAlpha = alpha * (1.0f - progress) * 0.62f;
            drawFlatGlowDisc(matrices, buffer, cameraPos, effect.position.add(0.0, 0.035, 0.0),
                    0.45f + expansion * 3.2f, primary, shockAlpha);
            drawFlatGlowDisc(matrices, buffer, cameraPos, effect.position.add(0.0, 0.06, 0.0),
                    0.18f + expansion * 1.65f, secondary, shockAlpha * 0.82f);

            for (int i = 0; i < 18; i++) {
                float direction = i * (MathHelper.TAU / 18.0f) + effect.phase;
                float twist = direction + expansion * (i % 2 == 0 ? 1.7f : -1.35f);
                double radius = 0.18 + expansion * (1.55 + (i % 4) * 0.27);
                double x = Math.cos(twist) * radius;
                double z = Math.sin(twist) * radius;
                double y = 0.28 + expansion * (0.65 + (i % 5) * 0.18)
                        + Math.sin(direction * 3.0f + progress * 13.0f) * 0.12;
                float sparkSize = (0.07f + (i % 3) * 0.018f) * (0.7f + alpha * 0.5f);
                int sparkColor = (i & 1) == 0 ? primary : secondary;
                emitBillboard(matrices, buffer, camera, effect.position.add(x, y, z), cameraPos,
                        sparkSize, progress * 150.0f + i * 23.0f, colorWithAlpha(sparkColor, alpha * 0.88f));
            }
        }

        drawBuffer(buffer);
        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private void renderGlowEffects(EventRender3D event) {
        if (glowEffects.isEmpty()) {
            return;
        }

        Camera camera = mc.getEntityRenderDispatcher().camera;
        if (camera == null) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        Vec3d cameraPos = camera.getPos();
        MatrixStack matrices = event.getMatrix();
        int themeColor = clientColor();

        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture(0, GLOW_TEXTURE);

        BufferBuilder buffer = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        Iterator<GlowEffect> effectIterator = glowEffects.iterator();
        while (effectIterator.hasNext()) {
            GlowEffect effect = effectIterator.next();
            float progress = (currentTime - effect.startTime) / (GLOW_DURATION * 1000f);
            if (progress >= 1.0f) {
                effectIterator.remove();
                continue;
            }
            buildGlowPillar(matrices, buffer, camera, cameraPos, effect.position, progress, themeColor);
        }

        drawBuffer(buffer);

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private void buildGlowPillar(MatrixStack matrices, BufferBuilder buffer, Camera camera, Vec3d cameraPos,
                                 Vec3d basePos, float progress, int themeColor) {
        Vec3d effectPos = basePos.add(0, GLOW_Y_OFFSET, 0);

        float appear = getAppearProgress(progress);
        float beamDisappear = getBeamDisappear(progress);
        float beamFactor = appear * (1f - beamDisappear);
        float discFade = getDiscGlowFade(progress);

        drawFlatGlowDisc(matrices, buffer, cameraPos, effectPos, GLOW_DISC_RADIUS, themeColor, appear * discFade);

        if (beamFactor <= 0.005f) {
            return;
        }

        float glowH = glowHeight.getCurrent();
        float beamHeight = glowH * beamFactor;
        float maxHeight = glowH * appear;
        float baseRadius = glowWidth.getCurrent();
        float softEdge = GLOW_SLICE_STEP * 0.55f;
        float tipZone = 0.14f;

        for (float y = 0f; y <= maxHeight; y += GLOW_SLICE_STEP) {
            float over = y - beamHeight;
            if (over > softEdge) {
                continue;
            }
            float edgeFade = over > 0f
                    ? (float) Math.pow(1f - MathHelper.clamp(over / softEdge, 0f, 1f), 2.2f)
                    : 1f;

            float heightT = beamHeight > 0.01f ? y / beamHeight : 0f;
            float tipFade = 1f;
            float tipRadius = 1f;
            if (heightT > 1f - tipZone) {
                float tipT = (heightT - (1f - tipZone)) / tipZone;
                tipFade = (float) Math.pow(1f - tipT, 2.6f);
                tipRadius = 0.28f + 0.72f * tipFade;
            }

            float sliceAlpha = appear * (1f - beamDisappear) * edgeFade * tipFade;
            if (sliceAlpha <= 0.005f) {
                continue;
            }

            float radius = baseRadius * tipRadius;
            Vec3d pos = effectPos.add(0, y, 0);
            emitBillboard(matrices, buffer, camera, pos, cameraPos, radius * 1.95f * 0.5f, 0f, colorWithAlpha(themeColor, sliceAlpha * 0.2f));
            emitBillboard(matrices, buffer, camera, pos, cameraPos, radius * 1.05f * 0.5f, 0f, colorWithAlpha(themeColor, sliceAlpha * 0.44f));
            emitBillboard(matrices, buffer, camera, pos, cameraPos, radius * 0.5f * 0.5f, 0f, colorWithAlpha(themeColor, sliceAlpha * 0.8f));
        }
    }

    private void drawFlatGlowDisc(MatrixStack matrices, BufferBuilder buffer, Vec3d cameraPos, Vec3d basePos,
                                  float radius, int themeColor, float alpha) {
        if (alpha <= 0.003f) {
            return;
        }

        matrices.push();
        matrices.translate(basePos.x - cameraPos.x, basePos.y - cameraPos.y + 0.03, basePos.z - cameraPos.z);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));

        emitFlatQuad(matrices, buffer, radius * 1.35f, colorWithAlpha(themeColor, alpha * 0.28f * alpha));
        emitFlatQuad(matrices, buffer, radius, colorWithAlpha(themeColor, alpha * 0.55f * alpha));
        emitFlatQuad(matrices, buffer, radius * 0.55f, colorWithAlpha(themeColor, alpha));

        matrices.pop();
    }

    private void emitFlatQuad(MatrixStack matrices, BufferBuilder buffer, float half, int argb) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        buffer.vertex(matrix, -half, -half, 0.0f).texture(0.0f, 0.0f).color(argb);
        buffer.vertex(matrix, -half, half, 0.0f).texture(0.0f, 1.0f).color(argb);
        buffer.vertex(matrix, half, half, 0.0f).texture(1.0f, 1.0f).color(argb);
        buffer.vertex(matrix, half, -half, 0.0f).texture(1.0f, 0.0f).color(argb);
    }

    @FastNative
    private void spawnLightning(double x, double y, double z) {
        if (!(mc.world instanceof ClientWorld world)) {
            return;
        }

        LightningEntity bolt = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
        bolt.setCosmetic(false);
        bolt.setPosition(x, y, z);

        try {
            world.addEntity(bolt);
        } catch (Throwable ignored) {
        }

        mc.getSoundManager().play(PositionedSoundInstance.master(
                SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
                0.5F,
                0.8F + random.nextFloat() * 0.2F
        ));

        mc.getSoundManager().play(PositionedSoundInstance.master(
                SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT,
                0.5F,
                0.8F + random.nextFloat() * 0.2F
        ));
    }

    private void drawBuffer(BufferBuilder buffer) {
        BuiltBuffer built = buffer.endNullable();
        if (built != null) {
            BufferRenderer.drawWithGlobalProgram(built);
        }
    }

    @FastNative
    private int clientColor() {
        return Wyvern.INSTANCE.getThemeManager().getClientColor(0).getRGB();
    }

    @FastNative
    private static int colorWithAlpha(int color, float alpha01) {
        int a = MathHelper.clamp((int) (alpha01 * 255.0f), 0, 255);
        return (a << 24) | (color & 0x00FFFFFF);
    }

    @FastNative
    private static int lerpColor(int first, int second, float progress) {
        progress = MathHelper.clamp(progress, 0.0f, 1.0f);
        int a = (int) (ColorUtil.alpha(first) + (ColorUtil.alpha(second) - ColorUtil.alpha(first)) * progress);
        int r = (int) (ColorUtil.red(first) + (ColorUtil.red(second) - ColorUtil.red(first)) * progress);
        int g = (int) (ColorUtil.green(first) + (ColorUtil.green(second) - ColorUtil.green(first)) * progress);
        int b = (int) (ColorUtil.blue(first) + (ColorUtil.blue(second) - ColorUtil.blue(first)) * progress);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    @FastNative
    private boolean isDeadOrRemoved(LivingEntity entity) {
        return entity == null || !entity.isAlive() || entity.isRemoved() || entity.getHealth() <= 0.0f || entity.deathTime > 0;
    }

    @FastNative
    private boolean isRecentlySpawned(int entityId, long nowMs) {
        Iterator<RecentKill> iterator = recentKills.iterator();
        while (iterator.hasNext()) {
            RecentKill stamp = iterator.next();
            if (nowMs - stamp.timeMs > SAME_ENTITY_COOLDOWN_MS) {
                iterator.remove();
                continue;
            }
            if (stamp.entityId == entityId) {
                return true;
            }
        }
        return false;
    }

    private void cleanupRecentKills(long nowMs) {
        recentKills.removeIf(stamp -> nowMs - stamp.timeMs > SAME_ENTITY_COOLDOWN_MS);
    }

    @FastNative
    private void resetTrackedEntity() {
        trackedEntityId = Integer.MIN_VALUE;
        trackedAtMs = 0L;
        lastTrackedPos = null;
        lastTrackedWidth = 0.0f;
        lastTrackedHeight = 0.0f;
        lastTrackedYaw = 0.0f;
    }

    @FastNative
    private void clearState() {
        activeParticles.clear();
        recentKills.clear();
        glowEffects.clear();
        novaEffects.clear();
        resetTrackedEntity();
    }

    @FastNative
    @Override
    public void onDisable() {
        clearState();
        super.onDisable();
    }

    @FastNative
    private static float smoothStep(float value) {
        float t = MathHelper.clamp(value, 0.0f, 1.0f);
        return t * t * (3.0f - 2.0f * t);
    }

    @FastNative
    private static float easeOutCubic(float value) {
        float inv = 1.0f - MathHelper.clamp(value, 0.0f, 1.0f);
        return 1.0f - inv * inv * inv;
    }

    @FastNative
    private static float easeSmooth(float t) {
        t = MathHelper.clamp(t, 0f, 1f);
        float inv = 1f - t;
        return 1f - inv * inv * inv;
    }

    @FastNative
    private static float smootherstep(float t) {
        t = MathHelper.clamp(t, 0f, 1f);
        return t * t * t * (t * (t * 6f - 15f) + 10f);
    }

    @FastNative
    private static float getDiscGlowFade(float progress) {
        if (progress < GLOW_DISC_FADE_START) {
            return 1f;
        }
        float gone = MathHelper.clamp((progress - GLOW_DISC_FADE_START) / (1f - GLOW_DISC_FADE_START), 0f, 1f);
        float remain = 1f - gone;
        return remain * remain * remain * remain * remain;
    }

    @FastNative
    private static float getAppearProgress(float progress) {
        if (progress >= GLOW_APPEAR_END) {
            return 1f;
        }
        return easeSmooth(progress / GLOW_APPEAR_END);
    }

    @FastNative
    private static float getBeamDisappear(float progress) {
        if (progress <= GLOW_BEAM_DISAPPEAR_START) {
            return 0f;
        }
        if (progress >= GLOW_BEAM_DISAPPEAR_END) {
            return 1f;
        }
        float t = (progress - GLOW_BEAM_DISAPPEAR_START) / (GLOW_BEAM_DISAPPEAR_END - GLOW_BEAM_DISAPPEAR_START);
        return smootherstep(t);
    }

    @FastNative
    private static double triangleArea(Vec3d a, Vec3d b, Vec3d c) {
        Vec3d ab = b.subtract(a);
        Vec3d ac = c.subtract(a);
        double crossX = ab.y * ac.z - ab.z * ac.y;
        double crossY = ab.z * ac.x - ab.x * ac.z;
        double crossZ = ab.x * ac.y - ab.y * ac.x;
        return Math.sqrt(crossX * crossX + crossY * crossY + crossZ * crossZ) * 0.5;
    }

    @FastNative
    private static Vec3d randomPointInTriangle(Vec3d a, Vec3d b, Vec3d c, Random random) {
        double u = random.nextDouble();
        double v = random.nextDouble();
        if (u + v > 1.0) {
            u = 1.0 - u;
            v = 1.0 - v;
        }
        return a.add(b.subtract(a).multiply(u)).add(c.subtract(a).multiply(v));
    }

    @FastNative
    private static boolean isSaneCoordinate(double value) {
        return Double.isFinite(value) && Math.abs(value) < 3.0E7;
    }

    private final class KillParticle {
        private final Vec3d anchor;
        private final Vec3d drift;
        private final long bornMs;
        private final int colorSeed;
        private final float startAngle;
        private final float spin;
        private final float phase;
        private final float sizeMultiplier;

        private KillParticle(Vec3d anchor, Vec3d drift, long bornMs, int colorSeed,
                             float startAngle, float spin, float phase, float sizeMultiplier) {
            this.anchor = anchor;
            this.drift = drift;
            this.bornMs = bornMs;
            this.colorSeed = colorSeed;
            this.startAngle = startAngle;
            this.spin = spin;
            this.phase = phase;
            this.sizeMultiplier = sizeMultiplier;
        }

        private boolean isDead(long nowMs, long maxLifeMs) {
            return nowMs - bornMs >= maxLifeMs;
        }

        private Vec3d getPosition(long nowMs, long holdMs, long fadeMs, float chaosValue, float riseValue) {
            float evaporationProgress = getEvaporation(nowMs, holdMs, fadeMs);
            if (evaporationProgress <= 0.0f) {
                return anchor;
            }

            float eased = easeOutCubic(evaporationProgress);
            double wave = Math.sin((nowMs - bornMs) * 0.012 + phase) * 0.075 * chaosValue * evaporationProgress;
            double sideWave = Math.cos((nowMs - bornMs) * 0.009 + phase * 1.37) * 0.055 * chaosValue * evaporationProgress;
            double rise = riseValue * eased * (0.75 + sizeMultiplier * 0.35);
            return anchor.add(drift.multiply(eased)).add(wave, rise, sideWave);
        }

        private float getAlpha(long nowMs, long holdMs, long fadeMs) {
            long ageMs = nowMs - bornMs;
            float fadeIn = MathHelper.clamp((float) ageMs / 80.0f, 0.0f, 1.0f);
            float evaporationProgress = getEvaporation(nowMs, holdMs, fadeMs);
            if (evaporationProgress <= 0.0f) {
                return fadeIn;
            }
            return fadeIn * (1.0f - smoothStep(evaporationProgress));
        }

        private float getSize(long nowMs, long holdMs, long fadeMs, float baseSize) {
            float pulse = 1.0f + MathHelper.sin((float) (nowMs - bornMs) * 0.018f + phase) * 0.08f;
            float dissolveScale = 1.0f - smoothStep(getEvaporation(nowMs, holdMs, fadeMs)) * 0.45f;
            return baseSize * sizeMultiplier * pulse * dissolveScale;
        }

        private float getRotation(long nowMs) {
            return startAngle + (float) (nowMs - bornMs) / 1000.0f * spin;
        }

        private float getEvaporation(long nowMs, long holdMs, long fadeMs) {
            return MathHelper.clamp((float) (nowMs - bornMs - holdMs) / (float) fadeMs, 0.0f, 1.0f);
        }

        private int getColorSeed() {
            return colorSeed;
        }
    }

    private static final class RecentKill {
        private final int entityId;
        private final long timeMs;

        private RecentKill(int entityId, long timeMs) {
            this.entityId = entityId;
            this.timeMs = timeMs;
        }
    }

    private static final class GlowEffect {
        private final Vec3d position;
        private final long startTime;

        private GlowEffect(Vec3d position, long startTime) {
            this.position = position;
            this.startTime = startTime;
        }
    }

    private static final class NovaEffect {
        private final Vec3d position;
        private final long startTime;
        private final float phase;

        private NovaEffect(Vec3d position, long startTime, float phase) {
            this.position = position;
            this.startTime = startTime;
            this.phase = phase;
        }
    }

    private static final class ParticleModel {
        private final List<Vec3d> vertices;
        private final List<Surface> surfaces;
        private final double totalArea;
        private final Vec3d center;

        private ParticleModel(List<Vec3d> vertices, List<Surface> surfaces, double totalArea, Vec3d center) {
            this.vertices = vertices;
            this.surfaces = surfaces;
            this.totalArea = totalArea;
            this.center = center;
        }

        private static ParticleModel empty() {
            return new ParticleModel(new ArrayList<>(), new ArrayList<>(), 0.0, Vec3d.ZERO);
        }

        private static ParticleModel fromVertices(List<Vec3d> capturedVertices) {
            if (capturedVertices == null || capturedVertices.size() < 8) {
                return empty();
            }

            ArrayList<Vec3d> vertices = new ArrayList<>(capturedVertices.size());
            for (Vec3d vertex : capturedVertices) {
                if (vertex == null
                        || !isSaneCoordinate(vertex.x)
                        || !isSaneCoordinate(vertex.y)
                        || !isSaneCoordinate(vertex.z)) {
                    continue;
                }
                vertices.add(vertex);
            }
            if (vertices.size() < 8) {
                return empty();
            }

            Vec3d min = vertices.get(0);
            Vec3d max = vertices.get(0);
            for (Vec3d vertex : vertices) {
                min = new Vec3d(Math.min(min.x, vertex.x), Math.min(min.y, vertex.y), Math.min(min.z, vertex.z));
                max = new Vec3d(Math.max(max.x, vertex.x), Math.max(max.y, vertex.y), Math.max(max.z, vertex.z));
            }

            Vec3d center = new Vec3d(
                    (min.x + max.x) * 0.5,
                    (min.y + max.y) * 0.5,
                    (min.z + max.z) * 0.5
            );

            ArrayList<Surface> surfaces = new ArrayList<>();
            double totalArea = 0.0;
            for (int i = 0; i + 3 < vertices.size(); i += 4) {
                Surface surface = new Surface(vertices.get(i), vertices.get(i + 1), vertices.get(i + 2), vertices.get(i + 3));
                if (surface.area <= 1.0E-7) {
                    continue;
                }
                surfaces.add(surface);
                totalArea += surface.area;
            }

            return new ParticleModel(vertices, surfaces, totalArea, center);
        }

        private boolean isEmpty() {
            return vertices.isEmpty();
        }

        private Vec3d randomPoint(Random random) {
            if (!surfaces.isEmpty() && totalArea > 1.0E-7) {
                double cursor = random.nextDouble() * totalArea;
                for (Surface surface : surfaces) {
                    cursor -= surface.area;
                    if (cursor <= 0.0) {
                        return surface.randomPoint(random);
                    }
                }
                return surfaces.get(surfaces.size() - 1).randomPoint(random);
            }

            Vec3d vertex = vertices.get(random.nextInt(vertices.size()));
            return vertex.add(
                    (random.nextDouble() - 0.5) * 0.025,
                    (random.nextDouble() - 0.5) * 0.025,
                    (random.nextDouble() - 0.5) * 0.025
            );
        }
    }

    private static final class Surface {
        private final Vec3d a;
        private final Vec3d b;
        private final Vec3d c;
        private final Vec3d d;
        private final double firstTriangleArea;
        private final double area;

        private Surface(Vec3d a, Vec3d b, Vec3d c, Vec3d d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.firstTriangleArea = triangleArea(a, b, c);
            this.area = firstTriangleArea + triangleArea(a, c, d);
        }

        private Vec3d randomPoint(Random random) {
            if (random.nextDouble() * area <= firstTriangleArea) {
                return randomPointInTriangle(a, b, c, random);
            }
            return randomPointInTriangle(a, c, d, random);
        }
    }

    private static final class EntityModelCapture {
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static List<Vec3d> capture(LivingEntity entity, float partialTicks, int maxVertices) {
            ArrayList<Vec3d> empty = new ArrayList<>();
            if (entity == null || maxVertices <= 0) {
                return empty;
            }

            EntityRenderDispatcher dispatcher = INSTANCE.mc.getEntityRenderDispatcher();
            if (dispatcher == null) {
                return empty;
            }

            try {
                EntityRenderer renderer = dispatcher.getRenderer(entity);
                if (renderer == null) {
                    return empty;
                }

                EntityRenderState state = renderer.getAndUpdateRenderState(entity, partialTicks);
                CapturingVertexConsumerProvider buffer = new CapturingVertexConsumerProvider(maxVertices);
                MatrixStack matrices = new MatrixStack();

                Vec3d pos = entity.getLerpedPos(partialTicks);
                matrices.push();
                matrices.translate(pos.x, pos.y, pos.z);
                renderer.render(state, matrices, buffer, FULL_BRIGHT);
                matrices.pop();

                return buffer.getVertices();
            } catch (Throwable ignored) {
                return empty;
            }
        }
    }

    private static final class CapturingVertexConsumerProvider implements VertexConsumerProvider {
        private final CapturingVertexConsumer vertexConsumer;

        private CapturingVertexConsumerProvider(int maxVertices) {
            this.vertexConsumer = new CapturingVertexConsumer(maxVertices);
        }

        @Override
        public VertexConsumer getBuffer(RenderLayer renderLayer) {
            return vertexConsumer;
        }

        private List<Vec3d> getVertices() {
            return vertexConsumer.getVertices();
        }
    }

    private static final class CapturingVertexConsumer implements VertexConsumer {
        private final List<Vec3d> vertices = new ArrayList<>();
        private final int maxVertices;

        private CapturingVertexConsumer(int maxVertices) {
            this.maxVertices = maxVertices;
        }

        @Override
        public VertexConsumer vertex(float x, float y, float z) {
            if (vertices.size() < maxVertices
                    && isSaneCoordinate(x)
                    && isSaneCoordinate(y)
                    && isSaneCoordinate(z)) {
                vertices.add(new Vec3d(x, y, z));
            }
            return this;
        }

        @Override
        public VertexConsumer color(int red, int green, int blue, int alpha) {
            return this;
        }

        @Override
        public VertexConsumer texture(float u, float v) {
            return this;
        }

        @Override
        public VertexConsumer overlay(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer light(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            return this;
        }

        private List<Vec3d> getVertices() {
            return vertices;
        }
    }
}

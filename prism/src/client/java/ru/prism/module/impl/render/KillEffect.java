package ru.prism.module.impl.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import ru.prism.manager.event_impl.AttackEvent;
import ru.prism.manager.event_impl.EventPacket;
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
import ru.prism.module.impl.render.killeffect.KillEffectEasing;
import ru.prism.module.impl.render.killeffect.KillParticleBurstEmitter;
import ru.prism.module.impl.render.killeffect.KillSoundScheduler;
import ru.prism.module.impl.render.killeffect.KillTracker;
import ru.prism.module.impl.render.killeffect.PendingKill;
import ru.prism.utils.colors.ColorUtil;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@ModuleInfo(
        name = "Kill Effect",
        desc = "Ебашит волну и частицы в того, кого ты только что разнёс.",
        category = Category.COMBAT
)
public class KillEffect extends Module implements ModulePreview {

    public ButtonSetting previewButton = PreviewSettings.button(this);

    public ModeSetting mode = new ModeSetting(this, "Режим", "Волна", "Частицы", "Сияние", "Призрак");
    public ModeSetting colorMode = new ModeSetting(this, "Цвет", "Тема", "Радуга", "Свой");
    public ColorSetting tintColor = new ColorSetting(this, "Цвет 1", 0xFF00FFFF).setVisible(() -> colorMode.is("Свой"));
    public BooleanSetting secondColor = new BooleanSetting(this, "Второй цвет", false).setVisible(() -> colorMode.is("Свой"));
    public ColorSetting tintColor2 = new ColorSetting(this, "Цвет 2", 0xFFFF00FF).setVisible(() -> colorMode.is("Свой") && secondColor.getValue());

    public SliderSetting duration = new SliderSetting(this, "Длительность", 2.2F, 1.0F, 5.0F, 0.1F);
    public SliderSetting waveSpeed = new SliderSetting(this, "Скорость волны", 1.5F, 0.4F, 4.0F, 0.1F).setVisible(() -> mode.is("Волна"));
    public SliderSetting particleCount = new SliderSetting(this, "Кол-во частиц", 20F, 5F, 50F, 1F).setVisible(() -> mode.is("Частицы"));
    public SliderSetting particleSpread = new SliderSetting(this, "Разлёт частиц", 0.8F, 0.2F, 2.0F, 0.05F).setVisible(() -> mode.is("Частицы"));
    public SliderSetting glowWidth = new SliderSetting(this, "Ширина глоу", 0.15F, 0.05F, 0.45F, 0.01F).setVisible(() -> mode.is("Сияние"));
    public SliderSetting glowHeight = new SliderSetting(this, "Высота глоу", 4.0F, 1.5F, 15.0F, 0.1F).setVisible(() -> mode.is("Сияние"));
    public SliderSetting ghostOpacity = new SliderSetting(this, "Прозрачность", 0.74F, 0.15F, 1.0F, 0.01F).setVisible(() -> mode.is("Призрак"));
    public SliderSetting ghostGlow = new SliderSetting(this, "Свечение", 1.15F, 0.0F, 2.4F, 0.05F).setVisible(() -> mode.is("Призрак"));

    public BooleanSetting sound = new BooleanSetting(this, "Звук", true);
    public SliderSetting volume = new SliderSetting(this, "Громкость", 1.0F, 0.1F, 2.0F, 0.1F).setVisible(() -> sound.getValue());

    private final PreviewSettings previewSettings = PreviewSettings.of(this, 4F, 0F, 2.5F);

    private static final byte DEATH_STATUS = 3;
    private static final long MEMORY_MS = 2500L;
    private static final long WAVE_DURATION_MS = 900L;
    private static final float GLOW_DURATION_MS = 2800F;
    private static final float GLOW_Y_OFFSET = -0.42F;
    private static final float GLOW_DISC_RADIUS = 2.15F;
    private static final float GLOW_SLICE_STEP = 0.035F;
    private static final float GLOW_APPEAR_END = 0.30F;
    private static final float GLOW_BEAM_DISAPPEAR_START = 0.34F;
    private static final float GLOW_BEAM_DISAPPEAR_END = 0.54F;
    private static final float GLOW_DISC_FADE_START = 0.40F;

    private static final float[][] PARTS = {
            {-0.25F, 1.50F, -0.25F, 0.25F, 2.00F, 0.25F},
            {-0.25F, 0.75F, -0.125F, 0.25F, 1.50F, 0.125F},
            {0.25F, 0.75F, -0.125F, 0.50F, 1.50F, 0.125F},
            {-0.50F, 0.75F, -0.125F, -0.25F, 1.50F, 0.125F},
            {0.00F, 0.00F, -0.125F, 0.25F, 0.75F, 0.125F},
            {-0.25F, 0.00F, -0.125F, 0.00F, 0.75F, 0.125F}
    };

    private static final float[][] PIVOTS = {
            {0.00F, 1.50F, 0.00F},
            {0.00F, 0.75F, 0.00F},
            {0.375F, 1.40F, 0.00F},
            {-0.375F, 1.40F, 0.00F},
            {0.125F, 0.75F, 0.00F},
            {-0.125F, 0.75F, 0.00F}
    };
    private static final int PART_COUNT = PARTS.length;

    private static final int[][] FACES = {
            {0, 1, 3, 2}, {4, 5, 7, 6},
            {0, 1, 5, 4}, {2, 3, 7, 6},
            {0, 2, 6, 4}, {1, 3, 7, 5}
    };
    private static final int[][] EDGES = {
            {0, 1}, {1, 3}, {3, 2}, {2, 0},
            {4, 5}, {5, 7}, {7, 6}, {6, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7}
    };

    private final KillTracker tracker = new KillTracker(MEMORY_MS, this::onKill);
    private final KillSoundScheduler sounds = new KillSoundScheduler();
    private final List<ScanWave> waves = new ArrayList<>();
    private final List<Glow> glows = new ArrayList<>();
    private final List<Ghost> ghosts = new ArrayList<>();
    private final BufferAllocator allocator = new BufferAllocator(1 << 19);
    private final Random random = new Random();

    @Override
    protected void onDisable() {
        clearState();
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        clearState();
    }

    @EventHandler
    public void onAttack(AttackEvent e) {
        if (e.getTarget() instanceof LivingEntity living && living != mc.player) {
            tracker.remember(living);
        }
    }

    @EventHandler
    public void onPacket(EventPacket e) {
        if (!(e.getPacket() instanceof EntityStatusS2CPacket packet)) return;
        if (packet.getStatus() != DEATH_STATUS || mc.world == null) return;

        Entity entity = packet.getEntity(mc.world);
        if (entity instanceof LivingEntity living) {
            tracker.triggerNow(living);
        }
    }

    @EventHandler
    public void onTick(EventTick e) {
        tracker.tick();
        sounds.tick();
    }

    private void onKill(PendingKill pending) {
        if (mc.player == null || mc.world == null) return;
        if (mode.is("Призрак")) {
            spawnGhost(pending.entity);
        }
        spawnEffects(pending.position(), pending.radius());
    }

    private void spawnEffects(Vec3d pos, float radius) {
        int color = pickColor();

        if (mode.is("Волна")) {
            waves.add(new ScanWave(pos, radius, color));
        }
        if (mode.is("Частицы")) {
            int count = Math.round(particleCount.getValue());
            KillParticleBurstEmitter.spawnBurst(
                    pos.add(0.0, radius * 0.5F, 0.0), count, particleSpread.getValue(), radius * 0.35F);
        }
        if (mode.is("Сияние")) {
            glows.add(new Glow(pos, color));
        }
        if (sound.getValue()) {
            sounds.schedule(SoundEvents.ENTITY_PLAYER_ATTACK_CRIT,
                    volume.getValue(), 0.75F + random.nextFloat() * 0.5F, 0L);
        }
    }

    private int pickColor() {
        if (colorMode.is("Радуга")) {
            float hue = (float) Math.floorMod(System.currentTimeMillis() / 12L + random.nextInt(360), 360L) / 360F;
            return Color.HSBtoRGB(hue, 1F, 1F) | 0xFF000000;
        }
        if (colorMode.is("Свой")) {
            int first = tintColor.getValue();
            int second = secondColor.getValue() ? tintColor2.getValue() : first;
            return ColorUtil.interpolateColor(first, second, random.nextFloat());
        }
        return ColorUtil.getClientColor1(1);
    }

    @Override
    public PreviewSettings previewSettings() {
        return previewSettings;
    }

    @Override
    public boolean previewNeedsDummy() {
        return true;
    }

    @Override
    public void previewSpawn(PreviewContext ctx) {
        LivingEntity target = ctx.dummy();
        if (target != null) {
            if (mode.is("Призрак")) {
                spawnGhost(target);
            }
            spawnEffects(target.getEntityPos(), Math.max(0.65F, target.getWidth()));
        } else {
            spawnEffects(ctx.anchor(), 0.65F);
        }
    }

    @Override
    public void previewStop() {
        clearState();
    }

    @EventHandler
    public void onRender3D(EventRender3D e) {
        if (mc.player == null || mc.world == null) return;
        if (waves.isEmpty() && glows.isEmpty() && ghosts.isEmpty()) return;

        long now = System.currentTimeMillis();
        long ghostDurationMs = (long) (duration.getValue() * 1000F);

        MatrixStack matrices = e.getMatrixStack();
        Matrix4f mat = matrices.peek().getPositionMatrix();
        Vec3d cam = mc.gameRenderer.getCamera().getCameraPos();

        Iterator<ScanWave> waveIterator = waves.iterator();
        while (waveIterator.hasNext()) {
            ScanWave wave = waveIterator.next();
            float progress = (now - wave.start) / (float) WAVE_DURATION_MS;
            if (progress >= 1F) {
                waveIterator.remove();
                continue;
            }
            wave.progress = progress;
        }

        glows.removeIf(glow -> glow.expired(now));

        Iterator<Ghost> ghostIterator = ghosts.iterator();
        while (ghostIterator.hasNext()) {
            Ghost g = ghostIterator.next();
            float t = (now - g.start) / (float) ghostDurationMs;
            if (t >= 1F) {
                ghostIterator.remove();
                continue;
            }

            float fadeIn = Math.min(t * 8F, 1F);
            float fadeOut = t > 0.55F ? 1F - (t - 0.55F) / 0.45F : 1F;
            g.alphaValue = fadeIn * fadeOut;

            double gx = g.x - cam.x;
            double gy = g.y - cam.y;
            double gz = g.z - cam.z;

            matrices.push();
            matrices.translate(gx, gy, gz);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-g.bodyYaw + t * 40F));
            for (int i = 0; i < PART_COUNT; i++) {
                float pitch = g.partPitchDeg[i];
                float yawOff = i == 0 ? g.headYawDeg : 0F;
                if (pitch == 0F && yawOff == 0F) {
                    g.partMatrices[i].set(matrices.peek().getPositionMatrix());
                    continue;
                }
                float[] pv = PIVOTS[i];
                matrices.push();
                matrices.translate(pv[0], pv[1], pv[2]);
                if (yawOff != 0F) matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-yawOff));
                if (pitch != 0F) matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
                matrices.translate(-pv[0], -pv[1], -pv[2]);
                g.partMatrices[i].set(matrices.peek().getPositionMatrix());
                matrices.pop();
            }
            matrices.pop();

            matrices.push();
            matrices.translate(gx, gy + 1.1, gz);
            matrices.multiply(mc.gameRenderer.getCamera().getRotation());
            g.glowMatrix.set(matrices.peek().getPositionMatrix());
            matrices.pop();
        }

        if (waves.isEmpty() && glows.isEmpty() && ghosts.isEmpty()) return;

        float opacity = ghostOpacity.getValue();
        float glowStrength = ghostGlow.getValue();

        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(allocator);

        if (!waves.isEmpty()) {
            VertexConsumer buf = immediate.getBuffer(EFFECT_LAYER);
            for (ScanWave wave : waves) {
                drawWave(buf, mat, cam, wave);
            }
        }

        if (!glows.isEmpty()) {
            VertexConsumer buf = immediate.getBuffer(GLOW_LAYER);
            for (Glow glow : glows) {
                drawGlow(buf, matrices, cam, glow, now);
            }
        }

        if (!ghosts.isEmpty() && opacity > 0.001F) {
            VertexConsumer buf = immediate.getBuffer(GHOST_FILL_LAYER);
            for (Ghost g : ghosts) {
                int alpha = (int) (g.alphaValue * opacity * 0.7F * 255F);
                if (alpha <= 0) continue;
                for (int i = 0; i < PART_COUNT; i++) {
                    drawBoxFill(buf, g.partMatrices[i], PARTS[i], g.r, g.g, g.b, alpha);
                }
            }

            buf = immediate.getBuffer(GHOST_LINE_LAYER);
            for (Ghost g : ghosts) {
                int alpha = (int) (g.alphaValue * opacity * 0.45F * 255F);
                if (alpha <= 0) continue;
                for (int i = 0; i < PART_COUNT; i++) {
                    drawBoxLines(buf, g.partMatrices[i], PARTS[i], g.r, g.g, g.b, alpha);
                }
            }
        }

        if (!ghosts.isEmpty() && glowStrength > 0.01F) {
            int glowBig = (int) Math.min(255F, 110F * glowStrength);
            int glowSmall = (int) Math.min(255F, 160F * glowStrength);

            VertexConsumer buf = immediate.getBuffer(GHOST_GLOW_LAYER_BIG);
            for (Ghost g : ghosts) {
                drawGlow(buf, g.glowMatrix, g.r, g.g, g.b, (int) (glowBig * g.alphaValue), 1.5F);
            }

            buf = immediate.getBuffer(GHOST_GLOW_LAYER_SMALL);
            for (Ghost g : ghosts) {
                drawGlow(buf, g.glowMatrix, g.r, g.g, g.b, (int) (glowSmall * g.alphaValue), 0.7F);
            }
        }

        immediate.draw();
    }

    private static float corner(float[] p, int bit, int axis) {
        return ((bit >> axis) & 1) == 0 ? p[axis] : p[axis + 3];
    }

    private static final float[] FACE_SHADE = {0.65F, 0.85F, 0.5F, 1.0F, 0.7F, 0.95F};

    private static void drawBoxFill(VertexConsumer buf, Matrix4f m, float[] p, int r, int g, int b, int alpha) {
        for (int f = 0; f < FACES.length; f++) {
            float sh = FACE_SHADE[f];
            int cr = (int) (r * sh), cg = (int) (g * sh), cb = (int) (b * sh);
            for (int idx : FACES[f]) {
                buf.vertex(m, corner(p, idx, 0), corner(p, idx, 1), corner(p, idx, 2)).color(cr, cg, cb, alpha);
            }
        }
    }

    private static void drawBoxLines(VertexConsumer buf, Matrix4f m, float[] p, int r, int g, int b, int alpha) {
        for (int[] edge : EDGES) {
            buf.vertex(m, corner(p, edge[0], 0), corner(p, edge[0], 1), corner(p, edge[0], 2)).color(r, g, b, alpha);
            buf.vertex(m, corner(p, edge[1], 0), corner(p, edge[1], 1), corner(p, edge[1], 2)).color(r, g, b, alpha);
        }
    }

    private static void drawGlow(VertexConsumer buf, Matrix4f m, int r, int g, int b, int alpha, float s) {
        if (alpha <= 0) return;
        buf.vertex(m, -s, -s, 0).color(r, g, b, alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
        buf.vertex(m, s, -s, 0).color(r, g, b, alpha).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
        buf.vertex(m, s, s, 0).color(r, g, b, alpha).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
        buf.vertex(m, -s, s, 0).color(r, g, b, alpha).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0, 0, 1);
    }

    private void spawnGhost(Entity entity) {
        if (entity == null) return;

        int color = pickColor();
        Ghost g = new Ghost(entity.getEntityPos(), entity.getYaw(),
                (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);

        if (entity instanceof LivingEntity le) {
            g.bodyYaw = le.getBodyYaw();
            g.headYawDeg = MathHelper.clamp(MathHelper.wrapDegrees(le.getHeadYaw() - le.getBodyYaw()), -75F, 75F);

            float limbAngle = le.limbAnimator.getAnimationProgress();
            float limbDist = Math.min(le.limbAnimator.getSpeed(), 1F);

            g.partPitchDeg[0] = le.getPitch();
            g.partPitchDeg[2] = (float) Math.toDegrees(MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 2.0F * limbDist * 0.5F);
            g.partPitchDeg[3] = (float) Math.toDegrees(MathHelper.cos(limbAngle * 0.6662F) * 2.0F * limbDist * 0.5F);
            g.partPitchDeg[4] = (float) Math.toDegrees(MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDist);
            g.partPitchDeg[5] = (float) Math.toDegrees(MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDist);
        } else {
            g.bodyYaw = entity.getYaw();
        }

        ghosts.add(g);
    }

    private void drawWave(VertexConsumer buf, Matrix4f mat, Vec3d cam, ScanWave wave) {
        float fade = 1F - KillEffectEasing.smooth(wave.progress);
        int alpha = (int) (150F * fade);
        if (alpha <= 2) return;

        int r = (wave.color >> 16) & 0xFF;
        int g = (wave.color >> 8) & 0xFF;
        int b = wave.color & 0xFF;

        float radius = wave.radius * (1.1F + wave.progress * 3.2F * waveSpeed.getValue());
        float thickness = 0.05F + wave.progress * 0.08F;
        float x = (float) (wave.x - cam.x);
        float y = (float) (wave.y - cam.y + 0.03F);
        float z = (float) (wave.z - cam.z);

        drawRing(buf, mat, x, y, z, radius, thickness, r, g, b, alpha);
        drawRing(buf, mat, x, y + 0.02F, z, radius * 0.72F, thickness * 0.8F, r, g, b, alpha / 2);
    }

    private static void drawRing(VertexConsumer buf, Matrix4f mat, float x, float y, float z,
                                 float radius, float width, int r, int g, int b, int alpha) {
        if (alpha <= 0 || radius <= width) return;

        int segments = 64;
        for (int i = 0; i < segments; i++) {
            float a0 = i / (float) segments * MathHelper.TAU;
            float a1 = (i + 1) / (float) segments * MathHelper.TAU;

            float x0 = MathHelper.cos(a0);
            float z0 = MathHelper.sin(a0);
            float x1 = MathHelper.cos(a1);
            float z1 = MathHelper.sin(a1);

            buf.vertex(mat, x + x0 * (radius - width), y, z + z0 * (radius - width)).color(r, g, b, 0);
            buf.vertex(mat, x + x0 * radius, y, z + z0 * radius).color(r, g, b, alpha);
            buf.vertex(mat, x + x1 * radius, y, z + z1 * radius).color(r, g, b, alpha);
            buf.vertex(mat, x + x1 * (radius - width), y, z + z1 * (radius - width)).color(r, g, b, 0);
        }
    }

    private void drawGlow(VertexConsumer buf, MatrixStack matrices, Vec3d cam, Glow glow, long now) {
        float progress = (now - glow.start) / GLOW_DURATION_MS;
        Vec3d effectPos = glow.position.add(0.0, GLOW_Y_OFFSET, 0.0);

        float appear = glowAppear(progress);
        float beamDisappear = glowBeamDisappear(progress);
        float beamFactor = appear * (1F - beamDisappear);
        float discFade = glowDiscFade(progress);

        drawGlowDisc(buf, matrices, cam, effectPos, GLOW_DISC_RADIUS, glow.color, appear * discFade);

        if (beamFactor <= 0.005F) return;

        float glowH = glowHeight.getValue();
        float beamHeight = glowH * beamFactor;
        float maxHeight = glowH * appear;
        float baseRadius = glowWidth.getValue();
        float softEdge = GLOW_SLICE_STEP * 0.55F;
        float tipZone = 0.14F;

        for (float y = 0F; y <= maxHeight; y += GLOW_SLICE_STEP) {
            float over = y - beamHeight;
            if (over > softEdge) continue;
            float edgeFade = over > 0F
                    ? (float) Math.pow(1F - MathHelper.clamp(over / softEdge, 0F, 1F), 2.2F)
                    : 1F;

            float heightT = beamHeight > 0.01F ? y / beamHeight : 0F;
            float tipFade = 1F;
            float tipRadius = 1F;
            if (heightT > 1F - tipZone) {
                float tipT = (heightT - (1F - tipZone)) / tipZone;
                tipFade = (float) Math.pow(1F - tipT, 2.6F);
                tipRadius = 0.28F + 0.72F * tipFade;
            }

            float sliceAlpha = appear * (1F - beamDisappear) * edgeFade * tipFade;
            if (sliceAlpha <= 0.005F) continue;

            float radius = baseRadius * tipRadius;
            Vec3d pos = effectPos.add(0.0, y, 0.0);
            drawGlowBillboard(buf, matrices, cam, pos, radius * 1.95F * 0.5F, glow.color, sliceAlpha * 0.20F);
            drawGlowBillboard(buf, matrices, cam, pos, radius * 1.05F * 0.5F, glow.color, sliceAlpha * 0.44F);
            drawGlowBillboard(buf, matrices, cam, pos, radius * 0.5F * 0.5F, glow.color, sliceAlpha * 0.80F);
        }
    }

    private void drawGlowBillboard(VertexConsumer buf, MatrixStack matrices, Vec3d cam, Vec3d pos,
                                   float half, int color, float alpha01) {
        int alpha = (int) (MathHelper.clamp(alpha01, 0F, 1F) * 255F);
        if (alpha <= 1) return;

        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        matrices.push();
        matrices.translate(pos.x - cam.x, pos.y - cam.y, pos.z - cam.z);
        matrices.multiply(mc.gameRenderer.getCamera().getRotation());
        Matrix4f m = matrices.peek().getPositionMatrix();
        buf.vertex(m, -half, -half, 0F).color(r, g, b, alpha).texture(1, 1);
        buf.vertex(m, -half, half, 0F).color(r, g, b, alpha).texture(1, 0);
        buf.vertex(m, half, half, 0F).color(r, g, b, alpha).texture(0, 0);
        buf.vertex(m, half, -half, 0F).color(r, g, b, alpha).texture(0, 1);
        matrices.pop();
    }

    private void drawGlowDisc(VertexConsumer buf, MatrixStack matrices, Vec3d cam, Vec3d pos,
                              float radius, int color, float alpha01) {
        if (alpha01 <= 0.003F) return;

        matrices.push();
        matrices.translate(pos.x - cam.x, pos.y - cam.y + 0.03, pos.z - cam.z);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90F));
        Matrix4f m = matrices.peek().getPositionMatrix();
        drawFlatQuad(buf, m, radius * 1.35F, color, alpha01 * 0.28F * alpha01);
        drawFlatQuad(buf, m, radius, color, alpha01 * 0.55F * alpha01);
        drawFlatQuad(buf, m, radius * 0.55F, color, alpha01);
        matrices.pop();
    }

    private static void drawFlatQuad(VertexConsumer buf, Matrix4f m, float half, int color, float alpha01) {
        int alpha = (int) (MathHelper.clamp(alpha01, 0F, 1F) * 255F);
        if (alpha <= 1) return;

        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        buf.vertex(m, -half, -half, 0F).color(r, g, b, alpha).texture(0, 0);
        buf.vertex(m, -half, half, 0F).color(r, g, b, alpha).texture(0, 1);
        buf.vertex(m, half, half, 0F).color(r, g, b, alpha).texture(1, 1);
        buf.vertex(m, half, -half, 0F).color(r, g, b, alpha).texture(1, 0);
    }

    private static float glowAppear(float progress) {
        if (progress >= GLOW_APPEAR_END) return 1F;
        return glowEaseOutCubic(progress / GLOW_APPEAR_END);
    }

    private static float glowBeamDisappear(float progress) {
        if (progress <= GLOW_BEAM_DISAPPEAR_START) return 0F;
        if (progress >= GLOW_BEAM_DISAPPEAR_END) return 1F;
        return glowSmootherStep((progress - GLOW_BEAM_DISAPPEAR_START)
                / (GLOW_BEAM_DISAPPEAR_END - GLOW_BEAM_DISAPPEAR_START));
    }

    private static float glowDiscFade(float progress) {
        if (progress < GLOW_DISC_FADE_START) return 1F;
        float gone = MathHelper.clamp((progress - GLOW_DISC_FADE_START) / (1F - GLOW_DISC_FADE_START), 0F, 1F);
        float remain = 1F - gone;
        return remain * remain * remain * remain * remain;
    }

    private static float glowEaseOutCubic(float t) {
        t = MathHelper.clamp(t, 0F, 1F);
        float inv = 1F - t;
        return 1F - inv * inv * inv;
    }

    private static float glowSmootherStep(float t) {
        t = MathHelper.clamp(t, 0F, 1F);
        return t * t * t * (t * (t * 6F - 15F) + 10F);
    }

    private void clearState() {
        waves.clear();
        glows.clear();
        ghosts.clear();
        tracker.clear();
        sounds.clear();
    }

    private static class ScanWave {
        final double x, y, z;
        final float radius;
        final int color;
        final long start = System.currentTimeMillis();
        float progress;

        ScanWave(Vec3d pos, float radius, int color) {
            this.x = pos.x;
            this.y = pos.y;
            this.z = pos.z;
            this.radius = radius;
            this.color = color;
        }
    }

    private static class Glow {
        final Vec3d position;
        final int color;
        final long start = System.currentTimeMillis();

        Glow(Vec3d position, int color) {
            this.position = position;
            this.color = color;
        }

        boolean expired(long now) {
            return now - this.start >= (long) GLOW_DURATION_MS;
        }
    }

    private static class Ghost {
        final double x, y, z;
        final int r, g, b;
        final long start;
        float bodyYaw;
        float headYawDeg;
        final float[] partPitchDeg = new float[PART_COUNT];
        float alphaValue;
        final Matrix4f[] partMatrices = new Matrix4f[PART_COUNT];
        final Matrix4f glowMatrix = new Matrix4f();

        Ghost(Vec3d pos, float yaw, int r, int g, int b) {
            this.x = pos.x;
            this.y = pos.y;
            this.z = pos.z;
            this.bodyYaw = yaw;
            this.r = r;
            this.g = g;
            this.b = b;
            this.start = System.currentTimeMillis();
            for (int i = 0; i < PART_COUNT; i++) {
                partMatrices[i] = new Matrix4f();
            }
        }
    }

    private static final RenderPipeline EFFECT_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "pipeline/kill_effect/effect"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
                    .withCull(false)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withBlend(BlendFunction.LIGHTNING)
                    .build()
    );

    private static final RenderLayer EFFECT_LAYER = RenderLayer.of(
            "kill_effect_effect",
            RenderSetup.builder(EFFECT_PIPELINE).expectedBufferSize(1 << 15).build()
    );

    private static final Identifier GLOW_TEXTURE = Identifier.of("client", "textures/visuals/glow.png");

    private static final RenderPipeline GLOW_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_TEX_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "pipeline/kill_effect/glow"))
                    .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS)
                    .withCull(false)
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withBlend(BlendFunction.LIGHTNING)
                    .build()
    );

    private static final RenderLayer GLOW_LAYER = RenderLayer.of(
            "kill_effect_glow",
            RenderSetup.builder(GLOW_PIPELINE)
                    .texture("Sampler0", GLOW_TEXTURE)
                    .translucent()
                    .expectedBufferSize(1 << 18)
                    .build()
    );

    private static final RenderPipeline GHOST_FILL_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "pipeline/kill_effect/ghost_fill"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
                    .withCull(false)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withBlend(BlendFunction.LIGHTNING)
                    .build()
    );

    private static final RenderLayer GHOST_FILL_LAYER = RenderLayer.of(
            "kill_effect_ghost_fill",
            RenderSetup.builder(GHOST_FILL_PIPELINE).translucent().expectedBufferSize(1 << 12).build()
    );

    private static final RenderPipeline GHOST_LINE_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "pipeline/kill_effect/ghost_lines"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.DEBUG_LINES)
                    .withCull(false)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withBlend(BlendFunction.LIGHTNING)
                    .build()
    );

    private static final RenderLayer GHOST_LINE_LAYER = RenderLayer.of(
            "kill_effect_ghost_lines",
            RenderSetup.builder(GHOST_LINE_PIPELINE).expectedBufferSize(1 << 12).build()
    );

    private static final Identifier GHOST_GLOW_TEXTURE_BIG = Identifier.of("client", "textures/visuals/particles_1.png");
    private static final Identifier GHOST_GLOW_TEXTURE_SMALL = Identifier.of("client", "textures/visuals/particles_2.png");

    private static final RenderPipeline GHOST_GLOW_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_TEX_COLOR_SNIPPET)
                    .withLocation(Identifier.of("client", "pipeline/kill_effect/ghost_glow"))
                    .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS)
                    .withCull(false)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withBlend(BlendFunction.LIGHTNING)
                    .build()
    );

    private static final RenderLayer GHOST_GLOW_LAYER_BIG = RenderLayer.of(
            "kill_effect_ghost_glow_big",
            RenderSetup.builder(GHOST_GLOW_PIPELINE)
                    .texture("Sampler0", GHOST_GLOW_TEXTURE_BIG)
                    .translucent()
                    .expectedBufferSize(1 << 10)
                    .build()
    );

    private static final RenderLayer GHOST_GLOW_LAYER_SMALL = RenderLayer.of(
            "kill_effect_ghost_glow_small",
            RenderSetup.builder(GHOST_GLOW_PIPELINE)
                    .texture("Sampler0", GHOST_GLOW_TEXTURE_SMALL)
                    .translucent()
                    .expectedBufferSize(1 << 10)
                    .build()
    );
}

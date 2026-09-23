package wtf.wyvern.client.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.core.events.impl.render.EventRender3D;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.level.Render3DUtil;
import wtf.wyvern.utility.interfaces.IMinecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "LineGlyphes",
        category = Category.RENDER,
        description = "Парящие угловатые линии в стиле VegaLine"
)
public final class LineGlyphes extends Module implements IMinecraft {

    public static final LineGlyphes INSTANCE = new LineGlyphes();

    private final SliderSetting count = new SliderSetting("Количество", 38.0F, 10.0F, 90.0F, 1.0F);
    private final SliderSetting distance = new SliderSetting("Дистанция", 20.0F, 8.0F, 32.0F, 1.0F);
    private final SliderSetting speed = new SliderSetting("Скорость", 1.0F, 0.35F, 2.5F, 0.05F);
    private final SliderSetting fadeTime = new SliderSetting("Затухание", 1.25F, 0.4F, 3.0F, 0.05F);
    private final BooleanSetting glow = new BooleanSetting("Свечение", true);
    private final BooleanSetting throughWalls = new BooleanSetting("Сквозь стены", true);
    private static final Identifier GLOW_TEXTURE = Identifier.of("wyvern", "icons/glow.png");

    private final List<Glyph> glyphs = new ArrayList<>();
    private final Random random = new Random();
    private long nextSpawnAt;

    private LineGlyphes() {
    }

    @FastNative
    @Override
    public void onEnable() {
        glyphs.clear();
        nextSpawnAt = 0L;
        super.onEnable();
    }

    @FastNative
    @Override
    public void onDisable() {
        glyphs.clear();
        nextSpawnAt = 0L;
        super.onDisable();
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (mc.player == null || mc.world == null) {
            glyphs.clear();
            nextSpawnAt = 0L;
            return;
        }

        long now = System.currentTimeMillis();
        for (Glyph glyph : glyphs) {
            glyph.update(now);
        }
        glyphs.removeIf(glyph -> glyph.isDead(now));

        int wanted = Math.round(count.getCurrent());
        while (glyphs.size() > wanted) {
            glyphs.remove(glyphs.size() - 1);
        }

        // Do not create the whole pool in one tick. A private spawn schedule keeps every
        // glyph on its own timeline instead of producing synchronized appearance waves.
        if (glyphs.size() < wanted) {
            if (nextSpawnAt == 0L) {
                nextSpawnAt = now + nextSpawnDelayMs(wanted);
            } else if (now >= nextSpawnAt) {
                glyphs.add(new Glyph(randomSpawnPos(), now));
                nextSpawnAt = glyphs.size() < wanted
                        ? now + nextSpawnDelayMs(wanted)
                        : 0L;
            }
        } else {
            nextSpawnAt = 0L;
        }
    }

    @FastNative
    private long nextSpawnDelayMs(int wanted) {
        double average = MathHelper.clamp(2600.0D / Math.max(1, wanted), 35.0D, 260.0D);
        return Math.max(25L, (long) (average * random(0.58D, 1.52D)));
    }

    @EventTarget
    private void onRender3D(EventRender3D event) {
        if (mc.player == null || mc.world == null || glyphs.isEmpty()) return;

        long now = System.currentTimeMillis();
        boolean depth = !throughWalls.isEnabled();
        int glyphIndex = 0;

        for (Glyph glyph : glyphs) {
            float glyphAlpha = glyph.alpha(now);
            if (glyphAlpha <= 0.01F) {
                glyphIndex++;
                continue;
            }

            List<Segment> segments = glyph.renderSegments(now);
            int segmentCount = Math.max(1, segments.size());
            for (int i = 0; i < segments.size(); i++) {
                Segment segment = segments.get(i);
                float tailAlpha = 0.28F + 0.72F * ((float) (i + 1) / segmentCount);
                float alpha = glyphAlpha * tailAlpha;
                ColorRGBA color = Wyvern.INSTANCE.getThemeManager()
                        .getClientColor(glyphIndex * 31 + i * 17);

                if (glow.isEnabled()) {
                    Render3DUtil.drawLine(segment.from(), segment.to(),
                            color.withAlpha(Math.round(42.0F * alpha)).getRGB(), 7.0F, depth);
                    Render3DUtil.drawLine(segment.from(), segment.to(),
                            color.withAlpha(Math.round(82.0F * alpha)).getRGB(), 4.0F, depth);
                }

                int coreColor = color.brighter(0.16F)
                        .withAlpha(Math.round(220.0F * alpha)).getRGB();
                Render3DUtil.drawLine(segment.from(), segment.to(), coreColor, 1.35F, depth);

                if (i == segments.size() - 1 || i % 2 == 0) {
                    drawJoint(segment.to(), color, alpha, depth);
                }
            }
            glyphIndex++;
        }
        if (glow.isEnabled()) {
            renderCornerGlows(event, now);
        }
    }

    private void renderCornerGlows(EventRender3D event, long now) {
        Vec3d camera = mc.gameRenderer.getCamera().getPos();
        BufferBuilder buffer = Tessellator.getInstance().begin(
                VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture(0, GLOW_TEXTURE);

        for (Glyph glyph : glyphs) {
            float alpha = glyph.alpha(now);
            if (alpha <= 0.01F) continue;
            List<Segment> segments = glyph.renderSegments(now);
            for (int i = 0; i < segments.size(); i++) {
                if (i != segments.size() - 1 && (i & 1) != 0) continue;
                Vec3d point = segments.get(i).to();
                ColorRGBA color = Wyvern.INSTANCE.getThemeManager().getClientColor(i * 17)
                        .withAlpha(Math.round(215.0F * alpha));
                float size = 0.14F + alpha * 0.07F;
                event.getMatrix().push();
                event.getMatrix().translate(point.x - camera.x, point.y - camera.y, point.z - camera.z);
                event.getMatrix().multiply(mc.gameRenderer.getCamera().getRotation());
                Matrix4f matrix = event.getMatrix().peek().getPositionMatrix();
                buffer.vertex(matrix, -size, size, 0.0F).texture(0.0F, 1.0F).color(color.getRGB());
                buffer.vertex(matrix, size, size, 0.0F).texture(1.0F, 1.0F).color(color.getRGB());
                buffer.vertex(matrix, size, -size, 0.0F).texture(1.0F, 0.0F).color(color.getRGB());
                buffer.vertex(matrix, -size, -size, 0.0F).texture(0.0F, 0.0F).color(color.getRGB());
                event.getMatrix().pop();
            }
        }
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    @FastNative
    private void drawJoint(Vec3d point, ColorRGBA color, float alpha, boolean depth) {
        double radius = 0.025D;
        int jointColor = color.brighter(0.28F)
                .withAlpha(Math.round(235.0F * alpha)).getRGB();
        Render3DUtil.drawLine(point.add(0.0D, -radius, 0.0D),
                point.add(0.0D, radius, 0.0D), jointColor, 2.2F, depth);
    }

    @FastNative
    private Vec3d randomSpawnPos() {
        double maxDistance = distance.getCurrent();
        double radius = random(6.0D, maxDistance);
        double yaw = Math.toRadians(mc.player.getYaw() + random(-105.0D, 105.0D));
        double x = mc.player.getX() - Math.sin(yaw) * radius;
        double z = mc.player.getZ() + Math.cos(yaw) * radius;
        double y = mc.player.getEyeY() + random(-maxDistance * 0.22D, maxDistance * 0.42D);
        return new Vec3d(x, y, z);
    }

    @FastNative
    private double random(double min, double max) {
        if (max <= min) return min;
        return min + random.nextDouble() * (max - min);
    }

    @FastNative
    private long fadeTimeMs() {
        return Math.max(100L, (long) (fadeTime.getCurrent() * 1000.0F));
    }

    private final class Glyph {
        private final List<Vec3d> points = new ArrayList<>();
        private final long bornAt;
        private final long fadeInDurationMs;
        private final long fadeOutDurationMs;
        private Vec3d target;
        private int direction;
        private int remainingSteps;
        private long stepStartedAt;
        private long stepDurationMs;
        private long finishedAt;

        private Glyph(Vec3d spawnPos, long now) {
            points.add(spawnPos);
            bornAt = now;
            fadeInDurationMs = (long) random(170.0D, 430.0D);
            fadeOutDurationMs = Math.max(100L,
                    (long) (fadeTimeMs() * random(0.72D, 1.34D)));
            direction = random.nextInt(6);
            remainingSteps = 7 + random.nextInt(6);
            beginNextStep(now, true);
        }

        private void update(long now) {
            if (finishedAt != 0L || target == null) return;

            if (now - stepStartedAt >= stepDurationMs) {
                points.add(target);
                remainingSteps--;
                if (remainingSteps <= 0) {
                    target = null;
                    finishedAt = now;
                } else {
                    beginNextStep(now, false);
                }
            }
        }

        private void beginNextStep(long now, boolean first) {
            if (!first) {
                int previousAxis = direction / 2;
                int nextAxis;
                do {
                    nextAxis = random.nextInt(3);
                } while (nextAxis == previousAxis);
                direction = nextAxis * 2 + random.nextInt(2);
            }

            Vec3d from = points.get(points.size() - 1);
            double length = random(0.75D, 2.35D);
            target = from.add(directionVector(direction).multiply(length));
            stepStartedAt = now;
            stepDurationMs = Math.max(35L,
                    (long) (random(85.0D, 155.0D) / Math.max(0.1F, speed.getCurrent())));
        }

        private Vec3d directionVector(int value) {
            return switch (value) {
                case 0 -> new Vec3d(1.0D, 0.0D, 0.0D);
                case 1 -> new Vec3d(-1.0D, 0.0D, 0.0D);
                case 2 -> new Vec3d(0.0D, 1.0D, 0.0D);
                case 3 -> new Vec3d(0.0D, -1.0D, 0.0D);
                case 4 -> new Vec3d(0.0D, 0.0D, 1.0D);
                default -> new Vec3d(0.0D, 0.0D, -1.0D);
            };
        }

        private List<Segment> renderSegments(long now) {
            List<Segment> segments = new ArrayList<>(points.size());
            for (int i = 1; i < points.size(); i++) {
                segments.add(new Segment(points.get(i - 1), points.get(i)));
            }

            if (target != null) {
                Vec3d from = points.get(points.size() - 1);
                float progress = MathHelper.clamp(
                        (float) (now - stepStartedAt) / Math.max(1L, stepDurationMs), 0.0F, 1.0F);
                progress = 1.0F - (1.0F - progress) * (1.0F - progress);
                segments.add(new Segment(from, from.lerp(target, progress)));
            }
            return segments;
        }

        private float alpha(long now) {
            float fadeIn = MathHelper.clamp(
                    (float) (now - bornAt) / Math.max(1L, fadeInDurationMs), 0.0F, 1.0F);
            if (finishedAt == 0L) return fadeIn;
            float fadeOut = 1.0F - MathHelper.clamp(
                    (float) (now - finishedAt) / Math.max(1L, fadeOutDurationMs), 0.0F, 1.0F);
            return fadeIn * fadeOut;
        }

        private boolean isDead(long now) {
            return finishedAt != 0L && now - finishedAt >= fadeOutDurationMs;
        }
    }

    private record Segment(Vec3d from, Vec3d to) {
    }
}

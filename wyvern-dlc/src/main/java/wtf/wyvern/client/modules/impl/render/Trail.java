package wtf.wyvern.client.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
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
import wtf.wyvern.utility.interfaces.IMinecraft;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "Trail",
        category = Category.RENDER,
        description = "Одна светящаяся DashTrail-лента под ногами"
)
public final class Trail extends Module implements IMinecraft {

    public static final Trail INSTANCE = new Trail();

    private static final String TEXTURE_ROOT = "trail/dashtrail/";
    private static final Identifier BLOOM_TEXTURE = Identifier.of(
            "wyvern", TEXTURE_ROOT + "dashbloomsample.png");
    private static final int[] ANIMATED_FRAME_COUNTS = {11, 23, 32, 16, 32};
    private static final float[] ANIMATED_ASPECTS = {3.0F, 1.0F, 2.0F, 1.0F, 1.0F};
    private static final TextureFrame[] STATIC_FRAMES = createStaticFrames();
    private static final TextureFrame[][] ANIMATED_FRAMES = createAnimatedFrames();
    private static final int MAX_MARKS = 220;

    private final SliderSetting density = new SliderSetting("Плотность", 1.0F, 0.45F, 2.0F, 0.05F);
    private final SliderSetting size = new SliderSetting("Размер", 0.42F, 0.18F, 0.90F, 0.01F);
    private final SliderSetting lifeTime = new SliderSetting("Время жизни", 0.90F, 0.45F, 2.0F, 0.05F);
    private final BooleanSetting glow = new BooleanSetting("Свечение", true);
    private final SliderSetting glowSize = new SliderSetting("Размер свечения", 2.7F, 1.2F, 4.5F, 0.1F,
            glow::isEnabled);

    private final List<TrailMark> marks = new ArrayList<>();
    private final Random random = new Random();
    private Vec3d lastPlayerPos;
    private double distanceRemainder;

    private Trail() {
    }

    @FastNative
    @Override
    public void onEnable() {
        marks.clear();
        lastPlayerPos = mc.player == null ? null : mc.player.getPos();
        distanceRemainder = 0.0D;
        super.onEnable();
    }

    @FastNative
    @Override
    public void onDisable() {
        marks.clear();
        lastPlayerPos = null;
        distanceRemainder = 0.0D;
        super.onDisable();
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (mc.player == null || mc.world == null) {
            marks.clear();
            lastPlayerPos = null;
            distanceRemainder = 0.0D;
            return;
        }

        long now = System.currentTimeMillis();
        marks.removeIf(mark -> mark.isDead(now));

        Vec3d current = mc.player.getPos();
        if (lastPlayerPos == null || current.squaredDistanceTo(lastPlayerPos) > 64.0D) {
            marks.clear();
            lastPlayerPos = current;
            distanceRemainder = 0.0D;
            return;
        }

        Vec3d movement = current.subtract(lastPlayerPos);
        Vec3d horizontal = new Vec3d(movement.x, 0.0D, movement.z);
        double travelled = horizontal.length();
        if (travelled < 0.012D) {
            lastPlayerPos = current;
            return;
        }

        double spacing = 0.19D / Math.max(0.1F, density.getCurrent());
        double total = distanceRemainder + travelled;
        int count = MathHelper.clamp((int) Math.floor(total / spacing), 0, 18);
        Vec3d direction = horizontal.normalize();

        for (int i = 1; i <= count; i++) {
            double along = i * spacing - distanceRemainder;
            double progress = MathHelper.clamp(along / travelled, 0.0D, 1.0D);
            Vec3d pathPoint = lastPlayerPos.lerp(current, progress)
                    .add(random(-0.012D, 0.012D), random(0.055D, 0.085D), random(-0.012D, 0.012D));
            addMark(pathPoint, direction, now);
        }

        distanceRemainder = total - count * spacing;
        if (distanceRemainder >= spacing) distanceRemainder %= spacing;
        lastPlayerPos = current;

        while (marks.size() > MAX_MARKS) marks.removeFirst();
    }

    @FastNative
    private void addMark(Vec3d position, Vec3d direction, long now) {
        boolean animated = random.nextFloat() < 0.64F;
        int animationGroup = animated ? random.nextInt(ANIMATED_FRAMES.length) : -1;
        int staticFrame = animated ? -1 : random.nextInt(STATIC_FRAMES.length);
        float directionAngle = (float) Math.toDegrees(Math.atan2(direction.z, direction.x));
        long lifetime = Math.max(250L,
                (long) (lifeTime.getCurrent() * 1000.0F * random(0.84D, 1.16D)));
        float markSize = (float) (size.getCurrent() * random(0.88D, 1.12D));

        marks.add(new TrailMark(
                position,
                markSize,
                lifetime,
                now,
                animationGroup,
                staticFrame,
                random.nextInt(900),
                directionAngle + (float) random(-7.0D, 7.0D)
        ));
    }

    @EventTarget
    private void onRender3D(EventRender3D event) {
        if (mc.player == null || mc.world == null || marks.isEmpty()) return;

        long now = System.currentTimeMillis();
        marks.removeIf(mark -> mark.isDead(now));
        if (marks.isEmpty()) return;

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);

        if (glow.isEnabled()) renderBloom(event.getMatrix(), now);
        renderDashFrames(event.getMatrix(), now);

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderBloom(MatrixStack matrices, long now) {
        RenderSystem.setShaderTexture(0, BLOOM_TEXTURE);
        BufferBuilder buffer = Tessellator.getInstance()
                .begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        for (TrailMark mark : marks) {
            float alpha = mark.alpha(now);
            if (alpha <= 0.004F) continue;
            ColorRGBA color = mark.color(now);
            float bloomSize = mark.size * glowSize.getCurrent();
            emitGroundQuad(matrices, buffer, mark, bloomSize, bloomSize,
                    color.brighter(0.12F), Math.round(116.0F * alpha), now, false);
        }

        BuiltBuffer built = buffer.endNullable();
        if (built != null) BufferRenderer.drawWithGlobalProgram(built);
    }

    private void renderDashFrames(MatrixStack matrices, long now) {
        Map<TextureFrame, List<TrailMark>> grouped = new LinkedHashMap<>();
        for (TrailMark mark : marks) {
            if (mark.alpha(now) <= 0.004F) continue;
            grouped.computeIfAbsent(mark.frame(now), ignored -> new ArrayList<>()).add(mark);
        }

        for (Map.Entry<TextureFrame, List<TrailMark>> entry : grouped.entrySet()) {
            TextureFrame frame = entry.getKey();
            RenderSystem.setShaderTexture(0, frame.texture());
            BufferBuilder buffer = Tessellator.getInstance()
                    .begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

            for (TrailMark mark : entry.getValue()) {
                float alpha = mark.alpha(now);
                float pulse = 0.94F + 0.06F * (float) Math.sin(mark.progress(now) * Math.PI * 3.0D);
                float height = mark.size * pulse;
                float width = height * frame.aspect();
                ColorRGBA color = mark.color(now);

                // A dim enlarged copy thickens the single strip without spawning extra particles.
                emitGroundQuad(matrices, buffer, mark, width * 1.22F, height * 1.22F,
                        color, Math.round(72.0F * alpha), now, true);
                emitGroundQuad(matrices, buffer, mark, width, height,
                        color.brighter(0.22F), Math.round(242.0F * alpha), now, true);
            }

            BuiltBuffer built = buffer.endNullable();
            if (built != null) BufferRenderer.drawWithGlobalProgram(built);
        }
    }

    private void emitGroundQuad(MatrixStack matrices, BufferBuilder buffer, TrailMark mark,
                                float length, float width, ColorRGBA color, int alpha,
                                long now, boolean alignToMovement) {
        Vec3d camera = mc.gameRenderer.getCamera().getPos();

        matrices.push();
        matrices.translate(mark.position.x - camera.x, mark.position.y - camera.y, mark.position.z - camera.z);
        // A road-marking dash lies flat on the ground. Its long local X axis is rotated
        // into the player's movement vector instead of facing the camera like a billboard.
        if (alignToMovement) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-mark.directionAngle));
        }
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        addGroundQuad(buffer, matrix, length, width, color, alpha);
        matrices.pop();
    }

    private void addGroundQuad(BufferBuilder buffer, Matrix4f matrix, float length, float width,
                               ColorRGBA color, int alpha) {
        float halfLength = length * 0.5F;
        float halfWidth = width * 0.5F;
        int a = MathHelper.clamp(alpha, 0, 255);
        buffer.vertex(matrix, -halfLength, 0.002F, -halfWidth).texture(0.0F, 1.0F)
                .color(color.getRed(), color.getGreen(), color.getBlue(), a);
        buffer.vertex(matrix, -halfLength, 0.002F, halfWidth).texture(0.0F, 0.0F)
                .color(color.getRed(), color.getGreen(), color.getBlue(), a);
        buffer.vertex(matrix, halfLength, 0.002F, halfWidth).texture(1.0F, 0.0F)
                .color(color.getRed(), color.getGreen(), color.getBlue(), a);
        buffer.vertex(matrix, halfLength, 0.002F, -halfWidth).texture(1.0F, 1.0F)
                .color(color.getRed(), color.getGreen(), color.getBlue(), a);
    }

    @FastNative
    private static TextureFrame[] createStaticFrames() {
        // 21 Vega dash frames plus the five user icon textures. They share the same
        // random strip pool, so the trail can alternate between every available image.
        TextureFrame[] result = new TextureFrame[26];
        for (int i = 1; i <= 21; i++) {
            float aspect;
            if (i <= 3) aspect = 1.0F;
            else if (i <= 12) aspect = 2.0F;
            else if (i <= 16) aspect = 1.0F;
            else aspect = 3.0F;
            result[i - 1] = new TextureFrame(Identifier.of(
                    "wyvern", TEXTURE_ROOT + "dashcubics/dashcubic" + i + ".png"), aspect);
        }
        result[21] = new TextureFrame(Identifier.of("wyvern", "icons/dollar.png"), 1.0F);
        result[22] = new TextureFrame(Identifier.of("wyvern", "icons/snow.png"), 1.0F);
        result[23] = new TextureFrame(Identifier.of("wyvern", "icons/star.png"), 1.0F);
        result[24] = new TextureFrame(Identifier.of("wyvern", "icons/spark_1.png"), 1.0F);
        result[25] = new TextureFrame(Identifier.of("wyvern", "icons/sparkle.png"), 1.0F);
        return result;
    }

    @FastNative
    private static TextureFrame[][] createAnimatedFrames() {
        TextureFrame[][] result = new TextureFrame[ANIMATED_FRAME_COUNTS.length][];
        for (int group = 0; group < result.length; group++) {
            int count = ANIMATED_FRAME_COUNTS[group];
            result[group] = new TextureFrame[count];
            for (int frame = 1; frame <= count; frame++) {
                result[group][frame - 1] = new TextureFrame(Identifier.of(
                        "wyvern", TEXTURE_ROOT + "dashcubics/group_dashs/group" + (group + 1)
                                + "/dashcubic" + frame + ".png"), ANIMATED_ASPECTS[group]);
            }
        }
        return result;
    }

    @FastNative
    private double random(double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

    private final class TrailMark {
        private final Vec3d position;
        private final float size;
        private final long lifetimeMs;
        private final long bornAt;
        private final int animationGroup;
        private final int staticFrame;
        private final int colorIndex;
        private final float directionAngle;

        private TrailMark(Vec3d position, float size, long lifetimeMs, long bornAt,
                          int animationGroup, int staticFrame, int colorIndex,
                          float directionAngle) {
            this.position = position;
            this.size = size;
            this.lifetimeMs = lifetimeMs;
            this.bornAt = bornAt;
            this.animationGroup = animationGroup;
            this.staticFrame = staticFrame;
            this.colorIndex = colorIndex;
            this.directionAngle = directionAngle;
        }

        private TextureFrame frame(long now) {
            if (animationGroup < 0) return STATIC_FRAMES[staticFrame];
            TextureFrame[] animation = ANIMATED_FRAMES[animationGroup];
            int index = Math.min(animation.length - 1,
                    (int) (progress(now) * animation.length));
            return animation[index];
        }

        private ColorRGBA color(long now) {
            return Wyvern.INSTANCE.getThemeManager().getClientColor(
                    colorIndex + (int) (progress(now) * 145.0F));
        }

        private float alpha(long now) {
            float progress = progress(now);
            float fadeIn = MathHelper.clamp(progress / 0.10F, 0.0F, 1.0F);
            float fadeOut = 1.0F - progress;
            return fadeIn * fadeOut * fadeOut;
        }

        private float progress(long now) {
            return MathHelper.clamp((float) (now - bornAt) / Math.max(1L, lifetimeMs), 0.0F, 1.0F);
        }

        private boolean isDead(long now) {
            return now - bornAt >= lifetimeMs;
        }
    }

    private record TextureFrame(Identifier texture, float aspect) {
    }
}

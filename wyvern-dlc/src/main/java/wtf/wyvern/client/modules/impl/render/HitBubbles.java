package wtf.wyvern.client.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventAttack;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.core.events.impl.render.EventRender3D;
import wtf.wyvern.render.display.base.color.ColorRGBA;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Optional;

@ModuleAnnotation(
        name = "HitBubbles",
        category = Category.RENDER,
        description = "Показывает анимированный круг при ударе"
)
public final class HitBubbles extends Module {
    public static final HitBubbles INSTANCE = new HitBubbles();

    private static final long LIFE_MS = 1600L;
    private static final Identifier BUBBLE_TEXTURE =
            Identifier.of("wyvern", "textures/hitbubble/bubble.png");

    private final CopyOnWriteArrayList<HitBubble> bubbles = new CopyOnWriteArrayList<>();

    private HitBubbles() {
    }

    @Override
    public void onDisable() {
        bubbles.clear();
        super.onDisable();
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        long now = System.currentTimeMillis();
        bubbles.removeIf(bubble -> now - bubble.spawnTime() >= LIFE_MS);
    }

    @EventTarget
    private void onAttack(EventAttack event) {
        if (mc.player == null || !(event.getTarget() instanceof LivingEntity living)) return;

        Vec3d sideDirection = getHitSideDirection(living, mc.player.getPos());
        Vec3d hitPosition = resolveCrosshairHitPosition(living);
        float sideYaw = (float) Math.toDegrees(Math.atan2(sideDirection.x, sideDirection.z));
        bubbles.add(new HitBubble(
                hitPosition,
                System.currentTimeMillis(),
                (float) (Math.random() * 360.0),
                sideYaw
        ));
    }

    @EventTarget
    private void onRender3D(EventRender3D event) {
        if (bubbles.isEmpty() || mc.player == null) return;

        MatrixStack matrices = event.getMatrix();
        Vec3d cameraPosition = mc.gameRenderer.getCamera().getPos();

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture(0, BUBBLE_TEXTURE);

        try {
            long now = System.currentTimeMillis();
            for (HitBubble bubble : bubbles) {
                renderBubble(matrices, cameraPosition, bubble, now);
            }
        } finally {
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
            RenderSystem.enableCull();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
        }
    }

    private void renderBubble(MatrixStack matrices, Vec3d cameraPosition, HitBubble bubble, long now) {
        float progress = (now - bubble.spawnTime()) / (float) LIFE_MS;
        if (progress >= 1.0F) return;

        float inPhase = Math.clamp(progress / 0.22F, 0.0F, 1.0F);
        float outPhase = Math.clamp((progress - 0.225F) / 0.40F, 0.0F, 1.0F);
        float scaleIn = inPhase * inPhase * (3.0F - 2.0F * inPhase);
        float scaleOut = 1.0F - outPhase * outPhase;
        float scale = 0.02F + 1.55F * scaleIn * scaleOut;
        float alpha = 1.0F - outPhase * outPhase * outPhase;
        float rotation = (now - bubble.spawnTime()) / 1.5F + bubble.spinSeed();

        Vec3d relativePosition = bubble.position().subtract(cameraPosition);
        ColorRGBA theme = Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor();
        int color = theme.withAlpha((int) (alpha * 255.0F)).getRGB();

        matrices.push();
        matrices.translate(relativePosition.x, relativePosition.y, relativePosition.z);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(bubble.sideYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-210.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
        drawTexturedQuad(matrices, -scale * 0.5F, -scale * 0.5F, scale, scale, color);
        matrices.pop();
    }

    private void drawTexturedQuad(MatrixStack matrices, float x, float y,
                                  float width, float height, int color) {
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;
        int alpha = color >>> 24;
        if (alpha <= 0) return;

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance()
                .begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(matrix, x, y, 0.0F).texture(0.0F, 0.0F).color(red, green, blue, alpha);
        buffer.vertex(matrix, x, y + height, 0.0F).texture(0.0F, 1.0F).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + width, y + height, 0.0F).texture(1.0F, 1.0F).color(red, green, blue, alpha);
        buffer.vertex(matrix, x + width, y, 0.0F).texture(1.0F, 0.0F).color(red, green, blue, alpha);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    private Vec3d getHitSideDirection(LivingEntity target, Vec3d attackerPosition) {
        Vec3d direction = attackerPosition.subtract(target.getPos());
        direction = new Vec3d(direction.x, 0.0, direction.z);
        if (direction.lengthSquared() < 1.0E-4) {
            Vec3d fallback = target.getRotationVector();
            direction = new Vec3d(fallback.x, 0.0, fallback.z);
        }
        if (direction.lengthSquared() < 1.0E-4) {
            direction = new Vec3d(0.0, 0.0, 1.0);
        }
        return direction.normalize();
    }

    private Vec3d resolveCrosshairHitPosition(LivingEntity target) {
        Vec3d eyePosition = mc.player.getCameraPosVec(1.0F);
        Vec3d look = mc.player.getRotationVec(1.0F);
        Vec3d targetCenter = target.getBoundingBox().getCenter();
        double reach = Math.max(eyePosition.distanceTo(targetCenter) + 1.0D, 6.0D);
        Optional<Vec3d> hit = target.getBoundingBox().raycast(
                eyePosition,
                eyePosition.add(look.multiply(reach))
        );
        return hit.orElse(targetCenter);
    }

    private record HitBubble(Vec3d position, long spawnTime, float spinSeed, float sideYaw) {
    }
}

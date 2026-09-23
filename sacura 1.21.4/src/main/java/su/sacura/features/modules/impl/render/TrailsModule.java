package su.sacura.features.modules.impl.render;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import su.sacura.events.render.EventRender3D;
import su.sacura.events.tick.EventUpdate;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;
import su.sacura.util.impl.math.api.Animation;
import su.sacura.util.impl.math.helper.MathUtil;
import su.sacura.util.impl.math.helper.TimerUtil;
import su.sacura.util.impl.math.impl.EaseBackIn;
import su.sacura.util.impl.render.RenderHelper;
import su.sacura.util.impl.render.providers.ColorProvider;
import su.sacura.util.type.MinecraftWrapper;

@ModuleAnnotations(name = "Trails", category = Category.RENDER)
public class TrailsModule extends Module {
    private final Identifier IMAGE = Identifier.of("sacura/images/trails.png");
    private final List<Circle> circles = new ArrayList<>();
    private final Map<PlayerEntity, TimerUtil> spawnTimers = new HashMap<>();

    @Subscribe
    public void onEvent(EventUpdate event) {
        this.circles.removeIf(c -> (c.timer.getTime() > 8000L));
        Vec3d velocity = mc.player.getVelocity();
        boolean isMoving = (velocity.x * velocity.x + velocity.z * velocity.z > 0.001D);
        TimerUtil spawnTimer = this.spawnTimers.computeIfAbsent(mc.player, p -> new TimerUtil());
        if (isMoving && mc.player.isOnGround() && spawnTimer.hasTimeElapsed(150L)) {
            spawnTimer.reset();
            Vec3d spawnPos = new Vec3d(mc.player.getX(), Math.floor(mc.player.getY()) + 0.0010000000474974513D, mc.player.getZ());
            Circle circle = new Circle(spawnPos, new TimerUtil(), (Animation)new EaseBackIn(400, 1.0D, 1.3F));
            // Исправлено: field_11056 -> Direction.AxisDirection.POSITIVE
            circle.animation.setDirection(Direction.AxisDirection.POSITIVE);
            circle.yaw = getYawFromVelocity(velocity);
            this.circles.add(circle);
        }
    }

    private static float getYawFromVelocity(Vec3d velocity) {
        if (velocity.lengthSquared() < 1.0E-4D)
            return 0.0F;
        double dx = velocity.x;
        double dz = velocity.z;
        return (float)-(Math.atan2(dx, dz) * 57.29577951308232D);
    }

    @Subscribe
    private void render(EventRender3D eventRender3D) {
        Collections.reverse(this.circles);
        eventRender3D.getMatrixStack().push();
        RenderHelper.enable(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderTexture(0, this.IMAGE);
        // Исправлено: field_53880 -> ShaderProgramKeys.POSITION_TEX_COLOR
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        // Исправлено: field_27382 -> VertexFormat.DrawMode.QUADS
        BufferBuilder buffer = MinecraftWrapper.tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        for (Circle c : this.circles) {
            float elapsed = (float)c.timer.getTime();
            float alphaFade = MathUtil.clamp(1.0F - elapsed / 1200.0F, 0.0F, 1.0F);
            float animScale = (float)c.animation.getOutput();
            eventRender3D.getMatrixStack().push();
            eventRender3D.getMatrixStack().translate(c.pos.x -
                    (mc.getEntityRenderDispatcher()).camera.getPos().getX(), c.pos.y -
                    (mc.getEntityRenderDispatcher()).camera.getPos().getY(), c.pos.z -
                    (mc.getEntityRenderDispatcher()).camera.getPos().getZ());
            eventRender3D.getMatrixStack().multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            eventRender3D.getMatrixStack().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(c.yaw - 180.0F));
            RenderHelper.size(eventRender3D.getMatrixStack(), 0.0D, 0.0D, (animScale * 0.45F));
            float size = 1.0F;
            Matrix4f matrix = eventRender3D.getMatrixStack().peek().getPositionMatrix();
            buffer.vertex(matrix, -size, size, 0.0F).texture(0.0F, 1.0F).color(ColorProvider.applyOpacity(ColorProvider.getColorStyle(270.0F), alphaFade));
            buffer.vertex(matrix, size - 0.3F, size, 0.0F).texture(1.0F, 1.0F).color(ColorProvider.applyOpacity(ColorProvider.getColorStyle(0.0F), alphaFade));
            buffer.vertex(matrix, size - 0.3F, -size, 0.0F).texture(1.0F, 0.0F).color(ColorProvider.applyOpacity(ColorProvider.getColorStyle(180.0F), alphaFade));
            buffer.vertex(matrix, -size, -size, 0.0F).texture(0.0F, 0.0F).color(ColorProvider.applyOpacity(ColorProvider.getColorStyle(90.0F), alphaFade));
            eventRender3D.getMatrixStack().pop();
        }
        RenderHelper.end(buffer);
        RenderSystem.depthMask(true);
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        eventRender3D.getMatrixStack().pop();
        Collections.reverse(this.circles);
    }

    static class Circle {
        private final Vec3d pos;
        private final TimerUtil timer;
        private final Animation animation;
        private float yaw;

        public Circle(Vec3d pos, TimerUtil timer, Animation animation) {
            this.pos = pos;
            this.timer = timer;
            this.animation = animation;
        }

        public Vec3d pos() {
            return this.pos;
        }

        public TimerUtil timer() {
            return this.timer;
        }

        public Animation animation() {
            return this.animation;
        }

        public float yaw() {
            return this.yaw;
        }
    }
}
package su.sacura.features.modules.impl.render;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.network.ClientPlayerEntity;
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
import su.sacura.features.modules.settings.impl.ModeSetting;
import su.sacura.features.modules.settings.impl.SliderSetting;
import su.sacura.util.impl.math.api.Animation;
import su.sacura.util.impl.math.helper.MathUtil;
import su.sacura.util.impl.math.helper.TimerUtil;
import su.sacura.util.impl.math.impl.EaseBackIn;
import su.sacura.util.impl.render.RenderHelper;
import su.sacura.util.impl.render.providers.ColorProvider;
import su.sacura.util.type.MinecraftWrapper;

@ModuleAnnotations(name="Jump Circles", category=Category.RENDER, desc="Красивые круги при прыжке")
public class JumpCirclesModule extends Module {
    ModeSetting circleType = new ModeSetting("Картинка", "Новый", "Новый", "Старый").setDescription("Изменяет картинку круга");
    SliderSetting rotateSpeed = new SliderSetting("Скорость", 2.0f, 0.1f, 5.0f, 0.1f).setDescription("Изменяет скорость круга");
    SliderSetting circleScale = new SliderSetting("Размер", 1.0f, 0.5f, 5.0f, 0.1f).setDescription("Изменяет размер круга");
    Identifier newt = Identifier.of("sacura/images/circles/new.png");
    Identifier oldt = Identifier.of("sacura/images/circles/old.png");
    List<Circle> circles = new ArrayList<Circle>();
    Map<PlayerEntity, Boolean> wasOnGround = new HashMap<PlayerEntity, Boolean>();
    Identifier texture = null;

    public JumpCirclesModule() {
        this.addSettings(this.circleType, this.rotateSpeed, this.circleScale);
    }

    @Subscribe
    public void onEvent(EventUpdate event) {
        this.circles.removeIf(c -> c.timer.getTime() > 8000L);
        ClientPlayerEntity player = JumpCirclesModule.mc.player;
        // Исправлено: method_31481 -> isUsingItem
        if (player == null || player.isUsingItem()) {
            return;
        }
        boolean previouslyOnGround = this.wasOnGround.getOrDefault(player, true);
        // Исправлено: method_24828 -> isOnGround
        boolean currentlyOnGround = player.isOnGround();
        if (previouslyOnGround && !currentlyOnGround) {
            Circle circle = new Circle(new Vec3d(player.getX(), Math.floor(player.getY()) + (double)0.001f, player.getZ()), new TimerUtil(), new EaseBackIn(400, 1.0, 1.3f));
            // Исправлено: field_11056 -> Direction.AxisDirection.POSITIVE
            circle.animation.setDirection(Direction.AxisDirection.POSITIVE);
            this.circles.add(circle);
        }
        this.wasOnGround.put((PlayerEntity)player, currentlyOnGround);
    }

    @Subscribe
    private void renderCircles(EventRender3D eventRender3D) {
        Collections.reverse(this.circles);
        eventRender3D.getMatrixStack().push();
        RenderHelper.enable(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        switch ((String)this.circleType.get()) {
            case "Новый": {
                this.texture = this.newt;
                break;
            }
            case "Старый": {
                this.texture = this.oldt;
            }
        }
        RenderSystem.setShaderTexture(0, this.texture);
        // Исправлено: POSITION_TEXTURE_COLOR -> POSITION_TEX_COLOR
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        // Исправлено: field_27382 -> VertexFormat.DrawMode.QUADS
        BufferBuilder buffer = MinecraftWrapper.tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        for (Circle c : this.circles) {
            float elapsed = c.timer.getTime();
            float alphaFade = MathUtil.clamp(1.0f - elapsed / 8000.0f, 0.0f, 1.0f);
            float animScale = (float)c.animation.getOutput();
            eventRender3D.getMatrixStack().push();
            // Исправлено: method_10216 -> getX, method_10214 -> getY, method_10215 -> getZ
            eventRender3D.getMatrixStack().translate(c.pos().x - JumpCirclesModule.mc.getEntityRenderDispatcher().camera.getPos().getX(), c.pos().y - JumpCirclesModule.mc.getEntityRenderDispatcher().camera.getPos().getY(), c.pos().z - JumpCirclesModule.mc.getEntityRenderDispatcher().camera.getPos().getZ());
            eventRender3D.getMatrixStack().multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
            eventRender3D.getMatrixStack().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(elapsed / 50.0f * ((Float)this.rotateSpeed.get()).floatValue()));
            RenderHelper.size(eventRender3D.getMatrixStack(), 0.0, 0.0, animScale * ((Float)this.circleScale.get()).floatValue());
            float size = 1.0f;
            Matrix4f matrix = eventRender3D.getMatrixStack().peek().getPositionMatrix();
            // Исправлено: method_22918 -> vertex
            buffer.vertex(matrix, -size, size, 0.0f).texture(0.0f, 1.0f).color(ColorProvider.applyOpacity(ColorProvider.getColorStyle(270.0f), alphaFade));
            buffer.vertex(matrix, size, size, 0.0f).texture(1.0f, 1.0f).color(ColorProvider.applyOpacity(ColorProvider.getColorStyle(0.0f), alphaFade));
            buffer.vertex(matrix, size, -size, 0.0f).texture(1.0f, 0.0f).color(ColorProvider.applyOpacity(ColorProvider.getColorStyle(180.0f), alphaFade));
            buffer.vertex(matrix, -size, -size, 0.0f).texture(0.0f, 0.0f).color(ColorProvider.applyOpacity(ColorProvider.getColorStyle(90.0f), alphaFade));
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

    record Circle(Vec3d pos, TimerUtil timer, Animation animation) {
    }
}
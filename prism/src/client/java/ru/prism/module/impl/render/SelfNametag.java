package ru.prism.module.impl.render;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import ru.prism.manager.event_impl.EventRender3D;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;

@ModuleInfo(
        name = "Self Nametag",
        desc = "Показывает твой ник над головой в третьем лице.",
        category = Category.VISUALS
)
public class SelfNametag extends Module {

    private final BufferAllocator allocator = new BufferAllocator(1 << 12);

    @EventHandler
    public void onRender(EventRender3D e) {
        if (mc.player == null || mc.world == null) return;
        if (mc.options.getPerspective().isFirstPerson()) return;

        float tickDelta = e.getTickDelta();
        Vec3d camPos = mc.gameRenderer.getCamera().getCameraPos();
        Vec3d playerPos = mc.player.getLerpedPos(tickDelta);
        Vec3d labelOffset = mc.player.getAttachments().getPoint(EntityAttachmentType.NAME_TAG, 0, mc.player.getLerpedYaw(tickDelta));

        MatrixStack matrices = e.getMatrixStack();
        matrices.push();
        matrices.translate(playerPos.x - camPos.x, playerPos.y - camPos.y, playerPos.z - camPos.z);
        matrices.translate(labelOffset.x, labelOffset.y + 0.5, labelOffset.z);
        matrices.multiply(mc.gameRenderer.getCamera().getRotation());
        matrices.scale(0.025F, -0.025F, 0.025F);
        Matrix4f matrix = new Matrix4f(matrices.peek().getPositionMatrix());
        matrices.pop();

        Text text = mc.player.getDisplayName();
        float x = -mc.textRenderer.getWidth(text) / 2F;
        int light = mc.getEntityRenderDispatcher().getLight(mc.player, tickDelta);
        int background = mc.options.getTextBackgroundColor(0.25F);

        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(allocator);

        if (mc.player.isSneaking()) {
            mc.textRenderer.draw(text, x, 0F, 0x80FFFFFF, false, matrix, immediate, TextRenderer.TextLayerType.NORMAL, background, light);
        } else {
            mc.textRenderer.draw(text, x, 0F, 0xFFFFFFFF, false, matrix, immediate, TextRenderer.TextLayerType.NORMAL, background, LightmapTextureManager.applyEmission(light, 2));
            mc.textRenderer.draw(text, x, 0F, 0x80FFFFFF, false, matrix, immediate, TextRenderer.TextLayerType.SEE_THROUGH, background, light);
        }

        immediate.draw();
    }
}

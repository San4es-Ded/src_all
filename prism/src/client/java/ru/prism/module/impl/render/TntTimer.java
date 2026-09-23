package ru.prism.module.impl.render;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import ru.prism.manager.event_impl.EventRender3D;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;

@ModuleInfo(
        name = "TNT Timer",
        desc = "Пишет над зажжённой ТНТ, сколько ей осталось тикать до взрыва.",
        category = Category.VISUALS
)
public class TntTimer extends Module {

    private final BufferAllocator allocator = new BufferAllocator(1 << 12);

    @EventHandler
    public void onRender(EventRender3D e) {
        if (mc.player == null || mc.world == null) return;

        MatrixStack matrices = e.getMatrixStack();
        Vec3d camPos = mc.gameRenderer.getCamera().getCameraPos();
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(allocator);

        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof TntEntity tnt)) continue;

            int fuse = tnt.getFuse();
            if (fuse <= 0) continue;

            float time = fuse / 20.0F;
            String text = String.format("%.1fc", time);

            float pulse = 1.0F;
            if (time < 2.0F) {
                pulse = (float) Math.sin(System.currentTimeMillis() % 1000L / 1000.0 * Math.PI * (2.0F + (2.0F - time) * 3.0F)) * 0.5F + 0.5F;
            }

            float fade = Math.min(time / 4.0F, 1.0F);
            int alpha = (int) (pulse * 255.0F);
            int color = (alpha << 24) | 0xFF0000 | ((int) (fade * 255.0F) << 8) | (int) (fade * 255.0F);

            matrices.push();
            matrices.translate(tnt.getX() - camPos.x, tnt.getBoundingBox().maxY + 0.25 - camPos.y, tnt.getZ() - camPos.z);
            matrices.multiply(mc.gameRenderer.getCamera().getRotation());
            matrices.scale(0.025F, -0.025F, 0.025F);
            Matrix4f matrix = new Matrix4f(matrices.peek().getPositionMatrix());
            matrices.pop();

            float x = -mc.textRenderer.getWidth(text) / 2.0F;
            mc.textRenderer.draw(text, x, 0.0F, color, false, matrix, immediate, TextRenderer.TextLayerType.NORMAL, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        }

        immediate.draw();
    }
}

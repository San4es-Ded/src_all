package ru.prism.module.impl.render;

import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import ru.prism.manager.event_impl.EventRender3D;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.ColorSetting;
import ru.prism.module.api.settings.impl.ModeSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.colors.ColorUtil;

@ModuleInfo(
        name = "Hitbox Customizer",
        desc = "Настраивает хитбоксы: обводка, углы, заливка и линии взгляда.",
        category = Category.VISUALS
)
public class HitboxCustomizer extends Module {

    public final ModeSetting targets = new ModeSetting(this, "Цели", "Все", "Игроки", "Мобы");
    public final SliderSetting maxDistance = new SliderSetting(this, "Дальность", 64.0F, 8.0F, 256.0F, 8.0F);
    public final ModeSetting outlineMode = new ModeSetting(this, "Режим обводки", "Обычный", "Углы");
    public final SliderSetting edgeLength = new SliderSetting(this, "Длина рёбер", 0.5F, 0.1F, 1.0F, 0.05F)
            .setVisible(() -> outlineMode.is("Углы"));
    public final SliderSetting lineThickness = new SliderSetting(this, "Толщина линий", 1.0F, 0.5F, 4.0F, 0.25F);
    public final BooleanSetting lookLines = new BooleanSetting(this, "Линии взгляда", false);
    public final SliderSetting lookLength = new SliderSetting(this, "Длина взгляда", 2.0F, 0.5F, 8.0F, 0.5F)
            .setVisible(() -> lookLines.getValue());
    public final BooleanSetting fill = new BooleanSetting(this, "Заливка", true);
    public final SliderSetting fillAlpha = new SliderSetting(this, "Прозрачность заливки", 64.0F, 0.0F, 255.0F, 5.0F)
            .setVisible(() -> fill.getValue());
    public final BooleanSetting clientColor = new BooleanSetting(this, "Цвет клиента", true);
    public final ColorSetting customColor = new ColorSetting(this, "Кастомный цвет", ColorUtil.WHITE)
            .setVisible(() -> !clientColor.getValue());

    @EventHandler
    public void onRender(EventRender3D event) {
        if (mc.player == null || mc.world == null || mc.gameRenderer == null) return;

        MatrixStack matrices = event.getMatrixStack();
        Vec3d cam = mc.gameRenderer.getCamera().getCameraPos();
        float tickDelta = event.getTickDelta();
        int color = clientColor.getValue() ? ColorUtil.getClientColor1(1) : customColor.getValue();
        int cr = (color >> 16) & 0xFF;
        int cg = (color >> 8) & 0xFF;
        int cb = color & 0xFF;
        Matrix4f mat = matrices.peek().getPositionMatrix();
        boolean firstPerson = mc.options.getPerspective().isFirstPerson();
        double maxDist = maxDistance.getValue();
        int fillA = fillAlpha.getValue().intValue();

        BufferAllocator allocator = new BufferAllocator(1 << 18);

        try {
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(allocator);

            for (Entity entity : mc.world.getEntities()) {
                if ((entity == mc.player && firstPerson) || !(entity instanceof LivingEntity)) continue;
                if (entity.isInvisible()) continue;
                if (targets.is("Игроки") && !(entity instanceof PlayerEntity)) continue;
                if (targets.is("Мобы") && entity instanceof PlayerEntity) continue;
                if (entity.squaredDistanceTo(cam) > maxDist * maxDist) continue;

                Vec3d offset = entity.getLerpedPos(tickDelta).subtract(entity.getEntityPos());
                Box box = entity.getBoundingBox().offset(offset);

                double x1 = box.minX - cam.x;
                double y1 = box.minY - cam.y;
                double z1 = box.minZ - cam.z;
                double x2 = box.maxX - cam.x;
                double y2 = box.maxY - cam.y;
                double z2 = box.maxZ - cam.z;

                if (fill.getValue()) {
                    drawFilledBox(mat, immediate.getBuffer(RenderLayers.debugQuads()), x1, y1, z1, x2, y2, z2, cr, cg, cb, fillA);
                }

                VertexConsumer lines = immediate.getBuffer(RenderLayers.linesTranslucent());
                if (outlineMode.is("Углы")) {
                    drawCornerBox(mat, lines, x1, y1, z1, x2, y2, z2, cr, cg, cb, 255, edgeLength.getValue() / 4.0F);
                } else {
                    drawOutlineBox(mat, lines, x1, y1, z1, x2, y2, z2, cr, cg, cb, 255);
                }

                if (lookLines.getValue()) {
                    Vec3d eye = entity.getEyePos().add(offset);
                    Vec3d look = entity.getRotationVec(tickDelta);
                    Vec3d end = eye.add(look.multiply(lookLength.getValue()));
                    drawLine(
                            lines,
                            mat,
                            (float) (eye.x - cam.x),
                            (float) (eye.y - cam.y),
                            (float) (eye.z - cam.z),
                            (float) (end.x - cam.x),
                            (float) (end.y - cam.y),
                            (float) (end.z - cam.z),
                            cr,
                            cg,
                            cb,
                            255
                    );
                }
            }

            immediate.draw();
        } finally {
            allocator.close();
        }
    }

    private void drawFilledBox(Matrix4f mat, VertexConsumer buf, double x1, double y1, double z1, double x2, double y2, double z2, int r, int g, int b, int a) {
        float fx1 = (float) x1;
        float fy1 = (float) y1;
        float fz1 = (float) z1;
        float fx2 = (float) x2;
        float fy2 = (float) y2;
        float fz2 = (float) z2;
        buf.vertex(mat, fx1, fy1, fz1).color(r, g, b, a);
        buf.vertex(mat, fx2, fy1, fz1).color(r, g, b, a);
        buf.vertex(mat, fx2, fy1, fz2).color(r, g, b, a);
        buf.vertex(mat, fx1, fy1, fz2).color(r, g, b, a);
        buf.vertex(mat, fx1, fy2, fz1).color(r, g, b, a);
        buf.vertex(mat, fx1, fy2, fz2).color(r, g, b, a);
        buf.vertex(mat, fx2, fy2, fz2).color(r, g, b, a);
        buf.vertex(mat, fx2, fy2, fz1).color(r, g, b, a);
        buf.vertex(mat, fx1, fy1, fz1).color(r, g, b, a);
        buf.vertex(mat, fx1, fy2, fz1).color(r, g, b, a);
        buf.vertex(mat, fx2, fy2, fz1).color(r, g, b, a);
        buf.vertex(mat, fx2, fy1, fz1).color(r, g, b, a);
        buf.vertex(mat, fx1, fy1, fz2).color(r, g, b, a);
        buf.vertex(mat, fx2, fy1, fz2).color(r, g, b, a);
        buf.vertex(mat, fx2, fy2, fz2).color(r, g, b, a);
        buf.vertex(mat, fx1, fy2, fz2).color(r, g, b, a);
        buf.vertex(mat, fx1, fy1, fz1).color(r, g, b, a);
        buf.vertex(mat, fx1, fy1, fz2).color(r, g, b, a);
        buf.vertex(mat, fx1, fy2, fz2).color(r, g, b, a);
        buf.vertex(mat, fx1, fy2, fz1).color(r, g, b, a);
        buf.vertex(mat, fx2, fy1, fz1).color(r, g, b, a);
        buf.vertex(mat, fx2, fy2, fz1).color(r, g, b, a);
        buf.vertex(mat, fx2, fy2, fz2).color(r, g, b, a);
        buf.vertex(mat, fx2, fy1, fz2).color(r, g, b, a);
    }

    private void drawOutlineBox(Matrix4f mat, VertexConsumer buf, double x1, double y1, double z1, double x2, double y2, double z2, int r, int g, int b, int a) {
        float fx1 = (float) x1;
        float fy1 = (float) y1;
        float fz1 = (float) z1;
        float fx2 = (float) x2;
        float fy2 = (float) y2;
        float fz2 = (float) z2;
        drawLine(buf, mat, fx1, fy1, fz1, fx2, fy1, fz1, r, g, b, a);
        drawLine(buf, mat, fx2, fy1, fz1, fx2, fy1, fz2, r, g, b, a);
        drawLine(buf, mat, fx2, fy1, fz2, fx1, fy1, fz2, r, g, b, a);
        drawLine(buf, mat, fx1, fy1, fz2, fx1, fy1, fz1, r, g, b, a);
        drawLine(buf, mat, fx1, fy2, fz1, fx2, fy2, fz1, r, g, b, a);
        drawLine(buf, mat, fx2, fy2, fz1, fx2, fy2, fz2, r, g, b, a);
        drawLine(buf, mat, fx2, fy2, fz2, fx1, fy2, fz2, r, g, b, a);
        drawLine(buf, mat, fx1, fy2, fz2, fx1, fy2, fz1, r, g, b, a);
        drawLine(buf, mat, fx1, fy1, fz1, fx1, fy2, fz1, r, g, b, a);
        drawLine(buf, mat, fx2, fy1, fz1, fx2, fy2, fz1, r, g, b, a);
        drawLine(buf, mat, fx2, fy1, fz2, fx2, fy2, fz2, r, g, b, a);
        drawLine(buf, mat, fx1, fy1, fz2, fx1, fy2, fz2, r, g, b, a);
    }

    private void drawCornerBox(Matrix4f mat, VertexConsumer buf, double x1, double y1, double z1, double x2, double y2, double z2, int r, int g, int b, int a, float l) {
        float fx1 = (float) x1;
        float fy1 = (float) y1;
        float fz1 = (float) z1;
        float fx2 = (float) x2;
        float fy2 = (float) y2;
        float fz2 = (float) z2;
        float dx = fx2 - fx1;
        float dy = fy2 - fy1;
        float dz = fz2 - fz1;
        float dlx = dx * l;
        float dly = dy * l;
        float dlz = dz * l;
        drawLine(buf, mat, fx1, fy1, fz1, fx1 + dlx, fy1, fz1, r, g, b, a);
        drawLine(buf, mat, fx1, fy1, fz1, fx1, fy1 + dly, fz1, r, g, b, a);
        drawLine(buf, mat, fx1, fy1, fz1, fx1, fy1, fz1 + dlz, r, g, b, a);
        drawLine(buf, mat, fx2, fy1, fz1, fx2 - dlx, fy1, fz1, r, g, b, a);
        drawLine(buf, mat, fx2, fy1, fz1, fx2, fy1 + dly, fz1, r, g, b, a);
        drawLine(buf, mat, fx2, fy1, fz1, fx2, fy1, fz1 + dlz, r, g, b, a);
        drawLine(buf, mat, fx1, fy1, fz2, fx1 + dlx, fy1, fz2, r, g, b, a);
        drawLine(buf, mat, fx1, fy1, fz2, fx1, fy1 + dly, fz2, r, g, b, a);
        drawLine(buf, mat, fx1, fy1, fz2, fx1, fy1, fz2 - dlz, r, g, b, a);
        drawLine(buf, mat, fx2, fy1, fz2, fx2 - dlx, fy1, fz2, r, g, b, a);
        drawLine(buf, mat, fx2, fy1, fz2, fx2, fy1 + dly, fz2, r, g, b, a);
        drawLine(buf, mat, fx2, fy1, fz2, fx2, fy1, fz2 - dlz, r, g, b, a);
        drawLine(buf, mat, fx1, fy2, fz1, fx1 + dlx, fy2, fz1, r, g, b, a);
        drawLine(buf, mat, fx1, fy2, fz1, fx1, fy2 - dly, fz1, r, g, b, a);
        drawLine(buf, mat, fx1, fy2, fz1, fx1, fy2, fz1 + dlz, r, g, b, a);
        drawLine(buf, mat, fx2, fy2, fz1, fx2 - dlx, fy2, fz1, r, g, b, a);
        drawLine(buf, mat, fx2, fy2, fz1, fx2, fy2 - dly, fz1, r, g, b, a);
        drawLine(buf, mat, fx2, fy2, fz1, fx2, fy2, fz1 + dlz, r, g, b, a);
        drawLine(buf, mat, fx1, fy2, fz2, fx1 + dlx, fy2, fz2, r, g, b, a);
        drawLine(buf, mat, fx1, fy2, fz2, fx1, fy2 - dly, fz2, r, g, b, a);
        drawLine(buf, mat, fx1, fy2, fz2, fx1, fy2, fz2 - dlz, r, g, b, a);
        drawLine(buf, mat, fx2, fy2, fz2, fx2 - dlx, fy2, fz2, r, g, b, a);
        drawLine(buf, mat, fx2, fy2, fz2, fx2, fy2 - dly, fz2, r, g, b, a);
        drawLine(buf, mat, fx2, fy2, fz2, fx2, fy2, fz2 - dlz, r, g, b, a);
    }

    private void drawLine(VertexConsumer buf, Matrix4f mat, float x1, float y1, float z1, float x2, float y2, float z2, int r, int g, int b, int a) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dz = z2 - z1;
        float len = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
        if (len > 1.0E-4F) {
            dx /= len;
            dy /= len;
            dz /= len;
        }

        buf.vertex(mat, x1, y1, z1).color(r, g, b, a).normal(dx, dy, dz).lineWidth(lineThickness.getValue());
        buf.vertex(mat, x2, y2, z2).color(r, g, b, a).normal(dx, dy, dz).lineWidth(lineThickness.getValue());
    }
}

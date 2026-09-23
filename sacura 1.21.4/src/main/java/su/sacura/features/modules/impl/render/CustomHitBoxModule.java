package su.sacura.features.modules.impl.render;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import su.sacura.events.render.EventRender3D;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;
import su.sacura.features.modules.settings.impl.BooleanSetting;
import su.sacura.features.modules.settings.impl.SliderSetting;
import su.sacura.util.impl.render.RenderHelper;

@ModuleAnnotations(name="Custom Hit Box", category=Category.RENDER)
public class CustomHitBoxModule
        extends Module {
    private final BooleanSetting fill = new BooleanSetting("Заполнять бокс", true).setDescription("Заполняет цветом бокс");
    private final BooleanSetting line = new BooleanSetting("Изменять линии", true).setDescription("Изменяет линии бокса");
    private final SliderSetting lineWidth = ((SliderSetting)new SliderSetting("Ширина линий", 1.5f, 0.5f, 6.0f, 0.1f).setVisible(this.line::get)).setDescription("Изменять ширину линий");
    private final SliderSetting fillAlpha = ((SliderSetting)new SliderSetting("Прозрачность бокса", 90.0f, 0.0f, 255.0f, 1.0f).setVisible(this.fill::get)).setDescription("Изменят прозрачность бокса");
    private final SliderSetting outlineAlpha = ((SliderSetting)new SliderSetting("Прозрачность линий", 255.0f, 0.0f, 255.0f, 1.0f).setVisible(this.line::get)).setDescription("Изменят прозрачность линий");
    private boolean prevRenderHitboxes;

    public CustomHitBoxModule() {
        this.addSettings(this.fill, this.fillAlpha, this.line, this.lineWidth, this.outlineAlpha);
    }

    @Subscribe
    public void render(EventRender3D e) {
        if (CustomHitBoxModule.mc.player == null || CustomHitBoxModule.mc.world == null) {
            return;
        }
        this.renderCustomHitBoxDirect(e.getMatrixStack(), e.getDeltatick().getTickDelta(true));
    }

    private void renderCustomHitBoxDirect(MatrixStack matrices, float tickDelta) {
        BufferBuilder buffer;
        float a;
        float b;
        float g;
        float r;
        RenderHelper.enable(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc((int)515);
        RenderSystem.depthMask((boolean)false);
        RenderSystem.disableCull();
        ArrayList<Box> boxes = new ArrayList<Box>();
        for (Entity entity : CustomHitBoxModule.mc.world.getEntities()) {
            if (!this.shouldRender(entity)) continue;
            boxes.add(this.getInterpolatedBox(entity, tickDelta));
        }
        if (boxes.isEmpty()) {
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.depthMask((boolean)true);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            return;
        }
        Color theme = new Color(113, 106, 223, 255);
        Vec3d camera = CustomHitBoxModule.mc.gameRenderer.getCamera().getPos();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        if (((Boolean)this.fill.get()).booleanValue()) {
            Color fillColor = new Color(theme.getRed(), theme.getGreen(), theme.getBlue(), ((Float)this.fillAlpha.get()).intValue());
            r = (float)fillColor.getRed() / 255.0f;
            g = (float)fillColor.getGreen() / 255.0f;
            b = (float)fillColor.getBlue() / 255.0f;
            a = (float)fillColor.getAlpha() / 255.0f;
            RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
            // ИСПРАВЛЕНО: TRIANGLES -> QUADS. Иначе вместо полного квадрата заливается
            // только один треугольник из пары (вторая половина пропадает).
            buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            for (Box box : boxes) {
                this.renderEntityBoxFill(buffer, matrix, box, r, g, b, a, camera);
            }
            RenderHelper.end(buffer);
        }
        if (((Boolean)this.line.get()).booleanValue()) {
            Color outlineColor = new Color(theme.getRed(), theme.getGreen(), theme.getBlue(), ((Float)this.outlineAlpha.get()).intValue());
            r = (float)outlineColor.getRed() / 255.0f;
            g = (float)outlineColor.getGreen() / 255.0f;
            b = (float)outlineColor.getBlue() / 255.0f;
            a = (float)outlineColor.getAlpha() / 255.0f;
            RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
            // Устанавливаем толщину линий
            RenderSystem.lineWidth(((Float)this.lineWidth.get()).floatValue());
            buffer = tessellator.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
            for (Box box : boxes) {
                this.renderEntityBoxOutline(buffer, matrix, box, r, g, b, a, camera);
            }
            RenderHelper.end(buffer);
            // Сбрасываем толщину линий обратно, чтобы не влиять на другие модули
            RenderSystem.lineWidth(1.0f);
        }
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private boolean shouldRender(Entity entity) {
        LivingEntity le;
        if (entity == CustomHitBoxModule.mc.player) {
            return false;
        }
        if (entity.isInvisible()) {
            return false;
        }
        if (entity instanceof LivingEntity && (le = (LivingEntity)entity).hasStatusEffect(StatusEffects.INVISIBILITY)) {
            return false;
        }
        Vec3d cameraPos = CustomHitBoxModule.mc.player.getCameraPosVec(1.0f);
        Vec3d entityPos = entity.getPos().add(0.0, (double)entity.getHeight() / 2.0, 0.0);
        Vec3d toEntity = entityPos.subtract(cameraPos).normalize();
        Vec3d forward = CustomHitBoxModule.mc.player.getRotationVec(1.0f);
        double dot = forward.dotProduct(toEntity);
        return dot > 0.0;
    }

    private Box getInterpolatedBox(Entity entity, float tickDelta) {
        Box worldBox = entity.getBoundingBox().expand(0.002);
        Vec3d interpolatedPos = entity.getLerpedPos(tickDelta);
        return worldBox.offset(interpolatedPos.subtract(entity.getPos()));
    }

    private void renderEntityBoxFill(BufferBuilder buffer, Matrix4f matrix, Box box, float r, float g, float b, float a, Vec3d camera) {
        float minX = (float)(box.minX - camera.x);
        float minY = (float)(box.minY - camera.y);
        float minZ = (float)(box.minZ - camera.z);
        float maxX = (float)(box.maxX - camera.x);
        float maxY = (float)(box.maxY - camera.y);
        float maxZ = (float)(box.maxZ - camera.z);
        buffer.vertex(matrix, minX, maxY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, minY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, minY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, minY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, minY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, minY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, maxY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, minY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, minY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, minY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, maxY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, minY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a);
    }

    private void renderEntityBoxOutline(BufferBuilder buffer, Matrix4f matrix, Box box, float r, float g, float b, float a, Vec3d camera) {
        float minX = (float)(box.minX - camera.x);
        float minY = (float)(box.minY - camera.y);
        float minZ = (float)(box.minZ - camera.z);
        float maxX = (float)(box.maxX - camera.x);
        float maxY = (float)(box.maxY - camera.y);
        float maxZ = (float)(box.maxZ - camera.z);
        buffer.vertex(matrix, minX, minY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, minY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, minY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, minY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, minY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, minY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, maxY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, maxY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, minY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, maxY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, minY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, minY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a);
        buffer.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a);
    }

    @Override
    public void onDisable() {
        EntityRenderDispatcher dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderHitboxes(this.prevRenderHitboxes);
        super.onDisable();
    }
}
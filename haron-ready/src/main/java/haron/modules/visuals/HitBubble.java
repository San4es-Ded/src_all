package haron.modules.visuals;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import haron.events.AttackTargetEvent;
import haron.events.WorldRenderPostEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.visuals.HitBubbleParticle;
import haron.render.icons.HaronIcons;
import haron.settings.NumberSetting;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

@ModuleInfo(a="Hit Bubble", b="Показывает анимированные пузыри при атаке", c=ModuleCategory.VISUALS)
public class HitBubble
extends HaronModule {
    private static final long LIFETIME_MS = 600L;
    private static final float RISE_DISTANCE = 0.6f;
    private static final float BASE_SIZE = 0.35f;
    private int hitCounter = 0;
    private final NumberSetting size = new NumberSetting("Размер", 0.35f, 0.15f, 0.8f, 0.05f);
    private final NumberSetting riseSpeed = new NumberSetting("Скорость подъёма", 0.6f, 0.1f, 1.5f, 0.05f);
    private final CopyOnWriteArrayList<HitBubbleParticle> bubbles = new CopyOnWriteArrayList();

    @EventHandler
    public void onAttack(AttackTargetEvent dt813s2) {
        Entity entity;
        if (this.k() && (entity = dt813s2.a()) != null) {
            double d = entity.getX();
            double d2 = entity.getY() + (double)entity.getHeight() * 0.5;
            double d3 = entity.getZ();
            if (HitBubble.c.player != null && HitBubble.c.gameRenderer != null) {
                float f = HitBubble.c.gameRenderer.getCamera().getYaw();
                float f2 = (float)Math.toRadians(-f);
                double d4 = -Math.sin(f2) * 0.4;
                double d5 = Math.cos(f2) * 0.4;
                d += d4;
                d3 += d5;
            }
            boolean bl = this.hitCounter % 2 == 0;
            ++this.hitCounter;
            this.bubbles.add(new HitBubbleParticle(d, d2, d3, bl ? "bubble_1" : "bubble_2", System.currentTimeMillis()));
        }
    }

    private void renderBubble(MatrixStack matrixStack, Vec3d vec3d, float f, float f2, HitBubbleParticle cvf4002, float f3) {
        double d = cvf4002.x - vec3d.x;
        double d2 = cvf4002.y - vec3d.y + (double)((1.0f - (1.0f - f3) * (1.0f - f3)) * this.riseSpeed.a());
        double d3 = cvf4002.z - vec3d.z;
        float f4 = MathHelper.clamp((float)(f3 < 0.15f ? f3 / 0.15f : (f3 > 0.7f ? 1.0f - (f3 - 0.7f) / 0.3f : 1.0f)), (float)0.0f, (float)1.0f);
        float f5 = f3 < 0.2f ? 0.4f + 0.8f * (f3 / 0.2f) : 1.0f;
        Identifier identifier = HaronIcons.get(cvf4002.textureKey);
        if (identifier == null) {
            return;
        }
        matrixStack.push();
        matrixStack.translate(d, d2, d3);
        matrixStack.multiply(new Quaternionf().rotationYXZ((float)Math.toRadians(-f), (float)Math.toRadians(f2), 0.0f));
        float f6 = this.size.a() * f5 / 2.0f;
        RenderSystem.setShaderTexture((int)0, (Identifier)identifier);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)f4);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        bufferBuilder.vertex(matrix4f, -f6, -f6, 0.0f).texture(0.0f, 0.0f).color(1.0f, 1.0f, 1.0f, f4);
        bufferBuilder.vertex(matrix4f, f6, -f6, 0.0f).texture(1.0f, 0.0f).color(1.0f, 1.0f, 1.0f, f4);
        bufferBuilder.vertex(matrix4f, f6, f6, 0.0f).texture(1.0f, 1.0f).color(1.0f, 1.0f, 1.0f, f4);
        bufferBuilder.vertex(matrix4f, -f6, f6, 0.0f).texture(0.0f, 1.0f).color(1.0f, 1.0f, 1.0f, f4);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        matrixStack.pop();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler
    public void onWorldRender(WorldRenderPostEvent kvprd92) {
        if (!this.k() || this.bubbles.isEmpty() || HitBubble.c.world == null || HitBubble.c.gameRenderer == null) {
            return;
        }
        MatrixStack matrixStack = kvprd92.a();
        Vec3d vec3d = HitBubble.c.gameRenderer.getCamera().getPos();
        float f = HitBubble.c.gameRenderer.getCamera().getYaw();
        float f2 = HitBubble.c.gameRenderer.getCamera().getPitch();
        long l = System.currentTimeMillis();
        ArrayList<HitBubbleParticle> arrayList = new ArrayList<HitBubbleParticle>();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc((GlStateManager.SrcFactor)GlStateManager.SrcFactor.SRC_ALPHA, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE);
        RenderSystem.disableCull();
        RenderSystem.depthMask((boolean)false);
        try {
            for (HitBubbleParticle cvf4002 : this.bubbles) {
                long l2 = l - cvf4002.spawnTime;
                if (l2 >= 600L) {
                    arrayList.add(cvf4002);
                    continue;
                }
                this.renderBubble(matrixStack, vec3d, f, f2, cvf4002, (float)l2 / 600.0f);
            }
        }
        catch (Exception exception) {
            arrayList.clear();
            this.bubbles.clear();
        }
        finally {
            RenderSystem.depthMask((boolean)true);
            RenderSystem.enableCull();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
        if (!arrayList.isEmpty()) {
            this.bubbles.removeAll(arrayList);
        }
    }
}


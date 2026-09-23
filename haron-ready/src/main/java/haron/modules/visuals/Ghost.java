package haron.modules.visuals;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import haron.events.WorldRenderPostEvent;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.BooleanSetting;
import java.awt.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

@ModuleInfo(a="Ghost", b="Soft neon ghost orbs around player", c=ModuleCategory.VISUALS)
public class Ghost
extends HaronModule {
    private static final Identifier GLOW = Identifier.of((String)"haron", (String)"textures/particle/glow.png");
    private final NumberSetting count = new NumberSetting("Кол-во", 4.0f, 2.0f, 8.0f, 1.0f);
    private final NumberSetting size = new NumberSetting("Размер", 0.42f, 0.15f, 1.0f, 0.01f);
    private final NumberSetting speed = new NumberSetting("Скорость", 1.0f, 0.2f, 2.5f, 0.05f);
    private final NumberSetting alpha = new NumberSetting("Прозрачность", 0.75f, 0.2f, 1.0f, 0.05f);
    private final BooleanSetting firstPerson = new BooleanSetting("От первого лица", false);

    @EventHandler
    public void onRender(WorldRenderPostEvent kvprd92) {
        if (Ghost.c.player == null || Ghost.c.world == null) {
            return;
        }
        if (!this.firstPerson.get() && Ghost.c.options.getPerspective() == Perspective.FIRST_PERSON) {
            return;
        }
        MatrixStack matrixStack = kvprd92.a();
        Vec3d vec3d = Ghost.c.gameRenderer.getCamera().getPos();
        Vec3d vec3d2 = Ghost.c.player.getLerpedPos(kvprd92.b());
        float f = ((float)Ghost.c.player.age + kvprd92.b()) * 0.045f * this.speed.get();
        Color color = ModuleManager.CLIENT_COLOR.n();
        Color color2 = ModuleManager.CLIENT_COLOR.n();
        int n = (int)this.count.get();
        float f2 = this.size.get();
        float f3 = this.alpha.get();
        boolean bl = Ghost.c.options.getPerspective() == Perspective.FIRST_PERSON;
        RenderSystem.enableBlend();
        RenderSystem.blendFunc((GlStateManager.SrcFactor)GlStateManager.SrcFactor.SRC_ALPHA, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.depthMask((boolean)false);
        for (int i = 0; i < n; ++i) {
            float f4 = f + (float)i * ((float)Math.PI * 2 / (float)n);
            Color color3 = i % 2 == 0 ? color : color2;
            int n2 = 10;
            for (int j = 9; j >= 0; --j) {
                float f5 = f4 - (float)j * 0.085f;
                float f6 = 1.0f - (float)j / 10.0f;
                Vec3d vec3d3 = this.orbit(vec3d2, f5, i);
                if (bl && vec3d3.squaredDistanceTo(vec3d) < 0.3) continue;
                float f7 = (0.12f + f6 * 0.28f) * f2;
                int n3 = (int)(f6 * f6 * (j == 0 ? 170.0f : 90.0f) * f3);
                this.drawOrb(matrixStack, vec3d, vec3d3, f7, color3, n3, j == 0);
            }
        }
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private void drawOrb(MatrixStack matrixStack, Vec3d vec3d, Vec3d vec3d2, float f, Color color, int n, boolean bl) {
        matrixStack.push();
        matrixStack.translate(vec3d2.x - vec3d.x, vec3d2.y - vec3d.y, vec3d2.z - vec3d.z);
        matrixStack.multiply(Ghost.c.gameRenderer.getCamera().getRotation());
        matrixStack.scale(-f, -f, f);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture((int)0, (Identifier)GLOW);
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        int n2 = Math.max(0, Math.min(255, n)) << 24 | color.getRed() << 16 | color.getGreen() << 8 | color.getBlue();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(matrix4f, -0.5f, -0.5f, 0.0f).texture(0.0f, 1.0f).color(n2);
        bufferBuilder.vertex(matrix4f, -0.5f, 0.5f, 0.0f).texture(0.0f, 0.0f).color(n2);
        bufferBuilder.vertex(matrix4f, 0.5f, 0.5f, 0.0f).texture(1.0f, 0.0f).color(n2);
        bufferBuilder.vertex(matrix4f, 0.5f, -0.5f, 0.0f).texture(1.0f, 1.0f).color(n2);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        if (bl) {
            matrixStack.scale(0.38f, 0.38f, 0.38f);
            Matrix4f matrix4f2 = matrixStack.peek().getPositionMatrix();
            int n3 = Math.min(255, n + 20);
            int n4 = n3 << 24 | Math.min(255, color.getRed() + 30) << 16 | Math.min(255, color.getGreen() + 30) << 8 | Math.min(255, color.getBlue() + 30);
            BufferBuilder bufferBuilder2 = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder2.vertex(matrix4f2, -0.5f, -0.5f, 0.0f).texture(0.0f, 1.0f).color(n4);
            bufferBuilder2.vertex(matrix4f2, -0.5f, 0.5f, 0.0f).texture(0.0f, 0.0f).color(n4);
            bufferBuilder2.vertex(matrix4f2, 0.5f, 0.5f, 0.0f).texture(1.0f, 0.0f).color(n4);
            bufferBuilder2.vertex(matrix4f2, 0.5f, -0.5f, 0.0f).texture(1.0f, 1.0f).color(n4);
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder2.end());
        }
        matrixStack.pop();
    }

    private Vec3d orbit(Vec3d vec3d, float f, int n) {
        double d = 0.85 + Math.sin((double)f * 0.7 + (double)n) * 0.14;
        double d2 = 0.55 + Math.sin((double)f * 2.0) * 0.22 + (double)(n % 2) * 0.1;
        return vec3d.add(Math.cos(f) * d, d2, Math.sin(f) * d);
    }
}


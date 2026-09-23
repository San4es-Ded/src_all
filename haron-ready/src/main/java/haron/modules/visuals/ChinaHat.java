package haron.modules.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.events.WorldRenderPostEvent;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.SettingGroup;
import haron.settings.BooleanSetting;
import java.awt.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

@ModuleInfo(a="China Hat", b="Draws a cone hat above players.", c=ModuleCategory.VISUALS)
public class ChinaHat
extends HaronModule {
    private static final int SEGMENTS = 90;
    private final NumberSetting height = new NumberSetting("Высота", 0.3f, 0.1f, 0.6f, 0.05f);
    private final NumberSetting radius = new NumberSetting("Радиус", 0.5f, 0.1f, 1.0f, 0.05f);
    private final BooleanSetting followSneaking = new BooleanSetting("Следовать за приседанием", true);
    private final SettingGroup colorGroup = new SettingGroup("Цвет");
    private final BooleanSetting useClientColor = new BooleanSetting("Цвет клиента", true);
    private final ColorSetting color = new ColorSetting("Кастомный цвет", new Color(120, 80, 255)).a(() -> {
        return !this.useClientColor.a();
    });

    private static void vertex(BufferBuilder bufferBuilder, Matrix4f matrix4f, double d, double d2, double d3, Color color, float f) {
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d3).color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, f);
    }

    private void renderHat(MatrixStack matrixStack, Vec3d vec3d, PlayerEntity playerEntity, float f) {
        float f2;
        double d = MathHelper.lerp((double)f, (double)playerEntity.prevX, (double)playerEntity.getX()) - vec3d.x;
        double d2 = MathHelper.lerp((double)f, (double)playerEntity.prevZ, (double)playerEntity.getZ()) - vec3d.z;
        double d3 = MathHelper.lerp((double)f, (double)playerEntity.prevY, (double)playerEntity.getY()) - vec3d.y + (double)playerEntity.getHeight() + 0.05;
        if (this.followSneaking.a() && playerEntity.isSneaking()) {
            d3 -= 0.18;
        }
        float f3 = this.radius.a();
        float f4 = this.height.a();
        Color color = this.n();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
        for (int i = 0; i < 90; ++i) {
            float f5 = (float)i / 90.0f;
            f2 = (float)(i + 1) / 90.0f;
            double d4 = (double)f5 * Math.PI * 2.0;
            double d5 = (double)f2 * Math.PI * 2.0;
            Color color2 = this.a(color, f5);
            ChinaHat.vertex(bufferBuilder, matrix4f, d, d3 + (double)f4, d2, color2, 0.55f);
            ChinaHat.vertex(bufferBuilder, matrix4f, d + Math.cos(d4) * (double)f3, d3, d2 + Math.sin(d4) * (double)f3, color2, 0.28f);
            ChinaHat.vertex(bufferBuilder, matrix4f, d + Math.cos(d5) * (double)f3, d3, d2 + Math.sin(d5) * (double)f3, this.a(color, f2), 0.28f);
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.lineWidth((float)2.0f);
        BufferBuilder bufferBuilder2 = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        for (int i = 0; i < 90; ++i) {
            f2 = (float)i / 90.0f;
            float f6 = (float)(i + 1) / 90.0f;
            double d6 = (double)f2 * Math.PI * 2.0;
            double d7 = (double)f6 * Math.PI * 2.0;
            ChinaHat.vertex(bufferBuilder2, matrix4f, d + Math.cos(d6) * (double)f3, d3, d2 + Math.sin(d6) * (double)f3, this.a(color, f2), 0.95f);
            ChinaHat.vertex(bufferBuilder2, matrix4f, d + Math.cos(d7) * (double)f3, d3, d2 + Math.sin(d7) * (double)f3, this.a(color, f6), 0.95f);
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder2.end());
        RenderSystem.lineWidth((float)1.0f);
    }

    private Color n() {
        return this.useClientColor.a() ? ModuleManager.CLIENT_COLOR.n() : this.color.a();
    }

    private Color a(Color color, float f) {
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        fArray[0] = (fArray[0] + (float)Math.sin((double)f * Math.PI) * 0.08f) % 1.0f;
        if (fArray[0] < 0.0f) {
            fArray[0] = fArray[0] + 1.0f;
        }
        return Color.getHSBColor(fArray[0], fArray[1], fArray[2]);
    }

    @EventHandler
    public void a(WorldRenderPostEvent kvprd92) {
        if (ChinaHat.c.player == null || ChinaHat.c.world == null || ChinaHat.c.gameRenderer == null) {
            return;
        }
        MatrixStack matrixStack = kvprd92.a();
        Vec3d vec3d = ChinaHat.c.gameRenderer.getCamera().getPos();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)false);
        for (AbstractClientPlayerEntity abstractClientPlayerEntity : ChinaHat.c.world.getPlayers()) {
            if (abstractClientPlayerEntity.isInvisible() || abstractClientPlayerEntity == ChinaHat.c.player && ChinaHat.c.options.getPerspective().isFirstPerson()) continue;
            this.renderHat(matrixStack, vec3d, (PlayerEntity)abstractClientPlayerEntity, kvprd92.b());
        }
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        matrixStack.pop();
    }
}


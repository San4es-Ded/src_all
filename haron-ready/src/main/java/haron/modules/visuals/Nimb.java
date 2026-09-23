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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

@ModuleInfo(a="Nimb", b="Draws a halo above the player.", c=ModuleCategory.VISUALS)
public class Nimb
extends HaronModule {
    private static final int SEGMENTS = 72;
    private final NumberSetting lineWidth = new NumberSetting("Толщина линии", 1.5f, 1.0f, 3.0f, 0.5f);
    private final NumberSetting radius = new NumberSetting("Радиус", 0.28f, 0.18f, 0.55f, 0.02f);
    private final NumberSetting yOffset = new NumberSetting("Смещение Y", 0.05f, -0.3f, 0.25f, 0.02f);
    private final SettingGroup colorGroup = new SettingGroup("Цвет");
    private final BooleanSetting useClientColor = new BooleanSetting("Цвет клиента", true);
    private final ColorSetting color = new ColorSetting("Кастомный цвет", Color.WHITE).a(() -> {
        return !this.useClientColor.a();
    });
    private static final double MAX_RENDER_DISTANCE_SQ = 16384.0;
    private static double[] cosTable = new double[73];
    private static double[] sinTable = new double[73];

    static {
        for (int i = 0; i <= 72; ++i) {
            double d = (double)i / 72.0 * Math.PI * 2.0;
            Nimb.cosTable[i] = Math.cos(d);
            Nimb.sinTable[i] = Math.sin(d);
        }
    }

    private Color n() {
        return this.useClientColor.a() ? ModuleManager.CLIENT_COLOR.n() : this.color.a();
    }

    @EventHandler
    public void a(WorldRenderPostEvent kvprd92) {
        if (Nimb.c.player == null || Nimb.c.world == null || Nimb.c.gameRenderer == null) {
            return;
        }
        MatrixStack matrixStack = kvprd92.a();
        Vec3d vec3d = Nimb.c.gameRenderer.getCamera().getPos();
        boolean bl = Nimb.c.options.getPerspective().isFirstPerson();
        double d = Nimb.c.player.getX();
        double d2 = Nimb.c.player.getY();
        double d3 = Nimb.c.player.getZ();
        Color color = this.n();
        float f = (float)color.getRed() / 255.0f;
        float f2 = (float)color.getGreen() / 255.0f;
        float f3 = (float)color.getBlue() / 255.0f;
        float f4 = 0.9f;
        float f5 = kvprd92.b();
        float f6 = this.yOffset.a();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.lineWidth((float)this.lineWidth.a());
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = null;
        for (AbstractClientPlayerEntity abstractClientPlayerEntity : Nimb.c.world.getPlayers()) {
            double d4;
            double d5;
            double d6;
            if (abstractClientPlayerEntity.isInvisible() || abstractClientPlayerEntity == Nimb.c.player && bl || (d6 = abstractClientPlayerEntity.getX() - d) * d6 + (d5 = abstractClientPlayerEntity.getY() - d2) * d5 + (d4 = abstractClientPlayerEntity.getZ() - d3) * d4 > 16384.0) continue;
            float f7 = this.radius.a();
            double d7 = MathHelper.lerp((double)f5, (double)abstractClientPlayerEntity.prevX, (double)abstractClientPlayerEntity.getX()) - vec3d.x;
            double d8 = MathHelper.lerp((double)f5, (double)abstractClientPlayerEntity.prevZ, (double)abstractClientPlayerEntity.getZ()) - vec3d.z;
            double d9 = MathHelper.lerp((double)f5, (double)abstractClientPlayerEntity.prevY, (double)abstractClientPlayerEntity.getY()) - vec3d.y + (double)abstractClientPlayerEntity.getEyeHeight(abstractClientPlayerEntity.getPose()) + 0.05 + (double)f6;
            if (bufferBuilder == null) {
                bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
            }
            for (int i = 0; i < 72; ++i) {
                double d10 = d7 + cosTable[i] * (double)f7;
                double d11 = d8 + sinTable[i] * (double)f7;
                double d12 = d7 + cosTable[i + 1] * (double)f7;
                double d13 = d8 + sinTable[i + 1] * (double)f7;
                bufferBuilder.vertex(matrix4f, (float)d10, (float)d9, (float)d11).color(f, f2, f3, 0.9f);
                bufferBuilder.vertex(matrix4f, (float)d12, (float)d9, (float)d13).color(f, f2, f3, 0.9f);
            }
        }
        if (bufferBuilder != null) {
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        }
        RenderSystem.lineWidth((float)1.0f);
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        matrixStack.pop();
    }
}


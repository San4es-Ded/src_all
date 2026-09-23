package haron.modules.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.events.WorldRenderPostEvent;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import java.awt.Color;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

@ModuleInfo(a="Wings", b="Добавляет игроку детализированные анимированные крылья", c=ModuleCategory.VISUALS)
public final class Wings
extends HaronModule {
    private static final float[][] WING_SHAPE = new float[][]{{0.08f, 0.1f}, {0.28f, 0.34f}, {0.56f, 0.82f}, {0.86f, 0.3f}, {1.14f, 0.46f}, {1.24f, 0.04f}, {1.02f, -0.18f}, {1.18f, -0.64f}, {0.86f, -0.46f}, {0.8f, -0.98f}, {0.54f, -0.74f}, {0.3f, -1.16f}, {0.1f, -0.54f}};
    private final NumberSetting size = new NumberSetting("Размер", 1.0f, 0.5f, 2.0f, 0.05f);
    private final NumberSetting opacity = new NumberSetting("Прозрачность", 0.75f, 0.1f, 1.0f, 0.05f);

    private static void drawWing(BufferBuilder bufferBuilder, Matrix4f matrix4f, float f, float f2, float f3, int n, int n2, int n3, int n4) {
        float f4 = 0.42f + f3;
        float f5 = f * 0.1f;
        float f6 = (float)Math.cos(f4 * f);
        float f7 = (float)Math.sin(f4 * f);
        for (int i = 0; i < WING_SHAPE.length; ++i) {
            float[] fArray = WING_SHAPE[i];
            float[] fArray2 = WING_SHAPE[(i + 1) % WING_SHAPE.length];
            float f8 = f * fArray[0] * f2;
            float f9 = fArray[1] * f2;
            float f10 = f * fArray2[0] * f2;
            float f11 = fArray2[1] * f2;
            float f12 = f5 + (f8 - f5) * f6 - f9 * f7;
            float f13 = (f8 - f5) * f7 + f9 * f6;
            float f14 = f5 + (f10 - f5) * f6 - f11 * f7;
            float f15 = (f10 - f5) * f7 + f11 * f6;
            bufferBuilder.vertex(matrix4f, f5, 0.0f, 0.05f).color((float)n / 255.0f, (float)n2 / 255.0f, (float)n3 / 255.0f, (float)n4 / 255.0f);
            bufferBuilder.vertex(matrix4f, f12, f13, 0.02f).color((float)n / 255.0f, (float)n2 / 255.0f, (float)n3 / 255.0f, (float)n4 / 255.0f);
            bufferBuilder.vertex(matrix4f, f14, f15, 0.02f).color((float)n / 255.0f, (float)n2 / 255.0f, (float)n3 / 255.0f, (float)n4 / 255.0f);
        }
    }

    private static void drawWingOutline(BufferBuilder bufferBuilder, Matrix4f matrix4f, float f, float f2, float f3, int n, int n2, int n3, int n4) {
        float f4 = f * 0.1f;
        float f5 = 0.42f + f3;
        float f6 = (float)Math.cos(f5 * f);
        float f7 = (float)Math.sin(f5 * f);
        for (int i = 0; i <= WING_SHAPE.length; ++i) {
            float[] fArray = WING_SHAPE[i % WING_SHAPE.length];
            float f8 = f * fArray[0] * f2;
            float f9 = fArray[1] * f2;
            float f10 = f4 + (f8 - f4) * f6 - f9 * f7;
            float f11 = (f8 - f4) * f7 + f9 * f6;
            bufferBuilder.vertex(matrix4f, f10, f11, 0.025f).color((float)n / 255.0f, (float)n2 / 255.0f, (float)n3 / 255.0f, (float)n4 / 255.0f);
        }
    }

    private static void drawWingRibs(BufferBuilder bufferBuilder, Matrix4f matrix4f, float f, float f2, float f3, int n, int n2, int n3, int n4) {
        int[] nArray = new int[]{2, 4, 7, 9, 11};
        float f4 = f * 0.1f;
        float f5 = 0.42f + f3;
        float f6 = (float)Math.cos(f5 * f);
        float f7 = (float)Math.sin(f5 * f);
        for (int n5 : nArray) {
            float[] fArray = WING_SHAPE[n5];
            float f8 = f * fArray[0] * f2;
            float f9 = fArray[1] * f2;
            float f10 = f4 + (f8 - f4) * f6 - f9 * f7;
            float f11 = (f8 - f4) * f7 + f9 * f6;
            bufferBuilder.vertex(matrix4f, f4, 0.0f, 0.03f).color((float)n / 255.0f, (float)n2 / 255.0f, (float)n3 / 255.0f, (float)n4 / 255.0f);
            bufferBuilder.vertex(matrix4f, f10, f11, 0.03f).color((float)n / 255.0f, (float)n2 / 255.0f, (float)n3 / 255.0f, (float)n4 / 255.0f);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler
    private void a(WorldRenderPostEvent kvprd92) {
        if (Wings.c.player == null || Wings.c.world == null || Wings.c.options.getPerspective().isFirstPerson()) {
            return;
        }
        MatrixStack matrixStack = kvprd92.a();
        float f = kvprd92.b();
        Vec3d vec3d = Wings.c.gameRenderer.getCamera().getPos();
        double d = MathHelper.lerp((double)f, (double)Wings.c.player.lastRenderX, (double)Wings.c.player.getX()) - vec3d.x;
        double d2 = MathHelper.lerp((double)f, (double)Wings.c.player.lastRenderY, (double)Wings.c.player.getY()) - vec3d.y;
        double d3 = MathHelper.lerp((double)f, (double)Wings.c.player.lastRenderZ, (double)Wings.c.player.getZ()) - vec3d.z;
        float f2 = (float)Math.sin(((float)Wings.c.player.age + f) * 0.22f) * 0.16f;
        float f3 = 0.45f * this.size.a();
        int n = Math.round(this.opacity.a() * 255.0f);
        Color color = ModuleManager.CLIENT_COLOR.n();
        int n2 = color.getRed();
        int n3 = color.getGreen();
        int n4 = color.getBlue();
        matrixStack.push();
        matrixStack.translate(d, d2 + 1.35, d3);
        float f4 = MathHelper.lerpAngleDegrees((float)f, (float)Wings.c.player.prevBodyYaw, (float)Wings.c.player.bodyYaw);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f4));
        if (Wings.c.player.isSneaking()) {
            matrixStack.translate(0.0, -0.15, 0.15);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(20.0f));
        }
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        try {
            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
            Wings.drawWing(bufferBuilder, matrix4f, -1.0f, f3, f2, n2, n3, n4, n);
            Wings.drawWing(bufferBuilder, matrix4f, 1.0f, f3, f2, n2, n3, n4, n);
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
            BufferBuilder bufferBuilder2 = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
            Wings.drawWingOutline(bufferBuilder2, matrix4f, -1.0f, f3, f2, n2, n3, n4, Math.min(255, n + 55));
            Wings.drawWingOutline(bufferBuilder2, matrix4f, 1.0f, f3, f2, n2, n3, n4, Math.min(255, n + 55));
            Wings.drawWingRibs(bufferBuilder2, matrix4f, -1.0f, f3, f2, n2, n3, n4, Math.max(45, n / 2));
            Wings.drawWingRibs(bufferBuilder2, matrix4f, 1.0f, f3, f2, n2, n3, n4, Math.max(45, n / 2));
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder2.end());
        }
        finally {
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            matrixStack.pop();
        }
    }
}


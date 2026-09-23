package rockstar.client.internal.script;


import rockstar.client.*;
import com.mojang.blaze3d.opengl.GlStateManager;
import rockstar.client.compat.RenderSystem;
import lombok.Generated;
import rockstar.client.compat.ShaderProgramKey;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import pyrock.utility.render.ColorRGBA;

public final class WorldDrawUtils {
    private static void internalMethod06769() {
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.blendFunc((com.mojang.blaze3d.platform.SourceFactor)com.mojang.blaze3d.platform.SourceFactor.SRC_ALPHA, (com.mojang.blaze3d.platform.DestFactor)com.mojang.blaze3d.platform.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
    }

    private static void internalMethod06770() {
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static void internalMethod05989(MatrixStack matrixStack, Box box, ColorRGBA colorRGBA) {
        WorldDrawUtils.internalMethod06769();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        WorldDrawUtils.internalMethod07439(matrixStack, bufferBuilder, box, colorRGBA);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        WorldDrawUtils.internalMethod06770();
    }

    public static void internalMethod06479(MatrixStack matrixStack, Box box, ColorRGBA colorRGBA) {
        WorldDrawUtils.internalMethod06769();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        WorldDrawUtils.internalMethod05594(matrixStack, bufferBuilder, box, colorRGBA);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        WorldDrawUtils.internalMethod06770();
    }

    public static void internalMethod01505(MatrixStack matrixStack, Vec3d vec3d, Vec3d vec3d2, ColorRGBA colorRGBA) {
        WorldDrawUtils.internalMethod06769();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        float f = colorRGBA.getRed() / 255.0f;
        float f2 = colorRGBA.getGreen() / 255.0f;
        float f3 = colorRGBA.getBlue() / 255.0f;
        float f4 = colorRGBA.getAlpha() / 255.0f;
        bufferBuilder.vertex(matrix4f, (float)vec3d.x, (float)vec3d.y, (float)vec3d.z).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, (float)vec3d2.x, (float)vec3d2.y, (float)vec3d2.z).color(f, f2, f3, f4);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        WorldDrawUtils.internalMethod06770();
    }

    private static void internalMethod07439(MatrixStack matrixStack, BufferBuilder bufferBuilder, Box box, ColorRGBA colorRGBA) {
        float f = colorRGBA.getRed() / 255.0f;
        float f2 = colorRGBA.getGreen() / 255.0f;
        float f3 = colorRGBA.getBlue() / 255.0f;
        float f4 = colorRGBA.getAlpha() / 255.0f;
        float f5 = (float)box.minX;
        float f6 = (float)box.minY;
        float f7 = (float)box.minZ;
        float f8 = (float)box.maxX;
        float f9 = (float)box.maxY;
        float f10 = (float)box.maxZ;
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        bufferBuilder.vertex(matrix4f, f5, f6, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f6, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f6, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f6, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f9, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f9, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f9, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f9, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f6, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f6, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f9, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f9, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f6, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f6, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f9, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f9, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f6, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f6, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f9, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f9, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f6, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f6, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f9, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f9, f7).color(f, f2, f3, f4);
    }

    private static void internalMethod05594(MatrixStack matrixStack, BufferBuilder bufferBuilder, Box box, ColorRGBA colorRGBA) {
        float f = colorRGBA.getRed() / 255.0f;
        float f2 = colorRGBA.getGreen() / 255.0f;
        float f3 = colorRGBA.getBlue() / 255.0f;
        float f4 = colorRGBA.getAlpha() / 255.0f;
        float f5 = (float)box.minX;
        float f6 = (float)box.minY;
        float f7 = (float)box.minZ;
        float f8 = (float)box.maxX;
        float f9 = (float)box.maxY;
        float f10 = (float)box.maxZ;
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        bufferBuilder.vertex(matrix4f, f5, f6, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f6, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f6, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f6, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f6, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f6, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f6, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f6, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f9, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f9, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f9, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f9, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f9, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f9, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f9, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f9, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f6, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f9, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f6, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f9, f7).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f6, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f8, f9, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f6, f10).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, f5, f9, f10).color(f, f2, f3, f4);
    }

    @Generated
    private WorldDrawUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}


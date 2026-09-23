package rockstar.client.internal.script;







import rockstar.client.rotation.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.mojang.blaze3d.opengl.GlStateManager;
import rockstar.client.compat.RenderSystem;
import net.minecraft.client.MinecraftClient;
import rockstar.client.compat.ShaderProgramKey;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import pyrock.events.render.Render3DEvent;
import rockstar.client.event.EventListener;
import rockstar.client.internal.core.NewtonGlobalState;
import rockstar.client.internal.rotation.NewtonCoreManager;
import rockstar.client.internal.game.PathNode;
import rockstar.client.internal.game.PathStartLocator;

public final class TargetBlockRenderer {
    private final EventListener<Render3DEvent> internalField0157 = render3DEvent -> this.internalMethod03364(render3DEvent.getMatrices(), render3DEvent.getCamera());

    public static TargetBlockRenderer internalMethod04562(NewtonCoreManager typedValue289) {
        TargetBlockRenderer typedValue310 = new TargetBlockRenderer();
        typedValue289.internalMethod05035().internalMethod00647(typedValue310);
        return typedValue310;
    }

    private void internalMethod03364(MatrixStack matrixStack, Camera camera) {
        if (!NewtonGlobalState.internalField0276) {
            return;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.player == null || minecraftClient.world == null) {
            return;
        }
        PathNode typedValue296 = PathStartLocator.internalMethod02372(minecraftClient);
        if (typedValue296 == null) {
            return;
        }
        Vec3d vec3d = camera.getCameraPos();
        double d = (float)typedValue296.internalMethod02945() + 0.01f;
        double d2 = (float)typedValue296.internalMethod02949() + 0.01f;
        double d3 = (float)typedValue296.internalMethod07945() + 0.01f;
        double d4 = (float)(typedValue296.internalMethod02945() + 1) - 0.01f;
        double d5 = (float)(typedValue296.internalMethod02949() + 1) - 0.01f;
        double d6 = (float)(typedValue296.internalMethod07945() + 1) - 0.01f;
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.blendFunc((com.mojang.blaze3d.platform.SourceFactor)com.mojang.blaze3d.platform.SourceFactor.SRC_ALPHA, (com.mojang.blaze3d.platform.DestFactor)com.mojang.blaze3d.platform.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.lineWidth((float)2.0f);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        matrixStack.push();
        matrixStack.translate(-vec3d.x, -vec3d.y, -vec3d.z);
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        int n = -446234778;
        TargetBlockRenderer.internalMethod01157(bufferBuilder, matrix4f, d, d2, d3, d4, d2, d3, n);
        TargetBlockRenderer.internalMethod01157(bufferBuilder, matrix4f, d4, d2, d3, d4, d2, d6, n);
        TargetBlockRenderer.internalMethod01157(bufferBuilder, matrix4f, d4, d2, d6, d, d2, d6, n);
        TargetBlockRenderer.internalMethod01157(bufferBuilder, matrix4f, d, d2, d6, d, d2, d3, n);
        TargetBlockRenderer.internalMethod01157(bufferBuilder, matrix4f, d, d5, d3, d4, d5, d3, n);
        TargetBlockRenderer.internalMethod01157(bufferBuilder, matrix4f, d4, d5, d3, d4, d5, d6, n);
        TargetBlockRenderer.internalMethod01157(bufferBuilder, matrix4f, d4, d5, d6, d, d5, d6, n);
        TargetBlockRenderer.internalMethod01157(bufferBuilder, matrix4f, d, d5, d6, d, d5, d3, n);
        TargetBlockRenderer.internalMethod01157(bufferBuilder, matrix4f, d, d2, d3, d, d5, d3, n);
        TargetBlockRenderer.internalMethod01157(bufferBuilder, matrix4f, d4, d2, d3, d4, d5, d3, n);
        TargetBlockRenderer.internalMethod01157(bufferBuilder, matrix4f, d4, d2, d6, d4, d5, d6, n);
        TargetBlockRenderer.internalMethod01157(bufferBuilder, matrix4f, d, d2, d6, d, d5, d6, n);
        matrixStack.pop();
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.lineWidth((float)1.0f);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private static void internalMethod01157(BufferBuilder bufferBuilder, Matrix4f matrix4f, double d, double d2, double d3, double d4, double d5, double d6, int n) {
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d3).color(n);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d5, (float)d6).color(n);
    }
}


package rockstar.client.internal.script;



import rockstar.client.render.*;
import rockstar.client.*;
import com.mojang.blaze3d.systems.ProjectionType;
import rockstar.client.compat.RenderSystem;
import rockstar.client.compat.ShaderProgramKeys;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.internal.script.AccentTimeShader;
import rockstar.client.render.ShaderPair;
import rockstar.client.render.ManagedFramebuffer;
import rockstar.client.render.compat.ImmediateRenderer;
import rockstar.client.render.compat.LegacyRenderState;

public class CustomSkyboxRenderer {
    private static ManagedFramebuffer internalField0769;
    private static ShaderPair internalField0315;
    private static int internalField1054;
    private static long internalField0230;

    public static void internalMethod04631(Identifier identifier, ColorRGBA colorRGBA) {
        if (identifier == null || colorRGBA == null) {
            return;
        }
        LegacyRenderState previousState = internalMethod10102();
        RenderSystem.setShaderColor((float)(colorRGBA.getRed() / 255.0f), (float)(colorRGBA.getGreen() / 255.0f), (float)(colorRGBA.getBlue() / 255.0f), (float)(colorRGBA.getAlpha() / 255.0f));
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture(0, identifier);
        try {
            ImmediateRenderer.drawWorldImmediate(CustomSkyboxRenderer.internalMethod01311());
        } finally {
            ImmediateRenderer.restoreState(previousState);
            RenderSystem.setShaderTexture(0, 0);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    public static void internalMethod04527(AccentTimeShader typedParameter1016, ColorRGBA colorRGBA, float f, float f2) {
        if (typedParameter1016 == null || colorRGBA == null) {
            return;
        }
        LegacyRenderState previousState = internalMethod10102();
        typedParameter1016.internalMethod02586(f, colorRGBA);
        float f3 = Math.max(0.0f, Math.min(1.0f, f2));
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)f3);
        typedParameter1016.internalMethod01220();
        try {
            ImmediateRenderer.drawWorldImmediate(CustomSkyboxRenderer.internalMethod01311());
        } finally {
            ImmediateRenderer.restoreState(previousState);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    public static void internalMethod01557(ShaderPair typedValue019, ColorRGBA colorRGBA, float f, float f2) {
        if (typedValue019 == null || colorRGBA == null) {
            return;
        }
        LegacyRenderState previousState = internalMethod10102();
        ManagedFramebuffer typedValue245 = CustomSkyboxRenderer.internalMethod02410();
        long l = System.currentTimeMillis();
        if (internalField0315 != typedValue019 || l - internalField0230 > 1000L) {
            CustomSkyboxRenderer.internalMethod06234(typedValue019, typedValue245, colorRGBA, f, 0, 12);
            internalField0315 = typedValue019;
            internalField1054 = 0;
        } else {
            CustomSkyboxRenderer.internalMethod06234(typedValue019, typedValue245, colorRGBA, f, internalField1054, internalField1054 + 1);
            internalField1054 = (internalField1054 + 1) % 12;
        }
        internalField0230 = l;
        float f3 = Math.max(0.0f, Math.min(1.0f, f2));
        typedValue019.internalMethod06188().internalMethod02586(f, colorRGBA);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)f3);
        RenderSystem.setShaderTexture(0, typedValue245.getColorAttachmentView());
        typedValue019.internalMethod06188().internalMethod01220();
        try {
            ImmediateRenderer.drawWorldImmediate(CustomSkyboxRenderer.internalMethod01311());
        } finally {
            ImmediateRenderer.restoreState(previousState);
            RenderSystem.setShaderTexture((int)0, (int)0);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    public static void internalMethod05026(AccentTimeShader typedParameter1016) {
        if (typedParameter1016 == null) {
            return;
        }
        typedParameter1016.internalMethod05563();
    }

    public static void internalMethod02273(ShaderPair typedValue019) {
        if (typedValue019 == null) {
            return;
        }
        CustomSkyboxRenderer.internalMethod05026(typedValue019.internalMethod06188());
    }

    public static void internalMethod01308() {
        if (internalField0769 != null) {
            internalField0769.delete();
            internalField0769 = null;
        }
        internalField0315 = null;
        internalField1054 = 0;
        internalField0230 = 0L;
    }

    private static ManagedFramebuffer internalMethod02410() {
        if (internalField0769 == null) {
            ManagedFramebuffer typedValue245 = new ManagedFramebuffer(false);
            typedValue245.internalMethod06013();
            typedValue245.setClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            typedValue245.resize(2048, 1024);
            internalField0769 = typedValue245;
            internalField0315 = null;
        }
        return internalField0769;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void internalMethod06234(ShaderPair typedValue019, ManagedFramebuffer typedValue245, ColorRGBA colorRGBA, float f, int n, int n2) {
        typedValue245.beginWrite(true);
        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix((Matrix4f)new Matrix4f().setOrtho(0.0f, (float)typedValue245.textureWidth, (float)typedValue245.textureHeight, 0.0f, 1000.0f, 21000.0f), (ProjectionType)ProjectionType.ORTHOGRAPHIC);
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.identity().translate(0.0f, 0.0f, -11000.0f);
        try {
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask((boolean)false);
            RenderSystem.disableCull();
            typedValue019.internalMethod04938().internalMethod01220();
            typedValue019.internalMethod04938().internalMethod02586(f, colorRGBA);
            CustomSkyboxRenderer.internalMethod06740(typedValue245.textureWidth, typedValue245.textureHeight, n, n2);
        }
        finally {
            typedValue245.endWrite();
            rockstar.client.render.FramebufferCompat.beginWrite(MinecraftClient.getInstance().getFramebuffer(), false);
            RenderSystem.depthMask((boolean)true);
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            matrix4fStack.popMatrix();
            RenderSystem.restoreProjectionMatrix();
        }
    }

    private static void internalMethod06740(float f, float f2, int n, int n2) {
        float f3 = (float)n / 12.0f;
        float f4 = (float)n2 / 12.0f;
        float f5 = (1.0f - f3) * f2;
        float f6 = (1.0f - f4) * f2;
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(0.0f, f5, 0.0f).texture(0.0f, f3).color(-1);
        bufferBuilder.vertex(0.0f, f6, 0.0f).texture(0.0f, f4).color(-1);
        bufferBuilder.vertex(f, f6, 0.0f).texture(1.0f, f4).color(-1);
        bufferBuilder.vertex(f, f5, 0.0f).texture(1.0f, f3).color(-1);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
    }

    private static BuiltBuffer internalMethod01311() {
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        internalMethod03804(builder);
        return builder.end();
    }

    private static LegacyRenderState internalMethod10102() {
        LegacyRenderState previousState = ImmediateRenderer.captureState();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.colorMask(true, true, true, true);
        return previousState;
    }

    private static void internalMethod03804(VertexConsumer vertexConsumer) {
        float f = 100.0f;
        CustomSkyboxRenderer.internalMethod06139(vertexConsumer, 1, -f, f, -f, -f, f, f, f, f, f, f, f, -f);
        CustomSkyboxRenderer.internalMethod06139(vertexConsumer, 0, -f, -f, f, -f, -f, -f, f, -f, -f, f, -f, f);
        CustomSkyboxRenderer.internalMethod06139(vertexConsumer, 2, f, f, -f, f, -f, -f, -f, -f, -f, -f, f, -f);
        CustomSkyboxRenderer.internalMethod06139(vertexConsumer, 4, -f, f, f, -f, -f, f, f, -f, f, f, f, f);
        CustomSkyboxRenderer.internalMethod06139(vertexConsumer, 3, -f, f, -f, -f, -f, -f, -f, -f, f, -f, f, f);
        CustomSkyboxRenderer.internalMethod06139(vertexConsumer, 5, f, f, f, f, -f, f, f, -f, -f, f, f, -f);
    }

    private static void internalMethod06139(VertexConsumer vertexConsumer, int n, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        int n2 = n % 3;
        int n3 = n / 3;
        float f13 = (float)n2 / 3.0f;
        float f14 = (float)(n2 + 1) / 3.0f;
        float f15 = (float)n3 / 2.0f;
        float f16 = (float)(n3 + 1) / 2.0f;
        Matrix4f matrix4f = new Matrix4f();
        vertexConsumer.vertex(matrix4f, f, f2, f3).texture(f13, f15).color(-1);
        vertexConsumer.vertex(matrix4f, f4, f5, f6).texture(f13, f16).color(-1);
        vertexConsumer.vertex(matrix4f, f7, f8, f9).texture(f14, f16).color(-1);
        vertexConsumer.vertex(matrix4f, f10, f11, f12).texture(f14, f15).color(-1);
    }

    static {
    }
}

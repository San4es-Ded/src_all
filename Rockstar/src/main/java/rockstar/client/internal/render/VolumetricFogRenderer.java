package rockstar.client.internal.render;




import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.*;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.ProjectionType;
import rockstar.client.compat.RenderSystem;
import net.minecraft.client.gl.Framebuffer;
import rockstar.client.compat.GlUniform;
import rockstar.client.compat.ShaderProgramKey;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import rockstar.client.internal.render.VolumetricFogShader;
import rockstar.client.RockstarClient;
import rockstar.client.internal.render.ShaderProgramBase;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.WindowAccess;
import rockstar.client.render.ManagedFramebuffer;
import rockstar.client.render.RenderPipeline;

public class VolumetricFogRenderer
implements MinecraftClientAccess,
WindowAccess {
    private final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(false).internalMethod06013();
    private final ManagedFramebuffer internalField0770 = new ManagedFramebuffer(false).internalMethod06013().internalMethod03472(0.5f);
    private VolumetricFogShader internalField0443;
    private ShaderProgramBase internalField0104;

    public void internalMethod03568() {
        if (this.internalField0443 != null) {
            return;
        }
        this.internalField0443 = new VolumetricFogShader(RockstarClient.id("volumetric_fog/data"));
        this.internalField0104 = new ShaderProgramBase(RockstarClient.id("volumetric_fog/tent/data"), VertexFormats.POSITION_TEXTURE_COLOR);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void internalMethod03258(Matrix4f matrix4f, VolumetricFogShader.InternalType0351 nestedValue0133) {
        if (this.internalField0443 == null || this.internalField0104 == null || nestedValue0133.internalField0227 == 0) {
            return;
        }
        Framebuffer framebuffer = internalField0149.getFramebuffer();
        if (framebuffer == null || framebuffer.getDepthAttachment() == null) {
            return;
        }
        int n = internalField0267.getScaledWidth();
        int n2 = internalField0267.getScaledHeight();
        if (n <= 0 || n2 <= 0) {
            return;
        }
        int n3 = rockstar.client.render.FramebufferCompat.glId(framebuffer.getDepthAttachment());
        RenderSystem.disableBlend();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.backupProjectionMatrix();
        Matrix4f matrix4f2 = new Matrix4f().setOrtho(0.0f, (float)n, (float)n2, 0.0f, 1000.0f, 21000.0f);
        RenderSystem.setProjectionMatrix((Matrix4f)matrix4f2, (ProjectionType)ProjectionType.ORTHOGRAPHIC);
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.identity().translate(0.0f, 0.0f, -11000.0f);
        try {
            this.internalField0769.internalMethod03472(nestedValue0133.internalField0277 ? 1.0f : 0.5f);
            this.internalField0769.internalMethod02227(true);
            this.internalField0443.internalMethod01220();
            this.internalField0443.internalMethod06888(matrix4f, nestedValue0133);
            RenderSystem.setShaderTexture((int)0, (int)n3);
            RenderSystem.setShaderTexture((int)1, (int)nestedValue0133.internalField0227);
            RenderPipeline.internalMethod01737(0.0f, 0.0f, n, n2);
            this.internalField0769.internalMethod03248();
            ManagedFramebuffer typedValue245 = this.internalField0769;
            if (!nestedValue0133.internalField0277) {
                this.internalField0770.internalMethod02227(true);
                this.internalField0104.internalMethod01220();
                GlUniform glUniform = this.internalField0104.internalMethod05981("TexelSize");
                if (glUniform != null) {
                    glUniform.set(1.0f / (float)Math.max(this.internalField0769.textureWidth, 1), 1.0f / (float)Math.max(this.internalField0769.textureHeight, 1));
                }
                RenderSystem.setShaderTexture(0, this.internalField0769.getColorAttachmentView());
                RenderPipeline.internalMethod01737(0.0f, 0.0f, n, n2);
                this.internalField0770.internalMethod03248();
                typedValue245 = this.internalField0770;
            }
            RenderSystem.enableBlend();
            RenderSystem.blendFunc((com.mojang.blaze3d.platform.SourceFactor)com.mojang.blaze3d.platform.SourceFactor.ONE, (com.mojang.blaze3d.platform.DestFactor)com.mojang.blaze3d.platform.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            RenderSystem.setShaderTexture(0, typedValue245.getColorAttachmentView());
            RenderPipeline.internalMethod01737(0.0f, 0.0f, n, n2);
            RenderSystem.setShaderTexture((int)1, (int)0);
            RenderSystem.setShaderTexture((int)0, (int)0);
        }
        finally {
            matrix4fStack.popMatrix();
            RenderSystem.restoreProjectionMatrix();
            RenderSystem.depthMask((boolean)true);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
        }
    }
}


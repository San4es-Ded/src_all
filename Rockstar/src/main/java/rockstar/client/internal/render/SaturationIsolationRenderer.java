package rockstar.client.internal.render;




import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.*;
import com.mojang.blaze3d.systems.ProjectionType;
import rockstar.client.compat.RenderSystem;
import net.minecraft.client.gl.Framebuffer;
import rockstar.client.compat.GlUniform;
import rockstar.client.compat.ShaderProgramKey;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import rockstar.client.internal.render.ShaderProgramBase;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.WindowAccess;
import rockstar.client.render.ManagedFramebuffer;
import rockstar.client.render.RenderPipeline;

public class SaturationIsolationRenderer
extends ShaderProgramBase
implements MinecraftClientAccess,
WindowAccess {
    private final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(false).internalMethod06013();
    private GlUniform internalField0855;
    private GlUniform internalField0854;
    private GlUniform internalField1356;
    private GlUniform internalField1354;
    private GlUniform internalField1355;
    private GlUniform internalField1353;

    public SaturationIsolationRenderer(Identifier identifier) {
        super(identifier, VertexFormats.POSITION_TEXTURE_COLOR);
    }

    @Override
    public void internalMethod06856() {
        this.internalField0855 = this.internalMethod05981("IsolationStrength");
        this.internalField0854 = this.internalMethod05981("IsolationColor");
        this.internalField1356 = this.internalMethod05981("HueTolerance");
        this.internalField1354 = this.internalMethod05981("MinSat");
        this.internalField1355 = this.internalMethod05981("MinVal");
        this.internalField1353 = this.internalMethod05981("BackgroundSat");
        super.internalMethod06856();
    }

    public void internalMethod02418(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        Framebuffer framebuffer = internalField0149.getFramebuffer();
        if (framebuffer == null) {
            return;
        }
        int n = internalField0267.getScaledWidth();
        int n2 = internalField0267.getScaledHeight();
        RenderSystem.disableBlend();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.backupProjectionMatrix();
        Matrix4f matrix4f = new Matrix4f().setOrtho(0.0f, (float)n, (float)n2, 0.0f, 1000.0f, 21000.0f);
        RenderSystem.setProjectionMatrix((Matrix4f)matrix4f, (ProjectionType)ProjectionType.ORTHOGRAPHIC);
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.identity().translate(0.0f, 0.0f, -11000.0f);
        this.internalField0769.internalMethod03245();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture(0, framebuffer.getColorAttachmentView());
        RenderPipeline.internalMethod01737(0.0f, 0.0f, n, n2);
        this.internalField0769.internalMethod03248();
        this.internalMethod01220();
        if (this.internalField0855 != null) {
            this.internalField0855.set(f);
        }
        if (this.internalField0854 != null) {
            this.internalField0854.set(f2, f3, f4);
        }
        if (this.internalField1356 != null) {
            this.internalField1356.set(f5);
        }
        if (this.internalField1354 != null) {
            this.internalField1354.set(f6);
        }
        if (this.internalField1355 != null) {
            this.internalField1355.set(f7);
        }
        if (this.internalField1353 != null) {
            this.internalField1353.set(f8);
        }
        RenderSystem.setShaderTexture(0, this.internalField0769.getColorAttachmentView());
        RenderPipeline.internalMethod01737(0.0f, 0.0f, n, n2);
        RenderSystem.setShaderTexture((int)0, (int)0);
        matrix4fStack.popMatrix();
        RenderSystem.restoreProjectionMatrix();
        RenderSystem.depthMask((boolean)true);
    }
}


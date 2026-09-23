package rockstar.client.internal.render;




import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.*;
import com.mojang.blaze3d.systems.ProjectionType;
import rockstar.client.compat.RenderSystem;
import java.util.List;
import net.minecraft.client.gl.Framebuffer;
import rockstar.client.compat.ShaderProgramKey;
import rockstar.client.compat.ShaderProgramKeys;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.WindowAccess;
import rockstar.client.render.ManagedFramebuffer;
import rockstar.client.render.RenderPipeline;
import rockstar.client.internal.render.ParticleLightShader;

public class ParticleLightRenderer
implements MinecraftClientAccess,
WindowAccess {
    private final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(false).internalMethod06013();
    private ParticleLightShader internalField0345;

    public void internalMethod04102() {
        if (this.internalField0345 != null) {
            return;
        }
        this.internalField0345 = new ParticleLightShader(RockstarClient.id("particle_light/data"));
    }

    public void internalMethod02901(Matrix4f matrix4f, float f, List<ParticleLightShader.InternalType0228> list) {
        if (this.internalField0345 == null || list == null || list.isEmpty()) {
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
        this.internalField0769.internalMethod02227(false);
        this.internalField0345.internalMethod01220();
        this.internalField0345.internalMethod02350(matrix4f, f, list);
        RenderSystem.setShaderTexture(0, framebuffer.getColorAttachmentView());
        RenderSystem.setShaderTexture((int)1, (int)n3);
        RenderPipeline.internalMethod01737(0.0f, 0.0f, n, n2);
        this.internalField0769.internalMethod03248();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture(0, this.internalField0769.getColorAttachmentView());
        RenderPipeline.internalMethod01737(0.0f, 0.0f, n, n2);
        RenderSystem.setShaderTexture((int)1, (int)0);
        RenderSystem.setShaderTexture((int)0, (int)0);
        matrix4fStack.popMatrix();
        RenderSystem.restoreProjectionMatrix();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableBlend();
    }
}


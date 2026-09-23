package rockstar.client.internal.render;




import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.*;
import com.mojang.blaze3d.systems.ProjectionType;
import rockstar.client.compat.RenderSystem;
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
import rockstar.client.internal.render.HeatHazeShader;

public class HeatHazeRenderer
implements MinecraftClientAccess,
WindowAccess {
    private final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(false).internalMethod06013();
    private final ManagedFramebuffer internalField0770 = new ManagedFramebuffer(false).internalMethod06013();
    private HeatHazeShader internalField0335;

    public void internalMethod01553() {
        if (this.internalField0335 != null) {
            return;
        }
        this.internalField0335 = new HeatHazeShader(RockstarClient.id("heat_haze/data"));
    }

    public void internalMethod04104(int n, float f, float f2, float f3) {
        if (this.internalField0335 == null || n == 0 || f2 <= 0.001f) {
            return;
        }
        Framebuffer framebuffer = internalField0149.getFramebuffer();
        if (framebuffer == null) {
            return;
        }
        int n2 = internalField0267.getScaledWidth();
        int n3 = internalField0267.getScaledHeight();
        if (n2 <= 0 || n3 <= 0) {
            return;
        }
        RenderSystem.disableBlend();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.backupProjectionMatrix();
        Matrix4f matrix4f = new Matrix4f().setOrtho(0.0f, (float)n2, (float)n3, 0.0f, 1000.0f, 21000.0f);
        RenderSystem.setProjectionMatrix((Matrix4f)matrix4f, (ProjectionType)ProjectionType.ORTHOGRAPHIC);
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.identity().translate(0.0f, 0.0f, -11000.0f);
        this.internalField0769.internalMethod03245();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture(0, framebuffer.getColorAttachmentView());
        RenderPipeline.internalMethod01737(0.0f, 0.0f, n2, n3);
        this.internalField0769.internalMethod03248();
        this.internalField0770.internalMethod03245();
        this.internalField0335.internalMethod01220();
        this.internalField0335.internalMethod06166(f3, f2, f);
        RenderSystem.setShaderTexture(0, this.internalField0769.getColorAttachmentView());
        RenderSystem.setShaderTexture((int)1, (int)n);
        RenderPipeline.internalMethod01737(0.0f, 0.0f, n2, n3);
        this.internalField0770.internalMethod03248();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture(0, this.internalField0770.getColorAttachmentView());
        RenderPipeline.internalMethod01737(0.0f, 0.0f, n2, n3);
        RenderSystem.setShaderTexture((int)1, (int)0);
        RenderSystem.setShaderTexture((int)0, (int)0);
        matrix4fStack.popMatrix();
        RenderSystem.restoreProjectionMatrix();
        RenderSystem.depthMask((boolean)true);
    }
}


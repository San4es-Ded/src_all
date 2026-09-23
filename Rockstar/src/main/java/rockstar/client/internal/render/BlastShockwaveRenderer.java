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
import rockstar.client.internal.render.JumpShockwaveRenderer;
import rockstar.client.internal.render.ShockwaveShader;

public class BlastShockwaveRenderer
implements MinecraftClientAccess,
WindowAccess {
    private final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(false).internalMethod06013();
    private final ManagedFramebuffer internalField0770 = new ManagedFramebuffer(false).internalMethod06013();
    private ShockwaveShader internalField0339;

    public void internalMethod04833() {
        if (this.internalField0339 != null) {
            return;
        }
        this.internalField0339 = new ShockwaveShader(RockstarClient.id("blast_shockwave/data"));
    }

    public void internalMethod03482(Matrix4f matrix4f, List<JumpShockwaveRenderer.InternalType0218> list) {
        if (this.internalField0339 == null || list == null || list.isEmpty()) {
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
        int n4 = Math.min(list.size(), 12);
        RenderSystem.disableBlend();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.backupProjectionMatrix();
        Matrix4f matrix4f2 = new Matrix4f().setOrtho(0.0f, (float)n, (float)n2, 0.0f, 1000.0f, 21000.0f);
        RenderSystem.setProjectionMatrix((Matrix4f)matrix4f2, (ProjectionType)ProjectionType.ORTHOGRAPHIC);
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.identity().translate(0.0f, 0.0f, -11000.0f);
        ManagedFramebuffer[] iIiiIIiII_Class357Array = new ManagedFramebuffer[]{this.internalField0769, this.internalField0770};
        Framebuffer framebuffer2 = framebuffer;
        int n5 = 0;
        for (int i = 0; i < n4; ++i) {
            ManagedFramebuffer typedValue245 = iIiiIIiII_Class357Array[n5];
            typedValue245.internalMethod03245();
            this.internalField0339.internalMethod01220();
            this.internalField0339.internalMethod04470(matrix4f, list.get(i));
            RenderSystem.setShaderTexture(0, framebuffer2.getColorAttachmentView());
            RenderSystem.setShaderTexture((int)1, (int)n3);
            RenderPipeline.internalMethod01737(0.0f, 0.0f, n, n2);
            typedValue245.internalMethod03248();
            framebuffer2 = typedValue245;
            n5 ^= 1;
        }
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture(0, framebuffer2.getColorAttachmentView());
        RenderPipeline.internalMethod01737(0.0f, 0.0f, n, n2);
        RenderSystem.setShaderTexture((int)1, (int)0);
        RenderSystem.setShaderTexture((int)0, (int)0);
        matrix4fStack.popMatrix();
        RenderSystem.restoreProjectionMatrix();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableBlend();
    }
}


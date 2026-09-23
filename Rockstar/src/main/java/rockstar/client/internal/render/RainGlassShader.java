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

public class RainGlassShader
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
    private GlUniform internalField1647;
    private GlUniform internalField1645;
    private GlUniform internalField1644;

    public RainGlassShader(Identifier identifier) {
        super(identifier, VertexFormats.POSITION_TEXTURE_COLOR);
    }

    @Override
    public void internalMethod06856() {
        this.internalField0855 = this.internalMethod05981("InvViewProj");
        this.internalField0854 = this.internalMethod05981("CamPos");
        this.internalField1356 = this.internalMethod05981("Tint");
        this.internalField1354 = this.internalMethod05981("Time");
        this.internalField1355 = this.internalMethod05981("Drops");
        this.internalField1353 = this.internalMethod05981("Splashes");
        this.internalField1647 = this.internalMethod05981("Aspect");
        this.internalField1645 = this.internalMethod05981("Roof");
        this.internalField1644 = this.internalMethod05981("RoofSpan");
        super.internalMethod06856();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void internalMethod01779(Matrix4f matrix4f, InternalType0344 nestedValue0124) {
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
            this.internalField0769.internalMethod02227(false);
            this.internalMethod01220();
            this.internalMethod02259(matrix4f, nestedValue0124, (float)n / (float)n2);
            RenderSystem.setShaderTexture(0, framebuffer.getColorAttachmentView());
            RenderSystem.setShaderTexture((int)1, (int)n3);
            RenderSystem.setShaderTexture((int)2, (int)nestedValue0124.internalField0227);
            RenderPipeline.internalMethod01737(0.0f, 0.0f, n, n2);
            this.internalField0769.internalMethod03248();
            RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            RenderSystem.setShaderTexture(0, this.internalField0769.getColorAttachmentView());
            RenderPipeline.internalMethod01737(0.0f, 0.0f, n, n2);
            RenderSystem.setShaderTexture((int)2, (int)0);
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

    private void internalMethod02259(Matrix4f matrix4f, InternalType0344 nestedValue0124, float f) {
        if (this.internalField0855 != null) {
            this.internalField0855.set(matrix4f);
        }
        if (this.internalField0854 != null) {
            this.internalField0854.set(nestedValue0124.internalField0205, nestedValue0124.internalField0206, nestedValue0124.internalField1048);
        }
        if (this.internalField1356 != null) {
            this.internalField1356.set(nestedValue0124.internalField1047, nestedValue0124.internalField1049, nestedValue0124.internalField1046);
        }
        if (this.internalField1354 != null) {
            this.internalField1354.set(nestedValue0124.internalField1456);
        }
        if (this.internalField1355 != null) {
            this.internalField1355.set(nestedValue0124.internalField1457);
        }
        if (this.internalField1353 != null) {
            this.internalField1353.set(nestedValue0124.internalField1458);
        }
        if (this.internalField1647 != null) {
            this.internalField1647.set(f);
        }
        if (this.internalField1645 != null) {
            this.internalField1645.set(nestedValue0124.internalField1459, nestedValue0124.internalField1460, nestedValue0124.internalField1461, nestedValue0124.internalField1462);
        }
        if (this.internalField1644 != null) {
            this.internalField1644.set(nestedValue0124.internalField1455);
        }
    }

    public static final class InternalType0344 {
        public float internalField0205;
        public float internalField0206;
        public float internalField1048;
        public float internalField1047 = 0.7f;
        public float internalField1049 = 0.78f;
        public float internalField1046 = 0.85f;
        public float internalField1456;
        public float internalField1457 = 0.6f;
        public float internalField1458 = 0.6f;
        public float internalField1459;
        public float internalField1460;
        public float internalField1461;
        public float internalField1462;
        public float internalField1455 = 192.0f;
        public int internalField0227;
    }
}


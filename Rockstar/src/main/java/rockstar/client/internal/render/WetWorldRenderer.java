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

public class WetWorldRenderer
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
    private GlUniform internalField1643;
    private GlUniform internalField1640;
    private GlUniform internalField1641;
    private GlUniform internalField1646;
    private GlUniform internalField1642;

    public WetWorldRenderer(Identifier identifier) {
        super(identifier, VertexFormats.POSITION_TEXTURE_COLOR);
    }

    @Override
    public void internalMethod06856() {
        this.internalField0855 = this.internalMethod05981("ViewProj");
        this.internalField0854 = this.internalMethod05981("InvViewProj");
        this.internalField1356 = this.internalMethod05981("CamPos");
        this.internalField1354 = this.internalMethod05981("SkyTint");
        this.internalField1355 = this.internalMethod05981("SunDir");
        this.internalField1353 = this.internalMethod05981("Reflectivity");
        this.internalField1647 = this.internalMethod05981("Wetness");
        this.internalField1645 = this.internalMethod05981("Ripple");
        this.internalField1644 = this.internalMethod05981("Gloss");
        this.internalField1643 = this.internalMethod05981("Steps");
        this.internalField1640 = this.internalMethod05981("HitThickness");
        this.internalField1641 = this.internalMethod05981("MaxDistance");
        this.internalField1646 = this.internalMethod05981("UpOnly");
        this.internalField1642 = this.internalMethod05981("Time");
        super.internalMethod06856();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void internalMethod01883(InternalType0469 nestedValue0165) {
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
        Matrix4f matrix4f = new Matrix4f().setOrtho(0.0f, (float)n, (float)n2, 0.0f, 1000.0f, 21000.0f);
        RenderSystem.setProjectionMatrix((Matrix4f)matrix4f, (ProjectionType)ProjectionType.ORTHOGRAPHIC);
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.identity().translate(0.0f, 0.0f, -11000.0f);
        try {
            this.internalField0769.internalMethod02227(false);
            this.internalMethod01220();
            this.internalMethod02553(nestedValue0165);
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
        }
        finally {
            matrix4fStack.popMatrix();
            RenderSystem.restoreProjectionMatrix();
            RenderSystem.depthMask((boolean)true);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
        }
    }

    private void internalMethod02553(InternalType0469 nestedValue0165) {
        if (this.internalField0855 != null) {
            this.internalField0855.set(nestedValue0165.internalField0788);
        }
        if (this.internalField0854 != null) {
            this.internalField0854.set(nestedValue0165.internalField0787);
        }
        if (this.internalField1356 != null) {
            this.internalField1356.set(nestedValue0165.internalField0205, nestedValue0165.internalField0206, nestedValue0165.internalField1048);
        }
        if (this.internalField1354 != null) {
            this.internalField1354.set(nestedValue0165.internalField1047, nestedValue0165.internalField1049, nestedValue0165.internalField1046);
        }
        if (this.internalField1355 != null) {
            this.internalField1355.set(nestedValue0165.internalField1456, nestedValue0165.internalField1457, nestedValue0165.internalField1458);
        }
        if (this.internalField1353 != null) {
            this.internalField1353.set(nestedValue0165.internalField1459);
        }
        if (this.internalField1647 != null) {
            this.internalField1647.set(nestedValue0165.internalField1460);
        }
        if (this.internalField1645 != null) {
            this.internalField1645.set(nestedValue0165.internalField1461);
        }
        if (this.internalField1644 != null) {
            this.internalField1644.set(nestedValue0165.internalField1462);
        }
        if (this.internalField1643 != null) {
            this.internalField1643.set(nestedValue0165.internalField1455);
        }
        if (this.internalField1640 != null) {
            this.internalField1640.set(nestedValue0165.internalField1723);
        }
        if (this.internalField1641 != null) {
            this.internalField1641.set(nestedValue0165.internalField1731);
        }
        if (this.internalField1646 != null) {
            this.internalField1646.set(nestedValue0165.internalField1727);
        }
        if (this.internalField1642 != null) {
            this.internalField1642.set(nestedValue0165.internalField1728);
        }
    }

    public static final class InternalType0469 {
        public final Matrix4f internalField0788 = new Matrix4f();
        public final Matrix4f internalField0787 = new Matrix4f();
        public float internalField0205;
        public float internalField0206;
        public float internalField1048;
        public float internalField1047 = 0.45f;
        public float internalField1049 = 0.6f;
        public float internalField1046 = 0.9f;
        public float internalField1456;
        public float internalField1457 = 1.0f;
        public float internalField1458;
        public float internalField1459 = 0.7f;
        public float internalField1460 = 0.6f;
        public float internalField1461 = 0.35f;
        public float internalField1462 = 0.5f;
        public float internalField1455 = 24.0f;
        public float internalField1723 = 0.6f;
        public float internalField1731 = 24.0f;
        public float internalField1727 = 1.0f;
        public float internalField1728;
    }
}


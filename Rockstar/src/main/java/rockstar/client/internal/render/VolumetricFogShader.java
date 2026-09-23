package rockstar.client.internal.render;


import rockstar.client.*;
import rockstar.client.compat.GlUniform;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import rockstar.client.internal.render.ShaderProgramBase;

public class VolumetricFogShader
extends ShaderProgramBase {
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

    public VolumetricFogShader(Identifier identifier) {
        super(identifier, VertexFormats.POSITION_TEXTURE_COLOR);
    }

    @Override
    public void internalMethod06856() {
        this.internalField0855 = this.internalMethod05981("InvViewProj");
        this.internalField0854 = this.internalMethod05981("CamPos");
        this.internalField1356 = this.internalMethod05981("FogTint");
        this.internalField1354 = this.internalMethod05981("Flow");
        this.internalField1355 = this.internalMethod05981("FlowPatch");
        this.internalField1353 = this.internalMethod05981("Density");
        this.internalField1647 = this.internalMethod05981("LayerY");
        this.internalField1645 = this.internalMethod05981("Thickness");
        this.internalField1644 = this.internalMethod05981("MaxDistance");
        this.internalField1643 = this.internalMethod05981("Coverage");
        this.internalField1640 = this.internalMethod05981("TileScale");
        this.internalField1641 = this.internalMethod05981("StepWorld");
        super.internalMethod06856();
    }

    public void internalMethod06888(Matrix4f matrix4f, InternalType0351 nestedValue0133) {
        if (this.internalField0855 != null) {
            this.internalField0855.set(matrix4f);
        }
        if (this.internalField0854 != null) {
            this.internalField0854.set(nestedValue0133.internalField0205, nestedValue0133.internalField0206, nestedValue0133.internalField1048);
        }
        if (this.internalField1356 != null) {
            this.internalField1356.set(nestedValue0133.internalField1047, nestedValue0133.internalField1049, nestedValue0133.internalField1046);
        }
        if (this.internalField1354 != null) {
            this.internalField1354.set(nestedValue0133.internalField1456, nestedValue0133.internalField1457);
        }
        if (this.internalField1355 != null) {
            this.internalField1355.set(nestedValue0133.internalField1458, nestedValue0133.internalField1459);
        }
        if (this.internalField1353 != null) {
            this.internalField1353.set(nestedValue0133.internalField1460);
        }
        if (this.internalField1647 != null) {
            this.internalField1647.set(nestedValue0133.internalField1461);
        }
        if (this.internalField1645 != null) {
            this.internalField1645.set(nestedValue0133.internalField1462);
        }
        if (this.internalField1644 != null) {
            this.internalField1644.set(nestedValue0133.internalField1455);
        }
        if (this.internalField1643 != null) {
            this.internalField1643.set(nestedValue0133.internalField1723);
        }
        if (this.internalField1640 != null) {
            this.internalField1640.set(nestedValue0133.internalField1731);
        }
        if (this.internalField1641 != null) {
            this.internalField1641.set(nestedValue0133.internalField1727);
        }
    }

    public static final class InternalType0351 {
        public float internalField0205;
        public float internalField0206;
        public float internalField1048;
        public float internalField1047 = 1.0f;
        public float internalField1049 = 1.0f;
        public float internalField1046 = 1.0f;
        public float internalField1456;
        public float internalField1457;
        public float internalField1458;
        public float internalField1459;
        public float internalField1460 = 0.35f;
        public float internalField1461 = 64.0f;
        public float internalField1462 = 10.0f;
        public float internalField1455 = 96.0f;
        public float internalField1723 = 0.45f;
        public float internalField1731 = 0.0034722222f;
        public float internalField1727 = 4.5f;
        public int internalField0227;
        public boolean internalField0277;
    }
}


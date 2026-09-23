package rockstar.client.internal.render;



import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.compat.GlUniform;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import rockstar.client.internal.render.ShaderProgramBase;

public class AmbientParticleShader
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
    private GlUniform internalField1646;
    private GlUniform internalField1642;

    public AmbientParticleShader(Identifier identifier) {
        super(identifier, VertexFormats.POSITION_TEXTURE_COLOR);
    }

    @Override
    public void internalMethod06856() {
        this.internalField0855 = this.internalMethod05981("ViewProj");
        this.internalField0854 = this.internalMethod05981("CamPos");
        this.internalField1356 = this.internalMethod05981("CamRight");
        this.internalField1354 = this.internalMethod05981("CamUp");
        this.internalField1355 = this.internalMethod05981("Motion");
        this.internalField1353 = this.internalMethod05981("Cell");
        this.internalField1647 = this.internalMethod05981("Tint");
        this.internalField1645 = this.internalMethod05981("Time");
        this.internalField1644 = this.internalMethod05981("Size");
        this.internalField1643 = this.internalMethod05981("Drift");
        this.internalField1640 = this.internalMethod05981("Flicker");
        this.internalField1641 = this.internalMethod05981("Stretch");
        this.internalField1646 = this.internalMethod05981("Roof");
        this.internalField1642 = this.internalMethod05981("RoofSpan");
        super.internalMethod06856();
    }

    public void internalMethod05560(Matrix4f matrix4f, InternalType0476 nestedValue0168) {
        if (this.internalField0855 != null) {
            this.internalField0855.set(matrix4f);
        }
        if (this.internalField0854 != null) {
            this.internalField0854.set(nestedValue0168.internalField0205, nestedValue0168.internalField0206, nestedValue0168.internalField1048);
        }
        if (this.internalField1356 != null) {
            this.internalField1356.set(nestedValue0168.internalField1047, nestedValue0168.internalField1049, nestedValue0168.internalField1046);
        }
        if (this.internalField1354 != null) {
            this.internalField1354.set(nestedValue0168.internalField1456, nestedValue0168.internalField1457, nestedValue0168.internalField1458);
        }
        if (this.internalField1355 != null) {
            this.internalField1355.set(nestedValue0168.internalField1459, nestedValue0168.internalField1460, nestedValue0168.internalField1461);
        }
        if (this.internalField1353 != null) {
            this.internalField1353.set(nestedValue0168.internalField1462, nestedValue0168.internalField1455, nestedValue0168.internalField1723);
        }
        if (this.internalField1647 != null) {
            this.internalField1647.set(nestedValue0168.internalField1731, nestedValue0168.internalField1727, nestedValue0168.internalField1728, nestedValue0168.internalField1717);
        }
        if (this.internalField1645 != null) {
            this.internalField1645.set(nestedValue0168.internalField1718);
        }
        if (this.internalField1644 != null) {
            this.internalField1644.set(nestedValue0168.internalField1719);
        }
        if (this.internalField1643 != null) {
            this.internalField1643.set(nestedValue0168.internalField1721);
        }
        if (this.internalField1640 != null) {
            this.internalField1640.set(nestedValue0168.internalField1722);
        }
        if (this.internalField1641 != null) {
            this.internalField1641.set(nestedValue0168.internalField1720);
        }
        if (this.internalField1646 != null) {
            this.internalField1646.set(nestedValue0168.internalField1730, nestedValue0168.internalField1729, nestedValue0168.internalField1725, nestedValue0168.internalField1726);
        }
        if (this.internalField1642 != null) {
            this.internalField1642.set(nestedValue0168.internalField1724);
        }
    }

    public static final class InternalType0476 {
        public float internalField0205;
        public float internalField0206;
        public float internalField1048;
        public float internalField1047 = 1.0f;
        public float internalField1049;
        public float internalField1046;
        public float internalField1456;
        public float internalField1457 = 1.0f;
        public float internalField1458;
        public float internalField1459;
        public float internalField1460 = -0.6f;
        public float internalField1461;
        public float internalField1462 = 48.0f;
        public float internalField1455 = 32.0f;
        public float internalField1723 = 48.0f;
        public float internalField1731 = 1.0f;
        public float internalField1727 = 1.0f;
        public float internalField1728 = 1.0f;
        public float internalField1717 = 1.0f;
        public float internalField1718;
        public float internalField1719 = 0.12f;
        public float internalField1721 = 0.6f;
        public float internalField1722;
        public float internalField1720 = 1.0f;
        public float internalField1730;
        public float internalField1729;
        public float internalField1725;
        public float internalField1726;
        public float internalField1724 = 192.0f;
    }
}


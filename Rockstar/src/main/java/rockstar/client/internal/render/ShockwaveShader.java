package rockstar.client.internal.render;


import rockstar.client.*;
import rockstar.client.compat.GlUniform;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import rockstar.client.internal.render.ShaderProgramBase;
import rockstar.client.internal.render.JumpShockwaveRenderer;

public class ShockwaveShader
extends ShaderProgramBase {
    public static final int internalField0227 = 12;
    private GlUniform internalField0855;
    private GlUniform internalField0854;
    private GlUniform internalField1356;
    private GlUniform internalField1354;
    private GlUniform internalField1355;
    private GlUniform internalField1353;

    public ShockwaveShader(Identifier identifier) {
        super(identifier, VertexFormats.POSITION_TEXTURE_COLOR);
    }

    @Override
    public void internalMethod06856() {
        this.internalField0855 = this.internalMethod05981("InvViewProj");
        this.internalField0854 = this.internalMethod05981("CircleCenter");
        this.internalField1356 = this.internalMethod05981("WorldRadius");
        this.internalField1354 = this.internalMethod05981("Thickness");
        this.internalField1355 = this.internalMethod05981("RingColor");
        this.internalField1353 = this.internalMethod05981("Strength");
        super.internalMethod06856();
    }

    public void internalMethod04470(Matrix4f matrix4f, JumpShockwaveRenderer.InternalType0218 nestedValue0086) {
        if (this.internalField0855 != null) {
            this.internalField0855.set(matrix4f);
        }
        if (this.internalField0854 != null) {
            this.internalField0854.set(nestedValue0086.internalMethod04651(), nestedValue0086.internalMethod04654(), nestedValue0086.internalMethod08125());
        }
        if (this.internalField1356 != null) {
            this.internalField1356.set(nestedValue0086.internalMethod08127());
        }
        if (this.internalField1354 != null) {
            this.internalField1354.set(nestedValue0086.internalMethod08136());
        }
        if (this.internalField1355 != null) {
            this.internalField1355.set(nestedValue0086.internalMethod08137(), nestedValue0086.internalMethod09362(), nestedValue0086.internalMethod09363(), nestedValue0086.internalMethod09371());
        }
        if (this.internalField1353 != null) {
            this.internalField1353.set(nestedValue0086.internalMethod09372());
        }
    }
}


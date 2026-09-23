package rockstar.client.internal.render;



import rockstar.client.ui.*;
import rockstar.client.*;
import rockstar.client.compat.GlUniform;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import rockstar.client.internal.render.ShaderProgramBase;
import rockstar.client.ui.WindowAccess;

public class KawasePassShader
extends ShaderProgramBase
implements WindowAccess {
    private GlUniform internalField0855;
    private GlUniform internalField0854;
    private GlUniform internalField1356;
    private GlUniform internalField1354;
    private GlUniform internalField1355;

    public KawasePassShader(Identifier identifier) {
        super(identifier, VertexFormats.POSITION_TEXTURE_COLOR);
    }

    public void internalMethod02832(float f) {
        this.internalField0854.set(f);
        this.internalField0855.set(1.0f / (float)internalField0267.getScaledWidth(), 1.0f / (float)internalField0267.getScaledHeight());
        this.internalField1356.set(1.0f);
        this.internalField1354.set(0.0f);
        this.internalField1355.set(1.0f, 1.0f, 1.0f);
    }

    public void internalMethod02942(float f, int n, int n2) {
        this.internalField0854.set(f);
        float f2 = n > 0 ? 1.0f / (float)n : 0.0f;
        float f3 = n2 > 0 ? 1.0f / (float)n2 : 0.0f;
        this.internalField0855.set(f2, f3);
        this.internalField1356.set(1.0f);
        this.internalField1354.set(0.0f);
        this.internalField1355.set(1.0f, 1.0f, 1.0f);
    }

    @Override
    public void internalMethod06856() {
        this.internalField0855 = this.internalMethod05981("Resolution");
        this.internalField0854 = this.internalMethod05981("Offset");
        this.internalField1356 = this.internalMethod05981("Saturation");
        this.internalField1354 = this.internalMethod05981("TintIntensity");
        this.internalField1355 = this.internalMethod05981("TintColor");
        super.internalMethod06856();
    }
}

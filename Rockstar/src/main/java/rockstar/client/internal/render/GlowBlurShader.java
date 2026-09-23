package rockstar.client.internal.render;



import rockstar.client.ui.*;
import rockstar.client.*;
import rockstar.client.compat.GlUniform;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import rockstar.client.internal.render.ShaderProgramBase;
import rockstar.client.ui.WindowAccess;

public class GlowBlurShader
extends ShaderProgramBase
implements WindowAccess {
    private GlUniform internalField0855;
    private GlUniform internalField0854;
    private GlUniform internalField1356;
    private GlUniform internalField1354;
    private GlUniform internalField1355;

    public GlowBlurShader(Identifier identifier) {
        super(identifier, VertexFormats.POSITION_TEXTURE_COLOR);
    }

    public void internalMethod06654(float f) {
        this.internalField0854.set(f);
        this.internalField0855.set(1.0f / (float)internalField0267.getScaledWidth(), 1.0f / (float)internalField0267.getScaledHeight());
    }

    public void internalMethod03262(float f, int n, int n2) {
        this.internalField0854.set(f);
        float f2 = n > 0 ? 1.0f / (float)n : 0.0f;
        float f3 = n2 > 0 ? 1.0f / (float)n2 : 0.0f;
        this.internalField0855.set(f2, f3);
    }

    public void internalMethod01522(float f, int n, int n2, float f2, float f3) {
        this.internalMethod03262(f, n, n2);
        if (this.internalField1356 != null) {
            this.internalField1356.set(f2);
        }
        if (this.internalField1354 != null) {
            this.internalField1354.set(f3);
        }
    }

    public void internalMethod04140(float f, int n, int n2, float f2, float f3, float f4, float f5) {
        this.internalMethod01522(f, n, n2, f2, f3);
        if (this.internalField1355 != null) {
            this.internalField1355.set(f4, f5);
        }
    }

    @Override
    public void internalMethod06856() {
        this.internalField0855 = this.internalMethod05981("Resolution");
        this.internalField0854 = this.internalMethod05981("Offset");
        this.internalField1356 = this.internalMethod05981("OutlineStrength");
        this.internalField1354 = this.internalMethod05981("OutlineRadius");
        this.internalField1355 = this.internalMethod05981("GlowOffset");
        super.internalMethod06856();
    }
}

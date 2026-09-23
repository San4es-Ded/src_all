package rockstar.client.internal.render;


import rockstar.client.*;
import rockstar.client.compat.GlUniform;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import rockstar.client.internal.render.ShaderProgramBase;

public class HeatHazeShader
extends ShaderProgramBase {
    private GlUniform internalField0855;
    private GlUniform internalField0854;
    private GlUniform internalField1356;

    public HeatHazeShader(Identifier identifier) {
        super(identifier, VertexFormats.POSITION_TEXTURE_COLOR);
    }

    @Override
    public void internalMethod06856() {
        this.internalField0855 = this.internalMethod05981("Time");
        this.internalField0854 = this.internalMethod05981("Strength");
        this.internalField1356 = this.internalMethod05981("Aspect");
        super.internalMethod06856();
    }

    public void internalMethod06166(float f, float f2, float f3) {
        if (this.internalField0855 != null) {
            this.internalField0855.set(f);
        }
        if (this.internalField0854 != null) {
            this.internalField0854.set(f2);
        }
        if (this.internalField1356 != null) {
            this.internalField1356.set(f3);
        }
    }
}


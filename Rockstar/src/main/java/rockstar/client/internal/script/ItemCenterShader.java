package rockstar.client.internal.script;



import rockstar.client.internal.render.*;
import rockstar.client.*;
import rockstar.client.compat.GlUniform;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.internal.render.ShaderProgramBase;

public class ItemCenterShader
extends ShaderProgramBase {
    private GlUniform internalField0855;
    private GlUniform internalField0854;
    private GlUniform internalField1356;

    public ItemCenterShader(Identifier identifier) {
        super(identifier, VertexFormats.POSITION_TEXTURE_COLOR);
    }

    public void internalMethod03819(float f, ColorRGBA colorRGBA, float f2, float f3) {
        if (this.internalField0855 != null) {
            this.internalField0855.set(f);
        }
        if (this.internalField0854 != null && colorRGBA != null) {
            this.internalField0854.set(colorRGBA.getRed() / 255.0f, colorRGBA.getGreen() / 255.0f, colorRGBA.getBlue() / 255.0f);
        }
        if (this.internalField1356 != null) {
            this.internalField1356.set(f2, f3);
        }
    }

    @Override
    public void internalMethod06856() {
        this.internalField0855 = this.internalMethod05981("Time");
        this.internalField0854 = this.internalMethod05981("Accent");
        this.internalField1356 = this.internalMethod05981("ItemCenter");
        super.internalMethod06856();
    }
}


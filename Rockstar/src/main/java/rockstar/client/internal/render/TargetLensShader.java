package rockstar.client.internal.render;


import rockstar.client.*;
import java.util.List;
import rockstar.client.compat.GlUniform;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import rockstar.client.internal.render.TargetLensRenderer;
import rockstar.client.internal.render.ShaderProgramBase;

public class TargetLensShader
extends ShaderProgramBase {
    public static final int internalField0227 = 18;
    private GlUniform internalField0855;
    private GlUniform internalField0854;
    private GlUniform internalField1356;
    private final GlUniform[] internalField0139 = new GlUniform[18];

    public TargetLensShader(Identifier identifier) {
        super(identifier, VertexFormats.POSITION_TEXTURE_COLOR);
    }

    @Override
    public void internalMethod06856() {
        this.internalField0855 = this.internalMethod05981("Count");
        this.internalField0854 = this.internalMethod05981("Aspect");
        this.internalField1356 = this.internalMethod05981("Strength");
        for (int i = 0; i < 18; ++i) {
            this.internalField0139[i] = this.internalMethod05981("Lens" + i);
        }
        super.internalMethod06856();
    }

    public void internalMethod02500(float f, float f2, List<TargetLensRenderer.InternalType0350> list) {
        int n = Math.min(list.size(), 18);
        if (this.internalField0855 != null) {
            this.internalField0855.set((float)n);
        }
        if (this.internalField0854 != null) {
            this.internalField0854.set(f);
        }
        if (this.internalField1356 != null) {
            this.internalField1356.set(f2);
        }
        for (int i = 0; i < n; ++i) {
            TargetLensRenderer.InternalType0350 nestedValue0132 = list.get(i);
            if (this.internalField0139[i] == null) continue;
            this.internalField0139[i].set(nestedValue0132.internalMethod00366(), nestedValue0132.internalMethod00369(), nestedValue0132.internalMethod07721(), nestedValue0132.internalMethod07723());
        }
    }
}


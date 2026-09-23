package rockstar.client.internal.render;


import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import java.util.ArrayList;
import java.util.List;
import rockstar.client.compat.Defines;
import rockstar.client.compat.GlUniform;
import rockstar.client.compat.ShaderProgram;
import rockstar.client.compat.ShaderProgramKey;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

public class ShaderProgramBase {
    private static final List<Runnable> internalField0416 = new ArrayList<Runnable>();
    public ShaderProgram internalField0207;
    public ShaderProgramKey internalField0661;

    public ShaderProgramBase(Identifier identifier, VertexFormat vertexFormat) {
        this.internalField0661 = new ShaderProgramKey(identifier.withPrefixedPath("core/"), vertexFormat, Defines.EMPTY);
        internalField0416.add(() -> {
            this.internalField0207 = this.internalField0661.program();
            this.internalMethod06856();
        });
    }

    public RenderPipeline internalMethod05563() {
        return this.internalField0661.program().pipeline();
    }

    public RenderPipeline worldLayerPipeline(boolean translucent, boolean polygonOffset) {
        return this.internalField0661.program().delegate().worldLayerPipeline(translucent, polygonOffset);
    }

    public RenderPipeline worldLayerPipeline(boolean translucent, boolean polygonOffset, boolean cull) {
        return this.internalField0661.program().delegate().worldLayerPipeline(translucent, polygonOffset, cull);
    }

    public boolean internalMethod04677() {
        return this.internalField0207 != null;
    }

    public ShaderProgram internalMethod01220() {
        return RenderSystem.setShader((ShaderProgramKey)this.internalField0661);
    }

    public void internalMethod06856() {
    }

    public GlUniform internalMethod05981(String string) {
        if (this.internalField0207 == null) {
            try {
                this.internalField0207 = this.internalField0661.program();
            }
            catch (Throwable throwable) {
                return null;
            }
            if (this.internalField0207 == null) {
                return null;
            }
        }
        return this.internalField0207.getUniform(string);
    }

    @ApiStatus.Internal
    public static void internalMethod04681() {
        ShaderProgramBase.internalMethod02836(false);
    }

    @ApiStatus.Internal
    public static void internalMethod02836(boolean bl) {
        for (Runnable runnable : internalField0416) {
            try {
                runnable.run();
            }
            catch (Throwable throwable) {
                if (bl) continue;
                StringBuilder stringBuilder = new StringBuilder();
                for (Throwable throwable2 = throwable; throwable2 != null && stringBuilder.length() < 2048; throwable2 = throwable2.getCause()) {
                    stringBuilder.append(stringBuilder.length() == 0 ? "" : "\n  caused by: ").append(throwable2);
                    if (throwable2 == throwable2.getCause()) break;
                }
                System.err.println("[Rockstar] Failed to (re)load a shader program, skipping: " + String.valueOf(stringBuilder));
            }
        }
    }
}

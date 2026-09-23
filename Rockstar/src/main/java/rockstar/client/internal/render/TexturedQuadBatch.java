package rockstar.client.internal.render;



import rockstar.client.render.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import lombok.Generated;
import rockstar.client.compat.ShaderProgramKey;
import rockstar.client.compat.ShaderProgramKeys;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix3x2fStack;
import rockstar.client.render.RenderPipeline;
import rockstar.client.internal.render.UiVertexBatch;

public class TexturedQuadBatch
extends UiVertexBatch {
    private final MatrixStack internalField0254;
    private final Matrix3x2fStack rockstar$guiMatrices;

    public TexturedQuadBatch(VertexFormat vertexFormat, MatrixStack matrixStack) {
        super(vertexFormat);
        this.internalField0254 = matrixStack;
        this.rockstar$guiMatrices = null;
    }

    public TexturedQuadBatch(VertexFormat vertexFormat, Matrix3x2fStack matrixStack) {
        super(vertexFormat);
        this.internalField0254 = new MatrixStack();
        this.rockstar$guiMatrices = matrixStack;
    }

    @Override
    public void internalMethod09053() {
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.enableBlend();
        this.internalMethod00851();
        RenderPipeline.internalMethod09648();
        RenderSystem.setShaderTexture((int)0, (int)0);
        this.internalMethod00854();
    }

    @Generated
    public MatrixStack internalMethod03359() {
        if (this.rockstar$guiMatrices != null) {
            this.internalField0254.peek().getPositionMatrix().set(GuiMatrixCompat.toMatrix4f(this.rockstar$guiMatrices));
        }
        return this.internalField0254;
    }
}

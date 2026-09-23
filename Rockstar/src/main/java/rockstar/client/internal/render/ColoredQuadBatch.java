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

public class ColoredQuadBatch
extends UiVertexBatch {
    private final MatrixStack internalField0254;
    private final Matrix3x2fStack rockstar$guiMatrices;

    public ColoredQuadBatch(VertexFormat vertexFormat, MatrixStack matrixStack) {
        super(vertexFormat);
        this.internalField0254 = matrixStack;
        this.rockstar$guiMatrices = null;
    }

    public ColoredQuadBatch(VertexFormat vertexFormat, Matrix3x2fStack matrixStack) {
        super(vertexFormat);
        this.internalField0254 = new MatrixStack();
        this.rockstar$guiMatrices = matrixStack;
    }

    @Override
    public void internalMethod09053() {
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        RenderPipeline.internalMethod09058();
        this.internalMethod00851();
        RenderPipeline.internalMethod09648();
        this.internalMethod00854();
    }

    @Generated
    public MatrixStack internalMethod02894() {
        if (this.rockstar$guiMatrices != null) {
            this.internalField0254.peek().getPositionMatrix().set(GuiMatrixCompat.toMatrix4f(this.rockstar$guiMatrices));
        }
        return this.internalField0254;
    }
}

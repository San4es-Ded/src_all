package rockstar.client.internal.render;



import rockstar.client.render.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import lombok.Generated;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.joml.Matrix4f;
import rockstar.client.render.CornerRadii;
import rockstar.client.render.UiBatchRenderer;

public abstract class UiVertexBatch {
    public static UiVertexBatch internalField0197;
    private final UiVertexBatch internalField0198;
    public BufferBuilder internalField0033;

    public UiVertexBatch(VertexFormat vertexFormat) {
        UiBatchRenderer.internalMethod02576();
        this.internalField0198 = internalField0197;
        this.internalField0033 = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, vertexFormat);
        internalField0197 = this;
    }

    public void internalMethod00851() {
        BuiltBuffer builtBuffer = this.internalField0033.endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)builtBuffer);
        }
    }

    public boolean internalMethod00818(Matrix4f matrix4f, float f, float f2, float f3, float f4, CornerRadii typedParameter1014, int n) {
        return false;
    }

    public void internalMethod00854() {
        if (internalField0197 == this) {
            internalField0197 = this.internalField0198;
        }
    }

    public abstract void internalMethod09053();

    @Generated
    public UiVertexBatch internalMethod03521() {
        return this.internalField0198;
    }

    @Generated
    public BufferBuilder internalMethod05457() {
        return this.internalField0033;
    }

    @Generated
    public static UiVertexBatch internalMethod04253() {
        return internalField0197;
    }
}


package rockstar.client.internal.render;



import rockstar.client.render.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;
import rockstar.client.render.CornerRadii;
import rockstar.client.internal.render.ShaderProgramBase;
import rockstar.client.render.RenderPipeline;
import rockstar.client.internal.render.UiVertexBatch;

public class RoundedRectBatch
extends UiVertexBatch {
    private final ShaderProgramBase internalField0104 = RenderPipeline.internalField0104;
    private float internalField0205 = 0.5f;

    public RoundedRectBatch() {
        super(VertexFormats.POSITION_COLOR);
    }

    public RoundedRectBatch internalMethod02668(float f) {
        this.internalField0205 = f;
        return this;
    }

    @Override
    public void internalMethod09053() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        this.internalField0104.internalMethod01220();
        this.internalField0104.internalMethod05981("Smoothness").set(this.internalField0205);
        BuiltBuffer builtBuffer = this.internalMethod05457().endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)builtBuffer);
        }
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        this.internalMethod00854();
    }

    public void internalMethod00517(Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int n) {
        this.internalField0104.internalMethod05981("Size").set(f3, f4);
        this.internalField0104.internalMethod05981("Radius").set(f5, f6, f7, f8);
        float f9 = -this.internalField0205 / 2.0f + this.internalField0205 * 2.0f;
        float f10 = this.internalField0205 / 2.0f + this.internalField0205;
        float f11 = f - f9 / 2.0f;
        float f12 = f2 - f10 / 2.0f;
        float f13 = f3 + f9;
        float f14 = f4 + f10;
        this.internalMethod05457().vertex(matrix4f, f11, f12, 0.0f).color(n);
        this.internalMethod05457().vertex(matrix4f, f11, f12 + f14, 0.0f).color(n);
        this.internalMethod05457().vertex(matrix4f, f11 + f13, f12 + f14, 0.0f).color(n);
        this.internalMethod05457().vertex(matrix4f, f11 + f13, f12, 0.0f).color(n);
    }

    @Override
    public boolean internalMethod00818(Matrix4f matrix4f, float f, float f2, float f3, float f4, CornerRadii typedParameter1014, int n) {
        this.internalMethod00517(matrix4f, f, f2, f3, f4, typedParameter1014.internalMethod05337(), typedParameter1014.internalMethod08942(), typedParameter1014.internalMethod05340(), typedParameter1014.internalMethod08939(), n);
        return true;
    }
}


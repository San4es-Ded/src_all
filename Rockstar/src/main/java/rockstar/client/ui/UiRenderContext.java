package rockstar.client.ui;



import rockstar.client.render.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.client.gui.DrawContext;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.render.CornerRadii;
import rockstar.client.render.RenderPipeline;

public class UiRenderContext
extends CustomDrawContext {
    private final int internalField0227;
    private final int internalField0228;
    private final float internalField0205;

    protected UiRenderContext(DrawContext drawContext, int n, int n2, float f) {
        super(drawContext);
        this.internalField0227 = n;
        this.internalField0228 = n2;
        this.internalField0205 = f;
    }

    public static UiRenderContext internalMethod02316(DrawContext drawContext, int n, int n2, float f) {
        return new UiRenderContext(drawContext, n, n2, f);
    }

    public void internalMethod05234(int n, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, CornerRadii typedParameter1014, ColorRGBA colorRGBA) {
        RenderPipeline.internalMethod02681(this.getMatrices(), n, f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, typedParameter1014, colorRGBA);
    }

    @Generated
    public int internalMethod05259() {
        return this.internalField0227;
    }

    @Generated
    public int internalMethod05261() {
        return this.internalField0228;
    }

    @Generated
    public float internalMethod05258() {
        return this.internalField0205;
    }
}


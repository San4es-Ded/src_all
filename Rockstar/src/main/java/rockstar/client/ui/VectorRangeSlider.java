package rockstar.client.ui;





import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.animation.Motion;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.animation.AnimatedFloat;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.render.ScissorStack;
import rockstar.client.ui.UiNode;

public class VectorRangeSlider
extends UiNode {
    private final Supplier<Vec2f> internalField0017;
    private final Supplier<Vec2f> internalField0018;
    private final Consumer<Vec2f> internalField0922;
    private final Consumer<Vec2f> internalField0921;
    private final AnimatedFloat internalField0623 = new AnimatedFloat(Motion.internalMethod01328(500L, Easing.internalField1325));
    private final AnimatedFloat internalField0624 = new AnimatedFloat(Motion.internalMethod01328(500L, Easing.internalField1325));
    private final AnimatedFloat internalField1245 = new AnimatedFloat(Motion.internalMethod01328(500L, Easing.internalField1325));
    private final AnimatedFloat internalField1244 = new AnimatedFloat(Motion.internalMethod01328(500L, Easing.internalField1325));
    private boolean internalField0277;
    private boolean internalField0276;
    private Function<VectorRangeSlider, ColorRGBA> internalField0571 = typedValue015 -> ThemeColors.internalField1614;
    private Function<VectorRangeSlider, ColorRGBA> internalField0570 = typedValue015 -> ThemeColors.internalField1616;
    private Function<VectorRangeSlider, ColorRGBA> internalField1216 = typedValue015 -> ColorRGBA.WHITE.mulAlpha(0.55f + 0.15f * typedValue015.hover());
    private Function<VectorRangeSlider, ColorRGBA> internalField1213 = typedValue015 -> ColorRGBA.WHITE;
    private Function<VectorRangeSlider, ColorRGBA> internalField1215 = typedValue015 -> ColorRGBA.WHITE.mulAlpha(0.45f);
    private Function<VectorRangeSlider, ColorRGBA> internalField1214 = typedValue015 -> ColorRGBA.WHITE;
    private float internalField0205 = 6.0f;
    private float internalField0206 = 4.0f;
    private float internalField1048 = 0.17f;
    private float internalField1047 = 3.0f;
    private float internalField1049 = 2.0f;
    private float internalField1046 = 3.0f;
    private float internalField1456 = 3.0f;
    private float internalField1457 = 1.5f;
    private float internalField1458 = 1.0f;

    public VectorRangeSlider(Supplier<Vec2f> supplier, Supplier<Vec2f> supplier2, Consumer<Vec2f> consumer, Consumer<Vec2f> consumer2) {
        this.internalField0017 = supplier;
        this.internalField0018 = supplier2;
        this.internalField0922 = consumer;
        this.internalField0921 = consumer2;
        Vec2f vec2f = supplier.get();
        Vec2f vec2f2 = supplier2.get();
        this.internalField0623.internalMethod03759(vec2f.x);
        this.internalField0624.internalMethod03759(vec2f.y);
        this.internalField1245.internalMethod03759(vec2f2.x);
        this.internalField1244.internalMethod03759(vec2f2.y);
        this.snapSize();
        this.cursor(CursorType.internalField0567);
        this.onClick(this::internalMethod00098);
    }

    public VectorRangeSlider internalMethod00441(ColorRGBA colorRGBA) {
        this.internalField0571 = typedValue015 -> colorRGBA;
        return this;
    }

    public VectorRangeSlider internalMethod05470(ColorRGBA colorRGBA) {
        this.internalField0570 = typedValue015 -> colorRGBA;
        return this;
    }

    public VectorRangeSlider internalMethod07806(ColorRGBA colorRGBA) {
        this.internalField1216 = typedValue015 -> colorRGBA;
        return this;
    }

    public VectorRangeSlider internalMethod06152(Function<VectorRangeSlider, ColorRGBA> function) {
        this.internalField1216 = function;
        return this;
    }

    public VectorRangeSlider internalMethod08773(ColorRGBA colorRGBA) {
        this.internalField1213 = typedValue015 -> colorRGBA;
        return this;
    }

    public VectorRangeSlider internalMethod07944(ColorRGBA colorRGBA) {
        this.internalField1215 = typedValue015 -> colorRGBA;
        return this;
    }

    public VectorRangeSlider internalMethod08889(ColorRGBA colorRGBA) {
        this.internalField1214 = typedValue015 -> colorRGBA;
        return this;
    }

    public VectorRangeSlider internalMethod05302(float f) {
        this.internalField0205 = f;
        return this;
    }

    public VectorRangeSlider internalMethod07085(float f) {
        this.internalField0206 = f;
        return this;
    }

    public VectorRangeSlider internalMethod08341(float f) {
        this.internalField1048 = f;
        return this;
    }

    public VectorRangeSlider internalMethod08708(float f) {
        this.internalField1047 = f;
        return this;
    }

    public VectorRangeSlider internalMethod08581(float f) {
        this.internalField1049 = f;
        return this;
    }

    public VectorRangeSlider internalMethod05489(float f, float f2) {
        this.internalField1046 = f;
        this.internalField1456 = f2;
        return this;
    }

    public VectorRangeSlider internalMethod08957(float f) {
        this.internalField1457 = f;
        return this;
    }

    public VectorRangeSlider internalMethod09467(float f) {
        this.internalField1458 = f;
        return this;
    }

    public VectorRangeSlider internalMethod05334(Motion typedParameter1004) {
        if (typedParameter1004 != null) {
            this.internalField0623.internalMethod00216(typedParameter1004);
            this.internalField0624.internalMethod00216(typedParameter1004);
            this.internalField1245.internalMethod00216(typedParameter1004);
            this.internalField1244.internalMethod00216(typedParameter1004);
        }
        return this;
    }

    public VectorRangeSlider internalMethod09631(float f) {
        super.width(f);
        return this;
    }

    public VectorRangeSlider internalMethod09588(float f) {
        super.height(f);
        return this;
    }

    public VectorRangeSlider internalMethod06779(float f, float f2) {
        super.size(f, f2);
        return this;
    }

    public VectorRangeSlider internalMethod05543() {
        super.fillWidth();
        return this;
    }

    public VectorRangeSlider internalMethod01682() {
        super.fillHeight();
        return this;
    }

    private float internalMethod01489() {
        return Math.min(this.w(), this.h()) * this.internalField1048;
    }

    private float internalMethod01492() {
        return this.x() + this.internalMethod01489();
    }

    private float internalMethod08013() {
        return this.y() + this.internalMethod01489();
    }

    private float internalMethod08014() {
        return Math.max(1.0f, this.w() - this.internalMethod01489() * 2.0f);
    }

    private float internalMethod08024() {
        return Math.max(1.0f, this.h() - this.internalMethod01489() * 2.0f);
    }

    private float internalMethod01688(float f) {
        return this.internalMethod01492() + f * this.internalMethod08014();
    }

    private float internalMethod01733(float f) {
        return this.internalMethod08013() + f * this.internalMethod08024();
    }

    private float internalMethod09077(float f) {
        return (f - this.internalMethod01492()) / this.internalMethod08014();
    }

    private float internalMethod09085(float f) {
        return (f - this.internalMethod08013()) / this.internalMethod08024();
    }

    private void internalMethod00098(MouseButton typedParameter1015, float f, float f2) {
        float f3;
        if (typedParameter1015 != MouseButton.internalField0102) {
            return;
        }
        float f4 = VectorRangeSlider.internalMethod04021(f, f2, this.internalMethod01688(this.internalField0623.internalMethod02046()), this.internalMethod01733(this.internalField0624.internalMethod02046()));
        if (f4 <= (f3 = VectorRangeSlider.internalMethod04021(f, f2, this.internalMethod01688(this.internalField1245.internalMethod02046()), this.internalMethod01733(this.internalField1244.internalMethod02046())))) {
            this.internalField0277 = true;
            this.internalMethod07105(f, f2);
        } else {
            this.internalField0276 = true;
            this.internalMethod00762(f, f2);
        }
    }

    private void internalMethod07105(float f, float f2) {
        this.internalField0922.accept(this.internalMethod03918(f, f2));
    }

    private void internalMethod00762(float f, float f2) {
        this.internalField0921.accept(this.internalMethod03918(f, f2));
    }

    private Vec2f internalMethod03918(float f, float f2) {
        float f3 = this.internalField1047 + 1.0f;
        float f4 = VectorRangeSlider.internalMethod07268(f, this.x() + f3, this.x() + this.w() - f3);
        float f5 = VectorRangeSlider.internalMethod07268(f2, this.y() + f3, this.y() + this.h() - f3);
        return new Vec2f(this.internalMethod09077(f4), this.internalMethod09085(f5));
    }

    @Override
    public void mouseReleased(float f, float f2, MouseButton typedParameter1015) {
        this.internalField0277 = false;
        this.internalField0276 = false;
        super.mouseReleased(f, f2, typedParameter1015);
    }

    @Override
    protected void measure() {
        if (!this.explicitH) {
            this.prefH = this.w();
        }
    }

    @Override
    protected void onTick(float f, float f2, float f3) {
        if (this.internalField0277) {
            this.internalMethod07105(f2, f3);
        } else if (this.internalField0276) {
            this.internalMethod00762(f2, f3);
        }
        Vec2f vec2f = this.internalField0017.get();
        Vec2f vec2f2 = this.internalField0018.get();
        this.internalField0623.internalMethod03690(vec2f.x);
        this.internalField0623.internalMethod08946(f);
        this.internalField0624.internalMethod03690(vec2f.y);
        this.internalField0624.internalMethod08946(f);
        this.internalField1245.internalMethod03690(vec2f2.x);
        this.internalField1245.internalMethod08946(f);
        this.internalField1244.internalMethod03690(vec2f2.y);
        this.internalField1244.internalMethod08946(f);
        if (this.internalField0277 || this.internalField0276) {
            CursorManager.internalMethod06882(CursorType.internalField1207);
        }
    }

    @Override
    protected void drawSelf(UiRenderContext iII, float f) {
        float f2 = this.x();
        float f3 = this.y();
        float f4 = this.w();
        float f5 = this.h();
        CornerRadii typedParameter1014 = CornerRadii.internalMethod03908(this.internalField0205);
        ColorRGBA colorRGBA = this.internalField0571.apply(this);
        if (colorRGBA != null && colorRGBA.getAlpha() > 0.0f) {
            iII.drawRoundedRect(f2, f3, f4, f5, typedParameter1014, colorRGBA);
        }
        org.joml.Matrix3x2fStack matrixStack = iII.getMatrices();
        ScissorStack.internalMethod06303(matrixStack, f2, f3, f4, f5);
        float f6 = this.internalMethod01492();
        float f7 = this.internalMethod08013();
        float f8 = this.internalMethod08014();
        float f9 = this.internalMethod08024();
        iII.drawDashedBorder(f6, f7, f8, f9, 0.5f, CornerRadii.internalMethod03908(this.internalField0206), this.internalField1046, this.internalField1456, this.internalField1216.apply(this), 0.0f, 1.0f, 1.0f, 0.0f);
        Vec2f vec2f = new Vec2f(this.internalMethod01688(0.0f), this.internalMethod01733(1.0f));
        Vec2f vec2f2 = new Vec2f(this.internalMethod01688(1.0f), this.internalMethod01733(0.0f));
        Vec2f vec2f3 = new Vec2f(this.internalMethod01688(this.internalField0623.internalMethod02046()), this.internalMethod01733(this.internalField0624.internalMethod02046()));
        Vec2f vec2f4 = new Vec2f(this.internalMethod01688(this.internalField1245.internalMethod02046()), this.internalMethod01733(this.internalField1244.internalMethod02046()));
        ColorRGBA colorRGBA2 = this.internalField1215.apply(this);
        iII.drawSmoothBezier(f2, f3, f4, f5, vec2f, vec2f, vec2f3, vec2f3, this.internalField1458, colorRGBA2);
        iII.drawSmoothBezier(f2, f3, f4, f5, vec2f2, vec2f2, vec2f4, vec2f4, this.internalField1458, colorRGBA2);
        iII.drawSmoothBezier(f2, f3, f4, f5, vec2f, vec2f3, vec2f4, vec2f2, this.internalField1457, this.internalField1213.apply(this));
        ColorRGBA colorRGBA3 = this.internalField1214.apply(this);
        VectorRangeSlider.internalMethod05132(iII, vec2f, this.internalField1047, colorRGBA3);
        VectorRangeSlider.internalMethod05068(iII, vec2f2, this.internalField1049, colorRGBA3);
        VectorRangeSlider.internalMethod05068(iII, vec2f3, this.internalField1047, colorRGBA3);
        VectorRangeSlider.internalMethod05068(iII, vec2f4, this.internalField1047, colorRGBA3);
        ScissorStack.internalMethod07643();
        ColorRGBA colorRGBA4 = this.internalField0570.apply(this);
        if (colorRGBA4 != null && colorRGBA4.getAlpha() > 0.0f) {
            iII.drawRoundedBorder(f2, f3, f4, f5, 0.5f, typedParameter1014, colorRGBA4);
        }
    }

    private static void internalMethod05132(UiRenderContext iII, Vec2f vec2f, float f, ColorRGBA colorRGBA) {
        iII.drawRoundedBorder(vec2f.x - f, vec2f.y - f, f * 2.0f, f * 2.0f, 1.0f, CornerRadii.internalMethod03908(f), colorRGBA);
    }

    private static void internalMethod05068(UiRenderContext iII, Vec2f vec2f, float f, ColorRGBA colorRGBA) {
        iII.drawRoundedRect(vec2f.x - f, vec2f.y - f, f * 2.0f, f * 2.0f, CornerRadii.internalMethod03908(f), colorRGBA);
    }

    private static float internalMethod04021(float f, float f2, float f3, float f4) {
        float f5 = f - f3;
        float f6 = f2 - f4;
        return (float)Math.sqrt(f5 * f5 + f6 * f6);
    }

    private static float internalMethod07268(float f, float f2, float f3) {
        return f < f2 ? f2 : (f > f3 ? f3 : f);
    }

}


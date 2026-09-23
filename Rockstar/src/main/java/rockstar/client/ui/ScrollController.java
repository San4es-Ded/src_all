package rockstar.client.ui;




import rockstar.client.render.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.core.*;
import rockstar.client.compat.RenderSystem;
import java.util.function.Function;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.animation.Motion;
import rockstar.client.render.CornerRadii;
import rockstar.client.animation.AnimatedFloat;
import rockstar.client.internal.core.ScrollPolicy;
import rockstar.client.animation.Easing;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.UiContainer;

public class ScrollController {
    private final UiContainer internalField0634;
    private float internalField0205 = 3.0f;
    private float internalField0206 = 1.5f;
    private float internalField1048 = 0.0f;
    private float internalField1047 = 0.0f;
    private float internalField1049 = 16.0f;
    private float internalField1046 = -1.0f;
    private float internalField1456 = 900.0f;
    private ScrollPolicy internalField0916 = ScrollPolicy.internalField0916;
    private Function<ScrollController, ColorRGBA> internalField0571 = typedParameter1005 -> ColorRGBA.WHITE.withAlpha(0.0f);
    private Function<ScrollController, ColorRGBA> internalField0570 = typedParameter1005 -> ColorRGBA.WHITE.withAlpha(255.0f * (0.32f + 0.28f * typedParameter1005.internalMethod05170() + 0.3f * typedParameter1005.internalMethod05173()));
    private InternalType0039 internalField0652;
    private final AnimatedFloat internalField0623 = new AnimatedFloat(0.0f, Motion.internalMethod01328(220L, Easing.internalField1828));
    private final AnimatedFloat internalField0624 = new AnimatedFloat(0.0f, Motion.internalField1381);
    private float internalField1457 = 0.0f;
    private boolean internalField0277 = false;
    private float internalField1458 = 0.0f;
    private boolean internalField0276 = true;
    private float internalField1459;
    private float internalField1460;
    private float internalField1461;
    private float internalField1462;
    private float internalField1455;
    private float internalField1723;
    private float internalField1731;
    private float internalField1727;
    private float internalField1728;
    private float internalField1717;
    private float internalField1718;

    public ScrollController(UiContainer typedValue006) {
        this.internalField0634 = typedValue006;
    }

    public ScrollController internalMethod02353(ScrollPolicy typedParameter1006) {
        this.internalField0916 = typedParameter1006 == null ? ScrollPolicy.internalField0916 : typedParameter1006;
        return this;
    }

    public ScrollController internalMethod00894(float f) {
        this.internalField0205 = Math.max(1.0f, f);
        return this;
    }

    public ScrollController internalMethod02712(float f) {
        this.internalField0206 = f;
        return this;
    }

    public ScrollController internalMethod09005(float f) {
        this.internalField1048 = this.internalField1047 = f;
        return this;
    }

    public ScrollController internalMethod02066(float f, float f2) {
        this.internalField1048 = f;
        this.internalField1047 = f2;
        return this;
    }

    public ScrollController internalMethod08056(float f) {
        this.internalField1049 = Math.max(4.0f, f);
        return this;
    }

    public ScrollController internalMethod07954(float f) {
        this.internalField1046 = f;
        return this;
    }

    public ScrollController internalMethod08313(float f) {
        this.internalField1456 = Math.max(0.0f, f);
        return this;
    }

    public ScrollController internalMethod02260(Function<ScrollController, ColorRGBA> function) {
        this.internalField0571 = function;
        return this;
    }

    public ScrollController internalMethod01081(ColorRGBA colorRGBA) {
        this.internalField0571 = typedParameter1005 -> colorRGBA;
        return this;
    }

    public ScrollController internalMethod04404(Function<ScrollController, ColorRGBA> function) {
        this.internalField0570 = function;
        return this;
    }

    public ScrollController internalMethod06058(ColorRGBA colorRGBA) {
        this.internalField0570 = typedParameter1005 -> colorRGBA;
        return this;
    }

    public ScrollController internalMethod06739(InternalType0039 nestedValue2008) {
        this.internalField0652 = nestedValue2008;
        return this;
    }

    public float internalMethod05170() {
        return this.internalField0624.internalMethod02046();
    }

    public float internalMethod05173() {
        return this.internalField0277 ? 1.0f : 0.0f;
    }

    public float internalMethod08004() {
        return this.internalField0623.internalMethod02046();
    }

    public boolean internalMethod05172() {
        return this.internalField0276;
    }

    public float internalMethod08008() {
        return this.internalField1046 < 0.0f ? this.internalField0205 / 2.0f - 0.5f : this.internalField1046;
    }

    public float internalMethod08018() {
        return this.internalField1455;
    }

    public float internalMethod08021() {
        return this.internalField1723;
    }

    public float internalMethod09797() {
        return this.internalField1731;
    }

    public float internalMethod09798() {
        return this.internalField1727;
    }

    public float internalMethod09802() {
        return this.internalField1459;
    }

    public float internalMethod09803() {
        return this.internalField1460;
    }

    public float internalMethod09159() {
        return this.internalField1461;
    }

    public float internalMethod09160() {
        return this.internalField1462;
    }

    public ScrollPolicy internalMethod06079() {
        return this.internalField0916;
    }

    void internalMethod05171() {
        this.internalField1457 = this.internalField1456;
    }

    void internalMethod04465(float f, float f2, float f3) {
        boolean bl;
        boolean bl2 = bl = this.internalField0634.internalMethod03629() && this.internalField0916 != ScrollPolicy.internalField1385;
        if (bl) {
            this.internalMethod08005();
        }
        boolean bl3 = bl && this.internalMethod03818(f2, f3, this.internalField1455, this.internalField1723, this.internalField1731, this.internalField1727);
        boolean bl4 = bl && this.internalMethod06500(f2, f3);
        this.internalField0624.internalMethod03690(bl3 ? 1.0f : 0.0f);
        if (this.internalField1457 > 0.0f) {
            this.internalField1457 = Math.max(0.0f, this.internalField1457 - f);
        }
        if (this.internalField0277 && !UiUtils.internalMethod07370(0)) {
            this.internalField0277 = false;
        }
        if (this.internalField0277) {
            this.internalMethod06499(f2, f3);
        }
        boolean bl5 = this.internalField0916 == ScrollPolicy.internalField0917 || this.internalField1457 > 0.0f || bl4 || this.internalField0277;
        this.internalField0623.internalMethod03690(bl && bl5 ? 1.0f : 0.0f);
        this.internalField0623.internalMethod08946(f);
        this.internalField0624.internalMethod08946(f);
    }

    private void internalMethod08005() {
        this.internalField0276 = this.internalField0634.internalMethod01862().internalMethod05114();
        float f = this.internalField0634.internalMethod03626();
        float f2 = this.internalField0634.internalMethod03630();
        float f3 = this.internalField0634.internalMethod08624();
        float f4 = this.internalField0634.internalMethod08625();
        float f5 = this.internalField0634.internalMethod08639();
        float f6 = Math.max(0.0f, this.internalField0634.internalMethod08638() - f5);
        float f7 = Math.max(0.0f, this.internalField0634.internalMethod09418() - f5);
        float f8 = this.internalField0634.internalMethod09428();
        this.internalField1728 = Math.max(0.0f, f6 - this.internalField1048 - this.internalField1047);
        float f9 = this.internalField1717 = f7 > 0.0f ? Math.max(this.internalField1049, this.internalField1728 * (f6 / f7)) : this.internalField1728;
        if (this.internalField1717 > this.internalField1728) {
            this.internalField1717 = this.internalField1728;
        }
        float f10 = this.internalField1728 - this.internalField1717;
        float f11 = f8 > 0.0f ? ScrollController.internalMethod04464(this.internalField0634.internalMethod09419() / f8, 0.0f, 1.0f) : 0.0f;
        this.internalField1718 = f11 * f10;
        if (this.internalField0276) {
            this.internalField1461 = this.internalField0205;
            this.internalField1462 = this.internalField1728;
            this.internalField1459 = f + f3 - this.internalField0205 - this.internalField0206;
            this.internalField1460 = f2 + f5 + this.internalField1048;
            this.internalField1455 = this.internalField1459;
            this.internalField1723 = this.internalField1460 + this.internalField1718;
            this.internalField1731 = this.internalField0205;
            this.internalField1727 = this.internalField1717;
        } else {
            this.internalField1461 = this.internalField1728;
            this.internalField1462 = this.internalField0205;
            this.internalField1459 = f + f5 + this.internalField1048;
            this.internalField1460 = f2 + f4 - this.internalField0205 - this.internalField0206;
            this.internalField1455 = this.internalField1459 + this.internalField1718;
            this.internalField1723 = this.internalField1460;
            this.internalField1731 = this.internalField1717;
            this.internalField1727 = this.internalField0205;
        }
    }

    private void internalMethod06499(float f, float f2) {
        float f3 = this.internalField1728 - this.internalField1717;
        float f4 = (this.internalField0276 ? f2 - this.internalField1460 : f - this.internalField1459) - this.internalField1458;
        f4 = ScrollController.internalMethod04464(f4, 0.0f, f3);
        float f5 = f3 > 0.0f ? f4 / f3 : 0.0f;
        this.internalField0634.internalMethod04955(f5 * this.internalField0634.internalMethod09428());
        this.internalField1457 = this.internalField1456;
    }

    void internalMethod06945(UiRenderContext iII, float f) {
        if (this.internalField0916 == ScrollPolicy.internalField1385 || !this.internalField0634.internalMethod03629()) {
            return;
        }
        float f2 = this.internalField0623.internalMethod02046();
        if (f2 <= 0.01f) {
            return;
        }
        this.internalMethod08005();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)(f * f2));
        if (this.internalField0652 != null) {
            this.internalField0652.internalMethod02675(iII, this);
        } else {
            ColorRGBA colorRGBA;
            float f3 = this.internalMethod08008();
            ColorRGBA colorRGBA2 = this.internalField0571.apply(this);
            if (colorRGBA2 != null && colorRGBA2.getAlpha() > 0.0f) {
                iII.drawRoundedRect(this.internalField1459, this.internalField1460, this.internalField1461, this.internalField1462, CornerRadii.internalMethod03908(f3), colorRGBA2);
            }
            if ((colorRGBA = this.internalField0570.apply(this)) != null && colorRGBA.getAlpha() > 0.0f) {
                iII.drawRoundedRect(this.internalField1455, this.internalField1723, this.internalField1731, this.internalField1727, CornerRadii.internalMethod03908(f3), colorRGBA);
            }
        }
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)f);
    }

    boolean internalMethod04466(float f, float f2, boolean bl) {
        if (!bl || this.internalField0916 == ScrollPolicy.internalField1385 || !this.internalField0634.internalMethod03629()) {
            return false;
        }
        if (this.internalField0623.internalMethod02046() < 0.05f && !this.internalMethod06500(f, f2)) {
            return false;
        }
        this.internalMethod08005();
        if (this.internalMethod03818(f, f2, this.internalField1455, this.internalField1723, this.internalField1731, this.internalField1727)) {
            this.internalField0277 = true;
            this.internalField1458 = this.internalField0276 ? f2 - this.internalField1723 : f - this.internalField1455;
            this.internalField1457 = this.internalField1456;
            return true;
        }
        if (this.internalMethod03818(f, f2, this.internalField1459, this.internalField1460, this.internalField1461, this.internalField1462)) {
            float f3 = this.internalField0276 ? f2 - this.internalField1460 : f - this.internalField1459;
            float f4 = ScrollController.internalMethod04464(f3 / Math.max(1.0f, this.internalField1728), 0.0f, 1.0f);
            this.internalField0634.internalMethod04955(f4 * this.internalField0634.internalMethod09428());
            this.internalField1457 = this.internalField1456;
            return true;
        }
        return false;
    }

    void internalMethod05174() {
        this.internalField0277 = false;
    }

    private boolean internalMethod03818(float f, float f2, float f3, float f4, float f5, float f6) {
        return f >= f3 && f <= f3 + f5 && f2 >= f4 && f2 <= f4 + f6;
    }

    private boolean internalMethod06500(float f, float f2) {
        float f3 = this.internalField0205 + this.internalField0206 + 6.0f;
        if (this.internalField0276) {
            float f4 = Math.max(this.internalField1459 + this.internalField1461, this.internalField1459 + this.internalField1461 + this.internalField0206);
            return f >= Math.min(this.internalField1459, this.internalField1459 - f3) && f <= f4 && f2 >= this.internalField1460 && f2 <= this.internalField1460 + this.internalField1728;
        }
        float f5 = Math.max(this.internalField1460 + this.internalField1462, this.internalField1460 + this.internalField1462 + this.internalField0206);
        return f2 >= Math.min(this.internalField1460, this.internalField1460 - f3) && f2 <= f5 && f >= this.internalField1459 && f <= this.internalField1459 + this.internalField1728;
    }

    private static float internalMethod04464(float f, float f2, float f3) {
        return Math.max(f2, Math.min(f3, f));
    }

    public static interface InternalType0039 {
        public void internalMethod02675(UiRenderContext localValue1, ScrollController localValue2);
    }
}


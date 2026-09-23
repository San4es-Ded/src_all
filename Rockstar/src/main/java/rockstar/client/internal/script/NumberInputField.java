package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.*;
import java.util.function.Function;
import java.util.function.Supplier;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.CustomTextField;
import rockstar.client.util.TextUtils;
import rockstar.client.core.CursorType;
import rockstar.client.ui.UiNode;

public class NumberInputField
extends UiNode {
    private final SizedFont internalField0447;
    private final InternalType0148 internalField0275;
    private final InternalType0147 internalField0274;
    private final float internalField0205;
    private final float internalField0206;
    private Supplier<String> internalField0017;
    private Supplier<String> internalField0018 = () -> "";
    private Function<NumberInputField, ColorRGBA> internalField0571 = typedValue014 -> ColorRGBA.WHITE;
    private Function<NumberInputField, ColorRGBA> internalField0570;
    private CustomTextField internalField0936;
    private boolean internalField0277;
    private static NumberInputField internalField0929;
    public NumberInputField(SizedFont typedValue020, InternalType0148 nestedValue2020, InternalType0147 nestedValue2019, float f, float f2) {
        this.internalField0447 = typedValue020;
        this.internalField0275 = nestedValue2020;
        this.internalField0274 = nestedValue2019;
        this.internalField0205 = f;
        this.internalField0206 = f2;
        this.internalField0017 = () -> TextUtils.internalMethod00670(nestedValue2020.get());
        this.height(typedValue020.internalMethod04890());
        this.cursor(CursorType.internalField0567);
        this.snapPosition();
        this.snapSize();
    }

    public NumberInputField internalMethod05772(Supplier<String> supplier) {
        if (supplier != null) {
            this.internalField0017 = supplier;
        }
        return this;
    }

    public NumberInputField internalMethod03023(Supplier<String> supplier) {
        if (supplier != null) {
            this.internalField0018 = supplier;
        }
        return this;
    }

    public NumberInputField internalMethod05286(ColorRGBA colorRGBA) {
        this.internalField0571 = typedValue014 -> colorRGBA;
        return this;
    }

    public NumberInputField internalMethod09106(Supplier<ColorRGBA> supplier) {
        this.internalField0571 = typedValue014 -> (ColorRGBA)supplier.get();
        return this;
    }

    public NumberInputField internalMethod01821(Function<NumberInputField, ColorRGBA> function) {
        this.internalField0571 = function;
        return this;
    }

    public NumberInputField internalMethod02106(ColorRGBA colorRGBA) {
        this.internalField0570 = typedValue014 -> colorRGBA;
        return this;
    }

    public NumberInputField internalMethod03943(Function<NumberInputField, ColorRGBA> function) {
        this.internalField0570 = function;
        return this;
    }

    public NumberInputField internalMethod06158(float f) {
        super.width(f);
        return this;
    }

    public NumberInputField internalMethod00877() {
        super.fillWidth();
        return this;
    }

    public boolean internalMethod05419() {
        return this.internalField0277;
    }

    private CustomTextField internalMethod03109() {
        if (this.internalField0936 == null) {
            this.internalField0936 = new CustomTextField(this.internalField0447);
            this.internalField0936.internalMethod07563(true);
        }
        return this.internalField0936;
    }

    public static void internalMethod05283(UiNode typedValue004) {
        NumberInputField typedValue014 = internalField0929;
        if (typedValue014 != null && typedValue014 != typedValue004 && typedValue014.internalField0277) {
            typedValue014.internalMethod05442();
        }
    }

    private void internalMethod05418() {
        NumberInputField.internalMethod05283(this);
        String string = TextUtils.internalMethod00670(this.internalField0275.get());
        this.internalMethod03109().internalMethod00484(string);
        this.internalMethod03109().internalMethod09000(string);
        this.internalMethod03109().internalMethod07508(true);
        this.internalMethod03109().internalMethod00344();
        this.internalField0277 = true;
        internalField0929 = this;
    }

    private void internalMethod05442() {
        if (this.internalField0936 != null) {
            try {
                String string = this.internalField0936.internalMethod06202().replace(',', '.');
                if (!(string.isEmpty() || string.equals("-") || string.equals("."))) {
                    float f = Float.parseFloat(string);
                    f = Math.max(this.internalField0205, Math.min(this.internalField0206, f));
                    this.internalField0274.accept(f);
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            this.internalField0936.internalMethod07508(false);
        }
        this.internalField0277 = false;
        if (internalField0929 == this) {
            internalField0929 = null;
        }
    }

    private void internalMethod08211() {
        if (this.internalField0936 != null) {
            this.internalField0936.internalMethod07508(false);
        }
        this.internalField0277 = false;
        if (internalField0929 == this) {
            internalField0929 = null;
        }
    }

    @Override
    public boolean mouseClicked(float f, float f2, MouseButton typedParameter1015) {
        if (this.contains(f, f2)) {
            if (typedParameter1015 == MouseButton.internalField0102) {
                if (!this.internalField0277) {
                    this.internalMethod05418();
                } else {
                    this.internalMethod03109().internalMethod01643(f, f2, typedParameter1015);
                }
            }
            return true;
        }
        if (this.internalField0277) {
            this.internalMethod05442();
        }
        return false;
    }

    @Override
    public boolean keyPressed(int n, int n2, int n3) {
        if (!this.internalField0277) {
            return false;
        }
        if (n == 257 || n == 335) {
            this.internalMethod05442();
            return true;
        }
        if (n == 256) {
            this.internalMethod08211();
            return true;
        }
        this.internalMethod03109().internalMethod05727(n, n2, n3);
        return true;
    }

    @Override
    public boolean charTyped(char c, int n) {
        if (!this.internalField0277) {
            return false;
        }
        return this.internalMethod03109().internalMethod05413(c, n);
    }

    @Override
    public void onTick(float f, float f2, float f3) {
        if (this.internalField0277 && this.internalField0936 != null && !this.internalField0936.internalMethod00342()) {
            this.internalMethod05442();
        }
    }

    private String internalMethod03172() {
        String string = this.internalField0277 && this.internalField0936 != null ? this.internalField0936.internalMethod06202() : this.internalField0017.get();
        return string == null ? "" : string;
    }

    @Override
    public void measure() {
        if (!this.explicitW) {
            String string = this.internalField0018.get();
            this.prefW = Math.max(6.0f, this.internalField0447.internalMethod00965(this.internalMethod03172()) + this.internalField0447.internalMethod00965(string == null ? "" : string));
        }
        if (!this.explicitH) {
            this.prefH = this.internalField0447.internalMethod04890();
        }
    }

    @Override
    public void drawSelf(UiRenderContext iII, float f) {
        Object object;
        float f2;
        float f3 = this.y() + this.h() / 2.0f - this.internalField0447.internalMethod04890() / 2.0f;
        ColorRGBA colorRGBA = this.internalField0571.apply(this);
        String string = this.internalField0018.get();
        if (this.internalField0277 && this.internalField0936 != null) {
            f2 = this.internalField0447.internalMethod00965(this.internalField0936.internalMethod06202());
            this.internalField0936.internalMethod05191(this.x() - 3.0f, f3 - 3.0f, f2 + 40.0f, this.internalField0447.internalMethod04890() + 6.0f);
            this.internalField0936.internalMethod08627(1.0f);
            this.internalField0936.internalMethod00143(colorRGBA);
            this.internalField0936.internalMethod03398(iII);
        } else {
            object = this.internalMethod03172();
            f2 = this.internalField0447.internalMethod00965((String)object);
            if (!((String)object).isEmpty()) {
                iII.drawText(this.internalField0447, (String)object, this.x(), f3, colorRGBA);
            }
        }
        if (string != null && !string.isEmpty()) {
            object = this.internalField0570 != null ? this.internalField0570.apply(this) : colorRGBA;
            iII.drawText(this.internalField0447, string, this.x() + f2, f3, (ColorRGBA)object);
        }
    }

    public static interface InternalType0148 {
        public float get();
    }

    public static interface InternalType0147 {
        public void accept(float localValue1);
    }
}


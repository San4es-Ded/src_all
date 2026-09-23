package rockstar.client.internal.script;




import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.ui.UiNode;

public class WrappedTextNode
extends UiNode {
    private final SizedFont internalField0447;
    private final Supplier<String> internalField0017;
    private Function<WrappedTextNode, ColorRGBA> internalField0571 = typedValue012 -> ColorRGBA.WHITE;
    private boolean internalField0277;
    private float internalField0205 = 3.0f;
    private final List<String> internalField0416 = new ArrayList<String>();
    private String internalField0248;
    private float internalField0206 = -1.0f;

    public WrappedTextNode(SizedFont typedValue020, String string) {
        this(typedValue020, () -> string);
    }

    public WrappedTextNode(SizedFont typedValue020, Supplier<String> supplier) {
        this.internalField0447 = typedValue020;
        this.internalField0017 = supplier;
    }

    public WrappedTextNode internalMethod02683(ColorRGBA colorRGBA) {
        this.internalField0571 = typedValue012 -> colorRGBA;
        return this;
    }

    public WrappedTextNode internalMethod04268(Function<WrappedTextNode, ColorRGBA> function) {
        this.internalField0571 = function;
        return this;
    }

    public WrappedTextNode internalMethod04223(boolean bl) {
        this.internalField0277 = bl;
        return this;
    }

    public WrappedTextNode internalMethod07389() {
        this.internalField0277 = true;
        return this;
    }

    public WrappedTextNode internalMethod06753(float f) {
        this.internalField0205 = f;
        return this;
    }

    public WrappedTextNode internalMethod00519(float f) {
        super.width(f);
        return this;
    }

    public WrappedTextNode internalMethod08001(float f) {
        super.height(f);
        return this;
    }

    public WrappedTextNode internalMethod04302() {
        super.fillWidth();
        return this;
    }

    public WrappedTextNode internalMethod08220() {
        super.fillHeight();
        return this;
    }

    @Override
    public void measure() {
        this.internalMethod02999();
        if (!this.explicitH) {
            int n = Math.max(1, this.internalField0416.size());
            this.prefH = (float)n * this.internalField0447.internalMethod04890() + (float)(n - 1) * this.internalField0205;
        }
    }

    private void internalMethod02999() {
        String string = this.internalField0017.get();
        float f = this.w();
        boolean bl = string == null ? this.internalField0248 == null : string.equals(this.internalField0248);
        if (bl && Math.abs(f - this.internalField0206) < 0.5f) {
            return;
        }
        this.internalField0248 = string;
        this.internalField0206 = f;
        this.internalField0416.clear();
        if (string == null || string.isEmpty()) {
            return;
        }
        if (f < 8.0f) {
            this.internalField0416.add(string);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String string2 : string.split(" ")) {
            if (stringBuilder.length() == 0) {
                stringBuilder.append(string2);
                continue;
            }
            if (this.internalField0447.internalMethod00965(String.valueOf(stringBuilder) + " " + string2) > f) {
                this.internalField0416.add(stringBuilder.toString());
                stringBuilder = new StringBuilder(string2);
                continue;
            }
            stringBuilder.append(' ').append(string2);
        }
        if (stringBuilder.length() > 0) {
            this.internalField0416.add(stringBuilder.toString());
        }
    }

    @Override
    public void drawSelf(UiRenderContext iII, float f) {
        if (this.internalField0416.isEmpty()) {
            return;
        }
        ColorRGBA colorRGBA = this.internalField0571.apply(this);
        if (colorRGBA == null || colorRGBA.getAlpha() <= 0.0f) {
            return;
        }
        float f2 = this.x();
        float f3 = this.y();
        float f4 = this.w();
        float f5 = f3;
        for (String string : this.internalField0416) {
            if (this.internalField0277) {
                iII.drawCenteredText(this.internalField0447, string, f2 + f4 / 2.0f, f5, colorRGBA);
            } else {
                iII.drawText(this.internalField0447, string, f2, f5, colorRGBA);
            }
            f5 += this.internalField0447.internalMethod04890() + this.internalField0205;
        }
    }

}

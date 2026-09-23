package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import net.minecraft.client.util.math.MatrixStack;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.animation.Motion;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.animation.AnimatedFloat;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.KeybindUtils;
import rockstar.client.util.TextUtils;
import rockstar.client.render.ScissorStack;
import rockstar.client.ui.UiNode;

public class KeybindButton
extends UiNode {
    private final SizedFont internalField0447;
    private final IntSupplier internalField0657;
    private final IntConsumer internalField0540;
    private boolean internalField0277;
    private int internalField0227 = -1;
    private static KeybindButton internalField0928;
    private final AnimatedFloat internalField0623 = new AnimatedFloat(1.0f, Motion.internalMethod01328(380L, Easing.internalField1828));
    private final AnimatedFloat internalField0624 = new AnimatedFloat(0.0f, Motion.internalField1381);
    private final AnimatedFloat internalField1245 = new AnimatedFloat(Motion.internalMethod01328(300L, Easing.internalField1828));
    private Function<KeybindButton, ColorRGBA> internalField0571 = typedValue013 -> ThemeColors.internalField1614;
    private ColorRGBA internalField0777 = ThemeColors.internalField1613;
    private ColorRGBA internalField0776 = ThemeColors.internalField1310;
    private float internalField0205 = 3.0f;
    private float internalField0206 = 4.0f;
    private float internalField1048 = 8.0f;
    private IntFunction<String> internalField0602 = TextUtils::internalMethod04982;
    private String internalField0248;

    public KeybindButton(SizedFont typedValue020, IntSupplier intSupplier, IntConsumer intConsumer) {
        this.internalField0447 = typedValue020;
        this.internalField0657 = intSupplier;
        this.internalField0540 = intConsumer;
        this.internalMethod08232(11.0f);
        this.snapSize();
        this.internalField1245.internalMethod03759(this.internalMethod06270());
    }

    public KeybindButton internalMethod07203(ColorRGBA colorRGBA) {
        this.internalField0571 = typedValue013 -> colorRGBA;
        return this;
    }

    public KeybindButton internalMethod00579(Function<KeybindButton, ColorRGBA> function) {
        this.internalField0571 = function;
        return this;
    }

    public KeybindButton internalMethod04105(ColorRGBA colorRGBA) {
        this.internalField0777 = colorRGBA;
        return this;
    }

    public KeybindButton internalMethod07752(ColorRGBA colorRGBA) {
        this.internalField0776 = colorRGBA;
        return this;
    }

    public KeybindButton internalMethod07230(float f) {
        this.internalField0205 = f;
        return this;
    }

    public KeybindButton internalMethod01015(float f) {
        this.internalField0206 = f;
        return this;
    }

    public KeybindButton internalMethod07893(float f) {
        this.internalField1048 = f;
        return this;
    }

    public KeybindButton internalMethod01488(IntFunction<String> intFunction) {
        if (intFunction != null) {
            this.internalField0602 = intFunction;
        }
        return this;
    }

    public KeybindButton internalMethod02734(String string) {
        this.internalField0248 = string;
        return this;
    }

    public KeybindButton internalMethod02444(Motion typedParameter1004) {
        if (typedParameter1004 != null) {
            this.internalField0623.internalMethod00216(typedParameter1004);
        }
        return this;
    }

    public KeybindButton internalMethod04883(Motion typedParameter1004) {
        if (typedParameter1004 != null) {
            this.internalField1245.internalMethod00216(typedParameter1004);
        }
        return this;
    }

    public boolean internalMethod06272() {
        return this.internalField0277;
    }

    public static boolean internalMethod06277() {
        return internalField0928 != null && KeybindButton.internalField0928.internalField0277;
    }

    public void internalMethod06271() {
        this.internalField0277 = false;
        if (internalField0928 == this) {
            internalField0928 = null;
        }
    }

    public boolean internalMethod00008(MouseButton typedParameter1015) {
        if (!this.internalField0277 || typedParameter1015 == null) {
            return false;
        }
        this.internalMethod05829(KeybindUtils.internalMethod08541(typedParameter1015.internalMethod02957()));
        return true;
    }

    public static boolean internalMethod01436(MouseButton typedParameter1015) {
        KeybindButton typedValue013 = internalField0928;
        if (typedValue013 == null) {
            return false;
        }
        if (!typedValue013.internalField0277) {
            internalField0928 = null;
            return false;
        }
        return typedValue013.internalMethod00008(typedParameter1015);
    }

    public static void internalMethod06276() {
        if (internalField0928 != null) {
            internalField0928.internalMethod06271();
        }
    }

    public KeybindButton internalMethod08232(float f) {
        super.height(f);
        return this;
    }

    public KeybindButton internalMethod08129(float f) {
        super.width(f);
        return this;
    }

    public KeybindButton internalMethod02928(float f, float f2) {
        super.size(f, f2);
        return this;
    }

    public void internalMethod00007(MouseButton typedParameter1015) {
        if (!this.internalField0277 && typedParameter1015 == MouseButton.internalField0102) {
            this.internalField0277 = true;
            internalField0928 = this;
        } else if (this.internalField0277) {
            this.internalMethod05829(KeybindUtils.internalMethod08541(typedParameter1015.internalMethod02957()));
        }
    }

    @Override
    public boolean keyPressed(int n, int n2, int n3) {
        if (!this.internalField0277) {
            return false;
        }
        if (n == 256 || n == 261) {
            this.internalMethod05829(-1);
            return true;
        }
        int n4 = KeybindUtils.internalMethod06041(n, n3);
        if (n4 == Integer.MIN_VALUE) {
            return true;
        }
        this.internalMethod05829(n4);
        return true;
    }

    @Override
    public boolean keyReleased(int n, int n2, int n3) {
        if (!this.internalField0277) {
            return false;
        }
        int n4 = KeybindUtils.internalMethod08281(n, n3);
        if (n4 == Integer.MIN_VALUE) {
            return false;
        }
        this.internalMethod05829(n4);
        return true;
    }

    private void internalMethod05829(int n) {
        this.internalField0227 = this.internalField0657.getAsInt();
        this.internalField0540.accept(n);
        this.internalField0623.internalMethod03759(0.0f);
        this.internalField0623.internalMethod03690(1.0f);
        this.internalField0277 = false;
        if (internalField0928 == this) {
            internalField0928 = null;
        }
    }

    @Override
    public void measure() {
        if (!this.explicitW) {
            this.prefW = this.internalField1245.internalMethod02046();
        }
    }

    @Override
    public void onTick(float f, float f2, float f3) {
        this.internalField1245.internalMethod03690(this.internalMethod06270());
        this.internalField1245.internalMethod08946(f);
        this.internalField0624.internalMethod03690(this.internalField0277 ? 1.0f : 0.0f);
        this.internalField0624.internalMethod08946(f);
        this.internalField0623.internalMethod08946(f);
    }

    private float internalMethod06270() {
        return this.internalField0447.internalMethod00965(this.internalMethod05698()) + this.internalField0206 * 2.0f;
    }

    private String internalMethod05698() {
        if (this.internalField0277) {
            int n = KeybindUtils.internalMethod06867();
            String string = n != 0 ? "..." : (this.internalField0248 != null ? this.internalField0248 : this.internalField0602.apply(this.internalField0657.getAsInt()));
            return KeybindUtils.internalMethod07434(n) + (string == null ? "" : string);
        }
        String string = this.internalField0602.apply(this.internalField0657.getAsInt());
        return string == null ? "" : string;
    }

    @Override
    public void drawSelf(UiRenderContext iII, float f) {
        String string;
        float f2 = this.x();
        float f3 = this.y();
        float f4 = this.w();
        float f5 = this.h();
        ColorRGBA colorRGBA = this.internalField0571.apply(this);
        if (colorRGBA != null && colorRGBA.getAlpha() > 0.0f) {
            iII.drawRoundedRect(f2, f3, f4, f5, CornerRadii.internalMethod03908(this.internalField0205), colorRGBA);
        }
        float f6 = this.internalField0623.internalMethod02046();
        float f7 = f3 + f5 / 2.0f - this.internalField0447.internalMethod04890() / 2.0f;
        float f8 = f2 + this.internalField0206;
        ColorRGBA colorRGBA2 = this.internalField0777.mix(this.internalField0776, this.internalField0624.internalMethod02046()).mulAlpha(0.75f + 0.25f * Math.max(this.hover(), this.internalField0624.internalMethod02046()));
        org.joml.Matrix3x2fStack matrixStack = iII.getMatrices();
        ScissorStack.internalMethod06303(matrixStack, f2, f3, f4, f5);
        iII.drawText(this.internalField0447, this.internalMethod05698(), f8 + (f6 - 1.0f) * this.w(), f7, colorRGBA2.mulAlpha(f6));
        if (f6 < 0.999f && (string = this.internalField0602.apply(this.internalField0227)) != null) {
            iII.drawText(this.internalField0447, string, f8 + f6 * this.w(), f7, colorRGBA2.mulAlpha(1.0f - f6));
        }
        ScissorStack.internalMethod07643();
    }

}


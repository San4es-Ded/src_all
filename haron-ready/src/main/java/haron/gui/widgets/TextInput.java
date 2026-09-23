package haron.gui.widgets;

import haron.gui.widgets.TextInputType;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.client.util.math.MatrixStack;

public class TextInput {
    private static final Set<TextInput> ACTIVE_INPUTS = new HashSet<TextInput>();
    private final TextInputType inputType;
    private String value;
    private String placeholder;
    private int maxLength = 256;
    private float x;
    private float y;
    private float width = 100.0f;
    private float height = 16.0f;
    private boolean focused;
    private boolean visible = true;
    private boolean editable = true;
    private boolean centered;
    private boolean clearOnFocus;
    private boolean valueSelected;
    private Consumer<String> changeListener;
    private Color background = pryrvd.e;
    private Color foreground = pryrvd.a;

    public void setClearOnFocus(boolean bl) {
        this.clearOnFocus = bl;
    }

    public void setRawValue(String string) {
        String string2 = this.clean(string);
        if (string2.length() > this.maxLength) {
            string2 = string2.substring(0, this.maxLength);
        }
        this.value = string2;
    }

    public TextInput(TextInputType imb71z2) {
        this(imb71z2, "", "");
    }

    public TextInput(TextInputType imb71z2, String string, String string2) {
        this.inputType = imb71z2 == null ? TextInputType.TEXT : imb71z2;
        this.value = this.clean(string);
        this.placeholder = string2 == null ? "" : string2;
    }

    public TextInput(TextInputType imb71z2, String string) {
        this(imb71z2, "", string);
    }

    public void e(boolean bl) {
        this.centered = bl;
    }

    public boolean e() {
        return this.editable;
    }

    public float i() {
        return this.width;
    }

    public void b(boolean bl) {
        this.visible = bl;
    }

    public String b() {
        return this.placeholder;
    }

    public void b(int n) {
        this.a(n);
    }

    public void b(String string) {
        this.placeholder = string == null ? "" : string;
    }

    public void b(float f, float f2) {
        this.width = Math.max(1.0f, f);
        this.height = Math.max(1.0f, f2);
    }

    public boolean b(int n, int n2, int n3) {
        if (!this.focused || !this.editable) {
            return false;
        }
        if (n == 259) {
            if (this.valueSelected) {
                this.value = "";
                this.valueSelected = false;
                if (this.changeListener != null) {
                    this.changeListener.accept(this.value);
                }
            } else if (!this.value.isEmpty()) {
                this.value = this.value.substring(0, this.value.length() - 1);
                if (this.changeListener != null) {
                    this.changeListener.accept(this.value);
                }
            }
        } else if (this.valueSelected) {
            this.valueSelected = false;
        }
        return true;
    }

    public void b(float f) {
        this.height = Math.max(1.0f, f);
    }

    public int c() {
        return this.maxLength;
    }

    public void c(boolean bl) {
        this.editable = bl;
    }

    public float h() {
        return this.y;
    }

    private boolean contains(int n, int n2) {
        return (float)n >= this.x && (float)n2 >= this.y && (float)n <= this.x + this.width && (float)n2 <= this.y + this.height;
    }

    public static boolean f() {
        int n = 683;
        return !ACTIVE_INPUTS.isEmpty();
    }

    public void d(boolean bl) {
        int n = 98;
        this.a(bl);
    }

    public boolean d() {
        return this.focused;
    }

    static String a(String string, String string2, int n, int n2, int n3, int n4) {
        return string;
    }

    public void a(Color color, Color color2) {
        if (color != null) {
            this.background = color;
        }
        if (color2 != null) {
            this.foreground = color2;
        }
    }

    public void a(boolean bl) {
        boolean bl2 = this.focused;
        this.focused = bl;
        if (bl) {
            ACTIVE_INPUTS.add(this);
            if (!bl2 && this.clearOnFocus && !this.value.isEmpty()) {
                this.valueSelected = true;
            }
        } else {
            ACTIVE_INPUTS.remove(this);
            this.valueSelected = false;
        }
    }

    public void a(int n) {
        this.maxLength = Math.max(0, n);
        if (this.value.length() > this.maxLength) {
            this.value = this.value.substring(0, this.maxLength);
        }
    }

    public void a(float f, float f2, float f3, float f4) {
        this.x = f;
        this.y = f2;
        this.width = Math.max(1.0f, f3);
        this.height = Math.max(1.0f, f4);
    }

    public void a(float f) {
        this.width = Math.max(1.0f, f);
    }

    public void a(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n, int n2, float f5) {
        float f6;
        this.a(f, f2, f3, f4);
        if (!this.visible) {
            return;
        }
        int n3 = (int)(255.0f * f5);
        Color color = pryrvd.a(this.background, n3);
        Color color2 = pryrvd.a(pryrvd.f, n3);
        Color color3 = pryrvd.a(pryrvd.q, n3);
        Color color4 = pryrvd.a(pryrvd.n, n3);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, f4 + 1.0f, 6.5f, color3, color3, color4, color4, matrixStack);
        s7swsm2.a(f, f2, f3, f4, 6.5f, color, color, color2, color2, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.a[14];
        String string = this.value.isEmpty() ? this.placeholder : this.value;
        Color color5 = this.value.isEmpty() ? pryrvd.a(pryrvd.b, n3) : pryrvd.a(this.foreground, n3);
        float f7 = this.centered ? f + (f3 - v6hnga2.a(string)) / 2.0f : f + 5.0f;
        float f8 = f2 + f4 / 2.0f - v6hnga2.b("Ag") / 4.0f;
        s7swsm2.b().a(f, f2, f3, f4, 6.5f, matrixStack);
        v6hnga2.a(string, f7, (double)f8, color5, matrixStack);
        if (this.focused && this.editable && (f6 = f7 + v6hnga2.a(this.value)) < f + f3 - 2.0f) {
            s7swsm2.a(f6, f2 + 4.0f, 0.5f, f4 - 8.0f, pryrvd.a(this.foreground, n3), matrixStack);
        }
        s7swsm2.b().a(matrixStack);
    }

    public String a() {
        int n = 155;
        return this.value;
    }

    public void a(String string) {
        this.setValue(string);
    }

    public void a(Consumer<String> consumer) {
        this.changeListener = consumer;
    }

    public boolean a(int n, int n2, int n3) {
        boolean bl = this.a(n, n2);
        if (n3 == 0) {
            this.a(bl);
        }
        return bl;
    }

    public boolean a(int n, int n2) {
        return this.visible && this.contains(n, n2);
    }

    public boolean a(char c, int n) {
        if (!this.focused || !this.editable || Character.isISOControl(c)) {
            return false;
        }
        if (this.valueSelected) {
            this.value = "";
            this.valueSelected = false;
        }
        this.setValue(TextInput.$sf$0(this.value, c));
        return true;
    }

    public float g() {
        return this.x;
    }

    public float j() {
        return this.height;
    }

    private String clean(String string) {
        if (string == null) {
            return "";
        }
        switch (this.inputType.ordinal()) {
            case 1: {
                return string.replaceAll("[^0-9-]", "");
            }
            case 2: {
                return string.replaceAll("[^0-9+\\-., ]", "");
            }
        }
        return string;
    }

    private void setValue(String string) {
        String string2 = this.clean(string);
        if (string2.length() > this.maxLength) {
            string2 = string2.substring(0, this.maxLength);
        }
        this.value = string2;
        if (this.changeListener != null) {
            this.changeListener.accept(this.value);
        }
    }

    private static /* synthetic */ String $sf$0(String string, char c) {
        return string + c;
    }
}


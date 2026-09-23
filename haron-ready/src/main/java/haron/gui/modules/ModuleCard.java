package haron.gui.modules;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.client.MinecraftClientAccess;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.core.ToggleableEntry;
import haron.gui.core.GuiLayerRegistry;
import haron.gui.core.ScrollFadeOverlay;
import haron.gui.core.GuiLayer;
import haron.gui.core.ClickGuiScreen;
import haron.gui.modules.VerticalPlacement;
import haron.gui.settings.SettingGroupHeaderRow;
import haron.gui.settings.TextInputMode;
import haron.gui.settings.ItemHighlightSettingRow;
import haron.gui.settings.ColorSettingRow;
import haron.gui.settings.ModeSettingRow;
import haron.gui.settings.NumberSettingRow;
import haron.gui.settings.SettingRow;
import haron.gui.settings.SettingsList;
import haron.gui.settings.ToggleSettingRow;
import haron.gui.settings.TextSettingRow;
import haron.gui.settings.KeybindSettingRow;
import haron.module.HaronModule;
import haron.render.icons.IconTexture;
import haron.render.icons.HaronIcons;
import haron.render.ShapeRenderer;
import haron.settings.NumberSetting;
import haron.settings.TextTokenType;
import haron.settings.ColorSetting;
import haron.settings.KeybindSetting;
import haron.settings.Setting;
import haron.settings.SettingGroup;
import haron.settings.ModeSetting;
import haron.settings.ValidatedTextSetting;
import haron.settings.ItemHighlightSetting;
import haron.settings.BooleanSetting;
import haron.theme.pryrvd;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ModuleCard {
    public static final float a = 150.0f;
    public static final float b = 30.0f;
    public static final float c = 100.0f;
    public static final float d = 30.0f;
    public static final float e = 9.5f;
    public static final float f = 8.0f;
    public static final float g = 8.0f;
    public static final float h = 7.0f;
    private static final int k = 25;
    private static final float l = 5.0f;
    private static final float DOCK_W = 42.0f;
    private static final float SIDE_GAP = 8.0f;
    private static final float SCREEN_MARGIN = 4.0f;
    private final ToggleableEntry m;
    private final HaronModule n;
    private final int o;
    private boolean p;
    private final VerticalPlacement q;
    private final boolean r;
    private float s;
    private float t;
    private final AnimatedValue u = new AnimatedValue();
    private boolean v = false;
    private float w;
    private float x;
    private boolean y = false;
    private float z;
    private float A;
    private boolean B = false;
    private final SettingsList C;
    private final ScrollFadeOverlay D;
    private final AnimatedValue E = new AnimatedValue();
    private boolean F = true;
    private KeybindSettingRow G;
    private float H = 0.0f;
    private int I = 0;
    private int J = 0;
    public static int i;
    public static boolean j;

    private void clampInsideGui(float f, float f2) {
        float f3 = f + ClickGuiScreen.d();
        float f4 = 150.0f;
        float f5 = this.s + 75.0f;
        float f6 = f + ClickGuiScreen.d() / 2.0f;
        if (f5 <= f6) {
            this.w = f - 42.0f - 8.0f - 150.0f;
            this.p = true;
        } else {
            this.w = f3 + 8.0f;
            this.p = false;
        }
        if (this.w < 4.0f) {
            this.w = 4.0f;
        }
        if (this.w + 150.0f > ModuleCard.screenW() - 4.0f) {
            this.w = ModuleCard.screenW() - 4.0f - 150.0f;
        }
        if (this.x < 4.0f) {
            this.x = 4.0f;
        }
        if (this.x + f2 > ModuleCard.screenH() - 4.0f) {
            this.x = ModuleCard.screenH() - 4.0f - f2;
        }
    }

    private void pushOutOfGui(float f) {
        boolean bl;
        float f2 = f - 42.0f;
        float f3 = f + ClickGuiScreen.d();
        boolean bl2 = bl = this.w + 150.0f > f2 - 8.0f && this.w < f3 + 8.0f;
        if (!bl) {
            return;
        }
        float f4 = f2 - 8.0f - 150.0f;
        float f5 = f3 + 8.0f;
        boolean bl3 = f4 >= 4.0f;
        boolean bl4 = f5 + 150.0f <= ModuleCard.screenW() - 4.0f;
        float f6 = this.w + 75.0f;
        float f7 = f + ClickGuiScreen.d() / 2.0f;
        this.w = f6 <= f7 ? (bl3 ? f4 : (bl4 ? f5 : 4.0f)) : (bl4 ? f5 : (bl3 ? f4 : ModuleCard.screenW() - 4.0f - 150.0f));
    }

    private void clampPopupPosition(float f, float f2) {
        float f3 = ModuleCard.screenW();
        float f4 = ModuleCard.screenH();
        if (this.w < 4.0f) {
            this.w = 4.0f;
        }
        if (this.w + f > f3 - 4.0f) {
            this.w = f3 - 4.0f - f;
        }
        if (this.x < 4.0f) {
            this.x = 4.0f;
        }
        if (this.x + f2 > f4 - 4.0f) {
            this.x = f4 - 4.0f - f2;
        }
    }

    private static float screenW() {
        try {
            int n = 288;
            return (float)MinecraftClientAccess.d.getWidth() / 2.0f;
        }
        catch (Throwable throwable) {
            return 10000.0f;
        }
    }

    private static float screenH() {
        try {
            return (float)MinecraftClientAccess.d.getHeight() / 2.0f;
        }
        catch (Throwable throwable) {
            return 10000.0f;
        }
    }

    public ModuleCard(ToggleableEntry mt5wd72, HaronModule jxs16t2, int n, boolean bl, float f, float f2, VerticalPlacement w19pyk2) {
        this(mt5wd72, jxs16t2, n, bl, f, f2, w19pyk2, false);
    }

    private ModuleCard(ToggleableEntry mt5wd72, HaronModule jxs16t2, int n, boolean bl, float f, float f2, VerticalPlacement w19pyk2, boolean bl2) {
        this.m = mt5wd72;
        this.n = jxs16t2;
        this.o = n;
        this.p = bl;
        this.s = f;
        this.t = f2;
        this.q = w19pyk2;
        this.r = bl2;
        this.C = new SettingsList();
        this.C.b(7.0f);
        this.C.a(100.0f);
        this.z();
        this.y();
        this.D = new ScrollFadeOverlay(25, 5.0f, 9.5f);
        this.u.d(0.0);
        this.u.a(1.0, 0.13, Easings.h);
        this.E.d(1.0);
    }

    public boolean e() {
        return this.v && this.u.d() && this.u.j() < 0.01;
    }

    public boolean e(int n, int n2) {
        return this.v || this.u.j() < 0.5 ? false : GuiInput.a(this.w, this.x, 150.0f, this.a(), (double)n, (double)n2);
    }

    public float i() {
        int n = 121;
        return !this.y ? this.x : this.A;
    }

    private float b(float f) {
        float f2 = this.a();
        switch (this.q.ordinal()) {
            case 0: {
                return 8.0f + f / 2.0f;
            }
            case 2: {
                return f2 - 8.0f - f / 2.0f;
            }
        }
        return f2 / 2.0f;
    }

    public boolean b(int n, int n2) {
        if (this.v || this.u.j() < 0.5) {
            return false;
        }
        boolean bl = this.f(n, n2);
        if (!this.d(n, n2) && !bl) {
            if (!this.C.d()) {
                return false;
            }
            this.C.g();
            return true;
        }
        if (this.C.d() && !this.C.b(n, n2)) {
            this.C.g();
            return true;
        }
        return this.C.b(this.w, this.x, 150.0f, this.a(), n, n2);
    }

    public void b() {
        if (this.v) {
            return;
        }
        this.v = true;
        this.C.g();
        this.u.a(0.0, 0.13, Easings.g);
    }

    public void b(MatrixStack matrixStack, ShapeRenderer s7swsm2, int n, int n2) {
    }

    public boolean x() {
        return this.C.d();
    }

    public KeybindSettingRow s() {
        return this.G;
    }

    public void c() {
        this.v = true;
        this.u.d(0.0);
        this.C.g();
        if (!this.r || this.G == null) {
            return;
        }
        this.G.e();
    }

    public void c(int n, int n2) {
        if (!this.r || this.G == null) {
            this.C.c(n, n2);
        } else {
            this.G.a(n, n2);
        }
    }

    public boolean n() {
        int n = 133;
        return this.p;
    }

    public boolean h() {
        return this.v || this.u.j() < 0.5 ? false : (this.r && this.G != null ? this.G.a_() : this.C.l());
    }

    public boolean f() {
        int n = 185;
        return this.v;
    }

    public boolean f(int n, int n2) {
        return !this.r ? this.C.a(n, n2) : false;
    }

    public ToggleableEntry l() {
        return this.m;
    }

    public void d() {
        this.v = false;
        this.y = false;
        this.u.d(1.0);
        this.C.g();
    }

    public boolean d(int n, int n2) {
        if (this.v || this.u.j() < 0.5) {
            return false;
        }
        if (this.y) {
            return GuiInput.a(this.w, this.x, 150.0f, this.a(), (double)n, (double)n2);
        }
        IconTexture hclqea2 = HaronIcons.getInfo("arrow_v");
        float f = hclqea2 != null ? (float)hclqea2.b() / 2.0f : 0.0f;
        float f2 = 150.0f + f;
        float f3 = this.p ? this.w : this.w - f;
        return GuiInput.a(f3, this.x, f2, this.a(), (double)n, (double)n2);
    }

    public boolean a(char c, int n) {
        if (this.r) {
            return false;
        }
        return this.C.a(c, n);
    }

    public void a(int n, int n2, double d, double d2) {
        if (this.r) {
            return;
        }
        this.C.a(this.a(), n, n2, d, d2);
    }

    public boolean a(int n, int n2, int n3) {
        if (!this.r || this.G == null) {
            return this.C.a(n, n2, n3);
        }
        boolean bl = this.G.a(n, n2, n3);
        if (bl && this.m != null) {
            this.m.a(this.G.c());
        }
        return bl;
    }

    public void a(float f, int n, int n2) {
        if (this.r) {
            if (859982073 < i) {
                return;
            }
            return;
        }
        if (this.d(n, n2)) {
            this.C.a(f, this.a());
            return;
        }
    }

    public static ModuleCard a(ToggleableEntry mt5wd72, HaronModule jxs16t2, int n, boolean bl, float f, float f2) {
        return new ModuleCard(mt5wd72, jxs16t2, n, bl, f, f2, VerticalPlacement.CENTER, true);
    }

    public void a(int n) {
        if (this.G != null) {
            this.G.a(n);
        }
    }

    public boolean a(int n, int n2) {
        if (this.v || this.u.j() < 0.5) {
            return false;
        }
        boolean bl = this.f(n, n2);
        if (!this.d(n, n2) && !bl) {
            if (!this.C.d()) {
                return false;
            }
            this.C.g();
            return true;
        }
        if (!this.r || this.G == null) {
            return this.C.a(this.w, this.x, 150.0f, this.a(), n, n2);
        }
        float f = this.a();
        float f2 = (float)Math.max(0.0, Math.min(1.0, this.u.j()));
        float f3 = 150.0f * f2;
        float f4 = f * f2;
        float f5 = this.w + 75.0f;
        float f6 = this.x + f / 2.0f;
        float f7 = f5 - f3 / 2.0f;
        float f8 = f6 - f4 / 2.0f;
        return this.G.a(f7 + 3.0f * f2, f8 + (f4 - 20.0f * f2) / 2.0f, f3 - 6.0f * f2, n, n2);
    }

    public boolean a(ModuleCard sudbet2) {
        if (this.p != sudbet2.p) {
            return false;
        }
        return BooleanCoercion.from(this.x + this.a() <= sudbet2.x || this.x >= sudbet2.x + sudbet2.a() ? 0 : 1);
    }

    public float a() {
        if (this.r) {
            return 30.0f;
        }
        float f = this.C.m();
        float f2 = Math.min(ModuleCard.screenH() - 30.0f, 300.0f);
        return Math.max(30.0f, Math.min(f, f2));
    }

    private TextInputMode inputModeFor(TextTokenType tokenType) {
        return switch (tokenType) {
            case TEXT, COMMAND -> TextInputMode.COMMAND;
            case PLAYER -> TextInputMode.PLAYER;
            case NUMBER -> TextInputMode.INT;
            case PRICE -> TextInputMode.PRICE;
        };
    }

    public void a(float f) {
        int n = 154;
        this.s = f;
    }

    private SettingRow a(Setting<?> mmdapc2) {
        if (mmdapc2 instanceof BooleanSetting) {
            return new ToggleSettingRow((BooleanSetting)mmdapc2);
        }
        if (mmdapc2 instanceof NumberSetting) {
            return new NumberSettingRow((NumberSetting)mmdapc2);
        }
        if (mmdapc2 instanceof ModeSetting) {
            return new ModeSettingRow((ModeSetting)mmdapc2);
        }
        if (mmdapc2 instanceof KeybindSetting) {
            return new KeybindSettingRow((KeybindSetting)mmdapc2);
        }
        if (mmdapc2 instanceof ColorSetting) {
            return new ColorSettingRow((ColorSetting)mmdapc2);
        }
        if (mmdapc2 instanceof ValidatedTextSetting) {
            ValidatedTextSetting trepl22 = (ValidatedTextSetting)mmdapc2;
            return new TextSettingRow(trepl22, this.inputModeFor(trepl22.b()));
        }
        if (mmdapc2 instanceof ItemHighlightSetting) {
            return new ItemHighlightSettingRow((ItemHighlightSetting)mmdapc2);
        }
        return !(mmdapc2 instanceof SettingGroup) ? null : new SettingGroupHeaderRow((SettingGroup)mmdapc2);
    }

    public static boolean a(float f, float f2, float f3, float f4) {
        return BooleanCoercion.from(f + f2 <= f3 || f >= f3 + f4 ? 0 : 1);
    }

    public static float a(float f, float f2, VerticalPlacement w19pyk2, float f3, float f4) {
        float f5 = f + f2 / 2.0f;
        return f5 - (switch (w19pyk2.ordinal()) {
            case 0 -> 8.0f + f3 / 2.0f;
            case 2 -> f4 - 8.0f - f3 / 2.0f;
            default -> f4 / 2.0f;
        });
    }

    public boolean a(float f, float f2) {
        if (this.y) {
            return false;
        }
        float f3 = this.s + this.t / 2.0f;
        return BooleanCoercion.from(f3 < f || f3 > f + f2 ? 1 : 0);
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, int n, int n2) {
        if (this.r) {
            return;
        }
        float f = (float)this.u.j();
        if (f >= 0.1f) {
            float f2 = Math.max(0.0f, Math.min(1.0f, f));
            this.C.a(matrixStack, s7swsm2, n, n2, f2, f2);
        }
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2, float f3, float f4) {
        this.a(matrixStack, s7swsm2, f, f2, n, n2, f3, f4, false);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2, float f3, float f4, int n3) {
        this.w = this.z;
        this.x = this.A;
        this.clampInsideGui(f, f3);
        this.clampPopupPosition(150.0f, f3);
        float f5 = 150.0f * f4;
        float f6 = f3 * f4;
        float f7 = this.w + 75.0f - f5 / 2.0f;
        float f8 = this.x + f3 / 2.0f - f6 / 2.0f;
        Color color = pryrvd.a(pryrvd.PANEL_BORDER_TOP, n3);
        Color color2 = pryrvd.a(pryrvd.PANEL_BORDER_BOT, n3);
        s7swsm2.a(f7 - 0.5f * f4, f8 - 0.5f * f4, f5 + f4, f6 + f4, 9.5f * f4, color, color, color2, color2, matrixStack);
        Color color3 = pryrvd.a(pryrvd.CELL_BG_TOP, n3);
        Color color4 = pryrvd.a(pryrvd.CELL_BG_BOT, n3);
        s7swsm2.a(f7, f8, f5, f6, 9.5f * f4, color3, color3, color4, color4, matrixStack);
        HaronIcons.getInfo("arrow_v");
        if (this.r && f4 > 0.1f && this.G != null) {
            this.G.a(matrixStack, s7swsm2, f7 + 3.0f * f4, f8 + (f6 - 20.0f * f4) / 2.0f, f5 - 6.0f * f4, n, n2, f4, f4);
            return;
        }
        if (!this.r && f4 > 0.1f) {
            float f9;
            int n4;
            float f10 = f8 + 7.0f * f4;
            float f11 = (f6 - 7.0f - 9.5f) * f4;
            if (f5 <= 0.0f || f11 <= 0.0f) {
                return;
            }
            s7swsm2.b().a(f7, f10, f5, f11, 9.5f * f4, matrixStack);
            this.C.a(matrixStack, s7swsm2, f7, f8, f5, f6, n, n2, f4, f4);
            int n5 = n4 = !this.C.d(f6) || this.C.e(f8 + f6) ? 0 : 1;
            if (BooleanCoercion.from(n4) != this.F) {
                this.E.a(n4 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
                this.F = BooleanCoercion.from(n4);
            }
            if ((f9 = (float)this.E.j()) > 0.01f) {
                this.D.a(matrixStack, s7swsm2, f7, f8, f5, f6, f4 * f9);
            }
            s7swsm2.b().a(matrixStack);
        }
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2, float f3, float f4, float f5, int n3) {
        float f6;
        int n4;
        float f7 = this.s + this.t / 2.0f;
        IconTexture hclqea2 = HaronIcons.getInfo("arrow_v");
        float f8 = hclqea2 != null ? (float)hclqea2.b() / 2.0f : 0.0f;
        float f9 = hclqea2 != null ? (float)hclqea2.c() / 2.0f : 0.0f;
        this.x = 8.0f;
        float f10 = f;
        float f11 = f + f4;
        this.w = f10 + 42.0f + (f11 - f10 - 42.0f - 150.0f) / 2.0f;
        this.clampPopupPosition(150.0f, f3);
        float f12 = 150.0f * f5;
        float f13 = f3 * f5;
        float f14 = f8 * f5;
        float f15 = f9 * f5;
        float f16 = this.w + 75.0f - f12 / 2.0f;
        float f17 = this.x + f3 / 2.0f - f13 / 2.0f;
        float f18 = f7 - f15 / 2.0f;
        float f19 = !this.p ? f16 - f14 : f16 + f12;
        Color color = pryrvd.a(pryrvd.PANEL_BORDER_TOP, n3);
        Color color2 = pryrvd.a(pryrvd.PANEL_BORDER_BOT, n3);
        s7swsm2.a(f16 - 0.5f * f5, f17 - 0.5f * f5, f12 + f5, f13 + f5, 9.5f * f5, color, color, color2, color2, matrixStack);
        Color color3 = pryrvd.a(pryrvd.CELL_BG_TOP, n3);
        Color color4 = pryrvd.a(pryrvd.CELL_BG_BOT, n3);
        s7swsm2.a(f16, f17, f12, f13, 9.5f * f5, color3, color3, color4, color4, matrixStack);
        if (hclqea2 != null) {
            Identifier identifier = hclqea2.a();
            Color color5 = pryrvd.a(pryrvd.aa, n3);
            if (this.p) {
                s7swsm2.a(identifier, f19, f18, f14, f15, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, color5, matrixStack);
            } else {
                s7swsm2.a(identifier, f19, f18, f14, f15, 0.0f, 1.0f, 0.0f, -1.0f, 1.0f, color5, matrixStack);
            }
        }
        if (this.r && f5 > 0.1f && this.G != null) {
            this.G.a(matrixStack, s7swsm2, f16 + 3.0f * f5, f17 + (f13 - 20.0f * f5) / 2.0f, f12 - 6.0f * f5, n, n2, f5, f5);
            return;
        }
        if (this.r || f5 <= 0.1f) {
            return;
        }
        float f20 = f17 + 7.0f * f5;
        float f21 = (f13 - 7.0f - 9.5f) * f5;
        if (f12 <= 0.0f || f21 <= 0.0f) {
            return;
        }
        s7swsm2.b().a(f16, f20, f12, f21, 9.5f * f5, matrixStack);
        this.C.a(matrixStack, s7swsm2, f16, f17, f12, f13, n, n2, f5, f5);
        int n5 = n4 = !this.C.d(f13) || this.C.e(f17 + f13) ? 0 : 1;
        if (BooleanCoercion.from(n4) != this.F) {
            this.E.a(n4 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
            this.F = BooleanCoercion.from(n4);
        }
        if ((f6 = (float)this.E.j()) > 0.01f) {
            this.D.a(matrixStack, s7swsm2, f16, f17, f12, f13, f5 * f6);
        }
        s7swsm2.b().a(matrixStack);
    }

    public void a(float f, float f2, boolean bl) {
        this.y = true;
        this.z = f;
        this.A = f2;
        this.B = bl;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2, float f3, float f4, boolean bl) {
        this.u.a();
        this.E.a();
        float f5 = (float)this.u.j();
        if (f5 >= 0.01f) {
            this.H = f5;
            this.I = n;
            this.J = n2;
            int n3 = !bl ? n : -1;
            int n4 = !bl ? n2 : -1;
            float f6 = this.a();
            float f7 = ClickGuiScreen.d();
            float f8 = Math.max(0.0f, Math.min(1.0f, f5));
            int n5 = (int)(255.0f * f8);
            if (this.y) {
                this.a(matrixStack, s7swsm2, f, f2, n3, n4, f6, f8, n5);
            } else {
                this.a(matrixStack, s7swsm2, f, f2, n3, n4, f6, f7, f8, n5);
            }
            if (this.v || f8 <= 0.5f) {
                return;
            }
            GuiLayerRegistry.a().a(this.A(), this.w, this.x, 150.0f, f6);
        }
    }

    public int m() {
        return this.o;
    }

    public VerticalPlacement o() {
        return this.q;
    }

    public float p() {
        return this.s;
    }

    public float k() {
        return this.a();
    }

    public int t() {
        if (this.G != null) {
            return this.G.c();
        }
        return -1;
    }

    public boolean g() {
        return this.r;
    }

    public void v() {
        if (this.r) {
            return;
        }
        this.C.i();
    }

    public float j() {
        int n = 772;
        return !this.y ? this.w : this.z;
    }

    public SettingsList q() {
        return this.C;
    }

    private void z() {
        if (this.n != null) {
            Iterator<Setting<?>> iterator = this.n.m().iterator();
            while (iterator.hasNext()) {
                SettingRow um973w2 = this.a(iterator.next());
                if (um973w2 == null) continue;
                this.C.a(um973w2);
            }
        }
    }

    public float[] w() {
        if (this.r) {
            return null;
        }
        return this.C.j();
    }

    public boolean u() {
        return !this.r ? this.C.h() : false;
    }

    public boolean r() {
        return this.y;
    }

    private void y() {
        int n = this.m != null ? this.m.f() : -1;
        this.G = new KeybindSettingRow("Клавиша активации", n);
    }

    private GuiLayer A() {
        int n = 673;
        return !this.r ? GuiLayer.SETTINGS_PANEL : GuiLayer.KEYBIND_PANEL;
    }
}

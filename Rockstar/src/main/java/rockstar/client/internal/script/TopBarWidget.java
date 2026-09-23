package rockstar.client.internal.script;









import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.ui.ModuleSettingsPanel;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.i18n.Language;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.internal.script.AnimatedColor;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.TextUtils;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.MathUtils;
import rockstar.client.util.Stopwatch;
import rockstar.profile.Profile;

public class TopBarWidget
implements MinecraftClientAccess {
    public static final float internalField0205 = 22.0f;
    private static final float internalField0206 = 12.0f;
    private static final float internalField1048 = 8.0f;
    private static final float internalField1047 = 0.5f;
    private static final float internalField1049 = 19.0f;
    private static final float internalField1046 = 106.0f;
    private final Runnable internalField0659;
    private final List<InternalType0055> internalField0416 = new ArrayList<InternalType0055>();
    private float internalField1460;
    private float internalField1461;
    private boolean internalField0276;
    private float internalField1462;
    private float internalField1455;
    private final AnimatedValue internalField0808 = new AnimatedValue(220L, 0.0f, Easing.internalField1626);
    private static final ColorRGBA internalField0777 = new ColorRGBA(28.0f, 28.0f, 30.0f);
    private static final ColorRGBA internalField0776 = new ColorRGBA(255.0f, 255.0f, 255.0f);
    private final Stopwatch internalField0519 = new Stopwatch();
    private boolean internalField1099;
    private boolean internalField1100;
    private final AnimatedColor internalField0814 = new AnimatedColor(300L, new ColorRGBA(255.0f, 255.0f, 255.0f), Easing.internalField1627);
    private final AnimatedColor internalField0813 = new AnimatedColor(300L, new ColorRGBA(255.0f, 255.0f, 255.0f), Easing.internalField1627);
    private float internalField1723;
    private float internalField1731;
    private float internalField1727;

    public TopBarWidget(Runnable runnable) {
        this.internalField0659 = runnable;
        this.internalField0416.add(new InternalType0055("mainmenu.bar.wallpaper", () -> {
            this.internalField0276 = false;
            this.internalField0659.run();
        }, () -> false));
    }

    public void internalMethod00578(UiRenderContext iII, int n, int n2, float f) {
        this.internalMethod01573(iII, n, n2, f, 1.0f);
    }

    public void internalMethod01573(UiRenderContext iII, int n, int n2, float f, float f2) {
        this.internalField1455 = f;
        this.internalMethod06863(n);
        ColorRGBA colorRGBA = this.internalField0814.internalMethod04159();
        ColorRGBA colorRGBA2 = this.internalField0813.internalMethod04159();
        this.internalMethod03553(iII, n, f, f2, colorRGBA2);
        float f3 = f * f2;
        if (f < 0.5f) {
            this.internalField0276 = false;
        }
        this.internalField1462 = f3;
        this.internalMethod02078(iII);
        if (f3 <= 0.003f) {
            return;
        }
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        float f4 = (1.0f - f2) * -5.0f;
        float f5 = 12.0f;
        float f6 = 8.0f;
        float f7 = 11.0f - f5 / 2.0f + f4;
        float f8 = f6 + f5 + 12.0f;
        for (InternalType0055 nestedValue2010 : this.internalField0416) {
            nestedValue2010.internalField1046 = typedValue020.internalMethod00965(LanguageManager.internalMethod07214(nestedValue2010.internalField0248));
            nestedValue2010.internalField1049 = f8;
            nestedValue2010.internalField0205 = f8 - 6.0f;
            nestedValue2010.internalField0206 = 0.0f;
            nestedValue2010.internalField1048 = nestedValue2010.internalField1046 + 12.0f;
            nestedValue2010.internalField1047 = 22.0f;
            f8 += nestedValue2010.internalField1046 + 12.0f;
        }
        int n3 = iII.internalMethod05259();
        int n4 = iII.internalMethod05261();
        for (InternalType0055 nestedValue2010 : this.internalField0416) {
            String string = LanguageManager.internalMethod07214(nestedValue2010.internalField0248);
            boolean bl = f3 > 0.5f && (float)n3 >= nestedValue2010.internalField0205 && (float)n3 <= nestedValue2010.internalField0205 + nestedValue2010.internalField1048 && (float)n4 >= nestedValue2010.internalField0206 && (float)n4 <= nestedValue2010.internalField0206 + nestedValue2010.internalField1047;
            boolean bl2 = nestedValue2010.internalField0424.getAsBoolean();
            nestedValue2010.internalField0808.internalMethod07062(bl || bl2);
            if (bl) {
                CursorManager.internalMethod06882(CursorType.internalField0567);
            }
            float f9 = MathUtils.internalMethod02587(0.83f, 1.0, nestedValue2010.internalField0808.internalMethod02881());
            float f10 = nestedValue2010.internalField0206 + nestedValue2010.internalField1047 / 2.0f - typedValue020.internalMethod04890() / 2.0f + 0.5f + f4;
            iII.drawText(typedValue020, string, nestedValue2010.internalField1049, f10, colorRGBA.withAlpha(255.0f * f9 * f3));
        }
    }

    private void internalMethod03553(UiRenderContext iII, int n, float f, float f2, ColorRGBA colorRGBA) {
        float f3;
        if (f <= 0.003f) {
            this.internalField1727 = 0.0f;
            return;
        }
        SizedFont typedValue020 = Fonts.internalField0449.internalMethod01432(8.0f);
        float f4 = 11.0f;
        float f5 = f4 - typedValue020.internalMethod04890() / 2.0f + 0.5f;
        float f6 = 10.0f;
        float f7 = 6.0f;
        float f8 = 13.0f;
        float f9 = 12.0f;
        float f10 = f;
        float f11 = f * f2;
        String string = TextUtils.internalMethod08270() + "  " + TextUtils.internalMethod04048();
        String string2 = Profile.getUsername();
        float f12 = typedValue020.internalMethod00965(string);
        float f13 = typedValue020.internalMethod00965(string2);
        float f14 = (float)n - f6;
        float f15 = f14 - f12;
        f14 = f15 - (f7 + 4.0f);
        float f16 = f14 - f9;
        f14 = f16 - f7;
        float f17 = f14 - f8;
        f14 = f17 - f7;
        float f18 = f14 - f13;
        float f19 = f12 + (f7 + 4.0f);
        float f20 = f19 * (1.0f - f2);
        iII.drawText(typedValue020, string, f15 + f20, f5, colorRGBA.withAlpha(255.0f * f11));
        this.internalField1723 = f3 = f16 + f20;
        this.internalField1731 = f4 - f9 / 2.0f;
        this.internalField1727 = f9;
        iII.drawIcon("language", f3, this.internalField1731, f9, colorRGBA.withAlpha(255.0f * f10));
         iII.drawText(typedValue020, string2, f18 + f20, f5, colorRGBA.withAlpha(255.0f * f11));
        this.internalField1460 = Math.max(4.0f, this.internalField1723 + f9 - 106.0f);
        this.internalField1461 = 26.0f;
    }

    private void internalMethod06863(int n) {
        if (this.internalField0519.internalMethod02365(250L)) {
            double d = internalField0149.getWindow().getScaleFactor();
            float f = 11.0f;
            this.internalField1099 = this.internalMethod00665(40.0f, f, d);
            this.internalField1100 = this.internalMethod00665((float)n - 70.0f, f, d);
            this.internalField0519.internalMethod00701();
        }
        this.internalField0814.internalMethod03893(this.internalField1099 ? internalField0777 : internalField0776);
        this.internalField0813.internalMethod03893(this.internalField1100 ? internalField0777 : internalField0776);
    }

    private boolean internalMethod00665(float f, float f2, double d) {
        ColorRGBA colorRGBA = ColorRGBA.fromPixel(f * (float)d, (float)internalField0149.getWindow().getHeight() - f2 * (float)d);
        return (colorRGBA.getRed() + colorRGBA.getGreen() + colorRGBA.getBlue()) / 3.0f > 120.0f;
    }

    private boolean internalMethod00071(double d, double d2) {
        return this.internalField1727 > 0.0f && d >= (double)(this.internalField1723 - 2.0f) && d <= (double)(this.internalField1723 + this.internalField1727 + 2.0f) && d2 >= (double)(this.internalField1731 - 2.0f) && d2 <= (double)(this.internalField1731 + this.internalField1727 + 2.0f);
    }

    private void internalMethod02078(UiRenderContext iII) {
        this.internalField0808.internalMethod07062(this.internalField0276);
        float f = this.internalField0808.internalMethod02881();
        if (f <= 0.003f) {
            return;
        }
        Language[] iiIIiiii_Class80Array = Language.values();
        float f2 = this.internalField1460;
        float f3 = this.internalField1461;
        float f4 = (float)iiIIiiii_Class80Array.length * 18.0f + 10.0f;
        float f5 = ThemeColors.internalMethod02435().internalMethod01359();
        float f6 = Math.max(2.0f, f5 - 3.0f);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)f);
        iII.drawShadow(f2, f3, 106.0f, f4, 12.0f, CornerRadii.internalMethod03908(f5), ColorRGBA.BLACK.withAlpha(120.0f));
        iII.drawClientRect(f2, f3, 106.0f, f4, f, 0.0f, 7.0f);
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        int n = iII.internalMethod05259();
        int n2 = iII.internalMethod05261();
        for (int i = 0; i < iiIIiiii_Class80Array.length; ++i) {
            Language typedValue141 = iiIIiiii_Class80Array[i];
            float f7 = f3 + 5.0f + (float)i * 18.0f;
            boolean bl = (float)n >= f2 + 5.0f && (float)n <= f2 + 106.0f - 5.0f && (float)n2 >= f7 && (float)n2 <= f7 + 18.0f;
            boolean bl2 = LanguageManager.internalMethod00625() == typedValue141;
            ColorRGBA colorRGBA = ThemeColors.internalMethod08459();
            if (bl) {
                CursorManager.internalMethod06882(CursorType.internalField0567);
                iII.drawSquircle(f2 + 5.0f, f7, 96.0f, 18.0f, 7.0f, CornerRadii.internalMethod03908(f6), colorRGBA.withAlpha(13.0f));
            }
            iII.drawText(typedValue020, TopBarWidget.internalMethod05121(typedValue141), f2 + 5.0f + 7.0f, f7 + 9.0f - typedValue020.internalMethod04890() / 2.0f, colorRGBA.withAlpha(bl2 ? 255.0f : 150.0f));
            if (!bl2) continue;
            float f8 = 4.0f;
            iII.drawRoundedRect(f2 + 106.0f - 5.0f - 7.0f - f8, f7 + 9.0f - f8 / 2.0f, f8, f8, CornerRadii.internalMethod03908(f8 / 2.0f), ColorRGBA.WHITE);
        }
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public boolean internalMethod00660(double d, double d2, int n) {
        if (this.internalField1455 > 0.5f && this.internalMethod00071(d, d2)) {
            if (n == 0) {
                this.internalField0276 = !this.internalField0276;
            }
            return true;
        }
        if (this.internalField0276) {
            Language[] iiIIiiii_Class80Array = Language.values();
            float f = this.internalField1460;
            float f2 = this.internalField1461;
            float f3 = (float)iiIIiiii_Class80Array.length * 18.0f + 10.0f;
            if (d >= (double)f && d <= (double)(f + 106.0f) && d2 >= (double)f2 && d2 <= (double)(f2 + f3)) {
                int n2 = (int)((d2 - (double)f2 - 5.0) / 18.0);
                if (n == 0 && n2 >= 0 && n2 < iiIIiiii_Class80Array.length) {
                    LanguageManager.internalMethod04491(iiIIiiii_Class80Array[n2]);
                }
            }
            this.internalField0276 = false;
            return true;
        }
        if (this.internalField1462 <= 0.5f) {
            return false;
        }
        for (InternalType0055 nestedValue2010 : this.internalField0416) {
            if (!(d >= (double)nestedValue2010.internalField0205) || !(d <= (double)(nestedValue2010.internalField0205 + nestedValue2010.internalField1048)) || !(d2 >= (double)nestedValue2010.internalField0206) || !(d2 <= (double)(nestedValue2010.internalField0206 + nestedValue2010.internalField1047))) continue;
            if (n == 0) {
                nestedValue2010.internalField0659.run();
            }
            return true;
        }
        return false;
    }

    public boolean internalMethod00668(int n, int n2, int n3) {
        if (this.internalField0276 && n == 256) {
            this.internalField0276 = false;
            return true;
        }
        return false;
    }

    private static String internalMethod05121(Language typedValue141) {
        return switch (typedValue141) {
            default -> throw new MatchException(null, null);
            case Language.internalField0165 -> "English";
            case Language.internalField0164 -> "\u0420\u0443\u0441\u0441\u043a\u0438\u0439";
            case Language.internalField1031 -> "\u0423\u043a\u0440\u0430\u0457\u043d\u0441\u044c\u043a\u0430";
            case Language.internalField1032 -> "Polski";
        };
    }

    static final class InternalType0055 {
        final String internalField0248;
        final Runnable internalField0659;
        final BooleanSupplier internalField0424;
        final AnimatedValue internalField0808 = new AnimatedValue(220L, 0.0f, Easing.internalField1626);
        float internalField0205;
        float internalField0206;
        float internalField1048;
        float internalField1047;
        float internalField1049;
        float internalField1046;

        InternalType0055(String string, Runnable runnable, BooleanSupplier booleanSupplier) {
            this.internalField0248 = string;
            this.internalField0659 = runnable;
            this.internalField0424 = booleanSupplier;
        }
    }
}

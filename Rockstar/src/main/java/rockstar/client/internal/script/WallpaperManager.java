package rockstar.client.internal.script;







import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.asset.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.Iterator;
import lombok.Generated;
import net.minecraft.util.math.MathHelper;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.MathUtils;
import rockstar.client.asset.AssetTextureManager;

public class WallpaperManager
implements MinecraftClientAccess {
    private static final String[] internalField0359 = new String[]{"image/mainmenu/day1", "image/mainmenu/day2", "image/mainmenu/day3", "image/mainmenu/day4", "image/mainmenu/day5", "image/mainmenu/day6", "image/mainmenu/night1", "image/mainmenu/night2", "image/mainmenu/night3"};
    private static final String[] internalField0358 = new String[]{"mainmenu.wallpaper.day1", "mainmenu.wallpaper.day2", "mainmenu.wallpaper.day3", "mainmenu.wallpaper.day4", "mainmenu.wallpaper.day5", "mainmenu.wallpaper.day6", "mainmenu.wallpaper.night1", "mainmenu.wallpaper.night2", "mainmenu.wallpaper.night3"};
    private boolean internalField0277;
    private float internalField0205;
    private int internalField0227;
    private float internalField0206;
    private int internalField0228;
    private float internalField1048 = 1.7777778f;
    private boolean internalField0276;
    private boolean internalField1099;
    private double internalField0194;
    private double internalField0193;
    private double internalField1045;
    private float internalField1047;
    private int internalField1053 = 1;
    private int internalField1055 = 1;
    private long internalField0229 = System.currentTimeMillis();
    private int internalField1056;

    public static int internalMethod00991() {
        return 6;
    }

    public static int internalMethod00997() {
        return internalField0359.length;
    }

    public WallpaperManager(int n) {
        this.internalField0227 = MathHelper.clamp((int)n, (int)0, (int)(internalField0359.length - 1));
        this.internalField0206 = this.internalField0227;
        this.internalField0228 = this.internalField0227;
    }

    public boolean internalMethod00993() {
        return this.internalField0277 || this.internalField0205 > 0.001f;
    }

    public void internalMethod00992() {
        this.internalField0277 = true;
        this.internalField0206 = this.internalField0227;
        this.internalField0228 = this.internalField0227;
        this.internalField0276 = false;
        this.internalField1099 = false;
    }

    public void internalMethod00998() {
        this.internalField0277 = false;
        this.internalField0276 = false;
        this.internalField1099 = false;
        this.internalField0228 = this.internalField0227;
    }

    public void internalMethod01889(UiRenderContext iII, int n3, int n4, float f, float f2, float f3, float f4) {
        float f5;
        this.internalField1053 = Math.max(1, n3);
        this.internalField1055 = Math.max(1, n4);
        if (this.internalField1056 < internalField0359.length) {
            AssetTextureManager.internalMethod02604(internalField0359[this.internalField1056]);
            ++this.internalField1056;
        }
        if ((f5 = AssetTextureManager.internalMethod02969(internalField0359[this.internalField0227])) > 0.0f) {
            this.internalField1048 = f5;
        }
        long l = System.currentTimeMillis();
        float f6 = Math.min(0.1f, (float)(l - this.internalField0229) / 1000.0f);
        this.internalField0229 = l;
        float f7 = 1.0f - (float)Math.pow(9.0E-4f, f6);
        float f8 = this.internalField0277 ? 1.0f : 0.0f;
        this.internalField0205 += (f8 - this.internalField0205) * f7;
        if (Math.abs(f8 - this.internalField0205) < 8.0E-4f) {
            this.internalField0205 = f8;
        }
        float f9 = this.internalField0205;
        if (!this.internalField1099) {
            this.internalField0206 += ((float)this.internalField0228 - this.internalField0206) * f7;
            if (Math.abs((float)this.internalField0228 - this.internalField0206) < 8.0E-4f) {
                this.internalField0206 = this.internalField0228;
            }
        }
        float f10 = this.internalMethod07570(n3, n4) * MathUtils.internalMethod02587(1.05f - 0.05f * f3, 1.0, f9) * (1.0f - 0.045f * f4);
        float f11 = this.internalMethod01217(n3, n4);
        float f12 = MathUtils.internalMethod02587(f10, f11, f9);
        float f13 = f12 / this.internalField1048;
        float f14 = MathUtils.internalMethod02587(0.0, f11 * 1.16f, f9);
        float f15 = (float)n3 / 2.0f - f * (1.0f - f9);
        float f16 = (float)n4 / 2.0f - f2 * (1.0f - f9) - 4.0f * f9;
        float f17 = 18.0f * f9;
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i = 0; i < internalField0359.length; ++i) {
            arrayList.add(i);
        }
        arrayList.sort((n, n2) -> Float.compare(Math.abs((float)n2.intValue() - this.internalField0206), Math.abs((float)n.intValue() - this.internalField0206)));
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            int n5 = (Integer)iterator.next();
            float f18 = (float)n5 - this.internalField0206;
            float f19 = Math.abs(f18);
            if (f19 > 1.85f) continue;
            float f20 = Math.min(1.0f, f19);
            float f21 = 1.0f - 0.14f * f20;
            float f22 = f12 * f21;
            float f23 = f13 * f21;
            float f24 = f15 + f18 * f14 - f22 / 2.0f;
            float f25 = f16 - f23 / 2.0f;
            float f26 = Math.max(f9, Math.max(0.0f, 1.0f - f19 * 6.0f));
            float f27 = (1.0f - 0.4f * f20) * f26;
            if (f27 <= 0.003f) continue;
            CornerRadii typedParameter1014 = CornerRadii.internalMethod03908(f17);
            if (f9 > 0.01f) {
                iII.drawShadow(f24, f25 + 6.0f * f9, f22, f23, 16.0f * f9, typedParameter1014, ColorRGBA.BLACK.withAlpha(150.0f * f9 * f27));
            }
            iII.drawRoundedTexture(AssetTextureManager.internalMethod02604(internalField0359[n5]), f24, f25, f22, f23, typedParameter1014, ColorRGBA.WHITE.withAlpha(255.0f * f27));
            if (!(f20 > 0.003f)) continue;
            iII.drawRoundedRect(f24, f25, f22, f23, typedParameter1014, ColorRGBA.BLACK.withAlpha(95.0f * f20 * f9));
        }
    }

    public void internalMethod00055(UiRenderContext iII, int n, int n2) {
        float f;
        float f2;
        float f3;
        float f4 = this.internalField0205;
        if (f4 <= 0.003f) {
            return;
        }
        SizedFont typedValue020 = Fonts.internalField1157.internalMethod01432(21.0f);
        float f5 = this.internalMethod05453(n, n2, typedValue020) - 14.0f * (1.0f - f4);
        float f6 = 140.0f;
        for (int i = 0; i < internalField0359.length; ++i) {
            f3 = (float)i - this.internalField0206;
            if (Math.abs(f3) >= 1.0f) continue;
            f2 = 1.0f - Math.abs(f3);
            f2 *= f2;
            f = (float)n / 2.0f + f3 * f6;
            String string = LanguageManager.internalMethod07214(internalField0358[i]);
            iII.drawCenteredTextWithShadow(typedValue020, string, f, f5, ColorRGBA.WHITE.withAlpha(255.0f * f4 * f2), ColorRGBA.BLACK.withAlpha(130.0f * f4 * f2), 1.0f, 2.0f, 4.0f);
        }
        float f7 = (float)n2 - 26.0f;
        f3 = 10.0f;
        f2 = 6.0f;
        f = (float)n / 2.0f - (float)(internalField0359.length - 1) * f3 / 2.0f;
        for (int i = 0; i < internalField0359.length; ++i) {
            float f8 = 1.0f - Math.min(1.0f, Math.abs((float)i - this.internalField0206));
            float f9 = (0.3f + 0.7f * f8) * f4;
            iII.drawRoundedRect(f + (float)i * f3 - f2 / 2.0f, f7 - f2 / 2.0f, f2, f2, CornerRadii.internalMethod03908(f2 / 2.0f), ColorRGBA.WHITE.withAlpha(255.0f * f9));
        }
    }

    public boolean internalMethod05038(double d, double d2, int n) {
        if (!this.internalField0277) {
            return false;
        }
        if (n == 1) {
            this.internalMethod00998();
            return true;
        }
        if (n != 0) {
            return true;
        }
        this.internalField0276 = true;
        this.internalField1099 = false;
        this.internalField0194 = d;
        this.internalField0193 = d2;
        this.internalField1045 = d;
        this.internalField1047 = this.internalField0206;
        return true;
    }

    public boolean internalMethod01847(double d, double d2, int n) {
        if (!this.internalField0277 || !this.internalField0276 || n != 0) {
            return this.internalField0277;
        }
        if (Math.abs(d - this.internalField0194) > 4.0 || Math.abs(d2 - this.internalField0193) > 4.0) {
            this.internalField1099 = true;
        }
        if (this.internalField1099) {
            float f = Math.max(1.0f, this.internalMethod01217(this.internalField1053, this.internalField1055) * 1.16f);
            this.internalField0206 = MathHelper.clamp((float)(this.internalField1047 - (float)(d - this.internalField1045) / f), (float)0.0f, (float)((float)internalField0359.length - 1.0f));
            this.internalField0228 = MathHelper.clamp((int)Math.round(this.internalField0206), (int)0, (int)(internalField0359.length - 1));
        }
        return true;
    }

    public boolean internalMethod08174(double d, double d2, int n) {
        if (!this.internalField0277 || n != 0 || !this.internalField0276) {
            this.internalField0276 = false;
            return this.internalField0277;
        }
        this.internalField0276 = false;
        if (this.internalField1099) {
            this.internalField1099 = false;
            this.internalField0228 = MathHelper.clamp((int)Math.round(this.internalField0206), (int)0, (int)(internalField0359.length - 1));
            return true;
        }
        int n2 = MathHelper.clamp((int)Math.round(this.internalField0206), (int)0, (int)(internalField0359.length - 1));
        float f = Math.max(1.0f, this.internalMethod01217(this.internalField1053, this.internalField1055) * 1.16f);
        int n3 = MathHelper.clamp((int)Math.round(this.internalField0206 + (float)(d - (double)this.internalField1053 / 2.0) / f), (int)0, (int)(internalField0359.length - 1));
        if (n3 == n2 && this.internalMethod07569(d, d2)) {
            this.internalField0227 = n2;
            this.internalMethod00998();
        } else if (n3 != n2) {
            this.internalField0228 = n3;
        }
        return true;
    }

    public boolean internalMethod03743(double d) {
        if (!this.internalField0277) {
            return false;
        }
        this.internalField0228 = MathHelper.clamp((int)(this.internalField0228 + (d > 0.0 ? -1 : 1)), (int)0, (int)(internalField0359.length - 1));
        return true;
    }

    public boolean internalMethod03744(int n) {
        if (!this.internalField0277) {
            return false;
        }
        if (n == 0) {
            return true;
        }
        int n2 = this.internalField1099 ? Math.round(this.internalField0206) : this.internalField0228;
        this.internalField0228 = MathHelper.clamp((int)(n2 + Integer.signum(n)), (int)0, (int)(internalField0359.length - 1));
        this.internalField0276 = false;
        this.internalField1099 = false;
        return true;
    }

    private boolean internalMethod07569(double d, double d2) {
        float f = this.internalMethod01217(this.internalField1053, this.internalField1055);
        float f2 = f / this.internalField1048;
        float f3 = (float)this.internalField1053 / 2.0f;
        float f4 = (float)this.internalField1055 / 2.0f - 4.0f;
        return d >= (double)(f3 - f / 2.0f) && d <= (double)(f3 + f / 2.0f) && d2 >= (double)(f4 - f2 / 2.0f) && d2 <= (double)(f4 + f2 / 2.0f);
    }

    private float internalMethod05453(int n, int n2, SizedFont typedValue020) {
        float f = (float)n2 / 2.0f - 4.0f - this.internalMethod01217(n, n2) / this.internalField1048 / 2.0f;
        float f2 = Math.min(42.0f, (float)n2 * 0.13f);
        float f3 = f - typedValue020.internalMethod07850() - MathHelper.clamp((float)((float)n2 * 0.045f), (float)10.0f, (float)28.0f);
        return Math.max(6.0f, Math.min(f2, f3));
    }

    private float internalMethod07570(int n, int n2) {
        float f;
        float f2;
        float f3 = (float)n / Math.max(1.0f, (float)n2);
        if (f3 > this.internalField1048) {
            f2 = n;
            f = (float)n / this.internalField1048;
        } else {
            f = n2;
            f2 = (float)n2 * this.internalField1048;
        }
        float f4 = 28.0f;
        float f5 = Math.max(((float)n + f4 * 2.0f) / f2, ((float)n2 + f4 * 2.0f) / f);
        f5 = Math.max(f5, 1.06f);
        return f2 * f5;
    }

    private float internalMethod01217(int n, int n2) {
        float f = (float)n * 0.52f;
        float f2 = (float)n2 * 0.62f;
        if (f / this.internalField1048 > f2) {
            f = f2 * this.internalField1048;
        }
        return f;
    }

    @Generated
    public boolean internalMethod00999() {
        return this.internalField0277;
    }

    @Generated
    public float internalMethod00990() {
        return this.internalField0205;
    }

    @Generated
    public int internalMethod08411() {
        return this.internalField0227;
    }
}


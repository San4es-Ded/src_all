package rockstar.client.ui;







import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.render.*;
import rockstar.client.compat.RenderSystem;
import java.util.Locale;
import net.minecraft.client.render.VertexFormats;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.setting.VectorRangeSetting;
import rockstar.client.ui.MouseButton;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.SettingComponent;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.LegacyUiElement;
import rockstar.client.internal.render.UiVertexBatch;
import rockstar.client.internal.render.ColoredQuadBatch;

public class VectorRangeSettingComponent
extends SettingComponent<VectorRangeSetting> {
    private final AnimatedValue internalField0809 = new AnimatedValue(400L, Easing.internalField1325);
    private final AnimatedValue internalField1321 = new AnimatedValue(400L, Easing.internalField1325);
    private boolean internalField0277;

    public VectorRangeSettingComponent(VectorRangeSetting typedValue171, LegacyUiElement typedValue001) {
        super(typedValue171, typedValue001);
    }

    @Override
    protected void internalMethod05619(UiRenderContext iII) {
        Object object;
        float f;
        float f2;
        float f3;
        float f4 = this.internalField0205 + 9.0f;
        float f5 = this.internalField0206 + 2.0f;
        float f6 = this.internalField1048 - 18.0f;
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        SizedFont typedValue021 = Fonts.internalField1154.internalMethod01432(7.0f);
        float f7 = 10.0f;
        this.internalField0808.internalMethod07062(this.internalMethod04933(iII.internalMethod05259(), iII.internalMethod05261()));
        float f8 = f4;
        float f9 = f5 + 17.0f;
        float f10 = f6;
        iII.drawRoundedRect(f8 - 1.0f, f9 - 1.0f, f10 + 2.0f, f10 + 2.0f, CornerRadii.internalMethod03908(3.0f), ThemeColors.internalMethod07738().withAlpha(89.25f));
        iII.drawRoundedRect(f8, f9, f10, f10, CornerRadii.internalMethod03908(2.0f), ThemeColors.internalMethod07738().withAlpha(140.25f));
        ColoredQuadBatch typedValue254 = new ColoredQuadBatch(VertexFormats.POSITION_COLOR, iII.getMatrices());
        for (int i = 1; i < 10; ++i) {
            f3 = (float)i / 10.0f;
            f2 = f8 + f10 * f3;
            f = f9 + f10 * f3;
            object = ThemeColors.internalMethod08459().mulAlpha(0.08f);
            iII.drawRect(f2, f9, 1.0f, f10, (ColorRGBA)object);
            iII.drawRect(f8, f, f10, 1.0f, (ColorRGBA)object);
        }
        this.internalField0809.internalMethod07059(((VectorRangeSetting)this.internalField0644).internalMethod03695());
        this.internalField1321.internalMethod07059(((VectorRangeSetting)this.internalField0644).internalMethod03697());
        float f11 = UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((VectorRangeSetting)this.internalField0644).internalMethod08931(), ((VectorRangeSetting)this.internalField0644).internalMethod08932());
        f3 = UiUtils.internalMethod07541(this.internalField1321.internalMethod02881(), ((VectorRangeSetting)this.internalField0644).internalMethod08949(), ((VectorRangeSetting)this.internalField0644).internalMethod08951());
        f2 = f8 + f10 * f11;
        f = f9 + f10 * f3;
        iII.drawRect(f2 - 0.5f, f9, 1.0f, f10, ThemeColors.internalMethod02531());
        iII.drawRect(f8, f - 0.5f, f10, 1.0f, ThemeColors.internalMethod02531());
        ((UiVertexBatch)typedValue254).internalMethod09053();
        object = String.format(Locale.ROOT, "%.1f : %.1f", Float.valueOf(this.internalField0809.internalMethod02881()), Float.valueOf(this.internalField1321.internalMethod02881()));
        float f12 = typedValue021.internalMethod00965((String)object);
        float f13 = typedValue021.internalMethod04890();
        float f14 = f12 + 6.0f;
        float f15 = f13 + 4.0f;
        float f16 = f4 + f6 - f14;
        float f17 = f5 + 11.0f - typedValue021.internalMethod04890() - 2.0f;
        iII.drawRoundedRect(f16, f17, f14, f15, CornerRadii.internalMethod03908(3.0f), ThemeColors.internalMethod07738().withAlpha(153.0f));
        this.internalMethod00985(iII, typedValue020, LanguageManager.internalMethod07214(((VectorRangeSetting)this.internalField0644).getName()), this.internalField0205 + f7, f5 + 11.0f - typedValue020.internalMethod04890(), this.internalMethod05803().internalMethod08827() - f7 - f14 - 10.0f, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881())), 0.8f, 1.0f);
        boolean bl = UiUtils.internalMethod06450(f8, f9, f10, f10, iII);
        if (bl || this.internalField0277) {
            CursorManager.internalMethod06882(CursorType.internalField1207);
        } else if (this.internalMethod03399(iII) && (float)iII.internalMethod05261() > this.internalMethod09904() && (float)iII.internalMethod05261() < this.internalMethod09904() + this.internalMethod09905()) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        if (this.internalField0277) {
            float f18 = UiUtils.internalMethod04852(((VectorRangeSetting)this.internalField0644).internalMethod08931(), ((VectorRangeSetting)this.internalField0644).internalMethod08932(), f8, f10, iII.internalMethod05259());
            float f19 = UiUtils.internalMethod04852(((VectorRangeSetting)this.internalField0644).internalMethod08949(), ((VectorRangeSetting)this.internalField0644).internalMethod08951(), f9, f10, iII.internalMethod05261());
            ((VectorRangeSetting)this.internalField0644).internalMethod03891(f18, f19);
        }
    }

    @Override
    public void internalMethod08256(UiRenderContext iII) {
        float f = this.internalField0205 + 9.0f;
        float f2 = this.internalField0206 + 2.0f;
        float f3 = this.internalField1048 - 18.0f;
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(7.0f);
        String string = String.format(Locale.ROOT, "%.1f : %.1f", Float.valueOf(this.internalField0809.internalMethod02881()), Float.valueOf(this.internalField1321.internalMethod02881()));
        float f4 = typedValue020.internalMethod00965(string);
        float f5 = typedValue020.internalMethod04890();
        float f6 = f4 + 6.0f;
        float f7 = f5 + 4.0f;
        float f8 = f + f3 - f6;
        float f9 = f2 + 11.0f - typedValue020.internalMethod04890() - 2.0f;
        iII.drawText(typedValue020, string, f8 + 3.0f, f9 + UiUtils.internalMethod07116(f5, f7), ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881()) * RenderSystem.getShaderColor()[3]));
    }

    @Override
    public void internalMethod01643(double d, double d2, MouseButton typedParameter1015) {
        float f = this.internalField0205 + 9.0f;
        float f2 = this.internalField0206 + 2.0f;
        float f3 = this.internalField1048 - 18.0f;
        float f4 = f;
        float f5 = f2 + 17.0f;
        float f6 = f3;
        if (typedParameter1015 == MouseButton.internalField0102 && UiUtils.internalMethod05785(f4, f5, f6, f6, d, d2)) {
            this.internalField0277 = true;
            float f7 = UiUtils.internalMethod04852(((VectorRangeSetting)this.internalField0644).internalMethod08931(), ((VectorRangeSetting)this.internalField0644).internalMethod08932(), f4, f6, d);
            float f8 = UiUtils.internalMethod04852(((VectorRangeSetting)this.internalField0644).internalMethod08949(), ((VectorRangeSetting)this.internalField0644).internalMethod08951(), f5, f6, d2);
            ((VectorRangeSetting)this.internalField0644).internalMethod03891(f7, f8);
        }
        super.internalMethod01643(d, d2, typedParameter1015);
    }

    @Override
    public void internalMethod02863(double d, double d2, MouseButton typedParameter1015) {
        this.internalField0277 = false;
        super.internalMethod02863(d, d2, typedParameter1015);
    }

    @Override
    public void internalMethod07807(UiRenderContext iII) {
        iII.drawRect(this.internalField0205, this.internalField0206 + this.internalField1047, this.internalField1048, 0.5f, ThemeColors.internalMethod08459().withAlpha(5.1f));
    }

    @Override
    public float internalMethod07809() {
        float f = this.internalField1048 - 18.0f;
        this.internalField1047 = Math.max(48.0f, f + 24.0f);
        return this.internalField1047;
    }
}

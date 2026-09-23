package rockstar.client.ui;






import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.render.*;
import java.util.List;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.setting.RegistryListSetting;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.CustomTextField;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.SettingComponent;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.LegacyUiElement;
import rockstar.client.internal.render.RoundedRectBatch;

public class RegistryListSettingComponent
extends SettingComponent<RegistryListSetting> {
    private final CustomTextField internalField0936 = new CustomTextField(Fonts.internalField1154.internalMethod01432(7.0f));
    private SizedFont internalField0447;
    private SizedFont internalField0448;

    public RegistryListSettingComponent(RegistryListSetting typedValue163, LegacyUiElement typedValue001) {
        super(typedValue163, typedValue001);
        this.internalField0936.internalMethod00484(typedValue163.internalMethod08875());
    }

    @Override
    public final void internalMethod02325() {
        this.internalField0936.internalMethod09000(LanguageManager.internalMethod07214("search"));
        this.internalField0447 = Fonts.internalField0449.internalMethod01432(8.0f);
        this.internalField0448 = Fonts.internalField0449.internalMethod01432(7.0f);
        super.internalMethod02325();
    }

    @Override
    protected final void internalMethod05619(UiRenderContext iII) {
        boolean bl;
        float f;
        float f2;
        float f3;
        float f4;
        int n;
        float f5 = this.internalField0205 + 9.0f;
        float f6 = this.internalField0206 + 1.0f;
        float f7 = this.internalField1048 - 18.0f;
        this.internalField0808.internalMethod07062(this.internalMethod04933(iII.internalMethod05259(), iII.internalMethod05261()));
        float f8 = 10.0f;
        String string = "%d/%d".formatted(((RegistryListSetting)this.internalField0644).internalMethod02610(), ((RegistryListSetting)this.internalField0644).internalMethod08970().size());
        this.internalMethod00985(iII, this.internalField0447, LanguageManager.internalMethod07214(((RegistryListSetting)this.internalField0644).getName()), this.internalField0205 + f8, f6 - 1.0f + UiUtils.internalMethod07116(this.internalField0447.internalMethod04890(), 19.0f), this.internalMethod05803().internalMethod08827() - f8 - 10.0f - this.internalField0448.internalMethod00965(string), ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881())), 0.8f, 1.0f);
        iII.drawRightText(this.internalField0448, string, f5 + f7, f6 - 1.0f + UiUtils.internalMethod07116(Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890(), 19.0f), ThemeColors.internalMethod08459().withAlpha(255.0f * (0.6f + 0.4f * this.internalField0808.internalMethod02881())));
        this.internalField0936.internalMethod09000(LanguageManager.internalMethod07214("search"));
        ((RegistryListSetting)this.internalField0644).internalMethod03086(this.internalField0936.internalMethod06202());
        List<RegistryListSetting.InternalType0387> list = ((RegistryListSetting)this.internalField0644).internalMethod01166();
        int n2 = this.internalMethod03566(f7);
        int n3 = list.size();
        int n4 = Math.max(1, (int)Math.ceil((float)n3 / (float)n2));
        float f9 = f5 - 1.0f;
        float f10 = f6 + 17.0f;
        float f11 = f5 + 6.0f;
        float f12 = f10 + 5.0f;
        float f13 = Math.max(0.0f, f7 - 12.0f);
        float f14 = f5 + 6.0f;
        float f15 = f12 + 14.0f + 6.0f;
        float f16 = (float)n4 * 20.0f;
        float f17 = 25.0f + f16 + 6.0f;
        iII.drawRoundedRect(f9, f10, f7 + 2.0f, f17, CornerRadii.internalMethod03908(6.0f), ThemeColors.internalMethod07738().withAlpha(76.5f));
        this.internalField0936.internalMethod05191(f11, f12, f13, 14.0f);
        this.internalField0936.internalMethod00143(ThemeColors.internalMethod08459());
        this.internalField0936.internalMethod08627(1.0f);
        this.internalField0936.internalMethod03398(iII);
        float f18 = this.internalField0206;
        float f19 = this.internalField0206 + this.internalField1047;
        RoundedRectBatch typedValue255 = new RoundedRectBatch();
        for (int i = 0; i < n3; ++i) {
            RegistryListSetting.InternalType0387 nestedValue2054 = list.get(i);
            int n5 = i % n2;
            n = i / n2;
            float f20 = f14 + (float)n5 * 20.0f;
            f4 = f15 + (float)n * 20.0f;
            f3 = f20 + 2.0f;
            f2 = f4 + 2.0f;
            f = 0.8000002f;
            if (f2 + 16.0f < this.internalField1049 || f2 > this.internalField1049 + this.internalField1046 * 2.0f) continue;
            boolean bl2 = UiUtils.internalMethod06450(f20, f4, 20.0, 20.0, iII);
            bl = ((RegistryListSetting)this.internalField0644).internalMethod01657(nestedValue2054);
            ColorRGBA colorRGBA = ThemeColors.internalMethod07738().mulAlpha(0.22f + (bl2 ? 0.08f : 0.0f));
            ColorRGBA colorRGBA2 = ThemeColors.internalMethod02531().mulAlpha(0.7f);
            nestedValue2054.internalMethod05804().internalMethod03893(bl ? colorRGBA2 : colorRGBA);
            if (bl2 && (float)iII.internalMethod05261() > this.internalMethod09904() && (float)iII.internalMethod05261() < this.internalMethod09904() + this.internalMethod09905()) {
                CursorManager.internalMethod06882(CursorType.internalField0567);
            }
            iII.drawRoundedRect(f3, f2, 16.0f, 16.0f, CornerRadii.internalMethod03908(4.0f), nestedValue2054.internalMethod05804().internalMethod04159());
        }
        typedValue255.internalMethod09053();
        try (CustomDrawContext.InternalType0486 nestedValue2061 = iII.beginItemBatch();){
            for (int i = 0; i < n3; ++i) {
                RegistryListSetting.InternalType0387 nestedValue2054 = list.get(i);
                n = i % n2;
                int n6 = i / n2;
                f4 = f14 + (float)n * 20.0f;
                f3 = f15 + (float)n6 * 20.0f;
                f2 = f4 + 2.0f;
                f = f3 + 2.0f;
                float f21 = 0.8000002f;
                if (f + 16.0f < this.internalField1049 || f > this.internalField1049 + this.internalField1046 * 2.0f) continue;
                bl = UiUtils.internalMethod06450(f4, f3, 20.0, 20.0, iII);
                if (bl) {
                    CursorManager.internalMethod06882(CursorType.internalField0567);
                }
                iII.drawBatchItem(nestedValue2054.internalMethod02202(), f2 + f21, f + f21, 0.9f);
            }
        }
        for (int i = 0; i < n3; ++i) {
            RegistryListSetting.InternalType0387 nestedValue2054 = list.get(i);
            int n7 = i % n2;
            n = i / n2;
            float f22 = f14 + (float)n7 * 20.0f;
            f4 = f15 + (float)n * 20.0f;
            f3 = f22 + 2.0f;
            f2 = f4 + 2.0f;
            if (f2 + 16.0f < this.internalField1049 || f2 > this.internalField1049 + this.internalField1046 * 2.0f) continue;
            boolean bl3 = UiUtils.internalMethod06450(f22, f4, 20.0, 20.0, iII);
            boolean bl4 = ((RegistryListSetting)this.internalField0644).internalMethod01657(nestedValue2054);
            nestedValue2054.internalMethod05801().internalMethod07059(bl3 ? 0.6f : 0.0f);
            if (bl3) {
                CursorManager.internalMethod06882(CursorType.internalField0567);
            }
            iII.drawRoundedBorder(f3, f2, 16.0f, 16.0f, 1.2f, CornerRadii.internalMethod03908(4.0f), ThemeColors.internalMethod02531().mulAlpha(nestedValue2054.internalMethod05801().internalMethod02881()));
        }
    }

    @Override
    public final void internalMethod01643(double d, double d2, MouseButton typedParameter1015) {
        this.internalField0936.internalMethod01643(d, d2, typedParameter1015);
        if (typedParameter1015 != MouseButton.internalField0102) {
            super.internalMethod01643(d, d2, typedParameter1015);
            return;
        }
        float f = this.internalField0205 + 9.0f;
        float f2 = f + 6.0f;
        float f3 = this.internalField0206 + 17.0f;
        float f4 = f3 + 5.0f;
        float f5 = this.internalField1048 - 18.0f;
        float f6 = Math.max(0.0f, f5 - 12.0f);
        if (UiUtils.internalMethod05785(f2, f4, f6, 14.0, d, d2)) {
            super.internalMethod01643(d, d2, typedParameter1015);
            return;
        }
        int n = this.internalMethod03566(f5);
        float f7 = f + 6.0f;
        float f8 = f4 + 14.0f + 6.0f;
        List<RegistryListSetting.InternalType0387> list = ((RegistryListSetting)this.internalField0644).internalMethod01166();
        for (int i = 0; i < list.size(); ++i) {
            RegistryListSetting.InternalType0387 nestedValue2054 = list.get(i);
            int n2 = i % n;
            float f9 = f7 + (float)n2 * 20.0f;
            int n3 = i / n;
            float f10 = f8 + (float)n3 * 20.0f;
            if (!UiUtils.internalMethod05785(f9, f10, 20.0, 20.0, d, d2)) continue;
            ((RegistryListSetting)this.internalField0644).internalMethod01656(nestedValue2054);
            break;
        }
        super.internalMethod01643(d, d2, typedParameter1015);
    }

    @Override
    public final void internalMethod02863(double d, double d2, MouseButton typedParameter1015) {
        this.internalField0936.internalMethod02863(d, d2, typedParameter1015);
        super.internalMethod02863(d, d2, typedParameter1015);
    }

    @Override
    public final void internalMethod05727(int n, int n2, int n3) {
        this.internalField0936.internalMethod05727(n, n2, n3);
        super.internalMethod05727(n, n2, n3);
    }

    @Override
    public final boolean internalMethod05413(char c, int n) {
        if (this.internalField0936.internalMethod05413(c, n)) {
            return true;
        }
        return super.internalMethod05413(c, n);
    }

    @Override
    public final void internalMethod07807(UiRenderContext iII) {
        float f = 0.5f;
        iII.drawRect(this.internalField0205, this.internalField0206 + this.internalField1047, this.internalField1048, f, ThemeColors.internalMethod08459().withAlpha(5.1f));
    }

    @Override
    public final float internalMethod07809() {
        float f = this.internalField1048 - 18.0f;
        int n = this.internalMethod03566(f);
        int n2 = ((RegistryListSetting)this.internalField0644).internalMethod01166().size();
        int n3 = Math.max(1, (int)Math.ceil((float)n2 / (float)n));
        float f2 = (float)n3 * 20.0f;
        float f3 = 25.0f + f2 + 6.0f;
        this.internalField1047 = 23.0f + f3;
        return this.internalField1047;
    }

    private int internalMethod03566(float f) {
        float f2 = Math.max(f - 12.0f, 20.0f);
        int n = (int)Math.floor(f2 / 20.0f);
        return Math.max(1, n);
    }
}


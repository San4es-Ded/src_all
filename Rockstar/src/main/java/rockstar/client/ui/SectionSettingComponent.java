package rockstar.client.ui;






import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.setting.SectionSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.SettingComponent;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.LegacyUiElement;

public class SectionSettingComponent
extends SettingComponent<SectionSetting> {
    public SectionSettingComponent(SectionSetting typedValue169, LegacyUiElement typedValue001) {
        super(typedValue169, typedValue001);
    }

    @Override
    public void internalMethod02325() {
        this.internalField1048 = 13.0f;
        this.internalField1047 = 8.0f;
        super.internalMethod02325();
    }

    @Override
    protected void internalMethod05619(UiRenderContext iII) {
        float f = this.internalField0205 + 8.0f;
        float f2 = this.internalField0206 + 15.0f;
        float f3 = this.internalField1048 - 16.0f;
        float f4 = this.internalField1047 - 20.0f;
        this.internalField0808.internalMethod07062(this.internalMethod04933(iII.internalMethod05259(), iII.internalMethod05261()));
        if (this.internalMethod03399(iII) && ((SectionSetting)this.internalField0644).isVisible() && this.internalMethod05803().internalMethod03399(iII)) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        SizedFont typedValue020 = (((SectionSetting)this.internalField0644).internalMethod04496() ? Fonts.internalField1154 : Fonts.internalField1157).internalMethod01432(8 + ((SectionSetting)this.internalField0644).internalMethod00687());
        if (!((SectionSetting)this.internalField0644).internalMethod04496()) {
            this.internalMethod00985(iII, typedValue020, LanguageManager.internalMethod07214(((SectionSetting)this.internalField0644).getName()), this.internalField0205 + 10.0f, this.internalField0206 + UiUtils.internalMethod07116(typedValue020.internalMethod04890(), 19.0f) - 0.5f - 0.5f * (float)((SectionSetting)this.internalField0644).internalMethod00687(), f3, ThemeColors.internalMethod08459(), 0.7f, 0.99f);
        }
    }

    @Override
    public void internalMethod08256(UiRenderContext iII) {
        if (!((SectionSetting)this.internalField0644).internalMethod04496()) {
            return;
        }
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8 + ((SectionSetting)this.internalField0644).internalMethod00687());
        float f = this.internalField0205 + 8.0f;
        float f2 = this.internalField1048 - 16.0f;
        float f3 = 0.0f;
        for (String string : this.internalMethod04082(typedValue020, f2 - 10.0f)) {
            iII.drawText(typedValue020, string, this.internalField0205 + 10.0f + (((SectionSetting)this.internalField0644).internalMethod08083() ? f2 / 2.0f - typedValue020.internalMethod00965(string) / 2.0f - 1.0f : 0.0f), this.internalField0206 + UiUtils.internalMethod07116(typedValue020.internalMethod04890(), 19.0f) - 0.5f - 0.5f * (float)((SectionSetting)this.internalField0644).internalMethod00687() + f3, ThemeColors.internalMethod08459().mulAlpha(0.75f * RenderSystem.getShaderColor()[3]));
            f3 += typedValue020.internalMethod04890() + 3.0f;
        }
    }

    @Override
    public void internalMethod07807(UiRenderContext iII) {
        float f = 0.5f;
        iII.drawRect(this.internalField0205, this.internalField0206 + this.internalField1047, this.internalField1048, f, ThemeColors.internalMethod08459().withAlpha(5.1f));
    }

    @Override
    public float internalMethod07809() {
        if (!((SectionSetting)this.internalField0644).internalMethod04496()) {
            this.internalField1047 = 18.0f;
            return 18.0f;
        }
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8 + ((SectionSetting)this.internalField0644).internalMethod00687());
        int n = this.internalMethod04082(typedValue020, this.internalField1048 - 26.0f).size();
        this.internalField1047 = Math.max(18.0f, 19.0f + (float)Math.max(0, n - 1) * (typedValue020.internalMethod04890() + 3.0f) + 2.0f);
        return this.internalField1047;
    }

    private List<String> internalMethod04082(SizedFont typedValue020, float f) {
        String[] stringArray = LanguageManager.internalMethod07214(((SectionSetting)this.internalField0644).getName()).split(" ");
        ArrayList<String> arrayList = new ArrayList<String>();
        int n = 0;
        f = Math.max(1.0f, f);
        for (String string : stringArray) {
            if (!arrayList.isEmpty() && typedValue020.internalMethod00965((String)arrayList.get(n) + " " + string) > f) {
                ++n;
            }
            if (arrayList.size() - 1 < n) {
                arrayList.add(string);
                continue;
            }
            arrayList.set(n, (String)arrayList.get(n) + " " + string);
        }
        return arrayList;
    }
}


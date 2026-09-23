package rockstar.client.setting;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;
import rockstar.client.ui.Insets;
import rockstar.client.internal.script.TextInputField;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.AbstractSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.ThemeColors;
import rockstar.client.ui.FlexDirection;
import rockstar.client.ui.TextLabel;
import rockstar.client.ui.UiContainer;

public class TextSetting
extends AbstractSetting {
    private static final Pattern internalField0293 = Pattern.compile("\\X");
    private String internalField0247;
    private boolean internalField0277;
    private int internalField0227;

    public TextSetting(@NotNull SettingOwner typedValue159, String string, String string2, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public TextSetting(@NotNull SettingOwner typedValue159, String string, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public TextSetting(@NotNull SettingOwner typedValue159, String string, String string2) {
        super(typedValue159, string);
    }

    public TextSetting(@NotNull SettingOwner typedValue159, String string) {
        super(typedValue159, string);
    }

    public final TextSetting internalMethod00011(String string) {
        String string2 = this.internalMethod05256(string);
        if (Objects.equals(this.internalField0247, string2)) {
            return this;
        }
        this.notifyChanged();
        this.internalField0247 = string2;
        return this;
    }

    public void internalMethod03337(String string) {
        this.internalMethod00011(string);
    }

    public final TextSetting internalMethod01004(int n) {
        this.internalField0227 = n;
        this.internalField0247 = this.internalMethod05256(this.internalField0247);
        return this;
    }

    private String internalMethod05256(String string) {
        if (this.internalField0227 <= 0 || string == null) {
            return string;
        }
        Matcher matcher = internalField0293.matcher(string);
        int n = 0;
        while (matcher.find()) {
            if (++n <= this.internalField0227) continue;
            return string.substring(0, matcher.start());
        }
        return string;
    }

    public final TextSetting internalMethod07009(boolean bl) {
        this.internalField0277 = bl;
        return this;
    }

    @Override
    public final JsonElement toJson() {
        return new JsonPrimitive(this.internalField0247);
    }

    @Override
    public final void fromJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isString()) {
            return;
        }
        this.internalMethod00011(jsonElement.getAsString());
    }

    @Override
    public UiContainer createComponent() {
        UiContainer typedValue006 = new UiContainer().internalMethod03907(new TextLabel(Fonts.internalField1154.internalMethod01432(8.0f), () -> LanguageManager.internalMethod07214(this.internalField0248)).internalMethod02959(typedValue011 -> ThemeColors.internalField1613.mulAlpha(0.75f + 0.25f * typedValue011.hover())).internalMethod02902().fill()).internalMethod01192(FlexDirection.internalField1246).internalMethod01855(TextAlignment.internalField0621).internalMethod03514(Insets.internalMethod00105(6.0f, 0.0f, 0.0f, 0.0f)).internalMethod09609();
        return new UiContainer().internalMethod01192(FlexDirection.internalField0629).internalMethod03062(4.0f).internalMethod03907(typedValue006).internalMethod03907(new TextInputField(Fonts.internalField1154.internalMethod01432(7.0f), this.internalField0247, this::internalMethod00011).internalMethod04564(this.internalField0277).internalMethod02459(this.internalField0227).internalMethod07533(() -> LanguageManager.internalMethod07214("type_text")).internalMethod06836(typedParameter1011 -> ThemeColors.internalField1614).internalMethod01901(4.0f).internalMethod06744().internalMethod07996(15.0f));
    }

    @Generated
    public String internalMethod08926() {
        return this.internalField0247;
    }

    @Generated
    public boolean internalMethod04496() {
        return this.internalField0277;
    }

    @Generated
    public int internalMethod00346() {
        return this.internalField0227;
    }
}


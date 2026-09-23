package rockstar.client.setting;







import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;
import rockstar.client.ui.LayoutAlignment;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.AbstractSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.KeybindUtils;
import rockstar.client.core.CursorType;
import rockstar.client.ui.FlexDirection;
import rockstar.client.ui.TextLabel;
import rockstar.client.internal.script.KeybindButton;
import rockstar.client.ui.UiContainer;

public class KeybindSetting
extends AbstractSetting {
    private int internalField0227 = -1;

    public KeybindSetting(@NotNull SettingOwner typedValue159, String string, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public KeybindSetting(@NotNull SettingOwner typedValue159, String string) {
        super(typedValue159, string);
    }

    public KeybindSetting internalMethod01713(int n) {
        this.internalMethod02164(n);
        return this;
    }

    public void internalMethod02164(int n) {
        if (this.internalField0227 == n) {
            return;
        }
        this.notifyChanged();
        this.internalField0227 = n;
    }

    public boolean internalMethod02165(int n) {
        return this.isVisible() && KeybindUtils.internalMethod04328(this.internalField0227, n);
    }

    public boolean internalMethod04496() {
        return this.isVisible() && KeybindUtils.internalMethod08521(this.internalField0227);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive((Number)this.internalField0227);
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
        if (jsonElement != null && jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber()) {
            this.internalMethod02164(-1);
            int n = jsonElement.getAsInt();
            if (n != -1) {
                this.internalMethod02164(n);
            }
        }
    }

    @Override
    public boolean isValidJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isNumber()) {
            return false;
        }
        double d = jsonElement.getAsDouble();
        return Double.isFinite(d) && d >= -2.147483648E9 && d <= 2.147483647E9;
    }

    @Override
    public UiContainer createComponent() {
        KeybindButton typedValue013 = new KeybindButton(Fonts.internalField1154.internalMethod01432(7.0f), this::internalMethod07477, this::internalMethod02164);
        return new UiContainer().internalMethod09266(17.0f).internalMethod03907(new TextLabel(Fonts.internalField1154.internalMethod01432(8.0f), () -> LanguageManager.internalMethod07214(this.internalField0248)).internalMethod02959(typedValue011 -> ThemeColors.internalField1613.mulAlpha(0.75f + 0.25f * typedValue011.hover())).internalMethod02902().fill()).internalMethod03907(typedValue013).internalMethod03062(5.0f).internalMethod01192(FlexDirection.internalField1246).internalMethod07607(LayoutAlignment.internalField1377).internalMethod01855(TextAlignment.internalField0621).internalMethod02525(typedValue013::internalMethod00007).internalMethod04332(CursorType.internalField0567);
    }

    @Generated
    public int internalMethod07477() {
        return this.internalField0227;
    }
}


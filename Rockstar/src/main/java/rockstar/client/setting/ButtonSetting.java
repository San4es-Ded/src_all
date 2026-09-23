package rockstar.client.setting;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;
import rockstar.client.ui.LayoutAlignment;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.AbstractSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.UiElement;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.ui.FlexDirection;
import rockstar.client.ui.UiContainer;

public class ButtonSetting
extends AbstractSetting {
    private Runnable internalField0659 = System.out::println;

    public ButtonSetting(@NotNull SettingOwner typedValue159, String string, String string2, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public ButtonSetting(@NotNull SettingOwner typedValue159, String string, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public ButtonSetting(@NotNull SettingOwner typedValue159, String string, String string2) {
        super(typedValue159, string);
    }

    public ButtonSetting(@NotNull SettingOwner typedValue159, String string) {
        super(typedValue159, string);
    }

    public ButtonSetting internalMethod07149(Runnable runnable) {
        this.internalField0659 = runnable;
        return this;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive("\u0441\u0443\u043a\u0430 \u043a\u0430\u043a \u0441\u0434\u0435\u043b\u0430\u0442\u044c \u0442\u0430\u043a \u0447\u0442\u043e\u0431\u044b \u0434\u043b\u044f \u043d\u0435\u0433\u043e \u043d\u0435 \u0431\u044b\u043b\u043e \u043a\u0444\u0433");
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
    }

    @Override
    public UiContainer createComponent() {
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        return new UiContainer().internalMethod01192(FlexDirection.internalField0629).internalMethod07607(LayoutAlignment.internalField0912).internalMethod09266(18.0f).internalMethod03907(new UiElement().fillWidth().height(16.0f).radius(6.0f).background(typedParameter1002 -> ThemeColors.internalField1612.mulAlpha(0.3f + 0.2f * typedParameter1002.hover() + 0.1f * typedParameter1002.press())).text(typedValue020, () -> LanguageManager.internalMethod07214(this.internalField0248), typedParameter1002 -> ThemeColors.internalField1613.mulAlpha(0.75f + 0.25f * typedParameter1002.hover())).textAlign(TextAlignment.internalField0621).cursor(CursorType.internalField0567).onClick(() -> this.internalField0659.run()));
    }

    @Generated
    public Runnable internalMethod03496() {
        return this.internalField0659;
    }

    @Generated
    public void internalMethod00488(Runnable runnable) {
        this.internalField0659 = runnable;
    }
}


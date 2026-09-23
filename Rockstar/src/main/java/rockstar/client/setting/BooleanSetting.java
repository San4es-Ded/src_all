package rockstar.client.setting;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;
import rockstar.client.ui.LayoutAlignment;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.internal.script.ToggleSwitchWidget;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.AbstractSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.ui.FlexDirection;
import rockstar.client.core.Toggleable;
import rockstar.client.ui.TextLabel;
import rockstar.client.ui.UiContainer;

public class BooleanSetting
extends AbstractSetting
implements Toggleable {
    private boolean internalField0277;

    public BooleanSetting(@NotNull SettingOwner typedValue159, String string, String string2, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public BooleanSetting(@NotNull SettingOwner typedValue159, String string, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public BooleanSetting(@NotNull SettingOwner typedValue159, String string, String string2) {
        super(typedValue159, string);
    }

    public BooleanSetting(@NotNull SettingOwner typedValue159, String string) {
        super(typedValue159, string);
    }

    public BooleanSetting internalMethod04836(boolean bl) {
        this.internalMethod02034(bl);
        return this;
    }

    public BooleanSetting internalMethod06630() {
        this.internalMethod02034(true);
        return this;
    }

    public void internalMethod02034(boolean bl) {
        if (this.internalField0277 == bl) {
            return;
        }
        this.notifyChanged();
        this.internalField0277 = bl;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(Boolean.valueOf(this.internalField0277));
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return;
        }
        if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isBoolean()) {
            this.internalMethod02034(jsonElement.getAsBoolean());
            return;
        }
        if (jsonElement.isJsonObject()) {
            JsonElement jsonElement2;
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement jsonElement3 = jsonElement2 = jsonObject.has("enabled") ? jsonObject.get("enabled") : jsonObject.get("value");
            if (jsonElement2 != null && jsonElement2.isJsonPrimitive() && jsonElement2.getAsJsonPrimitive().isBoolean()) {
                this.internalMethod02034(jsonElement2.getAsBoolean());
            }
        }
    }

    @Override
    public boolean isValidJson(JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return false;
        }
        if (jsonElement.isJsonPrimitive()) {
            return jsonElement.getAsJsonPrimitive().isBoolean();
        }
        if (!jsonElement.isJsonObject()) {
            return false;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement jsonElement2 = jsonObject.has("enabled") ? jsonObject.get("enabled") : jsonObject.get("value");
        return jsonElement2 != null && jsonElement2.isJsonPrimitive() && jsonElement2.getAsJsonPrimitive().isBoolean();
    }

    @Override
    public UiContainer createComponent() {
        return new UiContainer().internalMethod09266(18.0f).internalMethod03907(new TextLabel(Fonts.internalField1154.internalMethod01432(8.0f), () -> LanguageManager.internalMethod07214(this.internalField0248)).internalMethod02959(typedValue011 -> ThemeColors.internalField1613.mulAlpha(0.75f + 0.25f * typedValue011.hover())).internalMethod02902().fill()).internalMethod03907(new ToggleSwitchWidget(this::internalMethod04496).internalMethod05792(() -> ThemeColors.internalField1614).size(13.0f, 8.0f).minSize(13.0f, 8.0f).snapSize()).internalMethod03062(5.0f).internalMethod01192(FlexDirection.internalField1246).internalMethod07607(LayoutAlignment.internalField1377).internalMethod01855(TextAlignment.internalField0621).internalMethod05690(this::toggle).internalMethod04332(CursorType.internalField0567);
    }

    @Override
    public void toggle() {
        this.internalMethod02034(!this.internalField0277);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Generated
    public boolean internalMethod04496() {
        return this.internalField0277;
    }
}


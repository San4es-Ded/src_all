package rockstar.client.setting;







import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import rockstar.client.ui.Insets;
import rockstar.client.internal.script.RangeSliderWidget;
import rockstar.client.ui.LayoutAlignment;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.animation.Motion;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.AbstractSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.UiElement;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.TextUtils;
import rockstar.client.ui.FlexDirection;
import rockstar.client.ui.TextLabel;
import rockstar.client.ui.UiContainer;
import rockstar.client.internal.script.NumberInputField;

public class RangeSetting
extends AbstractSetting {
    private float internalField0205;
    private float internalField0206;
    private float internalField1048;
    private float internalField1047;
    private float internalField1049;
    private float internalField1046 = Float.NaN;

    public RangeSetting(@NotNull SettingOwner typedValue159, String string, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public RangeSetting(@NotNull SettingOwner typedValue159, String string) {
        super(typedValue159, string);
    }

    public RangeSetting internalMethod01407(float f) {
        this.internalMethod01445(f);
        return this;
    }

    public RangeSetting internalMethod06328(float f) {
        this.internalMethod01501(f);
        return this;
    }

    public RangeSetting internalMethod08834(float f) {
        this.internalField1048 = f;
        return this;
    }

    public RangeSetting internalMethod08219(float f) {
        this.internalField1047 = f;
        return this;
    }

    public RangeSetting internalMethod08853(float f) {
        this.internalField1049 = f;
        return this;
    }

    public RangeSetting internalMethod08255(float f) {
        this.internalField1046 = f;
        if (this.internalField0206 < this.internalMethod06916()) {
            this.internalField0206 = this.internalMethod06916();
        }
        return this;
    }

    public float internalMethod06916() {
        return Float.isNaN(this.internalField1046) ? this.internalField1048 : this.internalField1046;
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("first", (Number)Float.valueOf(this.internalField0205));
        jsonObject.addProperty("second", (Number)Float.valueOf(this.internalField0206));
        return jsonObject;
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (!(!jsonObject.has("first") || jsonObject.get("first").isJsonPrimitive() && jsonObject.get("first").getAsJsonPrimitive().isNumber())) {
            return;
        }
        if (!(!jsonObject.has("second") || jsonObject.get("second").isJsonPrimitive() && jsonObject.get("second").getAsJsonPrimitive().isNumber())) {
            return;
        }
        float f = this.internalField0205;
        float f2 = this.internalField0206;
        if (jsonObject.has("first")) {
            f = jsonObject.get("first").getAsFloat();
        }
        if (jsonObject.has("second")) {
            f2 = jsonObject.get("second").getAsFloat();
        }
        if (!Float.isFinite(f) || !Float.isFinite(f2)) {
            return;
        }
        this.internalMethod01445(f);
        this.internalMethod01501(f2);
    }

    @Override
    public boolean isValidJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return false;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject.has("first") && jsonObject.get("first").isJsonPrimitive() && jsonObject.get("first").getAsJsonPrimitive().isNumber() && Float.isFinite(jsonObject.get("first").getAsFloat()) && jsonObject.has("second") && jsonObject.get("second").isJsonPrimitive() && jsonObject.get("second").getAsJsonPrimitive().isNumber() && Float.isFinite(jsonObject.get("second").getAsFloat());
    }

    public void internalMethod01445(float f) {
        float f2 = (float)MathHelper.clamp((double)((double)Math.round((double)f * (1.0 / (double)this.internalField1049)) / (1.0 / (double)this.internalField1049)), (double)this.internalField1048, (double)this.internalField1047);
        if (this.internalField0205 == f2) {
            return;
        }
        this.notifyChanged();
        this.internalField0205 = f2;
    }

    public void internalMethod01501(float f) {
        float f2 = (float)MathHelper.clamp((double)((double)Math.round((double)f * (1.0 / (double)this.internalField1049)) / (1.0 / (double)this.internalField1049)), (double)this.internalMethod06916(), (double)this.internalField1047);
        if (this.internalField0206 == f2) {
            return;
        }
        this.notifyChanged();
        this.internalField0206 = f2;
    }

    @Override
    public UiContainer createComponent() {
        UiContainer typedValue006 = new UiContainer().internalMethod03907(new TextLabel(Fonts.internalField1154.internalMethod01432(8.0f), () -> LanguageManager.internalMethod07214(this.internalField0248)).internalMethod02959(typedValue011 -> ThemeColors.internalField1613.mulAlpha(0.75f + 0.25f * typedValue011.hover())).internalMethod02902().fill()).internalMethod03907(new UiContainer().internalMethod01192(FlexDirection.internalField1246).internalMethod01855(TextAlignment.internalField0621).internalMethod03062(3.0f).internalMethod03907(new NumberInputField(Fonts.internalField1154.internalMethod01432(7.0f), this::internalMethod06919, this::internalMethod01445, this.internalField1048, this.internalField1047).internalMethod05772(() -> TextUtils.internalMethod00670(this.internalMethod06919())).internalMethod01821(typedValue014 -> ThemeColors.internalField1310)).internalMethod03907(new UiElement().text(Fonts.internalField1154.internalMethod01432(7.0f), "-", ThemeColors.internalField1613.mulAlpha(0.5f)).interactive(false)).internalMethod03907(new NumberInputField(Fonts.internalField1154.internalMethod01432(7.0f), this::internalMethod07967, this::internalMethod01501, this.internalMethod06916(), this.internalField1047).internalMethod05772(() -> TextUtils.internalMethod00670(this.internalMethod07967())).internalMethod01821(typedValue014 -> ThemeColors.internalField1310))).internalMethod03062(6.0f).internalMethod01192(FlexDirection.internalField1246).internalMethod07607(LayoutAlignment.internalField1377).internalMethod01855(TextAlignment.internalField0621).internalMethod03514(Insets.internalMethod00105(6.0f, 0.0f, 0.0f, 0.0f)).internalMethod09609();
        return new UiContainer().internalMethod01192(FlexDirection.internalField0629).internalMethod03062(5.0f).internalMethod03907(typedValue006).internalMethod03907(new UiContainer().internalMethod01192(FlexDirection.internalField0629).internalMethod09609().internalMethod03514(Insets.internalMethod00105(0.0f, 0.0f, 4.0f, 0.0f)).internalMethod03907(new RangeSliderWidget(this::internalMethod06919, this::internalMethod01445, this::internalMethod07967, this::internalMethod01501, this.internalField1048, this.internalField1047).internalMethod05734(this.internalField1049).internalMethod05567().internalMethod09073(6.0f).internalMethod07015(3.0f).internalMethod08650(3.0f).internalMethod08906(1.5f).internalMethod02936(typedParameter1009 -> ThemeColors.internalField1614).internalMethod04358(typedParameter1009 -> ThemeColors.internalField1310.mulAlpha(1.0f - 0.25f * typedParameter1009.hover())).internalMethod05725(Motion.internalMethod01328(300L, Easing.internalField1325))));
    }

    @Generated
    public float internalMethod06919() {
        return this.internalField0205;
    }

    @Generated
    public float internalMethod07967() {
        return this.internalField0206;
    }

    @Generated
    public float internalMethod07968() {
        return this.internalField1048;
    }

    @Generated
    public float internalMethod07979() {
        return this.internalField1047;
    }

    @Generated
    public float internalMethod07980() {
        return this.internalField1049;
    }
}


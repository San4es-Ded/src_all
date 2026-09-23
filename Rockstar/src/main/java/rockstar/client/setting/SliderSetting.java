package rockstar.client.setting;







import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import rockstar.client.ui.Insets;
import rockstar.client.ui.LayoutAlignment;
import rockstar.client.internal.script.SingleValueSlider;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.internal.core.ValueTransform;
import rockstar.client.animation.Motion;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.AbstractSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.TextUtils;
import rockstar.client.ui.FlexDirection;
import rockstar.client.ui.TextLabel;
import rockstar.client.ui.UiContainer;
import rockstar.client.internal.script.NumberInputField;

public class SliderSetting
extends AbstractSetting {
    protected float internalField0205;
    protected float internalField0206;
    protected float internalField1048;
    protected float internalField1047;
    private InternalType0176 internalField0638 = f -> "";
    private ValueTransform<Float> internalField0445 = f -> f;

    public SliderSetting(@NotNull SettingOwner typedValue159, String string, String string2, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public SliderSetting(@NotNull SettingOwner typedValue159, String string, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public SliderSetting(@NotNull SettingOwner typedValue159, String string, String string2) {
        super(typedValue159, string);
    }

    public SliderSetting(@NotNull SettingOwner typedValue159, String string) {
        super(typedValue159, string);
    }

    public SliderSetting internalMethod05900(float f) {
        this.internalField0205 = f;
        return this;
    }

    public SliderSetting internalMethod02732(float f) {
        this.internalField0206 = f;
        return this;
    }

    public SliderSetting internalMethod08673(float f) {
        this.internalField1048 = f;
        return this;
    }

    public SliderSetting internalMethod05660(InternalType0176 nestedValue2024) {
        this.internalField0638 = nestedValue2024;
        return this;
    }

    public SliderSetting internalMethod06240(String string) {
        this.internalField0638 = f -> string;
        return this;
    }

    public SliderSetting internalMethod04966(ValueTransform<Float> typedValue156) {
        this.internalField0445 = typedValue156;
        return this;
    }

    public SliderSetting internalMethod08074(float f) {
        this.internalMethod04736(f);
        return this;
    }

    public String internalMethod08885() {
        return this.internalField0638.apply(this.internalMethod08576()).contains(" ") ? " " + LanguageManager.internalMethod07214(this.internalField0638.apply(this.internalMethod08576()).replace(" ", "")) : LanguageManager.internalMethod07214(this.internalField0638.apply(this.internalMethod08576()));
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive((Number)Float.valueOf(this.internalField1047));
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
        float f;
        if (jsonElement != null && jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber() && Float.isFinite(f = jsonElement.getAsFloat())) {
            this.internalMethod04736(f);
        }
    }

    @Override
    public boolean isValidJson(JsonElement jsonElement) {
        return jsonElement != null && jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber() && Float.isFinite(jsonElement.getAsFloat());
    }

    public void internalMethod04736(float f) {
        float f2 = MathHelper.clamp((float)((float)((double)Math.round((double)(f = this.internalField0445.changed(Float.valueOf(f)).floatValue()) * (1.0 / (double)this.internalField1048)) / (1.0 / (double)this.internalField1048))), (float)this.internalField0205, (float)this.internalField0206);
        if (this.internalField1047 == f2) {
            return;
        }
        this.notifyChanged();
        this.internalField1047 = f2;
    }

    @Override
    public UiContainer createComponent() {
        UiContainer typedValue006 = new UiContainer().internalMethod03907(new TextLabel(Fonts.internalField1154.internalMethod01432(8.0f), () -> LanguageManager.internalMethod07214(this.internalField0248)).internalMethod02959(typedValue011 -> ThemeColors.internalField1613.mulAlpha(0.75f + 0.25f * typedValue011.hover())).internalMethod02902().fill()).internalMethod03907(new NumberInputField(Fonts.internalField1154.internalMethod01432(7.0f), this::internalMethod08576, this::internalMethod04736, this.internalField0205, this.internalField0206).internalMethod05772(() -> TextUtils.internalMethod00670(this.internalMethod08576())).internalMethod03023(this::internalMethod08885).internalMethod01821(typedValue014 -> ThemeColors.internalField1310)).internalMethod03062(6.0f).internalMethod01192(FlexDirection.internalField1246).internalMethod07607(LayoutAlignment.internalField1377).internalMethod01855(TextAlignment.internalField0621).internalMethod03514(Insets.internalMethod00105(6.0f, 0.0f, 0.0f, 0.0f)).internalMethod09609();
        return new UiContainer().internalMethod01192(FlexDirection.internalField0629).internalMethod03062(5.0f).internalMethod03907(typedValue006).internalMethod03907(new UiContainer().internalMethod01192(FlexDirection.internalField0629).internalMethod09609().internalMethod03514(Insets.internalMethod00105(0.0f, 0.0f, 4.0f, 0.0f)).internalMethod03907(new SingleValueSlider(this::internalMethod08576, this::internalMethod04736, this.internalMethod05288(), this.internalMethod05291()).internalMethod07013(this.internalMethod08575()).internalMethod06319().internalMethod08206(6.0f).internalMethod00229(3.0f).internalMethod08796(3.0f).internalMethod09045(1.5f).internalMethod00551(typedParameter1010 -> ThemeColors.internalField1614).internalMethod01909(typedParameter1010 -> ThemeColors.internalField1310.mulAlpha(1.0f - 0.25f * typedParameter1010.hover())).internalMethod07361(Motion.internalMethod01328(300L, Easing.internalField1325))));
    }

    @Generated
    public float internalMethod05288() {
        return this.internalField0205;
    }

    @Generated
    public float internalMethod05291() {
        return this.internalField0206;
    }

    @Generated
    public float internalMethod08575() {
        return this.internalField1048;
    }

    @Generated
    public float internalMethod08576() {
        return this.internalField1047;
    }

    @Generated
    public ValueTransform<Float> internalMethod01757() {
        return this.internalField0445;
    }

    public static interface InternalType0176 {
        public String apply(float localValue1);
    }
}


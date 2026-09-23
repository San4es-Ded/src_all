package rockstar.client.setting;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import org.jetbrains.annotations.NotNull;
import rockstar.client.ui.Insets;
import rockstar.client.internal.script.TwoAxisPad;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.internal.core.ValueTransform;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.AbstractSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.ThemeColors;
import rockstar.client.ui.FlexDirection;
import rockstar.client.ui.TextLabel;
import rockstar.client.ui.UiContainer;

public class VectorRangeSetting
extends AbstractSetting {
    private float internalField0205;
    private float internalField0206;
    private float internalField1048 = -1.0f;
    private float internalField1047 = 1.0f;
    private float internalField1049 = -1.0f;
    private float internalField1046 = 1.0f;
    private float internalField1456;
    private float internalField1457;
    private ValueTransform<Vec2f> internalField0445 = vec2f -> vec2f;

    public VectorRangeSetting(@NotNull SettingOwner typedValue159, String string, String string2, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public VectorRangeSetting(@NotNull SettingOwner typedValue159, String string, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public VectorRangeSetting(@NotNull SettingOwner typedValue159, String string) {
        super(typedValue159, string);
    }

    public VectorRangeSetting internalMethod03305(ValueTransform<Vec2f> typedValue156) {
        this.internalField0445 = typedValue156 == null ? vec2f -> vec2f : typedValue156;
        this.internalMethod03891(this.internalField0205, this.internalField0206);
        return this;
    }

    public VectorRangeSetting internalMethod02107(float f) {
        this.internalMethod03891(f, this.internalField0206);
        return this;
    }

    public VectorRangeSetting internalMethod07041(float f) {
        this.internalMethod03891(this.internalField0205, f);
        return this;
    }

    public VectorRangeSetting internalMethod07235(float f, float f2) {
        this.internalMethod03891(f, f2);
        return this;
    }

    public VectorRangeSetting internalMethod08471(float f) {
        this.internalField1048 = f;
        this.internalMethod03698();
        return this;
    }

    public VectorRangeSetting internalMethod07903(float f) {
        this.internalField1047 = f;
        this.internalMethod03698();
        return this;
    }

    public VectorRangeSetting internalMethod08506(float f) {
        this.internalField1049 = f;
        this.internalMethod03698();
        return this;
    }

    public VectorRangeSetting internalMethod07929(float f) {
        this.internalField1046 = f;
        this.internalMethod03698();
        return this;
    }

    public VectorRangeSetting internalMethod09427(float f) {
        this.internalField1456 = MathHelper.clamp((float)f, (float)this.internalField1048, (float)this.internalField1047);
        return this;
    }

    public VectorRangeSetting internalMethod09929(float f) {
        this.internalField1457 = MathHelper.clamp((float)f, (float)this.internalField1049, (float)this.internalField1046);
        return this;
    }

    public Vec2f internalMethod00458() {
        return new Vec2f(this.internalField0205, this.internalField0206);
    }

    public Vec2f internalMethod05674() {
        return new Vec2f(this.internalField1456, this.internalField1457);
    }

    public void internalMethod03891(float f, float f2) {
        Vec2f vec2f = this.internalField0445.changed(new Vec2f(f, f2));
        float f3 = MathHelper.clamp((float)vec2f.x, (float)this.internalField1048, (float)this.internalField1047);
        float f4 = MathHelper.clamp((float)vec2f.y, (float)this.internalField1049, (float)this.internalField1046);
        if (this.internalField0205 == f3 && this.internalField0206 == f4) {
            return;
        }
        this.notifyChanged();
        this.internalField0205 = f3;
        this.internalField0206 = f4;
    }

    public void internalMethod03464(Vec2f vec2f) {
        this.internalMethod03891(vec2f.x, vec2f.y);
    }

    private void internalMethod03698() {
        this.internalMethod03891(this.internalField0205, this.internalField0206);
        this.internalMethod09427(this.internalField1456);
        this.internalMethod09929(this.internalField1457);
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("x", (Number)Float.valueOf(this.internalField0205));
        jsonObject.addProperty("y", (Number)Float.valueOf(this.internalField0206));
        jsonObject.addProperty("bind_x", (Number)Float.valueOf(this.internalField1456));
        jsonObject.addProperty("bind_y", (Number)Float.valueOf(this.internalField1457));
        return jsonObject;
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.has("x") && !this.internalMethod04006(jsonObject.get("x")) || jsonObject.has("y") && !this.internalMethod04006(jsonObject.get("y")) || jsonObject.has("bind_x") && !this.internalMethod04006(jsonObject.get("bind_x")) || jsonObject.has("bind_y") && !this.internalMethod04006(jsonObject.get("bind_y"))) {
            return;
        }
        float f = this.internalField0205;
        float f2 = this.internalField0206;
        float f3 = this.internalField1456;
        float f4 = this.internalField1457;
        if (jsonObject.has("x")) {
            f = jsonObject.get("x").getAsFloat();
        }
        if (jsonObject.has("y")) {
            f2 = jsonObject.get("y").getAsFloat();
        }
        if (jsonObject.has("bind_x")) {
            f3 = jsonObject.get("bind_x").getAsFloat();
        }
        if (jsonObject.has("bind_y")) {
            f4 = jsonObject.get("bind_y").getAsFloat();
        }
        if (!(Float.isFinite(f) && Float.isFinite(f2) && Float.isFinite(f3) && Float.isFinite(f4))) {
            return;
        }
        this.internalMethod03891(f, f2);
        this.internalMethod09427(f3);
        this.internalMethod09929(f4);
    }

    private boolean internalMethod04006(JsonElement jsonElement) {
        return jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber() && Float.isFinite(jsonElement.getAsFloat());
    }

    @Override
    public boolean isValidJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return false;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject.has("x") && this.internalMethod04006(jsonObject.get("x")) && jsonObject.has("y") && this.internalMethod04006(jsonObject.get("y")) && jsonObject.has("bind_x") && this.internalMethod04006(jsonObject.get("bind_x")) && jsonObject.has("bind_y") && this.internalMethod04006(jsonObject.get("bind_y"));
    }

    @Override
    public UiContainer createComponent() {
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(7.0f);
        UiContainer typedValue006 = new UiContainer().internalMethod03907(new TextLabel(Fonts.internalField1154.internalMethod01432(8.0f), () -> LanguageManager.internalMethod07214(this.internalField0248)).internalMethod02959(typedValue011 -> ThemeColors.internalField1613.mulAlpha(0.75f + 0.25f * typedValue011.hover())).internalMethod02902().fill()).internalMethod01192(FlexDirection.internalField1246).internalMethod01855(TextAlignment.internalField0621).internalMethod03514(Insets.internalMethod00105(6.0f, 0.0f, 0.0f, 0.0f)).internalMethod09609();
        return new UiContainer().internalMethod01192(FlexDirection.internalField0629).internalMethod03062(6.0f).internalMethod03907(typedValue006).internalMethod03907(new TwoAxisPad(this::internalMethod03695, this::internalMethod03697, this::internalMethod03891, this.internalField1048, this.internalField1047, this.internalField1049, this.internalField1046).internalMethod05064(2.0f).internalMethod03261(typedValue020, () -> String.format(Locale.ROOT, "%.2f : %.2f", Float.valueOf(this.internalMethod03695()), Float.valueOf(this.internalMethod03697()))).internalMethod04178());
    }

    @Generated
    public float internalMethod03695() {
        return this.internalField0205;
    }

    @Generated
    public float internalMethod03697() {
        return this.internalField0206;
    }

    @Generated
    public float internalMethod08931() {
        return this.internalField1048;
    }

    @Generated
    public float internalMethod08932() {
        return this.internalField1047;
    }

    @Generated
    public float internalMethod08949() {
        return this.internalField1049;
    }

    @Generated
    public float internalMethod08951() {
        return this.internalField1046;
    }

    @Generated
    public float internalMethod09559() {
        return this.internalField1456;
    }

    @Generated
    public float internalMethod09560() {
        return this.internalField1457;
    }

    @Generated
    public ValueTransform<Vec2f> internalMethod05944() {
        return this.internalField0445;
    }
}


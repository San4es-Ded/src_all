package rockstar.client.setting;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import net.minecraft.util.math.Vec2f;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rockstar.client.ui.Insets;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.AbstractSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.ui.VectorRangeSlider;
import rockstar.client.ui.FlexDirection;
import rockstar.client.ui.TextLabel;
import rockstar.client.ui.UiContainer;

public class Vector2Setting
extends AbstractSetting {
    private Vec2f internalField0281 = Vec2f.ZERO;
    private Vec2f internalField0280 = new Vec2f(1.0f, 1.0f);

    public Vector2Setting(@NotNull SettingOwner typedValue159, String string, String string2, @Nullable BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public Vector2Setting(@NotNull SettingOwner typedValue159, String string, @Nullable BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public Vector2Setting(@NotNull SettingOwner typedValue159, String string, String string2) {
        super(typedValue159, string);
    }

    public Vector2Setting(@NotNull SettingOwner typedValue159, String string) {
        super(typedValue159, string);
    }

    public Vector2Setting internalMethod06395(float f, float f2) {
        return this.internalMethod07286(new Vec2f(f, f2));
    }

    public Vector2Setting internalMethod06615(float f, float f2) {
        return this.internalMethod01741(new Vec2f(f, f2));
    }

    public Vector2Setting internalMethod07286(Vec2f vec2f) {
        if (this.internalField0281.equals(vec2f)) {
            return this;
        }
        this.notifyChanged();
        this.internalField0281 = vec2f;
        return this;
    }

    public Vector2Setting internalMethod01741(Vec2f vec2f) {
        if (this.internalField0280.equals(vec2f)) {
            return this;
        }
        this.notifyChanged();
        this.internalField0280 = vec2f;
        return this;
    }

    public Easing internalMethod03095() {
        return Easing.internalMethod05127(this.internalField0281.x, 1.0f - this.internalField0281.y, this.internalField0280.x, 1.0f - this.internalField0280.y);
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("start_x", (Number)Float.valueOf(this.internalField0281.x));
        jsonObject.addProperty("start_y", (Number)Float.valueOf(this.internalField0281.y));
        jsonObject.addProperty("end_x", (Number)Float.valueOf(this.internalField0280.x));
        jsonObject.addProperty("end_y", (Number)Float.valueOf(this.internalField0280.y));
        return jsonObject;
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.has("start_x") && !this.internalMethod03842(jsonObject.get("start_x")) || jsonObject.has("start_y") && !this.internalMethod03842(jsonObject.get("start_y")) || jsonObject.has("end_x") && !this.internalMethod03842(jsonObject.get("end_x")) || jsonObject.has("end_y") && !this.internalMethod03842(jsonObject.get("end_y"))) {
            return;
        }
        float f = this.internalField0281.x;
        float f2 = this.internalField0281.y;
        float f3 = this.internalField0280.x;
        float f4 = this.internalField0280.y;
        if (jsonObject.has("start_x") && jsonObject.has("start_y")) {
            f = jsonObject.get("start_x").getAsFloat();
            f2 = jsonObject.get("start_y").getAsFloat();
        }
        if (jsonObject.has("end_x") && jsonObject.has("end_y")) {
            f3 = jsonObject.get("end_x").getAsFloat();
            f4 = jsonObject.get("end_y").getAsFloat();
        }
        if (!(Float.isFinite(f) && Float.isFinite(f2) && Float.isFinite(f3) && Float.isFinite(f4))) {
            return;
        }
        this.internalMethod07286(new Vec2f(f, f2));
        this.internalMethod01741(new Vec2f(f3, f4));
    }

    private boolean internalMethod03842(JsonElement jsonElement) {
        return jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber() && Float.isFinite(jsonElement.getAsFloat());
    }

    @Override
    public boolean isValidJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return false;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject.has("start_x") && this.internalMethod03842(jsonObject.get("start_x")) && jsonObject.has("start_y") && this.internalMethod03842(jsonObject.get("start_y")) && jsonObject.has("end_x") && this.internalMethod03842(jsonObject.get("end_x")) && jsonObject.has("end_y") && this.internalMethod03842(jsonObject.get("end_y"));
    }

    @Override
    public UiContainer createComponent() {
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        UiContainer typedValue006 = new UiContainer().internalMethod03907(new TextLabel(typedValue020, () -> LanguageManager.internalMethod07214(this.internalField0248)).internalMethod02959(typedValue011 -> ThemeColors.internalField1613.mulAlpha(0.75f + 0.25f * typedValue011.hover())).internalMethod02902().fill()).internalMethod01192(FlexDirection.internalField1246).internalMethod01855(TextAlignment.internalField0621).internalMethod03514(Insets.internalMethod00105(6.0f, 0.0f, 0.0f, 0.0f)).internalMethod09609();
        return new UiContainer().internalMethod01192(FlexDirection.internalField0629).internalMethod03062(6.0f).internalMethod03907(typedValue006).internalMethod03907(new VectorRangeSlider(this::internalMethod03179, this::internalMethod00322, this::internalMethod07286, this::internalMethod01741).internalMethod07806(ThemeColors.internalField1312.mulAlpha(0.25f)).internalMethod07085(6.0f).internalMethod05543());
    }

    @Generated
    public Vec2f internalMethod03179() {
        return this.internalField0281;
    }

    @Generated
    public Vec2f internalMethod00322() {
        return this.internalField0280;
    }
}


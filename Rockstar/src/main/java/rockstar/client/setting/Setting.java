package rockstar.client.setting;



import rockstar.client.ui.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import java.util.Map;
import java.util.function.BooleanSupplier;
import rockstar.client.setting.SettingOwner;
import rockstar.client.ui.UiContainer;

public interface Setting {
    public String getName();

    public String getDescriptionKey();

    public BooleanSupplier getHiddenCondition();

    public void registerWithOwner(SettingOwner localValue1);

    default public boolean isVisible() {
        return !this.getHiddenCondition().getAsBoolean();
    }

    public JsonElement toJson();

    public void fromJson(JsonElement localValue1);

    default public boolean isValidJson(JsonElement jsonElement) {
        return Setting.internalMethod02763(this.toJson(), jsonElement);
    }

    private static boolean internalMethod02763(JsonElement jsonElement, JsonElement jsonElement2) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return jsonElement2 == null || jsonElement2.isJsonNull();
        }
        if (jsonElement2 == null || jsonElement2.isJsonNull()) {
            return false;
        }
        if (jsonElement.isJsonPrimitive()) {
            if (!jsonElement2.isJsonPrimitive()) {
                return false;
            }
            if (jsonElement.getAsJsonPrimitive().isBoolean()) {
                return jsonElement2.getAsJsonPrimitive().isBoolean();
            }
            if (jsonElement.getAsJsonPrimitive().isNumber()) {
                return jsonElement2.getAsJsonPrimitive().isNumber() && Double.isFinite(jsonElement2.getAsDouble());
            }
            return jsonElement2.getAsJsonPrimitive().isString();
        }
        if (jsonElement.isJsonArray()) {
            if (!jsonElement2.isJsonArray()) {
                return false;
            }
            if (jsonElement.getAsJsonArray().size() == 0) {
                return true;
            }
            for (JsonElement jsonElement3 : jsonElement2.getAsJsonArray()) {
                if (Setting.internalMethod02763(jsonElement.getAsJsonArray().get(0), jsonElement3)) continue;
                return false;
            }
            return true;
        }
        if (!jsonElement2.isJsonObject()) {
            return false;
        }
        for (Map.Entry entry : jsonElement.getAsJsonObject().entrySet()) {
            if (!jsonElement2.getAsJsonObject().has((String)entry.getKey()) || Setting.internalMethod02763((JsonElement)entry.getValue(), jsonElement2.getAsJsonObject().get((String)entry.getKey()))) continue;
            return false;
        }
        return true;
    }

    default public UiContainer createComponent() {
        return null;
    }
}


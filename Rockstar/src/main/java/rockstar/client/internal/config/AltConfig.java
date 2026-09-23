package rockstar.client.internal.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import rockstar.client.RockstarClient;
import rockstar.client.internal.auth.AltManager;
import rockstar.client.internal.core.ConfigFileAnnotation;
import rockstar.client.internal.script.ConfigManager;

@ConfigFileAnnotation(internalMethod03654="alts", internalMethod00190="json")
public final class AltConfig
extends AbstractClientConfig {
    @Override
    public void internalMethod07509() {
        try {
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray = new JsonArray();
            for (String string : AltManager.getAlts()) {
                jsonArray.add(string);
            }
            jsonObject.add("alts", jsonArray);
            ConfigManager.internalMethod01467(this.internalMethod05023(), jsonObject);
        }
        catch (Exception exception) {
            RockstarClient.internalField0572.error("Failed to save alt list", (Throwable)exception);
        }
    }

    @Override
    public void internalMethod07512() {
        try {
            JsonObject jsonObject = ConfigManager.internalField0931.fromJson(new FileReader(this.internalMethod05023()), JsonObject.class);
            if (jsonObject == null || !jsonObject.has("alts")) {
                return;
            }
            ArrayList<String> arrayList = new ArrayList<String>();
            for (JsonElement jsonElement : jsonObject.getAsJsonArray("alts")) {
                arrayList.add(jsonElement.getAsString());
            }
            AltManager.setAlts(arrayList);
        }
        catch (Exception exception) {
            RockstarClient.internalField0572.error("Failed to load alt list", (Throwable)exception);
        }
    }
}

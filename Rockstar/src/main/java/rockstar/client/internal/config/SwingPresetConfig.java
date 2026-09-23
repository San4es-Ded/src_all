package rockstar.client.internal.config;








import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.animation.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import lombok.Generated;
import net.minecraft.text.Text;
import rockstar.client.setting.Setting;
import rockstar.client.internal.script.ConfigManager;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleEntry;
import rockstar.client.RockstarClient;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.util.ClientMessages;
import rockstar.client.MinecraftClientAccess;

public class SwingPresetConfig
implements MinecraftClientAccess {
    private final File internalField0148;
    private final String internalField0248;
    private final AnimatedValue internalField0808 = new AnimatedValue(300L, Easing.internalField1626);
    private final AnimatedValue internalField0809 = new AnimatedValue(300L, Easing.internalField1626);

    public SwingPresetConfig(String string) {
        this.internalField0248 = string;
        File file = new File(String.valueOf(ConfigManager.internalField0148) + "/presets", "swing");
        if (!file.exists()) {
            file.mkdir();
        }
        this.internalField0148 = new File(file, string + ".%s".formatted("rock"));
    }

    public void internalMethod03765() {
        if (!this.internalField0148.exists()) {
            RockstarClient.internalField0572.warn("Config file not found: {}", (Object)this.internalField0148.getAbsolutePath());
            return;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(this.internalField0148));){
            JsonObject jsonObject = JsonParser.parseReader((Reader)bufferedReader).getAsJsonObject();
            JsonObject jsonObject2 = jsonObject.getAsJsonObject("animation");
            for (Setting object2 : RockstarClient.getInstance().internalMethod00061().internalMethod04275().getSettings()) {
                if (!jsonObject2.has(object2.getName())) continue;
                object2.fromJson(jsonObject2.get(object2.getName()));
            }
            JsonObject jsonObject3 = jsonObject.getAsJsonObject("startPhase");
            for (Setting typedValue157 : RockstarClient.getInstance().internalMethod00061().internalMethod04274().getSettings()) {
                if (!jsonObject3.has(typedValue157.getName())) continue;
                typedValue157.fromJson(jsonObject3.get(typedValue157.getName()));
            }
            JsonObject jsonObject4 = jsonObject.getAsJsonObject("endPhase");
            for (Setting typedValue157 : RockstarClient.getInstance().internalMethod00061().internalMethod05639().getSettings()) {
                if (!jsonObject4.has(typedValue157.getName())) continue;
                typedValue157.fromJson(jsonObject4.get(typedValue157.getName()));
            }
            if (!this.internalField0248.equals("autosave")) {
                RockstarClient.getInstance().internalMethod01001().internalMethod03314(this);
            }
        }
        catch (Exception exception) {
            RockstarClient.internalField0572.error("Failed to load config file {}: {}", (Object)this.internalField0248, (Object)exception.getMessage());
        }
    }

    public void internalMethod03767() {
        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject jsonObject2 = new JsonObject();
            for (Setting object2 : RockstarClient.getInstance().internalMethod00061().internalMethod04275().getSettings()) {
                jsonObject2.add(object2.getName(), object2.toJson());
            }
            jsonObject.add("animation", (JsonElement)jsonObject2);
            JsonObject jsonObject3 = new JsonObject();
            for (Setting typedValue157 : RockstarClient.getInstance().internalMethod00061().internalMethod04274().getSettings()) {
                jsonObject3.add(typedValue157.getName(), typedValue157.toJson());
            }
            jsonObject.add("startPhase", (JsonElement)jsonObject3);
            JsonObject jsonObject4 = new JsonObject();
            for (Setting typedValue157 : RockstarClient.getInstance().internalMethod00061().internalMethod05639().getSettings()) {
                jsonObject4.add(typedValue157.getName(), typedValue157.toJson());
            }
            jsonObject.add("endPhase", (JsonElement)jsonObject4);
            ConfigManager.internalMethod01467(this.internalField0148, (JsonElement)jsonObject);
            System.out.println("saved");
            if (!this.internalField0248.equals("autosave")) {
                RockstarClient.getInstance().internalMethod01001().internalMethod03314(this);
            }
        }
        catch (IOException iOException) {
            RockstarClient.internalField0572.error("Failed to save config file", (Throwable)iOException);
        }
    }

    public void internalMethod07672() {
        Path path = this.internalField0148.toPath();
        try {
            Files.delete(path);
            RockstarClient.getInstance().internalMethod01001().internalMethod01720().remove(this);
            RockstarClient.internalField0572.info("Config file deleted: {}", (Object)path);
        }
        catch (NoSuchFileException noSuchFileException) {
            RockstarClient.internalField0572.warn("Tried to delete a file that does not exist: {}", (Object)path);
        }
        catch (IOException iOException) {
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("swing_anim.delete_error")));
            RockstarClient.internalField0572.warn("Failed to delete config file: {}. Reason: {}", (Object)path, (Object)iOException.getMessage());
        }
    }

    @Generated
    public File internalMethod03356() {
        return this.internalField0148;
    }

    @Generated
    public String internalMethod02141() {
        return this.internalField0248;
    }

    @Generated
    public AnimatedValue internalMethod06902() {
        return this.internalField0808;
    }

    @Generated
    public AnimatedValue internalMethod07599() {
        return this.internalField0809;
    }
}


package rockstar.client.internal.config;





import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.Generated;
import net.minecraft.text.Text;
import rockstar.client.internal.script.ConfigManager;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;
import rockstar.client.internal.config.SwingPresetConfig;

public class SwingPresetManager {
    private final List<SwingPresetConfig> internalField0416 = new ArrayList<SwingPresetConfig>();
    private SwingPresetConfig internalField0752;
    private boolean internalField0277 = false;

    public void internalMethod05545() {
        if (this.internalMethod01444() == null) {
            this.internalMethod07525("autosave");
        }
        if (!this.internalField0277) {
            this.internalMethod08697();
            this.internalField0277 = true;
        }
    }

    public void internalMethod05548() {
        String[] stringArray = new String[]{"explorer " + new File(String.valueOf(ConfigManager.internalField0148) + "/presets", "swing").getAbsolutePath()};
        try {
            Runtime.getRuntime().exec(stringArray);
        }
        catch (Exception exception) {
            RockstarClient.internalField0572.error("\u0432\u0441\u0435 \u043d\u0430\u0435\u0431\u043d\u0443\u043b\u043e\u0441\u044c \u0432 dir \u043a\u043e\u043d\u0444\u0438\u0433\u0435 {}", (Object)exception.getMessage());
        }
    }

    public void internalMethod07525(String string) {
        if (string == null) {
            return;
        }
        if (this.internalMethod04541(string, false) != null) {
            RockstarClient.internalField0572.warn("Preset {} already exists", (Object)string);
            return;
        }
        SwingPresetConfig typedValue117 = new SwingPresetConfig(string);
        if (string.equals("autosave")) {
            typedValue117.internalMethod03765();
        }
        typedValue117.internalMethod03767();
        this.internalField0416.add(typedValue117);
    }

    public void internalMethod08674() {
        ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("swing_anim.preset_list")));
        for (SwingPresetConfig typedValue117 : this.internalField0416) {
            int n = this.internalField0416.indexOf(typedValue117) + 1;
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("swing_anim.preset_item", n, typedValue117.internalMethod02141())));
        }
    }

    private void internalMethod08697() {
        this.internalField0416.clear();
        Path path2 = Paths.get(String.valueOf(ConfigManager.internalField0148) + "/presets", "swing");
        if (!Files.exists(path2, new LinkOption[0])) {
            try {
                Files.createDirectories(path2, new FileAttribute[0]);
                return;
            }
            catch (IOException iOException) {
                RockstarClient.internalField0572.error("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043e\u0437\u0434\u0430\u0442\u044c \u0434\u0438\u0440\u0435\u043a\u0442\u043e\u0440\u0438\u044e \u043f\u0440\u0435\u0441\u0435\u0442\u043e\u0432: {}", (Object)iOException.getMessage());
                return;
            }
        }
        try (Stream<Path> stream = Files.list(path2);){
            stream.filter(path -> Files.isRegularFile(path, new LinkOption[0])).filter(path -> path.toString().endsWith(".rock")).forEach(path -> {
                String string = path.getFileName().toString();
                String string2 = string.substring(0, string.lastIndexOf(46));
                SwingPresetConfig typedValue117 = new SwingPresetConfig(string2);
                this.internalField0416.add(typedValue117);
            });
        }
        catch (IOException iOException) {
            RockstarClient.internalField0572.error("\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u0441\u043a\u0430\u043d\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0438 \u0434\u0438\u0440\u0435\u043a\u0442\u043e\u0440\u0438\u0438 \u043a\u043e\u043d\u0444\u0438\u0433\u043e\u0432: {}", (Object)iOException.getMessage());
        }
    }

    public SwingPresetConfig internalMethod04541(String string, boolean bl) {
        if (bl) {
            this.internalMethod08697();
        }
        return this.internalField0416.stream().filter(typedValue117 -> typedValue117.internalMethod02141().equalsIgnoreCase(string)).findFirst().orElse(null);
    }

    public SwingPresetConfig internalMethod06262(String string) {
        return this.internalMethod04541(string, false);
    }

    public SwingPresetConfig internalMethod01444() {
        return this.internalMethod04541("autosave", true);
    }

    public void internalMethod08675() {
        this.internalMethod08697();
    }

    @Generated
    public List<SwingPresetConfig> internalMethod01720() {
        return this.internalField0416;
    }

    @Generated
    public SwingPresetConfig internalMethod02695() {
        return this.internalField0752;
    }

    @Generated
    public boolean internalMethod05546() {
        return this.internalField0277;
    }

    @Generated
    public void internalMethod03314(SwingPresetConfig typedValue117) {
        this.internalField0752 = typedValue117;
    }
}

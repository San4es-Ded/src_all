package rockstar.client.internal.script;




import rockstar.client.internal.config.*;
import rockstar.client.internal.auth.*;
import rockstar.client.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import rockstar.client.internal.config.AbstractClientConfig;
import rockstar.client.internal.auth.ClientDataConfig;
import rockstar.client.internal.config.StaffListConfig;
import rockstar.client.RockstarClient;

public class ConfigManager {
    public static final Gson internalField0931 = new GsonBuilder().setPrettyPrinting().create();
    public static final File internalField0148 = new File(MinecraftClient.getInstance().runDirectory, "Rockstar");
    public static final String internalField0248 = "rock";
    private final List<AbstractClientConfig> internalField0416 = new ArrayList<AbstractClientConfig>();

    public ConfigManager() {
        try {
            if (!internalField0148.exists()) {
                Files.createDirectories(Path.of(internalField0148.toURI()), new FileAttribute[0]);
            }
        }
        catch (IOException iOException) {
            System.err.println("Error creating directory: " + iOException.getMessage());
        }
    }

    public static void internalMethod01467(File file, JsonElement jsonElement) throws IOException {
        ConfigManager.internalMethod04682(file, internalField0931.toJson(jsonElement));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void internalMethod04682(File file, String string) throws IOException {
        Path path = file.toPath();
        Path path2 = path.getParent();
        if (path2 != null) {
            Files.createDirectories(path2, new FileAttribute[0]);
        }
        Path path3 = path2 == null ? Files.createTempFile(file.getName(), ".tmp", new FileAttribute[0]) : Files.createTempFile(path2, file.getName(), ".tmp", new FileAttribute[0]);
        try {
            Files.writeString(path3, (CharSequence)string, StandardCharsets.UTF_8, new OpenOption[0]);
            try {
                Files.move(path3, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            }
            catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
                Files.move(path3, path, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        finally {
            Files.deleteIfExists(path3);
        }
    }

    public void internalMethod05330() {
        this.internalField0416.add(new ClientDataConfig());
        this.internalField0416.add(new StaffListConfig());
        this.internalField0416.add(new AltConfig());
    }

    public AbstractClientConfig internalMethod01175(String string) {
        return this.internalField0416.stream().filter(typedValue138 -> typedValue138.internalMethod04830().internalMethod03654().equalsIgnoreCase(string)).findFirst().orElse(null);
    }

    public void internalMethod02147(AbstractClientConfig typedValue138) {
        try {
            if (typedValue138.internalMethod05023().exists()) {
                typedValue138.internalMethod07512();
            }
        }
        catch (Exception exception) {
            System.err.println("Error reading file: " + exception.getMessage());
        }
    }

    public void internalMethod06625(String string) {
        AbstractClientConfig typedValue138 = this.internalMethod01175(string);
        if (typedValue138 != null) {
            this.internalMethod02147(typedValue138);
        }
    }

    public void internalMethod02765(AbstractClientConfig typedValue138) {
        try {
            typedValue138.internalMethod07509();
        }
        catch (Exception exception) {
            System.err.println("Error saving file: " + exception.getMessage());
        }
    }

    public void internalMethod05165(String string) {
        AbstractClientConfig typedValue138 = this.internalMethod01175(string);
        if (typedValue138 != null) {
            if (typedValue138 instanceof ClientDataConfig && RockstarClient.getInstance().internalMethod03318() != null) {
                RockstarClient.getInstance().internalMethod03318().internalMethod08884();
            }
            this.internalMethod02765(typedValue138);
        }
    }

    public void internalMethod05336() {
        for (AbstractClientConfig typedValue138 : this.internalField0416) {
            this.internalMethod02147(typedValue138);
        }
    }

    public void internalMethod08923() {
        for (AbstractClientConfig typedValue138 : this.internalField0416) {
            this.internalMethod02765(typedValue138);
        }
    }

    @Generated
    public List<AbstractClientConfig> internalMethod04309() {
        return this.internalField0416;
    }
}

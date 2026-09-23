package rockstar.client.asset;


import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.network.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import rockstar.client.internal.script.ConfigManager;
import rockstar.client.RockstarClient;
import rockstar.client.internal.network.WebImageUtils;
import rockstar.client.MinecraftClientAccess;

public final class AssetTextureManager {
    private static final String internalField0248 = "image/mainmenu/fallback.jpg";
    private static final Map<String, Identifier> internalField0543 = new ConcurrentHashMap<String, Identifier>();
    private static final Map<String, Float> internalField0544 = new ConcurrentHashMap<String, Float>();
    private static final Set<String> internalField0546 = ConcurrentHashMap.newKeySet();
    private static final ExecutorService internalField0124 = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "Rockstar-AssetPack");
        thread.setDaemon(true);
        return thread;
    });
    private static volatile Identifier internalField0354;

    private AssetTextureManager() {
    }

    public static File internalMethod00045() {
        return new File(ConfigManager.internalField0148, "assets");
    }

    public static boolean internalMethod02970(String string) {
        if (internalField0543.containsKey(string)) {
            return true;
        }
        AssetTextureManager.internalMethod02604(string);
        return false;
    }

    public static Identifier internalMethod02604(String string) {
        Identifier identifier = internalField0543.get(string);
        if (identifier != null) {
            return identifier;
        }
        File file = AssetTextureManager.internalMethod06196(string);
        if (file != null && internalField0546.add(string)) {
            internalField0124.execute(() -> AssetTextureManager.internalMethod03903(string, file));
        }
        return AssetTextureManager.internalMethod01466();
    }

    public static float internalMethod02969(String string) {
        return internalField0544.getOrDefault(string, Float.valueOf(0.0f)).floatValue();
    }

    public static void internalMethod00192() {
        ArrayList<Identifier> arrayList = new ArrayList<Identifier>(internalField0543.values());
        internalField0543.clear();
        internalField0544.clear();
        internalField0546.clear();
        MinecraftClientAccess.internalField0149.execute(() -> arrayList.forEach(identifier -> MinecraftClientAccess.internalField0149.getTextureManager().destroyTexture(identifier)));
    }

    private static void internalMethod03903(String string, File file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            if (bufferedImage == null) {
                RockstarClient.internalField0572.warn("[Assets] \u043d\u0435 \u043f\u043e\u043d\u044f\u043b \u0444\u043e\u0440\u043c\u0430\u0442 {}", (Object)file);
                return;
            }
            AssetTextureManager.internalMethod07346(string, bufferedImage, RockstarClient.id("pack/" + string + ".png"));
        }
        catch (Throwable throwable) {
            RockstarClient.internalField0572.warn("[Assets] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c {}: {}", (Object)file, (Object)throwable.toString());
            internalField0546.remove(string);
        }
    }

    private static Identifier internalMethod01466() {
        Identifier identifier = internalField0354;
        if (identifier != null) {
            return identifier;
        }
        internalField0354 = identifier = RockstarClient.id("pack/fallback.png");
        try (InputStream inputStream = AssetTextureManager.class.getResourceAsStream("/assets/rockstar/image/mainmenu/fallback.jpg");){
            BufferedImage bufferedImage;
            if (inputStream != null && (bufferedImage = ImageIO.read(inputStream)) != null) {
                AssetTextureManager.internalMethod07346(internalField0248, bufferedImage, identifier);
            }
        }
        catch (Throwable throwable) {
            RockstarClient.internalField0572.warn("[Assets] \u0432\u0441\u0442\u0440\u043e\u0435\u043d\u043d\u0430\u044f \u0437\u0430\u0433\u043b\u0443\u0448\u043a\u0430 \u043d\u0435 \u043f\u0440\u043e\u0447\u0438\u0442\u0430\u043b\u0430\u0441\u044c: {}", (Object)throwable.toString());
        }
        return identifier;
    }

    private static void internalMethod07346(String string, BufferedImage bufferedImage, Identifier identifier) {
        NativeImage nativeImage = WebImageUtils.internalMethod04372(bufferedImage, false);
        float f = (float)bufferedImage.getWidth() / (float)Math.max(1, bufferedImage.getHeight());
        MinecraftClientAccess.internalField0149.execute(() -> {
            MinecraftClientAccess.internalField0149.getTextureManager().registerTexture(identifier, (AbstractTexture)new NativeImageBackedTexture(() -> "Rockstar asset", nativeImage));
            internalField0544.put(string, Float.valueOf(f));
            internalField0543.put(string, identifier);
            internalField0546.remove(string);
        });
    }

    private static File internalMethod06196(String string) {
        for (String string2 : new String[]{".jpg", ".png"}) {
            File file = new File(AssetTextureManager.internalMethod00045(), string + string2);
            if (!file.isFile()) continue;
            return file;
        }
        return null;
    }
}

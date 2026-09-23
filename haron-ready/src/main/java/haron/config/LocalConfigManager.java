package haron.config;

import haron.config.ConfigProfileEntry;
import haron.hud.core.HudElement;
import haron.hud.core.HudManager;
import haron.module.ModuleManager;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.KeybindSetting;
import haron.settings.Setting;
import haron.settings.SettingGroup;
import haron.settings.ModeSetting;
import haron.settings.ValidatedTextSetting;
import haron.settings.ItemHighlightSetting;
import haron.settings.BooleanSetting;
import java.awt.Color;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocalConfigManager {
    private static final Logger LOG;
    private static final LocalConfigManager INSTANCE;
    private static final String FOLDER_NAME;
    private static final String APPDATA_DIR;
    private static final String FILE_EXT;
    private static final String AUTOSAVE_FILE;
    private static final String DEFAULT_NAME;
    private static final int FORMAT_VER = 1;
    private static final long SAVE_DEBOUNCE_MS = 200L;
    private volatile boolean applying;
    private volatile ScheduledFuture<?> pendingSave;
    private final LinkedHashMap<String, String> profiles = new LinkedHashMap();
    private String activeProfile = "default";
    private final ScheduledExecutorService saveExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable, "haron-autosave");
        thread.setDaemon(true);
        return thread;
    });

    private void deserializeSettings(List<Setting<?>> list, JSONObject jSONObject) {
        for (Setting<?> mmdapc2 : list) {
            String string;
            if (mmdapc2 instanceof SettingGroup || !jSONObject.has(string = mmdapc2.f())) continue;
            try {
                Setting mmdapc3;
                JSONObject jSONObject2 = jSONObject.getJSONObject(string);
                if (mmdapc2 instanceof BooleanSetting) {
                    ((BooleanSetting)mmdapc2).set(jSONObject2.getBoolean("value"));
                    continue;
                }
                if (mmdapc2 instanceof NumberSetting) {
                    ((NumberSetting)mmdapc2).set((float)jSONObject2.getDouble("value"));
                    continue;
                }
                if (mmdapc2 instanceof ColorSetting) {
                    ((ColorSetting)mmdapc2).setHsb((float)jSONObject2.getDouble("hue"), (float)jSONObject2.getDouble("sat"), (float)jSONObject2.getDouble("bri"));
                    continue;
                }
                if (mmdapc2 instanceof KeybindSetting) {
                    ((KeybindSetting)mmdapc2).setKey(jSONObject2.getInt("value"));
                    continue;
                }
                if (mmdapc2 instanceof ModeSetting) {
                    mmdapc3 = (ModeSetting)mmdapc2;
                    ((ModeSetting)mmdapc3).select(jSONObject2.getInt("value"));
                    if (!((ModeSetting)mmdapc3).c() || !jSONObject2.has("selected")) continue;
                    JSONArray jSONArray = jSONObject2.getJSONArray("selected");
                    HashSet<Integer> hashSet = new HashSet<Integer>();
                    for (int i = 0; i < jSONArray.length(); ++i) {
                        hashSet.add(jSONArray.getInt(i));
                    }
                    ((ModeSetting)mmdapc3).setSelectedIndices(hashSet);
                    continue;
                }
                if (mmdapc2 instanceof ValidatedTextSetting) {
                    ((ValidatedTextSetting)mmdapc2).set(jSONObject2.getString("value"));
                    continue;
                }
                if (!(mmdapc2 instanceof ItemHighlightSetting)) continue;
                mmdapc3 = (ItemHighlightSetting)mmdapc2;
                if (jSONObject2.has("enabled")) {
                    ((ItemHighlightSetting)mmdapc3).set(jSONObject2.getBoolean("enabled"));
                }
                if (!jSONObject2.has("color")) continue;
                ((ItemHighlightSetting)mmdapc3).setColor(new Color(jSONObject2.getInt("color"), true));
            }
            catch (Exception exception) {
                LOG.debug("[Haron] Failed to apply setting '{}'", (Object)string, (Object)exception);
            }
        }
    }

    private void serializeHudElement(JSONObject jSONObject, String string, HudElement hylpge2) {
        if (hylpge2 == null) {
            return;
        }
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("x", hylpge2.t().a());
        jSONObject2.put("y", hylpge2.t().b());
        jSONObject2.put("scale", hylpge2.g());
        jSONObject.put(string, (Object)jSONObject2);
    }

    private void deserializeModules(JSONObject jSONObject) {
        for (HaronModule jxs16t2 : ModuleManager.all()) {
            if (!jSONObject.has(jxs16t2.name())) continue;
            try {
                JSONObject jSONObject2 = jSONObject.getJSONObject(jxs16t2.name());
                if (jSONObject2.has("enabled")) {
                    jxs16t2.setEnabledStateQuiet(jSONObject2.getBoolean("enabled"));
                }
                if (jSONObject2.has("bindKey")) {
                    jxs16t2.setBindKeyQuiet(jSONObject2.getInt("bindKey"));
                }
                if (!jSONObject2.has("settings")) continue;
                this.deserializeSettings(jxs16t2.settings(), jSONObject2.getJSONObject("settings"));
            }
            catch (Exception exception) {}
        }
    }

    private void deserializeHud(JSONObject jSONObject) {
        HudManager kxdl862 = HudManager.a();
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        float f = (float)minecraftClient.getWindow().getFramebufferWidth() / 2.0f;
        float f2 = (float)minecraftClient.getWindow().getFramebufferHeight() / 2.0f;
        this.applyHudElement(jSONObject, "potions", kxdl862.i(), f, f2);
        this.applyHudElement(jSONObject, "hotkeys", kxdl862.j(), f, f2);
        this.applyHudElement(jSONObject, "cooldowns", kxdl862.k(), f, f2);
        this.applyHudElement(jSONObject, "target", kxdl862.l(), f, f2);
        kxdl862.d();
    }

    private JSONObject serializeHud() {
        JSONObject jSONObject = new JSONObject();
        HudManager kxdl862 = HudManager.a();
        this.serializeHudElement(jSONObject, "potions", kxdl862.i());
        this.serializeHudElement(jSONObject, "hotkeys", kxdl862.j());
        this.serializeHudElement(jSONObject, "cooldowns", kxdl862.k());
        this.serializeHudElement(jSONObject, "target", kxdl862.l());
        return jSONObject;
    }

    public void renameProfile(String string, String string2, Runnable runnable, Consumer<String> consumer) {
        block8: {
            String string3 = LocalConfigManager.sanitizeName(string2);
            if (!this.profiles.containsKey(string)) {
                if (consumer != null) {
                    consumer.accept(LocalConfigManager.$sf$2(string));
                    return;
                }
                return;
            }
            if (this.profiles.containsKey(string3)) {
                if (consumer != null) {
                    consumer.accept(LocalConfigManager.$sf$1(string3));
                    return;
                }
                return;
            }
            String string4 = (String)this.profiles.remove(string);
            this.profiles.put(string3, string4);
            this.deleteFromDisk(string);
            try {
                this.writeToDisk(string3, string4);
                if (this.activeProfile.equals(string)) {
                    this.activeProfile = string3;
                }
                if (runnable != null) {
                    runnable.run();
                }
            }
            catch (IOException iOException) {
                LOG.warn("[Haron] Cannot rename profile {} -> {}", (Object)string, (Object)string3, (Object)iOException);
                if (consumer == null) break block8;
                consumer.accept(iOException.getMessage());
            }
        }
    }

    private JSONObject serializeSettings(List<Setting<?>> list) {
        JSONObject jSONObject = new JSONObject();
        for (Setting<?> mmdapc2 : list) {
            if (mmdapc2 instanceof SettingGroup) continue;
            String string = mmdapc2.f();
            try {
                JSONObject jSONObject2;
                Setting mmdapc3;
                if (mmdapc2 instanceof BooleanSetting) {
                    mmdapc3 = (BooleanSetting)mmdapc2;
                    jSONObject2 = new JSONObject();
                    jSONObject2.put("type", (Object)"boolean");
                    jSONObject2.put("value", ((BooleanSetting)mmdapc3).get());
                    jSONObject.put(string, (Object)jSONObject2);
                    continue;
                }
                if (mmdapc2 instanceof NumberSetting) {
                    mmdapc3 = (NumberSetting)mmdapc2;
                    jSONObject2 = new JSONObject();
                    jSONObject2.put("type", (Object)"slider");
                    jSONObject2.put("value", ((NumberSetting)mmdapc3).get());
                    jSONObject.put(string, (Object)jSONObject2);
                    continue;
                }
                if (mmdapc2 instanceof ColorSetting) {
                    mmdapc3 = (ColorSetting)mmdapc2;
                    jSONObject2 = new JSONObject();
                    jSONObject2.put("type", (Object)"color");
                    jSONObject2.put("hue", ((ColorSetting)mmdapc3).c());
                    jSONObject2.put("sat", ((ColorSetting)mmdapc3).d());
                    jSONObject2.put("bri", ((ColorSetting)mmdapc3).e());
                    jSONObject.put(string, (Object)jSONObject2);
                    continue;
                }
                if (mmdapc2 instanceof KeybindSetting) {
                    mmdapc3 = (KeybindSetting)mmdapc2;
                    jSONObject2 = new JSONObject();
                    jSONObject2.put("type", (Object)"key");
                    jSONObject2.put("value", ((KeybindSetting)mmdapc3).a());
                    jSONObject.put(string, (Object)jSONObject2);
                    continue;
                }
                if (mmdapc2 instanceof ModeSetting) {
                    mmdapc3 = (ModeSetting)mmdapc2;
                    jSONObject2 = new JSONObject();
                    jSONObject2.put("type", (Object)"mode");
                    jSONObject2.put("value", mmdapc3.k());
                    if (((ModeSetting)mmdapc3).c()) {
                        JSONArray selected = new JSONArray();
                        Iterator<Integer> iterator = ((ModeSetting)mmdapc3).e().iterator();
                        while (iterator.hasNext()) {
                            selected.put(iterator.next().intValue());
                        }
                        jSONObject2.put("selected", selected);
                    }
                    jSONObject.put(string, (Object)jSONObject2);
                    continue;
                }
                if (mmdapc2 instanceof ValidatedTextSetting) {
                    mmdapc3 = (ValidatedTextSetting)mmdapc2;
                    jSONObject2 = new JSONObject();
                    jSONObject2.put("type", (Object)"token");
                    jSONObject2.put("value", (Object)((ValidatedTextSetting)mmdapc3).a());
                    jSONObject.put(string, (Object)jSONObject2);
                    continue;
                }
                if (!(mmdapc2 instanceof ItemHighlightSetting)) continue;
                mmdapc3 = (ItemHighlightSetting)mmdapc2;
                jSONObject2 = new JSONObject();
                jSONObject2.put("type", (Object)"itemtoggle");
                jSONObject2.put("enabled", ((ItemHighlightSetting)mmdapc3).get());
                Color color = ((ItemHighlightSetting)mmdapc3).d();
                jSONObject2.put("color", color != null ? color.getRGB() : -1);
                jSONObject.put(string, (Object)jSONObject2);
            }
            catch (Exception exception) {}
        }
        return jSONObject;
    }

    private void applyHudElement(JSONObject jSONObject, String string, HudElement hylpge2, float f, float f2) {
        if (hylpge2 == null || !jSONObject.has(string)) {
            return;
        }
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject(string);
            float f3 = (float)jSONObject2.getDouble("x");
            float f4 = (float)jSONObject2.getDouble("y");
            hylpge2.t().a(f3);
            hylpge2.t().b(f4);
            hylpge2.b();
            if (f > 1.0f && f2 > 1.0f) {
                hylpge2.a(hylpge2.t().a(hylpge2.n(), f));
                hylpge2.b(hylpge2.t().b(hylpge2.o(), f2));
                hylpge2.b(f, f2);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private Path getLegacyConfigDir() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null) {
            return null;
        }
        return minecraftClient.runDirectory.toPath().resolve("config").resolve("haron");
    }

    private static void writeUtf8File(Path path, String string) throws IOException {
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).encode(CharBuffer.wrap(string));
        byte[] byArray = new byte[byteBuffer.remaining()];
        byteBuffer.get(byArray);
        Files.write(path, byArray, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    public List<ConfigProfileEntry> toConfigEntries() {
        ArrayList<ConfigProfileEntry> arrayList = new ArrayList<ConfigProfileEntry>();
        for (Map.Entry<String, String> entry : this.profiles.entrySet()) {
            arrayList.add(new ConfigProfileEntry(entry.getKey(), entry.getValue()));
        }
        return arrayList;
    }

    private Path writeAutosave(String string) throws IOException {
        Path path = this.getConfigDir();
        Files.createDirectories(path, new FileAttribute[0]);
        Path path2 = path.resolve("haron.cfg");
        LocalConfigManager.writeUtf8File(path2, string);
        return path2;
    }

    private void writeStateToDisk() {
        if (this.applying) {
            LOG.debug("[Haron] writeStateToDisk skipped (applying)");
            return;
        }
        try {
            String string = this.serializeState();
            this.profiles.put(this.activeProfile, string);
            Path path = this.writeAutosave(string);
            Path path2 = this.writeToDisk(this.activeProfile, string);
            if (path == null || !Files.exists(path, new LinkOption[0])) {
                LOG.error("[Haron] haron.cfg was NOT written (check permissions)ῗ {}", (Object)this.getAutosavePath());
                return;
            }
            LOG.info("[Haron] Verified on disk: {} ({} bytes)", (Object)path, (Object)Files.size(path));
            if (path2 != null) {
                LOG.debug("[Haron] Profile copy: {}", (Object)path2);
            }
            this.logSaveSummary(string);
        }
        catch (Exception exception) {
            LOG.warn("[Haron] Failed to save config", (Throwable)exception);
        }
    }

    public void saveCurrentState() {
        this.requestSave("saveCurrentState");
    }

    private String serializeState() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("version", 1);
        JSONObject jSONObject2 = new JSONObject();
        for (HaronModule jxs16t2 : ModuleManager.all()) {
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("enabled", jxs16t2.isEnabled());
            jSONObject3.put("bindKey", jxs16t2.bindKey());
            jSONObject3.put("settings", (Object)this.serializeSettings(jxs16t2.settings()));
            jSONObject2.put(jxs16t2.name(), (Object)jSONObject3);
        }
        jSONObject.put("modules", (Object)jSONObject2);
        jSONObject.put("hud", (Object)this.serializeHud());
        return jSONObject.toString(2);
    }

    private Path writeToDisk(String string, String string2) throws IOException {
        Path path = this.getConfigDir();
        Files.createDirectories(path, new FileAttribute[0]);
        Path path2 = path.resolve(LocalConfigManager.$sf$0(LocalConfigManager.sanitizeName(string)));
        LocalConfigManager.writeUtf8File(path2, string2);
        return path2;
    }

    private Path getAutosavePath() {
        return this.getConfigDir().resolve("haron.cfg");
    }

    private void logSaveSummary(String string) {
        try {
            JSONObject jSONObject = new JSONObject(string);
            int n = jSONObject.optJSONObject("modules") != null ? jSONObject.getJSONObject("modules").length() : 0;
            int n2 = 0;
            if (jSONObject.has("modules")) {
                JSONObject jSONObject2 = jSONObject.getJSONObject("modules");
                Iterator iterator = jSONObject2.keySet().iterator();
                while (iterator.hasNext()) {
                    if (!jSONObject2.getJSONObject((String)iterator.next()).optBoolean("enabled", false)) continue;
                    ++n2;
                }
            }
            LOG.info("[Haron] Saved {} ({} bytes, {} modules, {} enabled, {} hud) -> {}", (Object)"haron.cfg", (Object)string.length(), (Object)n, (Object)n2, (Object)(jSONObject.optJSONObject("hud") != null ? jSONObject.getJSONObject("hud").length() : 0), (Object)this.getAutosavePath());
        }
        catch (Exception exception) {
            LOG.info("[Haron] Saved {} -> {}", (Object)"haron.cfg", (Object)this.getAutosavePath());
        }
    }

    public Set<String> profileNames() {
        return Collections.unmodifiableSet(this.profiles.keySet());
    }

    public void createProfile(String string, Runnable runnable, Consumer<String> consumer) {
        block7: {
            String string2 = LocalConfigManager.sanitizeName(string);
            if (string2.isEmpty()) {
                if (consumer != null) {
                    consumer.accept("Имя профиля пустое");
                    return;
                }
                return;
            }
            if (this.profiles.containsKey(string2)) {
                if (consumer != null) {
                    consumer.accept(LocalConfigManager.$sf$1(string2));
                    return;
                }
                return;
            }
            String string3 = this.serializeState();
            this.profiles.put(string2, string3);
            try {
                this.writeToDisk(string2, string3);
                if (runnable != null) {
                    runnable.run();
                }
            }
            catch (IOException iOException) {
                LOG.warn("[Haron] Cannot create profile {}", (Object)string2, (Object)iOException);
                if (consumer == null) break block7;
                consumer.accept(iOException.getMessage());
            }
        }
    }

    private static String sanitizeName(String string) {
        return string == null ? "" : string.trim().replaceAll("[/\\\\:*?\"<>| ]", "_");
    }

    public void deleteProfile(String string, Runnable runnable, Consumer<String> consumer) {
        if (!this.profiles.containsKey(string)) {
            if (consumer != null) {
                consumer.accept(LocalConfigManager.$sf$2(string));
                return;
            }
            return;
        }
        this.profiles.remove(string);
        this.deleteFromDisk(string);
        if (this.activeProfile.equals(string)) {
            String string2 = this.activeProfile = this.profiles.isEmpty() ? "default" : this.profiles.keySet().iterator().next();
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    private void deleteFromDisk(String string) {
        try {
            Files.deleteIfExists(this.getConfigDir().resolve(LocalConfigManager.$sf$0(LocalConfigManager.sanitizeName(string))));
        }
        catch (IOException iOException) {
            LOG.warn("[Haron] Cannot delete profile {}", (Object)string, (Object)iOException);
        }
    }

    private void logLoadSummary(String string, String string2) {
        try {
            JSONObject jSONObject = new JSONObject(string2);
            LOG.info("[Haron] Loaded {} ({} bytes, {} modules, {} hud)", (Object)string, (Object)string2.length(), (Object)(jSONObject.optJSONObject("modules") != null ? jSONObject.getJSONObject("modules").length() : 0), (Object)(jSONObject.optJSONObject("hud") != null ? jSONObject.getJSONObject("hud").length() : 0));
        }
        catch (Exception exception) {
            LOG.info("[Haron] Loaded {} ({} bytes)", (Object)string, (Object)(string2 != null ? string2.length() : 0));
        }
    }

    private void loadAllFromDisk() {
        this.profiles.clear();
        try {
            Path path = this.getConfigDir();
            Files.createDirectories(path, new FileAttribute[0]);
            if (!Files.exists(path, new LinkOption[0])) {
                return;
            }
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, "*.json");
            for (Path path2 : directoryStream) {
                String string = path2.getFileName().toString();
                String string2 = string.substring(0, string.length() - ".json".length());
                try {
                    this.profiles.put(string2, Files.readString(path2, StandardCharsets.UTF_8));
                }
                catch (IOException iOException) {
                    LOG.warn("[Haron] Cannot read profᾅle {}", (Object)path2, (Object)iOException);
                }
            }
            if (directoryStream != null) {
                directoryStream.close();
            }
        }
        catch (IOException iOException) {
            LOG.warn("[Haron] Cannot list config dir {}", (Object)this.getConfigDir(), (Object)iOException);
        }
        if (this.profiles.containsKey("default")) {
            return;
        }
        this.profiles.put("default", "{}");
    }

    private String readAutosave() {
        try {
            Path path = this.getAutosavePath();
            if (Files.exists(path, new LinkOption[0])) {
                return Files.readString(path, StandardCharsets.UTF_8);
            }
            return null;
        }
        catch (IOException iOException) {
            LOG.warn("[Haron] Cannot read {}", (Object)this.getAutosavePath(), (Object)iOException);
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void flushSaveNow(String string) {
        LocalConfigManager xdc2i72 = this;
        synchronized (xdc2i72) {
            if (this.pendingSave != null) {
                this.pendingSave.cancel(false);
                this.pendingSave = null;
            }
        }
        if (this.applying) {
            LOG.debug("[Haron] flushSaveNow skipped (applying): {}", (Object)string);
        } else {
            LOG.debug("[Haron] flushSaveNow: {}", (Object)string);
            this.writeStateToDisk();
        }
    }

    public String configDirectory() {
        return this.getConfigDir().toAbsolutePath().toString();
    }

    private void applyProfile(String string) {
        this.applyJson(this.profiles.get(string));
    }

    public void loadProfile(String string, Runnable runnable, Consumer<String> consumer) {
        if (!this.profiles.containsKey(string)) {
            if (consumer != null) {
                consumer.accept(LocalConfigManager.$sf$2(string));
                return;
            }
            return;
        }
        this.applying = true;
        try {
            this.applyProfile(string);
            this.activeProfile = string;
            this.applying = false;
            this.flushSaveNow(LocalConfigManager.$sf$3(string));
            if (runnable != null) {
                runnable.run();
            }
        }
        catch (Throwable throwable) {
            this.applying = false;
            throw throwable;
        }
    }

    public String getProfileData(String string) {
        return this.profiles.getOrDefault(string, "{}");
    }

    private void applyJson(String string) {
        if (string == null || string.isEmpty()) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(string);
            if (jSONObject.has("modules")) {
                this.deserializeModules(jSONObject.getJSONObject("modules"));
            }
            if (jSONObject.has("hud")) {
                this.deserializeHud(jSONObject.getJSONObject("hud"));
            }
        }
        catch (JSONException jSONException) {
            LOG.warn("[HἏᾜon] Failed to parse config JSON", (Throwable)jSONException);
        }
    }

    public boolean isApplying() {
        return this.applying;
    }

    private void flushSave() {
        if (this.applying) {
            LOG.debug("[Haroᾀ] ᾈlushSave skippeᾈ῍(aἝplying)");
        } else {
            this.writeStateToDisk();
        }
    }

    public void requestSave() {
        this.requestSave("unspecifieἋ");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void requestSave(String string) {
        if (this.applying) {
            LOG.debug("[Haron] Autosave skipped (applying): {}", (Object)string);
            return;
        }
        LOG.debug("[Haron] Autosave scheduled in {}ms: {}", (Object)200L, (Object)string);
        LocalConfigManager xdc2i72 = this;
        synchronized (xdc2i72) {
            if (this.pendingSave != null) {
                this.pendingSave.cancel(false);
            }
            this.pendingSave = this.saveExecutor.schedule(this::flushSave, 200L, TimeUnit.MILLISECONDS);
        }
    }

    public String activeProfile() {
        return this.activeProfile;
    }

    private void migrateLegacyConfigIfNeeded() {
        Path path = this.getLegacyConfigDir();
        Path path2 = this.getConfigDir();
        if (path == null || !Files.isDirectory(path, new LinkOption[0])) {
            return;
        }
        try {
            Files.createDirectories(path2, new FileAttribute[0]);
            Path path3 = path.resolve("haron.cfg");
            Path path4 = path2.resolve("haron.cfg");
            if (Files.exists(path3, new LinkOption[0]) && !Files.exists(path4, new LinkOption[0])) {
                Files.copy(path3, path4, StandardCopyOption.REPLACE_EXISTING);
                LOG.info("[Haron] Migrated {} -> {}", (Object)path3, (Object)path4);
            }
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, "*.json");
            for (Path path5 : directoryStream) {
                Path path6 = path2.resolve(path5.getFileName());
                if (Files.exists(path6, new LinkOption[0])) continue;
                Files.copy(path5, path6, StandardCopyOption.REPLACE_EXISTING);
                LOG.info("[Haron] Mἄgrated profile {}", (Object)path5.getFileName());
            }
            if (directoryStream != null) {
                directoryStream.close();
            }
        }
        catch (IOException iOException) {
            LOG.warn("[Haron] Legacy migration from {} failed", (Object)path, (Object)iOException);
        }
    }

    private LocalConfigManager() {
    }

    static {
        FOLDER_NAME = "haron";
        APPDATA_DIR = "haron";
        FILE_EXT = ".json";
        AUTOSAVE_FILE = "haron.cfg";
        DEFAULT_NAME = "default";
        LOG = LogManager.getLogger((String)"haron/config");
        INSTANCE = new LocalConfigManager();
    }

    public static LocalConfigManager get() {
        int n = 373;
        return INSTANCE;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void init() {
        this.migrateLegacyConfigIfNeeded();
        LOG.info("[Haron] init() — config dir: {}", (Object)this.configDirectory());
        this.applying = true;
        boolean bl = false;
        try {
            this.loadAllFromDisk();
            LOG.info("[Haron] Profiles on disk: {}", this.profiles.keySet());
            String string = this.readAutosave();
            if (string != null && !string.isBlank()) {
                this.applyJson(string);
                this.profiles.put(this.activeProfile, string);
                bl = true;
                this.logLoadSummary("haron.cfg", string);
            } else if (this.profiles.isEmpty()) {
                LOG.info("[Haron] No config files found, using defaults");
            } else {
                String string2 = this.profiles.containsKey(this.activeProfile) ? this.activeProfile : this.profiles.keySet().iterator().next();
                this.applyProfile(string2);
                this.logLoadSummary(LocalConfigManager.$sf$0(string2), this.profiles.get(string2));
            }
            if (bl) {
                return;
            }
            LOG.info("[Haron] Creating initial {}", (Object)"haron.cfg");
            this.flushSaveNow("init-migrate");
        }
        finally {
            this.applying = false;
        }
    }

    private static /* synthetic */ String $sf$0(String string) {
        return string + ".json";
    }

    private static /* synthetic */ String $sf$3(String string) {
        return "loᾍdProfile-" + string;
    }

    private static /* synthetic */ String $sf$1(String string) {
        return "Профиль '" + string + "' уже существует";
    }

    private static /* synthetic */ String $sf$2(String string) {
        return "Профиль '" + string + "' не найден";
    }

    private Path getConfigDir() {
        String string = System.getenv("APPDATA");
        if (string != null && !string.isBlank()) {
            return Path.of(string, "haron", "config");
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        return minecraftClient != null ? minecraftClient.runDirectory.toPath().resolve("config").resolve("haron") : Path.of(System.getProperty("user.home"), "haron", "config");
    }
}

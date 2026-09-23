package haron.modules.utilities;

import haron.markers.MarkerIcon;
import haron.markers.MarkerRegistry;
import haron.markers.Waypoint;
import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public final class GpsMarkerStorage {
    private static final String APPDATA_DIR = "Pulse";
    private static final String GPS_FILE = "gps.cfg";
    private static final int FORMAT_VERSION = 1;
    private static final long DEBOUNCE_MS = 200L;
    private volatile ScheduledFuture<?> pending;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable, "pulse-gps-save");
        thread.setDaemon(true);
        return thread;
    });
    private static final Logger LOG = LogManager.getLogger((String)"haron/gps");
    private static final GpsMarkerStorage INSTANCE = new GpsMarkerStorage();
    private static final Color DEFAULT_COLOR = new Color(80, 220, 100);

    private static void writeFile(Path path, String string) throws java.io.IOException {
        Files.writeString(path, (CharSequence)string, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    private static String buildJson(List<Waypoint> list) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("version", 1);
        JSONArray jSONArray = new JSONArray();
        for (Waypoint yo0tnu2 : list) {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("name", (Object)yo0tnu2.a());
            jSONObject2.put("x", yo0tnu2.b());
            jSONObject2.put("y", yo0tnu2.c());
            jSONObject2.put("z", yo0tnu2.d());
            jSONObject2.put("color", yo0tnu2.e().getRGB());
            jSONArray.put((Object)jSONObject2);
        }
        jSONObject.put("markers", (Object)jSONArray);
        return jSONObject.toString(2);
    }

    private void doSave() {
        try {
            List<Waypoint> list = GpsMarkerStorage.localMarkers();
            Path path = GpsMarkerStorage.getFilePath();
            Files.createDirectories(path.getParent(), new FileAttribute[0]);
            GpsMarkerStorage.writeFile(path, GpsMarkerStorage.buildJson(list));
            LOG.debug("[GPS] Saved {} markers -> {}", (Object)list.size(), (Object)path);
        }
        catch (Exception exception) {
            LOG.warn("[GPS] Save failed", (Throwable)exception);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void flushNow() {
        GpsMarkerStorage ibatt92 = this;
        synchronized (ibatt92) {
            if (this.pending != null) {
                this.pending.cancel(false);
                this.pending = null;
            }
        }
        this.doSave();
    }

    private static Path getFilePath() {
        String string = System.getenv("APPDATA");
        if (string != null && !string.isBlank()) {
            return Path.of(string, "Pulse", "config", "gps.cfg");
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        return minecraftClient != null ? minecraftClient.runDirectory.toPath().resolve("config").resolve("haron").resolve("gps.cfg") : Path.of(System.getProperty("user.home"), "Pulse", "config", "gps.cfg");
    }

    private static List<Waypoint> localMarkers() {
        ArrayList<Waypoint> arrayList = new ArrayList<Waypoint>();
        for (Waypoint yo0tnu2 : MarkerRegistry.a()) {
            if (yo0tnu2.j()) continue;
            arrayList.add(yo0tnu2);
        }
        return arrayList;
    }

    private GpsMarkerStorage() {
    }

    public static GpsMarkerStorage get() {
        return INSTANCE;
    }

    private void load(Path path) {
        try {
            JSONArray jSONArray;
            String string = Files.readString(path, StandardCharsets.UTF_8).trim();
            if (string.isEmpty() || (jSONArray = new JSONObject(string).optJSONArray("markers")) == null) {
                return;
            }
            for (int i = 0; i < jSONArray.length(); ++i) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                MarkerRegistry.a(new Waypoint(jSONObject.optString("name", GpsMarkerStorage.$sf$0(i)), jSONObject.optInt("x", 0), jSONObject.optInt("y", 64), jSONObject.optInt("z", 0), jSONObject.has("color") ? new Color(jSONObject.getInt("color"), true) : DEFAULT_COLOR, MarkerIcon.HOME));
            }
            LOG.info("[GPS] Loaded {} markers from {}", (Object)jSONArray.length(), (Object)path);
        }
        catch (Exception exception) {
            LOG.warn("[GPS] Load failed: {}", (Object)path, (Object)exception);
        }
    }

    public void init() {
        try {
            Path path = GpsMarkerStorage.getFilePath();
            Files.createDirectories(path.getParent(), new FileAttribute[0]);
            if (Files.exists(path, new LinkOption[0])) {
                this.load(path);
            } else {
                GpsMarkerStorage.writeFile(path, GpsMarkerStorage.buildJson(List.of()));
                LOG.info("[GPS] Created default {}", (Object)path);
            }
        }
        catch (Exception exception) {
            LOG.warn("[GPS] init() failed", (Throwable)exception);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void save() {
        GpsMarkerStorage ibatt92 = this;
        synchronized (ibatt92) {
            if (this.pending != null) {
                this.pending.cancel(false);
            }
            this.pending = this.executor.schedule(this::doSave, 200L, TimeUnit.MILLISECONDS);
        }
    }

    private static /* synthetic */ String $sf$0(int n) {
        return "GPS " + n;
    }
}

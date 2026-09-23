package haron.config;

import haron.config.ConfigMetadata;
import haron.config.ClientColorConfig;
import haron.config.SettingConfig;
import haron.config.ModuleConfig;
import haron.config.FriendConfig;
import haron.config.MarkerConfig;
import haron.config.HudPositionConfig;
import haron.config.NotificationVisibilityConfig;
import haron.config.MarkerSettingsConfig;
import haron.config.HaronConfigSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConfigJsonCodec {
    private static final int c = 2;
    public static int a;
    public static boolean b;

    private ConfigJsonCodec() {
    }

    private static SettingConfig e(JSONObject jSONObject) {
        SettingConfig bw3xj62 = new SettingConfig();
        String string = jSONObject.optString("type", "unknown");
        bw3xj62.a(string);
        int n = -1;
        switch (string.hashCode()) {
            case -1034364087: {
                break;
            }
            case 106079: {
                if (!string.equals("key")) break;
                n = 2;
                break;
            }
            case 0x3339A3: {
                if (!string.equals("mode")) break;
                break;
            }
            case 64711720: {
                break;
            }
            case 94842723: {
                break;
            }
            case 100358090: {
                break;
            }
        }
        switch (n) {
            case 0: {
                bw3xj62.a(jSONObject.optBoolean("value", false));
                break;
            }
            case 1: {
                bw3xj62.a(Float.valueOf((float)jSONObject.optDouble("value", 0.0)));
                break;
            }
            case 2: {
                bw3xj62.a(jSONObject.optInt("value", -1));
                break;
            }
            case 3: {
                bw3xj62.a((Object)jSONObject.optString("value", ""));
                break;
            }
            case 4: {
                bw3xj62.a(Float.valueOf((float)jSONObject.optDouble("hue", 0.0)));
                bw3xj62.b(Float.valueOf((float)jSONObject.optDouble("saturation", 1.0)));
                bw3xj62.c(Float.valueOf((float)jSONObject.optDouble("brightness", 1.0)));
                break;
            }
            case 5: {
                bw3xj62.a(jSONObject.optInt("value", 0));
                JSONArray jSONArray = jSONObject.optJSONArray("selectedIndices");
                if (jSONArray == null) break;
                int[] nArray = new int[jSONArray.length()];
                for (int i = 0; i < jSONArray.length(); ++i) {
                    nArray[i] = jSONArray.optInt(i, 0);
                }
                bw3xj62.a(nArray);
                break;
            }
            case 6: {
                bw3xj62.a(jSONObject.optBoolean("enabled", true));
                bw3xj62.a(jSONObject.optInt("colorRgb", -1));
            }
        }
        return bw3xj62;
    }

    private static ClientColorConfig i(JSONObject jSONObject) {
        ClientColorConfig b617vo2 = new ClientColorConfig();
        if (jSONObject == null) {
            return b617vo2;
        }
        b617vo2.a(jSONObject.optString("mode", "Статичный"));
        b617vo2.a((float)jSONObject.optDouble("staticHue", 0.75));
        b617vo2.b((float)jSONObject.optDouble("staticSaturation", 0.81));
        b617vo2.c((float)jSONObject.optDouble("staticBrightness", 0.89));
        b617vo2.d((float)jSONObject.optDouble("gradient1Hue", 0.0));
        b617vo2.e((float)jSONObject.optDouble("gradient1Saturation", 0.0));
        b617vo2.f((float)jSONObject.optDouble("gradient1Brightness", 1.0));
        b617vo2.g((float)jSONObject.optDouble("gradient2Hue", 0.58));
        b617vo2.h((float)jSONObject.optDouble("gradient2Saturation", 0.51));
        b617vo2.i((float)jSONObject.optDouble("gradient2Brightness", 0.96));
        return b617vo2;
    }

    private static JSONArray b(List<MarkerConfig> list) {
        JSONArray jSONArray = new JSONArray();
        for (MarkerConfig nafzlb2 : list) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("name", (Object)nafzlb2.a());
            jSONObject.put("x", nafzlb2.b());
            jSONObject.put("y", nafzlb2.c());
            jSONObject.put("z", nafzlb2.d());
            jSONObject.put("colorRgb", nafzlb2.e());
            jSONObject.put("iconType", (Object)nafzlb2.f());
            jSONObject.put("autoCreated", nafzlb2.g());
            jSONObject.put("creationTime", nafzlb2.h());
            jSONObject.put("serverNumber", nafzlb2.i());
            jSONObject.put("expirationTime", nafzlb2.j());
            jSONArray.put((Object)jSONObject);
        }
        return jSONArray;
    }

    private static List<MarkerConfig> b(JSONArray jSONArray) {
        ArrayList<MarkerConfig> arrayList = new ArrayList<MarkerConfig>();
        if (jSONArray == null) {
            return arrayList;
        }
        for (int i = 0; i < jSONArray.length(); ++i) {
            JSONObject jSONObject = jSONArray.optJSONObject(i);
            if (jSONObject == null) continue;
            MarkerConfig nafzlb2 = new MarkerConfig();
            nafzlb2.a(jSONObject.optString("name", ""));
            nafzlb2.a(jSONObject.optInt("x", 0));
            nafzlb2.b(jSONObject.optInt("y", 0));
            nafzlb2.c(jSONObject.optInt("z", 0));
            nafzlb2.d(jSONObject.optInt("colorRgb", -1));
            nafzlb2.b(jSONObject.optString("iconType", "HOME"));
            nafzlb2.a(jSONObject.optBoolean("autoCreated", false));
            nafzlb2.a(jSONObject.optLong("creationTime", 0L));
            nafzlb2.e(jSONObject.optInt("serverNumber", -1));
            nafzlb2.b(jSONObject.optLong("expirationTime", 0L));
            arrayList.add(nafzlb2);
        }
        return arrayList;
    }

    private static Map<String, ModuleConfig> b(JSONObject jSONObject) {
        HashMap<String, ModuleConfig> hashMap = new HashMap<String, ModuleConfig>();
        if (jSONObject == null) {
            return hashMap;
        }
        for (String string : jSONObject.keySet()) {
            JSONObject jSONObject2 = jSONObject.optJSONObject(string);
            if (jSONObject2 == null) continue;
            hashMap.put(string, ConfigJsonCodec.c(jSONObject2));
        }
        return hashMap;
    }

    private static JSONObject b(Map<String, SettingConfig> map) {
        JSONObject jSONObject = new JSONObject();
        for (Map.Entry<String, SettingConfig> entry : map.entrySet()) {
            jSONObject.put(entry.getKey(), (Object)ConfigJsonCodec.a(entry.getValue()));
        }
        return jSONObject;
    }

    private static ModuleConfig c(JSONObject jSONObject) {
        ModuleConfig hncvt12 = new ModuleConfig();
        hncvt12.a(jSONObject.optBoolean("enabled", false));
        hncvt12.a(jSONObject.optInt("key", 0));
        hncvt12.a(ConfigJsonCodec.d(jSONObject.optJSONObject("settings")));
        return hncvt12;
    }

    private static JSONObject c(Map<String, HudPositionConfig> map) {
        JSONObject jSONObject = new JSONObject();
        for (Map.Entry<String, HudPositionConfig> entry : map.entrySet()) {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("percentX", entry.getValue().a());
            jSONObject2.put("percentY", entry.getValue().b());
            jSONObject2.put("scale", entry.getValue().c());
            jSONObject.put(entry.getKey(), (Object)jSONObject2);
        }
        return jSONObject;
    }

    private static NotificationVisibilityConfig h(JSONObject jSONObject) {
        NotificationVisibilityConfig sewxud2 = new NotificationVisibilityConfig();
        if (jSONObject == null) {
            return sewxud2;
        }
        sewxud2.setNotificationsEnabled(jSONObject.optBoolean("notificationsEnabled", true));
        sewxud2.setMusicEnabled(jSONObject.optBoolean("musicEnabled", true));
        sewxud2.setPerformanceEnabled(jSONObject.optBoolean("fpsAndPingEnabled", true));
        sewxud2.setEventsEnabled(jSONObject.optBoolean("eventsEnabled", true));
        sewxud2.setModulesEnabled(jSONObject.optBoolean("modulesEnabled", true));
        return sewxud2;
    }

    private static MarkerSettingsConfig f(JSONObject jSONObject) {
        MarkerSettingsConfig srqyen2 = new MarkerSettingsConfig();
        if (jSONObject == null) {
            return srqyen2;
        }
        srqyen2.a(jSONObject.optBoolean("quickMarkerEnabled", true));
        srqyen2.a(jSONObject.optInt("quickMarkerKey", 0));
        srqyen2.b(jSONObject.optBoolean("deathMarkerEnabled", false));
        srqyen2.c(jSONObject.optBoolean("autoMarkersEnabled", true));
        srqyen2.d(jSONObject.optBoolean("mysteriousBeaconEnabled", false));
        return srqyen2;
    }

    private static Map<String, SettingConfig> d(JSONObject jSONObject) {
        HashMap<String, SettingConfig> hashMap = new HashMap<String, SettingConfig>();
        if (jSONObject == null) {
            return hashMap;
        }
        for (String string : jSONObject.keySet()) {
            JSONObject jSONObject2 = jSONObject.optJSONObject(string);
            if (jSONObject2 == null) continue;
            hashMap.put(string, ConfigJsonCodec.e(jSONObject2));
        }
        return hashMap;
    }

    private static List<FriendConfig> a(JSONArray jSONArray) {
        ArrayList<FriendConfig> arrayList = new ArrayList<FriendConfig>();
        if (jSONArray == null) {
            return arrayList;
        }
        for (int i = 0; i < jSONArray.length(); ++i) {
            JSONObject jSONObject = jSONArray.optJSONObject(i);
            if (jSONObject == null) continue;
            FriendConfig i8p39m2 = new FriendConfig();
            i8p39m2.a(jSONObject.optString("name", ""));
            i8p39m2.b(jSONObject.optString("addedDate", ""));
            arrayList.add(i8p39m2);
        }
        return arrayList;
    }

    private static ConfigMetadata a(JSONObject jSONObject) {
        return jSONObject == null ? ConfigMetadata.a("Unnamed") : ConfigMetadata.a(jSONObject.optString("name", "Unnamed"), jSONObject.optString("author", "Unknown"), jSONObject.optInt("authorId", 0), jSONObject.optString("signature", ""), jSONObject.optLong("createdAt", System.currentTimeMillis()), jSONObject.optString("description", ""), jSONObject.optLong("updatedAt", System.currentTimeMillis()));
    }

    public static String a(HaronConfigSnapshot xc39mk2) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("version", xc39mk2.a());
        jSONObject.put("metadata", (Object)ConfigJsonCodec.a(xc39mk2.b()));
        jSONObject.put("modules", (Object)ConfigJsonCodec.a(xc39mk2.c()));
        jSONObject.put("friends", (Object)ConfigJsonCodec.a(xc39mk2.d()));
        jSONObject.put("markers", (Object)ConfigJsonCodec.b(xc39mk2.e()));
        jSONObject.put("markerSettings", (Object)ConfigJsonCodec.a(xc39mk2.f()));
        jSONObject.put("hudPositions", (Object)ConfigJsonCodec.c(xc39mk2.g()));
        jSONObject.put("categoriesPosition", (Object)xc39mk2.h());
        jSONObject.put("watermarkSettings", (Object)ConfigJsonCodec.a(xc39mk2.i()));
        jSONObject.put("clientColor", (Object)ConfigJsonCodec.a(xc39mk2.j()));
        return jSONObject.toString(2);
    }

    private static JSONObject a(NotificationVisibilityConfig sewxud2) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("notificationsEnabled", sewxud2.areNotificationsEnabled());
        jSONObject.put("musicEnabled", sewxud2.isMusicEnabled());
        jSONObject.put("fpsAndPingEnabled", sewxud2.isPerformanceEnabled());
        jSONObject.put("eventsEnabled", sewxud2.areEventsEnabled());
        jSONObject.put("modulesEnabled", sewxud2.areModulesEnabled());
        return jSONObject;
    }

    private static JSONObject a(MarkerSettingsConfig srqyen2) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("quickMarkerEnabled", srqyen2.a());
        jSONObject.put("quickMarkerKey", srqyen2.b());
        jSONObject.put("deathMarkerEnabled", srqyen2.c());
        jSONObject.put("autoMarkersEnabled", srqyen2.d());
        jSONObject.put("mysteriousBeaconEnabled", srqyen2.e());
        return jSONObject;
    }

    private static JSONArray a(List<FriendConfig> list) {
        JSONArray jSONArray = new JSONArray();
        for (FriendConfig i8p39m2 : list) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("name", (Object)i8p39m2.a());
            jSONObject.put("addedDate", (Object)i8p39m2.b());
            jSONArray.put((Object)jSONObject);
        }
        return jSONArray;
    }

    private static JSONObject a(SettingConfig bw3xj62) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("type", (Object)bw3xj62.a());
        String string = bw3xj62.a();
        int n = -1;
        switch (string.hashCode()) {
            case -1034364087: {
                break;
            }
            case 106079: {
                if (!string.equals("key")) break;
                n = 2;
                break;
            }
            case 0x3339A3: {
                break;
            }
            case 64711720: {
                break;
            }
            case 94842723: {
                if (!string.equals("color") || !b) break;
                throw new IllegalAccessError();
            }
            case 100358090: {
                break;
            }
        }
        switch (n) {
            case 0: 
            case 1: 
            case 2: 
            case 3: {
                jSONObject.put("value", bw3xj62.b());
                break;
            }
            case 4: {
                jSONObject.put("hue", (Object)bw3xj62.c());
                jSONObject.put("saturation", (Object)bw3xj62.d());
                jSONObject.put("brightness", (Object)bw3xj62.e());
                break;
            }
            case 5: {
                jSONObject.put("value", bw3xj62.b());
                if (bw3xj62.f() == null || bw3xj62.f().length <= 0) break;
                jSONObject.put("selectedIndices", (Object)new JSONArray((Object)bw3xj62.f()));
                break;
            }
            case 6: {
                jSONObject.put("enabled", (Object)bw3xj62.g());
                jSONObject.put("colorRgb", (Object)bw3xj62.h());
            }
        }
        return jSONObject;
    }

    private static JSONObject a(ModuleConfig hncvt12) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("enabled", hncvt12.a());
        jSONObject.put("key", hncvt12.b());
        jSONObject.put("settings", (Object)ConfigJsonCodec.b(hncvt12.c()));
        return jSONObject;
    }

    private static JSONObject a(ConfigMetadata au05792) {
        JSONObject jSONObject = new JSONObject();
        if (au05792 == null) {
            return jSONObject;
        }
        jSONObject.put("name", (Object)au05792.i());
        jSONObject.put("author", (Object)au05792.c());
        jSONObject.put("authorId", au05792.d());
        jSONObject.put("signature", (Object)au05792.e());
        jSONObject.put("createdAt", au05792.f());
        jSONObject.put("description", (Object)au05792.j());
        jSONObject.put("updatedAt", au05792.k());
        return jSONObject;
    }

    private static JSONObject a(Map<String, ModuleConfig> map) {
        JSONObject jSONObject = new JSONObject();
        for (Map.Entry<String, ModuleConfig> entry : map.entrySet()) {
            jSONObject.put(entry.getKey(), (Object)ConfigJsonCodec.a(entry.getValue()));
        }
        return jSONObject;
    }

    private static JSONObject a(ClientColorConfig b617vo2) {
        JSONObject jSONObject = new JSONObject();
        if (b617vo2 == null) {
            return jSONObject;
        }
        jSONObject.put("mode", (Object)b617vo2.a());
        jSONObject.put("staticHue", b617vo2.b());
        jSONObject.put("staticSaturation", b617vo2.c());
        jSONObject.put("staticBrightness", b617vo2.d());
        jSONObject.put("gradient1Hue", b617vo2.e());
        jSONObject.put("gradient1Saturation", b617vo2.f());
        jSONObject.put("gradient1Brightness", b617vo2.g());
        jSONObject.put("gradient2Hue", b617vo2.h());
        jSONObject.put("gradient2Saturation", b617vo2.i());
        jSONObject.put("gradient2Brightness", b617vo2.j());
        return jSONObject;
    }

    public static HaronConfigSnapshot a(String string) {
        JSONObject jSONObject = new JSONObject(string);
        HaronConfigSnapshot xc39mk2 = new HaronConfigSnapshot();
        xc39mk2.a(jSONObject.optInt("version", 1));
        xc39mk2.a(ConfigJsonCodec.a(jSONObject.optJSONObject("metadata")));
        xc39mk2.a(ConfigJsonCodec.b(jSONObject.optJSONObject("modules")));
        xc39mk2.a(ConfigJsonCodec.a(jSONObject.optJSONArray("friends")));
        xc39mk2.b(ConfigJsonCodec.b(jSONObject.optJSONArray("markers")));
        xc39mk2.a(ConfigJsonCodec.f(jSONObject.optJSONObject("markerSettings")));
        xc39mk2.b(ConfigJsonCodec.g(jSONObject.optJSONObject("hudPositions")));
        xc39mk2.a(jSONObject.optString("categoriesPosition", "BOTTOM"));
        xc39mk2.a(ConfigJsonCodec.h(jSONObject.optJSONObject("watermarkSettings")));
        xc39mk2.a(ConfigJsonCodec.i(jSONObject.optJSONObject("clientColor")));
        return xc39mk2;
    }

    private static Map<String, HudPositionConfig> g(JSONObject jSONObject) {
        HashMap<String, HudPositionConfig> hashMap = new HashMap<String, HudPositionConfig>();
        if (jSONObject == null) {
            return hashMap;
        }
        for (String string : jSONObject.keySet()) {
            JSONObject jSONObject2 = jSONObject.optJSONObject(string);
            if (jSONObject2 == null) continue;
            HudPositionConfig p8xsxi2 = new HudPositionConfig();
            p8xsxi2.a((float)jSONObject2.optDouble("percentX", 0.0));
            p8xsxi2.b((float)jSONObject2.optDouble("percentY", 0.0));
            p8xsxi2.c((float)jSONObject2.optDouble("scale", 1.0));
            hashMap.put(string, p8xsxi2);
        }
        return hashMap;
    }
}

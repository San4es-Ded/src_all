package rockstar.client.internal.network;




import rockstar.client.internal.script.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockstar.client.internal.network.HttpDownloadUtils;
import rockstar.client.internal.network.GeniusLyricsService;
import rockstar.client.internal.script.LyricsTrack;
import rockstar.client.internal.config.TrackMatchScorer;

public class LyricsAggregatorService {
    static final Logger internalField0572 = LoggerFactory.getLogger((String)"rockstar-lyrics");
    private static final String internalField0248 = "https://lrclib.net/api";
    private static final String internalField1077 = "Rockstar/2.0 (https://github.com/rockstar)";
    private static final String internalField1076 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36";
    private static final long internalField0229 = 8000L;
    private static final InternalType0392 internalField0702 = new InternalType0392("lrclib");
    private static final InternalType0392 internalField0701 = new InternalType0392("netease");

    public static LyricsTrack internalMethod04088(String string, String string2) {
        return LyricsAggregatorService.internalMethod02041(string, string2, 0L);
    }

    public static LyricsTrack internalMethod02041(String string, String string2, long l) {
        if (string == null || string2 == null || string.isBlank() || string2.isBlank()) {
            return LyricsTrack.internalMethod02903();
        }
        long l2 = System.currentTimeMillis();
        internalField0572.info("[lyrics] \u0438\u0449\u0443 \u00ab{} - {}\u00bb ({} \u0441)", new Object[]{string, string2, l});
        LyricsTrack typedValue279 = LyricsAggregatorService.internalMethod07581(string, string2, l, LyricsAggregatorService.internalMethod02117());
        if (typedValue279 != null && !typedValue279.internalMethod00819() && typedValue279.internalMethod00823()) {
            return LyricsAggregatorService.internalMethod02182("lrclib", string, string2, typedValue279, l2);
        }
        LyricsTrack typedValue280 = LyricsAggregatorService.internalMethod03033(string, string2, l, LyricsAggregatorService.internalMethod02117());
        if (typedValue280 != null && !typedValue280.internalMethod00819() && typedValue280.internalMethod00823()) {
            return LyricsAggregatorService.internalMethod02182("netease", string, string2, typedValue280, l2);
        }
        if (typedValue279 != null && !typedValue279.internalMethod00819()) {
            return LyricsAggregatorService.internalMethod02182("lrclib (\u0431\u0435\u0437 \u0442\u0430\u0439\u043c\u043a\u043e\u0434\u043e\u0432)", string, string2, typedValue279, l2);
        }
        String string3 = GeniusLyricsService.internalMethod04363(string, string2);
        if (string3 != null && !string3.isBlank()) {
            return LyricsAggregatorService.internalMethod02182("genius (\u0431\u0435\u0437 \u0442\u0430\u0439\u043c\u043a\u043e\u0434\u043e\u0432)", string, string2, LyricsTrack.internalMethod02937(string3), l2);
        }
        internalField0572.info("[lyrics] {} - {}: \u043d\u0435 \u043d\u0430\u0448\u043b\u043e\u0441\u044c \u043d\u0438 \u0432 \u043e\u0434\u043d\u043e\u043c \u0438\u0441\u0442\u043e\u0447\u043d\u0438\u043a\u0435 ({} \u043c\u0441)", new Object[]{string, string2, System.currentTimeMillis() - l2});
        return LyricsTrack.internalMethod02903();
    }

    private static LyricsTrack internalMethod02182(String string, String string2, String string3, LyricsTrack typedValue279, long l) {
        internalField0572.info("[lyrics] {} - {}: {} \u0441\u0442\u0440\u043e\u043a \u0438\u0437 {} ({} \u043c\u0441)", new Object[]{string2, string3, typedValue279.internalMethod00129().size(), string, System.currentTimeMillis() - l});
        return typedValue279;
    }

    private static void internalMethod03605(String string, String string2, int n, Exception exception) {
        if (exception != null) {
            internalField0572.warn("[lyrics] {} \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d ({}): {}", new Object[]{string, string2, exception.toString()});
        } else {
            internalField0572.warn("[lyrics] {} \u043e\u0442\u0432\u0435\u0442\u0438\u043b {} ({})", new Object[]{string, n, string2});
        }
    }

    private static long internalMethod02117() {
        return System.currentTimeMillis() + 8000L;
    }

    private static boolean internalMethod05656(long l) {
        return System.currentTimeMillis() >= l;
    }

    private static LyricsTrack internalMethod03033(String string, String string2, long l, long l2) {
        if (!internalField0701.internalMethod00417()) {
            return null;
        }
        JsonArray jsonArray = LyricsAggregatorService.internalMethod00541(string + " " + string2, l2);
        String string3 = LyricsAggregatorService.internalMethod04022(string);
        if (!(jsonArray != null && !jsonArray.isEmpty() || string3.isBlank() || string3.equalsIgnoreCase(string.trim()))) {
            jsonArray = LyricsAggregatorService.internalMethod00541(string3 + " " + string2, l2);
        }
        if (jsonArray == null) {
            return null;
        }
        long l3 = -1L;
        double d = Double.NEGATIVE_INFINITY;
        for (JsonElement jsonElement : jsonArray) {
            double d2;
            JsonObject jsonObject;
            if (!jsonElement.isJsonObject() || !(jsonObject = jsonElement.getAsJsonObject()).has("id") || jsonObject.get("id").isJsonNull() || !((d2 = LyricsAggregatorService.internalMethod04790(LyricsAggregatorService.internalMethod03292(jsonObject), string, string2, l)) > d)) continue;
            d = d2;
            l3 = jsonObject.get("id").getAsLong();
        }
        return l3 < 0L ? null : LyricsAggregatorService.internalMethod06193(l3, l2);
    }

    private static JsonArray internalMethod00541(String string, long l) {
        JsonObject jsonObject = LyricsAggregatorService.internalMethod03984("https://music.163.com/api/search/get?type=1&limit=10&s=" + LyricsAggregatorService.internalMethod05092(string), l);
        if (jsonObject == null || !jsonObject.has("result") || !jsonObject.get("result").isJsonObject()) {
            return null;
        }
        JsonObject jsonObject2 = jsonObject.getAsJsonObject("result");
        return jsonObject2.has("songs") && jsonObject2.get("songs").isJsonArray() ? jsonObject2.getAsJsonArray("songs") : null;
    }

    private static LyricsTrack internalMethod06193(long l, long l2) {
        JsonObject jsonObject = LyricsAggregatorService.internalMethod03984("https://music.163.com/api/song/lyric?lv=1&kv=1&tv=-1&id=" + l, l2);
        if (jsonObject == null || !jsonObject.has("lrc") || !jsonObject.get("lrc").isJsonObject()) {
            return null;
        }
        String string = LyricsAggregatorService.internalMethod05669(jsonObject.getAsJsonObject("lrc"), "lyric");
        if (string.isBlank()) {
            return null;
        }
        LyricsTrack typedValue279 = LyricsTrack.internalMethod01213(string);
        return typedValue279.internalMethod00819() ? null : typedValue279;
    }

    private static JsonObject internalMethod03292(JsonObject jsonObject) {
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("trackName", LyricsAggregatorService.internalMethod05669(jsonObject, "name"));
        StringBuilder stringBuilder = new StringBuilder();
        if (jsonObject.has("artists") && jsonObject.get("artists").isJsonArray()) {
            for (JsonElement jsonElement : jsonObject.getAsJsonArray("artists")) {
                String string;
                if (!jsonElement.isJsonObject() || (string = LyricsAggregatorService.internalMethod05669(jsonElement.getAsJsonObject(), "name")).isBlank()) continue;
                if (!stringBuilder.isEmpty()) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(string);
            }
        }
        jsonObject2.addProperty("artistName", stringBuilder.toString());
        if (jsonObject.has("duration") && !jsonObject.get("duration").isJsonNull()) {
            jsonObject2.addProperty("duration", (Number)((double)jsonObject.get("duration").getAsLong() / 1000.0));
        }
        return jsonObject2;
    }

    private static JsonObject internalMethod03984(String string, long l) {
        if (LyricsAggregatorService.internalMethod05656(l)) {
            return null;
        }
        try {
            HttpDownloadUtils.InternalType0449 nestedValue0157 = HttpDownloadUtils.internalMethod02390(string, "User-Agent", internalField1076, "Referer", "https://music.163.com");
            if (!nestedValue0157.internalMethod02740()) {
                LyricsAggregatorService.internalMethod03605("netease", string, nestedValue0157.internalMethod02739(), null);
                internalField0701.internalMethod00422();
                return null;
            }
            internalField0701.internalMethod00416();
            JsonElement jsonElement = JsonParser.parseString((String)nestedValue0157.internalMethod00694());
            return jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() : null;
        }
        catch (Exception exception) {
            LyricsAggregatorService.internalMethod03605("netease", string, 0, exception);
            internalField0701.internalMethod00422();
            return null;
        }
    }

    private static LyricsTrack internalMethod07581(String string, String string2, long l, long l2) {
        if (!internalField0702.internalMethod00417()) {
            return null;
        }
        ArrayList<JsonArray> arrayList = new ArrayList<JsonArray>();
        if (!LyricsAggregatorService.internalMethod06266(arrayList, "track_name=" + LyricsAggregatorService.internalMethod05092(string2) + "&artist_name=" + LyricsAggregatorService.internalMethod05092(string), l2)) {
            return null;
        }
        JsonObject jsonObject = LyricsAggregatorService.internalMethod03775((JsonArray)arrayList.getFirst(), string, string2, l, true);
        if (jsonObject != null) {
            return LyricsAggregatorService.internalMethod06915(jsonObject, true);
        }
        String string3 = LyricsAggregatorService.internalMethod04022(string);
        if (!string3.isBlank() && !string3.equalsIgnoreCase(string.trim())) {
            if (!LyricsAggregatorService.internalMethod06266(arrayList, "track_name=" + LyricsAggregatorService.internalMethod05092(string2) + "&artist_name=" + LyricsAggregatorService.internalMethod05092(string3), l2)) {
                return null;
            }
            jsonObject = LyricsAggregatorService.internalMethod03775((JsonArray)arrayList.getLast(), string, string2, l, true);
            if (jsonObject != null) {
                return LyricsAggregatorService.internalMethod06915(jsonObject, true);
            }
        }
        if (!LyricsAggregatorService.internalMethod06266(arrayList, "track_name=" + LyricsAggregatorService.internalMethod05092(string2), l2)) {
            return null;
        }
        jsonObject = LyricsAggregatorService.internalMethod03775((JsonArray)arrayList.getLast(), string, string2, l, true);
        if (jsonObject != null) {
            return LyricsAggregatorService.internalMethod06915(jsonObject, true);
        }
        if (!LyricsAggregatorService.internalMethod06266(arrayList, "q=" + LyricsAggregatorService.internalMethod05092((string3.isBlank() ? string : string3) + " " + string2), l2)) {
            return null;
        }
        jsonObject = LyricsAggregatorService.internalMethod03775((JsonArray)arrayList.getLast(), string, string2, l, true);
        if (jsonObject != null) {
            return LyricsAggregatorService.internalMethod06915(jsonObject, true);
        }
        JsonObject jsonObject2 = null;
        double d = Double.NEGATIVE_INFINITY;
        for (JsonArray jsonArray : arrayList) {
            double d2;
            JsonObject jsonObject3 = LyricsAggregatorService.internalMethod03775(jsonArray, string, string2, l, false);
            if (jsonObject3 == null || !((d2 = LyricsAggregatorService.internalMethod04790(jsonObject3, string, string2, l)) > d)) continue;
            d = d2;
            jsonObject2 = jsonObject3;
        }
        return jsonObject2 == null ? null : LyricsAggregatorService.internalMethod06915(jsonObject2, false);
    }

    private static boolean internalMethod06266(List<JsonArray> list, String string, long l) {
        JsonArray jsonArray = LyricsAggregatorService.internalMethod00009(string, l);
        if (jsonArray == null) {
            return false;
        }
        list.add(jsonArray);
        return true;
    }

    private static JsonArray internalMethod00009(String string, long l) {
        if (LyricsAggregatorService.internalMethod05656(l)) {
            return null;
        }
        try {
            HttpDownloadUtils.InternalType0449 nestedValue0157 = HttpDownloadUtils.internalMethod02390("https://lrclib.net/api/search?" + string, "User-Agent", internalField1077);
            if (!nestedValue0157.internalMethod02740()) {
                LyricsAggregatorService.internalMethod03605("lrclib", "https://lrclib.net/api/search?" + string, nestedValue0157.internalMethod02739(), null);
                internalField0702.internalMethod00422();
                return null;
            }
            internalField0702.internalMethod00416();
            return JsonParser.parseString((String)nestedValue0157.internalMethod00694()).getAsJsonArray();
        }
        catch (Exception exception) {
            LyricsAggregatorService.internalMethod03605("lrclib", "https://lrclib.net/api/search?" + string, 0, exception);
            internalField0702.internalMethod00422();
            return null;
        }
    }

    public static JsonObject internalMethod03775(JsonArray jsonArray, String string, String string2, long l, boolean bl) {
        JsonObject jsonObject = null;
        double d = Double.NEGATIVE_INFINITY;
        String string3 = bl ? "syncedLyrics" : "plainLyrics";
        for (JsonElement jsonElement : jsonArray) {
            double d2;
            JsonObject jsonObject2;
            if (!jsonElement.isJsonObject() || !LyricsAggregatorService.internalMethod04649(jsonObject2 = jsonElement.getAsJsonObject(), string3) || !((d2 = LyricsAggregatorService.internalMethod04790(jsonObject2, string, string2, l)) > d)) continue;
            d = d2;
            jsonObject = jsonObject2;
        }
        return jsonObject;
    }

    private static double internalMethod04790(JsonObject jsonObject, String string, String string2, long l) {
        return TrackMatchScorer.internalMethod03931(jsonObject, string, string2, l);
    }

    private static String internalMethod04022(String string) {
        return TrackMatchScorer.internalMethod00620(string);
    }

    private static LyricsTrack internalMethod06915(JsonObject jsonObject, boolean bl) {
        String string = jsonObject.get(bl ? "syncedLyrics" : "plainLyrics").getAsString();
        return bl ? LyricsTrack.internalMethod01213(string) : LyricsTrack.internalMethod02937(string);
    }

    private static boolean internalMethod04649(JsonObject jsonObject, String string) {
        return jsonObject.has(string) && !jsonObject.get(string).isJsonNull() && !jsonObject.get(string).getAsString().isBlank();
    }

    private static String internalMethod05669(JsonObject jsonObject, String string) {
        return TrackMatchScorer.internalMethod06952(jsonObject, string);
    }

    private static String internalMethod05092(String string) {
        return URLEncoder.encode(string, StandardCharsets.UTF_8);
    }

    static final class InternalType0392 {
        private static final long internalField0229 = 300000L;
        private final String internalField0248;
        private int internalField0228;
        private long internalField0230;

        InternalType0392(String string) {
            this.internalField0248 = string;
        }

        synchronized boolean internalMethod00417() {
            if (System.currentTimeMillis() < this.internalField0230) {
                return false;
            }
            this.internalField0230 = 0L;
            return true;
        }

        synchronized void internalMethod00416() {
            this.internalField0228 = 0;
            this.internalField0230 = 0L;
        }

        synchronized void internalMethod00422() {
            if (++this.internalField0228 < 3) {
                return;
            }
            this.internalField0228 = 0;
            this.internalField0230 = System.currentTimeMillis() + 300000L;
            internalField0572.warn("[lyrics] {} \u043e\u0431\u043e\u0440\u0432\u0430\u043b \u0441\u043e\u0435\u0434\u0438\u043d\u0435\u043d\u0438\u0435 {} \u0440\u0430\u0437\u0430 \u043f\u043e\u0434\u0440\u044f\u0434 \u2014 \u043f\u0440\u043e\u043f\u0443\u0441\u043a\u0430\u0435\u043c \u0435\u0433\u043e {} \u043c\u0438\u043d\u0443\u0442", new Object[]{this.internalField0248, 3, 5L});
        }
    }
}


package haron.modules.hud;

import haron.modules.hud.LyricLine;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LyricsFetcher {
    private static final Logger LOG = LogManager.getLogger((String)"haron/lyrics");
    private static final LyricsFetcher INSTANCE = new LyricsFetcher();
    private final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "Haron-LyricsFetcher");
        thread.setDaemon(true);
        return thread;
    });
    private String lastTrackKey = "";
    private List<LyricLine> cachedLines = new ArrayList<LyricLine>();
    private volatile boolean fetching = false;

    public List<LyricLine> getLines(String string) {
        if (string.isEmpty()) {
            return List.of();
        }
        if (string.equals(this.lastTrackKey) && this.cachedLines != null) {
            return this.cachedLines;
        }
        if (!this.fetching) {
            this.fetching = true;
            this.lastTrackKey = string;
            this.cachedLines = new ArrayList<LyricLine>();
            this.executor.submit(() -> {
                try {
                    List<LyricLine> list = this.fetchFromLrclib(string);
                    this.cachedLines = list;
                }
                catch (Exception exception) {
                    LOG.error("[Lyrics] Fetch error", (Throwable)exception);
                    this.cachedLines = new ArrayList<LyricLine>();
                }
                finally {
                    this.fetching = false;
                }
            });
        }
        return this.cachedLines != null ? this.cachedLines : List.of();
    }

    private List<LyricLine> fetchFromLrclib(String string) throws Exception {
        String string2 = string.replaceAll("\\(.*?\\)", "").replaceAll("\\[.*?\\]", "").replaceAll("\\s*[-–]\\s*(Official|Music|Video|Audio|Lyric|Live|Remix|Cover|HD|4K|Clip).*", "").replaceAll("^\\d+\\.?\\s*", "").trim();
        String string3 = "";
        String string4 = string2;
        if (string2.contains(" - ")) {
            String[] parts = string2.split(" - ", 2);
            string3 = parts[0].trim();
            string4 = parts[1].trim();
        } else if (string2.contains(" — ")) {
            String[] parts = string2.split(" — ", 2);
            string3 = parts[0].trim();
            string4 = parts[1].trim();
        }
        String url = LyricsFetcher.$sf$0(URLEncoder.encode(string4, "UTF-8"));
        if (!string3.isEmpty()) {
            url = LyricsFetcher.$sf$1(url, URLEncoder.encode(string3, "UTF-8"));
        }
        LOG.info("[Lyrics] Fetching: artist='{}' track='{}' url='{}'", string3, string4, url);
        List<LyricLine> list = this.fetchUrl(url);
        if (list.isEmpty() && !string3.isEmpty()) {
            String string5 = LyricsFetcher.$sf$2(URLEncoder.encode(string, "UTF-8"));
            LOG.info("[Lyrics] Fallback search: {}", (Object)string5);
            list = this.fetchSearch(string5);
        }
        return list;
    }

    private List<LyricLine> fetchSearch(String string) throws Exception {
        int n;
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(string).openConnection();
        httpURLConnection.setRequestProperty("User-Agent", "HaronVisuals-Lyrics/1.0");
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);
        if (httpURLConnection.getResponseCode() != 200) {
            httpURLConnection.disconnect();
            return List.of();
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
            String string2;
            while ((string2 = reader.readLine()) != null) {
                stringBuilder.append(string2);
            }
        }
        httpURLConnection.disconnect();
        String response = stringBuilder.toString();
        if (response.length() > 2 && response.charAt(0) == '[' && (n = response.indexOf("\"syncedLyrics\":")) > 0) {
            int n2 = response.lastIndexOf(123, n);
            int n3 = response.indexOf(125, n);
            if (n2 >= 0 && n3 > n2) {
                return this.parseLrcFromJson(response.substring(n2, n3 + 1));
            }
        }
        return List.of();
    }

    private String extractJsonField(String string, String string2) {
        String string3 = LyricsFetcher.$sf$3(string2);
        int n = string.indexOf(string3);
        if (n < 0) {
            return null;
        }
        if ((n = string.indexOf(34, n + string3.length())) < 0) {
            return null;
        }
        int n2 = n + 1;
        while (n2 < string.length()) {
            char c = string.charAt(n2);
            if (c == '\\') {
                n2 += 2;
                continue;
            }
            if (c == '\"') break;
            ++n2;
        }
        String string4 = string.substring(n + 1, n2);
        return string4.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private float parseLrcTime(String string) {
        try {
            String[] stringArray = string.split(":");
            int n = Integer.parseInt(stringArray[0]);
            String[] stringArray2 = stringArray[1].split("\\.");
            float f = Float.parseFloat(stringArray2[0]);
            float f2 = stringArray2.length > 1 ? Float.parseFloat(LyricsFetcher.$sf$4(stringArray2[1])) : 0.0f;
            return (float)(n * 60) + f + f2;
        }
        catch (Exception exception) {
            return -1.0f;
        }
    }

    private List<LyricLine> parseLrcFromJson(String string) {
        String[] stringArray;
        ArrayList<LyricLine> arrayList = new ArrayList<LyricLine>();
        String string2 = this.extractJsonField(string, "syncedLyrics");
        if (string2 == null || string2.isEmpty()) {
            String string3 = this.extractJsonField(string, "plainLyrics");
            if (string3 != null && !string3.isEmpty()) {
                String[] stringArray2 = string3.split("\n");
                float f = 0.0f;
                for (String string4 : stringArray2) {
                    arrayList.add(new LyricLine(f, string4.trim()));
                    f += 3.0f;
                }
            }
            return arrayList;
        }
        for (String string5 : stringArray = string2.split("\n")) {
            float f;
            int n;
            if ((string5 = string5.trim()).isEmpty() || (n = string5.indexOf(93)) < 0) continue;
            String string6 = string5.substring(1, n);
            String string7 = string5.substring(n + 1).trim();
            if (string7.isEmpty() || !((f = this.parseLrcTime(string6)) >= 0.0f)) continue;
            arrayList.add(new LyricLine(f, string7));
        }
        return arrayList;
    }

    private List<LyricLine> fetchUrl(String string) throws Exception {
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(string).openConnection();
        httpURLConnection.setRequestProperty("User-Agent", "HaronVisuals-Lyrics/1.0");
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);
        if (httpURLConnection.getResponseCode() != 200) {
            httpURLConnection.disconnect();
            return List.of();
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
            String string2;
            while ((string2 = reader.readLine()) != null) {
                stringBuilder.append(string2);
            }
        }
        httpURLConnection.disconnect();
        return this.parseLrcFromJson(stringBuilder.toString());
    }

    public static LyricsFetcher getInstance() {
        return INSTANCE;
    }

    private static /* synthetic */ String $sf$0(String string) {
        return "https://lrclib.net/api/get?track_name=" + string;
    }

    private static /* synthetic */ String $sf$4(String string) {
        return "0." + string;
    }

    private static /* synthetic */ String $sf$3(String string) {
        return "\"" + string + "\":";
    }

    private static /* synthetic */ String $sf$1(String string, String string2) {
        return string + "&artist_name=" + string2;
    }

    private static /* synthetic */ String $sf$2(String string) {
        return "https://lrclib.net/api/search?q=" + string;
    }
}

package rockstar.client.internal.config;


import rockstar.client.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.client.MinecraftClient;
import rockstar.client.RockstarClient;

public final class NeuroModelConfig {
    public static final int internalField0227 = 17;
    public static final String internalField0248 = "default";
    private static NeuroModelConfig internalField0328;
    private static boolean internalField0277;
    private static String internalField0247;
    private final float[][] internalField0038;
    private final float[][] internalField0039;
    private final float[][] internalField0969;
    private final float[] internalField0615;
    private final float[] internalField0616;
    private final float[] internalField1238;
    private final float[] internalField1240;
    private final int internalField0228;
    private final int internalField1053;
    private final int internalField1055;
    private final float internalField0206;
    private final float internalField1048;

    private NeuroModelConfig(JsonObject jsonObject) {
        this.internalField0228 = jsonObject.get("hidden").getAsInt();
        this.internalField1053 = jsonObject.get("mix").getAsInt();
        this.internalField1055 = jsonObject.has("freezeCut") ? jsonObject.get("freezeCut").getAsInt() : 12;
        JsonArray jsonArray = jsonObject.getAsJsonArray("limits");
        this.internalField0206 = jsonArray.get(0).getAsFloat();
        this.internalField1048 = jsonArray.get(1).getAsFloat();
        this.internalField1240 = jsonObject.has("error") ? NeuroModelConfig.internalMethod03904(jsonObject.getAsJsonArray("error")) : new float[]{};
        JsonObject jsonObject2 = jsonObject.getAsJsonObject("gru");
        this.internalField0038 = NeuroModelConfig.internalMethod04538(jsonObject2.getAsJsonArray("wi"));
        this.internalField0039 = NeuroModelConfig.internalMethod04538(jsonObject2.getAsJsonArray("wh"));
        this.internalField0615 = NeuroModelConfig.internalMethod03904(jsonObject2.getAsJsonArray("bi"));
        this.internalField0616 = NeuroModelConfig.internalMethod03904(jsonObject2.getAsJsonArray("bh"));
        JsonObject jsonObject3 = jsonObject.getAsJsonObject("out");
        this.internalField0969 = NeuroModelConfig.internalMethod04538(jsonObject3.getAsJsonArray("w"));
        this.internalField1238 = NeuroModelConfig.internalMethod03904(jsonObject3.getAsJsonArray("b"));
    }

    private static float[][] internalMethod04538(JsonArray jsonArray) {
        float[][] fArrayArray = new float[jsonArray.size()][];
        for (int i = 0; i < jsonArray.size(); ++i) {
            fArrayArray[i] = NeuroModelConfig.internalMethod03904(jsonArray.get(i).getAsJsonArray());
        }
        return fArrayArray;
    }

    private static float[] internalMethod03904(JsonArray jsonArray) {
        float[] fArray = new float[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); ++i) {
            fArray[i] = jsonArray.get(i).getAsFloat();
        }
        return fArray;
    }

    public static Path internalMethod06488() {
        return MinecraftClient.getInstance().runDirectory.toPath().resolve("Rockstar").resolve("neuro");
    }

    public static Path internalMethod00627() {
        return NeuroModelConfig.internalMethod06488().resolve("data");
    }

    public static Path internalMethod04529(String string) {
        return NeuroModelConfig.internalMethod06488().resolve(string + ".json");
    }

    public static String internalMethod07182() {
        if (internalField0247 == null) {
            try {
                Path path = NeuroModelConfig.internalMethod06488().resolve("active.txt");
                internalField0247 = Files.isRegularFile(path, new LinkOption[0]) ? Files.readString(path).trim() : internalField0248;
            }
            catch (Exception exception) {
                internalField0247 = internalField0248;
            }
            if (internalField0247.isEmpty()) {
                internalField0247 = internalField0248;
            }
        }
        return internalField0247;
    }

    public static List<String> internalMethod01137() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(internalField0248);
        try (Stream<Path> stream = Files.list(NeuroModelConfig.internalMethod06488());){
            stream.filter(path -> path.getFileName().toString().endsWith(".json")).map(path -> path.getFileName().toString().replaceFirst("\\.json$", "")).filter(string -> !arrayList.contains(string)).sorted().forEach(arrayList::add);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return arrayList;
    }

    public static boolean internalMethod06694(String string) {
        return internalField0248.equals(string) || Files.isRegularFile(NeuroModelConfig.internalMethod04529(string), new LinkOption[0]);
    }

    public static boolean internalMethod05242(String string) {
        if (!NeuroModelConfig.internalMethod06694(string)) {
            return false;
        }
        internalField0247 = string;
        try {
            Files.createDirectories(NeuroModelConfig.internalMethod06488(), new FileAttribute[0]);
            Files.writeString(NeuroModelConfig.internalMethod06488().resolve("active.txt"), (CharSequence)string, new OpenOption[0]);
        }
        catch (Exception exception) {
            RockstarClient.internalField0572.error("[Neuro] \u043d\u0435 \u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0430\u043a\u0442\u0438\u0432\u043d\u0443\u044e \u043c\u043e\u0434\u0435\u043b\u044c", (Throwable)exception);
        }
        NeuroModelConfig.internalMethod00495();
        return true;
    }

    public static NeuroModelConfig internalMethod03469() {
        if (!internalField0277) {
            internalField0277 = true;
            internalField0328 = NeuroModelConfig.internalMethod01263(NeuroModelConfig.internalMethod07182());
        }
        return internalField0328;
    }

    public static void internalMethod00495() {
        internalField0277 = false;
        internalField0328 = null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static NeuroModelConfig internalMethod01263(String string) {
        try {
            JsonObject jsonObject;
            Path path = NeuroModelConfig.internalMethod04529(string);
            if (Files.isRegularFile(path, new LinkOption[0])) {
                jsonObject = JsonParser.parseString((String)Files.readString(path)).getAsJsonObject();
            } else {
                if (!internalField0248.equals(string)) return null;
                try (InputStream inputStream = NeuroModelConfig.class.getClassLoader().getResourceAsStream("assets/rockstar/neuro/default.json");){
                    if (inputStream == null) {
                        NeuroModelConfig typedValue121 = null;
                        return typedValue121;
                    }
                    jsonObject = JsonParser.parseReader((Reader)new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject();
                }
            }
            if (jsonObject.get("features").getAsInt() != 17) return null;
            if (!jsonObject.has("mix")) {
                return null;
            }
            if (!jsonObject.has("units")) return null;
            if ("deg".equals(jsonObject.get("units").getAsString())) return new NeuroModelConfig(jsonObject);
            return null;
        }
        catch (Exception exception) {
            RockstarClient.internalField0572.error("[Neuro] \u043c\u043e\u0434\u0435\u043b\u044c " + string + " \u043d\u0435 \u0447\u0438\u0442\u0430\u0435\u0442\u0441\u044f", (Throwable)exception);
            return null;
        }
    }

    public int internalMethod00494() {
        return this.internalField1055;
    }

    public float[] internalMethod04220() {
        return new float[this.internalField0228];
    }

    public float[] internalMethod00149(float[] fArray, float[] fArray2) {
        float f;
        int n;
        float[] fArray3 = new float[3 * this.internalField0228];
        float[] fArray4 = new float[3 * this.internalField0228];
        for (n = 0; n < 3 * this.internalField0228; ++n) {
            int n2;
            f = this.internalField0615[n];
            float[] fArray5 = this.internalField0038[n];
            for (n2 = 0; n2 < fArray5.length; ++n2) {
                f += fArray5[n2] * fArray[n2];
            }
            fArray3[n] = f;
            f = this.internalField0616[n];
            fArray5 = this.internalField0039[n];
            for (n2 = 0; n2 < fArray5.length; ++n2) {
                f += fArray5[n2] * fArray2[n2];
            }
            fArray4[n] = f;
        }
        for (n = 0; n < this.internalField0228; ++n) {
            f = NeuroModelConfig.internalMethod08878(fArray3[n] + fArray4[n]);
            float f2 = NeuroModelConfig.internalMethod08878(fArray3[this.internalField0228 + n] + fArray4[this.internalField0228 + n]);
            float f3 = (float)Math.tanh(fArray3[2 * this.internalField0228 + n] + f * fArray4[2 * this.internalField0228 + n]);
            fArray2[n] = (1.0f - f2) * f3 + f2 * fArray2[n];
        }
        float[] fArray6 = new float[this.internalField0969.length];
        for (int i = 0; i < this.internalField0969.length; ++i) {
            float f4 = this.internalField1238[i];
            float[] fArray7 = this.internalField0969[i];
            for (int j = 0; j < fArray7.length; ++j) {
                f4 += fArray7[j] * fArray2[j];
            }
            fArray6[i] = f4;
        }
        return fArray6;
    }

    public int internalMethod04897(float[] fArray, float f) {
        float f2 = fArray[1];
        for (int i = 1; i < this.internalField1053; ++i) {
            f2 = Math.max(f2, fArray[1 + 6 * i]);
        }
        float f3 = 0.0f;
        for (int i = 0; i < this.internalField1053; ++i) {
            f3 += (float)Math.exp(fArray[1 + 6 * i] - f2);
        }
        float f4 = f * f3;
        for (int i = 0; i < this.internalField1053; ++i) {
            if (!((f4 -= (float)Math.exp(fArray[1 + 6 * i] - f2)) <= 0.0f)) continue;
            return i;
        }
        return this.internalField1053 - 1;
    }

    public float internalMethod04221(float f) {
        if (this.internalField1240.length == 0) {
            return 0.0f;
        }
        float f2 = Math.max(0.0f, Math.min(1.0f, f)) * (float)(this.internalField1240.length - 1);
        int n = (int)f2;
        return n >= this.internalField1240.length - 1 ? this.internalField1240[this.internalField1240.length - 1] : this.internalField1240[n] + (this.internalField1240[n + 1] - this.internalField1240[n]) * (f2 - (float)n);
    }

    public float internalMethod05587(float f, float f2, boolean bl, float f3, float f4) {
        float f5 = (float)Math.exp(Math.max(-4.0f, Math.min(1.5f, f2)));
        float f6 = Math.max(-8.0f, Math.min(8.0f, f + f4 * f5 * f3));
        float f7 = bl ? this.internalField0206 : this.internalField1048;
        return Math.max(-f7, Math.min(f7, (float)Math.sinh(f6)));
    }

    public static float internalMethod04277(float f) {
        return NeuroModelConfig.internalMethod08878(f);
    }

    public static float internalMethod08867(float f) {
        return (float)Math.tanh(f) * 0.95f;
    }

    private static float internalMethod08878(float f) {
        return 1.0f / (1.0f + (float)Math.exp(-f));
    }
}


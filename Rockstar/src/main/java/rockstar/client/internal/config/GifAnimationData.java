package rockstar.client.internal.config;


import rockstar.client.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public class GifAnimationData {
    private final int internalField0227;
    private final int internalField0228;
    private final int internalField1053;
    private final boolean internalField0277;
    private final List<String> internalField0416;

    public static GifAnimationData internalMethod00892(String string) {
        JsonObject jsonObject = JsonParser.parseString((String)string).getAsJsonObject();
        int n = jsonObject.get("width").getAsInt();
        int n2 = jsonObject.get("height").getAsInt();
        int n3 = jsonObject.get("fps").getAsInt();
        boolean bl = jsonObject.get("loop_mode").getAsString().equals("loop");
        ArrayList<String> arrayList = new ArrayList<String>();
        JsonArray jsonArray = jsonObject.getAsJsonArray("frames");
        for (int i = 0; i < jsonArray.size(); ++i) {
            JsonObject jsonObject2 = jsonArray.get(i).getAsJsonObject();
            arrayList.add(jsonObject2.get("file").getAsString());
        }
        return new GifAnimationData(n, n2, n3, bl, arrayList);
    }

    public long internalMethod01507() {
        return 1000L / (long)this.internalField1053;
    }

    public int internalMethod01506() {
        return this.internalField0416.size();
    }

    @Generated
    public int internalMethod01510() {
        return this.internalField0227;
    }

    @Generated
    public int internalMethod08101() {
        return this.internalField0228;
    }

    @Generated
    public int internalMethod08102() {
        return this.internalField1053;
    }

    @Generated
    public boolean internalMethod01508() {
        return this.internalField0277;
    }

    @Generated
    public List<String> internalMethod01448() {
        return this.internalField0416;
    }

    @Generated
    public GifAnimationData(int n, int n2, int n3, boolean bl, List<String> list) {
        this.internalField0227 = n;
        this.internalField0228 = n2;
        this.internalField1053 = n3;
        this.internalField0277 = bl;
        this.internalField0416 = list;
    }
}


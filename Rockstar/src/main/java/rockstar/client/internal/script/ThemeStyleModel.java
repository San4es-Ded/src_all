package rockstar.client.internal.script;


import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Objects;
import lombok.Generated;
import pyrock.utility.render.ColorRGBA;

public class ThemeStyleModel {
    private ColorRGBA internalField0777;
    private ColorRGBA internalField0776;
    private ColorRGBA internalField1311;
    private ColorRGBA internalField1312;
    private ColorRGBA internalField1309;
    private ColorRGBA internalField1310;
    private ColorRGBA internalField1612;
    private ColorRGBA internalField1614;
    private float internalField0205;
    private float internalField0206;
    private float internalField1048;
    private float internalField1047;
    private float internalField1049;
    private float internalField1046;
    private float internalField1456;
    private float internalField1457;
    private float internalField1458;
    private float internalField1459;

    public JsonObject internalMethod00421() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("accent", (JsonElement)this.internalField0777.toJson());
        jsonObject.add("background", (JsonElement)this.internalField0776.toJson());
        jsonObject.add("additional", (JsonElement)this.internalField1311.toJson());
        jsonObject.add("text", (JsonElement)this.internalField1312.toJson());
        jsonObject.add("outline", (JsonElement)this.internalField1309.toJson());
        jsonObject.add("flat", (JsonElement)this.internalField1310.toJson());
        jsonObject.add("icons", (JsonElement)this.internalField1612.toJson());
        jsonObject.add("enabledModules", (JsonElement)this.internalField1614.toJson());
        jsonObject.addProperty("hudRounding", (Number)Float.valueOf(this.internalField0205));
        jsonObject.addProperty("blurOffset", (Number)Float.valueOf(this.internalField0206));
        jsonObject.addProperty("hudAlpha", (Number)Float.valueOf(this.internalField1048));
        jsonObject.addProperty("disalphaGlass", (Number)Float.valueOf(this.internalField1047));
        jsonObject.addProperty("glassPower", (Number)Float.valueOf(this.internalField1049));
        jsonObject.addProperty("glassStreng", (Number)Float.valueOf(this.internalField1046));
        jsonObject.addProperty("many", (Number)Float.valueOf(this.internalField1457));
        jsonObject.addProperty("padding", (Number)Float.valueOf(this.internalField1456));
        jsonObject.addProperty("splitters", (Number)Float.valueOf(this.internalField1458));
        jsonObject.addProperty("albomColor", (Number)Float.valueOf(this.internalField1459));
        return jsonObject;
    }

    public static ThemeStyleModel internalMethod01615(JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        float f = jsonObject.has("hudRounding") ? jsonObject.get("hudRounding").getAsFloat() : 0.0f;
        float f2 = jsonObject.has("blurOffset") ? jsonObject.get("blurOffset").getAsFloat() : 0.0f;
        float f3 = jsonObject.has("hudAlpha") ? jsonObject.get("hudAlpha").getAsFloat() : 0.0f;
        float f4 = jsonObject.has("disalphaGlass") ? jsonObject.get("disalphaGlass").getAsFloat() : 0.0f;
        float f5 = jsonObject.has("glassPower") ? jsonObject.get("glassPower").getAsFloat() : 0.0f;
        float f6 = jsonObject.has("glassStreng") ? jsonObject.get("glassStreng").getAsFloat() : 0.0f;
        float f7 = jsonObject.has("many") ? jsonObject.get("many").getAsFloat() : 0.0f;
        float f8 = jsonObject.has("padding") ? jsonObject.get("padding").getAsFloat() : 0.0f;
        float f9 = jsonObject.has("splitters") ? jsonObject.get("splitters").getAsFloat() : 0.0f;
        float f10 = jsonObject.has("albomColor") ? jsonObject.get("albomColor").getAsFloat() : 0.0f;
        ColorRGBA colorRGBA = jsonObject.has("accent") ? ColorRGBA.fromJson(jsonObject.getAsJsonObject("accent")) : new ColorRGBA(0.0f, 0.0f, 0.0f, 0.0f);
        ColorRGBA colorRGBA2 = jsonObject.has("background") ? ColorRGBA.fromJson(jsonObject.getAsJsonObject("background")) : new ColorRGBA(0.0f, 0.0f, 0.0f, 0.0f);
        ColorRGBA colorRGBA3 = jsonObject.has("additional") ? ColorRGBA.fromJson(jsonObject.getAsJsonObject("additional")) : new ColorRGBA(0.0f, 0.0f, 0.0f, 0.0f);
        ColorRGBA colorRGBA4 = jsonObject.has("text") ? ColorRGBA.fromJson(jsonObject.getAsJsonObject("text")) : new ColorRGBA(255.0f, 255.0f, 255.0f, 255.0f);
        ColorRGBA colorRGBA5 = jsonObject.has("outline") ? ColorRGBA.fromJson(jsonObject.getAsJsonObject("outline")) : new ColorRGBA(0.0f, 0.0f, 0.0f, 0.0f);
        ColorRGBA colorRGBA6 = jsonObject.has("flat") ? ColorRGBA.fromJson(jsonObject.getAsJsonObject("flat")) : new ColorRGBA(0.0f, 0.0f, 0.0f, 0.0f);
        ColorRGBA colorRGBA7 = jsonObject.has("icons") ? ColorRGBA.fromJson(jsonObject.getAsJsonObject("icons")) : colorRGBA4;
        ColorRGBA colorRGBA8 = jsonObject.has("enabledModules") ? ColorRGBA.fromJson(jsonObject.getAsJsonObject("enabledModules")) : colorRGBA4;
        return new ThemeStyleModel(colorRGBA, colorRGBA2, colorRGBA3, colorRGBA4, colorRGBA5, colorRGBA6, colorRGBA7, colorRGBA8, f, f2, f3, f4, f5, f6, f8, f7, f9, f10);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        ThemeStyleModel typedValue187 = (ThemeStyleModel)object;
        return Float.compare(this.internalField0205, typedValue187.internalField0205) == 0 && Float.compare(this.internalField0206, typedValue187.internalField0206) == 0 && Float.compare(this.internalField1048, typedValue187.internalField1048) == 0 && Float.compare(this.internalField1047, typedValue187.internalField1047) == 0 && Float.compare(this.internalField1049, typedValue187.internalField1049) == 0 && Float.compare(this.internalField1046, typedValue187.internalField1046) == 0 && Float.compare(this.internalField1456, typedValue187.internalField1456) == 0 && Float.compare(this.internalField1457, typedValue187.internalField1457) == 0 && Float.compare(this.internalField1458, typedValue187.internalField1458) == 0 && Float.compare(this.internalField1459, typedValue187.internalField1459) == 0 && Objects.equals(this.internalField0777, typedValue187.internalField0777) && Objects.equals(this.internalField0776, typedValue187.internalField0776) && Objects.equals(this.internalField1311, typedValue187.internalField1311) && Objects.equals(this.internalField1312, typedValue187.internalField1312) && Objects.equals(this.internalField1309, typedValue187.internalField1309) && Objects.equals(this.internalField1310, typedValue187.internalField1310) && Objects.equals(this.internalField1612, typedValue187.internalField1612) && Objects.equals(this.internalField1614, typedValue187.internalField1614);
    }

    public int hashCode() {
        return Objects.hash(this.internalField0777, this.internalField0776, this.internalField1311, this.internalField1312, this.internalField1309, this.internalField1310, this.internalField1612, this.internalField1614, Float.valueOf(this.internalField0205), Float.valueOf(this.internalField0206), Float.valueOf(this.internalField1048), Float.valueOf(this.internalField1047), Float.valueOf(this.internalField1049), Float.valueOf(this.internalField1046), Float.valueOf(this.internalField1456), Float.valueOf(this.internalField1457), Float.valueOf(this.internalField1458), Float.valueOf(this.internalField1459));
    }

    public ThemeStyleModel internalMethod05287() {
        return new ThemeStyleModel(this.internalField0777, this.internalField0776, this.internalField1311, this.internalField1312, this.internalField1309, this.internalField1310, this.internalField1612, this.internalField1614, this.internalField0205, this.internalField0206, this.internalField1048, this.internalField1047, this.internalField1049, this.internalField1046, this.internalField1456, this.internalField1457, this.internalField1458, this.internalField1459);
    }

    @Generated
    public ColorRGBA internalMethod01286() {
        return this.internalField0777;
    }

    @Generated
    public ColorRGBA internalMethod03957() {
        return this.internalField0776;
    }

    @Generated
    public ColorRGBA internalMethod07999() {
        return this.internalField1311;
    }

    @Generated
    public ColorRGBA internalMethod08833() {
        return this.internalField1312;
    }

    @Generated
    public ColorRGBA internalMethod08716() {
        return this.internalField1309;
    }

    @Generated
    public ColorRGBA internalMethod07983() {
        return this.internalField1310;
    }

    @Generated
    public ColorRGBA internalMethod09585() {
        return this.internalField1612;
    }

    @Generated
    public ColorRGBA internalMethod09184() {
        return this.internalField1614;
    }

    @Generated
    public float internalMethod01359() {
        return this.internalField0205;
    }

    @Generated
    public float internalMethod01360() {
        return this.internalField0206;
    }

    @Generated
    public float internalMethod08704() {
        return this.internalField1048;
    }

    @Generated
    public float internalMethod08705() {
        return this.internalField1047;
    }

    @Generated
    public float internalMethod08718() {
        return this.internalField1049;
    }

    @Generated
    public float internalMethod08719() {
        return this.internalField1046;
    }

    @Generated
    public float internalMethod09714() {
        return this.internalField1456;
    }

    @Generated
    public float internalMethod09715() {
        return this.internalField1457;
    }

    @Generated
    public float internalMethod09721() {
        return this.internalField1458;
    }

    @Generated
    public float internalMethod09722() {
        return this.internalField1459;
    }

    @Generated
    public ThemeStyleModel internalMethod03207(ColorRGBA colorRGBA) {
        this.internalField0777 = colorRGBA;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod06852(ColorRGBA colorRGBA) {
        this.internalField0776 = colorRGBA;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod09079(ColorRGBA colorRGBA) {
        this.internalField1311 = colorRGBA;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod08210(ColorRGBA colorRGBA) {
        this.internalField1312 = colorRGBA;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod08771(ColorRGBA colorRGBA) {
        this.internalField1309 = colorRGBA;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod07906(ColorRGBA colorRGBA) {
        this.internalField1310 = colorRGBA;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod09885(ColorRGBA colorRGBA) {
        this.internalField1612 = colorRGBA;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod09433(ColorRGBA colorRGBA) {
        this.internalField1614 = colorRGBA;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod00996(float f) {
        this.internalField0205 = f;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod05959(float f) {
        this.internalField0206 = f;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod08666(float f) {
        this.internalField1048 = f;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod08066(float f) {
        this.internalField1047 = f;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod08720(float f) {
        this.internalField1049 = f;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod08106(float f) {
        this.internalField1046 = f;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod09478(float f) {
        this.internalField1456 = f;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod09735(float f) {
        this.internalField1457 = f;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod09488(float f) {
        this.internalField1458 = f;
        return this;
    }

    @Generated
    public ThemeStyleModel internalMethod09157(float f) {
        this.internalField1459 = f;
        return this;
    }

    @Generated
    public ThemeStyleModel(ColorRGBA colorRGBA, ColorRGBA colorRGBA2, ColorRGBA colorRGBA3, ColorRGBA colorRGBA4, ColorRGBA colorRGBA5, ColorRGBA colorRGBA6, ColorRGBA colorRGBA7, ColorRGBA colorRGBA8, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        this.internalField0777 = colorRGBA;
        this.internalField0776 = colorRGBA2;
        this.internalField1311 = colorRGBA3;
        this.internalField1312 = colorRGBA4;
        this.internalField1309 = colorRGBA5;
        this.internalField1310 = colorRGBA6;
        this.internalField1612 = colorRGBA7;
        this.internalField1614 = colorRGBA8;
        this.internalField0205 = f;
        this.internalField0206 = f2;
        this.internalField1048 = f3;
        this.internalField1047 = f4;
        this.internalField1049 = f5;
        this.internalField1046 = f6;
        this.internalField1456 = f7;
        this.internalField1457 = f8;
        this.internalField1458 = f9;
        this.internalField1459 = f10;
    }
}


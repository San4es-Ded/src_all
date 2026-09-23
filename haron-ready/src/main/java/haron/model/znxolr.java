package haron.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import haron.core.BooleanCoercion;
import haron.model.g42uq5;
import haron.model.h3ql66;
import haron.model.hnlz1c;
import haron.model.l926yt;
import haron.model.qvj2pe;
import haron.model.yur0a9;
import java.util.HashMap;
import java.util.Iterator;
import ru.haron.Haron;

public class znxolr {
    public static int a;
    public static boolean b;

    private static g42uq5 b(JsonObject jsonObject, int n, int n2) {
        float[] fArray = new float[]{0.0f, 0.0f, 0.0f};
        float[] fArray2 = znxolr.a(jsonObject, "crypt", fArray);
        float[] fArray3 = new float[]{1.0f, 1.0f, 1.0f};
        float[] fArray4 = znxolr.a(jsonObject, "crypt", fArray3);
        float[] fArray5 = znxolr.a(jsonObject, "crypt", (float[])fArray2.clone());
        float[] fArray6 = new float[]{0.0f, 0.0f, 0.0f};
        float[] fArray7 = znxolr.a(jsonObject, "crypt", fArray6);
        float f = !jsonObject.has("crypt") ? 0.0f : jsonObject.get("crypt").getAsFloat();
        int n3 = jsonObject.has("crypt") && jsonObject.get("crypt").getAsBoolean() ? 1 : 0;
        g42uq5 g42uq52 = new g42uq5(fArray4[0], fArray4[1], fArray4[2]);
        g42uq52.c = new yur0a9(-fArray5[0], fArray5[1], fArray5[2]);
        g42uq52.d = new yur0a9((float)Math.toRadians(-fArray7[0]), (float)Math.toRadians(-fArray7[1]), (float)Math.toRadians(fArray7[2]));
        g42uq52.e = f;
        g42uq52.f = BooleanCoercion.from(n3);
        znxolr.a(g42uq52, fArray2, fArray4, f, BooleanCoercion.from(n3), jsonObject, n, n2);
        return g42uq52;
    }

    private static float[] a(JsonObject jsonObject, String string, float[] fArray) {
        if (!jsonObject.has(string)) {
            return fArray;
        }
        JsonArray jsonArray = jsonObject.getAsJsonArray(string);
        float[] fArray2 = new float[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); ++i) {
            fArray2[i] = jsonArray.get(i).getAsFloat();
        }
        return fArray2;
    }

    private static hnlz1c[] a(String string, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        hnlz1c[] hnlz1cArray = new hnlz1c[4];
        float f11 = f7 + f9;
        float f12 = f8 + f10;
        switch (string) {
            case "crypt_uv0": {
                hnlz1cArray[0] = new hnlz1c(f4, f5, f3, f7, f8);
                hnlz1cArray[1] = new hnlz1c(f, f5, f3, f11, f8);
                hnlz1cArray[2] = new hnlz1c(f, f2, f3, f11, f12);
                hnlz1cArray[3] = new hnlz1c(f4, f2, f3, f7, f12);
                break;
            }
            case "crypt_uv1": {
                hnlz1cArray[0] = new hnlz1c(f, f5, f6, f7, f8);
                hnlz1cArray[1] = new hnlz1c(f4, f5, f6, f11, f8);
                hnlz1cArray[2] = new hnlz1c(f4, f2, f6, f11, f12);
                hnlz1cArray[3] = new hnlz1c(f, f2, f6, f7, f12);
                break;
            }
            case "crypt_uv2": {
                hnlz1cArray[0] = new hnlz1c(f4, f5, f6, f7, f8);
                hnlz1cArray[1] = new hnlz1c(f4, f5, f3, f11, f8);
                hnlz1cArray[2] = new hnlz1c(f4, f2, f3, f11, f12);
                hnlz1cArray[3] = new hnlz1c(f4, f2, f6, f7, f12);
                break;
            }
            case "crypt_uv3": {
                hnlz1cArray[0] = new hnlz1c(f, f5, f3, f7, f8);
                hnlz1cArray[1] = new hnlz1c(f, f5, f6, f11, f8);
                hnlz1cArray[2] = new hnlz1c(f, f2, f6, f11, f12);
                hnlz1cArray[3] = new hnlz1c(f, f2, f3, f7, f12);
                break;
            }
            case "crypt_uv4": {
                hnlz1cArray[0] = new hnlz1c(f, f5, f3, f7, f8);
                hnlz1cArray[1] = new hnlz1c(f, f5, f6, f7, f12);
                hnlz1cArray[2] = new hnlz1c(f4, f5, f6, f11, f12);
                hnlz1cArray[3] = new hnlz1c(f4, f5, f3, f11, f8);
                break;
            }
            case "crypt_uv5": {
                hnlz1cArray[0] = new hnlz1c(f4, f2, f3, f7, f8);
                hnlz1cArray[1] = new hnlz1c(f4, f2, f6, f7, f12);
                hnlz1cArray[2] = new hnlz1c(f, f2, f6, f11, f12);
                hnlz1cArray[3] = new hnlz1c(f, f2, f3, f11, f8);
            }
        }
        return hnlz1cArray;
    }

    public static l926yt a(String string) {
        try {
            return znxolr.a(new JsonParser().parse(string).getAsJsonObject());
        }
        catch (Exception exception) {
            Haron.getLOGGER().error("crypt", (Throwable)exception);
            return null;
        }
    }

    private static qvj2pe a(JsonObject jsonObject, String string, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, int n, int n2) {
        if (!jsonObject.has(string)) {
            return null;
        }
        JsonObject jsonObject2 = jsonObject.getAsJsonObject(string);
        JsonArray jsonArray = jsonObject2.getAsJsonArray("တ✿ၶ̨ၳ");
        JsonArray jsonArray2 = jsonObject2.getAsJsonArray("crypt");
        return new qvj2pe(znxolr.a(string, f, f2, f3, f4, f5, f6, jsonArray.get(0).getAsFloat() / (float)n, jsonArray.get(1).getAsFloat() / (float)n2, jsonArray2.get(0).getAsFloat() / (float)n, jsonArray2.get(1).getAsFloat() / (float)n2), f7, f8, f9);
    }

    private static h3ql66 a(JsonObject jsonObject, int n, int n2) {
        h3ql66 h3ql662 = new h3ql66(jsonObject.get("crypt").getAsString());
        if (jsonObject.has("crypt")) {
            JsonArray origin = jsonObject.getAsJsonArray("crypt");
            h3ql662.f = -origin.get(0).getAsFloat();
            h3ql662.g = origin.get(1).getAsFloat();
            h3ql662.h = origin.get(2).getAsFloat();
        }
        if (jsonObject.has("crypt")) {
            JsonArray rotation = jsonObject.getAsJsonArray("crypt");
            h3ql662.a((float)Math.toRadians(-rotation.get(0).getAsFloat()));
            h3ql662.b((float)Math.toRadians(-rotation.get(1).getAsFloat()));
            h3ql662.c((float)Math.toRadians(rotation.get(2).getAsFloat()));
        }
        if (jsonObject.has("crypt")) {
            Iterator<JsonElement> cubes = jsonObject.getAsJsonArray("crypt").iterator();
            while (cubes.hasNext()) {
                h3ql662.c.add(znxolr.b(cubes.next().getAsJsonObject(), n, n2));
            }
        }
        return h3ql662;
    }

    private static void a(g42uq5 g42uq52, float[] fArray, float[] fArray2, float f, boolean bl, JsonObject jsonObject, int n, int n2) {
        float f2 = fArray[0];
        float f3 = fArray[1];
        float f4 = fArray[2];
        float f5 = f2 - f;
        float f6 = f3 - f;
        float f7 = f4 - f;
        float f8 = fArray2[0] + f * 2.0f;
        float f9 = fArray2[1] + f * 2.0f;
        float f10 = fArray2[2] + f * 2.0f;
        float f11 = 0.0f;
        float f12 = 0.0f;
        boolean bl2 = false;
        JsonObject jsonObject2 = null;
        if (jsonObject.has("crypt")) {
            JsonElement jsonElement = jsonObject.get("crypt");
            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                f11 = jsonArray.get(0).getAsFloat();
                f12 = jsonArray.get(1).getAsFloat();
            } else if (jsonElement.isJsonObject()) {
                bl2 = true;
                jsonObject2 = jsonElement.getAsJsonObject();
            }
        }
        float f13 = -(f5 + f8) / 16.0f;
        float f14 = f6 / 16.0f;
        float f15 = f7 / 16.0f;
        float f16 = -f5 / 16.0f;
        float f17 = (f6 + f9) / 16.0f;
        float f18 = (f7 + f10) / 16.0f;
        if (bl2) {
            if (jsonObject2 != null) {
                g42uq52.a[0] = znxolr.a(jsonObject2, "crypt", f13, f14, f15, f13, f17, f18, -1.0f, 0.0f, 0.0f, n, n2);
                g42uq52.a[1] = znxolr.a(jsonObject2, "crypt", f16, f14, f15, f16, f17, f18, 1.0f, 0.0f, 0.0f, n, n2);
                g42uq52.a[2] = znxolr.a(jsonObject2, "crypt", f13, f14, f15, f16, f14, f18, 0.0f, -1.0f, 0.0f, n, n2);
                g42uq52.a[3] = znxolr.a(jsonObject2, "crypt", f13, f17, f15, f16, f17, f18, 0.0f, 1.0f, 0.0f, n, n2);
                g42uq52.a[4] = znxolr.a(jsonObject2, "crypt", f13, f14, f15, f16, f17, f15, 0.0f, 0.0f, -1.0f, n, n2);
                g42uq52.a[5] = znxolr.a(jsonObject2, "crypt", f13, f14, f18, f16, f17, f18, 0.0f, 0.0f, 1.0f, n, n2);
                return;
            }
        } else if (b) {
            throw new IllegalAccessError();
        }
        float f19 = n;
        float f20 = n2;
        g42uq52.a[0] = znxolr.a(f13, f14, f15, f13, f17, f18, -1.0f, 0.0f, 0.0f, f11, f12, f10, f9, f8, f19, f20, "crypt");
        g42uq52.a[1] = znxolr.a(f16, f14, f15, f16, f17, f18, 1.0f, 0.0f, 0.0f, f11, f12, f10, f9, f8, f19, f20, "crypt");
        g42uq52.a[2] = znxolr.a(f13, f14, f15, f16, f14, f18, 0.0f, -1.0f, 0.0f, f11, f12, f10, f9, f8, f19, f20, "crypt");
        g42uq52.a[3] = znxolr.a(f13, f17, f15, f16, f17, f18, 0.0f, 1.0f, 0.0f, f11, f12, f10, f9, f8, f19, f20, "crypt");
        g42uq52.a[4] = znxolr.a(f13, f14, f15, f16, f17, f15, 0.0f, 0.0f, -1.0f, f11, f12, f10, f9, f8, f19, f20, "crypt");
        g42uq52.a[5] = znxolr.a(f13, f14, f18, f16, f17, f18, 0.0f, 0.0f, 1.0f, f11, f12, f10, f9, f8, f19, f20, "crypt");
    }

    private static l926yt a(JsonObject jsonObject) {
        JsonArray jsonArray = jsonObject.getAsJsonArray("crypt");
        if (jsonArray == null || jsonArray.size() == 0) {
            Haron.getLOGGER().error("crypt");
            return null;
        }
        JsonObject jsonObject2 = jsonArray.get(0).getAsJsonObject();
        JsonObject jsonObject3 = jsonObject2.getAsJsonObject("crypt");
        int n = !jsonObject3.has("crypt") ? 64 : jsonObject3.get("crypt").getAsInt();
        int n2 = jsonObject3.has("crypt") ? jsonObject3.get("crypt").getAsInt() : 64;
        int n3 = n2;
        l926yt l926yt2 = new l926yt();
        l926yt2.b = n;
        l926yt2.c = n3;
        JsonArray jsonArray2 = jsonObject2.getAsJsonArray("crypt");
        if (jsonArray2 != null) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            Iterator iterator = jsonArray2.iterator();
            while (iterator.hasNext()) {
                h3ql66 part = znxolr.a(((JsonElement)iterator.next()).getAsJsonObject(), n, n3);
                hashMap.put(part.d, part);
            }
            Iterator<JsonElement> parts = jsonArray2.iterator();
            while (parts.hasNext()) {
                JsonObject jsonObject4 = parts.next().getAsJsonObject();
                h3ql66 h3ql662 = (h3ql66)hashMap.get(jsonObject4.get("crypt").getAsString());
                if (jsonObject4.has("crypt")) {
                    h3ql66 h3ql663 = (h3ql66)hashMap.get(jsonObject4.get("crypt").getAsString());
                    if (h3ql663 == null) continue;
                    h3ql663.b.add(h3ql662);
                    h3ql662.a = h3ql663;
                    continue;
                }
                l926yt2.a.add(h3ql662);
            }
            if (b) {
                throw new IllegalAccessError();
            }
        }
        Haron.getLOGGER().info("crypt", (Object)l926yt2.a.size());
        return l926yt2;
    }

    private static qvj2pe a(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, String string) {
        float f17 = 0.0f;
        float f18 = 0.0f;
        float f19 = 0.0f;
        return new qvj2pe(znxolr.a(string, f, f2, f3, f4, f5, f6, f19, f18, f17, switch (string) {
            case "crypt_face0" -> {
                f19 = (f10 + f12 + f14) / f15;
                f18 = (f11 + f12) / f16;
                f17 = f14 / f15;
                yield f13 / f16;
            }
            case "crypt_face1" -> {
                f19 = (f10 + f12 + f14 + f12) / f15;
                f18 = (f11 + f12) / f16;
                f17 = f14 / f15;
                yield f13 / f16;
            }
            case "crypt_face2" -> {
                f19 = f10 / f15;
                f18 = (f11 + f12) / f16;
                f17 = f12 / f15;
                yield f13 / f16;
            }
            case "crypt_face3" -> {
                f19 = (f10 + f12 + f14) / f15;
                f18 = (f11 + f12) / f16;
                f17 = f12 / f15;
                yield f13 / f16;
            }
            case "crypt_face4" -> {
                f19 = (f10 + f12) / f15;
                f18 = f11 / f16;
                f17 = f14 / f15;
                yield f12 / f16;
            }
            case "crypt_face5" -> {
                f19 = (f10 + f12 + f14) / f15;
                f18 = f11 / f16;
                f17 = f14 / f15;
                yield f12 / f16;
            }
            default -> {
                f19 = 0.0f;
                f18 = 0.0f;
                f17 = 0.0f;
                yield 0.0f;
            }
        }), f7, f8, f9);
    }
}

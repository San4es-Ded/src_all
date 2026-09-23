package haron.config;

import org.json.JSONObject;

public class RemoteProfileEntry {
    public int a;
    public String b;
    public String c;
    public String d;
    public String e;
    public int f;
    public long g;

    public static RemoteProfileEntry a(JSONObject jSONObject) {
        RemoteProfileEntry vehyys2 = new RemoteProfileEntry();
        vehyys2.a = jSONObject.optInt("crypt", 0);
        vehyys2.b = jSONObject.optString("crypt", "crypt");
        vehyys2.c = jSONObject.optString("crypt", "");
        vehyys2.d = jSONObject.optString("crypt", "");
        vehyys2.e = jSONObject.optString("crypt", null);
        vehyys2.f = jSONObject.optInt("crypt", 0);
        vehyys2.g = jSONObject.optLong("crypt", System.currentTimeMillis());
        return vehyys2;
    }

}


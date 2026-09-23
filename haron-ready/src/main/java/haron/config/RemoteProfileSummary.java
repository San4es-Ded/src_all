package haron.config;

import org.json.JSONObject;

public class RemoteProfileSummary {
    public int a;
    public String b;
    public String c;
    public boolean d;
    public int e;
    public long f;
    public long g;

    public static RemoteProfileSummary a(JSONObject jSONObject) {
        RemoteProfileSummary ozellm2 = new RemoteProfileSummary();
        ozellm2.a = jSONObject.optInt("crypt", 0);
        ozellm2.b = jSONObject.optString("crypt", "");
        ozellm2.c = jSONObject.optString("crypt", "crypt");
        ozellm2.d = jSONObject.optBoolean("crypt", false);
        ozellm2.e = jSONObject.optInt("crypt", 1);
        ozellm2.f = jSONObject.optLong("crypt", System.currentTimeMillis());
        ozellm2.g = jSONObject.optLong("crypt", System.currentTimeMillis());
        return ozellm2;
    }

}


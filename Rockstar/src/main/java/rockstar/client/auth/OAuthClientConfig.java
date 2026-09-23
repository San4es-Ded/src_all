package rockstar.client.auth;



import rockstar.client.data.*;
import rockstar.client.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.auth.*;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import rockstar.client.internal.auth.MsaEnvironment;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.core.UuidUtils;

public final class OAuthClientConfig {
    private final String internalField0248;
    private final String internalField0247;
    private final String internalField1077;
    private final String internalField1076;
    private final MsaEnvironment internalField0726;

    public static OAuthClientConfig internalMethod07648(JsonObject jsonObject) {
        return OAuthClientConfig.internalMethod00614(new JsonObjectNode(jsonObject));
    }

    public static OAuthClientConfig internalMethod00614(JsonObjectNode typedValue030) {
        return new OAuthClientConfig(typedValue030.internalMethod03457("clientId"), typedValue030.internalMethod03457("scope"), typedValue030.internalMethod01170("clientSecret", null), typedValue030.internalMethod01170("redirectUri", null), MsaEnvironment.valueOf(typedValue030.internalMethod01170("environment", MsaEnvironment.internalField0726.name())));
    }

    public static JsonObject internalMethod00727(OAuthClientConfig typedValue071) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_saveVersion", (Number)1);
        jsonObject.addProperty("clientId", typedValue071.internalField0248);
        jsonObject.addProperty("scope", typedValue071.internalField0247);
        jsonObject.addProperty("clientSecret", typedValue071.internalField1077);
        jsonObject.addProperty("redirectUri", typedValue071.internalField1076);
        jsonObject.addProperty("environment", typedValue071.internalField0726.name());
        return jsonObject;
    }

    public OAuthClientConfig(String string, String string2) {
        this(string, string2, null, null, MsaEnvironment.internalField0726);
    }

    public boolean internalMethod06891() {
        return !UuidUtils.internalMethod06432(this.internalField0248);
    }

    public Map<String, String> internalMethod00213() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("client_id", this.internalField0248);
        hashMap.put("scope", this.internalField0247);
        if (this.internalField1076 != null) {
            hashMap.put("redirect_uri", this.internalField1076);
        }
        hashMap.put("response_type", "code");
        hashMap.put("response_mode", "query");
        return hashMap;
    }

    @Generated
    public String internalMethod06157() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod02674() {
        return this.internalField0247;
    }

    @Generated
    public String internalMethod07955() {
        return this.internalField1077;
    }

    @Generated
    public String internalMethod08847() {
        return this.internalField1076;
    }

    @Generated
    public MsaEnvironment internalMethod01376() {
        return this.internalField0726;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof OAuthClientConfig)) {
            return false;
        }
        OAuthClientConfig typedValue071 = (OAuthClientConfig)object;
        String string = this.internalMethod06157();
        String string2 = typedValue071.internalMethod06157();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.internalMethod02674();
        String string4 = typedValue071.internalMethod02674();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.internalMethod07955();
        String string6 = typedValue071.internalMethod07955();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.internalMethod08847();
        String string8 = typedValue071.internalMethod08847();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        MsaEnvironment typedValue068 = this.internalMethod01376();
        MsaEnvironment typedValue069 = typedValue071.internalMethod01376();
        return !(typedValue068 == null ? typedValue069 != null : !((Object)((Object)typedValue068)).equals((Object)typedValue069));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        String string = this.internalMethod06157();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.internalMethod02674();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.internalMethod07955();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.internalMethod08847();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        MsaEnvironment typedValue068 = this.internalMethod01376();
        n2 = n2 * 59 + (typedValue068 == null ? 43 : ((Object)((Object)typedValue068)).hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "MsaApplicationConfig(clientId=" + this.internalMethod06157() + ", scope=" + this.internalMethod02674() + ", clientSecret=" + this.internalMethod07955() + ", redirectUri=" + this.internalMethod08847() + ", environment=" + (Object)((Object)this.internalMethod01376()) + ")";
    }

    @Generated
    public OAuthClientConfig internalMethod07234(String string) {
        return this.internalField0248 == string ? this : new OAuthClientConfig(string, this.internalField0247, this.internalField1077, this.internalField1076, this.internalField0726);
    }

    @Generated
    public OAuthClientConfig internalMethod00633(String string) {
        return this.internalField0247 == string ? this : new OAuthClientConfig(this.internalField0248, string, this.internalField1077, this.internalField1076, this.internalField0726);
    }

    @Generated
    public OAuthClientConfig internalMethod08299(String string) {
        return this.internalField1077 == string ? this : new OAuthClientConfig(this.internalField0248, this.internalField0247, string, this.internalField1076, this.internalField0726);
    }

    @Generated
    public OAuthClientConfig internalMethod08561(String string) {
        return this.internalField1076 == string ? this : new OAuthClientConfig(this.internalField0248, this.internalField0247, this.internalField1077, string, this.internalField0726);
    }

    @Generated
    public OAuthClientConfig internalMethod05566(MsaEnvironment typedValue068) {
        return this.internalField0726 == typedValue068 ? this : new OAuthClientConfig(this.internalField0248, this.internalField0247, this.internalField1077, this.internalField1076, typedValue068);
    }

    @Generated
    public OAuthClientConfig(String string, String string2, String string3, String string4, MsaEnvironment typedValue068) {
        this.internalField0248 = string;
        this.internalField0247 = string2;
        this.internalField1077 = string3;
        this.internalField1076 = string4;
        this.internalField0726 = typedValue068;
    }
}


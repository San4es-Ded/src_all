package rockstar.client.auth;



import rockstar.client.data.*;
import rockstar.client.*;
import rockstar.client.internal.core.*;
import com.google.gson.JsonObject;
import lombok.Generated;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.core.ExpiringToken;

public final class OAuthToken
implements ExpiringToken {
    private final long internalField0229;
    private final String internalField0248;
    private final String internalField0247;

    public static OAuthToken internalMethod00241(JsonObject jsonObject) {
        return OAuthToken.internalMethod05862(new JsonObjectNode(jsonObject));
    }

    public static OAuthToken internalMethod05862(JsonObjectNode typedValue030) {
        return new OAuthToken(typedValue030.internalMethod02177("expireTimeMs"), typedValue030.internalMethod03457("accessToken"), typedValue030.internalMethod01170("refreshToken", null));
    }

    public static JsonObject internalMethod05498(OAuthToken typedValue074) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_saveVersion", (Number)1);
        jsonObject.addProperty("expireTimeMs", (Number)typedValue074.internalField0229);
        jsonObject.addProperty("accessToken", typedValue074.internalField0248);
        jsonObject.addProperty("refreshToken", typedValue074.internalField0247);
        return jsonObject;
    }

    @Generated
    public OAuthToken(long l, String string, String string2) {
        this.internalField0229 = l;
        this.internalField0248 = string;
        this.internalField0247 = string2;
    }

    @Override
    @Generated
    public long internalMethod03800() {
        return this.internalField0229;
    }

    @Generated
    public String internalMethod01950() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod06617() {
        return this.internalField0247;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof OAuthToken)) {
            return false;
        }
        OAuthToken typedValue074 = (OAuthToken)object;
        if (this.internalMethod03800() != typedValue074.internalMethod03800()) {
            return false;
        }
        String string = this.internalMethod01950();
        String string2 = typedValue074.internalMethod01950();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.internalMethod06617();
        String string4 = typedValue074.internalMethod06617();
        return !(string3 == null ? string4 != null : !string3.equals(string4));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        long l = this.internalMethod03800();
        n2 = n2 * 59 + (int)(l >>> 32 ^ l);
        String string = this.internalMethod01950();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.internalMethod06617();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "MsaToken(expireTimeMs=" + this.internalMethod03800() + ", accessToken=" + this.internalMethod01950() + ", refreshToken=" + this.internalMethod06617() + ")";
    }
}


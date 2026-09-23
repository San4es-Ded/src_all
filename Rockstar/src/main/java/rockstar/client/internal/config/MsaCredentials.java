package rockstar.client.internal.config;



import rockstar.client.data.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import lombok.Generated;
import rockstar.client.data.JsonObjectNode;

public final class MsaCredentials {
    private final String internalField0248;
    private final String internalField0247;

    public static MsaCredentials internalMethod04557(JsonObject jsonObject) {
        return MsaCredentials.internalMethod02697(new JsonObjectNode(jsonObject));
    }

    public static MsaCredentials internalMethod02697(JsonObjectNode typedValue030) {
        return new MsaCredentials(typedValue030.internalMethod03457("email"), typedValue030.internalMethod03457("password"));
    }

    public static JsonObject internalMethod02582(MsaCredentials typedValue072) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_saveVersion", (Number)1);
        jsonObject.addProperty("email", typedValue072.internalField0248);
        jsonObject.addProperty("password", typedValue072.internalField0247);
        return jsonObject;
    }

    @Generated
    public MsaCredentials(String string, String string2) {
        this.internalField0248 = string;
        this.internalField0247 = string2;
    }

    @Generated
    public String internalMethod00235() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod04989() {
        return this.internalField0247;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof MsaCredentials)) {
            return false;
        }
        MsaCredentials typedValue072 = (MsaCredentials)object;
        String string = this.internalMethod00235();
        String string2 = typedValue072.internalMethod00235();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.internalMethod04989();
        String string4 = typedValue072.internalMethod04989();
        return !(string3 == null ? string4 != null : !string3.equals(string4));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        String string = this.internalMethod00235();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.internalMethod04989();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "MsaCredentials(email=" + this.internalMethod00235() + ", password=" + this.internalMethod04989() + ")";
    }
}


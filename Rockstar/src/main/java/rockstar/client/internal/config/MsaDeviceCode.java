package rockstar.client.internal.config;




import rockstar.client.data.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import lombok.Generated;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.core.ExpiringToken;

public final class MsaDeviceCode
implements ExpiringToken {
    private final long internalField0229;
    private final long internalField0230;
    private final String internalField0248;
    private final String internalField0247;
    private final String internalField1077;

    public static MsaDeviceCode internalMethod03325(JsonObject jsonObject) {
        return MsaDeviceCode.internalMethod03747(new JsonObjectNode(jsonObject));
    }

    public static MsaDeviceCode internalMethod03747(JsonObjectNode typedValue030) {
        return new MsaDeviceCode(typedValue030.internalMethod02177("expireTimeMs"), typedValue030.internalMethod02177("intervalMs"), typedValue030.internalMethod03457("deviceCode"), typedValue030.internalMethod03457("userCode"), typedValue030.internalMethod03457("verificationUri"));
    }

    public static JsonObject internalMethod03540(MsaDeviceCode typedValue073) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_saveVersion", (Number)1);
        jsonObject.addProperty("expireTimeMs", (Number)typedValue073.internalField0229);
        jsonObject.addProperty("intervalMs", (Number)typedValue073.internalField0230);
        jsonObject.addProperty("deviceCode", typedValue073.internalField0248);
        jsonObject.addProperty("userCode", typedValue073.internalField0247);
        jsonObject.addProperty("verificationUri", typedValue073.internalField1077);
        return jsonObject;
    }

    public String internalMethod02119() {
        return this.internalField1077 + "?otc=" + this.internalField0247;
    }

    @Generated
    public MsaDeviceCode(long l, long l2, String string, String string2, String string3) {
        this.internalField0229 = l;
        this.internalField0230 = l2;
        this.internalField0248 = string;
        this.internalField0247 = string2;
        this.internalField1077 = string3;
    }

    @Override
    @Generated
    public long internalMethod03800() {
        return this.internalField0229;
    }

    @Generated
    public long internalMethod06388() {
        return this.internalField0230;
    }

    @Generated
    public String internalMethod04386() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod08574() {
        return this.internalField0247;
    }

    @Generated
    public String internalMethod07932() {
        return this.internalField1077;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof MsaDeviceCode)) {
            return false;
        }
        MsaDeviceCode typedValue073 = (MsaDeviceCode)object;
        if (this.internalMethod03800() != typedValue073.internalMethod03800()) {
            return false;
        }
        if (this.internalMethod06388() != typedValue073.internalMethod06388()) {
            return false;
        }
        String string = this.internalMethod04386();
        String string2 = typedValue073.internalMethod04386();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.internalMethod08574();
        String string4 = typedValue073.internalMethod08574();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.internalMethod07932();
        String string6 = typedValue073.internalMethod07932();
        return !(string5 == null ? string6 != null : !string5.equals(string6));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        long l = this.internalMethod03800();
        n2 = n2 * 59 + (int)(l >>> 32 ^ l);
        long l2 = this.internalMethod06388();
        n2 = n2 * 59 + (int)(l2 >>> 32 ^ l2);
        String string = this.internalMethod04386();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.internalMethod08574();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.internalMethod07932();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "MsaDeviceCode(expireTimeMs=" + this.internalMethod03800() + ", intervalMs=" + this.internalMethod06388() + ", deviceCode=" + this.internalMethod04386() + ", userCode=" + this.internalMethod08574() + ", verificationUri=" + this.internalMethod07932() + ")";
    }
}


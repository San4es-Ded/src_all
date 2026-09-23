package rockstar.client.internal.auth;




import rockstar.client.data.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import java.time.Instant;
import lombok.Generated;
import org.jetbrains.annotations.ApiStatus;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.core.ExpiringToken;

public final class XblXstsToken
implements ExpiringToken {
    private final long internalField0229;
    private final String internalField0248;
    private final String internalField0247;

    public static XblXstsToken internalMethod07224(JsonObject jsonObject) {
        return XblXstsToken.internalMethod06637(new JsonObjectNode(jsonObject));
    }

    public static XblXstsToken internalMethod06637(JsonObjectNode typedValue030) {
        return new XblXstsToken(typedValue030.internalMethod02177("expireTimeMs"), typedValue030.internalMethod03457("token"), typedValue030.internalMethod03457("userHash"));
    }

    public static JsonObject internalMethod06839(XblXstsToken typedValue043) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_saveVersion", (Number)1);
        jsonObject.addProperty("expireTimeMs", (Number)typedValue043.internalField0229);
        jsonObject.addProperty("token", typedValue043.internalField0248);
        jsonObject.addProperty("userHash", typedValue043.internalField0247);
        return jsonObject;
    }

    @ApiStatus.Internal
    public static XblXstsToken internalMethod04454(JsonObjectNode typedValue030) {
        return new XblXstsToken(Instant.parse(typedValue030.internalMethod03457("NotAfter")).toEpochMilli(), typedValue030.internalMethod03457("Token"), typedValue030.internalMethod03706("DisplayClaims").internalMethod03703("xui").internalMethod06594(0).internalMethod04512().internalMethod03457("uhs"));
    }

    public String internalMethod02091() {
        return "XBL3.0 x=" + this.internalField0247 + ';' + this.internalField0248;
    }

    @Generated
    public XblXstsToken(long l, String string, String string2) {
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
    public String internalMethod06747() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod08662() {
        return this.internalField0247;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof XblXstsToken)) {
            return false;
        }
        XblXstsToken typedValue043 = (XblXstsToken)object;
        if (this.internalMethod03800() != typedValue043.internalMethod03800()) {
            return false;
        }
        String string = this.internalMethod06747();
        String string2 = typedValue043.internalMethod06747();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.internalMethod08662();
        String string4 = typedValue043.internalMethod08662();
        return !(string3 == null ? string4 != null : !string3.equals(string4));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        long l = this.internalMethod03800();
        n2 = n2 * 59 + (int)(l >>> 32 ^ l);
        String string = this.internalMethod06747();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.internalMethod08662();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "XblXstsToken(expireTimeMs=" + this.internalMethod03800() + ", token=" + this.internalMethod06747() + ", userHash=" + this.internalMethod08662() + ")";
    }
}


package rockstar.client.internal.config;




import rockstar.client.data.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import java.time.Instant;
import lombok.Generated;
import org.jetbrains.annotations.ApiStatus;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.core.ExpiringToken;

public final class XblTitleToken
implements ExpiringToken {
    private final long internalField0229;
    private final String internalField0248;
    private final String internalField0247;

    public static XblTitleToken internalMethod01877(JsonObject jsonObject) {
        return XblTitleToken.internalMethod06742(new JsonObjectNode(jsonObject));
    }

    public static XblTitleToken internalMethod06742(JsonObjectNode typedValue030) {
        return new XblTitleToken(typedValue030.internalMethod02177("expireTimeMs"), typedValue030.internalMethod03457("token"), typedValue030.internalMethod03457("titleId"));
    }

    public static JsonObject internalMethod03846(XblTitleToken typedValue088) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_saveVersion", (Number)1);
        jsonObject.addProperty("expireTimeMs", (Number)typedValue088.internalField0229);
        jsonObject.addProperty("token", typedValue088.internalField0248);
        jsonObject.addProperty("titleId", typedValue088.internalField0247);
        return jsonObject;
    }

    @ApiStatus.Internal
    public static XblTitleToken internalMethod04585(JsonObjectNode typedValue030) {
        return new XblTitleToken(Instant.parse(typedValue030.internalMethod03457("NotAfter")).toEpochMilli(), typedValue030.internalMethod03457("Token"), typedValue030.internalMethod03706("DisplayClaims").internalMethod03706("xti").internalMethod03457("tid"));
    }

    @Generated
    public XblTitleToken(long l, String string, String string2) {
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
    public String internalMethod01911() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod06561() {
        return this.internalField0247;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof XblTitleToken)) {
            return false;
        }
        XblTitleToken typedValue088 = (XblTitleToken)object;
        if (this.internalMethod03800() != typedValue088.internalMethod03800()) {
            return false;
        }
        String string = this.internalMethod01911();
        String string2 = typedValue088.internalMethod01911();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.internalMethod06561();
        String string4 = typedValue088.internalMethod06561();
        return !(string3 == null ? string4 != null : !string3.equals(string4));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        long l = this.internalMethod03800();
        n2 = n2 * 59 + (int)(l >>> 32 ^ l);
        String string = this.internalMethod01911();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.internalMethod06561();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "XblTitleToken(expireTimeMs=" + this.internalMethod03800() + ", token=" + this.internalMethod01911() + ", titleId=" + this.internalMethod06561() + ")";
    }
}


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

public final class XblUserToken
implements ExpiringToken {
    private final long internalField0229;
    private final String internalField0248;
    private final String internalField0247;

    public static XblUserToken internalMethod06846(JsonObject jsonObject) {
        return XblUserToken.internalMethod00778(new JsonObjectNode(jsonObject));
    }

    public static XblUserToken internalMethod00778(JsonObjectNode typedValue030) {
        return new XblUserToken(typedValue030.internalMethod02177("expireTimeMs"), typedValue030.internalMethod03457("token"), typedValue030.internalMethod03457("userHash"));
    }

    public static JsonObject internalMethod05736(XblUserToken typedValue041) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_saveVersion", (Number)1);
        jsonObject.addProperty("expireTimeMs", (Number)typedValue041.internalField0229);
        jsonObject.addProperty("token", typedValue041.internalField0248);
        jsonObject.addProperty("userHash", typedValue041.internalField0247);
        return jsonObject;
    }

    @ApiStatus.Internal
    public static XblUserToken internalMethod06706(JsonObjectNode typedValue030) {
        return new XblUserToken(Instant.parse(typedValue030.internalMethod03457("NotAfter")).toEpochMilli(), typedValue030.internalMethod03457("Token"), typedValue030.internalMethod03706("DisplayClaims").internalMethod03703("xui").internalMethod06594(0).internalMethod04512().internalMethod03457("uhs"));
    }

    @Generated
    public XblUserToken(long l, String string, String string2) {
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
    public String internalMethod04117() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod00676() {
        return this.internalField0247;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof XblUserToken)) {
            return false;
        }
        XblUserToken typedValue041 = (XblUserToken)object;
        if (this.internalMethod03800() != typedValue041.internalMethod03800()) {
            return false;
        }
        String string = this.internalMethod04117();
        String string2 = typedValue041.internalMethod04117();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.internalMethod00676();
        String string4 = typedValue041.internalMethod00676();
        return !(string3 == null ? string4 != null : !string3.equals(string4));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        long l = this.internalMethod03800();
        n2 = n2 * 59 + (int)(l >>> 32 ^ l);
        String string = this.internalMethod04117();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.internalMethod00676();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "XblUserToken(expireTimeMs=" + this.internalMethod03800() + ", token=" + this.internalMethod04117() + ", userHash=" + this.internalMethod00676() + ")";
    }
}


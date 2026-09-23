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

public final class PlayFabEntityToken
implements ExpiringToken {
    private final long internalField0229;
    private final String internalField0248;
    private final String internalField0247;
    private final String internalField1077;

    public static PlayFabEntityToken internalMethod03168(JsonObject jsonObject) {
        return PlayFabEntityToken.internalMethod05463(new JsonObjectNode(jsonObject));
    }

    public static PlayFabEntityToken internalMethod05463(JsonObjectNode typedValue030) {
        return new PlayFabEntityToken(typedValue030.internalMethod02177("expireTimeMs"), typedValue030.internalMethod03457("token"), typedValue030.internalMethod03457("entityId"), typedValue030.internalMethod03457("entityType"));
    }

    public static JsonObject internalMethod02677(PlayFabEntityToken typedValue078) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_saveVersion", (Number)1);
        jsonObject.addProperty("expireTimeMs", (Number)typedValue078.internalField0229);
        jsonObject.addProperty("token", typedValue078.internalField0248);
        jsonObject.addProperty("entityId", typedValue078.internalField0247);
        jsonObject.addProperty("entityType", typedValue078.internalField1077);
        return jsonObject;
    }

    @ApiStatus.Internal
    public static PlayFabEntityToken internalMethod03175(JsonObjectNode typedValue030) {
        JsonObjectNode typedValue031 = typedValue030.internalMethod03706("Entity");
        return new PlayFabEntityToken(Instant.parse(typedValue030.internalMethod03457("TokenExpiration")).toEpochMilli(), typedValue030.internalMethod03457("EntityToken"), typedValue031.internalMethod03457("Id"), typedValue031.internalMethod03457("Type"));
    }

    @Generated
    public PlayFabEntityToken(long l, String string, String string2, String string3) {
        this.internalField0229 = l;
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
    public String internalMethod06972() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod03503() {
        return this.internalField0247;
    }

    @Generated
    public String internalMethod08310() {
        return this.internalField1077;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof PlayFabEntityToken)) {
            return false;
        }
        PlayFabEntityToken typedValue078 = (PlayFabEntityToken)object;
        if (this.internalMethod03800() != typedValue078.internalMethod03800()) {
            return false;
        }
        String string = this.internalMethod06972();
        String string2 = typedValue078.internalMethod06972();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.internalMethod03503();
        String string4 = typedValue078.internalMethod03503();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.internalMethod08310();
        String string6 = typedValue078.internalMethod08310();
        return !(string5 == null ? string6 != null : !string5.equals(string6));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        long l = this.internalMethod03800();
        n2 = n2 * 59 + (int)(l >>> 32 ^ l);
        String string = this.internalMethod06972();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.internalMethod03503();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.internalMethod08310();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "PlayFabEntityToken(expireTimeMs=" + this.internalMethod03800() + ", token=" + this.internalMethod06972() + ", entityId=" + this.internalMethod03503() + ", entityType=" + this.internalMethod08310() + ")";
    }
}


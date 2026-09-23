package rockstar.client.internal.config;




import rockstar.client.data.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import java.util.UUID;
import lombok.Generated;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.core.ExpiringToken;

public final class MinecraftProfile
implements ExpiringToken {
    private final UUID internalField0428;
    private final String internalField0248;

    public static MinecraftProfile internalMethod00337(JsonObject jsonObject) {
        return MinecraftProfile.internalMethod00387(new JsonObjectNode(jsonObject));
    }

    public static MinecraftProfile internalMethod00387(JsonObjectNode typedValue030) {
        return new MinecraftProfile(UUID.fromString(typedValue030.internalMethod03457("id")), typedValue030.internalMethod03457("name"));
    }

    public static JsonObject internalMethod06203(MinecraftProfile typedValue066) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_saveVersion", (Number)1);
        jsonObject.addProperty("id", typedValue066.internalField0428.toString());
        jsonObject.addProperty("name", typedValue066.internalField0248);
        return jsonObject;
    }

    @Override
    public long internalMethod03800() {
        return Long.MAX_VALUE;
    }

    @Generated
    public MinecraftProfile(UUID uUID, String string) {
        this.internalField0428 = uUID;
        this.internalField0248 = string;
    }

    @Generated
    public UUID internalMethod02519() {
        return this.internalField0428;
    }

    @Generated
    public String internalMethod04925() {
        return this.internalField0248;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof MinecraftProfile)) {
            return false;
        }
        MinecraftProfile typedValue066 = (MinecraftProfile)object;
        UUID uUID = this.internalMethod02519();
        UUID uUID2 = typedValue066.internalMethod02519();
        if (uUID == null ? uUID2 != null : !((Object)uUID).equals(uUID2)) {
            return false;
        }
        String string = this.internalMethod04925();
        String string2 = typedValue066.internalMethod04925();
        return !(string == null ? string2 != null : !string.equals(string2));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        UUID uUID = this.internalMethod02519();
        n2 = n2 * 59 + (uUID == null ? 43 : ((Object)uUID).hashCode());
        String string = this.internalMethod04925();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "MinecraftProfile(id=" + this.internalMethod02519() + ", name=" + this.internalMethod04925() + ")";
    }
}


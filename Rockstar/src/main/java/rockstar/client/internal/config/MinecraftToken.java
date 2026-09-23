package rockstar.client.internal.config;




import rockstar.client.data.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import lombok.Generated;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.core.ExpiringToken;

public final class MinecraftToken
implements ExpiringToken {
    private final long internalField0229;
    private final String internalField0248;
    private final String internalField0247;

    public static MinecraftToken internalMethod04169(JsonObject jsonObject) {
        return MinecraftToken.internalMethod00151(new JsonObjectNode(jsonObject));
    }

    public static MinecraftToken internalMethod00151(JsonObjectNode typedValue030) {
        return new MinecraftToken(typedValue030.internalMethod02177("expireTimeMs"), typedValue030.internalMethod03457("type"), typedValue030.internalMethod03457("token"));
    }

    public static JsonObject internalMethod05332(MinecraftToken typedValue067) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_saveVersion", (Number)1);
        jsonObject.addProperty("expireTimeMs", (Number)typedValue067.internalField0229);
        jsonObject.addProperty("type", typedValue067.internalField0248);
        jsonObject.addProperty("token", typedValue067.internalField0247);
        return jsonObject;
    }

    public String internalMethod06704() {
        return this.internalField0248 + ' ' + this.internalField0247;
    }

    @Generated
    public MinecraftToken(long l, String string, String string2) {
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
    public String internalMethod03181() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod08052() {
        return this.internalField0247;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof MinecraftToken)) {
            return false;
        }
        MinecraftToken typedValue067 = (MinecraftToken)object;
        if (this.internalMethod03800() != typedValue067.internalMethod03800()) {
            return false;
        }
        String string = this.internalMethod03181();
        String string2 = typedValue067.internalMethod03181();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.internalMethod08052();
        String string4 = typedValue067.internalMethod08052();
        return !(string3 == null ? string4 != null : !string3.equals(string4));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        long l = this.internalMethod03800();
        n2 = n2 * 59 + (int)(l >>> 32 ^ l);
        String string = this.internalMethod03181();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.internalMethod08052();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "MinecraftToken(expireTimeMs=" + this.internalMethod03800() + ", type=" + this.internalMethod03181() + ", token=" + this.internalMethod08052() + ")";
    }
}


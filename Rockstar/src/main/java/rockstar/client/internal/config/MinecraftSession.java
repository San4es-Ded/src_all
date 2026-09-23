package rockstar.client.internal.config;





import rockstar.client.data.*;
import rockstar.client.internal.network.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Generated;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.core.ExpiringToken;
import rockstar.client.internal.network.JwtToken;

public final class MinecraftSession
implements ExpiringToken {
    private final long internalField0229;
    private final String internalField0248;
    private final AtomicReference<Object> internalField0746 = new AtomicReference();

    public static MinecraftSession internalMethod03285(JsonObject jsonObject) {
        return MinecraftSession.internalMethod03814(new JsonObjectNode(jsonObject));
    }

    public static MinecraftSession internalMethod03814(JsonObjectNode typedValue030) {
        return new MinecraftSession(typedValue030.internalMethod02177("expireTimeMs"), typedValue030.internalMethod03457("authorizationHeader"));
    }

    public static JsonObject internalMethod01874(MinecraftSession internalValue0009) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_saveVersion", (Number)1);
        jsonObject.addProperty("expireTimeMs", (Number)internalValue0009.internalField0229);
        jsonObject.addProperty("authorizationHeader", internalValue0009.internalField0248);
        return jsonObject;
    }

    @Generated
    public MinecraftSession(long l, String string) {
        this.internalField0229 = l;
        this.internalField0248 = string;
    }

    @Override
    @Generated
    public long internalMethod03800() {
        return this.internalField0229;
    }

    @Generated
    public String internalMethod03445() {
        return this.internalField0248;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof MinecraftSession)) {
            return false;
        }
        MinecraftSession internalValue0009 = (MinecraftSession)object;
        if (this.internalMethod03800() != internalValue0009.internalMethod03800()) {
            return false;
        }
        String string = this.internalMethod03445();
        String string2 = internalValue0009.internalMethod03445();
        return !(string == null ? string2 != null : !string.equals(string2));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        long l = this.internalMethod03800();
        n2 = n2 * 59 + (int)(l >>> 32 ^ l);
        String string = this.internalMethod03445();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "MinecraftSession(expireTimeMs=" + this.internalMethod03800() + ", authorizationHeader=" + this.internalMethod03445() + ")";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Generated
    public JwtToken internalMethod01324() {
        Object object = this.internalField0746.get();
        if (object == null) {
            AtomicReference<Object> atomicReference = this.internalField0746;
            synchronized (atomicReference) {
                object = this.internalField0746.get();
                if (object == null) {
                    JwtToken typedValue085 = JwtToken.internalMethod04997(this.internalField0248.split(" ", 2)[1]);
                    object = typedValue085 == null ? this.internalField0746 : typedValue085;
                    this.internalField0746.set(object);
                }
            }
        }
        return (JwtToken)(object == this.internalField0746 ? null : object);
    }
}


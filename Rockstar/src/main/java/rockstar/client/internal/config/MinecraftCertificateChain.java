package rockstar.client.internal.config;





import rockstar.client.data.*;
import rockstar.client.internal.network.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Generated;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.core.ExpiringToken;
import rockstar.client.internal.network.JwtToken;

public final class MinecraftCertificateChain
implements ExpiringToken {
    private final String internalField0248;
    private final String internalField0247;
    private final AtomicReference<Object> internalField0746 = new AtomicReference();
    private final AtomicReference<Object> internalField0745 = new AtomicReference();

    public static MinecraftCertificateChain internalMethod06078(JsonObject jsonObject) {
        return MinecraftCertificateChain.internalMethod03937(new JsonObjectNode(jsonObject));
    }

    public static MinecraftCertificateChain internalMethod03937(JsonObjectNode typedValue030) {
        return new MinecraftCertificateChain(typedValue030.internalMethod03457("mojangJwt"), typedValue030.internalMethod03457("identityJwt"));
    }

    public static JsonObject internalMethod06949(MinecraftCertificateChain typedValue059) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_saveVersion", (Number)1);
        jsonObject.addProperty("mojangJwt", typedValue059.internalField0248);
        jsonObject.addProperty("identityJwt", typedValue059.internalField0247);
        return jsonObject;
    }

    @Override
    public long internalMethod03800() {
        return Math.min(this.internalMethod06063().internalMethod03800(), this.internalMethod06876().internalMethod03800());
    }

    public String internalMethod03240() {
        return this.internalMethod06876().internalMethod03211().internalMethod03706("extraData").internalMethod03457("displayName");
    }

    public String internalMethod04256() {
        return this.internalMethod06876().internalMethod03211().internalMethod03706("extraData").internalMethod03457("XUID");
    }

    public UUID internalMethod05282() {
        return UUID.fromString(this.internalMethod06876().internalMethod03211().internalMethod03706("extraData").internalMethod03457("identity"));
    }

    @Generated
    public MinecraftCertificateChain(String string, String string2) {
        this.internalField0248 = string;
        this.internalField0247 = string2;
    }

    @Generated
    public String internalMethod08113() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod09008() {
        return this.internalField0247;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof MinecraftCertificateChain)) {
            return false;
        }
        MinecraftCertificateChain typedValue059 = (MinecraftCertificateChain)object;
        String string = this.internalMethod08113();
        String string2 = typedValue059.internalMethod08113();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.internalMethod09008();
        String string4 = typedValue059.internalMethod09008();
        return !(string3 == null ? string4 != null : !string3.equals(string4));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        String string = this.internalMethod08113();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.internalMethod09008();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "MinecraftCertificateChain(mojangJwt=" + this.internalMethod08113() + ", identityJwt=" + this.internalMethod09008() + ")";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Generated
    public JwtToken internalMethod06063() {
        Object object = this.internalField0746.get();
        if (object == null) {
            AtomicReference<Object> atomicReference = this.internalField0746;
            synchronized (atomicReference) {
                object = this.internalField0746.get();
                if (object == null) {
                    JwtToken typedValue085 = JwtToken.internalMethod04997(this.internalField0248);
                    object = typedValue085 == null ? this.internalField0746 : typedValue085;
                    this.internalField0746.set(object);
                }
            }
        }
        return (JwtToken)(object == this.internalField0746 ? null : object);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Generated
    public JwtToken internalMethod06876() {
        Object object = this.internalField0745.get();
        if (object == null) {
            AtomicReference<Object> atomicReference = this.internalField0745;
            synchronized (atomicReference) {
                object = this.internalField0745.get();
                if (object == null) {
                    JwtToken typedValue085 = JwtToken.internalMethod04997(this.internalField0247);
                    object = typedValue085 == null ? this.internalField0745 : typedValue085;
                    this.internalField0745.set(object);
                }
            }
        }
        return (JwtToken)(object == this.internalField0745 ? null : object);
    }
}


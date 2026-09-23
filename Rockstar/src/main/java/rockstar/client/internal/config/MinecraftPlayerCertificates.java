package rockstar.client.internal.config;




import rockstar.client.data.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Base64;
import lombok.Generated;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.core.ExpiringToken;
import rockstar.client.internal.config.KeyPairCodec;

public final class MinecraftPlayerCertificates
implements ExpiringToken {
    private final long internalField0229;
    private final KeyPair internalField0069;
    private final byte[] internalField0609;
    private final byte[] internalField0610;

    public static MinecraftPlayerCertificates internalMethod03401(JsonObject jsonObject) {
        return MinecraftPlayerCertificates.internalMethod06341(new JsonObjectNode(jsonObject));
    }

    public static MinecraftPlayerCertificates internalMethod06341(JsonObjectNode typedValue030) {
        return new MinecraftPlayerCertificates(typedValue030.internalMethod02177("expireTimeMs"), KeyPairCodec.internalMethod06552(typedValue030.internalMethod03706("keyPair")), Base64.getDecoder().decode(typedValue030.internalMethod03457("publicKeySignature")), typedValue030.internalMethod08532("legacyPublicKeySignature").map(Base64.getDecoder()::decode).orElse(null));
    }

    public static JsonObject internalMethod04368(MinecraftPlayerCertificates typedValue065) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_saveVersion", (Number)1);
        jsonObject.addProperty("expireTimeMs", (Number)typedValue065.internalField0229);
        jsonObject.add("keyPair", (JsonElement)KeyPairCodec.internalMethod02216(typedValue065.internalField0069));
        jsonObject.addProperty("publicKeySignature", Base64.getEncoder().encodeToString(typedValue065.internalField0609));
        if (typedValue065.internalField0610 != null) {
            jsonObject.addProperty("legacyPublicKeySignature", Base64.getEncoder().encodeToString(typedValue065.internalField0610));
        }
        return jsonObject;
    }

    @Generated
    public MinecraftPlayerCertificates(long l, KeyPair keyPair, byte[] byArray, byte[] byArray2) {
        this.internalField0229 = l;
        this.internalField0069 = keyPair;
        this.internalField0609 = byArray;
        this.internalField0610 = byArray2;
    }

    @Override
    @Generated
    public long internalMethod03800() {
        return this.internalField0229;
    }

    @Generated
    public KeyPair internalMethod03678() {
        return this.internalField0069;
    }

    @Generated
    public byte[] internalMethod00039() {
        return this.internalField0609;
    }

    @Generated
    public byte[] internalMethod00099() {
        return this.internalField0610;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof MinecraftPlayerCertificates)) {
            return false;
        }
        MinecraftPlayerCertificates typedValue065 = (MinecraftPlayerCertificates)object;
        if (this.internalMethod03800() != typedValue065.internalMethod03800()) {
            return false;
        }
        KeyPair keyPair = this.internalMethod03678();
        KeyPair keyPair2 = typedValue065.internalMethod03678();
        if (keyPair == null ? keyPair2 != null : !keyPair.equals(keyPair2)) {
            return false;
        }
        if (!Arrays.equals(this.internalMethod00039(), typedValue065.internalMethod00039())) {
            return false;
        }
        return Arrays.equals(this.internalMethod00099(), typedValue065.internalMethod00099());
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        long l = this.internalMethod03800();
        n2 = n2 * 59 + (int)(l >>> 32 ^ l);
        KeyPair keyPair = this.internalMethod03678();
        n2 = n2 * 59 + (keyPair == null ? 43 : keyPair.hashCode());
        n2 = n2 * 59 + Arrays.hashCode(this.internalMethod00039());
        n2 = n2 * 59 + Arrays.hashCode(this.internalMethod00099());
        return n2;
    }

    @Generated
    public String toString() {
        return "MinecraftPlayerCertificates(expireTimeMs=" + this.internalMethod03800() + ", keyPair=" + this.internalMethod03678() + ", publicKeySignature=" + Arrays.toString(this.internalMethod00039()) + ", legacyPublicKeySignature=" + Arrays.toString(this.internalMethod00099()) + ")";
    }
}


package rockstar.client.internal.config;




import rockstar.client.data.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import java.security.KeyPair;
import java.util.Base64;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.script.CryptoKeyUtils;

public class KeyPairCodec {
    public static JsonObject internalMethod02216(KeyPair keyPair) {
        if (keyPair.getPublic() == null || keyPair.getPrivate() == null) {
            throw new IllegalArgumentException("KeyPair must contain both public and private key");
        }
        if (!keyPair.getPublic().getAlgorithm().equals(keyPair.getPrivate().getAlgorithm())) {
            throw new IllegalArgumentException("Public and private key must use the same algorithm");
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("algorithm", keyPair.getPublic().getAlgorithm());
        jsonObject.addProperty("publicKey", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        jsonObject.addProperty("privateKey", Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        return jsonObject;
    }

    public static KeyPair internalMethod06552(JsonObjectNode typedValue030) {
        String string;
        switch (string = typedValue030.internalMethod03457("algorithm")) {
            case "RSA": {
                return new KeyPair(CryptoKeyUtils.internalMethod00804(typedValue030.internalMethod03457("publicKey")), CryptoKeyUtils.internalMethod03302(typedValue030.internalMethod03457("privateKey")));
            }
            case "EC": {
                return new KeyPair(CryptoKeyUtils.internalMethod00603(typedValue030.internalMethod03457("publicKey")), CryptoKeyUtils.internalMethod05271(typedValue030.internalMethod03457("privateKey")));
            }
        }
        throw new IllegalArgumentException("Unsupported key algorithm: " + string);
    }
}


package rockstar.client.internal.auth;






import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyPair;
import java.time.Instant;
import java.util.Base64;
import rockstar.client.network.HttpPostRequest;
import rockstar.client.internal.config.MinecraftPlayerCertificates;
import rockstar.client.internal.config.MinecraftToken;
import rockstar.client.internal.config.MinecraftServicesErrorHandler;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.script.CryptoKeyUtils;

public class MinecraftPlayerCertificatesRequest
extends HttpPostRequest
implements MinecraftServicesErrorHandler<MinecraftPlayerCertificates> {
    public MinecraftPlayerCertificatesRequest(MinecraftToken typedValue067) throws MalformedURLException {
        super("https://api.minecraftservices.com/player/certificates");
        this.internalMethod01193("Authorization", typedValue067.internalMethod06704());
    }

    @Override
    public MinecraftPlayerCertificates internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        JsonObjectNode typedValue031 = typedValue030.internalMethod03706("keyPair");
        return new MinecraftPlayerCertificates(Instant.parse(typedValue030.internalMethod03457("expiresAt")).toEpochMilli(), new KeyPair(CryptoKeyUtils.internalMethod07348(Base64.getMimeDecoder().decode(typedValue031.internalMethod03457("publicKey").replace("-----BEGIN RSA PUBLIC KEY-----", "").replace("-----END RSA PUBLIC KEY-----", ""))), CryptoKeyUtils.internalMethod04136(Base64.getMimeDecoder().decode(typedValue031.internalMethod03457("privateKey").replace("-----BEGIN RSA PRIVATE KEY-----", "").replace("-----END RSA PRIVATE KEY-----", "")))), Base64.getDecoder().decode(typedValue030.internalMethod03457("publicKeySignatureV2")), typedValue030.internalMethod08532("publicKeySignature").map(Base64.getDecoder()::decode).orElse(null));
    }
}


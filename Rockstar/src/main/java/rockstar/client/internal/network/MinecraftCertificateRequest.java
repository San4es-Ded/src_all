package rockstar.client.internal.network;






import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.config.*;
import rockstar.client.internal.auth.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyPair;
import java.util.Base64;
import rockstar.client.network.HttpPostRequest;
import rockstar.client.internal.config.MinecraftCertificateChain;
import rockstar.client.internal.config.MinecraftCertificateErrorHandler;
import rockstar.client.data.JsonArrayNode;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.config.JsonRequestBody;
import rockstar.client.internal.auth.XblXstsToken;

public class MinecraftCertificateRequest
extends HttpPostRequest
implements MinecraftCertificateErrorHandler<MinecraftCertificateChain> {
    public MinecraftCertificateRequest(XblXstsToken typedValue043, KeyPair keyPair) throws MalformedURLException {
        super("https://multiplayer.minecraft.net/authentication");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("identityPublicKey", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        this.internalMethod07111(new JsonRequestBody(jsonObject));
        this.internalMethod01193("Authorization", typedValue043.internalMethod02091());
    }

    @Override
    public MinecraftCertificateChain internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        JsonArrayNode typedValue026 = typedValue030.internalMethod03703("chain");
        if (typedValue026.internalMethod06014() != 2) {
            throw new IllegalStateException("Invalid certificate chain length: " + typedValue026.internalMethod06014());
        }
        return new MinecraftCertificateChain(typedValue026.internalMethod06594(0).internalMethod00968(), typedValue026.internalMethod06594(1).internalMethod00968());
    }
}


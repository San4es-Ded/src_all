package rockstar.client.internal.network;





import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyPair;
import java.time.Instant;
import java.util.Base64;
import rockstar.client.network.HttpPostRequest;
import rockstar.client.internal.config.MinecraftMultiplayerToken;
import rockstar.client.internal.config.MinecraftSession;
import rockstar.client.internal.config.MinecraftServicesNestedErrorHandler;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.config.JsonRequestBody;

public class MinecraftMultiplayerSessionRequest
extends HttpPostRequest
implements MinecraftServicesNestedErrorHandler<MinecraftMultiplayerToken> {
    public MinecraftMultiplayerSessionRequest(MinecraftSession internalValue0009, KeyPair keyPair) throws MalformedURLException {
        super("https://authorization.franchise.minecraft-services.net/api/v1.0/multiplayer/session/start");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("publicKey", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        this.internalMethod07111(new JsonRequestBody(jsonObject));
        this.internalMethod01193("Authorization", internalValue0009.internalMethod03445());
    }

    @Override
    public MinecraftMultiplayerToken internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        JsonObjectNode typedValue031 = typedValue030.internalMethod03706("result");
        return new MinecraftMultiplayerToken(Instant.parse(typedValue031.internalMethod03457("validUntil")).toEpochMilli(), typedValue031.internalMethod03457("signedToken"));
    }
}


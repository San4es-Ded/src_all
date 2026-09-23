package rockstar.client.internal.auth;







import rockstar.client.network.*;
import rockstar.client.i18n.*;
import rockstar.client.data.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Instant;
import java.util.UUID;
import rockstar.client.network.HttpPostRequest;
import rockstar.client.internal.config.MinecraftSession;
import rockstar.client.internal.config.MinecraftServicesNestedErrorHandler;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.auth.PlayFabToken;
import rockstar.client.internal.core.UuidUtils;
import rockstar.client.internal.config.JsonRequestBody;
import rockstar.client.internal.auth.XblXstsToken;

public class MinecraftSessionStartRequest
extends HttpPostRequest
implements MinecraftServicesNestedErrorHandler<MinecraftSession> {
    @Deprecated
    public MinecraftSessionStartRequest(XblXstsToken typedValue043, PlayFabToken typedValue080, String string, UUID uUID) throws MalformedURLException {
        this(typedValue080, string, uUID);
    }

    public MinecraftSessionStartRequest(PlayFabToken typedValue080, String string, UUID uUID) throws MalformedURLException {
        super("https://authorization.franchise.minecraft-services.net/api/v1.0/session/start");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("applicationType", "MinecraftPE");
        jsonObject.addProperty("gameVersion", string);
        jsonObject.addProperty("id", UuidUtils.internalMethod01381(uUID));
        jsonObject.addProperty("memory", (Number)0x800000000L);
        jsonObject.addProperty("hardwareMemoryTier", (Number)5);
        jsonObject.addProperty("platform", "Windows10");
        jsonObject.addProperty("playFabTitleId", "20CA2");
        jsonObject.addProperty("storePlatform", "uwp.store");
        jsonObject.addProperty("type", "Windows10");
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("language", "en");
        jsonObject2.addProperty("regionCode", "US");
        jsonObject2.addProperty("languageCode", "en-US");
        jsonObject2.addProperty("tokenType", "PlayFab");
        jsonObject2.addProperty("token", typedValue080.internalMethod09087());
        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.add("device", (JsonElement)jsonObject);
        jsonObject3.add("user", (JsonElement)jsonObject2);
        this.internalMethod07111(new JsonRequestBody(jsonObject3));
    }

    @Override
    public MinecraftSession internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        JsonObjectNode typedValue031 = typedValue030.internalMethod03706("result");
        return new MinecraftSession(Instant.parse(typedValue031.internalMethod03457("validUntil")).toEpochMilli(), typedValue031.internalMethod03457("authorizationHeader"));
    }
}


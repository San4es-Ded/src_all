package rockstar.client.internal.auth;





import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;
import rockstar.client.network.HttpPostRequest;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.auth.PlayFabEntityToken;
import rockstar.client.internal.auth.PlayFabToken;
import rockstar.client.internal.config.PlayFabErrorHandler;
import rockstar.client.internal.config.JsonRequestBody;
import rockstar.client.internal.auth.XblXstsToken;

public class PlayFabLoginWithXboxRequest
extends HttpPostRequest
implements PlayFabErrorHandler<PlayFabToken> {
    public PlayFabLoginWithXboxRequest(XblXstsToken typedValue043, String string) throws MalformedURLException {
        super("https://" + string.toLowerCase(Locale.ROOT) + ".playfabapi.com/Client/LoginWithXbox");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("GetPlayerProfile", Boolean.valueOf(true));
        jsonObject.addProperty("GetUserAccountInfo", Boolean.valueOf(true));
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("CreateAccount", Boolean.valueOf(true));
        jsonObject2.add("InfoRequestParameters", (JsonElement)jsonObject);
        jsonObject2.addProperty("TitleId", string.toUpperCase(Locale.ROOT));
        jsonObject2.addProperty("XboxToken", typedValue043.internalMethod02091());
        this.internalMethod07111(new JsonRequestBody(jsonObject2));
    }

    @Override
    public PlayFabToken internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        JsonObjectNode typedValue031 = typedValue030.internalMethod03706("data");
        return new PlayFabToken(PlayFabEntityToken.internalMethod03175(typedValue031.internalMethod03706("EntityToken")), typedValue031.internalMethod03457("PlayFabId"), typedValue031.internalMethod03457("SessionTicket"));
    }
}


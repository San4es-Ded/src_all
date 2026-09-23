package rockstar.client.internal.auth;






import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.auth.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.net.MalformedURLException;
import rockstar.client.network.HttpPostRequest;
import rockstar.client.auth.OAuthClientConfig;
import rockstar.client.auth.OAuthToken;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.config.JsonRequestBody;
import rockstar.client.internal.config.XblUserToken;
import rockstar.client.internal.config.XblAuthErrorHandler;

public class XboxUserAuthRequest
extends HttpPostRequest
implements XblAuthErrorHandler<XblUserToken> {
    public XboxUserAuthRequest(OAuthClientConfig typedValue071, OAuthToken typedValue074) throws MalformedURLException {
        super("https://user.auth.xboxlive.com/user/authenticate");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SiteName", "user.auth.xboxlive.com");
        jsonObject.addProperty("AuthMethod", "RPS");
        jsonObject.addProperty("RpsTicket", (typedValue071.internalMethod06891() ? "t=" : "d=") + typedValue074.internalMethod01950());
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("Properties", (JsonElement)jsonObject);
        jsonObject2.addProperty("RelyingParty", "http://auth.xboxlive.com");
        jsonObject2.addProperty("TokenType", "JWT");
        this.internalMethod07111(new JsonRequestBody(jsonObject2));
        this.internalMethod01193("x-xbl-contract-version", "1");
    }

    @Override
    public XblUserToken internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) {
        return XblUserToken.internalMethod06706(typedValue030);
    }
}


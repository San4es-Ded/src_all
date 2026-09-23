package rockstar.client.internal.auth;





import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.net.MalformedURLException;
import rockstar.client.network.HttpPostRequest;
import rockstar.client.data.JsonArrayNode;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.config.JsonRequestBody;
import rockstar.client.internal.config.XblDeviceToken;
import rockstar.client.internal.config.XblTitleToken;
import rockstar.client.internal.config.XblUserToken;
import rockstar.client.internal.auth.XblXstsToken;
import rockstar.client.internal.config.XblAuthErrorHandler;

public class XstsAuthRequest
extends HttpPostRequest
implements XblAuthErrorHandler<XblXstsToken> {
    public XstsAuthRequest(XblDeviceToken typedValue086, XblUserToken typedValue041, XblTitleToken typedValue088, String string) throws MalformedURLException {
        super("https://xsts.auth.xboxlive.com/xsts/authorize");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SandboxId", "RETAIL");
        if (typedValue086 != null) {
            jsonObject.addProperty("DeviceToken", typedValue086.internalMethod05939());
        }
        jsonObject.add("UserTokens", (JsonElement)new JsonArrayNode().internalMethod03270(typedValue041.internalMethod04117()).internalMethod01532());
        if (typedValue088 != null) {
            jsonObject.addProperty("TitleToken", typedValue088.internalMethod01911());
        }
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("Properties", (JsonElement)jsonObject);
        jsonObject2.addProperty("RelyingParty", string);
        jsonObject2.addProperty("TokenType", "JWT");
        this.internalMethod07111(new JsonRequestBody(jsonObject2));
        this.internalMethod01193("x-xbl-contract-version", "1");
    }

    @Override
    public XblXstsToken internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) {
        return XblXstsToken.internalMethod04454(typedValue030);
    }
}


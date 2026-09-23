package rockstar.client.internal.auth;







import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.auth.*;
import rockstar.client.internal.network.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.net.MalformedURLException;
import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import rockstar.client.auth.OAuthClientConfig;
import rockstar.client.auth.OAuthToken;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.config.JsonRequestBody;
import rockstar.client.internal.config.XblDeviceToken;
import rockstar.client.internal.auth.XblSisuTokens;
import rockstar.client.internal.config.XblTitleToken;
import rockstar.client.internal.config.XblUserToken;
import rockstar.client.internal.auth.XblXstsToken;
import rockstar.client.internal.network.SignedHttpRequest;
import rockstar.client.internal.config.XblAuthErrorHandler;

public class SisuAuthRequest
extends SignedHttpRequest
implements XblAuthErrorHandler<XblSisuTokens> {
    public SisuAuthRequest(OAuthClientConfig typedValue071, OAuthToken typedValue074, XblDeviceToken typedValue086, KeyPair keyPair, String string) throws MalformedURLException {
        super("https://sisu.xboxlive.com/authorize");
        if (!typedValue071.internalMethod06891()) {
            throw new IllegalArgumentException("Client id must be a title client id for XBL SISU authentication");
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Sandbox", "RETAIL");
        jsonObject.addProperty("UseModernGamertag", Boolean.valueOf(true));
        jsonObject.addProperty("AppId", typedValue071.internalMethod06157());
        jsonObject.addProperty("AccessToken", "t=" + typedValue074.internalMethod01950());
        jsonObject.addProperty("DeviceToken", typedValue086.internalMethod05939());
        jsonObject.add("ProofKey", (JsonElement)this.internalMethod05780((ECPublicKey)keyPair.getPublic()));
        jsonObject.addProperty("RelyingParty", string);
        this.internalMethod07111(new JsonRequestBody(jsonObject));
        this.internalMethod05279((ECPrivateKey)keyPair.getPrivate());
    }

    @Override
    public XblSisuTokens internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) {
        return new XblSisuTokens(XblUserToken.internalMethod06706(typedValue030.internalMethod03706("UserToken")), XblTitleToken.internalMethod04585(typedValue030.internalMethod03706("TitleToken")), XblXstsToken.internalMethod04454(typedValue030.internalMethod03706("AuthorizationToken")));
    }
}


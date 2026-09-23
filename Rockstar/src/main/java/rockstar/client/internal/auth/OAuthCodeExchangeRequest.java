package rockstar.client.internal.auth;






import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.auth.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import java.net.MalformedURLException;
import java.util.HashMap;
import rockstar.client.network.HttpPostRequest;
import rockstar.client.auth.OAuthClientConfig;
import rockstar.client.auth.OAuthToken;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.script.RemoteConfigParser;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.network.FormRequestBody;

public class OAuthCodeExchangeRequest
extends HttpPostRequest
implements RemoteConfigParser<OAuthToken> {
    public OAuthCodeExchangeRequest(OAuthClientConfig typedValue071, String string) throws MalformedURLException {
        super(typedValue071.internalMethod01376().internalMethod08696());
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("client_id", typedValue071.internalMethod06157());
        hashMap.put("scope", typedValue071.internalMethod02674());
        if (typedValue071.internalMethod07955() != null) {
            hashMap.put("client_secret", typedValue071.internalMethod07955());
        }
        if (typedValue071.internalMethod08847() != null) {
            hashMap.put("redirect_uri", typedValue071.internalMethod08847());
        }
        hashMap.put("grant_type", "authorization_code");
        hashMap.put("code", string);
        this.internalMethod07111(new FormRequestBody(hashMap));
    }

    @Override
    public OAuthToken internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) {
        return new OAuthToken(System.currentTimeMillis() + (long)typedValue030.internalMethod02176("expires_in") * 1000L, typedValue030.internalMethod03457("access_token"), typedValue030.internalMethod01170("refresh_token", null));
    }
}


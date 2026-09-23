package rockstar.client.internal.auth;







import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.auth.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.net.MalformedURLException;
import java.util.HashMap;
import rockstar.client.network.HttpPostRequest;
import rockstar.client.auth.OAuthClientConfig;
import rockstar.client.internal.config.MsaDeviceCode;
import rockstar.client.auth.OAuthToken;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.script.RemoteConfigParser;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.network.FormRequestBody;

public class OAuthDeviceTokenRequest
extends HttpPostRequest
implements RemoteConfigParser<OAuthToken> {
    public OAuthDeviceTokenRequest(OAuthClientConfig typedValue071, MsaDeviceCode typedValue073) throws MalformedURLException {
        this(typedValue071, typedValue073.internalMethod04386());
    }

    public OAuthDeviceTokenRequest(OAuthClientConfig typedValue071, String string) throws MalformedURLException {
        super(typedValue071.internalMethod01376().internalMethod08696());
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("client_id", typedValue071.internalMethod06157());
        hashMap.put("grant_type", "device_code");
        hashMap.put("device_code", string);
        this.internalMethod07111(new FormRequestBody(hashMap));
    }

    @Override
    public OAuthToken internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) {
        return new OAuthToken(System.currentTimeMillis() + (long)typedValue030.internalMethod02176("expires_in") * 1000L, typedValue030.internalMethod03457("access_token"), typedValue030.internalMethod01170("refresh_token", null));
    }
}


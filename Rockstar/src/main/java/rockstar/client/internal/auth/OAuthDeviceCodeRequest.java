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
import rockstar.client.internal.auth.MsaEnvironment;
import rockstar.client.auth.OAuthClientConfig;
import rockstar.client.internal.config.MsaDeviceCode;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.script.RemoteConfigParser;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.network.FormRequestBody;

public class OAuthDeviceCodeRequest
extends HttpPostRequest
implements RemoteConfigParser<MsaDeviceCode> {
    public OAuthDeviceCodeRequest(OAuthClientConfig typedValue071) throws MalformedURLException {
        super(typedValue071.internalMethod01376().internalMethod02095());
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("client_id", typedValue071.internalMethod06157());
        hashMap.put("scope", typedValue071.internalMethod02674());
        if (typedValue071.internalMethod01376() == MsaEnvironment.internalField0726) {
            hashMap.put("response_type", "device_code");
        }
        this.internalMethod07111(new FormRequestBody(hashMap));
    }

    @Override
    public MsaDeviceCode internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) {
        return new MsaDeviceCode(System.currentTimeMillis() + (long)typedValue030.internalMethod02176("expires_in") * 1000L, (long)typedValue030.internalMethod02176("interval") * 1000L, typedValue030.internalMethod03457("device_code"), typedValue030.internalMethod03457("user_code"), typedValue030.internalMethod03457("verification_uri"));
    }
}


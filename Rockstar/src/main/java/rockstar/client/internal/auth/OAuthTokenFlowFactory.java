package rockstar.client.internal.auth;




import rockstar.client.network.*;
import rockstar.client.auth.*;
import rockstar.client.*;
import rockstar.client.auth.OAuthClientConfig;
import rockstar.client.internal.auth.OAuthTokenFlow;
import rockstar.client.network.RockstarHttpClient;

@FunctionalInterface
public interface OAuthTokenFlowFactory {
    public OAuthTokenFlow internalMethod04508(RockstarHttpClient localValue1, OAuthClientConfig localValue2);
}


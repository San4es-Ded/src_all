package rockstar.client.internal.auth;




import rockstar.client.network.*;
import rockstar.client.auth.*;
import rockstar.client.*;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import lombok.Generated;
import lombok.SneakyThrows;
import rockstar.client.auth.OAuthClientConfig;
import rockstar.client.auth.OAuthToken;
import rockstar.client.network.RockstarHttpClient;

public abstract class OAuthTokenFlow {
    public final RockstarHttpClient internalField0058;
    public final OAuthClientConfig internalField0727;

    public OAuthTokenFlow(RockstarHttpClient typedValue034, OAuthClientConfig typedValue071) {
        this.internalField0058 = typedValue034;
        this.internalField0727 = typedValue071;
    }

    public abstract OAuthToken internalMethod04761() throws IOException, InterruptedException, TimeoutException;

    @SneakyThrows({IOException.class, InterruptedException.class, TimeoutException.class})
    public OAuthToken internalMethod00715() {
        return this.internalMethod04761();
    }

    public CompletableFuture<OAuthToken> internalMethod01854() {
        return CompletableFuture.supplyAsync(this::internalMethod00715);
    }

    @Generated
    public RockstarHttpClient internalMethod04043() {
        return this.internalField0058;
    }

    @Generated
    public OAuthClientConfig internalMethod03845() {
        return this.internalField0727;
    }
}

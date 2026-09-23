package rockstar.client.internal.core;



import rockstar.client.network.*;
import rockstar.client.*;
import rockstar.client.internal.core.RetryDelay;
import rockstar.client.network.RockstarHttpResponse;

@FunctionalInterface
public interface HttpRetryHandler {
    public RetryDelay internalMethod07102(RockstarHttpResponse localValue1);
}


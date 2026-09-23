package rockstar.client.network;


import rockstar.client.*;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.network.HttpRequestException;

public class HttpRetryException
extends HttpRequestException {
    public HttpRetryException(RockstarHttpResponse typedValue035) {
        super(typedValue035, "Maximum retry count exceeded");
    }

    public HttpRetryException(RockstarHttpResponse typedValue035, String string) {
        super(typedValue035, string);
    }
}


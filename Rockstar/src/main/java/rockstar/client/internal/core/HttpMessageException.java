package rockstar.client.internal.core;



import rockstar.client.network.*;
import rockstar.client.*;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.network.HttpRequestException;

public class HttpMessageException
extends HttpRequestException {
    public HttpMessageException(RockstarHttpResponse typedValue035, String string) {
        super(typedValue035, "status: " + typedValue035.internalMethod00588() + " " + typedValue035.internalMethod06484() + ", message: " + string);
    }
}


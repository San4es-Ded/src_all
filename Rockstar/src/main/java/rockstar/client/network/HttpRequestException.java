package rockstar.client.network;


import rockstar.client.*;
import java.io.IOException;
import rockstar.client.network.RockstarHttpResponse;

public class HttpRequestException
extends IOException {
    private final RockstarHttpResponse internalField0059;

    public HttpRequestException(RockstarHttpResponse typedValue035) {
        this(typedValue035, "Request failed: " + typedValue035.internalMethod00588() + " " + typedValue035.internalMethod06484());
    }

    public HttpRequestException(RockstarHttpResponse typedValue035, String string) {
        super(string);
        this.internalField0059 = typedValue035;
    }

    public RockstarHttpResponse internalMethod00843() {
        return this.internalField0059;
    }
}


package rockstar.client.network;


import rockstar.client.*;
import java.net.MalformedURLException;
import java.net.URL;
import rockstar.client.network.CustomHttpRequest;

public class HttpPutRequest
extends CustomHttpRequest {
    public HttpPutRequest(String string) throws MalformedURLException {
        super("PUT", string);
    }

    public HttpPutRequest(URL uRL) {
        super("PUT", uRL);
    }
}


package rockstar.client.network;


import rockstar.client.*;
import java.net.MalformedURLException;
import java.net.URL;
import rockstar.client.network.RockstarHttpRequest;

public class HttpHeadRequest
extends RockstarHttpRequest {
    public HttpHeadRequest(String string) throws MalformedURLException {
        super("HEAD", string);
    }

    public HttpHeadRequest(URL uRL) {
        super("HEAD", uRL);
    }
}


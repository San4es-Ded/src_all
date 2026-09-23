package rockstar.client.network;


import rockstar.client.*;
import java.net.MalformedURLException;
import java.net.URL;
import rockstar.client.network.CustomHttpRequest;

public class HttpPostRequest
extends CustomHttpRequest {
    public HttpPostRequest(String string) throws MalformedURLException {
        super("POST", string);
    }

    public HttpPostRequest(URL uRL) {
        super("POST", uRL);
    }
}


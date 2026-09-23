package rockstar.client.network;


import rockstar.client.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.Nullable;
import rockstar.client.network.RequestBody;
import rockstar.client.network.RockstarHttpRequest;

public class CustomHttpRequest
extends RockstarHttpRequest {
    @Nullable
    private RequestBody internalField0061;

    public CustomHttpRequest(String string, String string2) throws MalformedURLException {
        super(string, string2);
    }

    public CustomHttpRequest(String string, URL uRL) {
        super(string, uRL);
    }

    public boolean internalMethod01029() {
        return this.internalField0061 != null;
    }

    @Nullable
    public RequestBody internalMethod05540() {
        return this.internalField0061;
    }

    public CustomHttpRequest internalMethod07111(@Nullable RequestBody typedValue036) {
        this.internalField0061 = typedValue036;
        return this;
    }
}


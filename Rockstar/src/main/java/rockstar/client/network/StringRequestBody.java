package rockstar.client.network;


import rockstar.client.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import rockstar.client.network.ByteArrayRequestBody;
import rockstar.client.network.MediaType;

public class StringRequestBody
extends ByteArrayRequestBody {
    public StringRequestBody(String string) {
        this(string, StandardCharsets.UTF_8);
    }

    public StringRequestBody(String string, Charset charset) {
        super(string.getBytes(charset));
    }

    public StringRequestBody(MediaType typedValue038, String string) {
        this(typedValue038, string, StandardCharsets.UTF_8);
    }

    public StringRequestBody(MediaType typedValue038, String string, Charset charset) {
        super(typedValue038, string.getBytes(charset));
    }
}


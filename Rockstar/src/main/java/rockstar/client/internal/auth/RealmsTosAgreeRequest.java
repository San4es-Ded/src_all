package rockstar.client.internal.auth;





import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.io.IOException;
import java.net.MalformedURLException;
import rockstar.client.network.HttpPostRequest;
import rockstar.client.internal.config.RealmsErrorHandler;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;

public class RealmsTosAgreeRequest
extends HttpPostRequest
implements RealmsErrorHandler<Void> {
    public RealmsTosAgreeRequest() throws MalformedURLException {
        super("https://pc.realms.minecraft.net/mco/tos/agreed");
    }

    @Override
    public Void internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        throw new UnsupportedOperationException("This request is not supposed to return any data");
    }
}


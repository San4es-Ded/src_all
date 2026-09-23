package rockstar.client.internal.auth;





import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.io.IOException;
import java.net.MalformedURLException;
import rockstar.client.network.HttpDeleteRequest;
import rockstar.client.internal.auth.RealmsServerModel;
import rockstar.client.internal.config.RealmsErrorHandler;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;

public class RealmsInviteDeleteRequest
extends HttpDeleteRequest
implements RealmsErrorHandler<Void> {
    public RealmsInviteDeleteRequest(RealmsServerModel typedValue062) throws MalformedURLException {
        super("https://pocket.realms.minecraft.net/invites/" + typedValue062.internalMethod02976());
    }

    @Override
    public Void internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        throw new UnsupportedOperationException("This request is not supposed to return any data");
    }
}


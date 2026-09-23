package rockstar.client.internal.auth;





import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.io.IOException;
import java.net.MalformedURLException;
import rockstar.client.network.HttpPostRequest;
import rockstar.client.internal.auth.RealmsServerModel;
import rockstar.client.internal.config.RealmsErrorHandler;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;

public class RealmsInviteAcceptRequest
extends HttpPostRequest
implements RealmsErrorHandler<RealmsServerModel> {
    public RealmsInviteAcceptRequest(String string) throws MalformedURLException {
        super("https://pocket.realms.minecraft.net/invites/v1/link/accept/" + string);
    }

    @Override
    public RealmsServerModel internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        return RealmsServerModel.internalMethod01457(typedValue030);
    }
}


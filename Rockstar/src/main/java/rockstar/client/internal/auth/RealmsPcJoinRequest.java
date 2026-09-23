package rockstar.client.internal.auth;





import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.io.IOException;
import java.net.MalformedURLException;
import rockstar.client.network.HttpGetRequest;
import rockstar.client.internal.auth.RealmsJoinInfoModel;
import rockstar.client.internal.auth.RealmsServerModel;
import rockstar.client.internal.config.RealmsErrorHandler;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;

public class RealmsPcJoinRequest
extends HttpGetRequest
implements RealmsErrorHandler<RealmsJoinInfoModel> {
    public RealmsPcJoinRequest(RealmsServerModel typedValue062) throws MalformedURLException {
        super("https://pc.realms.minecraft.net/worlds/v1/" + typedValue062.internalMethod02976() + "/join/pc");
    }

    @Override
    public RealmsJoinInfoModel internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        return RealmsJoinInfoModel.internalMethod07377(typedValue030);
    }
}


package rockstar.client.internal.network;






import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.config.*;
import rockstar.client.internal.auth.*;
import rockstar.client.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import rockstar.client.network.HttpGetRequest;
import rockstar.client.internal.auth.RealmsServerModel;
import rockstar.client.internal.config.RealmsErrorHandler;
import rockstar.client.data.JsonNode;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;

public class McoWorldListRequest
extends HttpGetRequest
implements RealmsErrorHandler<List<RealmsServerModel>> {
    public McoWorldListRequest(String string) throws MalformedURLException {
        super("https://" + string + "/worlds");
    }

    @Override
    public List<RealmsServerModel> internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        ArrayList<RealmsServerModel> arrayList = new ArrayList<RealmsServerModel>();
        for (JsonNode typedValue029 : typedValue030.internalMethod03703("servers")) {
            arrayList.add(RealmsServerModel.internalMethod01457(typedValue029.internalMethod04512()));
        }
        return arrayList;
    }
}


package rockstar.client.internal.auth;






import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.io.IOException;
import java.net.MalformedURLException;
import rockstar.client.network.HttpGetRequest;
import rockstar.client.internal.core.MissingProfileException;
import rockstar.client.internal.core.ProfileApiException;
import rockstar.client.internal.config.MinecraftProfile;
import rockstar.client.internal.config.MinecraftToken;
import rockstar.client.internal.config.MinecraftServicesErrorHandler;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.core.UuidUtils;

public class MinecraftProfileRequest
extends HttpGetRequest
implements MinecraftServicesErrorHandler<MinecraftProfile> {
    public MinecraftProfileRequest(MinecraftToken typedValue067) throws MalformedURLException {
        super("https://api.minecraftservices.com/minecraft/profile");
        this.internalMethod01193("Authorization", typedValue067.internalMethod06704());
    }

    @Override
    public MinecraftProfile internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        return new MinecraftProfile(UuidUtils.internalMethod01591(typedValue030.internalMethod03457("id")), typedValue030.internalMethod03457("name"));
    }

    @Override
    public void internalMethod05712(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        try {
            MinecraftServicesErrorHandler.super.internalMethod05712(typedValue035, typedValue030);
        }
        catch (ProfileApiException typedValue064) {
            if (typedValue064.internalMethod00843().internalMethod00588() == 404) {
                throw new MissingProfileException(typedValue064);
            }
            throw typedValue064;
        }
    }
}


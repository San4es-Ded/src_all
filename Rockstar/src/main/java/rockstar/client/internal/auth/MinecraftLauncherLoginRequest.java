package rockstar.client.internal.auth;





import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.MalformedURLException;
import rockstar.client.network.HttpPostRequest;
import rockstar.client.internal.config.MinecraftToken;
import rockstar.client.internal.config.MinecraftServicesErrorHandler;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.config.JsonRequestBody;
import rockstar.client.internal.auth.XblXstsToken;

public class MinecraftLauncherLoginRequest
extends HttpPostRequest
implements MinecraftServicesErrorHandler<MinecraftToken> {
    public MinecraftLauncherLoginRequest(XblXstsToken typedValue043) throws MalformedURLException {
        super("https://api.minecraftservices.com/launcher/login");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("platform", "PC_LAUNCHER");
        jsonObject.addProperty("xtoken", typedValue043.internalMethod02091());
        this.internalMethod07111(new JsonRequestBody(jsonObject));
    }

    @Override
    public MinecraftToken internalMethod05428(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        return new MinecraftToken(System.currentTimeMillis() + (long)typedValue030.internalMethod02176("expires_in") * 1000L, typedValue030.internalMethod03457("token_type"), typedValue030.internalMethod03457("access_token"));
    }
}


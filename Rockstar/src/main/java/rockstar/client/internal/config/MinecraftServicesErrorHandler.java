package rockstar.client.internal.config;





import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.io.IOException;
import rockstar.client.internal.core.ProfileApiException;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.core.HttpMessageException;
import rockstar.client.internal.config.ApiResponseHandler;

public interface MinecraftServicesErrorHandler<R>
extends ApiResponseHandler<R> {
    @Override
    default public void internalMethod05712(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        if (typedValue030.internalMethod09165("error") && typedValue030.internalMethod09165("errorMessage")) {
            throw new ProfileApiException(typedValue035, typedValue030.internalMethod03457("error"), typedValue030.internalMethod03457("errorMessage"));
        }
        if (typedValue030.internalMethod09165("errorMessage")) {
            throw new HttpMessageException(typedValue035, typedValue030.internalMethod03457("errorMessage"));
        }
    }
}


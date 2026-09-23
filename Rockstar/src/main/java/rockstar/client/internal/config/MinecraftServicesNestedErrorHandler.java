package rockstar.client.internal.config;





import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.io.IOException;
import rockstar.client.internal.core.NamespacedApiException;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.config.ApiResponseHandler;

public interface MinecraftServicesNestedErrorHandler<R>
extends ApiResponseHandler<R> {
    @Override
    default public void internalMethod05712(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        NamespacedApiException typedValue057 = null;
        Throwable throwable = null;
        JsonObjectNode typedValue031 = typedValue030;
        do {
            if (!typedValue031.internalMethod09165("namespace") || !typedValue031.internalMethod09165("code") || !typedValue031.internalMethod09165("message")) continue;
            NamespacedApiException typedValue058 = new NamespacedApiException(typedValue035, typedValue031.internalMethod02501("namespace"), typedValue031.internalMethod03457("code"), typedValue031.internalMethod03457("message"));
            if (typedValue057 == null) {
                typedValue057 = typedValue058;
            } else {
                throwable.addSuppressed(typedValue058);
            }
            throwable = typedValue058;
        } while ((typedValue031 = typedValue031.internalMethod02179("innerError") ? typedValue031.internalMethod03706("innerError") : null) != null);
        if (typedValue057 != null) {
            throw typedValue057;
        }
    }
}


package rockstar.client.internal.config;





import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.auth.*;
import rockstar.client.*;
import java.io.IOException;
import java.util.Optional;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.config.ApiResponseHandler;
import rockstar.client.internal.auth.XboxAuthException;

public interface XblAuthErrorHandler<R>
extends ApiResponseHandler<R> {
    @Override
    default public R handle(RockstarHttpResponse typedValue035) throws IOException {
        Optional<String> optional;
        if (typedValue035.internalMethod00588() >= 300 && (optional = typedValue035.internalMethod04855("X-Err")).isPresent()) {
            throw new XboxAuthException(typedValue035, Long.parseLong(optional.get()));
        }
        return ApiResponseHandler.super.handle(typedValue035);
    }

    @Override
    default public void internalMethod05712(RockstarHttpResponse typedValue035, JsonObjectNode typedValue030) throws IOException {
        if (typedValue030.internalMethod07773("XErr")) {
            throw new XboxAuthException(typedValue035, typedValue030.internalMethod02177("XErr"));
        }
    }
}


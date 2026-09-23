package rockstar.client.internal.config;





import rockstar.client.network.*;
import rockstar.client.data.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.io.IOException;
import rockstar.client.internal.config.JsonParserUtils;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.network.MediaTypes;
import rockstar.client.internal.core.HttpMessageException;
import rockstar.client.network.ResponseHandler;

public interface ApiResponseHandler<R>
extends ResponseHandler<R> {
    @Override
    default public R handle(RockstarHttpResponse typedValue035) throws IOException {
        String string = typedValue035.internalMethod02509().internalMethod04320();
        if (string.isEmpty() && typedValue035.internalMethod00588() == 204) {
            return null;
        }
        if (string.isEmpty() && typedValue035.internalMethod00588() >= 300) {
            throw new HttpMessageException(typedValue035, "Empty response");
        }
        if (!typedValue035.internalMethod02509().internalMethod05122().internalMethod05675().equals(MediaTypes.internalField1095.internalMethod05675())) {
            throw new HttpMessageException(typedValue035, "Wrong content type");
        }
        JsonObjectNode typedValue030 = JsonParserUtils.internalMethod05878(string).internalMethod04512();
        if (typedValue035.internalMethod00588() >= 300) {
            this.internalMethod05712(typedValue035, typedValue030);
            throw new HttpMessageException(typedValue035, string);
        }
        return this.internalMethod05428(typedValue035, typedValue030);
    }

    public R internalMethod05428(RockstarHttpResponse localValue1, JsonObjectNode localValue2) throws IOException;

    public void internalMethod05712(RockstarHttpResponse localValue1, JsonObjectNode localValue2) throws IOException;
}


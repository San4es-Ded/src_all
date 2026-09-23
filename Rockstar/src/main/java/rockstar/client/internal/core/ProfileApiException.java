package rockstar.client.internal.core;



import rockstar.client.network.*;
import rockstar.client.*;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.core.StructuredHttpException;

public class ProfileApiException
extends StructuredHttpException {
    public ProfileApiException(RockstarHttpResponse typedValue035, String string, String string2) {
        super(typedValue035, string, string2);
    }
}


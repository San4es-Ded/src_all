package rockstar.client.internal.core;



import rockstar.client.network.*;
import rockstar.client.*;
import lombok.Generated;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.core.StructuredHttpException;

public class NamespacedApiException
extends StructuredHttpException {
    private final String internalField0248;

    public NamespacedApiException(RockstarHttpResponse typedValue035, String string, String string2, String string3) {
        super(typedValue035, string2, string3);
        this.internalField0248 = string;
    }

    @Generated
    public String internalMethod01088() {
        return this.internalField0248;
    }
}


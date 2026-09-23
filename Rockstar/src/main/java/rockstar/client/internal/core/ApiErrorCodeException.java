package rockstar.client.internal.core;



import rockstar.client.network.*;
import rockstar.client.*;
import lombok.Generated;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.internal.core.StructuredHttpException;

public class ApiErrorCodeException
extends StructuredHttpException {
    public static final int internalField0227 = 6002;
    private final int internalField0228;

    public ApiErrorCodeException(RockstarHttpResponse typedValue035, int n, String string) {
        super(typedValue035, String.valueOf(n), string);
        this.internalField0228 = n;
    }

    @Generated
    public int internalMethod06987() {
        return this.internalField0228;
    }
}


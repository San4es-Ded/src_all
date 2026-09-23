package rockstar.client.internal.core;



import rockstar.client.network.*;
import rockstar.client.*;
import lombok.Generated;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.network.HttpRequestException;

public class StructuredHttpException
extends HttpRequestException {
    private final String internalField0248;
    private final String internalField0247;

    public StructuredHttpException(RockstarHttpResponse typedValue035, String string, String string2) {
        super(typedValue035, "status: " + typedValue035.internalMethod00588() + " " + typedValue035.internalMethod06484() + ", error: " + string + ", error message: " + string2);
        this.internalField0248 = string;
        this.internalField0247 = string2;
    }

    @Generated
    public String internalMethod00836() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod07668() {
        return this.internalField0247;
    }
}


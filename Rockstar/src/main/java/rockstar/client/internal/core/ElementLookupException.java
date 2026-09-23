package rockstar.client.internal.core;


import rockstar.client.*;
import lombok.Generated;

public class ElementLookupException
extends RuntimeException {
    private final String internalField0248;

    public ElementLookupException(String string) {
        super("%s is not found!".formatted(string));
        this.internalField0248 = string;
    }

    @Generated
    public String internalMethod06184() {
        return this.internalField0248;
    }
}


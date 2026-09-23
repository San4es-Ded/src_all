package rockstar.client.internal.core;


import rockstar.client.*;
import lombok.Generated;
import rockstar.client.internal.core.ExpressionToken;

public class VariableToken
extends ExpressionToken {
    private final String internalField0248;

    public VariableToken(String string) {
        super(6);
        this.internalField0248 = string;
    }

    @Generated
    public String internalMethod04449() {
        return this.internalField0248;
    }
}


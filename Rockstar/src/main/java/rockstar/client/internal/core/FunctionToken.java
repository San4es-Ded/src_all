package rockstar.client.internal.core;


import rockstar.client.*;
import lombok.Generated;
import rockstar.client.internal.core.ExpressionFunction;
import rockstar.client.internal.core.ExpressionToken;

public class FunctionToken
extends ExpressionToken {
    private final ExpressionFunction internalField0378;

    public FunctionToken(ExpressionFunction typedValue238) {
        super(3);
        this.internalField0378 = typedValue238;
    }

    @Generated
    public ExpressionFunction internalMethod02363() {
        return this.internalField0378;
    }
}


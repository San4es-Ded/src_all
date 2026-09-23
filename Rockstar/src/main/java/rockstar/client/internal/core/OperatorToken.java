package rockstar.client.internal.core;


import rockstar.client.*;
import lombok.Generated;
import rockstar.client.internal.core.ExpressionOperator;
import rockstar.client.internal.core.ExpressionToken;

public class OperatorToken
extends ExpressionToken {
    private final ExpressionOperator internalField0379;

    public OperatorToken(ExpressionOperator typedValue239) {
        super(2);
        if (typedValue239 == null) {
            throw new IllegalArgumentException("Operator is unknown for token.");
        }
        this.internalField0379 = typedValue239;
    }

    @Generated
    public ExpressionOperator internalMethod02443() {
        return this.internalField0379;
    }
}


package rockstar.client.internal.core;


import rockstar.client.*;
import lombok.Generated;
import rockstar.client.internal.core.ExpressionToken;

public final class NumberToken
extends ExpressionToken {
    private final double internalField0194;

    public NumberToken(double d) {
        super(1);
        this.internalField0194 = d;
    }

    public NumberToken(char[] cArray, int n, int n2) {
        this(Double.parseDouble(String.valueOf(cArray, n, n2)));
    }

    @Generated
    public double internalMethod02759() {
        return this.internalField0194;
    }
}


package rockstar.client.internal.core;


import rockstar.client.*;
import lombok.Generated;

public class UnknownFunctionOrVariableException
extends IllegalArgumentException {
    private final String internalField0248;
    private final String internalField0247;
    private final String internalField1077;
    private final int internalField0227;

    public UnknownFunctionOrVariableException(String string, int n, int n2) {
        this.internalField0247 = string;
        this.internalField1077 = UnknownFunctionOrVariableException.internalMethod06226(string, n, n2);
        this.internalField0227 = n;
        this.internalField0248 = "Unknown function or variable '" + this.internalField1077 + "' at pos " + n + " in expression '" + string + "'";
    }

    private static String internalMethod06226(String string, int n, int n2) {
        int n3;
        int n4 = string.length();
        if (n4 < (n3 = n + n2 - 1)) {
            n3 = n4;
        }
        return string.substring(n, n3);
    }

    @Override
    public String getMessage() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod02603() {
        return this.internalField0247;
    }

    @Generated
    public String internalMethod07242() {
        return this.internalField1077;
    }

    @Generated
    public int internalMethod04430() {
        return this.internalField0227;
    }
}


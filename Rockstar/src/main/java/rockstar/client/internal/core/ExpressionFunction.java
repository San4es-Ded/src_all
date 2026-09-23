package rockstar.client.internal.core;


import rockstar.client.*;
import lombok.Generated;

public abstract class ExpressionFunction {
    private final String internalField0248;
    public final int internalField0227;

    public ExpressionFunction(String string, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("The number of function arguments can not be less than 0 for '" + string + "'");
        }
        if (!ExpressionFunction.internalMethod04523(string)) {
            throw new IllegalArgumentException("The function name '" + string + "' is invalid");
        }
        this.internalField0248 = string;
        this.internalField0227 = n;
    }

    public ExpressionFunction(String string) {
        this(string, 1);
    }

    public abstract double internalMethod03000(double ... localValue1);

    public static boolean internalMethod04523(String string) {
        if (string == null) {
            return false;
        }
        int n = string.length();
        if (n == 0) {
            return false;
        }
        for (int i = 0; i < n; ++i) {
            char c = string.charAt(i);
            if (Character.isLetter(c) || c == '_' || Character.isDigit(c) && i > 0) continue;
            return false;
        }
        return true;
    }

    @Generated
    public String internalMethod04838() {
        return this.internalField0248;
    }

    @Generated
    public int internalMethod02745() {
        return this.internalField0227;
    }
}


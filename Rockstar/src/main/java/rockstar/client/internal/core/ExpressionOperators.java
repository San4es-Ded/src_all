package rockstar.client.internal.core;


import rockstar.client.*;
import rockstar.client.internal.core.ExpressionOperator;

public abstract class ExpressionOperators {
    private static final ExpressionOperator[] internalField0686 = new ExpressionOperator[8];

    public static ExpressionOperator internalMethod00317(char c, int n) {
        switch (c) {
            case '+': {
                if (n != 1) {
                    return internalField0686[0];
                }
                return internalField0686[7];
            }
            case '-': {
                if (n != 1) {
                    return internalField0686[1];
                }
                return internalField0686[6];
            }
            case '*': {
                return internalField0686[2];
            }
            case '/': 
            case '\u00f7': {
                return internalField0686[3];
            }
            case '^': {
                return internalField0686[4];
            }
            case '%': {
                return internalField0686[5];
            }
        }
        return null;
    }

    static {
        ExpressionOperators.internalField0686[0] = new ExpressionOperator("+", 2, true, 500){

            @Override
            public double internalMethod07375(double ... dArray) {
                return dArray[0] + dArray[1];
            }
        };
        ExpressionOperators.internalField0686[1] = new ExpressionOperator("-", 2, true, 500){

            @Override
            public double internalMethod07375(double ... dArray) {
                return dArray[0] - dArray[1];
            }
        };
        ExpressionOperators.internalField0686[6] = new ExpressionOperator("-", 1, false, 5000){

            @Override
            public double internalMethod07375(double ... dArray) {
                return -dArray[0];
            }
        };
        ExpressionOperators.internalField0686[7] = new ExpressionOperator("+", 1, false, 5000){

            @Override
            public double internalMethod07375(double ... dArray) {
                return dArray[0];
            }
        };
        ExpressionOperators.internalField0686[2] = new ExpressionOperator("*", 2, true, 1000){

            @Override
            public double internalMethod07375(double ... dArray) {
                return dArray[0] * dArray[1];
            }
        };
        ExpressionOperators.internalField0686[3] = new ExpressionOperator("/", 2, true, 1000){

            @Override
            public double internalMethod07375(double ... dArray) {
                if (dArray[1] == 0.0) {
                    throw new ArithmeticException("Division by zero!");
                }
                return dArray[0] / dArray[1];
            }
        };
        ExpressionOperators.internalField0686[4] = new ExpressionOperator("^", 2, false, 10000){

            @Override
            public double internalMethod07375(double ... dArray) {
                return Math.pow(dArray[0], dArray[1]);
            }
        };
        ExpressionOperators.internalField0686[5] = new ExpressionOperator("%", 2, true, 1000){

            @Override
            public double internalMethod07375(double ... dArray) {
                if (dArray[1] == 0.0) {
                    throw new ArithmeticException("Division by zero!");
                }
                return dArray[0] % dArray[1];
            }
        };
    }
}


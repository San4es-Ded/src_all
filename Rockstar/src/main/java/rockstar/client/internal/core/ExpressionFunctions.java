package rockstar.client.internal.core;


import rockstar.client.*;
import rockstar.client.internal.core.ExpressionFunction;

public class ExpressionFunctions {
    private static final ExpressionFunction[] internalField0685 = new ExpressionFunction[31];

    public static ExpressionFunction internalMethod03979(String string) {
        return switch (string) {
            case "sin" -> internalField0685[0];
            case "cos" -> internalField0685[1];
            case "tan" -> internalField0685[2];
            case "cot" -> internalField0685[5];
            case "asin" -> internalField0685[12];
            case "acos" -> internalField0685[13];
            case "atan" -> internalField0685[14];
            case "sinh" -> internalField0685[6];
            case "cosh" -> internalField0685[7];
            case "tanh" -> internalField0685[8];
            case "abs" -> internalField0685[17];
            case "log" -> internalField0685[25];
            case "log10" -> internalField0685[23];
            case "log2" -> internalField0685[24];
            case "log1p" -> internalField0685[26];
            case "ceil" -> internalField0685[18];
            case "floor" -> internalField0685[19];
            case "sqrt" -> internalField0685[15];
            case "cbrt" -> internalField0685[16];
            case "pow" -> internalField0685[20];
            case "exp" -> internalField0685[21];
            case "expm1" -> internalField0685[22];
            case "signum" -> internalField0685[28];
            case "csc" -> internalField0685[3];
            case "sec" -> internalField0685[4];
            case "csch" -> internalField0685[9];
            case "sech" -> internalField0685[10];
            case "coth" -> internalField0685[11];
            case "toradian" -> internalField0685[29];
            case "todegree" -> internalField0685[30];
            default -> null;
        };
    }

    static {
        ExpressionFunctions.internalField0685[0] = new ExpressionFunction("sin"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.sin(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[1] = new ExpressionFunction("cos"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.cos(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[2] = new ExpressionFunction("tan"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.tan(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[5] = new ExpressionFunction("cot"){

            @Override
            public double internalMethod03000(double ... dArray) {
                double d = Math.tan(dArray[0]);
                if (d == 0.0) {
                    throw new ArithmeticException("Division by zero in cotangent!");
                }
                return 1.0 / d;
            }
        };
        ExpressionFunctions.internalField0685[25] = new ExpressionFunction("log"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.log(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[24] = new ExpressionFunction("log2"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.log(dArray[0]) / Math.log(2.0);
            }
        };
        ExpressionFunctions.internalField0685[23] = new ExpressionFunction("log10"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.log10(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[26] = new ExpressionFunction("log1p"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.log1p(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[17] = new ExpressionFunction("abs"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.abs(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[13] = new ExpressionFunction("acos"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.acos(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[12] = new ExpressionFunction("asin"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.asin(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[14] = new ExpressionFunction("atan"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.atan(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[16] = new ExpressionFunction("cbrt"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.cbrt(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[19] = new ExpressionFunction("floor"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.floor(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[6] = new ExpressionFunction("sinh"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.sinh(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[15] = new ExpressionFunction("sqrt"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.sqrt(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[8] = new ExpressionFunction("tanh"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.tanh(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[7] = new ExpressionFunction("cosh"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.cosh(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[18] = new ExpressionFunction("ceil"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.ceil(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[20] = new ExpressionFunction("pow", 2){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.pow(dArray[0], dArray[1]);
            }
        };
        ExpressionFunctions.internalField0685[21] = new ExpressionFunction("exp", 1){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.exp(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[22] = new ExpressionFunction("expm1", 1){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.expm1(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[28] = new ExpressionFunction("signum", 1){

            @Override
            public double internalMethod03000(double ... dArray) {
                if (dArray[0] > 0.0) {
                    return 1.0;
                }
                if (dArray[0] < 0.0) {
                    return -1.0;
                }
                return 0.0;
            }
        };
        ExpressionFunctions.internalField0685[3] = new ExpressionFunction("csc"){

            @Override
            public double internalMethod03000(double ... dArray) {
                double d = Math.sin(dArray[0]);
                if (d == 0.0) {
                    throw new ArithmeticException("Division by zero in cosecant!");
                }
                return 1.0 / d;
            }
        };
        ExpressionFunctions.internalField0685[4] = new ExpressionFunction("sec"){

            @Override
            public double internalMethod03000(double ... dArray) {
                double d = Math.cos(dArray[0]);
                if (d == 0.0) {
                    throw new ArithmeticException("Division by zero in secant!");
                }
                return 1.0 / d;
            }
        };
        ExpressionFunctions.internalField0685[9] = new ExpressionFunction("csch"){

            @Override
            public double internalMethod03000(double ... dArray) {
                if (dArray[0] == 0.0) {
                    return 0.0;
                }
                return 1.0 / Math.sinh(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[10] = new ExpressionFunction("sech"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return 1.0 / Math.cosh(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[11] = new ExpressionFunction("coth"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.cosh(dArray[0]) / Math.sinh(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[27] = new ExpressionFunction("logb", 2){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.log(dArray[1]) / Math.log(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[29] = new ExpressionFunction("toradian"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.toRadians(dArray[0]);
            }
        };
        ExpressionFunctions.internalField0685[30] = new ExpressionFunction("todegree"){

            @Override
            public double internalMethod03000(double ... dArray) {
                return Math.toDegrees(dArray[0]);
            }
        };
    }
}


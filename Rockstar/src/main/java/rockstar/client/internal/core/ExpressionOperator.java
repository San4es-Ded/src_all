package rockstar.client.internal.core;


import rockstar.client.*;
import lombok.Generated;

public abstract class ExpressionOperator {
    public static final int internalField0227 = 500;
    public static final int internalField0228 = 500;
    public static final int internalField1053 = 1000;
    public static final int internalField1055 = 1000;
    public static final int internalField1056 = 1000;
    public static final int internalField1054 = 10000;
    public static final int internalField1464 = 5000;
    public static final int internalField1470 = 5000;
    public static final char[] internalField0611 = new char[]{'+', '-', '*', '/', '%', '^', '!', '#', '\u00a7', '$', '&', ';', ':', '~', '<', '>', '|', '=', '\u00f7', '\u221a', '\u221b', '\u2308', '\u230a'};
    private final int internalField1465;
    private final boolean internalField0277;
    private final String internalField0248;
    private final int internalField1463;

    public ExpressionOperator(String string, int n, boolean bl, int n2) {
        this.internalField1465 = n;
        this.internalField0277 = bl;
        this.internalField0248 = string;
        this.internalField1463 = n2;
    }

    public static boolean internalMethod02426(char c) {
        for (char c2 : internalField0611) {
            if (c != c2) continue;
            return true;
        }
        return false;
    }

    public abstract double internalMethod07375(double ... localValue1);

    @Generated
    public int internalMethod01042() {
        return this.internalField1465;
    }

    @Generated
    public boolean internalMethod01043() {
        return this.internalField0277;
    }

    @Generated
    public String internalMethod00746() {
        return this.internalField0248;
    }

    @Generated
    public int internalMethod01076() {
        return this.internalField1463;
    }
}


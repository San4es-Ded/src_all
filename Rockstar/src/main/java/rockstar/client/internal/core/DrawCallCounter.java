package rockstar.client.internal.core;


import rockstar.client.*;
public final class DrawCallCounter {
    private static int internalField0227;
    private static int internalField0228;
    private static int internalField1053;

    private DrawCallCounter() {
    }

    public static void internalMethod00121() {
        if (internalField1053 > 0) {
            ++internalField0227;
        }
    }

    public static void internalMethod00124() {
        internalField0228 = internalField0227;
        internalField0227 = 0;
        internalField1053 = 0;
    }

    public static void internalMethod08091() {
        ++internalField1053;
    }

    public static void internalMethod08092() {
        if (internalField1053 > 0) {
            --internalField1053;
        }
    }

    public static int internalMethod00120() {
        return internalField0228;
    }
}


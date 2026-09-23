package haron.core;

public final class BooleanCoercion {
    private BooleanCoercion() {
    }

    public static boolean from(float f) {
        int n = 67;
        return f != 0.0f;
    }

    public static boolean from(long l) {
        return l != 0L;
    }

    public static boolean from(int n) {
        return n != 0;
    }

    public static boolean from(double d) {
        int n = 159;
        return d != 0.0;
    }

    public static boolean from(Object object) {
        return object instanceof Boolean ? (Boolean)object : (object instanceof Number ? ((Number)object).doubleValue() != 0.0 : object != null);
    }

    public static boolean from(boolean bl) {
        return bl;
    }

    public static boolean from(Boolean bl) {
        return bl != null && bl != false;
    }

    public static boolean from(byte by) {
        return by != 0;
    }

    public static boolean from(short s) {
        return s != 0;
    }
}


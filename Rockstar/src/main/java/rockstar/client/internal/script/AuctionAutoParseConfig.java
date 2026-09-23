package rockstar.client.internal.script;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public final class AuctionAutoParseConfig {
    private static final List<Integer> internalField0416 = new ArrayList<Integer>(List.of(Integer.valueOf(204), Integer.valueOf(205), Integer.valueOf(206), Integer.valueOf(207)));
    private static long internalField0229 = 80000L;
    private static long internalField0230 = 11000L;
    private static boolean internalField0277 = false;
    private static boolean internalField0276 = false;
    private static long internalField1059 = 300000L;

    public static List<Integer> internalMethod04345() {
        return List.copyOf(internalField0416);
    }

    public static void internalMethod06080(int n) {
        if (n > 0 && !internalField0416.contains(n)) {
            internalField0416.add(n);
        }
    }

    public static void internalMethod06149(int n) {
        internalField0416.remove((Object)n);
    }

    public static long internalMethod05796() {
        return internalField0229;
    }

    public static void internalMethod06081(long l) {
        internalField0229 = Math.max(1000L, l);
    }

    public static long internalMethod05799() {
        return internalField0230;
    }

    public static void internalMethod06150(long l) {
        internalField0230 = Math.max(500L, l);
    }

    public static boolean internalMethod05797() {
        return internalField0277;
    }

    public static void internalMethod06082(boolean bl) {
        internalField0277 = bl;
    }

    public static boolean internalMethod05800() {
        return internalField0276;
    }

    public static void internalMethod06151(boolean bl) {
        internalField0276 = bl;
    }

    public static long internalMethod08854() {
        return internalField1059;
    }

    public static void internalMethod09110(long l) {
        internalField1059 = Math.max(60000L, l);
    }

    @Generated
    private AuctionAutoParseConfig() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}


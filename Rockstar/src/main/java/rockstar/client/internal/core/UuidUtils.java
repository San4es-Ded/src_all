package rockstar.client.internal.core;


import rockstar.client.*;
import java.util.UUID;

public class UuidUtils {
    private static final String internalField0248 = "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)";
    private static final String internalField0247 = "(\\p{XDigit}{8})-(\\p{XDigit}{4})-(\\p{XDigit}{4})-(\\p{XDigit}{4})-(\\p{XDigit}+)";

    public static UUID internalMethod01591(String string) {
        if (string != null) {
            return UUID.fromString(string.replaceFirst(internalField0248, "$1-$2-$3-$4-$5"));
        }
        return null;
    }

    public static String internalMethod01381(UUID uUID) {
        if (uUID != null) {
            return uUID.toString().replace("-", "");
        }
        return null;
    }

    public static boolean internalMethod03977(String string) {
        return string.matches(internalField0248);
    }

    public static boolean internalMethod06432(String string) {
        return string.matches(internalField0247);
    }
}


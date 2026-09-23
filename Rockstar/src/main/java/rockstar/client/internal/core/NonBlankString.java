package rockstar.client.internal.core;


import rockstar.client.*;
public class NonBlankString {
    public static String internalMethod04450(String string) {
        if (string == null || string.isEmpty() || string.trim().isEmpty()) {
            return null;
        }
        return string;
    }
}


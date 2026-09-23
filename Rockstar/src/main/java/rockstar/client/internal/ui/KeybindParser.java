package rockstar.client.internal.ui;



import rockstar.client.util.*;
import rockstar.client.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import lombok.Generated;
import org.lwjgl.glfw.GLFW;
import rockstar.client.util.KeybindUtils;

public final class KeybindParser {
    public static int internalMethod06543(String string) {
        if (string == null || string.isBlank()) {
            return -1;
        }
        String[] stringArray = string.split("\\+");
        if (stringArray.length > 1) {
            int n;
            int n2 = 0;
            for (n = 0; n < stringArray.length - 1; ++n) {
                int n3 = KeybindParser.internalMethod08647(KeybindParser.internalMethod00042(stringArray[n]));
                if (n3 == 0) {
                    return -1;
                }
                n2 |= n3;
            }
            n = KeybindParser.internalMethod05083(stringArray[stringArray.length - 1]);
            return n == -1 ? -1 : KeybindUtils.internalMethod04327(n, n2);
        }
        return KeybindParser.internalMethod05083(string);
    }

    private static int internalMethod05083(String string) {
        if (string == null || string.isBlank()) {
            return -1;
        }
        String string2 = KeybindParser.internalMethod00042(string);
        int n = KeybindParser.internalMethod08933(string2);
        if (n != -1) {
            return n;
        }
        try {
            return (Integer)GLFW.class.getField("GLFW_KEY_" + string2).get(null);
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            return -1;
        }
    }

    private static int internalMethod08647(String string) {
        return switch (string) {
            case "CTRL", "CONTROL", "LCTRL", "RCTRL", "LEFT_CONTROL", "RIGHT_CONTROL" -> 2;
            case "SHIFT", "LSHIFT", "RSHIFT", "LEFT_SHIFT", "RIGHT_SHIFT" -> 1;
            case "ALT", "LALT", "RALT", "LEFT_ALT", "RIGHT_ALT" -> 4;
            case "SUPER", "WIN", "CMD", "LEFT_SUPER", "RIGHT_SUPER" -> 8;
            default -> 0;
        };
    }

    public static List<String> internalMethod07034() {
        return Stream.of(GLFW.class.getFields()).map(Field::getName).filter(string -> string.startsWith("GLFW_KEY_")).map(string -> string.substring("GLFW_KEY_".length())).filter(string -> !string.matches("LAST|UNKNOWN|WORLD_\\d+")).toList();
    }

    private static String internalMethod00042(String string) {
        return string.trim().toUpperCase(Locale.ROOT).replace(" ", "_").replace("-", "_");
    }

    private static int internalMethod08933(String string) {
        switch (string) {
            case "LMB": {
                return 0;
            }
            case "RMB": {
                return 1;
            }
            case "MMB": {
                return 2;
            }
        }
        if (string.startsWith("MOUSE")) {
            String string2 = string.replace("MOUSE_BUTTON_", "").replace("MOUSE", "");
            try {
                int n = Integer.parseInt(string2);
                if (n >= 1 && n <= 8) {
                    return 0 + (n - 1);
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return -1;
    }

    @Generated
    private KeybindParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}


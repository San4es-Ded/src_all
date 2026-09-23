package haron.util;

import org.lwjgl.glfw.GLFW;

public final class effe6p {
    private static final int FIRST_MOUSE_BUTTON_CODE = 1450;

    private effe6p() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String format(int n) {
        if (n == -1 || n == 0) {
            return "";
        }
        if (n > 1450) {
            return effe6p.$sf$0(n - 1450);
        }
        String string = GLFW.glfwGetKeyName((int)n, (int)0);
        if (string != null) {
            return string.toUpperCase();
        }
        switch (n) {
            case 32: {
                return "SPACE";
            }
            case 256: {
                return "ESC";
            }
            case 257: {
                return "ENTER";
            }
            case 258: {
                return "TAB";
            }
            case 259: {
                return "BACK";
            }
            case 260: {
                return "IܵS";
            }
            case 261: {
                return "DEL";
            }
            case 262: {
                return "RIGHT";
            }
            case 263: {
                return "LEFT";
            }
            case 264: {
                return "DOWN";
            }
            case 265: {
                return "UP";
            }
            case 266: {
                return "PGUP";
            }
            case 267: {
                return "PGDN";
            }
            case 268: {
                return "HOME";
            }
            case 269: {
                return "END";
            }
            case 280: {
                return "CAPS";
            }
            case 290: {
                return "F1";
            }
            case 291: {
                return "F2";
            }
            case 292: {
                return "F3";
            }
            case 293: {
                return "F4";
            }
            case 294: {
                return "F5";
            }
            case 295: {
                return "F6";
            }
            case 296: {
                return "F7";
            }
            case 297: {
                return "F8";
            }
            case 298: {
                return "F9";
            }
            case 299: {
                return "F10";
            }
            case 300: {
                return "F11";
            }
            case 301: {
                return "F12";
            }
            case 340: {
                return "LSHIFT";
            }
            case 341: {
                return "LCTRL";
            }
            case 342: {
                return "LALT";
            }
            case 344: {
                return "RSHIFT";
            }
            case 345: {
                return "RCTRL";
            }
            case 346: {
                return "RALT";
            }
        }
        return effe6p.$sf$1(n);
    }

    public static String a(int n) {
        return effe6p.format(n);
    }

    private static /* synthetic */ String $sf$0(int n) {
        return "M" + n;
    }

    private static /* synthetic */ String $sf$1(int n) {
        return "KEY_" + n;
    }
}


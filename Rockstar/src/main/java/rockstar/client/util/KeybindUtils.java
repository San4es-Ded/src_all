package rockstar.client.util;




import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import rockstar.modules.other.AssistModule;
import rockstar.client.setting.Setting;
import rockstar.client.setting.KeybindSetting;
import rockstar.client.internal.core.ChatMacroEntry;
import rockstar.client.internal.script.ChatMacroManager;
import rockstar.client.module.ModuleEntry;
import rockstar.client.module.ModuleManager;
import rockstar.client.RockstarClient;
import rockstar.client.internal.inventory.AssistSwapItem;

public final class KeybindUtils {
    public static final int internalField0227 = -1;
    public static final int internalField0228 = Integer.MIN_VALUE;
    private static int internalField1054 = Integer.MIN_VALUE;
    private static int internalField1464;
    private static boolean internalField0277;
    private static long internalField0230;

    public static int internalMethod04327(int n, int n2) {
        if (n <= -1) {
            return -1;
        }
        return n & 0xFFFF | (n2 & 0xF) << 16;
    }

    public static int internalMethod02025(int n) {
        return n <= -1 ? -1 : n & 0xFFFF;
    }

    public static int internalMethod02857(int n) {
        return n <= -1 ? 0 : n >> 16 & 0xF;
    }

    public static boolean internalMethod02026(int n) {
        int n2 = KeybindUtils.internalMethod02025(n);
        return n2 >= 0 && n2 <= 7;
    }

    public static boolean internalMethod02858(int n) {
        return KeybindUtils.internalMethod08520(n) != 0;
    }

    public static int internalMethod08520(int n) {
        return switch (n) {
            case 340, 344 -> 1;
            case 341, 345 -> 2;
            case 342, 346 -> 4;
            case 343, 347 -> 8;
            default -> 0;
        };
    }

    public static int internalMethod06041(int n, int n2) {
        if (KeybindUtils.internalMethod02858(n)) {
            return Integer.MIN_VALUE;
        }
        return KeybindUtils.internalMethod04327(n, n2);
    }

    public static int internalMethod08281(int n, int n2) {
        if (!KeybindUtils.internalMethod02858(n)) {
            return Integer.MIN_VALUE;
        }
        return KeybindUtils.internalMethod04327(n, n2 & ~KeybindUtils.internalMethod08520(n));
    }

    public static int internalMethod08541(int n) {
        return KeybindUtils.internalMethod04327(n, KeybindUtils.internalMethod06867());
    }

    public static int internalMethod06867() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null || minecraftClient.getWindow() == null) {
            return 0;
        }
        long l = minecraftClient.getWindow().getHandle();
        int n = 0;
        if (KeybindUtils.internalMethod04330(l, 340) || KeybindUtils.internalMethod04330(l, 344)) {
            n |= 1;
        }
        if (KeybindUtils.internalMethod04330(l, 341) || KeybindUtils.internalMethod04330(l, 345)) {
            n |= 2;
        }
        if (KeybindUtils.internalMethod04330(l, 342) || KeybindUtils.internalMethod04330(l, 346)) {
            n |= 4;
        }
        if (KeybindUtils.internalMethod04330(l, 343) || KeybindUtils.internalMethod04330(l, 347)) {
            n |= 8;
        }
        return n;
    }

    public static boolean internalMethod04328(int n, int n2) {
        return KeybindUtils.internalMethod01369(n, n2, KeybindUtils.internalMethod06867());
    }

    public static boolean internalMethod01369(int n, int n2, int n3) {
        if (n == -1 || KeybindUtils.internalMethod02025(n) != n2) {
            return false;
        }
        int n4 = KeybindUtils.internalMethod02857(n);
        if ((n3 & n4) != n4) {
            return false;
        }
        return n4 != 0 || n3 == 0 || !KeybindUtils.internalMethod06042(n2, n3);
    }

    public static boolean internalMethod08521(int n) {
        if (n == -1) {
            return false;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null || minecraftClient.getWindow() == null) {
            return false;
        }
        int n2 = KeybindUtils.internalMethod02857(n);
        if ((KeybindUtils.internalMethod06867() & n2) != n2) {
            return false;
        }
        long l = minecraftClient.getWindow().getHandle();
        int n3 = KeybindUtils.internalMethod02025(n);
        return KeybindUtils.internalMethod02026(n) ? GLFW.glfwGetMouseButton((long)l, (int)n3) == 1 : KeybindUtils.internalMethod04330(l, n3);
    }

    public static String internalMethod07434(int n) {
        if ((n & 0xF) == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        if ((n & 2) != 0) {
            stringBuilder.append("CTRL + ");
        }
        if ((n & 1) != 0) {
            stringBuilder.append("SHIFT + ");
        }
        if ((n & 4) != 0) {
            stringBuilder.append("ALT + ");
        }
        if ((n & 8) != 0) {
            stringBuilder.append("WIN + ");
        }
        return stringBuilder.toString();
    }

    private static boolean internalMethod04330(long l, int n) {
        return n > -1 && InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow(), n);
    }

    private static boolean internalMethod06042(int n, int n2) {
        long l = System.currentTimeMillis();
        if (n == internalField1054 && n2 == internalField1464 && l - internalField0230 < 50L) {
            return internalField0277;
        }
        internalField1054 = n;
        internalField1464 = n2;
        internalField0230 = l;
        internalField0277 = KeybindUtils.internalMethod08282(n, n2);
        return internalField0277;
    }

    private static boolean internalMethod08282(int n, int n2) {
        Object object;
        RockstarClient typedParameter1001 = RockstarClient.getInstance();
        if (typedParameter1001 == null) {
            return false;
        }
        ModuleManager typedValue148 = typedParameter1001.getModuleManager();
        if (typedValue148 != null) {
            for (ModuleEntry object2 : typedValue148.getModules()) {
                if (!object2.isAvailable()) continue;
                if (KeybindUtils.internalMethod06255(object2.getKeybind(), n, n2)) {
                    return true;
                }
                for (Setting typedValue157 : object2.getSettings()) {
                    KeybindSetting typedValue161;
                    if (!(typedValue157 instanceof KeybindSetting) || !(typedValue161 = (KeybindSetting)typedValue157).isVisible() || !KeybindUtils.internalMethod06255(typedValue161.internalMethod07477(), n, n2)) continue;
                    return true;
                }
            }
            object = typedValue148.getModule(AssistModule.class);
            if (object != null) {
                for (AssistSwapItem typedValue106 : ((AssistModule)object).internalMethod03313()) {
                    if (!KeybindUtils.internalMethod06255(typedValue106.internalMethod03234(), n, n2)) continue;
                    return true;
                }
            }
        }
        if ((object = typedParameter1001.internalMethod05155()) != null) {
            for (ChatMacroEntry typedValue143 : ((ChatMacroManager)object).internalMethod06721()) {
                if (!KeybindUtils.internalMethod06255(typedValue143.internalMethod03890(), n, n2)) continue;
                return true;
            }
        }
        return false;
    }

    private static boolean internalMethod06255(int n, int n2, int n3) {
        int n4 = KeybindUtils.internalMethod02857(n);
        return n4 != 0 && KeybindUtils.internalMethod02025(n) == n2 && (n3 & n4) == n4;
    }

    @Generated
    private KeybindUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

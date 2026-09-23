package rockstar.client.compat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.SystemKeycodes;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Vec2f;
import moscow.rockstar.mixin.minecraft.client.input.InputAccessor;

/** Polling equivalents for modifier helpers removed from Screen in 1.21.11. */
public final class InputCompat {
    private InputCompat() {
    }

    public static boolean hasControlDown() {
        int key = SystemKeycodes.CTRL_MOD == InputUtil.GLFW_MOD_SUPER
            ? InputUtil.GLFW_KEY_LEFT_SUPER
            : InputUtil.GLFW_KEY_LEFT_CONTROL;
        int other = SystemKeycodes.CTRL_MOD == InputUtil.GLFW_MOD_SUPER
            ? InputUtil.GLFW_KEY_RIGHT_SUPER
            : InputUtil.GLFW_KEY_RIGHT_CONTROL;
        return pressed(key) || pressed(other);
    }

    public static boolean hasShiftDown() {
        return pressed(InputUtil.GLFW_KEY_LEFT_SHIFT) || pressed(InputUtil.GLFW_KEY_RIGHT_SHIFT);
    }

    public static int currentModifiers() {
        int modifiers = 0;
        if (hasShiftDown()) {
            modifiers |= InputUtil.GLFW_MOD_SHIFT;
        }
        if (hasControlDown()) {
            modifiers |= SystemKeycodes.CTRL_MOD;
        }
        if (pressed(InputUtil.GLFW_KEY_LEFT_ALT) || pressed(InputUtil.GLFW_KEY_RIGHT_ALT)) {
            modifiers |= InputUtil.GLFW_MOD_ALT;
        }
        return modifiers;
    }

    public static KeyInput keyInput(int key, int scancode) {
        return new KeyInput(key, scancode, currentModifiers());
    }

    public static boolean isSelectAll(int key) {
        return keyInput(key, 0).isSelectAll();
    }

    public static boolean isCopy(int key) {
        return keyInput(key, 0).isCopy();
    }

    public static boolean isCut(int key) {
        return keyInput(key, 0).isCut();
    }

    public static boolean isPaste(int key) {
        return keyInput(key, 0).isPaste();
    }

    public static float forward(Input input) {
        return ((InputAccessor)(Object)input).getMovementVector().y;
    }

    public static float sideways(Input input) {
        return ((InputAccessor)(Object)input).getMovementVector().x;
    }

    public static void setForward(Input input, float value) {
        InputAccessor accessor = (InputAccessor)(Object)input;
        accessor.setMovementVector(new Vec2f(accessor.getMovementVector().x, value));
    }

    public static void setSideways(Input input, float value) {
        InputAccessor accessor = (InputAccessor)(Object)input;
        accessor.setMovementVector(new Vec2f(value, accessor.getMovementVector().y));
    }

    private static boolean pressed(int key) {
        MinecraftClient client = MinecraftClient.getInstance();
        return client != null && client.getWindow() != null && InputUtil.isKeyPressed(client.getWindow(), key);
    }
}

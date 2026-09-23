package haron.gui.core;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public final class ClickGuiKeybind {
    public static KeyBinding OPEN_KEY;

    private ClickGuiKeybind() {
    }

    public static void register() {
        OPEN_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)new KeyBinding("Open Click GUI", InputUtil.Type.KEYSYM, 344, "Haron"));
    }
}


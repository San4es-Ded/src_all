package su.sacura.events.input;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import su.sacura.util.type.MinecraftWrapper;

public record KeyEvent(Screen screen, InputUtil.Type type, int key, int action) implements MinecraftWrapper
{
    public boolean isKeyDown(int key) {
        return this.isKeyDown(key, KeyEvent.mc.currentScreen == null);
    }

    public boolean isKeyDown(int key, boolean screen) {
        return this.key == key && this.action == 1 && screen;
    }

    public boolean isKeyReleased(int key) {
        return this.isKeyReleased(key, KeyEvent.mc.currentScreen == null);
    }

    public boolean isKeyReleased(int key, boolean screen) {
        return this.key == key && this.action == 0 && screen;
    }
}

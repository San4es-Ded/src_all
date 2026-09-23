package moscow.rockstar.mixin.minecraft.client;


import rockstar.client.internal.ui.*;
import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pyrock.events.window.CharTypedEvent;
import pyrock.events.window.KeyEvent;
import pyrock.events.window.KeyPressEvent;
import rockstar.modules.visual.MenuModule;
import rockstar.client.RockstarClient;
import rockstar.client.internal.ui.ImeCompositionHelper;
import rockstar.client.MinecraftClientAccess;

@Mixin(value={Keyboard.class})
public class KeyboardMixin
implements MinecraftClientAccess {
    @Inject(method={"onKey"}, at={@At(value="HEAD")}, cancellable=true)
    public void triggerKeyEvent(long l, int action, KeyInput input, CallbackInfo callbackInfo) {
        int n = input.key();
        int n2 = input.scancode();
        int n3 = action;
        int n4 = input.modifiers();
        if (n == -1) {
            return;
        }
        if (n != 292 && GLFW.glfwGetKey((long)l, (int)292) == 1) {
            return;
        }
        if (n3 == 0) {
            MenuModule.internalMethod08804(n);
        }
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new KeyPressEvent(n3, n));
        KeyEvent keyEvent = new KeyEvent(n, n2, n3, n4);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(keyEvent);
        if (keyEvent.isCancelled() && !this.isEscapeHatch(n, n2)) {
            callbackInfo.cancel();
            return;
        }
        if (n == 46 && n3 == 1 && (n4 & 8) != 0) {
            ImeCompositionHelper.internalMethod05309(l);
            return;
        }
        if (KeyboardMixin.internalField0149.currentScreen != null) {
            return;
        }
        if (n == 46 && n3 == 1) {
            internalField0149.setScreen((Screen)new ChatScreen("", false));
        }
    }

    @Inject(method={"onChar"}, at={@At(value="HEAD")}, cancellable=true)
    private void triggerCharEvent(long l, CharInput input, CallbackInfo callbackInfo) {
        int n = input.codepoint();
        int n2 = input.modifiers();
        CharTypedEvent charTypedEvent = new CharTypedEvent(n, n2);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(charTypedEvent);
        if (charTypedEvent.isCancelled()) {
            callbackInfo.cancel();
            return;
        }
        if (RockstarClient.getInstance().internalMethod01271() != null) {
            for (char c : Character.toChars(n)) {
                if (RockstarClient.getInstance().internalMethod01271().internalMethod07287(c, n2)) continue;
                return;
            }
            callbackInfo.cancel();
        }
    }

    @Unique
    private boolean isEscapeHatch(int n, int n2) {
        if (n == 256 || n == 46) {
            return true;
        }
        if (KeyboardMixin.internalField0149.options == null) {
            return false;
        }
        KeyInput input = new KeyInput(n, n2, 0);
        return KeyboardMixin.internalField0149.options.chatKey.matchesKey(input) || KeyboardMixin.internalField0149.options.commandKey.matchesKey(input);
    }
}

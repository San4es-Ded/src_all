package su.sacura.mixin.player.input;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.sacura.Sacura;
import su.sacura.display.csgui.Window;
import su.sacura.events.input.KeyEvent;
import su.sacura.features.modules.impl.render.BetterMinecraftModule;

@Mixin({Keyboard.class})
public class KeyboardMixin {
    @Final @Shadow private MinecraftClient field_1678;

    @Inject(method = {"onKey"}, at = {@At("HEAD")})
    private void onKey(long window, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if (key != -1 && window == this.field_1678.getWindow().getHandle()) {
            if (key == 344 && action == 1 && this.field_1678.currentScreen == null)
                Window.getInstance().openGui();
            // Исправлено: InputUtil.Type.field_1668 -> InputUtil.Type.KEYSYM
            Sacura.getInstance().getEventBus().post(new KeyEvent(this.field_1678.currentScreen, InputUtil.Type.KEYSYM, key, action));
            BetterMinecraftModule module = (BetterMinecraftModule)Sacura.getInstance().getModuleManager().getModule(BetterMinecraftModule.class);
            if (module == null || !module.enable)
                return;
            if (key == 294 && action == 1 && ((Boolean)module.smoothThirdPerson.get()).booleanValue())
                module.getThirdPersonAnimation().reset();
        }
    }
}
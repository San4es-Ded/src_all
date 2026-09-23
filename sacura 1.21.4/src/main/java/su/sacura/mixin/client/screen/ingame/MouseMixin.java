package su.sacura.mixin.client.screen.ingame;

import net.minecraft.client.Mouse;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.sacura.Sacura;
import su.sacura.events.input.KeyEvent;
import su.sacura.util.type.MinecraftWrapper;

@Mixin({Mouse.class})
public class MouseMixin implements MinecraftWrapper {
    @Inject(method = {"onMouseButton"}, at = {@At("HEAD")})
    public void onMouseButtonHook(long window, int button, int action, int mods, CallbackInfo ci) {
        // Исправлено: InputUtil.Type.field_1672 -> InputUtil.Type.MOUSE
        if (button != -1 && window == mc.getWindow().getHandle())
            Sacura.getInstance().getEventBus().post(new KeyEvent(mc.currentScreen, InputUtil.Type.MOUSE, button, action));
    }
}
package moscow.rockstar.mixin.minecraft.client.option;

import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rockstar.modules.visual.AmbienceModule;
import rockstar.client.RockstarClient;

@Mixin(value={SimpleOption.class})
public class SimpleOptionMixin<T> {
    @Shadow
    @Final
    Text text;
    @Shadow
    T value;

    @Inject(method={"getValue"}, at={@At(value="HEAD")}, cancellable=true)
    public void getGammaValue(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (RockstarClient.getInstance().getModuleManager() == null) {
            return;
        }
        AmbienceModule typedValue317 = RockstarClient.getInstance().getModuleManager().getModule(AmbienceModule.class);
        if (typedValue317 != null && typedValue317.isEnabled() && typedValue317.internalMethod09243() && typedValue317.internalMethod10118().isSelected() && this.text.equals((Object)Text.translatable((String)"options.gamma"))) {
            callbackInfoReturnable.setReturnValue(1337.0);
        }
    }

    @Inject(method={"setValue"}, at={@At(value="HEAD")}, cancellable=true)
    public void setGammaValue(T t, CallbackInfo callbackInfo) {
        if (RockstarClient.getInstance().getModuleManager() == null) {
            return;
        }
        AmbienceModule typedValue317 = RockstarClient.getInstance().getModuleManager().getModule(AmbienceModule.class);
        if (typedValue317 != null && typedValue317.isEnabled() && typedValue317.internalMethod10118().isSelected() && this.text.equals((Object)Text.translatable((String)"options.gamma"))) {
            this.value = t;
            callbackInfo.cancel();
        }
    }
}

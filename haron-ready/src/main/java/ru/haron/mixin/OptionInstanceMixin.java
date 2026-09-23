package ru.haron.mixin;

import java.util.Optional;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={SimpleOption.DoubleSliderCallbacks.class})
public class OptionInstanceMixin {
    @Inject(method={"validate(Ljava/lang/Double;)Ljava/util/Optional;"}, at={@At(value="HEAD")}, cancellable=true)
    private void unlimitGamma(Double d, CallbackInfoReturnable<Optional<Double>> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(Optional.of(d));
    }
}


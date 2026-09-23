package ru.haron.mixin;

import haron.markers.MarkerHudElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={MarkerHudElement.class}, remap=false)
public abstract class MapMarkerDistanceMixin {
    @Inject(method={"a(D)Ljava/lang/String;"}, at={@At(value="HEAD")}, cancellable=true)
    private void fixDistanceFormat(double d, CallbackInfoReturnable<String> callbackInfoReturnable) {
        if (d < 1.0) {
            callbackInfoReturnable.setReturnValue("<1 м");
        } else if (d < 1000.0) {
            callbackInfoReturnable.setReturnValue(((int)d + " м"));
        } else {
            callbackInfoReturnable.setReturnValue(String.format("%.1f км", d / 1000.0));
        }
    }
}


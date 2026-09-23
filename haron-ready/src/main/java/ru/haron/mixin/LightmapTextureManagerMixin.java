package ru.haron.mixin;

import haron.module.ModuleManager;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={LightmapTextureManager.class})
public class LightmapTextureManagerMixin {
    @Inject(method={"getDarknessFactor"}, at={@At(value="HEAD")}, cancellable=true)
    private void getDarknessFactor(float f, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (ModuleManager.FULL_BRIGHT.k()) {
            callbackInfoReturnable.setReturnValue(Float.valueOf(0.0f));
        }
    }

    @Inject(method={"getBrightness(FI)F"}, at={@At(value="HEAD")}, cancellable=true)
    private static void onGetBrightness(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (ModuleManager.FULL_BRIGHT.k()) {
            callbackInfoReturnable.setReturnValue(Float.valueOf(1.0f));
        }
    }

    @Inject(method={"getBrightness(Lnet/minecraft/world/dimension/DimensionType;I)F"}, at={@At(value="HEAD")}, cancellable=true)
    private static void onGetBrightnessAmbient(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (ModuleManager.FULL_BRIGHT.k()) {
            callbackInfoReturnable.setReturnValue(Float.valueOf(1.0f));
        }
    }
}


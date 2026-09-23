package ru.haron.mixin;

import haron.module.ModuleManager;
import haron.modules.visuals.RenderTweaks;
import haron.modules.visuals.TimeChanger;
import haron.modules.visuals.WorldCustomizer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ClientWorld.class})
public abstract class ClientWorldMixin {
    @Inject(method={"getSkyColor"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetSkyColor(Vec3d Vec3dVar, float f, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        WorldCustomizer worldCustomizer = ModuleManager.WORLD_CUSTOMIZER;
        if (worldCustomizer.r()) {
            callbackInfoReturnable.setReturnValue(worldCustomizer.t());
            return;
        }
        RenderTweaks renderTweaks = ModuleManager.RENDER_TWEAKS;
        if (!renderTweaks.n()) {
            return;
        }
        float fMax = Math.max(0.0f, Math.min(1.0f, (float)(1.0 - Math.cos((double)((ClientWorld)(Object)this).getSkyAngle(f) * Math.PI * 2.0) * 2.0 + 0.5)));
        callbackInfoReturnable.setReturnValue(ColorHelper.fromFloats((float)1.0f, (float)(0.7529412f * fMax), (float)(0.84705883f * fMax), (float)(1.0f * fMax)));
    }

    @Inject(method={"getCloudsColor"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetCloudsColor(float f, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        if (ModuleManager.RENDER_TWEAKS.n()) {
            float fMax = Math.max(0.0f, Math.min(1.0f, (float)(Math.cos((double)((ClientWorld)(Object)this).getSkyAngle(f) * Math.PI * 2.0) * 2.0 + 0.5))) * 0.9f + 0.1f;
            callbackInfoReturnable.setReturnValue(ColorHelper.fromFloats((float)1.0f, (float)fMax, (float)fMax, (float)(fMax * 0.85f + 0.15f)));
        }
    }

    @Inject(method={"getLightningTicksLeft"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetLightningTicks(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        if (ModuleManager.RENDER_TWEAKS.n()) {
            callbackInfoReturnable.setReturnValue(0);
        }
    }

    @Inject(method={"getSkyBrightness"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetSkyBrightness(float f, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        TimeChanger timeChanger = ModuleManager.TIME_CHANGER;
        if (!timeChanger.k()) {
            return;
        }
        callbackInfoReturnable.setReturnValue(Float.valueOf((1.0f - Math.max(0.0f, Math.min(1.0f, 1.0f - (float)(Math.cos((double)((ClientWorld)(Object)this).getDimension().getSkyAngle(timeChanger.n()) * Math.PI * 2.0) * 2.0 + 0.2)))) * 0.8f + 0.2f));
    }

    @Inject(method={"getStarBrightness"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetStarBrightness(float f, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        TimeChanger timeChanger = ModuleManager.TIME_CHANGER;
        if (!timeChanger.k()) {
            return;
        }
        float fMax = Math.max(0.0f, Math.min(1.0f, 1.0f - (float)(Math.cos((double)((ClientWorld)(Object)this).getDimension().getSkyAngle(timeChanger.n()) * Math.PI * 2.0) * 2.0 + 0.25)));
        callbackInfoReturnable.setReturnValue(Float.valueOf(fMax * fMax * 0.5f));
    }
}


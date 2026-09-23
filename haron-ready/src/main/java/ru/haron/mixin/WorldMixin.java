package ru.haron.mixin;

import haron.module.ModuleManager;
import haron.modules.visuals.RenderTweaks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={World.class})
public class WorldMixin {
    @Inject(method={"isRaining"}, at={@At(value="HEAD")}, cancellable=true)
    private void onIsRaining(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        RenderTweaks renderTweaks = ModuleManager.RENDER_TWEAKS;
        if (!renderTweaks.n()) {
            return;
        }
        callbackInfoReturnable.setReturnValue(false);
    }

    @Inject(method={"isThundering"}, at={@At(value="HEAD")}, cancellable=true)
    private void onIsThundering(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        RenderTweaks renderTweaks = ModuleManager.RENDER_TWEAKS;
        if (!renderTweaks.n()) {
            return;
        }
        callbackInfoReturnable.setReturnValue(false);
    }

    @Inject(method={"hasRain"}, at={@At(value="HEAD")}, cancellable=true)
    private void onHasRain(BlockPos BlockPosVar, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        RenderTweaks renderTweaks = ModuleManager.RENDER_TWEAKS;
        if (!renderTweaks.n()) {
            return;
        }
        callbackInfoReturnable.setReturnValue(false);
    }

    @Inject(method={"getRainGradient"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetRainGradient(float f, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        RenderTweaks renderTweaks = ModuleManager.RENDER_TWEAKS;
        if (!renderTweaks.n()) {
            return;
        }
        callbackInfoReturnable.setReturnValue(Float.valueOf(0.0f));
    }

    @Inject(method={"getThunderGradient"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetThunderGradient(float f, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        RenderTweaks renderTweaks = ModuleManager.RENDER_TWEAKS;
        if (!renderTweaks.n()) {
            return;
        }
        callbackInfoReturnable.setReturnValue(Float.valueOf(0.0f));
    }
}


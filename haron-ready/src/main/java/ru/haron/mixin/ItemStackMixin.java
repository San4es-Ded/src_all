package ru.haron.mixin;

import haron.events.ItemUseEvent;
import haron.events.EventDispatcher;
import haron.modules.utilities.Optimizations;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ItemStack.class})
public class ItemStackMixin {
    @Inject(method={"finishUsing"}, at={@At(value="HEAD")})
    private void onItemUseFinish(World WorldVar, LivingEntity LivingEntityVar, CallbackInfoReturnable<ItemStack> callbackInfoReturnable) {
        ItemUseEvent itemUseFinishEvent = new ItemUseEvent((ItemStack)(Object)this, WorldVar, LivingEntityVar);
        EventDispatcher.EVENT_BUS.post((Object)itemUseFinishEvent);
        if (itemUseFinishEvent.c()) {
            callbackInfoReturnable.cancel();
        }
    }

    @Inject(method={"hasGlint"}, at={@At(value="HEAD")}, cancellable=true)
    private void haron$noGlint(CallbackInfoReturnable<Boolean> cir) {
        if (Optimizations.hideGlint()) {
            cir.setReturnValue(false);
        }
    }
}


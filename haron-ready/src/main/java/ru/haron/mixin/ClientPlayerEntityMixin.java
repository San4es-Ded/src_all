package ru.haron.mixin;

import haron.events.ClientTickEvent;
import haron.events.DropItemEvent;
import haron.events.EventDispatcher;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ClientPlayerEntity.class})
public class ClientPlayerEntityMixin {
    @Inject(method={"tick"}, at={@At(value="HEAD")})
    private void onTick(CallbackInfo callbackInfo) {
        EventDispatcher.EVENT_BUS.post((Object)new ClientTickEvent());
    }

    @Inject(method={"dropSelectedItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void onDropSelectedItem(boolean z, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        DropItemEvent dropItemEvent = new DropItemEvent(((ClientPlayerEntity)(Object)this).getInventory().selectedSlot, z);
        EventDispatcher.EVENT_BUS.post((Object)dropItemEvent);
        if (dropItemEvent.c()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
}


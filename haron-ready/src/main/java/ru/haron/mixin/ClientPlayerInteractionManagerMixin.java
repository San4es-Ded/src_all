package ru.haron.mixin;

import haron.events.DropItemEvent;
import haron.events.PlayerInteractEvent;
import haron.events.EventDispatcher;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ClientPlayerInteractionManager.class})
public class ClientPlayerInteractionManagerMixin {
    @Inject(method={"interactItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRightClick(PlayerEntity PlayerEntityVar, Hand HandVar, CallbackInfoReturnable<ActionResult> callbackInfoReturnable) {
        PlayerInteractEvent itemUseEvent = new PlayerInteractEvent(PlayerEntityVar, PlayerEntityVar.getWorld(), HandVar);
        EventDispatcher.EVENT_BUS.post((Object)itemUseEvent);
        if (itemUseEvent.c()) {
            callbackInfoReturnable.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(method={"clickSlot"}, at={@At(value="HEAD")}, cancellable=true)
    private void onClickSlot(int i, int i2, int i3, SlotActionType SlotActionTypeVar, PlayerEntity PlayerEntityVar, CallbackInfo callbackInfo) {
        if (SlotActionTypeVar == SlotActionType.THROW) {
            int i4 = i2;
            if (i2 >= 0 && i2 < 9) {
                i4 = i2 + 27;
            } else if (i2 >= 36 && i2 < 45) {
                i4 = i2 - 36;
            } else if (i2 == 45) {
                // empty if block
            }
            DropItemEvent dropItemEvent = new DropItemEvent(i4, i3 == 1);
            EventDispatcher.EVENT_BUS.post((Object)dropItemEvent);
            if (dropItemEvent.c()) {
                callbackInfo.cancel();
            }
        }
    }
}


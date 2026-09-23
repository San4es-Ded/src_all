package ru.prism.mixin;


import ru.prism.manager.event_impl.AttackEvent;
import ru.prism.manager.event_impl.DropItemEvent;
import ru.prism.manager.event_impl.EventType;
import ru.prism.manager.event_impl.UsingItemEvent;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(method = "interactItem", at = @At(value = "RETURN"))
    public void interactItemHook(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (cir.getReturnValue() instanceof ActionResult.Success success && !success.swingSource().equals(ActionResult.SwingSource.CLIENT)) {
            UsingItemEvent event = new UsingItemEvent(EventType.PRE);
            event.hook();
        }
    }
    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
         AttackEvent event =  new AttackEvent(target);

        event.hook();;

        if(event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "stopUsingItem", at = @At("HEAD"),cancellable = true)
    public void stopUsingItemHook(CallbackInfo ci) {
        UsingItemEvent event = new UsingItemEvent(EventType.POST);
        event.hook();
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "interactItem", at = @At(value = "HEAD"), cancellable = true)
    private void gameModeHook(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        UsingItemEvent event = new UsingItemEvent(EventType.START);
        event.hook();
        if (event.isCancelled()) cir.setReturnValue(ActionResult.PASS);
    }

    @Inject(method = "clickSlot", at = @At("HEAD"), cancellable = true)
    private void onClickSlot(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (actionType != SlotActionType.THROW) return;

        int slot = slotId;
        if (slotId >= 0 && slotId < 9) {
            slot = slotId + 27;
        } else if (slotId >= 36 && slotId < 45) {
            slot = slotId - 36;
        }

        DropItemEvent event = new DropItemEvent(slot, button == 1);
        event.hook();
        if (event.isCancelled()) {
            ci.cancel();
        }
    }



}

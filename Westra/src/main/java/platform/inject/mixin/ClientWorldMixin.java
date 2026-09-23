package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.BlockChangeEvent;
import aethereal.event.PotionEvent;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_638.class})
public class ClientWorldMixin {
   @Inject(
      method = {"method_8444(Lnet/minecraft/class_1657;ILnet/minecraft/class_2338;I)V"},
      at = {@At("HEAD")}
   )
   private void onSyncWorldEvent(class_1657 player, int eventId, class_2338 pos, int data, CallbackInfo ci) {
      if (eventId == 2002) {
         EventManager.a((IEvent)(new PotionEvent(PotionEvent.a.PARTICLES, data, pos)));
      }
   }

   @Inject(
      method = {"method_41928"},
      at = {@At("HEAD")}
   )
   private void onHandleBlockUpdate(class_2338 pos, class_2680 state, int flags, CallbackInfo ci) {
      class_638 world = (class_638)this;
      class_2680 oldState = world.method_8320(pos);
      if (oldState != state) {
         EventManager.a((IEvent)(new BlockChangeEvent(pos.method_10062(), oldState, state)));
      }
   }
}

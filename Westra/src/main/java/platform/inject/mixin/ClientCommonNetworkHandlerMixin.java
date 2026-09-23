package platform.inject.mixin;

import aethereal.core.Westra;
import net.minecraft.class_2720;
import net.minecraft.class_2856;
import net.minecraft.class_8673;
import net.minecraft.class_2856.class_2857;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_8673.class})
public class ClientCommonNetworkHandlerMixin {
   @Inject(
      method = {"method_52784"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onResourcePackSend(class_2720 packet, CallbackInfo ci) {
      if (Westra.h().d().t().v().m()) {
         class_8673 self = (class_8673)this;
         self.method_52787(new class_2856(packet.comp_2158(), class_2857.field_13018));
         ci.cancel();
      }
   }
}

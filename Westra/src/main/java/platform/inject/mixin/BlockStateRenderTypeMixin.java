package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import net.minecraft.class_2464;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
   targets = {"net/minecraft/class_4970$class_4971"}
)
public class BlockStateRenderTypeMixin implements Interface {
   @Inject(
      method = {"method_26217"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getRenderType(CallbackInfoReturnable<class_2464> cir) {
      if (Westra.h() != null) {
         if (Westra.h().d().t().bi().a((class_2680)this)) {
            cir.setReturnValue(class_2464.field_11455);
         }
      }
   }
}

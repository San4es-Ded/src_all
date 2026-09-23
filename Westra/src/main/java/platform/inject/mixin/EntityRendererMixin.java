package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import net.minecraft.class_1297;
import net.minecraft.class_5498;
import net.minecraft.class_897;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_897.class})
public class EntityRendererMixin implements Interface {
   @Inject(
      method = {"method_3921"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hasLabel(class_1297 entity, double distance, CallbackInfoReturnable<Boolean> cir) {
      if (entity == aM_.field_1724 && Westra.h().d().t().cn().m() && aM_.field_1690.method_31044() != class_5498.field_26664) {
         cir.setReturnValue(Boolean.TRUE);
      } else {
         if (Westra.h().d().t().co().q()) {
            cir.setReturnValue(Boolean.FALSE);
         }
      }
   }
}

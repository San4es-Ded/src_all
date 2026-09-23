package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import net.minecraft.class_1297;
import net.minecraft.class_4604;
import net.minecraft.class_898;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_898.class})
public class EntityRenderDispatcherMixin implements Interface {
   @Inject(
      method = {"method_3950"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private <E extends class_1297> void shouldRender(E entity, class_4604 frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
      double limit = Westra.h().d().t().bi().u();
      if (limit > 0.0 && entity != aM_.field_1724 && entity.method_5649(x, y, z) > limit) {
         cir.setReturnValue(Boolean.FALSE);
      }
   }
}

package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.module.render.Zoom;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_757.class})
public class ZoomMixin implements Interface {
   @ModifyReturnValue(
      method = {"method_3196"},
      at = {@At("RETURN")}
   )
   private float modifyFov(float original) {
      if (Westra.h() == null) {
         return original;
      } else {
         Zoom zoom = Westra.h().d().t().bp();
         return zoom != null && zoom.m() ? (float)(original / zoom.q()) : original;
      }
   }
}

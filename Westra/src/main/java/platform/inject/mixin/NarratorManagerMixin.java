package platform.inject.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_333;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_333.class})
public class NarratorManagerMixin {
   @ModifyReturnValue(
      method = {"method_1791"},
      at = {@At("RETURN")}
   )
   private boolean isActive(boolean original) {
      return false;
   }
}

package platform.inject.mixin;

import net.minecraft.class_471;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({class_471.class})
public abstract class AnvilScreenMixin {
   @ModifyConstant(
      method = {"method_2388"},
      constant = {@Constant(
         intValue = 40
      )}
   )
   private int drawForeground(int constant) {
      return Integer.MAX_VALUE;
   }
}

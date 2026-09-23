package platform.inject.mixin;

import aethereal.util.GameSpeed;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.minecraft.class_9779.class_9781;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({class_9781.class})
public abstract class RenderTickCounterDynamicMixin {
   @Redirect(
      method = {"method_60639(J)I"},
      at = @At(
         value = "INVOKE",
         target = "Lit/unimi/dsi/fastutil/floats/FloatUnaryOperator;apply(F)F"
      )
   )
   private float westraTimer(FloatUnaryOperator operator, float tickTime) {
      return operator.apply(tickTime) / GameSpeed.a();
   }
}

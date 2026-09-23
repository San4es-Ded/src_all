package platform.inject.mixin;

import aethereal.mixin.ITransparentState;
import net.minecraft.class_10042;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_10042.class})
public class LivingEntityRenderStateMixin implements ITransparentState {
   @Unique
   private float westraAlpha = 1.0F;

   @Override
   public float getWestraAlpha() {
      return this.westraAlpha;
   }

   @Override
   public void setWestraAlpha(float alpha) {
      this.westraAlpha = alpha;
   }
}

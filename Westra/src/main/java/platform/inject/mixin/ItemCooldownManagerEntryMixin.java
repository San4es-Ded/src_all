package platform.inject.mixin;

import aethereal.mixin.IItemCooldownManager;
import aethereal.render.AnimationUtil;
import lombok.Generated;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(
   targets = {"net/minecraft/class_1796$class_1797"}
)
public class ItemCooldownManagerEntryMixin implements IItemCooldownManager {
   @Unique
   private final AnimationUtil animation = new AnimationUtil();

   @Generated
   @Override
   public AnimationUtil getAnimation() {
      return this.animation;
   }
}

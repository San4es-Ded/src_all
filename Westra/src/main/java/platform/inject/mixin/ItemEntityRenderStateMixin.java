package platform.inject.mixin;

import aethereal.mixin.IItemEntityRenderState;
import lombok.Generated;
import net.minecraft.class_10039;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_10039.class})
public abstract class ItemEntityRenderStateMixin implements IItemEntityRenderState {
   @Unique
   private boolean onGround;

   @Generated
   @Override
   public boolean isOnGround() {
      return this.onGround;
   }

   @Generated
   @Override
   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
   }
}

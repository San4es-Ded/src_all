package platform.inject.mixin;

import aethereal.mixin.IEmotionState;
import net.minecraft.class_10055;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_10055.class})
public class PlayerEntityRenderStateMixin implements IEmotionState {
   @Unique
   private boolean selfEmotion;

   @Override
   public boolean getSelfEmotion() {
      return this.selfEmotion;
   }

   @Override
   public void setSelfEmotion(boolean self) {
      this.selfEmotion = self;
   }
}

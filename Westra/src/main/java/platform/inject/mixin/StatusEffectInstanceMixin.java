package platform.inject.mixin;

import aethereal.mixin.IStatusEffectInstance;
import aethereal.render.AnimationUtil;
import lombok.Generated;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_6880;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1293.class})
public abstract class StatusEffectInstanceMixin implements IStatusEffectInstance {
   @Unique
   private final AnimationUtil animation = new AnimationUtil();
   @Unique
   private int initialDuration;

   @Shadow
   @Override
   public abstract int method_5584();

   @Shadow
   @Override
   public abstract int method_5578();

   @Shadow
   @Override
   public abstract class_6880<class_1291> method_5579();

   @Generated
   @Override
   public AnimationUtil getAnimation() {
      return this.animation;
   }

   @Generated
   @Override
   public int getInitialDuration() {
      return this.initialDuration;
   }

   @Generated
   @Override
   public void setInitialDuration(int initialDuration) {
      this.initialDuration = initialDuration;
   }

   @Inject(
      method = {"<init>*"},
      at = {@At("TAIL")}
   )
   private void onInit(CallbackInfo ci) {
      this.initialDuration = ((class_1293)this).method_5584();
   }
}

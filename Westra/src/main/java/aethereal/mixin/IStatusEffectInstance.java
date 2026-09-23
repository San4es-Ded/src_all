package aethereal.mixin;

import aethereal.render.AnimationUtil;
import net.minecraft.class_1291;
import net.minecraft.class_6880;

public interface IStatusEffectInstance {
   AnimationUtil getAnimation();

   int getInitialDuration();

   void setInitialDuration(int var1);

   int method_5584();

   int method_5578();

   class_6880<class_1291> method_5579();
}

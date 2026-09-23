package platform.inject.accessors;

import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_1309.class})
public interface LivingEntityAccessor {
   @Accessor("field_6228")
   int getJumpingCooldown();

   @Accessor("field_6228")
   @Mutable
   void setJumpingCooldown(int var1);

   @Invoker("method_6106")
   float invokeGetJumpVelocity();
}

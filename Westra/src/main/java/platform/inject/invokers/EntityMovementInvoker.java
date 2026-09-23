package platform.inject.invokers;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_1297.class})
public interface EntityMovementInvoker {
   @Invoker("method_17835")
   class_243 getAdjustMovementForCollisions(class_243 var1);
}

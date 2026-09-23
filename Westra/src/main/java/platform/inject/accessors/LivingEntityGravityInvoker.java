package platform.inject.accessors;

import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_1309.class})
public interface LivingEntityGravityInvoker {
   @Invoker("method_7490")
   double getGravityInvoker();
}

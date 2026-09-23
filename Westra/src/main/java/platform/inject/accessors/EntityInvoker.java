package platform.inject.accessors;

import net.minecraft.class_1297;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_1297.class})
public interface EntityInvoker {
   @Invoker("method_31482")
   void unset();

   @Invoker("method_5670")
   void baseTickInvoker();
}

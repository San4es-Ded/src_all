package platform.inject.invokers;

import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_310.class})
public interface MinecraftClientInvoker {
   @Invoker("method_1536")
   boolean invokeDoAttack();

   @Invoker("method_1583")
   void invokeDoItemUse();
}

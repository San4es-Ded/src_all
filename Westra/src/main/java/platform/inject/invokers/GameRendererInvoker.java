package platform.inject.invokers;

import net.minecraft.class_4184;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_757.class})
public interface GameRendererInvoker {
   @Invoker("method_3196")
   float invokeGetFov(class_4184 var1, float var2, boolean var3);
}

package platform.inject.invokers;

import net.minecraft.class_636;
import net.minecraft.class_638;
import net.minecraft.class_7204;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_636.class})
public interface ClientPlayerInteractionManagerInvoker {
   @Invoker("method_41931")
   void invokeSendSequencedPacket(class_638 var1, class_7204 var2);
}

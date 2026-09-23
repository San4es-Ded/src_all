package platform.inject.accessors;

import net.minecraft.class_2535;
import net.minecraft.class_2547;
import net.minecraft.class_2596;
import net.minecraft.class_7648;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_2535.class})
public interface ClientConnectionAccessor {
   @Invoker("method_10764")
   void sendWithoutEvent(class_2596<?> var1, class_7648 var2, boolean var3);

   @Accessor("field_11652")
   void setPacketListener(class_2547 var1);
}

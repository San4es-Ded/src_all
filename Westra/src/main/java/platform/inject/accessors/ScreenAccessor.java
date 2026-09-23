package platform.inject.accessors;

import net.minecraft.class_2561;
import net.minecraft.class_364;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_437.class})
public interface ScreenAccessor {
   @Invoker("method_37063")
   <T extends class_364> T invokeAddDrawableChild(T var1);

   @Accessor("field_22789")
   int getWidth();

   @Accessor("field_22790")
   int getHeight();

   @Accessor("field_22785")
   class_2561 getTitle();
}

package platform.inject.accessors;

import net.minecraft.class_4185;
import net.minecraft.class_4185.class_4241;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_4185.class})
public interface ButtonWidgetAccessor {
   @Accessor("field_22767")
   class_4241 getOnPress();

   @Accessor("field_22767")
   @Mutable
   void setOnPress(class_4241 var1);
}

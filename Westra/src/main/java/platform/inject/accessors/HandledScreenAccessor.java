package platform.inject.accessors;

import net.minecraft.class_1703;
import net.minecraft.class_1735;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_465.class})
public interface HandledScreenAccessor {
   @Accessor("field_2776")
   int getX();

   @Accessor("field_2800")
   int getY();

   @Accessor("field_2787")
   class_1735 getFocusedSlot();

   @Accessor("field_2792")
   int getBackgroundWidth();

   @Accessor("field_2779")
   int getBackgroundHeight();

   @Accessor("field_2797")
   class_1703 getScreenHandler();
}

package platform.inject.accessors;

import net.minecraft.class_2813;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2813.class})
public interface ClickSlotC2SPacketAccessor {
   @Accessor("field_12819")
   int getSyncId();

   @Accessor("field_12818")
   int getSlot();

   @Accessor("field_12818")
   @Mutable
   void setSlot(int var1);
}

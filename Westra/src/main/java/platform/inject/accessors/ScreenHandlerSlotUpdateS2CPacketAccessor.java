package platform.inject.accessors;

import net.minecraft.class_2653;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2653.class})
public interface ScreenHandlerSlotUpdateS2CPacketAccessor {
   @Accessor("field_12152")
   int getSyncId();

   @Accessor("field_12151")
   int getSlot();

   @Accessor("field_12151")
   @Mutable
   void setSlot(int var1);
}

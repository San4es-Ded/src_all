package platform.inject.accessors;

import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_746.class})
public interface ClientPlayerEntityAccessor {
   @Accessor("field_3941")
   float getLastYaw();

   @Accessor("field_3925")
   float getLastPitch();

   @Accessor("field_3919")
   void setWasSprinting(boolean var1);

   @Accessor("field_3919")
   boolean getWasSprinting();
}

package platform.inject.accessors;

import net.minecraft.class_2604;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2604.class})
public interface EntitySpawnS2CPacketAccessor {
   @Accessor("field_11948")
   double getX();

   @Accessor("field_11946")
   double getY();
}

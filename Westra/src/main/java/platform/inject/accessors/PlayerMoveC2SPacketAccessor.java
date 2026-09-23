package platform.inject.accessors;

import net.minecraft.class_2828;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2828.class})
public interface PlayerMoveC2SPacketAccessor {
   @Accessor("field_12887")
   void setYaw(float var1);

   @Accessor("field_12885")
   void setPitch(float var1);
}

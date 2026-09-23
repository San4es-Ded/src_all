package platform.inject.accessors;

import net.minecraft.class_1685;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1685.class})
public interface TridentEntityAccessor {
   @Accessor("field_7649")
   int getReturnTimer();
}

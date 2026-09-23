package platform.inject.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
   targets = {"net/minecraft/class_1796$class_1797"}
)
public interface ItemCooldownEntryAccessor {
   @Accessor("comp_3084")
   int getEndTick();

   @Accessor("comp_3083")
   int getStartTick();
}

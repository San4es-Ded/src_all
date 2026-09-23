package platform.inject.accessors;

import net.minecraft.class_357;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_357.class})
public interface SliderWidgetAccessor {
   @Accessor("field_22753")
   double getValue();
}

package platform.inject.accessors;

import java.util.Map;
import net.minecraft.class_1796;
import net.minecraft.class_2960;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1796.class})
public interface ItemCooldownManagerAccessor {
   @Accessor("field_8024")
   Map<class_2960, Object> getEntries();

   @Accessor("field_8025")
   int getTick();
}

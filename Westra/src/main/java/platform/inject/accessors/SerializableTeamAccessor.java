package platform.inject.accessors;

import net.minecraft.class_2561;
import net.minecraft.class_5900.class_5902;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_5902.class})
public interface SerializableTeamAccessor {
   @Accessor("field_29159")
   @Mutable
   void setPrefix(class_2561 var1);
}

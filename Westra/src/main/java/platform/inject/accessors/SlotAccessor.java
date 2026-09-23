package platform.inject.accessors;

import net.minecraft.class_1263;
import net.minecraft.class_1735;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1735.class})
public interface SlotAccessor {
   @Accessor("field_7871")
   class_1263 getInventory();

   @Accessor("field_7873")
   @Mutable
   void setX(int var1);

   @Accessor("field_7872")
   @Mutable
   void setY(int var1);
}

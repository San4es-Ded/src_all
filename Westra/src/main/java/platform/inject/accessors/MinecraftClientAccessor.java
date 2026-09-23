package platform.inject.accessors;

import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_310.class})
public interface MinecraftClientAccessor {
   @Accessor("field_1752")
   int getItemUseCooldown();

   @Accessor("field_1752")
   @Mutable
   void setItemUseCooldown(int var1);
}

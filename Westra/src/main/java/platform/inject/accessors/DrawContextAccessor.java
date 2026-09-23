package platform.inject.accessors;

import net.minecraft.class_10444;
import net.minecraft.class_332;
import net.minecraft.class_4597.class_4598;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_332.class})
public interface DrawContextAccessor {
   @Accessor("field_44658")
   class_4598 getVertexConsumers();

   @Accessor("field_55257")
   class_10444 getItemRenderState();
}

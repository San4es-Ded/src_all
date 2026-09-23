package platform.inject.accessors;

import java.util.Map;
import java.util.Queue;
import net.minecraft.class_3999;
import net.minecraft.class_702;
import net.minecraft.class_703;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_702.class})
public interface ParticleManagerAccessor {
   @Accessor("field_3830")
   Map<class_3999, Queue<class_703>> getParticles();
}

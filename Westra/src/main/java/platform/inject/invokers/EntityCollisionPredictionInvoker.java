package platform.inject.invokers;

import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_265;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_1297.class})
public interface EntityCollisionPredictionInvoker {
   @Invoker("method_59920")
   static List<class_265> findCollisionsForMovement(class_1297 entity, class_1937 world, List<class_265> reusable, class_238 box) {
      throw new AssertionError();
   }

   @Invoker("method_20736")
   static class_243 adjustMovementForCollisions(class_1297 entity, class_243 movement, class_238 boundingBox, class_1937 world, List<class_265> collisions) {
      throw new AssertionError();
   }
}

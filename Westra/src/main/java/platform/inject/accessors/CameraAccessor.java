package platform.inject.accessors;

import net.minecraft.class_4184;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_4184.class})
public interface CameraAccessor {
   @Invoker("method_19325")
   void invokeSetRotation(float var1, float var2);

   @Invoker("method_19327")
   void invokeSetPos(double var1, double var3, double var5);
}

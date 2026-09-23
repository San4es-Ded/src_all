package ru.pulse.mixin;

import net.minecraft.client.render.FrameGraphBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FrameGraphBuilder.class)
public abstract class FrameGraphBuilderMixin {
   @Redirect(method = {"run", "method_61910"}, at = @At(value = "INVOKE", target = "Ljava/lang/Runnable;run()V"), require = 0)
   private void pulse$runFramePassSafely(Runnable runnable) {
      try {
         runnable.run();
      } catch (IllegalStateException e) {
         if (!"Pose stack not empty".equals(e.getMessage())) {
            throw e;
         }
      }
   }
}

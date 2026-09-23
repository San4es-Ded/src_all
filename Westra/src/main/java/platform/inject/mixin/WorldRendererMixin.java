package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.RemovalsEvent;
import aethereal.module.render.BlockOverlay;
import net.minecraft.class_243;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_761;
import net.minecraft.class_9909;
import net.minecraft.class_9958;
import net.minecraft.class_4597.class_4598;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_761.class})
public class WorldRendererMixin implements Interface {
   @Inject(
      method = {"method_62210"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderTargetBlockOutline(class_4184 camera, class_4598 immediate, class_4587 matrices, boolean translucent, CallbackInfo ci) {
      BlockOverlay overlay = Westra.h().d().t().be();
      if (overlay.m() && overlay.q().c()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"method_62203"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderWeather(class_9909 frameGraphBuilder, class_243 pos, float tickDelta, class_9958 fog, CallbackInfo ci) {
      RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.WEATHER);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }

   @ModifyVariable(
      method = {"method_3273(Lnet/minecraft/class_4184;Lnet/minecraft/class_4604;ZZ)V"},
      at = @At("HEAD"),
      argsOnly = true,
      index = 4
   )
   private boolean onSetupTerrain(boolean spectator) {
      return Westra.h().d().t().h().m() || spectator;
   }
}

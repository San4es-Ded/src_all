 package su.sacura.mixin.client;
 
 import net.minecraft.client.MinecraftClient;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
 import su.sacura.Sacura;
 import su.sacura.events.tick.TickEvent;
 
 @Mixin({MinecraftClient.class})
 public class MinecraftClientMixin {
   @Inject(method = {"tick"}, at = {@At("HEAD")})
   private void eventTick(CallbackInfo ci) {
     Sacura.getInstance().getEventBus().post(new TickEvent());
   }
   
   @Inject(method = {"updateWindowTitle"}, at = {@At("HEAD")}, cancellable = true)
   private void title(CallbackInfo ci) {
     MinecraftClient.getInstance().getWindow().setTitle("Sacura Recode");
     ci.cancel();
   }
 }



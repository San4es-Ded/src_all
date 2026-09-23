 package su.sacura.mixin.client.world;
 
 import net.minecraft.client.world.ClientWorld;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.Shadow;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
 import su.sacura.Sacura;
 import su.sacura.features.modules.impl.render.WorldTweaksModule;
 import su.sacura.util.type.MinecraftWrapper;
 
 @Mixin({ClientWorld.Properties.class})
 public class ClientWorldPropertiesMixin implements MinecraftWrapper {
   @Shadow
   private long field_24439;
   
   @Inject(method = {"setTimeOfDay"}, at = {@At("HEAD")}, cancellable = true)
   public void setTimeOfDayHook(long timeOfDay, CallbackInfo ci) {
     WorldTweaksModule tweaks = (WorldTweaksModule)Sacura.getInstance().getModuleManager().getModule(WorldTweaksModule.class);
     if (tweaks.enable && ((Boolean)tweaks.world.getValueByName("Время").get()).booleanValue()) {
       this.field_24439 = ((Float)tweaks.time.get()).intValue() * 1000L;
       ci.cancel();
     } 
   }
 }



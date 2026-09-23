 package su.sacura.mixin.client.render;
 
 import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
 import net.minecraft.client.render.LightmapTextureManager;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
 import su.sacura.Sacura;
 import su.sacura.features.modules.impl.render.FullBrightModule;
 import su.sacura.features.modules.impl.render.NoOverlayModule;
 
 @Mixin({LightmapTextureManager.class})
 public class LightmapTextureManagerMixin {
   @ModifyExpressionValue(method = {"update(F)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;")})
   private Object injectXRayFullBright(Object original) {
     FullBrightModule full = (FullBrightModule)Sacura.getInstance().getModuleManager().getModule(FullBrightModule.class);
     if (full.enable && full.mode.is("Гамма"))
       return Double.valueOf(Math.max(((Double)original).doubleValue(), (((Float)full.bright.get()).floatValue() * 10.0F))); 
     return original;
   }
   
   @Inject(method = {"getDarkness"}, at = {@At("HEAD")}, cancellable = true)
   private void removeDarknessEffect(CallbackInfoReturnable<Float> cir) {
     NoOverlayModule noRender = (NoOverlayModule)Sacura.getInstance().getModuleManager().getModule(NoOverlayModule.class);
     if (noRender != null && noRender.enable && ((Boolean)noRender.delete.getValueByName("Тень").get()).booleanValue())
       cir.setReturnValue(Float.valueOf(0.0F)); 
   }
   
   @Inject(method = {"update"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/render/LightmapTextureManager;getDarknessFactor(F)F")}, cancellable = true)
   private void cancelDarknessInUpdate(float delta, CallbackInfo ci) {
     NoOverlayModule noRender = (NoOverlayModule)Sacura.getInstance().getModuleManager().getModule(NoOverlayModule.class);
     if (noRender != null && noRender.enable && ((Boolean)noRender.delete.getValueByName("Тень").get()).booleanValue())
       return; 
   }
 }



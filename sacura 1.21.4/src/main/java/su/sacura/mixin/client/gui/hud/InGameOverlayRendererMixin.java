 package su.sacura.mixin.client.gui.hud;
 
 import net.minecraft.client.MinecraftClient;
 import net.minecraft.client.gui.hud.InGameOverlayRenderer;
 import net.minecraft.client.render.VertexConsumerProvider;
 import net.minecraft.client.util.math.MatrixStack;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
 import su.sacura.Sacura;
 import su.sacura.features.modules.impl.render.NoOverlayModule;
 
 @Mixin({InGameOverlayRenderer.class})
 public class InGameOverlayRendererMixin {
   @Inject(method = {"renderFireOverlay"}, at = {@At("HEAD")}, cancellable = true)
   private static void renderFireOverlayHook(MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
     if (((NoOverlayModule)Sacura.getInstance().getModuleManager().getModule(NoOverlayModule.class)).enable && ((Boolean)((NoOverlayModule)Sacura.getInstance().getModuleManager().getModule(NoOverlayModule.class)).delete.getValueByName("Огонь на экране").get()).booleanValue())
       ci.cancel(); 
   }
   
   @Inject(method = {"renderUnderwaterOverlay"}, at = {@At("HEAD")}, cancellable = true)
   private static void renderUnderwaterOverlayHook(MinecraftClient client, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
     if (((NoOverlayModule)Sacura.getInstance().getModuleManager().getModule(NoOverlayModule.class)).enable && ((Boolean)((NoOverlayModule)Sacura.getInstance().getModuleManager().getModule(NoOverlayModule.class)).delete.getValueByName("Воду на экране").get()).booleanValue())
       ci.cancel(); 
   }
 }



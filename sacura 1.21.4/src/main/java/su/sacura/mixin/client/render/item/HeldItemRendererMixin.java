 package su.sacura.mixin.client.render.item;
 
 import net.minecraft.client.network.AbstractClientPlayerEntity;
 import net.minecraft.client.render.VertexConsumerProvider;
 import net.minecraft.client.render.item.HeldItemRenderer;
 import net.minecraft.client.util.math.MatrixStack;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.Hand;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
 import su.sacura.Sacura;
 import su.sacura.features.modules.impl.render.SwingAnimationsModule;
 
 @Mixin({HeldItemRenderer.class})
 public abstract class HeldItemRendererMixin {
   @Inject(method = {"renderFirstPersonItem"}, at = {@At("HEAD")}, cancellable = true)
   private void onRenderItemHook(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
     if (!item.isEmpty() && !(item.getItem() instanceof net.minecraft.item.FilledMapItem)) {
       ci.cancel();
       ((SwingAnimationsModule)Sacura.getInstance().getModuleManager().getModule(SwingAnimationsModule.class)).renderFirstPersonItem(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
     } 
   }
 }



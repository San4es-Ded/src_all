package wtf.wyvern.mixin.compat;

import com.lootbeams.containers.EntityRenderStateContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.state.ItemFrameEntityRenderState;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.wyvern.client.modules.impl.render.LootBeamsModule;

/** Stops every original LootBeams render entry point while its Wyvern module is disabled. */
@Pseudo
@org.spongepowered.asm.mixin.Mixin(targets = {
   "com.lootbeams.renderers.LootBeamRenderer",
   "com.lootbeams.managers.RenderManager",
   "com.lootbeams.renderers.HudRenderer"
})
public abstract class LootBeamsGateMixin {
   @Inject(method = "renderBeam", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
   private static void wyvern$gateBeam(CallbackInfo ci) {
      if (!LootBeamsModule.INSTANCE.isEnabled()) ci.cancel();
   }

   @Inject(method = {"onWorldRenderBeforeParticles", "onWorldRenderAfterTranslucent", "onWorldRenderAfterWeather", "onWorldRenderBeforeEnd", "onWorldRenderEnd"}, at = @At("HEAD"), cancellable = true, remap = false, require = 0)
   private static void wyvern$gateRenderCallbacks(CallbackInfo ci) {
      if (!LootBeamsModule.INSTANCE.isEnabled()) ci.cancel();
   }

   @Inject(method = "onHudRender", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
   private static void wyvern$guardHudTooltip(CallbackInfo ci) {
      if (!LootBeamsModule.INSTANCE.isEnabled()) {
         ci.cancel();
         return;
      }

      MinecraftClient client = MinecraftClient.getInstance();
      if (client.crosshairTarget instanceof EntityHitResult hit
              && hit.getEntity() instanceof ItemFrameEntity frame
              && !(EntityRenderStateContainer.getRenderState(frame) instanceof ItemFrameEntityRenderState)) {
         ci.cancel();
      }
   }
}

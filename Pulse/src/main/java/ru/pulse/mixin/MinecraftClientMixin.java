package ru.pulse.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.session.Session;
import net.minecraft.client.RunArgs;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.MinecraftClient.ChatRestriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pulse.client.MinecraftContext;
import pulse.events.AttackEntityEvent;
import pulse.events.EventBusService;
import pulse.events.WorldChangeEvent;
import pulse.gui.menu.PulseMainMenuScreen;
import pulse.render.RenderFrameTimer;
import ru.pulse.Pulse;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements MinecraftContext {
   @Shadow
   private void render(boolean tick) {
   }

   @Inject(require = 0, method = "joinWorld", at = @At("RETURN"))
   private void onWorldChange(ClientWorld ClientWorldVar, CallbackInfo callbackInfo) {
      EventBusService.EVENT_BUS.post(new WorldChangeEvent());
   }

   @Inject(require = 0, method = "<init>", at = @At("TAIL"))
   public void init(RunArgs RunArgsVar, CallbackInfo callbackInfo) {
      Pulse.getInstance().init();
   }

   @Redirect(
      method = {"render", "method_1523"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/GameRenderer;render(Lnet/minecraft/client/render/RenderTickCounter;Z)V"
      ),
      require = 0
   )
   private void pulse$renderGameSafely(GameRenderer gameRenderer, RenderTickCounter tickCounter, boolean tick) {
      try {
         gameRenderer.render(tickCounter, tick);
      } catch (IllegalStateException e) {
         if (!"Pose stack not empty".equals(e.getMessage())) {
            throw e;
         }
      }
   }

   @Redirect(
      method = {"run", "method_1514"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;render(Z)V"),
      require = 0
   )
   private void pulse$runRenderSafely(MinecraftClient client, boolean tick) {
      try {
         this.render(tick);
      } catch (IllegalStateException e) {
         if (!"Pose stack not empty".equals(e.getMessage())) {
            throw e;
         }
      }
   }

   @Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;fpsCounter:I", opcode = 181, ordinal = 0), require = 0)
   private void render(boolean z, CallbackInfo callbackInfo) {
      try {
         Class.forName("ru.pulse.Pulse").getMethod("runtimeBrandingTick").invoke(null);
      } catch (Throwable var4) {
      }

      RenderFrameTimer.b();
   }

   @Inject(require = 0, method = "isMultiplayerEnabled", at = @At("HEAD"), cancellable = true)
   public void allowsMultiplayer(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
      callbackInfoReturnable.setReturnValue(true);
   }

   @Inject(require = 0, method = "getChatRestriction", at = @At("HEAD"), cancellable = true)
   public void allowsChat(CallbackInfoReturnable<ChatRestriction> callbackInfoReturnable) {
      callbackInfoReturnable.setReturnValue(ChatRestriction.ENABLED);
   }

   @Inject(require = 0, method = "isRealmsEnabled", at = @At("HEAD"), cancellable = true)
   public void allowsRealms(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
      callbackInfoReturnable.setReturnValue(false);
   }

   @Inject(
      require = 0,
      method = "doAttack",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;attackEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V")
   )
   private void onAttack(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
      if (c.targetedEntity != null) {
         EventBusService.EVENT_BUS.post(new AttackEntityEvent(c.targetedEntity));
      }
   }

   @Inject(require = 0, method = "getSession", at = @At("HEAD"), cancellable = true)
   private void onGetSession(CallbackInfoReturnable<Session> cir) {
      if (PulseMainMenuScreen.selectedAccount != null && !PulseMainMenuScreen.selectedAccount.trim().isEmpty()) {
         Session s = PulseMainMenuScreen.createSession(PulseMainMenuScreen.selectedAccount);
         if (s != null) {
            cir.setReturnValue(s);
         }
      }
   }
}

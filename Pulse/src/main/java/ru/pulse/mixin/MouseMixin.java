package ru.pulse.mixin;

import net.minecraft.client.input.MouseInput;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pulse.events.EventBusService;
import pulse.events.KeyInputEvent;
import pulse.events.MouseButtonEvent;
import pulse.events.MouseScrollEvent;

@Mixin(Mouse.class)
public class MouseMixin {
   @Shadow
   @Final
   private MinecraftClient client;

   @Inject(require = 0, method = "onMouseButton", at = @At("HEAD"), cancellable = true)
   private void onMouseButton(long window, MouseInput mouseInput, int action, CallbackInfo callbackInfo) {
      if (window == this.client.getWindow().getHandle()) {
         MouseButtonEvent mouseButtonEvent = new MouseButtonEvent(
            mouseInput.button(),
            action,
            mouseInput.modifiers(),
            this.client.mouse.getX() * this.client.getWindow().getScaledWidth() / this.client.getWindow().getFramebufferWidth(),
            this.client.mouse.getY() * this.client.getWindow().getScaledHeight() / this.client.getWindow().getFramebufferHeight()
         );
         EventBusService.EVENT_BUS.post(mouseButtonEvent);
         if (mouseButtonEvent.c()) {
            callbackInfo.cancel();
         }
      }
   }

   @Inject(require = 0, method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
   private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo callbackInfo) {
      if (window == this.client.getWindow().getHandle()) {
         MouseScrollEvent mouseScrollEvent = new MouseScrollEvent(horizontal, vertical);
         EventBusService.EVENT_BUS.post(mouseScrollEvent);
         if (mouseScrollEvent.c()) {
            callbackInfo.cancel();
         }
      }
   }

   @Inject(require = 0, method = "onMouseButton", at = @At("TAIL"))
   private void onMouseButton1(long window, MouseInput mouseInput, int action, CallbackInfo callbackInfo) {
      if (mouseInput.button() > 1) {
         EventBusService.EVENT_BUS.post(new KeyInputEvent(1450 + mouseInput.button(), 0, action, mouseInput.modifiers()));
      }
   }
}

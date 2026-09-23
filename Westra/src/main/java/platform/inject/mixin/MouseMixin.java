package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.ClickEvent;
import aethereal.event.KeyEvent;
import aethereal.event.LookEvent;
import aethereal.event.ScrollEvent;
import aethereal.module.render.Zoom;
import aethereal.ui.screen.AssistantScreen;
import aethereal.ui.screen.SwapScreen;
import net.minecraft.class_312;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_312.class})
public class MouseMixin {
   @Inject(
      method = {"method_1601"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onMouseButton(long window, int button, int action, int modifiers, CallbackInfo ci) {
      if (Interface.aM_.field_1755 == null || Interface.aM_.field_1755 instanceof SwapScreen || Interface.aM_.field_1755 instanceof AssistantScreen) {
         EventManager.a((IEvent)(new KeyEvent(button >= 0 && button <= 7 ? -100 + button : button, 0, action, modifiers)));
      }

      if (action == 1) {
         ClickEvent event = new ClickEvent(
            Interface.aM_.field_1729.method_1603() / 2.0, Interface.aM_.field_1729.method_1604() / 2.0, button, ClickEvent.a.PRESS
         );
         EventManager.a((IEvent)event);
         if (event.a()) {
            ci.cancel();
         }
      } else {
         if (action == 0) {
            ClickEvent event2 = new ClickEvent(
               Interface.aM_.field_1729.method_1603() / 2.0, Interface.aM_.field_1729.method_1604() / 2.0, button, ClickEvent.a.RELEASE
            );
            EventManager.a((IEvent)event2);
            if (event2.a()) {
               ci.cancel();
            }
         }
      }
   }

   @Inject(
      method = {"method_1600"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onCursorPos(long window, double x, double y, CallbackInfo ci) {
      ClickEvent event = new ClickEvent(Interface.aM_.field_1729.method_1603() / 2.0, Interface.aM_.field_1729.method_1604() / 2.0, 0, ClickEvent.a.DRAG);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"method_1598"},
      at = {@At("RETURN")}
   )
   private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
      EventManager.a((IEvent)(new ScrollEvent(horizontal, vertical)));
   }

   @Redirect(
      method = {"method_1606"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_746;method_5872(DD)V"
      )
   )
   private void redirectChangeLookDirection(class_746 player, double yaw, double pitch) {
      Zoom zoom = Westra.h().d().t().bp();
      if (zoom != null && zoom.r()) {
         double factor = 1.0 / zoom.q();
         yaw *= factor;
         pitch *= factor;
      }

      LookEvent event = new LookEvent((float)yaw, (float)pitch);
      EventManager.a((IEvent)event);
      if (!event.a()) {
         player.method_5872(yaw, pitch);
      }
   }
}

package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.GlobalEvent;
import aethereal.core.IEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.HotbarEvent;
import aethereal.module.player.OpenWalls;
import aethereal.ui.screen.GUIScreen;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1764;
import net.minecraft.class_1799;
import net.minecraft.class_1812;
import net.minecraft.class_239;
import net.minecraft.class_309;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_434;
import net.minecraft.class_437;
import net.minecraft.class_5375;
import net.minecraft.class_636;
import net.minecraft.class_239.class_240;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({class_310.class})
public abstract class MinecraftClientMixin implements Interface {
   @Unique
   private static final String WESTRA_WINDOW_TITLE = "Westra Client";
   @Unique
   private Set<String> resourcePacks;

   @Inject(
      method = {"method_24287"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getWindowTitle(CallbackInfoReturnable<String> cir) {
      if (!EventManager.d()) {
         cir.setReturnValue("Westra Client");
      }
   }

   @Inject(
      method = {"method_1507"},
      at = {@At("HEAD")}
   )
   private void onSetScreen(class_437 screen, CallbackInfo ci) {
      if (screen instanceof class_5375) {
         this.resourcePacks = aM_.method_1520().method_14444().stream().map(v0 -> v0.method_14463()).collect(Collectors.toCollection(HashSet::new));
      } else if (this.resourcePacks != null && !(aM_.field_1755 instanceof class_5375)) {
         this.resourcePacks = null;
      }
   }

   @Inject(
      method = {"method_1521()Ljava/util/concurrent/CompletableFuture;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void reloadResources(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
      if (this.resourcePacks != null) {
         Set<String> current = aM_.method_1520().method_14444().stream().map(v0 -> v0.method_14463()).collect(Collectors.toSet());
         if (this.resourcePacks.equals(current)) {
            cir.setReturnValue(CompletableFuture.completedFuture(null));
         }

         this.resourcePacks = null;
      }
   }

   @Inject(
      method = {"method_1507"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void setScreen(class_437 screen, CallbackInfo ci) {
      if (aM_.field_1755 instanceof GUIScreen && (screen == null || screen instanceof class_434)) {
         for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().equals(class_437.class.getName()) || element.getClassName().equals(class_309.class.getName())) {
               return;
            }
         }

         ci.cancel();
      }
   }

   @Inject(
      method = {"method_1574"},
      at = {@At("HEAD")}
   )
   private void onGlobalTick(CallbackInfo ci) {
      EventManager.a((IEvent)(new GlobalEvent()));
   }

   @Redirect(
      method = {"method_1508"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_636;method_2897(Lnet/minecraft/class_1657;)V"
      )
   )
   private void handleInputEvents(class_636 manager, class_1657 player) {
      if (!Westra.h().d().v().k().a()) {
         if (player.method_6115()) {
            class_1799 stack = player.method_6030();
            if (stack.method_7909() instanceof class_1764
               && stack.method_7909().method_7881(stack, player) - player.method_6014() <= class_1764.method_7775(stack, player)) {
               return;
            }
         }

         manager.method_2897(player);
      }
   }

   @Inject(
      method = {"method_1508"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/class_1661;field_7545:I",
         opcode = 181
      )},
      cancellable = true,
      locals = LocalCapture.CAPTURE_FAILSOFT
   )
   private void handleInputEvents(CallbackInfo ci, int i) {
      HotbarEvent event = new HotbarEvent(i);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"method_1583"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void doItemUse(CallbackInfo ci) {
      class_1799 stack = aM_.field_1724.method_5998(class_1268.field_5808);
      if (stack.method_7909() instanceof class_1812 && aM_.field_1724.method_7357().method_7904(stack)) {
         ci.cancel();
      }

      if (stack.method_7909() instanceof class_1764 && class_1764.method_7781(stack) && aM_.field_1724.method_7357().method_7904(stack)) {
         ci.cancel();
      }
   }

   @Redirect(
      method = {"method_1583"},
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/class_310;field_1765:Lnet/minecraft/class_239;",
         opcode = 180
      )
   )
   private class_239 doItemUse(class_310 instance) {
      OpenWalls openWalls = Westra.h().d().t().a();
      if (!openWalls.m()) {
         return instance.field_1765;
      } else {
         class_3965 hit = openWalls.a(Objects.requireNonNull(instance.field_1724));
         return (class_239)(hit != null && hit.method_17783() == class_240.field_1332 ? hit : instance.field_1765);
      }
   }
}

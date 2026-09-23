package platform.inject.mixin;

import aethereal.autobuy.BuyConfig;
import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.ContainerEvent;
import aethereal.event.KeyEvent;
import aethereal.mixin.ISlot;
import aethereal.module.misc.AutoBuy;
import aethereal.module.player.ItemScroller;
import aethereal.render.Animations;
import aethereal.ui.screen.SwapScreen;
import net.minecraft.class_124;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1733;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_4185;
import net.minecraft.class_465;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import platform.inject.accessors.HandledScreenAccessor;
import platform.inject.accessors.ScreenAccessor;

@Mixin({class_465.class})
public abstract class HandledScreenMixin<T extends class_1703> {
   @Shadow
   @Final
   protected T field_2797;
   @Unique
   private class_4185 buttonFold;
   @Unique
   private class_4185 buttonTake;
   @Unique
   private class_4185 buttonDrop;
   @Unique
   private int chestSize;

   @Inject(
      method = {"method_2385"},
      at = {@At("HEAD")}
   )
   private void onDrawSlotHead(class_332 context, class_1735 slot, CallbackInfo ci) {
      Westra.h().d().t().ce().a(context, slot.method_7677(), slot.field_7873, slot.field_7872);
      Animations animations = Westra.h().d().t().Q();
      if (animations.m() && animations.q().a("Предметы").c()) {
         boolean focused = slot == ((HandledScreenAccessor)this).getFocusedSlot() && slot.method_7681();
         float scale = ((ISlot)slot).getAnimation().a(focused ? 1.25F : 1.0F, focused ? 1.25F : 0.75F);
         context.method_51448().method_22903();
         context.method_51448().method_46416(slot.field_7873 + 8.0F, slot.field_7872 + 8.0F, 0.0F);
         context.method_51448().method_22905(scale, scale, 1.0F);
         context.method_51448().method_46416(-(slot.field_7873 + 8.0F), -(slot.field_7872 + 8.0F), 0.0F);
      }
   }

   @Inject(
      method = {"method_2385"},
      at = {@At("RETURN")}
   )
   private void onDrawSlotTail(class_332 context, class_1735 slot, CallbackInfo ci) {
      Animations animations = Westra.h().d().t().Q();
      if (animations.m() && animations.q().a("Предметы").c()) {
         context.method_51448().method_22909();
      }
   }

   @ModifyArg(
      method = {"method_2388"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_332;method_51439(Lnet/minecraft/class_327;Lnet/minecraft/class_2561;IIIZ)I",
         ordinal = 0
      ),
      index = 1
   )
   private class_2561 modifyTitle(class_2561 title) {
      ContainerEvent event = new ContainerEvent((class_465<?>)this, title);
      EventManager.a((IEvent)event);
      return event.i();
   }

   @Inject(
      method = {"method_25426"},
      at = {@At("TAIL")}
   )
   private void onInit(CallbackInfo ci) {
      if (this.field_2797 instanceof class_1707 || this.field_2797 instanceof class_1733) {
         Westra.h().d().v().l();
         AutoBuy autoBuy = Westra.h().d().t().ba();
         HandledScreenAccessor screen = (HandledScreenAccessor)this;
         ScreenAccessor screenBase = (ScreenAccessor)this;
         class_1707 class_1707Var = this.field_2797 instanceof class_1707 ? (class_1707)this.field_2797 : null;
         int iMethod_17388;
         if (class_1707Var instanceof class_1707) {
            iMethod_17388 = class_1707Var.method_17388() * 9;
         } else {
            iMethod_17388 = this.field_2797 instanceof class_1733 ? 27 : this.chestSize;
         }

         this.chestSize = iMethod_17388;
         this.buttonFold = screenBase.invokeAddDrawableChild(
            class_4185.method_46430(class_2561.method_43470("Сложить"), b -> this.onFoldClick())
               .method_46434(
                  (screenBase.getWidth() + screen.getBackgroundWidth()) / 2 + 5, (screenBase.getHeight() - screen.getBackgroundHeight()) / 2, 100, 20
               )
               .method_46431()
         );
         this.buttonTake = screenBase.invokeAddDrawableChild(
            class_4185.method_46430(class_2561.method_43470("Забрать"), b2 -> this.onTakeClick())
               .method_46434(
                  (screenBase.getWidth() + screen.getBackgroundWidth()) / 2 + 5, (screenBase.getHeight() - screen.getBackgroundHeight()) / 2 + 24, 100, 20
               )
               .method_46431()
         );
         this.buttonDrop = screenBase.invokeAddDrawableChild(
            class_4185.method_46430(class_2561.method_43470("Выкинуть"), b3 -> this.onDropClick())
               .method_46434(
                  (screenBase.getWidth() + screen.getBackgroundWidth()) / 2 + 5, (screenBase.getHeight() - screen.getBackgroundHeight()) / 2 + 48, 100, 20
               )
               .method_46431()
         );
         if (screenBase.getTitle().getString().toLowerCase().contains("аукцион")
            || screenBase.getTitle().getString().toLowerCase().contains("категория: настоящие вещи")) {
            int[] discountSteps = new int[]{0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100};
            int[] discountIndex = new int[]{3};
            screenBase.invokeAddDrawableChild((T)class_4185.method_46430(this.statusMessage("AutoBuy", autoBuy.q()), widget -> {
               autoBuy.d(!autoBuy.q());
               if (autoBuy.q() && !autoBuy.m()) {
                  autoBuy.a(true);
               }

               widget.method_25355(this.statusMessage("AutoBuy", autoBuy.q()));
            }).method_46434(screenBase.getWidth() / 2 - 50, (screenBase.getHeight() + screen.getBackgroundHeight()) / 2 + 4, 100, 20).method_46431());
            screenBase.invokeAddDrawableChild((T)class_4185.method_46430(this.statusMessage("AutoParser", autoBuy.s()), widget -> {
               autoBuy.f(!autoBuy.s());
               if (autoBuy.s() && !autoBuy.m()) {
                  autoBuy.a(true);
               }

               widget.method_25355(this.statusMessage("AutoParser", autoBuy.s()));
            }).method_46434(screenBase.getWidth() / 2 - 50, (screenBase.getHeight() + screen.getBackgroundHeight()) / 2 + 28, 100, 20).method_46431());
            class_4185 discountBtn = class_4185.method_46430(class_2561.method_43470("Скидка: " + BuyConfig.getDiscountPercentage() + "%"), widget -> {
               discountIndex[0] = (discountIndex[0] + 1) % discountSteps.length;
               BuyConfig.setDiscountPercentage(discountSteps[discountIndex[0]]);
               widget.method_25355(class_2561.method_43470("Скидка: " + discountSteps[discountIndex[0]] + "%"));
            }).method_46434(screenBase.getWidth() / 2 - 50, (screenBase.getHeight() + screen.getBackgroundHeight()) / 2 + 52, 100, 20).method_46431();
            screenBase.invokeAddDrawableChild((T)discountBtn);
         }
      }
   }

   @Unique
   private class_2561 statusMessage(String name, boolean status) {
      return class_2561.method_43470(name + ": ")
         .method_10852(class_2561.method_43470(status ? "ON" : "OFF").method_27692(status ? class_124.field_1060 : class_124.field_1061));
   }

   @Unique
   private boolean hasStack(class_1735 slot) {
      return slot.method_7681() && !slot.method_7677().method_7960();
   }

   @Unique
   private void onFoldClick() {
      for (int i = this.chestSize; i < this.field_2797.field_7761.size(); i++) {
         class_1735 slot = (class_1735)this.field_2797.field_7761.get(i);
         if (this.hasStack(slot)) {
            Interface.aM_.field_1761.method_2906(this.field_2797.field_7763, slot.field_7874, 0, class_1713.field_7794, Interface.aM_.field_1724);
         }
      }
   }

   @Unique
   private void onTakeClick() {
      for (int i = 0; i < this.chestSize; i++) {
         class_1735 slot = (class_1735)this.field_2797.field_7761.get(i);
         if (this.hasStack(slot)) {
            Interface.aM_.field_1761.method_2906(this.field_2797.field_7763, slot.field_7874, 0, class_1713.field_7794, Interface.aM_.field_1724);
         }
      }
   }

   @Unique
   private void onDropClick() {
      for (int i = 0; i < this.chestSize; i++) {
         class_1735 slot = (class_1735)this.field_2797.field_7761.get(i);
         if (this.hasStack(slot)) {
            Interface.aM_.field_1761.method_2906(this.field_2797.field_7763, slot.field_7874, 1, class_1713.field_7795, Interface.aM_.field_1724);
         }
      }
   }

   @Inject(
      method = {"method_25404"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> info) {
      KeyEvent event = new KeyEvent(keyCode, scanCode, 0, modifiers);
      EventManager.a((IEvent)event);
      if (event.a()) {
         info.setReturnValue(true);
      } else {
         class_1735 focusedSlot;
         if (Interface.aM_.field_1690.field_1869 != null
            && Interface.aM_.field_1690.field_1869.method_1417(keyCode, scanCode)
            && (modifiers & 2) != 0
            && (modifiers & 1) != 0
            && (focusedSlot = ((HandledScreenAccessor)this).getFocusedSlot()) != null
            && focusedSlot.method_7681()) {
            this.handleDropItems(focusedSlot.method_7677().method_7909());
            info.setReturnValue(true);
         }
      }
   }

   @Unique
   private void handleDropItems(class_1792 targetItem) {
      for (class_1735 slot : this.field_2797.field_7761) {
         class_1799 stack = slot.method_7677();
         if (!stack.method_7960() && stack.method_7909() == targetItem) {
            Interface.aM_.field_1761.method_2906(this.field_2797.field_7763, slot.field_7874, 1, class_1713.field_7795, Interface.aM_.field_1724);
         }
      }
   }

   @Inject(
      method = {"method_2383(Lnet/minecraft/class_1735;IILnet/minecraft/class_1713;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onMouseClick(class_1735 slot, int slotId, int button, class_1713 actionType, CallbackInfo ci) {
      SwapScreen swapScreen = Westra.h().d().t().L().q();
      if (swapScreen.b() && slot != null && !slot.method_7677().method_7960()) {
         swapScreen.a(swapScreen.a(), slot.method_7677());
         swapScreen.a(-1);
         swapScreen.a(false);
         Interface.aM_.method_1507(swapScreen);
         ci.cancel();
      }
   }

   @Inject(
      method = {"method_25432"},
      at = {@At("HEAD")}
   )
   private void onRemoved(CallbackInfo ci) {
      SwapScreen swapMenu = Westra.h().d().t().L().q();
      if (swapMenu.b()) {
         swapMenu.a(false);
      }
   }

   @Inject(
      method = {"method_25394"},
      at = {@At("HEAD")}
   )
   private void onRender(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (this.buttonFold != null) {
         this.buttonFold.field_22763 = this.field_2797.field_7761.subList(this.chestSize, this.field_2797.field_7761.size()).stream().anyMatch(this::hasStack);
      }

      if (this.buttonTake != null && this.buttonDrop != null) {
         this.buttonTake.field_22763 = this.field_2797.field_7761.subList(0, this.chestSize).stream().anyMatch(this::hasStack);
         this.buttonDrop.field_22763 = this.field_2797.field_7761.subList(0, this.chestSize).stream().anyMatch(this::hasStack);
      }

      EventManager.a((IEvent)(new ContainerEvent((class_465<?>)this, context, mouseX, mouseY, ContainerEvent.Phase.PRE)));
      ItemScroller itemScroller = Westra.h().d().t().w();
      if (itemScroller.m()
         && ((HandledScreenAccessor)this).getFocusedSlot() != null
         && ((HandledScreenAccessor)this).getFocusedSlot().method_7681()
         && GLFW.glfwGetMouseButton(Interface.aM_.method_22683().method_4490(), 0) == 1
         && GLFW.glfwGetKey(Interface.aM_.method_22683().method_4490(), 340) == 1
         && itemScroller.r().a(itemScroller.q().c().intValue())) {
         Interface.aM_
            .field_1761
            .method_2906(
               this.field_2797.field_7763, ((HandledScreenAccessor)this).getFocusedSlot().field_7874, 0, class_1713.field_7794, Interface.aM_.field_1724
            );
         itemScroller.r().b();
      }
   }

   @Inject(
      method = {"method_25394"},
      at = {@At("TAIL")}
   )
   private void onRenderTail(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      EventManager.a((IEvent)(new ContainerEvent((class_465<?>)this, context, mouseX, mouseY, ContainerEvent.Phase.POST)));
   }
}

package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.render.Animations;
import net.minecraft.class_1661;
import net.minecraft.class_1713;
import net.minecraft.class_1723;
import net.minecraft.class_1735;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_4185;
import net.minecraft.class_465;
import net.minecraft.class_490;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_490.class})
public abstract class InventoryScreenMixin extends class_465<class_1723> {
   @Unique
   private class_4185 button;

   public InventoryScreenMixin(class_1723 handler, class_1661 inventory, class_2561 title) {
      super(handler, inventory, title);
   }

   public void method_25426() {
      super.method_25426();
      this.button = (class_4185)this.method_37063(
         class_4185.method_46430(class_2561.method_43470("Выкинуть всё"), this::onDropAllClick)
            .method_46434((this.field_22789 - 100) / 2, (this.field_22790 - this.field_2779) / 2 - 24, 100, 20)
            .method_46431()
      );
   }

   public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {
      context.method_51448().method_22903();
      context.method_51448().method_34426();
      context.method_25296(0, 0, this.field_22789, this.field_22790, -1072689136, -804253680);
      context.method_51448().method_22909();
      this.method_2389(context, delta, mouseX, mouseY);
   }

   @Inject(
      method = {"method_25394"},
      at = {@At("HEAD")}
   )
   private void headRender(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      Animations animations = Westra.h().d().t().Q();
      if (animations.m() && animations.q().a("Открытие инвентаря").c()) {
         float value = animations.t().c();
         context.method_51448().method_22903();
         context.method_51448().method_46416(this.field_22789 / 2.0F, this.field_22790 / 2.0F, 0.0F);
         context.method_51448().method_22905(value, value, 1.0F);
         context.method_51448().method_46416(-this.field_22789 / 2.0F, -this.field_22790 / 2.0F, 0.0F);
      }

      if (this.button != null) {
         this.button.field_22763 = ((class_1723)this.field_2797).field_7761.stream().anyMatch(this::hasStack);
      }
   }

   @Inject(
      method = {"method_25394"},
      at = {@At("RETURN")}
   )
   private void render(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      Animations animations = Westra.h().d().t().Q();
      if (animations.m() && animations.q().a("Открытие инвентаря").c()) {
         context.method_51448().method_22909();
      }
   }

   @Unique
   private boolean hasStack(class_1735 slot) {
      return slot.method_7681() && !slot.method_7677().method_7960();
   }

   @Unique
   private void onDropAllClick(class_4185 button) {
      for (class_1735 slot : ((class_1723)this.field_2797).field_7761) {
         if (this.hasStack(slot) && this.button.field_22763) {
            Interface.aM_.field_1761.method_2906(((class_1723)this.field_2797).field_7763, slot.field_7874, 1, class_1713.field_7795, Interface.aM_.field_1724);
         }
      }
   }
}

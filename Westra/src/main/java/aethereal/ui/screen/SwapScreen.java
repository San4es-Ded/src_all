package aethereal.ui.screen;

import aethereal.core.Interface;
import aethereal.core.Westra;
import lombok.Generated;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_490;
import net.minecraft.class_5611;

public class SwapScreen extends class_437 implements Interface {
   private final RadialScreen b = new RadialScreen(3, 75.0F, 101.25F);
   private boolean c;

   @Generated
   public boolean b() {
      return this.c;
   }

   @Generated
   public void a(boolean open) {
      this.c = open;
   }

   public SwapScreen(class_2561 title) {
      super(title);

      for (int i = 0; i < 3; i++) {
         int slot = i;
         this.b.a(slot, class_1799.field_8037, () -> this.c(slot), true);
      }
   }

   private void c(int slot) {
      class_1799 stack = this.b.c(slot);
      if (stack.method_7960()) {
         aM_.method_1507(new class_490(aM_.field_1724));
         this.a(true);
      } else {
         Westra.h().d().v().a().a(stack, 45, 1);
         aM_.field_1724.method_3137();
      }
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      return this.b.a(mouseX, mouseY, button, new class_5611(this.field_22789 / 2.0F, this.field_22790 / 2.0F));
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.b.a(context, mouseX, mouseY, new class_5611(this.field_22789 / 2.0F, this.field_22790 / 2.0F));
   }

   public int a() {
      return this.b.b();
   }

   public void a(int slot) {
      this.b.e(slot);
   }

   public void a(int segment, class_1799 stack) {
      this.b.a(segment, stack);
   }

   public class_1799 b(int segment) {
      return this.b.c(segment);
   }
}

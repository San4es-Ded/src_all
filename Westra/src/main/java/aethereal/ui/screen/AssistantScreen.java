package aethereal.ui.screen;

import aethereal.autobuy.AutoBuyEntry;
import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Action;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Draw3DProcessor;
import aethereal.render.Fonts;
import aethereal.util.MathUtil;
import java.util.Arrays;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_5611;

public class AssistantScreen extends class_437 implements Interface {
   private final RadialScreen b = new RadialScreen(2, 75.0F, 101.25F);
   private final List<AutoBuyEntry> c = Arrays.stream(AutoBuyEntry.values()).filter(info -> info.d() == class_1802.field_8436).toList();
   private class_5611 d = null;
   private class_5611 e = null;
   private int f = -1;
   private int g = -1;

   @Generated
   public RadialScreen a() {
      return this.b;
   }

   @Generated
   public int b() {
      return this.g;
   }

   public AssistantScreen(class_2561 title) {
      super(title);
      this.a(this.b.a());
   }

   public void a(int count) {
      this.b.b(Math.max(count, 1));

      for (int i = 0; i < this.b.a(); i++) {
         this.b.a(i, class_1799.field_8037, this.c(i), true);
      }
   }

   public void a(int slot, AutoBuyEntry potion) {
      this.b.a(slot, potion.a(), potion.b(), this.c(slot), true);
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      if (button == 0 && this.f != -1 && this.d != null && this.e != null) {
         for (int index = 0; index < this.c.size(); index++) {
            float rowY = this.d.method_32119() + index * 18.0F;
            if (MathUtil.a(mouseX, mouseY, this.d.method_32118(), rowY, this.e.method_32118(), 18.0F)) {
               AutoBuyEntry potion = this.c.get(index);

               for (int segment = 0; segment < this.b.a(); segment++) {
                  if (potion.a(this.b.c(segment))) {
                     this.c();
                     return true;
                  }
               }

               int slot2 = this.f;
               this.b.a(slot2, potion.a(), potion.b(), this.c(slot2), true);
               this.c();
               return true;
            }
         }

         this.c();
         return true;
      } else if (button == 2) {
         int newSlot = this.b.a();
         this.b.b(newSlot + 1);
         this.b.a(newSlot, class_1799.field_8037, this.c(newSlot), true);
         return true;
      } else {
         class_5611 center = new class_5611(this.field_22789 / 2.0F, this.field_22790 / 2.0F);
         int slot;
         if (button == 0 && (slot = this.b.a(mouseX, mouseY, center)) >= 0 && !this.b.c(slot).method_7960()) {
            this.g = slot;
            return true;
         } else {
            int before = this.b.a();
            boolean handled = this.b.a(mouseX, mouseY, button, center);
            if (this.b.a() != before) {
               this.c();

               for (int i = 0; i < this.b.a(); i++) {
                  this.b.a(i, this.c(i));
               }
            }

            return handled;
         }
      }
   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      if (button == 0 && this.g != -1) {
         this.b(this.b.a(mouseX, mouseY, new class_5611(this.field_22789 / 2.0F, this.field_22790 / 2.0F)));
         return true;
      } else {
         return super.method_25406(mouseX, mouseY, button);
      }
   }

   public void b(int target) {
      if (this.g != -1) {
         if (target >= 0 && target != this.g) {
            this.b.a(this.g, target);
         } else {
            this.b.d(this.g).execute();
         }

         this.g = -1;
      }
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.b.a(context, mouseX, mouseY, new class_5611(this.field_22789 / 2.0F, this.field_22790 / 2.0F));
      if (this.g != -1 && this.b.a(mouseX, mouseY, new class_5611(this.field_22789 / 2.0F, this.field_22790 / 2.0F)) != this.g) {
         Westra.h().d().j().a(context, this.b.c(this.g), mouseX - 8, mouseY - 8, 300, 1.0F, 1.0F, false);
      }

      if (this.f != -1 && this.d != null) {
         this.a(context, mouseX, mouseY);
      }
   }

   private void a(class_332 context, int mouseX, int mouseY) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      Draw3DProcessor draw3d = Westra.h().d().j();
      ThemeProcessor theme = Westra.h().d().o();
      draw.a(
         matrices,
         this.d.method_32118(),
         this.d.method_32119(),
         this.e.method_32118(),
         this.e.method_32119(),
         8.0F,
         theme.a(ThemeInfo.BACKGROUND_GUI).a(),
         1.0F,
         theme.a(ThemeInfo.BACKGROUND_GUI).a(),
         11.0F
      );
      draw.a(matrices, this.d.method_32118(), this.d.method_32119(), this.e.method_32118(), this.e.method_32119(), 8.0F, 1.0F, ColorUtil.a(255, 255, 255, 15));

      for (int index = 0; index < this.c.size(); index++) {
         AutoBuyEntry potion = this.c.get(index);
         float rowY = this.d.method_32119() + index * 18.0F;
         boolean hovered = MathUtil.a(mouseX, mouseY, this.d.method_32118(), rowY, this.e.method_32118(), 18.0F);
         if (hovered) {
            draw.a(matrices, this.d.method_32118() + 2.0F, rowY + 1.0F, this.e.method_32118() - 4.0F, 16.0F, 6.0F, ColorUtil.a(255, 255, 255, 12));
         }

         draw3d.a(context, potion.a(), this.d.method_32118() + 5.0F, rowY + 9.0F - 6.0F, 0, 1.0F, 0.75F, false);
         Fonts.e
            .a(
               matrices,
               potion.b(),
               this.d.method_32118() + 5.0F + 12.0F + 4.0F,
               rowY + 9.0F - Fonts.e.a(6.5F) / 2.0F,
               6.5F,
               hovered ? ColorUtil.a(255, 255, 255, 220) : ColorUtil.a(255, 255, 255, 150)
            );
      }
   }

   private Action c(int slot) {
      return () -> {
         if (this.b.c(slot).method_7960()) {
            this.f = slot;
            this.d = this.d(slot);
            this.e = this.d();
         } else {
            Westra.h().d().v().b().a(this.b.c(slot));
            aM_.field_1724.method_3137();
         }
      };
   }

   private void c() {
      this.f = -1;
      this.d = null;
      this.e = null;
   }

   private class_5611 d() {
      float maxLabelWidth = 0.0F;

      for (AutoBuyEntry potion : this.c) {
         maxLabelWidth = Math.max(maxLabelWidth, Fonts.e.a(potion.b(), 6.5F));
      }

      return new class_5611(26.0F + maxLabelWidth + 10.0F, this.c.size() * 18.0F);
   }

   private class_5611 d(int slot) {
      double angleStep = 6.28318530719644 / this.b.a();
      double startAngle = -1.5707963569506784 + angleStep * slot;
      return new class_5611(
         this.field_22789 / 2.0F + (float)Math.cos(startAngle + angleStep / 2.0) * 88.125F + 12.0F,
         this.field_22790 / 2.0F + (float)Math.sin(startAngle + angleStep / 2.0) * 88.125F + 12.0F
      );
   }
}

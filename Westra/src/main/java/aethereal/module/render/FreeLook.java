package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.LookEvent;
import aethereal.event.RotationEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import net.minecraft.class_3532;
import net.minecraft.class_5498;

@ModuleRegister(
   a = "Free Look",
   b = "Позволяет осмотреться вокруг, не поворачивая персонажа",
   c = Category.Render
)
public class FreeLook extends Module implements Interface {
   private final ModeSetting b = new ModeSetting("Режим", "Удержание", "Удержание", "Переключение");
   private final BindSetting c = new BindSetting("Клавиша обзора", 342, 0).a(() -> this.q()).b(() -> this.r());
   private final BooleanSetting d = new BooleanSetting("Вернуть вид после отпускания", true);
   private float e;
   private float f;
   private class_5498 g = class_5498.field_26664;
   private boolean h;

   public FreeLook() {
      this.a(new Setting[]{this.b, this.c, this.d});
   }

   private void q() {
      if (this.b.l("Переключение")) {
         if (this.h) {
            this.s();
         } else {
            this.t();
         }
      } else {
         this.t();
      }
   }

   private void r() {
      if (!this.b.l("Переключение")) {
         this.s();
      }
   }

   private void t() {
      if (!this.h && aM_.field_1724 != null && aM_.field_1690 != null) {
         this.g = aM_.field_1690.method_31044();
         this.e = aM_.field_1724.method_36454();
         this.f = aM_.field_1724.method_36455();
         aM_.field_1690.method_31043(class_5498.field_26665);
         this.h = true;
      }
   }

   private void s() {
      if (this.h) {
         if (aM_.field_1690 != null) {
            aM_.field_1690.method_31043(this.d.c() ? this.g : aM_.field_1690.method_31044());
         }

         this.h = false;
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.h && (aM_.field_1724 == null || aM_.field_1687 == null || aM_.field_1755 != null)) {
         this.s();
      }
   }

   @EventTarget
   public void a(LookEvent event) {
      if (this.h) {
         event.a(true);
         this.e = class_3532.method_15393(this.e + (float)(event.a * 0.15));
         this.f = class_3532.method_15363(this.f + (float)(event.b * 0.15), -90.0F, 90.0F);
      }
   }

   @EventTarget(
      a = 4
   )
   public void a(RotationEvent event) {
      if (this.h) {
         event.a = this.e;
         event.b = this.f;
      }
   }

   @Override
   public void c() {
      this.s();
      super.c();
   }
}

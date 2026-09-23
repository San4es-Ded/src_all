package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.CounterUtil;
import aethereal.util.InventoryUtil;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_9334;

@ModuleRegister(
   a = "Auto Totem",
   b = "Берёт тотем бессмертия в руку при падении здоровья ниже заданного значения",
   c = Category.Combat
)
public class AutoTotem extends Module implements Interface {
   private final SliderSetting c = new SliderSetting("Порог здоровья", 6.0F, 1.0F, 20.0F, 0.5F);
   private final MultiModeSetting d = new MultiModeSetting(
      "Дополнительные опции",
      new BooleanSetting("Возвращать предмет", true),
      new BooleanSetting("Сначала обычные тотемы", true),
      new BooleanSetting("Не во время еды", false)
   );
   private final BooleanSetting e = new BooleanSetting("Умное перемещение", true);
   private final MultiModeSetting i = new MultiModeSetting(
      "Надевать заранее при угрозе",
      new BooleanSetting("Кристаллы рядом", true),
      new BooleanSetting("Долгое падение", true),
      new BooleanSetting("Полёт на элитре", true),
      new BooleanSetting("Булава рядом", true)
   );
   private final SliderSetting j = new SliderSetting("Радиус кристалла", 4.0F, 1.0F, 6.0F, 0.5F).a(() -> this.a("Кристаллы рядом"));
   private final SliderSetting k = new SliderSetting("Высота кристалла", 2.5F, 0.5F, 8.0F, 0.5F).a(() -> this.a("Кристаллы рядом"));
   private final SliderSetting l = new SliderSetting("Высота падения", 15.0F, 5.0F, 50.0F, 1.0F).a(() -> this.a("Долгое падение"));
   private final SliderSetting n = new SliderSetting("Здоровье на элитре", 8.5F, 1.0F, 20.0F, 0.5F).a(() -> this.a("Полёт на элитре"));
   private final SliderSetting o = new SliderSetting("Радиус булавы", 6.0F, 2.0F, 12.0F, 0.5F).a(() -> this.a("Булава рядом"));
   private final BooleanSetting p = new BooleanSetting("Булава только в воздухе", true).a(() -> this.a("Булава рядом"));
   private float u;
   private boolean v;
   private final CounterUtil f = new CounterUtil();
   private final float[] g = new float[20];
   private int h = -1;
   public boolean b;

   public AutoTotem() {
      this.a(new Setting[]{this.c, this.d, this.e, this.i, this.j, this.k, this.l, this.n, this.o, this.p});
   }

   @Override
   public void b() {
      super.b();
      this.h = -1;
   }

   @Override
   public void c() {
      super.c();
      this.h = -1;
      this.b = false;
   }

   @EventTarget
   public void a(TickEvent event) {
      this.v();
      System.arraycopy(this.g, 0, this.g, 1, 19);
      this.g[0] = aM_.field_1724.method_6032() + (aM_.field_1724.method_6059(class_1294.field_5898) ? aM_.field_1724.method_6067() : 0.0F);
      if (!this.d.a("Не во время еды").c() || !this.t()) {
         Boolean should = this.s();
         this.b = should != null ? should : this.b;
         if (should != null) {
            if (should) {
               if (!this.f.a(400L) || !this.q()) {
                  return;
               }
            } else if (!this.r()) {
               return;
            }

            this.f.b();
         }
      }
   }

   private boolean q() {
      class_1799 offhand = aM_.field_1724.method_6079();
      boolean preferPlain = this.d.a("Сначала обычные тотемы").c();
      if (!this.a(offhand) || preferPlain && offhand.method_7958()) {
         int slot = preferPlain ? InventoryUtil.a(class_1802.field_8288, false, true) : -1;
         if (slot == -1 && !this.a(offhand)) {
            slot = InventoryUtil.b(class_1802.field_8288);
         }

         if (slot == -1) {
            return false;
         } else {
            if (this.d.a("Возвращать предмет").c() && this.h == -1 && !offhand.method_7960()) {
               this.h = slot;
            }

            Westra.h().d().v().a().a(slot, 40, 1);
            return true;
         }
      } else {
         return false;
      }
   }

   private boolean r() {
      class_1799 offhand = aM_.field_1724.method_6079();
      if (this.d.a("Возвращать предмет").c() && this.h != -1 && (offhand.method_7960() || this.a(offhand))) {
         Westra.h().d().v().a().a(this.h, 40, 1);
      }

      this.h = -1;
      return false;
   }

   private Boolean s() {
      if (aM_.field_1724.method_7357().method_7904(class_1802.field_8288.method_7854())) {
         return false;
      } else {
         class_1799 active = aM_.field_1724.method_6030();
         if (this.e.c() && this.t() && active.method_7909().method_7881(active, aM_.field_1724) > 5) {
            int left = aM_.field_1724.method_6014();
            if (left < 10) {
               return null;
            }

            if (left < 30 && (this.g[19] - this.g[0]) / 20.0F * left < this.g[0]) {
               return null;
            }
         }

         if (this.g[0] <= this.c.c()) {
            return true;
         } else if (this.u()) {
            return true;
         } else {
            return this.g[0] >= this.c.c() + 0.5F ? false : null;
         }
      }
   }

   private boolean a(String option) {
      BooleanSetting setting = this.i.a(option);
      return setting != null && setting.c();
   }

   private void v() {
      boolean falling = aM_.field_1724.method_18798().field_1351 < -0.1
         && !aM_.field_1724.method_24828()
         && !aM_.field_1724.method_6101()
         && !aM_.field_1724.method_5799();
      if (falling && !this.v) {
         this.u = (float)aM_.field_1724.method_23318();
      }

      if (aM_.field_1724.method_24828() || aM_.field_1724.method_5799() || aM_.field_1724.method_6101()) {
         this.u = (float)aM_.field_1724.method_23318();
      }

      this.v = falling;
   }

   private boolean u() {
      if (this.a("Полёт на элитре") && aM_.field_1724.method_6128() && this.g[0] <= this.n.c()) {
         return true;
      } else if (this.a("Долгое падение") && this.u - (float)aM_.field_1724.method_23318() >= this.l.c() && aM_.field_1724.method_18798().field_1351 < -0.1) {
         return true;
      } else {
         return this.a("Булава рядом") && this.w() ? true : this.a("Кристаллы рядом") && this.x();
      }
   }

   private boolean w() {
      double radius = this.o.c().floatValue();

      for (class_1657 other : aM_.field_1687.method_18456()) {
         if (other != aM_.field_1724
            && other.method_5805()
            && !other.method_7325()
            && !Westra.h().d().e().d(other.method_5477().getString())
            && (other.method_6047().method_7909() == class_1802.field_49814 || other.method_6079().method_7909() == class_1802.field_49814)
            && !(other.method_23318() - aM_.field_1724.method_23318() < -2.0)
            && !(Math.hypot(other.method_23317() - aM_.field_1724.method_23317(), other.method_23321() - aM_.field_1724.method_23321()) > radius)
            && (!this.p.c() || !other.method_24828())) {
            return true;
         }
      }

      return false;
   }

   private boolean x() {
      double radius = this.j.c().floatValue();
      double height = this.k.c().floatValue();

      for (class_1297 entity : aM_.field_1687.method_18112()) {
         if (entity instanceof class_1511
            && !(Math.abs(entity.method_23318() - aM_.field_1724.method_23318()) > height)
            && Math.hypot(entity.method_23317() - aM_.field_1724.method_23317(), entity.method_23321() - aM_.field_1724.method_23321()) <= radius) {
            return true;
         }
      }

      return false;
   }

   private boolean t() {
      class_1799 active = aM_.field_1724.method_6030();
      return aM_.field_1724.method_6115() && !active.method_7960() && active.method_57824(class_9334.field_50075) != null;
   }

   private boolean a(class_1799 stack) {
      return !stack.method_7960() && stack.method_7909() == class_1802.field_8288;
   }
}

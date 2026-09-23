package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.KeyEvent;
import aethereal.event.MotionEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.NetworkUtil;
import aethereal.util.Rotation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.class_1268;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2827;
import net.minecraft.class_2828;
import net.minecraft.class_2868;
import net.minecraft.class_2886;
import net.minecraft.class_6374;

@ModuleRegister(
   a = "Wind Boost",
   b = "Бросает два заряда ветра подряд: первый придерживается в буфере и уходит одновременно со вторым",
   c = Category.Movement
)
public class WindBoost extends Module implements Interface {
   private final ModeSetting b = new ModeSetting("Режим", "При включении", "При включении", "Прыжок", "По бинду", "Авто");
   private final BindSetting c = new BindSetting("Клавиша запуска", -1).a(() -> this.b.l("По бинду"));
   private final SliderSetting d2 = new SliderSetting("Задержка пакетов", 1000.0F, 500.0F, 2000.0F, 50.0F);
   private final SliderSetting e = new SliderSetting("Задержка второго заряда", 2.0F, 1.0F, 6.0F, 1.0F);
   private final BooleanSetting f2 = new BooleanSetting("Двойной бросок", true);
   private final BooleanSetting g2 = new BooleanSetting("Брать булаву в воздухе", true);
   private final BooleanSetting h2 = new BooleanSetting("Искать в инвентаре", true);
   private final BooleanSetting i2 = new BooleanSetting("Взмах рукой", true);
   private final BooleanSetting j2 = new BooleanSetting("Тихий поворот", true);
   private final ConcurrentLinkedQueue<WindBoost.a> k2 = new ConcurrentLinkedQueue<>();
   private final List<class_2596<?>> l2 = new ArrayList<>();
   private long m2;
   private int n2;
   private int o2;
   private int p2 = -1;
   private int q2 = -1;
   private boolean r2;
   private boolean s2;
   private long t2;

   public WindBoost() {
      this.a(new Setting[]{this.b, this.c, this.d2, this.e, this.f2, this.g2, this.h2, this.i2, this.j2});
   }

   @Override
   public void b() {
      super.b();
      if (aM_.field_1724 != null && aM_.field_1687 != null && this.b.l("При включении")) {
         this.F();
      }
   }

   @Override
   public void c() {
      this.G();
      this.H();
      super.c();
   }

   @EventTarget
   public void a(KeyEvent event) {
      if (event.d() != 0 && aM_.field_1724 != null && aM_.field_1687 != null) {
         if (this.b.l("По бинду") && this.c.c() != -1 && event.b() == this.c.c()) {
            this.F();
         }
      }
   }

   @EventTarget
   public void a(MotionEvent event) {
      if (this.n2 > 0 && this.j2.c() && aM_.field_1724 != null) {
         Westra.h().d().k().a(new Rotation(aM_.field_1724.method_36454(), 90.0F), 360.0F, 2, 9);
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      class_2596<?> packet = event.d();
      if (!this.s2 || !(packet instanceof class_2886) && !(packet instanceof class_2868)) {
         if (this.r2 && event.b() && (packet instanceof class_2827 || packet instanceof class_6374 || packet instanceof class_2828)) {
            this.k2.add(new WindBoost.a(packet, System.currentTimeMillis() + (long)this.d2.c().floatValue()));
            event.a(true);
         }
      } else {
         this.l2.add(packet);
         event.a(true);
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      long now = System.currentTimeMillis();

      while (!this.k2.isEmpty()) {
         WindBoost.a delayed = this.k2.peek();
         if (delayed == null || now < delayed.b()) {
            break;
         }

         this.k2.poll();
         NetworkUtil.a(delayed.a());
      }

      if (aM_.field_1724 == null || aM_.field_1687 == null || aM_.field_1761 == null) {
         this.G();
         this.H();
      } else if (this.n2 == 0) {
         if (this.r2 && (float)(now - this.t2) > this.d2.c() + 500.0F) {
            this.r2 = false;
            this.G();
         }

         if (this.b.l("Прыжок") && aM_.field_1690.field_1903.method_1434() && aM_.field_1724.method_24828() && now - this.m2 >= 500L) {
            this.F();
         } else if (this.b.l("Авто")) {
            boolean ground = aM_.field_1724.method_24828() && now - this.m2 >= 500L;
            boolean rising = !aM_.field_1724.method_24828() && aM_.field_1724.method_18798().field_1351 > 0.05 && now - this.m2 >= 600L;
            if (ground || rising) {
               this.F();
            }
         }
      } else {
         this.o2++;
         switch (this.n2) {
            case 1:
               if (aM_.field_1724.method_24828()) {
                  aM_.field_1724.method_6043();
               }

               this.I();
               this.n2 = 2;
               this.o2 = 0;
               break;
            case 2:
               if (this.o2 >= 1) {
                  this.s2 = true;
                  this.J();
                  this.n2 = 3;
                  this.o2 = 0;
               }
               break;
            case 3:
               if (this.o2 >= (int)this.e.c().floatValue()) {
                  this.s2 = false;

                  for (class_2596<?> packet : this.l2) {
                     NetworkUtil.a(packet);
                  }

                  this.l2.clear();
                  if (this.f2.c()) {
                     this.I();
                     this.J();
                  }

                  this.n2 = 4;
                  this.o2 = 0;
               }
               break;
            case 4:
               if (this.o2 >= 2) {
                  int mace = this.g2.c() ? this.K() : -1;
                  int slot = mace != -1 ? mace : this.p2;
                  if (slot != -1) {
                     aM_.field_1724.method_31548().field_7545 = slot;
                     this.M();
                  }

                  this.n2 = 0;
                  this.o2 = 0;
                  if (this.b.l("При включении")) {
                     this.a();
                  }
               }
         }
      }
   }

   private void F() {
      int slot = this.L();
      if (slot != -1) {
         this.r2 = true;
         this.t2 = System.currentTimeMillis();
         this.p2 = aM_.field_1724.method_31548().field_7545;
         this.q2 = slot;
         this.n2 = 1;
         this.o2 = 0;
         this.s2 = false;
         this.l2.clear();
      }
   }

   private void G() {
      for (class_2596<?> packet : this.l2) {
         NetworkUtil.a(packet);
      }

      this.l2.clear();

      while (!this.k2.isEmpty()) {
         WindBoost.a delayed = this.k2.poll();
         if (delayed != null) {
            NetworkUtil.a(delayed.a());
         }
      }
   }

   private void H() {
      this.n2 = 0;
      this.o2 = 0;
      this.p2 = -1;
      this.q2 = -1;
      this.r2 = false;
      this.s2 = false;
      this.l2.clear();
   }

   private void I() {
      int current = this.L();
      if (current != -1) {
         this.q2 = current;
      }

      if (this.q2 >= 0 && this.q2 <= 8) {
         aM_.field_1724.method_31548().field_7545 = this.q2;
         this.M();
      } else if (this.q2 >= 9 && this.q2 <= 35) {
         int sync = aM_.field_1724.field_7512 != null ? aM_.field_1724.field_7512.field_7763 : 0;
         aM_.field_1761.method_2906(sync, this.q2, Math.max(0, this.p2), class_1713.field_7791, aM_.field_1724);
      }
   }

   private void J() {
      class_1268 hand = this.q2 == 40 ? class_1268.field_5810 : class_1268.field_5808;
      aM_.field_1761.method_2919(aM_.field_1724, hand);
      if (this.i2.c()) {
         aM_.field_1724.method_6104(hand);
      }

      this.m2 = System.currentTimeMillis();
   }

   private void M() {
      if (aM_.field_1724 != null && aM_.field_1724.field_3944 != null) {
         aM_.field_1724.field_3944.method_52787(new class_2868(aM_.field_1724.method_31548().field_7545));
      }
   }

   private int K() {
      for (int slot = 0; slot < 9; slot++) {
         class_1799 stack = aM_.field_1724.method_31548().method_5438(slot);
         if (!stack.method_7960() && stack.method_31574(class_1802.field_49814)) {
            return slot;
         }
      }

      return -1;
   }

   private int L() {
      if (aM_.field_1724 == null) {
         return -1;
      } else if (aM_.field_1724.method_6079().method_31574(class_1802.field_49098)) {
         return 40;
      } else {
         for (int slot = 0; slot < 9; slot++) {
            class_1799 stack = aM_.field_1724.method_31548().method_5438(slot);
            if (!stack.method_7960() && stack.method_31574(class_1802.field_49098)) {
               return slot;
            }
         }

         if (this.h2.c()) {
            for (int slotx = 9; slotx < 36; slotx++) {
               class_1799 stack = aM_.field_1724.method_31548().method_5438(slotx);
               if (!stack.method_7960() && stack.method_31574(class_1802.field_49098)) {
                  return slotx;
               }
            }
         }

         return -1;
      }
   }

   private record a(class_2596<?> a, long b) {
   }
}

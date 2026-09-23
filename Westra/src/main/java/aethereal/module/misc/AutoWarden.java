package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.handler.BaritoneBridge;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.setting.StringSetting;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import aethereal.util.ServerUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1542;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2586;
import net.minecraft.class_2595;
import net.minecraft.class_2818;
import net.minecraft.class_3719;
import net.minecraft.class_3965;
import net.minecraft.class_476;
import net.minecraft.class_8172;
import net.minecraft.class_9334;

@ModuleRegister(
   a = "Auto Warden",
   b = "Сам находит сундуки, идёт к ним и забирает лут",
   c = Category.Misc
)
public class AutoWarden extends Module implements Interface {
   private final ModeSetting b = new ModeSetting("Режим", "Варден", "Варден", "Медный данж");
   private final MultiModeSetting c = new MultiModeSetting(
      "Забирать лут",
      new BooleanSetting("Сферы", true),
      new BooleanSetting("Талисманы", true),
      new BooleanSetting("Ценные предметы", true),
      new BooleanSetting("Зелья", false),
      new BooleanSetting("Стрелы", false),
      new BooleanSetting("Оружие", false),
      new BooleanSetting("Броня", false),
      new BooleanSetting("Всё подряд", false)
   );
   private final SliderSetting d = new SliderSetting("Радиус поиска", 64.0F, 16.0F, 160.0F, 8.0F);
   private final SliderSetting e = new SliderSetting("Ждать сундук, сек", 20.0F, 5.0F, 240.0F, 5.0F);
   private final BooleanSetting f = new BooleanSetting("Отходить после лута", true);
   private final SliderSetting g = new SliderSetting("Дистанция отхода", 12.0F, 4.0F, 40.0F, 2.0F).a(this.f::h);
   private final BooleanSetting h = new BooleanSetting("Сообщать о находках", true);
   private final BooleanSetting o = new BooleanSetting("Ломать кувшины", true).a(() -> this.b.l("Медный данж"));
   private final StringSetting p = new StringSetting("Точка дома для фарма", "warden");
   private final BooleanSetting r = new BooleanSetting("Авто-невидимость", true);
   private final SliderSetting t = new SliderSetting("Держать невидимок", 3.0F, 1.0F, 16.0F, 1.0F).a(this.r::h);
   private final BooleanSetting u = new BooleanSetting("Докупать на аукционе", true).a(this.r::h);
   private final StringSetting v2 = new StringSetting("Что искать на аукционе", "Невидимость").a(this.u::h);
   private final ModeSetting x = new ModeSetting("Куда складывать лут", "Клан-хранилище", "Клан-хранилище", "Сундук у дома", "Не складывать");
   private final StringSetting z = new StringSetting("Точка дома для склада", "sklad").a(() -> this.x.l("Сундук у дома"));
   private final SliderSetting A = new SliderSetting("Складывать от стоимости, млн", 5.0F, 0.5F, 100.0F, 0.5F).a(() -> !this.x.l("Не складывать"));
   private final BooleanSetting B2 = new BooleanSetting("Складывать и при полном инвентаре", true).a(() -> !this.x.l("Не складывать"));
   private final Set<class_2338> i = new HashSet<>();
   private final List<Integer> n = new ArrayList<>();
   private final CounterUtil j = new CounterUtil();
   private AutoWarden.a k = AutoWarden.a.SEARCH;
   private class_2338 l;
   private class_243 m;
   private boolean q;

   public AutoWarden() {
      this.a(new Setting[]{this.b, this.o, this.c, this.d, this.e, this.f, this.g, this.r, this.t, this.u, this.v2, this.x, this.z, this.A, this.B2, this.h});
   }

   public List<Integer> q() {
      return this.n;
   }

   @Override
   public void b() {
      super.b();
      this.k = AutoWarden.a.SEARCH;
      this.l = null;
      this.j.b();
      if (!BaritoneBridge.a()) {
         ChatUtil.a("Поиск пути недоступен — нужен мод-пасфайндер, иначе дойти до сундука нечем.");
      }
   }

   @Override
   public void c() {
      BaritoneBridge.d();
      this.l = null;
      this.k = AutoWarden.a.SEARCH;
      super.c();
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1687 != null) {
         switch (this.k) {
            case SEARCH:
               this.y();
               break;
            case GOTO:
               this.r();
               break;
            case OPEN:
               this.s();
               break;
            case LOOT:
               this.t();
               break;
            case BREAK:
               this.B();
               break;
            case PICKUP:
               this.O();
               break;
            case RETREAT:
               this.u();
               break;
            case GO_HOME:
               this.C();
               break;
            case STORE:
               this.D();
               break;
            case BUY_INVIS:
               this.E();
         }
      }
   }

   private void y() {
      if (this.x.l("Не складывать") || this.P() < (long)(this.A.c() * 1000000.0F) && (!this.B2.c() || !(this.L() >= 95.0F))) {
         if (this.r.c() && this.u.c() && this.K() < (int)this.t.c().floatValue()) {
            this.q = false;
            this.j.b();
            this.k = AutoWarden.a.BUY_INVIS;
         } else if (this.r.c() && !this.M() && this.K() > 0) {
            this.N();
         } else {
            class_2338 best = null;
            double bestDistance = Double.MAX_VALUE;
            double limit = this.d.c().doubleValue();
            int radius = Math.max(2, ((int)limit >> 4) + 1);
            int centerX = aM_.field_1724.method_24515().method_10263() >> 4;
            int centerZ = aM_.field_1724.method_24515().method_10260() >> 4;

            for (int cx = centerX - radius; cx <= centerX + radius; cx++) {
               for (int cz = centerZ - radius; cz <= centerZ + radius; cz++) {
                  class_2818 chunk = aM_.field_1687.method_8497(cx, cz);
                  if (chunk != null) {
                     for (Entry<class_2338, class_2586> entry : chunk.method_12214().entrySet()) {
                        class_2338 pos = entry.getKey();
                        if (!this.i.contains(pos) && this.v(entry.getValue())) {
                           double distance = aM_.field_1724.method_19538().method_1022(pos.method_46558());
                           if (distance <= limit && distance < bestDistance) {
                              bestDistance = distance;
                              best = pos.method_10062();
                           }
                        }
                     }
                  }
               }
            }

            if (best != null) {
               this.l = best;
               this.m = aM_.field_1724.method_19538();
               this.j.b();
               this.k = AutoWarden.a.GOTO;
               BaritoneBridge.a(best);
            }
         }
      } else {
         this.q = false;
         this.j.b();
         this.k = AutoWarden.a.GO_HOME;
      }
   }

   private boolean v(class_2586 entity) {
      if (this.b.l("Медный данж")) {
         return entity instanceof class_8172 ? this.o.c() : entity instanceof class_3719;
      } else {
         return entity instanceof class_2595;
      }
   }

   private boolean F(class_2338 pos) {
      return aM_.field_1687 != null && aM_.field_1687.method_8321(pos) instanceof class_8172;
   }

   private void r() {
      if (this.l == null) {
         this.k = AutoWarden.a.SEARCH;
      } else {
         double distance = aM_.field_1724.method_19538().method_1022(this.l.method_46558());
         if (distance <= 3.5) {
            BaritoneBridge.d();
            this.j.b();
            this.k = this.F(this.l) ? AutoWarden.a.BREAK : AutoWarden.a.OPEN;
         } else if (this.j.a((long)(this.e.c() * 1000.0F))) {
            this.w("не удалось дойти");
         } else {
            if (!BaritoneBridge.c()) {
               BaritoneBridge.a(this.l);
            }
         }
      }
   }

   private void s() {
      if (this.l == null) {
         this.k = AutoWarden.a.SEARCH;
      } else if (aM_.field_1755 instanceof class_476) {
         this.j.b();
         this.k = AutoWarden.a.LOOT;
      } else if (this.j.a(3000L)) {
         this.w("сундук не открылся");
      } else {
         class_243 center = this.l.method_46558();
         class_243 eye = aM_.field_1724.method_33571();
         class_243 direction = center.method_1020(eye);
         float yaw = (float)Math.toDegrees(Math.atan2(direction.field_1350, direction.field_1352)) - 90.0F;
         float pitch = (float)(-Math.toDegrees(Math.atan2(direction.field_1351, Math.hypot(direction.field_1352, direction.field_1350))));
         aM_.field_1724.method_36456(yaw);
         aM_.field_1724.method_36457(pitch);
         if (aM_.field_1761 != null && aM_.field_1724.field_6012 % 10 == 0) {
            aM_.field_1761.method_2896(aM_.field_1724, class_1268.field_5808, new class_3965(center, class_2350.field_11036, this.l, false));
         }
      }
   }

   private void t() {
      if (!(aM_.field_1755 instanceof class_476 screen)) {
         this.j.b();
         this.k = this.f.c() ? AutoWarden.a.RETREAT : AutoWarden.a.SEARCH;
      } else if (aM_.field_1761 != null) {
         int rows = ((class_1707)screen.method_17577()).method_17388() * 9;
         boolean moved = false;

         for (int slot = 0; slot < rows; slot++) {
            class_1799 stack = ((class_1707)screen.method_17577()).method_7611(slot).method_7677();
            if (!stack.method_7960() && this.x(stack)) {
               aM_.field_1761.method_2906(((class_1707)screen.method_17577()).field_7763, slot, 0, class_1713.field_7794, aM_.field_1724);
               if (this.h.c()) {
                  ChatUtil.a("Забрано: &c" + stack.method_7964().getString());
               }

               moved = true;
               break;
            }
         }

         if (!moved || this.j.a(5000L)) {
            aM_.field_1724.method_7346();
            this.i.add(this.l);
            this.j.b();
            this.k = this.f.c() ? AutoWarden.a.RETREAT : AutoWarden.a.SEARCH;
         }
      }
   }

   private boolean x(class_1799 stack) {
      if (this.c.a("Всё подряд").c()) {
         return true;
      } else {
         String text = (stack.method_7964().getString() + " " + stack.method_7909().toString()).toLowerCase(Locale.ROOT);
         if (this.c.a("Сферы").c() && text.contains("сфера")) {
            return true;
         } else if (this.c.a("Талисманы").c() && text.contains("талисман")) {
            return true;
         } else if (!this.c.a("Зелья").c() || !text.contains("зелье") && !text.contains("potion")) {
            if (!this.c.a("Стрелы").c() || !text.contains("стрела") && !text.contains("arrow")) {
               if (!this.c.a("Оружие").c() || !text.contains("меч") && !text.contains("sword") && !text.contains("axe")) {
                  return !this.c.a("Броня").c()
                        || !text.contains("шлем") && !text.contains("нагрудник") && !text.contains("поножи") && !text.contains("ботинки")
                     ? this.c.a("Ценные предметы").c() && (stack.method_57826(class_9334.field_49631) || stack.method_57826(class_9334.field_49632))
                     : true;
               } else {
                  return true;
               }
            } else {
               return true;
            }
         } else {
            return true;
         }
      }
   }

   private void u() {
      if (this.m != null && !(aM_.field_1724.method_19538().method_1022(this.m) >= this.g.c().doubleValue()) && !this.j.a(8000L)) {
         if (!BaritoneBridge.c()) {
            class_243 away = aM_.field_1724.method_19538().method_1020(this.l.method_46558()).method_1029().method_1021(this.g.c().doubleValue());
            BaritoneBridge.a((int)(aM_.field_1724.method_23317() + away.field_1352), (int)(aM_.field_1724.method_23321() + away.field_1350));
         }
      } else {
         BaritoneBridge.d();
         this.j.b();
         this.k = AutoWarden.a.SEARCH;
      }
   }

   private void B() {
      if (this.l == null) {
         this.k = AutoWarden.a.SEARCH;
      } else if (!this.F(this.l)) {
         this.i.add(this.l);
         this.j.b();
         this.k = AutoWarden.a.PICKUP;
      } else if (this.j.a(6000L)) {
         this.w("кувшин не разбился");
      } else {
         this.G(this.l);
         if (aM_.field_1761 != null) {
            aM_.field_1761.method_2902(this.l, class_2350.field_11036);
            aM_.field_1724.method_6104(class_1268.field_5808);
         }
      }
   }

   private void G(class_2338 pos) {
      class_243 center = pos.method_46558();
      class_243 eye = aM_.field_1724.method_33571();
      class_243 direction = center.method_1020(eye);
      aM_.field_1724.method_36456((float)Math.toDegrees(Math.atan2(direction.field_1350, direction.field_1352)) - 90.0F);
      aM_.field_1724.method_36457((float)(-Math.toDegrees(Math.atan2(direction.field_1351, Math.hypot(direction.field_1352, direction.field_1350)))));
   }

   private void C() {
      if (aM_.field_1724 != null && aM_.field_1724.field_3944 != null) {
         if (!this.q) {
            this.q = true;
            this.j.b();
            String home = this.x.l("Сундук у дома") ? this.z.c() : this.p.c();
            aM_.field_1724.field_3944.method_45730("home " + home);
         } else {
            if (this.j.a(5000L)) {
               this.q = false;
               this.j.b();
               this.k = this.x.l("Не складывать") ? AutoWarden.a.SEARCH : AutoWarden.a.STORE;
            }
         }
      }
   }

   private void D() {
      if (aM_.field_1724 != null && aM_.field_1724.field_3944 != null) {
         if (aM_.field_1755 instanceof class_476) {
            this.H();
         } else if (!this.x.l("Клан-хранилище")) {
            class_2338 chest = this.z();
            if (chest == null) {
               if (this.j.a(6000L)) {
                  this.I();
               }
            } else {
               double distance = aM_.field_1724.method_19538().method_1022(chest.method_46558());
               if (distance > 3.5) {
                  if (!BaritoneBridge.c()) {
                     BaritoneBridge.a(chest);
                  }

                  if (this.j.a(15000L)) {
                     this.I();
                  }
               } else {
                  BaritoneBridge.d();
                  this.G(chest);
                  if (aM_.field_1761 != null && aM_.field_1724.field_6012 % 10 == 0) {
                     aM_.field_1761
                        .method_2896(aM_.field_1724, class_1268.field_5808, new class_3965(chest.method_46558(), class_2350.field_11036, chest, false));
                  }
               }
            }
         } else if (!this.q) {
            this.q = true;
            this.j.b();
            aM_.field_1724.field_3944.method_45730("clan storage");
         } else {
            if (this.j.a(5000L)) {
               this.I();
            }
         }
      }
   }

   private class_2338 z() {
      class_2338 best = null;
      double bestDistance = Double.MAX_VALUE;
      int centerX = aM_.field_1724.method_24515().method_10263() >> 4;
      int centerZ = aM_.field_1724.method_24515().method_10260() >> 4;

      for (int cx = centerX - 2; cx <= centerX + 2; cx++) {
         for (int cz = centerZ - 2; cz <= centerZ + 2; cz++) {
            class_2818 chunk = aM_.field_1687.method_8497(cx, cz);
            if (chunk != null) {
               for (Entry<class_2338, class_2586> entry : chunk.method_12214().entrySet()) {
                  if (entry.getValue() instanceof class_2595) {
                     double distance = aM_.field_1724.method_19538().method_1022(entry.getKey().method_46558());
                     if (distance < bestDistance) {
                        bestDistance = distance;
                        best = entry.getKey().method_10062();
                     }
                  }
               }
            }
         }
      }

      return best;
   }

   private void H() {
      if (aM_.field_1755 instanceof class_476 screen && aM_.field_1761 != null) {
         int container = ((class_1707)screen.method_17577()).method_17388() * 9;
         int total = ((class_1707)screen.method_17577()).field_7761.size();

         for (int slot = container; slot < total; slot++) {
            class_1799 stack = ((class_1707)screen.method_17577()).method_7611(slot).method_7677();
            if (!stack.method_7960() && !J(stack) && this.x(stack)) {
               aM_.field_1761.method_2906(((class_1707)screen.method_17577()).field_7763, slot, 0, class_1713.field_7794, aM_.field_1724);
               return;
            }
         }

         aM_.field_1724.method_7346();
         this.I();
      }
   }

   private void I() {
      this.q = false;
      this.j.b();
      BaritoneBridge.d();
      if (this.h.c()) {
         ChatUtil.a("Лут сложен, возвращаюсь к фарму.");
      }

      if (aM_.field_1724 != null && aM_.field_1724.field_3944 != null) {
         aM_.field_1724.field_3944.method_45730("home " + this.p.c());
      }

      this.k = AutoWarden.a.SEARCH;
   }

   private static boolean J(class_1799 stack) {
      String text = stack.method_7964().getString().toLowerCase(Locale.ROOT);
      return text.contains("невидим") || text.contains("invis");
   }

   private int K() {
      int count = 0;

      for (int slot = 0; slot < aM_.field_1724.method_31548().method_5439(); slot++) {
         class_1799 stack = aM_.field_1724.method_31548().method_5438(slot);
         if (!stack.method_7960() && J(stack)) {
            count += stack.method_7947();
         }
      }

      return count;
   }

   private void E() {
      if (aM_.field_1724 != null && aM_.field_1724.field_3944 != null) {
         if (!this.q) {
            this.q = true;
            this.j.b();
            aM_.field_1724.field_3944.method_45730("ah search " + this.v2.c());
         } else if (this.j.a(8000L)) {
            this.q = false;
            this.j.b();
            this.k = AutoWarden.a.SEARCH;
         } else if (aM_.field_1755 instanceof class_476 screen && aM_.field_1761 != null) {
            int container = ((class_1707)screen.method_17577()).method_17388() * 9;
            if (screen.method_25440().getString().toLowerCase(Locale.ROOT).contains("подтверж")) {
               for (int slot = 0; slot < container; slot++) {
                  class_1799 stack = ((class_1707)screen.method_17577()).method_7611(slot).method_7677();
                  if (!stack.method_7960() && stack.method_7964().getString().toLowerCase(Locale.ROOT).contains("купит")) {
                     aM_.field_1761.method_2906(((class_1707)screen.method_17577()).field_7763, slot, 0, class_1713.field_7790, aM_.field_1724);
                     if (this.h.c()) {
                        ChatUtil.a("Покупка подтверждена.");
                     }

                     this.q = false;
                     this.j.b();
                     this.k = AutoWarden.a.SEARCH;
                     return;
                  }
               }
            } else {
               for (int slotx = 0; slotx < container; slotx++) {
                  class_1799 stack = ((class_1707)screen.method_17577()).method_7611(slotx).method_7677();
                  if (!stack.method_7960() && J(stack)) {
                     aM_.field_1761.method_2906(((class_1707)screen.method_17577()).field_7763, slotx, 0, class_1713.field_7790, aM_.field_1724);
                     this.j.b();
                     return;
                  }
               }
            }
         }
      }
   }

   private float L() {
      int used = 0;

      for (int slot = 0; slot < aM_.field_1724.method_31548().method_5439(); slot++) {
         if (!aM_.field_1724.method_31548().method_5438(slot).method_7960()) {
            used++;
         }
      }

      return used * 100.0F / aM_.field_1724.method_31548().method_5439();
   }

   private boolean M() {
      return aM_.field_1724 != null && aM_.field_1724.method_6059(class_1294.field_5905);
   }

   private void N() {
      if (aM_.field_1724 != null && aM_.field_1761 != null && this.j.a(2000L)) {
         for (int slot = 0; slot < 9; slot++) {
            if (J(aM_.field_1724.method_31548().method_5438(slot))) {
               aM_.field_1724.method_31548().field_7545 = slot;
               aM_.field_1761.method_2919(aM_.field_1724, class_1268.field_5808);
               this.j.b();
               return;
            }
         }

         for (int slotx = 9; slotx < aM_.field_1724.method_31548().method_5439(); slotx++) {
            if (J(aM_.field_1724.method_31548().method_5438(slotx))) {
               aM_.field_1761.method_2906(0, slotx < 36 ? slotx : slotx, 8, class_1713.field_7791, aM_.field_1724);
               this.j.b();
               return;
            }
         }
      }
   }

   private void O() {
      if (aM_.field_1724 != null && aM_.field_1687 != null) {
         if (this.j.a(7000L)) {
            BaritoneBridge.d();
            this.j.b();
            this.k = this.f.c() ? AutoWarden.a.RETREAT : AutoWarden.a.SEARCH;
         } else {
            class_1542 nearest = null;
            double best = Double.MAX_VALUE;
            class_238 area = aM_.field_1724.method_5829().method_1014(12.0);

            for (class_1542 drop : aM_.field_1687.method_8390(class_1542.class, area, dropx -> true)) {
               if (this.x(drop.method_6983())) {
                  double distance = aM_.field_1724.method_19538().method_1022(drop.method_19538());
                  if (distance < best) {
                     best = distance;
                     nearest = drop;
                  }
               }
            }

            if (nearest == null) {
               BaritoneBridge.d();
               this.j.b();
               this.k = this.f.c() ? AutoWarden.a.RETREAT : AutoWarden.a.SEARCH;
            } else if (best > 1.2) {
               if (!BaritoneBridge.c()) {
                  BaritoneBridge.a(nearest.method_24515());
               }
            } else {
               this.j.b();
            }
         }
      } else {
         this.k = AutoWarden.a.SEARCH;
      }
   }

   private long P() {
      long total = 0L;

      for (int slot = 0; slot < aM_.field_1724.method_31548().method_5439(); slot++) {
         class_1799 stack = aM_.field_1724.method_31548().method_5438(slot);
         if (!stack.method_7960() && !J(stack) && this.x(stack)) {
            int price = ServerUtil.a.a(stack);
            if (price > 0) {
               total += (long)price * Math.max(1, stack.method_7947());
            }
         }
      }

      return total;
   }

   private void w(String reason) {
      if (this.h.c() && this.l != null) {
         ChatUtil.a("Пропускаю сундук: &c" + reason + "&7.");
      }

      if (this.l != null) {
         this.i.add(this.l);
      }

      BaritoneBridge.d();
      this.l = null;
      this.j.b();
      this.k = AutoWarden.a.SEARCH;
   }

   private static enum a {
      SEARCH,
      GOTO,
      OPEN,
      LOOT,
      BREAK,
      PICKUP,
      RETREAT,
      GO_HOME,
      STORE,
      BUY_INVIS;
   }
}

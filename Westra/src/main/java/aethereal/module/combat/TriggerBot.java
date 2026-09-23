package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.InputEvent;
import aethereal.event.TickEvent;
import aethereal.event.WillLandEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.ui.screen.AssistantScreen;
import aethereal.ui.screen.GUIScreen;
import aethereal.util.CounterUtil;
import aethereal.util.InventoryUtil;
import aethereal.util.Look;
import aethereal.util.MathUtil;
import aethereal.util.MoveUtil;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1429;
import net.minecraft.class_1657;
import net.minecraft.class_1738;
import net.minecraft.class_1743;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import net.minecraft.class_243;
import net.minecraft.class_2848;
import net.minecraft.class_3532;
import net.minecraft.class_9362;
import net.minecraft.class_2848.class_2849;
import platform.inject.accessors.ClientPlayerEntityAccessor;
import platform.inject.invokers.MinecraftClientInvoker;

@ModuleRegister(
   a = "Trigger Bot",
   b = "Автоматически наносит удар при наведении прицела на цель",
   c = Category.Combat
)
public class TriggerBot extends Module {
   private int l;
   boolean c;
   public int d;
   private int m;
   private float n;
   private class_1309 o;
   private final MultiModeSetting f = new MultiModeSetting(
      "Цели для атаки",
      new BooleanSetting("Игроки", true),
      new BooleanSetting("Животные", false),
      new BooleanSetting("Мобы", false),
      new BooleanSetting("Друзья", true)
   );
   private final MultiModeSetting g = new MultiModeSetting(
      "Дополнительно",
      new BooleanSetting("Только критические удары", true),
      new BooleanSetting("Адаптивные удары", true),
      new BooleanSetting("Случайные промахи", true)
   );
   private final MultiModeSetting h = new MultiModeSetting(
      "Не бить когда",
      new BooleanSetting("Используется предмет", true),
      new BooleanSetting("Открыт контейнер", true),
      new BooleanSetting("Враг за стеной", false)
   );
   private final ModeSetting i = new ModeSetting("Сброс спринта", "Легитный", "Легитный", "Рейдж");
   private final ModeSetting j = new ModeSetting("Выбор таргета", "Свободный", "Свободный", "Фиксирующий");
   BooleanSetting b = new BooleanSetting("Преследование цели", false);
   private final SliderSetting p = new SliderSetting("Дистанция удара", 3.0F, 1.0F, 6.0F, 0.1F);
   private final BooleanSetting q2 = new BooleanSetting("Только с оружием", false);
   private final BooleanSetting r2 = new BooleanSetting("Свой кулдаун", false);
   private final SliderSetting s2 = new SliderSetting("Готовность оружия", 80.0F, 0.0F, 100.0F, 1.0F).a(() -> this.r2.c());
   private final BooleanSetting t2 = new BooleanSetting("Не бить свипами", false);
   private final MultiModeSetting u2 = new MultiModeSetting(
      "Фильтр целей", new BooleanSetting("Невидимых", true), new BooleanSetting("Только в броне", false), new BooleanSetting("Только голых", false)
   );
   private final CounterUtil k = new CounterUtil();
   boolean e = false;

   @Generated
   public int r() {
      return this.d;
   }

   @Generated
   public class_1309 s() {
      return this.o;
   }

   public TriggerBot() {
      this.a(new Setting[]{this.f, this.g, this.h, this.u2, this.i, this.j, this.b, this.p, this.q2, this.r2, this.s2, this.t2});
   }

   @Override
   public void c() {
      super.c();
      this.o = null;
      this.l = 0;
      this.d = 0;
      this.k.b();
   }

   @EventTarget
   public void a(InputEvent e) {
      if (this.b.c() && this.o != null) {
         MoveUtil.a(e, this.n, 3);
      }

      if (this.o != null) {
         class_243 targetPosition = AuraUtil.a(aM_.field_1724.method_33571(), this.o, this.F(), true);
         this.n = targetPosition == class_243.field_1353
            ? Look.b()
            : (float)class_3532.method_15338(Math.toDegrees(Math.atan2(targetPosition.field_1350, targetPosition.field_1352)) - 90.0);
      }

      if (this.i.l("Легитный") && this.l > 0) {
         e.a(0.0F);
         e.b(0.0F);
         this.l--;
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      this.t();
   }

   @EventTarget
   public void a(WillLandEvent e) {
      this.c = e.b() && !aM_.field_1724.method_24828();
   }

   private void t() {
      this.d++;
      this.v();
      if (this.o != null
         && this.g.a("Случайные промахи").c()
         && this.d >= 2
         && this.m >= 30
         && (Math.random() > 0.5 && this.d >= 1 || this.d == 4)
         && (!this.e || !AuraUtil.a(aM_.field_1724.method_36454(), aM_.field_1724.method_36455(), this.F(), this.o, false))) {
         ((MinecraftClientInvoker)aM_).invokeDoAttack();
         this.e = !this.e;
         this.m = (int)MathUtil.a(-10.0F, 10.0F);
      }

      if (this.o != null && AuraUtil.a(this.d, this.o, false)) {
         this.l = 1;
      }

      this.u();
   }

   private void u() {
      if (this.o != null && this.q()) {
         if (AuraUtil.a(aM_.field_1724.method_36454(), aM_.field_1724.method_36455(), this.F(), this.o, !this.h.a("Враг за стеной").c())) {
            boolean skip = false;
            if ((Westra.h().d().t().H().e || aM_.field_1724.field_6017 > 2.0F && Westra.h().d().t().H().c.c()) && InventoryUtil.b(class_1802.field_49814) != -1
               )
             {
               if (aM_.field_1724.field_6017 < 1.5F) {
                  return;
               }

               double landDist = MaceUtil.a(aM_.field_1724, aM_.field_1687).map(pos -> pos.method_1022(this.o.method_19538())).orElse(33.0);
               boolean hitNow = landDist > 2.0;
               if (!this.c && !MaceUtil.b() && Westra.h().d().t().H().b.c() && !hitNow || !MaceUtil.a() || aM_.field_1724.method_6128()) {
                  return;
               }

               skip = true;
            }

            if (skip || !this.w()) {
               aM_.field_1761.method_2918(aM_.field_1724, this.o);
               aM_.field_1724.method_6104(class_1268.field_5808);
               if (Math.random() <= 0.89999974F) {
                  this.m++;
               }

               this.d = 0;
            }
         }
      }
   }

   private void v() {
      if (!this.j.l("Фиксирующий")) {
         class_1309 aimed = this.d(true);
         if (aimed != null) {
            this.o = aimed;
            this.k.b();
         } else if (this.o != null && this.k.a(1000L)) {
            this.o = null;
         }
      } else if (!this.a(this.o) || MaceUtil.a() && !this.c && !aM_.field_1724.method_7357().method_7904(class_1802.field_49814.method_7854())) {
         this.o = this.d(false);
      }
   }

   private boolean a(class_1309 entity) {
      if (entity == null || !entity.method_5805() || entity.method_7325() || entity == aM_.field_1724) {
         return false;
      } else if (!this.u2.a("Невидимых").c() && entity.method_5767()) {
         return false;
      } else if (this.u2.a("Только в броне").c() && !this.c(entity)) {
         return false;
      } else {
         return this.u2.a("Только голых").c() && this.c(entity) ? false : this.b(entity) && this.d(entity);
      }
   }

   private double F() {
      return this.p.c().doubleValue();
   }

   private boolean G() {
      if (!this.q2.c()) {
         return true;
      } else {
         class_1792 item = aM_.field_1724.method_6047().method_7909();
         return item instanceof class_1829 || item instanceof class_1743 || item instanceof class_9362 || item == class_1802.field_8547;
      }
   }

   private boolean H() {
      return !this.r2.c() ? true : aM_.field_1724.method_7261(0.0F) * 100.0F >= this.s2.c();
   }

   private boolean I() {
      return this.t2.c() && this.c && !aM_.field_1724.method_24828();
   }

   private boolean b(class_1309 entity) {
      return Westra.h().d().t().G().m() && aM_.field_1724.method_6128()
         ? true
         : AuraUtil.a(
            (class_1297)entity,
            4.0
               + aM_.field_1724.method_18798().method_1033() * 3.0
               + (InventoryUtil.b(class_1802.field_49814) != -1 && !(aM_.field_1724.field_6017 <= 1.5) ? 1.5F : 0.0F)
               + (
                  Westra.h().d().t().H().m()
                        && InventoryUtil.b(class_1802.field_49814) != -1
                        && MaceUtil.a(aM_.field_1724, aM_.field_1687).map(p -> aM_.field_1724.method_23318() - p.method_10214() > 2.0).orElse(false)
                     ? 10
                     : 0
               )
         );
   }

   private boolean c(class_1309 entity) {
      return Stream.of(class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166)
         .anyMatch(s -> entity.method_6118(s).method_7909() instanceof class_1738);
   }

   private boolean w() {
      if (!((ClientPlayerEntityAccessor)aM_.field_1724).getWasSprinting()
         || aM_.field_1724.method_5799()
         || aM_.field_1724.method_5771()
         || aM_.field_1724.method_5681()
         || aM_.field_1724.method_24828()) {
         return false;
      } else if (this.i.l("Рейдж")) {
         ((ClientPlayerEntityAccessor)aM_.field_1724).setWasSprinting(false);
         aM_.field_1724.method_5728(false);
         aM_.field_1724.field_3944.method_52787(new class_2848(aM_.field_1724, class_2849.field_12985));
         this.l = 1;
         return false;
      } else {
         this.l = 1;
         return ((ClientPlayerEntityAccessor)aM_.field_1724).getWasSprinting();
      }
   }

   public boolean q() {
      if (!this.G() || !this.H() || this.I()) {
         return false;
      } else if (this.h.a("Используется предмет") != null
         && this.h.a("Используется предмет").c()
         && aM_.field_1724.method_6115()
         && aM_.field_1724.method_6014() > 0
         && this.d >= 8) {
         this.d = 8;
         return false;
      } else if ((
            this.h.a("Открыт контейнер") == null
               || !this.h.a("Открыт контейнер").c()
               || aM_.field_1755 == null
               || aM_.field_1755 instanceof GUIScreen
               || aM_.field_1755 instanceof AssistantScreen
         )
         && AuraUtil.a(this.o, this.F())) {
         if (Westra.h().d().t().H().e) {
            if (aM_.field_1724.method_7357().method_7904(aM_.field_1724.method_6047())) {
               return false;
            }
         } else if (aM_.field_1724.field_6017 > 1.5F) {
            if (aM_.field_1724.method_7357().method_7904(aM_.field_1724.method_6047()) || this.d <= 3) {
               return false;
            }
         } else if (MaceUtil.a()) {
            if (aM_.field_1724.method_7357().method_7904(aM_.field_1724.method_6047()) || aM_.field_1724.method_7261(0.5F) < 0.9F) {
               return false;
            }
         } else if (aM_.field_1724.method_7261(0.5F) < 0.9F || this.d < 10) {
            return false;
         }

         return AuraUtil.c()
            || this.g.a("Адаптивные удары").c() && aM_.field_1724.method_24828() && !aM_.field_1724.field_3913.field_54155.comp_3163()
            || !AuraUtil.b();
      } else {
         return false;
      }
   }

   private class_1309 d(boolean aimed) {
      if (!aimed) {
         return (this.h.a("Враг за стеной").c() ? this.e(false).or(() -> this.e(true)) : this.e(true)).orElse(null);
      } else {
         float yaw = aM_.field_1724.method_36454();
         float pitch = aM_.field_1724.method_36455();
         return this.x()
            .filter(e -> AuraUtil.a(yaw, pitch, this.F(), e, !this.h.a("Враг за стеной").c()))
            .min(Comparator.comparingDouble(v0 -> AuraUtil.a(v0)))
            .orElse(null);
      }
   }

   private Optional<class_1309> e(boolean allowBehindWalls) {
      class_243 eye = aM_.field_1724.method_33571();
      Comparator<? super class_1309> comparatorComparingDouble;
      if (MaceUtil.a()) {
         class_243 landing = MaceUtil.a(aM_.field_1724, aM_.field_1687).orElse(null);
         class_243 landingEye = landing != null ? landing.method_1031(0.0, aM_.field_1724.method_5751(), 0.0) : null;
         comparatorComparingDouble = Comparator.<class_1309, Boolean>comparing(
               e -> !AuraUtil.a(eye, e, 4.0) && (landingEye == null || !AuraUtil.a(landingEye, e, 4.0))
            )
            .thenComparing(e2 -> aM_.field_1724.field_6017 > 1.0F && !this.c(e2))
            .thenComparingDouble(v0 -> AuraUtil.a(v0));
      } else {
         comparatorComparingDouble = Comparator.comparingDouble(
            e3 -> Math.acos(
               class_3532.method_15350(
                  class_243.method_1030(aM_.field_1724.method_36455(), aM_.field_1724.method_36454())
                     .method_1026(e3.method_5829().method_1005().method_1020(eye).method_1029()),
                  -1.0,
                  1.0
               )
            )
         );
      }

      Stream<class_1309> stream = this.x();
      if (!allowBehindWalls) {
         stream = stream.filter(e4 -> AuraUtil.a(eye, e4, 4.0));
      }

      return stream.min(comparatorComparingDouble);
   }

   private Stream<class_1309> x() {
      return StreamSupport.<class_1297>stream(aM_.field_1687.method_18112().spliterator(), false)
         .filter(class_1309.class::isInstance)
         .map(class_1309.class::cast)
         .filter(this::a);
   }

   private boolean d(class_1309 e) {
      if (!(e instanceof class_1657 p)) {
         if (e instanceof class_1308) {
            return this.f.a("Мобы").c();
         } else {
            return e instanceof class_1429 ? this.f.a("Животные").c() : false;
         }
      } else {
         return this.f.a("Игроки").c() && (this.f.a("Друзья").c() || !Westra.h().d().e().d(p.method_5477().getString()));
      }
   }
}

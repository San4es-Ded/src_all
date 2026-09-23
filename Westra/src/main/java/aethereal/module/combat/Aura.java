package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.GlobalEvent;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.InputEvent;
import aethereal.event.WillLandEvent;
import aethereal.handler.InventoryHandler;
import aethereal.lib.log4j.LoggerFactory;
import aethereal.lib.log4j.Logger_2;
import aethereal.module.combat.rotation.FunTimeRotation;
import aethereal.module.combat.rotation.HolyWorldRotation;
import aethereal.module.combat.rotation.HvHRotation;
import aethereal.module.combat.rotation.LegendsRotation;
import aethereal.module.combat.rotation.LegitSnapRotation;
import aethereal.module.combat.rotation.PolarRotation;
import aethereal.module.combat.rotation.ReallyWorldRotation;
import aethereal.module.combat.rotation.ReallyWorldV2Rotation;
import aethereal.module.combat.rotation.RotationBase;
import aethereal.module.combat.rotation.ShardRotation;
import aethereal.module.combat.rotation.SpookyRotation;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.ui.screen.AssistantScreen;
import aethereal.ui.screen.GUIScreen;
import aethereal.util.ChatUtil;
import aethereal.util.InventoryUtil;
import aethereal.util.Look;
import aethereal.util.MathUtil;
import aethereal.util.MoveUtil;
import aethereal.util.NeuroData;
import aethereal.util.Rotation;
import aethereal.util.RotationAnimator;
import aethereal.util.ServerUtil;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1296;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1307;
import net.minecraft.class_1309;
import net.minecraft.class_1421;
import net.minecraft.class_1427;
import net.minecraft.class_1510;
import net.minecraft.class_1588;
import net.minecraft.class_1621;
import net.minecraft.class_1657;
import net.minecraft.class_1738;
import net.minecraft.class_1743;
import net.minecraft.class_1802;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2848;
import net.minecraft.class_3532;
import net.minecraft.class_7298;
import net.minecraft.class_2848.class_2849;
import platform.inject.accessors.ClientPlayerEntityAccessor;
import platform.inject.invokers.MinecraftClientInvoker;

@ModuleRegister(
   a = "Aura",
   b = "Автоматически атакует цели рядом с вами",
   c = Category.Combat
)
public class Aura extends Module {
   @Generated
   private static final Logger_2 g = LoggerFactory.a(Aura.class);
   private class_1309 t;
   boolean d;
   private final ModeSetting h = new ModeSetting(
      "Выберите тип наведения",
      "ФанТайм",
      "ФанТайм",
      "ФанТайм ФОВ",
      "Легит",
      "ReallyWorld",
      "SpookyTime",
      "Sloth",
      "MLSAC",
      "Matrix",
      "HvH",
      "Vulcan",
      "Snap",
      "Neuro",
      "HolyWorld",
      "Spooky",
      "ReallyWorld+",
      "Shard",
      "FunTime+",
      "Legit Snap",
      "HvH+",
      "Legends",
      "Polar",
      "ReallyWorld v2"
   );
   private final MultiModeSetting i = new MultiModeSetting(
      "Цели для атаки",
      new BooleanSetting("Без брони", true),
      new BooleanSetting("Враждебные мобы", false),
      new BooleanSetting("Животные", false),
      new BooleanSetting("Друзья", false),
      new BooleanSetting("Игроки", true)
   );
   private final SliderSetting j = new SliderSetting("Дистанция атаки", 3.0F, 0.1F, 6.0F, 0.1F);
   private float B = 34.0F;
   private float C = 12.0F;
   private long D;
   private final HolyWorldRotation E = new HolyWorldRotation();
   private final SpookyRotation F2 = new SpookyRotation();
   private final ReallyWorldRotation G2 = new ReallyWorldRotation();
   private final ShardRotation H2 = new ShardRotation();
   private final FunTimeRotation I2 = new FunTimeRotation();
   private final LegitSnapRotation J2 = new LegitSnapRotation();
   private final HvHRotation K2 = new HvHRotation();
   private final LegendsRotation L2 = new LegendsRotation();
   private final PolarRotation N2 = new PolarRotation();
   private final ReallyWorldV2Rotation O2 = new ReallyWorldV2Rotation();
   private final SliderSetting M2 = new SliderSetting("Угол доворота", 30.0F, 5.0F, 90.0F, 1.0F).a(() -> this.h.l("Legit Snap"));
   private final SliderSetting P2 = new SliderSetting("FOV наведения", 360.0F, 10.0F, 360.0F, 5.0F).a(() -> this.h.l("Polar") || this.h.l("ReallyWorld v2"));
   private final ModeSetting Q2 = new ModeSetting("Античит", "Matrix", "Matrix", "Grim").a(() -> this.h.l("ReallyWorld v2"));
   private final SliderSetting k = new SliderSetting("Дополнительная дистанция", 0.5F, 0.1F, 3.0F, 0.1F);
   private final BooleanSetting l = new BooleanSetting("Только критические удары", true);
   private final BooleanSetting m = new BooleanSetting("Адаптивные удары", true).a(() -> this.l.c());
   private final MultiModeSetting n = new MultiModeSetting(
      "Не бить когда",
      new BooleanSetting("Используется предмет", true),
      new BooleanSetting("Открыт контейнер", true),
      new BooleanSetting("Враг за стеной", true)
   );
   private final BooleanSetting o = new BooleanSetting("Пробитие щита", true);
   private final BooleanSetting p = new BooleanSetting("Умный спринт", false);
   private final ModeSetting q = new ModeSetting("Приоритет цели", "Прицел", "Прицел", "Дистанция", "ХП");
   private final ModeSetting r = new ModeSetting("Коррекция движения", "Фокус", "Фокус", "Свободно");
   private final ModeSetting s = new ModeSetting("Визуализация цели", "Сферы", "Сферы", "Круг", "Тест");
   public int b = 0;
   float[] c = new float[]{-1.0F, -1.0F, -1.0F, -1.0F, 0.0F, -1.0F, -1.0F, -1.0F, -1.0F, -1.0F, -1.0F, -1.0F};
   private final float[] u = new float[30];
   int[] e = new int[]{-1, -1};
   boolean f = false;

   @Generated
   public ModeSetting r() {
      return this.s;
   }

   @Generated
   public class_1309 s() {
      return this.t;
   }

   public Aura() {
      this.a(new Setting[]{this.j, this.k, this.h, this.M2, this.P2, this.Q2, this.r, this.s, this.q, this.i, this.n, this.l, this.m, this.o, this.p});
   }

   @Override
   public void b() {
      if (this.c[9] == -1.0F) {
         this.c[9] = (int)MathUtil.a(9.0F, 13.0F);
      }

      super.b();
      this.c[8] = 2.0F;
      this.c[10] = 0.0F;
      this.c[11] = 0.0F;
      Arrays.fill(this.u, aM_.field_1724 != null ? aM_.field_1724.method_36455() : 0.0F);
      this.t = null;
   }

   @Override
   public void c() {
      super.c();
      this.c[10] = 0.0F;
      this.t = null;

      for (RotationBase rotation : new RotationBase[]{this.E, this.F2, this.G2, this.H2, this.I2, this.J2, this.K2, this.L2, this.N2, this.O2}) {
         rotation.b();
      }
   }

   private int F() {
      if (aM_.field_1724 == null) {
         return 0;
      } else {
         float progress = aM_.field_1724.method_7261(0.0F);
         return progress >= 1.0F ? 0 : (int)Math.ceil((1.0F - progress) * 10.0F);
      }
   }

   @EventTarget
   public void a(InputEvent e) {
      if (this.t != null) {
         MoveUtil.a(e, !this.r.l("Фокус") ? Look.b() : this.c[1], 2);
      }

      if (this.c[0] > 0.0F && this.t != null && AuraUtil.a(this.t, (double)this.j.c().floatValue())) {
         e.a(0.0F);
         e.b(0.0F);
         float[] fArr = this.c;
         fArr[0]--;
      }
   }

   @EventTarget
   public void a(GlobalEvent e) {
      if (this.t == null || !this.b(this.t) || MaceUtil.a() && !this.d && !aM_.field_1724.method_7357().method_7904(class_1802.field_49814.method_7854())) {
         class_1309 prev = this.t;
         boolean fresh = prev == null || !this.b(prev);
         this.t = fresh && this.n.a("Враг за стеной").c() ? this.d(false).or(() -> this.d(true)).orElse(null) : this.v().orElse(null);
         if (this.t != prev && this.t != null) {
            this.c[10] = 0.0F;
            this.c[11] = 0.0F;
            Arrays.fill(this.u, aM_.field_1724 != null ? aM_.field_1724.method_36455() : 0.0F);
         }
      }

      this.t();
      if (this.t != null) {
         this.u();
         this.w();
         this.u();
      } else {
         this.c[8] = 1.0F;
      }
   }

   @EventTarget
   public void a(WillLandEvent e) {
      this.d = e.b() && !aM_.field_1724.method_24828();
   }

   private int a(int from, int to) {
      for (int i = from; i < to; i++) {
         if (aM_.field_1724.method_31548().method_5438(i).method_7909() instanceof class_1743) {
            return i;
         }
      }

      return -1;
   }

   private void t() {
      if (!this.o.c() || this.t == null || !this.t.method_6039()) {
         if (this.e[0] != -1) {
            aM_.field_1724.method_31548().field_7545 = this.e[0];
            this.e[0] = -1;
         }

         if (this.e[1] != -1 && Westra.h().d().v().a().a().isEmpty()) {
            InventoryHandler var10000 = Westra.h().d().v().a();
            int var10002 = this.e[1];
            var10000.a(aM_.field_1724.method_31548().field_7545, var10002, 1);
            this.e[1] = -1;
         }
      }
   }

   private void u() {
      if (AuraUtil.a(aM_.field_1724.method_36454(), aM_.field_1724.method_36455(), this.j.c().floatValue(), this.t, !this.n.a("Враг за стеной").c())) {
         if (this.o.c() && this.t.method_6039()) {
            if (aM_.field_1724.method_6047().method_7909() instanceof class_1743) {
               aM_.field_1761.method_2918(aM_.field_1724, this.t);
               aM_.field_1724.method_6104(class_1268.field_5808);
            }

            int hotbar = this.a(0, 9);
            if (hotbar != -1) {
               if (aM_.field_1724.method_31548().field_7545 != hotbar) {
                  if (this.e[0] == -1) {
                     this.e[0] = aM_.field_1724.method_31548().field_7545;
                  }

                  aM_.field_1724.method_31548().field_7545 = hotbar;
               }
            } else {
               int inventory = this.a(9, 36);
               if (inventory != -1 && this.e[1] == -1 && Westra.h().d().v().a().a().isEmpty()) {
                  this.e[1] = inventory;
                  Westra.h().d().v().a().a(inventory, aM_.field_1724.method_31548().field_7545, 1);
               }
            }
         }

         if (this.q()) {
            boolean skip = false;
            if ((Westra.h().d().t().H().e || aM_.field_1724.field_6017 > 2.0F && Westra.h().d().t().H().c.c()) && InventoryUtil.b(class_1802.field_49814) != -1
               )
             {
               if (aM_.field_1724.field_6017 < 1.5F) {
                  return;
               }

               double landDist = MaceUtil.a(aM_.field_1724, aM_.field_1687).map(pos -> pos.method_1022(this.t.method_19538())).orElse(33.0);
               boolean hitNow = landDist > 2.0;
               if (!this.d && !MaceUtil.b() && Westra.h().d().t().H().b.c() && !hitNow || !MaceUtil.a() || aM_.field_1724.method_6128()) {
                  return;
               }

               skip = true;
            }

            if (((ClientPlayerEntityAccessor)aM_.field_1724).getWasSprinting()
               && !aM_.field_1724.method_5799()
               && !aM_.field_1724.method_5771()
               && !aM_.field_1724.method_5681()
               && !aM_.field_1724.method_24828()
               && !skip) {
               if (!this.p.c()) {
                  ((ClientPlayerEntityAccessor)aM_.field_1724).setWasSprinting(false);
                  aM_.field_1724.method_5728(false);
                  aM_.field_1724.field_3944.method_52787(new class_2848(aM_.field_1724, class_2849.field_12985));
                  this.c[0] = 1.0F;
               } else {
                  this.c[0] = 1.0F;
                  if (((ClientPlayerEntityAccessor)aM_.field_1724).getWasSprinting()) {
                     return;
                  }
               }
            }

            if (aM_.field_1761 != null) {
               this.c[3] = 0.0F;
               aM_.field_1761.method_2918(aM_.field_1724, this.t);
               aM_.field_1724.method_6104(class_1268.field_5808);
               this.b = 0;
               this.c[5] = MathUtil.a(8.0F, 10.0F);
               this.c[9] = (int)MathUtil.a(9.0F, 13.0F);
               if (this.c[2] == -1.0F) {
                  this.c[4] = (int)MathUtil.a(30.0F, 35.0F);
               }

               float[] fArr = this.c;
               fArr[2]++;
            }
         }
      }
   }

   public boolean q() {
      if (this.n.a("Используется предмет") != null
         && this.n.a("Используется предмет").c()
         && aM_.field_1724.method_6115()
         && aM_.field_1724.method_6014() > 0
         && this.b >= 8) {
         this.b = 8;
         return false;
      } else if ((
            this.n.a("Открыт контейнер") == null
               || !this.n.a("Открыт контейнер").c()
               || aM_.field_1755 == null
               || aM_.field_1755 instanceof GUIScreen
               || aM_.field_1755 instanceof AssistantScreen
         )
         && AuraUtil.a(this.t, (double)this.j.c().floatValue())) {
         if (Westra.h().d().t().H().e) {
            if (aM_.field_1724.method_7357().method_7904(aM_.field_1724.method_6047())) {
               return false;
            }
         } else if (aM_.field_1724.field_6017 > 1.5F) {
            if (aM_.field_1724.method_7357().method_7904(aM_.field_1724.method_6047()) || this.b <= 3) {
               return false;
            }
         } else if (MaceUtil.a()) {
            if (aM_.field_1724.method_7357().method_7904(aM_.field_1724.method_6047()) || aM_.field_1724.method_7261(0.5F) < 0.9F) {
               return false;
            }
         } else if (aM_.field_1724.method_7261(0.5F) < 0.9F || this.b < 10) {
            return false;
         }

         return AuraUtil.c() || this.m.c() && aM_.field_1724.method_24828() && !aM_.field_1724.field_3913.field_54155.comp_3163() || !AuraUtil.b();
      } else {
         return false;
      }
   }

   private boolean a(class_1309 entity) {
      if (aM_.field_1724 == null || aM_.field_1687 == null) {
         return false;
      } else {
         return Westra.h().d().t().G().m() && aM_.field_1724.method_6128()
            ? true
            : AuraUtil.a(
               (class_1297)entity,
               this.j.c() + this.k.c()
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
   }

   private Optional<class_1309> v() {
      return this.d(true);
   }

   private Optional<class_1309> d(boolean allowBehindWalls) {
      if (aM_.field_1687 != null && aM_.field_1724 != null) {
         class_243 eye = aM_.field_1724.method_33571();
         double reach = this.j.c() + this.k.c();
         Comparator<class_1309> order;
         if (MaceUtil.a()) {
            class_243 landing = MaceUtil.a(aM_.field_1724, aM_.field_1687).orElse(null);
            class_243 landingEye = landing != null ? landing.method_1031(0.0, aM_.field_1724.method_5751(), 0.0) : null;
            order = Comparator.<class_1309, Boolean>comparing(e -> !AuraUtil.a(eye, e, reach) && (landingEye == null || !AuraUtil.a(landingEye, e, reach)))
               .thenComparing(e2 -> aM_.field_1724.field_6017 > 1.0F && !this.c(e2))
               .thenComparingDouble(v0 -> AuraUtil.a(v0));
         } else {
            String var9 = this.q.c();

            order = switch (var9) {
               case "Дистанция" -> Comparator.comparingDouble(v0 -> AuraUtil.a(v0));
               case "ХП" -> Comparator.comparingDouble(v0 -> v0.method_6032());
               default -> Comparator.comparingDouble(
                  e3 -> Math.acos(
                     class_3532.method_15350(
                        class_243.method_1030(aM_.field_1724.method_36455(), aM_.field_1724.method_36454())
                           .method_1026(e3.method_5829().method_1005().method_1020(eye).method_1029()),
                        -1.0,
                        1.0
                     )
                  )
               );
            };
         }

         Stream<class_1309> stream2 = StreamSupport.<class_1297>stream(aM_.field_1687.method_18112().spliterator(), false)
            .filter(class_1309.class::isInstance)
            .map(class_1309.class::cast)
            .filter(e4 -> e4 != aM_.field_1724 && e4.method_5805())
            .filter(this::a)
            .filter(this::b);
         if (!allowBehindWalls) {
            stream2 = stream2.filter(e5 -> AuraUtil.a(eye, e5, reach));
         }

         return stream2.min(order);
      } else {
         return Optional.empty();
      }
   }

   private boolean b(class_1309 entity) {
      if (entity == null || !entity.method_5805() || !this.a(entity)) {
         return false;
      } else if (entity instanceof class_1657) {
         boolean isFriend = Westra.h().d().e().d(entity.method_5477().getString());
         boolean naked = Stream.of(class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166)
            .noneMatch(slot -> entity.method_6118(slot).method_7909() instanceof class_1738);
         if (!this.a("Игроки")) {
            return false;
         } else {
            return isFriend ? this.a("Друзья") : !naked || this.a("Без брони");
         }
      } else if (entity instanceof class_1588 || entity instanceof class_1621 || entity instanceof class_1307 || entity instanceof class_1510) {
         return this.a("Враждебные мобы");
      } else {
         return !(entity instanceof class_1296) && !(entity instanceof class_1427) && !(entity instanceof class_7298) && !(entity instanceof class_1421)
            ? false
            : this.a("Животные");
      }
   }

   private boolean a(String name) {
      BooleanSetting setting = this.i.a(name);
      return setting != null && setting.c();
   }

   private boolean c(class_1309 entity) {
      return Stream.of(class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166)
         .anyMatch(s -> entity.method_6118(s).method_7909() instanceof class_1738);
   }

   private void w() {
      class_243 class_243VarMethod_33571 = aM_.field_1724.method_33571();
      class_1309 class_1309Var = this.t;
      double dFloatValue = this.j.c().floatValue();
      boolean z = this.h.c().contains("ФанТайм") || !this.n.a("Враг за стеной").c();
      class_243 targetPosition = AuraUtil.a(class_243VarMethod_33571, class_1309Var, dFloatValue, z);
      float yawToTarget = targetPosition == class_243.field_1353
         ? Look.b()
         : (float)class_3532.method_15338(Math.toDegrees(Math.atan2(targetPosition.field_1350, targetPosition.field_1352)) - 90.0);
      float pitchToTarget = targetPosition == class_243.field_1353
         ? Look.c()
         : (float)(-Math.toDegrees(Math.atan2(targetPosition.field_1351, Math.hypot(targetPosition.field_1352, targetPosition.field_1350))));
      System.arraycopy(this.u, 0, this.u, 1, 29);
      this.u[0] = pitchToTarget;
      if (this.t != null
         && this.b >= 2
         && (ServerUtil.a.a(this.t) > 6.0F || this.c[2] > 43.0F)
         && this.c[2] >= 33.0F
         && (this.b == 4 || Math.random() > 0.5)
         && (!this.f || !AuraUtil.a(aM_.field_1724.method_36454(), aM_.field_1724.method_36455(), 3.0, this.t, false))) {
         ((MinecraftClientInvoker)aM_).invokeDoAttack();
         ChatUtil.a(this.f);
         if (Math.random() > 0.5) {
            this.f = !this.f;
         }

         this.c[2] = (int)MathUtil.a(-10.0F, 10.0F);
      }

      boolean skip = this.n.a("Используется предмет").c() && aM_.field_1724.method_6115() && aM_.field_1724.method_6014() > 0 && this.b >= 8
         || this.n.a("Открыт контейнер") != null
            && this.n.a("Открыт контейнер").c()
            && aM_.field_1755 != null
            && !(aM_.field_1755 instanceof GUIScreen)
            && !(aM_.field_1755 instanceof AssistantScreen);
      if (this.c[3] <= 0.0F && this.q() || AuraUtil.a(this.b, this.t, skip)) {
         this.c[3] = 1.0F;
         if (this.h.l("Shard")) {
            this.H2.q();
         } else if (this.h.l("HolyWorld")) {
            this.E.q();
         }

         if (!aM_.field_1724.method_5799() && this.p.c() && !aM_.field_1724.method_24828()) {
            this.c[0] = 1.0F;
         }
      }

      if (Westra.h().d().t().F().m() && this.q() && AuraUtil.a(this.t, 3.0) && aM_.field_1724.method_6128()) {
         Westra.h().d().k().a(new Rotation(yawToTarget, pitchToTarget), 180.0F, 0, 3);
      }

      if (!this.h.c().contains("ФанТайм")
         && (
            InventoryUtil.b(class_1802.field_49814) != -1
               || Westra.h().d().t().H().e
                  && aM_.field_1724.field_6017 > 3.0F
                  && MaceUtil.a(aM_.field_1724, aM_.field_1687).map(pos -> pos.method_1022(aM_.field_1724.method_19538())).orElse(0.0) > 2.0
                  && AuraUtil.a(this.t, 4.0 + aM_.field_1724.method_18798().method_1033() * 3.0)
         )) {
         float t = aM_.field_1724.field_6012 + aM_.method_61966().method_60637(false);
         float smoothW = (float)(
               (Math.sin(t * 0.31F) * 0.5 + Math.sin(t * 0.73F + 1.1F) * 0.3000000314327426 + Math.sin(t * 1.7F + 2.6F) * 0.2000000098386085) * 8.0
            )
            / 8.0F;
         float finalYaw = AuraUtil.a(aM_.field_1724.method_36454(), yawToTarget, 0.8F);
         float finalPitch = AuraUtil.a(aM_.field_1724.method_36455(), pitchToTarget, 0.8F);
         Westra.h().d().k().a(new Rotation(finalYaw + smoothW, finalPitch + smoothW), 180.0F, 1, 2);
      }

      String var14 = this.h.c();
      switch (var14) {
         case "ФанТайм":
         case "ФанТайм ФОВ":
            this.a(yawToTarget, pitchToTarget, targetPosition);
            break;
         case "Легит":
            this.b(yawToTarget, pitchToTarget, targetPosition);
            break;
         case "ReallyWorld":
            this.c(yawToTarget, pitchToTarget, targetPosition);
            break;
         case "SpookyTime":
            this.d(yawToTarget, pitchToTarget, targetPosition);
            break;
         case "Sloth":
            this.f(yawToTarget, pitchToTarget);
            break;
         case "MLSAC":
            this.g(yawToTarget, pitchToTarget);
            break;
         case "Matrix":
            this.h(yawToTarget, pitchToTarget);
            break;
         case "HvH":
            this.j(yawToTarget, pitchToTarget);
            break;
         case "Vulcan":
            this.k(yawToTarget, pitchToTarget);
            break;
         case "Snap":
            this.l(yawToTarget, pitchToTarget);
            break;
         case "HolyWorld":
            this.E.a(this.t, new Rotation(yawToTarget, pitchToTarget), this.F());
            break;
         case "Spooky":
            this.F2.a(this.t, new Rotation(yawToTarget, pitchToTarget), this.F());
            break;
         case "ReallyWorld+":
            this.G2.a(this.t, new Rotation(yawToTarget, pitchToTarget), this.F());
            break;
         case "Shard":
            this.H2.a(this.t, new Rotation(yawToTarget, pitchToTarget), this.F());
            break;
         case "FunTime+":
            this.I2.a(this.t, new Rotation(yawToTarget, pitchToTarget), this.F());
            break;
         case "Legit Snap":
            this.J2.a(this.M2.c());
            this.J2.a(this.t, new Rotation(yawToTarget, pitchToTarget), this.F());
            break;
         case "HvH+":
            this.K2.a(this.t, new Rotation(yawToTarget, pitchToTarget), this.F());
            break;
         case "Legends":
            this.L2.a(this.t, new Rotation(yawToTarget, pitchToTarget), this.F());
            break;
         case "Polar":
            this.N2.a(this.P2.c());
            this.N2.a(this.t, new Rotation(yawToTarget, pitchToTarget), this.F());
            break;
         case "ReallyWorld v2":
            this.O2.a(this.P2.c(), this.Q2.l("Grim"));
            this.O2.a(this.t, new Rotation(yawToTarget, pitchToTarget), this.F());
            break;
         case "Neuro":
            this.i(yawToTarget, pitchToTarget);
      }

      float[] fArr = this.c;
      fArr[3]--;
      float[] fArr2 = this.c;
      fArr2[5]--;
      float[] fArr3 = this.c;
      fArr3[8]--;
      this.c[1] = (float)class_3532.method_15338(
         Math.toDegrees(Math.atan2(this.t.method_23321() - aM_.field_1724.method_23321(), this.t.method_23317() - aM_.field_1724.method_23317())) - 90.0
      );
   }

   private void a(float yawToTarget, float pitchToTarget, class_243 vec3d) {
      float t = aM_.field_1724.field_6012 + aM_.method_61966().method_60637(false);
      float smoothW = (float)(Math.sin(t * 0.4000000008323731) * 3.0 + Math.sin(t * 0.9500002390239708 + 1.4000004888461306) * 2.0);
      float smoothH = (float)(Math.cos(t * 0.5 + 0.7000001555309916) * 0.5 + Math.cos(t * 0.7800000620494261 + 3.10000031689524) * 1.5);
      float finalPitch = AuraUtil.a(aM_.field_1724.method_36455(), this.u[class_3532.method_15340(10 - this.b, 0, 29)] + smoothH * 1.5F, MathUtil.a(0.1F, 0.5F));
      float finalYaw = AuraUtil.a(aM_.field_1724.method_36454(), yawToTarget + smoothW, MathUtil.a(0.1F, 0.4F));
      if (this.c[3] >= 0.0F) {
         if (!AuraUtil.a(aM_.field_1724.method_36454(), aM_.field_1724.method_36455(), this.j.c().floatValue(), this.t, true) && this.c[8] <= 0.0F) {
            finalYaw = yawToTarget;
         }

         if (!AuraUtil.a(yawToTarget, finalPitch, this.j.c().floatValue(), this.t, true) && this.c[8] <= 0.0F) {
            finalPitch = pitchToTarget;
         }

         if (!AuraUtil.a(aM_.field_1724.method_36454() + smoothW, aM_.field_1724.method_36454() + smoothH, this.j.c().floatValue(), this.t, true)
            && AuraUtil.a(aM_.field_1724.method_36454(), aM_.field_1724.method_36455(), this.j.c().floatValue(), this.t, true)) {
            smoothW = class_3532.method_15363(smoothW, -0.05F, 0.05F);
            smoothH = class_3532.method_15363(smoothH, -0.05F, 0.05F);
         }
      }

      if (this.b <= 4 && this.c[2] % 2.0F == 0.0F) {
         finalYaw = aM_.field_1724.method_36454();
      }

      Westra.h().d().k().a(new Rotation(finalYaw + smoothW, (this.h.c().equals("ФанТайм") ? finalPitch : Look.c()) + smoothH), 220.0F, 1, 1);
   }

   private void b(float yawToTarget, float pitchToTarget, class_243 vec3d) {
      float t = aM_.field_1724.field_6012 + aM_.method_61966().method_60637(false);
      float fSin = (float)((Math.sin(t * 0.31F) * 0.5 + Math.sin(t * 1.7F + 2.6F) * 0.2000000098386085) * 8.0) / 4.0F;
      float smoothW = fSin;
      float smoothH = fSin;
      float finalYaw = AuraUtil.a(aM_.field_1724.method_36454(), yawToTarget, MathUtil.a(0.2F, 0.35F));
      float finalPitch = AuraUtil.a(aM_.field_1724.method_36455(), pitchToTarget, MathUtil.a(0.15F, 0.25F));
      if (this.c[3] >= 0.0F) {
         finalPitch = AuraUtil.a(aM_.field_1724.method_36455(), pitchToTarget, 0.35F);
         smoothH = fSin / 3.0F;
         smoothW = fSin / 3.0F;
         if (!AuraUtil.a(aM_.field_1724.method_36454(), aM_.field_1724.method_36455(), this.j.c().floatValue(), this.t, true)) {
            finalYaw = AuraUtil.a(aM_.field_1724.method_36454(), yawToTarget, MathUtil.a(0.7F, 1.0F));
         }
      }

      if (!AuraUtil.a(finalYaw + smoothW, finalPitch + smoothH, this.j.c().floatValue(), this.t, true)
         && AuraUtil.a(yawToTarget, pitchToTarget, this.j.c().floatValue(), this.t, true)) {
         smoothW = class_3532.method_15363(smoothW, -0.15F, 0.15F);
         smoothH = class_3532.method_15363(smoothH, -0.15F, 0.15F);
      }

      if (this.c[5] >= 0.0F) {
         smoothW *= 8.0F;
         if (this.b >= 1 && this.c[2] % 5.0F == 0.0F) {
            finalPitch = AuraUtil.a(aM_.field_1724.method_36455(), -pitchToTarget, 0.05F);
         }
      }

      Westra.h().d().k().a(new Rotation(finalYaw + smoothW, finalPitch + smoothH), 180.0F, 1, 1);
   }

   private void c(float yawToTarget, float pitchToTarget, class_243 vec3d) {
      class_243 eye = aM_.field_1724.method_33571();
      class_238 box = this.t.method_5829();
      class_243 aim = new class_243(
         class_3532.method_15350(eye.field_1352, box.field_1323, box.field_1320) - eye.field_1352,
         class_3532.method_15350(eye.field_1351, box.field_1322, box.field_1325) - eye.field_1351,
         class_3532.method_15350(eye.field_1350, box.field_1321, box.field_1324) - eye.field_1350
      );
      double horizontal = Math.hypot(aim.field_1352, aim.field_1350);
      float yaw = (float)class_3532.method_15338(Math.toDegrees(Math.atan2(aim.field_1350, aim.field_1352)) - 90.0);
      float pitch = (float)(-Math.toDegrees(Math.atan2(aim.field_1351, horizontal)));
      float yawJitter = MathUtil.a(0.0F, 3.0F);
      float pitchJitter = MathUtil.a(0.0F, 2.0F);
      yaw += MathUtil.a(-yawJitter, yawJitter);
      pitch = class_3532.method_15363(pitch + MathUtil.a(-pitchJitter, pitchJitter), -89.0F, 89.0F);
      Westra.h().d().k().a(new Rotation(yaw, pitch), 360.0F, 1, 1);
   }

   private void d(float yawToTarget, float pitchToTarget, class_243 vec3d) {
      float currentYaw = aM_.field_1724.method_36454();
      float currentPitch = aM_.field_1724.method_36455();
      float yawDelta = class_3532.method_15393(yawToTarget - currentYaw);
      float pitchDelta = pitchToTarget - currentPitch;
      float difference = Math.max(1.0E-4F, Math.abs(yawDelta) + Math.abs(pitchDelta));
      float yawSpeed = MathUtil.a(22.0F, 45.0F);
      float pitchSpeed = MathUtil.a(5.0F, 15.0F);
      float yawJitter = e(-2.0F, 2.0F, 12.0F);
      float pitchJitter = e(-2.0F, 2.0F, 12.0F);
      float maxYawStep = Math.abs(yawDelta / difference) * yawSpeed;
      float maxPitchStep = Math.abs(pitchDelta / difference) * pitchSpeed;
      float finalYaw = currentYaw + class_3532.method_15363(yawDelta, -maxYawStep, maxYawStep) + yawJitter;
      float finalPitch = class_3532.method_15363(currentPitch + class_3532.method_15363(pitchDelta, -maxPitchStep, maxPitchStep) + pitchJitter, -89.0F, 89.0F);
      Rotation smoothed = RotationAnimator.e("aura_spooky", 2.0F).a(new Rotation(currentYaw, currentPitch), finalYaw, finalPitch);
      Westra.h().d().k().a(smoothed, 360.0F, 1, 1);
   }

   private void i(float yawToTarget, float pitchToTarget) {
      float currentYaw = aM_.field_1724.method_36454();
      float currentPitch = aM_.field_1724.method_36455();
      float deltaYaw = class_3532.method_15393(yawToTarget - currentYaw);
      float deltaPitch = pitchToTarget - currentPitch;
      float distance = this.t == null ? 3.0F : aM_.field_1724.method_5739(this.t);
      float[] step = NeuroData.a().a(deltaYaw, deltaPitch, distance);
      if (step == null) {
         Westra.h().d().k().a(this.a(yawToTarget, pitchToTarget, 40.0F, 20.0F, 0.0F, 0.0F), 360.0F, 1, 1);
      } else {
         float yawStep = Math.abs(step[0]) > Math.abs(deltaYaw) ? deltaYaw : step[0];
         float pitchStep = Math.abs(step[1]) > Math.abs(deltaPitch) ? deltaPitch : step[1];
         float pitch = class_3532.method_15363(currentPitch + pitchStep, -89.0F, 89.0F);
         Westra.h().d().k().a(new Rotation(currentYaw + yawStep, pitch), 360.0F, 1, 1);
      }
   }

   private Rotation a(float yawToTarget, float pitchToTarget, float yawSpeed, float pitchSpeed, float yawJitter, float pitchJitter) {
      float currentYaw = aM_.field_1724.method_36454();
      float currentPitch = aM_.field_1724.method_36455();
      float yawDelta = class_3532.method_15393(yawToTarget - currentYaw);
      float pitchDelta = pitchToTarget - currentPitch;
      float difference = Math.max(1.0E-4F, (float)Math.hypot(Math.abs(yawDelta), Math.abs(pitchDelta)));
      float maxYaw = Math.abs(yawDelta / difference) * yawSpeed;
      float maxPitch = Math.abs(pitchDelta / difference) * pitchSpeed;
      float yaw = currentYaw + class_3532.method_15363(yawDelta, -maxYaw, maxYaw) + yawJitter;
      float pitch = class_3532.method_15363(currentPitch + class_3532.method_15363(pitchDelta, -maxPitch, maxPitch) + pitchJitter, -89.0F, 89.0F);
      return new Rotation(yaw, pitch);
   }

   private boolean y() {
      return this.t != null && AuraUtil.a(aM_.field_1724.method_36454(), aM_.field_1724.method_36455(), this.j.c().floatValue(), this.t, true);
   }

   private void f(float yawToTarget, float pitchToTarget) {
      boolean aiming = this.y();
      float yawJitter = aiming ? 0.0F : 64.0F * (float)Math.cos(System.currentTimeMillis() / 29.0);
      float pitchJitter = aiming ? 0.0F : 12.0F * (float)Math.cos(System.currentTimeMillis() / 29.0);
      Westra.h().d().k().a(this.a(yawToTarget, pitchToTarget, 70.0F, 15.0F, yawJitter, pitchJitter), 360.0F, 1, 1);
   }

   private void g(float yawToTarget, float pitchToTarget) {
      float yawJitter = (float)(MathUtil.a(5.0F, 35.0F) * Math.cos(System.currentTimeMillis() / 90.0));
      float pitchJitter = (float)(MathUtil.a(5.0F, 15.0F) * Math.sin(System.currentTimeMillis() / 90.0));
      Rotation stepped = this.a(yawToTarget, pitchToTarget, 180.0F, 5.0F, yawJitter, pitchJitter);
      Rotation current = new Rotation(aM_.field_1724.method_36454(), aM_.field_1724.method_36455());
      Westra.h().d().k().a(RotationAnimator.c("aura_mlsac", MathUtil.a(0.1F, 0.4F)).a(current, stepped), 360.0F, 1, 1);
   }

   private void j(float yawToTarget, float pitchToTarget) {
      boolean aiming = this.y();
      float yawSpeed = aiming ? MathUtil.a(450.0F, 700.0F) : MathUtil.a(480.0F, 625.0F);
      float pitchSpeed = aiming ? 0.0F : MathUtil.a(455.0F, 555.0F);
      Westra.h().d().k().a(this.a(yawToTarget, pitchToTarget, yawSpeed, pitchSpeed, 0.0F, 0.0F), 425.0F, 0, 1);
   }

   private void k(float yawToTarget, float pitchToTarget) {
      long now = System.currentTimeMillis();
      float delta = this.D == 0L ? 1.0F : Math.min((float)(now - this.D) / 50.0F, 3.0F);
      this.D = now;
      float currentYaw = aM_.field_1724.method_36454();
      float currentPitch = aM_.field_1724.method_36455();
      float deltaYaw = class_3532.method_15393(yawToTarget - currentYaw);
      float deltaPitch = pitchToTarget - currentPitch;
      float distance = (float)Math.hypot(deltaYaw, deltaPitch);
      boolean engaged = this.y() && aM_.field_1724.method_7261(0.0F) > 0.9F;
      float stepYaw;
      float stepPitch;
      if (engaged) {
         float limit = Math.min(distance * 0.55F, 80.0F);
         stepYaw = class_3532.method_15363(deltaYaw, -limit, limit) * delta;
         stepPitch = class_3532.method_15363(deltaPitch, -limit * 0.5F, limit * 0.5F) * delta;
      } else {
         stepYaw = deltaYaw * 0.25F * delta;
         stepPitch = deltaPitch * 0.25F * delta;
         stepYaw += (float)(Math.sin(now / 120.0) * 2.0 + Math.sin(now / 310.0)) * delta;
         stepPitch += (float)(Math.cos(now / 150.0) * 1.5 + Math.cos(now / 280.0) * 0.7) * delta;
      }

      float yaw = currentYaw + stepYaw;
      float pitch = currentPitch + stepPitch;
      if (this.t != null && aM_.field_1724.method_5739(this.t) < 3.5F) {
         pitch += (float)(Math.sin(now / 90.0) * 1.8);
      }

      pitch = class_3532.method_15363(pitch, -89.0F, 89.0F);
      Westra.h().d().k().a(new Rotation(yaw, pitch), engaged ? 70.0F : 30.0F, MathUtil.a(15.0F, 25.0F), 0, 1);
   }

   private void l(float yawToTarget, float pitchToTarget) {
      boolean aiming = this.y();
      long now = System.currentTimeMillis();
      float yawJitter = aiming ? 0.0F : (float)Math.cos(now / 30.0) * MathUtil.a(8.0F, 16.0F);
      float pitchJitter = aiming ? 0.0F : (float)Math.sin(now / 30.0) * MathUtil.a(8.0F, 16.0F);
      float yawSpeed = MathUtil.a(80.0F, 90.0F);
      float pitchSpeed = MathUtil.a(80.0F, 90.0F);
      Westra.h().d().k().a(this.a(yawToTarget, pitchToTarget, yawSpeed, pitchSpeed, yawJitter, pitchJitter), 360.0F, 1, 1);
   }

   private void h(float yawToTarget, float pitchToTarget) {
      boolean aiming = this.y();
      float targetYawSpeed = aiming ? MathUtil.a(3.0F, 4.0F) : MathUtil.a(50.0F, 80.0F);
      float targetPitchSpeed = aiming ? MathUtil.a(1.0F, 2.0F) : MathUtil.a(7.0F, 12.0F);
      this.B = this.B + (targetYawSpeed - this.B) * 0.3F;
      this.C = this.C + (targetPitchSpeed - this.C) * 0.3F;
      Rotation stepped = this.a(yawToTarget, pitchToTarget, this.B, this.C, 0.0F, 0.0F);
      Westra.h().d().k().a(stepped, MathUtil.a(360.0F, 390.0F), 1, 1);
   }

   private static float e(float min, float max, float speed) {
      double t = (float)System.currentTimeMillis() * speed / 1000.0;
      float normalized = (float)(Math.sin(t) * 0.5 + 0.5);
      return min + (max - min) * normalized;
   }
}

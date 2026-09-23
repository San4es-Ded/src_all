package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.AttackEvent;
import aethereal.event.LookEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.StreamSupport;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1429;
import net.minecraft.class_1531;
import net.minecraft.class_1657;
import net.minecraft.class_1743;
import net.minecraft.class_1764;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_9362;

@ModuleRegister(
   a = "Aim Assistant",
   b = "Плавно доводит прицел до цели человеческой кривой наведения",
   c = Category.Combat
)
public class AimAssistant extends Module implements Interface {
   private final ModeSetting b = new ModeSetting("Ротация", "Легит", "Легит", "Свободная");
   private final ModeSetting c = new ModeSetting("Приоритет", "Угол", "Угол", "Дистанция", "Здоровье");
   private final SliderSetting d = new SliderSetting("Дистанция", 4.5F, 1.0F, 6.0F, 0.1F);
   private final SliderSetting e = new SliderSetting("Угол обзора", 360.0F, 20.0F, 360.0F, 1.0F);
   private final SliderSetting f2 = new SliderSetting("Удержание после удара", 5.0F, 0.0F, 30.0F, 1.0F);
   private final BooleanSetting g2 = new BooleanSetting("Только по последней цели", false);
   private final BooleanSetting h2 = new BooleanSetting("Наводка с арбалетом", false);
   private final SliderSetting i2 = new SliderSetting("Дистанция арбалета", 20.0F, 1.0F, 100.0F, 0.5F).a(() -> this.h2.c());
   private final MultiModeSetting j2 = new MultiModeSetting(
      "Цели",
      new BooleanSetting("Только игроки", true),
      new BooleanSetting("Невидимые", false),
      new BooleanSetting("Только в броне", false),
      new BooleanSetting("Только в незеритовой", false),
      new BooleanSetting("Мобы", false),
      new BooleanSetting("Животные", false),
      new BooleanSetting("Друзья", false)
   );
   private final MultiModeSetting k2 = new MultiModeSetting(
      "Условия",
      new BooleanSetting("Только с оружием", true),
      new BooleanSetting("Не целить в инвентаре", true),
      new BooleanSetting("Не целить во время еды", true),
      new BooleanSetting("Проверка стен", true),
      new BooleanSetting("Только по горизонтали", false)
   );
   private final SliderSetting l2 = new SliderSetting("Скорость", 1.0F, 0.1F, 3.0F, 0.05F).a(() -> this.b.l("Легит"));
   private final SliderSetting m2 = new SliderSetting("Сглаживание", 1.0F, 0.2F, 3.0F, 0.05F).a(() -> this.b.l("Легит"));
   private final SliderSetting n2 = new SliderSetting("Максимальный шаг", 1.5F, 0.2F, 2.5F, 0.05F).a(() -> this.b.l("Легит"));
   private final SliderSetting o2 = new SliderSetting("Минимальный шаг", 0.05F, 0.01F, 0.5F, 0.01F).a(() -> this.b.l("Легит"));
   private final BooleanSetting p2 = new BooleanSetting("Рандомизация", true).a(() -> this.b.l("Легит"));
   private class_1309 q2;
   private class_1297 r2;
   private long s2;
   private final SecureRandom t2 = new SecureRandom();
   private final float[] u2 = new float[5];
   private final double[] v2 = new double[15];
   private int w2;
   private int x2;
   private double y2;
   private long z2;
   private long A2;
   private float B2;
   private long C2;

   @Generated
   public class_1309 q() {
      return this.q2;
   }

   public AimAssistant() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e, this.f2, this.g2, this.h2, this.i2, this.j2, this.k2, this.l2, this.m2, this.n2, this.o2, this.p2});
   }

   @Override
   public void b() {
      this.G();
      super.b();
   }

   @Override
   public void c() {
      this.q2 = null;
      this.r2 = null;
      this.s2 = 0L;
      super.c();
   }

   private void G() {
      this.w2 = 0;
      this.x2 = 0;
      this.y2 = 0.0;
      this.z2 = System.currentTimeMillis();
      this.A2 = System.nanoTime();
      this.B2 = 0.0F;
      this.C2 = System.currentTimeMillis();
      Arrays.fill(this.u2, 0.0F);
      this.H();
   }

   private void H() {
      for (int phase = 0; phase < 5; phase++) {
         int index = phase * 3;
         long seed = this.A2 + phase * 7919L;
         this.v2[index] = a(seed, 0.3, 0.7);
         this.v2[index + 1] = a(seed + 1L, 0.2, 0.9);
         this.v2[index + 2] = a(seed + 2L, 0.1, 0.8);
      }
   }

   private static double a(long seed, double min, double max) {
      double wave = Math.abs(Math.sin(seed * 0.001)) % 1.0;
      return min + wave * (max - min);
   }

   @EventTarget
   public void a(AttackEvent event) {
      if ((this.f2.c() > 0.0F || this.g2.c()) && event.b() instanceof class_1309 target && this.a(target)) {
         this.r2 = target;
         this.s2 = System.currentTimeMillis();
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1687 != null) {
         TriggerBot trigger = Westra.h().d().t().X();
         class_1309 found = trigger.m() && trigger.s() != null ? trigger.s() : this.I();
         if (found != this.q2) {
            this.G();
         }

         this.q2 = found;
      } else {
         this.q2 = null;
      }
   }

   private class_1309 J() {
      if (this.r2 instanceof class_1309 held && this.a(held)) {
         long hold = (long)this.f2.c().floatValue() * 1000L;
         if (this.g2.c() || hold > 0L && System.currentTimeMillis() - this.s2 <= hold) {
            return held;
         }
      }

      return null;
   }

   private class_1309 I() {
      class_1309 held = this.J();
      if (held != null) {
         return held;
      } else if (this.g2.c()) {
         return null;
      } else {
         class_243 eye = aM_.field_1724.method_33571();
         class_243 look = class_243.method_1030(aM_.field_1724.method_36455(), aM_.field_1724.method_36454());
         Comparator<class_1309> order;
         if (this.c.l("Дистанция")) {
            order = Comparator.comparingDouble(entity -> entity.method_5858(aM_.field_1724));
         } else if (this.c.l("Здоровье")) {
            order = Comparator.comparingDouble(class_1309::method_6032);
         } else {
            order = Comparator.comparingDouble(
               entity -> Math.acos(class_3532.method_15350(look.method_1026(entity.method_5829().method_1005().method_1020(eye).method_1029()), -1.0, 1.0))
            );
         }

         return StreamSupport.<class_1297>stream(aM_.field_1687.method_18112().spliterator(), false)
            .filter(class_1309.class::isInstance)
            .map(class_1309.class::cast)
            .filter(this::a)
            .min(order)
            .orElse(null);
      }
   }

   @EventTarget
   public void a(LookEvent event) {
      class_1309 target = this.q2;
      if (target != null && aM_.field_1724 != null && this.K()) {
         class_243 aim = this.L(target);
         class_243 eye = aM_.field_1724.method_33571();
         double dx = aim.field_1352 - eye.field_1352;
         double dy = aim.field_1351 - eye.field_1351;
         double dz = aim.field_1350 - eye.field_1350;
         double flat = Math.sqrt(dx * dx + dz * dz);
         double full = Math.sqrt(dx * dx + dy * dy + dz * dz);
         if (!(full < 0.001)) {
            float yaw = aM_.field_1724.method_36454();
            float pitch = aM_.field_1724.method_36455();
            double targetYaw = flat < 0.001 ? yaw : Math.toDegrees(Math.atan2(-dx, dz));
            double targetPitch = -Math.toDegrees(Math.asin(class_3532.method_15350(dy / full, -1.0, 1.0)));
            double deltaYaw = class_3532.method_15338(targetYaw - yaw);
            double deltaPitch = targetPitch - pitch;
            double angle = Math.hypot(deltaYaw, deltaPitch);
            double step = this.b.l("Легит") ? this.M(angle) : this.N();
            double moveYaw = deltaYaw * step;
            double movePitch = deltaPitch * step;
            if (Math.abs(deltaYaw) < 0.02) {
               moveYaw = deltaYaw;
            }

            if (Math.abs(deltaPitch) < 0.02) {
               movePitch = deltaPitch;
            }

            aM_.field_1724.method_36456(yaw + (float)moveYaw);
            if (!this.k2.a("Только по горизонтали").c()) {
               aM_.field_1724.method_36457(class_3532.method_15363(pitch + (float)movePitch, -90.0F, 90.0F));
            }
         }
      }
   }

   private double N() {
      float frame = aM_.method_61966().method_60636();
      return 1.0 - Math.pow(0.72, Math.max((double)frame, 0.01));
   }

   private double M(double angle) {
      long now = System.currentTimeMillis();
      this.O(now);
      float sample = this.P((float)angle);
      this.u2[this.x2] = sample;
      this.x2 = (this.x2 + 1) % this.u2.length;
      float average = this.Q();
      float byAngle = this.R((float)angle);
      float frame = aM_.method_61966().method_60636();
      double base = angle / 3.0 * frame * this.l2.c().floatValue() / this.m2.c().floatValue();
      double min = this.o2.c().floatValue();
      double max = this.n2.c().floatValue();
      if (min > max) {
         min = max;
      }

      double step = class_3532.method_15350(base * average * byAngle, min, max);
      return class_3532.method_15350(step / Math.max(angle, 0.001), 0.0, 1.0);
   }

   private void O(long now) {
      long elapsed = now - this.z2;
      double duration = 3000.0 + this.v2[0] * 2000.0;
      this.y2 = elapsed / duration;
      if (this.y2 >= 1.0) {
         this.w2 = (this.w2 + 1) % 5;
         this.y2 = 0.0;
         this.z2 = now;
      }
   }

   private float P(float angle) {
      boolean random = this.p2.c();
      long since = System.currentTimeMillis() - this.C2;
      float value;
      if (random) {
         value = switch (this.w2) {
            case 0 -> 0.6F + this.t2.nextFloat() * 0.4F;
            case 1 -> 0.8F + this.t2.nextFloat() * 0.3F;
            case 2 -> 0.5F + this.t2.nextFloat() * 0.3F;
            case 3 -> 0.7F + this.t2.nextFloat() * 0.5F;
            case 4 -> 0.4F + this.t2.nextFloat() * 0.4F;
            default -> 0.7F + this.t2.nextFloat() * 0.4F;
         } * (0.9F + this.t2.nextFloat() * 0.3F);
         if (since < 45L) {
            value *= 0.7F + this.t2.nextFloat() * 0.4F;
         } else if (since > 300L) {
            value *= 1.1F + this.t2.nextFloat() * 0.4F;
         }

         if (angle > 120.0F) {
            value *= 1.3F;
         } else if (angle < 15.0F) {
            value *= 0.5F + this.t2.nextFloat() * 0.4F;
         }

         if (this.B2 > 0.0F) {
            float jump = value - this.B2;
            if (Math.abs(jump) > 0.35F) {
               value = this.B2 + (jump > 0.0F ? 0.35F : -0.35F);
            }
         }
      } else {
         value = 1.0F;
         if (angle > 120.0F) {
            value *= 1.3F;
         } else if (angle < 15.0F) {
            value *= 0.7F;
         }
      }

      float result = class_3532.method_15363(value, 0.15F, 1.8F);
      this.B2 = result;
      this.C2 = System.currentTimeMillis();
      return result;
   }

   private float Q() {
      float sum = 0.0F;
      int count = 0;

      for (float sample : this.u2) {
         if (sample > 0.0F) {
            sum += sample;
            count++;
         }
      }

      if (count > 0) {
         return sum / count;
      } else {
         return this.B2 > 0.0F ? this.B2 : 1.0F;
      }
   }

   private float R(float angle) {
      if (!this.p2.c()) {
         return angle < 20.0F ? 1.0F : (angle > 90.0F ? 0.95F : 0.97F);
      } else if (angle < 20.0F) {
         return 0.95F + this.t2.nextFloat() * 0.1F;
      } else {
         return angle > 90.0F ? 0.8F + this.t2.nextFloat() * 0.3F : 0.85F + this.t2.nextFloat() * 0.25F;
      }
   }

   private class_243 L(class_1309 target) {
      class_243 eye = aM_.field_1724.method_33571();
      class_243 base = target.method_19538();
      double dx = base.field_1352 - eye.field_1352;
      double dz = base.field_1350 - eye.field_1350;
      double flat = Math.sqrt(dx * dx + dz * dz);
      double factor;
      if (flat <= 4.0) {
         factor = 0.55;
      } else if (flat <= 12.0) {
         factor = 0.55 + (flat - 4.0) / 8.0 * 0.3;
      } else {
         factor = 0.85 + class_3532.method_15350((flat - 12.0) / 13.0, 0.0, 1.0) * 0.2;
      }

      double height = base.field_1351 + target.method_17682() * 0.5 - eye.field_1351;
      if (flat > 0.5) {
         factor -= class_3532.method_15350(height / flat * 0.15, -0.15, 0.15);
      }

      factor = class_3532.method_15350(factor, 0.2, 1.15);
      return base.method_1031(0.0, target.method_17682() * factor, 0.0);
   }

   private boolean K() {
      if (this.k2.a("Не целить в инвентаре").c() && aM_.field_1755 != null) {
         return false;
      } else {
         return this.k2.a("Не целить во время еды").c() && aM_.field_1724.method_6115() && !this.S() ? false : !this.k2.a("Только с оружием").c() || this.T();
      }
   }

   private boolean S() {
      return this.h2.c() && aM_.field_1724.method_6047().method_7909() instanceof class_1764;
   }

   private boolean T() {
      class_1792 item = aM_.field_1724.method_6047().method_7909();
      return this.S() ? true : item instanceof class_1829 || item instanceof class_1743 || item instanceof class_9362 || item == class_1802.field_8547;
   }

   private double U() {
      return this.S() ? this.i2.c().floatValue() : this.d.c().floatValue();
   }

   private boolean a(class_1309 entity) {
      if (entity != null && entity != aM_.field_1724 && entity.method_5805() && !entity.method_31481() && !(entity instanceof class_1531)) {
         if (aM_.field_1724.method_5739(entity) > this.U()) {
            return false;
         } else if (!this.j2.a("Невидимые").c() && entity.method_5767()) {
            return false;
         } else if (this.e.c() < 360.0F && this.V(entity) > this.e.c() / 2.0F) {
            return false;
         } else if (this.k2.a("Проверка стен").c() && !AuraUtil.a(aM_.field_1724.method_33571(), entity, this.U())) {
            return false;
         } else if (entity instanceof class_1657 player) {
            if (!this.j2.a("Друзья").c() && Westra.h().d().e().d(player.method_5477().getString())) {
               return false;
            } else {
               return this.j2.a("Только в незеритовой").c() && !this.W(player, class_1802.field_22028)
                  ? false
                  : !this.j2.a("Только в броне").c() || !player.method_6118(class_1304.field_6174).method_7960();
            }
         } else if (this.j2.a("Только игроки").c()) {
            return false;
         } else {
            return entity instanceof class_1308 ? this.j2.a("Мобы").c() : entity instanceof class_1429 && this.j2.a("Животные").c();
         }
      } else {
         return false;
      }
   }

   private boolean W(class_1657 player, class_1792 chest) {
      class_1799 stack = player.method_6118(class_1304.field_6174);
      return !stack.method_7960() && stack.method_31574(chest);
   }

   private float V(class_1297 entity) {
      class_243 eye = aM_.field_1724.method_33571();
      class_243 look = class_243.method_1030(aM_.field_1724.method_36455(), aM_.field_1724.method_36454());
      class_243 direction = entity.method_5829().method_1005().method_1020(eye).method_1029();
      return (float)Math.toDegrees(Math.acos(class_3532.method_15350(look.method_1026(direction), -1.0, 1.0)));
   }
}

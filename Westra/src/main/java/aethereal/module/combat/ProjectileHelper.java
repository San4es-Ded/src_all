package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.ClickEvent;
import aethereal.event.TickEvent;
import aethereal.event.WillLandEvent;
import aethereal.util.Look;
import aethereal.util.Rotation;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.StreamSupport;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1753;
import net.minecraft.class_1799;
import net.minecraft.class_1835;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3486;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

@ModuleRegister(
   a = "Projectile Helper",
   b = "Помогает целиться по противнику при стрельбе из лука или трезубца",
   c = Category.Combat
)
public class ProjectileHelper extends Module {
   private int d;
   private int e;
   private boolean g;
   private boolean h;
   private final class_1309[] b = new class_1309[2];
   private final class_243[] c = new class_243[5];
   private boolean f = true;

   public class_1309 q() {
      return this.b[0];
   }

   public boolean r() {
      if (this.b[0] != null && aM_.field_1724 != null && aM_.field_1724.method_6115()) {
         class_1799 active = aM_.field_1724.method_6030();
         return active.method_7909().method_7881(active, aM_.field_1724) - aM_.field_1724.method_6014() > 2;
      } else {
         return false;
      }
   }

   @Override
   public void c() {
      super.c();
      this.s();
   }

   @EventTarget
   public void a(WillLandEvent event) {
      this.g = event.b();
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1687 != null) {
         class_1799 stack = aM_.field_1724.method_5998(class_1268.field_5808);
         if (!(stack.method_7909() instanceof class_1753) && !(stack.method_7909() instanceof class_1835)) {
            this.s();
         } else {
            if (!aM_.field_1724.method_6115()) {
               this.f = true;
            }

            if (!this.f) {
               this.s();
            } else {
               this.h = aM_.field_1724.field_3913.field_54155.comp_3163() && (aM_.field_1724.method_24828() || this.g);
               this.a(this.t());
               if (this.b[0] != null) {
                  class_243[] class_243VarArr = this.c;
                  int i = this.e++;
                  class_243VarArr[i % this.c.length] = new class_243(
                     this.b[0].method_23317() - this.b[0].field_6014, 0.0, this.b[0].method_23321() - this.b[0].field_5969
                  );
               }

               Rotation aim;
               if (this.r() && (aim = this.a(stack)) != null) {
                  Westra.h().d().k().a(aim, 180.0F, 1, 1);
               }
            }
         }
      }
   }

   @EventTarget
   public void a(ClickEvent event) {
      if (event.b() && event.h() == 0 && aM_.field_1724 != null && aM_.field_1724.method_6115()) {
         this.f = !this.f;
      }
   }

   private void s() {
      class_1309[] class_1309VarArr = this.b;
      this.b[1] = null;
      class_1309VarArr[0] = null;
      this.d = 0;
      Arrays.fill(this.c, null);
   }

   private class_1309 t() {
      class_243 eye = aM_.field_1724.method_33571();
      class_243 look = class_243.method_1030(Look.c(), Look.b());
      return StreamSupport.<class_1297>stream(aM_.field_1687.method_18112().spliterator(), false)
         .filter(class_1657.class::isInstance)
         .map(e -> (class_1657)e)
         .filter(
            e -> e != aM_.field_1724
               && e.method_5805()
               && !Westra.h().d().e().d(e.method_5477().getString())
               && eye.method_1025(e.method_5829().method_1005()) <= 14400.0
         )
         .min(Comparator.comparingDouble(e2 -> -look.method_1026(e2.method_5829().method_1005().method_1020(eye).method_1029())))
         .map(e -> (class_1309)e)
         .orElse(null);
   }

   private void a(class_1309 best) {
      if (best != this.b[1]) {
         this.b[1] = best;
         this.d = 0;
      } else {
         this.d++;
      }

      if (this.b[0] != this.b[1] && (this.b[0] == null || this.d >= 4)) {
         this.b[0] = this.b[1];
         Arrays.fill(this.c, null);
      }
   }

   private class_243 u() {
      class_243 sum = class_243.field_1353;
      int count = 0;

      for (class_243 entry : this.c) {
         if (entry != null && entry.method_37268() > 1.000000229429758E-6) {
            sum = sum.method_1019(entry);
            count++;
         }
      }

      return count == 0 ? class_243.field_1353 : sum.method_1021(1.0 / count);
   }

   private Rotation a(class_1799 stack) {
      class_243 shooter = this.v();
      class_243 origin = aM_.field_1724.method_33571().method_1031(0.0, -0.1000000074661073, 0.0);
      double speed = stack.method_7909() instanceof class_1753 ? this.b(stack) : 2.5;
      class_238 box = this.b[0].method_5829();
      class_243 motion = this.u();
      class_243 aim = box.method_1005();
      float yaw = 0.0F;
      float pitch = 0.0F;

      for (int i = 0; i < 6; i++) {
         yaw = this.a(origin, aim);
         pitch = this.a(origin, aim, shooter, speed);
         double[] shot = this.a(
            origin,
            class_243.method_1030(pitch, yaw).method_1021(speed).method_1019(shooter),
            Math.hypot(aim.field_1352 - origin.method_10216(), aim.field_1350 - origin.method_10215()),
            true
         );
         if (shot == null) {
            return null;
         }

         class_243 moved = box.method_1005().method_1019(motion.method_1021(Math.min(shot[1] + 6.0, 13.0)));
         if (moved.method_1025(aim) < 9.999996044721066E-5) {
            break;
         }

         aim = moved;
      }

      Rotation rotation = new Rotation(class_3532.method_15393(yaw), pitch);
      float t = aM_.field_1724.field_6012 + aM_.method_61966().method_60637(false);
      float smoothW = (float)(
            Math.sin(t * 0.8F) * 11.0
               + Math.sin(t * 0.04000001688754603 + 17.200001527756587) * 1.5
               + Math.sin(t * 0.11000000003049541 + 5.800002923050999) * 3.0
               + Math.sin(t * 0.07000000374109333 + 12.300000031704212) * 1.0
         )
         / 4.0F;
      float smoothH = (float)(Math.sin(t * 0.1000000001867308) + Math.sin(t * 0.029999988014174556 + 54.09998474500903) * 0.5) / 2.0F;
      boolean tridentEarly = stack.method_7909() instanceof class_1835
         && stack.method_7909().method_7881(stack, aM_.field_1724) - aM_.field_1724.method_6014() < 9;
      if (!tridentEarly) {
         smoothW = class_3532.method_15363(smoothW, -0.3F, 0.3F);
         smoothH = class_3532.method_15363(smoothH, -0.3F, 0.3F);
      }

      rotation.a(rotation.c() + smoothW);
      rotation.b(rotation.d() + smoothH);
      return rotation;
   }

   private float a(class_243 origin, class_243 aim, class_243 shooter, double speed) {
      float low = -90.0F;
      float high = 90.0F;
      float yaw = this.a(origin, aim);
      double target = Math.hypot(aim.field_1352 - origin.method_10216(), aim.field_1350 - origin.method_10215());
      double height = aim.field_1351 - origin.method_10214();

      for (int i = 0; i < 24; i++) {
         float middle = (low + high) / 2.0F;
         double[] shot = this.a(origin, class_243.method_1030(middle, yaw).method_1021(speed).method_1019(shooter), target, false);
         if (shot != null && !(shot[0] >= height)) {
            high = middle;
         } else {
            low = middle;
         }
      }

      return (low + high) / 2.0F;
   }

   private double[] a(class_243 origin, class_243 velocity, double target, boolean blocked) {
      class_243 position = origin;
      class_243 current = velocity;
      double travelled = 0.0;

      for (int tick = 1; tick <= 100; tick++) {
         class_243 next = position.method_1019(current);
         if (blocked
            && aM_.field_1687.method_17742(new class_3959(position, next, class_3960.field_17558, class_242.field_1348, aM_.field_1724)).method_17783()
               != class_240.field_1333) {
            return null;
         }

         double reached = Math.hypot(next.field_1352 - origin.method_10216(), next.field_1350 - origin.method_10215());
         if (reached >= target) {
            double alpha = reached == travelled ? 1.0 : (target - travelled) / (reached - travelled);
            return new double[]{class_3532.method_16436(alpha, position.field_1351, next.field_1351) - origin.method_10214(), tick - 1 + alpha};
         }

         position = next;
         travelled = reached;
         current = current.method_1021(this.a(next) ? 0.6000002908794272 : 0.9900000228356232).method_1031(0.0, -0.050000001868616015, 0.0);
      }

      return null;
   }

   private boolean a(class_243 position) {
      return aM_.field_1687.method_8320(class_2338.method_49638(position)).method_26227().method_15767(class_3486.field_15517);
   }

   private class_243 v() {
      class_243 velocity = new class_243(
         aM_.field_1724.method_23317() - aM_.field_1724.field_6014,
         aM_.field_1724.method_23318() - aM_.field_1724.field_6036,
         aM_.field_1724.method_23321() - aM_.field_1724.field_5969
      );
      if (!this.h) {
         return new class_243(velocity.field_1352, aM_.field_1724.method_24828() ? 0.0 : velocity.field_1351, velocity.field_1350);
      } else {
         float yaw = aM_.field_1724.method_36454() * (float) (Math.PI / 180.0);
         double sprint = aM_.field_1724.method_5624() ? 0.19999997617511883 : 0.0;
         return new class_243(
            velocity.field_1352 - class_3532.method_15374(yaw) * sprint,
            Math.max((double)(0.42F + aM_.field_1724.method_37416()), velocity.field_1351),
            velocity.field_1350 + class_3532.method_15362(yaw) * sprint
         );
      }
   }

   private double b(class_1799 stack) {
      float pull = 1.0F;
      class_1799 active = aM_.field_1724.method_6030();
      if (aM_.field_1724.method_6115() && active.method_7909() instanceof class_1753) {
         float f = (active.method_7909().method_7881(active, aM_.field_1724) - aM_.field_1724.method_6014() + 1.5F) / 20.0F;
         pull = Math.min((f * f + f * 2.0F) / 3.0F, 1.0F);
      }

      return pull * 3.0;
   }

   private float a(class_243 from, class_243 to) {
      return (float)Math.toDegrees(Math.atan2(-(to.method_10216() - from.method_10216()), to.method_10215() - from.method_10215()));
   }
}

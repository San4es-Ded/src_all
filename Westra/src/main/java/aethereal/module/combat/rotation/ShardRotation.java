package aethereal.module.combat.rotation;

import aethereal.core.Westra;
import aethereal.util.GCDUtil;
import aethereal.util.Rotation;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public final class ShardRotation extends RotationBase {
   private class_1309 a;
   private float b;
   private float c;
   private float d2;
   private int e;
   private boolean f2;
   private boolean g2;
   private long h2;
   private long i2;
   private float j2;
   private float k2 = 1.2F;
   private int l2 = 1;
   private int m2;
   private float n2;
   private float o2;

   @Override
   public void a(class_1309 target, Rotation angle, int ticksToAttack) {
      if (aM_.field_1724 != null && target != null) {
         if (aM_.field_1724.method_5765()) {
            this.b = aM_.field_1724.method_36454();
            this.c = aM_.field_1724.method_36455();
            this.lastYaw = this.b;
            this.lastPitch = this.c;
         } else {
            if (!this.g2) {
               this.b = aM_.field_1724.method_36454();
               this.c = aM_.field_1724.method_36455();
               this.lastYaw = this.b;
               this.lastPitch = this.c;
               this.g2 = true;
            }

            if (this.a != target) {
               this.a = target;
               this.n2 = 0.0F;
               this.f2 = false;
               this.e = 0;
               this.h2 = 0L;
               this.i2 = 0L;
            }

            this.e++;
            this.d2 = (float)((Math.sin(this.e * 0.134) * 0.42 + (Math.random() * 0.09 - 0.21)) * 0.33);
            class_243 point = this.q(target);
            float[] rotations = this.r(point);
            float hold = System.currentTimeMillis() % 500L >= 350L ? 0.0F : 1.0F;
            float yawOffset = 0.0F;
            float pitchOffset = 0.0F;
            if (hold == 1.0F && this.h2 > 0L) {
               long elapsed = System.currentTimeMillis() - this.h2;
               if (elapsed < this.i2 && this.i2 > 0L) {
                  float progress = class_3532.method_15363((float)elapsed / (float)this.i2, 0.0F, 1.0F);
                  double phase = Math.PI * Math.pow(progress, this.k2);
                  float power = (float)(Math.sin(phase) * Math.sin(phase));
                  yawOffset = this.l2 * this.j2 * power;
                  pitchOffset = this.o2 * power;
               } else {
                  this.h2 = 0L;
               }
            }

            float targetYaw = rotations[0] + yawOffset;
            float targetPitch = rotations[1] + pitchOffset;
            float deltaYawAbs = Math.abs(class_3532.method_15393(targetYaw - this.b));
            boolean shouldHit = aM_.field_1724.method_7261(1.0F) > 0.7F && ticksToAttack <= 1;
            if (!this.f2) {
               float rate = 0.052F + (deltaYawAbs > 60.0F ? 0.032F : (deltaYawAbs > 50.0F ? 0.052F : 0.065F));
               if (shouldHit) {
                  rate += 0.038F;
               }

               this.n2 = this.n2 + rate * this.d2;
               if (this.n2 >= 0.07F) {
                  this.f2 = true;
               }
            } else {
               float ratex = shouldHit ? 0.025F : 0.008F;
               this.n2 = this.n2 - ratex * (3.0F + this.d2);
               if (this.n2 <= -0.1F) {
                  this.f2 = false;
               }
            }

            float speed = class_3532.method_15363(this.n2, 0.0F, aM_.field_1724.method_5624() ? 6.0F : 5.2F);
            if (shouldHit) {
               speed = Math.min(speed + 0.08F, aM_.field_1724.method_5624() ? 3.6F : 1.75F);
            }

            speed += this.d2 + 0.2F;
            float yawSpeed = speed * 0.7F * hold;
            float pitchSpeed = speed * (0.93F + this.d2) * hold;
            float deltaYaw = class_3532.method_15393(targetYaw - this.b);
            float deltaPitch = targetPitch - this.c;
            float yawLimit = aM_.field_1724.method_5624() ? 95.0F : (shouldHit ? 20.0F : 18.0F);
            float pitchLimit = aM_.field_1724.method_5624() ? 16.0F : (shouldHit ? 6.5F : 5.0F);
            deltaYaw = class_3532.method_15363(deltaYaw, -yawLimit, yawLimit);
            deltaPitch = class_3532.method_15363(deltaPitch, -pitchLimit, pitchLimit);
            float newYaw = this.b + deltaYaw * pitchSpeed;
            float newPitch = this.c + deltaPitch * yawSpeed;
            float mult = class_3532.method_15363(aM_.field_1724.method_5739(target) / 3.0F, 0.5F, 1.5F);
            newYaw += this.s((float)Math.random() * 0.1F * mult * hold, 15.0F);
            float gcd = GCDUtil.a();
            if (gcd > 0.0F) {
               newYaw = this.b + Math.round((newYaw - this.b) / gcd) * gcd;
               newPitch = this.c + Math.round((newPitch - this.c) / gcd) * gcd;
            }

            newPitch = class_3532.method_15363(newPitch, -89.0F, 89.0F);
            float limit = aM_.field_1724.method_5624() && target.method_5624() ? 160.0F : 45.0F;
            Westra.h().d().k().a(new Rotation(newYaw, newPitch), limit, limit, 360.0F, 360.0F, 0, 1);
            this.b = newYaw;
            this.c = newPitch;
            this.lastYaw = newYaw;
            this.lastPitch = newPitch;
         }
      }
   }

   private class_243 q(class_1309 target) {
      class_238 box = target.method_5829();
      double width = box.field_1320 - box.field_1323;
      double height = box.field_1325 - box.field_1322;
      double depth = box.field_1324 - box.field_1321;
      float time = (float)(System.currentTimeMillis() % 1000000L) / 1000.0F;
      int seed = target.method_5628();
      double wobbleX = Math.sin(time * 1.43F + seed) * (width * 0.38);
      double wobbleY = Math.sin(time * 0.89F + seed * 2.1F) * (height * 0.22) + height * 0.55;
      double wobbleZ = Math.cos(time * 1.67F + seed * 3.7F) * (depth * 0.38);
      return new class_243(box.field_1323 + width * 0.5 + wobbleX, box.field_1322 + wobbleY, box.field_1321 + depth * 0.5 + wobbleZ);
   }

   private float[] r(class_243 point) {
      double deltaX = point.field_1352 - aM_.field_1724.method_23317();
      double deltaY = point.field_1351 - aM_.field_1724.method_23320();
      double deltaZ = point.field_1350 - aM_.field_1724.method_23321();
      double flat = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
      float yaw = (float)(class_3532.method_15349(deltaZ, deltaX) * (180.0 / Math.PI) - 90.0);
      float pitch = (float)(-class_3532.method_15349(deltaY, flat) * (180.0 / Math.PI));
      return new float[]{yaw, pitch};
   }

   private float s(float amplitude, float period) {
      float time = (float)(System.currentTimeMillis() % 1000L) / period;
      return (float)Math.sin(time) * amplitude;
   }

   public void q() {
      this.l2 = ThreadLocalRandom.current().nextBoolean() ? 1 : -1;
      if (this.l2 == this.m2) {
         this.l2 = -this.m2;
      }

      this.m2 = this.l2;
      this.h2 = System.currentTimeMillis();
      this.i2 = 260L + ThreadLocalRandom.current().nextLong(160L);
      float min = 7.2F;
      float max = 15.6F;
      float strength = min + ThreadLocalRandom.current().nextFloat() * (max - min);
      if (Math.abs(strength - this.j2) < 2.4F) {
         strength = strength + 3.6F > max ? min : strength + 3.6F;
      }

      this.j2 = strength;
      this.n2 = 0.0F;
      this.k2 = 1.15F + ThreadLocalRandom.current().nextFloat() * 0.35F;
   }

   @Override
   public void b() {
      this.a = null;
      this.n2 = 0.0F;
      this.f2 = false;
      this.d2 = 0.0F;
      this.e = 0;
      this.h2 = 0L;
      this.i2 = 0L;
      this.j2 = 0.0F;
      this.g2 = aM_.field_1724 != null;
      if (aM_.field_1724 != null) {
         this.b = aM_.field_1724.method_36454();
         this.c = aM_.field_1724.method_36455();
      }

      super.b();
   }
}

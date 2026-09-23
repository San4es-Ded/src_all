package aethereal.module.combat.rotation;

import aethereal.core.Westra;
import aethereal.util.MathUtil;
import aethereal.util.Rotation;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1309;
import net.minecraft.class_3532;

public final class FunTimeRotation extends RotationBase {
   private static final long a = 7000L;
   private static final long b = 10000L;
   private static final float c = 0.8F;
   private static final float d2 = 1.0F;
   private static final float e = 180.0F;
   private static final float f2 = 0.55F;
   private float g2;
   private float h2;
   private float i2;
   private float j2;
   private float k2;
   private int l2 = -1;
   private boolean m2;
   private long n2;

   @Override
   public void a(class_1309 target, Rotation angle, int ticksToAttack) {
      if (aM_.field_1724 != null && target != null && angle != null) {
         if (target.method_5628() != this.l2) {
            this.q(target);
         }

         boolean attack = ticksToAttack <= 0;
         float baseYaw = angle.c();
         this.g2 = class_3532.method_15363(angle.d(), -90.0F, 90.0F);
         long now = System.currentTimeMillis();
         if (attack) {
            this.i2 = 0.0F;
            this.j2 = 0.0F;
            this.h2 = 0.0F;
            this.k2 = 0.0F;
            this.n2 = 0L;
         } else {
            if (this.m2) {
               this.r(now, true);
            } else if (now >= this.n2) {
               this.r(now, false);
            }

            float damp = this.m2 ? 1.0F : 0.8F;
            this.h2 = this.h2 + (this.i2 - this.h2) * damp;
            this.k2 = this.k2 + (this.j2 - this.k2) * damp;
         }

         this.m2 = attack;
         float targetYaw = baseYaw + this.h2;
         float targetPitch = class_3532.method_15363(this.g2 + this.k2, -90.0F, 90.0F);
         float currentYaw = aM_.field_1724.method_36454();
         float currentPitch = aM_.field_1724.method_36455();
         float deltaYaw = class_3532.method_15393(targetYaw - currentYaw);
         float deltaPitch = targetPitch - currentPitch;
         float hypot = (float)Math.hypot(Math.abs(deltaYaw), Math.abs(deltaPitch));
         if (hypot < 1.0E-4F) {
            hypot = 1.0E-4F;
         }

         boolean inOffset = Math.abs(this.h2) > 0.5F || Math.abs(this.k2) > 0.5F;
         float speedBase = !attack && !inOffset ? MathUtil.a(40.0F, 60.0F) : MathUtil.a(100.0F, 130.0F);
         float maxYawSpeed = Math.abs(deltaYaw / hypot) * speedBase;
         float maxPitchSpeed = Math.abs(deltaPitch / hypot) * speedBase;
         float shakeYaw = (float)(this.s(4, 7) * Math.sin(now / 60.0));
         float shakePitch = (float)(this.s(3, 7) * Math.cos(now / 60.0));
         float stepYaw = class_3532.method_16439(0.55F, 0.0F, deltaYaw);
         float stepPitch = class_3532.method_16439(0.55F, 0.0F, deltaPitch);
         stepYaw = Math.signum(stepYaw) * Math.min(Math.abs(stepYaw), 180.0F);
         stepPitch = Math.signum(stepPitch) * Math.min(Math.abs(stepPitch), 180.0F);
         float newYaw = currentYaw + stepYaw;
         this.g2 = currentPitch + stepPitch;
         int age = aM_.field_1724.field_6012;
         if (age % 15 == 0 && age > 0) {
            this.g2 -= 4.0F;
         }

         this.lastYaw = newYaw + shakeYaw;
         this.lastPitch = class_3532.method_15363(this.g2 + shakePitch, -90.0F, 90.0F);
         float reset = MathUtil.a(22.0F, 32.0F);
         Westra.h().d().k().a(new Rotation(this.lastYaw, this.lastPitch), maxYawSpeed, maxPitchSpeed, reset, reset * 0.85F, 0, 15);
      }
   }

   private void r(long now, boolean postAttack) {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      if (postAttack) {
         this.i2 = random.nextFloat(-70.0F, 70.0F);
         this.j2 = random.nextFloat(-60.0F, 60.0F);
      } else if (random.nextBoolean()) {
         this.i2 = random.nextFloat(-180.0F, 180.0F);
         this.j2 = random.nextFloat(-90.0F, 90.0F);
      } else {
         this.i2 = random.nextFloat(-20.0F, 20.0F);
         this.j2 = random.nextFloat(-15.0F, 15.0F);
      }

      this.n2 = now + 7000L + random.nextInt(3000);
   }

   private void q(class_1309 target) {
      this.h2 = 0.0F;
      this.k2 = 0.0F;
      this.i2 = 0.0F;
      this.j2 = 0.0F;
      this.m2 = false;
      this.n2 = System.currentTimeMillis() + 7000L + ThreadLocalRandom.current().nextInt(3000);
      this.l2 = target.method_5628();
   }

   private float s(int min, int max) {
      return min + this.rng.nextInt(max - min + 1);
   }

   @Override
   public void b() {
      this.h2 = 0.0F;
      this.k2 = 0.0F;
      this.i2 = 0.0F;
      this.j2 = 0.0F;
      this.m2 = false;
      this.n2 = 0L;
      this.l2 = -1;
      if (aM_.field_1724 != null) {
         this.g2 = aM_.field_1724.method_36455();
      }

      super.b();
   }
}

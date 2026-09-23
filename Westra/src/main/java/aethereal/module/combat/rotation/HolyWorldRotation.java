package aethereal.module.combat.rotation;

import aethereal.core.Westra;
import aethereal.util.GCDUtil;
import aethereal.util.Rotation;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public final class HolyWorldRotation extends RotationBase {
   private class_1309 b;
   private float c;
   private float d2;
   private float e;
   private float f2;
   private int g2 = -1;
   private int h2;
   private int i2;
   private int j2;
   private boolean k2;
   private HolyWorldRotation.a l2 = HolyWorldRotation.a.PURSUIT;
   private int m2;
   private int n2;
   private float o2 = 0.6F;
   private float p2 = 30.0F;
   private float q2;
   private float r2;
   private double s2 = 0.5;
   private double t2 = 0.5;
   private int u2 = 100;
   private double v2;
   private double w2;
   private double x2;
   private double y2;
   private float z2;
   private float A2;
   private int B2;
   private float C2 = 0.9F;
   private boolean D2;

   @Override
   public void a(class_1309 target, Rotation angle, int ticksToAttack) {
      if (aM_.field_1724 != null && target != null && angle != null) {
         if (aM_.field_1724.field_6012 != this.g2) {
            this.g2 = aM_.field_1724.field_6012;
            this.h2 = 0;
            this.F(target, angle, ticksToAttack);
         }

         float remainYaw = class_3532.method_15393(this.c - this.e);
         float remainPitch = this.d2 - this.f2;
         float remaining = (float)Math.hypot(remainYaw, remainPitch);
         float gcd = GCDUtil.a();
         if (remaining > 0.0F && remaining < gcd * 3.0F && this.rng.nextFloat() < 0.15F && ticksToAttack > 2) {
            this.h2++;
         } else {
            float speedJitter = 1.0F + (float)this.rng.nextGaussian() * 0.25F;
            float baseChase = 0.2F + remaining * 0.012F;
            float chase = class_3532.method_15363(baseChase * speedJitter, 0.15F, 0.7F);
            boolean attackWindow = ticksToAttack > 0 && ticksToAttack <= 2;
            boolean smallRemainder = gcd > 0.0F && remaining <= gcd * 1.5F;
            this.h2++;
            boolean complete = attackWindow || smallRemainder || this.h2 >= 4 + this.rng.nextInt(2);
            float fraction = complete ? 1.0F : chase;
            boolean flicking = this.l2 == HolyWorldRotation.a.FLICK;
            float outYaw = this.e + this.G(remainYaw * fraction, flicking);
            float outPitch = class_3532.method_15363(this.f2 + this.G(remainPitch * fraction, flicking), -89.0F, 89.0F);
            this.H(outYaw, outPitch);
         }
      }
   }

   private void F(class_1309 target, Rotation angle, int ticksToAttack) {
      if (this.b != target) {
         this.I(target);
      } else {
         this.N();
      }

      this.J();
      this.K();
      this.P(target);
      if (this.n2 > 0) {
         this.n2--;
      }

      Rotation led = this.O(target, angle);
      float wantedYaw = class_3532.method_15393(led.c() + this.z2);
      float wantedPitch = class_3532.method_15363(led.d() + this.A2, -89.0F, 89.0F);
      if (!this.k2) {
         if (++this.i2 >= this.j2) {
            this.k2 = true;
         }
      } else {
         float diffYaw = class_3532.method_15393(wantedYaw - this.c);
         float diffPitch = wantedPitch - this.d2;
         float error = (float)Math.hypot(diffYaw, diffPitch);
         this.v2 = this.v2 + (-this.v2 * 0.12 + this.rng.nextGaussian() * 0.1);
         this.w2 = this.w2 + (-this.w2 * 0.07 + this.rng.nextGaussian() * 0.045);
         float gainScale = 1.0F + class_3532.method_15363((float)this.v2, -0.6F, 0.6F);
         if (this.l2 == HolyWorldRotation.a.PURSUIT && error > this.R(13.0F, 7.0F) && this.n2 <= 0) {
            this.L(diffYaw, diffPitch);
         }

         float stepYaw;
         float stepPitch;
         switch (this.l2) {
            case FLICK:
               float flickYaw = class_3532.method_15393(this.q2 - this.c);
               float flickPitch = this.r2 - this.d2;
               float gainx = this.o2 * gainScale;
               float[] stepx = this.Q(flickYaw * gainx, flickPitch * gainx, this.p2);
               stepYaw = stepx[0];
               stepPitch = stepx[1];
               if (--this.m2 <= 0 || Math.hypot(flickYaw, flickPitch) < 1.0) {
                  this.M();
               }
               break;
            case SETTLE:
               float gain = (0.18F + this.rng.nextFloat() * 0.08F) * gainScale;
               float[] step = this.Q(diffYaw * gain, diffPitch * gain, 2.5F);
               stepYaw = step[0];
               stepPitch = step[1];
               if (--this.m2 <= 0) {
                  this.l2 = HolyWorldRotation.a.PURSUIT;
               }
               break;
            default:
               float lockRadius = this.R(1.6F, 0.9F);
               if (error < lockRadius) {
                  if (error > lockRadius * 0.6F && this.rng.nextFloat() < 0.3F) {
                     float[] stepx = this.Q(diffYaw * 0.4F, diffPitch * 0.4F, 1.1F);
                     stepYaw = stepx[0];
                     stepPitch = stepx[1];
                  } else {
                     stepYaw = 0.0F;
                     stepPitch = 0.0F;
                  }
               } else {
                  float gainx = this.R(0.2F, 0.42F) * gainScale;
                  float[] stepx = this.Q(diffYaw * gainx, diffPitch * gainx, this.R(3.5F, 8.5F));
                  stepYaw = stepx[0];
                  stepPitch = stepx[1];
               }
         }
         float tremor = this.R(0.05F, 0.11F) * switch (this.l2) {
            case FLICK -> 0.25F;
            case SETTLE -> 0.7F;
            default -> 1.0F;
         };
         stepYaw += (float)this.rng.nextGaussian() * tremor;
         stepPitch += (float)this.rng.nextGaussian() * tremor * 0.8F;
         if (ticksToAttack == 2) {
            float ease = 0.6F * (0.85F + this.rng.nextFloat() * 0.3F);
            stepYaw *= ease;
            stepPitch *= ease;
         } else if (ticksToAttack == 1) {
            float ease = 0.35F * (0.8F + this.rng.nextFloat() * 0.4F);
            stepYaw *= ease;
            stepPitch *= ease;
         }

         float curveScale = this.l2 == HolyWorldRotation.a.FLICK ? 0.3F : 1.0F;
         float curve = class_3532.method_15363((float)this.w2, -0.28F, 0.28F) * curveScale;
         float cos = class_3532.method_15362(curve);
         float sin = class_3532.method_15374(curve);
         this.c += stepYaw * cos - stepPitch * sin;
         this.d2 = class_3532.method_15363(this.d2 + stepYaw * sin + stepPitch * cos, -89.0F, 89.0F);
      }
   }

   public int q() {
      this.t2 = class_3532.method_15350(this.t2 + this.rng.nextGaussian() * 0.12 + 0.02, 0.05, 0.95);
      this.C2 = 0.8F + 0.2F * (float)Math.sqrt(this.rng.nextDouble());
      this.D2 = this.rng.nextFloat() < this.R(0.04F, 0.09F);
      float mean = this.R(10.0F, 5.5F);
      double delay = mean + this.rng.nextGaussian() * (1.2 + (1.0 - this.s2) * 0.8);
      if (this.rng.nextFloat() < 0.18F) {
         delay += 1.0 + Math.abs(this.rng.nextGaussian()) * 2.5;
      }

      if (this.rng.nextFloat() < 0.07F) {
         delay += 6.0 + this.rng.nextInt(9);
      }

      return class_3532.method_15340((int)Math.round(delay), 2, 26);
   }

   public float r() {
      return this.C2;
   }

   public boolean s() {
      return this.D2;
   }

   @Override
   public void b() {
      this.b = null;
      this.g2 = -1;
      this.h2 = 0;
      this.l2 = HolyWorldRotation.a.PURSUIT;
      this.m2 = 0;
      this.n2 = 0;
      if (aM_.field_1724 != null) {
         this.c = aM_.field_1724.method_36454();
         this.e = this.c;
         this.lastYaw = this.c;
         this.d2 = aM_.field_1724.method_36455();
         this.f2 = this.d2;
         this.lastPitch = this.d2;
         this.B2 = aM_.field_1724.field_6235;
      }
   }

   private void I(class_1309 target) {
      this.b = target;
      this.h2 = 0;
      this.c = aM_.field_1724.method_36454();
      this.d2 = aM_.field_1724.method_36455();
      this.e = this.c;
      this.f2 = this.d2;
      this.lastYaw = this.c;
      this.lastPitch = this.d2;
      this.B2 = aM_.field_1724.field_6235;
      this.j2 = 2 + this.rng.nextInt(4);
      this.i2 = 0;
      this.k2 = false;
      this.l2 = HolyWorldRotation.a.PURSUIT;
      this.m2 = 0;
      this.n2 = 0;
      this.u2 = 60 + this.rng.nextInt(120);
      this.v2 = 0.0;
      this.w2 = 0.0;
      this.x2 = 0.0;
      this.y2 = 0.0;
      this.z2 = 0.0F;
      this.A2 = 0.0F;
      this.C2 = 0.85F + 0.15F * this.rng.nextFloat();
      this.D2 = false;
   }

   private void J() {
      int hurt = aM_.field_1724.field_6235;
      if (hurt > this.B2) {
         this.c = this.c + (this.rng.nextFloat() - 0.5F) * 2.0F * (1.0F + this.rng.nextFloat() * 2.5F);
         this.d2 = class_3532.method_15363(this.d2 + (this.rng.nextFloat() - 0.5F) * 2.0F * (0.8F + this.rng.nextFloat() * 1.8F), -89.0F, 89.0F);
         if (this.l2 == HolyWorldRotation.a.FLICK) {
            this.M();
         }
      }

      this.B2 = hurt;
   }

   private void K() {
      if (--this.u2 <= 0) {
         this.t2 = class_3532.method_15350(this.t2 + this.rng.nextGaussian() * 0.45, 0.05, 0.95);
         this.u2 = 80 + this.rng.nextInt(160);
      }

      this.s2 = this.s2 + ((this.t2 - this.s2) * 0.02 + this.rng.nextGaussian() * 0.003);
      this.s2 = class_3532.method_15350(this.s2, 0.0, 1.0);
   }

   private void L(float diffYaw, float diffPitch) {
      this.l2 = HolyWorldRotation.a.FLICK;
      this.m2 = 1 + this.rng.nextInt(2 + (int)Math.round(this.s2));
      this.o2 = 0.5F + this.rng.nextFloat() * 0.35F;
      this.p2 = 24.0F + this.rng.nextFloat() * 16.0F + (float)this.s2 * 6.0F;
      float land = this.rng.nextFloat() < 0.22F ? 1.01F + this.rng.nextFloat() * 0.04F : 0.9F + this.rng.nextFloat() * 0.09F;
      this.q2 = this.c + diffYaw * land;
      this.r2 = this.d2 + diffPitch * land;
   }

   private void M() {
      this.l2 = HolyWorldRotation.a.SETTLE;
      this.m2 = 1 + this.rng.nextInt(3);
      this.n2 = Math.round(this.R(9.0F, 4.0F)) + this.rng.nextInt(4);
   }

   private void N() {
      float deltaYaw = Math.abs(class_3532.method_15393(aM_.field_1724.method_36454() - this.e));
      float deltaPitch = Math.abs(aM_.field_1724.method_36455() - this.f2);
      if (deltaYaw > 2.0F || deltaPitch > 2.0F) {
         this.c = aM_.field_1724.method_36454();
         this.e = this.c;
         this.d2 = aM_.field_1724.method_36455();
         this.f2 = this.d2;
      }
   }

   private Rotation O(class_1309 target, Rotation base) {
      if (aM_.field_1724.method_6128()) {
         return base;
      } else {
         class_243 movement = new class_243(
            target.method_23317() - target.field_6014, target.method_23318() - target.field_6036, target.method_23321() - target.field_5969
         );
         if (movement.method_1027() < 1.0E-6) {
            return base;
         } else {
            double leadTicks = class_3532.method_15350(this.e() / 50.0 * 0.5 + 0.5, 0.5, 2.5);
            class_243 offset = movement.method_1021(leadTicks);
            double length = offset.method_1033();
            if (length > 0.45) {
               offset = offset.method_1021(0.45 / length);
            }

            class_243 from = target.method_5829().method_1005().method_1020(aM_.field_1724.method_33571());
            class_243 led = from.method_1019(offset);
            float rawYaw = (float)Math.toDegrees(Math.atan2(-from.field_1352, from.field_1350));
            float rawPitch = (float)(-Math.toDegrees(Math.atan2(from.field_1351, Math.hypot(from.field_1352, from.field_1350))));
            float ledYaw = (float)Math.toDegrees(Math.atan2(-led.field_1352, led.field_1350));
            float ledPitch = (float)(-Math.toDegrees(Math.atan2(led.field_1351, Math.hypot(led.field_1352, led.field_1350))));
            return new Rotation(base.c() + class_3532.method_15393(ledYaw - rawYaw), base.d() + (ledPitch - rawPitch));
         }
      }
   }

   private void P(class_1309 target) {
      this.x2 = this.x2 + (-this.x2 * 0.05 + this.rng.nextGaussian() * 0.1);
      this.y2 = this.y2 + (-this.y2 * 0.05 + this.rng.nextGaussian() * 0.1);
      double distance = Math.max(aM_.field_1724.method_5739(target), 1.0F);
      float yawRange = (float)Math.toDegrees(Math.atan(0.14 / distance));
      float pitchRange = (float)Math.toDegrees(Math.atan(0.18 / distance));
      this.z2 = class_3532.method_15363((float)this.x2, -1.0F, 1.0F) * yawRange;
      this.A2 = class_3532.method_15363((float)this.y2, -1.0F, 1.0F) * pitchRange;
   }

   private float R(float careful, float aggressive) {
      return (float)(careful + (aggressive - careful) * this.s2);
   }

   private float[] Q(float stepYaw, float stepPitch, float cap) {
      float length = (float)Math.hypot(stepYaw, stepPitch);
      if (length > cap && length > 1.0E-4F) {
         float scale = cap / length;
         return new float[]{stepYaw * scale, stepPitch * scale};
      } else {
         return new float[]{stepYaw, stepPitch};
      }
   }

   private void H(float yaw, float pitch) {
      this.e = yaw;
      this.f2 = pitch;
      this.lastYaw = yaw;
      this.lastPitch = pitch;
      Westra.h().d().k().a(new Rotation(yaw, pitch), 360.0F, 360.0F, 45.0F, 45.0F, 20, 1);
   }

   private float G(float delta, boolean sensorNoise) {
      float gcd = GCDUtil.a();
      if (gcd > 0.0F && Float.isFinite(gcd)) {
         if (sensorNoise && Math.abs(delta) > gcd * 4.0F) {
            float chance = this.rng.nextFloat();
            if (chance < 0.07F) {
               delta -= Math.signum(delta) * gcd;
            } else if (chance < 0.1F) {
               delta += Math.signum(delta) * (gcd * (1.0F + this.rng.nextFloat()));
            }
         }

         return GCDUtil.a(delta);
      } else {
         return delta;
      }
   }

   private static enum a {
      PURSUIT,
      FLICK,
      SETTLE;
   }
}

package aethereal.module.combat.rotation;

import aethereal.core.Westra;
import aethereal.module.combat.AuraUtil;
import aethereal.util.GCDUtil;
import aethereal.util.Rotation;
import net.minecraft.class_1309;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_5498;

public final class LegendsRotation extends RotationBase {
   private float a;
   private boolean b;

   @Override
   public void a(class_1309 target, Rotation angle, int ticksToAttack) {
      if (aM_.field_1724 != null && target != null && angle != null) {
         if (aM_.field_1724.method_6128()) {
            this.q(target, angle);
         } else {
            this.r(target, angle);
         }
      }
   }

   private void q(class_1309 target, Rotation angle) {
      boolean bothGliding = target.method_6128();
      float riseLimit = bothGliding ? 0.17F : 0.22F;
      float fallLimit = bothGliding ? -0.02F : -0.01F;
      if (this.b) {
         if (this.a >= fallLimit) {
            float decel = Math.abs(class_3532.method_15393(angle.c() - this.lastYaw)) > 80.0F ? 0.1F : 0.001F + this.rng.nextFloat() * 0.004F;
            this.a = this.a - decel * (0.94F + this.rng.nextFloat() * 0.12F);
         }

         if (this.a <= fallLimit) {
            this.b = false;
         }
      } else {
         float accel = bothGliding ? 0.005F + this.rng.nextFloat() * 0.007F : 0.001F + this.rng.nextFloat() * 0.006F;
         this.a = this.a + accel * (0.9F + this.rng.nextFloat() * 0.2F);
         if (this.a >= riseLimit || AuraUtil.a(this.lastYaw, this.lastPitch, 3.0, target, true)) {
            this.b = true;
         }
      }

      float smooth = class_3532.method_15363(class_3532.method_15363(this.a, 0.0F, 1.0F) * 1.55F, 0.08F, 1.0F);
      float step = this.u(5.0F) ? 360.0F : 120.0F;
      this.s(angle, this.t(smooth), step, step);
   }

   private void r(class_1309 target, Rotation angle) {
      double distance = aM_.field_1724.method_5739(target);
      float yawDiff = Math.abs(class_3532.method_15393(angle.c() - this.lastYaw));
      float constraint = (distance < 0.7 || yawDiff > 85.0F) && yawDiff > 40.0F ? 0.3F : 1.0F;
      if (this.b) {
         if (this.a >= -0.01F) {
            this.a = this.a - 0.007F * (0.93F + this.rng.nextFloat() * 0.14F);
         }

         if (this.a <= -0.01F) {
            this.b = false;
         }
      } else {
         float multiplier = class_3532.method_15363(yawDiff / 25.0F, 0.5F, 1.0F);
         this.a = this.a + 0.0035F * multiplier * (0.9F + this.rng.nextFloat() * 0.2F);
         if (this.a >= 0.12F) {
            this.b = true;
         }
      }

      float smooth = class_3532.method_15363(this.t(class_3532.method_15363(this.a, 0.0F, 1.0F)), 0.08F, 1.0F) * constraint;
      this.s(angle, smooth, constraint < 1.0F ? 45.0F : 110.0F, this.u(3.0F) ? 360.0F : 0.0F);
   }

   private float t(float value) {
      return aM_.field_1724.field_6235 <= 0 || !aM_.field_1724.field_6007 && !(aM_.field_1724.method_18798().method_1027() > 0.001) ? value : value * 0.45F;
   }

   private void s(Rotation angle, float smooth, float speed, float reset) {
      float microYaw = (this.rng.nextFloat() - 0.5F) * 0.018F;
      float microPitch = (this.rng.nextFloat() - 0.5F) * 0.009F;
      float newYaw = this.lastYaw + class_3532.method_15393(angle.c() - this.lastYaw) * smooth + microYaw;
      float rawPitch = angle.d() - this.lastPitch;
      float steps = (float)Math.floor(Math.abs(rawPitch) / 0.72F);
      float newPitch = this.lastPitch + steps * 0.72F * Math.signum(rawPitch) * smooth + microPitch;
      float gcd = GCDUtil.a();
      if (gcd > 0.0F) {
         newYaw -= (newYaw - this.lastYaw) % gcd;
         newPitch -= (newPitch - this.lastPitch) % gcd;
      }

      newPitch = class_3532.method_15363(newPitch, -89.0F, 89.0F);
      this.lastYaw = newYaw;
      this.lastPitch = newPitch;
      Westra.h().d().k().a(new Rotation(newYaw, newPitch), speed, speed, reset, reset, 0, 1);
   }

   private boolean u(float tolerance) {
      class_4184 camera = aM_.field_1773.method_19418();
      float cameraYaw = camera.method_19330();
      float cameraPitch = camera.method_19329();
      if (aM_.field_1690.method_31044() == class_5498.field_26666) {
         cameraYaw -= 180.0F;
         cameraPitch = -cameraPitch;
      }

      float deltaYaw = class_3532.method_15393(cameraYaw - this.lastYaw);
      float deltaPitch = class_3532.method_15393(cameraPitch - this.lastPitch);
      return Math.abs(deltaYaw) < tolerance && Math.abs(deltaPitch) < tolerance;
   }

   @Override
   public void b() {
      this.a = 0.0F;
      this.b = false;
      super.b();
   }
}

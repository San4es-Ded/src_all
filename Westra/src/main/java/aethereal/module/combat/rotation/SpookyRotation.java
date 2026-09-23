package aethereal.module.combat.rotation;

import aethereal.core.Westra;
import aethereal.module.combat.AuraUtil;
import aethereal.util.GCDUtil;
import aethereal.util.Rotation;
import net.minecraft.class_1309;
import net.minecraft.class_3532;

public final class SpookyRotation extends RotationBase {
   private class_1309 a;
   private float b;
   private float c;
   private float d;
   private float e;
   private float f;
   private float g;
   private float h;
   private float i;
   private float j;
   private int k;

   @Override
   public void a(class_1309 target, Rotation angle, int ticksToAttack) {
      if (aM_.field_1724 != null && target != null && angle != null) {
         if (this.a != target) {
            this.a = target;
            this.b = this.rng.nextFloat() * (float) Math.PI * 2.0F;
            this.c = this.rng.nextFloat() * (float) Math.PI * 2.0F;
            this.h = 0.1F;
            this.q();
         }

         float yawError = class_3532.method_15393(angle.c() - this.lastYaw);
         float pitchError = angle.d() - this.lastPitch;
         float totalError = (float)Math.hypot(yawError, pitchError);
         boolean traced = AuraUtil.a(this.lastYaw, this.lastPitch, 999.0, target, true);
         if (--this.k <= 0) {
            this.q();
         }

         this.b = this.b + this.d;
         this.c = this.c + this.e;
         float waveYaw = (float)(Math.sin(this.b) * Math.cos(this.b * 0.618F)) * this.f;
         float wavePitch = (float)(Math.cos(this.c) * Math.sin(this.c * 1.414F)) * this.g;
         float targetFactor = traced ? 0.22F + this.rng.nextFloat() * 0.15F : 0.45F + this.rng.nextFloat() * 0.35F;
         if (totalError > 45.0F) {
            targetFactor *= 1.4F;
         }

         this.h = this.h + (targetFactor - this.h) * 0.25F;
         float rawYawStep = (yawError + waveYaw) * this.h;
         float rawPitchStep = (pitchError + wavePitch) * this.h * (aM_.field_1724.method_24828() ? 0.85F : 0.5F);
         float maxYawRate = 12.0F + (float)Math.sin(this.b) * 4.0F;
         float maxPitchRate = 7.0F + (float)Math.cos(this.c) * 2.5F;
         float yawStep = class_3532.method_15363(rawYawStep, -maxYawRate, maxYawRate);
         float pitchStep = class_3532.method_15363(rawPitchStep, -maxPitchRate, maxPitchRate);
         if (Math.abs(yawStep - this.i) < 0.008F) {
            yawStep += (this.rng.nextFloat() - 0.5F) * 0.04F;
         }

         if (Math.abs(pitchStep - this.j) < 0.008F) {
            pitchStep += (this.rng.nextFloat() - 0.5F) * 0.03F;
         }

         yawStep = GCDUtil.a(yawStep);
         pitchStep = GCDUtil.a(pitchStep);
         if (Math.abs(yawStep) > Math.abs(yawError) && Math.signum(yawStep) == Math.signum(yawError)) {
            yawStep = GCDUtil.a(yawError);
         }

         if (Math.abs(pitchStep) > Math.abs(pitchError) && Math.signum(pitchStep) == Math.signum(pitchError)) {
            pitchStep = GCDUtil.a(pitchError);
         }

         this.lastYaw = class_3532.method_15393(this.lastYaw + yawStep);
         this.lastPitch = class_3532.method_15363(this.lastPitch + pitchStep, -89.0F, 89.0F);
         this.i = yawStep;
         this.j = pitchStep;
         Westra.h().d().k().a(new Rotation(this.lastYaw, this.lastPitch), 360.0F, 360.0F, 360.0F, 360.0F, 0, 1);
      }
   }

   private void q() {
      this.k = 4 + this.rng.nextInt(7);
      this.d = 0.15F + this.rng.nextFloat() * 0.35F;
      this.e = 0.12F + this.rng.nextFloat() * 0.28F;
      this.f = (this.rng.nextFloat() - 0.5F) * 1.4F;
      this.g = (this.rng.nextFloat() - 0.5F) * 0.8F;
   }

   @Override
   public void b() {
      this.a = null;
      super.b();
   }
}

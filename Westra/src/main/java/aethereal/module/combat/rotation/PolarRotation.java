package aethereal.module.combat.rotation;

import aethereal.core.Westra;
import aethereal.util.Rotation;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public final class PolarRotation extends RotationBase {
   private static final float MAX_YAW_STEP = 42.0F;
   private static final float MAX_PITCH_STEP = 16.0F;
   private class_1309 target;
   private float velocityYaw;
   private float velocityPitch;
   private float tremorYaw;
   private float tremorPitch;
   private float aimOffsetX;
   private float aimOffsetY;
   private long nextAimShift;
   private float fov = 360.0F;

   public void a(float fov) {
      this.fov = fov;
   }

   @Override
   public void a(class_1309 entity, Rotation angle, int ticksToAttack) {
      if (aM_.field_1724 != null && entity != null) {
         if (entity != this.target) {
            this.target = entity;
            this.velocityYaw = 0.0F;
            this.velocityPitch = 0.0F;
            this.nextAimShift = 0L;
         }

         boolean canAttack = ticksToAttack <= 1;
         ThreadLocalRandom random = ThreadLocalRandom.current();
         class_243 velocity = entity.method_18798();
         double lead = 0.5;
         class_238 box = entity.method_5829().method_989(velocity.field_1352 * lead, velocity.field_1351 * lead * 0.5, velocity.field_1350 * lead);
         long now = System.currentTimeMillis();
         if (now >= this.nextAimShift) {
            this.nextAimShift = now + random.nextInt(240, 620);
            float halfWidth = (float)(box.field_1320 - box.field_1323) * 0.5F;
            this.aimOffsetX = random(-halfWidth * 0.4F, halfWidth * 0.4F);
            this.aimOffsetY = random(0.35F, 0.75F);
         }

         float shrink = canAttack ? 0.35F : 1.0F;
         class_243 aimPoint = new class_243(
            (box.field_1323 + box.field_1320) * 0.5 + this.aimOffsetX * shrink,
            box.field_1322 + (box.field_1325 - box.field_1322) * class_3532.method_16439(shrink, this.aimOffsetY, 0.6F),
            (box.field_1321 + box.field_1324) * 0.5 + this.aimOffsetX * 0.5 * shrink
         );
         class_243 aim = aimPoint.method_1020(aM_.field_1724.method_33571());
         float aimYaw = (float)Math.toDegrees(Math.atan2(-aim.field_1352, aim.field_1350));
         float aimPitch = (float)class_3532.method_15350(-Math.toDegrees(Math.atan2(aim.field_1351, Math.hypot(aim.field_1352, aim.field_1350))), -90.0, 90.0);
         float yawDelta = class_3532.method_15393(aimYaw - aM_.field_1724.method_36454());
         float pitchDelta = aimPitch - aM_.field_1724.method_36455();
         if ((float)Math.hypot(yawDelta, pitchDelta) > this.fov) {
            this.velocityYaw *= 0.6F;
            this.velocityPitch *= 0.6F;
         } else {
            float tremor = canAttack ? 0.15F : 0.45F;
            this.tremorYaw = this.tremorYaw + (random(-tremor, tremor) - this.tremorYaw) * 0.35F;
            this.tremorPitch = this.tremorPitch + (random(-tremor * 0.7F, tremor * 0.7F) - this.tremorPitch) * 0.35F;
            float desiredYaw = class_3532.method_15363(Math.abs(yawDelta) * 0.55F, 1.2F, 42.0F);
            float desiredPitch = class_3532.method_15363(Math.abs(pitchDelta) * 0.5F, 0.8F, 16.0F);
            this.velocityYaw = this.velocityYaw + (desiredYaw - this.velocityYaw) * 0.5F;
            this.velocityPitch = this.velocityPitch + (desiredPitch - this.velocityPitch) * 0.5F;
            float noise = random(0.92F, 1.08F);
            Rotation targetRotation = new Rotation(aimYaw + this.tremorYaw, class_3532.method_15363(aimPitch + this.tremorPitch, -90.0F, 90.0F));
            float returnSpeed = random(30.0F, 40.0F);
            Westra.h().d().k().a(targetRotation, this.velocityYaw * noise, this.velocityPitch * noise, returnSpeed, returnSpeed, random.nextInt(2, 5), 15);
            this.lastYaw = targetRotation.c();
            this.lastPitch = targetRotation.d();
         }
      }
   }

   private static float random(float min, float max) {
      return min + ThreadLocalRandom.current().nextFloat() * (max - min);
   }

   @Override
   public void b() {
      this.target = null;
      this.velocityYaw = 0.0F;
      this.velocityPitch = 0.0F;
      this.tremorYaw = 0.0F;
      this.tremorPitch = 0.0F;
      this.nextAimShift = 0L;
      super.b();
   }
}

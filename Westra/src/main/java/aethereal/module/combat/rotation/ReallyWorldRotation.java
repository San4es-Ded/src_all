package aethereal.module.combat.rotation;

import aethereal.core.Westra;
import aethereal.util.GCDUtil;
import aethereal.util.Rotation;
import net.minecraft.class_1309;
import net.minecraft.class_3532;

public final class ReallyWorldRotation extends RotationBase {
   @Override
   public void a(class_1309 target, Rotation angle, int ticksToAttack) {
      if (aM_.field_1724 != null && angle != null) {
         double tick = aM_.field_1724.field_6012;
         float yawShake = (float)(Math.sin(tick * 0.82) * 0.2 + Math.sin(tick * 0.31 + 1.7) * 0.04);
         float pitchShake = (float)(Math.cos(tick * 0.67 + 0.4) * 0.13);
         float yaw = angle.c() + yawShake;
         float pitch = class_3532.method_15363(angle.d() + pitchShake, -89.0F, 89.0F);
         float gcd = GCDUtil.a();
         if (gcd > 0.0F) {
            yaw -= (yaw - this.lastYaw) % gcd;
            pitch -= (pitch - this.lastPitch) % gcd;
         }

         this.lastYaw = yaw;
         this.lastPitch = pitch;
         boolean gliding = aM_.field_1724.method_6128();
         float speed = gliding ? 1440.0F : 360.0F;
         Westra.h().d().k().a(new Rotation(yaw, pitch), speed, speed, speed, speed, 0, gliding ? 3 : 1);
      }
   }
}

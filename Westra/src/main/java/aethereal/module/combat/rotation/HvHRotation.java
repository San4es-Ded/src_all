package aethereal.module.combat.rotation;

import aethereal.core.Westra;
import aethereal.util.GCDUtil;
import aethereal.util.Rotation;
import net.minecraft.class_1309;
import net.minecraft.class_3532;

public final class HvHRotation extends RotationBase {
   @Override
   public void a(class_1309 target, Rotation angle, int ticksToAttack) {
      if (aM_.field_1724 != null && angle != null) {
         float yaw = this.lastYaw + class_3532.method_15393(angle.c() - this.lastYaw);
         float pitch = this.lastPitch + (angle.d() - this.lastPitch);
         float gcd = GCDUtil.a();
         if (gcd > 0.0F) {
            yaw -= (yaw - this.lastYaw) % gcd;
            pitch -= (pitch - this.lastPitch) % gcd;
         }

         pitch = class_3532.method_15363(pitch, -89.0F, 89.0F);
         this.lastYaw = yaw;
         this.lastPitch = pitch;
         Westra.h().d().k().a(new Rotation(yaw, pitch), 360.0F, 360.0F, 360.0F, 360.0F, 0, 1);
      }
   }
}

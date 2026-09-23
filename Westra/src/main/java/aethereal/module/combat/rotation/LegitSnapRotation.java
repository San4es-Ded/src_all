package aethereal.module.combat.rotation;

import aethereal.core.Westra;
import aethereal.util.MathUtil;
import aethereal.util.Rotation;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public final class LegitSnapRotation extends RotationBase {
   private float a = 30.0F;

   public void a(float fov) {
      this.a = fov;
   }

   @Override
   public void a(class_1309 target, Rotation angle, int ticksToAttack) {
      if (aM_.field_1724 != null && target != null && angle != null) {
         boolean attack = ticksToAttack <= 0;
         float yaw = aM_.field_1724.method_36454();
         float pitch = aM_.field_1724.method_36455();
         if (attack && this.q(target, this.a) && this.r(angle.c(), angle.d()) <= this.a) {
            yaw = angle.c();
            pitch = angle.d();
         }

         this.lastYaw = yaw;
         this.lastPitch = pitch;
         Westra.h()
            .d()
            .k()
            .a(new Rotation(yaw, pitch), MathUtil.a(150.0F, 180.0F), MathUtil.a(150.0F, 180.0F), MathUtil.a(150.0F, 180.0F), MathUtil.a(150.0F, 180.0F), 0, 1);
      }
   }

   private boolean q(class_1309 target, float halfFov) {
      class_243 eyes = aM_.field_1724.method_33571();
      class_238 box = target.method_5829();
      double centerX = (box.field_1323 + box.field_1320) / 2.0;
      double centerZ = (box.field_1321 + box.field_1324) / 2.0;
      double[] heights = new double[]{box.field_1322, (box.field_1322 + box.field_1325) / 2.0, box.field_1325};

      for (double height : heights) {
         class_243 toPoint = new class_243(centerX, height, centerZ).method_1020(eyes);
         double length = toPoint.method_1033();
         if (length < 1.0E-4) {
            return true;
         }

         class_243 direction = toPoint.method_1021(1.0 / length);
         float pointYaw = (float)Math.toDegrees(Math.atan2(-direction.field_1352, direction.field_1350));
         float pointPitch = (float)(-Math.toDegrees(Math.atan2(direction.field_1351, Math.hypot(direction.field_1352, direction.field_1350))));
         float deltaYaw = Math.abs(class_3532.method_15393(pointYaw - aM_.field_1724.method_36454()));
         float deltaPitch = Math.abs(class_3532.method_15393(pointPitch - aM_.field_1724.method_36455()));
         if (deltaYaw <= halfFov && deltaPitch <= halfFov) {
            return true;
         }
      }

      return false;
   }

   private float r(float targetYaw, float targetPitch) {
      float yawDiff = Math.abs(class_3532.method_15393(targetYaw - aM_.field_1724.method_36454()));
      float pitchDiff = Math.abs(class_3532.method_15393(targetPitch - aM_.field_1724.method_36455()));
      return (float)Math.hypot(yawDiff, pitchDiff);
   }
}

package aethereal.handler;

import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.util.NeuroData;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_3532;

@Handler_2
public class NeuroRecordHandler extends BaseHandler implements Interface {
   private float a;
   private float b;
   private boolean c;

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 == null || aM_.field_1687 == null || !NeuroData.a().b()) {
         this.c = false;
      } else if (Westra.h().d().t().B().m()) {
         this.c = false;
      } else {
         float yaw = aM_.field_1724.method_36454();
         float pitch = aM_.field_1724.method_36455();
         if (!this.c) {
            this.a = yaw;
            this.b = pitch;
            this.c = true;
         } else {
            float yawStep = class_3532.method_15393(yaw - this.a);
            float pitchStep = pitch - this.b;
            this.a = yaw;
            this.b = pitch;
            class_1309 target = a();
            if (target != null) {
               class_243 eye = aM_.field_1724.method_33571();
               class_243 point = target.method_5829().method_1005();
               double difference = point.field_1352 - eye.field_1352;
               double height = point.field_1351 - eye.field_1351;
               double depth = point.field_1350 - eye.field_1350;
               float targetYaw = (float)class_3532.method_15338(Math.toDegrees(Math.atan2(depth, difference)) - 90.0);
               float targetPitch = (float)(-Math.toDegrees(Math.atan2(height, Math.hypot(difference, depth))));
               float deltaYaw = class_3532.method_15393(targetYaw - (yaw - yawStep));
               float deltaPitch = targetPitch - (pitch - pitchStep);
               NeuroData.a().a(deltaYaw, deltaPitch, aM_.field_1724.method_5739(target), yawStep, pitchStep);
            }
         }
      }
   }

   private static class_1309 a() {
      class_1309 best = null;
      double bestDistance = 7.0;

      for (class_1657 player : aM_.field_1687.method_18456()) {
         if (player != aM_.field_1724 && player.method_5805() && !player.method_7325() && !Westra.h().d().e().d(player.method_5477().getString())) {
            double distance = aM_.field_1724.method_5739(player);
            if (distance < bestDistance) {
               bestDistance = distance;
               best = player;
            }
         }
      }

      return best;
   }
}

package aethereal.module.combat;

import aethereal.core.Interface;
import java.util.ArrayList;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.class_1294;
import net.minecraft.class_1802;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_3486;
import net.minecraft.class_3610;
import net.minecraft.class_746;
import platform.inject.accessors.LivingEntityGravityInvoker;
import platform.inject.invokers.EntityCollisionPredictionInvoker;
import platform.inject.invokers.EntityMovementInvoker;

public class MaceUtil implements Interface {
   @Generated
   private MaceUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static boolean a() {
      return aM_.field_1724 != null && aM_.field_1724.method_6047().method_31574(class_1802.field_49814);
   }

   public static Optional<class_243> a(class_746 player, class_1937 world) {
      if (world == null || player.method_24828()) {
         return Optional.empty();
      } else if (player.method_31549().field_7479 || player.method_6128() || player.method_5765() || player.method_6101()) {
         return Optional.empty();
      } else if (player.method_6059(class_1294.field_5902)) {
         return Optional.empty();
      } else if (!player.method_5799() && !player.method_5771()) {
         LivingEntityGravityInvoker gravitySrc = (LivingEntityGravityInvoker)player;
         double gravity = gravitySrc.getGravityInvoker();
         class_238 baseBox = player.method_5829();
         double ox = 0.0;
         double oy = 0.0;
         double oz = 0.0;
         class_243 velocity = player.method_18798();
         ArrayList<class_265> shapes = new ArrayList<>();

         for (int tick = 0; tick < 400; tick++) {
            double vx = velocity.field_1352 * 0.9799995613681012;
            double vy = (velocity.field_1351 - gravity) * 0.9799995613681012;
            double vz = velocity.field_1350 * 0.9799995613681012;
            class_243 step = new class_243(vx, vy, vz);
            class_238 simBox = baseBox.method_989(ox, oy, oz);
            shapes.clear();
            EntityCollisionPredictionInvoker.findCollisionsForMovement(player, world, shapes, simBox);
            class_243 allowed = EntityCollisionPredictionInvoker.adjustMovementForCollisions(player, step, simBox, world, shapes);
            if (step.field_1351 < -9.999995680744685E-5 && allowed.field_1351 > step.field_1351 + 9.999995999686725E-5) {
               return Optional.of(player.method_19538().method_1031(ox + allowed.field_1352, oy + allowed.field_1351, oz + allowed.field_1350));
            }

            ox += allowed.field_1352;
            oy += allowed.field_1351;
            oz += allowed.field_1350;
            if (a(world, baseBox.method_989(ox, oy, oz))) {
               return Optional.of(player.method_19538().method_1031(ox, oy, oz));
            }

            if (Math.abs(allowed.field_1352 - step.field_1352) > 9.999995999686725E-5) {
               vx = 0.0;
            }

            if (Math.abs(allowed.field_1351 - step.field_1351) > 9.999995999686725E-5) {
               vy = 0.0;
            }

            if (Math.abs(allowed.field_1350 - step.field_1350) > 9.999995999686725E-5) {
               vz = 0.0;
            }

            velocity = new class_243(vx, vy, vz);
            if (velocity.method_1027() < 1.0000000090069629E-12 && step.field_1351 >= -9.999995680744685E-5) {
               break;
            }
         }

         return Optional.empty();
      } else {
         return Optional.empty();
      }
   }

   public static boolean b() {
      if (aM_.field_1687 == null) {
         return false;
      } else {
         class_746 player = aM_.field_1724;
         if (player == null) {
            return false;
         } else {
            LivingEntityGravityInvoker gravity = (LivingEntityGravityInvoker)player;
            EntityMovementInvoker collider = (EntityMovementInvoker)player;
            double offsetY = 0.0;
            class_243 velocity = player.method_18798();

            for (int tick = 0; tick < 2; tick++) {
               double nextVy = (velocity.field_1351 - gravity.getGravityInvoker()) * 0.9799995613681012;
               class_243 step = new class_243(0.0, nextVy, 0.0);
               class_238 box = player.method_5829().method_989(0.0, offsetY, 0.0);
               class_243 allowed;
               if (tick == 0 && offsetY == 0.0) {
                  allowed = collider.getAdjustMovementForCollisions(step);
               } else {
                  ArrayList<class_265> shapes = new ArrayList<>();
                  EntityCollisionPredictionInvoker.findCollisionsForMovement(player, aM_.field_1687, shapes, box);
                  allowed = EntityCollisionPredictionInvoker.adjustMovementForCollisions(player, step, box, aM_.field_1687, shapes);
               }

               if (nextVy < 0.0 && b(aM_.field_1687, box.method_989(allowed.field_1352, allowed.field_1351, allowed.field_1350))) {
                  return true;
               }

               offsetY += allowed.field_1351;
               velocity = new class_243(velocity.field_1352, allowed.field_1351, velocity.field_1350);
            }

            return false;
         }
      }
   }

   private static boolean a(class_1937 world, class_238 box) {
      for (class_2338 pos : class_2338.method_10097(
         class_2338.method_49637(box.field_1323, box.field_1322, box.field_1321), class_2338.method_49637(box.field_1320, box.field_1325, box.field_1324)
      )) {
         class_3610 fluid = world.method_8320(pos).method_26227();
         if (!fluid.method_15769() && (fluid.method_15767(class_3486.field_15517) || fluid.method_15767(class_3486.field_15518))) {
            return true;
         }
      }

      return false;
   }

   private static boolean b(class_1937 world, class_238 box) {
      class_2338 min = class_2338.method_49637(box.field_1323, box.field_1322, box.field_1321);
      class_2338 max = class_2338.method_49637(box.field_1320, box.field_1325, box.field_1324);

      for (class_2338 pos : class_2338.method_10097(min, max)) {
         class_2680 state = world.method_8320(pos);
         if (!state.method_27852(class_2246.field_10343) && !state.method_27852(class_2246.field_16999)) {
            class_3610 fluid = state.method_26227();
            if (fluid.method_15769() || !fluid.method_15767(class_3486.field_15517) && !fluid.method_15767(class_3486.field_15518)) {
               continue;
            }

            return true;
         }

         return true;
      }

      return false;
   }
}

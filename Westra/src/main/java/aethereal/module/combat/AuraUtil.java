package aethereal.module.combat;

import aethereal.core.Interface;
import aethereal.handler.RotationProcessor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3486;
import net.minecraft.class_3532;
import net.minecraft.class_3610;
import net.minecraft.class_3959;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public class AuraUtil implements Interface {
   @Generated
   private AuraUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static double a(class_243 eye, class_1297 entity) {
      class_238 box = entity.method_5829();
      double cx = class_3532.method_15350(eye.field_1352, box.field_1323, box.field_1320);
      double cy = class_3532.method_15350(eye.field_1351, box.field_1322, box.field_1325);
      double cz = class_3532.method_15350(eye.field_1350, box.field_1321, box.field_1324);
      double dx = cx - eye.field_1352;
      double dy = cy - eye.field_1351;
      double dz = cz - eye.field_1350;
      return dx * dx + dy * dy + dz * dz;
   }

   public static double a(class_1297 entity) {
      return aM_.field_1724 == null ? Double.POSITIVE_INFINITY : a(aM_.field_1724.method_33571(), entity);
   }

   public static boolean a(class_1297 entity, double maxReach) {
      return a(entity) <= maxReach * maxReach;
   }

   public static boolean a(class_1309 entity, double distance) {
      class_243 eye = aM_.field_1724.method_33571();
      class_238 box = entity.method_5829();
      double cx = class_3532.method_15350(eye.field_1352, box.field_1323, box.field_1320);
      double cy = class_3532.method_15350(eye.field_1351, box.field_1322, box.field_1325);
      double cz = class_3532.method_15350(eye.field_1350, box.field_1321, box.field_1324);
      class_243 d = new class_243(cx - eye.field_1352, cy - eye.field_1351, cz - eye.field_1350);
      float yaw = (float)class_3532.method_15338(Math.toDegrees(Math.atan2(d.field_1350, d.field_1352)) - 90.0);
      float pitch = (float)(-Math.toDegrees(Math.atan2(d.field_1351, Math.hypot(d.field_1352, d.field_1350))));
      return a(yaw, pitch, distance, entity, true);
   }

   public static boolean a(float yaw, float pitch, double distance, class_1297 entity, boolean throwalls) {
      return aM_.field_1724 != null && aM_.field_1687 != null ? a(aM_.field_1724.method_33571(), yaw, pitch, distance, entity, throwalls) : false;
   }

   public static boolean a(class_243 rayOrigin, float yaw, float pitch, double distance, class_1297 entity, boolean throwalls) {
      if (aM_.field_1724 != null && aM_.field_1687 != null) {
         class_243 dir = class_243.method_1030(pitch, yaw).method_1021(distance);
         Optional<class_243> opt = entity.method_5829().method_1006(rayOrigin)
            ? Optional.of(rayOrigin)
            : entity.method_5829().method_992(rayOrigin, rayOrigin.method_1019(dir));
         return opt.isEmpty()
            ? false
            : throwalls
               || aM_.field_1687
                     .method_17742(new class_3959(rayOrigin, opt.get(), class_3960.field_17559, class_242.field_1348, aM_.field_1724))
                     .method_17783()
                  == class_240.field_1333;
      } else {
         return false;
      }
   }

   public static boolean a(class_243 from, class_1309 entity, double reach) {
      class_238 bb = entity.method_5829();
      double[] t = new double[]{0.0, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0};
      int last = t.length - 1;
      double reachSq = reach * reach;

      for (int a = 0; a <= last; a++) {
         for (int b = 0; b <= last; b++) {
            for (int c = 0; c <= last; c++) {
               if (a <= 0 || a >= last || b <= 0 || b >= last || c <= 0 || c >= last) {
                  class_243 point = new class_243(
                     class_3532.method_16436(t[a], bb.field_1323, bb.field_1320),
                     class_3532.method_16436(t[b], bb.field_1322, bb.field_1325),
                     class_3532.method_16436(t[c], bb.field_1321, bb.field_1324)
                  );
                  double distSq = from.method_1025(point);
                  if (!(distSq > reachSq)) {
                     class_243 end = point.method_1019(from.method_1020(point).method_1021(0.05000000993895991 / Math.sqrt(distSq)));
                     if (aM_.field_1687.method_17742(new class_3959(from, end, class_3960.field_17559, class_242.field_1348, aM_.field_1724)).method_17783()
                        == class_240.field_1333) {
                        return true;
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   public static boolean a(int ticks, class_1309 target, boolean checks) {
      return !checks && ticks >= 7 && a(target, 3.0) && aM_.field_1724.method_7261(0.5F) > 0.7F ? a() : false;
   }

   public static boolean a() {
      if (aM_.field_1724 != null && aM_.field_1687 != null) {
         double dy = (aM_.field_1724.method_18798().field_1351 - 0.08000000049877275) * 0.9799995837206814;
         if (dy >= 0.0) {
            return false;
         } else {
            class_238 moved = aM_.field_1724.method_5829().method_989(0.0, dy, 0.0);
            class_238 feet = new class_238(
               moved.field_1323, moved.field_1322 - 0.010000001417203743, moved.field_1321, moved.field_1320, moved.field_1322, moved.field_1324
            );
            return aM_.field_1687.method_52569(aM_.field_1724, feet);
         }
      } else {
         return false;
      }
   }

   public static class_243 a(class_243 eye, class_1309 target, double reach, boolean throughWalls) {
      class_238 bb = target.method_5829();
      boolean mace = MaceUtil.a();
      class_243 aimEye = mace && aM_.field_1724 != null ? eye.method_1019(aM_.field_1724.method_18798()) : eye;
      double mx = (bb.field_1323 + bb.field_1320) * 0.5;
      double mz = (bb.field_1321 + bb.field_1324) * 0.5;
      class_243 targetEye = target.method_19538().method_1031(0.0, target.method_5751(), 0.0);
      double distToTargetEye = aimEye.method_1022(targetEye);
      class_243 aimOrigin = aimEye;
      if (mace && distToTargetEye > 3.0) {
         aimOrigin = new class_243(aimEye.field_1352, targetEye.field_1351, aimEye.field_1350);
      }

      double blendDist = mace ? Math.min(distToTargetEye, 3.0) : distToTargetEye;
      double aimHeight = aimEye.field_1351;
      if (mace && distToTargetEye > 3.0) {
         aimHeight = targetEye.field_1351;
      }

      double ay = class_3532.method_16436(
         class_3532.method_15350(blendDist / 3.0, 0.0, 1.0), bb.field_1322, class_3532.method_15350(aimHeight, bb.field_1322, bb.field_1325)
      );
      class_243 ideal = new class_243(mx, ay, mz);
      List<class_243> pts = new ArrayList<>();
      pts.add(ideal);
      double[] t = new double[]{0.0, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0};
      int last = t.length - 1;

      for (int a = 0; a < t.length; a++) {
         for (int b = 0; b < t.length; b++) {
            for (int c = 0; c < t.length; c++) {
               if (a == 0 || a == last || b == 0 || b == last || c == 0 || c == last) {
                  pts.add(
                     new class_243(
                        class_3532.method_16436(t[a], bb.field_1323, bb.field_1320),
                        class_3532.method_16436(t[b], bb.field_1322, bb.field_1325),
                        class_3532.method_16436(t[c], bb.field_1321, bb.field_1324)
                     )
                  );
               }
            }
         }
      }

      for (double pad : new double[]{0.0, 0.20000001551382535}) {
         List<class_243> visible = new ArrayList<>();

         for (class_243 p : pts) {
            class_243 d = p.method_1020(aimOrigin);
            double len = d.method_1033();
            double limit = reach + pad;
            if (mace || len <= limit) {
               float traceDist = (float)(mace ? len + pad + 0.010000001417203743 : limit);
               if (a(
                  aimOrigin,
                  (float)class_3532.method_15338(Math.toDegrees(Math.atan2(d.field_1350, d.field_1352)) - 90.0),
                  (float)(-Math.toDegrees(Math.atan2(d.field_1351, Math.hypot(d.field_1352, d.field_1350)))),
                  traceDist,
                  target,
                  false
               )) {
                  visible.add(p);
               }
            }
         }

         if (!visible.isEmpty()) {
            class_243 centroid = visible.stream().reduce(class_243.field_1353, (v0, v1) -> v0.method_1019(v1)).method_1021(1.0 / visible.size());
            return visible.stream().min(Comparator.comparingDouble(pt -> pt.method_1025(centroid))).get().method_1020(aimOrigin);
         }

         if (throughWalls) {
            List<class_243> through = new ArrayList<>();

            for (class_243 p2 : pts) {
               class_243 d2 = p2.method_1020(aimOrigin);
               double len2 = d2.method_1033();
               double limit2 = reach + pad;
               if (mace || len2 <= limit2) {
                  float traceDist2 = (float)(mace ? len2 + pad + 0.010000001417203743 : limit2);
                  if (a(
                     aimOrigin,
                     (float)class_3532.method_15338(Math.toDegrees(Math.atan2(d2.field_1350, d2.field_1352)) - 90.0),
                     (float)(-Math.toDegrees(Math.atan2(d2.field_1351, Math.hypot(d2.field_1352, d2.field_1350)))),
                     traceDist2,
                     target,
                     true
                  )) {
                     through.add(p2);
                  }
               }
            }

            if (!through.isEmpty()) {
               class_243 centroid2 = through.stream().reduce(class_243.field_1353, (v0, v1) -> v0.method_1019(v1)).method_1021(1.0 / through.size());
               return through.stream().min(Comparator.comparingDouble(pt2 -> pt2.method_1025(centroid2))).get().method_1020(aimOrigin);
            }
         }
      }

      return class_243.field_1353;
   }

   public static boolean b() {
      if (aM_.field_1724 != null && aM_.field_1724.method_37908() != null) {
         class_1937 world = aM_.field_1724.method_37908();
         class_2338 eye = class_2338.method_49638(aM_.field_1724.method_33571());
         class_3610 fluid = world.method_8316(eye);
         return !aM_.field_1724.method_6059(class_1294.field_5902)
            && !aM_.field_1724.method_6059(class_1294.field_5919)
            && !fluid.method_15767(class_3486.field_15517)
            && !fluid.method_15767(class_3486.field_15518)
            && !aM_.field_1724.method_31549().field_7479
            && !aM_.field_1724.method_6128()
            && !aM_.field_1724.method_6101()
            && !aM_.field_1724.method_5765();
      } else {
         return false;
      }
   }

   public static boolean c() {
      return aM_.field_1724 != null && b() && aM_.field_1724.field_6017 > 0.0F && !aM_.field_1724.method_24828();
   }

   public static float a(float start, float end, float amount) {
      float a = class_3532.method_15363(amount, 0.0F, 1.0F);
      float d = class_3532.method_15393(end - start);
      if (Math.abs(d) < 0.5F) {
         return end;
      } else {
         float stepped = class_3532.method_15393(start + d * a);
         float patched = RotationProcessor.a(start, stepped);
         float remaining = class_3532.method_15393(end - patched);
         return Math.abs(remaining) < 0.5F ? end : patched;
      }
   }
}

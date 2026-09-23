package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.GlobalEvent;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.HotbarEvent;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.handler.UseableHandler;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.util.Look;
import aethereal.util.MoveUtil;
import aethereal.util.Rotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2488;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2868;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import platform.inject.invokers.MinecraftClientInvoker;

@ModuleRegister(
   a = "Scaffold",
   b = "Автоматически ставит блоки под вами",
   c = Category.Movement
)
public class Scaffold extends Module {
   private Scaffold.a c;
   private final BooleanSetting b = new BooleanSetting("Избегать падения", false);
   private class_243 d = class_243.field_1353;
   private final int[] e = new int[]{-1, -1, -1};

   public Scaffold() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(InputEvent e) {
      if (!this.r()) {
         if (this.s() != null) {
            MoveUtil.a(e, Look.b(), 1);
            if (this.b.c()) {
               Westra.h().d().t().ah().b(e);
            }
         }

         if (!this.a(aM_.field_1724.method_6047()) && !this.a(aM_.field_1724.method_6079())) {
            int hotbarSlot = this.d(true);
            if (hotbarSlot != -1) {
               if (aM_.field_1724.method_31548().field_7545 != hotbarSlot && this.e[2] < 7 && Westra.h().d().v().a().a().isEmpty()) {
                  aM_.field_1724.method_31548().field_7545 = hotbarSlot;
                  this.e[2] = 9;
               }
            } else {
               int invSlot = this.d(false);
               if (this.e[2] < 5 && invSlot != -1 && Westra.h().d().v().a().a().isEmpty()) {
                  if (this.e[1] == -1) {
                     this.e[1] = invSlot;
                  }

                  if (aM_.field_1724.method_31548().field_7545 != 5) {
                     aM_.field_1724.method_31548().field_7545 = 5;
                  }

                  Westra.h().d().v().a().a(invSlot, 5, 1);
                  this.e[2] = 9;
               }
            }
         }
      }
   }

   @EventTarget
   public void a(HotbarEvent event) {
      if (this.e[2] > 5 && this.d(false) != -1) {
         event.a(true);
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.b() && event.d() instanceof class_2868) {
         this.e[2] = 9;
      }
   }

   @EventTarget
   public void a(TickEvent e) {
      int[] iArr = this.e;
      iArr[2]--;
   }

   @EventTarget
   public void a(GlobalEvent e) throws MatchException {
      if (this.s() != null && !this.r()) {
         if (this.c == null || !this.a(this.c)) {
            this.c = null;

            for (class_2338 target : this.q()) {
               this.c = this.b(target);
               if (this.c != null) {
                  break;
               }
            }
         }

         if (this.c != null) {
            float t = aM_.field_1724.field_6012 + aM_.method_61966().method_60637(false);
            float smoothYaw = (float)(Math.sin(t * 0.40000001611738834) * 3.0 + Math.sin(t * 0.950000126718632 + 1.4000003101900576) * 2.0) / 10.0F;
            float smoothPitch = (float)(Math.cos(t * 0.5 + 0.7000001047992626) * 0.5 + Math.cos(t * 0.7800000028540086 + 3.099999110838922) * 1.5) / 4.0F;
            Rotation rotation = this.b(this.c);
            rotation.a(rotation.c() + smoothYaw);
            rotation.b(rotation.d() + smoothPitch);
            Westra.h().d().k().a(rotation, 100.0F, 7, 1);
            if (this.u() && !this.v() && !Westra.h().d().v().c().a()) {
               ((MinecraftClientInvoker)aM_).invokeDoItemUse();
               this.e[2] = 9;
               this.c = null;
            }
         }
      }
   }

   private boolean a(Scaffold.a data) {
      class_2338 placePos = data.a.method_10093(data.b);
      return this.a(aM_.field_1687.method_8320(placePos)) && this.a(data.a) && !this.a(data.a, data.b).isEmpty();
   }

   private List<class_243> a(class_2338 pos, class_2350 face) throws MatchException {
      List<class_243> points = new ArrayList<>();
      class_243 eye = this.t();
      double reach = aM_.field_1724.method_55754();
      class_243 normal = new class_243(face.method_10148(), face.method_10164(), face.method_10165());
      double[] offsets = new double[]{
         0.0, -0.20000000236855192, 0.200000000060146, -0.3500000030268554, 0.3500000598673184, -0.4500000079401218, 0.4499998886196472
      };

      for (double u : offsets) {
         for (double v : offsets) {
            class_243 point = this.a(pos, face, u, v);
            if (eye.method_1022(point) <= reach && eye.method_1020(point).method_1029().method_1026(normal) > 0.1000000076546522) {
               class_3965 class_3965VarMethod_17742 = aM_.field_1687
                  .method_17742(new class_3959(eye, point, class_3960.field_17558, class_242.field_1348, aM_.field_1724));
               if (class_3965VarMethod_17742.method_17783() == class_240.field_1333) {
                  points.add(point);
               } else if (class_3965VarMethod_17742 instanceof class_3965 && class_3965VarMethod_17742.method_17777().equals(pos)) {
                  points.add(point);
               }
            }
         }
      }

      return points;
   }

   private List<class_2338> q() {
      List<class_2338> list = new ArrayList<>();
      this.d = this.d
         .method_1021(0.6000003608875443)
         .method_1019(
            new class_243(aM_.field_1724.method_23317() - aM_.field_1724.field_6014, 0.0, aM_.field_1724.method_23321() - aM_.field_1724.field_5969)
               .method_1021(0.200000000060146)
         );

      for (int i = 0; i <= 2; i++) {
         class_243 at = aM_.field_1724.method_19538().method_1019(this.d.method_1021(i));
         class_2338 pos = class_2338.method_49637(at.field_1352, aM_.field_1724.method_23318() - 1.0, at.field_1350);
         if (!list.contains(pos) && this.a(aM_.field_1687.method_8320(pos))) {
            list.add(pos);
         }
      }

      list.sort(Comparator.comparingDouble(pos2 -> pos2.method_19770(aM_.field_1724.method_19538())));
      return list;
   }

   private boolean r() {
      List<UseableHandler.a> tasks = Westra.h().d().v().b().a();
      return !tasks.isEmpty() && tasks.getFirst().d() <= 1;
   }

   @Override
   public void b() {
      super.b();
      this.c = null;
      this.d = class_243.field_1353;
      if (aM_.field_1724 != null) {
         this.e[0] = aM_.field_1724.method_31548().field_7545;
      }
   }

   @Override
   public void c() {
      super.c();
      if (this.e[0] != -1 && aM_.field_1724 != null) {
         aM_.field_1724.method_31548().field_7545 = this.e[0];
         if (this.e[1] != -1) {
            Westra.h().d().v().a().a(this.e[1], 5, 1);
         }
      }

      this.e[0] = -1;
      this.e[1] = -1;
      this.e[2] = -1;
      this.c = null;
      this.d = class_243.field_1353;
   }

   private int d(boolean hotbarOnly) {
      int end = hotbarOnly ? 9 : 36;

      for (int i = 0; i < end; i++) {
         if (this.a(aM_.field_1724.method_31548().method_5438(i))) {
            return i;
         }
      }

      return -1;
   }

   private boolean a(class_1799 stack) {
      return !stack.method_7960()
         && stack.method_7909() instanceof class_1747 class_1747VarMethod_7909
         && class_1747VarMethod_7909.method_7711().method_9564().method_26216();
   }

   private class_1268 s() {
      if (this.a(aM_.field_1724.method_6047())) {
         return class_1268.field_5808;
      } else {
         return this.a(aM_.field_1724.method_6079()) ? class_1268.field_5810 : null;
      }
   }

   private boolean a(class_2680 state) {
      return state.method_45474() ? true : state.method_26204() == class_2246.field_10477 && (Integer)state.method_11654(class_2488.field_11518) < 8;
   }

   private boolean a(class_2338 pos) {
      class_2680 state = aM_.field_1687.method_8320(pos);
      return !state.method_26215()
         && state.method_26204() != class_2246.field_10477
         && !state.method_45474()
         && !state.method_26220(aM_.field_1687, pos).method_1110();
   }

   private Scaffold.a b(class_2338 pos) throws MatchException {
      Scaffold.a data = this.c(pos);
      if (data != null) {
         return data;
      } else {
         int[][] offsets = new int[][]{
            {-1, 0, 0},
            {1, 0, 0},
            {0, 0, -1},
            {0, 0, 1},
            {-1, 0, -1},
            {1, 0, 1},
            {-1, 0, 1},
            {1, 0, -1},
            {0, -1, 0},
            {-1, -1, 0},
            {1, -1, 0},
            {0, -1, -1},
            {0, -1, 1}
         };
         class_243 feet = aM_.field_1724.method_19538();
         return Arrays.stream(offsets)
            .map(o -> pos.method_10069(o[0], o[1], o[2]))
            .sorted(Comparator.comparingDouble(p -> p.method_19770(feet)))
            .map(this::c)
            .filter(v0 -> Objects.nonNull(v0))
            .findFirst()
            .orElse(null);
      }
   }

   private Scaffold.a c(class_2338 pos) throws MatchException {
      if (!this.a(aM_.field_1687.method_8320(pos))) {
         return null;
      } else {
         Scaffold.a best = null;
         int bestCount = 0;

         for (class_2350 face : class_2350.values()) {
            class_2338 neighbor = pos.method_10093(face);
            if (this.a(neighbor)) {
               class_2350 placeFace = face.method_10153();
               List<class_243> points = this.a(neighbor, placeFace);
               if (points.size() > bestCount) {
                  bestCount = points.size();
                  best = new Scaffold.a(neighbor, placeFace, new class_3965(this.a(neighbor, placeFace, 0.0, 0.0), placeFace, neighbor, false));
               }
            }
         }

         return best;
      }
   }

   private Rotation b(Scaffold.a data) throws MatchException {
      class_243 eye = this.t();
      List<class_243> points = this.a(data.a, data.b);
      if (points.isEmpty()) {
         return Rotation.a(eye, data.c.method_17784());
      } else {
         class_243 center = points.stream().reduce(class_243.field_1353, (v0, v1) -> v0.method_1019(v1)).method_1021(1.0 / points.size());
         class_243 best = points.stream().min(Comparator.comparingDouble(point -> point.method_1025(center))).orElse(center);
         return Rotation.a(eye, best);
      }
   }

   private class_243 t() {
      class_243 eye = aM_.field_1724.method_33571();
      double fall = aM_.field_1724.method_18798().field_1351;
      return fall < 0.0 ? eye.method_1031(0.0, fall * 0.5, 0.0) : eye;
   }

   private class_238 d(class_2338 pos) {
      class_265 shape = aM_.field_1687.method_8320(pos).method_26220(aM_.field_1687, pos);
      return shape.method_1110() ? new class_238(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) : shape.method_1107();
   }

   private class_243 a(class_2338 pos, class_2350 face, double u, double v) throws MatchException {
      class_238 shape = this.d(pos);
      double cx = pos.method_10263() + (shape.field_1323 + shape.field_1320) / 2.0;
      double cy = pos.method_10264() + (shape.field_1322 + shape.field_1325) / 2.0;
      double cz = pos.method_10260() + (shape.field_1321 + shape.field_1324) / 2.0;
      double sx = shape.method_17939();
      double sy = shape.method_17940();
      double sz = shape.method_17941();
      switch (Scaffold.AnonymousClass1.a[face.ordinal()]) {
         case 1:
            return new class_243(cx + u * sx, pos.method_10264() + shape.field_1325, cz + v * sz);
         case 2:
            return new class_243(cx + u * sx, pos.method_10264() + shape.field_1322, cz + v * sz);
         case 3:
            return new class_243(cx + u * sx, cy + v * sy, pos.method_10260() + shape.field_1321);
         case 4:
            return new class_243(cx + u * sx, cy + v * sy, pos.method_10260() + shape.field_1324);
         case 5:
            return new class_243(pos.method_10263() + shape.field_1323, cy + v * sy, cz + u * sz);
         case 6:
            return new class_243(pos.method_10263() + shape.field_1320, cy + v * sy, cz + u * sz);
         default:
            throw new MatchException((String)null, (Throwable)null);
      }
   }

   private boolean u() {
      class_3965 class_3965Var = aM_.field_1765 instanceof class_3965 ? (class_3965)aM_.field_1765 : null;
      if (!(class_3965Var instanceof class_3965)) {
         return false;
      } else if (class_3965Var.method_17783() != class_240.field_1332) {
         return false;
      } else {
         class_2338 placePos = this.c.a.method_10093(this.c.b);
         return class_3965Var.method_17777().equals(this.c.a) && class_3965Var.method_17780() == this.c.b
            ? true
            : class_3965Var.method_17777().equals(placePos) && this.a(aM_.field_1687.method_8320(placePos));
      }
   }

   private boolean v() {
      class_2338 placePos = this.c.a.method_10093(this.c.b);
      return !aM_.field_1687.method_8390(class_1297.class, new class_238(placePos), entity -> !entity.method_7325() && entity.method_5805()).isEmpty();
   }

   static class AnonymousClass1 {
      static final int[] a = new int[class_2350.values().length];

      static {
         try {
            a[class_2350.field_11036.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
         }

         try {
            a[class_2350.field_11033.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
         }

         try {
            a[class_2350.field_11043.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
         }

         try {
            a[class_2350.field_11035.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            a[class_2350.field_11039.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
         }

         try {
            a[class_2350.field_11034.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
         }
      }
   }

   static final class a {
      final class_2338 a;
      final class_2350 b;
      final class_3965 c;

      a(class_2338 pos, class_2350 face, class_3965 result) {
         this.a = pos;
         this.b = face;
         this.c = result;
      }

      public class_2338 a() {
         return this.a;
      }

      public class_2350 b() {
         return this.b;
      }

      public class_3965 c() {
         return this.c;
      }
   }
}

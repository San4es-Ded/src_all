package aethereal.handler;

import aethereal.api.Compile;
import aethereal.config.BaseProcessor;
import aethereal.core.EventTarget;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import aethereal.event.InputEvent;
import aethereal.module.combat.AuraUtil;
import aethereal.util.Look;
import aethereal.util.MathUtil;
import aethereal.util.MoveUtil;
import aethereal.util.Rotation;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_3532;

public class RotationProcessor extends BaseProcessor implements Interface {
   private final Look b = new Look();
   private static RotationProcessor.a c = RotationProcessor.a.IDLE;
   private static float d;
   private static int e;
   private static int f;
   private static int g;
   private static int h;
   private static int i;
   private static boolean j;
   private static float k;
   private static float l;
   private static float m;
   private static float n;

   @Compile
   @Override
   public void setup() {
   }

   @Generated
   public Look a() {
      return this.b;
   }

   @Generated
   public static RotationProcessor.a b() {
      return c;
   }

   @Generated
   public static int c() {
      return i;
   }

   @Override
   public void unSetup() {
   }

   @EventTarget
   private void a(InputEvent e2) {
      if (this.d()) {
         MoveUtil.a(e2, Look.b(), 10);
      }
   }

   @EventTarget
   private void a(GlobalEvent e2) {
      h++;
      if (this.d()) {
         if (e()) {
            this.a(f(), d, false);
         } else if (j) {
            this.a(a(f, h), m, n, false);
         } else {
            this.a(a(f, h), d, false);
         }
      }

      if (c == RotationProcessor.a.AIM && h > g) {
         c = RotationProcessor.a.RESET;
      }

      if (c == RotationProcessor.a.RESET && (j && m <= 0.0F && n <= 0.0F || (j ? this.a(Rotation.a(), m, n, true) : this.a(Rotation.a(), d, true)))) {
         this.b.a(false);
         c = RotationProcessor.a.IDLE;
         e = 0;
      }
   }

   private boolean d() {
      return h >= 2 && h <= g && c != RotationProcessor.a.IDLE;
   }

   public void a(int ticks) {
      i = Math.max(ticks, 0);
   }

   public void a(Rotation rotation, float turnSpeed, int lookMode, int priority) {
      this.a(rotation, turnSpeed, turnSpeed, lookMode, priority);
   }

   public void a(Rotation rotation, float aimSpeed, float resetSpeed, int lookMode, int priority) {
      if (e <= priority) {
         if (c != RotationProcessor.a.IDLE && e()) {
            rotation = f();
         }

         if (c == RotationProcessor.a.IDLE) {
            this.b.a(true);
         }

         d = resetSpeed;
         f = lookMode;
         g = b(lookMode);
         e = priority;
         c = RotationProcessor.a.AIM;
         h = 0;
         j = false;
         this.a(rotation, aimSpeed, true);
      }
   }

   public void a(Rotation rotation, float yawSpeed, float pitchSpeed, float yawReset, float pitchReset, int holdTicks, int priority) {
      if (e <= priority) {
         if (c != RotationProcessor.a.IDLE && e()) {
            rotation = f();
         }

         if (c == RotationProcessor.a.IDLE) {
            this.b.a(true);
         }

         d = Math.max(yawReset, pitchReset);
         k = yawSpeed;
         l = pitchSpeed;
         m = yawReset;
         n = pitchReset;
         j = true;
         f = 1;
         g = Math.max(holdTicks, 1);
         e = priority;
         c = RotationProcessor.a.AIM;
         h = 0;
         this.a(rotation, yawSpeed, pitchSpeed, true);
      }
   }

   private static boolean e() {
      List<UseableHandler.a> tasks = Westra.h().d().v().b().a();
      if (!tasks.isEmpty() && tasks.getFirst().d() < 1) {
         class_1792 item = tasks.getFirst().a().method_7909();
         return item == class_1802.field_49098
            || item == class_1802.field_8634
            || item == class_1802.field_8543
            || item == class_1802.field_8436
            || item == class_1802.field_8551;
      } else {
         return false;
      }
   }

   private static Rotation f() {
      return a(new Rotation(Look.b(), Look.c()));
   }

   public static Rotation a(Rotation rotation) {
      if (aM_.field_1724 == null) {
         return rotation;
      } else {
         float t = aM_.field_1724.field_6012 + aM_.method_61966().method_60637(false);
         float sw = (float)(
               Math.sin(t * 0.8F) * 11.0
                  + Math.sin(t * 0.04000001502137623 + 17.200010267039897) * 1.5
                  + Math.sin(t * 0.10999997113093289 + 5.8000000238651515) * 3.0
                  + Math.sin(t * 0.07000004685868849 + 12.300000009313816) * 1.0
            )
            / 6.0F;
         float sh = (float)(Math.sin(t * 0.09999998815548458) + Math.sin(t * 0.029999993539464892 + 54.10000012300467) * 0.5) / 4.0F;
         return new Rotation(rotation.c() + class_3532.method_15363(sw, -0.15F, 0.15F), rotation.d() + class_3532.method_15363(sh, -0.15F, 0.15F));
      }
   }

   private static int b(int mode) {
      switch (mode) {
         case 0:
            return 1;
         case 1:
            return 9;
         case 7:
            return 30;
         default:
            return 10;
      }
   }

   private static Rotation a(int mode, int idleTicks) {
      if (aM_.field_1724 == null) {
         return new Rotation(Look.b(), Look.c());
      } else {
         float baseYaw = Look.b();
         float basePitch = Look.c();
         float t = aM_.field_1724.field_6012 + aM_.method_61966().method_60637(false);
         float sw = (float)(
               (Math.sin(t * 0.31F) * 0.5 + Math.sin(t * 0.73F + 1.1F) * 0.3000000002422922 + Math.sin(t * 1.7F + 2.6F) * 0.19999998556632664) * 12.0
            )
            / 4.0F;
         switch (mode) {
            case 1:
               float baseYaw2 = AuraUtil.a(aM_.field_1724.method_36454(), Look.b(), MathUtil.a(0.1F, 0.45F));
               float basePitch2 = AuraUtil.a(aM_.field_1724.method_36455(), Look.c(), MathUtil.a(0.1F, 0.45F));
               return new Rotation(baseYaw2 + sw, class_3532.method_15363(basePitch2 + sw, -90.0F, 90.0F));
            case 7:
               if (!Westra.h().d().t().aS().m()) {
                  idleTicks = 25;
               }

               if (idleTicks <= 20) {
                  return new Rotation(aM_.field_1724.method_36454() + sw, class_3532.method_15363(aM_.field_1724.method_36455() + sw, -90.0F, 90.0F));
               }

               float baseYaw3 = AuraUtil.a(aM_.field_1724.method_36454(), Look.b(), MathUtil.a(0.2F, 0.35F));
               float basePitch3 = AuraUtil.a(aM_.field_1724.method_36455(), Look.c(), MathUtil.a(0.2F, 0.35F));
               return new Rotation(baseYaw3 + sw, class_3532.method_15363(basePitch3 + sw, -90.0F, 90.0F));
            default:
               return new Rotation(baseYaw + sw, basePitch + sw);
         }
      }
   }

   private boolean a(Rotation rotation, float yawCap, float pitchCap, boolean bait) {
      Rotation currentRotation = new Rotation(aM_.field_1724);
      float yawDelta = class_3532.method_15393(rotation.c() - currentRotation.c());
      float pitchDelta = rotation.d() - currentRotation.d();
      float newYaw = aM_.field_1724.method_36454() + class_3532.method_15363(yawDelta, -yawCap, yawCap);
      float newPitch = aM_.field_1724.method_36455() + class_3532.method_15363(pitchDelta, -pitchCap, pitchCap);
      float newYaw2 = a(aM_.field_1724.method_36454(), newYaw);
      float newPitch2 = class_3532.method_15363(a(aM_.field_1724.method_36455(), newPitch), -90.0F, 90.0F);
      if (i > 0) {
         newPitch2 = aM_.field_1724.method_36455();
         newYaw2 = aM_.field_1724.method_36454();
         i--;
      }

      aM_.field_1724.method_36456(newYaw2);
      aM_.field_1724.method_36457(newPitch2);
      if (bait) {
         h = 0;
      }

      return new Rotation(aM_.field_1724).a(rotation) < Math.max(yawCap, pitchCap);
   }

   private boolean a(Rotation rotation, float turnSpeed, boolean bait) {
      Rotation currentRotation = new Rotation(aM_.field_1724);
      float yawDelta = class_3532.method_15393(rotation.c() - currentRotation.c());
      float pitchDelta = rotation.d() - currentRotation.d();
      float totalDelta = Math.abs(yawDelta) + Math.abs(pitchDelta);
      float yawSpeed = totalDelta == 0.0F ? 0.0F : Math.abs(yawDelta / totalDelta) * turnSpeed;
      float pitchSpeed = totalDelta == 0.0F ? 0.0F : Math.abs(pitchDelta / totalDelta) * turnSpeed;
      float newYaw = aM_.field_1724.method_36454() + class_3532.method_15363(yawDelta, -yawSpeed, yawSpeed);
      float newPitch = aM_.field_1724.method_36455() + class_3532.method_15363(pitchDelta, -pitchSpeed, pitchSpeed);
      float newYaw2 = a(aM_.field_1724.method_36454(), newYaw);
      float newPitch2 = class_3532.method_15363(a(aM_.field_1724.method_36455(), newPitch), -90.0F, 90.0F);
      if (i > 0) {
         newPitch2 = aM_.field_1724.method_36455();
         newYaw2 = aM_.field_1724.method_36454();
         i--;
      }

      aM_.field_1724.method_36456(newYaw2);
      aM_.field_1724.method_36457(newPitch2);
      Rotation finalRotation = new Rotation(aM_.field_1724);
      if (bait) {
         h = 0;
      }

      return finalRotation.a(rotation) < turnSpeed;
   }

   public static float a(float lastYaw, float current) {
      double sens = (Double)aM_.field_1690.method_42495().method_41753() * 0.6000000498956214 + 0.19999998556632664;
      double gcd = sens * sens * sens * 8.0;
      return (float)(lastYaw + Math.ceil((current - lastYaw) / gcd / 0.15000007F) * gcd * 0.15000007F);
   }

   static {
      NativeMethodLookup.lookup(RotationProcessor.class, 36);
   }

   public static enum a {
      AIM,
      RESET,
      IDLE;
   }
}

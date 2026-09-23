package aethereal.util;

import aethereal.core.EventManager;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.LookEvent;
import aethereal.event.RotationEvent;
import lombok.Generated;
import net.minecraft.class_3532;

public class Look implements Interface {
   private boolean b;
   private static float c;
   private static float d;

   @Generated
   public boolean a() {
      return this.b;
   }

   @Generated
   public static float b() {
      return c;
   }

   @Generated
   public static float c() {
      return d;
   }

   @Generated
   public static void a(float freeYaw) {
      c = freeYaw;
   }

   @Generated
   public static void b(float freePitch) {
      d = freePitch;
   }

   public Look() {
      EventManager.a(this);
   }

   @EventTarget
   private void a(LookEvent e) {
      if (this.b) {
         this.a(e.a, e.b);
         e.a(true);
      }
   }

   @EventTarget
   private void a(RotationEvent e) {
      if (this.b) {
         e.a(c);
         e.b(d);
      } else {
         c = e.b();
         d = e.c();
      }
   }

   public void a(boolean state) {
      if (this.b != state) {
         this.b = state;
         d();
      }
   }

   private void a(double yaw, double pitch) {
      double d0 = pitch * 0.15000001238751678;
      double d1 = yaw * 0.15000001238751678;
      d = (float)(d + d0);
      c = (float)(c + d1);
      d = class_3532.method_15363(d, -90.0F, 90.0F);
   }

   private static void d() {
      if (aM_.field_1724 != null) {
         float py = aM_.field_1724.method_36454();
         float fy = c;
         aM_.field_1724.method_36456(py + class_3532.method_15393(fy - py));
         aM_.field_1724.method_36457(d);
      }
   }
}

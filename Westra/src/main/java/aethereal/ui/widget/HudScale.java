package aethereal.ui.widget;

import aethereal.core.Interface;
import lombok.Generated;

public class HudScale {
   private static float a = 1.0F;

   @Generated
   private HudScale() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   @Generated
   public static float a() {
      return a;
   }

   public static void a(float scale) {
      a = Math.max(0.25F, scale);
   }

   public static float b() {
      int base = Interface.aM_.method_22683().method_4476(2, Interface.aM_.method_1573());
      return base <= 0 ? 1.0F : (float)Interface.aM_.method_22683().method_4495() / base;
   }

   public static float c() {
      return Interface.aM_.method_22683().method_4489() / (Interface.aM_.method_22683().method_4476(2, Interface.aM_.method_1573()) * a);
   }

   public static float d() {
      return Interface.aM_.method_22683().method_4506() / (Interface.aM_.method_22683().method_4476(2, Interface.aM_.method_1573()) * a);
   }
}

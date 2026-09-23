package aethereal.util;

import aethereal.core.Interface;
import lombok.Generated;

public class GCDUtil implements Interface {
   @Generated
   private GCDUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static float a() {
      if (aM_.field_1690 == null) {
         return 0.0F;
      } else {
         double sensitivity = (Double)aM_.field_1690.method_42495().method_41753() * 0.6 + 0.2;
         return (float)(sensitivity * sensitivity * sensitivity * 1.2);
      }
   }

   public static float a(float delta) {
      float gcd = a();
      return gcd > 0.0F && Float.isFinite(gcd) ? Math.round(delta / gcd) * gcd : delta;
   }
}

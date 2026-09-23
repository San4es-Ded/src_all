package aethereal.render;

import aethereal.core.Interface;
import lombok.Generated;
import net.minecraft.class_1041;
import net.minecraft.class_332;

public class ScaleUtil implements Interface {
   @Generated
   private ScaleUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void a(class_332 context, int scale) {
      class_1041 window = aM_.method_22683();
      double previous = window.method_4495();
      double target = window.method_4476(scale, aM_.method_1573());
      window.method_15997(target);
      context.method_51448().method_22903();
      context.method_51448().method_22905((float)(target / previous), (float)(target / previous), 1.0F);
   }

   public static void a(class_332 context) {
      context.method_51448().method_22909();
      aM_.method_22683().method_15997(aM_.method_22683().method_4476((Integer)aM_.field_1690.method_42474().method_41753(), aM_.method_1573()));
   }

   public static void b(class_332 context) {
      a(context, (Integer)aM_.field_1690.method_42474().method_41753());
   }

   public static void c(class_332 context) {
      context.method_51448().method_22909();
      aM_.method_22683().method_15997(aM_.method_22683().method_4476(2, aM_.method_1573()));
   }
}

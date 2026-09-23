package aethereal.ui.shader;

import aethereal.render.ColorUtil;
import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_5250;

public class GradientUtil {
   @Generated
   private GradientUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static class_5250 a(String text, int startColor, int endColor, int speed, float ratio) {
      class_5250 component = class_2561.method_43470("");
      if (text != null && !text.isEmpty()) {
         float time = (float)(System.currentTimeMillis() % 10000L) / 1000.0F * (100.0F / speed);
         int length = text.length();

         for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            int color = ColorUtil.a(startColor, endColor, i, length, time, ratio);
            class_2583 style = class_2583.field_24360.method_36139(color & 16777215);
            component.method_10852(class_2561.method_43470(String.valueOf(c)).method_10862(style));
         }

         return component;
      } else {
         return component;
      }
   }

   public static class_5250 a(String text, int color, float speed, float offset) {
      class_5250 component = class_2561.method_43470("");
      if (text != null && !text.isEmpty()) {
         float time = (float)(System.currentTimeMillis() % (long)(speed * 1000.0F)) / (speed * 1000.0F);
         float[] hsb = Color.RGBtoHSB(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, (float[])null);

         for (int i = 0; i < text.length(); i++) {
            float factor = (float)(Math.sin((time + i * offset / text.length()) * 3.1415926649985018 * 2.0) * 0.5 + 0.5);
            int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2] * (0.5F + 0.5F * factor)) & 16777215;
            component.method_10852(class_2561.method_43470(String.valueOf(text.charAt(i))).method_10862(class_2583.field_24360.method_36139(rgb)));
         }

         return component;
      } else {
         return component;
      }
   }

   public static int a(int speed, int angle, int startColor, int endColor, long time) {
      float animatedAngle = (float)((angle + time / 10L) % 360L);
      float ratio = animatedAngle / 360.0F;
      return ColorUtil.b(startColor, endColor, (float)((Math.sin(ratio * 3.1415926649985018 * 2.0 * speed) + 1.0) / 2.0));
   }
}

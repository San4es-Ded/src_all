package aethereal.util;

import aethereal.core.Interface;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_243;

public class MathUtil implements Interface {
   @Generated
   private MathUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static float a(float start, float end, float delta) {
      return start + (end - start) * delta;
   }

   public static class_243 a(class_1297 entity, float partialTicks) {
      return new class_243(
         entity.field_6014 + (entity.method_23317() - entity.field_6014) * partialTicks,
         entity.field_6036 + (entity.method_23318() - entity.field_6036) * partialTicks,
         entity.field_5969 + (entity.method_23321() - entity.field_5969) * partialTicks
      );
   }

   public static float b(float num, float min, float max) {
      return Math.min(Math.max(num, min), max);
   }

   public static double scale(double coordinate, int factor) {
      return coordinate * aM_.method_22683().method_4495() / aM_.method_22683().method_4476(factor, aM_.method_1573());
   }

   public static float a(float value) {
      float clamped = b(value, 0.0F, 1.0F);
      return clamped * clamped * (3.0F - 2.0F * clamped);
   }

   public static float a(float min, float max) {
      return (float)(Math.random() * (max - min) + min);
   }

   public static float[] b(float smoothness) {
      float horizontal = -smoothness / 2.0F + smoothness * 2.0F;
      float vertical = smoothness / 2.0F + smoothness;
      return new float[]{horizontal, vertical};
   }

   public static String a(int amplifier) {
      String[] strings = new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
      return amplifier >= 0 && amplifier < strings.length ? strings[amplifier] : String.valueOf(amplifier + 1);
   }

   public static int a(String text) {
      if (text != null && !text.isBlank()) {
         String norm = text.replaceAll("§.", "").toLowerCase().replaceAll("\\s+", "").trim();
         String[] strings = new String[]{"i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x"};

         for (int i = strings.length - 1; i >= 0; i--) {
            if (norm.contains(strings[i])) {
               return i + 1;
            }
         }

         String digits = norm.replaceAll("[^0-9]", "");
         return !digits.isEmpty() && Integer.parseInt(digits) >= 1 && Integer.parseInt(digits) <= 10 ? Integer.parseInt(digits) : 0;
      } else {
         return 0;
      }
   }

   public static boolean a(double mouseX, double mouseY, float x, float y, float width, float height) {
      return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
   }

   public static float c(float current, float target, float speed) {
      float delta = aM_.method_61966().method_60636();
      return current + (target - current) * (1.0F - (float)Math.exp(-speed * delta));
   }
}

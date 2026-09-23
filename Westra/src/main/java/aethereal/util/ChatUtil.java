package aethereal.util;

import aethereal.config.ThemeInfo;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.render.ColorUtil;
import aethereal.ui.shader.GradientUtil;
import lombok.Generated;
import net.minecraft.class_2561;
import net.minecraft.class_2568;
import net.minecraft.class_2583;
import net.minecraft.class_5250;
import net.minecraft.class_2568.class_5247;

public class ChatUtil implements Interface {
   @Generated
   private ChatUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void sendMessage(Object message) {
      a(message);
   }

   public static void a(Object message) {
      a("[Westra 1.21.4]", message);
   }

   public static void a(String prefix, Object message) {
      if (aM_.field_1724 != null) {
         class_5250 class_5250VarB;
         if (prefix != null && !prefix.isEmpty()) {
            class_5250VarB = a(prefix).method_27661().method_10852(class_2561.method_43470("")).method_10852(b(message));
         } else {
            class_5250VarB = b(message);
         }

         aM_.field_1724.method_7353(class_5250VarB, false);
      }
   }

   public static class_5250 b(Object message) {
      if (message instanceof class_5250 mutableText) {
         return mutableText;
      } else {
         return message instanceof class_2561 text ? text.method_27661() : class_2561.method_43470(("&7" + message).replace('&', '§'));
      }
   }

   public static class_5250 a(Object message, class_2561 hover) {
      String strValueOf;
      if (message instanceof class_2561 text) {
         strValueOf = text.getString();
      } else {
         strValueOf = String.valueOf(message);
      }

      return class_2561.method_43470(strValueOf.replace('&', '§'))
         .method_10862(class_2583.field_24360.method_10949(new class_2568(class_5247.field_24342, hover.method_27661())));
   }

   private static class_5250 a(String prefix) {
      int primary = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
      return GradientUtil.a(prefix + " » ", primary, ColorUtil.b(primary, 0.5F), 1, 5.0F);
   }
}

package aethereal.util;

import aethereal.core.Interface;
import lombok.Generated;
import org.lwjgl.glfw.GLFW;

public class CursorUtil implements Interface {
   @Generated
   private CursorUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void a(CursorUtil.a type) {
      if (aM_.method_22683() != null) {
         long cursor = GLFW.glfwCreateStandardCursor(type.a());
         if (cursor != 0L) {
            GLFW.glfwSetCursor(aM_.method_22683().method_4490(), cursor);
         }
      }
   }

   public static enum a {
      DEFAULT(221185),
      HAND(221188),
      ARROW_HORIZONTAL(221189),
      ARROW_VERTICAL(221190),
      TEXT(221186),
      CROSSHAIR(221187),
      BLOCK(221194),
      RESIZE_ALL(221193);

      private final int i;

      @Generated
      private a(final int glfwType) {
         this.i = glfwType;
      }

      @Generated
      public int a() {
         return this.i;
      }
   }
}

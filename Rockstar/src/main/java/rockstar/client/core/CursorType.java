package rockstar.client.core;


import rockstar.client.*;
import lombok.Generated;
import org.lwjgl.glfw.GLFW;

public enum CursorType {
   internalField0566(GLFW.glfwCreateStandardCursor(221185)),
   internalField0567(GLFW.glfwCreateStandardCursor(221188)),
   internalField1208(GLFW.glfwCreateStandardCursor(221189)),
   internalField1205(GLFW.glfwCreateStandardCursor(221190)),
   internalField1206(GLFW.glfwCreateStandardCursor(221186)),
   internalField1207(GLFW.glfwCreateStandardCursor(221187)),
   internalField1562(GLFW.glfwCreateStandardCursor(221194)),
   internalField1563(GLFW.glfwCreateStandardCursor(221193));

   private final long internalField0229;

   @Generated
   public long internalMethod04926() {
      return this.internalField0229;
   }

   @Generated
   private CursorType(long localValue3) {
      this.internalField0229 = localValue3;
   }
}

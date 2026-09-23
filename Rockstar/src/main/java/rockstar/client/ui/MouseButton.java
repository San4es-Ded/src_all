package rockstar.client.ui;


import rockstar.client.*;
import lombok.Generated;

public enum MouseButton {
   internalField0102(0),
   internalField0101(1),
   internalField0990(2),
   internalField0991(3),
   internalField0989(4),
   internalField0992(5),
   internalField1413(6),
   internalField1414(7);

   private final int internalField0227;

   public static MouseButton internalMethod01669(int localValue0) {
      for (MouseButton localValue4 : values()) {
         if (localValue4.internalMethod02957() == localValue0) {
            return localValue4;
         }
      }

      return internalField0102;
   }

   @Generated
   private MouseButton(int localValue3) {
      this.internalField0227 = localValue3;
   }

   @Generated
   public int internalMethod02957() {
      return this.internalField0227;
   }
}

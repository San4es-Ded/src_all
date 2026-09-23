package rockstar.client.esp;


import rockstar.client.*;
import lombok.Generated;

public enum ItemTargetType {
   internalField0012("held", "esp.targets.items.held"),
   internalField0013("dropped", "esp.targets.items.dropped");

   private final String internalField0248;
   private final String internalField0247;

   private ItemTargetType(String localValue3, String localValue4) {
      this.internalField0248 = localValue3;
      this.internalField0247 = localValue4;
   }

   public static ItemTargetType internalMethod06052(String localValue0) {
      for (ItemTargetType localValue4 : values()) {
         if (localValue4.internalField0248.equals(localValue0)) {
            return localValue4;
         }
      }

      return null;
   }

   @Generated
   public String internalMethod04152() {
      return this.internalField0248;
   }

   @Generated
   public String internalMethod00716() {
      return this.internalField0247;
   }
}

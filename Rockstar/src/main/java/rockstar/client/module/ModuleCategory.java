package rockstar.client.module;


import rockstar.client.*;
import lombok.Generated;

public enum ModuleCategory {
   COMBAT("Combat"),
   MOVEMENT("Movement"),
   VISUALS("Visuals"),
   PLAYER("Player"),
   OTHER("Other");

   private final String internalField0248;

   public static ModuleCategory internalMethod05008(String localValue0) {
      for (ModuleCategory localValue4 : values()) {
         if (localValue4.name().equalsIgnoreCase(localValue0) || localValue4.internalField0248.equalsIgnoreCase(localValue0)) {
            return localValue4;
         }
      }

      return null;
   }

   @Generated
   public String internalMethod05277() {
      return this.internalField0248;
   }

   @Generated
   private ModuleCategory(String localValue3) {
      this.internalField0248 = localValue3;
   }
}

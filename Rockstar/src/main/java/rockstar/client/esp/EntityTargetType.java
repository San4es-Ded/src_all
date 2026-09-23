package rockstar.client.esp;


import rockstar.client.*;
import lombok.Generated;

public enum EntityTargetType {
   internalField0027("players", "esp.targets.players", true),
   internalField0028("mobs", "esp.targets.mobs", false),
   internalField0963("animals", "esp.targets.animals", false),
   internalField0964("items", "esp.targets.items", true);

   private final String internalField0248;
   private final String internalField0247;
   private final boolean internalField0277;

   private EntityTargetType(String localValue3, String localValue4, boolean localValue5) {
      this.internalField0248 = localValue3;
      this.internalField0247 = localValue4;
      this.internalField0277 = localValue5;
   }

   public boolean internalMethod03264() {
      return this.internalField0277;
   }

   public static EntityTargetType internalMethod00815(String localValue0) {
      for (EntityTargetType localValue4 : values()) {
         if (localValue4.internalField0248.equals(localValue0)) {
            return localValue4;
         }
      }

      return null;
   }

   @Generated
   public String internalMethod00613() {
      return this.internalField0248;
   }

   @Generated
   public String internalMethod04227() {
      return this.internalField0247;
   }

   @Generated
   public boolean internalMethod03267() {
      return this.internalField0277;
   }
}

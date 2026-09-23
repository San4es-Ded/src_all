package rockstar.client.esp;


import rockstar.client.*;
import lombok.Generated;

public enum PlayerTargetType {
   internalField0025("others", "esp.targets.players.others"),
   internalField0026("local", "esp.targets.players.local"),
   internalField0961("friends", "esp.targets.players.friends"),
   internalField0962("rockstar_users", "esp.targets.players.rockstar_users");

   private final String internalField0248;
   private final String internalField0247;

   private PlayerTargetType(String localValue3, String localValue4) {
      this.internalField0248 = localValue3;
      this.internalField0247 = localValue4;
   }

   public static PlayerTargetType internalMethod07211(String localValue0) {
      for (PlayerTargetType localValue4 : values()) {
         if (localValue4.internalField0248.equals(localValue0)) {
            return localValue4;
         }
      }

      return null;
   }

   @Generated
   public String internalMethod03561() {
      return this.internalField0248;
   }

   @Generated
   public String internalMethod00118() {
      return this.internalField0247;
   }
}

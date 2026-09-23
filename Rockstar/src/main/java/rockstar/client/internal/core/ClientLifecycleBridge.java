package rockstar.client.internal.core;


import rockstar.client.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rockstar.profile.Profile;

public class ClientLifecycleBridge {
   public static void internalMethod01636() {
      RockstarClient.internalField0240.initialize();
   }

   public static void internalMethod01639() {
      RockstarClient.internalField0240.shutdown();
   }

   public static void internalMethod06700(CallbackInfoReturnable<String> localValue0) {
      if (!RockstarClient.internalField0240.internalMethod06896()) {
         String localValue1 = "NoName";
         localValue0.setReturnValue(localValue1);
      }
   }
}

package platform;

import aethereal.api.Compile;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import net.fabricmc.api.ClientModInitializer;

public class Initializer implements ClientModInitializer {
   @Compile
   public void onInitializeClient() {
      new Westra();
   }

   static {
      NativeMethodLookup.lookup(Initializer.class, 1);
   }
}

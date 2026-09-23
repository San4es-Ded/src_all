package org.patch.arbuzhack.api;

import aethereal.ArbuzClient;
import net.fabricmc.api.ModInitializer;

public class Proxy implements ModInitializer {
   ArbuzClient field0573 = new ArbuzClient();

   public void onInitialize() {
      this.field0573.method0578();
   }
}

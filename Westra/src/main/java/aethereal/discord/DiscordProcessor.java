package aethereal.discord;

import aethereal.api.Compile;
import aethereal.config.BaseProcessor;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import java.io.IOException;
import lombok.Generated;

public class DiscordProcessor extends BaseProcessor {
   private DiscordIPC b;

   @Compile
   @Override
   public void setup() {
   }

   @Override
   public void unSetup() {
   }

   @Generated
   public DiscordIPC a() {
      return this.b;
   }

   public void a(Void result, Throwable ex) {
      if (ex == null) {
         try {
            this.b
               .a(
                  new Activity.a()
                     .a(ActivityType.PLAYING)
                     .b("Westra Client")
                     .a(Westra.h().g().b() + " · " + (Westra.h().c() != null ? "development" : "public"))
                     .a("westra_logo", "Westra Client")
                     .a(System.currentTimeMillis() / 1000L)
                     .a()
               );
         } catch (IOException var4) {
            throw new RuntimeException(var4);
         }
      }
   }

   static {
      NativeMethodLookup.lookup(DiscordProcessor.class, 25);
   }
}

package aethereal.handler;

import aethereal.core.EventTarget;
import aethereal.event.PacketEvent;
import lombok.Generated;
import net.minecraft.class_2761;

@Handler_2
public class TPSHandler extends BaseHandler {
   private long a = -1L;
   private long b = -1L;
   private float c = 20.0F;

   @Generated
   public float a() {
      return this.c;
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c() && event.d() instanceof class_2761 class_2761VarD) {
         long now = System.currentTimeMillis();
         long worldTime = class_2761VarD.comp_3219();
         if (this.b > 0L && worldTime > this.a) {
            long ticksDelta = worldTime - this.a;
            long msDelta = now - this.b;
            this.c = Math.min(20.0F, (float)ticksDelta * 1000.0F / (float)msDelta);
         }

         this.b = now;
         this.a = worldTime;
      }
   }
}

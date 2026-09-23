package aethereal.module.misc;

import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.InputEvent;
import aethereal.event.TickEvent;
import aethereal.handler.BaseHandler;
import aethereal.handler.Handler_2;
import aethereal.util.MathUtil;
import aethereal.util.Rotation;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Generated;

@Handler_2
public class AFKHandler extends BaseHandler implements Interface {
   private int b = -1;

   @Generated
   public int b() {
      return this.b;
   }

   public void a(int ticks) {
      this.b = ticks;
   }

   public boolean a() {
      return this.b > 0;
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.b > 0) {
         this.b--;
      }
   }

   @EventTarget
   public void a(InputEvent e) {
      if (this.b > 0) {
         e.a(ThreadLocalRandom.current().nextBoolean() ? 1.0F : -1.0F);
         e.b(ThreadLocalRandom.current().nextBoolean() ? 1.0F : -1.0F);
         Westra.h()
            .d()
            .k()
            .a(
               new Rotation(
                  aM_.field_1724.method_36454() + MathUtil.a(-2.0F, 2.0F), MathUtil.b(aM_.field_1724.method_36455() + MathUtil.a(-1.0F, 1.0F), -90.0F, 90.0F)
               ),
               150.0F,
               10,
               1
            );
      }
   }
}

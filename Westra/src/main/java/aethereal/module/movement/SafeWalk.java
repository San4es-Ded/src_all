package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.InputEvent;

@ModuleRegister(
   a = "Safe Walk",
   b = "Не даёт упасть с края блоков",
   c = Category.Movement
)
public class SafeWalk extends Module {
   @EventTarget
   public void a(InputEvent event) {
      this.b(event);
   }

   public void b(InputEvent event) {
      event.c(
         event.e()
            || aM_.field_1687
                  .method_8320(aM_.field_1724.method_24515().method_10074())
                  .method_26220(aM_.field_1687, aM_.field_1724.method_24515().method_10074())
                  .method_1110()
               && aM_.field_1724.method_24828()
      );
   }
}

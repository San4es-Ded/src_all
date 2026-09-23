package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import net.minecraft.class_1294;

@ModuleRegister(
   a = "Sprint",
   b = "Автоматически включает спринт при движении",
   c = Category.Movement
)
public class Sprint extends Module {
   @EventTarget
   public void a(TickEvent event) {
      aM_.field_1724
         .method_5728(
            aM_.field_1724.field_3913.field_3905 > 0.0F
               && !aM_.field_1724.method_6059(class_1294.field_5919)
               && (aM_.field_1724.method_31549().field_7480 || aM_.field_1724.method_7344().method_7586() > 6)
         );
   }
}

package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;

@ModuleRegister(
   a = "Death Coords",
   b = "Выводит координаты последней смерти",
   c = Category.Player
)
public class DeathCoords extends Module {
   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724.field_6213 == 1) {
         ChatUtil.a(
            String.format(
               "Вы погибли на координатах: &c[%d, %d, %d]",
               aM_.field_1724.method_24515().method_10263(),
               aM_.field_1724.method_24515().method_10264(),
               aM_.field_1724.method_24515().method_10260()
            )
         );
      }
   }
}

package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import net.minecraft.class_418;

@ModuleRegister(
   a = "Auto Respawn",
   b = "Автоматически возрождает персонажа после смерти",
   c = Category.Player
)
public class AutoRespawn extends Module {
   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1755 instanceof class_418 && aM_.field_1724.field_6213 >= 5) {
         aM_.field_1724.method_7331();
      }
   }
}

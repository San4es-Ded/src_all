package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.AttackEvent;
import aethereal.event.InputEvent;
import aethereal.event.TickEvent;
import net.minecraft.class_1657;

@ModuleRegister(
   a = "Shift TAP",
   b = "Автоматически приседает в момент удара по игроку",
   c = Category.Combat
)
public class ShiftTAP extends Module {
   private int b;

   @EventTarget
   public void a(AttackEvent event) {
      if (event.b() instanceof class_1657) {
         this.b = 2;
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.b > 0) {
         this.b--;
      }
   }

   @EventTarget
   public void a(InputEvent event) {
      if (this.b > 0) {
         event.c(true);
      }
   }
}

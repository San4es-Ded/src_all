package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.AttackEvent;

@ModuleRegister(
   a = "No Friend Damage",
   b = "Не позволяет наносить урон вашим друзьям",
   c = Category.Combat
)
public class NoFriendDamage extends Module {
   @EventTarget
   public void a(AttackEvent event) {
      event.a(Westra.h().d().e().d(event.b().method_5477().getString()));
   }
}

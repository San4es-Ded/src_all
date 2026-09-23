package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import net.minecraft.class_1802;
import platform.inject.accessors.MinecraftClientAccessor;

@ModuleRegister(
   a = "Fast EXP",
   b = "Позволяет очень быстро бросать опыт",
   c = Category.Player
)
public class FastEXP extends Module implements Interface {
   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724.method_6047().method_7909() == class_1802.field_8287) {
         ((MinecraftClientAccessor)aM_).setItemUseCooldown(0);
      }
   }
}

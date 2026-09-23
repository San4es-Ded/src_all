package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PacketEvent;
import net.minecraft.class_2735;
import net.minecraft.class_2868;

@ModuleRegister(
   a = "No Slot Change",
   b = "Не даёт серверу принудительно менять активный слот в хотбаре",
   c = Category.Combat
)
public class NoSlotChange extends Module {
   @EventTarget
   public void a(PacketEvent event) {
      if (event.c() && event.d() instanceof class_2735) {
         aM_.field_1724.field_3944.method_52787(new class_2868(aM_.field_1724.method_31548().field_7545));
         event.a(true);
      }
   }
}

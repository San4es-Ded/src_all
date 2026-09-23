package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PacketEvent;
import net.minecraft.class_2708;

@ModuleRegister(
   a = "No Server Desync",
   b = "Не даёт серверу принудительно сбрасывать поворот вашей камеры",
   c = Category.Combat
)
public class NoServerDesync extends Module {
   @EventTarget
   public void a(PacketEvent event) {
      if (event.c() && event.d() instanceof class_2708 packet && packet.comp_3228().comp_3151() != 0.0F && packet.comp_3228().comp_3150() != 0.0F) {
         event.a(true);
      }
   }
}

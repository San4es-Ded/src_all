package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import net.minecraft.class_243;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2851;
import net.minecraft.class_1297.class_5529;
import platform.inject.accessors.EntityInvoker;

@ModuleRegister(
   a = "Air Stuck",
   b = "Позволяет зависнуть в воздухе на месте",
   c = Category.Movement
)
public class AirStuck extends Module implements Interface {
   private final ModeSetting b = new ModeSetting("Режим зависания", "Обычный", "Обычный", "Удаляющий игрока");
   private class_243 c;

   public AirStuck() {
      this.a(new Setting[]{this.b});
   }

   @Override
   public void b() {
      super.b();
      if (aM_.field_1724 != null) {
         this.c = aM_.field_1724.method_19538();
         if (this.b.l("Удаляющий игрока")) {
            aM_.field_1724.method_31745(class_5529.field_26999);
         }
      }
   }

   @Override
   public void c() {
      super.c();
      if (aM_.field_1724 != null && this.b.l("Удаляющий игрока")) {
         ((EntityInvoker)aM_.field_1724).unset();
         aM_.field_1687.method_53875(aM_.field_1724);
         aM_.field_1724.method_29495(this.c);
      }

      this.c = null;
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.c != null) {
         aM_.field_1724.method_18800(0.0, 0.0, 0.0);
         aM_.field_1724.method_5814(this.c.field_1352, this.c.field_1351, this.c.field_1350);
      }
   }

   @EventTarget
   public void a(GlobalEvent event) {
      if (aM_.field_1724 != null && aM_.field_1724.method_31481()) {
         ((EntityInvoker)aM_.field_1724).baseTickInvoker();
      }
   }

   @EventTarget
   public void a(InputEvent event) {
      if (this.b.l("Обычный")) {
         event.a(true);
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c() && (event.d() instanceof class_2828 || event.d() instanceof class_2851 || event.d() instanceof class_2846)) {
         event.a(true);
      }
   }
}

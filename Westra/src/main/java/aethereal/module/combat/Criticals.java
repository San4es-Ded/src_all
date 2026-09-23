package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.AttackEvent;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1802;
import net.minecraft.class_2828.class_2830;

@ModuleRegister(
   a = "Criticals",
   b = "Превращает каждый удар в критический с помощью пакетов движения",
   c = Category.Combat
)
public class Criticals extends Module implements Interface {
   private final ModeSetting b = new ModeSetting("Режим", "Grim Air", "Grim Air", "NCP+", "Булава");

   public Criticals() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(AttackEvent event) {
      if (aM_.field_1724 != null && !aM_.field_1724.method_5799() && !aM_.field_1724.method_5771() && !aM_.field_1724.method_6101()) {
         double x = aM_.field_1724.method_23317();
         double y = aM_.field_1724.method_23318();
         double z = aM_.field_1724.method_23321();
         if (this.b.l("Grim Air")) {
            if (!aM_.field_1724.method_24828() && aM_.field_1724.field_6017 == 0.0F) {
               float drop = ThreadLocalRandom.current().nextFloat(1.0E-5F, 1.0E-4F);
               aM_.field_1724.field_6017 = drop;
               this.send(x, y - drop, z, aM_.field_1724.method_24828());
            }
         } else if (this.b.l("NCP+")) {
            if (aM_.field_1724.method_24828()) {
               this.send(x, y + 0.0625, z, false);
               this.send(x, y, z, false);
            }
         } else if (this.b.l("Булава") && aM_.field_1724.method_6047().method_31574(class_1802.field_49814) && aM_.field_1724.field_6017 < 9.0F) {
            this.send(x, y + 9.0, z, false);
            this.send(x, y, z, false);
         }
      }
   }

   private void send(double x, double y, double z, boolean onGround) {
      aM_.field_1724
         .field_3944
         .method_52787(new class_2830(x, y, z, aM_.field_1724.method_36454(), aM_.field_1724.method_36455(), onGround, aM_.field_1724.field_5976));
   }
}

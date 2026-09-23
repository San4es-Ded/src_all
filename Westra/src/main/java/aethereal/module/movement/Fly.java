package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import net.minecraft.class_2848;
import net.minecraft.class_2848.class_2849;

@ModuleRegister(
   a = "Fly",
   b = "Позволяет свободно летать по миру",
   c = Category.Movement
)
public class Fly extends Module {
   private final ModeSetting d = new ModeSetting("Режим полёта", "Ванильный", "Ванильный", "Способности", "Элитра");
   private final SliderSetting b = new SliderSetting("Скорость X и Z", 1.0F, 0.1F, 5.0F, 0.1F).a(() -> !this.d.l("Способности"));
   private final SliderSetting c = new SliderSetting("Скорость Y", 1.0F, 0.1F, 5.0F, 0.1F).a(() -> this.d.l("Ванильный"));
   private boolean e;

   public Fly() {
      this.a(new Setting[]{this.d, this.b, this.c});
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null) {
         if (this.d.l("Способности")) {
            this.r();
         } else if (this.d.l("Элитра")) {
            this.s();
         } else {
            this.q();
         }
      }
   }

   private void r() {
      if (!aM_.field_1724.method_7337() && !aM_.field_1724.method_31549().field_7478) {
         aM_.field_1724.method_31549().field_7478 = true;
         this.e = true;
      }
   }

   private void s() {
      if (aM_.field_1724.method_6128()) {
         double yaw = Math.toRadians(aM_.field_1724.method_36454());
         double speed = this.b.c().floatValue() * 1.8;
         aM_.field_1724.method_18800(-Math.sin(yaw) * speed, aM_.field_1724.method_18798().field_1351 * 0.95 + 0.02, Math.cos(yaw) * speed);
      } else {
         if (!aM_.field_1724.method_24828() && aM_.field_1724.field_3944 != null) {
            aM_.field_1724.field_3944.method_52787(new class_2848(aM_.field_1724, class_2849.field_12982));
         }
      }
   }

   @Override
   public void c() {
      if (this.e && aM_.field_1724 != null && !aM_.field_1724.method_7337()) {
         aM_.field_1724.method_31549().field_7478 = false;
         aM_.field_1724.method_31549().field_7479 = false;
      }

      this.e = false;
      super.c();
   }

   private void q() {
      float yaw = aM_.field_1724.method_36454();
      double dFloatValue;
      if (aM_.field_1690.field_1832.method_1434()) {
         dFloatValue = -this.c.c();
      } else {
         dFloatValue = aM_.field_1690.field_1903.method_1434() ? this.c.c().floatValue() : 0.0;
      }

      if (aM_.field_1724.field_3913.field_3905 == 0.0F && aM_.field_1724.field_3913.field_3907 == 0.0F) {
         aM_.field_1724.method_18800(0.0, dFloatValue, 0.0);
      } else {
         if (aM_.field_1724.field_3913.field_3905 != 0.0F) {
            float fSignum;
            if (aM_.field_1724.field_3913.field_3907 != 0.0F) {
               fSignum = (aM_.field_1724.field_3913.field_3905 > 0.0F ? -45.0F : 45.0F) * Math.signum(aM_.field_1724.field_3913.field_3907);
            } else {
               fSignum = 0.0F;
            }

            yaw += fSignum;
            aM_.field_1724.field_3913.field_3907 = 0.0F;
            aM_.field_1724.field_3913.field_3905 = Math.signum(aM_.field_1724.field_3913.field_3905);
         }

         double radians = Math.toRadians(yaw + 90.0F);
         double speedXZ = this.b.c().floatValue();
         double motionX = aM_.field_1724.field_3913.field_3905 * speedXZ * Math.cos(radians)
            + aM_.field_1724.field_3913.field_3907 * speedXZ * Math.sin(radians);
         double motionZ = aM_.field_1724.field_3913.field_3905 * speedXZ * Math.sin(radians)
            - aM_.field_1724.field_3913.field_3907 * speedXZ * Math.cos(radians);
         aM_.field_1724.method_18800(motionX, dFloatValue, motionZ);
      }
   }
}

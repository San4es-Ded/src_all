package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import net.minecraft.class_1309;
import net.minecraft.class_243;

@ModuleRegister(
   a = "Elytra Motion",
   b = "Держит дистанцию до цели в полёте на элитре",
   c = Category.Movement
)
public class ElytraMotion extends Module implements Interface {
   private final SliderSetting b = new SliderSetting("Дистанция до цели", 2.5F, 1.5F, 4.0F, 0.1F);
   private final SliderSetting c = new SliderSetting("Сила притяжения", 0.35F, 0.05F, 1.0F, 0.05F);
   private final BooleanSetting d = new BooleanSetting("Гасить падение", true);

   public ElytraMotion() {
      this.a(new Setting[]{this.b, this.c, this.d});
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1724.method_6128()) {
         class_1309 target = Westra.h().d().t().B().s();
         if (target != null) {
            double wanted = this.b.c().floatValue();
            class_243 toTarget = target.method_19538().method_1020(aM_.field_1724.method_19538());
            double distance = Math.sqrt(toTarget.field_1352 * toTarget.field_1352 + toTarget.field_1350 * toTarget.field_1350);
            if (!(distance < 0.01)) {
               double difference = distance - wanted;
               double strength = this.c.c().floatValue();
               double stepX = toTarget.field_1352 / distance * difference * strength;
               double stepZ = toTarget.field_1350 / distance * difference * strength;
               class_243 velocity = aM_.field_1724.method_18798();
               double motionY = this.d.c() ? Math.max(velocity.field_1351, -0.08) : velocity.field_1351;
               aM_.field_1724.method_18800(stepX, motionY, stepZ);
            }
         }
      }
   }
}

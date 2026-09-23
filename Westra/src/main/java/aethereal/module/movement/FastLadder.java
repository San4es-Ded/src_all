package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.MoveEvent;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2680;

@ModuleRegister(
   a = "Fast Ladder",
   b = "Ускоряет подъём по лестницам и лианам",
   c = Category.Movement
)
public class FastLadder extends Module implements Interface {
   private final SliderSetting b = new SliderSetting("Скорость подъёма", 0.6F, 0.2F, 1.0F, 0.05F);

   public FastLadder() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(MoveEvent event) {
      if (aM_.field_1724 != null && aM_.field_1687 != null) {
         double middleY = (aM_.field_1724.method_5829().field_1322 + aM_.field_1724.method_5829().field_1325) / 2.0;
         class_2680 state = aM_.field_1687.method_8320(class_2338.method_49637(aM_.field_1724.method_23317(), middleY, aM_.field_1724.method_23321()));
         if (state.method_27852(class_2246.field_9983) || state.method_27852(class_2246.field_10597)) {
            event.b(true);
            aM_.field_1724.method_24830(true);
            if (aM_.field_1724.field_3913.field_54155.comp_3163()) {
               event.b(this.b.c().floatValue());
            }
         }
      }
   }
}

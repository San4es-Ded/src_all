package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import net.minecraft.class_1268;
import net.minecraft.class_1511;
import net.minecraft.class_3966;

@ModuleRegister(
   a = "Crystal Optimizer",
   b = "Убирает задержку разбивания энд-кристаллов при зажатой атаке",
   c = Category.Combat
)
public class CrystalOptimizer extends Module implements Interface {
   private final BooleanSetting b = new BooleanSetting("Взмах рукой", true);

   public CrystalOptimizer() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1687 != null && aM_.field_1761 != null && aM_.field_1755 == null) {
         if (aM_.field_1690.field_1886.method_1434()) {
            if (aM_.field_1765 instanceof class_3966 hit && hit.method_17782() instanceof class_1511 crystal) {
               aM_.field_1724.method_7350();
               aM_.field_1761.method_2918(aM_.field_1724, crystal);
               if (this.b.c()) {
                  aM_.field_1724.method_6104(class_1268.field_5808);
               }
            }
         }
      }
   }
}

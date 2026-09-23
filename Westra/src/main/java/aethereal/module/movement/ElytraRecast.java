package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.InputEvent;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import net.minecraft.class_2848;
import net.minecraft.class_2848.class_2849;

@ModuleRegister(
   a = "Elytra Recast",
   b = "Перезапускает полёт на элитре по нажатию прыжка, не давая ему прерваться",
   c = Category.Movement
)
public class ElytraRecast extends Module implements Interface {
   private final SliderSetting b = new SliderSetting("Задержка (тики)", 10.0F, 1.0F, 20.0F, 1.0F);
   private int cooldown;

   public ElytraRecast() {
      this.a(new Setting[]{this.b});
   }

   @Override
   public void b() {
      this.cooldown = 0;
      super.b();
   }

   @EventTarget
   public void a(InputEvent event) {
      if (aM_.field_1724 != null && aM_.field_1755 == null) {
         if (aM_.field_1724.method_6128() && event.d() && this.cooldown <= 0) {
            aM_.field_1724.field_3944.method_52787(new class_2848(aM_.field_1724, class_2849.field_12982));
            this.cooldown = this.b.c().intValue();
         } else if (this.cooldown > 0) {
            this.cooldown--;
         }
      }
   }
}

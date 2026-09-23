package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.InputEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import net.minecraft.class_2848;
import net.minecraft.class_2848.class_2849;

@ModuleRegister(
   a = "No Crouch",
   b = "Убирает замедление от приседания, не мешая самому приседанию",
   c = Category.Movement
)
public class NoCrouch extends Module implements Interface {
   private final BooleanSetting b = new BooleanSetting("Убирать замедление", true);
   private final BooleanSetting c = new BooleanSetting("Скрывать приседание от сервера", false);
   private boolean d;

   public NoCrouch() {
      this.a(new Setting[]{this.b, this.c});
   }

   public boolean q() {
      return this.b.c();
   }

   @EventTarget
   public void a(InputEvent event) {
      if (!this.c.c()) {
         this.d = false;
      } else if (aM_.field_1724 != null && aM_.field_1724.field_3944 != null) {
         boolean sneaking = event.e();
         class_2849 mode = sneaking ? class_2849.field_12979 : (this.d ? class_2849.field_12984 : null);
         if (mode != null) {
            aM_.field_1724.field_3944.method_52787(new class_2848(aM_.field_1724, mode));
         }

         this.d = sneaking;
         event.c(false);
      }
   }

   @Override
   public void c() {
      if (this.d && aM_.field_1724 != null && aM_.field_1724.field_3944 != null) {
         aM_.field_1724.field_3944.method_52787(new class_2848(aM_.field_1724, class_2849.field_12984));
      }

      this.d = false;
      super.c();
   }
}

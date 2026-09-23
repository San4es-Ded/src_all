package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import lombok.Generated;

@ModuleRegister(
   a = "No Interact",
   b = "Блокирует случайное взаимодействие с контейнерами и блоками",
   c = Category.Misc
)
public class NoInteract extends Module {
   private final BooleanSetting b = new BooleanSetting("Учитывать включённую Aura", true);

   @Generated
   public BooleanSetting q() {
      return this.b;
   }

   public NoInteract() {
      this.a(new Setting[]{this.b});
   }
}

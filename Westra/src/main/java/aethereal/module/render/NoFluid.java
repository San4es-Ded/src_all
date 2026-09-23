package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import lombok.Generated;

@ModuleRegister(
   a = "No Fluid",
   b = "Убирает наложения жидкостей и огня с экрана",
   c = Category.Render
)
public class NoFluid extends Module {
   private final BooleanSetting b = new BooleanSetting("Вода", true);
   private final BooleanSetting c = new BooleanSetting("Огонь", true);
   private final BooleanSetting d = new BooleanSetting("Блок перед лицом", true);

   public NoFluid() {
      this.a(new Setting[]{this.b, this.c, this.d});
   }

   @Generated
   public boolean q() {
      return this.m() && this.b.c();
   }

   @Generated
   public boolean r() {
      return this.m() && this.c.c();
   }

   @Generated
   public boolean s() {
      return this.m() && this.d.c();
   }
}

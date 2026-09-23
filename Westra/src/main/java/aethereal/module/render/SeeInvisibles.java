package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import lombok.Generated;

@ModuleRegister(
   a = "See Invisibles",
   b = "Делает невидимых игроков видимыми",
   c = Category.Render
)
public class SeeInvisibles extends Module {
   private final SliderSetting b = new SliderSetting("Прозрачность", 0.5F, 0.1F, 1.0F, 0.1F);

   @Generated
   public SliderSetting r() {
      return this.b;
   }

   public SeeInvisibles() {
      this.a(new Setting[]{this.b});
   }

   public float q() {
      return this.b.c();
   }

   @EventTarget
   public void a(TickEvent event) {
   }
}

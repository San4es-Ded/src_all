package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.CounterUtil;
import lombok.Generated;

@ModuleRegister(
   a = "Item Scroller",
   b = "Позволяет быстро перекладывать предметы в окнах прокруткой",
   c = Category.Player
)
public class ItemScroller extends Module {
   private final SliderSetting b = new SliderSetting("Задержка между слотами", 50.0F, 0.0F, 100.0F, 1.0F);
   private final CounterUtil c = new CounterUtil();

   @Generated
   public SliderSetting q() {
      return this.b;
   }

   @Generated
   public CounterUtil r() {
      return this.c;
   }

   public ItemScroller() {
      this.a(new Setting[]{this.b});
   }
}

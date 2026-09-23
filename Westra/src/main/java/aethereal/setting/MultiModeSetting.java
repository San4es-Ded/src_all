package aethereal.setting;

import aethereal.ui.element.Element_2;
import aethereal.ui.element.MultiModeElement;
import java.util.Arrays;
import java.util.List;

public class MultiModeSetting extends Setting<List<BooleanSetting>> {
   public MultiModeSetting(String name, BooleanSetting... strings) {
      super(name, Arrays.asList(strings));
   }

   @Override
   public Element_2<?> d() {
      return new MultiModeElement(this);
   }

   public BooleanSetting a(String settingName) {
      return this.c().stream().filter(booleanSetting -> booleanSetting.i().equalsIgnoreCase(settingName)).findFirst().orElse(null);
   }

   public BooleanSetting a(int index) {
      if (index >= 0 && index < this.c().size()) {
         return this.c().get(index);
      } else {
         throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for size " + this.c().size());
      }
   }
}

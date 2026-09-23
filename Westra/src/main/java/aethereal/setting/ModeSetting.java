package aethereal.setting;

import aethereal.ui.element.Element_2;
import aethereal.ui.element.ModeElement;
import java.util.Arrays;
import java.util.List;
import lombok.Generated;

public class ModeSetting extends Setting<String> {
   private final List<String> a;

   @Generated
   public List<String> k() {
      return this.a;
   }

   public ModeSetting(String name, String defaultVal, String... strings) {
      super(name, defaultVal);
      this.a = Arrays.asList(strings);
   }

   public boolean l(String settingName) {
      return this.c().equalsIgnoreCase(settingName);
   }

   @Override
   public Element_2<?> d() {
      return new ModeElement(this);
   }
}

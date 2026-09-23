package pyrock;









import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import java.util.List;
import net.minecraft.text.Text;
import pyrock.classes.PyDynamicIsland;
import pyrock.classes.PyModule;
import pyrock.classes.PyMusic;
import pyrock.classes.settings.PyBooleanSetting;
import pyrock.classes.settings.PyButtonSetting;
import pyrock.classes.settings.PyColorSetting;
import pyrock.classes.settings.PyModeSetting;
import pyrock.classes.settings.PyRangeSetting;
import pyrock.classes.settings.PySelectSetting;
import pyrock.classes.settings.PySliderSetting;
import rockstar.client.render.Fonts;
import rockstar.client.setting.Setting;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.render.CornerRadii;
import rockstar.client.setting.ButtonSetting;
import rockstar.client.setting.ColorSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.RangeSetting;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.internal.script.ConfigManager;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleEntry;
import rockstar.client.RockstarClient;
import rockstar.client.internal.core.AbstractMenuScreen;
import rockstar.client.internal.core.MenuLayoutProvider;
import rockstar.client.util.ClientMessages;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.MinecraftClientAccess;

public class Client {
   public void msg(String localValue1) {
      ClientMessages.internalMethod01809(Text.of(localValue1));
   }

   public void warn(String localValue1) {
      ClientMessages.internalMethod03058(Text.of(localValue1));
   }

   public void error(String localValue1) {
      ClientMessages.internalMethod09025(Text.of(localValue1));
   }

   public void overlay(String localValue1) {
      ClientMessages.internalMethod04056(ClientMessages.InternalType0214.internalField1130, Text.of(localValue1));
   }

   public void cursor(String localValue1) {
      String localValue3 = localValue1.toLowerCase();

      CursorType localValue2 = switch (localValue3) {
         case "arrow", "default" -> CursorType.internalField0566;
         case "hand", "pointer" -> CursorType.internalField0567;
         case "text", "ibeam" -> CursorType.internalField1206;
         case "crosshair" -> CursorType.internalField1207;
         case "hresize", "horizontal" -> CursorType.internalField1208;
         case "vresize", "vertical" -> CursorType.internalField1205;
         case "block", "notallowed" -> CursorType.internalField1562;
         case "resize", "resizeall" -> CursorType.internalField1563;
         default -> CursorType.internalField0566;
      };
      CursorManager.internalMethod06882(localValue2);
   }

   public Object find(String localValue1, String localValue2) {
      ModuleEntry localValue3;
      try {
         localValue3 = RockstarClient.getInstance().getModuleManager().getModuleByName(localValue1);
      } catch (Exception localValue6) {
         return null;
      }

      if (localValue3 == null) {
         return null;
      } else if (localValue2 != null && !localValue2.isEmpty()) {
         for (Setting localValue5 : localValue3.getSettings()) {
            if (matchesSetting(localValue5, localValue2)) {
               return this.wrapSetting(localValue5);
            }
         }

         return null;
      } else {
         return new PyModule(localValue3);
      }
   }

   public PyDynamicIsland island() {
      return new PyDynamicIsland();
   }

   public PyDynamicIsland dynamicIsland() {
      return this.island();
   }

   public PyMusic music() {
      return new PyMusic();
   }

   public boolean menu_opened() {
      return MinecraftClientAccess.internalField0149.currentScreen instanceof AbstractMenuScreen
         || MinecraftClientAccess.internalField0149.currentScreen instanceof MenuLayoutProvider;
   }

   public float fontWidth(String localValue1, float localValue2, String localValue3) {
      return Fonts.internalMethod00574(localValue1).internalMethod01432(localValue2).internalMethod00965(localValue3);
   }

   public float fontHeight(String localValue1, float localValue2) {
      return Fonts.internalMethod00574(localValue1).internalMethod01432(localValue2).internalMethod04890();
   }

   public Object find(String localValue1) {
      return this.find(localValue1, null);
   }

   private static boolean matchesSetting(Setting localValue0, String localValue1) {
      String localValue2 = localValue0.getName();
      if (!looseEquals(localValue2, localValue1) && !looseEquals(LanguageManager.internalMethod07214(localValue2), localValue1)) {
         int localValue3 = localValue2.lastIndexOf(46);
         return localValue3 >= 0 && localValue3 < localValue2.length() - 1 && looseEquals(localValue2.substring(localValue3 + 1), localValue1);
      } else {
         return true;
      }
   }

   private static boolean looseEquals(String localValue0, String localValue1) {
      return localValue0 != null && localValue1 != null ? localValue0.replace(" ", "").equalsIgnoreCase(localValue1.replace(" ", "")) : false;
   }

   private Object wrapSetting(Setting localValue1) {
      if (localValue1 instanceof BooleanSetting) {
         return new PyBooleanSetting((BooleanSetting)localValue1);
      } else if (localValue1 instanceof SliderSetting) {
         return new PySliderSetting((SliderSetting)localValue1);
      } else if (localValue1 instanceof ModeSetting) {
         return new PyModeSetting((ModeSetting)localValue1);
      } else if (localValue1 instanceof MultiSelectSetting) {
         return new PySelectSetting((MultiSelectSetting)localValue1);
      } else if (localValue1 instanceof RangeSetting) {
         return new PyRangeSetting((RangeSetting)localValue1);
      } else if (localValue1 instanceof ColorSetting) {
         return new PyColorSetting((ColorSetting)localValue1);
      } else {
         return localValue1 instanceof ButtonSetting ? new PyButtonSetting((ButtonSetting)localValue1) : null;
      }
   }

   public List<PyModule> modules() {
      return RockstarClient.getInstance().getModuleManager().getModules().stream().map(PyModule::new).toList();
   }

   public String gameDir() {
      return ConfigManager.internalField0148.getAbsolutePath();
   }

   public CornerRadii border(double localValue1) {
      return CornerRadii.internalMethod03908((float)localValue1);
   }

   public CornerRadii border4(double localValue1, double localValue3, double localValue5, double localValue7) {
      return new CornerRadii((float)localValue1, (float)localValue3, (float)localValue5, (float)localValue7);
   }
}

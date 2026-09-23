package aethereal.config;

import aethereal.lib.json.JSONObject;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ColorSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.setting.StringSetting;

public final class ConverterUtil {
   private ConverterUtil() {
   }

   public static Object a(Setting<?> setting) {
      if (!(setting instanceof MultiModeSetting multi)) {
         return setting.c();
      } else {
         JSONObject object = new JSONObject();

         for (BooleanSetting child : multi.c()) {
            object.b(child.i(), child.c());
         }

         return object;
      }
   }

   public static void a(Setting<?> setting, Object value) {
      if (setting instanceof BooleanSetting booleanSetting && value instanceof Boolean b) {
         booleanSetting.a(b);
      } else if (setting instanceof ModeSetting modeSetting && value instanceof String s) {
         modeSetting.a(s);
      } else if (setting instanceof StringSetting stringSetting && value instanceof String s) {
         stringSetting.a(s);
      } else if (setting instanceof SliderSetting sliderSetting && value instanceof Number n) {
         sliderSetting.a(n.floatValue());
      } else if (setting instanceof BindSetting bindSetting && value instanceof Number n) {
         bindSetting.a(n.intValue());
      } else if (setting instanceof ColorSetting colorSetting && value instanceof Number n) {
         colorSetting.a(n.intValue());
      } else {
         if (setting instanceof MultiModeSetting multiModeSetting && value instanceof org.json.JSONObject obj) {
            for (BooleanSetting child : multiModeSetting.c()) {
               if (obj.has(child.i())) {
                  child.a(obj.optBoolean(child.i()));
               }
            }
         }
      }
   }
}

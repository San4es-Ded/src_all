package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ColorSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_332;

@ModuleRegister(
   a = "Item Highlight",
   b = "Заливает ячейки выбранных предметов своим цветом, чтобы находить их взглядом",
   c = Category.Render
)
public class ItemHighlight extends Module implements Interface {
   private final SliderSetting b = new SliderSetting("Непрозрачность", 80.0F, 0.0F, 100.0F, 1.0F);
   private final Map<class_1792, ItemHighlight.a> c = new LinkedHashMap<>();

   public ItemHighlight() {
      this.a(new Setting[]{this.b});
      this.q(class_1802.field_8634, "Эндер-жемчуг", ColorUtil.a(0, 150, 0, 255));
      this.q(class_1802.field_8543, "Снежок", ColorUtil.a(100, 200, 255, 255));
      this.q(class_1802.field_22021, "Незеритовый лом", ColorUtil.a(200, 180, 150, 255));
      this.q(class_1802.field_16539, "Светильник", ColorUtil.a(255, 150, 0, 255));
      this.q(class_1802.field_8814, "Огненный шар", ColorUtil.a(255, 0, 0, 255));
      this.q(class_1802.field_8288, "Тотем бессмертия", ColorUtil.a(255, 200, 0, 255));
      this.q(class_1802.field_8399, "Арбалет", ColorUtil.a(150, 100, 50, 255));
      this.q(class_1802.field_22022, "Незеритовый меч", ColorUtil.a(90, 80, 80, 255));
      this.q(class_1802.field_49814, "Булава", ColorUtil.a(120, 110, 180, 255));
      this.q(class_1802.field_8233, "Плод хоруса", ColorUtil.a(200, 100, 200, 255));
      this.q(class_1802.field_8479, "Сахар", ColorUtil.a(255, 255, 255, 255));
      this.q(class_1802.field_8614, "Мембрана фантома", ColorUtil.a(200, 180, 150, 255));
      this.q(class_1802.field_8449, "Око Эндера", ColorUtil.a(100, 0, 200, 255));
      this.q(class_1802.field_8551, "Сушёные водоросли", ColorUtil.a(100, 150, 50, 255));
      this.q(class_1802.field_8287, "Пузырёк опыта", ColorUtil.a(0, 200, 100, 255));
      this.q(class_1802.field_8463, "Золотое яблоко", ColorUtil.a(255, 200, 0, 255));
      this.q(class_1802.field_8367, "Зачарованное яблоко", ColorUtil.a(255, 150, 0, 255));
      this.q(class_1802.field_8574, "Зелье", ColorUtil.a(160, 90, 220, 255));
      this.q(class_1802.field_8436, "Взрывное зелье", ColorUtil.a(200, 90, 200, 255));
      this.q(class_1802.field_8833, "Элитры", ColorUtil.a(180, 180, 210, 255));
   }

   private void q(class_1792 item, String name, int color) {
      BooleanSetting enabled = new BooleanSetting(name, true);
      ColorSetting tint = new ColorSetting(name + " — цвет", color).a(() -> enabled.c());
      this.a(new Setting[]{enabled, tint});
      this.c.put(item, new ItemHighlight.a(enabled, tint));
   }

   public int q(class_1799 stack) {
      if (this.m() && stack != null && !stack.method_7960()) {
         ItemHighlight.a entry = this.c.get(stack.method_7909());
         if (entry != null && entry.a().c()) {
            float opacity = Math.max(0.0F, Math.min(1.0F, this.b.c() / 100.0F));
            int alpha = Math.round(255.0F * opacity);
            return alpha <= 0 ? 0 : alpha << 24 | entry.b().c() & 16777215;
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public void a(class_332 context, class_1799 stack, int x, int y) {
      int color = this.q(stack);
      if (color != 0) {
         context.method_25294(x, y, x + 16, y + 16, color);
      }
   }

   private record a(BooleanSetting a, ColorSetting b) {
   }
}

package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import net.minecraft.class_1753;
import net.minecraft.class_1764;
import net.minecraft.class_1799;
import net.minecraft.class_1835;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_7924;

@ModuleRegister(
   a = "Auto Release",
   b = "Сам отпускает лук, арбалет и трезубец в момент нужного заряда",
   c = Category.Player
)
public class AutoRelease extends Module implements Interface {
   private final MultiModeSetting b = new MultiModeSetting(
      "Предметы", new BooleanSetting("Лук", true), new BooleanSetting("Арбалет", true), new BooleanSetting("Трезубец", true)
   );
   private final ModeSetting c = new ModeSetting("Режим лука", "Максимальный", "Спам", "Максимальный").a(() -> this.b.a("Лук").c());

   public AutoRelease() {
      this.a(new Setting[]{this.b, this.c});
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1761 != null && aM_.field_1724.method_6115()) {
         class_1799 stack = aM_.field_1724.method_6030();
         int useTime = aM_.field_1724.method_6048();
         boolean release = false;
         if (stack.method_7909() instanceof class_1753) {
            release = this.b.a("Лук").c() && useTime >= (this.c.l("Спам") ? 3 : 20);
         } else if (stack.method_7909() instanceof class_1764) {
            release = this.b.a("Арбалет").c() && useTime >= 25 - this.quickCharge(stack) * 5;
         } else if (stack.method_7909() instanceof class_1835) {
            release = this.b.a("Трезубец").c() && useTime > 9;
         }

         if (release) {
            aM_.field_1761.method_2897(aM_.field_1724);
         }
      }
   }

   private int quickCharge(class_1799 stack) {
      return aM_.field_1687 != null && stack.method_7942()
         ? class_1890.method_8225(aM_.field_1687.method_30349().method_30530(class_7924.field_41265).method_46747(class_1893.field_9098), stack)
         : 0;
   }
}

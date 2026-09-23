package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.setting.StringSetting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ModuleRegister(
   a = "Auto Commands",
   b = "Сам отправляет выбранные команды по кулдауну, разнося их по очереди",
   c = Category.Misc
)
public class AutoCommands extends Module implements Interface {
   private static final long b = 10000L;
   private final MultiModeSetting c = new MultiModeSetting(
      "Команды",
      new BooleanSetting("/fix all", true),
      new BooleanSetting("/heal", true),
      new BooleanSetting("/kit", false),
      new BooleanSetting("/repair", false)
   );
   private final SliderSetting d2 = new SliderSetting("Кулдаун", 60.0F, 10.0F, 360.0F, 1.0F);
   private final StringSetting e = new StringSetting("Свои команды", "");
   private final BooleanSetting f2 = new BooleanSetting("Не отправлять в меню", true);
   private final Map<String, Long> g2 = new HashMap<>();
   private List<String> h2 = new ArrayList<>();

   public AutoCommands() {
      this.a(new Setting[]{this.c, this.e, this.d2, this.f2});
   }

   @Override
   public void b() {
      this.g2.clear();
      this.h2 = new ArrayList<>();
      super.b();
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1724.field_3944 != null && aM_.field_1687 != null) {
         if (!this.f2.c() || aM_.field_1755 == null) {
            long now = System.currentTimeMillis();
            List<String> selected = this.q();
            if (!selected.equals(this.h2)) {
               this.h2 = selected;
               this.g2.clear();

               for (int index = 0; index < selected.size(); index++) {
                  this.g2.put(selected.get(index), now + index * 10000L);
               }
            }

            if (!selected.isEmpty()) {
               long cooldown = (long)this.d2.c().floatValue() * 1000L;

               for (String command : selected) {
                  Long next = this.g2.get(command);
                  if (next == null || now >= next) {
                     this.r(command);
                     this.g2.put(command, now + cooldown);
                  }
               }
            }
         }
      }
   }

   private List<String> q() {
      List<String> result = new ArrayList<>();

      for (BooleanSetting setting : this.c.c()) {
         if (setting.c()) {
            result.add(setting.i());
         }
      }

      for (String custom : this.e.c().split(",")) {
         String trimmed = custom.trim();
         if (!trimmed.isEmpty()) {
            result.add(trimmed);
         }
      }

      return result;
   }

   private void r(String command) {
      String text = command.startsWith("/") ? command.substring(1) : command;
      text = text.trim();
      if (!text.isEmpty()) {
         aM_.field_1724.field_3944.method_45730(text);
      }
   }
}

package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.setting.StringSetting;
import aethereal.util.ChatUtil;
import aethereal.util.ServerUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import java.util.Iterator;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_2813;
import net.minecraft.class_7439;

@ModuleRegister(
   a = "Auto Sell",
   b = "Выставляет выбранные предметы на аукцион по заданной цене",
   c = Category.Misc
)
public class AutoSell extends Module {
   private final StringSetting b = new StringSetting("Команда продажи", "ah sell");
   private final StringSetting c = new StringSetting("Название предметов", "");
   private final SliderSetting d = new SliderSetting("Цена за предмет", 10000.0F, 100.0F, 1000000.0F, 100.0F);
   private final SliderSetting e = new SliderSetting("Задержка, тиков", 20.0F, 5.0F, 100.0F, 5.0F);
   private final BooleanSetting f = new BooleanSetting("Пропускать дороже цены", true);
   private final BooleanSetting g = new BooleanSetting("Сообщать в чат", true);
   private int h;
   private int i;
   private class_1799 j = class_1799.field_8037;

   public AutoSell() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e, this.f, this.g});
   }

   @Override
   public void b() {
      super.b();
      this.h = 0;
      this.i = 0;
      this.j = class_1799.field_8037;
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1724.field_3944 != null) {
         int delay = this.h + 1;
         this.h = delay;
         if (delay >= Math.round(this.e.c())) {
            this.h = 0;
            class_1703 handler = aM_.field_1724.field_7512;
            class_1735 target = this.a(handler);
            if (target != null) {
               this.j = target.method_7677().method_7972();
               this.a(handler, target.field_7874, class_1713.field_7790);
               aM_.field_1724.field_3944.method_45730(this.b.c().trim() + " " + Math.round(this.d.c()));
            }
         }
      }
   }

   private class_1735 a(class_1703 handler) {
      String needle = this.c.c() == null ? "" : this.c.c().trim().toLowerCase();
      if (needle.isEmpty()) {
         return null;
      } else {
         int limit = Math.round(this.d.c());
         Iterator var4 = handler.field_7761.iterator();

         class_1735 slot;
         while (true) {
            if (!var4.hasNext()) {
               return null;
            }

            slot = (class_1735)var4.next();
            if (slot.field_7871 == aM_.field_1724.method_31548()) {
               class_1799 stack = slot.method_7677();
               if (!stack.method_7960()) {
                  String name = stack.method_7964().getString().replaceAll("§.", "").toLowerCase();
                  if (name.contains(needle)) {
                     if (!this.f.c()) {
                        break;
                     }

                     int lore = ServerUtil.a.a(stack);
                     if (lore <= 0 || lore <= limit) {
                        break;
                     }
                  }
               }
            }
         }

         return slot;
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.d() instanceof class_7439) {
         String message = ((class_7439)event.d()).comp_763().getString();
         if (message.contains("выставили") || message.contains("выставлен")) {
            this.i++;
            if (this.g.c() && !this.j.method_7960()) {
               ChatUtil.a("Выставлено: &c" + this.j.method_7964().getString() + "&7. Всего: &c" + this.i + "&7.");
            }

            this.j = class_1799.field_8037;
         }
      }
   }

   private void a(class_1703 handler, int slot, class_1713 action) {
      aM_.field_1724
         .field_3944
         .method_52787(
            new class_2813(handler.field_7763, handler.method_37421(), slot, 0, action, handler.method_34255().method_7972(), Int2ObjectMaps.emptyMap())
         );
   }
}

package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.DropItemEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.util.ChatUtil;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2248;
import net.minecraft.class_2480;
import net.minecraft.class_9288;
import net.minecraft.class_9334;

@ModuleRegister(
   a = "Anti Drop",
   b = "Не даёт случайно выбросить ценные предметы",
   c = Category.Player
)
public class AntiDrop extends Module {
   private final MultiModeSetting b = new MultiModeSetting(
      "Не выбрасывать",
      new BooleanSetting("Шалкеры", true),
      new BooleanSetting("Тотемы", true),
      new BooleanSetting("Элитры", true),
      new BooleanSetting("Зачарованные книги", false)
   );
   private final BooleanSetting e = new BooleanSetting("Шалкеры только с лутом", false);
   private final BooleanSetting c = new BooleanSetting("Сообщать о блокировке", true);
   private long d;

   public AntiDrop() {
      this.a(new Setting[]{this.b, this.e, this.c});
   }

   @EventTarget
   public void a(DropItemEvent event) {
      if (aM_.field_1724 != null) {
         class_1799 stack = aM_.field_1724.method_31548().method_5438(event.b());
         if (!stack.method_7960() && this.a(stack)) {
            event.a(true);
            if (this.c.c() && System.currentTimeMillis() - this.d > 2000L) {
               this.d = System.currentTimeMillis();
               ChatUtil.a("Выброс отменён: " + stack.method_7964().getString() + ".");
            }
         }
      }
   }

   private boolean a(class_1799 stack) {
      if (stack.method_7909() == class_1802.field_8288) {
         return this.a("Тотемы");
      } else if (stack.method_7909() == class_1802.field_8833) {
         return this.a("Элитры");
      } else if (stack.method_7909() == class_1802.field_8598) {
         return this.a("Зачарованные книги");
      } else {
         return b(stack) && this.a("Шалкеры") ? !this.e.c() || c(stack) : false;
      }
   }

   private static boolean b(class_1799 stack) {
      if (!(stack.method_7909() instanceof class_1747)) {
         return false;
      } else {
         class_2248 block = ((class_1747)stack.method_7909()).method_7711();
         return block instanceof class_2480;
      }
   }

   private static boolean c(class_1799 stack) {
      class_9288 contents = (class_9288)stack.method_57824(class_9334.field_49622);
      return contents != null && contents.method_59712().findAny().isPresent();
   }

   private boolean a(String option) {
      BooleanSetting setting = this.b.a(option);
      return setting != null && setting.c();
   }
}

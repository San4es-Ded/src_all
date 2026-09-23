package aethereal.module.misc;

import aethereal.autobuy.AutoBuyEntry;
import aethereal.core.Category;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.setting.BindSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.ui.screen.AssistantScreen;
import lombok.Generated;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_437;

@ModuleRegister(
   a = "Potion Thrower",
   b = "Быстрое метание бафов через колесо или по клавише",
   c = Category.Misc
)
public class PotionThrower extends Module implements Interface {
   private final ModeSetting b = new ModeSetting("Способ использования зелий", "Колесо выбора", "Колесо выбора", "Клавиша");
   private final AssistantScreen c = new AssistantScreen(class_2561.method_43470("Potion Thrower"));
   private final BindSetting d = new BindSetting("Открыть меню зелий", 86, 0).a(() -> aM_.method_1507(this.c)).b(() -> {
      if (aM_.field_1755 == this.c) {
         this.c.b(this.c.b());
         if (aM_.field_1755 == this.c) {
            aM_.method_1507((class_437)null);
         }
      }
   }).a(() -> this.b.l("Колесо выбора"));

   @Generated
   public AssistantScreen q() {
      return this.c;
   }

   public PotionThrower() {
      this.a(new Setting[]{this.b, this.d});

      for (AutoBuyEntry potion : AutoBuyEntry.values()) {
         if (potion.d() == class_1802.field_8436) {
            this.a(new Setting[]{new BindSetting(potion.b(), -1).a(() -> Westra.h().d().v().b().a(potion.a())).a(() -> this.b.l("Клавиша"))});
         }
      }
   }
}

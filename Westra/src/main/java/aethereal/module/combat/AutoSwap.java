package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.setting.BindSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.ui.screen.SwapScreen;
import aethereal.util.InventoryUtil;
import lombok.Generated;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_437;

@ModuleRegister(
   a = "Auto Swap",
   b = "Мгновенно перекладывает выбранные предметы во вторую руку по нажатию клавиши",
   c = Category.Combat
)
public class AutoSwap extends Module implements Interface {
   private final ModeSetting b = new ModeSetting("Режим перемещения", "Двойной", "Двойной", "Тройной");
   private final ModeSetting c = new ModeSetting("Первый предмет", "Сфера", "Сфера", "Тотем", "Золотое яблоко", "Щит").a(() -> this.b.l("Двойной"));
   private final ModeSetting d = new ModeSetting("Второй предмет", "Тотем", "Сфера", "Тотем", "Золотое яблоко", "Щит").a(() -> this.b.l("Двойной"));
   private final SwapScreen e = new SwapScreen(class_2561.method_43470("SwapMenu"));
   private final BindSetting f = new BindSetting("Кнопка перемещения", 86, 0).a(() -> {
      if (!Westra.h().d().t().V().b) {
         if (this.b.l("Двойной")) {
            Westra.h().d().v().a().a(InventoryUtil.c(aM_.field_1724.method_6079().method_7909() == this.a(this.c) ? this.a(this.d) : this.a(this.c)), 45, 1);
         } else if (this.b.l("Тройной")) {
            aM_.method_1507(this.e);
         }
      }
   }).b(() -> {
      if (aM_.field_1755 instanceof SwapScreen) {
         aM_.method_1507((class_437)null);
      }
   });

   @Generated
   public SwapScreen q() {
      return this.e;
   }

   public AutoSwap() {
      this.a(new Setting[]{this.f, this.b, this.c, this.d});
   }

   private class_1792 a(ModeSetting modeSetting) {
      String var2 = modeSetting.c();
      switch (var2) {
         case "Сфера":
            return class_1802.field_8575;
         case "Тотем":
            return class_1802.field_8288;
         case "Золотое яблоко":
            return class_1802.field_8463;
         case "Щит":
            return class_1802.field_8255;
         default:
            return null;
      }
   }
}

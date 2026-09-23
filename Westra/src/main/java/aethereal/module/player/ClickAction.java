package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.setting.BindSetting;
import aethereal.setting.Setting;
import aethereal.util.ChatUtil;
import lombok.Generated;
import net.minecraft.class_1802;
import net.minecraft.class_3966;
import net.minecraft.class_742;

@ModuleRegister(
   a = "Click Action",
   b = "Выполняет действие, привязанное к выбранной клавише",
   c = Category.Player
)
public class ClickAction extends Module implements Interface {
   private final BindSetting b = new BindSetting("Эндер-жемчуг", -1).a(() -> Westra.h().d().v().b().a(class_1802.field_8634.method_7854()));
   private final BindSetting c = new BindSetting("Добавление друга", -1)
      .a(
         () -> {
            class_3966 class_3966Var = aM_.field_1765 instanceof class_3966 ? (class_3966)aM_.field_1765 : null;
            if (class_3966Var instanceof class_3966
               && class_3966Var.method_17782() instanceof class_742 class_746VarMethod_17782
               && class_746VarMethod_17782 != aM_.field_1724) {
               String name = class_746VarMethod_17782.method_5477().getString();
               if (Westra.h().d().e().d(name)) {
                  Westra.h().d().e().c(name);
                  Westra.h().d().e().unSetup();
                  ChatUtil.a("Товарищ " + name + " был успешно удален из списка друзей.");
               } else {
                  Westra.h().d().e().b(name);
                  Westra.h().d().e().unSetup();
                  ChatUtil.a("Товарищ " + name + " был успешно добавлен в список друзей.");
               }
            }
         }
      );

   @Generated
   public BindSetting q() {
      return this.b;
   }

   @Generated
   public BindSetting r() {
      return this.c;
   }

   public ClickAction() {
      this.a(new Setting[]{this.b, this.c});
   }
}

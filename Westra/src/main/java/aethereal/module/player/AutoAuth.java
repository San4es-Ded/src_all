package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.Setting;
import aethereal.setting.StringSetting;
import aethereal.util.ServerUtil;
import lombok.Generated;
import net.minecraft.class_7439;

@ModuleRegister(
   a = "Auto Auth",
   b = "Автоматически вводит пароль при авторизации и регистрации",
   c = Category.Player
)
public class AutoAuth extends Module implements Interface {
   private final StringSetting b = new StringSetting("Пароль авторизации", "").a();
   private final StringSetting c = new StringSetting("Пароль регистрации", "").a();
   private String d;

   @Generated
   public StringSetting q() {
      return this.b;
   }

   @Generated
   public StringSetting r() {
      return this.c;
   }

   @Generated
   public String s() {
      return this.d;
   }

   public AutoAuth() {
      this.a(new Setting[]{this.b, this.c});
   }

   @EventTarget
   public void a(PacketEvent eventPacket) {
      if (eventPacket.c() && eventPacket.d() instanceof class_7439 class_7439VarD) {
         String message = class_7439VarD.comp_763().getString();
         if ((message.contains("Зарегистрируйтесь") || message.contains("/reg") || message.contains("/register")) && !this.c.c().isEmpty()) {
            this.d = "/reg " + this.c.c();
         }

         if ((message.contains("Авторизуйтесь") || message.contains("Войдите в игру") || message.contains("/login")) && !this.b.c().isEmpty()) {
            this.d = "/login " + this.b.c();
         }
      }
   }

   @EventTarget
   public void a(TickEvent tickEvent) {
      if (this.d != null) {
         if (ServerUtil.a.a() && ServerUtil.a.c()) {
            return;
         }

         aM_.field_1724.field_3944.method_45729(this.d);
         this.d = null;
      }
   }
}

package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.util.ServerUtil;
import lombok.Generated;

@ModuleRegister(
   a = "Chat Helper",
   b = "Расширяет возможности чата и его настройки",
   c = Category.Misc
)
public class ChatHelper extends Module {
   private final BooleanSetting b = new BooleanSetting("Ширина под сообщение", false);
   private final BooleanSetting c = new BooleanSetting("Автоматическое /event delay", false);
   private int d = -1;

   @Generated
   public BooleanSetting q() {
      return this.b;
   }

   public ChatHelper() {
      this.a(new Setting[]{this.b, this.c});
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.c.c()) {
         int iB;
         if (ServerUtil.a.a()) {
            iB = ServerUtil.a.d();
         } else {
            iB = ServerUtil.d.a() ? ServerUtil.d.b() : -1;
         }

         if (iB == -1) {
            this.d = 0;
            return;
         }

         if (this.d == -1) {
            this.d = iB;
         } else if (iB != this.d && aM_.field_1724.field_6012 >= 5) {
            aM_.field_1724.field_3944.method_45730("event delay");
            this.d = iB;
         }
      }
   }
}

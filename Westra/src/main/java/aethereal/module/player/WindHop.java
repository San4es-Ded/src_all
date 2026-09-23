package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import lombok.Generated;
import net.minecraft.class_1802;
import net.minecraft.class_2886;

@ModuleRegister(
   a = "Wind Hop",
   b = "Автоматически прыгает после использования заряда ветра",
   c = Category.Player
)
public class WindHop extends Module implements Interface {
   private final BooleanSetting b = new BooleanSetting("Поворачивать голову вниз", true);
   private int c = -1;

   @Generated
   public BooleanSetting q() {
      return this.b;
   }

   public WindHop() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.b()
         && event.d() instanceof class_2886 class_2886VarD
         && aM_.field_1724.method_5998(class_2886VarD.method_12551()).method_31574(class_1802.field_49098)) {
         this.c = 2;
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.c > 0) {
         this.c--;
      }
   }

   @EventTarget
   public void a(InputEvent event) {
      if (this.c == 0) {
         event.b(true);
         this.c = -1;
      }
   }
}

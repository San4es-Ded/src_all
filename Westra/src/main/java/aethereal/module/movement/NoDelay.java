package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import net.minecraft.class_1747;
import platform.inject.accessors.LivingEntityAccessor;
import platform.inject.accessors.MinecraftClientAccessor;

@ModuleRegister(
   a = "No Delay",
   b = "Убирает задержку у выбранных действий",
   c = Category.Movement
)
public class NoDelay extends Module {
   public final MultiModeSetting b = new MultiModeSetting(
      "Отключить задержку на", new BooleanSetting("Поставку блоков", true), new BooleanSetting("Прыжки", true)
   );

   public NoDelay() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.b.a("Поставку блоков").c() && aM_.field_1724.method_6047().method_7909() instanceof class_1747 && !Westra.h().d().t().aS().m()) {
         ((MinecraftClientAccessor)aM_).setItemUseCooldown(0);
      }

      if (this.b.a("Прыжки").c()) {
         ((LivingEntityAccessor)aM_.field_1724).setJumpingCooldown(0);
      }
   }
}

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
import aethereal.util.ServerUtil;

@ModuleRegister(
   a = "Lock Slot",
   b = "Запрещает выбрасывать предметы из выбранных слотов",
   c = Category.Player
)
public class LockSlot extends Module {
   private final MultiModeSetting b = new MultiModeSetting(
      "Заблокированные слоты",
      new BooleanSetting("1", false),
      new BooleanSetting("2", false),
      new BooleanSetting("3", false),
      new BooleanSetting("4", false),
      new BooleanSetting("5", false),
      new BooleanSetting("6", false),
      new BooleanSetting("7", false),
      new BooleanSetting("8", false),
      new BooleanSetting("9", false)
   );
   private final BooleanSetting c = new BooleanSetting("Блокировать только в PVP", true);

   public LockSlot() {
      this.a(new Setting[]{this.c, this.b});
   }

   @EventTarget
   public void a(DropItemEvent event) {
      if ((!this.c.c() || ServerUtil.e()) && this.b.a(event.b()).c()) {
         ChatUtil.a("Попытка выброса из слота \"&c" + (event.b() + 1) + "&7\" была заблокирована");
         event.a(true);
      }
   }
}

package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import java.util.Set;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_9334;

@ModuleRegister(
   a = "Auto Eat",
   b = "Автоматически утоляет голод при его падении",
   c = Category.Player
)
public class AutoEat extends Module {
   private final SliderSetting b = new SliderSetting("Есть при голоде", 16.0F, 1.0F, 20.0F, 1.0F);

   public AutoEat() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(TickEvent event) {
      this.b(this.b.c().intValue());
   }

   public void b(int level) {
      int slot;
      if (aM_.field_1724 != null && aM_.field_1724.method_7344().method_7586() < level && (slot = this.q()) >= 0) {
         Westra.h().d().v().k().a(slot);
      }
   }

   private int q() {
      Set<class_1792> blacklist = Set.of(class_1802.field_8233, class_1802.field_8323, class_1802.field_8551);
      Set<class_1792> raw = Set.of(
         class_1802.field_8046,
         class_1802.field_8389,
         class_1802.field_8726,
         class_1802.field_8748,
         class_1802.field_8504,
         class_1802.field_8429,
         class_1802.field_8209,
         class_1802.field_8567
      );
      int bestSlot = -1;
      int bestRank = Integer.MAX_VALUE;

      for (int slot = 0; slot < 36 && bestRank > 0; slot++) {
         class_1799 stack = aM_.field_1724.method_31548().method_5438(slot);
         if (stack.method_57826(class_9334.field_50075) && !blacklist.contains(stack.method_7909()) && !stack.method_57826(class_9334.field_49631)) {
            int i;
            if (stack.method_31574(class_1802.field_8367)) {
               i = 3;
            } else if (stack.method_31574(class_1802.field_8463)) {
               i = 2;
            } else {
               i = raw.contains(stack.method_7909()) ? 1 : 0;
            }

            if (i < bestRank) {
               bestRank = i;
               bestSlot = slot;
            }
         }
      }

      return bestSlot;
   }
}

package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.SlowEvent;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.util.CounterUtil;
import net.minecraft.class_1268;
import net.minecraft.class_1713;
import net.minecraft.class_1764;
import net.minecraft.class_1839;
import net.minecraft.class_243;
import net.minecraft.class_2868;
import net.minecraft.class_2886;
import net.minecraft.class_746;

@ModuleRegister(
   a = "No Slow Down",
   b = "Убирает замедление при использовании предметов",
   c = Category.Movement
)
public class NoSlowDown extends Module {
   private final ModeSetting b = new ModeSetting("Режим использования", "Vanilla", "Vanilla", "Арбалет", "Matrix", "Grim", "Grim 50%", "Holy World");
   private final CounterUtil c = new CounterUtil();
   private boolean d;

   public NoSlowDown() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(SlowEvent slow) {
      class_746 player = aM_.field_1724;
      if (player != null && player.method_6115() && !player.method_5765()) {
         if (this.b.l("Vanilla")) {
            slow.a(true);
            this.q(slow);
         } else if (this.b.l("Арбалет")) {
            if (player.method_6030().method_7909() instanceof class_1764) {
               slow.a(true);
               this.q(slow);
            }
         } else if (this.b.l("Matrix")) {
            this.r(slow, player);
         } else if (this.b.l("Grim")) {
            this.s(slow, player);
         } else if (this.b.l("Grim 50%")) {
            if (player.method_6048() > 1 && this.c.a(100L)) {
               this.c.b();
               slow.a(true);
               this.q(slow);
            }
         } else {
            if (this.b.l("Holy World")) {
               this.t(slow, player);
            }
         }
      }
   }

   private void q(SlowEvent slow) {
      if (slow.a() && aM_.field_1724 != null) {
         aM_.field_1724.method_5728(true);
      }
   }

   private void r(SlowEvent slow, class_746 player) {
      slow.a(true);
      class_243 velocity = player.method_18798();
      boolean falling = player.field_6017 > 0.725F;
      if (player.method_24828() && !player.field_3913.field_54155.comp_3163()) {
         if (player.field_6012 % 2 == 0) {
            float factor = player.field_3913.method_3128().field_1343 == 0.0F ? 0.5F : 0.4F;
            player.method_18799(velocity.method_18805(factor, 1.0, factor));
         }

         this.q(slow);
      } else {
         if (falling) {
            float factor = player.field_6017 > 1.4F ? 0.95F : 0.97F;
            player.method_18799(velocity.method_18805(factor, 1.0, factor));
         }

         this.q(slow);
      }
   }

   private void s(SlowEvent slow, class_746 player) {
      if (player.method_6048() > 3 && player.field_3944 != null) {
         if (player.method_6058() == class_1268.field_5810) {
            if (!player.method_7357().method_7904(player.method_6079())) {
               int slot = player.method_31548().field_7545;
               player.field_3944.method_52787(new class_2868(slot + 1 > 8 ? slot - 1 : slot + 1));
               player.field_3944.method_52787(new class_2868(slot));
               slow.a(true);
            }
         } else if (!player.method_7357().method_7904(player.method_6047())) {
            player.field_3944.method_52787(new class_2886(class_1268.field_5810, 0, player.method_36454(), player.method_36455()));
            if (player.method_6079().method_7976() == class_1839.field_8952) {
               slow.a(true);
            }
         }

         this.q(slow);
      }
   }

   private void t(SlowEvent slow, class_746 player) {
      int useTime = player.method_6048();
      if (this.d && useTime < 10 && aM_.field_1761 != null && this.c.a(250L)) {
         this.c.b();
         int hotbar = 36 + player.method_31548().field_7545;
         aM_.field_1761.method_2906(0, hotbar, 40, class_1713.field_7791, player);
         aM_.field_1761.method_2906(0, 45, player.method_31548().field_7545, class_1713.field_7791, player);
      }

      if (useTime % 2 == 0 || !this.d) {
         slow.a(true);
         this.q(slow);
      }

      this.d = true;
   }

   @Override
   public void c() {
      this.d = false;
      this.c.b();
      super.c();
   }
}

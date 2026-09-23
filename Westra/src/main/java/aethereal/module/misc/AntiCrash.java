package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PacketEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.util.ChatUtil;
import net.minecraft.class_10182;
import net.minecraft.class_243;
import net.minecraft.class_2664;
import net.minecraft.class_2675;
import net.minecraft.class_2708;
import net.minecraft.class_2743;

@ModuleRegister(
   a = "Anti Crash",
   b = "Отбрасывает пакеты, которыми пытаются уронить клиент",
   c = Category.Misc
)
public class AntiCrash extends Module implements Interface {
   private static final double b = 100000.0;
   private static final double c = 3.0E7;
   private static final int d = 500;
   private final BooleanSetting e = new BooleanSetting("Анти-скорость", true);
   private final BooleanSetting f = new BooleanSetting("Анти-позиция", true);
   private final BooleanSetting g = new BooleanSetting("Анти-частицы", true);
   private final BooleanSetting h = new BooleanSetting("Сообщать в чат", true);
   private long i;

   public AntiCrash() {
      this.a(new Setting[]{this.e, this.f, this.g, this.h});
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c()) {
         Object packet = event.d();
         if (this.e.c() && packet instanceof class_2743 velocity) {
            if (q(velocity.method_11815()) || q(velocity.method_11816()) || q(velocity.method_11819())) {
               event.a(true);
               this.r("некорректная скорость движения");
            }
         } else if (this.e.c() && packet instanceof class_2664 explosion) {
            explosion.comp_2884().ifPresent(knockback -> {
               if (q(knockback.field_1352) || q(knockback.field_1351) || q(knockback.field_1350)) {
                  event.a(true);
                  this.r("некорректный отброс от взрыва");
               }
            });
         } else if (this.f.c() && packet instanceof class_2708 look) {
            class_10182 change = look.comp_3228();
            if (change != null) {
               class_243 position = change.comp_3148();
               if (position == null || s(position.field_1352) || s(position.field_1351) || s(position.field_1350)) {
                  event.a(true);
                  this.r("выход за пределы мира");
               }
            }
         } else {
            if (this.g.c() && packet instanceof class_2675 particles) {
               float speed = particles.method_11543();
               if (Float.isNaN(speed) || Float.isInfinite(speed) || particles.method_11545() > 500 || particles.method_11545() < 0) {
                  event.a(true);
                  this.r("лавина частиц");
               }
            }
         }
      }
   }

   private static boolean q(double value) {
      return Double.isNaN(value) || Double.isInfinite(value) || Math.abs(value) > 100000.0;
   }

   private static boolean s(double value) {
      return Double.isNaN(value) || Double.isInfinite(value) || Math.abs(value) > 3.0E7;
   }

   private void r(String reason) {
      if (this.h.c() && aM_.field_1724 != null) {
         long now = System.currentTimeMillis();
         if (now - this.i >= 2000L) {
            this.i = now;
            ChatUtil.a("Отброшен пакет: &c" + reason + "&7.");
         }
      }
   }
}

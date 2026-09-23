package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.util.Look;
import net.minecraft.class_1282;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_2743;
import net.minecraft.class_3532;
import net.minecraft.class_8143;

@ModuleRegister(
   a = "Velocity",
   b = "Не позволяет игрокам откидывать вас",
   c = Category.Combat
)
public class Velocity extends Module {
   private final ModeSetting b = new ModeSetting("Режим анти-отбрасывания", "Легитный", "Обычный", "Легитный");
   private final BooleanSetting c = new BooleanSetting("Прыгать в легит", true).a(() -> this.b.l("Легитный"));
   private final BooleanSetting d = new BooleanSetting("Легитный", true).a(() -> this.b.l("Легитный"));
   private class_243 e = class_243.field_1353;
   private int f;

   public Velocity() {
      this.a(new Setting[]{this.b, this.c, this.d});
   }

   @Override
   public void c() {
      super.c();
      this.e = class_243.field_1353;
      this.f = 0;
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c() && aM_.field_1724 != null) {
         if (event.d() instanceof class_8143 class_8143VarD && class_8143VarD.comp_1267() == aM_.field_1724.method_5628()) {
            class_1282 source = class_8143VarD.method_49071(aM_.field_1687);
            boolean player = source.method_5529() instanceof class_1657;
            this.f = player ? aM_.field_1724.field_6012 : 0;
            if (!player) {
               this.e = class_243.field_1353;
            }
         }

         if (event.d() instanceof class_2743 class_2743VarD && class_2743VarD.method_11818() == aM_.field_1724.method_5628()) {
            if (!this.b.l("Обычный")) {
               this.e = new class_243(class_2743VarD.method_11815(), 0.0, class_2743VarD.method_11819());
            } else {
               event.a(true);
            }
         }
      }
   }

   @EventTarget
   public void a(InputEvent event) {
      if (aM_.field_1724.field_6235 <= 0) {
         this.e = class_243.field_1353;
      } else if (this.b.l("Легитный") && this.e.method_1027() != 0.0 && aM_.field_1724.field_6012 - this.f <= 10) {
         double angle = class_3532.method_15338(Math.toDegrees(Math.atan2(-this.e.field_1350, -this.e.field_1352)) - 90.0 - Look.b());
         if (!this.d.c() || !aM_.field_1690.field_1894.method_1434() || !(Math.abs(angle) >= 140.0)) {
            float f;
            if (!(angle <= -45.0) && !(angle >= 45.0)) {
               f = 1.0F;
            } else {
               f = !(angle > 135.0) && !(angle < -135.0) ? 0.0F : -1.0F;
            }

            event.a(f);
            float f2;
            if (!(angle < 45.0) && !(angle > 135.0)) {
               f2 = -1.0F;
            } else {
               f2 = !(angle > -45.0) && !(angle < -135.0) ? 1.0F : 0.0F;
            }

            event.b(f2);
            event.b(this.c.c() && aM_.field_1724.method_24828());
         }
      }
   }
}

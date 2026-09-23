package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.MoveEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.util.MoveUtil;
import net.minecraft.class_2708;

@ModuleRegister(
   a = "Strafe",
   b = "Мгновенно разворачивает движение в сторону нажатых клавиш, сохраняя разгон",
   c = Category.Movement
)
public class Strafe extends Module implements Interface {
   private final BooleanSetting b = new BooleanSetting("Сброс спринта", true);
   private double speed;

   public Strafe() {
      this.a(new Setting[]{this.b});
   }

   @Override
   public void b() {
      this.speed = 0.0;
      super.b();
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null) {
         this.speed = MoveUtil.b() * 0.91;
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c() && event.d() instanceof class_2708) {
         this.speed = 0.0;
      }
   }

   @EventTarget
   public void a(MoveEvent event) {
      if (aM_.field_1724 != null) {
         if (aM_.field_1724.method_5715()
            || aM_.field_1724.method_5771()
            || aM_.field_1724.method_5869()
            || aM_.field_1724.method_31549().field_7479
            || aM_.field_1724.method_6128()) {
            this.speed = 0.0;
         } else if (!MoveUtil.a()) {
            this.speed = 0.0;
            event.a(0.0);
            event.c(0.0);
         } else {
            double current = Math.max(this.speed, Math.hypot(event.b(), event.d()));
            double[] motion = MoveUtil.c(Math.min(current, this.sprintCap()));
            event.a(motion[0]);
            event.c(motion[1]);
            if (this.b.c()) {
               aM_.field_1724.method_5728(false);
            }
         }
      }
   }

   private double sprintCap() {
      boolean sprinting = aM_.field_1724.method_5624();
      aM_.field_1724.method_5728(false);
      double cap = aM_.field_1724.method_6029() * 1.3;
      aM_.field_1724.method_5728(sprinting);
      return cap;
   }
}

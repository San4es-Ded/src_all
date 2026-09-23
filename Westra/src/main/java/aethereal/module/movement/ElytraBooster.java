package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import net.minecraft.class_243;
import net.minecraft.class_3532;

@ModuleRegister(
   a = "Elytra Booster",
   b = "Ускоряет полёт на элитре",
   c = Category.Movement
)
public class ElytraBooster extends Module implements Interface {
   private final BooleanSetting b = new BooleanSetting("Скорость по углам", false);
   private final SliderSetting c = new SliderSetting("Скорость XZ", 1.65F, 1.0F, 2.5F, 0.01F).a(() -> !this.b.c());
   private final SliderSetting d = new SliderSetting("Скорость Y", 1.59F, 1.0F, 2.5F, 0.01F).a(() -> !this.b.c());
   private final SliderSetting[] e = new SliderSetting[8];
   private final SliderSetting[] f = new SliderSetting[8];
   private static final float[] g = new float[]{1.6F, 1.62F, 1.65F, 1.68F, 1.74F, 1.8F, 1.8F, 1.8F};
   private static final float[] h = new float[]{1.59F, 1.6F, 1.61F, 1.62F, 1.68F, 1.74F, 1.95F, 2.0F};
   private final BooleanSetting i = new BooleanSetting("Только при разгоне вперёд", true);

   public ElytraBooster() {
      this.a(new Setting[]{this.b, this.c, this.d});

      for (int index = 0; index < 8; index++) {
         String range = index * 5 + "-" + (index + 1) * 5;
         this.e[index] = new SliderSetting("XZ угол " + range, g[index], 1.0F, 2.5F, 0.01F).a(this.b::h);
         this.f[index] = new SliderSetting("Y угол " + range, h[index], 1.0F, 2.5F, 0.01F).a(this.b::h);
         this.a(new Setting[]{this.e[index], this.f[index]});
      }

      this.a(new Setting[]{this.i});
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1724.method_6128()) {
         if (!this.i.c() || !(aM_.field_1724.field_3913.field_3905 <= 0.0F)) {
            int bucket = q(Math.abs(aM_.field_1724.method_36455()));
            float speedXZ = this.b.c() ? this.e[bucket].c() : this.c.c();
            float speedY = this.b.c() ? this.f[bucket].c() : this.d.c();
            float yaw = (float)Math.toRadians(aM_.field_1724.method_36454());
            class_243 velocity = aM_.field_1724.method_18798();
            double motionX = -class_3532.method_15374(yaw) * speedXZ;
            double motionZ = class_3532.method_15362(yaw) * speedXZ;
            double motionY = Math.max(velocity.field_1351, (double)(-speedY));
            aM_.field_1724.method_18800(motionX, motionY, motionZ);
         }
      }
   }

   private static int q(float pitch) {
      return Math.max(0, Math.min(7, (int)pitch / 5));
   }
}

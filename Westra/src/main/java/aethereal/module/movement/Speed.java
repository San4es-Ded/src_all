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
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.GameSpeed;
import aethereal.util.MoveUtil;
import net.minecraft.class_10255;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2708;
import platform.inject.accessors.LivingEntityAccessor;

@ModuleRegister(
   a = "Speed",
   b = "Ускоряет передвижение по земле под разные античиты",
   c = Category.Movement
)
public class Speed extends Module implements Interface {
   private final ModeSetting b = new ModeSetting("Режим", "NCP", "NCP", "Matrix JB", "Grim Entity", "Grim Entity 2");
   private final BooleanSetting c = new BooleanSetting("Таймер", false).a(() -> this.b.l("NCP") || this.b.l("Matrix JB"));
   private final SliderSetting d = new SliderSetting("Скорость таймера", 1.088F, 1.0F, 2.0F, 0.001F).a(() -> this.b.l("Matrix JB") && this.c.c());
   private final BooleanSetting e = new BooleanSetting("Стойки для брони", false).a(() -> this.b.l("Grim Entity 2"));
   private final MultiModeSetting f = new MultiModeSetting("Пауза", new BooleanSetting("В жидкости", false), new BooleanSetting("В приседе", false));
   private double moveSpeed;
   private int stage;
   private int boostTicks;
   private float lastForward;
   private long lastLagBack;

   public Speed() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e, this.f});
   }

   @Override
   public void b() {
      this.stage = 1;
      this.boostTicks = 0;
      this.moveSpeed = 0.2873;
      this.lastForward = 0.0F;
      super.b();
   }

   @Override
   public void c() {
      super.c();
      GameSpeed.b();
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c() && event.d() instanceof class_2708) {
         this.lastLagBack = System.currentTimeMillis();
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1687 != null && !this.paused()) {
         if (this.b.l("Matrix JB")) {
            boolean groundNear = aM_.field_1687
               .method_20812(aM_.field_1724, aM_.field_1724.method_5829().method_1009(0.5, 0.0, 0.5).method_989(0.0, -1.0, 0.0))
               .iterator()
               .hasNext();
            if (MoveUtil.a() && groundNear && aM_.field_1724.field_6017 <= 0.0F) {
               GameSpeed.b();
               aM_.field_1724.method_24830(true);
               aM_.field_1724.method_6043();
            } else if (aM_.field_1724.field_6017 > 0.0F && this.c.c()) {
               GameSpeed.a(this.d.c());
               aM_.field_1724.method_5762(0.0, -0.003, 0.0);
            }
         } else if (System.currentTimeMillis() - this.lastLagBack >= 1000L) {
            if (this.b.l("Grim Entity")) {
               for (class_1657 other : aM_.field_1687.method_18456()) {
                  if (other != aM_.field_1724 && !(aM_.field_1724.method_5858(other) > 2.25)) {
                     float slipperiness = aM_.field_1687.method_8320(this.groundPos()).method_26204().method_9499();
                     float friction = aM_.field_1724.method_24828() ? slipperiness * 0.91F : 0.91F;
                     float target = aM_.field_1724.method_24828() ? slipperiness : 0.99F;
                     class_243 velocity = aM_.field_1724.method_18798();
                     aM_.field_1724.method_18800(velocity.field_1352 / friction * target, velocity.field_1351, velocity.field_1350 / friction * target);
                     break;
                  }
               }
            } else if (this.b.l("Grim Entity 2") && MoveUtil.a()) {
               int touching = 0;

               for (class_1297 entity : aM_.field_1687.method_18112()) {
                  if (entity != aM_.field_1724
                     && (!(entity instanceof class_1531) || this.e.c())
                     && (entity instanceof class_1309 || entity instanceof class_10255)
                     && aM_.field_1724.method_5829().method_1014(1.0).method_994(entity.method_5829())) {
                     touching++;
                  }
               }

               if (touching > 0) {
                  double[] push = MoveUtil.c(0.08 * touching);
                  aM_.field_1724.method_5762(push[0], 0.0, push[1]);
               }
            }
         }
      }
   }

   @EventTarget
   public void a(MoveEvent event) {
      if (aM_.field_1724 != null && this.b.l("NCP") && !this.paused()) {
         if (!aM_.field_1724.method_31549().field_7479 && !aM_.field_1724.method_6128() && aM_.field_1724.method_7344().method_7586() > 6) {
            if (!MoveUtil.a()) {
               GameSpeed.b();
               event.a(0.0);
               event.c(0.0);
            } else {
               GameSpeed.a(this.c.c() ? 1.088F : 1.0F);
               float forward = aM_.field_1724.field_3913.field_3905;
               double lastDist = MoveUtil.b();
               double base = forward <= 0.0F && this.lastForward > 0.0F ? lastDist * 0.66 : lastDist;
               if (this.stage == 1 && aM_.field_1724.method_24828() && !aM_.field_1724.field_5976) {
                  double jump = ((LivingEntityAccessor)aM_.field_1724).invokeGetJumpVelocity();
                  aM_.field_1724.method_18800(aM_.field_1724.method_18798().field_1352, jump, aM_.field_1724.method_18798().field_1350);
                  event.b(jump);
                  this.moveSpeed *= 2.149;
                  this.stage = 2;
               } else if (this.stage == 2) {
                  this.moveSpeed = base - 0.66 * (base - this.baseSpeed());
                  this.stage = 3;
               } else {
                  boolean landing = aM_.field_1687
                     .method_20812(aM_.field_1724, aM_.field_1724.method_5829().method_989(0.0, aM_.field_1724.method_18798().field_1351, 0.0))
                     .iterator()
                     .hasNext();
                  if (landing || aM_.field_1724.field_5992) {
                     this.stage = 1;
                  }

                  this.moveSpeed = base - base / 159.0;
               }

               this.moveSpeed = Math.max(this.moveSpeed, this.baseSpeed());
               double boosted = forward < 1.0F ? 0.465 : 0.576;
               double normal = forward < 1.0F ? 0.44 : 0.57;
               if (aM_.field_1724.method_6059(class_1294.field_5904)) {
                  double factor = 1.0 + 0.2 * (aM_.field_1724.method_6112(class_1294.field_5904).method_5578() + 1);
                  boosted *= factor;
                  normal *= factor;
               }

               if (aM_.field_1724.method_6059(class_1294.field_5909)) {
                  double factor = 1.0 + 0.2 * (aM_.field_1724.method_6112(class_1294.field_5909).method_5578() + 1);
                  boosted /= factor;
                  normal /= factor;
               }

               this.moveSpeed = Math.min(this.moveSpeed, this.boostTicks > 25 ? boosted : normal);
               if (this.boostTicks++ > 50) {
                  this.boostTicks = 0;
               }

               double[] motion = MoveUtil.c(this.moveSpeed);
               event.a(motion[0]);
               event.c(motion[1]);
               this.lastForward = forward;
            }
         }
      }
   }

   private boolean paused() {
      return this.f.a("В жидкости").c() && (aM_.field_1724.method_5799() || aM_.field_1724.method_5771())
         || this.f.a("В приседе").c() && aM_.field_1724.method_5715();
   }

   private double baseSpeed() {
      double speed = 0.2873;
      if (aM_.field_1724.method_6059(class_1294.field_5904)) {
         speed *= 1.0 + 0.2 * (aM_.field_1724.method_6112(class_1294.field_5904).method_5578() + 1);
      }

      return speed;
   }

   private class_2338 groundPos() {
      return class_2338.method_49637(aM_.field_1724.method_23317(), aM_.field_1724.method_5829().field_1322 - 0.500001, aM_.field_1724.method_23321());
   }
}

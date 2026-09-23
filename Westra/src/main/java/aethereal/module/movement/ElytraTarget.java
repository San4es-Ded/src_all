package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.module.combat.Aura;
import aethereal.module.combat.AuraUtil;
import aethereal.util.CounterUtil;
import aethereal.util.Rotation;
import net.minecraft.class_1268;
import net.minecraft.class_1309;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

@ModuleRegister(
   a = "Elytra Target",
   b = "Наводит на врага в полёте на элитре и ускоряется фейерверком из второй руки",
   c = Category.Movement
)
public class ElytraTarget extends Module {
   private final CounterUtil b = new CounterUtil();

   @EventTarget
   public void a(TickEvent event) {
      Aura aura = Westra.h().d().t().B();
      if (aura.m() && aM_.field_1724.method_6128()) {
         if (aM_.field_1724.method_6079().method_7909() != class_1802.field_8639 && !Westra.h().d().t().V().b) {
            if (Westra.h().d().v().a().a().isEmpty()) {
               Westra.h().d().v().a().a(class_1802.field_8639, 45, 1);
            }
         } else if (this.b.a(150L) && aM_.field_1724.method_18798().method_1033() < 1.5 || aura.b == 1) {
            aM_.field_1761.method_2919(aM_.field_1724, class_1268.field_5810);
            this.b.b();
         }

         class_1309 target = aura.s();
         if (target == null) {
            return;
         }

         class_243 eye = aM_.field_1724.method_33571();
         class_243 enemy = target.method_5829().method_1005();
         double dx = enemy.field_1352 - eye.field_1352;
         double dz = enemy.field_1350 - eye.field_1350;
         double horizontal = Math.sqrt(dx * dx + dz * dz);
         double nx = horizontal == 0.0 ? 0.0 : dx / horizontal;
         double nz = horizontal == 0.0 ? 0.0 : dz / horizontal;
         double lift = Math.max(0.0, 3.0 - this.q());
         class_243 aim = new class_243(enemy.field_1352 + nx * 4.0, enemy.field_1351 + lift, enemy.field_1350 + nz * 4.0);
         Rotation aimRotation = Rotation.a(eye, aim);
         float Yaw = AuraUtil.a(aM_.field_1724.method_36454(), aimRotation.c(), 1.0F);
         float Pitch = AuraUtil.a(
            aM_.field_1724.method_36455(), aura.b <= 3 ? 0.0F : aimRotation.d(), aura.b <= 3 ? 1.0F : Math.clamp(aura.b / 10.0F, 0.0F, 1.0F)
         );
         Westra.h().d().k().a(new Rotation(Yaw, Pitch), 180.0F, 1, 1);
      }
   }

   private double q() {
      class_243 start = aM_.field_1724.method_19538();
      class_243 end = start.method_1023(0.0, 2.0, 0.0);
      class_3965 class_3965VarMethod_17742 = aM_.field_1687
         .method_17742(new class_3959(start, end, class_3960.field_17558, class_242.field_1348, aM_.field_1724));
      return class_3965VarMethod_17742.method_17783() == class_240.field_1333
         ? 2.0
         : start.method_10214() - class_3965VarMethod_17742.method_17784().field_1351;
   }
}

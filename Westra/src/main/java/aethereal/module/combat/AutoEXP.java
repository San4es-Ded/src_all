package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.HotbarEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BindSetting;
import aethereal.setting.Setting;
import aethereal.util.InventoryUtil;
import aethereal.util.Look;
import aethereal.util.Rotation;
import net.minecraft.class_1268;
import net.minecraft.class_1802;

@ModuleRegister(
   a = "Auto EXP",
   b = "Бросает бутылочки опыта под себя, пока удерживается заданная клавиша",
   c = Category.Combat
)
public class AutoEXP extends Module {
   private boolean c;
   private final BindSetting b = new BindSetting("Кнопка активации", -1, 0).a(() -> this.d(true)).b(() -> this.d(false));
   private final int[] d = new int[]{-1, -1};

   public AutoEXP() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(HotbarEvent event) {
      if (this.c) {
         event.a(true);
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null) {
         if (aM_.field_1755 != null) {
            this.d(false);
         }

         if (this.c) {
            this.q();
         }

         if (!this.c && this.d[0] != -1) {
            aM_.field_1724.method_31548().field_7545 = this.d[0];
            if (this.d[1] != -1) {
               Westra.h().d().v().a().a(7, this.d[1], 1);
            }

            this.d[0] = -1;
            this.d[1] = -1;
         }
      }
   }

   private void q() {
      boolean inHand = aM_.field_1724.method_6047().method_7909() == class_1802.field_8287;
      if (!inHand) {
         int hotbarSlot = InventoryUtil.a(class_1802.field_8287, true);
         if (hotbarSlot != -1) {
            if (aM_.field_1724.method_31548().field_7545 != hotbarSlot) {
               aM_.field_1724.method_31548().field_7545 = hotbarSlot;
            }
         } else {
            int invSlot;
            if (Westra.h().d().v().a().a().isEmpty() && this.c && (invSlot = InventoryUtil.b(class_1802.field_8287)) != -1) {
               if (this.d[1] == -1) {
                  this.d[1] = invSlot;
               }

               if (aM_.field_1724.method_31548().field_7545 != 7) {
                  aM_.field_1724.method_31548().field_7545 = 7;
               }

               Westra.h().d().v().a().a(invSlot, 7, 1);
            }
         }
      } else {
         float t = aM_.field_1724.field_6012 + aM_.method_61966().method_60637(false);
         float smoothYaw = (float)(
               Math.sin(t * 0.8F) * 11.0
                  + Math.sin(t * 0.04000000011823444 + 17.20000385061287) * 1.5
                  + Math.sin(t * 0.10999997043280933 + 5.800000963109878) * 3.0
                  + Math.sin(t * 0.07000004669766619 + 12.300002384186381) * 1.0
            )
            / 3.0F;
         float smoothPitch = (float)(Math.sin(t * 0.10000000392993033) + Math.sin(t * 0.029999989348000328 + 54.099982886210135) * 0.5);
         Westra.h().d().k().a(new Rotation(Look.b() + smoothYaw, 86.0F + smoothPitch), 70.0F, 1, 3);
         if (Rotation.b().d() > 83.0F) {
            aM_.field_1761.method_2919(aM_.field_1724, class_1268.field_5808);
            aM_.field_1724.method_6104(class_1268.field_5808);
         }
      }
   }

   private void d(boolean value) {
      this.c = value;
      if (value && aM_.field_1724 != null) {
         this.d[0] = aM_.field_1724.method_31548().field_7545;
      }
   }

   @Override
   public void c() {
      super.c();
      this.c = false;
      this.d[0] = -1;
      this.d[1] = -1;
   }
}

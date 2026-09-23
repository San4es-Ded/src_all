package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.ChatUtil;
import net.minecraft.class_1304;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_3675;
import net.minecraft.class_490;

@ModuleRegister(
   a = "Elytra Swap",
   b = "Меняет элитру на нагрудник и обратно через настоящее открытие инвентаря",
   c = Category.Player
)
public class ElytraSwap extends Module implements Interface {
   private static final int b = 6;
   private final BindSetting c = new BindSetting("Клавиша свапа", -1);
   private final SliderSetting d2 = new SliderSetting("Шаг последовательности", 70.0F, 30.0F, 250.0F, 5.0F);
   private final BooleanSetting e = new BooleanSetting("Сообщать об ошибке", true);
   private int f2;
   private long g2;
   private int h2 = -1;
   private boolean i2;
   private boolean j2;
   private long k2;

   public ElytraSwap() {
      this.a(new Setting[]{this.c, this.d2, this.e});
   }

   @Override
   public void c() {
      if (this.i2) {
         this.F();
      }

      this.f2 = 0;
      this.h2 = -1;
      this.j2 = false;
      super.c();
   }

   @EventTarget
   public void a(TickEvent event) {
      long now = System.currentTimeMillis();
      if (aM_.field_1724 == null || aM_.field_1687 == null) {
         this.j2 = false;
         this.f2 = 0;
      } else if (this.f2 != 0) {
         this.q(now);
      } else {
         int key = this.c.c();
         boolean down = key != -1 && class_3675.method_15987(aM_.method_22683().method_4490(), key);
         boolean ready = aM_.field_1755 == null && now - this.k2 >= 350L;
         if (down && !this.j2 && ready) {
            this.G();
            this.k2 = now;
         }

         this.j2 = down;
      }
   }

   private void q(long now) {
      long step = (long)this.d2.c().floatValue();
      if (now - this.g2 >= step) {
         this.g2 = now;
         switch (this.f2) {
            case 1:
               this.r(6);
               this.f2 = 2;
               break;
            case 2:
               this.r(this.h2);
               this.f2 = 3;
               break;
            case 3:
               this.r(6);
               this.f2 = 4;
               break;
            case 4:
               this.F();
               this.f2 = 0;
               break;
            default:
               this.f2 = 0;
         }
      }
   }

   private void r(int slot) {
      if (slot >= 0 && aM_.field_1755 instanceof class_490 && aM_.field_1761 != null && aM_.field_1724 != null) {
         aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, slot, 0, class_1713.field_7790, aM_.field_1724);
      }
   }

   private void G() {
      this.h2 = this.H();
      if (this.h2 == -1) {
         if (this.e.c()) {
            ChatUtil.a("Нет элитры или нагрудника для свапа.");
         }
      } else {
         aM_.method_1507(new class_490(aM_.field_1724));
         this.i2 = true;
         this.f2 = 1;
         this.g2 = System.currentTimeMillis();
      }
   }

   private void F() {
      if (aM_.field_1724 != null && aM_.field_1755 instanceof class_490) {
         aM_.field_1724.method_7346();
         aM_.method_1507(null);
      }

      this.i2 = false;
      this.h2 = -1;
   }

   private int H() {
      if (aM_.field_1724 == null) {
         return -1;
      } else {
         boolean wearingElytra = aM_.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833);

         for (int slot = 0; slot < aM_.field_1724.method_31548().method_5439(); slot++) {
            class_1799 stack = aM_.field_1724.method_31548().method_5438(slot);
            if (!stack.method_7960()) {
               boolean elytra = stack.method_31574(class_1802.field_8833);
               boolean chestplate = stack.method_31574(class_1802.field_22028)
                  || stack.method_31574(class_1802.field_8058)
                  || stack.method_31574(class_1802.field_8523)
                  || stack.method_31574(class_1802.field_8678)
                  || stack.method_31574(class_1802.field_8873)
                  || stack.method_31574(class_1802.field_8577);
               if (wearingElytra && chestplate || !wearingElytra && elytra) {
                  return this.I(slot);
               }
            }
         }

         return -1;
      }
   }

   private int I(int inventorySlot) {
      return inventorySlot < 9 ? 36 + inventorySlot : inventorySlot;
   }
}

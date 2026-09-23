package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.ChatUtil;
import aethereal.util.InventoryUtil;
import java.util.Iterator;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1701;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2885;
import net.minecraft.class_3965;

@ModuleRegister(
   a = "Cross Cart",
   b = "Ставит ТНТ-вагонетку на рельсу, поджигает её и переключает на арбалет",
   c = Category.Combat
)
public class CrossCart extends Module implements Interface {
   private final ModeSetting b = new ModeSetting("Версия", "V3", "V2", "V3", "Лава");
   private final BooleanSetting c = new BooleanSetting("Блокировать ходьбу", true);
   private final BooleanSetting d = new BooleanSetting("Максимальное ускорение", true).a(() -> !this.b.l("V2"));
   private final SliderSetting e = new SliderSetting("Задержка между шагами, тик", 2.0F, 0.0F, 6.0F, 1.0F).a(() -> this.b.l("V2") || !this.d.c());
   private final BooleanSetting f = new BooleanSetting("Возвращать слот", true);
   private final BooleanSetting g = new BooleanSetting("Логи", true);
   private CrossCart.a h;
   private class_2338 i;
   private boolean j;
   private int k;
   private int l;
   private int m = -1;

   public CrossCart() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e, this.f, this.g});
   }

   @Override
   public void c() {
      this.q();
      super.c();
   }

   private void q() {
      this.j = false;
      this.i = null;
      this.h = null;
      this.k = 0;
      this.l = 0;
      if (this.f.c() && this.m >= 0 && aM_.field_1724 != null) {
         aM_.field_1724.method_31548().field_7545 = this.m;
      }

      this.m = -1;
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.b() && !this.j && aM_.field_1724 != null) {
         if (event.d() instanceof class_2885 packet) {
            if (this.r(packet.method_12546())) {
               class_3965 hit = packet.method_12543();
               class_2338 target = hit.method_17777().method_10093(hit.method_17780());
               this.i = target;
               this.j = true;
               this.h = CrossCart.a.SWAP_CART;
               this.k = 0;
               this.l = 0;
               this.m = aM_.field_1724.method_31548().field_7545;
               if (this.g.c()) {
                  ChatUtil.a("Связка пошла.");
               }
            }
         }
      }
   }

   private boolean r(class_1268 hand) {
      class_1799 stack = aM_.field_1724.method_5998(hand);
      return stack.method_31574(class_1802.field_8129)
         || stack.method_31574(class_1802.field_8848)
         || stack.method_31574(class_1802.field_8211)
         || stack.method_31574(class_1802.field_8655);
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.j && aM_.field_1724 != null && aM_.field_1687 != null && this.i != null) {
         if (this.c.c()) {
            aM_.field_1690.field_1894.method_23481(false);
            aM_.field_1690.field_1881.method_23481(false);
            aM_.field_1690.field_1913.method_23481(false);
            aM_.field_1690.field_1849.method_23481(false);
         }

         if (++this.k > 60) {
            this.s("таймаут");
         } else if (this.l > 0) {
            this.l--;
         } else {
            this.u(this.i);
            switch (this.h) {
               case SWAP_CART:
                  this.v(class_1802.field_8069, CrossCart.a.PLACE_CART, "нет ТНТ-вагонетки");
                  break;
               case PLACE_CART:
                  if (this.w() != null) {
                     this.h = this.b.l("Лава") ? CrossCart.a.SWAP_SLAB : CrossCart.a.SWAP_FIRE;
                     this.l = this.t();
                     return;
                  }

                  this.x();
                  break;
               case SWAP_SLAB:
                  this.v(class_1802.field_8291, CrossCart.a.PLACE_SLAB, "нет плиты");
                  break;
               case PLACE_SLAB:
                  this.x();
                  this.h = CrossCart.a.SWAP_LAVA;
                  this.l = this.t();
                  break;
               case SWAP_LAVA:
                  this.v(class_1802.field_8187, CrossCart.a.PLACE_LAVA, "нет ведра лавы");
                  break;
               case PLACE_LAVA:
                  this.y();
                  this.h = CrossCart.a.SWAP_FIRE;
                  this.l = this.t();
                  break;
               case SWAP_FIRE:
                  this.v(class_1802.field_8884, CrossCart.a.PLACE_FIRE, "нет огнива");
                  break;
               case PLACE_FIRE:
                  this.x();
                  this.h = CrossCart.a.SWAP_XBOW;
                  this.l = this.t();
                  break;
               case SWAP_XBOW:
                  int slot = InventoryUtil.a(class_1802.field_8399, true);
                  if (slot != -1) {
                     aM_.field_1724.method_31548().field_7545 = slot;
                  }

                  if (this.g.c()) {
                     ChatUtil.a("Связка готова.");
                  }

                  this.j = false;
                  this.i = null;
                  this.h = null;
            }
         }
      }
   }

   private int t() {
      return !this.b.l("V2") && this.d.c() ? 0 : (int)this.e.c().floatValue();
   }

   private void u(class_2338 pos) {
      class_243 center = pos.method_46558();
      class_243 eye = aM_.field_1724.method_33571();
      class_243 direction = center.method_1020(eye);
      aM_.field_1724.method_36456((float)Math.toDegrees(Math.atan2(direction.field_1350, direction.field_1352)) - 90.0F);
      aM_.field_1724.method_36457((float)(-Math.toDegrees(Math.atan2(direction.field_1351, Math.hypot(direction.field_1352, direction.field_1350)))));
   }

   private void v(class_1792 item, CrossCart.a next, String failure) {
      int slot = InventoryUtil.a(item, true);
      if (slot == -1) {
         this.s(failure);
      } else {
         aM_.field_1724.method_31548().field_7545 = slot;
         this.h = next;
         this.l = this.t();
      }
   }

   private class_1297 w() {
      Iterator var1 = aM_.field_1687.method_8390(class_1701.class, new class_238(this.i).method_1014(1.0), entity -> true).iterator();
      return var1.hasNext() ? (class_1297)var1.next() : null;
   }

   private void x() {
      if (aM_.field_1761 != null) {
         aM_.field_1761.method_2896(aM_.field_1724, class_1268.field_5808, new class_3965(this.i.method_46558(), class_2350.field_11036, this.i, false));
         aM_.field_1724.method_6104(class_1268.field_5808);
      }
   }

   private void y() {
      if (aM_.field_1761 != null) {
         class_2338 above = this.i.method_10084();
         aM_.field_1761.method_2896(aM_.field_1724, class_1268.field_5808, new class_3965(above.method_46558(), class_2350.field_11036, above, false));
         aM_.field_1724.method_6104(class_1268.field_5808);
      }
   }

   private void s(String reason) {
      if (this.g.c()) {
         ChatUtil.a("Связка прервана: &c" + reason + "&7.");
      }

      this.q();
   }

   private static enum a {
      SWAP_CART,
      PLACE_CART,
      SWAP_SLAB,
      PLACE_SLAB,
      SWAP_LAVA,
      PLACE_LAVA,
      SWAP_FIRE,
      PLACE_FIRE,
      SWAP_XBOW;
   }
}

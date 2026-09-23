package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.util.InventoryUtil;
import aethereal.util.MathUtil;
import aethereal.util.Rotation;
import net.minecraft.class_1268;
import net.minecraft.class_1511;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2885;
import net.minecraft.class_3532;
import net.minecraft.class_3965;

@ModuleRegister(
   a = "Auto Explosion",
   b = "Размещает кристалл на обсидиане и мгновенно его подрывает",
   c = Category.Combat
)
public class AutoExplosion extends Module {
   private class_2338 c;
   private int e;
   private final BooleanSetting b = new BooleanSetting("Установить двойной кристалл", false);
   private int d = -1;

   public AutoExplosion() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.b() && event.d() instanceof class_2885 class_2885VarD && aM_.field_1724.method_6047().method_7909() == class_1802.field_8281) {
         class_3965 hit = class_2885VarD.method_12543();
         this.c = hit.method_17777().method_10093(hit.method_17780());
         this.d = -1;
         this.e = 0;
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.c != null) {
         int slot = InventoryUtil.a(class_1802.field_8301, true);
         if (slot != -1 && aM_.field_1687.method_8320(this.c).method_27852(class_2246.field_10540) && aM_.field_1724.method_56093(this.c, 0.0)) {
            if (this.e == 1 || this.b.c() && this.e == 5) {
               this.b(slot);
            }

            for (class_1511 crystal : aM_.field_1687.method_8390(class_1511.class, new class_238(this.c.method_10084()).method_1014(0.5), c -> true)) {
               this.a(this.a(crystal.method_5829()));
               if (this.e >= 3 && aM_.field_1724.method_56094(crystal, 0.0)) {
                  aM_.field_1761.method_2918(aM_.field_1724, crystal);
                  aM_.field_1724.method_6104(class_1268.field_5808);
                  if (!this.b.c() || this.e >= 7) {
                     this.q();
                     return;
                  }
                  break;
               }
            }

            int i = this.e + 1;
            this.e = i;
            if (i > (this.b.c() ? 8 : 4)) {
               this.q();
            }

            return;
         } else {
            this.q();
         }
      }
   }

   private void b(int slot) {
      if (aM_.field_1724.method_31548().field_7545 != slot) {
         this.d = aM_.field_1724.method_31548().field_7545;
         aM_.field_1724.method_31548().field_7545 = slot;
      }

      class_243 center = this.c.method_46558();
      class_243 hit = new class_238(this.c).method_992(aM_.field_1724.method_33571(), center).orElse(center);
      this.a(center);
      aM_.field_1761.method_2896(aM_.field_1724, class_1268.field_5808, new class_3965(hit, class_2350.method_58251(hit.method_1020(center)), this.c, false));
      aM_.field_1724.method_6104(class_1268.field_5808);
   }

   private class_243 a(class_238 b) {
      class_243 e = aM_.field_1724.method_33571();
      return new class_243(
         class_3532.method_15350(e.method_10216(), b.field_1323, b.field_1320) + MathUtil.a(-0.1F, 0.1F),
         class_3532.method_15350(e.method_10214(), b.field_1322, b.field_1325) + MathUtil.a(-0.1F, 0.1F),
         class_3532.method_15350(e.field_1350, b.field_1321, b.field_1324) + MathUtil.a(-0.1F, 0.1F)
      );
   }

   private void a(class_243 point) {
      Rotation r = Rotation.a(aM_.field_1724.method_33571(), point);
      Westra.h().d().k().a(new Rotation(r.c() + MathUtil.a(-3.0F, 3.0F), r.d() + MathUtil.a(-3.0F, 3.0F)), 120.0F, 1, 2);
   }

   private void q() {
      if (this.d != -1) {
         aM_.field_1724.method_31548().field_7545 = this.d;
      }

      this.d = -1;
      this.c = null;
      this.e = 0;
   }

   @Override
   public void c() {
      super.c();
      this.q();
   }
}

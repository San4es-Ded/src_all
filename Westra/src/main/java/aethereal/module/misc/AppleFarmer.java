package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.util.InventoryUtil;
import aethereal.util.MathUtil;
import aethereal.util.Rotation;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.IntStream;
import net.minecraft.class_1268;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1723;
import net.minecraft.class_1735;
import net.minecraft.class_1743;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1794;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2473;
import net.minecraft.class_2588;
import net.minecraft.class_2680;
import net.minecraft.class_3481;
import net.minecraft.class_3965;
import net.minecraft.class_476;
import net.minecraft.class_2338.class_2339;
import platform.inject.accessors.SlotAccessor;

@ModuleRegister(
   a = "Apple Farmer",
   b = "Автоматически фармит яблоки",
   c = Category.Misc
)
public class AppleFarmer extends Module {
   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724.field_6012 % 600 < 10) {
         if (aM_.field_1724.method_6047().method_7909() instanceof class_1743) {
            aM_.field_1724.method_31548().field_7545 = (int)MathUtil.a(0.0F, 8.0F);
         }
      } else {
         Westra.h().d().t().aV().b(19);
         if (!Westra.h().d().v().k().a() && Westra.h().d().v().i().a(aM_.field_1724.method_6047(), 10.0, 98.0)) {
            class_2338 leaf = this.a(5.0, s -> s.method_26164(class_3481.field_15503), this::d);
            if (leaf != null) {
               this.a(leaf, s2 -> s2.method_7909() instanceof class_1794);
            } else {
               class_2338 log = this.a(5.0, s3 -> s3.method_26164(class_3481.field_15475), v0 -> v0.method_10264());
               if (log != null) {
                  this.a(log, s4 -> s4.method_7909() instanceof class_1743);
               } else if (aM_.field_1755 instanceof class_476) {
                  this.q();
               } else if (InventoryUtil.a(class_1802.field_8606) > 0 && InventoryUtil.a(class_1802.field_8324) == 0) {
                  this.r();
               } else if (!this.u() && !this.v()) {
                  class_2338 dirt = this.a(16.0, s5 -> s5.method_26164(class_3481.field_29822), this::c);
                  if (dirt != null) {
                     if (InventoryUtil.a(class_1802.field_8600) <= 128 && InventoryUtil.a(class_1802.field_17535) <= 128) {
                        this.a(dirt);
                     } else {
                        this.s();
                     }
                  }
               } else {
                  this.q();
               }
            }
         }
      }
   }

   private void a(class_2338 pos, Predicate<class_1799> tool) {
      if (!tool.test(aM_.field_1724.method_6047())) {
         this.a(tool);
      } else if (!(aM_.field_1724.method_6047().method_7909() instanceof class_1743) || !(aM_.field_1724.method_7261(0.0F) <= 0.15F)) {
         class_243 center = pos.method_46558();
         if (this.a(center)) {
            class_243 hit = new class_238(pos).method_992(aM_.field_1724.method_33571(), center).orElse(center);
            aM_.field_1761.method_2902(pos, class_2350.method_58251(hit.method_1020(center)));
            aM_.field_1724.method_6104(class_1268.field_5808);
         }
      }
   }

   private void a(class_2338 dirt) {
      boolean grown = aM_.field_1687.method_8320(dirt.method_10084()).method_26204() instanceof class_2473;
      Predicate<class_1799> want = grown
         ? s -> s.method_31574(class_1802.field_8324)
         : s2 -> s2.method_7909() instanceof class_1747 class_1747VarMethod_7909 && class_1747VarMethod_7909.method_7711() instanceof class_2473;
      if (!want.test(aM_.field_1724.method_6047())) {
         this.a(want);
      } else {
         class_243 top = new class_243(dirt.method_10263() + 0.5, dirt.method_10264() + 1, dirt.method_10260() + 0.5);
         if (this.a(top) && aM_.field_1724.field_6012 % 4 == 0) {
            aM_.field_1761
               .method_2896(aM_.field_1724, class_1268.field_5808, new class_3965(top, class_2350.field_11036, grown ? dirt.method_10084() : dirt, false));
            aM_.field_1724.method_6104(class_1268.field_5808);
         }
      }
   }

   private void q() {
      class_476 class_476Var = (class_476)aM_.field_1755;
      if (!(class_476Var instanceof class_476)) {
         this.b(this.a(5.0, s -> s.method_27852(this.u() ? class_2246.field_10034 : class_2246.field_16328), this::c));
      } else if (aM_.field_1724.field_6012 % 50 == 0) {
         aM_.field_1724.method_7346();
      } else if (aM_.field_1724.field_6012 % 2 == 0) {
         class_1703 handler = class_476Var.method_17577();
         boolean z;
         if (class_476Var.method_25440().method_10851() instanceof class_2588 class_2588VarMethod_10851) {
            if (class_2588VarMethod_10851.method_11022().contains("chest")) {
               z = true;
            } else {
               z = false;
            }
         } else {
            z = false;
         }

         int iA;
         if (z) {
            iA = this.a(handler, false, 5, s2 -> s2.method_31574(class_1802.field_8279) || s2.method_31574(class_1802.field_8583));
         } else {
            iA = this.v() ? this.a(handler, true, 3, this::a) : 0;
         }

         if (iA == 0) {
            aM_.field_1724.method_7346();
         }
      }
   }

   private void r() {
      if (aM_.field_1724.field_6012 % 2 == 0) {
         class_1723 class_1723Var = aM_.field_1724.field_7498;
         if (class_1723Var.method_7611(0).method_7677().method_31574(class_1802.field_8324)) {
            this.a(class_1723Var, 0, 0, class_1713.field_7794);
            aM_.field_1724.method_7346();
         } else {
            int bone = this.a(class_1802.field_8606);
            if (bone >= 0) {
               this.a(class_1723Var, bone, 0, class_1713.field_7790);
               this.a(class_1723Var, 1, 0, class_1713.field_7790);
            }
         }
      }
   }

   private void s() {
      float sw = this.t();
      this.a(new Rotation(sw * 10.0F, MathUtil.b(sw / 4.0F, -30.0F, 30.0F)));
      if (aM_.field_1724.field_6012 % 5 == 0) {
         class_1723 class_1723Var = aM_.field_1724.field_7498;
         int keep = -1;
         int max = -1;

         for (class_1735 slot : class_1723Var.field_7761) {
            if (this.a(slot) && slot.method_7677().method_31574(class_1802.field_17535) && slot.method_7677().method_7947() > max) {
               max = slot.method_7677().method_7947();
               keep = slot.field_7874;
            }
         }

         for (class_1735 slot2 : class_1723Var.field_7761) {
            if (this.a(slot2)
               && (
                  slot2.method_7677().method_31574(class_1802.field_8600)
                     || slot2.method_7677().method_31574(class_1802.field_17535) && slot2.field_7874 != keep
               )) {
               this.a(class_1723Var, slot2.field_7874, 1, class_1713.field_7795);
            }
         }
      }
   }

   private void b(class_2338 block) {
      if (block != null && this.a(block.method_46558()) && aM_.field_1724.field_6012 % 4 == 0) {
         class_243 center = block.method_46558();
         class_243 hit = new class_238(block).method_992(aM_.field_1724.method_33571(), center).orElse(center);
         aM_.field_1761.method_2896(aM_.field_1724, class_1268.field_5808, new class_3965(hit, class_2350.method_58251(hit.method_1020(center)), block, false));
      }
   }

   private boolean a(Predicate<class_1799> match) {
      int slot = IntStream.range(0, 36).filter(i -> match.test(aM_.field_1724.method_31548().method_5438(i))).findFirst().orElse(-1);
      if (slot < 0) {
         return false;
      } else if (slot < 9) {
         aM_.field_1724.method_31548().field_7545 = slot;
         return true;
      } else if (aM_.field_1724.field_6012 % 4 != 0) {
         return true;
      } else {
         int target = IntStream.range(0, 9)
            .filter(i2 -> aM_.field_1724.method_31548().method_5438(i2).method_7960())
            .findFirst()
            .orElse(aM_.field_1724.method_31548().field_7545);
         Westra.h().d().v().a().a(slot, target, 1);
         aM_.field_1724.method_31548().field_7545 = target;
         return true;
      }
   }

   private boolean a(class_243 point) {
      Rotation rotation = Rotation.a(aM_.field_1724.method_33571(), point);
      float sw = this.t();
      this.a(new Rotation(rotation.c() + sw / 2.0F, MathUtil.b(rotation.d() + sw / 4.0F, -90.0F, 90.0F)));
      return Rotation.b().a(rotation) < 20.0;
   }

   private void a(Rotation rotation) {
      Westra.h().d().k().a(rotation, 180.0F, 1, 1);
   }

   private float t() {
      float t = aM_.field_1724.field_6012 + aM_.method_61966().method_60637(false);
      return (float)((Math.sin(t * 0.31F) * 0.5 + Math.sin(t * 0.73F + 1.1F) * 0.30000003042305273 + Math.sin(t * 1.7F + 2.6F) * 0.2000000149681302) * 8.0);
   }

   private int a(class_1703 handler, boolean fromContainer, int limit, Predicate<class_1799> match) {
      int moved = 0;

      for (class_1735 slot : handler.field_7761) {
         if (moved >= limit) {
            break;
         }

         if (this.a(slot) != fromContainer && match.test(slot.method_7677())) {
            this.a(handler, slot.field_7874, 0, class_1713.field_7794);
            moved++;
         }
      }

      return moved;
   }

   private void a(class_1703 handler, int slot, int button, class_1713 action) {
      aM_.field_1761.method_2906(handler.field_7763, slot, button, action, aM_.field_1724);
   }

   private boolean a(class_1735 slot) {
      return ((SlotAccessor)slot).getInventory() == aM_.field_1724.method_31548();
   }

   private int a(class_1792 item) {
      for (class_1735 slot : aM_.field_1724.field_7498.field_7761) {
         if (this.a(slot) && slot.method_7677().method_31574(item)) {
            return slot.field_7874;
         }
      }

      return -1;
   }

   private boolean a(class_1799 stack) {
      if (stack.method_7909() instanceof class_1747 class_1747VarMethod_7909) {
         if (!(class_1747VarMethod_7909.method_7711() instanceof class_2473)
            && stack.method_31574(class_1802.field_8324)
            && (!stack.method_31574(class_1802.field_8606) || InventoryUtil.a(class_1802.field_8324) != 0)) {
            return false;
         }
      } else if (stack.method_31574(class_1802.field_8324)) {
      }

      return true;
   }

   private boolean u() {
      return InventoryUtil.a(class_1802.field_8279) > 128 || InventoryUtil.a(class_1802.field_8583) > 192;
   }

   private boolean v() {
      return InventoryUtil.a(class_1802.field_17535) == 0 || InventoryUtil.a(class_1802.field_8324) == 0;
   }

   private double c(class_2338 pos) {
      return aM_.field_1724.method_33571().method_1025(class_243.method_24953(pos));
   }

   private double d(class_2338 pos) {
      class_243 eye = aM_.field_1724.method_33571();
      class_243 look = aM_.field_1724.method_5828(1.0F);
      class_243 diff = class_243.method_24953(pos).method_1020(eye);
      double along = diff.method_1026(look);
      return along <= 0.0 ? 1.7976922776554332E308 : diff.method_1020(look.method_1021(along)).method_1027();
   }

   private class_2338 a(double radius, Predicate<class_2680> match, ToDoubleFunction<class_2338> score) {
      class_2338 origin = aM_.field_1724.method_24515();
      class_2338 best = null;
      double bestScore = 1.7976922776554332E308;
      double reachSq = radius * radius;
      int r = (int)Math.ceil(radius);
      class_2339 pos = new class_2339();

      for (int x = -r; x <= r; x++) {
         for (int y = -r; y <= r; y++) {
            for (int z = -r; z <= r; z++) {
               pos.method_10103(origin.method_10263() + x, origin.method_10264() + y, origin.method_10260() + z);
               if (match.test(aM_.field_1687.method_8320(pos)) && this.c(pos) <= reachSq) {
                  double s = score.applyAsDouble(pos);
                  if (s < bestScore) {
                     bestScore = s;
                     best = pos.method_10062();
                  }
               }
            }
         }
      }

      return best;
   }
}

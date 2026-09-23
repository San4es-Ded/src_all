package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.handler.InventoryHandler;
import java.util.Comparator;
import java.util.stream.IntStream;
import net.minecraft.class_1661;
import net.minecraft.class_1743;
import net.minecraft.class_1794;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1810;
import net.minecraft.class_1821;
import net.minecraft.class_2246;
import net.minecraft.class_2680;
import net.minecraft.class_3481;
import net.minecraft.class_3965;

@ModuleRegister(
   a = "Auto Tool",
   b = "Автоматически выбирает подходящий инструмент для блока",
   c = Category.Player
)
public class AutoTool extends Module implements Interface {
   private final int[] b = new int[]{-1, -1};

   @EventTarget
   public void a(TickEvent event) {
      InventoryHandler handler = Westra.h().d().v().a();
      if (handler.a().isEmpty()) {
         if (aM_.field_1765 instanceof class_3965 class_3965Var && aM_.field_1690.field_1886.method_1434()) {
            int bestSlot;
            if (this.b[0] == -1 && (bestSlot = this.a(aM_.field_1687.method_8320(class_3965Var.method_17777()))) != -1) {
               this.b[0] = aM_.field_1724.method_31548().field_7545;
               if (bestSlot > 8) {
                  this.b[1] = bestSlot;
                  handler.a(bestSlot, this.b[0], 1);
                  return;
               }

               aM_.field_1724.method_31548().field_7545 = bestSlot;
               return;
            }

            return;
         }

         if (this.b[0] == -1) {
            return;
         }

         if (this.b[1] == -1) {
            aM_.field_1724.method_31548().field_7545 = this.b[0];
         } else {
            handler.a(this.b[1], this.b[0], 1);
         }

         this.b[0] = -1;
         this.b[1] = -1;
      }
   }

   private int a(class_2680 state) {
      class_1661 inventory = aM_.field_1724.method_31548();
      int shears;
      return state.method_27852(class_2246.field_10343)
            && (
                  shears = IntStream.range(0, inventory.field_7547.size())
                     .filter(i -> inventory.method_5438(i).method_31574(class_1802.field_8868))
                     .findFirst()
                     .orElse(-1)
               )
               != -1
         ? shears
         : IntStream.range(0, inventory.field_7547.size())
            .filter(i2 -> inventory.method_5438(i2).method_7924(state) > 1.0F && this.a(inventory.method_5438(i2), state))
            .boxed()
            .max(Comparator.comparingDouble(i3 -> inventory.method_5438(i3).method_7924(state)))
            .orElse(-1);
   }

   private boolean a(class_1799 stack, class_2680 state) {
      if (state.method_26164(class_3481.field_33713)) {
         return stack.method_7909() instanceof class_1743;
      } else if (state.method_26164(class_3481.field_33715)) {
         return stack.method_7909() instanceof class_1810;
      } else if (state.method_26164(class_3481.field_33716)) {
         return stack.method_7909() instanceof class_1821;
      } else {
         return state.method_26164(class_3481.field_33714) ? stack.method_7909() instanceof class_1794 : true;
      }
   }
}

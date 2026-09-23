package aethereal.handler;

import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2815;
import net.minecraft.class_9334;

@Handler_2
public class InventoryHandler extends BaseHandler implements Interface {
   private final List<InventoryHandler.a> b = new ArrayList<>();

   @Generated
   public List<InventoryHandler.a> a() {
      return this.b;
   }

   @EventTarget
   public void a(TickEvent event) {
      if (!this.b.isEmpty()) {
         InventoryHandler.a task = this.b.getFirst();
         StopHandler stopHandler = Westra.h().d().v().c();
         if (stopHandler.c() < task.c()) {
            int from = this.a(task.a());
            int to = task.d() ? task.b() : this.a(task.b());
            if (aM_.field_1724.field_7498.method_7611(from).method_7677().method_57826(class_9334.field_49650)) {
               aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, from, 1, class_1713.field_7790, aM_.field_1724);
               aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, to, 0, class_1713.field_7790, aM_.field_1724);
               if (!aM_.field_1724.field_7498.method_34255().method_7960()) {
                  aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, from, 0, class_1713.field_7790, aM_.field_1724);
               }
            } else {
               int swapButton = this.a(task.b(), to);
               if (swapButton != -1) {
                  aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, from, swapButton, class_1713.field_7791, aM_.field_1724);
               } else {
                  int swapButton2 = this.a(task.a(), from);
                  if (swapButton2 != -1) {
                     aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, to, swapButton2, class_1713.field_7791, aM_.field_1724);
                  } else if (from != to) {
                     aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, from, 0, class_1713.field_7791, aM_.field_1724);
                     aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, to, 0, class_1713.field_7791, aM_.field_1724);
                     aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, from, 0, class_1713.field_7791, aM_.field_1724);
                  }
               }
            }

            aM_.field_1724.field_3944.method_52787(new class_2815(aM_.field_1724.field_7512.field_7763));
            this.b.remove(task);
            if (!this.b.isEmpty()) {
               stopHandler.a(this.b.getFirst().c());
            }
         }
      }
   }

   public void a(int fromSlot, int toSlot, int bypass) {
      this.a(new InventoryHandler.a(fromSlot, toSlot, bypass, false));
   }

   public void b(int fromSlot, int armorSlot, int bypass) {
      this.a(new InventoryHandler.a(fromSlot, 5 + armorSlot, bypass, true));
   }

   public void a(class_1792 item, int toSlot, int bypass) {
      int slot = InventoryUtil.b(item);
      if (slot != -1) {
         this.a(new InventoryHandler.a(slot, toSlot, bypass, false));
      }
   }

   public void a(class_1799 stack, int toSlot, int bypass) {
      int slot = InventoryUtil.a(stack, false);
      if (slot != -1) {
         this.a(new InventoryHandler.a(slot, toSlot, bypass, false));
      }
   }

   private void a(InventoryHandler.a task) {
      if (task.a() != -1 && task.b() != -1) {
         if (this.b.isEmpty() && task.c > 0) {
            Westra.h().d().v().c().a(task.c);
         }

         this.b.add(task);
      }
   }

   private int a(int slot) {
      return slot >= 0 && slot <= 8 ? slot + 36 : slot;
   }

   private int a(int original, int normalized) {
      if (original == 40 || original == 45 || normalized == 45) {
         return 40;
      } else {
         return normalized >= 36 && normalized <= 44 ? normalized - 36 : -1;
      }
   }

   static final class a {
      private final int a;
      private final int b;
      final int c;
      private final boolean d;

      @Generated
      public int a() {
         return this.a;
      }

      @Generated
      public int b() {
         return this.b;
      }

      @Generated
      public int c() {
         return this.c;
      }

      @Generated
      public boolean d() {
         return this.d;
      }

      public a(int from, int to, int bypass, boolean raw) {
         this.a = from;
         this.b = to;
         this.c = bypass;
         this.d = raw;
      }
   }
}

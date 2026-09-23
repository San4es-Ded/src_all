package aethereal.handler;

import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import aethereal.util.InventoryUtil;
import aethereal.util.MoveUtil;
import aethereal.util.Rotation;
import aethereal.util.ServerUtil;
import java.util.ArrayDeque;
import java.util.Deque;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1893;
import net.minecraft.class_476;
import net.minecraft.class_7439;

@Handler_2
public class PvEHandler extends BaseHandler implements Interface {
   private final Deque<PvEHandler.b> b = new ArrayDeque<>();

   @Generated
   public Deque<PvEHandler.b> a() {
      return this.b;
   }

   @EventTarget
   public void a(TickEvent event) {
      if (!this.b.isEmpty() && this.b.peek().a()) {
         this.b.poll();
      }
   }

   @EventTarget(
      a = 4
   )
   public void a(InputEvent event) {
      if (!this.b.isEmpty()) {
         MoveUtil.b(event);
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (!this.b.isEmpty() && event.c()) {
         if (event.d() instanceof class_7439 class_7439VarD && class_7439VarD.comp_763().getString().equals("Данная команда недоступна в режиме AFK")) {
            Westra.h().d().v().g().a(7);
         }
      }
   }

   public boolean a(class_1799 tool, double startPct, double endPct) {
      for (PvEHandler.b task : this.b) {
         if (task instanceof PvEHandler.c) {
            return false;
         }
      }

      if (a(tool) > startPct) {
         return true;
      } else if (!InventoryUtil.a(tool, class_1893.field_9101, 1)) {
         ChatUtil.a("На предмете нету починки, отмена.");
         return true;
      } else {
         class_1799 main = aM_.field_1724.method_6047();
         class_1799 off = aM_.field_1724.method_6079();
         class_1799 class_1799VarMethod_7972;
         if (!main.method_7960() && !class_1799.method_31577(main, tool)) {
            class_1799VarMethod_7972 = main.method_7972();
         } else {
            class_1799VarMethod_7972 = !off.method_7960() && off.method_7909() != tool.method_7909() ? off.method_7972() : class_1799.field_8037;
         }

         this.b.add(new PvEHandler.c(tool.method_7909(), endPct, aM_.field_1724.method_31548().field_7545, class_1799VarMethod_7972));
         return false;
      }
   }

   static double a(class_1799 stack) {
      return stack.method_7963() && stack.method_7936() > 0 ? (1.0 - (double)stack.method_7919() / stack.method_7936()) * 100.0 : 100.0;
   }

   static final class a implements Interface, PvEHandler.b {
      final class_1792 b;
      final int c;
      final int d;
      int e;
      int f;

      a(class_1792 targetItem, int need, int priceLimit) {
         this.b = targetItem;
         this.c = need;
         this.d = priceLimit;
      }

      @Override
      public boolean a() {
         switch (this.e) {
            case 0:
               if (aM_.field_1755 != null) {
                  aM_.field_1755.method_25419();
               }

               int i = this.f + 1;
               this.f = i;
               if (i >= 3) {
                  aM_.field_1724
                     .field_3944
                     .method_45729("/ah search " + (this.b == class_1802.field_8287 ? "Опыт" : new class_1799(this.b).method_7964().getString()));
                  this.e = 1;
                  this.f = 0;
                  return false;
               }

               return false;
            case 1:
               class_476 class_476Var = (class_476)aM_.field_1755;
               if (!(class_476Var instanceof class_476)) {
                  int i2 = this.f + 1;
                  this.f = i2;
                  if (i2 > 20) {
                     ChatUtil.a("Аукцион не открылся, повторяю.");
                     this.f = 0;
                     this.e = 0;
                     return false;
                  }

                  return false;
               } else {
                  if (!class_476Var.method_25440().getString().startsWith("☃") && !class_476Var.method_25440().getString().startsWith("0A2z")) {
                     return false;
                  }

                  this.e = 2;
                  this.f = 0;
                  return false;
               }
            case 2:
               int i3 = this.f + 1;
               this.f = i3;
               if (i3 >= 15) {
                  this.e = 3;
                  this.f = 0;
                  return false;
               }

               return false;
            case 3:
               class_476 class_476Var2 = (class_476)aM_.field_1755;
               if (!(class_476Var2 instanceof class_476)) {
                  this.f = 0;
                  this.e = 0;
                  return false;
               } else {
                  class_476 screen2 = class_476Var2;
                  if (InventoryUtil.a(this.b) >= this.c) {
                     int i4 = this.f + 1;
                     this.f = i4;
                     if (i4 >= 6) {
                        aM_.field_1755.method_25419();
                        return true;
                     }

                     return false;
                  } else {
                     this.f = 0;
                     if (aM_.field_1724.field_6012 % 7 != 0) {
                        return false;
                     } else {
                        class_1735 offer = null;
                        int i5 = 0;

                        for (; i5 < ((class_1707)screen2.method_17577()).field_7761.size() - 36; i5++) {
                           class_1735 slot = (class_1735)((class_1707)screen2.method_17577()).field_7761.get(i5);
                           if (!slot.method_7677().method_7960()
                              && slot.method_7677().method_7909() == this.b
                              && ServerUtil.a.a(slot.method_7677()) > 0
                              && ServerUtil.a.a(slot.method_7677()) <= this.d
                              && (offer == null || ServerUtil.a.a(slot.method_7677()) < ServerUtil.a.a(offer.method_7677()))) {
                              offer = slot;
                           }
                        }

                        if (offer == null) {
                           aM_.field_1761.method_2906(((class_1707)screen2.method_17577()).field_7763, 50, 0, class_1713.field_7794, aM_.field_1724);
                           return false;
                        } else {
                           aM_.field_1761
                              .method_2906(((class_1707)screen2.method_17577()).field_7763, offer.field_7874, 0, class_1713.field_7794, aM_.field_1724);
                           return false;
                        }
                     }
                  }
               }
            default:
               return false;
         }
      }
   }

   interface b {
      boolean a();
   }

   static final class c implements Interface, PvEHandler.b {
      private final class_1792 b;
      private final double c;
      private final int d;
      private final class_1799 e;
      private int f;
      private int g;

      c(class_1792 tool, double endPct, int toolBarSlot, class_1799 restore) {
         this.b = tool;
         this.c = endPct;
         this.d = toolBarSlot;
         this.e = restore;
      }

      @Override
      public boolean a() {
         InventoryHandler handler = Westra.h().d().v().a();
         class_1799 offHand = aM_.field_1724.method_6079();
         class_1799 mainHand = aM_.field_1724.method_6047();
         switch (this.f) {
            case 0:
               if (aM_.field_1755 != null) {
                  aM_.field_1755.method_25419();
               }

               int i = this.g + 1;
               this.g = i;
               if (i >= 3 && handler.a().isEmpty()) {
                  handler.a(this.d, 40, 1);
                  this.f = 1;
                  return false;
               }

               return false;
            case 1:
               if (handler.a().isEmpty() && offHand.method_7909() == this.b) {
                  if (PvEHandler.a(offHand) >= this.c) {
                     this.f = 3;
                     return false;
                  }

                  int onBar = InventoryUtil.a(class_1802.field_8287, true);
                  if (onBar != -1) {
                     aM_.field_1724.method_31548().field_7545 = onBar;
                     this.f = 2;
                     return false;
                  }

                  int inStorage = InventoryUtil.b(class_1802.field_8287);
                  if (inStorage != -1) {
                     handler.a(inStorage, this.d, 1);
                     this.f = 2;
                     return false;
                  }

                  Westra.h().d().v().i().a().addFirst(new PvEHandler.a(class_1802.field_8287, 128, 2000));
                  return false;
               }

               return false;
            case 2:
               if (handler.a().isEmpty() && offHand.method_7909() == this.b) {
                  if (PvEHandler.a(offHand) >= this.c) {
                     this.g = 0;
                     this.f = 3;
                     return false;
                  }

                  if (mainHand.method_7960()) {
                     this.f = 1;
                     return false;
                  }

                  if (mainHand.method_7909() != class_1802.field_8287) {
                     int bar = InventoryUtil.a(class_1802.field_8287, true);
                     aM_.field_1724.method_31548().field_7545 = bar != -1 ? bar : this.d;
                     return false;
                  }

                  Westra.h().d().k().a(new Rotation(aM_.field_1724.method_36454(), 90.0F), 360.0F, 1, 1);
                  aM_.field_1761.method_2919(aM_.field_1724, class_1268.field_5808);
                  return false;
               }

               return false;
            case 3:
               int i2 = this.g + 1;
               this.g = i2;
               if (i2 >= 3) {
                  handler.a(this.d, 40, 1);
                  this.g = 0;
                  this.f = 4;
                  return false;
               }

               return false;
            case 4:
               int i3 = this.g + 1;
               this.g = i3;
               if (i3 >= 3) {
                  if (!this.e.method_7960() && InventoryUtil.a(this.e, false) != -1) {
                     handler.a(InventoryUtil.a(this.e, false), 40, 1);
                     return true;
                  }

                  return true;
               }

               return false;
            default:
               return false;
         }
      }
   }
}

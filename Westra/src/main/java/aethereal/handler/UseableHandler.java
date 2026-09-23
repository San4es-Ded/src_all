package aethereal.handler;

import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.module.player.WindHop;
import aethereal.util.InventoryUtil;
import aethereal.util.Look;
import aethereal.util.Rotation;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2886;
import net.minecraft.class_9334;
import net.minecraft.class_9837;
import platform.inject.invokers.ClientPlayerInteractionManagerInvoker;

@Handler_2
public class UseableHandler extends BaseHandler implements Interface {
   private final List<UseableHandler.a> b = new ArrayList<>();

   @Generated
   public List<UseableHandler.a> a() {
      return this.b;
   }

   @EventTarget
   public void a(TickEvent event) {
      if (!this.b.isEmpty()) {
         UseableHandler.a task = this.b.getFirst();
         WindHop windHop = Westra.h().d().t().aW();
         int hotbar = task.a().method_7909() == class_1802.field_8436 ? InventoryUtil.b(task.a(), true) : InventoryUtil.a(task.a().method_7909(), true);
         int inventory = task.a().method_7909() == class_1802.field_8436 ? InventoryUtil.b(task.a(), false) : InventoryUtil.a(task.a().method_7909(), false);
         if (task.d() == -1 && hotbar == -1 && inventory == -1) {
            this.b.remove(task);
            return;
         }

         task.c(task.d() + 1);
         if (task.d() == 0) {
            task.a(aM_.field_1724.method_31548().field_7545);
            if (hotbar != -1) {
               task.b(hotbar);
               if (hotbar != aM_.field_1724.method_31548().field_7545) {
                  this.a(hotbar);
                  return;
               }

               return;
            }

            if (inventory != -1) {
               int bundle = InventoryUtil.a(aM_.field_1724.method_31548().method_5438(inventory), task.a());
               if (bundle != -1) {
                  aM_.field_1724.field_3944.method_52787(new class_9837(inventory < 9 ? 36 + inventory : inventory, bundle));
               }

               task.b(bundle != -1 && aM_.field_1724.method_6047().method_7960() ? task.b() : inventory);
               Westra.h().d().v().a().a(inventory, aM_.field_1724.method_31548().field_7545, 1);
               return;
            }

            return;
         }

         if (task.d() == 1) {
            if (task.a().method_7909() == class_1802.field_49098 && windHop.m() && windHop.q().c()) {
               float t = aM_.field_1724.field_6012 + aM_.method_61966().method_60637(false);
               float silent = (float)(Math.sin(t * 0.31F) * 6.600001001477404 + Math.sin(t * 0.73F + 1.1F) * 0.3000001491338646);
               Westra.h().d().k().a(new Rotation(Look.b() + silent, 90.0F + silent / 2.0F), 180.0F, 1, 3);
            }

            this.a(task);
            if (aM_.field_1724.method_31548().method_5438(task.c()).method_57826(class_9334.field_49650)) {
               aM_.field_1724.method_31548().method_5447(task.b(), class_1799.field_8037);
               Westra.h().d().v().a().a(task.c(), 36 + task.b(), 1);
            } else if (task.c() > 8) {
               Westra.h().d().v().a().a(task.b(), task.c(), 1);
            } else if (task.b() != aM_.field_1724.method_31548().field_7545) {
               this.a(task.b());
            }

            this.b.remove(task);
         }
      }
   }

   public void a(int slot) {
      aM_.field_1724.method_31548().field_7545 = slot;
   }

   public void a(UseableHandler.a task) {
      ((ClientPlayerInteractionManagerInvoker)aM_.field_1761)
         .invokeSendSequencedPacket(
            aM_.field_1687, sequence -> new class_2886(class_1268.field_5808, sequence, aM_.field_1724.method_36454(), aM_.field_1724.method_36455())
         );
   }

   public void a(class_1799 itemStack) {
      this.b.add(new UseableHandler.a(itemStack));
   }

   public static final class a {
      private final class_1799 a;
      private int b;
      private int c;
      private int d = -1;

      @Generated
      public void a(int selectedSlot) {
         this.b = selectedSlot;
      }

      @Generated
      public void b(int itemSlot) {
         this.c = itemSlot;
      }

      @Generated
      public void c(int ticks) {
         this.d = ticks;
      }

      @Generated
      public class_1799 a() {
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
      public int d() {
         return this.d;
      }

      public a(class_1799 itemStack) {
         this.a = itemStack;
      }
   }
}

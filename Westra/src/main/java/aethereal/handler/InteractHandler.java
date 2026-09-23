package aethereal.handler;

import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.ClickEvent;
import aethereal.event.HotbarEvent;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1268;

@Handler_2
public class InteractHandler extends BaseHandler implements Interface {
   private final List<InteractHandler.a> b = new ArrayList<>();

   @Generated
   public List<InteractHandler.a> b() {
      return this.b;
   }

   public void a(int slot) {
      if (this.b.isEmpty() && Westra.h().d().v().a().a().isEmpty()) {
         this.b.add(new InteractHandler.a(slot));
      }
   }

   public boolean a() {
      return !this.b.isEmpty();
   }

   @EventTarget
   public void a(TickEvent event) {
      if (!this.b.isEmpty() && aM_.field_1724.field_6012 > 40) {
         InventoryHandler inventoryHandler = Westra.h().d().v().a();
         InteractHandler.a task = this.b.getFirst();
         boolean inventory = task.b() > 8;
         task.a(task.d() + 1);
         if (task.d() == 1) {
            if (inventory) {
               inventoryHandler.a(task.b(), task.a(), 2);
            } else {
               aM_.field_1724.method_31548().field_7545 = task.b();
            }
         } else if (!task.c() && task.d() > 0 && inventoryHandler.a().isEmpty()) {
            if (aM_.field_1724.method_6115()) {
               task.a(true);
            } else {
               aM_.field_1761.method_2919(aM_.field_1724, class_1268.field_5808);
            }
         } else if (task.c() && !aM_.field_1724.method_6115() && inventoryHandler.a().isEmpty()) {
            if (inventory) {
               inventoryHandler.a(task.a(), task.b(), 2);
            } else {
               aM_.field_1724.method_31548().field_7545 = task.a();
            }

            this.b.remove(task);
         }

         if (task.d() >= 60) {
            ChatUtil.a("Использование предмета не удалось по неизвестной причине");
            this.b.remove(task);
         }
      }
   }

   @EventTarget
   public void a(HotbarEvent event) {
      if (this.a()) {
         event.a(true);
      }
   }

   @EventTarget
   public void a(ClickEvent event) {
      if (this.a() && event.h() == 1) {
         event.a(true);
      }
   }

   public static final class a {
      private final int a = Interface.aM_.field_1724.method_31548().field_7545;
      private final int b;
      private boolean c;
      private int d;

      @Generated
      public void a(boolean returned) {
         this.c = returned;
      }

      @Generated
      public void a(int ticks) {
         this.d = ticks;
      }

      @Generated
      public int a() {
         return this.a;
      }

      @Generated
      public int b() {
         return this.b;
      }

      @Generated
      public boolean c() {
         return this.c;
      }

      @Generated
      public int d() {
         return this.d;
      }

      public a(int eatSlot) {
         this.b = eatSlot;
      }
   }
}

package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.handler.StopHandler;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.util.MoveUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1747;
import net.minecraft.class_2480;
import net.minecraft.class_2596;
import net.minecraft.class_2645;
import net.minecraft.class_2813;
import net.minecraft.class_2815;
import net.minecraft.class_304;
import net.minecraft.class_3675;
import net.minecraft.class_3944;
import net.minecraft.class_408;
import net.minecraft.class_471;
import net.minecraft.class_481;
import net.minecraft.class_490;
import net.minecraft.class_498;
import platform.inject.accessors.ClientConnectionAccessor;

@ModuleRegister(
   a = "Screen Walk",
   b = "Позволяет двигаться с открытым контейнером, задерживая пакеты инвентаря",
   c = Category.Movement
)
public class ScreenWalk extends Module {
   private final ModeSetting b = new ModeSetting("Обход перемещения предметов", "Ускоренный", "Ускоренный", "Медленный");
   private final List<ScreenWalk.a> c = new ArrayList<>();
   private boolean d = false;

   public ScreenWalk() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget(
      a = 0
   )
   public void a(PacketEvent event) {
      StopHandler stopHandler = Westra.h().d().v().c();
      if (event.b()) {
         if (aM_.field_1755 instanceof class_490 && event.d() instanceof class_2813 class_2813VarD && MoveUtil.a()) {
            boolean z;
            if (class_2813VarD.method_12193() == 1) {
               if (aM_.field_1724.field_7512.method_34255().method_7909() instanceof class_1747 class_1747VarMethod_7909) {
                  if (class_1747VarMethod_7909.method_7711() instanceof class_2480) {
                     z = true;
                  } else {
                     z = false;
                  }
               } else {
                  z = false;
               }
            } else {
               z = false;
            }

            if (z) {
               stopHandler.a(2);
            }

            this.c.add(new ScreenWalk.a(event.d(), this.b.l("Медленный") ? (this.c.isEmpty() ? 1 : this.c.size() + 1) : 2, z));
            event.a(true);
         }

         if (event.d() instanceof class_2815) {
            if (MoveUtil.a() && aM_.field_1755 instanceof class_490) {
               event.a(true);

               for (ScreenWalk.a packet : this.c) {
                  stopHandler.a(packet.b());
               }
            }

            this.d = false;
         }
      }

      if (event.c() && this.b.l("Медленный")) {
         if (event.d() instanceof class_3944) {
            this.d = true;
         }

         if (event.d() instanceof class_2645) {
            this.d = false;
         }
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (!this.d
         && aM_.field_1755 != null
         && !(aM_.field_1755 instanceof class_408)
         && !(aM_.field_1755 instanceof class_498)
         && !(aM_.field_1755 instanceof class_471)
         && !(aM_.field_1755 instanceof class_481)) {
         for (class_304 keyBinding : new class_304[]{
            aM_.field_1690.field_1894, aM_.field_1690.field_1881, aM_.field_1690.field_1913, aM_.field_1690.field_1849, aM_.field_1690.field_1903
         }) {
            keyBinding.method_23481(class_3675.method_15987(aM_.method_22683().method_4490(), keyBinding.method_1429().method_1444()));
         }
      }

      if (!MoveUtil.a() && !this.c.isEmpty()) {
         ClientConnectionAccessor connection = (ClientConnectionAccessor)aM_.field_1724.field_3944.method_48296();
         if (this.b.l("Медленный")) {
            connection.sendWithoutEvent(this.c.removeFirst().a(), null, true);
         } else {
            this.c.forEach(packetInfo -> connection.sendWithoutEvent(packetInfo.a(), null, true));
            this.c.clear();
         }

         if (this.c.isEmpty() && aM_.field_1755 == null) {
            connection.sendWithoutEvent(new class_2815(aM_.field_1724.field_7512.field_7763), null, true);
         }
      }
   }

   static final class a {
      private final class_2596<?> a;
      private final int b;
      private final boolean c;

      a(class_2596<?> packet, int delay, boolean shulker) {
         this.a = packet;
         this.b = delay;
         this.c = shulker;
      }

      public class_2596<?> a() {
         return this.a;
      }

      public int b() {
         return this.b;
      }

      public boolean c() {
         return this.c;
      }
   }
}

package aethereal.network;

import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.ClickEvent;
import aethereal.event.ContainerEvent;
import aethereal.handler.BaseHandler;
import aethereal.handler.Handler_2;
import aethereal.util.MathUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_2588;
import net.minecraft.class_2960;
import net.minecraft.class_7922;
import net.minecraft.class_7923;
import platform.inject.accessors.HandledScreenAccessor;

@Handler_2
public class DistributionHandler extends BaseHandler implements Interface {
   private boolean b;

   @EventTarget
   public void a(ContainerEvent event) {
      this.b = false;
      if (event.h() == ContainerEvent.Phase.POST
         && event.c() instanceof class_1707
         && event.i().method_10851() instanceof class_2588 class_2588VarMethod_10851
         && (class_2588VarMethod_10851.method_11022().equals("container.chest") || class_2588VarMethod_10851.method_11022().equals("container.chestDouble"))) {
         HandledScreenAccessor screen = (HandledScreenAccessor)event.b();
         float x = screen.getX() + screen.getBackgroundWidth() - 17;
         float y = screen.getY() + 5;
         this.b = MathUtil.a(event.f(), event.g(), x, y, 10.0F, 10.0F);
         Westra.h()
            .d()
            .i()
            .a(
               event.d().method_51448(),
               class_2960.method_60655("westra", this.b ? "pictures/minecraft/distribution_button_hovered.png" : "pictures/minecraft/distribution_button.png"),
               x,
               y,
               10.0F,
               10.0F,
               0.0F,
               -1
            );
         if (this.b) {
            event.d().method_51434(event.b().method_64506(), List.of(class_2561.method_30163("Отсортировать предметы")), event.f(), event.g());
         }
      }
   }

   @EventTarget
   public void a(ClickEvent event) {
      if (event.b() && this.b) {
         class_1707 class_1707Var = aM_.field_1724.field_7512 instanceof class_1707 ? (class_1707)aM_.field_1724.field_7512 : null;
         if (class_1707Var instanceof class_1707) {
            class_1707 handler = class_1707Var;
            List<class_1735> slots = class_1707Var.field_7761.subList(0, class_1707Var.method_17388() * 9);
            List<class_1792> order = new ArrayList<>(
               slots.stream().map(slot -> slot.method_7677().method_7909()).filter(item -> item != class_1802.field_8162).toList()
            );
            class_7922 class_7922Var = class_7923.field_41178;
            order.sort(Comparator.comparingInt(v1 -> class_7922Var.method_10206(v1)));

            for (int i = 0; i < order.size(); i++) {
               class_1792 item2 = order.get(i);
               class_1735 target = slots.get(i);
               if (target.method_7677().method_7909() != item2) {
                  class_1735 source = slots.stream().skip(i + 1).filter(slot2 -> slot2.method_7677().method_7909() == item2).findFirst().orElseThrow();
                  aM_.field_1761.method_2906(handler.field_7763, source.field_7874, 0, class_1713.field_7790, aM_.field_1724);
                  aM_.field_1761.method_2906(handler.field_7763, target.field_7874, 0, class_1713.field_7790, aM_.field_1724);
                  aM_.field_1761.method_2906(handler.field_7763, source.field_7874, 0, class_1713.field_7790, aM_.field_1724);
               }
            }
         }
      }
   }
}

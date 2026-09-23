package aethereal.ui.widget;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ScaleUtil;
import aethereal.ui.element.DragInfo;
import net.minecraft.class_1304;
import net.minecraft.class_1306;
import net.minecraft.class_1799;
import net.minecraft.class_1921;
import net.minecraft.class_2960;

public class ArmorWidget extends Widget implements Interface {
   public ArmorWidget() {
      super(new DragInfo("Броня", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
   }

   @Override
   public void a(DrawEvent event) {
      if (event.b() && !aM_.field_1690.field_1842 && !aM_.field_1724.method_7325()) {
         class_1304[] armorSlots = new class_1304[]{class_1304.field_6166, class_1304.field_6172, class_1304.field_6174, class_1304.field_6169};
         int count = 0;

         for (class_1304 slot : armorSlots) {
            if (!aM_.field_1724.method_6118(slot).method_7960()) {
               count++;
            }
         }

         if (count > 0) {
            ScaleUtil.b(event.i());
            event.i().method_51448().method_22903();
            event.i().method_51448().method_46416(0.0F, -16.0F * Westra.h().d().t().Q().s().c(), 0.0F);
            int startX = aM_.method_22683().method_4486() / 2 - 91 + 182 + 4;
            int startY = aM_.method_22683().method_4502() - 22;
            int epta = startX + (aM_.field_1724.method_6068() == class_1306.field_6182 && !aM_.field_1724.method_6079().method_7960() ? 30 : 0);
            event.i().method_52708(class_1921::method_62277, class_2960.method_60656("hud/hotbar"), 182, 22, 0, 0, epta, startY, count * 20 + 1, 22);
            int index = 0;

            for (class_1304 slot2 : armorSlots) {
               class_1799 stack = aM_.field_1724.method_6118(slot2);
               if (!stack.method_7960()) {
                  int x = epta + 3 + index * 20;
                  int y = startY + 3;
                  event.i().method_51427(stack, x, y);
                  event.i().method_51431(aM_.field_1772, stack, x, y);
                  index++;
               }
            }

            event.i().method_51448().method_22909();
            ScaleUtil.c(event.i());
         }
      }

      super.a(event);
   }
}

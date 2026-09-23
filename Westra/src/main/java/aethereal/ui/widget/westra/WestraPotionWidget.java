package aethereal.ui.widget.westra;

import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.event.DrawEvent;
import aethereal.mixin.IStatusEffectInstance;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_2561;
import net.minecraft.class_4081;

public class WestraPotionWidget extends WestraListWidget implements Interface {
   public WestraPotionWidget() {
      super("Зелья", "Эффекты");
   }

   @Override
   public void a(DrawEvent event) {
      if (aM_.field_1724 != null) {
         for (class_1293 effect : aM_.field_1724.method_6026()) {
            IStatusEffectInstance animated = (IStatusEffectInstance)effect;
            animated.getAnimation().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
            animated.getAnimation().a(true);
         }
      }

      super.a(event);
   }

   @Override
   protected List<WestraListWidget.a> u() {
      List<WestraListWidget.a> rows = new ArrayList<>();
      if (aM_.field_1724 == null) {
         return rows;
      } else {
         for (class_1293 effect : aM_.field_1724.method_6026()) {
            IStatusEffectInstance animated = (IStatusEffectInstance)effect;
            float animation = animated.getAnimation().c();
            if (!(animation <= 0.0F)) {
               String name = class_2561.method_43471(((class_1291)effect.method_5579().comp_349()).method_5567()).getString();
               if (effect.method_5578() > 0) {
                  name = name + " " + (effect.method_5578() + 1);
               }

               int seconds = effect.method_5584() / 20;
               String duration = effect.method_5584() > 1000000 ? "∞" : seconds / 60 + ":" + String.format("%02d", seconds % 60);
               boolean harmful = ((class_1291)effect.method_5579().comp_349()).method_18792() == class_4081.field_18272;
               int initial = Math.max(1, animated.getInitialDuration());
               float progress = effect.method_5584() > 1000000 ? 1.0F : Math.min(1.0F, (float)effect.method_5584() / Math.max(initial, effect.method_5584()));
               rows.add(
                  new WestraListWidget.a(
                     name, duration, harmful ? ColorUtil.a(240, 105, 105, 255) : -1, harmful ? ColorUtil.a(240, 105, 105, 255) : -1, animation, progress
                  )
               );
            }
         }

         return rows;
      }
   }

   @Override
   protected String w() {
      return "t";
   }

   @Override
   public void a(GlobalEvent event) {
      this.d().a(v() || aM_.field_1724 != null && !aM_.field_1724.method_6026().isEmpty());
      super.a(event);
   }
}

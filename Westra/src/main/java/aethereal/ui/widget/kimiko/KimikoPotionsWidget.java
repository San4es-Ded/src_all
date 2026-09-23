package aethereal.ui.widget.kimiko;

import aethereal.config.ThemeInfo;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.mixin.IStatusEffectInstance;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.ui.element.DragInfo;
import aethereal.ui.widget.Widget;
import aethereal.util.MathUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_2561;
import net.minecraft.class_408;
import net.minecraft.class_4081;

public class KimikoPotionsWidget extends Widget implements Interface {
   private static final float g = 7.0F;
   private static final float h = 6.0F;
   private static final float i = 15.0F;
   private static final float j = 5.0F;
   private static final float k = 2.0F;
   private static final float l = 1.5F;
   private final BooleanSetting m = new BooleanSetting("Полоска времени", true);
   private final BooleanSetting n = new BooleanSetting("Подкрашивать вредные", true);

   public KimikoPotionsWidget() {
      super(new DragInfo("Эффекты", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.a(new Setting[]{this.m, this.n});
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      if (aM_.field_1724 != null) {
         for (class_1293 effect : aM_.field_1724.method_6026()) {
            IStatusEffectInstance animated = (IStatusEffectInstance)effect;
            animated.getAnimation().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
            animated.getAnimation().a(true);
         }
      }

      float animation = this.a();
      List<KimikoPotionsWidget.a> rows = this.q();
      float width = 0.0F;
      float height = 0.0F;

      for (KimikoPotionsWidget.a row : rows) {
         width = Math.max(width, 10.0F + Fonts.c.a(row.a(), 7.0F) + 8.0F + Fonts.c.a(row.b(), 6.0F));
         height += 15.0F * row.d();
      }

      float target = MathUtil.c(this.j().f(), Math.max(width, 60.0F), 0.35F);
      float x = this.j().a();
      float y = this.j().b();
      this.j().c(target);
      this.j().d(Math.max(height, 15.0F));
      if (!(animation <= 0.0F) && !rows.isEmpty()) {
         int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
         int muted = Westra.h().d().o().a(ThemeInfo.TEXT_DISABLED).a();
         float rowY = y;

         for (KimikoPotionsWidget.a row : rows) {
            float value = row.d() * animation;
            if (!(value <= 0.002F)) {
               float rowHeight = 15.0F * row.d();
               this.a(event, x, rowY, target, rowHeight - 2.0F, false, value);
               int nameColor = row.e() && this.n.c() ? ColorUtil.a(240, 105, 105, 255) : ColorUtil.a(255, 255, 255, 255);
               float textY = rowY + (rowHeight - 2.0F - Fonts.c.a(7.0F)) / 2.0F - 0.5F;
               Fonts.c.a(event.h(), row.a(), x + 5.0F, textY, 7.0F, ColorUtil.a(nameColor, value));
               float durationWidth = Fonts.c.a(row.b(), 6.0F);
               Fonts.c
                  .a(
                     event.h(),
                     row.b(),
                     x + target - 5.0F - durationWidth,
                     rowY + (rowHeight - 2.0F - Fonts.c.a(6.0F)) / 2.0F - 0.25F,
                     6.0F,
                     ColorUtil.a(muted, value)
                  );
               if (this.m.c()) {
                  float barY = rowY + rowHeight - 2.0F - 1.5F - 1.0F;
                  event.d().a(event.h(), x + 5.0F, barY, target - 10.0F, 1.5F, 0.75F, ColorUtil.a(muted, 0.35F * value));
                  event.d().a(event.h(), x + 5.0F, barY, (target - 10.0F) * row.c(), 1.5F, 0.75F, ColorUtil.a(accent, value));
               }

               rowY += rowHeight;
            }
         }

         super.a(event);
      } else {
         super.a(event);
      }
   }

   private List<KimikoPotionsWidget.a> q() {
      List<KimikoPotionsWidget.a> rows = new ArrayList<>();
      if (aM_.field_1724 == null) {
         return rows;
      } else {
         for (class_1293 effect : aM_.field_1724.method_6026()) {
            IStatusEffectInstance animated = (IStatusEffectInstance)effect;
            float value = animated.getAnimation().c();
            if (!(value <= 0.0F)) {
               class_1291 type = (class_1291)effect.method_5579().comp_349();
               String name = class_2561.method_43471(type.method_5567()).getString();
               if (effect.method_5578() > 0) {
                  name = name + " " + (effect.method_5578() + 1);
               }

               boolean infinite = effect.method_5584() > 1000000;
               int seconds = effect.method_5584() / 20;
               String duration = infinite ? "∞" : seconds / 60 + ":" + String.format("%02d", seconds % 60);
               float ratio = infinite ? 1.0F : Math.min(1.0F, effect.method_5584() / 3600.0F);
               rows.add(new KimikoPotionsWidget.a(name, duration, ratio, value, type.method_18792() == class_4081.field_18272));
            }
         }

         return rows;
      }
   }

   @Override
   public void a(GlobalEvent event) {
      this.d().a(aM_.field_1755 instanceof class_408 || aM_.field_1724 != null && !aM_.field_1724.method_6026().isEmpty());
      super.a(event);
   }

   private record a(String a, String b, float c, float d, boolean e) {
   }
}

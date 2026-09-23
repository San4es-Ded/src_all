package aethereal.ui.widget.pulse;

import aethereal.core.Interface;
import aethereal.event.DrawEvent;
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
import net.minecraft.class_2960;
import net.minecraft.class_640;

public class PulseWatermarkWidget extends Widget implements Interface {
   private static final class_2960 g = class_2960.method_60655("westra", "pictures/pulse_ico.png");
   private static final String h = "westraclient.pro";
   private final BooleanSetting i = new BooleanSetting("Частота кадров", true);
   private final BooleanSetting j = new BooleanSetting("Задержка игрока", true);
   private float k;

   public PulseWatermarkWidget() {
      super(new DragInfo("Инфо-панель", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.j().a(2);
      this.a(new Setting[]{this.i, this.j});
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(true);
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      this.k = MathUtil.c(this.k, aM_.method_47599(), 0.1F);
      float animation = this.a();
      float icon = this.e;
      List<String> sections = this.q();
      float width = 3.0F + icon + 3.5F;

      for (int index = 0; index < sections.size(); index++) {
         if (index > 0) {
            width += 7.0F;
         }

         width += Fonts.e.a(sections.get(index), this.e);
      }

      width += 4.0F;
      float x = this.j().a();
      float y = this.j().b();
      this.j().c(width);
      this.j().d(this.d);
      if (animation <= 0.0F) {
         super.a(event);
      } else {
         this.a(event, x, y, width, this.d, true, animation);
         event.d().a(event.h(), g, x + 3.0F, y + (this.d - icon) / 2.0F, icon, icon, 0.0F, PulseCard.a(animation));
         float textX = x + 3.0F + icon + 3.5F;
         float textY = y + (this.d - Fonts.e.a(this.e)) / 2.0F - 0.5F;

         for (int index = 0; index < sections.size(); index++) {
            if (index > 0) {
               this.a(event, textX + 1.5F, y, this.d, animation);
               textX += 7.0F;
            }

            String section = sections.get(index);
            Fonts.e.a(event.h(), section, textX, textY, this.e, ColorUtil.a(-1, index == 0 ? animation : 0.8F * animation));
            textX += Fonts.e.a(section, this.e);
         }

         super.a(event);
      }
   }

   private List<String> q() {
      List<String> sections = new ArrayList<>();
      sections.add("westraclient.pro");
      if (this.i.c()) {
         sections.add(Math.round(this.k) + " fps");
      }

      if (this.j.c()) {
         sections.add(r() + " ms");
      }

      return sections;
   }

   private static int r() {
      if (aM_.field_1724 != null && aM_.field_1724.field_3944 != null) {
         class_640 entry = aM_.field_1724.field_3944.method_2871(aM_.field_1724.method_5667());
         return entry == null ? 0 : entry.method_2959();
      } else {
         return 0;
      }
   }
}

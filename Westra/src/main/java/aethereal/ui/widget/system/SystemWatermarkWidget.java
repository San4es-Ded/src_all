package aethereal.ui.widget.system;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.ui.element.DragInfo;
import aethereal.ui.widget.Widget;
import aethereal.util.MathUtil;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_640;

public class SystemWatermarkWidget extends Widget implements Interface {
   private static final DateTimeFormatter g = DateTimeFormatter.ofPattern("HH:mm");
   private static final float h = 12.5F;
   private static final float i = 5.0F;
   private static final float j = 7.0F;
   private final BooleanSetting k = new BooleanSetting("Частота кадров", true);
   private final BooleanSetting l = new BooleanSetting("Задержка игрока", true);
   private final BooleanSetting m = new BooleanSetting("Задержка сервера", true);
   private final BooleanSetting n = new BooleanSetting("Текущее время", true);
   private float o;

   public SystemWatermarkWidget() {
      super(new DragInfo("Инфо-панель", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.j().a(2);
      this.a(new Setting[]{this.k, this.l, this.m, this.n});
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(true);
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      this.o = MathUtil.c(this.o, aM_.method_47599(), 0.1F);
      List<String> segments = this.q();
      float target = 5.0F + Fonts.e.a("System", 7.0F);

      for (String segment : segments) {
         target += 5.0F + Fonts.e.a(segment, 7.0F);
      }

      target += 5.0F;
      float width = MathUtil.c(this.j().f(), target, 0.25F);
      float x = this.j().a();
      float y = this.j().b();
      this.j().c(width);
      this.j().d(12.5F);
      float animation = this.a();
      if (animation <= 0.0F) {
         super.a(event);
      } else {
         this.a(event, x, y, width, 12.5F, false, animation);
         float textY = y + (12.5F - Fonts.e.a(7.0F)) / 2.0F - 0.5F;
         float textX = x + 5.0F;
         Fonts.e.a(event.h(), "System", textX, textY, 7.0F, ColorUtil.a(-1, animation));
         textX += Fonts.e.a("System", 7.0F);

         for (String segment : segments) {
            textX += 5.0F;
            Fonts.e.a(event.h(), segment, textX, textY, 7.0F, ColorUtil.a(-1, 0.75F * animation));
            textX += Fonts.e.a(segment, 7.0F);
         }

         super.a(event);
      }
   }

   private List<String> q() {
      List<String> segments = new ArrayList<>();
      if (this.k.c()) {
         segments.add(Math.round(this.o) + " Fps");
      }

      if (this.l.c()) {
         segments.add(r() + " Ping");
      }

      if (this.m.c()) {
         segments.add(String.format("%.1f TPS", Westra.h().d().v().j().a()));
      }

      if (this.n.c()) {
         segments.add(LocalTime.now().format(g));
      }

      return segments;
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

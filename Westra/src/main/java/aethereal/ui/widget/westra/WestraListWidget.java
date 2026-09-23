package aethereal.ui.widget.westra;

import aethereal.config.ThemeInfo;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.ui.recode.RecodeKit;
import aethereal.util.MathUtil;
import java.util.List;
import net.minecraft.class_408;
import net.minecraft.class_4587;

public abstract class WestraListWidget extends WestraWidget implements Interface {
   protected static final float O = 12.0F;
   private static final float HEADER = 15.0F;
   private final String P;
   private final BooleanSetting Q = new BooleanSetting("Показывать заголовок", true);

   protected WestraListWidget(String name, String title) {
      super(name);
      this.P = title;
      this.a(new Setting[]{this.Q});
   }

   protected abstract List<WestraListWidget.a> u();

   protected String w() {
      return "C";
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float animation = this.a();
      List<WestraListWidget.a> rows = this.u();
      boolean header = this.Q.c();
      String count = String.valueOf(rows.size());
      float target = 70.0F;
      if (header) {
         target = Math.max(target, 20.0F + Fonts.d.a(this.P, 6.5F) + 6.0F + WestraStyle.f(count) + 5.0F);
      }

      float rowsHeight = 0.0F;

      for (WestraListWidget.a row : rows) {
         target = Math.max(target, 7.0F + Fonts.d.a(row.a, 6.25F) + 8.0F + WestraStyle.f(row.b) + 5.0F);
         rowsHeight += 12.0F * row.e;
      }

      float width = MathUtil.c(this.j().f(), target, 0.35F);
      float height = (header ? 15.0F : 3.0F) + rowsHeight + 3.0F;
      float x = this.j().a();
      float y = this.j().b();
      this.j().c(width);
      this.j().d(Math.max(height, 15.0F));
      if (animation <= 0.0F) {
         super.a(event);
      } else {
         class_4587 matrices = event.h();
         float cardHeight = Math.max(height, 15.0F);
         this.q(event, x, y, width, cardHeight, animation, false);
         if (header) {
            WestraStyle.e(event, this.w(), x + 4.0F, y + 3.0F, 9.0F, animation);
            Fonts.d.a(matrices, this.P, x + 17.0F, Fonts.d.a(this.P, 6.5F, y + 7.5F), 6.5F, ColorUtil.a(-1, animation));
            WestraStyle.f(event, count, x + width - 4.5F, y + 7.5F, -1, animation);
            if (rowsHeight > 0.5F) {
               event.d().a(matrices, x + 5.0F, y + 15.0F - 0.25F, width - 10.0F, 0.5F, 0.0F, ColorUtil.a(-1, 0.07F * animation));
            }
         }

         float rowY = y + (header ? 15.0F : 3.0F);

         for (WestraListWidget.a row : rows) {
            float value = row.e * animation;
            if (!(value <= 0.002F)) {
               float rowHeight = 12.0F * row.e;
               float shift = (1.0F - EasingList.p.ease(row.e)) * 8.0F;
               matrices.method_22903();
               matrices.method_46416(shift, 0.0F, 0.0F);
               float center = rowY + rowHeight / 2.0F - (row.f >= 0.0F ? 0.75F : 0.0F);
               int labelColor = row.c == -1 ? ColorUtil.a(RecodeKit.text(), -1, 0.6F) : row.c;
               Fonts.d.a(matrices, row.a, x + 7.0F, Fonts.d.a(row.a, 6.25F, center), 6.25F, ColorUtil.a(labelColor, value));
               WestraStyle.f(event, row.b, x + width - 4.5F, center, row.d, value);
               if (row.f >= 0.0F) {
                  float barX = x + 7.0F;
                  float barW = width - 11.5F;
                  float barY = rowY + rowHeight - 2.25F;
                  event.d().a(matrices, barX, barY, barW, 1.0F, 0.5F, ColorUtil.a(-1, 0.06F * value));
                  int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
                  int left = ColorUtil.a(accent, value);
                  int right = ColorUtil.a(RecodeKit.accentShade(), value);
                  event.d().a(matrices, barX, barY, Math.max(1.0F, barW * MathUtil.b(row.f, 0.0F, 1.0F)), 1.0F, 0.5F, left, right, left, right);
               }

               matrices.method_22909();
               rowY += rowHeight;
            }
         }

         super.a(event);
      }
   }

   protected static boolean v() {
      return aM_.field_1755 instanceof class_408;
   }

   public static final class a {
      private final String a;
      private final String b;
      private final int c;
      private final int d;
      private final float e;
      private final float f;

      public a(String label, String value, int labelColor, int valueColor, float animation) {
         this(label, value, labelColor, valueColor, animation, -1.0F);
      }

      public a(String label, String value, int labelColor, int valueColor, float animation, float progress) {
         this.a = label;
         this.b = value;
         this.c = labelColor;
         this.d = valueColor;
         this.e = animation;
         this.f = progress;
      }
   }
}

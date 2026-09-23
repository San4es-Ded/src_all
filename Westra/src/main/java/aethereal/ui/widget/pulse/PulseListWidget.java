package aethereal.ui.widget.pulse;

import aethereal.core.Interface;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.ui.element.DragInfo;
import aethereal.ui.widget.Widget;
import aethereal.util.MathUtil;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_4587;

public abstract class PulseListWidget extends Widget implements Interface {
   protected static final float g = 12.5F;
   protected static final float h = 11.5F;
   protected static final float i = 7.0F;
   protected static final float j = 5.0F;
   private final String k;

   protected PulseListWidget(String dragName, String title) {
      super(new DragInfo(dragName, 0.0F, 0.0F, 0.0F, 0.0F));
      this.k = title;
      this.j().a(this);
   }

   protected abstract List<PulseListWidget.a> q();

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float x = this.j().a();
      float y = this.j().b();
      float animation = this.a();
      List<PulseListWidget.a> rows = this.q();
      float target = 10.0F + Fonts.e.a(this.k, 7.0F) + 6.0F;
      float content = 0.0F;

      for (PulseListWidget.a row : rows) {
         target = Math.max(target, 10.0F + Fonts.e.a(row.a(), 7.0F) + 10.0F + Fonts.e.a(row.b(), 7.0F));
         content += 11.5F * row.d();
      }

      float width = MathUtil.c(this.j().f(), target, 0.5F);
      float height = 12.5F + content + (content > 0.0F ? 3.0F : 0.0F);
      this.j().c(width);
      this.j().d(height);
      if (animation <= 0.0F) {
         super.a(event);
      } else {
         PulseCard.a(event, x, y, width, height, 3.25F, animation);
         event.d().a(event.h(), x + 5.0F, y + 3.75F, 1.5F, 5.0F, 0.75F, PulseCard.a(animation));
         Fonts.e.a(event.h(), this.k, x + 5.0F + 4.0F, y + (12.5F - Fonts.e.a(7.0F)) / 2.0F - 0.5F, 7.0F, ColorUtil.a(-1, animation));
         class_4587 matrices = event.h();
         float rowY = y + 12.5F;

         for (PulseListWidget.a row : rows) {
            float value = row.d() * animation;
            if (!(value <= 0.0F)) {
               float rowHeight = 11.5F * value;
               float offsetX = -6.0F * (1.0F - value);
               float textY = rowY + (rowHeight - Fonts.e.a(7.0F)) / 2.0F - 0.5F;
               Fonts.e.a(matrices, row.a(), x + 5.0F + offsetX, textY, 7.0F, ColorUtil.a(row.c(), value));
               Fonts.e.a(matrices, row.b(), x + width - 5.0F - Fonts.e.a(row.b(), 7.0F), textY, 7.0F, ColorUtil.a(row.e(), 0.93F * value));
               rowY += rowHeight;
            }
         }

         super.a(event);
      }
   }

   protected static final class a {
      private final String a;
      private final String b;
      private final int c;
      private final int d;
      private final float e;

      public a(String left, String right, int leftColor, int rightColor, float animation) {
         this.a = left;
         this.b = right;
         this.c = leftColor;
         this.d = rightColor;
         this.e = animation;
      }

      @Generated
      public String a() {
         return this.a;
      }

      @Generated
      public String b() {
         return this.b;
      }

      @Generated
      public int c() {
         return this.c;
      }

      @Generated
      public int e() {
         return this.d;
      }

      @Generated
      public float d() {
         return this.e;
      }
   }
}

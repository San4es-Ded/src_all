package aethereal.ui.widget.system;

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

public abstract class SystemListWidget extends Widget implements Interface {
   protected static final float g = 12.5F;
   protected static final float h = 11.5F;
   protected static final float i = 7.0F;
   protected static final float j = 5.0F;
   private final String k;

   protected SystemListWidget(String dragName, String title) {
      super(new DragInfo(dragName, 0.0F, 0.0F, 0.0F, 0.0F));
      this.k = title;
      this.j().a(this);
   }

   protected abstract List<SystemListWidget.a> q();

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float x = this.j().a();
      float y = this.j().b();
      float animation = this.a();
      List<SystemListWidget.a> rows = this.q();
      float target = 10.0F + Fonts.e.a(this.k, 7.0F);

      for (SystemListWidget.a row : rows) {
         target = Math.max(target, 10.0F + Fonts.e.a(row.a(), 7.0F) + 8.0F + Fonts.e.a(row.b(), 7.0F));
      }

      float width = MathUtil.c(this.j().f(), target, 0.5F);
      this.j().c(width);
      if (animation <= 0.0F) {
         this.j().d(12.5F);
         super.a(event);
      } else {
         this.a(event, x, y, width, 12.5F, false, animation);
         Fonts.e.a(event.h(), this.k, x + 5.0F, y + (12.5F - Fonts.e.a(7.0F)) / 2.0F - 0.5F, 7.0F, ColorUtil.a(-1, animation));
         class_4587 matrices = event.h();
         float rowY = y + 12.5F + 1.0F;

         for (SystemListWidget.a row : rows) {
            float value = row.d() * animation;
            if (!(value <= 0.0F)) {
               float height = 11.5F * value;
               float scale = 0.95F + 0.05F * EasingList.g.ease(value);
               float centerX = x + width / 2.0F;
               float centerY = rowY + height / 2.0F;
               matrices.method_22903();
               matrices.method_46416(centerX, centerY, 0.0F);
               matrices.method_22905(scale, scale, 1.0F);
               matrices.method_46416(-centerX, -centerY, 0.0F);
               this.a(event, x, rowY, width, height, false, value);
               float textY = rowY + (height - Fonts.e.a(7.0F)) / 2.0F - 0.5F;
               Fonts.e.a(matrices, row.a(), x + 5.0F, textY, 7.0F, ColorUtil.a(row.c(), value));
               Fonts.e.a(matrices, row.b(), x + width - 5.0F - Fonts.e.a(row.b(), 7.0F), textY, 7.0F, ColorUtil.a(row.e(), 0.75F * value));
               matrices.method_22909();
               rowY += 12.5F * value;
            }
         }

         this.j().d(rows.isEmpty() ? 12.5F : rowY - y - 1.0F);
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

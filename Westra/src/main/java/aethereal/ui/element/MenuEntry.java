package aethereal.ui.element;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Westra;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.util.MathUtil;
import lombok.Generated;
import net.minecraft.class_332;
import net.minecraft.class_4587;

public class MenuEntry {
   private final AnimationUtil a = new AnimationUtil();
   private final String b;
   private final String c;
   private final boolean d;
   private final Runnable e;
   private float f;
   private float g;
   private float h;
   private float i;

   @Generated
   public AnimationUtil a() {
      return this.a;
   }

   @Generated
   public String b() {
      return this.b;
   }

   @Generated
   public String c() {
      return this.c;
   }

   @Generated
   public boolean d() {
      return this.d;
   }

   @Generated
   public Runnable e() {
      return this.e;
   }

   @Generated
   public float f() {
      return this.f;
   }

   @Generated
   public float g() {
      return this.g;
   }

   @Generated
   public float h() {
      return this.h;
   }

   @Generated
   public float i() {
      return this.i;
   }

   public MenuEntry(String glyph, String label, boolean danger, Runnable action) {
      this.b = glyph;
      this.c = label;
      this.d = danger;
      this.e = action;
   }

   public void a(float x, float y, float width, float height) {
      this.f = x;
      this.g = y;
      this.h = width;
      this.i = height;
   }

   public boolean a(double mouseX, double mouseY) {
      return MathUtil.a(mouseX, mouseY, this.f, this.g, this.h, this.i);
   }

   public void a(class_332 context, double mouseX, double mouseY, float delta, float open) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      ThemeProcessor theme = Westra.h().d().o();
      this.a.a(this.e != null && this.a(mouseX, mouseY));
      this.a.a(0.0F, 1.0F, 0.35F, EasingList.i, delta);
      float hover = this.a.c();
      float slide = EasingList.p.ease(hover);
      int accent = this.d ? ColorUtil.a(235, 86, 106, 255) : theme.a(ThemeInfo.PRIMARY).a();
      float centerY = this.g + this.i / 2.0F;
      draw.a(matrices, this.f, this.g, this.h, this.i, 5.0F, ColorUtil.a(accent, 0.07058824F * hover * open));
      draw.a(
         matrices,
         this.f,
         this.g,
         this.h,
         this.i,
         5.0F,
         0.5F,
         ColorUtil.a(theme.a(ThemeInfo.OUTLINE_SMALL).a(), theme.a(ThemeInfo.OUTLINE_SMALL).b() * hover * open)
      );
      float barHeight = (this.i - 8.0F) * slide;
      if (barHeight > 0.0F) {
         draw.a(matrices, this.f + 3.0F, centerY - barHeight / 2.0F, 2.0F, barHeight, 1.0F, ColorUtil.a(accent, open));
      }

      float shift = 2.0F * slide;
      int iconColor = ColorUtil.a(ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), accent, hover), open);
      int labelColor = ColorUtil.a(ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), theme.a(ThemeInfo.TEXT).a(), hover), open);
      Fonts.a.a(matrices, this.b, this.f + 11.0F + shift, Fonts.a.a(this.b, 8.0F, centerY), 8.0F, iconColor);
      Fonts.e.a(matrices, this.c, this.f + 24.0F + shift, centerY - Fonts.e.a(8.0F) / 2.0F - 0.5F, 8.0F, labelColor);
   }
}

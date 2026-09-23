package aethereal.ui.screen;

import aethereal.config.ThemeConstructor;
import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.config.ThemeType;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.ui.recode.RecodeKit;
import aethereal.util.MathUtil;
import java.awt.Color;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;

public class ThemePanel implements Interface {
   public static final float a = 104.0F;
   private static final float b = 8.0F;
   private static final float c = 12.0F;
   private static final float d = 11.5F;
   private static final float t = 6.0F;
   private static final float u = 4.0F;
   private static final float v = 60.0F;
   private static final int[] e = new int[]{
      ColorUtil.a(255, 111, 181, 255),
      ColorUtil.a(150, 120, 255, 255),
      ColorUtil.a(90, 190, 255, 255),
      ColorUtil.a(90, 220, 160, 255),
      ColorUtil.a(255, 175, 90, 255),
      ColorUtil.a(240, 90, 90, 255)
   };
   private static final String[] f = new String[]{"Акцент", "Фон худа", "Фон меню", "Обводка тонкая", "Обводка", "Текст", "Текст тусклый"};
   private final Vector4f g = new Vector4f();
   private final Vector4f h = new Vector4f();
   private final Vector4f i = new Vector4f();
   private final Vector4f j = new Vector4f();
   private final AnimationUtil[] k = new AnimationUtil[ThemeInfo.values().length];
   private final AnimationUtil l = new AnimationUtil();
   private ThemeInfo m = ThemeInfo.PRIMARY;
   private float n;
   private float o;
   private float p;
   private float q;
   private float r;
   private boolean s;

   public ThemePanel() {
      for (int index = 0; index < this.k.length; index++) {
         this.k[index] = new AnimationUtil();
      }

      this.d();
   }

   public Vector4f a() {
      return this.g;
   }

   public void a(class_332 context, float winX, float winY, float winW, float winH, double mouseX, double mouseY, float delta, float open) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      ThemeProcessor theme = Westra.h().d().o();
      float x = winX + winW + 10.0F;
      this.g.set(x, winY, 104.0F, winH);
      int accent = theme.a(ThemeInfo.PRIMARY).a();
      RecodeKit.glass(matrices, x, winY, 104.0F, winH, 13.0F, open, 0.93F, 0.1F, 0.12F);
      Fonts.d.a(matrices, "Тема", x + 8.0F, winY + 9.0F, 9.0F, ColorUtil.a(-1, open));
      this.a(context, theme, x, winY + 8.0F, mouseX, mouseY, delta, open);
      draw.a(matrices, x + 8.0F, winY + 24.0F, 88.0F, 0.5F, 0.0F, ColorUtil.a(-1, 0.07F * open));
      float cursor = winY + 29.0F;
      ThemeInfo[] tokens = ThemeInfo.values();

      for (int index = 0; index < tokens.length; index++) {
         boolean hovered = MathUtil.a(mouseX, mouseY, x + 8.0F, cursor, 88.0F, 11.5F);
         AnimationUtil animation = this.k[index];
         animation.a(0.0F, 1.0F, 0.25F, EasingList.i, delta);
         animation.a(hovered || tokens[index] == this.m);
         float value = animation.c();
         ThemeConstructor token = theme.a(tokens[index]);
         if (tokens[index] == this.m) {
            int pillLeft = ColorUtil.a(accent, 0.26F * open);
            int pillRight = ColorUtil.a(RecodeKit.accentShade(), 0.08F * open);
            draw.a(matrices, x + 8.0F, cursor, 88.0F, 11.5F, 5.0F, pillLeft, pillRight, pillLeft, pillRight);
            draw.a(matrices, x + 8.0F, cursor, 88.0F, 11.5F, 5.0F, 0.5F, ColorUtil.a(accent, 0.38F * open));
         } else if (value > 0.004F) {
            draw.a(matrices, x + 8.0F, cursor, 88.0F, 11.5F, 5.0F, ColorUtil.a(-1, 0.04F * value * open));
         }

         Fonts.c
            .a(
               matrices,
               f[index],
               x + 8.0F + 4.0F,
               Fonts.c.a(f[index], 6.5F, cursor + 5.75F),
               6.5F,
               ColorUtil.a(tokens[index] == this.m ? theme.a(ThemeInfo.TEXT).a() : theme.a(ThemeInfo.TEXT_DISABLED).a(), open)
            );
         float swatch = 7.0F;
         float swatchX = x + 104.0F - 8.0F - 4.0F - swatch;
         draw.a(matrices, swatchX, cursor + (11.5F - swatch) / 2.0F, swatch, swatch, swatch / 2.0F, ColorUtil.a(token.a(), open));
         draw.a(matrices, swatchX, cursor + (11.5F - swatch) / 2.0F, swatch, swatch, swatch / 2.0F, 0.5F, ColorUtil.a(-1, 0.25F * open));
         cursor += 12.5F;
      }

      cursor += 4.0F;
      this.b(context, theme, x, cursor, mouseX, mouseY, delta, open);
      cursor += 17.0F;
      this.a(context, x, cursor, mouseX, mouseY, open);
      cursor += 66.0F;
      this.b(context, theme, x, cursor, mouseX, mouseY, open);
   }

   private void a(class_332 context, ThemeProcessor theme, float x, float y, double mouseX, double mouseY, float delta, float open) {
      Draw2DProcessor draw = Westra.h().d().i();
      class_4587 matrices = context.method_51448();
      float width = 14.0F;
      float height = 8.5F;
      float toggleX = x + 104.0F - 8.0F - width;
      float toggleY = y + 3.0F;
      this.j.set(toggleX, toggleY, width, height);
      boolean light = theme.a() == ThemeType.LIGHT;
      this.l.a(0.0F, 1.0F, 0.5F, EasingList.i, delta);
      this.l.a(light);
      float value = this.l.c();
      int accent = theme.a(ThemeInfo.PRIMARY).a();
      RecodeKit.toggle(matrices, toggleX, toggleY, width, height, value, open);
      String label = light ? "Светлая" : "Тёмная";
      Fonts.c
         .a(
            matrices,
            label,
            toggleX - 3.0F - Fonts.c.a(label, 6.5F),
            Fonts.c.a(label, 6.5F, toggleY + height / 2.0F),
            6.5F,
            ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), open)
         );
   }

   private void b(class_332 context, ThemeProcessor theme, float x, float y, double mouseX, double mouseY, float delta, float open) {
      Draw2DProcessor draw = Westra.h().d().i();
      class_4587 matrices = context.method_51448();
      float size = 11.0F;
      float gap = (88.0F - size * e.length) / (e.length - 1);
      this.i.set(x + 8.0F, y, 88.0F, size);

      for (int index = 0; index < e.length; index++) {
         float swatchX = x + 8.0F + index * (size + gap);
         boolean hovered = MathUtil.a(mouseX, mouseY, swatchX, y, size, size);
         if (hovered) {
            draw.a(matrices, swatchX - 1.5F, y - 1.5F, size + 3.0F, size + 3.0F, (size + 3.0F) / 2.0F, ColorUtil.a(e[index], 0.3F * open));
         }

         draw.a(matrices, swatchX, y, size, size, size / 2.0F, ColorUtil.a(e[index], open));
         draw.a(matrices, swatchX, y, size, size, size / 2.0F, 0.5F, ColorUtil.a(-1, (hovered ? 0.6F : 0.15F) * open));
      }
   }

   private void a(class_332 context, float x, float y, double mouseX, double mouseY, float open) {
      Draw2DProcessor draw = Westra.h().d().i();
      class_4587 matrices = context.method_51448();
      ThemeProcessor theme = Westra.h().d().o();
      float area = 68.0F;
      this.h.set(x + 8.0F, y, area, 60.0F);
      float hueX = this.e();
      float alphaX = this.f();
      int hueColor = Color.HSBtoRGB(this.o, 1.0F, 1.0F);
      int rgb = theme.a(this.m).a() & 16777215;
      int handle = ColorUtil.a(16777215, open);
      draw.a(
         matrices,
         this.h.x,
         this.h.y,
         this.h.z,
         this.h.w,
         5.0F,
         ColorUtil.a(16777215, open),
         ColorUtil.a(hueColor, open),
         ColorUtil.a(0, open),
         ColorUtil.a(0, open)
      );
      draw.a(matrices, this.h.x, this.h.y, this.h.z, this.h.w, 5.0F, 0.5F, ColorUtil.a(-1, 0.12F * open));
      float cursorX = MathUtil.b(this.h.x + this.p * this.h.z, this.h.x + 2.0F, this.h.x + this.h.z - 2.0F);
      float cursorY = MathUtil.b(this.h.y + (1.0F - this.q) * this.h.w, this.h.y + 2.0F, this.h.y + this.h.w - 2.0F);
      draw.a(matrices, cursorX - 3.0F, cursorY - 3.0F, 6.0F, 6.0F, 3.0F, 0.75F, handle);
      draw.a(matrices, class_2960.method_60655("westra", "pictures/color.png"), hueX, y, 6.0F, 60.0F, 3.0F, ColorUtil.a(16777215, open));
      draw.a(matrices, hueX - 1.0F, y + this.o * 60.0F - 1.0F, 8.0F, 2.0F, 1.0F, 0.75F, handle);
      draw.a(matrices, class_2960.method_60655("westra", "pictures/opacity.png"), alphaX, y, 6.0F, 60.0F, 3.0F, ColorUtil.a(16777215, 0.019607844F * open));
      draw.a(matrices, alphaX, y, 6.0F, 60.0F, 3.0F, ColorUtil.a(rgb, open), ColorUtil.a(rgb, open), ColorUtil.a(rgb, 0.0F), ColorUtil.a(rgb, 0.0F));
      draw.a(matrices, alphaX - 1.0F, y + (1.0F - this.r) * 60.0F - 1.0F, 8.0F, 2.0F, 1.0F, 0.75F, handle);
   }

   private float e() {
      return this.h.x + this.h.z + 4.0F;
   }

   private float f() {
      return this.e() + 6.0F + 4.0F;
   }

   private void b(class_332 context, ThemeProcessor theme, float x, float y, double mouseX, double mouseY, float open) {
      Draw2DProcessor draw = Westra.h().d().i();
      class_4587 matrices = context.method_51448();
      float width = 88.0F;
      float height = 12.0F;
      this.n = y;
      boolean hovered = MathUtil.a(mouseX, mouseY, x + 8.0F, y, width, height);
      int accent = theme.a(ThemeInfo.PRIMARY).a();
      int buttonLeft = ColorUtil.a(accent, (hovered ? 0.42F : 0.22F) * open);
      int buttonRight = ColorUtil.a(RecodeKit.accentShade(), (hovered ? 0.32F : 0.12F) * open);
      draw.a(matrices, x + 8.0F, y, width, height, 5.0F, buttonLeft, buttonRight, buttonLeft, buttonRight);
      draw.a(matrices, x + 8.0F, y, width, height, 5.0F, 0.5F, ColorUtil.a(accent, (hovered ? 0.7F : 0.4F) * open));
      String label = "Сбросить";
      Fonts.d.a(matrices, label, x + 8.0F + (width - Fonts.d.a(label, 6.5F)) / 2.0F, Fonts.d.a(label, 6.5F, y + height / 2.0F), 6.5F, ColorUtil.a(-1, open));
   }

   public boolean a(double mouseX, double mouseY, int button) {
      if (!MathUtil.a(mouseX, mouseY, this.g.x, this.g.y, this.g.z, this.g.w)) {
         return false;
      } else {
         ThemeProcessor theme = Westra.h().d().o();
         if (button == 0 && MathUtil.a(mouseX, mouseY, this.j.x, this.j.y, this.j.z, this.j.w)) {
            theme.a(theme.a() == ThemeType.LIGHT ? ThemeType.DARK : ThemeType.LIGHT);
            Westra.h().d().t().bd().q(theme.a(ThemeInfo.PRIMARY).a());
            this.d();
            return true;
         } else {
            float cursor = this.g.y + 29.0F;
            ThemeInfo[] tokens = ThemeInfo.values();

            for (ThemeInfo token : tokens) {
               if (MathUtil.a(mouseX, mouseY, this.g.x + 8.0F, cursor, 88.0F, 11.5F)) {
                  this.m = token;
                  this.d();
                  return true;
               }

               cursor += 12.5F;
            }

            if (button == 0 && MathUtil.a(mouseX, mouseY, this.i.x, this.i.y, this.i.z, this.i.w)) {
               float size = 11.0F;
               float gap = (this.i.z - size * e.length) / (e.length - 1);

               for (int index = 0; index < e.length; index++) {
                  if (MathUtil.a(mouseX, mouseY, this.i.x + index * (size + gap), this.i.y, size, size)) {
                     this.m = ThemeInfo.PRIMARY;
                     this.c(e[index]);
                     if (this.m == ThemeInfo.PRIMARY) {
                        this.d();
                     }

                     return true;
                  }
               }
            }

            if (button == 0 && MathUtil.a(mouseX, mouseY, this.g.x + 8.0F, this.n, 88.0F, 12.0F)) {
               theme.b(theme.a());
               Westra.h().d().t().bd().q(theme.a(ThemeInfo.PRIMARY).a());
               this.d();
               return true;
            } else if (button != 0
               || !MathUtil.a(mouseX, mouseY, this.h.x, this.h.y, this.h.z, this.h.w)
                  && !MathUtil.a(mouseX, mouseY, this.e(), this.h.y, 6.0F, this.h.w)
                  && !MathUtil.a(mouseX, mouseY, this.f(), this.h.y, 6.0F, this.h.w)) {
               return true;
            } else {
               this.s = true;
               this.b(mouseX, mouseY);
               return true;
            }
         }
      }
   }

   public boolean b(double mouseX, double mouseY, int button) {
      boolean dragging = this.s;
      this.s = false;
      return dragging;
   }

   public boolean a(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (!this.s) {
         return false;
      } else {
         this.b(mouseX, mouseY);
         return true;
      }
   }

   private void b(double mouseX, double mouseY) {
      float stripX = this.e();
      float alphaX = this.f();
      if (mouseX >= alphaX) {
         this.r = 1.0F - MathUtil.b((float)(mouseY - this.h.y) / this.h.w, 0.0F, 1.0F);
      } else if (mouseX >= stripX) {
         this.o = MathUtil.b((float)(mouseY - this.h.y) / this.h.w, 0.0F, 1.0F);
      } else {
         this.p = MathUtil.b((float)(mouseX - this.h.x) / this.h.z, 0.0F, 1.0F);
         this.q = 1.0F - MathUtil.b((float)(mouseY - this.h.y) / this.h.w, 0.0F, 1.0F);
      }

      this.c(ColorUtil.a(Color.HSBtoRGB(this.o, this.p, this.q), this.r));
   }

   private void c(int color) {
      if (this.m == ThemeInfo.PRIMARY) {
         Westra.h().d().t().bd().q(color);
      } else {
         Westra.h().d().o().a(this.m).a(color);
      }
   }

   private void d() {
      int color = Westra.h().d().o().a(this.m).a();
      float[] hsb = Color.RGBtoHSB(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, (float[])null);
      this.o = hsb[0];
      this.p = hsb[1];
      this.q = hsb[2];
      this.r = (color >> 24 & 0xFF) / 255.0F;
   }
}

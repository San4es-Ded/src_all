package aethereal.ui.element;

import aethereal.config.ThemeInfo;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Font;
import aethereal.render.Fonts;
import aethereal.util.CursorUtil;
import aethereal.util.MathUtil;
import lombok.Generated;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_5611;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class TextField {
   private final TextField.a a;
   private final boolean b;
   private class_5611 c = new class_5611(0.0F, 0.0F);
   private class_5611 d = new class_5611(0.0F, 0.0F);
   private String e = "";
   private final StringBuilder f = new StringBuilder();
   private class_5611 g = new class_5611(0.0F, 0.0F);
   private int h;
   private boolean i;
   private boolean j;
   private float k;

   @Generated
   public TextField.a b() {
      return this.a;
   }

   @Generated
   public boolean c() {
      return this.b;
   }

   @Generated
   public void a(class_5611 position) {
      this.c = position;
   }

   @Generated
   public class_5611 d() {
      return this.c;
   }

   @Generated
   public void b(class_5611 size) {
      this.d = size;
   }

   @Generated
   public class_5611 e() {
      return this.d;
   }

   @Generated
   public void a(String placeholder) {
      this.e = placeholder;
   }

   @Generated
   public String f() {
      return this.e;
   }

   @Generated
   public StringBuilder g() {
      return this.f;
   }

   @Generated
   public class_5611 h() {
      return this.g;
   }

   @Generated
   public int i() {
      return this.h;
   }

   @Generated
   public boolean j() {
      return this.i;
   }

   @Generated
   public boolean k() {
      return this.j;
   }

   @Generated
   public float l() {
      return this.k;
   }

   public TextField(TextField.a type) {
      this(type, false);
   }

   public TextField(TextField.a type, boolean numbers) {
      this.a = type;
      this.b = numbers;
   }

   public void a(class_332 context, double mouseX, double mouseY, float delta, float alpha) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      float lineHeight = this.a.d.d().lineHeight() * this.a.e;
      float textX = this.c.method_32118() + this.a.f;
      float textY = this.c.method_32119() + (this.d.method_32119() - lineHeight) / 2.0F + this.a.g;
      float visibleWidth = this.d.method_32118() - this.a.f * 2.0F;
      this.a(visibleWidth);
      boolean hover = MathUtil.a(mouseX, mouseY, this.c.method_32118(), this.c.method_32119(), this.d.method_32118(), this.d.method_32119());
      if (hover != this.j) {
         this.j = hover;
         CursorUtil.a(hover ? CursorUtil.a.TEXT : CursorUtil.a.DEFAULT);
      }

      boolean placeholding = !this.i && this.f.isEmpty() && this.a.h;
      String content = placeholding ? this.e : this.f.toString();
      int color = ColorUtil.a(Westra.h().d().o().a(placeholding ? ThemeInfo.TEXT_DISABLED : ThemeInfo.TEXT).a(), alpha);
      this.a.a(draw, matrices, this.c.method_32118(), this.c.method_32119(), this.d.method_32118(), this.d.method_32119(), alpha);
      this.a(context, textX, textY, lineHeight, visibleWidth, alpha);
      this.b(context, textX, textY, lineHeight, visibleWidth, alpha);
      this.a.d.c(matrices, this.b(content), textX, textY, this.a.e, color, visibleWidth);
   }

   private void a(class_332 context, float textX, float textY, float lineHeight, float visibleWidth, float alpha) {
      if (this.i && this.g.method_32118() != this.g.method_32119()) {
         int from = (int)Math.min(this.g.method_32118(), this.g.method_32119());
         int to = (int)Math.max(this.g.method_32118(), this.g.method_32119());
         float start = Math.max(textX + this.a(from) - this.k, textX);
         float end = Math.min(textX + this.a(to) - this.k, textX + visibleWidth);
         if (start < end) {
            Draw2DProcessor draw = Westra.h().d().i();
            draw.a(context, start, textY, end - start, lineHeight, ColorUtil.a(Westra.h().d().o().a(ThemeInfo.PRIMARY).a(), 0.47F * alpha));
         }
      }
   }

   private void b(class_332 context, float textX, float textY, float lineHeight, float visibleWidth, float alpha) {
      if (this.i) {
         float caretX = textX + this.a((int)this.g.method_32119()) - this.k;
         if (caretX >= textX && caretX <= textX + visibleWidth) {
            float blink = (float)(Math.sin(System.currentTimeMillis() / 150.0) * 0.5 + 0.5);
            float caretHeight = this.a.e / 1.01F;
            float caretY = textY + (lineHeight - caretHeight) / 2.0F;
            Draw2DProcessor draw = Westra.h().d().i();
            draw.a(context, caretX, caretY, 0.5F, caretHeight, ColorUtil.a(Westra.h().d().o().a(ThemeInfo.TEXT).a(), blink * alpha));
         }
      }
   }

   private float a(int index) {
      return this.a.d.a(this.f.substring(0, Math.min(index, this.f.length())), this.a.e) + 0.5F;
   }

   private String b(String content) {
      if (content != null && !content.isEmpty()) {
         for (int i = 0; i < content.length(); i++) {
            if (this.a.d.a(content.substring(0, i), this.a.e) >= this.k) {
               return content.substring(i);
            }
         }

         return "";
      } else {
         return content == null ? "" : content;
      }
   }

   private void a(float visibleWidth) {
      float cursor = this.a((int)this.g.method_32119());
      if (cursor - this.k > visibleWidth) {
         this.k = cursor - visibleWidth;
      } else if (cursor < this.k) {
         this.k = cursor;
      }

      if (this.a.d.a(this.f.toString(), this.a.e) < visibleWidth) {
         this.k = 0.0F;
      }
   }

   public void a(double mouseX, double mouseY, int button) {
      if (MathUtil.a(mouseX, mouseY, this.c.method_32118(), this.c.method_32119(), this.d.method_32118(), this.d.method_32119())) {
         this.i = true;
         if (button == 0) {
            int cursor = this.b((float)mouseX);
            this.h = cursor;
            this.g = new class_5611(cursor, cursor);
         }
      } else {
         if (this.i) {
            this.a(false);
         }
      }
   }

   public void b(double mouseX, double mouseY, int button) {
      if (this.i && button == 0) {
         int cursor = this.b((float)mouseX);
         this.g = new class_5611(Math.min(this.h, cursor), Math.max(this.h, cursor));
      }
   }

   private int b(float mouseX) {
      float textX = this.c.method_32118() + this.a.f;
      float adjusted = mouseX + this.k;

      for (int i = 0; i <= this.f.length(); i++) {
         if (textX + this.a(i) > adjusted) {
            return i;
         }
      }

      return this.f.length();
   }

   public void a(int keyCode, int scanCode, int modifiers) {
      if (this.i) {
         boolean ctrl = (modifiers & 2) != 0;
         boolean hasSelection = this.g.method_32118() != this.g.method_32119();
         if (ctrl && keyCode == 65) {
            this.g = new class_5611(0.0F, this.f.length());
            return;
         }

         if (ctrl && keyCode == 67) {
            this.m();
            return;
         }

         if (ctrl && keyCode == 86) {
            this.n();
            return;
         }

         if (keyCode == 259) {
            this.b(hasSelection);
            return;
         }

         if (keyCode == 261) {
            this.c(hasSelection);
            return;
         }

         if (keyCode == 263) {
            this.b(-1);
            return;
         }

         if (keyCode == 262) {
            this.b(1);
         } else if (keyCode == 257 || keyCode == 256) {
            this.a(false);
         }
      }
   }

   public void a(char chr, int modifiers) {
      if (this.i && (!this.b || chr >= '0' && chr <= '9')) {
         this.c(String.valueOf(chr));
      }
   }

   private void c(String string) {
      if (this.g.method_32118() != this.g.method_32119()) {
         this.o();
      }

      int pos = (int)this.g.method_32119();
      this.f.insert(pos, string);
      this.g = new class_5611(pos + string.length(), pos + string.length());
   }

   private void m() {
      int from = (int)Math.min(this.g.method_32118(), this.g.method_32119());
      int to = (int)Math.max(this.g.method_32118(), this.g.method_32119());
      if (from < to) {
         GLFW.glfwSetClipboardString(Interface.aM_.method_22683().method_4490(), this.f.substring(from, to));
      }
   }

   private void n() {
      String clip = GLFW.glfwGetClipboardString(Interface.aM_.method_22683().method_4490());
      if (clip != null && !clip.isEmpty()) {
         this.c(this.b ? clip.replaceAll("[^0-9]", "") : clip);
      }
   }

   private void b(boolean hasSelection) {
      if (hasSelection) {
         this.o();
      } else if (this.g.method_32119() > 0.0F) {
         int pos = (int)this.g.method_32119();
         this.f.deleteCharAt(pos - 1);
         this.g = new class_5611(pos - 1, pos - 1);
      }
   }

   private void c(boolean hasSelection) {
      if (hasSelection) {
         this.o();
      } else if (this.g.method_32119() < this.f.length()) {
         this.f.deleteCharAt((int)this.g.method_32119());
      }
   }

   private void b(int direction) {
      int pos = (int)this.g.method_32119() + direction;
      if (pos >= 0 && pos <= this.f.length()) {
         this.g = new class_5611(pos, pos);
      }
   }

   private void o() {
      int from = (int)Math.min(this.g.method_32118(), this.g.method_32119());
      int to = (int)Math.max(this.g.method_32118(), this.g.method_32119());
      this.f.delete(from, to);
      this.g = new class_5611(from, from);
   }

   public void a() {
      this.f.setLength(0);
      this.g = new class_5611(0.0F, 0.0F);
      this.i = false;
   }

   public void a(boolean status) {
      this.i = status;
      this.g = new class_5611(status ? this.f.length() : 0.0F, status ? this.f.length() : 0.0F);
   }

   public static enum a {
      ALT_MANAGER(Fonts.b, 7.0F, 6.0F, -0.5F, true) {
         @Override
         public void a(Draw2DProcessor draw, class_4587 matrices, float x, float y, float width, float height, float alpha) {
            draw.a(matrices, x, y, width + 2.0F, height, new Vector4f(5.0F, 1.0F, 5.0F, 1.0F), ColorUtil.a(16777215, 0.039215688F * alpha));
         }
      },
      GUI(Fonts.c, 6.5F, 8.0F, 0.0F, true) {
         @Override
         public void a(Draw2DProcessor draw, class_4587 matrices, float x, float y, float width, float height, float alpha) {
            int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
            draw.a(matrices, x, y, width, height, height / 2.0F, ColorUtil.a(16777215, 0.045F * alpha));
            draw.a(matrices, x, y, width, height, height / 2.0F, 0.5F, ColorUtil.a(ColorUtil.a(-1, accent, 0.35F), 0.1F * alpha));
         }
      },
      GUI_SETTING(Fonts.c, 6.5F, 5.0F, 0.0F, true) {
         @Override
         public void a(Draw2DProcessor draw, class_4587 matrices, float x, float y, float width, float height, float alpha) {
            draw.a(matrices, x, y, width, height, 5.0F, ColorUtil.a(16777215, 0.04F * alpha));
            draw.a(matrices, x, y, width, height, 5.0F, 0.5F, ColorUtil.a(16777215, 0.08F * alpha));
         }
      };

      final Font d;
      final float e;
      final float f;
      final float g;
      final boolean h;

      public abstract void a(Draw2DProcessor var1, class_4587 var2, float var3, float var4, float var5, float var6, float var7);

      @Generated
      private a(final Font font, final float fontSize, final float paddingX, final float textOffset, final boolean placeholder) {
         this.d = font;
         this.e = fontSize;
         this.f = paddingX;
         this.g = textOffset;
         this.h = placeholder;
      }

      @Generated
      public Font a() {
         return this.d;
      }

      @Generated
      public float b() {
         return this.e;
      }

      @Generated
      public float c() {
         return this.f;
      }

      @Generated
      public float d() {
         return this.g;
      }

      @Generated
      public boolean e() {
         return this.h;
      }
   }
}

package aethereal.ui.element;

import aethereal.api.Compile;
import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.ColorSetting;
import aethereal.util.MathUtil;
import java.awt.Color;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;

public class ColorElement extends Element_2<ColorSetting> {
   private final Vector4f d = new Vector4f();
   private final Vector4f e = new Vector4f();
   private final Vector4f f = new Vector4f();
   private final Vector4f g = new Vector4f();
   private float h;
   private float i;
   private float j;
   private float k;
   private ColorElement.DragMode l = ColorElement.DragMode.NONE;
   private boolean m;

   @Compile
   @Override
   public boolean a(double mouseX, double mouseY, int button) {
      Vector4f vector4f = this.a;
      Vector4f vector4f2 = this.e;
      Vector4f vector4f3 = this.f;
      Vector4f vector4f4 = this.g;
      if (MathUtil.a(mouseX, mouseY, vector4f.x + vector4f.z - 11.0F, vector4f.y + vector4f.w / 2.0F - 5.0F, 11.0F, 11.0F)) {
         this.m = !this.m;
         return true;
      } else if (!this.m || button != 0) {
         return false;
      } else if (MathUtil.a(mouseX, mouseY, vector4f2.x, vector4f2.y, vector4f2.z, vector4f2.w)) {
         this.l = ColorElement.DragMode.AREA;
         this.a(mouseX, mouseY);
         return true;
      } else if (MathUtil.a(mouseX, mouseY, vector4f3.x, vector4f3.y, vector4f3.z, vector4f3.w)) {
         this.l = ColorElement.DragMode.HUE;
         this.a(mouseX, mouseY);
         return true;
      } else if (!MathUtil.a(mouseX, mouseY, vector4f4.x, vector4f4.y, vector4f4.z, vector4f4.w)) {
         return false;
      } else {
         this.l = ColorElement.DragMode.ALPHA;
         this.a(mouseX, mouseY);
         return true;
      }
   }

   @Compile
   @Override
   public boolean b(double mouseX, double mouseY, int button) {
      this.l = ColorElement.DragMode.NONE;
      return false;
   }

   public ColorElement(ColorSetting setting) {
      super(setting);
      this.a.w = 11.0F;
      this.g();
   }

   @Override
   public void a(class_332 context, double mouseX, double mouseY, float delta, float extend) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      ThemeProcessor theme = Westra.h().d().o();
      float centerY = this.a.y + this.a.w / 2.0F + 0.5F;
      float boxX = this.a.x + this.a.z - 11.0F;
      float boxY = centerY - 5.5F;
      this.e.set(this.a.x + this.a.z + 6.0F + 5.0F, boxY, 56.0F, 56.0F);
      this.f.set(this.e.x + 56.0F + 5.0F, this.e.y, 4.0F, 56.0F);
      this.g.set(this.f.x + 4.0F + 5.0F, this.e.y, 4.0F, 56.0F);
      this.d.set(this.e.x - 5.0F, this.e.y - 5.0F, 84.0F, 66.0F);
      boolean hovered = MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w) && extend >= 1.0F;
      if (extend < 1.0F) {
         this.m = false;
      }

      this.a(matrices, Fonts.c, this.b.i(), this.a.x, this.a.y, this.a.w, 6.5F, theme.a(ThemeInfo.TEXT).a(), boxX - this.a.x - 4.0F, hovered, extend, delta);
      draw.a(matrices, boxX, boxY, 11.0F, 11.0F, 2.0F, ColorUtil.a(theme.a(ThemeInfo.PRIMARY).a(), 0.039215688F * extend));
      draw.a(matrices, boxX, boxY, 11.0F, 11.0F, 2.0F, 0.5F, ColorUtil.a(theme.a(ThemeInfo.OUTLINE_MEDIUM).a(), theme.a(ThemeInfo.OUTLINE_MEDIUM).b() * extend));
      Fonts.a
         .a(
            matrices,
            "J",
            boxX + (11.0F - Fonts.a.b("J", 6.5F)) / 2.0F,
            Fonts.a.a("J", 6.5F, centerY),
            6.5F,
            ColorUtil.a(theme.a(ThemeInfo.PRIMARY).a(), extend)
         );
      draw.a(matrices, boxX + 11.0F - 3.0F - 1.25F, boxY + 11.0F - 3.0F - 1.25F, 3.0F, 3.0F, 0.5F, ColorUtil.a(this.b.c(), extend));
   }

   @Override
   public void a(class_332 context, double mouseX, double mouseY, float delta) {
      this.b().a(this.m);
      this.b().a(0.0F, 1.0F, 0.25F, EasingList.p, delta);
      float anim = EasingList.p.ease(this.b().c());
      if (anim > 0.0F) {
         class_4587 matrices = context.method_51448();
         Draw2DProcessor draw = Westra.h().d().i();
         ThemeProcessor theme = Westra.h().d().o();
         this.a(mouseX, mouseY);
         int hueColor = Color.HSBtoRGB(this.h, 1.0F, 1.0F);
         int rgb = this.b.c() & 16777215;
         int handle = ColorUtil.a(16777215, anim);
         int background = ColorUtil.a(ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_GUI).a(), theme.a(ThemeInfo.PRIMARY).a(), 0.05F), 0.8235294F * anim);
         float scale = 0.85F + 0.15F * EasingList.s.ease(this.b().c());
         float centerX = this.d.x + this.d.z / 2.0F;
         float centerY = this.d.y + this.d.w / 2.0F;
         matrices.method_22903();
         matrices.method_46416(centerX, centerY + (1.0F - anim) * 6.0F, 0.0F);
         matrices.method_22905(scale, scale, 1.0F);
         matrices.method_46416(-centerX, -centerY, 0.0F);
         draw.b(matrices, this.d.x, this.d.y, this.d.z, this.d.w, 4.0F, background, anim);
         draw.a(
            matrices,
            this.d.x,
            this.d.y,
            this.d.z,
            this.d.w,
            4.0F,
            0.5F,
            ColorUtil.a(theme.a(ThemeInfo.OUTLINE_MEDIUM).a(), theme.a(ThemeInfo.OUTLINE_MEDIUM).b() * anim)
         );
         draw.a(
            matrices,
            this.e.x,
            this.e.y,
            this.e.z,
            this.e.w,
            2.0F,
            ColorUtil.a(16777215, anim),
            ColorUtil.a(hueColor, anim),
            ColorUtil.a(0, anim),
            ColorUtil.a(0, anim)
         );
         draw.a(
            matrices,
            this.e.x,
            this.e.y,
            this.e.z,
            this.e.w,
            2.0F,
            0.5F,
            ColorUtil.a(theme.a(ThemeInfo.OUTLINE_SMALL).a(), theme.a(ThemeInfo.OUTLINE_SMALL).b() * anim)
         );
         float cursorX = MathUtil.b(this.e.x + this.i * this.e.z, this.e.x + 2.0F, this.e.x + this.e.z - 2.0F);
         float cursorY = MathUtil.b(this.e.y + (1.0F - this.j) * this.e.w, this.e.y + 2.0F, this.e.y + this.e.w - 2.0F);
         draw.a(matrices, cursorX - 2.0F, cursorY - 2.0F, 4.0F, 4.0F, 1.0F, 0.5F, handle);
         float knob = this.f.z + 2.0F;
         draw.a(
            matrices,
            class_2960.method_60655("westra", "pictures/color.png"),
            this.f.x,
            this.f.y,
            this.f.z,
            this.f.w,
            this.f.z / 4.0F,
            ColorUtil.a(16777215, anim)
         );
         draw.a(context, this.f.x - 1.0F, this.f.y + this.h * this.f.w - 0.5F, knob, 1.0F, handle);
         draw.a(
            matrices,
            class_2960.method_60655("westra", "pictures/opacity.png"),
            this.g.x,
            this.g.y,
            this.g.z,
            this.g.w,
            this.g.z / 4.0F,
            ColorUtil.a(16777215, 0.019607844F * anim)
         );
         draw.a(
            matrices,
            this.g.x,
            this.g.y,
            this.g.z,
            this.g.w,
            this.g.z / 4.0F,
            ColorUtil.a(rgb, anim),
            ColorUtil.a(rgb, anim),
            ColorUtil.a(rgb, 0.0F),
            ColorUtil.a(rgb, 0.0F)
         );
         draw.a(context, this.g.x - 1.0F, this.g.y + (1.0F - this.k) * this.g.w - 0.5F, knob, 1.0F, handle);
         matrices.method_22909();
      }
   }

   private void a(double mouseX, double mouseY) {
      switch (this.l) {
         case NONE:
            return;
         case AREA:
            this.i = MathUtil.b((float)(mouseX - this.e.x) / this.e.z, 0.0F, 1.0F);
            this.j = 1.0F - MathUtil.b((float)(mouseY - this.e.y) / this.e.w, 0.0F, 1.0F);
            break;
         case HUE:
            this.h = MathUtil.b((float)(mouseY - this.f.y) / this.f.w, 0.0F, 1.0F);
            break;
         case ALPHA:
            this.k = 1.0F - MathUtil.b((float)(mouseY - this.g.y) / this.g.w, 0.0F, 1.0F);
      }

      this.b.a(ColorUtil.a(Color.HSBtoRGB(this.h, this.i, this.j), this.k));
   }

   private void g() {
      int color = this.b.c();
      float[] hsb = Color.RGBtoHSB(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, (float[])null);
      this.h = hsb[0];
      this.i = hsb[1];
      this.j = hsb[2];
      this.k = (color >> 24 & 0xFF) / 255.0F;
   }

   static {
      NativeMethodLookup.lookup(ColorElement.class, 10);
   }

   static enum DragMode {
      NONE,
      AREA,
      HUE,
      ALPHA;
   }
}

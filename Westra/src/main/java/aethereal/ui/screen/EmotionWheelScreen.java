package aethereal.ui.screen;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.emotion.Emotion;
import aethereal.emotion.EmotionPlayback;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.render.ScaleUtil;
import aethereal.util.MathUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_4587;

public class EmotionWheelScreen extends class_437 implements Interface {
   private static final float a = 58.0F;
   private static final float b = 30.0F;
   private static final float c = 26.0F;
   private final AnimationUtil d = new AnimationUtil();
   private final List<AnimationUtil> e = new ArrayList<>();
   private int f = -1;

   public EmotionWheelScreen() {
      super(class_2561.method_43470(""));

      for (int index = 0; index < Emotion.values().length; index++) {
         this.e.add(new AnimationUtil());
      }
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      super.method_25394(context, mouseX, mouseY, delta);
      double mx = MathUtil.scale(mouseX, 2);
      double my = MathUtil.scale(mouseY, 2);
      ScaleUtil.a(context, 2);
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      ThemeProcessor theme = Westra.h().d().o();
      int accent = theme.a(ThemeInfo.PRIMARY).a();
      float centerX = aM_.method_22683().method_4486() / 2.0F;
      float centerY = aM_.method_22683().method_4502() / 2.0F;
      this.d.a(0.0F, 1.0F, 0.3F, EasingList.g, delta);
      this.d.a(aM_.field_1755 instanceof EmotionWheelScreen);
      float open = this.d.c();
      Emotion[] values = Emotion.values();
      this.f = this.a(mx, my, centerX, centerY, values.length);

      for (int index = 0; index < values.length; index++) {
         Emotion emotion = values[index];
         AnimationUtil animation = this.e.get(index);
         animation.a(0.0F, 1.0F, 0.25F, EasingList.i, delta);
         animation.a(index == this.f);
         float value = animation.c();
         double angle = a(index, values.length);
         float distance = 58.0F + value * 6.0F;
         float x = centerX + (float)(Math.cos(angle) * distance);
         float y = centerY + (float)(Math.sin(angle) * distance);
         float size = 26.0F + value * 4.0F;
         float half = size / 2.0F;
         int background = ColorUtil.a(ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_GUI).a(), accent, 0.1F + 0.35F * value), 210);
         draw.a(matrices, x - half, y - half, size, size, size / 2.0F, ColorUtil.a(background, open));
         draw.a(matrices, x - half, y - half, size, size, size / 2.0F, 0.6F, ColorUtil.a(accent, (0.25F + 0.65F * value) * open));
         String label = emotion.a();
         float labelSize = 6.5F;
         int color = ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), ColorUtil.a(255, 255, 255, 255), value);
         Fonts.c.a(matrices, label, x - Fonts.c.a(label, labelSize) / 2.0F, Fonts.c.a(label, labelSize, y), labelSize, ColorUtil.a(color, open));
      }

      draw.a(
         matrices,
         centerX - 30.0F,
         centerY - 30.0F,
         60.0F,
         60.0F,
         30.0F,
         ColorUtil.a(ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_GUI).a(), accent, 0.08F), (int)(190.0F * open))
      );
      draw.a(matrices, centerX - 30.0F, centerY - 30.0F, 60.0F, 60.0F, 30.0F, 0.6F, ColorUtil.a(accent, 0.4F * open));
      String center = this.f >= 0 ? values[this.f].a() : "Эмоции";
      Fonts.c
         .a(
            matrices,
            center,
            centerX - Fonts.c.a(center, 8.0F) / 2.0F,
            Fonts.c.a(center, 8.0F, centerY - 3.0F),
            8.0F,
            ColorUtil.a(ColorUtil.a(255, 255, 255, 255), open)
         );
      String hint = this.f >= 0 ? "ЛКМ — включить" : "наведите курсор";
      Fonts.c
         .a(
            matrices,
            hint,
            centerX - Fonts.c.a(hint, 6.0F) / 2.0F,
            Fonts.c.a(hint, 6.0F, centerY + 8.0F),
            6.0F,
            ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), open)
         );
      ScaleUtil.a(context);
   }

   private static double a(int index, int count) {
      return (double)index / count * Math.PI * 2.0 - (Math.PI / 2);
   }

   private int a(double mouseX, double mouseY, float centerX, float centerY, int count) {
      double dx = mouseX - centerX;
      double dy = mouseY - centerY;
      if (Math.hypot(dx, dy) < 30.0) {
         return -1;
      } else {
         double angle = Math.atan2(dy, dx) + (Math.PI / 2);

         while (angle < 0.0) {
            angle += Math.PI * 2;
         }

         double step = (Math.PI * 2) / count;
         return (int)Math.round(angle / step) % count;
      }
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      if (button == 0 && this.f >= 0) {
         Emotion emotion = Emotion.values()[this.f];
         EmotionPlayback.a(emotion);
         Westra.h().d().t().cu().r();
         this.method_25419();
         return true;
      } else {
         return super.method_25402(mouseX, mouseY, button);
      }
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      if (keyCode >= 49 && keyCode <= 57) {
         int index = keyCode - 49;
         if (index < Emotion.values().length) {
            EmotionPlayback.a(Emotion.values()[index]);
            Westra.h().d().t().cu().r();
            this.method_25419();
            return true;
         }
      }

      return super.method_25404(keyCode, scanCode, modifiers);
   }

   public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {
   }

   public boolean method_25421() {
      return false;
   }
}

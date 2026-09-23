package aethereal.ui.widget.kimiko;

import aethereal.config.ThemeInfo;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.ui.element.DragInfo;
import aethereal.ui.widget.Widget;
import net.minecraft.class_304;
import org.lwjgl.glfw.GLFW;

public class KimikoKeyStrokesWidget extends Widget implements Interface {
   private static final float g = 17.0F;
   private static final float h = 2.0F;
   private static final float i = 4.0F;
   private static final float j = 7.0F;
   private static final float k = 6.0F;
   private final BooleanSetting l = new BooleanSetting("Кнопки мыши", true);
   private final BooleanSetting m = new BooleanSetting("Пробел", true);
   private final BooleanSetting n = new BooleanSetting("Счётчик кликов", true);
   private final BooleanSetting o = new BooleanSetting("Клавиша Shift", true);
   private final AnimationUtil[] p = new AnimationUtil[8];
   private final int[] q = new int[2];
   private final long[] r = new long[2];
   private final int[] s = new int[2];
   private final boolean[] t = new boolean[2];

   public KimikoKeyStrokesWidget() {
      super(new DragInfo("Раскладка клавиш", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.a(new Setting[]{this.l, this.m, this.n, this.o});

      for (int index = 0; index < this.p.length; index++) {
         this.p[index] = new AnimationUtil();
      }
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(true);
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float animation = this.a();
      float unit = 17.0F;
      float width = unit * 3.0F + 4.0F;
      float rows = 2.0F;
      if (this.l.c()) {
         rows++;
      }

      if (this.m.c()) {
         rows++;
      }

      float height = unit * rows + 2.0F * (rows - 1.0F);
      float x = this.j().a();
      float y = this.j().b();
      this.j().c(width);
      this.j().d(height);
      if (animation <= 0.0F) {
         super.a(event);
      } else {
         this.q(event);
         float centerX = x + unit + 2.0F;
         this.a(event, centerX, y, unit, unit, 0, this.r(aM_.field_1690.field_1894), "W", animation);
         float rowY = y + (unit + 2.0F);
         this.a(event, x, rowY, unit, unit, 1, this.r(aM_.field_1690.field_1913), "A", animation);
         this.a(event, centerX, rowY, unit, unit, 2, this.r(aM_.field_1690.field_1881), "S", animation);
         this.a(event, x + unit * 2.0F + 4.0F, rowY, unit, unit, 3, this.r(aM_.field_1690.field_1849), "D", animation);
         rowY += unit + 2.0F;
         if (this.l.c()) {
            float half = (width - 2.0F) / 2.0F;
            this.a(event, x, rowY, half, unit, 4, this.t[0], this.s(0), animation);
            this.a(event, x + half + 2.0F, rowY, half, unit, 5, this.t[1], this.s(1), animation);
            rowY += unit + 2.0F;
         }

         if (this.m.c()) {
            boolean sneaking = this.o.c() && this.r(aM_.field_1690.field_1832);
            this.a(event, x, rowY, width, unit, 6, sneaking || this.r(aM_.field_1690.field_1903), sneaking ? "SHIFT" : "SPACE", animation);
         }

         super.a(event);
      }
   }

   private void q(DrawEvent event) {
      long now = System.currentTimeMillis();

      for (int button = 0; button < 2; button++) {
         boolean down = GLFW.glfwGetMouseButton(aM_.method_22683().method_4490(), button) == 1;
         if (down && !this.t[button]) {
            this.q[button]++;
         }

         this.t[button] = down;
         if (now - this.r[button] >= 1000L) {
            this.s[button] = this.q[button];
            this.q[button] = 0;
            this.r[button] = now;
         }
      }
   }

   private String s(int button) {
      String label = button == 0 ? "ЛКМ" : "ПКМ";
      return !this.n.c() ? label : label + " " + this.s[button];
   }

   private boolean r(class_304 binding) {
      return binding != null && binding.method_1434();
   }

   private void a(DrawEvent event, float x, float y, float width, float height, int index, boolean pressed, String label, float animation) {
      AnimationUtil press = this.p[index];
      press.a(0.0F, 1.0F, 0.35F, EasingList.i, event.g());
      press.a(pressed);
      float value = press.c();
      int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
      int background = Westra.h().d().o().a(ThemeInfo.BACKGROUND_HUD).a();
      float sink = value * 1.0F;
      float top = y + sink;
      int fill = ColorUtil.a(background, accent, 0.15F + 0.55F * value);
      event.d().a(event.h(), x, top, width, height - sink, 4.0F, ColorUtil.a(fill, (0.55F + 0.35F * value) * animation));
      event.d().a(event.h(), x, top, width, height - sink, 4.0F, 0.5F, ColorUtil.a(accent, (0.2F + 0.6F * value) * animation));
      float size = width > 17.0F ? 6.0F : 7.0F;
      int text = ColorUtil.a(Westra.h().d().o().a(ThemeInfo.TEXT_DISABLED).a(), ColorUtil.a(255, 255, 255, 255), value);
      Fonts.c
         .a(
            event.h(),
            label,
            x + (width - Fonts.c.a(label, size)) / 2.0F,
            Fonts.c.a(label, size, top + (height - sink) / 2.0F),
            size,
            ColorUtil.a(text, animation)
         );
   }
}

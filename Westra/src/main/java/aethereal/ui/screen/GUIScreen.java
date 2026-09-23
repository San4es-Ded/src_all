package aethereal.ui.screen;

import aethereal.api.Compile;
import aethereal.core.Category;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Fonts;
import aethereal.render.Motion;
import aethereal.render.ScaleUtil;
import aethereal.ui.element.TextField;
import aethereal.ui.recode.RecodeKit;
import aethereal.util.DiscordAvatar;
import aethereal.util.MathUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_5611;
import org.joml.Vector4f;

public class GUIScreen extends class_437 {
   private static final float WIDTH = 470.0F;
   private static final float HEIGHT = 300.0F;
   private static final float INSET = 7.0F;
   private static final float SIDE = 118.0F;
   private static final float ROW = 19.0F;
   private static final float ROW_GAP = 3.0F;
   private static final String[] NAMES = new String[]{"Бой", "Движение", "Визуалы", "Игрок", "Разное"};
   private final TextField a;
   private final AnimationUtil b;
   private final List<GUIPanel> c;
   private String d;
   private int e;
   private final Vector4f g = new Vector4f();
   private final Vector4f i = new Vector4f();
   private final ThemePanel r = new ThemePanel();
   private final Vector4f s = new Vector4f();
   private final List<Vector4f> z = new ArrayList<>();
   private final Motion open = new Motion(420L, Motion.SPRING);
   private final Motion pill = new Motion(320L, Motion.SPRING);
   private final List<Motion> hovers = new ArrayList<>();
   private final Motion cosmeticHover = new Motion(200L, Motion.SMOOTH);
   private boolean closing;
   private float fit = 1.0F;
   private float pivotX;
   private float pivotY;

   public GUIScreen(class_2561 title) {
      super(title);
      this.a = new TextField(TextField.a.GUI);
      this.b = new AnimationUtil();
      this.c = new ArrayList<>();

      for (Category category : Category.values()) {
         this.c.add(new GUIPanel(category));
         this.z.add(new Vector4f());
         this.hovers.add(new Motion(200L, Motion.SMOOTH));
      }

      this.a.a("Поиск функций");
   }

   protected void method_25426() {
      super.method_25426();
      if (this.closing || this.open.value() <= 0.01F) {
         this.closing = false;
         this.open.duration(420L).curve(Motion.SPRING);
         this.open.set(0.0F);
         this.c.get(this.e).k();
      }
   }

   public void method_25419() {
      if (!this.closing) {
         this.closing = true;
         this.open.duration(200L).curve(Motion.IN);
      }
   }

   private void finishClose() {
      super.method_25419();
      this.c.forEach(panel -> panel.b().c(0.0F));
   }

   @Compile
   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      float progress = this.open.to(this.closing ? 0.0F : 1.0F);
      if (this.closing && progress <= 0.001F) {
         this.finishClose();
      } else {
         double rawX = MathUtil.scale(mouseX, 2);
         double rawY = MathUtil.scale(mouseY, 2);
         ScaleUtil.a(context, 2);
         int sw = Interface.aM_.method_22683().method_4486();
         int sh = Interface.aM_.method_22683().method_4502();
         float group = 584.0F;
         float winX = (sw - group) * 0.5F;
         float winY = (sh - 300.0F) * 0.5F;
         this.fit = Math.min(1.0F, Math.min((sw - 16.0F) / group, (sh - 16.0F) / 300.0F));
         this.pivotX = winX + group / 2.0F;
         this.pivotY = winY + 150.0F;
         double mx = this.pivotX + (rawX - this.pivotX) / this.fit;
         double my = this.pivotY + (rawY - this.pivotY) / this.fit;
         this.g.set(winX, winY, 470.0F, 300.0F);
         float contentX = winX + 7.0F + 118.0F + 10.0F;
         this.i.set(contentX, winY + 44.0F, winX + 470.0F - 10.0F - contentX, 248.0F);
         GUIPanel active = this.c.get(this.e);
         active.a(
            Westra.h()
               .d()
               .t()
               .e()
               .stream()
               .filter(module -> this.a(active, module))
               .sorted(Comparator.<Module, Boolean>comparing(module -> module.d().isEmpty()).thenComparing(Module::j, String.CASE_INSENSITIVE_ORDER))
               .toList()
         );
         class_4587 matrices = context.method_51448();
         float alpha = Math.max(0.0F, Math.min(1.0F, progress));
         int shade = RecodeKit.alpha(-16448248, 0.45F * alpha);
         int clear = RecodeKit.alpha(-16448248, 0.15F * alpha);
         RecodeKit.draw().a(matrices, 0.0F, 0.0F, (float)sw, (float)sh, 0.0F, shade, shade, clear, clear);
         matrices.method_22903();
         float scale = (0.9F + 0.1F * progress) * this.fit;
         matrices.method_46416(this.pivotX, this.pivotY + (1.0F - progress) * 12.0F, 0.0F);
         matrices.method_22905(scale, scale, 1.0F);
         matrices.method_46416(-this.pivotX, -this.pivotY, 0.0F);
         RecodeKit.glass(matrices, winX, winY, 470.0F, 300.0F, 13.0F, alpha, 0.93F, 0.1F, 0.18F);
         RecodeKit.shimmer(matrices, winX + 20.0F, winY + 0.5F, 430.0F, 0.75F, RecodeKit.time() * 0.8F, 0.55F * alpha);
         this.sidebar(context, winX, winY, (int)mx, (int)my, delta, alpha);
         this.header(matrices, winY, alpha);
         active.f().set(this.i.x, this.i.y, this.i.z, this.i.w);
         active.d(true);
         active.a(context, (int)mx, (int)my, delta);
         this.r.a(context, winX, winY, 470.0F, 300.0F, (int)mx, (int)my, delta, alpha);
         active.a(context, mx, my, delta);
         active.d(false);
         matrices.method_22909();
         ScaleUtil.a(context);
      }
   }

   private void sidebar(class_332 context, float winX, float winY, int mouseX, int mouseY, float delta, float alpha) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = RecodeKit.draw();
      float x = winX + 7.0F;
      float y = winY + 7.0F;
      float height = 286.0F;
      draw.a(matrices, x, y, 118.0F, height, 9.0F, RecodeKit.alpha(-1, 0.025F * alpha));
      draw.a(matrices, x, y, 118.0F, height, 9.0F, 0.5F, RecodeKit.alpha(-1, 0.06F * alpha));
      RecodeKit.logo(matrices, x + 10.0F, y + 10.0F, 10.5F, alpha);
      Fonts.c.a(matrices, "1.21.4  |  " + Interface.aM_.method_1548().method_1676(), x + 10.5F, y + 23.5F, 5.5F, RecodeKit.alpha(RecodeKit.dim(), alpha));
      this.a.b(new class_5611(104.0F, 16.0F));
      this.a.a(new class_5611(x + 7.0F, y + 34.0F));
      this.a.a(context, mouseX, mouseY, delta, alpha);
      this.label(matrices, "РАЗДЕЛЫ", x + 10.0F, y + 60.0F, alpha);
      float rowX = x + 6.0F;
      float rowW = 106.0F;
      float firstRow = y + 67.0F;
      float activeY = firstRow + this.e * 22.0F;
      if (this.pill.target() == 0.0F && this.pill.value() == 0.0F) {
         this.pill.set(activeY);
      }

      float pillY = this.pill.to(activeY);
      int accent = RecodeKit.accent();
      int left = RecodeKit.alpha(accent, 0.26F * alpha);
      int right = RecodeKit.alpha(RecodeKit.accentShade(), 0.08F * alpha);
      draw.a(matrices, rowX, pillY, rowW, 19.0F, 6.0F, left, right, left, right);
      draw.a(matrices, rowX, pillY, rowW, 19.0F, 6.0F, 0.5F, RecodeKit.alpha(accent, 0.38F * alpha));
      draw.a(matrices, rowX + 2.0F, pillY + 5.5F, 1.5F, 8.0F, 0.75F, RecodeKit.alpha(accent, alpha));
      float rowY = firstRow;

      for (int index = 0; index < this.c.size(); index++) {
         Category category = this.c.get(index).c();
         boolean selected = index == this.e;
         boolean hovered = MathUtil.a(mouseX, mouseY, rowX, rowY, rowW, 19.0F);
         float hover = this.hovers.get(index).to(hovered && !selected);
         this.z.get(index).set(rowX, rowY, rowW, 19.0F);
         if (hover > 0.01F) {
            draw.a(matrices, rowX, rowY, rowW, 19.0F, 6.0F, RecodeKit.alpha(-1, 0.04F * hover * alpha));
         }

         float shift = hover * 1.5F + (selected ? 2.0F : 0.0F);
         int color = selected ? -1 : ColorUtil.a(RecodeKit.dim(), RecodeKit.text(), hover);
         float center = rowY + 9.5F;
         Fonts.a.a(matrices, category.a(), rowX + 9.0F + shift, Fonts.a.a(category.a(), 7.5F, center), 7.5F, RecodeKit.alpha(selected ? accent : color, alpha));
         String name = index < NAMES.length ? NAMES[index] : category.name();
         Fonts.d.a(matrices, name, rowX + 22.0F + shift, Fonts.d.a(name, 7.0F, center), 7.0F, RecodeKit.alpha(color, alpha));
         rowY += 22.0F;
      }

      rowY += 6.0F;
      this.label(matrices, "ПРОЧЕЕ", x + 10.0F, rowY, alpha);
      rowY += 7.0F;
      this.s.set(rowX, rowY, rowW, 19.0F);
      float cosmetic = this.cosmeticHover.to(MathUtil.a(mouseX, mouseY, rowX, rowY, rowW, 19.0F));
      if (cosmetic > 0.01F) {
         draw.a(matrices, rowX, rowY, rowW, 19.0F, 6.0F, RecodeKit.alpha(-1, 0.04F * cosmetic * alpha));
      }

      int cosmeticColor = ColorUtil.a(RecodeKit.dim(), RecodeKit.text(), cosmetic);
      float cosmeticCenter = rowY + 9.5F;
      Fonts.a.a(matrices, "P", rowX + 9.0F + cosmetic * 1.5F, Fonts.a.a("P", 7.5F, cosmeticCenter), 7.5F, RecodeKit.alpha(cosmeticColor, alpha));
      Fonts.d
         .a(matrices, "Косметика", rowX + 22.0F + cosmetic * 1.5F, Fonts.d.a("Косметика", 7.0F, cosmeticCenter), 7.0F, RecodeKit.alpha(cosmeticColor, alpha));
      this.profile(matrices, x, y + height, alpha);
   }

   private void profile(class_4587 matrices, float x, float bottom, float alpha) {
      Draw2DProcessor draw = RecodeKit.draw();
      float cardX = x + 6.0F;
      float cardW = 106.0F;
      float cardH = 30.0F;
      float cardY = bottom - 6.0F - cardH;
      int accent = RecodeKit.accent();
      int top = RecodeKit.alpha(accent, 0.12F * alpha);
      int low = RecodeKit.alpha(-1, 0.02F * alpha);
      draw.a(matrices, cardX, cardY, cardW, cardH, 7.0F, top, low, top, low);
      draw.a(matrices, cardX, cardY, cardW, cardH, 7.0F, 0.5F, RecodeKit.alpha(-1, 0.07F * alpha));
      float avatar = 18.0F;
      float avatarX = cardX + 6.0F;
      float avatarY = cardY + (cardH - avatar) / 2.0F;
      draw.a(matrices, DiscordAvatar.a(), avatarX, avatarY, avatar, avatar, avatar / 2.0F, RecodeKit.alpha(-1, alpha));
      float pulse = (float)((Math.sin(RecodeKit.time() * 3.0) + 1.0) * 0.5);
      draw.a(matrices, avatarX + avatar - 5.0F, avatarY + avatar - 5.0F, 5.5F, 5.5F, 2.75F, RecodeKit.alpha(-15724524, alpha));
      draw.a(matrices, avatarX + avatar - 4.0F, avatarY + avatar - 4.0F, 3.5F, 3.5F, 1.75F, RecodeKit.alpha(ColorUtil.a(-12723068, -7536704, pulse), alpha));
      String nick = Interface.aM_.method_1548().method_1676();
      float textX = avatarX + avatar + 6.0F;
      Fonts.d.c(matrices, nick, textX, cardY + 8.5F, 7.0F, RecodeKit.alpha(RecodeKit.text(), alpha), cardX + cardW - textX - 4.0F);
      Fonts.c.a(matrices, "Recode build", textX, cardY + 17.5F, 5.5F, RecodeKit.alpha(RecodeKit.dim(), alpha));
   }

   private void header(class_4587 matrices, float winY, float alpha) {
      float x = this.i.x + 2.0F;
      float y = winY + 13.0F;
      String query = this.a.g().toString();
      String title = query.isEmpty() ? (this.e < NAMES.length ? NAMES[this.e] : this.c.get(this.e).c().name()) : "Поиск";
      Fonts.d.a(matrices, title, x, y, 11.0F, RecodeKit.alpha(-1, alpha));
      List<Module> modules = this.c.get(this.e).d();
      int count = modules == null ? 0 : modules.size();
      int enabled = modules == null ? 0 : (int)modules.stream().filter(Module::m).count();
      float chipX = x + Fonts.d.a(title, 11.0F) + 6.0F;
      RecodeKit.chip(matrices, count + " функций", chipX, y + 1.0F, 5.5F, 10.0F, false, alpha);
      String sub = enabled > 0 ? "Включено: " + enabled : "Всё выключено";
      Fonts.c.a(matrices, sub, x + 0.5F, y + 15.0F, 5.75F, RecodeKit.alpha(RecodeKit.dim(), alpha));
      String hint = "ЛКМ вкл  |  ПКМ настройки  |  СКМ бинд";
      float right = this.i.x + this.i.z - 2.0F;
      Fonts.c.a(matrices, hint, right - Fonts.c.a(hint, 5.5F), y + 3.0F, 5.5F, RecodeKit.alpha(RecodeKit.dim(), 0.8F * alpha));
      RecodeKit.draw().a(matrices, this.i.x, winY + 39.0F, this.i.z, 0.5F, 0.0F, RecodeKit.alpha(-1, 0.06F * alpha));
   }

   private double px(double mouseX) {
      return this.pivotX + (MathUtil.scale(mouseX, 2) - this.pivotX) / this.fit;
   }

   private double py(double mouseY) {
      return this.pivotY + (MathUtil.scale(mouseY, 2) - this.pivotY) / this.fit;
   }

   private void label(class_4587 matrices, String text, float x, float y, float alpha) {
      Fonts.d.a(matrices, text, x, y, 5.25F, RecodeKit.alpha(RecodeKit.dim(), 0.75F * alpha));
   }

   @Compile
   public boolean method_25402(double mouseX, double mouseY, int button) {
      if (this.closing) {
         return true;
      } else {
         double sx = this.px(mouseX);
         double sy = this.py(mouseY);
         this.a.a(sx, sy, button);

         for (int index = 0; index < this.z.size(); index++) {
            Vector4f row = this.z.get(index);
            if (button == 0 && MathUtil.a(sx, sy, row.x, row.y, row.z, row.w)) {
               if (this.e != index) {
                  this.e = index;
                  this.c.get(index).a().c(0.0F);
                  this.c.get(index).k();
               }

               return true;
            }
         }

         if (button == 0 && MathUtil.a(sx, sy, this.s.x, this.s.y, this.s.z, this.s.w)) {
            Interface.aM_.method_1507(new StationScreen(class_2561.method_43470(""), 2));
            return true;
         } else if (this.r.a(sx, sy, button)) {
            return true;
         } else {
            GUIPanel active = this.c.get(this.e);
            return active.d() != null && active.a(sx, sy, button) ? true : super.method_25402(mouseX, mouseY, button);
         }
      }
   }

   @Compile
   public boolean method_25406(double mouseX, double mouseY, int button) {
      if (this.r.b(this.px(mouseX), this.py(mouseY), button)) {
         return true;
      } else {
         GUIPanel active = this.c.get(this.e);
         return active.d() != null && active.b(this.px(mouseX), this.py(mouseY), button) ? true : super.method_25406(mouseX, mouseY, button);
      }
   }

   @Compile
   public boolean method_25403(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.r.a(this.px(mouseX), this.py(mouseY), button, MathUtil.scale(deltaX, 2) / this.fit, MathUtil.scale(deltaY, 2) / this.fit)) {
         return true;
      } else {
         this.a.b(this.px(mouseX), this.py(mouseY), button);
         GUIPanel active = this.c.get(this.e);
         return active.d() != null
               && active.a(this.px(mouseX), this.py(mouseY), button, MathUtil.scale(deltaX, 2) / this.fit, MathUtil.scale(deltaY, 2) / this.fit)
            ? true
            : super.method_25403(mouseX, mouseY, button, deltaX, deltaY);
      }
   }

   @Compile
   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      double sx = this.px(mouseX);
      double sy = this.py(mouseY);
      if (MathUtil.a(sx, sy, this.i.x, this.i.y, this.i.z, this.i.w)) {
         GUIPanel active = this.c.get(this.e);
         if (active.d() != null) {
            return active.a(sx, sy, verticalAmount);
         }
      }

      return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   @Compile
   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 70 && (modifiers & 2) != 0) {
         this.a.a(!this.a.j());
         return true;
      } else if (this.a.j()) {
         this.a.a(keyCode, scanCode, modifiers);
         return true;
      } else {
         GUIPanel active = this.c.get(this.e);
         return active.d() != null && active.a(keyCode, scanCode, modifiers) ? true : super.method_25404(keyCode, scanCode, modifiers);
      }
   }

   @Compile
   public boolean method_25400(char character, int modifiers) {
      if (this.a.j()) {
         this.a.a(character, modifiers);
         return true;
      } else {
         GUIPanel active = this.c.get(this.e);
         return active.d() != null && active.a(character, modifiers) ? true : super.method_25400(character, modifiers);
      }
   }

   @Generated
   public TextField a() {
      return this.a;
   }

   @Generated
   public AnimationUtil b() {
      return this.b;
   }

   @Generated
   public List<GUIPanel> c() {
      return this.c;
   }

   @Generated
   public String d() {
      return this.d;
   }

   public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {
   }

   public boolean a(GUIPanel panel, Module module) {
      String query = this.a.g().toString().toLowerCase();
      return !query.isEmpty() ? module.j().toLowerCase().contains(query) : module.l() == panel.c();
   }

   public static boolean f(GUIPanel panel) {
      return panel.d() != null;
   }

   public static boolean e(GUIPanel panel) {
      return panel.d() != null;
   }

   public static boolean d(GUIPanel panel) {
      return panel.d() != null;
   }

   public static boolean c(GUIPanel panel) {
      return panel.d() != null;
   }

   public static boolean b(GUIPanel panel) {
      return panel.d() != null;
   }

   public static boolean a(GUIPanel panel) {
      return panel.d() != null;
   }

   static {
      NativeMethodLookup.lookup(GUIScreen.class, 5);
   }
}

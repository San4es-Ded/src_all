package aethereal.ui.screen;

import aethereal.api.Compile;
import aethereal.core.Category;
import aethereal.core.Module;
import aethereal.core.NativeMethodLookup;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.render.Motion;
import aethereal.render.ScissorUtil;
import aethereal.ui.element.Element_2;
import aethereal.ui.recode.RecodeKit;
import aethereal.util.KeyUtil;
import aethereal.util.MathUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import lombok.Generated;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;

public class GUIPanel {
   private static final float GAP = 4.0F;
   private static final float RADIUS = 7.0F;
   private final Vector4f a = new Vector4f(0.0F, 0.0F, 125.0F, 270.0F);
   private final AnimationUtil b = new AnimationUtil();
   private final AnimationUtil c = new AnimationUtil();
   private final Category d;
   private List<Module> e;
   private Module f;
   private boolean g;
   private final Map<Module, Motion> toggles = new WeakHashMap<>();
   private final Map<Module, Motion> hovers = new WeakHashMap<>();
   private long shownAt = System.currentTimeMillis();

   public GUIPanel(Category category) {
      this.d = category;
   }

   public void k() {
      this.shownAt = System.currentTimeMillis();
   }

   @Compile
   public boolean a(double mouseX, double mouseY, int button) {
      for (Module module : this.e) {
         if (module.n()) {
            module.a(button >= 0 && button <= 7 ? -100 + button : -100);
            module.b(false);
            return true;
         }
      }

      if (this.f != null) {
         if (this.f.isLocked()) {
            return true;
         }

         if (button == 0) {
            this.f.a();
            return true;
         }

         if (button == 1) {
            this.f.c(!this.f.o());
            return true;
         }

         if (button == 2) {
            this.e.forEach(this::i);
            return true;
         }
      }

      return this.e
         .stream()
         .filter(Module::o)
         .flatMap(modulex -> modulex.d().stream())
         .filter(Element_2::a)
         .anyMatch(element -> element.a(mouseX, mouseY, button));
   }

   @Compile
   public boolean b(double mouseX, double mouseY, int button) {
      return this.e
         .stream()
         .filter(Module::o)
         .flatMap(module -> module.d().stream())
         .filter(Element_2::a)
         .anyMatch(element -> element.b(mouseX, mouseY, button));
   }

   @Compile
   public boolean a(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      return this.e
         .stream()
         .filter(Module::o)
         .flatMap(module -> module.d().stream())
         .filter(Element_2::a)
         .anyMatch(element -> element.a(mouseX, mouseY, button, deltaX, deltaY));
   }

   @Compile
   public boolean a(int keyCode, int scanCode, int modifiers) {
      for (Module module : this.e) {
         if (module.n()) {
            module.a(keyCode != KeyUtil.ESC.a() && keyCode != KeyUtil.BACKSPACE.a() ? keyCode : -1);
            module.b(false);
            return true;
         }
      }

      return this.e
         .stream()
         .filter(Module::o)
         .flatMap(modulex -> modulex.d().stream())
         .filter(Element_2::a)
         .anyMatch(element -> element.a(keyCode, scanCode, modifiers));
   }

   @Compile
   public boolean a(char chr, int modifiers) {
      return this.e.stream().filter(Module::o).flatMap(module -> module.d().stream()).filter(Element_2::a).anyMatch(element -> element.a(chr, modifiers));
   }

   @Compile
   public boolean a(double mouseX, double mouseY, double amount) {
      if (!MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w)) {
         return false;
      } else if (this.e
         .stream()
         .filter(Module::o)
         .flatMap(module -> module.d().stream())
         .filter(Element_2::a)
         .anyMatch(element -> element.a(mouseX, mouseY, amount))) {
         return true;
      } else {
         this.b.a((float)amount * 18.0F);
         return true;
      }
   }

   @Generated
   public void a(List<Module> modules) {
      this.e = modules;
   }

   @Generated
   public void a(Module hovered) {
      this.f = hovered;
   }

   @Generated
   public Vector4f f() {
      return this.a;
   }

   @Generated
   public AnimationUtil a() {
      return this.b;
   }

   @Generated
   public AnimationUtil b() {
      return this.c;
   }

   @Generated
   public Category c() {
      return this.d;
   }

   @Generated
   public List<Module> d() {
      return this.e;
   }

   @Generated
   public Module e() {
      return this.f;
   }

   @Generated
   public boolean g() {
      return this.g;
   }

   @Generated
   public void d(boolean contentOnly) {
      this.g = contentOnly;
   }

   public void a(class_332 context, int mouseX, int mouseY, float delta) {
      this.list(context, mouseX, mouseY, this.a.y, delta);
   }

   private void list(class_332 context, int mouseX, int mouseY, float top, float delta) {
      class_4587 matrices = context.method_51448();
      float view = this.a.y + this.a.w - top - 2.0F;
      float cardX = this.a.x;
      float cardW = this.a.z;
      float content = 0.0F;

      for (Module module : this.e) {
         content += this.height(module) + 4.0F;
      }

      float scrolled = top + this.b.a(Math.min(0.0F, view - content), 0.0F, 1.0F);
      this.f = null;
      ScissorUtil.a(matrices, this.a.x - 2.0F, top, this.a.z + 4.0F, view);
      float bottom = top + view;
      float rowY = scrolled;
      int index = 0;

      for (Module module : this.e) {
         float appear = Motion.progress(this.shownAt, Math.min(index, 14) * 28L, 340L, Motion.OUT);
         this.card(context, module, cardX, rowY, cardW, mouseX, mouseY, delta, top, bottom, appear);
         rowY += this.height(module) + 4.0F;
         index++;
      }

      ScissorUtil.a(matrices);
      int base = RecodeKit.base();
      int solid = RecodeKit.alpha(base, 0.9F);
      int clear = RecodeKit.alpha(base, 0.0F);
      if (scrolled < top - 1.0F) {
         RecodeKit.draw().a(matrices, this.a.x - 2.0F, top, this.a.z + 4.0F, 8.0F, 0.0F, solid, solid, clear, clear);
      }

      if (scrolled + content > bottom + 1.0F) {
         RecodeKit.draw().a(matrices, this.a.x - 2.0F, bottom - 8.0F, this.a.z + 4.0F, 8.0F, 0.0F, clear, clear, solid, solid);
      }
   }

   private void card(class_332 context, Module module, float x, float y, float w, int mouseX, int mouseY, float delta, float top, float bottom, float appear) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = RecodeKit.draw();
      float head = this.head(module, w);
      float total = this.height(module);
      boolean hover = mouseY >= top && mouseY <= bottom && MathUtil.a(mouseX, mouseY, x, y, w, head);
      if (hover) {
         this.f = module;
      }

      module.h().a(0.0F, 1.0F, 0.5F, EasingList.i, delta);
      module.i().a(0.0F, 1.0F, 0.25F, EasingList.i, delta);
      module.h().a(module.o());
      module.i().a(module == this.f);
      if (!(y + total <= top) && !(y >= bottom) && !(appear <= 0.0F)) {
         float lift = (1.0F - appear) * 10.0F;
         matrices.method_22903();
         matrices.method_46416(0.0F, lift, 0.0F);
         float hovered = this.hovers.computeIfAbsent(module, key -> new Motion(220L, Motion.SMOOTH)).to(module == this.f);
         float enabled = this.toggles.computeIfAbsent(module, key -> new Motion(380L, module.m() ? 1.0F : 0.0F, Motion.SPRING)).to(module.m());
         float glowOn = Math.max(0.0F, Math.min(1.0F, enabled));
         int accent = RecodeKit.accent();
         float right = x + w;
         float center = y + 10.75F;
         if (module.isLocked()) {
            draw.a(matrices, x, y, w, total, 7.0F, RecodeKit.alpha(-1, 0.02F * appear));
            draw.a(matrices, x, y, w, total, 7.0F, 0.5F, RecodeKit.alpha(-1, 0.05F * appear));
            Fonts.d.a(matrices, module.j(), x + 9.0F, y + 7.0F, 7.5F, RecodeKit.alpha(RecodeKit.dim(), appear));
            RecodeKit.chip(matrices, "скоро", right - 9.0F - Fonts.d.a("скоро", 5.5F) - 9.0F, center - 5.0F, 5.5F, 10.0F, false, appear);
            this.description(matrices, module, x, y, w, 0.6F * appear);
            matrices.method_22909();
         } else {
            draw.a(matrices, x, y, w, total, 7.0F, RecodeKit.alpha(-1, (0.028F + 0.03F * hovered) * appear));
            if (glowOn > 0.01F) {
               int wash = RecodeKit.alpha(accent, 0.17F * glowOn * appear);
               int clear = RecodeKit.alpha(accent, 0.0F);
               draw.a(matrices, x, y, w * 0.75F, total, 7.0F, wash, clear, wash, clear);
            }

            draw.a(
               matrices,
               x,
               y,
               w,
               total,
               7.0F,
               0.5F,
               RecodeKit.alpha(ColorUtil.a(-1, accent, 0.6F * glowOn), (0.06F + 0.05F * hovered + 0.12F * glowOn) * appear)
            );
            if (glowOn > 0.01F) {
               float barHeight = (head - 12.0F) * glowOn;
               float barY = y + head / 2.0F - barHeight / 2.0F;
               draw.a(matrices, x + 2.0F, barY - 1.0F, 3.5F, barHeight + 2.0F, 1.75F, RecodeKit.alpha(accent, 0.25F * glowOn * appear));
               draw.a(matrices, x + 2.75F, barY, 2.0F, barHeight, 1.0F, RecodeKit.alpha(accent, glowOn * appear));
            }

            float nameX = x + 9.0F + hovered * 1.5F;
            int nameColor = ColorUtil.a(ColorUtil.a(RecodeKit.text(), -1, 0.5F), -1, glowOn);
            Fonts.d.a(matrices, module.j(), nameX, y + 7.0F, 7.5F, RecodeKit.alpha(nameColor, appear));
            if (module.g().c() > 0.0F) {
               this.bind(matrices, module, nameX + Fonts.d.a(module.j(), 7.5F) + 4.0F, center, appear * module.g().c());
            }

            this.description(matrices, module, nameX - 9.0F, y, w, 0.85F * appear);
            float toggleW = 17.0F;
            float toggleH = 9.5F;
            float toggleX = right - 9.0F - toggleW;
            RecodeKit.toggle(matrices, toggleX, center - toggleH / 2.0F, toggleW, toggleH, glowOn, appear);
            if (module.d().stream().anyMatch(Element_2::a)) {
               this.dots(matrices, toggleX - 6.0F, center, module.h().c(), hovered, appear);
            }

            float extend = module.h().c();
            if (extend > 0.0F) {
               this.settings(context, module, x, y, w, head, total, mouseX, mouseY, delta, extend, appear);
            }

            matrices.method_22909();
         }
      }
   }

   private void settings(
      class_332 context, Module module, float x, float y, float w, float head, float total, int mouseX, int mouseY, float delta, float extend, float fade
   ) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = RecodeKit.draw();
      ScissorUtil.a(matrices, x, y, w, total);
      draw.a(matrices, x + 8.0F, y + head - 1.0F, w - 16.0F, 0.5F, 0.0F, RecodeKit.alpha(-1, 0.07F * extend * fade));
      float baseY = y + head + 3.0F - 4.0F * (1.0F - extend);
      float offset = 0.0F;
      if (module.d().isEmpty()) {
         String empty = "Настроек нет";
         Fonts.c.a(matrices, empty, x + w / 2.0F - Fonts.c.a(empty, 6.5F) / 2.0F, baseY + 1.0F, 6.5F, RecodeKit.alpha(RecodeKit.dim(), fade * extend));
      } else {
         for (Element_2<?> element : module.d()) {
            element.c().a(element.a());
            element.c().a(0.0F, 1.0F, 0.4F, EasingList.i, delta);
            float visible = element.c().c();
            if (!(visible <= 0.0F)) {
               float targetY = baseY + offset - 4.0F * (1.0F - visible);
               float currentY = baseY + (targetY - baseY) * extend;
               element.d().set(x + 10.0F, currentY, w - 20.0F, element.d().w());
               element.a(context, (double)mouseX, (double)mouseY, delta, extend * visible * fade);
               offset += (element.d().w() + 4.0F) * visible;
            }
         }
      }

      ScissorUtil.a(matrices);
   }

   private void bind(class_4587 matrices, Module module, float x, float center, float alpha) {
      String text = module.n() ? "..." : KeyUtil.b(module.p());
      float height = 9.5F;
      float width = Fonts.d.a(text, 5.75F) + 9.0F;
      Draw2DProcessor draw = RecodeKit.draw();
      draw.a(matrices, x, center - height / 2.0F, width, height, height / 2.0F, RecodeKit.alpha(RecodeKit.accent(), 0.18F * alpha));
      draw.a(matrices, x, center - height / 2.0F, width, height, height / 2.0F, 0.5F, RecodeKit.alpha(RecodeKit.accent(), 0.5F * alpha));
      Fonts.d.b(matrices, text, x + width / 2.0F, Fonts.d.a(text, 5.75F, center), 5.75F, RecodeKit.alpha(-1, alpha));
   }

   private void dots(class_4587 matrices, float right, float center, float open, float hovered, float alpha) {
      Draw2DProcessor draw = RecodeKit.draw();
      int color = ColorUtil.a(RecodeKit.dim(), RecodeKit.accent(), Math.max(open, hovered * 0.5F));
      float size = 1.6F;
      float spacing = 3.0F * (1.0F - open) + 1.1F * open;

      for (int index = 0; index < 3; index++) {
         float dotX = right - size - index * spacing;
         draw.a(matrices, dotX, center - size / 2.0F, size, size, size / 2.0F, RecodeKit.alpha(color, alpha));
      }
   }

   private void description(class_4587 matrices, Module module, float x, float y, float w, float alpha) {
      float lineY = y + 18.5F;

      for (String line : this.k(module.k(), w - 18.0F - 32.0F)) {
         Fonts.c.a(matrices, line, x + 9.0F, lineY, 5.9F, RecodeKit.alpha(RecodeKit.dim(), alpha));
         lineY += 7.0F;
      }
   }

   private List<String> k(String text, float width) {
      List<String> lines = new ArrayList<>();
      if (text != null && !text.isEmpty() && !(width <= 10.0F)) {
         StringBuilder current = new StringBuilder();

         for (String word : text.split(" ")) {
            String candidate = current.length() == 0 ? word : current + " " + word;
            if (Fonts.c.a(candidate, 5.9F) <= width) {
               current.setLength(0);
               current.append(candidate);
            } else {
               if (current.length() > 0) {
                  lines.add(current.toString());
                  current.setLength(0);
               }

               current.append(word);
               if (lines.size() == 2) {
                  break;
               }
            }
         }

         if (lines.size() < 2 && current.length() > 0) {
            lines.add(current.toString());
         }

         while (lines.size() > 2) {
            lines.remove(lines.size() - 1);
         }

         if (lines.size() == 2 && Fonts.c.a(text, 5.9F) > width * 2.0F) {
            String last = lines.get(1);

            while (last.length() > 1 && Fonts.c.a(last + "...", 5.9F) > width) {
               last = last.substring(0, last.length() - 1);
            }

            lines.set(1, last + "...");
         }

         return lines;
      } else {
         return lines;
      }
   }

   private float head(Module module, float width) {
      int lines = this.k(module.k(), width - 18.0F - 32.0F).size();
      return lines == 0 ? 22.0F : 18.5F + lines * 7.0F + 3.5F;
   }

   public void a(class_332 context, double mouseX, double mouseY, float delta) {
      for (Module module : this.e) {
         for (Element_2<?> element : module.d()) {
            element.a(context, mouseX, mouseY, delta);
         }
      }
   }

   private float height(Module module) {
      float head = this.head(module, this.a.z);
      return module.d().isEmpty()
         ? head + 14.0F * module.h().c()
         : head + 3.0F + (float)module.d().stream().mapToDouble(element -> (element.d().w() + 4.0F) * element.c().c()).sum() * module.h().c();
   }

   public void i(Module module) {
      module.b(module == this.f && !module.n());
   }

   static {
      NativeMethodLookup.lookup(GUIPanel.class, 4);
   }
}

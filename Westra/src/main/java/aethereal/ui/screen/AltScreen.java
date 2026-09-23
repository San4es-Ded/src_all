package aethereal.ui.screen;

import aethereal.api.Compile;
import aethereal.config.ThemeInfo;
import aethereal.core.Interface;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Processor_2;
import aethereal.core.Westra;
import aethereal.network.AccountConstructor;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.render.ScaleUtil;
import aethereal.render.ScissorUtil;
import aethereal.ui.element.TextField;
import aethereal.ui.shader.BlurShader;
import aethereal.ui.shader.GradientUtil;
import aethereal.util.MathUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.class_1041;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_5611;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class AltScreen extends class_437 {
   private final AnimationUtil a = new AnimationUtil();
   private final AnimationUtil b = new AnimationUtil();
   private final TextField c = new TextField(TextField.a.ALT_MANAGER);
   private final List<AltScreen.a> d = new ArrayList<>();
   AltScreen.a e;
   private AltScreen.a f;
   private AccountConstructor g;
   private float h;
   private boolean i;

   @Compile
   public void method_25394(class_332 context, int mx, int my, float delta) {
      super.method_25394(context, mx, my, delta);
      AnimationUtil animationUtil = this.a;
      class_310 class_310Var = Interface.aM_;
      if (class_310Var != null) {
         class_437 class_437Var = class_310Var.field_1755;
         if (animationUtil != null) {
            animationUtil.a(class_437Var instanceof AltScreen);
            AnimationUtil animationUtil2 = this.a;
            if (animationUtil2 != null) {
               animationUtil2.a(0.0F, 1.0F, 0.15F, EasingList.g, delta);
               AnimationUtil animationUtil3 = this.a;
               if (animationUtil3 != null) {
                  float fMin = Math.min(1.0F, animationUtil3.c() / 0.9F);
                  double dA = MathUtil.scale(mx, 2);
                  double dA2 = MathUtil.scale(my, 2);
                  ScaleUtil.a(context, 2);
                  class_310 class_310Var2 = Interface.aM_;
                  class_1041 class_1041VarMethod_22683;
                  if (class_310Var2 != null && (class_1041VarMethod_22683 = class_310Var2.method_22683()) != null) {
                     int iMethod_4486 = class_1041VarMethod_22683.method_4486();
                     class_310 class_310Var3 = Interface.aM_;
                     class_1041 class_1041VarMethod_22684;
                     if (class_310Var3 != null && (class_1041VarMethod_22684 = class_310Var3.method_22683()) != null) {
                        int iMethod_4502 = class_1041VarMethod_22684.method_4502();
                        EasingList.a aVar = EasingList.s;
                        if (aVar != null) {
                           MainScreen.a(context, iMethod_4486, iMethod_4502, (int)dA, (int)dA2, aVar.ease(fMin) * 0.2F + 1.05F);
                           Westra westra = Westra.h();
                           Processor_2 processor_2D;
                           Draw2DProcessor draw2DProcessorI;
                           BlurShader blurShaderE;
                           if (westra != null
                              && (processor_2D = westra.d()) != null
                              && (draw2DProcessorI = processor_2D.i()) != null
                              && (blurShaderE = draw2DProcessorI.e()) != null
                              && context != null) {
                              blurShaderE.a(context.method_51448());
                              EasingList.a aVar2 = EasingList.s;
                              if (aVar2 != null) {
                                 float fEase = aVar2.ease(fMin);
                                 class_4587 class_4587VarMethod_51448;
                                 if (context != null && (class_4587VarMethod_51448 = context.method_51448()) != null) {
                                    class_4587VarMethod_51448.method_22903();
                                    class_4587VarMethod_51448.method_46416(iMethod_4486 * 0.5F, iMethod_4502 * 0.5F, 0.0F);
                                    float f = fEase * 0.15F + 0.85F;
                                    class_4587VarMethod_51448.method_22905(f, f, 1.0F);
                                    class_4587VarMethod_51448.method_46416(-iMethod_4486 * 0.5F, -iMethod_4502 * 0.5F, 0.0F);
                                    float f2 = (iMethod_4486 - 190.0F) * 0.5F;
                                    float f3 = (iMethod_4502 - 250.0F) * 0.5F;
                                    this.a(class_4587VarMethod_51448, iMethod_4486, f2, f3, fMin);
                                    this.a(class_4587VarMethod_51448, iMethod_4486, iMethod_4502, f, f2, f3, (int)dA, (int)dA2, fMin);
                                    this.a(context, f2, f3, (int)dA, (int)dA2, delta, fMin);
                                    class_4587VarMethod_51448.method_22909();
                                    ScaleUtil.a(context);
                                    return;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      throw new NullPointerException();
   }

   @Compile
   public boolean method_25402(double rawX, double rawY, int button) {
      double dA = MathUtil.scale(rawX, 2);
      double dA2 = MathUtil.scale(rawY, 2);
      this.c.a(dA, dA2, button);
      float fMethod_32118 = this.c.d().method_32118();
      float fMethod_32119 = this.c.d().method_32119();
      float fMethod_321110 = this.c.e().method_32118();
      float f = fMethod_32118 + fMethod_321110;
      if (MathUtil.a(dA, dA2, 5.0F + f, fMethod_32119, 18.0F, 18.0F)) {
         this.a(this.c.g().toString());
         return true;
      } else if (MathUtil.a(dA, dA2, 28.0F + f, fMethod_32119, 150.0F - fMethod_321110, 18.0F)) {
         this.a(this.b());
         return true;
      } else if (MathUtil.a(dA, dA2, fMethod_32118 + 7.0F, fMethod_32119 + 33.0F, 160.0F, 20.0F)) {
         this.d.forEach(new Consumer() {
            @Override
            public void accept(Object obj) {
               ((AltScreen.a)obj).g = true;
            }
         });
         this.a().clear();
         return true;
      } else if (this.e == null) {
         return super.method_25402(rawX, rawY, button);
      } else if (button == 1) {
         this.a(this.e);
         return true;
      } else {
         this.f = this.e;
         AltScreen.a aVar = this.e;
         if (aVar == null) {
            NullPointerException nullPointerException = new NullPointerException();
            this.h = (float)dA2;
            throw nullPointerException;
         } else {
            this.h = (float)dA2 - aVar.e;
            this.i = false;
            return true;
         }
      }
   }

   @Compile
   public boolean method_25403(double mx, double my, int button, double dx, double dy) {
      this.c.b(MathUtil.scale(mx, 2), MathUtil.scale(my, 2), button);
      if (this.f == null) {
         return super.method_25403(mx, my, button, dx, dy);
      } else {
         double dA = MathUtil.scale(my, 2) - this.h;
         AltScreen.a aVar = this.f;
         if (aVar == null) {
            return true;
         } else if (Math.abs(dA - aVar.e) <= 8.0) {
            return true;
         } else {
            this.i = true;
            return true;
         }
      }
   }

   @Compile
   public boolean method_25406(double rawX, double rawY, int button) {
      if (this.f == null) {
         return super.method_25406(rawX, rawY, button);
      } else if (!this.i) {
         AltScreen.a aVar = this.f;
         if (aVar != null) {
            if (aVar.f) {
               AltScreen.a aVar2 = this.f;
               if (aVar2 != null) {
                  AccountConstructor accountConstructor2 = aVar2.b;
                  AltScreen.a aVar3 = this.f;
                  if (aVar3 != null) {
                     AccountConstructor accountConstructor = aVar3.b;
                     if (aVar3.b != null) {
                        boolean z = !accountConstructor.d();
                        if (accountConstructor2 != null) {
                           accountConstructor2.b(z);
                        }
                     }
                  }
               }
            } else {
               AltScreen.a aVar4 = this.f;
               if (aVar4 != null) {
                  this.a(aVar4.b);
               }
            }
         }

         return true;
      } else {
         this.c();
         this.f = null;
         return true;
      }
   }

   @Compile
   public boolean method_25401(double mx, double my, double dx, double dy) {
      this.b.a((float)dy * 29.0F);
      return true;
   }

   @Compile
   public boolean method_25400(char chr, int modifiers) {
      if (!this.c.j()) {
         return super.method_25400(chr, modifiers);
      } else {
         this.c.a(chr, modifiers);
         return true;
      }
   }

   @Compile
   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      TextField textField = this.c;
      if ((modifiers & 2) != 0 && keyCode == 86) {
         this.d();
         return true;
      } else if (!textField.j()) {
         if (keyCode != 256) {
            return super.method_25404(keyCode, scanCode, modifiers);
         } else {
            Interface.aM_.method_1507(new MainScreen());
            return true;
         }
      } else if (keyCode == 257) {
         this.a(textField.g().toString());
         return true;
      } else {
         textField.a(keyCode, scanCode, modifiers);
         return true;
      }
   }

   public AltScreen() {
      super(class_2561.method_43473());
      this.c.a("Никнейм");
      this.a().forEach(account -> this.d.add(new AltScreen.a(account)));
   }

   public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {
   }

   private void a(class_4587 matrices, int w, float px, float py, float open) {
      Draw2DProcessor draw = Westra.h().d().i();
      AccountConstructor selected = Westra.h().d().h().a();
      Fonts.e
         .a(
            matrices,
            GradientUtil.a("Менеджер Аккаунтов", this.a(open), 5.0F, 0.5F),
            (w - Fonts.e.a("Менеджер Аккаунтов", 11.0F)) / 2.0F,
            py - 28.0F,
            11.0F,
            0.0F,
            open
         );
      String info = (selected != null ? selected.b() : "Не выбран") + "  |  " + this.a().size() + " аккаунтов";
      Fonts.b.a(matrices, info, (w - Fonts.b.a(info, 7.0F)) / 2.0F, py - 13.5F, 7.0F, ColorUtil.a(255, 255, 255, (int)(255.0F * open)));
      draw.b(matrices, px, py, 190.0F, 250.0F, 8.0F, ColorUtil.a(11, 11, 13, 120), open);
      draw.a(matrices, px, py, 190.0F, 250.0F, 8.0F, 0.5F, ColorUtil.a(255, 255, 255, (int)(15.0F * open)));
   }

   private void a(class_4587 matrices, int w, int h, float scale, float px, float py, int mx, int my, float open) {
      Draw2DProcessor draw = Westra.h().d().i();
      AccountConstructor selected = Westra.h().d().h().a();
      int accent = this.a(open);
      float listTop = py + 10.0F;
      float listBottom = py + 218.0F;
      float listHeight = listBottom - listTop;
      float overflow = Math.min(0.0F, listHeight - this.d.size() * 29.0F);
      float offset = this.b.a(overflow, 0.0F, 0.5F);
      boolean drag = this.f != null && this.i;
      float baseY = listTop + offset;
      float dragSlot = my - this.h - baseY;
      List<AltScreen.a> visual = this.d.stream().sorted(Comparator.comparing(a2 -> !a2.b.d())).collect(Collectors.toCollection(ArrayList::new));
      if (drag) {
         this.a(visual, dragSlot);
      }

      this.e = null;
      float selectedSlot = -1.0F;
      int index = 0;
      ScissorUtil.a(matrices, w / 2.0F + (px - w / 2.0F) * scale, h / 2.0F + (listTop - 2.0F - h / 2.0F) * scale, 190.0F * scale, (listHeight + 1.0F) * scale);

      for (AltScreen.a account : visual) {
         float targetSlot = account.g ? account.d : index * 29.0F;
         if (account != this.f || !drag) {
            account.a(matrices, draw, accent, px + 1.0F, baseY, targetSlot, listTop, listBottom, mx, my, open);
         }

         if (account.b == selected) {
            selectedSlot = targetSlot;
         }

         if (!account.g) {
            index++;
         }
      }

      if (drag) {
         this.f.d = dragSlot;
         this.f.a(matrices, draw, accent, px + 1.0F, baseY, dragSlot, listTop, listBottom, mx, my, open);
      }

      ScissorUtil.a(matrices);
      this.d.removeIf(v0 -> v0.a());
      if (selected != this.g) {
         this.g = selected;
         if (selectedSlot >= 0.0F) {
            float top = selectedSlot + offset;
            float desired = top < 0.0F ? -selectedSlot : (top + 29.0F > listHeight ? listHeight - 29.0F - selectedSlot : offset);
            this.b.a(MathUtil.b(desired, overflow, 0.0F) - offset);
         }
      }

      float content = Math.max(listHeight, this.d.size() * 29.0F);
      float thumb = listHeight * listHeight / content;
      draw.a(matrices, px + 182.5F, listTop, 1.5F, listHeight, 0.75F, ColorUtil.a(255, 255, 255, (int)(20.0F * open)));
      draw.a(matrices, px + 182.5F, listTop - offset / Math.max(1.0F, content - listHeight) * (listHeight - thumb), 1.5F, thumb, 0.75F, accent);
   }

   private void a(class_332 context, float px, float py, int mx, int my, float delta, float open) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      this.a(open);
      int white = ColorUtil.a(222, 222, 222, (int)(222.0F * open));
      float fieldY = py + 224.0F;
      float randomWidth = Fonts.a.a("H", 8.0F) + 3.0F + Fonts.d.a("Случайный", 7.0F) + 14.0F;
      float fieldWidth = 146.0F - randomWidth;
      float right = px + 11.0F + fieldWidth;
      this.c.a(new class_5611(px + 7.0F, fieldY));
      this.c.b(new class_5611(fieldWidth, 18.0F));
      this.c.a(context, mx, my, delta, open);
      this.a(
         matrices,
         draw,
         right + 0.5F,
         fieldY,
         18.0F,
         18.0F,
         new Vector4f(1.0F, 5.0F, 1.0F, 5.0F),
         null,
         "m",
         ColorUtil.a(255, 255, 255, 10),
         white,
         open,
         mx,
         my
      );
      this.a(
         matrices,
         draw,
         right + 27.0F,
         fieldY,
         randomWidth,
         18.0F,
         new Vector4f(6.0F, 6.0F, 6.0F, 6.0F),
         "Случайный",
         "",
         ColorUtil.a(255, 255, 255, 10),
         white,
         open,
         mx,
         my
      );
      this.a(
         matrices,
         draw,
         px + 15.0F,
         py + 257.0F,
         160.0F,
         20.0F,
         new Vector4f(6.0F, 6.0F, 6.0F, 6.0F),
         "Удалить все аккаунты",
         null,
         ColorUtil.a(220, 80, 80, 20),
         ColorUtil.a(220, 80, 80, (int)(255.0F * open)),
         open,
         mx,
         my
      );
   }

   private int a(float open) {
      int rgba = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
      return rgba & 16777215 | (int)((rgba >>> 24 & 0xFF) * open) << 24;
   }

   private void a(
      class_4587 matrices,
      Draw2DProcessor draw,
      float x,
      float y,
      float width,
      float height,
      Vector4f radius,
      String text,
      String icon,
      int background,
      int content,
      float open,
      int mx,
      int my
   ) {
      boolean hover = MathUtil.a(mx, my, x, y, width, height);
      draw.a(matrices, x, y, width, height, radius, background);
      draw.a(matrices, x, y, width, height, radius, 0.25F, ColorUtil.a(255, 255, 255, (int)((hover ? 25 : 12) * open)));
      float fA;
      if (icon != null) {
         fA = Fonts.a.a(icon, 8.0F) + (text != null ? 3.0F : 0.0F);
      } else {
         fA = 0.0F;
      }

      float startX = x + (width - fA - (text != null ? Fonts.d.a(text, 7.0F) : 0.0F)) / 2.0F;
      if (icon != null) {
         Fonts.a.a(matrices, icon, startX, y + (height - 10.0F) / 2.0F, 10.0F, content);
      }

      if (text != null) {
         Fonts.d.a(matrices, text, startX + 2.0F, y + (height - 8.5F) / 2.0F, 7.0F, content);
      }
   }

   private List<AccountConstructor> a() {
      return Westra.h().d().h().e();
   }

   private void a(AccountConstructor account) {
      this.a().forEach(other -> other.a(other == account));
   }

   private String b() {
      StringBuilder name = new StringBuilder();
      int syllables = 2 + (int)(Math.random() * 3.0);

      for (int i = 0; i < syllables; i++) {
         char c = "bcdfghjklmnpqrstvwz".charAt((int)(Math.random() * "bcdfghjklmnpqrstvwz".length()));
         name.append(c);
         if (Math.random() < 0.11999994994142604) {
            name.append(c);
         }

         char v = "aeiouy".charAt((int)(Math.random() * "aeiouy".length()));
         name.append(v);
         if (Math.random() < 0.1000000000145568) {
            name.append(v);
         }
      }

      if (Math.random() < 0.30000018030598535) {
         name.setCharAt(0, Character.toUpperCase(name.charAt(0)));
      }

      if (Math.random() < 0.15000000097794938) {
         name.append('_');
      }

      if (Math.random() < 0.25) {
         int digits = 1 + (int)(Math.random() * 3.0);

         for (int i2 = 0; i2 < digits; i2++) {
            name.append((char)(48 + (int)(Math.random() * 10.0)));
         }
      }

      if (name.length() < 5) {
         return this.b();
      } else {
         return name.length() > 16 ? name.substring(0, 16) : name.toString();
      }
   }

   private void a(String raw) {
      String name = raw.trim();
      if (!name.isEmpty() && !this.a().stream().anyMatch(other -> other.b().equalsIgnoreCase(name))) {
         AccountConstructor account = new AccountConstructor(name);
         this.a(account);
         this.a().add(account);
         this.d.add(new AltScreen.a(account));
         this.c.a();
      }
   }

   private void a(AltScreen.a account) {
      account.g = true;
      boolean wasSelected = account.b.c();
      this.a().remove(account.b);
      if (wasSelected) {
         this.a(this.a().stream().findFirst().orElse(null));
      }
   }

   private void a(List<AltScreen.a> visual, float draggedY) {
      int from = visual.indexOf(this.f);
      int favorites = (int)visual.stream().filter(account -> account.b.d()).count();
      int lo = this.f.b.d() ? 0 : favorites;
      int hi = this.f.b.d() ? favorites - 1 : visual.size() - 1;
      int to = Math.max(lo, Math.min(hi, Math.round(draggedY / 29.0F)));
      if (from >= 0 && from != to) {
         visual.remove(from);
         visual.add(to, this.f);
         this.d.clear();
         this.d.addAll(visual);
      }
   }

   private void c() {
      List<AccountConstructor> list = this.a();
      Stream<AccountConstructor> map = this.d.stream().map(account -> account.b);
      List<AccountConstructor> ordered = map.filter(v1 -> list.contains(v1)).toList();
      list.clear();
      list.addAll(ordered);
   }

   private void d() {
      String clip = GLFW.glfwGetClipboardString(Interface.aM_.method_22683().method_4490());
      if (clip != null) {
         String name = clip.replaceAll("[^a-zA-Z0-9_]", "");
         this.a(name.substring(0, Math.min(16, name.length())));
      }
   }

   public boolean method_25422() {
      return false;
   }

   static {
      NativeMethodLookup.lookup(AltScreen.class, 15);
   }

   class a {
      final AccountConstructor b;
      private final float[] c = new float[4];
      float d = Float.NaN;
      float e;
      boolean f;
      boolean g;

      a(AccountConstructor data) {
         this.b = data;
      }

      private boolean a() {
         return this.g && this.c[3] < 0.01F;
      }

      void a(
         class_4587 matrices,
         Draw2DProcessor draw,
         int accent,
         float px,
         float baseY,
         float targetSlot,
         float listTop,
         float listBottom,
         int mx,
         int my,
         float open
      ) {
         this.d = Float.isNaN(this.d) ? targetSlot : MathUtil.c(this.d, targetSlot, 1.4F);
         this.e = baseY + this.d;
         boolean over = !this.g && my > listTop && my < listBottom && MathUtil.a(mx, my, px + 7.0F, this.e, 168.0F, 25.0F);
         this.f = over && mx > px + 156.0F;
         if (over) {
            AltScreen.this.e = this;
         }

         float[] target = new float[]{over ? 1.0F : 0.0F, this.b.c() ? 1.0F : 0.0F, this.b.d() ? 1.0F : 0.0F, this.g ? 0.0F : 1.0F};

         for (int i = 0; i < 4; i++) {
            this.c[i] = MathUtil.c(this.c[i], target[i], 1.5F);
         }

         float hover = this.c[0];
         float select = this.c[1];
         float fav = this.c[2];
         float a = open * this.c[3];
         if (!(this.e + 25.0F < listTop - 2.0F) && !(this.e > listBottom + 2.0F)) {
            if (select > 0.01F) {
               draw.a(matrices, px + 7.0F, this.e, 168.0F, 25.0F, 6.0F, ColorUtil.a(accent, 0.1F * select * a));
            } else if (hover > 0.01F) {
               draw.a(matrices, px + 7.0F, this.e, 168.0F, 25.0F, 6.0F, ColorUtil.a(255, 255, 255, (int)(6.0F * hover * a)));
            }

            draw.a(
               matrices,
               px + 7.0F,
               this.e,
               168.0F,
               25.0F,
               6.0F,
               0.5F,
               ColorUtil.a(ColorUtil.a(255, 255, 255, (int)(8.0F * a)), ColorUtil.a(255, 205, 60, (int)(30.0F * a)), fav)
            );
            draw.a(matrices, this.b.a(), null, px + 11.5F, this.e + 4.0F, 16.5F, 16.5F, 3.0F, a);
            Fonts.d.a(matrices, this.b.b(), px + 34.0F, this.e + 7.5F, 8.0F, ColorUtil.a(255, 255, 255, (int)(255.0F * a)));
            if (fav > 0.01F || hover > 0.01F) {
               Fonts.a
                  .a(
                     matrices,
                     "\\",
                     px + 159.0F,
                     this.e + 8.0F,
                     9.0F,
                     ColorUtil.a(ColorUtil.a(255, 255, 255, (int)((this.f ? 130 : 45) * hover * a)), ColorUtil.a(255, 205, 60, (int)(255.0F * a)), fav)
                  );
            }
         }
      }
   }
}

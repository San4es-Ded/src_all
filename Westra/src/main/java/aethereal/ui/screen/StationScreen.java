package aethereal.ui.screen;

import aethereal.api.Compile;
import aethereal.autobuy.AutoBuySection;
import aethereal.autobuy.CollectorSection;
import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Interface;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import aethereal.cosmetic.figura.CosmeticsSection;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.ui.element.Section;
import aethereal.ui.recode.RecodeKit;
import aethereal.util.DiscordAvatar;
import aethereal.util.MathUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import org.joml.Vector4f;

public class StationScreen extends class_437 {
   private final List<Section> a = new ArrayList<>();
   private final Vector4f b = new Vector4f(0.0F, 0.0F, 425.0F, 235.0F);
   private final Vector4f c = new Vector4f(0.0F, 0.0F, 0.0F, 16.0F);
   private final Vector4f d = new Vector4f(0.0F, 0.0F, 0.0F, 0.0F);
   private final AnimationUtil e = new AnimationUtil();
   private final AnimationUtil f = new AnimationUtil();
   private int g;
   private float h;

   @Compile
   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      super.method_25394(context, mouseX, mouseY, delta);
      Vector4f vector4f = this.b;
      class_310 class_310Var = Interface.aM_;
      vector4f.x = (class_310Var.method_22683().method_4486() - this.b.z) * 0.5F;
      this.b.y = (class_310Var.method_22683().method_4502() - this.b.w) * 0.5F;
      this.f.a(0.0F, 1.0F, 0.3F, EasingList.g, delta);
      this.f.a(true);
      Vector4f vector4f2 = this.b;
      float f = vector4f2.x;
      float f2 = 0.5F * vector4f2.z;
      float f3 = vector4f2.y;
      float f4 = 0.5F * vector4f2.w;
      float fEase = 0.85F + EasingList.s.ease(this.f.c()) * 0.15F;
      class_4587 class_4587VarMethod_51448 = context.method_51448();
      class_4587VarMethod_51448.method_22903();
      float f5 = f2 + f;
      float f6 = f4 + f3;
      class_4587VarMethod_51448.method_46416(f5, (1.0F - EasingList.p.ease(this.f.c())) * 14.0F + f6, 0.0F);
      class_4587VarMethod_51448.method_22905(fEase, fEase, 1.0F);
      class_4587VarMethod_51448.method_46416(-f5, -f6, 0.0F);
      Draw2DProcessor draw2DProcessorI = Westra.h().d().i();
      ThemeProcessor themeProcessorO = Westra.h().d().o();
      int iA = themeProcessorO.a(ThemeInfo.BACKGROUND_GUI).a();
      ThemeInfo themeInfo = ThemeInfo.PRIMARY;
      int iA2 = ColorUtil.a(ColorUtil.a(iA, themeProcessorO.a(themeInfo).a(), themeProcessorO.a(themeInfo).b() * 0.25F), 220);
      float open = Math.min(1.0F, this.f.c());
      RecodeKit.glass(context.method_51448(), vector4f2.x, vector4f2.y, vector4f2.z, vector4f2.w, 12.0F, open, 0.93F, 0.1F, 0.18F);
      RecodeKit.shimmer(context.method_51448(), vector4f2.x + 16.0F, vector4f2.y + 0.5F, vector4f2.z - 32.0F, 0.75F, RecodeKit.time() * 0.8F, 0.55F * open);
      this.a(context, delta, iA2);
      this.d.set(vector4f2.x, this.c.y + this.c.w + 8.0F, vector4f2.z, vector4f2.y + vector4f2.w - 8.0F - (this.c.y + this.c.w + 12.0F));
      this.b().a(context, this.d, mouseX, mouseY, this.e.a(), delta);
      class_4587VarMethod_51448.method_22909();
      this.e.a(Math.min(0.0F, this.d.w - this.b().a(this.d)), 0.0F, 1.0F);
   }

   @Compile
   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (this.b().a(mouseX, mouseY, verticalAmount)) {
         return true;
      } else {
         this.e.a((float)verticalAmount * 15.0F);
         return true;
      }
   }

   @Compile
   public boolean method_25402(double mouseX, double mouseY, int button) {
      Vector4f vector4f = this.c;
      List<Section> list = this.a;
      Vector4f vector4f2 = this.d;
      AnimationUtil animationUtil = this.e;
      float f = vector4f.x + 12.0F;

      for (int i = 0; list.size() > i; i++) {
         float fB = Fonts.a.b(list.get(i).b(), 8.5F);
         if (MathUtil.a(mouseX, mouseY, f - 6.0F, vector4f.y, fB + 12.0F, vector4f.w)) {
            this.g = i;
            animationUtil.b(0.0F);
            return true;
         }

         f += fB + 12.0F;
      }

      return !(vector4f2.y > mouseY) && this.b().a(mouseX, mouseY, button) ? true : super.method_25402(mouseX, mouseY, button);
   }

   @Compile
   public boolean method_25406(double mouseX, double mouseY, int button) {
      return this.b().b(mouseX, mouseY, button) ? true : super.method_25406(mouseX, mouseY, button);
   }

   @Compile
   public boolean method_25403(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      return this.b().a(mouseX, mouseY, button, deltaX, deltaY) ? true : super.method_25403(mouseX, mouseY, button, deltaX, deltaY);
   }

   @Compile
   public boolean method_25400(char character, int modifiers) {
      return this.b().a(character, modifiers) ? true : super.method_25400(character, modifiers);
   }

   @Compile
   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      Section sectionB = this.b();
      if (sectionB == null) {
         throw new NullPointerException();
      } else {
         return sectionB.a(keyCode, scanCode, modifiers) ? true : super.method_25404(keyCode, scanCode, modifiers);
      }
   }

   @Generated
   public AnimationUtil a() {
      return this.e;
   }

   public StationScreen(class_2561 title) {
      this(title, 0);
   }

   public StationScreen(class_2561 title, int selected) {
      super(title);
      this.a.add(new CollectorSection());
      this.a.add(new AutoBuySection());
      this.a.add(new CosmeticsSection());
      this.g = selected;
   }

   public void method_25419() {
      super.method_25419();
      this.f.c(0.0F);
   }

   public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {
   }

   private Section b() {
      return this.a.get(this.g);
   }

   private void a(class_332 context, float delta, int background) {
      Draw2DProcessor draw = Westra.h().d().i();
      ThemeProcessor theme = Westra.h().d().o();
      float iconsWidth = 12.0F * (this.a.size() - 1);
      Iterator<Section> it = this.a.iterator();

      while (it.hasNext()) {
         iconsWidth += Fonts.a.b(it.next().b(), 8.5F);
      }

      this.c.z = iconsWidth + 24.0F;
      this.c.x = this.b.x + (this.b.z - this.c.z) / 2.0F;
      this.c.y = this.b.y + 8.0F;
      class_4587 matrices = context.method_51448();
      draw.a(matrices, this.c.x, this.c.y, this.c.z, this.c.w, this.c.w / 2.0F, ColorUtil.a(-1, 0.04F));
      draw.a(matrices, this.c.x, this.c.y, this.c.z, this.c.w, this.c.w / 2.0F, 0.5F, ColorUtil.a(-1, 0.07F));
      draw.a(matrices, DiscordAvatar.a(), this.b.x + 8.0F, this.c.y + (this.c.w - 12.0F) / 2.0F, 12.0F, 12.0F, 6.0F, ColorUtil.a(-1, 1.0F));
      RecodeKit.logo(matrices, this.b.x + 8.0F + 12.0F + 6.0F, Fonts.e.a("W", 7.5F, this.c.y + this.c.w / 2.0F), 7.5F, 1.0F);
      String[] names = new String[]{"Коллектор", "Автобай", "Косметика"};
      String sectionName = this.g < names.length ? names[this.g] : this.b().c();
      float nameRight = this.b.x + this.b.z - 10.0F;
      Fonts.d
         .a(
            matrices,
            sectionName,
            nameRight - Fonts.d.a(sectionName, 6.75F),
            Fonts.d.a(sectionName, 6.75F, this.c.y + this.c.w / 2.0F),
            6.75F,
            ColorUtil.a(-1, 0.85F)
         );
      if (this.h != 0.0F) {
         float pillWidth = 20.0F;
         int pillLeft = ColorUtil.a(theme.a(ThemeInfo.PRIMARY).a(), 0.3F);
         int pillRight = ColorUtil.a(RecodeKit.accentShade(), 0.12F);
         draw.a(
            matrices,
            this.h - pillWidth / 2.0F,
            this.c.y + 2.0F,
            pillWidth,
            this.c.w - 4.0F,
            (this.c.w - 4.0F) / 2.0F,
            pillLeft,
            pillRight,
            pillLeft,
            pillRight
         );
      }

      float x = this.c.x + 12.0F;
      float y = this.c.y + (this.c.w - Fonts.a.a(8.5F)) / 2.0F;
      float target = this.h;

      for (int i = 0; i < this.a.size(); i++) {
         Section section = this.a.get(i);
         float iconWidth = Fonts.a.b(section.b(), 8.5F);
         section.a().a(i == this.g);
         section.a().a(0.0F, 1.0F, 0.3F, EasingList.i, delta);
         Fonts.a.a(matrices, section.b(), x, y, 8.5F, ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), -1, section.a().c()));
         if (i == this.g) {
            target = x + iconWidth / 2.0F;
         }

         x += iconWidth + 12.0F;
      }

      this.h = this.h == 0.0F ? target : MathUtil.c(this.h, target, 0.35F);
   }

   static {
      NativeMethodLookup.lookup(StationScreen.class, 17);
   }
}

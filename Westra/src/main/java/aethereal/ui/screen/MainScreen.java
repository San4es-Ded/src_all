package aethereal.ui.screen;

import aethereal.api.Compile;
import aethereal.config.ThemeInfo;
import aethereal.core.Interface;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Fonts;
import aethereal.render.Motion;
import aethereal.render.ScaleUtil;
import aethereal.ui.recode.RecodeKit;
import aethereal.ui.widget.EffectMarker;
import aethereal.util.MathUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import net.minecraft.class_1143;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_429;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_500;
import net.minecraft.class_5195;
import net.minecraft.class_526;

public class MainScreen extends class_437 {
   private static final float[] PARALLAX = new float[2];
   private static final String SPLASH_TEXT = "Westra Recode";
   private static final float SPLASH_TIME = 2300.0F;
   private static final float BUTTON_WIDTH = 132.0F;
   private static final float BUTTON_HEIGHT = 19.0F;
   private static final float BUTTON_GAP = 4.0F;
   private static final DateTimeFormatter CLOCK = DateTimeFormatter.ofPattern("HH:mm");
   private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("EEEE, d MMMM", new Locale("ru"));
   private static boolean splashPlayed;
   private final List<MainScreen.Entry> entries = new ArrayList<>();
   private final List<EffectMarker.a> ripples = new ArrayList<>();
   private final List<MainScreen.Particle> particles = new ArrayList<>();
   private final long openedAt = System.currentTimeMillis();
   private final boolean splash;
   private final Motion reveal = new Motion(700L, Motion.OUT);
   private float buttonsX;
   private float buttonsY;

   public MainScreen() {
      super(class_2561.method_43473());
      this.splash = !splashPlayed;
      splashPlayed = true;
      this.entries
         .add(
            new MainScreen.Entry(
               "h", "Одиночная игра", "Миры на этом компьютере", false, () -> Interface.aM_.method_1507(new class_526(this)), new Motion(260L, Motion.OUT)
            )
         );
      this.entries
         .add(
            new MainScreen.Entry(
               "P", "Сетевая игра", "Серверы и подключение", false, () -> Interface.aM_.method_1507(new class_500(this)), new Motion(260L, Motion.OUT)
            )
         );
      this.entries
         .add(
            new MainScreen.Entry(
               "L", "Аккаунты", "Смена и добавление ников", false, () -> Interface.aM_.method_1507(new AltScreen()), new Motion(260L, Motion.OUT)
            )
         );
      this.entries
         .add(
            new MainScreen.Entry(
               "%",
               "Настройки",
               "Параметры игры",
               false,
               () -> Interface.aM_.method_1507(new class_429(this, Interface.aM_.field_1690)),
               new Motion(260L, Motion.OUT)
            )
         );
      this.entries.add(new MainScreen.Entry("U", "Выйти", "Закрыть игру", true, () -> Interface.aM_.method_1592(), new Motion(260L, Motion.OUT)));
      Random random = new Random();

      for (int index = 0; index < 18; index++) {
         MainScreen.Particle particle = new MainScreen.Particle();
         particle.x = random.nextFloat();
         particle.y = random.nextFloat();
         particle.speed = 0.004F + random.nextFloat() * 0.01F;
         particle.size = 0.6F + random.nextFloat() * 0.9F;
         particle.phase = random.nextFloat() * 6.28F;
         this.particles.add(particle);
      }
   }

   @Compile
   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      double mx = MathUtil.scale(mouseX, 2);
      double my = MathUtil.scale(mouseY, 2);
      ScaleUtil.a(context, 2);
      int width = Interface.aM_.method_22683().method_4486();
      int height = Interface.aM_.method_22683().method_4502();
      float shown = this.reveal.to(this.splashDone() ? 1.0F : 0.0F);
      a(context, width, height, (int)mx, (int)my, 1.12F - 0.06F * shown);
      this.wash(context.method_51448(), width, height, shown);
      this.particles(context.method_51448(), width, height, delta, shown);
      this.brand(context.method_51448(), width, height, shown);
      this.buttons(context.method_51448(), width, height, mx, my, shown);
      this.clock(context.method_51448(), width, shown);
      this.footer(context.method_51448(), width, height, shown);
      EffectMarker.a(context.method_51448(), delta, this.ripples);
      this.splash(context.method_51448(), width, height);
      ScaleUtil.a(context);
   }

   public static void a(class_332 context, int width, int height, int mouseX, int mouseY, float scale) {
      float marginX = width * 0.03F;
      float marginY = height * 0.03F;
      PARALLAX[0] = PARALLAX[0]
         + (class_3532.method_15363(((float)mouseX / width - 0.5F) * 2.0F * marginX, -marginX * 0.9F, marginX * 0.9F) - PARALLAX[0]) * 0.04F;
      PARALLAX[1] = PARALLAX[1]
         + (class_3532.method_15363(((float)mouseY / height - 0.5F) * 2.0F * marginY, -marginY * 0.9F, marginY * 0.9F) - PARALLAX[1]) * 0.04F;
      class_4587 matrices = context.method_51448();
      matrices.method_22903();
      matrices.method_46416(width / 2.0F, height / 2.0F, 0.0F);
      matrices.method_22905(scale, scale, 1.0F);
      matrices.method_46416(-width / 2.0F, -height / 2.0F, 0.0F);
      int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
      int tint = ColorUtil.a(-1, accent, 0.18F);
      Westra.h()
         .d()
         .i()
         .a(
            matrices,
            class_2960.method_60655("westra", "pictures/main.png"),
            -marginX - PARALLAX[0],
            -marginY - PARALLAX[1],
            width + marginX * 2.0F,
            height + marginY * 2.0F,
            0.0F,
            tint
         );
      matrices.method_22909();
      Draw2DProcessor draw = Westra.h().d().i();
      int left = ColorUtil.a(-16448503, 0.5F);
      int right = ColorUtil.a(-16448503, 0.0F);
      draw.a(matrices, 0.0F, 0.0F, (float)width, (float)height, 0.0F, left, right, left, right);
      int bottom = ColorUtil.a(-16448503, 0.25F);
      int clear = ColorUtil.a(-16448503, 0.0F);
      draw.a(matrices, 0.0F, height * 0.6F, (float)width, height * 0.4F, 0.0F, clear, clear, bottom, bottom);
   }

   private void wash(class_4587 matrices, int width, int height, float shown) {
      float breath = 0.75F + 0.25F * (float)Math.sin(RecodeKit.time() * 0.6F);
      int glow = RecodeKit.alpha(RecodeKit.accent(), 0.14F * breath * shown);
      int clear = RecodeKit.alpha(RecodeKit.accent(), 0.0F);
      RecodeKit.draw().a(matrices, 0.0F, height * 0.3F, width * 0.75F, height * 0.7F, 0.0F, clear, clear, glow, clear);
   }

   private void particles(class_4587 matrices, int width, int height, float delta, float shown) {
      Draw2DProcessor draw = RecodeKit.draw();
      float time = RecodeKit.time();

      for (MainScreen.Particle particle : this.particles) {
         particle.y = particle.y - particle.speed * delta * 0.05F;
         if (particle.y < -0.02F) {
            particle.y = 1.02F;
         }

         float twinkle = (float)((Math.sin(time * 1.6F + particle.phase) + 1.0) * 0.5);
         float x = particle.x * width + (float)Math.sin(time * 0.5F + particle.phase) * 6.0F;
         float y = particle.y * height;
         float alpha = (0.06F + 0.22F * twinkle) * shown;
         draw.a(matrices, x, y, particle.size, particle.size, particle.size / 2.0F, RecodeKit.alpha(ColorUtil.a(-1, RecodeKit.accent(), 0.4F), alpha));
      }
   }

   private float leftEdge(int width) {
      return Math.max(22.0F, width * 0.08F);
   }

   private float brandTop(int height) {
      float stack = this.entries.size() * 23.0F - 4.0F;
      return (height - (42.0F + stack)) / 2.0F;
   }

   private void brand(class_4587 matrices, int width, int height, float shown) {
      float appear = Motion.progress(this.openedAt, this.splashDelay(), 700L, Motion.OUT);
      float alpha = appear * shown;
      if (!(alpha <= 0.0F)) {
         float x = this.leftEdge(width);
         float y = this.brandTop(height) - (1.0F - appear) * 10.0F;
         String hello = greeting() + ", " + Interface.aM_.method_1548().method_1676();
         Fonts.c.a(matrices, hello, x + 1.0F, y, 6.5F, RecodeKit.alpha(RecodeKit.dim(), alpha));
         RecodeKit.logo(matrices, x, y + 10.0F, 20.0F, alpha);
      }
   }

   private void buttons(class_4587 matrices, int width, int height, double mouseX, double mouseY, float shown) {
      Draw2DProcessor draw = RecodeKit.draw();
      float x = this.leftEdge(width);
      float y = this.brandTop(height) + 42.0F;
      this.buttonsX = x;
      this.buttonsY = y;
      int accent = RecodeKit.accent();

      for (int index = 0; index < this.entries.size(); index++) {
         MainScreen.Entry entry = this.entries.get(index);
         float appear = Motion.progress(this.openedAt, this.splashDelay() + 180L + index * 70L, 520L, Motion.SPRING);
         float alpha = Math.min(1.0F, appear) * shown;
         if (alpha <= 0.0F) {
            y += 23.0F;
         } else {
            float bx = x + (1.0F - appear) * -20.0F;
            boolean hovered = MathUtil.a(mouseX, mouseY, x, y, 132.0F, 19.0F) && appear > 0.9F;
            float hover = entry.hover().to(hovered);
            int tone = entry.danger() ? -42386 : accent;
            draw.a(matrices, bx, y, 132.0F, 19.0F, 6.0F, RecodeKit.alpha(-1, (0.035F + 0.025F * hover) * alpha));
            if (hover > 0.01F) {
               int left = RecodeKit.alpha(tone, 0.28F * hover * alpha);
               int right = RecodeKit.alpha(tone, 0.0F);
               draw.a(matrices, bx, y, 132.0F, 19.0F, 6.0F, left, right, left, right);
            }

            draw.a(matrices, bx, y, 132.0F, 19.0F, 6.0F, 0.5F, RecodeKit.alpha(ColorUtil.a(-1, tone, hover), (0.07F + 0.3F * hover) * alpha));
            float center = y + 9.5F;
            int iconColor = ColorUtil.a(RecodeKit.dim(), tone, 0.4F + 0.6F * hover);
            Fonts.a.a(matrices, entry.icon(), bx + 8.0F, Fonts.a.a(entry.icon(), 7.0F, center), 7.0F, RecodeKit.alpha(iconColor, alpha));
            Fonts.d
               .a(
                  matrices,
                  entry.label(),
                  bx + 21.0F + hover * 2.0F,
                  Fonts.d.a(entry.label(), 7.0F, center),
                  7.0F,
                  RecodeKit.alpha(ColorUtil.a(RecodeKit.text(), -1, hover), alpha)
               );
            y += 23.0F;
         }
      }
   }

   private void clock(class_4587 matrices, int width, float shown) {
      float appear = Motion.progress(this.openedAt, this.splashDelay() + 250L, 700L, Motion.OUT);
      float alpha = appear * shown;
      if (!(alpha <= 0.0F)) {
         LocalDateTime now = LocalDateTime.now();
         String time = now.format(CLOCK);
         String date = now.format(DATE);
         float right = width - Math.max(22.0F, width * 0.05F);
         float y = 18.0F - (1.0F - appear) * 8.0F;
         Fonts.e.a(matrices, time, right - Fonts.e.a(time, 26.0F), y, 26.0F, RecodeKit.alpha(-1, 0.92F * alpha));
         Fonts.c.a(matrices, date, right - Fonts.c.a(date, 7.0F), y + 29.0F, 7.0F, RecodeKit.alpha(RecodeKit.dim(), alpha));
      }
   }

   private void footer(class_4587 matrices, int width, int height, float shown) {
      float alpha = Motion.progress(this.openedAt, this.splashDelay() + 500L, 600L, Motion.SMOOTH) * shown;
      if (!(alpha <= 0.0F)) {
         String text = "Minecraft 1.21.4";
         float edge = width - Math.max(22.0F, width * 0.05F);
         Fonts.c.a(matrices, text, edge - Fonts.c.a(text, 5.5F), height - 15.0F, 5.5F, RecodeKit.alpha(RecodeKit.dim(), 0.6F * alpha));
      }
   }

   private void splash(class_4587 matrices, int width, int height) {
      if (this.splash) {
         float elapsed = this.elapsed();
         if (!(elapsed >= 2300.0F)) {
            float fadeOut = 1.0F - class_3532.method_15363((elapsed - 1700.0F) / 600.0F, 0.0F, 1.0F);
            float fadeIn = class_3532.method_15363(elapsed / 300.0F, 0.0F, 1.0F);
            float alpha = fadeIn * fadeOut;
            Draw2DProcessor draw = RecodeKit.draw();
            draw.a(matrices, 0.0F, 0.0F, width, height, 0.0F, RecodeKit.alpha(-16448503, alpha));
            float size = 20.0F;
            float logoWidth = RecodeKit.logoWidth(size);
            float x = (width - logoWidth) / 2.0F;
            float y = height / 2.0F - size / 2.0F;
            float typed = class_3532.method_15363((elapsed - 250.0F) / 900.0F, 0.0F, 1.0F);
            float scale = 0.94F + 0.06F * Motion.OUT.ease(typed);
            matrices.method_22903();
            matrices.method_46416(width / 2.0F, height / 2.0F, 0.0F);
            matrices.method_22905(scale, scale, 1.0F);
            matrices.method_46416(-width / 2.0F, -height / 2.0F, 0.0F);
            RecodeKit.logo(matrices, x, y, size, alpha * Motion.OUT.ease(typed));
            float bar = logoWidth * Motion.OUT.ease(class_3532.method_15363((elapsed - 500.0F) / 900.0F, 0.0F, 1.0F));
            int left = RecodeKit.alpha(RecodeKit.accent(), alpha);
            int right = RecodeKit.alpha(RecodeKit.accentShade(), alpha);
            draw.a(matrices, x, y + size + 6.0F, bar, 1.5F, 0.75F, left, right, left, right);
            matrices.method_22909();
         }
      }
   }

   private long splashDelay() {
      return this.splash ? 1900L : 0L;
   }

   private boolean splashDone() {
      return !this.splash || this.elapsed() >= 1700.0F;
   }

   private float elapsed() {
      return (float)(System.currentTimeMillis() - this.openedAt);
   }

   private static String greeting() {
      int hour = LocalDateTime.now().getHour();
      if (hour < 5) {
         return "Доброй ночи";
      } else if (hour < 12) {
         return "Доброе утро";
      } else {
         return hour < 18 ? "Добрый день" : "Добрый вечер";
      }
   }

   @Compile
   public boolean method_25402(double mouseX, double mouseY, int button) {
      if (!this.splashDone()) {
         return true;
      } else {
         double sx = MathUtil.scale(mouseX, 2);
         double sy = MathUtil.scale(mouseY, 2);
         EffectMarker.a(this.ripples, (float)sx, (float)sy);
         float x = this.buttonsX;
         float y = this.buttonsY;

         for (MainScreen.Entry entry : this.entries) {
            if (button == 0 && MathUtil.a(sx, sy, x, y, 132.0F, 19.0F)) {
               entry.action().run();
               return true;
            }

            y += 23.0F;
         }

         return super.method_25402(mouseX, mouseY, button);
      }
   }

   public class_5195 method_50024() {
      return class_1143.field_5585;
   }

   public boolean method_25422() {
      return false;
   }

   public void method_25419() {
   }

   public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {
   }

   static {
      NativeMethodLookup.lookup(MainScreen.class, 16);
   }

   private record Entry(String icon, String label, String hint, boolean danger, Runnable action, Motion hover) {
   }

   private static final class Particle {
      float x;
      float y;
      float speed;
      float size;
      float phase;
   }
}

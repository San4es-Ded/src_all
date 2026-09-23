package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.BackendEvent;
import aethereal.event.DrawEvent;
import aethereal.event.PacketEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.Setting;
import aethereal.ui.element.DragInfo;
import aethereal.ui.element.Element_2;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_408;

public class Widget {
   private final List<Setting<?>> f = new ObjectArrayList();
   private final List<Element_2<?>> g = new ObjectArrayList();
   protected final AnimationUtil a = new AnimationUtil();
   protected final AnimationUtil b = new AnimationUtil();
   protected final AnimationUtil c = new AnimationUtil();
   private boolean h = false;
   protected float d = 12.5F;
   protected float e = 7.0F;
   private final DragInfo i;

   @Generated
   public List<Setting<?>> b() {
      return this.f;
   }

   @Generated
   public List<Element_2<?>> c() {
      return this.g;
   }

   @Generated
   public AnimationUtil d() {
      return this.a;
   }

   @Generated
   public AnimationUtil e() {
      return this.b;
   }

   @Generated
   public AnimationUtil f() {
      return this.c;
   }

   @Generated
   public void a(boolean status) {
      this.h = status;
   }

   @Generated
   public boolean g() {
      return this.h;
   }

   @Generated
   public float h() {
      return this.d;
   }

   @Generated
   public float i() {
      return this.e;
   }

   @Generated
   public DragInfo j() {
      return this.i;
   }

   public Widget(DragInfo dragInfo) {
      this.i = dragInfo;
      dragInfo.a(this);
   }

   protected final void a(Setting<?>... settings) {
      for (Setting<?> setting : settings) {
         this.f.add(setting);
         this.g.add(setting.d());
      }
   }

   public void a(DrawEvent event) {
      this.e().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      this.c.a(this.h && Interface.aM_.field_1755 instanceof class_408);
      this.c.a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      if (this.c.c() > 0.0F) {
         this.b(event);
      }
   }

   public void a(GlobalEvent event) {
      this.e().a(this.i == Westra.h().d().s().g());
   }

   public void a(PacketEvent event) {
   }

   public void a(BackendEvent event) {
   }

   protected void b(DrawEvent event) {
      List<Element_2<?>> visible = this.g.stream().filter(e -> e.e().e().get()).toList();
      if (!visible.isEmpty()) {
         float panelWidth = visible.stream().map(e2 -> 19.5F + Fonts.e.a(e2.e().i(), 6.5F) + 25.0F).reduce(0.0F, (v0, v1) -> Math.max(v0, v1));
         float totalHeight = 12.0F * visible.size() + (visible.size() - 1);
         float anim = this.c.c() * this.a();
         float baseX = this.i.b() - totalHeight - 2.0F >= 0.0F ? this.i.a() + this.i.f() / 2.0F - panelWidth / 2.0F : this.i.a() + this.i.f() + 2.0F;
         float baseY = this.i.b() - totalHeight - 2.0F >= 0.0F ? this.i.b() - totalHeight - 2.0F : this.i.b();
         float baseX2 = Math.min(Math.max(baseX, 0.0F), Interface.aM_.method_22683().method_4486() - panelWidth - 2.0F);
         float baseY2 = Math.min(Math.max(baseY, 0.0F), Interface.aM_.method_22683().method_4502() - totalHeight);
         this.a(event, baseX2, baseY2, panelWidth, totalHeight, true, anim);
         float y = baseY2;

         for (Element_2<?> element : visible) {
            element.d().set(baseX2, y, panelWidth, 12.0F);
            element.a(event, baseX2, y, panelWidth, anim);
            y += 13.0F;
            if (element != visible.getLast()) {
               event.d().a(event.i().method_51448(), baseX2, y - 1.0F, panelWidth, 0.75F, 0.0F, ColorUtil.a(ColorUtil.a(200, 200, 200, 255), 0.2F * anim));
            }
         }
      }
   }

   protected void a(DrawEvent event, String icon, Object title, float width, float animation) {
      this.a(event, icon, title, width, animation, Westra.h().d().o().a(ThemeInfo.PRIMARY).a());
   }

   protected void a(DrawEvent event, String icon, Object title, float width, float animation, int iconColor) {
      this.a(event, this.i.a(), this.i.b(), icon, title, width, animation, iconColor);
   }

   protected void a(DrawEvent event, float x, float y, String icon, Object title, float width, float animation, int iconColor) {
      this.a(event, x, y, icon, null, title, width, animation, iconColor);
   }

   protected void a(DrawEvent event, float x, float y, class_1799 icon, Object title, float width, float animation, int iconColor) {
      this.a(event, x, y, null, icon, title, width, animation, iconColor);
   }

   private void a(DrawEvent event, float x, float y, String icon, class_1799 stack, Object title, float width, float animation, int iconColor) {
      if (animation > 0.0F) {
         float iconSize = this.e + 1.0F;
         this.a(event, x, y, width, this.d, true, animation);
         if (stack != null) {
            Westra.h().d().j().a(event.i(), stack, x + 3.0F, y + (this.d - 8.0F) / 2.0F - 0.25F, 0, animation, 0.5F, false);
         } else {
            Fonts.a.a(event.h(), icon, x + 3.0F, y + (this.d - Fonts.a.a(iconSize)) / 2.0F, iconSize, ColorUtil.a(iconColor, animation));
         }

         this.a(event, x + 13.5F, y, this.d, animation);
         if (title instanceof class_2561 text) {
            Fonts.e.a(event.h(), text, x + 17.5F, y + (this.d - Fonts.e.a(this.e)) / 2.0F - 0.5F, this.e, (double)animation);
         } else {
            Fonts.e.a(event.h(), String.valueOf(title), x + 17.5F, y + (this.d - Fonts.e.a(this.e)) / 2.0F - 0.5F, this.e, ColorUtil.a(-1, animation));
         }
      }
   }

   protected void a(DrawEvent event, float x, float y, float width, float height, boolean glow, float animation) {
      if (animation > 0.0F) {
         ThemeProcessor themeProcessor = Westra.h().d().o();
         float alpha = themeProcessor.a(ThemeInfo.BACKGROUND_HUD).b() * animation;
         int background = ColorUtil.a(
            themeProcessor.a(ThemeInfo.BACKGROUND_HUD).a(), themeProcessor.a(ThemeInfo.PRIMARY).a(), themeProcessor.a(ThemeInfo.PRIMARY).b() / 6.0F
         );
         themeProcessor.a(ThemeInfo.BACKGROUND_HUD).e(170);
         if (glow) {
            event.d()
               .a(
                  event.h(),
                  x,
                  y,
                  width,
                  height,
                  5.0F + 1.0F * this.b.c(),
                  ColorUtil.a(background, alpha),
                  animation,
                  ColorUtil.a(background, alpha),
                  8.0F + 2.0F * this.b.c()
               );
         } else {
            event.d().b(event.h(), x, y, width, height, 5.0F, ColorUtil.a(background, alpha), animation);
         }
      }
   }

   protected void a(DrawEvent event, float x, float y, float height, float animation) {
      float separatorHeight = height / 2.0F;
      event.d()
         .a(
            event.i().method_51448(),
            x,
            y + (height - separatorHeight) / 2.0F,
            0.75F,
            separatorHeight,
            0.0F,
            ColorUtil.a(ColorUtil.a(200, 200, 200, 255), 0.5F * animation)
         );
   }

   public float a() {
      return this.a.c() * (1.0F - 0.1F * this.b.c());
   }
}

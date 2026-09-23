package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.mixin.IStatusEffectInstance;
import aethereal.notification.Notification;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.ui.element.DragInfo;
import aethereal.util.MathUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_2561;
import net.minecraft.class_408;
import net.minecraft.class_4081;
import org.joml.Vector4f;

public class PotionWidget extends Widget implements Interface {
   private final BooleanSetting f = new BooleanSetting("Боковое отображение", false);
   private final class_1293 g = new class_1293(class_1294.field_5904, 1200, 0);

   public PotionWidget() {
      super(new DragInfo("Зелья", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.a(new Setting[]{this.f});
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      Iterator<class_1293> it = this.k().iterator();

      while (it.hasNext()) {
         ((IStatusEffectInstance)it.next()).getAnimation().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      }

      if (this.f.c()) {
         this.d(event);
      } else {
         this.c(event);
      }

      this.j().a(this.f.c() ? 2 : 0);
   }

   private void c(DrawEvent event) {
      float x = this.j().a();
      float y = this.j().b();
      float targetWidth = 14.5F + Fonts.e.a("Potion-list", this.e) + 5.0F + 2.0F;
      float contentY = y + this.d + 3.0F;
      boolean active = false;

      for (IStatusEffectInstance iStatusEffectInstance : this.k()) {
         if (iStatusEffectInstance.getAnimation().c() > 0.0F) {
            active = true;
            String name = class_2561.method_43471(((class_1291)iStatusEffectInstance.method_5579().comp_349()).method_5567()).getString()
               + " "
               + (iStatusEffectInstance.method_5578() + 1);
            targetWidth = Math.max(
               targetWidth,
               19.0F
                  + Fonts.e.a(name, 6.5F)
                  + 8.0F
                  + Fonts.e
                     .a(
                        iStatusEffectInstance.method_5584() > 1000000
                           ? "∞"
                           : iStatusEffectInstance.method_5584() / 20 / 60 + ":" + String.format("%02d", iStatusEffectInstance.method_5584() / 20 % 60),
                        6.5F
                     )
                  + 5.0F
                  + 2.0F
            );
         }
      }

      float width = MathUtil.c(this.j().f(), targetWidth, 0.5F);
      this.j().c(width);
      if (this.a() > 0.0F) {
         this.a(event, "E", "Potion-list", width, this.a());
      }

      for (IStatusEffectInstance iStatusEffectInstance2 : this.k()) {
         float animation = iStatusEffectInstance2.getAnimation().c() * this.a();
         if (animation > 0.0F) {
            String name2 = class_2561.method_43471(((class_1291)iStatusEffectInstance2.method_5579().comp_349()).method_5567()).getString()
               + " "
               + (iStatusEffectInstance2.method_5578() + 1);
            int seconds = iStatusEffectInstance2.method_5584() / 20;
            String duration = iStatusEffectInstance2.method_5584() > 1000000 ? "∞" : seconds / 60 + ":" + String.format("%02d", seconds % 60);
            float offsetX = -8.0F * (1.0F - animation);
            float offsetY = -(1.0F - animation);
            float drawY = contentY + offsetY;
            float durationWidth = Fonts.e.a(duration, 6.5F);
            float textY = drawY + (11.5F - Fonts.e.a(6.5F)) / 2.0F - 0.5F;
            this.a(event, x + offsetX, drawY, width, 11.5F, false, animation);
            this.a(event, x + offsetX + 15.0F, drawY, 11.5F, animation);
            event.e()
               .a(event.i(), aM_.method_18505().method_18663(iStatusEffectInstance2.method_5579()), x + offsetX + 5.0F, drawY + 2.0F, 0.0F, 0.4F, animation);
            Fonts.e
               .a(
                  event.h(),
                  name2,
                  x + offsetX + 19.0F,
                  textY,
                  6.5F,
                  ColorUtil.a(
                     ((class_1291)iStatusEffectInstance2.method_5579().comp_349()).method_18792() == class_4081.field_18272
                        ? ColorUtil.a(255, 125, 125, 255)
                        : -1,
                     animation
                  )
               );
            Fonts.e.a(event.h(), duration, x + offsetX + width - 5.0F - durationWidth - 1.0F, textY, 6.5F, ColorUtil.a(-1, 0.55F * animation));
            contentY += 13.5F * animation;
         }
      }

      this.j().d(active ? contentY - y - 2.0F : this.d);
      super.a(event);
   }

   private void d(DrawEvent event) {
      int primary = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
      int visibleCount = 0;
      Iterator<class_1293> it = this.k().iterator();

      while (it.hasNext()) {
         if (((IStatusEffectInstance)it.next()).getAnimation().c() > 0.0F) {
            visibleCount++;
         }
      }

      float posY = (aM_.method_22683().method_4502() - (visibleCount * 26.0F + (visibleCount - 1) * 2.0F)) / 2.0F;
      float contentY = posY;
      float maxWidth = 0.0F;

      for (IStatusEffectInstance iStatusEffectInstance : this.k()) {
         float animation = iStatusEffectInstance.getAnimation().c() * this.a();
         if (animation > 0.0F) {
            boolean harmful = ((class_1291)iStatusEffectInstance.method_5579().comp_349()).method_18792() == class_4081.field_18272;
            String name = class_2561.method_43471(((class_1291)iStatusEffectInstance.method_5579().comp_349()).method_5567()).getString()
               + " "
               + (iStatusEffectInstance.method_5578() + 1);
            int seconds = iStatusEffectInstance.method_5584() / 20;
            String duration = iStatusEffectInstance.method_5584() > 1000000 ? "∞" : seconds / 60 + ":" + String.format("%02d", seconds % 60);
            float textWidth = Math.max(Fonts.e.a(name, 7.0F), Fonts.e.a(duration, 6.0F));
            float width = 18.5F + textWidth + 8.0F;
            float drawX = 3.0F - width * (1.0F - animation);
            float textX = drawX + 13.5F + 6.0F;
            this.a(event, drawX, contentY, width, 24.0F, true, animation);
            event.e()
               .a(event.i(), aM_.method_18505().method_18663(iStatusEffectInstance.method_5579()), drawX + 3.5F, contentY + 5.75F, 0.0F, 0.6944444F, animation);
            Fonts.e.a(event.h(), name, textX, contentY + 3.5F, 7.0F, ColorUtil.a(harmful ? ColorUtil.a(215, 76, 76, 255) : -1, animation));
            Fonts.e.a(event.h(), duration, textX, contentY + 13.0F, 6.0F, ColorUtil.a(-1, 0.55F * animation));
            int initialDuration = iStatusEffectInstance.getInitialDuration();
            float progress = initialDuration <= 0 ? 1.0F : Math.min(1.0F, (float)(iStatusEffectInstance.method_5584() / initialDuration));
            int accent = harmful ? ColorUtil.a(215, 76, 76, 255) : primary;
            event.d()
               .a(
                  event.h(),
                  drawX + 2.0F,
                  contentY + 24.0F - 1.5F,
                  width - 4.0F,
                  1.5F,
                  new Vector4f(0.0F, 0.0F, 1.0F, 1.0F),
                  ColorUtil.a(accent, 0.15F * animation)
               );
            event.d()
               .a(
                  event.h(),
                  drawX + 2.0F,
                  contentY + 24.0F - 1.5F,
                  (width - 4.0F) * progress,
                  1.5F,
                  new Vector4f(0.0F, 0.0F, 1.0F, 1.0F),
                  ColorUtil.a(accent, animation)
               );
            maxWidth = Math.max(maxWidth, width);
            contentY += 28.0F * animation;
         }
      }

      this.j().a(3.0F);
      this.j().b(posY);
      this.j().c(maxWidth);
      this.j().d(contentY - posY - 2.0F);
      super.a(event);
   }

   @Override
   public void a(GlobalEvent event) {
      boolean visible = aM_.field_1755 instanceof class_408;

      for (class_1293 effect : this.k()) {
         if (!effect.method_5579().equals(class_1294.field_5925)) {
            ((IStatusEffectInstance)effect).getAnimation().a(effect == this.g ? aM_.field_1755 instanceof class_408 : effect.method_5584() > 20);
            if (((IStatusEffectInstance)effect).getAnimation().c() > 0.0) {
               visible = true;
            }

            if (effect != this.g
               && effect.method_5584() == 100
               && (
                  effect.method_5579().equals(class_1294.field_5910)
                     || effect.method_5579().equals(class_1294.field_5904)
                     || effect.method_5579().equals(class_1294.field_5914)
                     || effect.method_5579().equals(class_1294.field_5905)
               )) {
               Westra.h()
                  .d()
                  .m()
                  .a(
                     new Notification(
                        "E",
                        class_2561.method_43470("Эффект ")
                           .method_10852(
                              class_2561.method_43471(((class_1291)effect.method_5579().comp_349()).method_5567())
                                 .method_27693(" " + (effect.method_5578() + 1))
                                 .method_27694(style -> style.method_36139(Westra.h().d().o().a(ThemeInfo.PRIMARY).a()))
                           )
                           .method_10852(class_2561.method_43470(" заканчивается")),
                        2500
                     )
                  );
            }
         }
      }

      this.d().a(visible);
      super.a(event);
   }

   private List<class_1293> k() {
      List<class_1293> effects = new ArrayList<>(aM_.field_1724.method_6026());
      boolean empty = effects.stream().allMatch(effect -> effect.method_5579().equals(class_1294.field_5925));
      if (this.f.c() && empty && (aM_.field_1755 instanceof class_408 || ((IStatusEffectInstance)this.g).getAnimation().c() > 0.0F)) {
         effects.add(this.g);
      }

      effects.sort(Comparator.comparingInt(effect2 -> {
         if (effect2.method_5579().equals(class_1294.field_5910)) {
            return 0;
         } else {
            return effect2.method_5579().equals(class_1294.field_5911) ? 1 : 2;
         }
      }));
      return effects;
   }
}

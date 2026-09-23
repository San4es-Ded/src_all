package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.ui.element.DragInfo;
import aethereal.util.DiscordAvatar;
import aethereal.util.MathUtil;
import aethereal.util.ServerUtil;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import platform.inject.accessors.BossBarHudAccessor;

public class WatermarkWidget extends Widget implements Interface {
   private float f;
   private final BooleanSetting g = new BooleanSetting("Боковое отображение", false);
   private final BooleanSetting h = new BooleanSetting("Разделять элементы", false);
   private final BooleanSetting i = new BooleanSetting("Частота кадров", true);
   private final BooleanSetting j = new BooleanSetting("Задержка игрока", true);
   private final BooleanSetting k = new BooleanSetting("Текущее время", true);
   private final BooleanSetting l = new BooleanSetting("Логин в клиенте", true);
   private final BooleanSetting m = new BooleanSetting("Координаты", true);
   private final BooleanSetting n = new BooleanSetting("Задержка сервера", true);
   private final BooleanSetting o = new BooleanSetting("Скорость игрока", true);

   public WatermarkWidget() {
      super(new DragInfo("Инфо-панель", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.j().a(2);
      this.a(new Setting[]{this.g, this.h, this.i, this.j, this.k, this.l, this.m, this.n, this.o});
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(true);
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      this.f = MathUtil.c(this.f, aM_.method_47599(), 0.1F);
      float iconSize = this.e - 0.5F;
      float logoSize = this.e + 1.0F;
      float sectionGap = !this.h.c() ? 5.0F : 2.0F;
      String[][] topSections = this.k();
      String[][] bottomSections = this.l();
      float topWidth = this.a(topSections, true, iconSize, logoSize, 5.0F, sectionGap, 3.0F, 4.0F);
      float bottomWidth = this.a(bottomSections, false, iconSize, logoSize, 5.0F, sectionGap, 3.0F, 4.0F);
      float x = this.g.c() ? 5.0F : (aM_.method_22683().method_4486() - topWidth) / 2.0F;
      float fMethod_4476;
      if (!this.g.c() && !((BossBarHudAccessor)aM_.field_1705.method_1740()).getBossBars().isEmpty()) {
         int size = ((BossBarHudAccessor)aM_.field_1705.method_1740()).getBossBars().size() - 1;
         fMethod_4476 = (12 + size * 19 + 5)
            * aM_.method_22683().method_4476((Integer)aM_.field_1690.method_42474().method_41753(), aM_.method_1573())
            / aM_.method_22683().method_4476(2, aM_.method_1573());
      } else {
         fMethod_4476 = 0.0F;
      }

      float y = 5.0F + fMethod_4476;
      float bottomX = this.g.c() ? x : x + (topWidth - bottomWidth) / 2.0F;
      this.j().a(x);
      this.j().b(y);
      this.j().c(topWidth);
      this.j().d(bottomSections.length > 0 ? this.d + 3.0F + this.d : this.d);
      int primaryColor = ColorUtil.a(Westra.h().d().o().a(ThemeInfo.PRIMARY).a(), 1.0F);
      this.a(event, x, y, topWidth, topSections, true, primaryColor, iconSize, logoSize, 5.0F, sectionGap, 3.0F, 4.0F, -0.5F);
      if (bottomSections.length > 0) {
         this.a(event, bottomX, y + this.d + 3.0F, bottomWidth, bottomSections, false, primaryColor, iconSize, logoSize, 5.0F, sectionGap, 3.0F, 4.0F, -0.5F);
      }

      super.a(event);
   }

   private void a(
      DrawEvent event,
      float x,
      float y,
      float width,
      String[][] sections,
      boolean logo,
      int primaryColor,
      float iconSize,
      float logoSize,
      float startPadding,
      float sectionGap,
      float iconTextGap,
      float logoGap,
      float textYOffset
   ) {
      if (this.h.c()) {
         this.a(event, x, y, sections, logo, primaryColor, iconSize, logoSize, startPadding, sectionGap, iconTextGap, textYOffset);
      } else {
         this.a(event, x, y, width, this.d, true, 1.0F);
         float cursor = x + startPadding;
         float textY = y + (this.d - Fonts.e.a(this.e)) / 2.0F + textYOffset;
         if (logo) {
            event.d().a(event.h(), DiscordAvatar.a(), cursor, y + (this.d - logoSize) / 2.0F, logoSize, logoSize, logoSize / 2.0F, ColorUtil.a(-1, 1.0F));
            float cursor2 = cursor + logoSize + logoGap;
            this.a(event, cursor2, y, this.d, 1.0F);
            cursor = cursor2 + 1.0F + sectionGap;
         }

         for (int i = 0; i < sections.length; i++) {
            if (i > 0) {
               this.a(event, cursor, y, this.d, 1.0F);
               cursor += 1.0F + sectionGap;
            }

            Fonts.a.a(event.h(), sections[i][0], cursor, y + (this.d - Fonts.a.a(iconSize)) / 2.0F, iconSize, primaryColor);
            float cursor3 = cursor + Fonts.a.a(sections[i][0], iconSize) + iconTextGap;
            Fonts.e.a(event.h(), sections[i][1], cursor3, textY, this.e, -1);
            cursor = cursor3 + Fonts.e.a(sections[i][1], this.e) + sectionGap;
         }
      }
   }

   private void a(
      DrawEvent event,
      float x,
      float y,
      String[][] sections,
      boolean logo,
      int primaryColor,
      float iconSize,
      float logoSize,
      float startPadding,
      float sectionGap,
      float iconTextGap,
      float textYOffset
   ) {
      float cursor = x;
      float textY = y + (this.d - Fonts.e.a(this.e)) / 2.0F + textYOffset;
      if (logo) {
         float logoWidth = startPadding * 2.0F + logoSize;
         this.a(event, x, y, logoWidth, this.d, true, 1.0F);
         event.d()
            .a(event.h(), DiscordAvatar.a(), x + startPadding, y + (this.d - logoSize) / 2.0F, logoSize, logoSize, logoSize / 2.0F, ColorUtil.a(-1, 1.0F));
         cursor = x + (logoWidth + sectionGap);
      }

      for (String[] section : sections) {
         float sectionWidth = startPadding * 2.0F + Fonts.a.a(section[0], iconSize) + iconTextGap + Fonts.e.a(section[1], this.e);
         this.a(event, cursor, y, sectionWidth, this.d, true, 1.0F);
         float inner = cursor + startPadding;
         Fonts.a.a(event.h(), section[0], inner, y + (this.d - Fonts.a.a(iconSize)) / 2.0F, iconSize, primaryColor);
         Fonts.e.a(event.h(), section[1], inner + Fonts.a.a(section[0], iconSize) + iconTextGap, textY, this.e, -1);
         cursor += sectionWidth + sectionGap;
      }
   }

   private float a(String[][] sections, boolean logo, float iconSize, float logoSize, float startPadding, float sectionGap, float iconTextGap, float logoGap) {
      if (!this.h.c()) {
         float width2 = startPadding;
         if (logo) {
            width2 = startPadding + logoSize + logoGap + 1.0F + sectionGap;
         }

         for (int i = 0; i < sections.length; i++) {
            if (i > 0) {
               width2 += 1.0F + sectionGap;
            }

            width2 = width2 + Fonts.a.a(sections[i][0], iconSize) + iconTextGap + Fonts.e.a(sections[i][1], this.e) + sectionGap;
         }

         return width2;
      } else {
         float width = 0.0F;
         if (logo) {
            width = 0.0F + startPadding * 2.0F + logoSize + sectionGap;
         }

         for (String[] section : sections) {
            width += startPadding * 2.0F + Fonts.a.a(section[0], iconSize) + iconTextGap + Fonts.e.a(section[1], this.e) + sectionGap;
         }

         return Math.max(0.0F, width - sectionGap);
      }
   }

   private String[][] k() {
      List<String[]> sections = new ArrayList<>();
      if (this.l.c()) {
         sections.add(new String[]{"L", Westra.h().g().b()});
      }

      if (this.i.c()) {
         sections.add(new String[]{"q", (int)this.f + " FPS"});
      }

      if (this.j.c()) {
         sections.add(new String[]{"P", ServerUtil.d() + " ms"});
      }

      if (this.k.c()) {
         sections.add(new String[]{"T", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))});
      }

      return sections.toArray(new String[0][]);
   }

   private String[][] l() {
      List<String[]> sections = new ArrayList<>();
      if (this.m.c()) {
         sections.add(
            new String[]{
               "b", "x " + (int)aM_.field_1724.method_23317() + " y " + (int)aM_.field_1724.method_23318() + " z " + (int)aM_.field_1724.method_23321()
            }
         );
      }

      if (this.n.c()) {
         sections.add(new String[]{"g", String.format("%.1f TPS", Westra.h().d().v().j().a())});
      }

      if (this.o.c()) {
         sections.add(new String[]{"e", String.format("%.2f BPS", ServerUtil.c())});
      }

      return sections.toArray(new String[0][]);
   }
}

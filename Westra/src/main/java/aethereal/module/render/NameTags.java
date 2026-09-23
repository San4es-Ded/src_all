package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.ProjectUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_4587;
import net.minecraft.class_5498;
import org.joml.Vector2f;

@ModuleRegister(
   a = "Name Tags",
   b = "Рисует свои таблички над игроками: имя, здоровье, дистанцию и надетые вещи",
   c = Category.Render
)
public class NameTags extends Module implements Interface {
   private static final class_1304[] b = new class_1304[]{
      class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166, class_1304.field_6173, class_1304.field_6171
   };
   private final SliderSetting c = new SliderSetting("Масштаб", 1.0F, 0.5F, 2.0F, 0.05F);
   private final SliderSetting d2 = new SliderSetting("Дистанция видимости", 64.0F, 8.0F, 128.0F, 1.0F);
   private final MultiModeSetting e = new MultiModeSetting(
      "Показывать",
      new BooleanSetting("Здоровье", true),
      new BooleanSetting("Дистанцию", true),
      new BooleanSetting("Экипировку", true),
      new BooleanSetting("Себя", false),
      new BooleanSetting("Только игроков", true)
   );
   private final BooleanSetting f2 = new BooleanSetting("Скрывать за стенами", false);
   private final BooleanSetting g2 = new BooleanSetting("Подложка", true);
   private final BooleanSetting h2 = new BooleanSetting("Прятать ванильные таблички", true);

   public NameTags() {
      this.a(new Setting[]{this.c, this.d2, this.e, this.f2, this.g2, this.h2});
   }

   public boolean q() {
      return this.m() && this.h2.c();
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b() && aM_.field_1687 != null && aM_.field_1724 != null) {
         List<class_1309> targets = new ArrayList<>();

         for (class_1297 entity : aM_.field_1687.method_18112()) {
            if (entity instanceof class_1309 living && this.a(living)) {
               targets.add(living);
            }
         }

         targets.sort((first, second) -> Double.compare(second.method_5858(aM_.field_1724), first.method_5858(aM_.field_1724)));

         for (class_1309 target : targets) {
            this.a(event, target);
         }
      }
   }

   private boolean a(class_1309 entity) {
      if (entity == null || !entity.method_5805() || entity.method_31481()) {
         return false;
      } else if (entity == aM_.field_1724) {
         return this.e.a("Себя").c() && aM_.field_1690.method_31044() != class_5498.field_26664;
      } else if (this.e.a("Только игроков").c() && !(entity instanceof class_1657)) {
         return false;
      } else {
         return aM_.field_1724.method_5739(entity) > this.d2.c() ? false : !this.f2.c() || aM_.field_1724.method_6057(entity);
      }
   }

   private void a(DrawEvent event, class_1309 entity) {
      double lerpX = entity.field_6014 + (entity.method_23317() - entity.field_6014) * event.g();
      double lerpY = entity.field_6036 + (entity.method_23318() - entity.field_6036) * event.g();
      double lerpZ = entity.field_5969 + (entity.method_23321() - entity.field_5969) * event.g();
      Vector2f screen = ProjectUtil.a(lerpX, lerpY + entity.method_17682() + 0.5, lerpZ);
      if (ProjectUtil.a(screen)) {
         class_4587 matrices = event.h();
         Draw2DProcessor draw = Westra.h().d().i();
         ThemeProcessor theme = Westra.h().d().o();
         float scale = this.c.c();
         float nameSize = 8.0F * scale;
         float infoSize = 6.5F * scale;
         String name = entity.method_5477().getString();
         boolean friend = entity instanceof class_1657 player && Westra.h().d().e().d(player.method_5477().getString());
         String health = this.e.a("Здоровье").c() ? String.format(Locale.ROOT, "%.0f", Math.max(0.0F, entity.method_6032() + entity.method_6067())) : "";
         String distance = this.e.a("Дистанцию").c() ? String.format(Locale.ROOT, "%.0fм", aM_.field_1724.method_5739(entity)) : "";
         float nameWidth = Fonts.c.a(name, nameSize);
         float healthWidth = health.isEmpty() ? 0.0F : Fonts.c.a(health, infoSize) + 4.0F * scale;
         float distanceWidth = distance.isEmpty() ? 0.0F : Fonts.c.a(distance, infoSize) + 4.0F * scale;
         float padding = 4.0F * scale;
         float width = nameWidth + healthWidth + distanceWidth + padding * 2.0F;
         float height = 12.0F * scale;
         List<class_1799> gear = this.q(entity);
         float gearSize = 10.0F * scale;
         float gearWidth = gear.size() * gearSize;
         float boxX = screen.x - width / 2.0F;
         float boxY = screen.y - height;
         if (this.g2.c()) {
            int background = ColorUtil.a(ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_HUD).a(), theme.a(ThemeInfo.PRIMARY).a(), 0.15F), 170);
            draw.a(matrices, boxX, boxY, width, height, 3.0F * scale, background);
            draw.a(
               matrices, boxX, boxY, width, height, 3.0F * scale, 0.5F, ColorUtil.a(theme.a(ThemeInfo.OUTLINE_SMALL).a(), theme.a(ThemeInfo.OUTLINE_SMALL).b())
            );
         }

         float cursor = boxX + padding;
         int nameColor = friend ? theme.a(ThemeInfo.PRIMARY).a() : ColorUtil.a(255, 255, 255, 255);
         Fonts.c.a(matrices, name, cursor, Fonts.c.a(name, nameSize, boxY + height / 2.0F), nameSize, nameColor);
         cursor += nameWidth;
         if (!health.isEmpty()) {
            cursor += 4.0F * scale;
            float ratio = Math.max(0.0F, Math.min(1.0F, entity.method_6032() / Math.max(1.0F, entity.method_6063())));
            int color = ColorUtil.a(ColorUtil.a(235, 64, 64, 255), ColorUtil.a(96, 220, 96, 255), ratio);
            Fonts.c.a(matrices, health, cursor, Fonts.c.a(health, infoSize, boxY + height / 2.0F), infoSize, color);
            cursor += Fonts.c.a(health, infoSize);
         }

         if (!distance.isEmpty()) {
            cursor += 4.0F * scale;
            Fonts.c.a(matrices, distance, cursor, Fonts.c.a(distance, infoSize, boxY + height / 2.0F), infoSize, theme.a(ThemeInfo.TEXT_DISABLED).a());
         }

         if (!gear.isEmpty()) {
            float gearX = screen.x - gearWidth / 2.0F;
            float gearY = boxY - gearSize - 1.0F * scale;

            for (class_1799 stack : gear) {
               event.e().a(event.i(), stack, gearX, gearY, 0, 1.0F, 0.625F * scale, true);
               gearX += gearSize;
            }
         }
      }
   }

   private List<class_1799> q(class_1309 entity) {
      List<class_1799> gear = new ArrayList<>();
      if (!this.e.a("Экипировку").c()) {
         return gear;
      } else {
         for (class_1304 slot : b) {
            class_1799 stack = entity.method_6118(slot);
            if (!stack.method_7960()) {
               gear.add(stack);
            }
         }

         return gear;
      }
   }
}

package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ColorSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1429;
import net.minecraft.class_1542;
import net.minecraft.class_1588;
import net.minecraft.class_1646;
import net.minecraft.class_1657;
import net.minecraft.class_1667;

@ModuleRegister(
   a = "Shader ESP",
   b = "Обводит сущности шейдерным контуром сквозь стены",
   c = Category.Render
)
public class ShaderESP extends Module implements Interface {
   private final MultiModeSetting b = new MultiModeSetting(
      "Отслеживаемые сущности",
      new BooleanSetting("Игроки", true),
      new BooleanSetting("Мобы", false),
      new BooleanSetting("Животные", false),
      new BooleanSetting("Предметы", false)
   );
   private final ModeSetting c = new ModeSetting("Цвет обводки", "Тема клиента", "Тема клиента", "Свой цвет", "По здоровью");
   private final ColorSetting d = new ColorSetting("Свой цвет", ColorUtil.a(255, 111, 181, 255)).a(() -> this.c.l("Свой цвет"));
   private final BooleanSetting e = new BooleanSetting("Друзей — зелёным", true);
   private final BooleanSetting f = new BooleanSetting("Цель ауры — красным", true);

   public ShaderESP() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e, this.f});
   }

   public boolean q(class_1297 entity) {
      if (this.m() && entity != null && entity != aM_.field_1724) {
         String category = r(entity);
         if (category == null) {
            return false;
         } else {
            BooleanSetting setting = this.b.a(category);
            return setting != null && setting.c();
         }
      } else {
         return false;
      }
   }

   private static String r(class_1297 entity) {
      if (entity instanceof class_1657) {
         return "Игроки";
      } else if (entity instanceof class_1588) {
         return "Мобы";
      } else if (!(entity instanceof class_1429) && !(entity instanceof class_1646)) {
         return !(entity instanceof class_1542) && !(entity instanceof class_1667) ? null : "Предметы";
      } else {
         return "Животные";
      }
   }

   public int s(class_1297 entity) {
      if (this.f.c() && entity == Westra.h().d().t().B().s()) {
         return ColorUtil.a(255, 70, 70, 255);
      } else if (this.e.c() && entity instanceof class_1657 && Westra.h().d().e().d(entity.method_5477().getString())) {
         return ColorUtil.a(90, 230, 130, 255);
      } else if (this.c.l("Свой цвет")) {
         return this.d.c();
      } else if (this.c.l("По здоровью") && entity instanceof class_1309 living) {
         float ratio = Math.max(0.0F, Math.min(1.0F, living.method_6032() / Math.max(1.0F, living.method_6063())));
         return ColorUtil.b(ColorUtil.a(235, 90, 90, 255), ColorUtil.a(110, 220, 130, 255), ratio);
      } else {
         return Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
      }
   }
}

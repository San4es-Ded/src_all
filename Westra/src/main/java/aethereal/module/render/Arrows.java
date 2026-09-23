package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.MathUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2960;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_7833;

@ModuleRegister(
   a = "Arrows",
   b = "Показывает направление на игроков стрелками вокруг прицела",
   c = Category.Render
)
public class Arrows extends Module implements Interface {
   private static final class_2960 b = class_2960.method_60655("westra", "pictures/arrow_marker.png");
   private final BooleanSetting c = new BooleanSetting("Показ дистанции", true);
   private final BooleanSetting d = new BooleanSetting("Только игроки в броне", false);
   private final BooleanSetting e = new BooleanSetting("Выделять цель ауры", true);
   private final BooleanSetting f = new BooleanSetting("Анимировать появление", true);
   private final SliderSetting g = new SliderSetting("Размер стрелки", 10.0F, 4.0F, 30.0F, 1.0F);
   private final SliderSetting h = new SliderSetting("Дистанция от центра", 75.0F, 30.0F, 160.0F, 5.0F);
   private final BooleanSetting i = new BooleanSetting("Отодвигать дальних", false);
   private final BooleanSetting j = new BooleanSetting("Мерцать на дальних", true).a(() -> !this.c.c());
   private final Map<UUID, AnimationUtil> k = new HashMap<>();

   public Arrows() {
      this.a(new Setting[]{this.c, this.d, this.e, this.f, this.g, this.h, this.i, this.j});
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b() && aM_.field_1724 != null && aM_.field_1687 != null) {
         class_1309 aura = Westra.h().d().t().B().s();
         float centerX = aM_.method_22683().method_4486() / 2.0F;
         float centerY = aM_.method_22683().method_4502() / 2.0F;
         float cameraYaw = aM_.field_1773.method_19418().method_19330();
         double camX = aM_.field_1773.method_19418().method_19326().field_1352;
         double camY = aM_.field_1773.method_19418().method_19326().field_1351;
         double camZ = aM_.field_1773.method_19418().method_19326().field_1350;
         class_4587 matrices = event.h();

         for (class_1657 player : aM_.field_1687.method_18456()) {
            if (player != aM_.field_1724 && player.method_5805()) {
               AnimationUtil animation = this.k.computeIfAbsent(player.method_5667(), id -> new AnimationUtil());
               animation.a(0.0F, 1.0F, this.f.c() ? 0.3F : 1.0F, EasingList.g, event.g());
               animation.a(this.q(player));
               float value = animation.c();
               if (!(value <= 0.004F)) {
                  double dx = MathUtil.a(player, event.g()).field_1352 - camX;
                  double dy = MathUtil.a(player, event.g()).field_1351 - camY;
                  double dz = MathUtil.a(player, event.g()).field_1350 - camZ;
                  double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                  double cos = class_3532.method_15362((float)Math.toRadians(cameraYaw));
                  double sin = class_3532.method_15374((float)Math.toRadians(cameraYaw));
                  double rotatedX = -(dz * cos - dx * sin);
                  double rotatedZ = -(dx * cos + dz * sin);
                  double angle = Math.toDegrees(Math.atan2(rotatedX, rotatedZ));
                  float radius = this.h.c() + (this.i.c() ? (float)Math.min(this.h.c().floatValue() * 0.85, Math.max(0.0, distance * 0.65)) : 0.0F);
                  float x = centerX + radius * class_3532.method_15362((float)Math.toRadians(angle));
                  float y = centerY + radius * class_3532.method_15374((float)Math.toRadians(angle));
                  float alpha = value;
                  if (!this.c.c() && this.j.c() && distance > 50.0 && System.currentTimeMillis() % 5000L > 2500L) {
                     alpha = 0.0F;
                  }

                  if (!(alpha <= 0.02F)) {
                     int color = this.r(player, aura);
                     float size = this.g.c() * 2.0F;
                     matrices.method_22903();
                     matrices.method_46416(x, y, 0.0F);
                     matrices.method_22907(class_7833.field_40718.rotationDegrees((float)(angle + 90.0)));
                     event.d().a(matrices, b, -size / 2.0F, -size / 2.0F, size, size, 0.0F, ColorUtil.a(color, alpha));
                     matrices.method_22909();
                     if (this.c.c()) {
                        String text = distance > 100.0 ? Math.round(distance / 10.0) * 10L + "м" : Math.round(distance) + "м";
                        float textWidth = Fonts.e.a(text, 6.5F);
                        Fonts.e.a(matrices, text, x - textWidth / 2.0F, y + size / 2.0F + 1.0F, 6.5F, ColorUtil.a(color, alpha));
                     }
                  }
               }
            }
         }

         this.k.keySet().removeIf(id -> aM_.field_1687.method_18470(id) == null);
      }
   }

   private boolean q(class_1657 player) {
      if (!this.d.c()) {
         return true;
      } else {
         for (class_1304 slot : new class_1304[]{class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166}) {
            if (!player.method_6118(slot).method_7960()) {
               return true;
            }
         }

         return false;
      }
   }

   private int r(class_1657 player, class_1309 aura) {
      if (this.e.c() && aura == player) {
         return ColorUtil.a(255, 80, 80, 255);
      } else {
         return Westra.h().d().e().d(player.method_5477().getString()) ? ColorUtil.a(90, 230, 130, 255) : Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
      }
   }

   @Override
   public void c() {
      this.k.clear();
      super.c();
   }
}

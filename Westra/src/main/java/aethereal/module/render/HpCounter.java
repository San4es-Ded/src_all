package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ColorSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.ProjectUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import org.joml.Vector2f;

@ModuleRegister(
   a = "HP Counter",
   b = "Показывает всплывающие числа урона и лечения над игроками рядом",
   c = Category.Render
)
public class HpCounter extends Module implements Interface {
   private static final class_243[] b = new class_243[]{
      new class_243(-0.36, -0.08, 0.12),
      new class_243(-0.12, 0.1, -0.3),
      new class_243(0.22, -0.14, -0.22),
      new class_243(0.4, 0.04, 0.09),
      new class_243(0.11, 0.16, 0.33),
      new class_243(-0.28, -0.18, 0.27),
      new class_243(0.34, -0.03, -0.12),
      new class_243(-0.05, 0.22, 0.05)
   };
   private final SliderSetting c = new SliderSetting("Время жизни", 900.0F, 300.0F, 2000.0F, 50.0F);
   private final SliderSetting d2 = new SliderSetting("Дистанция", 24.0F, 4.0F, 64.0F, 1.0F);
   private final SliderSetting e = new SliderSetting("Размер", 10.0F, 5.0F, 18.0F, 0.5F);
   private final BooleanSetting f2 = new BooleanSetting("Показывать лечение", true);
   private final BooleanSetting g2 = new BooleanSetting("Тень под текстом", true);
   private final ColorSetting h2 = new ColorSetting("Цвет урона", ColorUtil.a(255, 76, 77, 255));
   private final ColorSetting i2 = new ColorSetting("Цвет лечения", ColorUtil.a(66, 235, 91, 255)).a(() -> this.f2.c());
   private final Map<Integer, Float> j2 = new HashMap<>();
   private final List<HpCounter.a> k2 = new ArrayList<>();
   private int l2;

   public HpCounter() {
      this.a(new Setting[]{this.c, this.d2, this.e, this.f2, this.g2, this.h2, this.i2});
   }

   @Override
   public void c() {
      this.j2.clear();
      this.k2.clear();
      this.l2 = 0;
      super.c();
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1687 != null) {
         long now = System.currentTimeMillis();
         float range = this.d2.c();

         for (class_1657 player : aM_.field_1687.method_18456()) {
            if (player != aM_.field_1724) {
               float health = player.method_6032();
               Float previous = this.j2.put(player.method_5628(), health);
               if (previous != null && !(Math.abs(health - previous) < 0.01F)) {
                  float delta = health - previous;
                  if ((!(delta > 0.0F) || this.f2.c()) && this.q(player) && !(aM_.field_1724.method_5739(player) > range)) {
                     class_243 offset = b[this.l2++ % b.length];
                     class_243 position = player.method_19538()
                        .method_1031(offset.field_1352, player.method_17682() * 0.55 + offset.field_1351, offset.field_1350);
                     this.k2.add(new HpCounter.a(position, Math.abs(delta), delta > 0.0F, now));
                  }
               }
            }
         }

         this.j2.keySet().removeIf(id -> aM_.field_1687.method_8469(id) == null);
         long life = (long)this.c.c().floatValue();
         this.k2.removeIf(number -> now - number.d() >= life);
      } else {
         this.j2.clear();
         this.k2.clear();
      }
   }

   private boolean q(class_1657 player) {
      return player.method_5805() && !player.method_5767();
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b() && !this.k2.isEmpty() && aM_.field_1724 != null) {
         long now = System.currentTimeMillis();
         float life = this.c.c();

         for (HpCounter.a number : new ArrayList<>(this.k2)) {
            float progress = Math.max(0.0F, Math.min(1.0F, (float)(now - number.d()) / life));
            float fade = r(Math.min(1.0F, progress / 0.14F)) * r(Math.min(1.0F, (1.0F - progress) / 0.35F));
            if (!(fade <= 0.004F)) {
               class_243 position = number.a().method_1031(0.0, progress * 0.65, 0.0);
               Vector2f screen = ProjectUtil.a(position.field_1352, position.field_1351, position.field_1350);
               if (ProjectUtil.a(screen)) {
                  double distance = aM_.field_1724.method_33571().method_1022(position);
                  float scale = Math.max(0.38F, Math.min(1.05F, (float)(7.0 / Math.max(0.5, distance))));
                  float size = this.e.c() * scale * (0.88F + 0.12F * fade);
                  String text = (number.c() ? "+" : "-") + s(number.b());
                  float width = Fonts.c.a(text, size);
                  float x = screen.x - width / 2.0F;
                  float y = screen.y;
                  if (this.g2.c()) {
                     Fonts.c.a(event.h(), text, x + 0.7F, y + 0.7F, size, ColorUtil.a(ColorUtil.a(0, 0, 0, 255), 0.55F * fade));
                  }

                  int color = number.c() ? this.i2.c() : this.h2.c();
                  Fonts.c.a(event.h(), text, x, y, size, ColorUtil.a(color, fade));
               }
            }
         }
      }
   }

   private static float r(float value) {
      return value * value * (3.0F - 2.0F * value);
   }

   private static String s(float value) {
      return Math.abs(value - Math.round(value)) < 0.05F ? Integer.toString(Math.round(value)) : String.format(Locale.ROOT, "%.1f", value).replace('.', ',');
   }

   private record a(class_243 a, float b, boolean c, long d) {
   }
}

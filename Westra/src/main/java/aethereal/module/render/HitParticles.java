package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.AttackEvent;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ColorSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.ProjectUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import org.joml.Vector2f;

@ModuleRegister(
   a = "Hit Particles",
   b = "Разбрасывает светящиеся частицы в месте удара по цели",
   c = Category.Render
)
public class HitParticles extends Module implements Interface {
   private static final int b = 400;
   private final SliderSetting c = new SliderSetting("Количество", 12.0F, 3.0F, 56.0F, 1.0F);
   private final SliderSetting d2 = new SliderSetting("Размер", 2.0F, 0.5F, 6.0F, 0.1F);
   private final SliderSetting e = new SliderSetting("Разброс", 1.0F, 0.2F, 3.0F, 0.1F);
   private final SliderSetting f2 = new SliderSetting("Время жизни", 900.0F, 300.0F, 2000.0F, 25.0F);
   private final SliderSetting g2 = new SliderSetting("Начальная скорость", 0.25F, 0.05F, 1.0F, 0.05F);
   private final SliderSetting h2 = new SliderSetting("Гравитация", 0.2F, -0.5F, 1.0F, 0.05F);
   private final ModeSetting i2 = new ModeSetting("Разлёт", "Случайно", "Случайно", "От цели", "К цели", "Закручивание");
   private final BooleanSetting j2 = new BooleanSetting("Свечение", true);
   private final BooleanSetting k2 = new BooleanSetting("Уменьшать со временем", true);
   private final BooleanSetting l2 = new BooleanSetting("Свой цвет", false);
   private final ColorSetting m2 = new ColorSetting("Цвет", ColorUtil.a(255, 255, 255, 255)).a(() -> this.l2.c());
   private final List<HitParticles.a> n2 = new ArrayList<>();
   private final Random o2 = new Random();

   public HitParticles() {
      this.a(new Setting[]{this.c, this.d2, this.e, this.f2, this.g2, this.h2, this.i2, this.j2, this.k2, this.l2, this.m2});
   }

   @Override
   public void c() {
      this.n2.clear();
      super.c();
   }

   @EventTarget
   public void a(AttackEvent event) {
      class_1297 target = event.b();
      if (target != null && aM_.field_1724 != null) {
         class_243 center = target.method_19538().method_1031(0.0, target.method_17682() * 0.55, 0.0);
         int color = this.l2.c() ? this.m2.c() : Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
         int count = (int)this.c.c().floatValue();
         float spread = this.e.c();
         float speed = this.g2.c();
         long now = System.currentTimeMillis();

         for (int index = 0; index < count && this.n2.size() < 400; index++) {
            HitParticles.a particle = new HitParticles.a();
            particle.a = center.method_1031(
               (this.o2.nextDouble() - 0.5) * spread, (this.o2.nextDouble() - 0.5) * spread * 0.6, (this.o2.nextDouble() - 0.5) * spread
            );
            particle.b = this.q(center, particle.a, speed, index, count);
            particle.c = now;
            particle.d = color;
            this.n2.add(particle);
         }
      }
   }

   private class_243 q(class_243 center, class_243 origin, float speed, int index, int count) {
      if (this.i2.l("От цели")) {
         class_243 away = origin.method_1020(center);
         if (away.method_1027() < 1.0E-4) {
            away = new class_243(0.0, 1.0, 0.0);
         }

         return away.method_1029().method_1021(speed);
      } else if (this.i2.l("К цели")) {
         class_243 toward = center.method_1020(origin);
         if (toward.method_1027() < 1.0E-4) {
            toward = new class_243(0.0, -1.0, 0.0);
         }

         return toward.method_1029().method_1021(speed);
      } else if (this.i2.l("Закручивание")) {
         double angle = (double)index / Math.max(1, count) * Math.PI * 2.0;
         return new class_243(Math.cos(angle), 0.35, Math.sin(angle)).method_1021(speed);
      } else {
         return new class_243((this.o2.nextDouble() - 0.5) * 2.0, (this.o2.nextDouble() - 0.2) * 2.0, (this.o2.nextDouble() - 0.5) * 2.0)
            .method_1029()
            .method_1021(speed);
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (!this.n2.isEmpty()) {
         long now = System.currentTimeMillis();
         long life = (long)this.f2.c().floatValue();
         double gravity = this.h2.c().floatValue() * 0.012;
         this.n2.removeIf(particlex -> now - particlex.c >= life);

         for (HitParticles.a particle : this.n2) {
            particle.a = particle.a.method_1019(particle.b);
            particle.b = new class_243(particle.b.field_1352 * 0.92, (particle.b.field_1351 - gravity) * 0.96, particle.b.field_1350 * 0.92);
         }
      }
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b() && !this.n2.isEmpty() && aM_.field_1724 != null) {
         Draw2DProcessor draw = Westra.h().d().i();
         long now = System.currentTimeMillis();
         float life = this.f2.c();
         float size = this.d2.c();

         for (HitParticles.a particle : new ArrayList<>(this.n2)) {
            float progress = Math.max(0.0F, Math.min(1.0F, (float)(now - particle.c) / life));
            float alpha = 1.0F - progress;
            if (!(alpha <= 0.01F)) {
               Vector2f screen = ProjectUtil.a(particle.a.field_1352, particle.a.field_1351, particle.a.field_1350);
               if (ProjectUtil.a(screen)) {
                  double distance = aM_.field_1724.method_33571().method_1022(particle.a);
                  float scale = (float)Math.max(0.3, Math.min(2.5, 6.0 / Math.max(1.0, distance)));
                  float current = size * scale * (this.k2.c() ? alpha : 1.0F);
                  if (!(current <= 0.05F)) {
                     float half = current / 2.0F;
                     if (this.j2.c()) {
                        draw.a(
                           event.h(),
                           screen.x - half,
                           screen.y - half,
                           current,
                           current,
                           half,
                           ColorUtil.a(particle.d, alpha),
                           1.0F,
                           ColorUtil.a(particle.d, alpha),
                           current * 3.0F
                        );
                     } else {
                        draw.a(event.h(), screen.x - half, screen.y - half, current, current, half, ColorUtil.a(particle.d, alpha));
                     }
                  }
               }
            }
         }
      }
   }

   private static final class a {
      class_243 a;
      class_243 b;
      long c;
      int d;
   }
}

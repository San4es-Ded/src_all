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
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ColorSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.ProjectUtil;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import org.joml.Vector2f;

@ModuleRegister(
   a = "Target ESP",
   b = "Обводит текущую цель вращающимся кольцом под ногами",
   c = Category.Render
)
public class TargetESP extends Module implements Interface {
   private final ModeSetting b = new ModeSetting("Стиль", "Кольцо", "Кольцо", "Аура", "Полукруги");
   private final SliderSetting c = new SliderSetting("Радиус", 1.0F, 0.4F, 2.5F, 0.05F);
   private final SliderSetting d2 = new SliderSetting("Высота", 0.05F, 0.0F, 2.5F, 0.05F);
   private final SliderSetting e = new SliderSetting("Скорость", 2.0F, 0.2F, 6.0F, 0.1F);
   private final SliderSetting f2 = new SliderSetting("Толщина", 1.75F, 0.5F, 4.0F, 0.05F);
   private final SliderSetting g2 = new SliderSetting("Точек", 64.0F, 16.0F, 128.0F, 1.0F);
   private final BooleanSetting h2 = new BooleanSetting("Только после удара", false);
   private final BooleanSetting i2 = new BooleanSetting("Свой цвет", false);
   private final ColorSetting j2 = new ColorSetting("Цвет", ColorUtil.a(255, 255, 255, 255)).a(() -> this.i2.c());
   private final AnimationUtil k2 = new AnimationUtil();
   private class_1309 l2;
   private long m2;

   public TargetESP() {
      this.a(new Setting[]{this.b, this.c, this.d2, this.e, this.f2, this.g2, this.h2, this.i2, this.j2});
   }

   @Override
   public void c() {
      this.l2 = null;
      super.c();
   }

   @EventTarget
   public void a(AttackEvent event) {
      if (event.b() instanceof class_1309 target) {
         this.l2 = target;
         this.m2 = System.currentTimeMillis();
      }
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b() && aM_.field_1724 != null && aM_.field_1687 != null) {
         class_1309 target = this.q();
         this.k2.a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
         this.k2.a(target != null);
         float animation = this.k2.c();
         if (target != null && !(animation <= 0.004F)) {
            Draw2DProcessor draw = Westra.h().d().i();
            int color = this.i2.c() ? this.j2.c() : Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
            double x = target.field_6014 + (target.method_23317() - target.field_6014) * event.g();
            double y = target.field_6036 + (target.method_23318() - target.field_6036) * event.g();
            double z = target.field_5969 + (target.method_23321() - target.field_5969) * event.g();
            y += this.d2.c().floatValue();
            double radius = this.c.c().floatValue();
            int points = (int)this.g2.c().floatValue();
            float thickness = this.f2.c();
            double phase = System.currentTimeMillis() % 10000L / 1000.0 * this.e.c().floatValue();
            boolean crescents = this.b.l("Полукруги");
            boolean aura = this.b.l("Аура");

            for (int index = 0; index < points; index++) {
               double angle = (double)index / points * Math.PI * 2.0 + phase;
               double px = x + Math.cos(angle) * radius;
               double pz = z + Math.sin(angle) * radius;
               Vector2f screen = ProjectUtil.a(px, y, pz);
               if (ProjectUtil.a(screen)) {
                  float head = (float)((double)index / points);
                  float alpha;
                  if (!crescents) {
                     if (aura) {
                        alpha = 0.85F;
                     } else {
                        alpha = 0.15F + 0.85F * head;
                     }
                  } else {
                     double local = (angle % (Math.PI * 2) + (Math.PI * 2)) % (Math.PI * 2);
                     boolean visible = local < Math.PI * 2.0 / 5.0 || local > Math.PI && local < Math.PI * 7.0 / 5.0;
                     alpha = visible ? 1.0F : 0.0F;
                  }

                  if (!(alpha <= 0.01F)) {
                     double distance = aM_.field_1724.method_33571().method_1022(new class_243(px, y, pz));
                     float size = thickness * (float)Math.max(0.35, Math.min(2.0, 6.0 / Math.max(1.0, distance)));
                     float half = size / 2.0F;
                     if (aura) {
                        draw.a(
                           event.h(),
                           screen.x - half,
                           screen.y - half,
                           size,
                           size,
                           half,
                           ColorUtil.a(color, alpha * animation),
                           1.0F,
                           ColorUtil.a(color, alpha * animation),
                           size * 3.0F
                        );
                     } else {
                        draw.a(event.h(), screen.x - half, screen.y - half, size, size, half, ColorUtil.a(color, alpha * animation));
                     }
                  }
               }
            }
         }
      }
   }

   private class_1309 q() {
      if (this.h2.c()) {
         return this.l2 != null && this.l2.method_5805() && System.currentTimeMillis() - this.m2 <= 4000L ? this.l2 : null;
      } else {
         class_1309 aura = Westra.h().d().t().B().s();
         if (aura == null) {
            aura = Westra.h().d().t().X().s();
         }

         if (aura != null && aura.method_5805()) {
            return aura;
         } else {
            return this.l2 != null && this.l2.method_5805() && System.currentTimeMillis() - this.m2 <= 3000L ? this.l2 : null;
         }
      }
   }
}

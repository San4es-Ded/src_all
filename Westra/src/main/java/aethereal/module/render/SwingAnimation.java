package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.HandAnimationEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_4587;
import net.minecraft.class_7833;

@ModuleRegister(
   a = "Swing Animation",
   b = "Настраивает анимацию взмаха руки",
   c = Category.Render
)
public class SwingAnimation extends Module {
   private final BooleanSetting b = new BooleanSetting("Учитывать включённую Aura", true);
   private final ModeSetting c = new ModeSetting("Режим анимации", "Мод 1", "Мод 1", "Мод 2", "Мод 3", "Мод 4", "Мод 5");
   private final SliderSetting d = new SliderSetting("Угол поворота", 75.0F, 0.0F, 360.0F, 1.0F).a(() -> this.c.l("Мод 1"));
   private final SliderSetting e = new SliderSetting("Наклон кончика", -20.0F, -90.0F, 90.0F, 1.0F).a(() -> !this.c.l("Мод 5"));
   private final SliderSetting f = new SliderSetting("Интенсивность взмаха", 5.0F, 1.0F, 10.0F, 1.0F);

   @Generated
   public BooleanSetting q() {
      return this.b;
   }

   @Generated
   public ModeSetting r() {
      return this.c;
   }

   @Generated
   public SliderSetting s() {
      return this.d;
   }

   @Generated
   public SliderSetting t() {
      return this.e;
   }

   @Generated
   public SliderSetting u() {
      return this.f;
   }

   public SwingAnimation() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e, this.f});
   }

   @EventTarget
   public void a(HandAnimationEvent event) {
      if ((!this.b.c() || Westra.h().d().t().B().s() != null) && event.c() == class_1268.field_5808) {
         class_4587 matrices = event.b();
         float anim = (float)Math.sin(event.d() * 3.1415936112270124);
         float power = this.f.c() * 10.0F;
         int arm = event.e();
         matrices.method_46416(arm * (this.c.l("Мод 5") ? 0.5F : 0.72F), -0.5F, this.c.l("Мод 5") ? -0.72F : -1.0F);
         if (!this.c.l("Мод 5")) {
            matrices.method_22907(class_7833.field_40714.rotationDegrees(-this.e.c()));
         }

         String var6 = this.c.c();
         switch (var6) {
            case "Мод 1":
               matrices.method_22907(class_7833.field_40716.rotationDegrees(arm * 90));
               matrices.method_22907(class_7833.field_40718.rotationDegrees(arm * -70));
               matrices.method_22907(class_7833.field_40714.rotationDegrees(-this.d.c() - power * anim));
               break;
            case "Мод 2":
               matrices.method_22907(class_7833.field_40716.rotationDegrees(arm * 90));
               matrices.method_22907(class_7833.field_40718.rotationDegrees(arm * -65));
               matrices.method_22907(class_7833.field_40714.rotationDegrees(-65.0F + power * anim));
               break;
            case "Мод 3":
               matrices.method_22907(class_7833.field_40716.rotationDegrees(arm * -90));
               matrices.method_22907(class_7833.field_40718.rotationDegrees(arm * 60));
               matrices.method_22907(class_7833.field_40714.rotationDegrees(30.0F));
               matrices.method_22907(class_7833.field_40718.rotationDegrees(arm * power * anim));
               break;
            case "Мод 4":
               matrices.method_22907(class_7833.field_40716.rotationDegrees(arm * 90));
               matrices.method_22907(class_7833.field_40718.rotationDegrees(arm * -75));
               matrices.method_22907(class_7833.field_40714.rotationDegrees(-45.0F - power * anim));
               matrices.method_22907(class_7833.field_40716.rotationDegrees(arm * power * anim * 0.5F));
               break;
            case "Мод 5":
               float strength = power / 80.0F;
               float swing = anim * anim;
               float twist = (float)Math.sin(event.d() * event.d() * 3.1415936112270124);
               matrices.method_22907(class_7833.field_40716.rotationDegrees(arm * (45.0F + twist * -20.0F * strength)));
               matrices.method_22907(class_7833.field_40718.rotationDegrees(arm * swing * -22.0F * strength));
               matrices.method_22907(class_7833.field_40714.rotationDegrees(swing * -85.0F * strength));
               matrices.method_22907(class_7833.field_40716.rotationDegrees(arm * -45.0F));
         }

         event.a(true);
      }
   }
}

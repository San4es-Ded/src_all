package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import lombok.Generated;
import net.minecraft.class_2261;
import net.minecraft.class_2397;
import net.minecraft.class_2680;

@ModuleRegister(
   a = "Optimization",
   b = "Снижает нагрузку на видеокарту ради стабильных кадров",
   c = Category.Misc
)
public class Optimization extends Module {
   private final ModeSetting b = new ModeSetting("Качество размытия", "Высокое", "Высокое", "Среднее", "Низкое").a(mode -> w());
   private final BooleanSetting c = new BooleanSetting("Ограничить кадры вне игры", true);
   private final SliderSetting d = new SliderSetting("Кадры без фокуса окна", 30.0F, 5.0F, 120.0F, 5.0F).a(() -> this.c.c());
   private final SliderSetting e = new SliderSetting("Кадры в меню клиента", 120.0F, 30.0F, 260.0F, 10.0F).a(() -> this.c.c());
   private final BooleanSetting f = new BooleanSetting("Лимит частиц", true);
   private final SliderSetting g = new SliderSetting("Максимум частиц", 600.0F, 50.0F, 4000.0F, 50.0F).a(() -> this.f.c());
   private final MultiModeSetting j = new MultiModeSetting(
         "Не отрисовывать блоки", new BooleanSetting("Траву и цветы", false), new BooleanSetting("Листву", false)
      )
      .a(value -> w());
   private final BooleanSetting h = new BooleanSetting("Ограничить дальность сущностей", false);
   private final SliderSetting i = new SliderSetting("Дальность сущностей", 64.0F, 8.0F, 128.0F, 4.0F).a(() -> this.h.c());

   @Generated
   public ModeSetting q() {
      return this.b;
   }

   public Optimization() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e, this.f, this.g, this.j, this.h, this.i});
   }

   @Override
   public void b() {
      super.b();
      w();
   }

   @Override
   public void c() {
      super.c();
      w();
   }

   private static void w() {
      if (aM_.method_22683() != null) {
         Westra.h().d().i().e().g();
         v();
      }
   }

   private static void v() {
      if (aM_.field_1769 != null) {
         aM_.field_1769.method_3279();
      }
   }

   public boolean a(class_2680 state) {
      if (!this.m()) {
         return false;
      } else {
         return state.method_26204() instanceof class_2261 ? this.a("Траву и цветы") : state.method_26204() instanceof class_2397 && this.a("Листву");
      }
   }

   private boolean a(String option) {
      BooleanSetting setting = this.j.a(option);
      return setting != null && setting.c();
   }

   public int r() {
      if (!this.m()) {
         return 4;
      } else if (this.b.l("Низкое")) {
         return 2;
      } else {
         return this.b.l("Среднее") ? 3 : 4;
      }
   }

   public int s() {
      if (this.m() && this.c.c()) {
         return aM_.method_1569() ? Math.round(this.e.c()) : Math.round(this.d.c());
      } else {
         return -1;
      }
   }

   public int t() {
      return this.m() && this.f.c() ? Math.round(this.g.c()) : -1;
   }

   public double u() {
      if (this.m() && this.h.c()) {
         float distance = this.i.c();
         return distance * distance;
      } else {
         return -1.0;
      }
   }
}

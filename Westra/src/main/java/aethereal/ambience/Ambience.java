package aethereal.ambience;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.AmbienceEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ColorSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import net.minecraft.class_3532;
import net.minecraft.class_5636;
import net.minecraft.class_6854;
import net.minecraft.class_1959.class_1963;

@ModuleRegister(
   a = "Ambience",
   b = "Настраивает атмосферу и окружение игрового мира",
   c = Category.Render
)
public class Ambience extends Module {
   private final ModeSetting c = new ModeSetting("Время в мире", "Без изменений", "Без изменений", "Рассвет", "День", "Полдень", "Закат", "Ночь", "Полночь");
   public final ModeSetting b = new ModeSetting("Погода в мире", "Без изменений", "Без изменений", "Солнечно", "Дождь", "Снег", "Гроза");
   private final BooleanSetting d = new BooleanSetting("Пользовательский туман", false);
   private final BooleanSetting h = new BooleanSetting("Скрыть облака", false);
   private final ColorSetting e;
   private final SliderSetting f;
   private final SliderSetting g;

   public Ambience() {
      ColorSetting colorSetting = new ColorSetting("Цвет тумана", ColorUtil.a(255, 255, 255));
      BooleanSetting booleanSetting = this.d;
      this.e = colorSetting.a(booleanSetting::h);
      SliderSetting sliderSetting = new SliderSetting("Дальность тумана", -8.0F, -8.0F, 25.0F, 1.0F);
      BooleanSetting booleanSetting2 = this.d;
      this.f = sliderSetting.a(booleanSetting2::h);
      SliderSetting sliderSetting2 = new SliderSetting("Плотность тумана", 100.0F, 0.0F, 100.0F, 1.0F);
      BooleanSetting booleanSetting3 = this.d;
      this.g = sliderSetting2.a(booleanSetting3::h);
      this.a(new Setting[]{this.c, this.b, this.d, this.h, this.e, this.f, this.g});
   }

   public boolean q() {
      return this.m() && this.h.c();
   }

   @EventTarget
   public void a(AmbienceEvent.c event) {
      String var4 = this.c.c();

      event.a(switch (var4) {
         case "Рассвет" -> 23041L;
         case "День" -> 1000L;
         case "Полдень" -> 6000L;
         case "Закат" -> 12610L;
         case "Ночь" -> 13000L;
         case "Полночь" -> 18000L;
         default -> event.b();
      });
   }

   @EventTarget
   public void a(AmbienceEvent.a event) {
      if (this.d.c()) {
         float[] rgba = ColorUtil.a(this.e.c());
         event.a(rgba[0]);
         event.b(rgba[1]);
         event.c(rgba[2]);
         event.d(1.0F);
         event.a(true);
      }
   }

   @EventTarget
   public void a(AmbienceEvent.b event) {
      if (this.d.c()) {
         class_6854 shape = event.d().comp_3011();
         if (event.b().method_19334() == class_5636.field_27888) {
            shape = class_6854.field_36350;
         }

         float[] rgba = ColorUtil.a(this.e.c());
         event.a(
            class_3532.method_15363(this.f.c(), -8.0F, event.c()),
            class_3532.method_15363(this.f.c() + this.g.c(), 0.0F, event.c()),
            shape,
            rgba[0],
            rgba[1],
            rgba[2],
            1.0F
         );
      }
   }

   @EventTarget
   public void a(AmbienceEvent.d event) {
      switch (event.b()) {
         case RAIN_GRADIENT:
            String var6 = this.b.c();

            event.a(switch (var6) {
               case "Солнечно" -> 0.0F;
               case "Дождь", "Гроза" -> 1.0F;
               case "Снег" -> 0.9F;
               default -> event.c();
            });
            break;
         case THUNDER_GRADIENT:
            String var4 = this.b.c();

            event.a(switch (var4) {
               case "Солнечно", "Дождь", "Снег" -> 0.0F;
               case "Гроза" -> 1.0F;
               default -> event.c();
            });
            break;
         case PRECIPITATION_PARTICLES:
            if (this.b.l("Снег")) {
               event.a(0.0F);
            }
            break;
         case PRECIPITATION:
            if (this.b.l("Снег")) {
               event.a(class_1963.field_9383);
            }
      }
   }
}

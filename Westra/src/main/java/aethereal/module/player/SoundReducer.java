package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.SoundEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;

@ModuleRegister(
   a = "Sound Reducer",
   b = "Уменьшает громкость выбранных игровых звуков",
   c = Category.Player
)
public class SoundReducer extends Module {
   private final ModeSetting b = new ModeSetting("Способ обработки звуков", "Приглушение", "Приглушение", "Отключение");
   private final SliderSetting c = new SliderSetting("Громкость производящего звука", 0.5F, 0.0F, 1.0F, 0.01F).a(() -> this.b.l("Приглушение"));
   private final MultiModeSetting d = new MultiModeSetting(
      "Выберите звуки",
      new BooleanSetting("Бросание зелей/опыта", true),
      new BooleanSetting("Взрыв Крипера", false),
      new BooleanSetting("Взрыв TNT", false),
      new BooleanSetting("Наковальни", true),
      new BooleanSetting("Выстрел лука", false),
      new BooleanSetting("Крик Гаста", false),
      new BooleanSetting("Шаги скелета", false),
      new BooleanSetting("Звук портала в ад", false),
      new BooleanSetting("Ивента", true),
      new BooleanSetting("Иссушения", true),
      new BooleanSetting("Фантомы", true),
      new BooleanSetting("Трезубца", true),
      new BooleanSetting("Тотема", false),
      new BooleanSetting("Вардена", false),
      new BooleanSetting("Скалк Сенсор", false)
   );

   public SoundReducer() {
      this.a(new Setting[]{this.b, this.d, this.c});
   }

   @EventTarget
   public void a(SoundEvent event) {
      if (this.a(event.b().method_4775().method_12832())) {
         event.a(this.b.l("Отключение") ? 0.0F : event.c() * this.c.c());
      }
   }

   public boolean a(String path) {
      return this.d.a("Бросание зелей/опыта").c() && (path.contains("splash_potion") || path.contains("experience_orb") || path.contains("lingering_potion"))
         || this.d.a("Взрыв Крипера").c() && (path.contains("creeper") || path.contains("generic") && path.contains("explode"))
         || this.d.a("Взрыв TNT").c() && (path.contains("tnt") || path.contains("generic") && path.contains("explode"))
         || this.d.a("Наковальни").c() && path.contains("anvil")
         || this.d.a("Выстрел лука").c() && (path.contains("arrow") || path.contains("bow"))
         || this.d.a("Крик Гаста").c() && path.contains("ghast")
         || this.d.a("Шаги скелета").c() && path.contains("skeleton") && !path.contains("skeleton_horse")
         || this.d.a("Звук портала в ад").c() && path.contains("portal")
         || this.d.a("Ивента").c() && path.contains("entity.ender_dragon.ambient")
         || this.d.a("Иссушения").c() && path.contains("wither") && !path.contains("wither_skeleton")
         || this.d.a("Фантомы").c() && path.contains("phantom")
         || this.d.a("Трезубца").c() && path.contains("trident")
         || this.d.a("Тотема").c() && path.contains("totem")
         || this.d.a("Вардена").c() && path.contains("warden")
         || this.d.a("Скалк Сенсор").c() && path.contains("sculk");
   }
}

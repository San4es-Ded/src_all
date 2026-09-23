package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.emotion.EmotionPlayback;
import aethereal.event.KeyEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.ui.screen.EmotionWheelScreen;
import net.minecraft.class_5498;

@ModuleRegister(
   a = "Emotions",
   b = "Колесо эмоций: поза проигрывается на вашей модели от третьего лица",
   c = Category.Render
)
public class Emotions extends Module implements Interface {
   private final BindSetting b = new BindSetting("Клавиша колеса", -1);
   private final SliderSetting c = new SliderSetting("Скорость", 1.0F, 0.25F, 3.0F, 0.05F);
   private final BooleanSetting d2 = new BooleanSetting("Повторять", false);
   private final BooleanSetting e = new BooleanSetting("Прерывать при движении", true);
   private final BooleanSetting f2 = new BooleanSetting("Вид от третьего лица", true);
   private class_5498 g2;

   public Emotions() {
      this.a(new Setting[]{this.b, this.c, this.d2, this.e, this.f2});
   }

   @Override
   public void c() {
      this.q();
      super.c();
   }

   @EventTarget
   public void a(KeyEvent event) {
      if (event.d() != 0 && aM_.field_1724 != null && aM_.field_1755 == null) {
         int key = this.b.c();
         if (key != -1 && event.b() == key) {
            aM_.method_1507(new EmotionWheelScreen());
         }
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      EmotionPlayback.a(this.c.c());
      EmotionPlayback.a(this.d2.c());
      boolean wasPlaying = EmotionPlayback.b();
      if (wasPlaying && this.e.c() && EmotionPlayback.f()) {
         EmotionPlayback.a();
      }

      EmotionPlayback.e();
      if (wasPlaying && !EmotionPlayback.b()) {
         this.q();
      }
   }

   public void r() {
      if (this.f2.c() && aM_.field_1690 != null) {
         if (this.g2 == null && aM_.field_1690.method_31044() == class_5498.field_26664) {
            this.g2 = class_5498.field_26664;
            aM_.field_1690.method_31043(class_5498.field_26665);
         }
      }
   }

   private void q() {
      if (this.g2 != null && aM_.field_1690 != null) {
         aM_.field_1690.method_31043(this.g2);
         this.g2 = null;
      }
   }
}

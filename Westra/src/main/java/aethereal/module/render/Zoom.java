package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.DrawEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.EasingList;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;

@ModuleRegister(
   a = "Zoom",
   b = "Приближает вид по нажатию клавиши",
   c = Category.Render
)
public class Zoom extends Module {
   private final SliderSetting b = new SliderSetting("Кратность", 3.0F, 1.5F, 10.0F, 0.5F);
   private final SliderSetting c = new SliderSetting("Скорость перехода", 0.35F, 0.05F, 1.0F, 0.05F);
   private final BooleanSetting d = new BooleanSetting("Плавная мышь", true);
   private final AnimationUtil e = new AnimationUtil();
   private boolean f;
   private final BindSetting g = new BindSetting("Клавиша приближения", -1, 0).a(() -> this.f = true).b(() -> this.f = false);

   public Zoom() {
      this.a(new Setting[]{this.g, this.b, this.c, this.d});
   }

   @Override
   public void c() {
      super.c();
      this.f = false;
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b()) {
         this.e.a(0.0F, 1.0F, this.c.c(), EasingList.g, event.g());
         this.e.a(this.f);
      }
   }

   public double q() {
      float value = this.e.c();
      return value <= 0.0F ? 1.0 : 1.0 + (this.b.c().floatValue() - 1.0) * value;
   }

   public boolean r() {
      return this.m() && this.d.c() && this.e.c() > 0.0F;
   }

   public float s() {
      return this.e.c();
   }
}

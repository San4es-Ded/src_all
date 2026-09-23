package aethereal.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.util.MathUtil;
import lombok.Generated;
import net.minecraft.class_408;
import net.minecraft.class_490;
import net.minecraft.class_5498;

@ModuleRegister(
   a = "Animations",
   b = "Анимирует выбранные элементы игры",
   c = Category.Render
)
public class Animations extends Module {
   private final MultiModeSetting b = new MultiModeSetting(
      "Выберите что анимировать",
      new BooleanSetting("TAB", true),
      new BooleanSetting("Открытие инвентаря", true),
      new BooleanSetting("Смена перспективы", true),
      new BooleanSetting("Поднятие хотбара", true),
      new BooleanSetting("Слот хотбара", true),
      new BooleanSetting("Появление сообщений", true),
      new BooleanSetting("Предметы", true)
   );
   private final AnimationUtil c = new AnimationUtil();
   private final AnimationUtil d = new AnimationUtil();
   private final AnimationUtil e = new AnimationUtil();
   private final AnimationUtil f = new AnimationUtil();
   private float g = -1.0F;

   @Generated
   public MultiModeSetting q() {
      return this.b;
   }

   @Generated
   public AnimationUtil r() {
      return this.c;
   }

   @Generated
   public AnimationUtil s() {
      return this.d;
   }

   @Generated
   public AnimationUtil t() {
      return this.e;
   }

   @Generated
   public AnimationUtil u() {
      return this.f;
   }

   @Generated
   public float v() {
      return this.g;
   }

   public Animations() {
      this.a(new Setting[]{this.b});
   }

   @Override
   public void c() {
      super.c();
      this.g = -1.0F;
   }

   @EventTarget
   public void a(DrawEvent event) {
      this.c.a(0.0F, 1.0F, 0.5F, EasingList.g, event.g());
      this.d.a(0.0F, 1.0F, 0.45F, EasingList.g, event.g());
      this.e.a(0.0F, 1.0F, 0.4F, EasingList.g, event.g());
      this.f.a(0.0F, 1.0F, 0.35F, EasingList.g, event.g());
      this.g = this.g < 0.0F ? aM_.field_1724.method_31548().field_7545 : MathUtil.c(this.g, aM_.field_1724.method_31548().field_7545, 1.25F);
   }

   @EventTarget
   public void a(TickEvent event) {
      this.e.a(aM_.field_1755 instanceof class_490);
      this.f.a(aM_.field_1690.method_31044() != class_5498.field_26664);
      this.d.a(aM_.field_1755 instanceof class_408);
   }
}

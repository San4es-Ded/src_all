package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_3532;

@ModuleRegister(
   a = "Transparent Players",
   b = "Делает игроков прозрачнее по мере приближения к ним",
   c = Category.Render
)
public class TransparentPlayers extends Module implements Interface {
   private final SliderSetting b = new SliderSetting("Дистанция", 8.0F, 2.0F, 32.0F, 0.5F);
   private final Map<class_1309, Float> smoothed = new WeakHashMap<>();

   public TransparentPlayers() {
      this.a(new Setting[]{this.b});
   }

   @Override
   public void c() {
      super.c();
      this.smoothed.clear();
   }

   public float a(class_1309 entity, float tickDelta) {
      if (this.m() && aM_.field_1724 != null && entity != aM_.field_1724 && entity instanceof class_1657) {
         double range = this.b.c().floatValue();
         double distance = aM_.field_1724.method_30950(tickDelta).method_1022(entity.method_30950(tickDelta));
         float target = 1.0F;
         if (distance <= range) {
            float t = class_3532.method_15363((float)(distance / range), 0.0F, 1.0F);
            t = t * t * (3.0F - 2.0F * t);
            target = class_3532.method_15363(t * 0.99F, 0.0F, 0.99F);
         }

         Float previous = this.smoothed.get(entity);
         float alpha = target;
         if (previous != null) {
            float factor = class_3532.method_15363(1.0F - (float)Math.exp(-0.2F * aM_.method_61966().method_60636()), 0.0F, 1.0F);
            alpha = class_3532.method_15363(class_3532.method_16439(factor, previous, target), 0.0F, 1.0F);
         }

         this.smoothed.put(entity, alpha);
         return alpha;
      } else {
         return 1.0F;
      }
   }
}

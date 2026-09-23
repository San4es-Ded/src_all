package aethereal.ui.widget;

import aethereal.core.Westra;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.util.MathUtil;
import java.util.List;
import net.minecraft.class_4587;
import net.minecraft.class_7833;

public class EffectMarker {
   private EffectMarker() {
   }

   public static void a(List<EffectMarker.a> list, float x, float y) {
      if (list != null) {
         list.add(new EffectMarker.a(x, y));
      }
   }

   public static void a(class_4587 matrices, float partialTicks, List<EffectMarker.a> list) {
      if (list != null && !list.isEmpty()) {
         for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).a(matrices, partialTicks)) {
               list.remove(i);
            }
         }
      }
   }

   public static final class a {
      private final AnimationUtil a = new AnimationUtil();
      private final long b = System.nanoTime() + 300000000L;
      private final float c;
      private final float d;
      private boolean e;

      a(float x, float y) {
         this.c = x;
         this.d = y;
      }

      boolean a(class_4587 matrices, float partialTicks) {
         this.a.a(0.0F, 1.0F, 0.2F, EasingList.h, partialTicks);
         if (!this.e && System.nanoTime() >= this.b) {
            this.e = true;
         }

         this.a.a(!this.e);
         float progress = MathUtil.b(this.a.c(), 0.0F, 1.0F);
         float scale = this.e ? progress : this.a(progress);
         float length = 4.0F * Math.max(1.0E-4F, scale);
         int color = ColorUtil.a(255, 255, 255, Math.round(250.0F * progress));
         matrices.method_22903();
         matrices.method_46416(this.c, this.d, 0.0F);

         for (int i = 0; i < 4; i++) {
            this.a(matrices, length, 45.0F + 90.0F * i, length, color);
         }

         matrices.method_22909();
         return this.e && progress <= 0.01F;
      }

      private void a(class_4587 matrices, float length, float angleDeg, float offset, int color) {
         matrices.method_22903();
         matrices.method_22907(class_7833.field_40718.rotationDegrees(angleDeg));
         matrices.method_46416(offset, 0.0F, 0.0F);
         Westra.h().d().i().a(matrices, -length / 2.0F, -0.25F, length, 0.5F, 0.0F, color);
         matrices.method_22909();
      }

      private float a(float scale) {
         if (scale <= 0.0F) {
            return 0.0F;
         } else if (scale < 0.6F) {
            return scale / 0.6F;
         } else {
            return scale < 0.8F ? 1.0F + (scale - 0.6F) / 0.5F * 0.5F : 1.2F - (scale - 0.8F) / 0.2F * 0.2F;
         }
      }
   }
}

package aethereal.cosmetic;

import aethereal.core.Interface;
import aethereal.core.Westra;
import java.util.UUID;
import net.minecraft.class_10055;
import net.minecraft.class_1297;
import net.minecraft.class_3883;
import net.minecraft.class_3887;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_591;

public class CosmeticsRenderer extends class_3887<class_10055, class_591> implements Interface {
   public CosmeticsRenderer(class_3883<class_10055, class_591> context) {
      super(context);
   }

   public void render(class_4587 matrices, class_4597 buffers, int light, class_10055 state, float limbAngle, float limbDistance) {
      class_1297 entity = aM_.field_1687.method_8469(state.field_53528);
      if (entity != null) {
         UUID uuid = entity.method_5667();
         float tickDelta = aM_.method_61966().method_60637(false);

         for (Cosmetic cosmetic : Westra.h().d().r().getCosmetics()) {
            if (uuid.equals(cosmetic.getUuid()) && cosmetic.getType() == CosmeticsType.COSMETIC) {
               matrices.method_22903();
               cosmetic.getCategory().transform(matrices, (class_591)this.method_17165(), cosmetic);
               cosmetic.getRenderer().render(matrices, cosmetic, buffers, null, null, light, tickDelta);
               matrices.method_22909();
            }
         }
      }
   }
}

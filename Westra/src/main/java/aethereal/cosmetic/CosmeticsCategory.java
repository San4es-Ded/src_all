package aethereal.cosmetic;

import java.util.function.Function;
import net.minecraft.class_4587;
import net.minecraft.class_591;
import net.minecraft.class_630;
import net.minecraft.class_7833;

public enum CosmeticsCategory {
   f1(model -> model.field_3391),
   f2(model2 -> model2.field_3391),
   f3_(model3 -> model3.field_3398),
   f4(model4 -> model4.field_3398),
   f5(model5 -> model5.field_3391),
   f6(model6 -> model6.field_3391);

   private final Function<class_591, class_630> part;

   private CosmeticsCategory(Function<class_591, class_630> part) {
      this.part = part;
   }

   public void transform(class_4587 matrices, class_591 model, Cosmetic cosmetic) {
      this.part.apply(model).method_22703(matrices);
      matrices.method_22907(class_7833.field_40718.rotationDegrees(180.0F));
      matrices.method_22905(cosmetic.getScale(), cosmetic.getScale(), cosmetic.getScale());
      matrices.method_46416(cosmetic.getOffset().x, cosmetic.getOffset().y, cosmetic.getOffset().z());
   }
}

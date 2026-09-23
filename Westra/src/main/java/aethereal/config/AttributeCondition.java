package aethereal.config;

import lombok.Generated;
import net.minecraft.class_1320;
import net.minecraft.class_1322;
import net.minecraft.class_1799;
import net.minecraft.class_6880;
import net.minecraft.class_9285;
import net.minecraft.class_9334;
import net.minecraft.class_1322.class_1323;
import net.minecraft.class_9285.class_9287;

public class AttributeCondition {
   private final class_6880<class_1320> a;
   private final class_1323 b;
   private final double c;

   @Generated
   public class_6880<class_1320> a() {
      return this.a;
   }

   @Generated
   public class_1323 b() {
      return this.b;
   }

   @Generated
   public double c() {
      return this.c;
   }

   public AttributeCondition(class_6880<class_1320> attribute, double expectedAmount, class_1323 expectedOperation) {
      if (attribute == null) {
         throw new IllegalArgumentException("Attribute cannot be null");
      } else {
         this.a = attribute;
         this.c = expectedAmount;
         this.b = expectedOperation;
      }
   }

   public boolean a(class_6880<class_1320> currentAttr, class_1322 modifier) {
      return currentAttr.equals(this.a) && Math.abs(modifier.comp_2449() - this.c) < 9.9999942618434E-4 && modifier.comp_2450() == this.b;
   }

   public boolean a(class_1799 stack) {
      class_9285 modifiersComponent = (class_9285)stack.method_57825(class_9334.field_49636, class_9285.field_49326);

      for (class_9287 entry : modifiersComponent.comp_2393()) {
         if (this.a(entry.comp_2395(), entry.comp_2396())) {
            return true;
         }
      }

      return false;
   }
}

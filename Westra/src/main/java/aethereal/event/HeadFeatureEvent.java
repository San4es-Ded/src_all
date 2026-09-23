package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_1657;
import net.minecraft.class_3882;
import net.minecraft.class_4587;
import net.minecraft.class_4597;

public class HeadFeatureEvent extends Event implements IEvent {
   private class_4587 a;
   private class_4597 b;
   private class_1657 c;
   private class_3882 d;

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof HeadFeatureEvent other)) {
         return false;
      } else if (other.a(this) && super.equals(o)) {
         Object this$matrix = this.b();
         Object other$matrix = other.b();
         if (this$matrix == null) {
            if (other$matrix != null) {
               return false;
            }
         } else if (!this$matrix.equals(other$matrix)) {
            return false;
         }

         Object this$vertexConsumerProvider = this.c();
         Object other$vertexConsumerProvider = other.c();
         if (this$vertexConsumerProvider == null) {
            if (other$vertexConsumerProvider != null) {
               return false;
            }
         } else if (!this$vertexConsumerProvider.equals(other$vertexConsumerProvider)) {
            return false;
         }

         Object this$player = this.d();
         Object other$player = other.d();
         if (this$player == null) {
            if (other$player != null) {
               return false;
            }
         } else if (!this$player.equals(other$player)) {
            return false;
         }

         Object this$model = this.e();
         Object other$model = other.e();
         return this$model == null ? other$model == null : this$model.equals(other$model);
      } else {
         return false;
      }
   }

   @Generated
   protected boolean a(Object other) {
      return other instanceof HeadFeatureEvent;
   }

   @Generated
   @Override
   public int hashCode() {
      int result = super.hashCode();
      Object $matrix = this.b();
      int result2 = result * 59 + ($matrix == null ? 43 : $matrix.hashCode());
      Object $vertexConsumerProvider = this.c();
      int result3 = result2 * 59 + ($vertexConsumerProvider == null ? 43 : $vertexConsumerProvider.hashCode());
      Object $player = this.d();
      int result4 = result3 * 59 + ($player == null ? 43 : $player.hashCode());
      Object $model = this.e();
      return result4 * 59 + ($model == null ? 43 : $model.hashCode());
   }

   @Generated
   public HeadFeatureEvent(class_4587 matrix, class_4597 vertexConsumerProvider, class_1657 player, class_3882 model) {
      this.a = matrix;
      this.b = vertexConsumerProvider;
      this.c = player;
      this.d = model;
   }

   @Generated
   public void a(class_4587 matrix) {
      this.a = matrix;
   }

   @Generated
   public void a(class_4597 vertexConsumerProvider) {
      this.b = vertexConsumerProvider;
   }

   @Generated
   public void a(class_1657 player) {
      this.c = player;
   }

   @Generated
   public void a(class_3882 model) {
      this.d = model;
   }

   @Generated
   @Override
   public String toString() {
      return "HeadFeatureEvent(matrix=" + this.b() + ", vertexConsumerProvider=" + this.c() + ", player=" + this.d() + ", model=" + this.e() + ")";
   }

   @Generated
   public class_4587 b() {
      return this.a;
   }

   @Generated
   public class_4597 c() {
      return this.b;
   }

   @Generated
   public class_1657 d() {
      return this.c;
   }

   @Generated
   public class_3882 e() {
      return this.d;
   }
}

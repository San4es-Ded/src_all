package rockstar.client.internal.core;





import rockstar.client.rotation.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;

public final class PathExecutor {
   private final ComputedPath internalField0905;
   @Nullable
   private final PathGoal internalField0925;
   private int internalField0227 = 0;
   private double internalField0193;
   private double internalField1045;
   private double internalField1043;
   private int internalField1053 = -1;
   private int internalField1055;
   private boolean internalField0277;

   public PathExecutor(ComputedPath localValue1, @Nullable PathGoal localValue2) {
      this.internalField0905 = localValue1;
      this.internalField0925 = localValue2;
   }

   public PathExecutor.InternalType0263 internalMethod04868() {
      if (this.internalField0905.internalMethod02878().isEmpty()) {
         return PathExecutor.InternalType0263.internalField0719;
      } else if (this.internalField0227 >= this.internalField0905.internalMethod02878().size()) {
         return PathExecutor.InternalType0263.internalField0719;
      } else {
         this.internalMethod03490();
         if (this.internalField0227 >= this.internalField0905.internalMethod02878().size()) {
            return PathExecutor.InternalType0263.internalField0719;
         } else {
            ClientPlayerEntity localValue1 = MinecraftClient.getInstance().player;
            if (localValue1 != null && this.internalMethod03660(localValue1)) {
               this.internalField0905.internalMethod02878().get(this.internalField0227).internalMethod01347();
               this.internalMethod07913();
               return PathExecutor.InternalType0263.internalField1287;
            } else {
               this.internalMethod03486();
               int localValue2 = 16;

               while (localValue2-- > 0) {
                  AbstractPathStep localValue3 = this.internalField0905.internalMethod02878().get(this.internalField0227);
                  AbstractPathStep.InternalType0058 localValue4 = localValue3.internalMethod04014();
                  switch (localValue4) {
                     case internalField0453:
                        return PathExecutor.InternalType0263.internalField0720;
                     case internalField0454:
                        this.internalField0227++;
                        if (this.internalField0227 >= this.internalField0905.internalMethod02878().size()) {
                           return PathExecutor.InternalType0263.internalField0719;
                        }

                        this.internalMethod03486();
                        break;
                     case internalField1160:
                        localValue3.internalMethod01347();
                        return PathExecutor.InternalType0263.internalField1287;
                  }
               }

               return PathExecutor.InternalType0263.internalField0720;
            }
         }
      }
   }

   private void internalMethod03486() {
      int localValue1 = Math.min(this.internalField0227 + 6, this.internalField0905.internalMethod02878().size());

      for (int localValue2 = this.internalField0227; localValue2 < localValue1; localValue2++) {
         AbstractPathStep localValue3 = this.internalField0905.internalMethod02878().get(localValue2);
         AbstractPathStep localValue4 = localValue2 + 1 < this.internalField0905.internalMethod02878().size() ? this.internalField0905.internalMethod02878().get(localValue2 + 1) : null;
         localValue3.internalMethod02953(localValue4);
      }
   }

   private void internalMethod03490() {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      ClientPlayerEntity localValue2 = localValue1.player;
      if (localValue2 != null) {
         double localValue3 = localValue2.getX();
         double localValue5 = localValue2.getY();
         double localValue7 = localValue2.getZ();
         int localValue9 = 8;

         while (localValue9-- > 0 && this.internalField0227 + 1 < this.internalField0905.internalMethod02878().size()) {
            AbstractPathStep localValue10 = this.internalField0905.internalMethod02878().get(this.internalField0227);
            AbstractPathStep localValue11 = this.internalField0905.internalMethod02878().get(this.internalField0227 + 1);
            double localValue12 = internalMethod06777(localValue3, localValue5, localValue7, localValue10.internalMethod02540());
            double localValue14 = internalMethod06777(localValue3, localValue5, localValue7, localValue11.internalMethod01873());
            double localValue16 = internalMethod06777(localValue3, localValue5, localValue7, localValue11.internalMethod02540());
            boolean localValue18 = localValue14 < 0.36;
            boolean localValue19 = localValue16 < 0.36;
            boolean localValue20 = localValue16 + 0.25 < localValue12;
            if (!localValue18 && !localValue19 && !localValue20) {
               break;
            }

            localValue10.internalMethod01347();
            this.internalField0227++;
         }
      }
   }

   private boolean internalMethod03660(ClientPlayerEntity localValue1) {
      if (this.internalField0277 && this.internalField0227 == this.internalField1053) {
         double localValue2 = localValue1.getX() - this.internalField0193;
         double localValue4 = localValue1.getY() - this.internalField1045;
         double localValue6 = localValue1.getZ() - this.internalField1043;
         if (localValue2 * localValue2 + localValue4 * localValue4 + localValue6 * localValue6 > 0.81) {
            this.internalMethod03659(localValue1);
            return false;
         } else {
            AbstractPathStep localValue8 = this.internalMethod02430();
            int localValue9 = Math.max(60, localValue8 != null ? localValue8.internalMethod01346() : 0);
            return ++this.internalField1055 >= localValue9;
         }
      } else {
         this.internalMethod03659(localValue1);
         return false;
      }
   }

   private void internalMethod03659(ClientPlayerEntity localValue1) {
      this.internalField0193 = localValue1.getX();
      this.internalField1045 = localValue1.getY();
      this.internalField1043 = localValue1.getZ();
      this.internalField1053 = this.internalField0227;
      this.internalField1055 = 0;
      this.internalField0277 = true;
   }

   private void internalMethod07913() {
      this.internalField0277 = false;
      this.internalField1053 = -1;
      this.internalField1055 = 0;
   }

   private static double internalMethod06777(double localValue0, double localValue2, double localValue4, PathNode localValue6) {
      double localValue7 = localValue0 - (localValue6.internalMethod02945() + 0.5);
      double localValue9 = localValue2 - localValue6.internalMethod02949();
      double localValue11 = localValue4 - (localValue6.internalMethod07945() + 0.5);
      return localValue7 * localValue7 + localValue9 * localValue9 + localValue11 * localValue11;
   }

   public AbstractPathStep internalMethod02430() {
      return this.internalField0227 >= this.internalField0905.internalMethod02878().size() ? null : this.internalField0905.internalMethod02878().get(this.internalField0227);
   }

   public void internalMethod01173(int localValue1) {
      AbstractPathStep localValue2 = this.internalMethod02430();
      if (localValue2 != null) {
         localValue2.internalMethod01347();
      }

      this.internalField0227 = Math.max(0, Math.min(localValue1, this.internalField0905.internalMethod02878().size()));
      this.internalMethod07913();
   }

   @Generated
   public ComputedPath internalMethod00712() {
      return this.internalField0905;
   }

   @Nullable
   @Generated
   public PathGoal internalMethod00771() {
      return this.internalField0925;
   }

   @Generated
   public int internalMethod03485() {
      return this.internalField0227;
   }

   public static enum InternalType0263 {
      internalField0720,
      internalField0719,
      internalField1287;

      public static PathExecutor.InternalType0263[] internalMethod03196() {
         return values();
      }

      public static PathExecutor.InternalType0263 internalMethod06627(String localValue0) {
         return Enum.valueOf(PathExecutor.InternalType0263.class, localValue0);
      }
   }
}

package rockstar.client.internal.rotation;





import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import net.minecraft.client.network.ClientPlayerEntity;

public class BasicMovementPathStep extends AbstractPathStep {
   private final BasicMovementPathStep.InternalType0152 internalField0076;

   public BasicMovementPathStep(PathNode localValue1, PathNode localValue2, BasicMovementPathStep.InternalType0152 localValue3) {
      super(localValue1, localValue2);
      this.internalField0076 = localValue3;
   }

   public BasicMovementPathStep.InternalType0152 internalMethod00558() {
      return this.internalField0076;
   }

   @Override
   public double internalMethod01349() {
      return switch (this.internalField0076) {
         case internalField0076 -> 3.0 * Math.max(1.0, this.internalMethod08660());
         case internalField0077 -> 3.5;
         case internalField0984 -> 3.0;
         case internalField0983 -> 3.0;
         case internalField0982 -> 3.5;
         case internalField0981 -> 4.5;
      };
   }

   @Override
   public int internalMethod01346() {
      return 120;
   }

   @Override
   public boolean internalMethod01348() {
      return this.internalField0076 != BasicMovementPathStep.InternalType0152.internalField0983;
   }

   @Override
   public boolean internalMethod01351() {
      return false;
   }

   @Override
   public boolean internalMethod04946(PathfindBlockView localValue1) {
      int localValue2 = this.internalField0924.internalMethod02945() - this.internalField0923.internalMethod02945();
      int localValue3 = this.internalField0924.internalMethod02949() - this.internalField0923.internalMethod02949();
      int localValue4 = this.internalField0924.internalMethod07945() - this.internalField0923.internalMethod07945();
      int localValue5 = Math.abs(localValue2) + Math.abs(localValue4);
      switch (this.internalField0076) {
         case internalField0076:
            boolean localValue12 = localValue5 == 1;
            boolean localValue7 = Math.abs(localValue2) == 1 && Math.abs(localValue4) == 1;
            if (localValue3 == 0 && (localValue12 || localValue7)) {
               if (!localValue1.internalMethod08763(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945()
               )) {
                  return false;
               }

               if (!localValue1.internalMethod09927(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949() + 1, this.internalField0924.internalMethod07945()
               )) {
                  return false;
               }

               if (this.internalMethod00958(localValue1)) {
                  this.internalMethod05093(1.5 * (localValue7 ? 1.41 : 1.0));
               }

               return localValue1.internalMethod03056(
                  this.internalField0923.internalMethod02945(),
                  this.internalField0923.internalMethod07945(),
                  this.internalField0924.internalMethod02945(),
                  this.internalField0924.internalMethod07945(),
                  this.internalField0924.internalMethod02949() + 0.1,
                  this.internalField0924.internalMethod02949() + 1.7
               );
            }

            return false;
         case internalField0077:
            if (localValue5 == 0 && localValue3 == 1) {
               if (!localValue1.internalMethod08763(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945()
               )) {
                  return false;
               }

               return localValue1.internalMethod09927(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949() + 1, this.internalField0924.internalMethod07945()
               );
            }

            return false;
         case internalField0984:
            if (localValue5 == 0 && localValue3 == -1) {
               if (!localValue1.internalMethod08763(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945()
               )) {
                  return false;
               }

               return !localValue1.internalMethod08739(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949() - 1, this.internalField0924.internalMethod07945()
               );
            }

            return false;
         case internalField0983:
            if (localValue5 == 1 && localValue3 >= -1 && localValue3 <= 0) {
               if (!localValue1.internalMethod08763(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945()
               )) {
                  return false;
               }

               if (!localValue1.internalMethod09927(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949() + 1, this.internalField0924.internalMethod07945()
               )) {
                  return false;
               }

               double localValue11 = localValue1.internalMethod05956(
                  this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949(), this.internalField0923.internalMethod07945()
               );
               if (Double.isNaN(localValue11)) {
                  localValue11 = this.internalField0923.internalMethod02949();
               }

               this.internalMethod01515(localValue11, this.internalField0924.internalMethod02949());
               return localValue1.internalMethod03056(
                  this.internalField0923.internalMethod02945(),
                  this.internalField0923.internalMethod07945(),
                  this.internalField0924.internalMethod02945(),
                  this.internalField0924.internalMethod07945(),
                  localValue11 + 0.05,
                  localValue11 + 1.8
               );
            }

            return false;
         case internalField0982:
            if (localValue5 == 1 && localValue3 == 0) {
               double localValue10 = localValue1.internalMethod05956(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945()
               );
               if (!Double.isNaN(localValue10) && !(Math.abs(localValue10 - this.internalField0923.internalMethod02949()) > 0.9)) {
                  this.internalMethod01515(this.internalField0923.internalMethod02949(), localValue10);
                  return localValue1.internalMethod03056(
                     this.internalField0923.internalMethod02945(),
                     this.internalField0923.internalMethod07945(),
                     this.internalField0924.internalMethod02945(),
                     this.internalField0924.internalMethod07945(),
                     localValue10 + 0.05,
                     localValue10 + 1.8
                  );
               }

               return false;
            }

            return false;
         case internalField0981:
            if (localValue5 == 1 && localValue3 == 1) {
               double localValue6 = localValue1.internalMethod05956(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945()
               );
               if (Double.isNaN(localValue6)) {
                  return false;
               }

               double localValue8 = localValue6 - this.internalField0923.internalMethod02949();
               if (!(localValue8 < 0.4) && !(localValue8 > 1.4)) {
                  if (!localValue1.internalMethod09927(
                     this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949() + 1, this.internalField0923.internalMethod07945()
                  )) {
                     return false;
                  }

                  this.internalMethod01515(this.internalField0923.internalMethod02949(), localValue6);
                  return localValue1.internalMethod03056(
                     this.internalField0923.internalMethod02945(),
                     this.internalField0923.internalMethod07945(),
                     this.internalField0924.internalMethod02945(),
                     this.internalField0924.internalMethod07945(),
                     localValue6 + 0.05,
                     localValue6 + 1.8
                  );
               }

               return false;
            }

            return false;
         default:
            return false;
      }
   }

   private boolean internalMethod00958(PathfindBlockView localValue1) {
      return localValue1.internalMethod07797(
            this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949() + 1, this.internalField0923.internalMethod07945()
         )
         && localValue1.internalMethod07797(
            this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949() + 1, this.internalField0924.internalMethod07945()
         );
   }

   @Override
   public AbstractPathStep.InternalType0058 internalMethod04014() {
      ClientPlayerEntity localValue1 = internalMethod03778();
      if (localValue1 == null) {
         return AbstractPathStep.InternalType0058.internalField1160;
      } else if (localValue1.isSubmergedInWater() && localValue1.getAir() < 60) {
         return AbstractPathStep.InternalType0058.internalField1160;
      } else {
         double localValue2 = localValue1.getX();
         double localValue4 = localValue1.getY();
         double localValue6 = localValue1.getZ();
         double localValue8 = localValue2 - (this.internalField0924.internalMethod02945() + 0.5);
         double localValue10 = localValue6 - (this.internalField0924.internalMethod07945() + 0.5);
         double localValue12 = Math.hypot(localValue8, localValue10);
         boolean localValue14 = this.internalField0076 == BasicMovementPathStep.InternalType0152.internalField0982
            || this.internalField0076 == BasicMovementPathStep.InternalType0152.internalField0981;
         if (localValue14) {
            if (localValue1.isOnGround() && Math.abs(localValue4 - this.internalField1045) < 0.5 && localValue12 < 0.6) {
               return AbstractPathStep.InternalType0058.internalField0454;
            }
         } else if (localValue12 < 0.5 && Math.abs(localValue4 - (this.internalField0924.internalMethod02949() + 0.2)) < 0.8) {
            return AbstractPathStep.InternalType0058.internalField0454;
         }

         if (localValue4 < this.internalField0924.internalMethod02949() - 2.5 && !localValue1.isTouchingWater()) {
            return AbstractPathStep.InternalType0058.internalField1160;
         } else {
            MovementInputController localValue15 = internalMethod00577();
            localValue15.internalMethod03557(false);
            localValue15.internalMethod08033(false);
            localValue15.internalMethod08045(false);
            localValue15.internalMethod08371(false);
            if (this.internalField0076 != BasicMovementPathStep.InternalType0152.internalField0077
               && this.internalField0076 != BasicMovementPathStep.InternalType0152.internalField0984) {
               double localValue16 = localValue14 ? this.internalField1045 + 0.3 : this.internalField0924.internalMethod02949() + 0.6;
               double localValue18 = this.internalField0924.internalMethod02945() + 0.5 - localValue2;
               double localValue20 = this.internalField0924.internalMethod07945() + 0.5 - localValue6;
               float localValue22;
               if (Math.hypot(localValue18, localValue20) < 0.3) {
                  float localValue23 = this.internalMethod01350();
                  localValue22 = Float.isNaN(localValue23) ? localValue1.getYaw() : localValue23;
               } else {
                  localValue22 = internalMethod01514(localValue18, localValue20);
               }

               boolean localValue30 = !localValue14
                  && this.internalField0100 instanceof BasicMovementPathStep localValue24
                  && localValue24.internalMethod00558() == BasicMovementPathStep.InternalType0152.internalField0076;
               float localValue31 = 0.0F;
               boolean localValue33;
               if (localValue14) {
                  localValue33 = true;
               } else if (localValue30) {
                  if (localValue1.getAir() < 130) {
                     localValue31 = -25.0F;
                     localValue33 = true;
                  } else if (!localValue1.isSubmergedInWater()) {
                     localValue31 = 25.0F;
                     localValue33 = false;
                  } else {
                     double localValue26 = this.internalField0924.internalMethod02949() + 0.45;
                     localValue31 = localValue4 > localValue26 + 0.15 ? 8.0F : (localValue4 < localValue26 - 0.15 ? -10.0F : 0.0F);
                     localValue33 = false;
                  }
               } else {
                  if (localValue1.isSubmergedInWater()) {
                     double localValue34 = localValue16 - localValue1.getEyeY();
                     double localValue28 = Math.max(0.05, Math.hypot(localValue18, localValue20));
                     localValue31 = (float)(-Math.toDegrees(Math.atan2(localValue34, localValue28)));
                     localValue31 = Math.max(-75.0F, Math.min(75.0F, localValue31));
                  }

                  localValue33 = localValue4 < this.internalField0924.internalMethod02949() + 0.1;
               }

               internalMethod01516(localValue22, localValue31);
               localValue15.internalMethod03508(true);
               localValue15.internalMethod09358(!localValue14);
               localValue15.internalMethod08359(localValue33);
               return AbstractPathStep.InternalType0058.internalField0453;
            } else {
               localValue15.internalMethod03508(false);
               localValue15.internalMethod09358(false);
               localValue15.internalMethod08359(this.internalField0076 == BasicMovementPathStep.InternalType0152.internalField0077);
               localValue15.internalMethod08371(this.internalField0076 == BasicMovementPathStep.InternalType0152.internalField0984);
               return AbstractPathStep.InternalType0058.internalField0453;
            }
         }
      }
   }

   @Override
   public void internalMethod01347() {
      MovementInputController localValue1 = internalMethod00577();
      localValue1.internalMethod03508(false);
      localValue1.internalMethod09358(false);
      localValue1.internalMethod08359(false);
   }

   public static enum InternalType0152 {
      internalField0076,
      internalField0077,
      internalField0984,
      internalField0983,
      internalField0982,
      internalField0981;

      public static BasicMovementPathStep.InternalType0152[] internalMethod05710() {
         return values();
      }

      public static BasicMovementPathStep.InternalType0152 internalMethod02875(String localValue0) {
         return Enum.valueOf(BasicMovementPathStep.InternalType0152.class, localValue0);
      }
   }
}

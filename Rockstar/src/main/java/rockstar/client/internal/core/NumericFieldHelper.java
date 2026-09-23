package rockstar.client.internal.core;



import rockstar.client.internal.ui.*;
import rockstar.client.*;
import java.util.function.IntConsumer;
import java.util.function.IntUnaryOperator;

public class NumericFieldHelper {
   public static void internalMethod06060(RoundedTextFieldWidget localValue0, int localValue1, IntUnaryOperator localValue2, IntUnaryOperator localValue3, IntConsumer localValue4) {
      String localValue5 = localValue0.internalMethod01396().trim();
      if (localValue5.isEmpty()) {
         int localValue10 = localValue2.applyAsInt(0);
         int localValue12 = localValue3.applyAsInt(localValue10);
         localValue0.internalMethod03483(String.valueOf(localValue12));
         localValue4.accept(localValue10);
         NumericFieldHelper.InternalType0394.internalMethod04607(localValue10, String.valueOf(localValue12));
      } else {
         try {
            int localValue6 = Integer.parseInt(localValue5);
            int localValue11 = localValue2.applyAsInt(localValue6);
            int localValue8 = localValue3.applyAsInt(localValue11);
            localValue0.internalMethod03483(String.valueOf(localValue8));
            localValue4.accept(localValue11);
            NumericFieldHelper.InternalType0394.internalMethod04607(localValue11, String.valueOf(localValue8));
         } catch (NumberFormatException localValue9) {
            int localValue7 = localValue3.applyAsInt(localValue1);
            localValue0.internalMethod03483(String.valueOf(localValue7));
            NumericFieldHelper.InternalType0394.internalMethod01609(localValue1, String.valueOf(localValue7));
         }
      }
   }

   public static void internalMethod01391(RoundedTextFieldWidget localValue0, int localValue1, IntUnaryOperator localValue2, IntConsumer localValue3) {
      internalMethod06060(localValue0, localValue1, localValue2, localValue0x -> localValue0x, localValue3);
   }

   public static IntUnaryOperator internalMethod05399(int localValue0, int localValue1) {
      return localValue2 -> Math.clamp((long)localValue2, localValue0, localValue1);
   }

   public static IntUnaryOperator internalMethod06031(int localValue0) {
      return localValue1 -> Math.max(localValue0, localValue1);
   }

   public static IntUnaryOperator internalMethod02618(int localValue0) {
      return localValue1 -> localValue1 + localValue0;
   }

   public static final class InternalType0394 {
      private final int internalField0227;
      private final boolean internalField0277;
      private final String internalField0248;

      public InternalType0394(int localValue1, boolean localValue2, String localValue3) {
         this.internalField0227 = localValue1;
         this.internalField0277 = localValue2;
         this.internalField0248 = localValue3;
      }

      public static NumericFieldHelper.InternalType0394 internalMethod04212(int localValue0) {
         return new NumericFieldHelper.InternalType0394(localValue0, true, String.valueOf(localValue0));
      }

      public static NumericFieldHelper.InternalType0394 internalMethod04607(int localValue0, String localValue1) {
         return new NumericFieldHelper.InternalType0394(localValue0, true, localValue1);
      }

      public static NumericFieldHelper.InternalType0394 internalMethod01609(int localValue0, String localValue1) {
         return new NumericFieldHelper.InternalType0394(localValue0, false, localValue1);
      }

      @Override
      public final String toString() {
         return "InternalType0394[value=" + this.internalField0227 + ", valid=" + this.internalField0277 + ", displayText=" + this.internalField0248 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         NumericFieldHelper.InternalType0394 other = (NumericFieldHelper.InternalType0394) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248);
      }

      public int internalMethod03670() {
         return this.internalField0227;
      }

      public boolean internalMethod03671() {
         return this.internalField0277;
      }

      public String internalMethod00508() {
         return this.internalField0248;
      }
   }
}

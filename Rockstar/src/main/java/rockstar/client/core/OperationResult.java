package rockstar.client.core;



import rockstar.client.util.*;
import rockstar.client.*;
import net.minecraft.text.Text;

public sealed interface OperationResult permits OperationResult.InternalType0448, OperationResult.InternalType0447 {
   static <T> OperationResult.InternalType0448<T> internalMethod00116(T localValue0) {
      return new OperationResult.InternalType0448<>((T)localValue0);
   }

   static OperationResult.InternalType0447 internalMethod05941(String localValue0) {
      ClientMessages.internalMethod09025(Text.of(localValue0));
      return new OperationResult.InternalType0447(localValue0);
   }

   public static final class InternalType0447 implements OperationResult {
      private final String internalField0248;

      public InternalType0447(String localValue1) {
         this.internalField0248 = localValue1;
      }

      @Override
      public final String toString() {
         return "InternalType0447[message=" + this.internalField0248 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         OperationResult.InternalType0447 other = (OperationResult.InternalType0447) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248);
      }

      public String internalMethod03912() {
         return this.internalField0248;
      }
   }

   public static final class InternalType0448<T> implements OperationResult {
      private final T internalField0290;

      public InternalType0448(T localValue1) {
         this.internalField0290 = (T)localValue1;
      }

      @Override
      public final String toString() {
         return "InternalType0448[value=" + this.internalField0290 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0290);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         OperationResult.InternalType0448 other = (OperationResult.InternalType0448) localValue1;
         return java.util.Objects.equals(this.internalField0290, other.internalField0290);
      }

      public T internalMethod06555() {
         return this.internalField0290;
      }
   }
}

package rockstar.client.internal.core;



import rockstar.client.internal.ui.*;
import rockstar.client.*;
import java.util.Stack;

public class HudPositionHistory {
   private final Stack<HudPositionHistory.InternalType0171> internalField0895 = new Stack<>();
   private final Stack<HudPositionHistory.InternalType0171> internalField0896 = new Stack<>();

   public void internalMethod00453(AbstractHudWidget localValue1, float localValue2, float localValue3, float localValue4, float localValue5) {
      this.internalField0895.push(new HudPositionHistory.InternalType0171(localValue1, localValue2, localValue3, localValue4, localValue5, System.currentTimeMillis()));
      this.internalField0896.clear();
   }

   public void internalMethod05238() {
      if (!this.internalField0895.isEmpty()) {
         HudPositionHistory.InternalType0171 localValue1 = this.internalField0895.pop();
         localValue1.internalMethod01996().pos(localValue1.internalMethod03083(), localValue1.internalMethod03085());
         this.internalField0896.push(localValue1);
      }
   }

   public void internalMethod05244() {
      if (!this.internalField0896.isEmpty()) {
         HudPositionHistory.InternalType0171 localValue1 = this.internalField0896.pop();
         localValue1.internalMethod01996().pos(localValue1.internalMethod08684(), localValue1.internalMethod08685());
         this.internalField0895.push(localValue1);
      }
   }

   public boolean internalMethod05239() {
      return !this.internalField0895.isEmpty();
   }

   public boolean internalMethod05245() {
      return !this.internalField0896.isEmpty();
   }

   public long internalMethod05237() {
      return this.internalField0895.isEmpty() ? Long.MIN_VALUE : this.internalField0895.peek().internalMethod03084();
   }

   public long internalMethod05243() {
      return this.internalField0896.isEmpty() ? Long.MIN_VALUE : this.internalField0896.peek().internalMethod03084();
   }

   static final class InternalType0171 {
      private final AbstractHudWidget internalField0947;
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;
      private final float internalField1047;
      private final long internalField0229;

      InternalType0171(AbstractHudWidget localValue1, float localValue2, float localValue3, float localValue4, float localValue5, long localValue6) {
         this.internalField0947 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
         this.internalField1047 = localValue5;
         this.internalField0229 = localValue6;
      }

      @Override
      public final String toString() {
         return "InternalType0171[element=" + this.internalField0947 + ", fromX=" + this.internalField0205 + ", fromY=" + this.internalField0206 + ", toX=" + this.internalField1048 + ", toY=" + this.internalField1047 + ", time=" + this.internalField0229 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0947);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         HudPositionHistory.InternalType0171 other = (HudPositionHistory.InternalType0171) localValue1;
         return java.util.Objects.equals(this.internalField0947, other.internalField0947)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229);
      }

      public AbstractHudWidget internalMethod01996() {
         return this.internalField0947;
      }

      public float internalMethod03083() {
         return this.internalField0205;
      }

      public float internalMethod03085() {
         return this.internalField0206;
      }

      public float internalMethod08684() {
         return this.internalField1048;
      }

      public float internalMethod08685() {
         return this.internalField1047;
      }

      public long internalMethod03084() {
         return this.internalField0229;
      }
   }
}

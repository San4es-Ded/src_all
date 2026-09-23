package rockstar.client.internal.core;


import rockstar.client.*;
import lombok.Generated;

public class AlignmentGuideLine {
   private final AlignmentGuideLine.InternalType0164 internalField0501;
   private final float internalField0205;
   private boolean internalField0277;

   @Generated
   public AlignmentGuideLine.InternalType0164 internalMethod04528() {
      return this.internalField0501;
   }

   @Generated
   public float internalMethod05366() {
      return this.internalField0205;
   }

   @Generated
   public boolean internalMethod05367() {
      return this.internalField0277;
   }

   @Generated
   public AlignmentGuideLine(AlignmentGuideLine.InternalType0164 localValue1, float localValue2) {
      this.internalField0501 = localValue1;
      this.internalField0205 = localValue2;
   }

   @Generated
   public void internalMethod07380(boolean localValue1) {
      this.internalField0277 = localValue1;
   }

   public static enum InternalType0164 {
      internalField0501,
      internalField0502;
   }
}

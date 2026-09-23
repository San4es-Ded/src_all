package rockstar.client.rotation;


import rockstar.client.*;
public enum RotationBehavior {
   internalField0115(false, false, false, false, false),
   internalField0114(true, true, false, false, false),
   internalField1004(true, true, false, false, false),
   internalField1003(true, true, true, false, false),
   internalField1002(true, true, false, true, false),
   internalField1001(true, true, false, false, true),
   internalField1423(true, true, false, false, false);

   private final boolean internalField0277;
   private final boolean internalField0276;
   private final boolean internalField1099;
   private final boolean internalField1100;
   private final boolean internalField1102;

   private RotationBehavior(boolean localValue3, boolean localValue4, boolean localValue5, boolean localValue6, boolean localValue7) {
      this.internalField0277 = localValue3;
      this.internalField0276 = localValue4;
      this.internalField1099 = localValue5;
      this.internalField1100 = localValue6;
      this.internalField1102 = localValue7;
   }

   public boolean internalMethod03280() {
      return this.internalField0277;
   }

   public boolean internalMethod03281() {
      return this.internalField0276;
   }

   public boolean internalMethod08350() {
      return this.internalField1099;
   }

   public boolean internalMethod08352() {
      return this.internalField1100;
   }

   public boolean internalMethod08363() {
      return this.internalField1102;
   }
}

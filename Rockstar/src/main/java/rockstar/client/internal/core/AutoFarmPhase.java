package rockstar.client.internal.core;



import rockstar.client.i18n.*;
import rockstar.client.*;
public enum AutoFarmPhase {
   internalField0848("idle"),
   internalField0847("working"),
   internalField1350("moving"),
   internalField1348("planting"),
   internalField1347("growing"),
   internalField1349("pickup"),
   internalField1639("deposit"),
   internalField1636("selling"),
   internalField1634("buying"),
   internalField1635("crafting"),
   internalField1637("repairing"),
   internalField1638("restocking");

   private final String internalField0248;

   private AutoFarmPhase(String localValue3) {
      this.internalField0248 = "modules.auto_farm.phase." + localValue3;
   }

   public String internalMethod00107() {
      return LanguageManager.internalMethod07214(this.internalField0248);
   }
}

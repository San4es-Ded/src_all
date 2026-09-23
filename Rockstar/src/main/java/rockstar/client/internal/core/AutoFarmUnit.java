package rockstar.client.internal.core;



import rockstar.client.i18n.*;
import rockstar.client.*;
public enum AutoFarmUnit {
   internalField0853("blocks"),
   internalField0852("potions"),
   internalField1351("sales"),
   internalField1352("items");

   private final String internalField0248;

   private AutoFarmUnit(String localValue3) {
      this.internalField0248 = "modules.auto_farm.unit." + localValue3;
   }

   public String internalMethod02188() {
      return LanguageManager.internalMethod07214(this.internalField0248);
   }
}

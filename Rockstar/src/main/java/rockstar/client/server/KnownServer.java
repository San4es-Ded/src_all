package rockstar.client.server;


import rockstar.client.*;
public enum KnownServer {
   internalField0578("funtime", "playft"),
   internalField0579("spooky"),
   internalField1220("reallyworld", "playrw"),
   internalField1218("holy", "holly", "playhw"),
   internalField1219("cherry.pizza"),
   internalField1217("funtime", "playft", "reallyworld", "playrw", "funsky", "slimeworld"),
   internalField1572("mineblaze", "dexland"),
   internalField1568("funtime", "playft", "spooky", "funsky", "holytime"),
   internalField1570("saturn"),
   internalField1567("funsky"),
   internalField1566("funtime", "playft", "spooky", "funsky", "slimeworld", "cakeworld", "holytime"),
   internalField1569("holyworld", "playhw", "hollyworld"),
   internalField1571("reallyworld", "playrw", "slimeworld", "cakeworld");

   private final String[] internalField0359;

   private KnownServer(String... localValue3) {
      this.internalField0359 = localValue3;
   }

   public boolean internalMethod04913(String localValue1) {
      localValue1 = localValue1.toLowerCase();

      for (String localValue5 : this.internalField0359) {
         if (localValue1.contains(localValue5)) {
            return true;
         }
      }

      return false;
   }
}

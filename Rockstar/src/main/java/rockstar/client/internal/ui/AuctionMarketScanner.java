package rockstar.client.internal.ui;





import rockstar.client.util.*;
import rockstar.client.data.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.Generated;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

public class AuctionMarketScanner implements MinecraftClientAccess {
   private final Map<String, String> internalField0543 = Map.of(
      "\u041a\u0440\u0443\u0448\u0438\u0442\u0435\u043b\u044f",
      "\u0422\u0430\u043b\u0438\u0441\u043c\u0430\u043d \u041a\u0440\u0443\u0448\u0438\u0442\u0435\u043b\u044f",
      "\u0412\u0438\u0445\u0440\u044f",
      "\u0412\u0438\u0445\u0440\u044c",
      "\u0420\u0430\u0437\u0434\u043e\u0440\u0430",
      "\u0420\u0430\u0437\u0434\u043e\u0440",
      "\u0422\u0438\u0440\u0430\u043d\u0430",
      "\u0422\u0438\u0440\u0430\u043d"
   );
   private AuctionMarketScanner.InternalType0161 internalField0598;
   private final Stopwatch internalField0519;
   private final Map<String, List<Long>> internalField0544;
   private final Map<String, Double> internalField1197;
   private List<AuctionItem.InternalType0159> internalField0416;
   private int internalField0228;
   private int internalField1053;
   private int internalField1055;
   private static final String[] internalField0359 = new String[]{
      "\u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u0430 \u0432 \u0440\u0435\u0436\u0438\u043c\u0435 afk",
      "\u043d\u0435 \u043c\u043e\u0436\u0435\u0442\u0435 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u044d\u0442\u043e \u0432 \u044d\u0442\u043e\u043c \u043c\u0435\u0441\u0442\u0435",
      "\u0437\u0430\u043f\u0440\u0435\u0449\u0451\u043d\u043d\u044b\u0435 \u0441\u0438\u043c\u0432\u043e\u043b\u044b",
      "\u0437\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u043d\u044b\u0435 \u0441\u0438\u043c\u0432\u043e\u043b\u044b"
   };
   private String internalField0248;

   public AuctionMarketScanner() {
      this.internalField0598 = AuctionMarketScanner.InternalType0161.internalField0598;
      this.internalField0519 = new Stopwatch();
      this.internalField0544 = new HashMap<>();
      this.internalField1197 = new HashMap<>();
      this.internalField0416 = new ArrayList<>();
      this.internalField0228 = 0;
      this.internalField1053 = 0;
      this.internalField1055 = 0;
   }

   public void internalMethod01389(List<AuctionItem.InternalType0159> localValue1) {
      this.internalField0416 = new ArrayList<>(localValue1);
      if (!this.internalField0416.isEmpty()) {
         this.internalField0544.clear();
         this.internalField1197.clear();
         this.internalField0248 = null;
         this.internalField0228 = 0;
         this.internalField1053 = 0;
         this.internalField1055 = 0;
         this.internalField0598 = AuctionMarketScanner.InternalType0161.internalField0599;
         this.internalField0519.internalMethod00701();
      }
   }

   public void internalMethod04378() {
      switch (this.internalField0598) {
         case internalField0598:
         default:
            break;
         case internalField0599:
            this.internalMethod04383();
            break;
         case internalField1231:
            this.internalMethod08234();
            break;
         case internalField1232:
            this.internalMethod08243();
            break;
         case internalField1230:
            this.internalMethod08245();
            break;
         case internalField1229:
            this.internalMethod09504();
      }
   }

   private void internalMethod04383() {
      if (this.internalField0228 >= this.internalField0416.size()) {
         if (internalField0149.currentScreen != null) {
            internalField0149.player.closeHandledScreen();
         }

         this.internalField0598 = AuctionMarketScanner.InternalType0161.internalField1229;
         this.internalMethod09505();
      } else {
         if (this.internalField0519.internalMethod02365(900L)) {
            AuctionItem.InternalType0159 localValue1 = this.internalField0416.get(this.internalField0228);
            if (internalField0149.currentScreen instanceof HandledScreen) {
               internalField0149.player.closeHandledScreen();
            }

            internalField0149.player.networkHandler.sendChatCommand("ah search " + this.internalMethod04374(localValue1));
            this.internalField0598 = AuctionMarketScanner.InternalType0161.internalField1231;
            this.internalField0519.internalMethod00701();
            this.internalField1053 = 0;
            this.internalField1055++;
         }
      }
   }

   private String internalMethod04374(AuctionItem.InternalType0159 localValue1) {
      if (localValue1.internalMethod04343() == null) {
         return ItemNbtUtils.internalMethod06695(localValue1.internalMethod00723());
      } else {
         String localValue2 = localValue1.internalMethod04343();
         if (this.internalField0543.containsKey(localValue2)) {
            return this.internalField0543.get(localValue2);
         } else {
            CustomItemUtils.InternalType0254 localValue3 = CustomItemUtils.internalMethod03238(localValue1.internalMethod00723());
            if (localValue3 != null) {
               String localValue4 = localValue3.internalMethod00671(localValue1.internalMethod00723());
               if (localValue4 != null && localValue4.toLowerCase().contains(localValue2.toLowerCase())) {
                  return localValue4;
               }
            }

            return localValue2;
         }
      }
   }

   public void internalMethod00573(String localValue1) {
      if (this.internalField0598 != AuctionMarketScanner.InternalType0161.internalField0598 && localValue1 != null) {
         String localValue2 = localValue1.toLowerCase(Locale.ROOT);

         for (String localValue6 : internalField0359) {
            if (localValue2.contains(localValue6)) {
               this.internalField0248 = localValue1.trim();
               return;
            }
         }
      }
   }

   private void internalMethod08233() {
      this.internalField0248 = null;
      this.internalField0416.clear();
      this.internalField0228 = 0;
      this.internalField1055 = 0;
      if (internalField0149.currentScreen != null) {
         internalField0149.player.closeHandledScreen();
      }

      this.internalField0598 = AuctionMarketScanner.InternalType0161.internalField0598;
   }

   private void internalMethod08234() {
      if (this.internalField0248 != null) {
         ClientMessages.internalMethod03058(
            Text.of(
               "\u0421\u0435\u0440\u0432\u0435\u0440 \u043d\u0435 \u0434\u0430\u0451\u0442 \u043e\u0442\u043a\u0440\u044b\u0442\u044c \u0430\u0443\u043a\u0446\u0438\u043e\u043d: \u00ab"
                  + this.internalField0248
                  + "\u00bb \u2014 \u043f\u0430\u0440\u0441\u0438\u043d\u0433 \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d"
            )
         );
         this.internalMethod08233();
      } else if (internalField0149.currentScreen instanceof HandledScreen localValue1 && AuctionTooltipParser.internalMethod08274(localValue1.getTitle().getString())) {
         if (this.internalField0519.internalMethod02365(300L)) {
            this.internalField0598 = AuctionMarketScanner.InternalType0161.internalField1232;
            this.internalField0519.internalMethod00701();
         }
      } else if (this.internalField0519.internalMethod02365(3000L)) {
         if (this.internalField1055 >= 3) {
            AuctionItem.InternalType0159 localValue3 = this.internalField0416.get(this.internalField0228);
            ClientMessages.internalMethod03058(
               Text.of(
                  AuctionItem.internalMethod06551(localValue3)
                     + ": \u0430\u0443\u043a\u0446\u0438\u043e\u043d \u043d\u0435 \u043e\u0442\u043a\u0440\u044b\u043b\u0441\u044f \u043f\u043e \u0437\u0430\u043f\u0440\u043e\u0441\u0443 \u00ab"
                     + this.internalMethod04374(localValue3)
                     + "\u00bb \u2014 \u043f\u0440\u043e\u043f\u0443\u0441\u043a\u0430\u044e"
               )
            );
            this.internalField0598 = AuctionMarketScanner.InternalType0161.internalField1230;
            this.internalField0519.internalMethod00701();
         } else {
            this.internalField0598 = AuctionMarketScanner.InternalType0161.internalField0599;
            this.internalField0519.internalMethod00701();
         }
      }
   }

   private void internalMethod08243() {
      if (internalField0149.currentScreen instanceof HandledScreen localValue1 && AuctionTooltipParser.internalMethod08274(localValue1.getTitle().getString())) {
         if (this.internalField0519.internalMethod02365(200L)) {
            this.internalMethod02724(localValue1);
            this.internalField1053++;
            if (this.internalField1053 >= 4) {
               this.internalField0598 = AuctionMarketScanner.InternalType0161.internalField1230;
               this.internalField0519.internalMethod00701();
            } else {
               internalField0149.interactionManager.clickSlot(localValue1.getScreenHandler().syncId, 49, 0, SlotActionType.PICKUP, internalField0149.player);
               this.internalField0519.internalMethod00701();
            }
         }
      } else {
         this.internalField0598 = AuctionMarketScanner.InternalType0161.internalField1230;
      }
   }

   private void internalMethod08245() {
      if (this.internalField0519.internalMethod02365(200L)) {
         this.internalMethod09509();
         this.internalField0228++;
         this.internalField1055 = 0;
         if (internalField0149.currentScreen != null) {
            internalField0149.player.closeHandledScreen();
         }

         this.internalField0598 = AuctionMarketScanner.InternalType0161.internalField0599;
         this.internalField0519.internalMethod00701();
      }
   }

   private void internalMethod09504() {
      if (internalField0149.currentScreen != null) {
         internalField0149.player.closeHandledScreen();
      }

      this.internalField0598 = AuctionMarketScanner.InternalType0161.internalField0598;
   }

   private void internalMethod09505() {
      ClientMessages.internalMethod01809(Text.of("\u0426\u0435\u043d\u044b \u0440\u044b\u043d\u043a\u0430 (\u0437\u0430 1 \u0448\u0442):"));

      for (AuctionItem.InternalType0159 localValue2 : this.internalField0416) {
         String localValue3 = AuctionItem.internalMethod06551(localValue2);
         long localValue4 = (long)this.internalField1197.getOrDefault(localValue2.internalMethod00891(), 0.0).doubleValue();
         if (localValue4 <= 0L) {
            ClientMessages.internalMethod01809(
               Text.of(localValue3 + " \u2014 \u043d\u0435\u0442 \u043f\u0440\u0435\u0434\u043b\u043e\u0436\u0435\u043d\u0438\u0439")
            );
         } else {
            String localValue6 = localValue3 + " \u2014 \u0440\u044b\u043d\u043e\u043a " + MathUtils.internalMethod00005(localValue4);
            if (localValue2.internalMethod05344() == AuctionItem.InternalType0158.internalField0591) {
               long localValue7 = (long)(localValue4 * (1.0 - localValue2.internalMethod00347() / 100.0));
               localValue6 = localValue6
                  + ", \u0437\u0430\u043a\u0443\u043f\u043a\u0430 \u0434\u043e "
                  + MathUtils.internalMethod00005(localValue7)
                  + " (-"
                  + (int)localValue2.internalMethod00347()
                  + "%)";
            }

            ClientMessages.internalMethod01809(Text.of(localValue6));
         }
      }
   }

   private void internalMethod02724(HandledScreen<?> localValue1) {
      AuctionItem.InternalType0159 localValue2 = this.internalField0416.get(this.internalField0228);
      String localValue3 = localValue2.internalMethod00891();
      this.internalField0544.putIfAbsent(localValue3, new ArrayList<>());
      List localValue4 = this.internalField0544.get(localValue3);
      AuctionTooltipParser.InternalType0248 localValue5 = AuctionTooltipParser.internalMethod06312(localValue1, false, null);

      for (AuctionTooltipParser.InternalType0247 localValue7 : localValue5.internalMethod05468()) {
         if (AuctionItem.internalMethod07258(localValue7.internalMethod02415(), localValue2)) {
            int localValue8 = Math.max(1, localValue7.internalMethod00227());
            long localValue9 = localValue7.internalMethod00226() / localValue8;
            localValue4.add(localValue9);
         }
      }
   }

   private void internalMethod09509() {
      String localValue1 = this.internalField0416.get(this.internalField0228).internalMethod00891();
      List localValue2 = this.internalField0544.get(localValue1);
      if (localValue2 != null && !localValue2.isEmpty()) {
         ArrayList localValue3 = new ArrayList(localValue2);
         localValue3.sort((left, right) -> ((Long)left).compareTo((Long)right));
         int localValue4 = Math.max(1, localValue3.size() / 2);
         List localValue5 = localValue3.subList(0, localValue4);
         int localValue8 = localValue5.size();
         double localValue6;
         if (localValue8 % 2 == 0) {
            localValue6 = ((Long)localValue5.get(localValue8 / 2 - 1) + (Long)localValue5.get(localValue8 / 2)) / 2.0;
         } else {
            localValue6 = ((Long)localValue5.get(localValue8 / 2)).longValue();
         }

         this.internalField1197.put(localValue1, localValue6);
      } else {
         this.internalField1197.put(localValue1, 0.0);
      }
   }

   public double internalMethod01977(AuctionTooltipParser.InternalType0247 localValue1) {
      for (AuctionItem.InternalType0159 localValue3 : this.internalField0416) {
         if (AuctionItem.internalMethod07258(localValue1.internalMethod02415(), localValue3)) {
            return this.internalField1197.getOrDefault(localValue3.internalMethod00891(), 0.0);
         }
      }

      return 0.0;
   }

   public double internalMethod00572(String localValue1) {
      return localValue1 == null ? 0.0 : this.internalField1197.getOrDefault(localValue1, 0.0);
   }

   public boolean internalMethod04379() {
      return this.internalField0598 == AuctionMarketScanner.InternalType0161.internalField1229;
   }

   public boolean internalMethod04384() {
      return this.internalField0598 == AuctionMarketScanner.InternalType0161.internalField0598;
   }

   @Generated
   public AuctionMarketScanner.InternalType0161 internalMethod00793() {
      return this.internalField0598;
   }

   public static enum InternalType0161 {
      internalField0598,
      internalField0599,
      internalField1231,
      internalField1232,
      internalField1230,
      internalField1229;
   }
}

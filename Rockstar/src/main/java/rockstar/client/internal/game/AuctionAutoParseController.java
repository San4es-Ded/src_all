package rockstar.client.internal.game;




import rockstar.client.util.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import java.util.List;
import java.util.Locale;
import lombok.Generated;

public class AuctionAutoParseController implements MinecraftClientAccess {
   private AuctionAutoParseController.InternalType0160 internalField0597;
   private final Stopwatch internalField0519;
   private long internalField0230;
   private int internalField0227;
   private int internalField0228;
   private boolean internalField0277;

   public AuctionAutoParseController() {
      this.internalField0597 = AuctionAutoParseController.InternalType0160.internalField0597;
      this.internalField0519 = new Stopwatch();
      this.internalField0230 = System.currentTimeMillis();
      this.internalField0227 = 0;
      this.internalField0228 = 0;
      this.internalField0277 = false;
   }

   public void internalMethod02634() {
      if (!this.internalField0277) {
         List localValue1 = AuctionAutoParseConfig.internalMethod04345();
         if (!localValue1.isEmpty()) {
            if (this.internalField0597 == AuctionAutoParseController.InternalType0160.internalField0597
               && System.currentTimeMillis() - this.internalField0230 >= AuctionAutoParseConfig.internalMethod05796()) {
               this.internalField0228 = 0;
               this.internalField0597 = AuctionAutoParseController.InternalType0160.internalField0596;
               this.internalField0519.internalMethod00701();
            }

            switch (this.internalField0597) {
               case internalField0597:
               default:
                  break;
               case internalField0596:
                  if (!this.internalField0519.internalMethod02365(1500L)) {
                     return;
                  }

                  internalField0149.player.networkHandler.sendChatCommand("an" + localValue1.get(this.internalField0227 % localValue1.size()));
                  this.internalField0597 = AuctionAutoParseController.InternalType0160.internalField1227;
                  this.internalField0519.internalMethod00701();
                  break;
               case internalField1227:
                  if (this.internalField0519.internalMethod02365(AuctionAutoParseConfig.internalMethod05799())) {
                     this.internalField0597 = AuctionAutoParseController.InternalType0160.internalField1228;
                  }
                  break;
               case internalField1228:
                  internalField0149.player.networkHandler.sendChatCommand("ah");
                  this.internalField0597 = AuctionAutoParseController.InternalType0160.internalField0597;
                  this.internalField0227 = (this.internalField0227 + 1) % localValue1.size();
                  this.internalField0228 = 0;
                  this.internalField0230 = System.currentTimeMillis();
            }
         }
      }
   }

   public void internalMethod04652(String localValue1) {
      if (this.internalField0597 == AuctionAutoParseController.InternalType0160.internalField0596
         || this.internalField0597 == AuctionAutoParseController.InternalType0160.internalField1227) {
         if (this.internalMethod04653(localValue1)) {
            List localValue2 = AuctionAutoParseConfig.internalMethod04345();
            if (!localValue2.isEmpty()) {
               if (++this.internalField0228 >= localValue2.size()) {
                  this.internalField0228 = 0;
                  this.internalField0597 = AuctionAutoParseController.InternalType0160.internalField0597;
                  this.internalField0230 = System.currentTimeMillis();
               } else {
                  this.internalField0227 = (this.internalField0227 + 1) % localValue2.size();
                  this.internalField0597 = AuctionAutoParseController.InternalType0160.internalField0596;
                  this.internalField0519.internalMethod00701();
               }
            }
         }
      }
   }

   private boolean internalMethod04653(String localValue1) {
      String localValue2 = localValue1.toLowerCase(Locale.ROOT);
      return localValue2.contains("\u0441\u0435\u0440\u0432\u0435\u0440 \u0437\u0430\u043f\u043e\u043b\u043d\u0435\u043d")
         || localValue2.contains("\u043a\u0438\u043a\u043d\u0443\u0442\u044b \u043f\u0440\u0438 \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0438")
         || localValue2.contains("\u0441\u0435\u0440\u0432\u0435\u0440 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d");
   }

   public void internalMethod05952(boolean localValue1) {
      this.internalField0277 = localValue1;
   }

   public void internalMethod02636() {
   }

   public void internalMethod08845() {
      this.internalField0230 = System.currentTimeMillis();
      this.internalField0228 = 0;
   }

   @Generated
   public AuctionAutoParseController.InternalType0160 internalMethod02913() {
      return this.internalField0597;
   }

   @Generated
   public boolean internalMethod02635() {
      return this.internalField0277;
   }

   public static enum InternalType0160 {
      internalField0597,
      internalField0596,
      internalField1227,
      internalField1228;
   }
}

package rockstar.client.internal.ui;




import rockstar.client.util.*;
import rockstar.client.data.*;
import rockstar.client.*;
import java.util.function.ToLongFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class AuctionSellAutomation implements MinecraftClientAccess {
   private ToLongFunction<AuctionItem.InternalType0159> internalField0737 = localValue0 -> 0L;
   private AuctionSellAutomation.InternalType0281 internalField0035;
   private AuctionItem.InternalType0159 internalField0593;
   private boolean internalField0277;
   private boolean internalField0276;
   private int internalField0228;
   private int internalField1053;
   private boolean internalField1099;
   private boolean internalField1100;
   private long internalField1060;
   private long internalField1057;
   private long internalField1473;
   private long internalField1477;
   private static final Pattern internalField0293 = Pattern.compile("\\$([\\d.,]+)");

   public AuctionSellAutomation() {
      this.internalField0035 = AuctionSellAutomation.InternalType0281.internalField0035;
      this.internalField1477 = System.currentTimeMillis();
   }

   public void internalMethod02050(ToLongFunction<AuctionItem.InternalType0159> localValue1) {
      if (localValue1 != null) {
         this.internalField0737 = localValue1;
      }
   }

   public boolean internalMethod03853() {
      return this.internalField0035 != AuctionSellAutomation.InternalType0281.internalField0035;
   }

   public boolean internalMethod03858() {
      return this.internalMethod03853() || this.internalMethod01009() != null || this.internalMethod08261();
   }

   private boolean internalMethod08261() {
      return this.internalField1099 && System.currentTimeMillis() - this.internalField1477 >= 60000L;
   }

   public void internalMethod03852() {
      this.internalMethod09610();
      this.internalField0593 = null;
      this.internalField0276 = false;
      this.internalField1100 = false;
      this.internalField1099 = false;
      this.internalField1053 = 0;
      this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0035);
   }

   public void internalMethod03857() {
      if (internalField0149.player != null && internalField0149.world != null && internalField0149.interactionManager != null) {
         try {
            this.internalMethod08260();
         } catch (Exception localValue2) {
            this.internalMethod03852();
         }
      }
   }

   private void internalMethod08260() {
      switch (this.internalField0035) {
         case internalField0035:
            AuctionItem.InternalType0159 localValue3 = this.internalMethod01009();
            if (localValue3 != null) {
               this.internalField0593 = localValue3;
               this.internalField0276 = false;
               this.internalMethod09610();
               this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0034);
               return;
            }

            if (this.internalMethod08261()) {
               this.internalField1477 = this.internalMethod08259();
               this.internalField1100 = true;
               this.internalField0593 = null;
               this.internalField0276 = false;
               this.internalMethod09610();
               this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField1401);
               return;
            }

            this.internalField1100 = false;
            this.internalField0593 = null;
            break;
         case internalField0034:
            if (!this.internalMethod02907(this.internalMethod03856())) {
               return;
            }

            if (this.internalMethod03001(this.internalField0593) <= 0) {
               if (this.internalField0276) {
                  this.internalField1057 = this.internalMethod08259();
                  this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0966);
               } else {
                  this.internalMethod09529();
               }

               return;
            }

            if (!this.internalMethod03901("ah sellgui " + this.internalMethod03851())) {
               return;
            }

            this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0968);
            break;
         case internalField0968:
            if (this.internalMethod00553() != null) {
               this.internalField1053 = 0;
               this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0967);
            } else if (this.internalMethod02907(5000L)) {
               if (++this.internalField1053 <= 3) {
                  this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0034);
               } else {
                  this.internalField1053 = 0;
                  this.internalField1057 = this.internalMethod08259();
                  this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0966);
               }
            }
            break;
         case internalField0967:
            this.internalMethod08263();
            break;
         case internalField0965:
            this.internalMethod08272();
            break;
         case internalField0966:
            if (this.internalField0277) {
               this.internalField0277 = false;
               this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0034);
            } else if (this.internalMethod08259() - this.internalField1057 >= 8000L) {
               this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField1401);
            }
            break;
         case internalField1401:
            if (!this.internalMethod02907(this.internalMethod03856())) {
               return;
            }

            if (!this.internalMethod03901("ah")) {
               return;
            }

            this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField1400);
            break;
         case internalField1400:
            HandledScreen localValue1 = this.internalMethod00553();
            if (localValue1 != null) {
               int localValue2 = this.internalMethod02750(localValue1.getScreenHandler(), "\u0425\u0440\u0430\u043d\u0438\u043b\u0438\u0449\u0435");
               if (localValue2 != -1) {
                  if (this.internalMethod02907(this.internalMethod03856())) {
                     internalField0149.interactionManager.clickSlot(localValue1.getScreenHandler().syncId, localValue2, 0, SlotActionType.PICKUP, internalField0149.player);
                     this.internalField0228 = 0;
                     this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField1399);
                  }
               } else if (this.internalMethod02907(5000L)) {
                  this.internalMethod09521();
               }
            } else if (this.internalMethod02907(5000L)) {
               this.internalMethod09521();
            }
            break;
         case internalField1399:
            this.internalMethod08273();
      }
   }

   private void internalMethod08263() {
      HandledScreen localValue1 = this.internalMethod00553();
      if (localValue1 == null) {
         this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0034);
      } else if (this.internalMethod02907(this.internalMethod03856())) {
         ScreenHandler localValue2 = localValue1.getScreenHandler();
         int localValue3 = this.internalMethod07148(localValue2);
         if (localValue3 != -1 && this.internalMethod09066(localValue2) != -1) {
            this.internalMethod02637(localValue2, localValue3, Math.max(1, this.internalField0593.internalMethod00348()));
            this.internalMethod09611();
         } else {
            this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0965);
         }
      }
   }

   private void internalMethod02637(ScreenHandler localValue1, int localValue2, int localValue3) {
      int localValue4 = localValue3;

      for (int localValue5 = localValue3 + 64; localValue4 > 0 && localValue5-- > 0; localValue4--) {
         ItemStack localValue6 = localValue1.getCursorStack();
         if (!this.internalMethod03422(localValue6)) {
            int localValue7 = this.internalMethod09066(localValue1);
            if (localValue7 == -1) {
               break;
            }

            internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue7, 0, SlotActionType.PICKUP, internalField0149.player);
            localValue6 = localValue1.getCursorStack();
            if (localValue6.isEmpty()) {
               break;
            }
         }

         internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, 1, SlotActionType.PICKUP, internalField0149.player);
      }

      if (!localValue1.getCursorStack().isEmpty()) {
         int localValue9 = this.internalMethod07225(localValue1);
         if (localValue9 == -1) {
            localValue9 = this.internalMethod09066(localValue1);
         }

         if (localValue9 != -1) {
            internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue9, 0, SlotActionType.PICKUP, internalField0149.player);
         }
      }
   }

   private void internalMethod08272() {
      HandledScreen localValue1 = this.internalMethod00553();
      if (localValue1 == null) {
         this.internalField0276 = true;
         this.internalField1099 = true;
         this.internalField1057 = this.internalMethod08259();
         this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0966);
      } else if (this.internalMethod02907(this.internalMethod03856())) {
         int localValue2 = this.internalMethod02750(localValue1.getScreenHandler(), "\u041f\u043e\u0434\u0442\u0432\u0435\u0440\u0434\u0438\u0442\u044c");
         if (localValue2 != -1) {
            internalField0149.interactionManager.clickSlot(localValue1.getScreenHandler().syncId, localValue2, 0, SlotActionType.PICKUP, internalField0149.player);
            this.internalField0276 = true;
            this.internalField1099 = true;
            this.internalMethod09610();
            this.internalField1057 = this.internalMethod08259();
            this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0966);
         } else if (this.internalMethod02907(5000L)) {
            this.internalField0276 = true;
            this.internalField1099 = true;
            this.internalMethod09610();
            this.internalField1057 = this.internalMethod08259();
            this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0966);
         }
      }
   }

   private void internalMethod08273() {
      HandledScreen localValue1 = this.internalMethod00553();
      if (localValue1 == null) {
         this.internalMethod07498(
            this.internalField0593 == null ? AuctionSellAutomation.InternalType0281.internalField0035 : AuctionSellAutomation.InternalType0281.internalField0034
         );
      } else if (this.internalMethod02907(this.internalMethod03856())) {
         int localValue2 = this.internalMethod08882(localValue1.getScreenHandler());
         if (localValue2 != -1) {
            internalField0149.interactionManager.clickSlot(localValue1.getScreenHandler().syncId, localValue2, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
            this.internalField0228++;
            this.internalMethod09611();
         } else {
            this.internalMethod09610();
            if (this.internalField0593 == null) {
               this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0035);
            } else if (this.internalField0228 == 0 && this.internalMethod03001(this.internalField0593) <= 0) {
               this.internalMethod09529();
            } else {
               this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0034);
            }
         }
      }
   }

   private void internalMethod09521() {
      if (this.internalField0593 != null) {
         this.internalField1057 = this.internalMethod08259();
         this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0966);
      } else {
         this.internalMethod09529();
      }
   }

   private void internalMethod09529() {
      this.internalField0593 = null;
      this.internalField0276 = false;
      this.internalMethod07498(AuctionSellAutomation.InternalType0281.internalField0035);
   }

   private AuctionItem.InternalType0159 internalMethod01009() {
      for (AuctionItem.InternalType0159 localValue2 : AuctionItem.internalMethod00895()) {
         int localValue3 = this.internalField1100 ? 1 : localValue2.internalMethod00351();
         if (this.internalMethod03002(localValue2) > 0L && this.internalMethod03001(localValue2) >= localValue3) {
            return localValue2;
         }
      }

      return null;
   }

   private long internalMethod03851() {
      return Math.max(1L, this.internalMethod03002(this.internalField0593));
   }

   private long internalMethod03002(AuctionItem.InternalType0159 localValue1) {
      if (localValue1 == null) {
         return 0L;
      } else if (localValue1.internalMethod00352() > 0L) {
         return localValue1.internalMethod00352();
      } else {
         long localValue2 = this.internalField0737.applyAsLong(localValue1);
         return localValue2 > 0L ? localValue2 : 0L;
      }
   }

   public void internalMethod03900(String localValue1) {
      if (localValue1.contains("\u0423 \u0412\u0430\u0441 \u043a\u0443\u043f\u0438\u043b\u0438")) {
         this.internalField0277 = true;
      } else if (localValue1.contains("\u0441\u043b\u0438\u0448\u043a\u043e\u043c \u0434\u043e\u0440\u043e\u0433\u043e")) {
         this.internalMethod09529();
      }
   }

   private boolean internalMethod03422(ItemStack localValue1) {
      if (localValue1 == null || localValue1.isEmpty()) {
         return false;
      } else {
         return this.internalField0593 == null
            ? AuctionItem.internalMethod04294(localValue1) != null
            : AuctionItem.internalMethod07258(localValue1, this.internalField0593);
      }
   }

   private int internalMethod03001(AuctionItem.InternalType0159 localValue1) {
      PlayerInventory localValue2 = internalField0149.player.getInventory();
      int localValue3 = 0;

      for (int localValue4 = 0; localValue4 < localValue2.size(); localValue4++) {
         ItemStack localValue5 = localValue2.getStack(localValue4);
         if (AuctionItem.internalMethod07258(localValue5, localValue1)) {
            localValue3 += localValue5.getCount();
         }
      }

      return localValue3;
   }

   private int internalMethod07148(ScreenHandler localValue1) {
      for (Slot localValue3 : localValue1.slots) {
         if (localValue3.inventory != internalField0149.player.getInventory() && !localValue3.hasStack()) {
            return localValue3.id;
         }
      }

      return -1;
   }

   private int internalMethod07225(ScreenHandler localValue1) {
      for (Slot localValue3 : localValue1.slots) {
         if (localValue3.inventory == internalField0149.player.getInventory() && !localValue3.hasStack()) {
            return localValue3.id;
         }
      }

      return -1;
   }

   private int internalMethod09066(ScreenHandler localValue1) {
      for (Slot localValue3 : localValue1.slots) {
         if (localValue3.inventory == internalField0149.player.getInventory() && this.internalMethod03422(localValue3.getStack())) {
            return localValue3.id;
         }
      }

      return -1;
   }

   private int internalMethod08882(ScreenHandler localValue1) {
      for (Slot localValue3 : localValue1.slots) {
         if (localValue3.inventory != internalField0149.player.getInventory() && this.internalMethod03422(localValue3.getStack())) {
            return localValue3.id;
         }
      }

      return -1;
   }

   private int internalMethod02750(ScreenHandler localValue1, String localValue2) {
      for (Slot localValue4 : localValue1.slots) {
         if (localValue4.inventory != internalField0149.player.getInventory() && localValue4.hasStack() && localValue4.getStack().getName().getString().contains(localValue2)) {
            return localValue4.id;
         }
      }

      return -1;
   }

   private HandledScreen<?> internalMethod00553() {
      return internalField0149.currentScreen instanceof HandledScreen localValue1 ? localValue1 : null;
   }

   private void internalMethod09610() {
      if (internalField0149.currentScreen != null && internalField0149.player != null) {
         internalField0149.player.closeHandledScreen();
      }
   }

   private boolean internalMethod03901(String localValue1) {
      if (this.internalMethod08259() - this.internalField1473 < 900L) {
         return false;
      } else {
         this.internalMethod09610();
         internalField0149.player.networkHandler.sendChatCommand(localValue1);
         this.internalField1473 = this.internalMethod08259();
         return true;
      }
   }

   private long internalMethod03856() {
      return (long)(
         internalField0149.player.networkHandler.getPlayerListEntry(internalField0149.player.getUuid()).getLatency() * 2.5F
            + MathUtils.internalMethod05368(24.0, 59.0)
      );
   }

   private long internalMethod08259() {
      return System.currentTimeMillis();
   }

   private boolean internalMethod02907(long localValue1) {
      return this.internalMethod08259() - this.internalField1060 >= localValue1;
   }

   private void internalMethod09611() {
      this.internalField1060 = this.internalMethod08259();
   }

   private void internalMethod07498(AuctionSellAutomation.InternalType0281 localValue1) {
      this.internalField0035 = localValue1;
      this.internalMethod09611();
   }

   @Generated
   public AuctionSellAutomation.InternalType0281 internalMethod04252() {
      return this.internalField0035;
   }

   static enum InternalType0281 {
      internalField0035,
      internalField0034,
      internalField0968,
      internalField0967,
      internalField0965,
      internalField0966,
      internalField1401,
      internalField1400,
      internalField1399;
   }
}

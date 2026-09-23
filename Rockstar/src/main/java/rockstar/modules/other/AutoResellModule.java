package rockstar.modules.other;





import rockstar.client.util.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.ui.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.NotNull;
import pyrock.events.network.ReceivePacketEvent;

@ModuleInfo(
   name = "Auto Resell",
   category = ModuleCategory.OTHER,
   internalMethod09633 = "modules.descriptions.auto_resell"
)
public class AutoResellModule extends Module {
   private AutoResellModule.InternalType0021 internalField0643 = AutoResellModule.InternalType0021.internalField0642;
   private final Stopwatch internalField0519 = new Stopwatch();
   private long internalField0229 = 60000L;
   private final EventListener<ReceivePacketEvent> internalField0157 = localValue1 -> {
      if (localValue1.getPacket() instanceof GameMessageS2CPacket localValue2) {
         String localValue8 = localValue2.content().getString();
         if (localValue8.contains(
            "\u041f\u0440\u0435\u0434\u043c\u0435\u0442\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0435\u0440\u0435\u0432\u044b\u0441\u0442\u0430\u0432\u043b\u0435\u043d\u044b"
         )) {
            this.internalField0229 = 60000L;
            this.internalField0643 = AutoResellModule.InternalType0021.internalField1259;
            this.internalField0519.internalMethod00701();
         }

         if (localValue8.contains("\u041f\u043e\u0434\u043e\u0436\u0434\u0438\u0442\u0435") && localValue8.contains("\u0441\u0435\u043a")) {
            for (String localValue7 : localValue8.split(" ")) {
               if (localValue7.matches("\\d+")) {
                  this.internalField0229 = Integer.parseInt(localValue7) * 1000L + 500L;
                  this.internalField0643 = AutoResellModule.InternalType0021.internalField0643;
                  this.internalField0519.internalMethod00701();
                  break;
               }
            }
         }
      }
   };

   @Override
   public void internalMethod08229() {
      long localValue1 = (long)(
         internalField0149.player.networkHandler.getPlayerListEntry(internalField0149.player.getUuid()).getLatency() * 2.5F
            + MathUtils.internalMethod05368(24.0, 59.0)
      );
      switch (this.internalField0643) {
         case internalField0643:
            this.internalMethod09309();
            break;
         case internalField0642:
            this.internalMethod02187(localValue1);
            break;
         case internalField1258:
            this.internalMethod02242(localValue1);
            break;
         case internalField1259:
            this.internalMethod09316();
      }
   }

   private void internalMethod09309() {
      if (this.internalField0519.internalMethod02365(this.internalField0229)) {
         this.internalField0643 = AutoResellModule.InternalType0021.internalField0642;
         this.internalField0519.internalMethod00701();
      }
   }

   private void internalMethod09316() {
      if (this.internalField0519.internalMethod02365(500L)) {
         internalField0149.player.closeHandledScreen();
         this.internalField0643 = AutoResellModule.InternalType0021.internalField0643;
         this.internalField0519.internalMethod00701();
      }
   }

   private void internalMethod02187(long localValue1) {
      if (internalField0149.currentScreen instanceof HandledScreen localValue3) {
         if (localValue3.getTitle().getString().contains("\u0425\u0440\u0430\u043d\u0438\u043b\u0438\u0449\u0435")) {
            this.internalField0643 = AutoResellModule.InternalType0021.internalField1258;
            this.internalField0519.internalMethod00701();
         } else if (this.internalMethod03593(localValue3.getTitle().getString())) {
            if (this.internalField0519.internalMethod02365(localValue1 + 200L)) {
               internalField0149.interactionManager.clickSlot(localValue3.getScreenHandler().syncId, 46, 0, SlotActionType.PICKUP, internalField0149.player);
               this.internalField0519.internalMethod00701();
            }
         } else {
            if (this.internalField0519.internalMethod02365(localValue1 + 200L)) {
               internalField0149.player.closeHandledScreen();
               this.internalField0519.internalMethod00701();
            }
         }
      } else {
         if (this.internalField0519.internalMethod02365(localValue1 + 200L)) {
            internalField0149.player.networkHandler.sendChatCommand("ah");
            this.internalField0519.internalMethod00701();
         }
      }
   }

   private void internalMethod02242(long localValue1) {
      if (internalField0149.currentScreen instanceof HandledScreen localValue3
         && localValue3.getTitle().getString().contains("\u0425\u0440\u0430\u043d\u0438\u043b\u0438\u0449\u0435")) {
         if (this.internalField0519.internalMethod02365(localValue1 + 200L)) {
            internalField0149.interactionManager.clickSlot(localValue3.getScreenHandler().syncId, 52, 0, SlotActionType.PICKUP, internalField0149.player);
            this.internalField0519.internalMethod00701();
         }
      }
   }

   public boolean internalMethod03593(@NotNull String localValue1) {
      return AuctionTooltipParser.internalMethod08274(localValue1);
   }

   static enum InternalType0021 {
      internalField0643,
      internalField0642,
      internalField1258,
      internalField1259;
   }
}

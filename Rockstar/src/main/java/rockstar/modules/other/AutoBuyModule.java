package rockstar.modules.other;











import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.data.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.render.ScreenRenderEvent;
import pyrock.utility.render.CustomDrawContext;

@ModuleInfo(
   name = "Auto Buy",
   category = ModuleCategory.OTHER,
   internalMethod09633 = "modules.descriptions.auto_buy"
)
public class AutoBuyModule extends Module {
   private AutoBuyModule.InternalType0149 internalField0471;
   private static final long internalField0229 = 3000L;
   private final Stopwatch internalField0519;
   private final Stopwatch internalField0518;
   private long internalField1059;
   private AuctionItem.InternalType0159 internalField0593;
   private final AuctionParserService internalField0799;
   private final AuctionAutoParseController internalField0794;
   private final AuctionSellAutomation internalField0800;
   private final ScoreboardBalanceTracker internalField0801;
   private final ButtonSetting internalField0663;
   private final ButtonSetting internalField0662;
   private final Pattern internalField0293;
   private final List<AutoBuyModule.InternalType0475> internalField0416;
   private AutoBuyModule.InternalType0474 internalField0547;
   private final EventListener<ReceivePacketEvent> internalField0157;
   private final EventListener<ScreenRenderEvent> internalField0158;

   public AutoBuyModule() {
      this.internalField0471 = AutoBuyModule.InternalType0149.internalField0471;
      this.internalField0519 = new Stopwatch();
      this.internalField0518 = new Stopwatch();
      this.internalField1059 = System.currentTimeMillis();
      this.internalField0799 = AuctionParserService.internalMethod07210();
      this.internalField0794 = new AuctionAutoParseController();
      this.internalField0800 = new AuctionSellAutomation();
      this.internalField0801 = new ScoreboardBalanceTracker();
      this.internalField0663 = new ButtonSetting(this, "\u041e\u0442\u043a\u0440\u044b\u0442\u044c \u043c\u0435\u043d\u044e")
         .internalMethod07149(() -> internalField0149.setScreen(new AutoBuyScreen()));
      this.internalField0662 = new ButtonSetting(this, "\u041f\u0430\u0440\u0441\u0438\u0442\u044c \u0446\u0435\u043d\u044b")
         .internalMethod07149(this::internalMethod09212);
      this.internalField0293 = Pattern.compile(
         "\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043a\u0443\u043f\u0438\u043b\u0438\\s+(.+?)(?:\\s+x(\\d+))?\\s+\u0437\u0430\\s+\\$([\\d,]+)!",
         66
      );
      this.internalField0416 = new ArrayList<>();
      this.internalField0547 = AutoBuyModule.InternalType0474.internalField0547;
      this.internalField0157 = localValue1 -> {
         if (localValue1.getPacket() instanceof GameMessageS2CPacket localValue2) {
            String localValue13 = localValue2.content().getString();
            this.internalField0800.internalMethod03900(localValue13);
            this.internalField0794.internalMethod04652(localValue13);
            if (localValue13.contains("\u0423 \u0412\u0430\u0441 \u043a\u0443\u043f\u0438\u043b\u0438")) {
               String localValue4 = localValue13.substring(localValue13.indexOf("\u043a\u0443\u043f\u0438\u043b\u0438") + "\u043a\u0443\u043f\u0438\u043b\u0438".length()).trim();
               this.internalMethod03094(localValue4)
                  .ifPresent(localValue0 -> AuctionItem.internalMethod06853(localValue0.internalMethod00891(), Math.max(1, localValue0.internalMethod00348())));
            }

            if (localValue13.contains("\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043a\u0443\u043f\u0438\u043b\u0438")) {
               this.internalField1059 = System.currentTimeMillis();
               Matcher localValue14 = this.internalField0293.matcher(localValue13);
               if (localValue14.find()) {
                  String localValue5 = localValue14.group(1).trim();
                  String localValue6 = localValue14.group(2);
                  String localValue7 = localValue14.group(3);
                  int localValue8 = localValue6 != null ? Integer.parseInt(localValue6) : 1;
                  long localValue9 = Long.parseLong(localValue7.replace(",", ""));
                  AuctionItem.InternalType0159 localValue11 = this.internalField0593 != null ? this.internalField0593 : this.internalMethod03094(localValue5).orElse(null);
                  this.internalField0593 = null;
                  ItemStack localValue12 = localValue11 != null ? localValue11.internalMethod00723() : ItemStack.EMPTY;
                  if (localValue11 != null) {
                     AuctionItem.internalMethod04691(localValue11.internalMethod00891(), localValue8);
                  }

                  this.internalField0416.add(new AutoBuyModule.InternalType0475(localValue5, localValue8, localValue9, localValue12));
                  if (this.internalField0416.size() > 10) {
                     this.internalField0416.removeFirst();
                  }
               }

               this.internalField0471 = AutoBuyModule.InternalType0149.internalField0472;
               this.internalField0519.internalMethod00701();
            }
         }
      };
      this.internalField0158 = localValue1 -> {
         if (internalField0149.currentScreen instanceof HandledScreen localValue2 && AuctionTooltipParser.internalMethod08274(localValue2.getTitle().getString())) {
            UiRenderContext localValue4 = UiRenderContext.internalMethod02316(
               localValue1.getContext(),
               internalField0149.currentScreen == null ? -1 : (int)UiUtils.internalMethod03634().x(),
               internalField0149.currentScreen == null ? -1 : (int)UiUtils.internalMethod03634().y(),
               MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false)
            );
            this.internalMethod05306(localValue4);
         }
      };
   }

   private void internalMethod07838(boolean localValue1) {
      if (!AuctionAutoParseConfig.internalMethod05797()) {
         if (this.internalField0547 == AutoBuyModule.InternalType0474.internalField0548) {
            this.internalMethod09873();
         }
      } else if (!localValue1) {
         if (this.internalField0547 == AutoBuyModule.InternalType0474.internalField0547) {
            if (this.internalMethod09874() && this.internalField0800.internalMethod03858()) {
               this.internalField0547 = AutoBuyModule.InternalType0474.internalField0548;
               this.internalMethod09204();
            }
         } else {
            if (!this.internalField0800.internalMethod03858()) {
               this.internalMethod09873();
            }
         }
      }
   }

   private void internalMethod09873() {
      this.internalField0547 = AutoBuyModule.InternalType0474.internalField0547;
      this.internalField1059 = System.currentTimeMillis();
      this.internalField0471 = AutoBuyModule.InternalType0149.internalField0472;
      this.internalField0518.internalMethod00701();
   }

   private void internalMethod09875() {
      for (AuctionItem.InternalType0159 localValue2 : AuctionItem.internalMethod00895()) {
         if (localValue2.internalMethod05344() == AuctionItem.InternalType0158.internalField0591
            && !(this.internalField0799.internalMethod03919(localValue2.internalMethod00891()) > 0.0)) {
            ClientMessages.internalMethod03058(
               Text.of(
                  AuctionItem.internalMethod06551(localValue2)
                     + ": \u043d\u0435\u0442 \u0446\u0435\u043d\u044b \u0440\u044b\u043d\u043a\u0430 \u2014 \u00ab\u043d\u0438\u0436\u0435 \u0440\u044b\u043d\u043a\u0430\u00bb \u043d\u0435 \u0441\u0440\u0430\u0431\u043e\u0442\u0430\u0435\u0442, \u0441\u043f\u0430\u0440\u0441\u0438 \u0446\u0435\u043d\u044b"
               )
            );
         }
      }
   }

   private void internalMethod09204() {
      for (AuctionItem.InternalType0159 localValue2 : AuctionItem.internalMethod00895()) {
         if (localValue2.internalMethod00352() <= 0L
            && !(this.internalField0799.internalMethod03919(localValue2.internalMethod00891()) > 0.0)
            && AuctionItem.internalMethod03650(localValue2) > 0) {
            ClientMessages.internalMethod03058(
               Text.of(
                  AuctionItem.internalMethod06551(localValue2)
                     + ": \u043d\u0435\u0442 \u0446\u0435\u043d\u044b \u043f\u0440\u043e\u0434\u0430\u0436\u0438 \u2014 \u0441\u043f\u0430\u0440\u0441\u0438 \u0446\u0435\u043d\u044b \u0438\u043b\u0438 \u0437\u0430\u0434\u0430\u0439 \u00ab\u0426\u0435\u043d\u0430 \u043f\u0440\u043e\u0434\u0430\u0436\u0438\u00bb"
               )
            );
         }
      }
   }

   private Optional<AuctionItem.InternalType0159> internalMethod03094(String localValue1) {
      String localValue2 = localValue1 == null ? "" : localValue1.trim();
      return localValue2.isEmpty()
         ? Optional.empty()
         : AuctionItem.internalMethod00895()
            .stream()
            .filter(
               localValue1x -> localValue1x.internalMethod04343() != null
                  ? internalMethod06475(localValue1x.internalMethod04343(), localValue2)
                  : internalMethod06475(ItemNbtUtils.internalMethod02181(localValue1x.internalMethod00723()), localValue2)
                     || internalMethod06475(ItemNbtUtils.internalMethod06695(localValue1x.internalMethod00723()), localValue2)
            )
            .findFirst();
   }

   private static boolean internalMethod06475(String localValue0, String localValue1) {
      String localValue2 = localValue0 == null ? "" : localValue0.trim();
      return localValue2.isEmpty() ? false : localValue2.equalsIgnoreCase(localValue1) || localValue1.contains(localValue2) || localValue2.contains(localValue1);
   }

   private boolean internalMethod09874() {
      List localValue1 = AuctionItem.internalMethod00895();
      if (localValue1.isEmpty()) {
         return true;
      } else {
         for (AuctionItem.InternalType0159 localValue3 : (Iterable<AuctionItem.InternalType0159>)(Iterable<?>)localValue1) {
            if (!AuctionItem.internalMethod03651(localValue3)) {
               return System.currentTimeMillis() - this.internalField1059 >= 90000L;
            }
         }

         return true;
      }
   }

   private void internalMethod09206() {
      if (this.internalField0794.internalMethod02913() != AuctionAutoParseController.InternalType0160.internalField1227) {
         if (internalField0149.currentScreen == null || internalField0149.currentScreen instanceof HandledScreen) {
            if (this.internalField0518.internalMethod02365(3000L)) {
               internalField0149.player.networkHandler.sendChatCommand("ah");
               this.internalField0518.internalMethod00701();
            }
         }
      }
   }

   private void internalMethod09212() {
      if (this.internalField0799.internalMethod08858()) {
         this.internalField0794.internalMethod05952(true);
      }
   }

   @Override
   public void internalMethod08229() {
      boolean localValue1 = this.internalField0799.internalMethod02126();
      this.internalMethod07838(localValue1);
      boolean localValue2 = this.internalField0547 == AutoBuyModule.InternalType0474.internalField0548;
      boolean localValue3 = localValue2 || localValue1;
      if (localValue3 != this.internalField0794.internalMethod02635()) {
         this.internalField0794.internalMethod05952(localValue3);
      }

      if (localValue2) {
         this.internalField0800.internalMethod03857();
      } else if (!localValue1) {
         if (this.internalField0799.internalMethod02128()) {
            this.internalMethod09875();
            this.internalField0471 = AutoBuyModule.InternalType0149.internalField0472;
            this.internalField0519.internalMethod00701();
            this.internalField0518.internalMethod00701();
         } else if (this.internalField0799.internalMethod08859()) {
            this.internalField0794.internalMethod05952(true);
         } else {
            this.internalField0794.internalMethod02634();
            if (internalField0149.currentScreen instanceof HandledScreen localValue4 && AuctionTooltipParser.internalMethod08274(localValue4.getTitle().getString())) {
               this.internalField0518.internalMethod00701();
               long localValue21 = (long)(
                  internalField0149.player.networkHandler.getPlayerListEntry(internalField0149.player.getUuid()).getLatency() * 2.5F
                     + MathUtils.internalMethod05368(24.0, 59.0)
               );
               AuctionTooltipParser.InternalType0248 localValue7 = AuctionTooltipParser.internalMethod06312(localValue4, false, null);
               AuctionTooltipParser.InternalType0247 localValue8 = null;

               for (AuctionTooltipParser.InternalType0247 localValue10 : localValue7.internalMethod05468()) {
                  long localValue11 = localValue10.internalMethod00226();
                  int localValue13 = Math.max(1, localValue10.internalMethod00227());
                  long localValue14 = localValue11 / localValue13;
                  double localValue16 = localValue10.internalMethod00224() / localValue13;
                  double localValue18 = this.internalField0799.internalMethod06357().internalMethod01977(localValue10);
                  if (this.internalField0801.internalMethod05965(localValue11)) {
                     AuctionTooltipParser.InternalType0247 localValue20 = new AuctionTooltipParser.InternalType0247(
                        localValue10.internalMethod00225(), localValue10.internalMethod02415(), localValue14, localValue13, localValue10.internalMethod08632(), localValue10.internalMethod08634(), localValue16
                     );
                     if (AuctionItem.internalMethod05703(localValue20, localValue18) && (localValue8 == null || localValue20.internalMethod00224() < localValue8.internalMethod00224())) {
                        localValue8 = localValue20;
                     }
                  }
               }

               switch (this.internalField0471) {
                  case internalField0471:
                  default:
                     break;
                  case internalField0472:
                     this.internalMethod00860(localValue4, localValue21, localValue8);
                     break;
                  case internalField1167:
                     this.internalMethod03188(localValue4, localValue21, localValue8);
               }
            } else {
               this.internalMethod09206();
            }
         }
      }
   }

   private void internalMethod00860(HandledScreen<?> localValue1, long localValue2, AuctionTooltipParser.InternalType0247 localValue4) {
      if (localValue4 != null) {
         this.internalField0471 = AutoBuyModule.InternalType0149.internalField1167;
         this.internalField0519.internalMethod00701();
      } else {
         if (this.internalField0519.internalMethod02365(localValue2 + 200L)) {
            internalField0149.interactionManager.clickSlot(localValue1.getScreenHandler().syncId, 49, 0, SlotActionType.PICKUP, internalField0149.player);
            this.internalField0519.internalMethod00701();
         }
      }
   }

   private void internalMethod03188(HandledScreen<?> localValue1, long localValue2, AuctionTooltipParser.InternalType0247 localValue4) {
      if (localValue4 == null) {
         this.internalField0471 = AutoBuyModule.InternalType0149.internalField0472;
         this.internalField0519.internalMethod00701();
      } else if (!AuctionItem.internalMethod05703(localValue4, this.internalField0799.internalMethod06357().internalMethod01977(localValue4))) {
         this.internalField0471 = AutoBuyModule.InternalType0149.internalField0472;
         this.internalField0519.internalMethod00701();
      } else if (!this.internalField0801.internalMethod05965(localValue4.internalMethod00226() * Math.max(1, localValue4.internalMethod00227()))) {
         this.internalField0471 = AutoBuyModule.InternalType0149.internalField0472;
         this.internalField0519.internalMethod00701();
      } else {
         if (this.internalField0519.internalMethod02365(localValue2)) {
            this.internalField0593 = AuctionItem.internalMethod04294(localValue4.internalMethod02415());
            internalField0149.interactionManager
               .clickSlot(localValue1.getScreenHandler().syncId, localValue4.internalMethod00225(), 0, SlotActionType.QUICK_MOVE, internalField0149.player);
            this.internalField0519.internalMethod00701();
            this.internalField0471 = AutoBuyModule.InternalType0149.internalField0472;
         }
      }
   }

   private void internalMethod05306(CustomDrawContext localValue1) {
      List localValue2 = this.internalMethod04335();
      short localValue3 = 270;
      int localValue4 = (int)(internalField0389.internalMethod03589() / 2.0F) - 105;
      byte localValue5 = 16;
      localValue1.drawClientRect(localValue3, localValue4 - 5, 120.0F, localValue2.size() * 20 + 8, 255.0F, 1.0F, 7.0F, 6.0F);
      byte localValue6 = 0;

      for (AutoBuyModule.InternalType0475 localValue8 : (Iterable<AutoBuyModule.InternalType0475>)(Iterable<?>)localValue2) {
         localValue1.drawItem(localValue8.internalMethod02102(), (float)(localValue3 + 4), (float)(localValue4 + localValue6), 1.0F);
         localValue1.drawText(Fonts.internalField0449.internalMethod01432(7.0F), Text.of(localValue8.internalMethod00336()), localValue3 + localValue5 + 6, localValue4 + localValue6 + 2);
         localValue1.drawText(
            Fonts.internalField1154.internalMethod01432(7.0F),
            Text.of(MathUtils.internalMethod00005(localValue8.internalMethod07393()) + " x" + localValue8.internalMethod07392()),
            localValue3 + localValue5 + 6,
            localValue4 + localValue6 + 10
         );
         localValue6 += 20;
      }
   }

   @Override
   public void onEnable() {
      this.internalField0800.internalMethod02050(localValue1 -> (long)this.internalField0799.internalMethod03919(localValue1.internalMethod00891()));
      this.internalField1059 = System.currentTimeMillis();
      this.internalField0518.internalMethod00701();
      this.internalField0547 = AutoBuyModule.InternalType0474.internalField0547;
      this.internalField0471 = AutoBuyModule.InternalType0149.internalField0472;
      this.internalMethod09875();
      this.internalField0794.internalMethod08845();
      super.onEnable();
   }

   @Override
   public void onDisable() {
      this.internalField0794.internalMethod02636();
      this.internalField0800.internalMethod03852();
      this.internalField0801.internalMethod07056();
      AuctionItem.internalMethod07503();
      this.internalField0593 = null;
      this.internalField0547 = AutoBuyModule.InternalType0474.internalField0547;
      this.internalField0471 = AutoBuyModule.InternalType0149.internalField0471;
      super.onDisable();
   }

   @Generated
   public List<AutoBuyModule.InternalType0475> internalMethod04335() {
      return this.internalField0416;
   }

   static enum InternalType0149 {
      internalField0471,
      internalField0472,
      internalField1167;
   }

   static enum InternalType0474 {
      internalField0547,
      internalField0548;
   }

   public static final class InternalType0475 {
      private final String internalField0248;
      private final int internalField0227;
      private final long internalField0229;
      private final ItemStack internalField0878;

      public InternalType0475(String localValue1, int localValue2, long localValue3, ItemStack localValue5) {
         this.internalField0248 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0229 = localValue3;
         this.internalField0878 = localValue5;
      }

      @Override
      public final String toString() {
         return "InternalType0475[name=" + this.internalField0248 + ", amount=" + this.internalField0227 + ", price=" + this.internalField0229 + ", stack=" + this.internalField0878 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0878);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AutoBuyModule.InternalType0475 other = (AutoBuyModule.InternalType0475) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229)
            && java.util.Objects.equals(this.internalField0878, other.internalField0878);
      }

      public String internalMethod00336() {
         return this.internalField0248;
      }

      public int internalMethod07392() {
         return this.internalField0227;
      }

      public long internalMethod07393() {
         return this.internalField0229;
      }

      public ItemStack internalMethod02102() {
         return this.internalField0878;
      }
   }
}

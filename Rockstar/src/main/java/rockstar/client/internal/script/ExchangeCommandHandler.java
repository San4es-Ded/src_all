package rockstar.client.internal.script;








import rockstar.client.util.*;
import rockstar.client.server.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.*;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.window.KeyPressEvent;

public class ExchangeCommandHandler implements MinecraftClientAccess {
   private static final int[][] internalField0040 = new int[][]{{17, 50}, {16, 25}, {15, 10}, {14, 5}};
   private static final Pattern internalField0293 = Pattern.compile("\u0412\u0430\u0448 \u0431\u0430\u043b\u0430\u043d\u0441\\D*(\\d[\\d\\s.,]*)", 66);
   private static final Pattern internalField0294 = Pattern.compile(
      "\u041e\u0436\u0438\u0434\u0430\u0435\u0442\u0441\u044f \u043a\u043e\u0438\u043d\u043e\u0432\\D*(\\d[\\d\\s.,]*)", 66
   );
   private static final Pattern internalField1112 = Pattern.compile("\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c\\D*(\\d[\\d\\s.,]*)", 66);
   private boolean internalField0277;
   private int internalField1465;
   private int internalField1463;
   private int internalField1466;
   private int internalField1467;
   private int internalField1469;
   private int internalField1468;
   private int internalField1740;
   private int internalField1741;
   private boolean internalField0276;
   private boolean internalField1099;
   private int internalField1736;
   private int internalField1735;
   private int internalField1748;
   private int internalField1733;
   private int internalField1738;
   private int internalField1739;
   private final Stopwatch internalField0519 = new Stopwatch();
   private final Stopwatch internalField0518 = new Stopwatch();
   private final EventListener<KeyPressEvent> internalField0157 = localValue1 -> {
      if (this.internalField0277) {
         if (localValue1.getAction() == 1 && localValue1.getKey() == 256) {
            this.internalMethod03273("commands.exchange.cancelled", false, this.internalField1463);
         }
      }
   };
   private final EventListener<ClientPlayerTickEvent> internalField0158 = localValue1 -> {
      if (this.internalField0277) {
         if (internalField0149.player != null && internalField0149.world != null && internalField0149.interactionManager != null) {
            GenericContainerScreenHandler localValue2 = internalField0149.currentScreen != null
                  && internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue3
               ? localValue3
               : null;
            if (localValue2 != null) {
               String localValue5 = this.internalMethod03884(internalField0149.currentScreen.getTitle().getString());
               if (localValue5.contains("\u0431\u0438\u0440\u0436\u0430")) {
                  this.internalField1738 = 0;
                  this.internalMethod04765(localValue2);
               } else if (localValue5.contains("\u043f\u043e\u043a\u0443\u043f\u043a\u0430")) {
                  this.internalField1738 = 0;
                  this.internalMethod05926(localValue2);
               } else if (this.internalField0518.internalMethod02365(8000L)) {
                  this.internalMethod03273("commands.exchange.no_menu", true);
               }
            } else if (!this.internalField1099 && this.internalField1465 > 0 && this.internalField1463 >= this.internalField1465) {
               this.internalMethod07381();
            } else if (this.internalField0518.internalMethod02365(1200L)) {
               if (this.internalField1738 >= 3) {
                  if (this.internalField1463 > 0) {
                     this.internalMethod07381();
                  } else {
                     this.internalMethod03273("commands.exchange.no_menu", true);
                  }
               } else {
                  this.internalField1738++;
                  this.internalField0518.internalMethod00701();
                  this.internalField0519.internalMethod00701();
                  internalField0149.player.networkHandler.sendChatCommand("exchange");
               }
            }
         } else {
            this.internalField0277 = false;
         }
      }
   };

   public ExchangeCommandHandler() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   public CommandNode internalMethod01559() {
      return CommandBuilder.internalMethod07482(
            "exchange", localValue1 -> localValue1.internalMethod05325("exc").internalMethod06148("commands.exchange.description").internalMethod01539("args", localValue0 -> {
               localValue0.internalMethod06921().internalMethod00125().internalMethod00776(OperationResult::internalMethod00116);
               localValue0.internalMethod07138("stop", "dump");
            }).internalMethod00262(this::internalMethod01098)
         )
         .internalMethod04146();
   }

   private void internalMethod01098(ParsedCommand localValue1) {
      if (internalField0149.player != null && internalField0149.world != null) {
         List localValue2 = !localValue1.internalMethod02266().isEmpty() && localValue1.internalMethod02266().getFirst() != null ? (List)localValue1.internalMethod02266().getFirst() : List.of();
         String localValue3 = localValue2.isEmpty() ? "" : ((String)localValue2.getFirst()).toLowerCase(Locale.ROOT);
         if (!localValue3.equals("stop") && !localValue3.equals("cancel")) {
            if (localValue3.equals("dump")) {
               this.internalMethod07388();
            } else {
               int localValue4 = 0;
               if (!localValue3.isEmpty()) {
                  try {
                     localValue4 = Integer.parseInt(localValue3);
                  } catch (NumberFormatException localValue6) {
                     localValue4 = -1;
                  }

                  if (localValue4 <= 0) {
                     ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.exchange.usage")));
                     return;
                  }
               }

               if (!ServerUtils.internalMethod08700()) {
                  ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.exchange.only_hw")));
               } else {
                  this.internalMethod07484(localValue4);
               }
            }
         } else {
            if (this.internalField0277) {
               this.internalMethod03273("commands.exchange.cancelled", false, this.internalField1463);
            } else {
               ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.exchange.not_active")));
            }
         }
      }
   }

   private void internalMethod07484(int localValue1) {
      this.internalField0277 = true;
      this.internalField1465 = localValue1;
      this.internalField1463 = 0;
      this.internalField1466 = 0;
      this.internalField1467 = 0;
      this.internalField1469 = -1;
      this.internalField1468 = 0;
      this.internalField1740 = 0;
      this.internalField1741 = 0;
      this.internalField0276 = false;
      this.internalField1099 = false;
      this.internalField1736 = 0;
      this.internalField1735 = 0;
      this.internalField1748 = 0;
      this.internalField1733 = 0;
      this.internalField1738 = 0;
      this.internalField1739 = 0;
      this.internalField0519.internalMethod00701();
      this.internalField0518.internalMethod00701();
      internalField0149.player.networkHandler.sendChatCommand("exchange");
      ClientMessages.internalMethod01809(
         Text.of(
            this.internalField1465 > 0
               ? LanguageManager.internalMethod00160("commands.exchange.started_limit", this.internalField1465)
               : LanguageManager.internalMethod07214("commands.exchange.started")
         )
      );
   }

   private void internalMethod04765(GenericContainerScreenHandler localValue1) {
      int localValue2 = this.internalMethod04764(localValue1);
      int localValue3 = -1;
      Integer localValue4 = null;
      this.internalField1468 = 0;

      for (int localValue5 = 0; localValue5 < localValue2; localValue5++) {
         ItemStack localValue6 = localValue1.getSlot(localValue5).getStack();
         if (!localValue6.isEmpty()) {
            String localValue7 = this.internalMethod02795(localValue6.getName().getString());
            if (localValue4 == null) {
               Integer localValue8 = this.internalMethod06331(internalField0293, localValue7);
               if (localValue8 != null) {
                  localValue4 = localValue8;
                  localValue3 = localValue5;
                  continue;
               }
            }

            Integer localValue12 = this.internalMethod06331(internalField0294, localValue7);
            if (localValue12 != null) {
               this.internalField1468 = localValue12;
            }
         }
      }

      if (localValue4 == null) {
         if (this.internalField0518.internalMethod02365(8000L)) {
            this.internalMethod03273("commands.exchange.no_balance", true);
         }
      } else {
         int localValue9 = localValue4;
         if (this.internalField1099) {
            if (localValue9 != this.internalField1469) {
               int localValue10 = Math.max(0, this.internalField1469 - localValue9);
               this.internalField1463 += localValue10;
               if (localValue10 > 0) {
                  this.internalField1466++;
                  this.internalField1739 = 0;
               } else {
                  this.internalField1739++;
               }

               this.internalField1099 = false;
               this.internalField1733 = 0;
               this.internalField0518.internalMethod00701();
            } else {
               if (!this.internalField0519.internalMethod02365(900L)) {
                  return;
               }

               if (this.internalField1733 < 4 && localValue3 >= 0) {
                  this.internalField1733++;
                  this.internalMethod06676(localValue1, localValue3, 0);
                  return;
               }

               this.internalField1099 = false;
               this.internalField1733 = 0;
               this.internalField1739++;
               this.internalField0518.internalMethod00701();
            }
         }

         this.internalField1467 = localValue9;
         if (this.internalField1739 >= 2) {
            this.internalMethod03273("commands.exchange.stalled", true, this.internalField1463);
         } else {
            int localValue11 = this.internalField1465 > 0 ? Math.min(this.internalField1465 - this.internalField1463, this.internalField1467) : this.internalField1467;
            if (this.internalField1468 > 0 && (this.internalField1740 <= 0 || this.internalField1468 >= this.internalField1740)) {
               localValue11 = Math.min(localValue11, this.internalField1468);
            }

            if (this.internalField1741 > 1) {
               localValue11 -= localValue11 % this.internalField1741;
            }

            if (localValue11 <= 0 || this.internalField1740 > 0 && localValue11 < this.internalField1740) {
               this.internalMethod07381();
            } else if (this.internalField0519.internalMethod02365(150L)) {
               if (!this.internalField0276 || this.internalField0518.internalMethod02365(8000L)) {
                  this.internalField0276 = false;
                  if (localValue1.getSlot(0).hasStack()) {
                     this.internalField1733 = 0;
                     this.internalField1469 = this.internalField1467;
                     this.internalField1735 = localValue11;
                     this.internalField1736 = 0;
                     this.internalField1748 = 0;
                     this.internalField0276 = true;
                     this.internalMethod06676(localValue1, 0, 1);
                  } else if (this.internalField1733 < 4 && localValue3 >= 0) {
                     this.internalField1733++;
                     this.internalMethod06676(localValue1, localValue3, 0);
                  } else {
                     this.internalMethod03273("commands.exchange.empty", false, this.internalField1463);
                  }
               }
            }
         }
      }
   }

   private void internalMethod05926(GenericContainerScreenHandler localValue1) {
      this.internalField0276 = false;
      if (this.internalField1099) {
         if (this.internalField0518.internalMethod02365(8000L)) {
            this.internalField0518.internalMethod00701();
            internalField0149.player.closeHandledScreen();
         }
      } else if (this.internalField0519.internalMethod02365(150L)) {
         int localValue2 = this.internalMethod04764(localValue1);
         int localValue3 = this.internalField1735 - this.internalField1736;
         int localValue4 = -1;
         int localValue5 = 0;
         int localValue6 = 0;
         int localValue7 = 0;
         boolean localValue8 = false;

         for (int localValue9 = 0; localValue9 < localValue2; localValue9++) {
            ItemStack localValue10 = localValue1.getSlot(localValue9).getStack();
            if (!localValue10.isEmpty()) {
               Integer localValue11 = this.internalMethod06331(internalField1112, this.internalMethod02795(localValue10.getName().getString()));
               if (localValue11 != null && localValue11 > 0) {
                  localValue8 = true;
                  localValue7 = this.internalMethod02306(localValue7, localValue11);
                  if (localValue6 == 0 || localValue11 < localValue6) {
                     localValue6 = localValue11;
                  }

                  if (localValue11 <= localValue3 && localValue11 > localValue5) {
                     localValue5 = localValue11;
                     localValue4 = localValue9;
                  }
               }
            }
         }

         if (!localValue8) {
            for (int[] localValue12 : internalField0040) {
               int localValue13 = localValue12[0];
               int localValue14 = localValue12[1];
               if (localValue13 < localValue2 && localValue1.getSlot(localValue13).hasStack()) {
                  localValue7 = this.internalMethod02306(localValue7, localValue14);
                  if (localValue6 == 0 || localValue14 < localValue6) {
                     localValue6 = localValue14;
                  }

                  if (localValue14 <= localValue3 && localValue14 > localValue5) {
                     localValue5 = localValue14;
                     localValue4 = localValue13;
                  }
               }
            }
         }

         if (localValue6 > 0) {
            this.internalField1740 = localValue6;
            this.internalField1741 = localValue7;
         }

         if (localValue4 >= 0 && this.internalField1748 < 64) {
            this.internalField1736 += localValue5;
            this.internalField1748++;
            this.internalMethod06676(localValue1, localValue4, 0);
         } else if (this.internalField1736 <= 0) {
            this.internalMethod03273("commands.exchange.empty", false, this.internalField1463);
            internalField0149.player.closeHandledScreen();
         } else {
            int localValue16 = this.internalMethod05345(localValue1, localValue2);
            if (localValue16 < 0) {
               this.internalMethod03273("commands.exchange.no_confirm", true, this.internalField1463);
            } else {
               this.internalField1099 = true;
               this.internalMethod06676(localValue1, localValue16, 0);
            }
         }
      }
   }

   private int internalMethod05345(GenericContainerScreenHandler localValue1, int localValue2) {
      if (13 < localValue2 && localValue1.getSlot(13).hasStack()) {
         return 13;
      } else {
         for (int localValue3 = 0; localValue3 < localValue2; localValue3++) {
            ItemStack localValue4 = localValue1.getSlot(localValue3).getStack();
            if (!localValue4.isEmpty()) {
               String localValue5 = this.internalMethod03884(localValue4.getName().getString());
               if (localValue5.contains("\u043a\u0443\u043f\u0438\u0442\u044c")
                  || localValue5.contains("\u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0434\u0438\u0442\u044c")
                  || localValue5.contains("\u043e\u0431\u043c\u0435\u043d\u044f\u0442\u044c")
                  || localValue5.contains("\u043e\u0444\u043e\u0440\u043c\u0438\u0442\u044c")) {
                  return localValue3;
               }
            }
         }

         return -1;
      }
   }

   private void internalMethod06676(GenericContainerScreenHandler localValue1, int localValue2, int localValue3) {
      if (internalField0149.interactionManager != null && localValue2 >= 0 && localValue2 < localValue1.slots.size()) {
         internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, localValue3, SlotActionType.PICKUP, internalField0149.player);
         this.internalField0519.internalMethod00701();
         this.internalField0518.internalMethod00701();
      }
   }

   private void internalMethod07381() {
      if (this.internalField1465 > 0) {
         this.internalMethod03273("commands.exchange.finished_limit", false, this.internalField1463, this.internalField1465, this.internalField1466);
      } else {
         this.internalMethod03273("commands.exchange.finished", false, this.internalField1463, this.internalField1466);
      }
   }

   private void internalMethod03273(String localValue1, boolean localValue2, Object... localValue3) {
      this.internalField0277 = false;
      Text localValue4 = Text.of(localValue3.length == 0 ? LanguageManager.internalMethod07214(localValue1) : LanguageManager.internalMethod00160(localValue1, localValue3));
      if (localValue2) {
         ClientMessages.internalMethod09025(localValue4);
      } else {
         ClientMessages.internalMethod01809(localValue4);
      }
   }

   private int internalMethod04764(GenericContainerScreenHandler localValue1) {
      return Math.max(0, localValue1.slots.size() - 36);
   }

   private Integer internalMethod06331(Pattern localValue1, String localValue2) {
      if (localValue2 == null) {
         return null;
      } else {
         Matcher localValue3 = localValue1.matcher(localValue2);
         if (!localValue3.find()) {
            return null;
         } else {
            String localValue4 = localValue3.group(1).replaceAll("\\D", "");
            return !localValue4.isEmpty() && localValue4.length() <= 9 ? Integer.parseInt(localValue4) : null;
         }
      }
   }

   private int internalMethod02306(int localValue1, int localValue2) {
      while (localValue2 != 0) {
         int localValue3 = localValue1 % localValue2;
         localValue1 = localValue2;
         localValue2 = localValue3;
      }

      return localValue1;
   }

   private String internalMethod02795(String localValue1) {
      return localValue1 == null ? "" : localValue1.replaceAll("\u00a7.", "");
   }

   private String internalMethod03884(String localValue1) {
      return this.internalMethod02795(localValue1).replace('\u0451', '\u0435').replace('\u0401', '\u0415').toLowerCase(Locale.ROOT);
   }

   private void internalMethod07388() {
      if (internalField0149.currentScreen != null && internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue1) {
         RockstarClient.internalField0572
            .info("[Exchange] '{}', \u0441\u043b\u043e\u0442\u043e\u0432 {}", internalField0149.currentScreen.getTitle().getString(), localValue1.slots.size());
         int localValue9 = this.internalMethod04764(localValue1);

         for (int localValue3 = 0; localValue3 < localValue9; localValue3++) {
            ItemStack localValue4 = localValue1.getSlot(localValue3).getStack();
            if (!localValue4.isEmpty()) {
               StringBuilder localValue5 = new StringBuilder();

               try {
                  for (Text localValue7 : localValue4.getTooltip(TooltipContext.create(internalField0149.world), internalField0149.player, TooltipType.BASIC)) {
                     localValue5.append(" | ").append(localValue7.getString());
                  }
               } catch (Exception localValue8) {
               }

               RockstarClient.internalField0572.info("[Exchange] {} x{} {}{}", new Object[]{localValue3, localValue4.getCount(), localValue4.getName().getString(), localValue5});
            }
         }

         ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("commands.exchange.dumped")));
      } else {
         ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.exchange.no_screen")));
      }
   }
}

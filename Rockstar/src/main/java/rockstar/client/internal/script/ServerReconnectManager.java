package rockstar.client.internal.script;











import rockstar.client.util.*;
import rockstar.client.server.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import java.util.ArrayList;
import java.util.List;
import moscow.rockstar.mixin.accessors.PlayerListHudAccessor;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.world.Difficulty;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;

public class ServerReconnectManager implements MinecraftClientAccess {
   private static final SlotCollection<InventorySlot> internalField0221 = InventorySlots.internalMethod03558()
      .internalMethod07591(InventorySlots.internalMethod02872())
      .internalMethod07591(InventorySlots.internalMethod07766());
   private final ServerReconnectManager.InternalType0195 internalField0068 = new ServerReconnectManager.InternalType0195();
   private final Stopwatch internalField0519 = new Stopwatch();
   private final EventListener<ClientPlayerTickEvent> internalField0157 = localValue1 -> {
      if (this.internalField0068.internalField0277 && internalField0149.world != null && internalField0149.player != null) {
         if (ServerUtils.internalMethod08700()) {
            this.internalMethod07936();
         } else if (ServerUtils.internalMethod01786(KnownServer.internalField0579)
            || ServerUtils.internalMethod01786(KnownServer.internalField0578)) {
            this.internalMethod00923();
         } else if (ServerUtils.internalMethod01786(KnownServer.internalField1220)) {
            this.internalMethod00925();
         }
      }
   };
   private final EventListener<ReceivePacketEvent> internalField0158 = localValue1 -> {
      if (this.internalField0068.internalField0277 && internalField0149.player != null && internalField0149.world != null) {
         if (ServerUtils.internalMethod08700() && !(localValue1.getPacket() instanceof OpenScreenS2CPacket)) {
            InventorySlot localValue2 = internalField0221.internalMethod03297(localValue0 -> localValue0.getItem() == Items.COMPASS);
            if (localValue2 != null) {
               if (this.internalField0068.internalField0067 == ServerReconnectManager.InternalType0194.internalField0066) {
                  if (!this.internalField0068.internalField1102) {
                     this.internalField0068.internalField1102 = true;
                     this.internalMethod07923();
                     internalField0149.player.networkHandler.sendChatCommand("lite");
                  }
               } else if (!this.internalField0068.internalField1099) {
                  this.internalField0068.internalField1099 = true;
                  internalField0149.player.networkHandler.sendChatCommand("menu");
               }
            }
         }

         if (ServerUtils.internalMethod08700() && localValue1.getPacket() instanceof OpenScreenS2CPacket localValue4) {
            String localValue5 = localValue4.getName().getString();
            if (this.internalField0068.internalField0067 == ServerReconnectManager.InternalType0194.internalField0066
               && localValue5.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0440\u0435\u0436\u0438\u043c")) {
               this.internalField0068.internalField0255 = ServerReconnectManager.InternalType0292.internalField0256;
            } else if (this.internalField0068.internalField0067 == ServerReconnectManager.InternalType0194.internalField0066
               && localValue5.contains("\u0412\u044b\u0431\u043e\u0440 \u041b\u0430\u0439\u0442 \u0430\u043d\u0430\u0440\u0445\u0438\u0438")
               && this.internalField0068.internalField0255 != ServerReconnectManager.InternalType0292.internalField1087) {
               this.internalMethod07923();
            } else if (this.internalField0068.internalField0067 == ServerReconnectManager.InternalType0194.internalField0975
               && localValue5.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0440\u0435\u0436\u0438\u043c")) {
               this.internalField0068.internalField0255 = ServerReconnectManager.InternalType0292.internalField1086;
            } else if (this.internalField0068.internalField0067 == ServerReconnectManager.InternalType0194.internalField0975
               && localValue5.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0441\u0435\u0440\u0432\u0435\u0440 \u041b\u0430\u0439\u0442")) {
               this.internalField0068.internalField0255 = ServerReconnectManager.InternalType0292.internalField1085;
            } else if (this.internalField0068.internalField0067 == ServerReconnectManager.InternalType0194.internalField0976
               && localValue5.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0440\u0435\u0436\u0438\u043c")) {
               this.internalField0068.internalField0255 = ServerReconnectManager.InternalType0292.internalField1084;
            } else if (this.internalField0068.internalField0067 == ServerReconnectManager.InternalType0194.internalField0976
               && localValue5.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u041a\u043b\u0430\u0441\u0441\u0438\u0447")) {
               this.internalField0068.internalField0255 = ServerReconnectManager.InternalType0292.internalField1503;
            }
         }
      }
   };

   public ServerReconnectManager() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   public final CommandNode internalMethod06043() {
      return CommandBuilder.internalMethod00593("rct")
         .internalMethod05325("reconnect")
         .internalMethod06148("commands.rehub.description")
         .internalMethod01539("args", localValue0 -> localValue0.internalMethod06921().internalMethod00125().internalMethod00776(OperationResult::internalMethod00116))
         .internalMethod00262(this::internalMethod03830)
         .internalMethod04146();
   }

   private void internalMethod03830(ParsedCommand localValue1) {
      if (internalField0149.player != null && internalField0149.world != null) {
         List localValue2 = localValue1.internalMethod02266().isEmpty() ? List.of() : this.internalMethod00404(localValue1.internalMethod02266().getFirst());
         ServerReconnectManager.InternalType0293 localValue3 = ServerReconnectManager.InternalType0293.internalField0257;
         Integer localValue4 = null;
         if (!localValue2.isEmpty()) {
            ServerReconnectManager.InternalType0293 localValue5 = ServerReconnectManager.InternalType0293.internalMethod03536((String)localValue2.getFirst());
            int localValue6 = 0;
            if (localValue5 != null) {
               localValue3 = localValue5;
               localValue6++;
            }

            if (localValue6 < localValue2.size()) {
               localValue4 = this.internalMethod03307((String)localValue2.get(localValue6));
               if (localValue4 == null) {
                  ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands_rehub.invalid_number")));
                  return;
               }
            }
         }

         if (!localValue1.internalMethod02266().isEmpty() && localValue1.internalMethod02266().getFirst() instanceof Integer localValue8) {
            localValue4 = localValue8;
         }

         if (ServerUtils.internalField0277 && !RockstarClient.getInstance().getModuleManager().getModule(KtLeaveModule.class).isEnabled()
            )
          {
            ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands_rehub.ct")));
         } else {
            this.internalField0068.internalMethod02475();
            this.internalField0068.internalField0257 = this.internalMethod01165(localValue3);
            this.internalMethod07922();
            if (localValue4 != null) {
               if (ServerUtils.internalMethod01786(KnownServer.internalField1220)) {
                  if (this.internalField0068.internalField0257 == ServerReconnectManager.InternalType0293.internalField0258
                     || this.internalField0068.internalField0257 == ServerReconnectManager.InternalType0293.internalField1088) {
                     this.internalField0068.internalField0227 = localValue4;
                  }
               } else if (ServerUtils.internalMethod08700()) {
                  if (this.internalField0068.internalField0067 == ServerReconnectManager.InternalType0194.internalField0067) {
                     this.internalField0068.internalField0067 = ServerReconnectManager.InternalType0194.internalField0066;
                  }

                  if (!this.internalMethod01202(localValue4)) {
                     this.internalField0068.internalMethod02475();
                     return;
                  }
               } else if (ServerUtils.internalMethod01786(KnownServer.internalField0578)) {
                  this.internalField0068.internalField0227 = localValue4;
               } else {
                  this.internalField0068.internalField0227 = localValue4;
               }
            } else if (ServerUtils.internalMethod01786(KnownServer.internalField1220)
               && this.internalField0068.internalField0257 == ServerReconnectManager.InternalType0293.internalField0258) {
               ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands_rehub.invalid_number")));
               return;
            }

            if (ServerUtils.internalMethod01786(KnownServer.internalField0578)
               || ServerUtils.internalMethod01786(KnownServer.internalField0579)) {
               this.internalField0068.internalField0227 = this.internalMethod07921();
               if (this.internalField0068.internalField0227 <= 0) {
                  ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands_rehub.invalid_number")));
                  this.internalField0068.internalMethod02475();
                  return;
               }
            }

            this.internalField0519.internalMethod00701();
            internalField0149.player.networkHandler.sendChatCommand("hub");
            this.internalField0068.internalField0277 = true;
         }
      }
   }

   private void internalMethod00923() {
      if (this.internalField0519.internalMethod02365(1000L)) {
         int localValue1 = this.internalField0068.internalField0227 > 0 ? this.internalField0068.internalField0227 : this.internalMethod07921();
         if (localValue1 <= 0) {
            ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands_rehub.invalid_number")));
            this.internalMethod07938();
         } else if (internalField0149.world.getDifficulty() == Difficulty.EASY
            || ServerUtils.internalMethod08699()
            || this.internalField0519.internalMethod02365(4000L)) {
            internalField0149.player.networkHandler.sendChatCommand("an" + localValue1);
            this.internalField0519.internalMethod00701();
            this.internalField0068.internalField0277 = false;
         }
      }
   }

   private void internalMethod00925() {
      if (this.internalField0519.internalMethod02365(300L)) {
         if (!this.internalField0068.internalField1100) {
            if (ServerSelectMenuNavigator.internalMethod00388()) {
               this.internalField0068.internalField1100 = true;
               this.internalField0519.internalMethod00701();
            }
         } else if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue1 && internalField0149.currentScreen != null) {
            String localValue6 = internalField0149.currentScreen.getTitle().getString();
            boolean localValue3 = this.internalField0068.internalField0257 == ServerReconnectManager.InternalType0293.internalField1088;
            if (ServerSelectMenuNavigator.internalMethod00290(localValue6)) {
               int localValue7 = ServerSelectMenuNavigator.internalMethod06353(localValue1, localValue3);
               if (localValue7 != -1) {
                  ServerSelectMenuNavigator.internalMethod06352(localValue1, localValue7);
                  this.internalField0519.internalMethod00701();
               }
            } else if (ServerSelectMenuNavigator.internalMethod06582(localValue6, localValue3)) {
               int localValue4 = this.internalField0068.internalField0227 > 0 ? this.internalField0068.internalField0227 : this.internalMethod00922();
               if (localValue4 <= 0) {
                  ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands_rehub.invalid_number")));
                  this.internalMethod07938();
               } else {
                  ServerSelectMenuNavigator.InternalType0222 localValue5 = ServerSelectMenuNavigator.internalMethod01194(localValue1, localValue6, localValue4, localValue3);
                  if (localValue5 == ServerSelectMenuNavigator.InternalType0222.internalField1192) {
                     this.internalMethod07938();
                  } else if (localValue5 == ServerSelectMenuNavigator.InternalType0222.internalField0535) {
                     this.internalField0519.internalMethod00701();
                  }
               }
            }
         }
      }
   }

   private void internalMethod07922() {
      this.internalField0068.internalField0067 = ServerReconnectManager.InternalType0194.internalField0067;
      this.internalField0068.internalField0248 = "";
      this.internalField0068.internalField0227 = -1;
      if (ServerUtils.internalMethod08700() && internalField0149.player != null && internalField0149.inGameHud != null) {
         Text localValue1 = null;

         try {
            localValue1 = ((PlayerListHudAccessor)(Object)internalField0149.inGameHud.getPlayerListHud()).getHeader();
         } catch (RuntimeException localValue3) {
            RockstarClient.internalField0572.debug("Failed to read tab header for reconnect mode detection", localValue3);
         }

         if ((localValue1 == null || localValue1.getString().isBlank())
            && internalField0149.player.networkHandler != null
            && internalField0149.player.networkHandler.getPlayerList() != null) {
            StringBuilder localValue2 = new StringBuilder();
            internalField0149.player.networkHandler.getPlayerList().forEach(localValue1x -> {
               if (localValue1x.getProfile() != null && localValue1x.getProfile().name() != null) {
                  localValue2.append(localValue1x.getProfile().name()).append(" ");
               }
            });
            if (!localValue2.isEmpty()) {
               localValue1 = Text.of(localValue2.toString());
            }
         }

         if (localValue1 != null) {
            String localValue4 = localValue1.getString();
            if (!localValue4.isBlank()) {
               if (localValue4.contains("\u041a\u043b\u0430\u0441\u0441\u0438\u043a")) {
                  this.internalField0068.internalField0067 = ServerReconnectManager.InternalType0194.internalField0976;
                  this.internalMethod02742(localValue4, "\u041a\u043b\u0430\u0441\u0441\u0438\u043a");
               } else if (localValue4.contains("\u041b\u0430\u0439\u0442")
                  && !localValue4.contains("\u0421\u043e\u043b\u043e\u041b\u0430\u0439\u0442")
                  && !localValue4.contains("\u0414\u0443\u043e\u041b\u0430\u0439\u0442")
                  && !localValue4.contains("\u0422\u0440\u0438\u043e\u041b\u0430\u0439\u0442")
                  && !localValue4.contains("\u041a\u043b\u0430\u043d\u041b\u0430\u0439\u0442")) {
                  this.internalField0068.internalField0067 = ServerReconnectManager.InternalType0194.internalField0975;
                  this.internalMethod02742(localValue4, "\u041b\u0430\u0439\u0442");
               } else if (localValue4.contains("\u041b\u0430\u0439\u0442")) {
                  this.internalField0068.internalField0067 = ServerReconnectManager.InternalType0194.internalField0066;
                  this.internalMethod02742(localValue4, "\u041b\u0430\u0439\u0442");
               }
            }
         }
      }
   }

   private void internalMethod02742(String localValue1, String localValue2) {
      try {
         String[] localValue3 = localValue1.split("\u25b6");
         if (localValue3.length < 2) {
            return;
         }

         String localValue4 = localValue3[1].replace("\u0410\u043d\u0430\u0440\u0445\u0438\u044f", "").trim();
         String[] localValue5 = localValue4.split("#");
         if (localValue5.length < 2) {
            return;
         }

         this.internalField0068.internalField0248 = localValue4.replace(localValue2, "").replaceAll("#\\d+", "").trim();
         String localValue6 = localValue5[1].replaceAll("[^0-9]", "").trim();
         if (localValue6.isEmpty()) {
            return;
         }

         this.internalField0068.internalField0227 = Integer.parseInt(localValue6);
      } catch (Exception localValue7) {
         this.internalField0068.internalField0248 = "";
         this.internalField0068.internalField0227 = -1;
      }
   }

   private void internalMethod07923() {
      this.internalField0068.internalField0255 = ServerReconnectManager.InternalType0292.internalField1087;
      this.internalField0068.internalField0276 = false;
      this.internalField0068.internalField0416.clear();
      this.internalField0068.internalField0228 = 0;
   }

   private void internalMethod07936() {
      if (internalField0149.currentScreen instanceof HandledScreen localValue1
         && internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue2) {
         String localValue5 = localValue1.getTitle().getString();
         switch (this.internalField0068.internalField0067) {
            case internalField0066:
               this.internalMethod04176(localValue2, localValue5);
               break;
            case internalField0975:
               this.internalMethod06666(localValue2, localValue5);
               break;
            case internalField0976:
               this.internalMethod07815(localValue2, localValue5);
         }
      }
   }

   private void internalMethod04176(GenericContainerScreenHandler localValue1, String localValue2) {
      if (localValue2.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0440\u0435\u0436\u0438\u043c")) {
         ContainerMenuUtils.internalMethod06018(localValue1, 12);
         this.internalMethod07923();
      } else {
         if (this.internalField0068.internalField0255 == ServerReconnectManager.InternalType0292.internalField1087
            && localValue2.contains("\u0412\u044b\u0431\u043e\u0440 \u041b\u0430\u0439\u0442 \u0430\u043d\u0430\u0440\u0445\u0438\u0438")) {
            int localValue3 = this.internalMethod02617(localValue1);
            if (this.internalMethod01744(localValue1, localValue3)) {
               ContainerMenuUtils.internalMethod06018(localValue1, localValue3);
               this.internalMethod07938();
               return;
            }

            if (this.internalField0068.internalField0228 > 0) {
               this.internalField0068.internalField0228--;
               return;
            }

            int localValue4 = this.internalMethod01426(localValue1);
            if (localValue4 == -1) {
               this.internalField0068.internalField0255 = ServerReconnectManager.InternalType0292.internalField0255;
               return;
            }

            if (!this.internalField0068.internalField0276) {
               ContainerMenuUtils.internalMethod06018(localValue1, localValue4);
               this.internalField0068.internalField0276 = true;
               this.internalField0068.internalField0416.add(localValue4);
               this.internalField0068.internalField0228 = 5;
               return;
            }

            this.internalField0068.internalField0276 = false;
         }
      }
   }

   private boolean internalMethod01202(int localValue1) {
      return switch (this.internalField0068.internalField0067) {
         case internalField0067 -> {
            ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands_rehub.mode_unknown")));
            yield false;
         }
         case internalField0066 -> {
            ServerModeCategory localValue2 = this.internalMethod01177(localValue1);
            if (localValue2 == ServerModeCategory.internalField1224) {
               ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands_rehub.invalid_number")));
               yield false;
            } else {
               this.internalField0068.internalField0227 = localValue1;
               this.internalField0068.internalField0248 = this.internalMethod07401(localValue2);
               yield true;
            }
         }
         case internalField0975 -> {
            if (localValue1 >= 1 && localValue1 <= 3) {
               this.internalField0068.internalField0227 = localValue1;
               yield true;
            } else {
               ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands_rehub.invalid_number")));
               yield false;
            }
         }
         case internalField0976 -> {
            if (ContainerMenuUtils.internalMethod03242(localValue1) == -1) {
               ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands_rehub.invalid_number")));
               yield false;
            } else {
               this.internalField0068.internalField0227 = localValue1;
               yield true;
            }
         }
      };
   }

   private void internalMethod06666(GenericContainerScreenHandler localValue1, String localValue2) {
      if (this.internalField0068.internalField0255 == ServerReconnectManager.InternalType0292.internalField1086
         && localValue2.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0440\u0435\u0436\u0438\u043c")) {
         ContainerMenuUtils.internalMethod06018(localValue1, 10);
         this.internalField0068.internalField0255 = ServerReconnectManager.InternalType0292.internalField1085;
      } else {
         if (this.internalField0068.internalField0255 == ServerReconnectManager.InternalType0292.internalField1085
            && localValue2.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0441\u0435\u0440\u0432\u0435\u0440 \u041b\u0430\u0439\u0442")) {
            int localValue3 = ContainerMenuUtils.internalMethod03296(this.internalField0068.internalField0227);
            if (this.internalMethod01744(localValue1, localValue3)) {
               ContainerMenuUtils.internalMethod06018(localValue1, localValue3);
               this.internalMethod07938();
            }
         }
      }
   }

   private void internalMethod07815(GenericContainerScreenHandler localValue1, String localValue2) {
      if (this.internalField0068.internalField0255 == ServerReconnectManager.InternalType0292.internalField1084
         && localValue2.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0440\u0435\u0436\u0438\u043c")) {
         ContainerMenuUtils.internalMethod06018(localValue1, 15);
         this.internalField0068.internalField0255 = ServerReconnectManager.InternalType0292.internalField1503;
      } else {
         if (this.internalField0068.internalField0255 == ServerReconnectManager.InternalType0292.internalField1503
            && localValue2.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u041a\u043b\u0430\u0441\u0441\u0438\u0447")) {
            int localValue3 = ContainerMenuUtils.internalMethod03242(this.internalField0068.internalField0227);
            if (this.internalMethod01744(localValue1, localValue3)) {
               ContainerMenuUtils.internalMethod06018(localValue1, localValue3);
               this.internalMethod07938();
            }
         }
      }
   }

   private boolean internalMethod01744(GenericContainerScreenHandler localValue1, int localValue2) {
      return localValue2 >= 0 && localValue2 < localValue1.slots.size() && localValue1.getSlot(localValue2).hasStack();
   }

   private int internalMethod01426(GenericContainerScreenHandler localValue1) {
      if (!this.internalField0068.internalField0248.isBlank()) {
         int localValue2 = ContainerMenuUtils.internalMethod02224(localValue1, this.internalField0068.internalField0248);
         if (localValue2 != -1 && !this.internalField0068.internalField0416.contains(localValue2)) {
            return localValue2;
         }

         localValue2 = ContainerMenuUtils.internalMethod05511(this.internalMethod02243(this.internalField0068.internalField0248));
         if (localValue2 != -1 && !this.internalField0068.internalField0416.contains(localValue2)) {
            return localValue2;
         }
      }

      for (int localValue3 : ContainerMenuUtils.internalMethod01343(localValue1)) {
         if (!this.internalField0068.internalField0416.contains(localValue3)) {
            return localValue3;
         }
      }

      return -1;
   }

   private int internalMethod02617(GenericContainerScreenHandler localValue1) {
      return ContainerMenuUtils.internalMethod00770(localValue1, "", this.internalField0068.internalField0227);
   }

   private ServerModeCategory internalMethod01177(int localValue1) {
      return ContainerMenuUtils.internalMethod03905(localValue1);
   }

   private ServerModeCategory internalMethod02243(String localValue1) {
      if (localValue1 == null) {
         return ServerModeCategory.internalField1224;
      } else if (localValue1.contains("\u0421\u043e\u043b\u043e")) {
         return ServerModeCategory.internalField0584;
      } else if (localValue1.contains("\u0414\u0443\u043e")) {
         return ServerModeCategory.internalField0583;
      } else if (localValue1.contains("\u0422\u0440\u0438\u043e")) {
         return ServerModeCategory.internalField1223;
      } else {
         return localValue1.contains("\u041a\u043b\u0430\u043d") ? ServerModeCategory.internalField1225 : ServerModeCategory.internalField1224;
      }
   }

   private String internalMethod07401(ServerModeCategory localValue1) {
      return switch (localValue1) {
         case internalField0584 -> "\u0421\u043e\u043b\u043e";
         case internalField0583 -> "\u0414\u0443\u043e";
         case internalField1223 -> "\u0422\u0440\u0438\u043e";
         case internalField1225 -> "\u041a\u043b\u0430\u043d";
         default -> "";
      };
   }

   private void internalMethod07938() {
      this.internalField0068.internalMethod02475();
      this.internalField0519.internalMethod00701();
   }

   private int internalMethod00922() {
      return this.internalField0068.internalField0257 == ServerReconnectManager.InternalType0293.internalField1088 ? ServerUtils.internalField1055 : -1;
   }

   private int internalMethod00924() {
      return this.internalField0068.internalField0257 == ServerReconnectManager.InternalType0293.internalField1088
         ? ServerUtils.internalField1053
         : ServerUtils.internalField0228;
   }

   private int internalMethod07921() {
      if (this.internalField0068.internalField0227 > 0) {
         return this.internalField0068.internalField0227;
      } else {
         int localValue1 = ServerUtils.internalMethod07319();
         return localValue1 > 0 ? localValue1 : this.internalMethod00924();
      }
   }

   private ServerReconnectManager.InternalType0293 internalMethod01165(ServerReconnectManager.InternalType0293 localValue1) {
      if (localValue1 != ServerReconnectManager.InternalType0293.internalField0257) {
         return localValue1;
      } else if (ServerUtils.internalMethod01786(KnownServer.internalField1220)) {
         return ServerReconnectManager.InternalType0293.internalField1088;
      } else {
         return (
                  ServerUtils.internalMethod01786(KnownServer.internalField0578)
                     || ServerUtils.internalMethod01786(KnownServer.internalField0579)
               )
               && ServerUtils.internalField0276
            ? ServerReconnectManager.InternalType0293.internalField1088
            : ServerReconnectManager.InternalType0293.internalField0258;
      }
   }

   private List<String> internalMethod00404(Object localValue1) {
      return localValue1 == null ? List.of() : (List)localValue1;
   }

   private Integer internalMethod03307(String localValue1) {
      try {
         int localValue2 = Integer.parseInt(localValue1);
         return localValue2 > 0 ? localValue2 : null;
      } catch (NumberFormatException localValue3) {
         return null;
      }
   }

   static enum InternalType0194 {
      internalField0067,
      internalField0066,
      internalField0975,
      internalField0976;
   }

   static class InternalType0195 {
      boolean internalField0277;
      ServerReconnectManager.InternalType0194 internalField0067;
      String internalField0248;
      int internalField0227;
      ServerReconnectManager.InternalType0292 internalField0255;
      boolean internalField0276;
      int internalField0228;
      boolean internalField1099;
      boolean internalField1100;
      ServerReconnectManager.InternalType0293 internalField0257;
      boolean internalField1102;
      final List<Integer> internalField0416;

      InternalType0195() {
         this.internalField0067 = ServerReconnectManager.InternalType0194.internalField0067;
         this.internalField0248 = "";
         this.internalField0227 = -1;
         this.internalField0255 = ServerReconnectManager.InternalType0292.internalField0255;
         this.internalField0257 = ServerReconnectManager.InternalType0293.internalField0257;
         this.internalField0416 = new ArrayList<>();
      }

      final void internalMethod02475() {
         this.internalField0277 = false;
         this.internalField0067 = ServerReconnectManager.InternalType0194.internalField0067;
         this.internalField0248 = "";
         this.internalField0227 = -1;
         this.internalField1100 = false;
         this.internalField0255 = ServerReconnectManager.InternalType0292.internalField0255;
         this.internalField0257 = ServerReconnectManager.InternalType0293.internalField0257;
         this.internalField0276 = false;
         this.internalField0228 = 0;
         this.internalField1099 = false;
         this.internalField0416.clear();
         this.internalField1102 = false;
      }
   }

   static enum InternalType0292 {
      internalField0255,
      internalField0256,
      internalField1087,
      internalField1086,
      internalField1085,
      internalField1084,
      internalField1503;
   }

   static enum InternalType0293 {
      internalField0257,
      internalField0258,
      internalField1088;

      public static ServerReconnectManager.InternalType0293 internalMethod03536(String localValue0) {
         if (localValue0 == null) {
            return null;
         } else {
            String localValue1 = localValue0.toLowerCase();
            if (localValue1.startsWith("an") || localValue1.contains("\u0430\u043d\u0430\u0440\u0445")) {
               return internalField0258;
            } else {
               return !localValue1.startsWith("gr") && !localValue1.contains("\u0433\u0440\u0438\u0444") ? null : internalField1088;
            }
         }
      }
   }
}

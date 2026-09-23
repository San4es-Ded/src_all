package rockstar.client.server;




import rockstar.client.util.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import lombok.Generated;
import moscow.rockstar.mixin.accessors.PlayerListHudAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.network.ServerInfo.ServerType;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import pyrock.events.game.GameTickEvent;

public final class ServerUtils implements MinecraftClientAccess {
   public static boolean internalField0277;
   public static int internalField0227;
   public static int internalField0228 = -1;
   public static int internalField1053 = -1;
   public static int internalField1055 = -1;
   public static int internalField1056 = -1;
   public static int internalField1054 = -1;
   public static boolean internalField0276;
   public static boolean internalField1099;
   public static boolean internalField1100;
   public static boolean internalField1102;
   public static String internalField0248 = "";
   private static String internalField0247;
   private static String internalField1077;
   private static boolean internalField1101;
   private static ServerSlotReference internalField0003;
   private static boolean internalField1516;
   private static ServerJoinStage internalField0002 = ServerJoinStage.internalField0002;
   private static ServerJoinMode internalField0581 = ServerJoinMode.internalField0581;
   private static boolean internalField1517;
   private static boolean internalField1512;
   private static boolean internalField1515;
   private static int internalField1464 = -1;
   private static int internalField1470 = -1;
   private static final List<Integer> internalField0416 = new ArrayList<>();
   private static int internalField1465 = -1;
   private static int internalField1463 = -1;
   private static final Stopwatch internalField0519 = new Stopwatch();
   static final EventListener<GameTickEvent> internalField0157 = localValue0 -> {
      if (internalField1101 && internalField0149.player != null && internalMethod05063(internalField0247)) {
         if (internalField1516) {
            internalMethod07325();
         } else {
            if (internalMethod08717()) {
               internalField0149.player.networkHandler.sendChatCommand(internalField1077);
            }

            internalMethod07320();
         }
      }

      Text localValue1 = ((PlayerListHudAccessor)(Object)internalField0149.inGameHud.getPlayerListHud()).getHeader();
      if (localValue1 != null) {
         String localValue2 = localValue1.getString();
         if (localValue2.contains("\u0433\u0440\u0438\u0444") && internalMethod01786(KnownServer.internalField0578)) {
            internalField0276 = true;
         } else if (localValue2.contains("\u0434\u0443\u044d\u043b\u0438") && internalMethod01786(KnownServer.internalField0579)) {
            internalField1099 = true;
         } else if (localValue2.contains("1.21") && internalMethod01786(KnownServer.internalField1220)) {
            internalField1100 = true;
         } else if (localValue2.contains("\u0420\u0435\u0439\u0442\u0438\u043d\u0433") && internalMethod01786(KnownServer.internalField1567)) {
            internalField1102 = true;
         }

         if (internalMethod01786(KnownServer.internalField1567)) {
            try {
               internalField1056 = Integer.parseInt(localValue2.split("\u0410\u043d\u0430\u0440\u0445\u0438\u044f PvP #")[1].trim());
            } catch (Exception localValue6) {
            }
         }

         if (internalMethod01786(KnownServer.internalField1218)) {
            String localValue3 = localValue2.split("\u25b6")[1].replace("\u0410\u043d\u0430\u0440\u0445\u0438\u044f", "").trim();

            try {
               internalField1054 = Integer.parseInt(localValue3.split("#")[1].trim());
               internalField0248 = localValue3.split("#")[0].trim();
            } catch (Exception localValue5) {
            }
         }
      }
   };
   private static final Object internalField0290 = new Object() {
      private final EventListener<GameTickEvent> internalField0157;

      {
         this.internalField0157 = ServerUtils.internalField0157;
      }
   };

   public static boolean internalMethod01786(KnownServer localValue0) {
      return localValue0.internalMethod04913(internalMethod00929());
   }

   public static String internalMethod00929() {
      return internalField0149.player != null && internalField0149.player.networkHandler.getServerInfo() != null
         ? internalField0149.player.networkHandler.getServerInfo().address
         : "single";
   }

   public static String internalMethod05631() {
      if (internalField0149.player != null && internalField0149.player.networkHandler.getServerInfo() != null) {
         String localValue0 = internalField0149.player.networkHandler.getServerInfo().address;

         try {
            InetAddress localValue1 = InetAddress.getByName(localValue0);
            return localValue1.getHostAddress();
         } catch (UnknownHostException localValue2) {
            return localValue0;
         }
      } else {
         return "single";
      }
   }

   public static boolean internalMethod06501(String localValue0) {
      return internalMethod00929().toLowerCase().contains(localValue0.toLowerCase());
   }

   public static boolean internalMethod07321() {
      if (!internalMethod01786(KnownServer.internalField0579)) {
         return false;
      } else {
         if (internalField0149.inGameHud != null) {
            Text localValue0 = ((PlayerListHudAccessor)(Object)internalField0149.inGameHud.getPlayerListHud()).getHeader();
            if (localValue0 != null && localValue0.getString().toLowerCase(Locale.ROOT).contains("\u0434\u0443\u044d\u043b")) {
               return true;
            }
         }

         boolean localValue5 = false;
         boolean localValue1 = false;

         for (String localValue3 : internalMethod04896()) {
            String localValue4 = localValue3.toLowerCase(Locale.ROOT);
            if (localValue4.contains("\u0434\u0443\u044d\u043b")) {
               return true;
            }

            if (localValue4.contains("\u0443\u0431\u0438\u0439\u0441\u0442\u0432")) {
               localValue5 = true;
            }

            if (localValue4.contains("\u0441\u043c\u0435\u0440\u0442")) {
               localValue1 = true;
            }
         }

         return localValue5 && localValue1;
      }
   }

   public static List<String> internalMethod04896() {
      if (internalField0149.world == null) {
         return List.of();
      } else {
         Scoreboard localValue0 = internalField0149.world.getScoreboard();
         ScoreboardObjective localValue1 = localValue0.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
         if (localValue1 == null) {
            return List.of();
         } else {
            ArrayList localValue2 = new ArrayList();

            for (ScoreboardEntry localValue4 : localValue0.getScoreboardEntries(localValue1)) {
               String localValue5 = localValue4.owner();
               String localValue6 = localValue4.display() == null ? localValue5 : localValue4.display().getString();
               Team localValue7 = localValue0.getScoreHolderTeam(localValue5);
               if (localValue7 != null) {
                  localValue6 = localValue7.getPrefix().getString() + localValue6 + localValue7.getSuffix().getString();
               }

               localValue2.add(localValue6);
            }

            return localValue2;
         }
      }
   }

   public static boolean internalMethod07326() {
      if (internalField0149.inGameHud == null) {
         return false;
      } else {
         Text localValue0 = ((PlayerListHudAccessor)(Object)internalField0149.inGameHud.getPlayerListHud()).getHeader();
         if (localValue0 == null) {
            return false;
         } else {
            String localValue1 = localValue0.getString().toLowerCase(Locale.ROOT);
            return localValue1.contains("funtime") || localValue1.contains("fun time") || localValue1.contains("\u0444\u0430\u043d\u0442\u0430\u0439\u043c");
         }
      }
   }

   public static String internalMethod06458(boolean localValue0) {
      return internalMethod04844(internalMethod00929(), localValue0);
   }

   public static String internalMethod04844(String localValue0, boolean localValue1) {
      String[] localValue2 = localValue0.split("\\.");
      if (internalMethod06501("liquidproxy")) {
         return localValue1 ? "LP" : "LiquidProxy";
      } else if (internalField0149.isInSingleplayer()) {
         return internalMethod03902(localValue0, localValue1);
      } else if (localValue2.length == 3) {
         return internalMethod03902(localValue2[1], localValue1);
      } else if (localValue2.length == 2) {
         return internalMethod03902(localValue2[0], localValue1);
      } else {
         return localValue0.contains(":") ? localValue0.split(":")[0] : localValue0;
      }
   }

   private static String internalMethod03902(String localValue0, boolean localValue1) {
      localValue0 = localValue0.replace("-", "");
      ArrayList localValue2 = new ArrayList();
      String[] localValue3 = new String[]{
         "legacy",
         "bars",
         "world",
         "best",
         "times",
         "time",
         "shine",
         "sky",
         "lands",
         "land",
         "trainer",
         "server",
         "blaze",
         "mine",
         "lord",
         "cube",
         "grief",
         "craft",
         "rise",
         "force",
         "project",
         "lite",
         "client"
      };
      Arrays.stream(localValue3).forEach(localValue1x -> localValue2.add(internalMethod07233(localValue1x)));
      localValue2.addAll(
         Arrays.asList(
            new ServerUtils.InternalType0142("mc", "MC", "-MC"),
            new ServerUtils.InternalType0142("hvh", "HVH", "-HVH"),
            new ServerUtils.InternalType0142("pvp", "PVP", "PVP")
         )
      );
      if (internalField0149.isInSingleplayer() && !localValue1) {
         localValue0 = "LocalHost";
      }

      if (internalMethod06501("sunmc")) {
         localValue0 = localValue1 ? "SR" : "SunRise";
      }

      if (internalMethod06501("saturn")) {
         localValue0 = localValue1 ? "S-X" : "SaturnX";
      }

      if (internalMethod06501("sunw")) {
         localValue0 = localValue1 ? "SW" : "SunWay";
      }

      for (ServerUtils.InternalType0142 localValue5 : (Iterable<ServerUtils.InternalType0142>)(Iterable<?>)localValue2) {
         if (localValue0.contains(localValue5.internalField0248)) {
            if (localValue1) {
               localValue0 = localValue0.substring(0, 1).toUpperCase() + localValue5.internalField1077;
            } else {
               localValue0 = localValue0.replace(localValue5.internalField0248, localValue5.internalField0247);
               localValue0 = localValue0.substring(0, 1).toUpperCase() + localValue0.substring(1);
            }

            return localValue0;
         }
      }

      return localValue0.substring(0, 1).toUpperCase() + localValue0.substring(1);
   }

   public static boolean internalMethod08699() {
      if (internalField0149.player != null && internalField0149.world != null) {
         BlockPos localValue0 = internalField0149.player.getBlockPos();
         Block localValue1 = internalField0149.world.getBlockState(localValue0.down(1)).getBlock();
         Block localValue2 = internalField0149.world.getBlockState(new BlockPos(localValue0.getX(), 0, localValue0.getZ())).getBlock();
         Block localValue3 = internalField0149.world.getBlockState(new BlockPos(localValue0.getX(), 6, localValue0.getZ())).getBlock();
         if (internalMethod01786(KnownServer.internalField0578) && internalField0149.world.getDifficulty() == Difficulty.NORMAL) {
            return false;
         } else if (internalMethod01786(KnownServer.internalField0578) || internalMethod01786(KnownServer.internalField0579)) {
            return localValue1 == Blocks.AIR || localValue2 == Blocks.AIR;
         } else if (internalMethod01786(KnownServer.internalField1218)) {
            return localValue3 == Blocks.SAND;
         } else {
            return internalField0149.world.getRegistryKey() != World.OVERWORLD ? false : localValue2 == Blocks.BEDROCK || localValue1 == Blocks.BEDROCK;
         }
      } else {
         return false;
      }
   }

   private static ServerUtils.InternalType0142 internalMethod07233(String localValue0) {
      return new ServerUtils.InternalType0142(localValue0, localValue0.substring(0, 1).toUpperCase() + localValue0.substring(1), localValue0.substring(0, 1).toUpperCase());
   }

   public static int internalMethod07319() {
      if (internalMethod01786(KnownServer.internalField0578)) {
         if (internalField0276) {
            return internalField1053;
         } else {
            return internalField0149.world.getDifficulty() == Difficulty.EASY ? -1 : internalField0228;
         }
      } else if (internalMethod01786(KnownServer.internalField0579)) {
         return internalField1099 ? 1 : internalField0228;
      } else {
         return internalMethod01786(KnownServer.internalField1218) ? internalField1054 : -1;
      }
   }

   public static void internalMethod04902(String localValue0, String localValue1) {
      if (internalField0149 != null) {
         internalField0247 = internalMethod06345(localValue0);
         String localValue2 = internalMethod07362(localValue1);
         internalField1077 = localValue2;
         internalMethod08698();
         boolean localValue3 = false;
         if (internalMethod03938(internalField0247, localValue2)) {
            Integer localValue4 = internalMethod05461(localValue2);
            int localValue5 = ContainerMenuUtils.internalMethod03296(localValue4);
            if (localValue5 >= 0) {
               internalMethod05352(localValue5);
               localValue3 = true;
            }
         }

         if (!localValue3 && internalMethod08679(internalField0247, localValue2)) {
            Integer localValue8 = internalMethod05461(localValue2);
            int localValue11 = ContainerMenuUtils.internalMethod03242(localValue8);
            if (localValue11 >= 0) {
               internalMethod08707(localValue11);
               localValue3 = true;
            }
         }

         if (!localValue3 && internalMethod04903(internalField0247, localValue2)) {
            ServerSlotReference localValue9 = internalMethod06051(localValue2);
            if (localValue9 != null) {
               internalMethod01201(localValue9);
               localValue3 = true;
            }
         }

         internalField1101 = true;
         boolean localValue10 = internalMethod05063(internalField0247);
         if (!localValue10 && internalField0149.getCurrentServerEntry() != null) {
            String localValue12 = internalField0149.getCurrentServerEntry().address;
            if (localValue12 != null) {
               localValue10 = internalMethod06345(localValue12).equals(internalField0247);
            }
         }

         if (localValue10) {
            if (internalField0149.player != null && internalMethod08717()) {
               internalField0149.player.networkHandler.sendChatCommand(internalField1077);
               internalMethod07320();
            }
         } else {
            String localValue13 = internalMethod09107(internalField0247);
            ServerInfo localValue6 = new ServerInfo(localValue13, internalField0247, ServerType.OTHER);
            internalField0149.disconnect(net.minecraft.client.world.ClientWorld.QUITTING_MULTIPLAYER_TEXT);
            internalField0149.execute(
               () -> ConnectScreen.connect(new MultiplayerScreen(new TitleScreen()), internalField0149, ServerAddress.parse(internalField0247), localValue6, false, null)
            );
         }
      }
   }

   private static void internalMethod07320() {
      internalField0247 = null;
      internalField1077 = null;
      internalField1101 = false;
      internalMethod08698();
   }

   private static boolean internalMethod05063(String localValue0) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         String localValue1 = internalMethod00929();
         return localValue1 != null && !localValue1.equals("single") ? internalMethod06345(localValue1).equals(localValue0) : false;
      } else {
         return false;
      }
   }

   private static void internalMethod07325() {
      if (internalField1516 && internalField0149.player != null) {
         if (internalField0581 == ServerJoinMode.internalField0581) {
            internalMethod07320();
         } else if (internalField0581 == ServerJoinMode.internalField0582 && internalField0003 == null) {
            internalMethod07320();
         } else if (internalField0581 == ServerJoinMode.internalField1222 && internalField1465 < 0) {
            internalMethod07320();
         } else if (internalField0581 == ServerJoinMode.internalField1221 && internalField1463 < 0) {
            internalMethod07320();
         } else if (!internalField1517) {
            internalField0149.player.networkHandler.sendChatCommand("hub");
            internalField1517 = true;
            internalField0519.internalMethod00701();
         } else if (!internalField1512) {
            if (internalField0519.internalMethod02365(600L)) {
               internalField0149.player.networkHandler.sendChatCommand("menu");
               internalField1512 = true;
               internalField0519.internalMethod00701();
            }
         } else if (internalField0149.currentScreen == null) {
            ServerJoinStage localValue5 = internalMethod04241();
            if (internalField0002 != localValue5) {
               internalField0002 = localValue5;
               internalField1515 = false;
            }

            if (internalField0519.internalMethod02365(2000L)) {
               internalField0149.player.networkHandler.sendChatCommand("menu");
               internalField0519.internalMethod00701();
            }
         } else if (internalField0149.currentScreen instanceof HandledScreen localValue0) {
            if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue6) {
               String localValue7 = localValue0.getTitle().getString();
               if (internalField0581 == ServerJoinMode.internalField0582
                  && localValue7.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0440\u0435\u0436\u0438\u043c")
                  && internalField0002 != ServerJoinStage.internalField0001) {
                  internalField1515 = false;
                  internalField0002 = ServerJoinStage.internalField0001;
               }

               if (localValue7.contains("\u0412\u044b\u0431\u043e\u0440 \u041b\u0430\u0439\u0442 \u0430\u043d\u0430\u0440\u0445\u0438\u0438")
                  && internalField0002 == ServerJoinStage.internalField0001) {
                  internalField0002 = ServerJoinStage.internalField0957;
                  internalField1515 = false;
               }

               if (internalField0581 == ServerJoinMode.internalField0582) {
                  if (internalField0002 == ServerJoinStage.internalField0001) {
                     if (localValue7.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0440\u0435\u0436\u0438\u043c")) {
                        ContainerMenuUtils.internalMethod06018(localValue6, 12);
                        internalField0002 = ServerJoinStage.internalField0957;
                        internalField0519.internalMethod00701();
                     }
                  } else if (internalField0002 == ServerJoinStage.internalField0957) {
                     if (localValue7.contains("\u0412\u044b\u0431\u043e\u0440 \u041b\u0430\u0439\u0442 \u0430\u043d\u0430\u0440\u0445\u0438\u0438")) {
                        int localValue8 = ContainerMenuUtils.internalMethod00770(localValue6, "", internalField0003.internalMethod05526());
                        if (localValue8 >= 0) {
                           ContainerMenuUtils.internalMethod06018(localValue6, localValue8);
                           internalMethod07320();
                           return;
                        }

                        if (!internalField1515) {
                           if (internalField1464 < 0) {
                              internalMethod07320();
                              return;
                           }

                           ContainerMenuUtils.internalMethod06018(localValue6, internalField1464);
                           internalField0416.add(internalField1464);
                           internalField1515 = true;
                           internalField0002 = ServerJoinStage.internalField0958;
                           internalField0519.internalMethod00701();
                        }
                     }
                  } else {
                     if (internalField0002 == ServerJoinStage.internalField0958
                        && localValue7.contains("\u0412\u044b\u0431\u043e\u0440 \u041b\u0430\u0439\u0442 \u0430\u043d\u0430\u0440\u0445\u0438\u0438")) {
                        int localValue3 = ContainerMenuUtils.internalMethod00770(localValue6, "", internalField0003.internalMethod05526());
                        if (localValue3 >= 0) {
                           ContainerMenuUtils.internalMethod06018(localValue6, localValue3);
                           internalMethod07320();
                           return;
                        }

                        if (!internalField0519.internalMethod02365(250L)) {
                           return;
                        }

                        int localValue4 = internalMethod06012(localValue6);
                        if (localValue4 < 0) {
                           internalMethod07320();
                           return;
                        }

                        ContainerMenuUtils.internalMethod06018(localValue6, localValue4);
                        internalField0416.add(localValue4);
                        internalField0519.internalMethod00701();
                     }
                  }
               } else if (internalField0581 == ServerJoinMode.internalField1222) {
                  if (internalField0002 == ServerJoinStage.internalField0956) {
                     if (localValue7.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0440\u0435\u0436\u0438\u043c")) {
                        ContainerMenuUtils.internalMethod06018(localValue6, 10);
                        internalField0002 = ServerJoinStage.internalField0959;
                        internalField0519.internalMethod00701();
                     }
                  } else {
                     if (internalField0002 == ServerJoinStage.internalField0959
                        && localValue7.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0441\u0435\u0440\u0432\u0435\u0440 \u041b\u0430\u0439\u0442")) {
                        if (internalField1465 < 0) {
                           internalMethod07320();
                           return;
                        }

                        if (internalField1465 < localValue6.slots.size() && localValue6.getSlot(internalField1465).hasStack()) {
                           ContainerMenuUtils.internalMethod06018(localValue6, internalField1465);
                           internalMethod07320();
                        }
                     }
                  }
               } else {
                  if (internalField0581 == ServerJoinMode.internalField1221) {
                     if (internalField0002 == ServerJoinStage.internalField1398) {
                        if (localValue7.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0440\u0435\u0436\u0438\u043c")) {
                           ContainerMenuUtils.internalMethod06018(localValue6, 15);
                           internalField0002 = ServerJoinStage.internalField1397;
                           internalField0519.internalMethod00701();
                        }

                        return;
                     }

                     if (internalField0002 == ServerJoinStage.internalField1397
                        && localValue7.contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u041a\u043b\u0430\u0441\u0441\u0438\u043a")) {
                        if (internalField1463 < 0) {
                           internalMethod07320();
                           return;
                        }

                        if (internalField1463 < localValue6.slots.size() && localValue6.getSlot(internalField1463).hasStack()) {
                           ContainerMenuUtils.internalMethod06018(localValue6, internalField1463);
                           internalMethod07320();
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static void internalMethod08698() {
      internalField0003 = null;
      internalField1516 = false;
      internalField0002 = ServerJoinStage.internalField0002;
      internalField1517 = false;
      internalField0581 = ServerJoinMode.internalField0581;
      internalField1512 = false;
      internalField1515 = false;
      internalField1464 = -1;
      internalField0416.clear();
      internalField1465 = -1;
      internalField1463 = -1;
      internalField1470 = -1;
      internalField0519.internalMethod00701();
   }

   private static ServerJoinStage internalMethod04241() {
      return switch (internalField0581) {
         case internalField0582 -> ServerJoinStage.internalField0001;
         case internalField1222 -> ServerJoinStage.internalField0956;
         case internalField1221 -> ServerJoinStage.internalField1398;
         default -> ServerJoinStage.internalField0002;
      };
   }

   private static void internalMethod01201(ServerSlotReference localValue0) {
      internalField0003 = localValue0;
      internalField1516 = true;
      internalField0002 = ServerJoinStage.internalField0001;
      internalField1517 = false;
      internalField1512 = false;
      internalField1515 = false;
      internalField0581 = ServerJoinMode.internalField0582;
      internalField1464 = localValue0.internalMethod05528();
      internalField1470 = localValue0.internalMethod08512();
      internalField0416.clear();
      internalField0519.internalMethod00701();
      internalField1077 = null;
   }

   private static boolean internalMethod04903(String localValue0, String localValue1) {
      return localValue0 != null && localValue1 != null ? internalMethod09086(localValue0) && localValue1.toLowerCase().startsWith("lite") : false;
   }

   private static ServerSlotReference internalMethod06051(String localValue0) {
      if (localValue0 == null) {
         return null;
      } else {
         String localValue1 = localValue0.replaceAll("[^0-9]", "");
         if (localValue1.isEmpty()) {
            return null;
         } else {
            int localValue2;
            try {
               localValue2 = Integer.parseInt(localValue1);
            } catch (NumberFormatException localValue6) {
               return null;
            }

            ServerModeCategory localValue3 = internalMethod01941(localValue2);
            if (localValue3 == ServerModeCategory.internalField1224) {
               return null;
            } else {
               int localValue4 = ContainerMenuUtils.internalMethod05511(localValue3);
               int localValue5 = internalMethod01650(localValue3, localValue2);
               return localValue4 >= 0 && localValue5 >= 0 ? new ServerSlotReference(localValue2, localValue3, localValue4, localValue5) : null;
            }
         }
      }
   }

   private static ServerModeCategory internalMethod01941(int localValue0) {
      return ContainerMenuUtils.internalMethod03905(localValue0);
   }

   private static int internalMethod01650(ServerModeCategory localValue0, int localValue1) {
      return ContainerMenuUtils.internalMethod03939(localValue0, localValue1);
   }

   private static int internalMethod06012(GenericContainerScreenHandler localValue0) {
      for (int localValue2 : ContainerMenuUtils.internalMethod01343(localValue0)) {
         if (!internalField0416.contains(localValue2)) {
            return localValue2;
         }
      }

      for (ServerModeCategory localValue4 : ServerModeCategory.values()) {
         if (localValue4 != ServerModeCategory.internalField1224) {
            int localValue5 = ContainerMenuUtils.internalMethod05511(localValue4);
            if (!internalField0416.contains(localValue5) && localValue5 < localValue0.slots.size() && localValue0.getSlot(localValue5).hasStack()) {
               return localValue5;
            }
         }
      }

      return -1;
   }

   private static boolean internalMethod09086(String localValue0) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         String localValue1 = internalMethod09107(localValue0).toLowerCase();
         return localValue1.contains("holy") || localValue1.contains("holly") || localValue1.contains("playhw") || localValue1.equals("hw") || localValue1.startsWith("hw.");
      } else {
         return false;
      }
   }

   private static void internalMethod05352(int localValue0) {
      internalField0003 = null;
      internalField1516 = true;
      internalField0002 = ServerJoinStage.internalField0956;
      internalField0581 = ServerJoinMode.internalField1222;
      internalField1517 = false;
      internalField1512 = false;
      internalField1515 = false;
      internalField1464 = -1;
      internalField1470 = -1;
      internalField1465 = localValue0;
      internalField1463 = -1;
      internalField0519.internalMethod00701();
      internalField1077 = null;
   }

   private static void internalMethod08707(int localValue0) {
      internalField0003 = null;
      internalField1516 = true;
      internalField0002 = ServerJoinStage.internalField1398;
      internalField0581 = ServerJoinMode.internalField1221;
      internalField1517 = false;
      internalField1512 = false;
      internalField1515 = false;
      internalField1464 = -1;
      internalField1470 = -1;
      internalField1465 = -1;
      internalField1463 = localValue0;
      internalField0519.internalMethod00701();
      internalField1077 = null;
   }

   private static String internalMethod06345(String localValue0) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         String localValue1 = localValue0.trim().toLowerCase();
         if (!localValue1.contains(":")) {
            localValue1 = localValue1 + ":25565";
         }

         return localValue1;
      } else {
         return "";
      }
   }

   private static boolean internalMethod03938(String localValue0, String localValue1) {
      if (localValue0 != null && localValue1 != null) {
         String localValue2 = localValue1.toLowerCase();
         return internalMethod09086(localValue0) && (localValue2.startsWith("lite120") || localValue2.startsWith("lite 120"));
      } else {
         return false;
      }
   }

   private static boolean internalMethod08679(String localValue0, String localValue1) {
      if (localValue0 != null && localValue1 != null) {
         String localValue2 = localValue1.toLowerCase();
         return internalMethod09086(localValue0) && (localValue2.startsWith("classik") || localValue2.startsWith("classic"));
      } else {
         return false;
      }
   }

   private static String internalMethod07362(String localValue0) {
      if (localValue0 == null) {
         return null;
      } else {
         String localValue1 = localValue0.trim();
         if (localValue1.startsWith("/")) {
            localValue1 = localValue1.substring(1);
         }

         return localValue1;
      }
   }

   private static boolean internalMethod08717() {
      return internalField1077 != null
         && !internalField1077.isEmpty()
         && (internalMethod01786(KnownServer.internalField0578) || internalMethod01786(KnownServer.internalField1218));
   }

   private static Integer internalMethod05461(String localValue0) {
      if (localValue0 == null) {
         return null;
      } else {
         String localValue1 = localValue0.replaceAll("[^0-9]", "");
         if (localValue1.isEmpty()) {
            return null;
         } else {
            try {
               return Integer.parseInt(localValue1);
            } catch (NumberFormatException localValue3) {
               return null;
            }
         }
      }
   }

   private static String internalMethod09107(String localValue0) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         int localValue1 = localValue0.lastIndexOf(58);
         return localValue1 >= 0 ? localValue0.substring(0, localValue1) : localValue0;
      } else {
         return "";
      }
   }

   public static boolean internalMethod08700() {
      Text localValue0 = ((PlayerListHudAccessor)(Object)internalField0149.inGameHud.getPlayerListHud()).getHeader();
      String localValue1 = localValue0 != null ? localValue0.getString() : "\u0445\u0443\u0439";
      return internalMethod01786(KnownServer.internalField1218)
         || localValue1.toLowerCase().contains("holyworld")
         || localValue1.toLowerCase().contains("\u0440\u0435\u0436\u0438\u043cpvp");
   }

   @Generated
   private ServerUtils() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(internalField0290);
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   @Generated
   public static void internalMethod05301(boolean localValue0) {
      internalField0277 = localValue0;
   }

   @Generated
   public static void internalMethod05300(int localValue0) {
      internalField0227 = localValue0;
   }

   static final class InternalType0142 {
      final String internalField0248;
      final String internalField0247;
      final String internalField1077;

      InternalType0142(String localValue1, String localValue2, String localValue3) {
         this.internalField0248 = localValue1;
         this.internalField0247 = localValue2;
         this.internalField1077 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0142[orig=" + this.internalField0248 + ", big=" + this.internalField0247 + ", small=" + this.internalField1077 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0247);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1077);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ServerUtils.InternalType0142 other = (ServerUtils.InternalType0142) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0247, other.internalField0247)
            && java.util.Objects.equals(this.internalField1077, other.internalField1077);
      }

      public String internalMethod01953() {
         return this.internalField0248;
      }

      public String internalMethod06623() {
         return this.internalField0247;
      }

      public String internalMethod07860() {
         return this.internalField1077;
      }
   }
}

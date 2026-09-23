package rockstar.client.internal.script;







import rockstar.client.util.*;
import rockstar.client.server.*;
import rockstar.client.event.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import pyrock.events.player.ClientPlayerTickEvent;

public class PlayerParseCommandHandler implements MinecraftClientAccess {
   private final Queue<PlayerParseCommandHandler.InternalType0188> internalField0877 = new ArrayDeque<>();
   private PlayerParseCommandHandler.InternalType0188 internalField0861;
   private boolean internalField0277;
   private int internalField0227 = -1;
   private int internalField0228;
   private final EventListener<ClientPlayerTickEvent> internalField0157 = localValue1 -> {
      if (this.internalField0277 && this.internalField0861 != null && internalField0149.player != null && internalField0149.world != null) {
         Integer localValue2 = this.internalMethod06550(this.internalField0861.internalField0859);
         if (localValue2 != null && localValue2 == this.internalField0861.internalMethod06208()) {
            int localValue3 = internalField0149.getNetworkHandler().getPlayerList().size();
            if (localValue3 < 30) {
               this.internalMethod01046();
            } else {
               if (localValue3 == this.internalField0227) {
                  this.internalField0228++;
               } else {
                  this.internalField0228 = 0;
                  this.internalField0227 = localValue3;
               }

               if (this.internalField0228 >= 20) {
                  this.internalMethod03016(this.internalField0861.internalField0859, this.internalField0861.internalMethod06208());
                  this.internalMethod01045();
               }
            }
         }
      }
   };

   public PlayerParseCommandHandler() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   public CommandNode internalMethod00083() {
      return CommandBuilder.internalMethod07482(
            "parse",
            localValue1 -> localValue1.internalMethod05325("parser")
               .internalMethod06148("commands.parse.description")
               .internalMethod01539("args", localValue0 -> localValue0.internalMethod06921().internalMethod00125().internalMethod00776(OperationResult::internalMethod00116))
               .internalMethod00262(this::internalMethod06754)
         )
         .internalMethod04146();
   }

   private void internalMethod06754(ParsedCommand localValue1) {
      List localValue2 = localValue1.internalMethod02266().isEmpty() ? Collections.emptyList() : this.internalMethod03809(localValue1.internalMethod02266().getFirst());
      PlayerParseCommandHandler.InternalType0187 localValue3 = PlayerParseCommandHandler.InternalType0187.internalField0859;
      ArrayList localValue4 = new ArrayList();
      if (!localValue2.isEmpty()) {
         PlayerParseCommandHandler.InternalType0187 localValue5 = PlayerParseCommandHandler.InternalType0187.internalMethod04394((String)localValue2.getFirst());
         int localValue6 = 0;
         if (localValue5 != null) {
            localValue3 = localValue5;
            localValue6++;
         }

         for (int localValue7 = localValue6; localValue7 < localValue2.size(); localValue7++) {
            Integer localValue8 = this.internalMethod00413((String)localValue2.get(localValue7));
            if (localValue8 == null) {
               ClientMessages.internalMethod09025(Text.of("Invalid server number: " + (String)localValue2.get(localValue7)));
               return;
            }

            localValue4.add(localValue8);
         }
      }

      if (!localValue4.isEmpty()) {
         this.internalMethod00408(localValue3, localValue4);
      } else {
         this.internalMethod03016(this.internalMethod00615(localValue3), null);
      }
   }

   private List<String> internalMethod03809(Object localValue1) {
      return localValue1 == null ? Collections.emptyList() : (List)localValue1;
   }

   private Integer internalMethod00413(String localValue1) {
      try {
         int localValue2 = Integer.parseInt(localValue1);
         return localValue2 > 0 ? localValue2 : null;
      } catch (Exception localValue3) {
         return null;
      }
   }

   private void internalMethod00408(PlayerParseCommandHandler.InternalType0187 localValue1, List<Integer> localValue2) {
      if (this.internalField0277) {
         ClientMessages.internalMethod09025(Text.of("Parser already running"));
      } else {
         this.internalField0877.clear();
         PlayerParseCommandHandler.InternalType0187 localValue3 = this.internalMethod00615(localValue1);

         for (Integer localValue5 : localValue2) {
            this.internalField0877.add(new PlayerParseCommandHandler.InternalType0188(localValue3, localValue5));
         }

         this.internalField0277 = true;
         this.internalField0861 = null;
         this.internalMethod01045();
      }
   }

   private void internalMethod01045() {
      this.internalField0861 = this.internalField0877.poll();
      this.internalMethod01046();
      if (this.internalField0861 == null) {
         this.internalField0277 = false;
         ClientMessages.internalMethod01809(Text.of("\u0412\u044b\u0434\u043e\u0445\u0441\u044f \u0438 \u0437\u0430\u043a\u043e\u043d\u0447\u0438\u043b."));
      } else {
         this.internalMethod02067(this.internalField0861);
      }
   }

   private void internalMethod02067(PlayerParseCommandHandler.InternalType0188 localValue1) {
      String localValue2 = RockstarClient.getInstance().internalMethod05348().internalMethod03606();
      StringBuilder localValue3 = new StringBuilder(localValue2).append("rct");
      if (localValue1.internalField0859 != PlayerParseCommandHandler.InternalType0187.internalField0859) {
         localValue3.append(" ").append(localValue1.internalField0859.internalMethod07087());
      }

      localValue3.append(" ").append(localValue1.internalField0227);
      RockstarClient.getInstance().internalMethod05348().internalMethod04610(localValue3.toString());
   }

   private void internalMethod01046() {
      this.internalField0227 = -1;
      this.internalField0228 = 0;
   }

   private void internalMethod03016(PlayerParseCommandHandler.InternalType0187 localValue1, Integer localValue2) {
      if (internalField0149.isInSingleplayer()) {
         ClientMessages.internalMethod09025(Text.of("\u0422\u044b \u0435\u0431\u043b\u0430\u043d ?"));
      } else if (internalField0149.getNetworkHandler() != null && internalField0149.getNetworkHandler().getServerInfo() != null) {
         File localValue3 = new File(ConfigManager.internalField0148, "parser");
         if (!localValue3.exists() && !localValue3.mkdirs()) {
            ClientMessages.internalMethod09025(
               Text.of("\u0429\u0430 \u043f\u043e\u0434\u043e\u0436\u0434\u0438 \u043f\u0430\u043f\u043a\u0443 \u0441\u043e\u0437\u0434\u0430\u043c")
            );
         } else {
            String localValue4 = this.internalMethod07223(internalField0149.getNetworkHandler().getServerInfo().address);
            String localValue5 = this.internalMethod06711(localValue1, localValue2);
            File localValue6 = new File(new File(localValue3, localValue4), localValue5);
            if (!localValue6.exists() && !localValue6.mkdirs()) {
               ClientMessages.internalMethod09025(
                  Text.of(
                     "\u041e\u0448\u0438\u0431\u043a\u0430 \u0441\u043e\u0437\u0434\u0430\u043d\u0438\u044f \u043f\u0430\u043f\u043a\u0438 \u0430\u043d\u043a\u0438"
                  )
               );
            } else {
               Map localValue7 = this.internalMethod06093(internalField0149.getNetworkHandler().getPlayerList());
               if (localValue7.isEmpty()) {
                  ClientMessages.internalMethod09025(
                     Text.of(
                        "\u0421\u0435\u0440\u0432\u0435\u0440 \u0438\u0441\u0441\u0443\u0435 \u043b\u0430\u0433\u0430\u0435\u0442 0 \u0442\u043f\u0441 \u0441\u0435\u0440\u0432\u0435\u0440 \u0447\u0442\u043e\u043b\u0435"
                     )
                  );
               } else {
                  try {
                     this.internalMethod05731(localValue6);
                     this.internalMethod00370(localValue6, localValue7);
                     ClientMessages.internalMethod01809(
                        Text.of(
                           "\u0421\u043f\u0430\u0440\u0441\u0438\u043b "
                              + localValue7.values().stream().mapToInt(value -> ((List<?>)value).size()).sum()
                              + " \u0438\u0433\u0440\u043e\u043a\u043e\u0432"
                        )
                     );
                  } catch (Exception localValue9) {
                     ClientMessages.internalMethod09025(Text.of("Save error: " + localValue9.getMessage()));
                  }
               }
            }
         }
      } else {
         ClientMessages.internalMethod09025(Text.of("\u0421\u0435\u0440\u0432\u0435\u0440 \u043a\u0430\u043b\u043b"));
      }
   }

   private Map<String, List<String>> internalMethod06093(Collection<PlayerListEntry> localValue1) {
      TreeMap<String, List<String>> localValue2 = new TreeMap<>();

      for (PlayerListEntry localValue4 : localValue1) {
         if (localValue4.getScoreboardTeam() != null) {
            String localValue5 = localValue4.getScoreboardTeam().getPrefix().getString().trim();
            if (localValue5.isBlank()) {
               localValue5 = "\u0411\u0435\u0437\u0414\u043e\u043d\u0430\u0442\u0430";
            }

            localValue2.computeIfAbsent(localValue5, localValue0 -> new ArrayList<>()).add(localValue4.getProfile().name());
         }
      }

      return localValue2;
   }

   private void internalMethod05731(File localValue1) throws IOException {
      File[] localValue2 = localValue1.listFiles((localValue0, localValue1x) -> localValue1x.endsWith(".txt"));
      if (localValue2 != null) {
         for (File localValue6 : localValue2) {
            if (!localValue6.delete()) {
               throw new IOException("Failed delete " + localValue6.getName());
            }
         }
      }
   }

   private void internalMethod00370(File localValue1, Map<String, List<String>> localValue2) throws IOException {
      for (Entry localValue4 : localValue2.entrySet()) {
         File localValue5 = new File(localValue1, this.internalMethod07223(this.internalMethod00108((String)localValue4.getKey())) + ".txt");

         try (FileWriter localValue6 = new FileWriter(localValue5)) {
            for (String localValue8 : (Iterable<String>)(Iterable<?>)(List)localValue4.getValue()) {
               localValue6.write(localValue8 + "\n");
            }
         }
      }
   }

   private String internalMethod06711(PlayerParseCommandHandler.InternalType0187 localValue1, Integer localValue2) {
      int localValue3 = localValue2 != null ? localValue2 : Optional.ofNullable(this.internalMethod06550(localValue1)).orElse(-1);
      return localValue1.internalMethod03648() + (localValue3 > 0 ? localValue3 : "unknown");
   }

   private String internalMethod07223(String localValue1) {
      return localValue1.replace(':', '_').replaceAll("[\\\\/:*?\"<>|]", "_");
   }

   private String internalMethod00108(String localValue1) {
      return localValue1.replace("[", "").replace("]", "").trim().replaceAll("\\s+", "_").toLowerCase();
   }

   private PlayerParseCommandHandler.InternalType0187 internalMethod00615(PlayerParseCommandHandler.InternalType0187 localValue1) {
      if (localValue1 != PlayerParseCommandHandler.InternalType0187.internalField0859) {
         return localValue1;
      } else if (ServerUtils.internalMethod01786(KnownServer.internalField1220)) {
         return PlayerParseCommandHandler.InternalType0187.internalField1357;
      } else {
         return (
                  ServerUtils.internalMethod01786(KnownServer.internalField0578)
                     || ServerUtils.internalMethod01786(KnownServer.internalField0579)
               )
               && ServerUtils.internalField0276
            ? PlayerParseCommandHandler.InternalType0187.internalField1357
            : PlayerParseCommandHandler.InternalType0187.internalField0860;
      }
   }

   private Integer internalMethod06550(PlayerParseCommandHandler.InternalType0187 localValue1) {
      return switch (localValue1) {
         case internalField0860 -> ServerUtils.internalField0228;
         case internalField1357 -> ServerUtils.internalField1055;
         default -> null;
      };
   }

   static enum InternalType0187 {
      internalField0859("auto", "an"),
      internalField0860("an", "an"),
      internalField1357("grief", "grif");

      private final String internalField0248;
      private final String internalField0247;

      private InternalType0187(String localValue3, String localValue4) {
         this.internalField0248 = localValue3;
         this.internalField0247 = localValue4;
      }

      public String internalMethod07087() {
         return this.internalField0248;
      }

      public String internalMethod03648() {
         return this.internalField0247;
      }

      public static PlayerParseCommandHandler.InternalType0187 internalMethod04394(String localValue0) {
         if (localValue0 == null) {
            return null;
         } else {
            localValue0 = localValue0.toLowerCase();
            if (localValue0.startsWith("an")) {
               return internalField0860;
            } else {
               return localValue0.startsWith("gr") ? internalField1357 : null;
            }
         }
      }
   }

   static final class InternalType0188 {
      final PlayerParseCommandHandler.InternalType0187 internalField0859;
      final int internalField0227;

      InternalType0188(PlayerParseCommandHandler.InternalType0187 localValue1, int localValue2) {
         this.internalField0859 = localValue1;
         this.internalField0227 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0188[mode=" + this.internalField0859 + ", number=" + this.internalField0227 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0859);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         PlayerParseCommandHandler.InternalType0188 other = (PlayerParseCommandHandler.InternalType0188) localValue1;
         return java.util.Objects.equals(this.internalField0859, other.internalField0859)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public PlayerParseCommandHandler.InternalType0187 internalMethod06900() {
         return this.internalField0859;
      }

      public int internalMethod06208() {
         return this.internalField0227;
      }
   }
}

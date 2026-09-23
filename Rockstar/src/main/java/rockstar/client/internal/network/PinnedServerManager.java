package rockstar.client.internal.network;


import rockstar.client.*;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.network.ServerInfo.ServerType;
import net.minecraft.client.option.ServerList;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public final class PinnedServerManager {
   private static final Logger internalField0572 = LogUtils.getLogger();
   private static final HttpClient internalField0791 = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3L)).build();
   private static volatile boolean internalField0277;
   private static volatile List<PinnedServerManager.InternalType0141> internalField0416 = List.of();

   private PinnedServerManager() {
   }

   public static void internalMethod01284(MinecraftClient localValue0, ServerList localValue1, Runnable localValue2) {
      List localValue3 = internalMethod06423(localValue0);
      if (internalMethod00472(localValue1, localValue3, localValue3)) {
         localValue2.run();
      }

      CompletableFuture.supplyAsync(PinnedServerManager::internalMethod01130, Util.getIoWorkerExecutor()).thenAccept(localValue3x -> {
         if (!localValue3x.isEmpty()) {
            localValue0.execute(() -> {
               List localValue4 = internalField0416;
               internalMethod00773(localValue0, localValue3x);
               if (internalMethod00472(localValue1, localValue3x, localValue4)) {
                  localValue2.run();
               }
            });
         }
      });
   }

   public static boolean internalMethod04101(@Nullable ServerInfo localValue0) {
      if (localValue0 == null) {
         return false;
      } else {
         for (PinnedServerManager.InternalType0141 localValue2 : internalField0416) {
            if (localValue2.internalMethod07395(localValue0)) {
               return true;
            }
         }

         return false;
      }
   }

   private static List<PinnedServerManager.InternalType0141> internalMethod01130() {
      try {
         HttpRequest localValue0 = HttpRequest.newBuilder(URI.create("https://vontam.su/minecraft/ips.php"))
            .timeout(Duration.ofSeconds(3L))
            .header("User-Agent", "Rockstar/1.0")
            .GET()
            .build();
         HttpResponse localValue1 = internalField0791.send(localValue0, BodyHandlers.ofString());
         return localValue1.statusCode() >= 200 && localValue1.statusCode() < 300 ? internalMethod01880((String)localValue1.body()) : List.of();
      } catch (Exception localValue2) {
         internalField0572.debug("Failed to fetch pinned multiplayer server", localValue2);
         return List.of();
      }
   }

   private static List<PinnedServerManager.InternalType0141> internalMethod06423(MinecraftClient localValue0) {
      if (internalField0277) {
         return internalField0416;
      } else {
         synchronized (PinnedServerManager.class) {
            if (internalField0277) {
               return internalField0416;
            } else {
               internalField0277 = true;

               try {
                  Path localValue2 = internalMethod04484(localValue0);
                  if (!Files.exists(localValue2)) {
                     return internalField0416;
                  }

                  internalField0416 = internalMethod01880(Files.readString(localValue2));
               } catch (Exception localValue4) {
                  internalField0572.debug("Failed to load pinned multiplayer server cache", localValue4);
               }

               return internalField0416;
            }
         }
      }
   }

   private static void internalMethod00773(MinecraftClient localValue0, List<PinnedServerManager.InternalType0141> localValue1) {
      internalField0416 = List.copyOf(localValue1);
      internalField0277 = true;

      try {
         Files.writeString(
            internalMethod04484(localValue0),
            localValue1.stream().map(PinnedServerManager.InternalType0141::internalMethod08725).collect(Collectors.joining(System.lineSeparator()))
         );
      } catch (IOException localValue3) {
         internalField0572.debug("Failed to store pinned multiplayer server cache", localValue3);
      }
   }

   private static boolean internalMethod00472(ServerList localValue0, List<PinnedServerManager.InternalType0141> localValue1, List<PinnedServerManager.InternalType0141> localValue2) {
      if (localValue1.isEmpty()) {
         return false;
      } else {
         boolean localValue3 = false;
         ArrayList localValue4 = new ArrayList(localValue1.size());

         for (PinnedServerManager.InternalType0141 localValue6 : localValue1) {
            PinnedServerManager.InternalType0141 localValue7 = internalMethod07075(localValue2, localValue6);
            ServerInfo localValue8 = internalMethod07029(localValue0, localValue4, localValue6, localValue7);
            if (localValue8 == null) {
               localValue8 = new ServerInfo(localValue6.internalMethod04758(), localValue6.internalMethod01293(), ServerType.OTHER);
               localValue0.add(localValue8, false);
               localValue3 = true;
            }

            if (!Objects.equals(localValue8.name, localValue6.internalMethod04758())) {
               localValue8.name = localValue6.internalMethod04758();
               localValue3 = true;
            }

            if (!Objects.equals(localValue8.address, localValue6.internalMethod01293())) {
               localValue8.address = localValue6.internalMethod01293();
               localValue3 = true;
            }

            localValue4.add(new PinnedServerManager.InternalType0140(localValue6, localValue7, localValue8));
         }

         for (int localValue9 = localValue0.size() - 1; localValue9 >= 0; localValue9--) {
            ServerInfo localValue11 = localValue0.get(localValue9);
            if (!internalMethod03180(localValue4, localValue11) && internalMethod00757(localValue4, localValue11)) {
               localValue0.remove(localValue11);
               localValue3 = true;
            }
         }

         for (int localValue10 = 0; localValue10 < localValue4.size(); localValue10++) {
            ServerInfo localValue12 = ((PinnedServerManager.InternalType0140)localValue4.get(localValue10)).internalMethod04299();

            for (int localValue13 = internalMethod01769(localValue0, localValue12); localValue13 > localValue10; localValue3 = true) {
               localValue0.swapEntries(localValue13, localValue13 - 1);
               localValue13--;
            }
         }

         if (localValue3) {
            localValue0.saveFile();
         }

         return localValue3;
      }
   }

   private static int internalMethod01769(ServerList localValue0, ServerInfo localValue1) {
      for (int localValue2 = 0; localValue2 < localValue0.size(); localValue2++) {
         if (localValue0.get(localValue2) == localValue1) {
            return localValue2;
         }
      }

      return -1;
   }

   @Nullable
   private static PinnedServerManager.InternalType0141 internalMethod07075(
      List<PinnedServerManager.InternalType0141> localValue0, PinnedServerManager.InternalType0141 localValue1
   ) {
      for (PinnedServerManager.InternalType0141 localValue3 : localValue0) {
         if (localValue3.internalField0247.equalsIgnoreCase(localValue1.internalField0247)) {
            return localValue3;
         }
      }

      for (PinnedServerManager.InternalType0141 localValue5 : localValue0) {
         if (localValue5.internalField0248.equalsIgnoreCase(localValue1.internalField0248)) {
            return localValue5;
         }
      }

      return null;
   }

   @Nullable
   private static ServerInfo internalMethod07029(
      ServerList localValue0,
      List<PinnedServerManager.InternalType0140> localValue1,
      PinnedServerManager.InternalType0141 localValue2,
      @Nullable PinnedServerManager.InternalType0141 localValue3
   ) {
      for (int localValue4 = 0; localValue4 < localValue0.size(); localValue4++) {
         ServerInfo localValue5 = localValue0.get(localValue4);
         if (!internalMethod03180(localValue1, localValue5)) {
            if (localValue2.internalMethod07395(localValue5)) {
               return localValue5;
            }

            if (localValue3 != null && localValue3.internalMethod07395(localValue5)) {
               return localValue5;
            }
         }
      }

      return null;
   }

   private static boolean internalMethod00757(List<PinnedServerManager.InternalType0140> localValue0, ServerInfo localValue1) {
      for (PinnedServerManager.InternalType0140 localValue3 : localValue0) {
         if (localValue3.internalMethod02605(localValue1)) {
            return true;
         }
      }

      return false;
   }

   private static boolean internalMethod03180(List<PinnedServerManager.InternalType0140> localValue0, ServerInfo localValue1) {
      for (PinnedServerManager.InternalType0140 localValue3 : localValue0) {
         if (localValue3.internalMethod04299() == localValue1) {
            return true;
         }
      }

      return false;
   }

   private static Path internalMethod04484(MinecraftClient localValue0) {
      return localValue0.runDirectory.toPath().resolve("rockstar-pinned-multiplayer.txt");
   }

   private static List<PinnedServerManager.InternalType0141> internalMethod01880(String localValue0) {
      if (localValue0 != null && !localValue0.isBlank()) {
         ArrayList localValue1 = new ArrayList();

         for (String localValue5 : localValue0.split("\\R")) {
            String localValue6 = localValue5.trim();
            if (!localValue6.isEmpty()) {
               int localValue7 = localValue6.indexOf(36);
               if (localValue7 > 0 && localValue7 < localValue6.length() - 1) {
                  String localValue8 = localValue6.substring(0, localValue7).trim();
                  String localValue9 = localValue6.substring(localValue7 + 1).trim();
                  if (!localValue8.isEmpty() && !localValue9.isEmpty()) {
                     localValue1.add(new PinnedServerManager.InternalType0141(localValue8, localValue9));
                  }
               }
            }
         }

         return List.copyOf(localValue1);
      } else {
         return List.of();
      }
   }

   static final class InternalType0140 {
      private final PinnedServerManager.InternalType0141 internalField0954;
      @Nullable
      private final PinnedServerManager.InternalType0141 internalField0955;
      private final ServerInfo internalField0391;

      InternalType0140(PinnedServerManager.InternalType0141 localValue1, @Nullable PinnedServerManager.InternalType0141 localValue2, ServerInfo localValue3) {
         this.internalField0954 = localValue1;
         this.internalField0955 = localValue2;
         this.internalField0391 = localValue3;
      }

      boolean internalMethod02605(ServerInfo localValue1) {
         return this.internalField0954.internalMethod07395(localValue1) ? true : this.internalField0955 != null && this.internalField0955.internalMethod07395(localValue1);
      }

      @Override
      public final String toString() {
         return "InternalType0140[definition=" + this.internalField0954 + ", previousDefinition=" + this.internalField0955 + ", serverInfo=" + this.internalField0391 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0954);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0955);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0391);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         PinnedServerManager.InternalType0140 other = (PinnedServerManager.InternalType0140) localValue1;
         return java.util.Objects.equals(this.internalField0954, other.internalField0954)
            && java.util.Objects.equals(this.internalField0955, other.internalField0955)
            && java.util.Objects.equals(this.internalField0391, other.internalField0391);
      }

      public PinnedServerManager.InternalType0141 internalMethod05333() {
         return this.internalField0954;
      }

      @Nullable
      public PinnedServerManager.InternalType0141 internalMethod05523() {
         return this.internalField0955;
      }

      public ServerInfo internalMethod04299() {
         return this.internalField0391;
      }
   }

   static final class InternalType0141 {
      final String internalField0248;
      final String internalField0247;

      InternalType0141(String localValue1, String localValue2) {
         this.internalField0248 = localValue1;
         this.internalField0247 = localValue2;
      }

      boolean internalMethod07395(@Nullable ServerInfo localValue1) {
         return localValue1 != null && this.internalField0247.equalsIgnoreCase(localValue1.address);
      }

      private String internalMethod08725() {
         return this.internalField0248 + "$" + this.internalField0247;
      }

      @Override
      public final String toString() {
         return "InternalType0141[name=" + this.internalField0248 + ", address=" + this.internalField0247 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0247);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         PinnedServerManager.InternalType0141 other = (PinnedServerManager.InternalType0141) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0247, other.internalField0247);
      }

      public String internalMethod04758() {
         return this.internalField0248;
      }

      public String internalMethod01293() {
         return this.internalField0247;
      }
   }
}

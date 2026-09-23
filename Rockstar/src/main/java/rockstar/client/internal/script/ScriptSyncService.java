package rockstar.client.internal.script;


import rockstar.client.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import globals.client.api.RockNetClient;
import globals.shared.proto.Packet;
import globals.shared.proto.Packets;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import net.minecraft.client.MinecraftClient;

public class ScriptSyncService {
   private static final String internalField0248 = "python";
   private static final String internalField0247 = ".py";
   private final Path internalField0214;
   private final Path internalField0215;
   private final Object internalField0290 = new Object();
   private final Map<String, String> internalField0543 = new HashMap<>();
   private final Map<String, byte[]> internalField0544 = new ConcurrentHashMap<>();
   private volatile boolean internalField0277 = false;
   private volatile WatchService internalField0049;
   private volatile Thread internalField0380;
   private volatile boolean internalField0276 = true;
   private final ScheduledExecutorService internalField0220 = Executors.newSingleThreadScheduledExecutor(localValue0 -> {
      Thread localValue1 = new Thread(localValue0, "Script-Reload");
      localValue1.setDaemon(true);
      return localValue1;
   });
   private volatile ScheduledFuture<?> internalField0825;

   public ScriptSyncService() {
      this.internalField0214 = Path.of(ConfigManager.internalField0148.getPath(), "scripts");
      this.internalField0215 = this.internalField0214.resolve(".sync.json");

      try {
         Files.createDirectories(this.internalField0214);
      } catch (Exception localValue2) {
         RockstarClient.internalField0572
            .error(
               "[Scripts] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043e\u0437\u0434\u0430\u0442\u044c \u043f\u0430\u043f\u043a\u0443 \u0441\u043a\u0440\u0438\u043f\u0442\u043e\u0432",
               localValue2
            );
      }

      this.internalMethod09203();
      this.internalMethod08551();
   }

   public void internalMethod02595() {
      this.internalField0277 = false;
      this.internalMethod05589(new Packets.InternalType0239());
   }

   public void internalMethod02597() {
      this.internalField0277 = false;
   }

   public void internalMethod01582(List<Packets.InternalType0238> localValue1) {
      synchronized (this.internalField0290) {
         try {
            this.internalMethod01187(localValue1 == null ? List.of() : localValue1);
         } catch (Exception localValue9) {
            RockstarClient.internalField0572.error("[Scripts] reconcile failed", localValue9);
         } finally {
            this.internalField0277 = true;
         }
      }

      if (localValue1 != null) {
         for (Packets.InternalType0238 localValue3 : localValue1) {
            if (localValue3 != null) {
               ScriptLibraryInstaller.internalMethod03301(localValue3.libraries());
            }
         }
      }
   }

   public void internalMethod00177(String localValue1, String localValue2, List<String> localValue3) {
      if (!internalMethod03619(localValue1)) {
         synchronized (this.internalField0290) {
            boolean localValue5 = this.internalMethod07378(localValue1).exists();
            this.internalMethod05409(localValue1, localValue2 == null ? "" : localValue2);
            this.internalMethod08560();
            if (!localValue5) {
               this.internalMethod08558();
            }
         }

         ScriptLibraryInstaller.internalMethod03301(localValue3);
      }
   }

   public void internalMethod01471(String localValue1, String localValue2, List<String> localValue3) {
      if (!internalMethod03619(localValue1) && localValue2 != null) {
         synchronized (this.internalField0290) {
            byte[] localValue5;
            try {
               localValue5 = Base64.getDecoder().decode(localValue2.trim());
            } catch (Exception localValue8) {
               RockstarClient.internalField0572
                  .error(
                     "[Scripts] \u0431\u0438\u0442\u044b\u0439 \u0437\u0430\u0449\u0438\u0449\u0451\u043d\u043d\u044b\u0439 \u0441\u043a\u0440\u0438\u043f\u0442 {}",
                     localValue1,
                     localValue8
                  );
               return;
            }

            byte[] localValue6 = this.internalField0544.get(localValue1);
            if (localValue6 != null && Arrays.equals(localValue6, localValue5)) {
               ScriptLibraryInstaller.internalMethod03301(localValue3);
               return;
            }

            this.internalField0544.put(localValue1, localValue5);
            this.internalMethod08558();
         }

         ScriptLibraryInstaller.internalMethod03301(localValue3);
      }
   }

   public void internalMethod03618(String localValue1) {
      if (!internalMethod03619(localValue1)) {
         synchronized (this.internalField0290) {
            this.internalMethod09129(localValue1);
            this.internalField0544.remove(localValue1);
            this.internalMethod08560();
            this.internalMethod08558();
         }
      }
   }

   public void internalMethod06222(String localValue1, String localValue2) {
      if (!internalMethod03619(localValue1) && !internalMethod03619(localValue2)) {
         synchronized (this.internalField0290) {
            this.internalMethod07736(localValue1, localValue2);
            byte[] localValue4 = this.internalField0544.remove(localValue1);
            if (localValue4 != null) {
               this.internalField0544.put(localValue2, localValue4);
            }

            this.internalMethod08560();
            this.internalMethod08558();
         }
      }
   }

   private void internalMethod01187(List<Packets.InternalType0238> localValue1) {
      Map localValue2 = this.internalMethod02874();
      HashMap localValue3 = new HashMap();

      for (Packets.InternalType0238 localValue5 : localValue1) {
         if (localValue5 != null && !internalMethod03619(localValue5.name())) {
            localValue3.put(internalMethod03452(localValue5.name()), localValue5);
         }
      }

      HashSet localValue15 = new HashSet();
      localValue15.addAll(localValue2.keySet());
      localValue15.addAll(localValue3.keySet());
      boolean localValue16 = false;

      for (String localValue7 : (Iterable<String>)(Iterable<?>)localValue15) {
         ScriptSyncService.InternalType0294 localValue8 = (ScriptSyncService.InternalType0294)localValue2.get(localValue7);
         Packets.InternalType0238 localValue9 = (Packets.InternalType0238)localValue3.get(localValue7);
         String localValue10 = this.internalField0543.get(localValue7);
         if (localValue8 != null && localValue8.internalField0277) {
            RockstarClient.internalField0572
               .warn(
                  "[Scripts] '{}' \u0431\u043e\u043b\u044c\u0448\u0435 {} \u041c\u0411 \u2014 \u0441\u0438\u043d\u0445\u0440\u043e\u043d\u0438\u0437\u0430\u0446\u0438\u044f \u044d\u0442\u043e\u0433\u043e \u0444\u0430\u0439\u043b\u0430 \u043f\u0440\u043e\u043f\u0443\u0449\u0435\u043d\u0430",
                  localValue8.internalField0248,
                  4
               );
         } else if (localValue9 != null && localValue8 != null) {
            String localValue17 = internalMethod07851(localValue8.internalField0247);
            String localValue12 = internalMethod07851(localValue9.source());
            if (localValue17.equals(localValue12)) {
               this.internalField0543.put(localValue7, localValue17);
            } else {
               boolean localValue13 = localValue10 == null || !localValue17.equals(localValue10);
               boolean localValue14 = localValue10 == null || !localValue12.equals(localValue10);
               if (localValue13 && !localValue14) {
                  this.internalMethod09138(localValue8.internalField0248, localValue8.internalField0247);
               } else {
                  this.internalMethod05409(localValue9.name(), localValue9.source());
               }
            }
         } else if (localValue9 != null) {
            if (localValue10 != null && internalMethod07851(localValue9.source()).equals(localValue10)) {
               this.internalMethod08849(localValue9.name());
            } else {
               this.internalMethod05409(localValue9.name(), localValue9.source());
               localValue16 = true;
            }
         } else {
            String localValue11 = internalMethod07851(localValue8.internalField0247);
            if (localValue10 != null && localValue11.equals(localValue10)) {
               this.internalMethod09129(localValue8.internalField0248);
               localValue16 = true;
            } else {
               this.internalMethod09138(localValue8.internalField0248, localValue8.internalField0247);
            }
         }
      }

      this.internalMethod08560();
      if (localValue16) {
         this.internalMethod08558();
      }
   }

   private void internalMethod08551() {
      try {
         WatchService localValue1 = this.internalField0214.getFileSystem().newWatchService();
         this.internalField0214.register(localValue1, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
         this.internalField0049 = localValue1;
         Thread localValue2 = new Thread(() -> this.internalMethod05635(localValue1), "Script-Watch");
         localValue2.setDaemon(true);
         localValue2.start();
         this.internalField0380 = localValue2;
      } catch (Exception localValue3) {
         RockstarClient.internalField0572
            .error(
               "[Scripts] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0437\u0430\u043f\u0443\u0441\u0442\u0438\u0442\u044c watcher \u043f\u0430\u043f\u043a\u0438 \u0441\u043a\u0440\u0438\u043f\u0442\u043e\u0432",
               localValue3
            );
      }
   }

   private void internalMethod05635(WatchService localValue1) {
      while (this.internalField0276) {
         WatchKey localValue2;
         try {
            localValue2 = localValue1.poll(400L, TimeUnit.MILLISECONDS);
         } catch (InterruptedException localValue9) {
            Thread.currentThread().interrupt();
            return;
         } catch (Exception localValue10) {
            return;
         }

         if (localValue2 != null) {
            HashSet localValue3 = new HashSet();

            for (WatchEvent localValue5 : localValue2.pollEvents()) {
               if (localValue5.context() instanceof Path localValue7) {
                  String localValue8 = localValue7.getFileName().toString();
                  if (localValue8.endsWith(".py")) {
                     localValue3.add(localValue8);
                  }
               }
            }

            boolean localValue11 = localValue2.reset();

            for (String localValue13 : (Iterable<String>)(Iterable<?>)localValue3) {
               this.internalMethod02161(localValue13);
            }

            if (!localValue11) {
               return;
            }
         }
      }
   }

   private void internalMethod02161(String localValue1) {
      synchronized (this.internalField0290) {
         if (this.internalField0277) {
            String localValue3 = internalMethod02491(localValue1);
            String localValue4 = internalMethod03452(localValue3);
            File localValue5 = this.internalField0214.resolve(localValue1).toFile();
            if (localValue5.exists()) {
               if (localValue5.length() > 4194304L) {
                  RockstarClient.internalField0572
                     .warn(
                        "[Scripts] '{}' \u0431\u043e\u043b\u044c\u0448\u0435 {} \u041c\u0411 \u2014 \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440 \u043d\u0435 \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u044f\u0435\u043c",
                        localValue3,
                        4
                     );
                  return;
               }

               String localValue6 = internalMethod02892(localValue5);
               if (localValue6 == null) {
                  return;
               }

               String localValue7 = internalMethod07851(localValue6);
               if (localValue7.equals(this.internalField0543.get(localValue4))) {
                  return;
               }

               this.internalField0543.put(localValue4, localValue7);
               this.internalMethod08560();
               this.internalMethod05589(new Packets.InternalType0240(localValue3, "python", localValue6));
            } else {
               if (!this.internalField0543.containsKey(localValue4)) {
                  return;
               }

               this.internalField0543.remove(localValue4);
               this.internalMethod08560();
               this.internalMethod05589(new Packets.InternalType0036(localValue3));
            }
         }
      }
   }

   private void internalMethod05409(String localValue1, String localValue2) {
      String localValue3 = localValue2 == null ? "" : localValue2;
      this.internalField0543.put(internalMethod03452(localValue1), internalMethod07851(localValue3));

      try {
         ConfigManager.internalMethod04682(this.internalMethod07378(localValue1), localValue3);
      } catch (Exception localValue5) {
         RockstarClient.internalField0572
            .error(
               "[Scripts] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0441\u043a\u0440\u0438\u043f\u0442 {}",
               localValue1,
               localValue5
            );
      }
   }

   private void internalMethod09129(String localValue1) {
      this.internalField0543.remove(internalMethod03452(localValue1));

      try {
         Files.deleteIfExists(this.internalMethod07378(localValue1).toPath());
      } catch (Exception localValue3) {
         RockstarClient.internalField0572
            .error(
               "[Scripts] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0443\u0434\u0430\u043b\u0438\u0442\u044c \u0441\u043a\u0440\u0438\u043f\u0442 {}",
               localValue1,
               localValue3
            );
      }
   }

   private void internalMethod07736(String localValue1, String localValue2) {
      File localValue3 = this.internalMethod07378(localValue1);
      File localValue4 = this.internalMethod07378(localValue2);
      String localValue5 = this.internalField0543.get(internalMethod03452(localValue1));
      this.internalField0543.remove(internalMethod03452(localValue1));
      if (localValue5 != null) {
         this.internalField0543.put(internalMethod03452(localValue2), localValue5);
      }

      try {
         if (localValue3.exists()) {
            Files.deleteIfExists(localValue4.toPath());
            Files.move(localValue3.toPath(), localValue4.toPath());
         }
      } catch (Exception localValue7) {
         RockstarClient.internalField0572
            .error(
               "[Scripts] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u0435\u0440\u0435\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u0442\u044c \u0441\u043a\u0440\u0438\u043f\u0442 {} -> {}",
               new Object[]{localValue1, localValue2, localValue7}
            );
      }
   }

   private Map<String, ScriptSyncService.InternalType0294> internalMethod02874() {
      HashMap localValue1 = new HashMap();
      if (!Files.exists(this.internalField0214)) {
         return localValue1;
      } else {
         try (Stream<Path> localValue2 = Files.list(this.internalField0214)) {
            localValue2.filter(localValue0 -> Files.isRegularFile(localValue0)).filter(localValue0 -> ((java.nio.file.Path)localValue0).getFileName().toString().endsWith(".py")).forEach(localValue1x -> {
               String localValue2x = internalMethod02491(((java.nio.file.Path)localValue1x).getFileName().toString());
               File localValue3 = ((java.nio.file.Path)localValue1x).toFile();
               if (localValue3.length() > 4194304L) {
                  localValue1.put(internalMethod03452(localValue2x), new ScriptSyncService.InternalType0294(localValue2x, "", true));
               } else {
                  String localValue4 = internalMethod02892(localValue3);
                  if (localValue4 != null) {
                     localValue1.put(internalMethod03452(localValue2x), new ScriptSyncService.InternalType0294(localValue2x, localValue4, false));
                  }
               }
            });
         } catch (Exception localValue7) {
            RockstarClient.internalField0572
               .error(
                  "[Scripts] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u0440\u043e\u0447\u0438\u0442\u0430\u0442\u044c \u043f\u0430\u043f\u043a\u0443 \u0441\u043a\u0440\u0438\u043f\u0442\u043e\u0432",
                  localValue7
               );
         }

         return localValue1;
      }
   }

   private void internalMethod09138(String localValue1, String localValue2) {
      this.internalField0543.put(internalMethod03452(localValue1), internalMethod07851(localValue2 == null ? "" : localValue2));
      this.internalMethod05589(new Packets.InternalType0240(localValue1, "python", localValue2 == null ? "" : localValue2));
   }

   private void internalMethod08849(String localValue1) {
      this.internalField0543.remove(internalMethod03452(localValue1));
      this.internalMethod05589(new Packets.InternalType0036(localValue1));
   }

   private void internalMethod08558() {
      ScheduledFuture localValue1 = this.internalField0825;
      if (localValue1 != null) {
         localValue1.cancel(false);
      }

      this.internalField0825 = this.internalField0220.schedule(() -> MinecraftClient.getInstance().execute(() -> {
         try {
            PythonScriptManager localValue0 = RockstarClient.getInstance().internalMethod04979();
            if (localValue0 != null) {
               localValue0.internalMethod07673();
            }
         } catch (Exception localValue1x) {
            RockstarClient.internalField0572.error("[Scripts] py.reload failed", localValue1x);
         }
      }), 400L, TimeUnit.MILLISECONDS);
   }

   private void internalMethod08560() {
      try {
         JsonObject localValue1 = new JsonObject();

         for (Entry localValue3 : this.internalField0543.entrySet()) {
            localValue1.addProperty((String)localValue3.getKey(), (String)localValue3.getValue());
         }

         ConfigManager.internalMethod04682(this.internalField0215.toFile(), localValue1.toString());
      } catch (Exception localValue4) {
         RockstarClient.internalField0572
            .error(
               "[Scripts] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c baseline \u0441\u0438\u043d\u043a\u0430",
               localValue4
            );
      }
   }

   private void internalMethod09203() {
      try {
         if (!Files.exists(this.internalField0215)) {
            return;
         }

         String localValue1 = Files.readString(this.internalField0215, StandardCharsets.UTF_8);
         JsonObject localValue2 = JsonParser.parseString(localValue1).getAsJsonObject();

         for (String localValue4 : localValue2.keySet()) {
            this.internalField0543.put(localValue4, localValue2.get(localValue4).getAsString());
         }
      } catch (Exception localValue5) {
         RockstarClient.internalField0572
            .error(
               "[Scripts] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u0440\u043e\u0447\u0438\u0442\u0430\u0442\u044c baseline \u0441\u0438\u043d\u043a\u0430",
               localValue5
            );
      }
   }

   public void internalMethod08550() {
      this.internalField0276 = false;

      try {
         WatchService localValue1 = this.internalField0049;
         if (localValue1 != null) {
            localValue1.close();
         }
      } catch (Exception localValue2) {
      }

      Thread localValue3 = this.internalField0380;
      if (localValue3 != null) {
         localValue3.interrupt();
      }

      this.internalField0220.shutdownNow();
   }

   private File internalMethod07378(String localValue1) {
      return this.internalField0214.resolve(localValue1 + ".py").toFile();
   }

   public Map<String, byte[]> internalMethod06391() {
      return new HashMap<>(this.internalField0544);
   }

   private static String internalMethod02491(String localValue0) {
      int localValue1 = localValue0.lastIndexOf(46);
      return localValue1 > 0 ? localValue0.substring(0, localValue1) : localValue0;
   }

   private static String internalMethod03452(String localValue0) {
      return localValue0.trim().toLowerCase(Locale.ROOT);
   }

   private static boolean internalMethod03619(String localValue0) {
      return localValue0 == null || localValue0.isBlank();
   }

   private static String internalMethod02892(File localValue0) {
      try {
         return Files.readString(localValue0.toPath(), StandardCharsets.UTF_8);
      } catch (Exception localValue2) {
         return null;
      }
   }

   private static String internalMethod07851(String localValue0) {
      try {
         MessageDigest localValue1 = MessageDigest.getInstance("SHA-256");
         byte[] localValue2 = localValue1.digest((localValue0 == null ? "" : localValue0).getBytes(StandardCharsets.UTF_8));
         StringBuilder localValue3 = new StringBuilder(localValue2.length * 2);

         for (byte localValue7 : localValue2) {
            localValue3.append(Character.forDigit(localValue7 >> 4 & 15, 16)).append(Character.forDigit(localValue7 & 15, 16));
         }

         return localValue3.toString();
      } catch (Exception localValue8) {
         return Integer.toHexString((localValue0 == null ? "" : localValue0).hashCode());
      }
   }

   private void internalMethod05589(Packet localValue1) {
      RockNetClient localValue2 = RockstarClient.getInstance().internalMethod06050();
      if (localValue2 != null) {
         localValue2.send(localValue1);
      }
   }

   static final class InternalType0294 {
      final String internalField0248;
      final String internalField0247;
      final boolean internalField0277;

      InternalType0294(String localValue1, String localValue2, boolean localValue3) {
         this.internalField0248 = localValue1;
         this.internalField0247 = localValue2;
         this.internalField0277 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0294[name=" + this.internalField0248 + ", source=" + this.internalField0247 + ", oversize=" + this.internalField0277 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0247);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ScriptSyncService.InternalType0294 other = (ScriptSyncService.InternalType0294) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0247, other.internalField0247)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277);
      }

      public String internalMethod02237() {
         return this.internalField0248;
      }

      public String internalMethod06868() {
         return this.internalField0247;
      }

      public boolean internalMethod06650() {
         return this.internalField0277;
      }
   }
}

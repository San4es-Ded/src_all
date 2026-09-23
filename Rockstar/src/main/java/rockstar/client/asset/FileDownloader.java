package rockstar.client.asset;


import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.network.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;

public final class FileDownloader {
   private static final Set<String> internalField0546 = ConcurrentHashMap.newKeySet();
   private static final Map<String, Long> internalField0543 = new ConcurrentHashMap<>();

   private FileDownloader() {
   }

   public static boolean internalMethod06346(String localValue0) {
      if (localValue0 == null) {
         return false;
      } else {
         String localValue1 = localValue0.toLowerCase(Locale.ROOT);
         return localValue1.startsWith("http://") || localValue1.startsWith("https://");
      }
   }

   public static Path internalMethod03010(String localValue0) {
      Path localValue1 = internalMethod01090(localValue0);
      return Files.isRegularFile(localValue1) ? localValue1 : null;
   }

   public static void internalMethod07340(String localValue0, Consumer<Path> localValue1) {
      Path localValue2 = internalMethod03010(localValue0);
      if (localValue2 != null) {
         localValue1.accept(localValue2);
      } else {
         Long localValue3 = internalField0543.get(localValue0);
         if (localValue3 == null || System.currentTimeMillis() >= localValue3) {
            if (internalField0546.add(localValue0)) {
               Thread localValue4 = new Thread(
                  () -> {
                     try {
                        byte[] localValue2x = HttpDownloadUtils.internalMethod05458(localValue0);
                        Path localValue3x = internalMethod01090(localValue0);
                        Files.createDirectories(localValue3x.getParent());
                        Path localValue4x = localValue3x.resolveSibling(localValue3x.getFileName() + ".part");
                        Files.write(localValue4x, localValue2x);
                        Files.move(localValue4x, localValue3x, StandardCopyOption.REPLACE_EXISTING);
                        internalField0543.remove(localValue0);
                        MinecraftClient.getInstance().execute(() -> localValue1.accept(localValue3x));
                     } catch (Exception localValue8) {
                        internalField0543.put(localValue0, System.currentTimeMillis() + 30000L);
                        RockstarClient.internalField0572
                           .warn(
                              "\u0410\u0441\u0441\u0435\u0442\u044b: \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043a\u0430\u0447\u0430\u0442\u044c {}: {}",
                              localValue0,
                              localValue8.getMessage()
                           );
                     } finally {
                        internalField0546.remove(localValue0);
                     }
                  },
                  "rockstar-web-asset"
               );
               localValue4.setDaemon(true);
               localValue4.start();
            }
         }
      }
   }

   public static Path internalMethod04812() {
      return Path.of(ConfigManager.internalField0148.toURI()).resolve("cache").resolve("web");
   }

   private static Path internalMethod01090(String localValue0) {
      return internalMethod04812().resolve(internalMethod00784(localValue0) + internalMethod01815(localValue0));
   }

   private static String internalMethod00784(String localValue0) {
      try {
         MessageDigest localValue1 = MessageDigest.getInstance("SHA-1");
         return HexFormat.of().formatHex(localValue1.digest(localValue0.getBytes(StandardCharsets.UTF_8)));
      } catch (Exception localValue2) {
         return Integer.toHexString(localValue0.hashCode());
      }
   }

   private static String internalMethod01815(String localValue0) {
      String localValue1 = localValue0;
      int localValue2 = localValue0.indexOf(63);
      if (localValue2 >= 0) {
         localValue1 = localValue0.substring(0, localValue2);
      }

      int localValue3 = localValue1.lastIndexOf(47);
      int localValue4 = localValue1.lastIndexOf(46);
      if (localValue4 > localValue3 && localValue4 != localValue1.length() - 1) {
         String localValue5 = localValue1.substring(localValue4).toLowerCase(Locale.ROOT);
         return localValue5.matches("\\.[a-z0-9]{1,8}") ? localValue5 : "";
      } else {
         return "";
      }
   }
}

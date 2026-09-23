package rockstar.client.internal.network;




import rockstar.client.util.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.minecraft.text.Text;

public final class PythonRuntimeInstaller {
   private static final Duration internalField0590 = Duration.ofSeconds(15L);
   private static final Duration internalField0589 = Duration.ofSeconds(20L);
   private static final Duration internalField1226 = Duration.ofMinutes(10L);
   private static volatile boolean internalField0277;

   private PythonRuntimeInstaller() {
   }

   public static void internalMethod01801(String localValue0) {
      if (localValue0 == null || localValue0.isBlank()) {
         PythonTrainerService.internalMethod07293();
      } else if (!internalField0277) {
         internalField0277 = true;
         Thread localValue1 = new Thread(
            () -> {
               try {
                  internalMethod00251(localValue0);
               } catch (Throwable localValue5) {
                  RockstarClient.internalField0572
                     .error(
                        "[Runtime] \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0430 \u043f\u0438\u0442\u043e\u043d-\u0440\u0430\u043d\u0442\u0430\u0439\u043c\u0430 \u0441\u043e\u0440\u0432\u0430\u043b\u0430\u0441\u044c",
                        localValue5
                     );
               } finally {
                  internalField0277 = false;
                  PythonTrainerService.internalMethod07293();
               }
            },
            "Python-Runtime-Installer"
         );
         localValue1.setDaemon(true);
         localValue1.start();
      }
   }

   private static void internalMethod00251(String localValue0) throws Exception {
      internalMethod04628(EmbeddedPythonRuntime.internalMethod00088().toPath().resolveSibling("python.old"));
      HttpClient localValue1 = HttpClient.newBuilder().connectTimeout(internalField0590).followRedirects(Redirect.NORMAL).build();
      JsonObject localValue2 = internalMethod01577(localValue1, localValue0);
      if (localValue2 != null) {
         String localValue3 = localValue2.get("sha256").getAsString().toLowerCase(Locale.ROOT);
         long localValue4 = localValue2.has("size") ? localValue2.get("size").getAsLong() : -1L;
         if (EmbeddedPythonRuntime.internalMethod07577() && localValue3.equals(internalMethod04741())) {
            RockstarClient.internalField0572
               .info(
                  "[Runtime] \u043f\u0438\u0442\u043e\u043d-\u0440\u0430\u043d\u0442\u0430\u0439\u043c \u043d\u0430 \u043c\u0435\u0441\u0442\u0435 \u0438 \u0430\u043a\u0442\u0443\u0430\u043b\u0435\u043d"
               );
         } else {
            boolean localValue6 = EmbeddedPythonRuntime.internalMethod07577();
            RockstarClient.internalField0572
               .info(
                  "[Runtime] {} \u043f\u0438\u0442\u043e\u043d-\u0440\u0430\u043d\u0442\u0430\u0439\u043c ({} \u041c\u0411)...",
                  localValue6 ? "\u043e\u0431\u043d\u043e\u0432\u043b\u044f\u044e" : "\u043a\u0430\u0447\u0430\u044e",
                  Math.max(localValue4, 0L) / 1048576L
               );
            internalMethod08874(
               localValue6
                  ? "\u041e\u0431\u043d\u043e\u0432\u043b\u044f\u044e Python-\u0440\u0430\u043d\u0442\u0430\u0439\u043c \u0434\u043b\u044f \u0441\u043a\u0440\u0438\u043f\u0442\u043e\u0432, \u044d\u0442\u043e \u0437\u0430\u0439\u043c\u0451\u0442 \u043c\u0438\u043d\u0443\u0442\u0443..."
                  : "\u041a\u0430\u0447\u0430\u044e Python-\u0440\u0430\u043d\u0442\u0430\u0439\u043c \u0434\u043b\u044f \u0441\u043a\u0440\u0438\u043f\u0442\u043e\u0432 (~"
                     + Math.max(localValue4, 0L) / 1048576L
                     + " \u041c\u0411), \u044d\u0442\u043e \u0440\u0430\u0437\u043e\u0432\u0430\u044f \u043e\u043f\u0435\u0440\u0430\u0446\u0438\u044f..."
            );
            Path localValue7 = EmbeddedPythonRuntime.internalMethod00088().toPath();
            Path localValue8 = localValue7.resolveSibling("python.download");
            Path localValue9 = localValue7.resolveSibling("python-runtime.zip.part");
            internalMethod04628(localValue8);
            Files.createDirectories(localValue8);
            Files.createDirectories(localValue9.getParent());

            try {
               String localValue10 = internalMethod04549(localValue1, localValue0 + "/runtime/download?os=" + internalMethod01266(), localValue9, localValue4);
               if (!localValue3.equals(localValue10)) {
                  RockstarClient.internalField0572
                     .error(
                        "[Runtime] \u0430\u0440\u0445\u0438\u0432 \u043f\u043e\u0431\u0438\u043b\u0441\u044f: \u043e\u0436\u0438\u0434\u0430\u043b\u0438 sha256={}, \u043f\u043e\u043b\u0443\u0447\u0438\u043b\u0438 {}",
                        localValue3,
                        localValue10
                     );
                  internalMethod08874(
                     "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043a\u0430\u0447\u0430\u0442\u044c Python-\u0440\u0430\u043d\u0442\u0430\u0439\u043c: \u0430\u0440\u0445\u0438\u0432 \u043f\u043e\u0431\u0438\u043b\u0441\u044f. \u0421\u043a\u0440\u0438\u043f\u0442\u044b \u043f\u043e\u043a\u0430 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u044b."
                  );
                  return;
               }

               internalMethod04800(localValue9, localValue8);
               if (internalMethod07191(localValue8) == null) {
                  RockstarClient.internalField0572
                     .error(
                        "[Runtime] \u0432 \u0430\u0440\u0445\u0438\u0432\u0435 \u043d\u0435\u0442 jep \u2014 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0430 \u043e\u0442\u043c\u0435\u043d\u0435\u043d\u0430"
                     );
                  internalMethod08874(
                     "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u043e\u0441\u0442\u0430\u0432\u0438\u0442\u044c Python-\u0440\u0430\u043d\u0442\u0430\u0439\u043c: \u0430\u0440\u0445\u0438\u0432 \u043d\u0435\u043f\u043e\u043b\u043d\u044b\u0439. \u0421\u043a\u0440\u0438\u043f\u0442\u044b \u043f\u043e\u043a\u0430 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u044b."
                  );
                  return;
               }

               internalMethod00060(localValue8, localValue7);
               Files.writeString(localValue7.resolve(".rockstar-runtime"), localValue3, StandardCharsets.UTF_8);
               RockstarClient.internalField0572
                  .info(
                     "[Runtime] \u043f\u0438\u0442\u043e\u043d-\u0440\u0430\u043d\u0442\u0430\u0439\u043c \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d: {}",
                     localValue7
                  );
               if (EmbeddedPythonRuntime.internalMethod08721()) {
                  RockstarClient.internalField0572
                     .warn(
                        "[Runtime] \u043d\u0430\u0442\u0438\u0432\u043a\u0438 \u0441\u0442\u0430\u0440\u043e\u0433\u043e \u0440\u0430\u043d\u0442\u0430\u0439\u043c\u0430 \u0443\u0436\u0435 \u0432 \u043f\u0440\u043e\u0446\u0435\u0441\u0441\u0435 \u2014 \u043f\u0438\u0442\u043e\u043d \u043f\u043e\u0434\u043d\u0438\u043c\u0435\u0442\u0441\u044f \u0442\u043e\u043b\u044c\u043a\u043e \u043f\u043e\u0441\u043b\u0435 \u043f\u0435\u0440\u0435\u0437\u0430\u043f\u0443\u0441\u043a\u0430"
                     );
                  internalMethod08874(
                     "Python-\u0440\u0430\u043d\u0442\u0430\u0439\u043c \u043e\u0431\u043d\u043e\u0432\u043b\u0451\u043d. \u041f\u0435\u0440\u0435\u0437\u0430\u0439\u0434\u0438\u0442\u0435 \u0432 \u0438\u0433\u0440\u0443, \u0447\u0442\u043e\u0431\u044b \u0441\u043a\u0440\u0438\u043f\u0442\u044b \u0437\u0430\u0440\u0430\u0431\u043e\u0442\u0430\u043b\u0438."
                  );
               } else {
                  internalMethod08874(
                     "Python-\u0440\u0430\u043d\u0442\u0430\u0439\u043c \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u2014 \u0441\u043a\u0440\u0438\u043f\u0442\u044b \u0433\u043e\u0442\u043e\u0432\u044b \u043a \u0440\u0430\u0431\u043e\u0442\u0435."
                  );
                  MinecraftClientAccess.internalField0149.execute(PythonRuntimeInstaller::internalMethod02678);
               }
            } finally {
               Files.deleteIfExists(localValue9);
               internalMethod04628(localValue8);
            }
         }
      }
   }

   private static void internalMethod02678() {
      try {
         if (RockstarClient.getInstance().internalMethod04979() != null) {
            RockstarClient.getInstance().internalMethod04979().internalMethod07673();
         }
      } catch (Exception localValue1) {
         RockstarClient.internalField0572
            .error(
               "[Runtime] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u0441\u043a\u0440\u0438\u043f\u0442\u044b \u043f\u043e\u0441\u043b\u0435 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0438 \u0440\u0430\u043d\u0442\u0430\u0439\u043c\u0430",
               localValue1
            );
      }
   }

   private static JsonObject internalMethod01577(HttpClient localValue0, String localValue1) {
      String localValue2 = localValue1 + "/runtime/manifest?os=" + internalMethod01266();

      try {
         HttpRequest localValue3 = HttpRequest.newBuilder(URI.create(localValue2)).timeout(internalField0589).GET().build();
         HttpResponse localValue4 = localValue0.send(localValue3, BodyHandlers.ofString());
         if (localValue4.statusCode() != 200) {
            RockstarClient.internalField0572
               .warn(
                  "[Runtime] \u043c\u0430\u043d\u0438\u0444\u0435\u0441\u0442 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d (HTTP {}) \u2014 \u0441\u043a\u0440\u0438\u043f\u0442\u044b \u043e\u0441\u0442\u0430\u043d\u0443\u0442\u0441\u044f \u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d\u043d\u044b\u043c\u0438",
                  localValue4.statusCode()
               );
            return null;
         } else {
            JsonObject localValue5 = JsonParser.parseString((String)localValue4.body()).getAsJsonObject();
            if (!localValue5.has("sha256")) {
               RockstarClient.internalField0572
                  .warn(
                     "[Runtime] \u0432 \u043c\u0430\u043d\u0438\u0444\u0435\u0441\u0442\u0435 \u043d\u0435\u0442 sha256 \u2014 \u043f\u0440\u043e\u043f\u0443\u0441\u043a\u0430\u0435\u043c \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0443"
                  );
               return null;
            } else {
               return localValue5;
            }
         }
      } catch (Exception localValue6) {
         RockstarClient.internalField0572
            .warn(
               "[Runtime] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u043c\u0430\u043d\u0438\u0444\u0435\u0441\u0442 ({}): {}",
               localValue2,
               localValue6.getMessage()
            );
         return null;
      }
   }

   private static String internalMethod04549(HttpClient localValue0, String localValue1, Path localValue2, long localValue3) throws Exception {
      HttpRequest localValue5 = HttpRequest.newBuilder(URI.create(localValue1)).timeout(internalField1226).GET().build();
      HttpResponse localValue6 = localValue0.send(localValue5, BodyHandlers.ofInputStream());
      if (localValue6.statusCode() != 200) {
         throw new IOException("\u0441\u0435\u0440\u0432\u0435\u0440 \u043e\u0442\u0432\u0435\u0442\u0438\u043b HTTP " + localValue6.statusCode());
      } else {
         MessageDigest localValue7 = MessageDigest.getInstance("SHA-256");
         byte[] localValue8 = new byte[65536];
         long localValue9 = 0L;
         int localValue11 = 0;

         int localValue14;
         try (
            InputStream localValue12 = (InputStream)localValue6.body();
            OutputStream localValue13 = Files.newOutputStream(localValue2);
         ) {
            while ((localValue14 = localValue12.read(localValue8)) != -1) {
               localValue13.write(localValue8, 0, localValue14);
               localValue7.update(localValue8, 0, localValue14);
               localValue9 += localValue14;
               if (localValue3 > 0L) {
                  int localValue15 = (int)(localValue9 * 100L / localValue3);
                  if (localValue15 >= localValue11 + 25 && localValue15 < 100) {
                     localValue11 = localValue15;
                     RockstarClient.internalField0572.info("[Runtime] \u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430: {}%", localValue15);
                  }
               }
            }
         }

         return internalMethod01766(localValue7.digest());
      }
   }

   private static void internalMethod04800(Path localValue0, Path localValue1) throws IOException {
      String localValue2 = internalMethod07112(localValue0);

      ZipEntry localValue4;
      try (ZipInputStream localValue3 = new ZipInputStream(Files.newInputStream(localValue0))) {
         while ((localValue4 = localValue3.getNextEntry()) != null) {
            String localValue5 = localValue4.getName().replace('\\', '/');
            if (localValue5.startsWith(localValue2)) {
               String localValue6 = localValue5.substring(localValue2.length());
               if (!localValue6.isEmpty()) {
                  Path localValue7 = localValue1.resolve(localValue6).normalize();
                  if (!localValue7.startsWith(localValue1)) {
                     throw new IOException(
                        "\u0430\u0440\u0445\u0438\u0432 \u043f\u044b\u0442\u0430\u0435\u0442\u0441\u044f \u043f\u0438\u0441\u0430\u0442\u044c \u0437\u0430 \u043f\u0440\u0435\u0434\u0435\u043b\u044b \u043f\u0430\u043f\u043a\u0438: "
                           + localValue5
                     );
                  }

                  if (localValue4.isDirectory()) {
                     Files.createDirectories(localValue7);
                  } else {
                     Files.createDirectories(localValue7.getParent());
                     Files.copy(localValue3, localValue7, StandardCopyOption.REPLACE_EXISTING);
                  }
               }
            }
         }
      }
   }

   private static String internalMethod07112(Path localValue0) throws IOException {
      ZipEntry localValue2;
      try (ZipInputStream localValue1 = new ZipInputStream(Files.newInputStream(localValue0))) {
         while ((localValue2 = localValue1.getNextEntry()) != null) {
            String[] localValue3 = localValue2.getName().replace('\\', '/').split("/");

            for (int localValue4 = 0; localValue4 < localValue3.length; localValue4++) {
               if (localValue3[localValue4].equals("python")) {
                  StringBuilder localValue5 = new StringBuilder();

                  for (int localValue6 = 0; localValue6 <= localValue4; localValue6++) {
                     localValue5.append(localValue3[localValue6]).append('/');
                  }

                  return localValue5.toString();
               }
            }
         }
      }

      return "";
   }

   private static void internalMethod00060(Path localValue0, Path localValue1) throws IOException {
      Path localValue2 = localValue1.resolveSibling("python.old");
      internalMethod04628(localValue2);
      if (Files.exists(localValue1)) {
         Files.move(localValue1, localValue2, StandardCopyOption.REPLACE_EXISTING);
      }

      try {
         Files.createDirectories(localValue1.getParent());
         Files.move(localValue0, localValue1, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException localValue4) {
         if (Files.exists(localValue2)) {
            Files.move(localValue2, localValue1, StandardCopyOption.REPLACE_EXISTING);
         }

         throw localValue4;
      }

      internalMethod04628(localValue2);
   }

   private static String internalMethod04741() {
      Path localValue0 = EmbeddedPythonRuntime.internalMethod00088().toPath().resolve(".rockstar-runtime");

      try {
         return Files.exists(localValue0) ? Files.readString(localValue0, StandardCharsets.UTF_8).trim().toLowerCase(Locale.ROOT) : "";
      } catch (IOException localValue2) {
         return "";
      }
   }

   private static Path internalMethod07191(Path localValue0) {
      String localValue1 = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
      String localValue2 = localValue1.contains("win") ? "jep.dll" : (localValue1.contains("mac") ? "libjep.jnilib" : "libjep.so");
      Path localValue3 = localValue0.resolve(localValue2);
      if (Files.exists(localValue3)) {
         return localValue3;
      } else {
         Path localValue4 = localValue0.resolve("Lib/site-packages/jep/" + localValue2);
         return Files.exists(localValue4) ? localValue4 : null;
      }
   }

   private static String internalMethod01266() {
      String localValue0 = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
      if (localValue0.contains("mac")) {
         return "macos";
      } else {
         return !localValue0.contains("nux") && !localValue0.contains("nix") ? "windows" : "linux";
      }
   }

   private static void internalMethod04628(Path localValue0) throws IOException {
      if (Files.exists(localValue0)) {
         try (Stream<Path> localValue1 = Files.walk(localValue0)) {
            localValue1.sorted(Comparator.reverseOrder()).forEach(localValue0x -> {
               try {
                  Files.deleteIfExists(localValue0x);
               } catch (IOException localValue2) {
               }
            });
         }
      }
   }

   private static String internalMethod01766(byte[] localValue0) {
      StringBuilder localValue1 = new StringBuilder(localValue0.length * 2);

      for (byte localValue5 : localValue0) {
         localValue1.append(Character.forDigit(localValue5 >> 4 & 15, 16));
         localValue1.append(Character.forDigit(localValue5 & 15, 16));
      }

      return localValue1.toString();
   }

   private static void internalMethod08874(String localValue0) {
      MinecraftClientAccess.internalField0149.execute(() -> ClientMessages.internalMethod01809(Text.of(localValue0)));
   }
}

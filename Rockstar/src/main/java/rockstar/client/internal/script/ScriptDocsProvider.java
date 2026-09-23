package rockstar.client.internal.script;



import rockstar.client.internal.network.*;
import rockstar.client.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.MinecraftClient;

public final class ScriptDocsProvider {
   private static final String internalField0248 = "assets/rockstar/mcp/scripting/";
   private static final Map<String, String> internalField0543 = new LinkedHashMap<>();
   private static final Map<String, String> internalField0544 = new ConcurrentHashMap<>();

   private ScriptDocsProvider() {
   }

   public static String internalMethod06334(String localValue0, String localValue1) {
      String localValue2 = internalMethod03837(localValue0);
      if (!internalField0543.containsKey(localValue2)) {
         throw new McpHttpServer.InternalType0086(
            "\u043d\u0435\u0442 \u0440\u0430\u0437\u0434\u0435\u043b\u0430 \""
               + localValue0
               + "\"; \u0435\u0441\u0442\u044c: "
               + String.join(", ", internalField0543.keySet())
         );
      } else {
         Object localValue3 = internalField0544.computeIfAbsent(localValue2, ScriptDocsProvider::internalMethod04934);
         String localValue4 = "";
         if (localValue1 != null && !localValue1.isBlank()) {
            ScriptDocsProvider.InternalType0427 localValue5 = internalMethod06492((String)localValue3, localValue1.trim());
            localValue3 = localValue5.internalMethod00393();
            localValue4 = localValue5.internalMethod05085();
         }

         StringBuilder localValue6 = new StringBuilder();
         localValue6.append(
               "# \u0421\u043f\u0440\u0430\u0432\u043e\u0447\u043d\u0438\u043a \u043f\u043e \u0441\u043a\u0440\u0438\u043f\u0442\u0430\u043c Rockstar \u2014 \u0440\u0430\u0437\u0434\u0435\u043b \u00ab"
            )
            .append(localValue2)
            .append("\u00bb\n\n");
         localValue6.append(
            "\u0420\u0430\u0437\u0434\u0435\u043b\u044b \u044d\u0442\u043e\u0433\u043e \u0438\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u0430 (\u0437\u0430\u043f\u0440\u0430\u0448\u0438\u0432\u0430\u0439 \u043d\u0443\u0436\u043d\u044b\u0439 \u0442\u0435\u043c \u0436\u0435 script_api):\n"
         );
         internalField0543.forEach((localValue1x, localValue2x) -> localValue6.append("- `").append(localValue1x).append("` \u2014 ").append(localValue2x).append('\n'));
         localValue6.append(
            "\n\u0412\u0441\u0451, \u0447\u0435\u0433\u043e \u0432 \u0441\u043f\u0440\u0430\u0432\u043e\u0447\u043d\u0438\u043a\u0435 \u043d\u0435\u0442, \u0432 \u043a\u043b\u0438\u0435\u043d\u0442\u0435 \u043d\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442: \u043c\u0435\u0442\u043e\u0434\u044b \u0438 \u0441\u043e\u0431\u044b\u0442\u0438\u044f \u043d\u0435 \u0432\u044b\u0434\u0443\u043c\u044b\u0432\u0430\u0439, \u0430 \u0441\u0432\u0435\u0440\u044f\u0439 \u0441 \u043d\u0443\u0436\u043d\u044b\u043c \u0440\u0430\u0437\u0434\u0435\u043b\u043e\u043c.\n"
         );
         if (!localValue4.isEmpty()) {
            localValue6.append(localValue4).append('\n');
         }

         localValue6.append("\n---\n\n");
           return localValue6.toString() + localValue3;
      }
   }

   private static String internalMethod03837(String localValue0) {
      if (localValue0 != null && !localValue0.isBlank()) {
         String localValue1 = localValue0.trim().toLowerCase(Locale.ROOT).replace('\\', '/');
         int localValue2 = localValue1.lastIndexOf(47);
         if (localValue2 >= 0) {
            localValue1 = localValue1.substring(localValue2 + 1);
         }

         return localValue1.endsWith(".md") ? localValue1.substring(0, localValue1.length() - 3) : localValue1;
      } else {
         return "basics";
      }
   }

   private static boolean internalMethod04107(String localValue0) {
      String localValue1 = localValue0.replace("#", "").trim();
      return localValue1.isEmpty() || !Character.isDigit(localValue1.charAt(0));
   }

   private static ScriptDocsProvider.InternalType0427 internalMethod06492(String localValue0, String localValue1) {
      String localValue2 = localValue1.toLowerCase(Locale.ROOT);
      String[] localValue3 = localValue0.split("(?m)^(?=## )");
      if (localValue3.length < 2) {
         return new ScriptDocsProvider.InternalType0427(localValue0, "");
      } else {
         StringBuilder localValue4 = new StringBuilder(localValue3[0]);
         ArrayList localValue5 = new ArrayList();
         int localValue6 = 0;

         for (int localValue7 = 1; localValue7 < localValue3.length; localValue7++) {
            String localValue8 = localValue3[localValue7];
            String localValue9 = localValue8.substring(0, localValue8.indexOf(10) < 0 ? localValue8.length() : localValue8.indexOf(10)).trim();
            localValue5.add(localValue9);
            if (localValue8.toLowerCase(Locale.ROOT).contains(localValue2)) {
               localValue4.append(localValue8);
               localValue6++;
            } else if (internalMethod04107(localValue9)) {
               localValue4.append(localValue8);
            }
         }

         return localValue6 == 0
            ? new ScriptDocsProvider.InternalType0427(
               "\u041f\u043e \u0437\u0430\u043f\u0440\u043e\u0441\u0443 \u00ab"
                  + localValue1
                  + "\u00bb \u0432 \u044d\u0442\u043e\u043c \u0440\u0430\u0437\u0434\u0435\u043b\u0435 \u043d\u0438\u0447\u0435\u0433\u043e \u043d\u0435\u0442. \u0415\u0433\u043e \u043f\u043e\u0434\u0440\u0430\u0437\u0434\u0435\u043b\u044b:\n\n- "
                  + String.join("\n- ", localValue5)
                  + "\n\n\u0417\u0430\u043f\u0440\u043e\u0441\u0438 \u0440\u0430\u0437\u0434\u0435\u043b \u0431\u0435\u0437 query \u0438\u043b\u0438 \u043f\u043e\u0438\u0449\u0438 \u0432 \u0434\u0440\u0443\u0433\u043e\u043c.",
               ""
            )
            : new ScriptDocsProvider.InternalType0427(
               localValue4.toString(),
               "\n\u041f\u043e\u043a\u0430\u0437\u0430\u043d\u044b \u0442\u043e\u043b\u044c\u043a\u043e \u043f\u043e\u0434\u0440\u0430\u0437\u0434\u0435\u043b\u044b \u0441\u043e \u0441\u043b\u043e\u0432\u043e\u043c \u00ab"
                  + localValue1
                  + "\u00bb ("
                  + localValue6
                  + " \u0438\u0437 "
                  + (localValue3.length - 1)
                  + "). \u0411\u0435\u0437 query \u043f\u0440\u0438\u0434\u0451\u0442 \u0440\u0430\u0437\u0434\u0435\u043b \u0446\u0435\u043b\u0438\u043a\u043e\u043c."
            );
      }
   }

   private static String internalMethod04934(String localValue0) {
      try {
         Path localValue1 = MinecraftClient.getInstance()
            .runDirectory
            .toPath()
            .resolve("../src/main/resources/assets/rockstar/mcp/scripting/" + localValue0 + ".md")
            .normalize();
         if (Files.isRegularFile(localValue1)) {
            return Files.readString(localValue1, StandardCharsets.UTF_8);
         }
      } catch (Exception localValue5) {
         RockstarClient.internalField0572
            .warn(
               "[MCP] \u0441\u043f\u0440\u0430\u0432\u043e\u0447\u043d\u0438\u043a \u0438\u0437 \u0440\u0435\u043f\u043e\u0437\u0438\u0442\u043e\u0440\u0438\u044f \u043d\u0435 \u043f\u0440\u043e\u0447\u0438\u0442\u0430\u043d: {}",
               localValue5.getMessage()
            );
      }

      try {
         String localValue2;
         try (InputStream localValue8 = ScriptDocsProvider.class.getClassLoader().getResourceAsStream("assets/rockstar/mcp/scripting/" + localValue0 + ".md")) {
            if (localValue8 == null) {
               throw new IllegalStateException("\u0432 jar \u043d\u0435\u0442 " + localValue0 + ".md");
            }

            localValue2 = new String(localValue8.readAllBytes(), StandardCharsets.UTF_8);
         }

         return localValue2;
      } catch (Exception localValue7) {
         throw new McpHttpServer.InternalType0086(
            "\u0441\u043f\u0440\u0430\u0432\u043e\u0447\u043d\u0438\u043a \u043d\u0435 \u0447\u0438\u0442\u0430\u0435\u0442\u0441\u044f: " + localValue7.getMessage()
         );
      }
   }

   static {
      internalField0543.put(
         "basics",
         "\u0441 \u0447\u0435\u0433\u043e \u043d\u0430\u0447\u0438\u043d\u0430\u0442\u044c: \u0443\u0441\u0442\u0440\u043e\u0439\u0441\u0442\u0432\u043e \u0441\u043a\u0440\u0438\u043f\u0442\u0430, \u0441\u043a\u0435\u043b\u0435\u0442, \u043d\u0435\u0439\u043c\u0441\u043f\u0435\u0439\u0441, \u0433\u043b\u0430\u0432\u043d\u044b\u0435 \u0433\u0440\u0430\u0431\u043b\u0438, \u0447\u0435\u043a-\u043b\u0438\u0441\u0442"
      );
      internalField0543.put(
         "api",
         "\u043f\u043e\u043b\u043d\u044b\u0439 \u0441\u043f\u0440\u0430\u0432\u043e\u0447\u043d\u0438\u043a: \u043c\u043e\u0434\u0443\u043b\u0438, \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438, \u043c\u0438\u0440 \u0438 \u0438\u0433\u0440\u043e\u043a, Newton, \u043a\u043e\u043c\u0430\u043d\u0434\u044b, Aura, \u0440\u0438\u0441\u043e\u0432\u0430\u043d\u0438\u0435"
      );
      internalField0543.put(
         "events",
         "\u0432\u0441\u0435 \u0441\u043e\u0431\u044b\u0442\u0438\u044f: \u043e\u0442\u043c\u0435\u043d\u044f\u0435\u043c\u043e\u0441\u0442\u044c, \u043f\u043e\u043b\u044f \u043e\u0431\u044a\u0435\u043a\u0442\u0430, \u043f\u0440\u0438\u043c\u0435\u0440 \u043f\u043e\u0434\u043f\u0438\u0441\u043a\u0438"
      );
      internalField0543.put(
         "ui",
         "HUD, Dynamic Island, \u044d\u043a\u0440\u0430\u043d\u044b, ui-\u0431\u0438\u043b\u0434\u0435\u0440, 2D-\u0440\u0438\u0441\u043e\u0432\u0430\u043d\u0438\u0435"
      );
      internalField0543.put(
         "recipes",
         "\u0433\u043e\u0442\u043e\u0432\u044b\u0435 \u0441\u043a\u0440\u0438\u043f\u0442\u044b \u0446\u0435\u043b\u0438\u043a\u043e\u043c: HUD, ESP, \u043c\u0435\u043d\u044e, \u0444\u043e\u043d\u043e\u0432\u044b\u0439 \u043f\u043e\u0442\u043e\u043a, storage, \u0441\u0432\u043e\u044f \u0440\u043e\u0442\u0430\u0446\u0438\u044f"
      );
   }

   static final class InternalType0427 {
      private final String internalField0248;
      private final String internalField0247;

      InternalType0427(String localValue1, String localValue2) {
         this.internalField0248 = localValue1;
         this.internalField0247 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0427[text=" + this.internalField0248 + ", note=" + this.internalField0247 + "]";
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
         ScriptDocsProvider.InternalType0427 other = (ScriptDocsProvider.InternalType0427) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0247, other.internalField0247);
      }

      public String internalMethod00393() {
         return this.internalField0248;
      }

      public String internalMethod05085() {
         return this.internalField0247;
      }
   }
}

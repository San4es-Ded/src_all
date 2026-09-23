package rockstar.client.internal.network;



import rockstar.client.internal.script.*;
import rockstar.client.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public final class McpHttpServer {
   public static final String internalField0248 = "2025-06-18";
   private static final Set<String> internalField0546 = Set.of("2025-06-18", "2025-03-26", "2024-11-05");
   public static final int internalField0227 = 8420;
   private static final Gson internalField0931 = new GsonBuilder().disableHtmlEscaping().create();
   private static McpHttpServer internalField0401;
   private ServerSocket internalField0946;
   private ExecutorService internalField0124;
   private volatile boolean internalField0277;
   private volatile int internalField1055;
   private volatile String internalField0247;

   private McpHttpServer() {
   }

   public static McpHttpServer internalMethod07531() {
      if (internalField0401 == null) {
         internalField0401 = new McpHttpServer();
      }

      return internalField0401;
   }

   public boolean internalMethod01545() {
      return this.internalField0277;
   }

   public int internalMethod01543() {
      return this.internalField1055;
   }

   public String internalMethod02728() {
      return this.internalField0247;
   }

   public String internalMethod07430() {
      return "http://127.0.0.1:" + this.internalField1055 + "/mcp";
   }

   public static File internalMethod03768() {
      return new File(ConfigManager.internalField0148, "mcp.json");
   }

   public void internalMethod01544() {
      Thread localValue1 = new Thread(() -> {
         try {
            this.internalMethod01549();
         } catch (Exception localValue2) {
            RockstarClient.internalField0572.error("[MCP] \u0441\u0435\u0440\u0432\u0435\u0440 \u043d\u0435 \u043f\u043e\u0434\u043d\u044f\u043b\u0441\u044f", localValue2);
         }
      }, "Rockstar-MCP-Boot");
      localValue1.setDaemon(true);
      localValue1.start();
   }

   public synchronized void internalMethod01549() throws IOException {
      if (!this.internalField0277) {
         this.internalField0247 = this.internalMethod09049();
         IOException localValue1 = null;

         for (int localValue2 = 0; localValue2 < 10; localValue2++) {
            try {
               this.internalField0946 = new ServerSocket(8420 + localValue2, 16, InetAddress.getLoopbackAddress());
               this.internalField1055 = 8420 + localValue2;
               localValue1 = null;
               break;
            } catch (IOException localValue4) {
               localValue1 = localValue4;
            }
         }

         if (this.internalField0946 == null) {
            throw localValue1 == null
               ? new IOException("\u043d\u0435\u0442 \u0441\u0432\u043e\u0431\u043e\u0434\u043d\u043e\u0433\u043e \u043f\u043e\u0440\u0442\u0430")
               : localValue1;
         } else {
            this.internalField0277 = true;
            this.internalField0124 = Executors.newCachedThreadPool(localValue0 -> {
               Thread localValue1x = new Thread(localValue0, "Rockstar-MCP");
               localValue1x.setDaemon(true);
               return localValue1x;
            });
            Thread localValue5 = new Thread(this::internalMethod08328, "Rockstar-MCP-Accept");
            localValue5.setDaemon(true);
            localValue5.start();
            this.internalMethod08332();
            RockstarClient.internalField0572
               .info(
                  "[MCP] \u0441\u043b\u0443\u0448\u0430\u044e {} (\u0442\u043e\u043a\u0435\u043d \u0432 {})",
                  this.internalMethod07430(),
                  internalMethod03768().getAbsolutePath()
               );
         }
      }
   }

   public synchronized void internalMethod08318() {
      this.internalField0277 = false;

      try {
         if (this.internalField0946 != null) {
            this.internalField0946.close();
         }
      } catch (IOException localValue2) {
      }

      this.internalField0946 = null;
      if (this.internalField0124 != null) {
         this.internalField0124.shutdownNow();
         this.internalField0124 = null;
      }

      this.internalField1055 = 0;
   }

   public synchronized void internalMethod08320() throws IOException {
      this.internalMethod08318();
      this.internalMethod01549();
   }

   public synchronized String internalMethod08162() {
      this.internalField0247 = HexFormat.of().formatHex(internalMethod03209());
      this.internalMethod08332();
      return this.internalField0247;
   }

   private void internalMethod08328() {
      while (this.internalField0277) {
         try {
            Socket localValue1 = this.internalField0946.accept();
            if (!localValue1.getInetAddress().isLoopbackAddress()) {
               localValue1.close();
            } else {
               this.internalField0124.submit(() -> this.internalMethod05891(localValue1));
            }
         } catch (IOException localValue2) {
            if (this.internalField0277) {
               RockstarClient.internalField0572.warn("[MCP] accept: {}", localValue2.toString());
            }
         }
      }
   }

   private void internalMethod05891(Socket localValue1) {
      try {
         Socket localValue2 = localValue1;

         label61: {
            try {
               localValue1.setSoTimeout(300000);
               localValue1.setTcpNoDelay(true);
               BufferedInputStream localValue3 = new BufferedInputStream(localValue1.getInputStream());
               BufferedOutputStream localValue4 = new BufferedOutputStream(localValue1.getOutputStream());

               while (this.internalField0277) {
                  McpHttpServer.InternalType0087 localValue5 = this.internalMethod07406(localValue3);
                  if (localValue5 == null || !this.internalMethod04207(localValue5, localValue4)) {
                     break label61;
                  }
               }
            } catch (Throwable localValue7) {
               if (localValue1 != null) {
                  try {
                     localValue2.close();
                  } catch (Throwable localValue6) {
                     localValue7.addSuppressed(localValue6);
                  }
               }

               throw localValue7;
            }

            if (localValue1 != null) {
               localValue1.close();
            }

            return;
         }

         if (localValue1 != null) {
            localValue1.close();
         }

         return;
      } catch (IOException localValue8) {
      } catch (Exception localValue9) {
         RockstarClient.internalField0572.error("[MCP] \u0441\u043e\u0435\u0434\u0438\u043d\u0435\u043d\u0438\u0435 \u0443\u043f\u0430\u043b\u043e", localValue9);
      }
   }

   private boolean internalMethod04207(McpHttpServer.InternalType0087 localValue1, OutputStream localValue2) throws IOException {
      boolean localValue3 = !"close".equalsIgnoreCase(localValue1.internalMethod05150().get("connection"));
      String localValue4 = localValue1.internalMethod04958();
      int localValue5 = localValue4.indexOf(63);
      String localValue6 = localValue5 < 0 ? "" : localValue4.substring(localValue5 + 1);
      if (localValue5 >= 0) {
         localValue4 = localValue4.substring(0, localValue5);
      }

      if ("OPTIONS".equals(localValue1.internalMethod00195())) {
         this.internalMethod03039(localValue2, 204, "text/plain", new byte[0], localValue3);
         return localValue3;
      } else if (!"/health".equals(localValue4)) {
         if (!"/mcp".equals(localValue4) && !"/".equals(localValue4)) {
            this.internalMethod03039(localValue2, 404, "text/plain", "not found".getBytes(StandardCharsets.UTF_8), localValue3);
            return localValue3;
         } else if (!this.internalMethod02529(localValue1, localValue6)) {
            this.internalMethod03039(localValue2, 401, "text/plain", "unauthorized".getBytes(StandardCharsets.UTF_8), false);
            return false;
         } else if (!"POST".equals(localValue1.internalMethod00195())) {
            this.internalMethod03039(localValue2, 405, "text/plain", "use POST".getBytes(StandardCharsets.UTF_8), localValue3);
            return localValue3;
         } else {
            JsonElement localValue10;
            try {
               localValue10 = this.internalMethod07629(JsonParser.parseString(new String(localValue1.internalMethod02781(), StandardCharsets.UTF_8)));
            } catch (Exception localValue9) {
               localValue10 = this.internalMethod03889(null, -32700, "\u043d\u0435 \u0440\u0430\u0437\u043e\u0431\u0440\u0430\u043b JSON: " + localValue9.getMessage());
            }

            if (localValue10 == null) {
               this.internalMethod03039(localValue2, 202, "text/plain", new byte[0], localValue3);
               return localValue3;
            } else {
               this.internalMethod02662(localValue2, 200, localValue10, localValue3);
               return localValue3;
            }
         }
      } else {
         JsonObject localValue7 = new JsonObject();
         localValue7.addProperty("ok", true);
         localValue7.addProperty("name", "Rockstar");
         localValue7.addProperty("version", "2.1");
         localValue7.addProperty("protocol", "2025-06-18");
         localValue7.addProperty("port", this.internalField1055);
         localValue7.addProperty("inGame", MinecraftClientAccess.internalField0149.player != null && MinecraftClientAccess.internalField0149.world != null);
         this.internalMethod02662(localValue2, 200, localValue7, localValue3);
         return localValue3;
      }
   }

   private boolean internalMethod02529(McpHttpServer.InternalType0087 localValue1, String localValue2) {
      String localValue3 = this.internalField0247;
      if (localValue3 != null && !localValue3.isBlank()) {
         String localValue4 = localValue1.internalMethod05150().get("authorization");
         if (localValue4 != null) {
            String localValue5 = localValue4.trim();
            if (localValue5.regionMatches(true, 0, "bearer ", 0, 7)) {
               localValue5 = localValue5.substring(7).trim();
            }

            if (localValue3.equals(localValue5)) {
               return true;
            }
         }

         for (String localValue8 : localValue2.split("&")) {
            int localValue9 = localValue8.indexOf(61);
            if (localValue9 > 0 && "token".equals(localValue8.substring(0, localValue9)) && localValue3.equals(localValue8.substring(localValue9 + 1))) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private JsonElement internalMethod07629(JsonElement localValue1) {
      if (localValue1 != null && localValue1.isJsonArray()) {
         JsonArray localValue2 = new JsonArray();

         for (JsonElement localValue4 : localValue1.getAsJsonArray()) {
            JsonElement localValue5 = this.internalMethod02029(localValue4);
            if (localValue5 != null) {
               localValue2.add(localValue5);
            }
         }

         return localValue2.isEmpty() ? null : localValue2;
      } else {
         return this.internalMethod02029(localValue1);
      }
   }

   private JsonElement internalMethod02029(JsonElement localValue1) {
      if (localValue1 != null && localValue1.isJsonObject()) {
         JsonObject localValue2 = localValue1.getAsJsonObject();
         JsonElement localValue3 = localValue2.get("id");
         String localValue4 = localValue2.has("method") ? localValue2.get("method").getAsString() : "";
         JsonObject localValue5 = localValue2.has("params") && localValue2.get("params").isJsonObject() ? localValue2.getAsJsonObject("params") : new JsonObject();

         try {
            JsonObject localValue6 = this.internalMethod06167(localValue4, localValue5);
            if (localValue3 != null && !localValue3.isJsonNull()) {
               JsonObject localValue7 = new JsonObject();
               localValue7.addProperty("jsonrpc", "2.0");
               localValue7.add("id", localValue3);
               localValue7.add("result", localValue6 == null ? new JsonObject() : localValue6);
               return localValue7;
            } else {
               return null;
            }
         } catch (McpHttpServer.InternalType0086 localValue8) {
            return this.internalMethod03889(localValue3, localValue8.internalMethod00552(), localValue8.getMessage());
         } catch (Exception localValue9) {
            RockstarClient.internalField0572.error("[MCP] {} \u0443\u043f\u0430\u043b", localValue4, localValue9);
            return this.internalMethod03889(localValue3, -32603, String.valueOf(localValue9));
         }
      } else {
         return this.internalMethod03889(null, -32600, "\u043e\u0436\u0438\u0434\u0430\u043b\u0441\u044f \u043e\u0431\u044a\u0435\u043a\u0442 JSON-RPC");
      }
   }

   private JsonObject internalMethod06167(String localValue1, JsonObject localValue2) {
      switch (localValue1) {
         case "initialize":
            String localValue12 = localValue2.has("protocolVersion") ? localValue2.get("protocolVersion").getAsString() : "2025-06-18";
            JsonObject localValue13 = new JsonObject();
            JsonObject localValue7 = new JsonObject();
            localValue7.addProperty("listChanged", false);
            localValue13.add("tools", localValue7);
            JsonObject localValue8 = new JsonObject();
            localValue8.addProperty("name", "rockstar");
            localValue8.addProperty("title", "Rockstar 2.1");
            localValue8.addProperty("version", "2.1");
            JsonObject localValue9 = new JsonObject();
            localValue9.addProperty("protocolVersion", internalField0546.contains(localValue12) ? localValue12 : "2025-06-18");
            localValue9.add("capabilities", localValue13);
            localValue9.add("serverInfo", localValue8);
            localValue9.addProperty(
               "instructions",
               "Rockstar \u2014 \u0447\u0438\u0442-\u043a\u043b\u0438\u0435\u043d\u0442 Minecraft. \u0427\u0435\u0440\u0435\u0437 \u044d\u0442\u0438 \u0438\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u044b \u0432\u0438\u0434\u043d\u043e \u0441\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u0435 \u0438\u0433\u0440\u044b \u0438 \u043a\u043b\u0438\u0435\u043d\u0442\u0430 \u0438 \u043c\u043e\u0436\u043d\u043e \u0438\u043c \u0443\u043f\u0440\u0430\u0432\u043b\u044f\u0442\u044c.\n\u041f\u043e\u0440\u044f\u0434\u043e\u043a \u0440\u0430\u0431\u043e\u0442\u044b: game_state \u2014 \u0447\u0442\u043e \u043f\u0440\u043e\u0438\u0441\u0445\u043e\u0434\u0438\u0442; screenshot \u2014 \u043a\u0430\u043a \u044d\u0442\u043e \u0432\u044b\u0433\u043b\u044f\u0434\u0438\u0442; mouse/keyboard \u2014 \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u044f \u043f\u043e \u043a\u0430\u0440\u0442\u0438\u043d\u043a\u0435.\n\u041a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b \u0434\u043b\u044f mouse \u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e \u0441\u0447\u0438\u0442\u0430\u044e\u0442\u0441\u044f \u0432 \u043f\u0438\u043a\u0441\u0435\u043b\u044f\u0445 \u043f\u043e\u0441\u043b\u0435\u0434\u043d\u0435\u0433\u043e \u0441\u043a\u0440\u0438\u043d\u0448\u043e\u0442\u0430, \u043f\u043e\u044d\u0442\u043e\u043c\u0443 \u043a\u043b\u0438\u043a\u0430\u0442\u044c \u043d\u0443\u0436\u043d\u043e \u043f\u043e \u0442\u043e\u043c\u0443, \u0447\u0442\u043e \u0432\u0438\u0434\u043d\u043e \u043d\u0430 \u043d\u0451\u043c (space \u043c\u0435\u043d\u044f\u0435\u0442 \u0441\u0438\u0441\u0442\u0435\u043c\u0443 \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442).\n\u041c\u043e\u0434\u0443\u043b\u0438 \u043c\u0435\u043d\u044f\u044e\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 module_control \u0438 setting_set: \u0438\u043c\u0435\u043d\u0430 \u043d\u0430\u0441\u0442\u0440\u043e\u0435\u043a \u0431\u0435\u0440\u0443\u0442\u0441\u044f \u0438\u0437 module_info.\n\u0421\u043a\u0440\u0438\u043f\u0442\u044b \u043a\u043b\u0438\u0435\u043d\u0442\u0430 \u2014 Python \u043f\u043e \u043d\u0430\u0448\u0435\u043c\u0443 API, \u0438 \u043f\u043e \u043f\u0430\u043c\u044f\u0442\u0438 \u043e\u043d \u043d\u0435 \u043f\u0438\u0448\u0435\u0442\u0441\u044f: \u043f\u0435\u0440\u0435\u0434 scripts write \u043e\u0431\u044f\u0437\u0430\u0442\u0435\u043b\u044c\u043d\u043e \u043f\u0440\u043e\u0447\u0438\u0442\u0430\u0439 script_api (\u0440\u0430\u0437\u0434\u0435\u043b basics, \u0434\u0430\u043b\u044c\u0448\u0435 \u043f\u043e \u0435\u0433\u043e \u043a\u0430\u0440\u0442\u0435 \u0440\u0430\u0437\u0434\u0435\u043b\u043e\u0432)."
            );
            return localValue9;
         case "ping":
            return new JsonObject();
         case "tools/list":
            return McpToolRegistry.internalMethod02233();
         case "tools/call":
            String localValue11 = localValue2.has("name") ? localValue2.get("name").getAsString() : "";
            JsonObject localValue6 = localValue2.has("arguments") && localValue2.get("arguments").isJsonObject() ? localValue2.getAsJsonObject("arguments") : new JsonObject();
            return McpToolRegistry.internalMethod04076(localValue11, localValue6);
         case "resources/list":
         case "resources/templates/list":
            JsonObject localValue10 = new JsonObject();
            localValue10.add("resources", new JsonArray());
            return localValue10;
         case "prompts/list":
            JsonObject localValue5 = new JsonObject();
            localValue5.add("prompts", new JsonArray());
            return localValue5;
         default:
            if (localValue1.startsWith("notifications/")) {
               return new JsonObject();
            } else {
               throw new McpHttpServer.InternalType0086(
                  -32601, "\u043d\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043d\u044b\u0439 \u043c\u0435\u0442\u043e\u0434: " + localValue1
               );
            }
      }
   }

   private JsonElement internalMethod03889(JsonElement localValue1, int localValue2, String localValue3) {
      JsonObject localValue4 = new JsonObject();
      localValue4.addProperty("code", localValue2);
      localValue4.addProperty("message", localValue3 == null ? "\u043e\u0448\u0438\u0431\u043a\u0430" : localValue3);
      JsonObject localValue5 = new JsonObject();
      localValue5.addProperty("jsonrpc", "2.0");
      localValue5.add("id", (JsonElement)(localValue1 == null ? JsonNull.INSTANCE : localValue1));
      localValue5.add("error", localValue4);
      return localValue5;
   }

   public static <T> T internalMethod06265(Supplier<T> localValue0) {
      if (MinecraftClientAccess.internalField0149.isOnThread()) {
         return (T)localValue0.get();
      } else {
         CompletableFuture localValue1 = new CompletableFuture();
         MinecraftClientAccess.internalField0149.execute(() -> {
            try {
               localValue1.complete(localValue0.get());
            } catch (Throwable localValue3x) {
               localValue1.completeExceptionally(localValue3x);
            }
         });

         try {
            return (T)localValue1.get(10000L, TimeUnit.MILLISECONDS);
         } catch (TimeoutException localValue5) {
            throw new McpHttpServer.InternalType0086(
               -32603,
               "\u0438\u0433\u0440\u0430 \u043d\u0435 \u043e\u0442\u0432\u0435\u0442\u0438\u043b\u0430 \u0437\u0430 10 \u0441 \u2014 \u043e\u043d\u0430 \u0433\u0440\u0443\u0437\u0438\u0442\u0441\u044f \u0438\u043b\u0438 \u0432\u0438\u0441\u0438\u0442"
            );
         } catch (Exception localValue6) {
            Object localValue3 = localValue6.getCause() == null ? localValue6 : localValue6.getCause();
            if (localValue3 instanceof McpHttpServer.InternalType0086 localValue4) {
               throw localValue4;
            } else {
               throw new McpHttpServer.InternalType0086(-32603, String.valueOf(localValue3));
            }
         }
      }
   }

   private String internalMethod09049() {
      File localValue1 = internalMethod03768();
      if (localValue1.isFile()) {
         try {
            JsonElement localValue2 = JsonParser.parseString(Files.readString(localValue1.toPath()));
            if (localValue2.isJsonObject()) {
               JsonObject localValue3 = localValue2.getAsJsonObject();
               if (localValue3.has("token") && !localValue3.get("token").getAsString().isBlank()) {
                  return localValue3.get("token").getAsString();
               }
            }
         } catch (Exception localValue4) {
         }
      }

      return HexFormat.of().formatHex(internalMethod03209());
   }

   private static byte[] internalMethod03209() {
      byte[] localValue0 = new byte[24];
      new SecureRandom().nextBytes(localValue0);
      return localValue0;
   }

   private void internalMethod08332() {
      JsonObject localValue1 = new JsonObject();
      localValue1.addProperty("url", this.internalMethod07430());
      localValue1.addProperty("port", this.internalField1055);
      localValue1.addProperty("token", this.internalField0247);
      localValue1.addProperty("protocol", "2025-06-18");
      localValue1.addProperty("client", "Rockstar 2.1");

      try {
         ConfigManager.internalMethod01467(internalMethod03768(), localValue1);
      } catch (IOException localValue3) {
         RockstarClient.internalField0572.warn("[MCP] \u043d\u0435 \u0437\u0430\u043f\u0438\u0441\u0430\u043b {}: {}", internalMethod03768(), localValue3.toString());
      }
   }

   private McpHttpServer.InternalType0087 internalMethod07406(InputStream localValue1) throws IOException {
      String localValue2 = internalMethod06161(localValue1);
      if (localValue2 != null && !localValue2.isEmpty()) {
         String[] localValue3 = localValue2.split(" ");
         if (localValue3.length < 2) {
            return null;
         } else {
            HashMap localValue4 = new HashMap();

            String localValue5;
            while ((localValue5 = internalMethod06161(localValue1)) != null && !localValue5.isEmpty()) {
               int localValue6 = localValue5.indexOf(58);
               if (localValue6 > 0) {
                  localValue4.put(localValue5.substring(0, localValue6).trim().toLowerCase(Locale.ROOT), localValue5.substring(localValue6 + 1).trim());
               }
            }

            int localValue9 = 0;

            try {
                 localValue9 = Integer.parseInt((String)localValue4.getOrDefault("content-length", "0"));
            } catch (NumberFormatException localValue8) {
            }

            if (localValue9 >= 0 && localValue9 <= 8388608) {
               byte[] localValue7 = localValue1.readNBytes(localValue9);
               return localValue7.length < localValue9 ? null : new McpHttpServer.InternalType0087(localValue3[0], localValue3[1], localValue4, localValue7);
            } else {
               throw new IOException(
                  "\u0442\u0435\u043b\u043e \u0437\u0430\u043f\u0440\u043e\u0441\u0430 "
                     + localValue9
                     + " \u0431\u0430\u0439\u0442 \u2014 \u0441\u043b\u0438\u0448\u043a\u043e\u043c \u0431\u043e\u043b\u044c\u0448\u043e\u0435"
               );
            }
         }
      } else {
         return null;
      }
   }

   private static String internalMethod06161(InputStream localValue0) throws IOException {
      ByteArrayOutputStream localValue1 = new ByteArrayOutputStream(128);

      int localValue2;
      while ((localValue2 = localValue0.read()) != -1) {
         if (localValue2 == 10) {
            String localValue3 = localValue1.toString(StandardCharsets.UTF_8);
            return localValue3.endsWith("\r") ? localValue3.substring(0, localValue3.length() - 1) : localValue3;
         }

         localValue1.write(localValue2);
         if (localValue1.size() > 8192) {
            throw new IOException(
               "\u0441\u043b\u0438\u0448\u043a\u043e\u043c \u0434\u043b\u0438\u043d\u043d\u044b\u0439 \u0437\u0430\u0433\u043e\u043b\u043e\u0432\u043e\u043a"
            );
         }
      }

      return localValue1.size() == 0 ? null : localValue1.toString(StandardCharsets.UTF_8);
   }

   private void internalMethod02662(OutputStream localValue1, int localValue2, JsonElement localValue3, boolean localValue4) throws IOException {
      this.internalMethod03039(localValue1, localValue2, "application/json; charset=utf-8", internalField0931.toJson(localValue3).getBytes(StandardCharsets.UTF_8), localValue4);
   }

   private void internalMethod03039(OutputStream localValue1, int localValue2, String localValue3, byte[] localValue4, boolean localValue5) throws IOException {
      String localValue6 = "HTTP/1.1 "
         + localValue2
         + " "
         + internalMethod04301(localValue2)
         + "\r\nContent-Type: "
         + localValue3
         + "\r\nContent-Length: "
         + localValue4.length
         + "\r\nConnection: "
         + (localValue5 ? "keep-alive" : "close")
         + "\r\n\r\n";
      localValue1.write(localValue6.getBytes(StandardCharsets.UTF_8));
      localValue1.write(localValue4);
      localValue1.flush();
   }

   private static String internalMethod04301(int localValue0) {
      return switch (localValue0) {
         case 202 -> "Accepted";
         case 204 -> "No Content";
         case 401 -> "Unauthorized";
         case 404 -> "Not Found";
         case 405 -> "Method Not Allowed";
         default -> "OK";
      };
   }

   public static final class InternalType0086 extends RuntimeException {
      private final int internalField0227;

      public InternalType0086(String localValue1) {
         this(-32602, localValue1);
      }

      public InternalType0086(int localValue1, String localValue2) {
         super(localValue2);
         this.internalField0227 = localValue1;
      }

      public int internalMethod00552() {
         return this.internalField0227;
      }
   }

   static final class InternalType0087 {
      private final String internalField0248;
      private final String internalField0247;
      private final Map<String, String> internalField0543;
      private final byte[] internalField0609;

      InternalType0087(String localValue1, String localValue2, Map<String, String> localValue3, byte[] localValue4) {
         this.internalField0248 = localValue1;
         this.internalField0247 = localValue2;
         this.internalField0543 = localValue3;
         this.internalField0609 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0087[method=" + this.internalField0248 + ", path=" + this.internalField0247 + ", headers=" + this.internalField0543 + ", body=" + this.internalField0609 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0247);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0543);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0609);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         McpHttpServer.InternalType0087 other = (McpHttpServer.InternalType0087) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0247, other.internalField0247)
            && java.util.Objects.equals(this.internalField0543, other.internalField0543)
            && java.util.Objects.equals(this.internalField0609, other.internalField0609);
      }

      public String internalMethod00195() {
         return this.internalField0248;
      }

      public String internalMethod04958() {
         return this.internalField0247;
      }

      public Map<String, String> internalMethod05150() {
         return this.internalField0543;
      }

      public byte[] internalMethod02781() {
         return this.internalField0609;
      }
   }
}

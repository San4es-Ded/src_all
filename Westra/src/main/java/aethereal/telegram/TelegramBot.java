package aethereal.telegram;

import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.Westra;
import aethereal.module.misc.AutoBuy;
import aethereal.util.ChatUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.class_318;

public final class TelegramBot {
   private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
   private static final Map<String, TelegramBot.Bot> BOTS = new LinkedHashMap<>();
   private static final HttpClient HTTP = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build();
   private static String active;
   private static Thread poller;
   private static final AtomicBoolean running = new AtomicBoolean(false);
   private static long updateOffset;
   private static boolean loaded;

   private TelegramBot() {
   }

   public static boolean hasBot(String name) {
      load();
      return BOTS.containsKey(name);
   }

   public static void addBot(String name, String token, String chatId) {
      load();
      BOTS.put(name, new TelegramBot.Bot(token, chatId));
      save();
   }

   public static void delBot(String name) {
      load();
      BOTS.remove(name);
      if (name.equals(active)) {
         stop();
         active = null;
      }

      save();
   }

   public static List<String> listBots() {
      load();
      return new ArrayList<>(BOTS.keySet());
   }

   public static String activeName() {
      return active;
   }

   public static void startBot(String name) {
      load();
      if (BOTS.containsKey(name)) {
         stop();
         active = name;
         updateOffset = 0L;
         running.set(true);
         poller = new Thread(TelegramBot::poll, "Westra Telegram");
         poller.setDaemon(true);
         poller.start();
         save();
         send("Westra подключён. Команды: /warden, /autobuy, /module list, /screenshot");
      }
   }

   public static void stop() {
      running.set(false);
      if (poller != null) {
         poller.interrupt();
         poller = null;
      }
   }

   public static void notifyAutoBuy(String text) {
      send(text);
   }

   public static void send(String text) {
      TelegramBot.Bot bot = current();
      if (bot != null) {
         new Thread(() -> {
            try {
               JsonObject body = new JsonObject();
               body.addProperty("chat_id", bot.chatId);
               body.addProperty("text", text);
               post(bot, "sendMessage", body);
            } catch (Exception var3) {
            }
         }, "Westra Telegram Send").start();
      }
   }

   private static TelegramBot.Bot current() {
      load();
      return active == null ? null : BOTS.get(active);
   }

   private static void poll() {
      while (running.get()) {
         TelegramBot.Bot bot = current();
         if (bot == null) {
            return;
         }

         try {
            String url = api(bot, "getUpdates") + "?timeout=25&offset=" + updateOffset;
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).timeout(Duration.ofSeconds(35L)).GET().build();
            HttpResponse<String> response = HTTP.send(request, BodyHandlers.ofString());
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            if (!json.get("ok").getAsBoolean()) {
               Thread.sleep(5000L);
            } else {
               for (JsonElement element : json.getAsJsonArray("result")) {
                  JsonObject update = element.getAsJsonObject();
                  updateOffset = update.get("update_id").getAsLong() + 1L;
                  if (update.has("message")) {
                     JsonObject message = update.getAsJsonObject("message");
                     if (message.has("text")) {
                        String chat = message.getAsJsonObject("chat").get("id").getAsString();
                        if (chat.equals(bot.chatId)) {
                           handle(message.get("text").getAsString().trim());
                        }
                     }
                  }
               }
            }
         } catch (InterruptedException var12) {
            return;
         } catch (Exception var13) {
            try {
               Thread.sleep(5000L);
            } catch (InterruptedException var11) {
               return;
            }
         }
      }
   }

   private static void handle(String text) {
      String command = text.toLowerCase();
      if (command.startsWith("/warden")) {
         send(wardenReport());
      } else if (command.startsWith("/autobuy")) {
         send(autoBuyReport());
      } else if (command.startsWith("/module")) {
         send(moduleReport());
      } else if (command.startsWith("/screenshot")) {
         screenshot();
      } else if (command.startsWith("/start") || command.startsWith("/help")) {
         send("Команды: /warden, /autobuy, /module list, /screenshot");
      }
   }

   private static String wardenReport() {
      if (Interface.aM_.field_1724 == null) {
         return "Игрок не в мире.";
      } else {
         Module warden = Westra.h().d().t().aU();
         StringBuilder report = new StringBuilder("Страж: ");
         report.append(warden != null && warden.m() ? "включён" : "выключен");
         report.append("\nНик: ").append(Interface.aM_.method_1548().method_1676());
         report.append("\nКоординаты: ")
            .append((int)Interface.aM_.field_1724.method_23317())
            .append(' ')
            .append((int)Interface.aM_.field_1724.method_23318())
            .append(' ')
            .append((int)Interface.aM_.field_1724.method_23321());
         report.append("\nЗдоровье: ").append((int)Interface.aM_.field_1724.method_6032());
         return report.toString();
      }
   }

   private static String autoBuyReport() {
      AutoBuy autoBuy = Westra.h().d().t().ba();
      return autoBuy == null
         ? "Автобай не найден."
         : "Автобай: " + (autoBuy.m() ? "включён" : "выключен") + "\nПоиск: " + (autoBuy.q() ? "да" : "нет") + "\nПарсер: " + (autoBuy.s() ? "да" : "нет");
   }

   private static String moduleReport() {
      StringBuilder report = new StringBuilder("Включённые функции:");
      int count = 0;

      for (Module module : Westra.h().d().t().e()) {
         if (module.m()) {
            report.append("\n- ").append(module.j());
            count++;
         }
      }

      return count == 0 ? "Ничего не включено." : report.toString();
   }

   private static void screenshot() {
      TelegramBot.Bot bot = current();
      if (bot != null && Interface.aM_.method_1522() != null) {
         Interface.aM_.execute(() -> {
            try {
               File directory = new File(Interface.aM_.field_1697, "screenshots");
               class_318.method_1659(Interface.aM_.field_1697, Interface.aM_.method_1522(), message -> {});
               File[] shots = directory.listFiles((dir, name) -> name.endsWith(".png"));
               if (shots == null || shots.length == 0) {
                  return;
               }

               File latest = shots[0];

               for (File shot : shots) {
                  if (shot.lastModified() > latest.lastModified()) {
                     latest = shot;
                  }
               }

               File file = latest;
               new Thread(() -> sendPhoto(bot, file), "Westra Telegram Photo").start();
            } catch (Exception var8) {
               send("Скриншот не получился: " + var8.getMessage());
            }
         });
      }
   }

   private static void sendPhoto(TelegramBot.Bot bot, File file) {
      String boundary = "westra" + System.nanoTime();

      try {
         HttpURLConnection connection = (HttpURLConnection)new URL(api(bot, "sendPhoto")).openConnection();
         connection.setDoOutput(true);
         connection.setRequestMethod("POST");
         connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

         try (OutputStream output = connection.getOutputStream()) {
            writePart(output, boundary, "chat_id", bot.chatId);
            output.write(
               ("--" + boundary + "\r\nContent-Disposition: form-data; name=\"photo\"; filename=\"" + file.getName() + "\"\r\nContent-Type: image/png\r\n\r\n")
                  .getBytes(StandardCharsets.UTF_8)
            );
            Files.copy(file.toPath(), output);
            output.write(("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
         }

         connection.getInputStream().close();
      } catch (IOException var9) {
         send("Скриншот не отправился: " + var9.getMessage());
      }
   }

   private static void writePart(OutputStream output, String boundary, String name, String value) throws IOException {
      output.write(("--" + boundary + "\r\nContent-Disposition: form-data; name=\"" + name + "\"\r\n\r\n" + value + "\r\n").getBytes(StandardCharsets.UTF_8));
   }

   private static void post(TelegramBot.Bot bot, String method, JsonObject body) throws Exception {
      HttpRequest request = HttpRequest.newBuilder(URI.create(api(bot, method)))
         .timeout(Duration.ofSeconds(15L))
         .header("Content-Type", "application/json")
         .POST(BodyPublishers.ofString(GSON.toJson(body), StandardCharsets.UTF_8))
         .build();
      HTTP.send(request, BodyHandlers.discarding());
   }

   private static String api(TelegramBot.Bot bot, String method) {
      return "https://api.telegram.org/bot" + URLEncoder.encode(bot.token, StandardCharsets.UTF_8).replace("%3A", ":") + "/" + method;
   }

   private static Path file() {
      return new File(Interface.aM_.field_1697, "configs\\general\\telegram.json").toPath();
   }

   private static synchronized void load() {
      if (!loaded) {
         loaded = true;

         try {
            if (!Files.exists(file())) {
               return;
            }

            JsonObject json = JsonParser.parseString(Files.readString(file(), StandardCharsets.UTF_8)).getAsJsonObject();
            if (json.has("bots")) {
               for (Entry<String, JsonElement> entry : json.getAsJsonObject("bots").entrySet()) {
                  JsonObject bot = entry.getValue().getAsJsonObject();
                  BOTS.put(entry.getKey(), new TelegramBot.Bot(bot.get("token").getAsString(), bot.get("chatId").getAsString()));
               }
            }

            if (json.has("active") && !json.get("active").isJsonNull()) {
               active = json.get("active").getAsString();
            }
         } catch (Exception var4) {
            ChatUtil.a("Не удалось прочитать список телеграм-ботов.");
         }
      }
   }

   private static synchronized void save() {
      try {
         JsonObject bots = new JsonObject();
         BOTS.forEach((name, bot) -> {
            JsonObject entry = new JsonObject();
            entry.addProperty("token", bot.token);
            entry.addProperty("chatId", bot.chatId);
            bots.add(name, entry);
         });
         JsonObject json = new JsonObject();
         json.add("bots", bots);
         json.addProperty("active", active);
         Files.createDirectories(file().getParent());
         Files.writeString(file(), GSON.toJson(json), StandardCharsets.UTF_8);
      } catch (Exception var2) {
         ChatUtil.a("Не удалось сохранить список телеграм-ботов.");
      }
   }

   public static final class Bot {
      public String token;
      public String chatId;

      Bot(String token, String chatId) {
         this.token = token;
         this.chatId = chatId;
      }
   }
}

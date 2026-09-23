package rockstar.client.internal.script;





import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.internal.network.*;
import rockstar.client.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public final class McpToolRegistry {
   public static final String internalField0248 = "Rockstar \u2014 \u0447\u0438\u0442-\u043a\u043b\u0438\u0435\u043d\u0442 Minecraft. \u0427\u0435\u0440\u0435\u0437 \u044d\u0442\u0438 \u0438\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u044b \u0432\u0438\u0434\u043d\u043e \u0441\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u0435 \u0438\u0433\u0440\u044b \u0438 \u043a\u043b\u0438\u0435\u043d\u0442\u0430 \u0438 \u043c\u043e\u0436\u043d\u043e \u0438\u043c \u0443\u043f\u0440\u0430\u0432\u043b\u044f\u0442\u044c.\n\u041f\u043e\u0440\u044f\u0434\u043e\u043a \u0440\u0430\u0431\u043e\u0442\u044b: game_state \u2014 \u0447\u0442\u043e \u043f\u0440\u043e\u0438\u0441\u0445\u043e\u0434\u0438\u0442; screenshot \u2014 \u043a\u0430\u043a \u044d\u0442\u043e \u0432\u044b\u0433\u043b\u044f\u0434\u0438\u0442; mouse/keyboard \u2014 \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u044f \u043f\u043e \u043a\u0430\u0440\u0442\u0438\u043d\u043a\u0435.\n\u041a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b \u0434\u043b\u044f mouse \u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e \u0441\u0447\u0438\u0442\u0430\u044e\u0442\u0441\u044f \u0432 \u043f\u0438\u043a\u0441\u0435\u043b\u044f\u0445 \u043f\u043e\u0441\u043b\u0435\u0434\u043d\u0435\u0433\u043e \u0441\u043a\u0440\u0438\u043d\u0448\u043e\u0442\u0430, \u043f\u043e\u044d\u0442\u043e\u043c\u0443 \u043a\u043b\u0438\u043a\u0430\u0442\u044c \u043d\u0443\u0436\u043d\u043e \u043f\u043e \u0442\u043e\u043c\u0443, \u0447\u0442\u043e \u0432\u0438\u0434\u043d\u043e \u043d\u0430 \u043d\u0451\u043c (space \u043c\u0435\u043d\u044f\u0435\u0442 \u0441\u0438\u0441\u0442\u0435\u043c\u0443 \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442).\n\u041c\u043e\u0434\u0443\u043b\u0438 \u043c\u0435\u043d\u044f\u044e\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 module_control \u0438 setting_set: \u0438\u043c\u0435\u043d\u0430 \u043d\u0430\u0441\u0442\u0440\u043e\u0435\u043a \u0431\u0435\u0440\u0443\u0442\u0441\u044f \u0438\u0437 module_info.\n\u0421\u043a\u0440\u0438\u043f\u0442\u044b \u043a\u043b\u0438\u0435\u043d\u0442\u0430 \u2014 Python \u043f\u043e \u043d\u0430\u0448\u0435\u043c\u0443 API, \u0438 \u043f\u043e \u043f\u0430\u043c\u044f\u0442\u0438 \u043e\u043d \u043d\u0435 \u043f\u0438\u0448\u0435\u0442\u0441\u044f: \u043f\u0435\u0440\u0435\u0434 scripts write \u043e\u0431\u044f\u0437\u0430\u0442\u0435\u043b\u044c\u043d\u043e \u043f\u0440\u043e\u0447\u0438\u0442\u0430\u0439 script_api (\u0440\u0430\u0437\u0434\u0435\u043b basics, \u0434\u0430\u043b\u044c\u0448\u0435 \u043f\u043e \u0435\u0433\u043e \u043a\u0430\u0440\u0442\u0435 \u0440\u0430\u0437\u0434\u0435\u043b\u043e\u0432).";
   private static final Gson internalField0931 = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
   private static final Map<String, McpToolRegistry.InternalType0090> internalField0543 = new LinkedHashMap<>();

   private McpToolRegistry() {
   }

   public static JsonObject internalMethod02233() {
      JsonArray localValue0 = new JsonArray();

      for (McpToolRegistry.InternalType0090 localValue2 : internalField0543.values()) {
         JsonObject localValue3 = new JsonObject();
         localValue3.addProperty("name", localValue2.internalMethod02112());
         localValue3.addProperty("description", localValue2.internalMethod06768());
         localValue3.add("inputSchema", localValue2.internalMethod04539());
         localValue0.add(localValue3);
      }

      JsonObject localValue4 = new JsonObject();
      localValue4.add("tools", localValue0);
      return localValue4;
   }

   public static JsonObject internalMethod04076(String localValue0, JsonObject localValue1) {
      McpToolRegistry.InternalType0090 localValue2 = internalField0543.get(localValue0);
      if (localValue2 == null) {
         return internalMethod01690(
            "\u043d\u0435\u0442 \u0438\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u0430 \""
               + localValue0
               + "\"; \u0435\u0441\u0442\u044c: "
               + String.join(", ", internalField0543.keySet()),
            true
         );
      } else {
         try {
            Object localValue3 = localValue2.internalMethod06622()
               ? McpHttpServer.internalMethod06265(() -> localValue2.internalMethod02796().apply(localValue1))
               : localValue2.internalMethod02796().apply(localValue1);
            return internalMethod03970(localValue3);
         } catch (McpHttpServer.InternalType0086 localValue4) {
            return internalMethod01690(localValue4.getMessage(), true);
         } catch (Exception localValue5) {
            RockstarClient.internalField0572.error("[MCP] \u0438\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442 {} \u0443\u043f\u0430\u043b", localValue0, localValue5);
            return internalMethod01690("\u0438\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442 \u0443\u043f\u0430\u043b: " + localValue5, true);
         }
      }
   }

   private static JsonObject internalMethod03970(Object localValue0) {
      if (localValue0 instanceof McpInputController.InternalType0428 localValue6) {
         JsonObject localValue2 = new JsonObject();
         localValue2.addProperty("type", "image");
         localValue2.addProperty("data", Base64.getEncoder().encodeToString(localValue6.internalMethod03687()));
         localValue2.addProperty("mimeType", localValue6.internalMethod04674());
         JsonObject localValue3 = new JsonObject();
         localValue3.addProperty("imageWidth", localValue6.internalMethod04133());
         localValue3.addProperty("imageHeight", localValue6.internalMethod04135());
         localValue3.addProperty("frameWidth", localValue6.internalMethod08399());
         localValue3.addProperty("frameHeight", localValue6.internalMethod08400());
         localValue3.addProperty(
            "hint",
            "\u043a\u043b\u0438\u043a\u0438 \u043f\u043e \u044d\u0442\u043e\u043c\u0443 \u043a\u0430\u0434\u0440\u0443: mouse \u0441\u043e space=image (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e)"
         );
         JsonArray localValue4 = new JsonArray();
         localValue4.add(localValue2);
         localValue4.add(internalMethod05658(internalField0931.toJson(localValue3)));
         JsonObject localValue5 = new JsonObject();
         localValue5.add("content", localValue4);
         return localValue5;
      } else {
         return localValue0 instanceof JsonElement localValue1 ? internalMethod01690(internalField0931.toJson(localValue1), false) : internalMethod01690(String.valueOf(localValue0), false);
      }
   }

   private static JsonObject internalMethod01690(String localValue0, boolean localValue1) {
      JsonArray localValue2 = new JsonArray();
      localValue2.add(internalMethod05658(localValue0));
      JsonObject localValue3 = new JsonObject();
      localValue3.add("content", localValue2);
      if (localValue1) {
         localValue3.addProperty("isError", true);
      }

      return localValue3;
   }

   private static JsonObject internalMethod05658(String localValue0) {
      JsonObject localValue1 = new JsonObject();
      localValue1.addProperty("type", "text");
      localValue1.addProperty("text", localValue0);
      return localValue1;
   }

   private static void internalMethod01530(String localValue0, String localValue1, JsonObject localValue2, boolean localValue3, Function<JsonObject, Object> localValue4) {
      internalField0543.put(localValue0, new McpToolRegistry.InternalType0090(localValue0, localValue1, localValue2, localValue3, localValue4));
   }

   private static String internalMethod02760(JsonObject localValue0, String localValue1, String localValue2) {
      JsonElement localValue3 = localValue0.get(localValue1);
      return localValue3 != null && !localValue3.isJsonNull() ? localValue3.getAsString() : localValue2;
   }

   private static double internalMethod00323(JsonObject localValue0, String localValue1, double localValue2) {
      JsonElement localValue4 = localValue0.get(localValue1);
      if (localValue4 != null && !localValue4.isJsonNull()) {
         try {
            return localValue4.getAsDouble();
         } catch (Exception localValue6) {
            throw new McpHttpServer.InternalType0086(
               localValue1 + " \u0434\u043e\u043b\u0436\u0435\u043d \u0431\u044b\u0442\u044c \u0447\u0438\u0441\u043b\u043e\u043c"
            );
         }
      } else {
         return localValue2;
      }
   }

   private static Double internalMethod02828(JsonObject localValue0, String localValue1) {
      JsonElement localValue2 = localValue0.get(localValue1);
      return localValue2 != null && !localValue2.isJsonNull() ? localValue2.getAsDouble() : null;
   }

   private static int internalMethod00324(JsonObject localValue0, String localValue1, int localValue2) {
      return (int)Math.round(internalMethod00323(localValue0, localValue1, localValue2));
   }

   private static boolean internalMethod00325(JsonObject localValue0, String localValue1, boolean localValue2) {
      JsonElement localValue3 = localValue0.get(localValue1);
      if (localValue3 != null && !localValue3.isJsonNull()) {
         return localValue3.getAsJsonPrimitive().isBoolean() ? localValue3.getAsBoolean() : Boolean.parseBoolean(localValue3.getAsString());
      } else {
         return localValue2;
      }
   }

   private static List<String> internalMethod04138(JsonObject localValue0, String localValue1) {
      ArrayList localValue2 = new ArrayList();
      JsonElement localValue3 = localValue0.get(localValue1);
      if (localValue3 != null && !localValue3.isJsonNull()) {
         if (localValue3.isJsonArray()) {
            localValue3.getAsJsonArray().forEach(localValue1x -> localValue2.add(localValue1x.getAsString()));
         } else {
            localValue2.add(localValue3.getAsString());
         }

         return localValue2;
      } else {
         return localValue2;
      }
   }

   private static McpToolRegistry.InternalType0089 internalMethod01523() {
      return new McpToolRegistry.InternalType0089();
   }

   static {
      internalMethod01530(
         "game_state",
         "\u0421\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u0435 \u0438\u0433\u0440\u044b \u0438 \u043a\u043b\u0438\u0435\u043d\u0442\u0430: \u0438\u0433\u0440\u043e\u043a (\u043d\u0438\u043a, \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b, \u0437\u0434\u043e\u0440\u043e\u0432\u044c\u0435, \u0435\u0434\u0430, \u044d\u0444\u0444\u0435\u043a\u0442\u044b, \u0440\u0443\u043a\u0438, \u0431\u0440\u043e\u043d\u044f), \u043c\u0438\u0440, \u0441\u0435\u0440\u0432\u0435\u0440 (\u0430\u0434\u0440\u0435\u0441, \u043f\u0438\u043d\u0433, TPS, \u043e\u043d\u043b\u0430\u0439\u043d), \u043a\u043b\u0438\u0435\u043d\u0442 (\u0432\u0435\u0440\u0441\u0438\u044f, FPS, \u043a\u043e\u043d\u0444\u0438\u0433, \u0432\u043a\u043b\u044e\u0447\u0451\u043d\u043d\u044b\u0435 \u043c\u043e\u0434\u0443\u043b\u0438) \u0438 \u044d\u043a\u0440\u0430\u043d (\u0440\u0430\u0437\u043c\u0435\u0440 \u043e\u043a\u043d\u0430, gui-scale, \u043e\u0442\u043a\u0440\u044b\u0442\u044b\u0439 \u044d\u043a\u0440\u0430\u043d).",
         internalMethod01523()
            .internalMethod02971(
               "sections",
               "\u041a\u0430\u043a\u0438\u0435 \u0440\u0430\u0437\u0434\u0435\u043b\u044b \u0432\u0435\u0440\u043d\u0443\u0442\u044c: player, world, server, client, screen, inventory, chat. \u041f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e \u0432\u0441\u0435, \u043a\u0440\u043e\u043c\u0435 inventory \u0438 chat.",
               "string"
            )
            .internalMethod06713(),
         true,
         localValue0 -> {
            List localValue1 = internalMethod04138(localValue0, "sections");
            if (localValue1.isEmpty()) {
               localValue1 = List.of("player", "world", "server", "client", "screen");
            }

            JsonObject localValue2 = new JsonObject();

            for (String localValue4 : (Iterable<String>)(Iterable<?>)localValue1) {
               String localValue5 = localValue4.toLowerCase(Locale.ROOT);
               switch (localValue5) {
                  case "player":
                     localValue2.add("player", GameStateSnapshotter.internalMethod02895());
                     break;
                  case "world":
                     localValue2.add("world", GameStateSnapshotter.internalMethod03738());
                     break;
                  case "server":
                     localValue2.add("server", GameStateSnapshotter.internalMethod08143());
                     break;
                  case "client":
                     localValue2.add("client", GameStateSnapshotter.internalMethod09093());
                     break;
                  case "screen":
                     localValue2.add("screen", GameStateSnapshotter.internalMethod08175());
                     break;
                  case "render":
                     localValue2.add("render", rockstar.client.render.compat.RenderAudit.request());
                     break;
                  case "inventory":
                     localValue2.add("inventory", GameStateSnapshotter.internalMethod08084());
                     break;
                  case "chat":
                     localValue2.add("chat", GameStateSnapshotter.internalMethod03981(30));
                     break;
                  default:
                     throw new McpHttpServer.InternalType0086(
                        "\u043d\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043d\u044b\u0439 \u0440\u0430\u0437\u0434\u0435\u043b: " + localValue4
                     );
               }
            }

            return localValue2;
         }
      );
      internalMethod01530(
         "entities_nearby",
         "\u041a\u0442\u043e \u0440\u044f\u0434\u043e\u043c: \u0438\u0433\u0440\u043e\u043a\u0438 \u0438 \u043c\u043e\u0431\u044b \u0441 \u0440\u0430\u0441\u0441\u0442\u043e\u044f\u043d\u0438\u0435\u043c, \u0437\u0434\u043e\u0440\u043e\u0432\u044c\u0435\u043c \u0438 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u043c \u0432 \u0440\u0443\u043a\u0435.",
         internalMethod01523()
            .internalMethod02966(
               "radius",
               "\u0420\u0430\u0434\u0438\u0443\u0441 \u043f\u043e\u0438\u0441\u043a\u0430 \u0432 \u0431\u043b\u043e\u043a\u0430\u0445 (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e 32)"
            )
            .internalMethod09012(
               "limit",
               "\u0421\u043a\u043e\u043b\u044c\u043a\u043e \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u0435\u0439 \u0432\u0435\u0440\u043d\u0443\u0442\u044c (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e 20)"
            )
            .internalMethod04070(
               "type",
               "players (\u0442\u043e\u043b\u044c\u043a\u043e \u0438\u0433\u0440\u043e\u043a\u0438), living (\u0436\u0438\u0432\u044b\u0435), all (\u0432\u0441\u0435). \u041f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e living"
            )
            .internalMethod06713(),
         true,
         localValue0 -> GameStateSnapshotter.internalMethod05987(
            "entities",
            GameStateSnapshotter.internalMethod03555(
               internalMethod00323(localValue0, "radius", 32.0), internalMethod00324(localValue0, "limit", 20), internalMethod02760(localValue0, "type", "living")
            )
         )
      );
      internalMethod01530(
         "chat_history",
         "\u041f\u043e\u0441\u043b\u0435\u0434\u043d\u0438\u0435 \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u044f \u0438\u0433\u0440\u043e\u0432\u043e\u0433\u043e \u0447\u0430\u0442\u0430, \u0441\u0432\u0435\u0436\u0438\u0435 \u0441\u0432\u0435\u0440\u0445\u0443.",
         internalMethod01523()
            .internalMethod09012(
               "limit",
               "\u0421\u043a\u043e\u043b\u044c\u043a\u043e \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0439 \u0432\u0435\u0440\u043d\u0443\u0442\u044c (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e 30)"
            )
            .internalMethod06713(),
         true,
         localValue0 -> GameStateSnapshotter.internalMethod05987("chat", GameStateSnapshotter.internalMethod03981(internalMethod00324(localValue0, "limit", 30)))
      );
      internalMethod01530(
         "chat_send",
         "\u041e\u0442\u043f\u0440\u0430\u0432\u0438\u0442\u044c \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0435 \u0432 \u0447\u0430\u0442. \u0422\u0435\u043a\u0441\u0442 \u0441 / \u0443\u0445\u043e\u0434\u0438\u0442 \u043a\u043e\u043c\u0430\u043d\u0434\u043e\u0439 \u0441\u0435\u0440\u0432\u0435\u0440\u0443, \u0442\u0435\u043a\u0441\u0442 \u0441 \u043f\u0440\u0435\u0444\u0438\u043a\u0441\u043e\u043c \u043a\u043b\u0438\u0435\u043d\u0442\u0430 (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e \u0442\u043e\u0447\u043a\u0430) \u0432\u044b\u043f\u043e\u043b\u043d\u044f\u0435\u0442\u0441\u044f \u043a\u0430\u043a \u043a\u043e\u043c\u0430\u043d\u0434\u0430 \u0441\u0430\u043c\u043e\u0433\u043e Rockstar.",
         internalMethod01523()
            .internalMethod04070("message", "\u0427\u0442\u043e \u043e\u0442\u043f\u0440\u0430\u0432\u0438\u0442\u044c")
            .internalMethod01022("message")
            .internalMethod06713(),
         true,
         localValue0 -> McpActionService.internalMethod06444(internalMethod02760(localValue0, "message", null))
      );
      internalMethod01530(
         "modules_list",
         "\u0421\u043f\u0438\u0441\u043e\u043a \u043c\u043e\u0434\u0443\u043b\u0435\u0439 \u043a\u043b\u0438\u0435\u043d\u0442\u0430: \u0438\u043c\u044f, \u043a\u0430\u0442\u0435\u0433\u043e\u0440\u0438\u044f, \u0432\u043a\u043b\u044e\u0447\u0451\u043d \u043b\u0438, \u0431\u0438\u043d\u0434, \u0447\u0438\u0441\u043b\u043e \u043d\u0430\u0441\u0442\u0440\u043e\u0435\u043a.",
         internalMethod01523()
            .internalMethod04070("category", "COMBAT, MOVEMENT, VISUALS, PLAYER \u0438\u043b\u0438 OTHER")
            .internalMethod08184("enabled_only", "\u0422\u043e\u043b\u044c\u043a\u043e \u0432\u043a\u043b\u044e\u0447\u0451\u043d\u043d\u044b\u0435")
            .internalMethod04070("query", "\u0424\u0438\u043b\u044c\u0442\u0440 \u043f\u043e \u0447\u0430\u0441\u0442\u0438 \u0438\u043c\u0435\u043d\u0438")
            .internalMethod08184(
               "include_hidden",
               "\u041f\u043e\u043a\u0430\u0437\u0430\u0442\u044c \u0438 \u0441\u043a\u0440\u044b\u0442\u044b\u0435 \u043c\u043e\u0434\u0443\u043b\u0438"
            )
            .internalMethod06713(),
         true,
         localValue0 -> GameStateSnapshotter.internalMethod05987(
            "modules",
            GameStateSnapshotter.internalMethod02493(
               internalMethod02760(localValue0, "category", null),
               internalMethod00325(localValue0, "enabled_only", false),
               internalMethod02760(localValue0, "query", null),
               internalMethod00325(localValue0, "include_hidden", false)
            )
         )
      );
      internalMethod01530(
         "module_info",
         "\u041c\u043e\u0434\u0443\u043b\u044c \u0446\u0435\u043b\u0438\u043a\u043e\u043c: \u0441\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u0435, \u0431\u0438\u043d\u0434 \u0438 \u0432\u0441\u0435 \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438 \u0441 \u0442\u0438\u043f\u0430\u043c\u0438, \u0442\u0435\u043a\u0443\u0449\u0438\u043c\u0438 \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u044f\u043c\u0438, \u0434\u0438\u0430\u043f\u0430\u0437\u043e\u043d\u0430\u043c\u0438 \u0438 \u0441\u043f\u0438\u0441\u043a\u0430\u043c\u0438 \u0432\u0430\u0440\u0438\u0430\u043d\u0442\u043e\u0432. \u0418\u043c\u0435\u043d\u0430 \u043d\u0430\u0441\u0442\u0440\u043e\u0435\u043a \u043e\u0442\u0441\u044e\u0434\u0430 \u0438\u0434\u0443\u0442 \u0432 setting_set.",
         internalMethod01523()
            .internalMethod04070("module", "\u0418\u043c\u044f \u043c\u043e\u0434\u0443\u043b\u044f, \u043d\u0430\u043f\u0440\u0438\u043c\u0435\u0440 Aura")
            .internalMethod01022("module")
            .internalMethod06713(),
         true,
         localValue0 -> {
            ModuleEntry localValue1 = McpActionService.internalMethod05485(internalMethod02760(localValue0, "module", null));
            return GameStateSnapshotter.internalMethod04760(localValue1, true);
         }
      );
      internalMethod01530(
         "module_control",
         "\u0412\u043a\u043b\u044e\u0447\u0438\u0442\u044c, \u0432\u044b\u043a\u043b\u044e\u0447\u0438\u0442\u044c \u0438\u043b\u0438 \u043f\u0435\u0440\u0435\u043a\u043b\u044e\u0447\u0438\u0442\u044c \u043c\u043e\u0434\u0443\u043b\u044c, \u043b\u0438\u0431\u043e \u043f\u043e\u0432\u0435\u0441\u0438\u0442\u044c \u043d\u0430 \u043d\u0435\u0433\u043e \u043a\u043b\u0430\u0432\u0438\u0448\u0443.",
         internalMethod01523()
            .internalMethod04070("module", "\u0418\u043c\u044f \u043c\u043e\u0434\u0443\u043b\u044f")
            .internalMethod01022("module")
            .internalMethod04070(
               "action", "enable, disable, toggle \u0438\u043b\u0438 bind (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e toggle)"
            )
            .internalMethod08319(
               "key",
               "\u0414\u043b\u044f action=bind: \u0438\u043c\u044f \u043a\u043b\u0430\u0432\u0438\u0448\u0438 (\"R\", \"MOUSE4\", \"none\") \u0438\u043b\u0438 GLFW-\u043a\u043e\u0434"
            )
            .internalMethod06713(),
         true,
         localValue0 -> {
            String localValue1 = internalMethod02760(localValue0, "action", "toggle");
            return "bind".equalsIgnoreCase(localValue1)
               ? McpActionService.internalMethod05565(internalMethod02760(localValue0, "module", null), localValue0.get("key"))
               : McpActionService.internalMethod02764(internalMethod02760(localValue0, "module", null), localValue1);
         }
      );
      internalMethod01530(
         "setting_set",
         "\u0418\u0437\u043c\u0435\u043d\u0438\u0442\u044c \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0443 \u043c\u043e\u0434\u0443\u043b\u044f. \u0417\u043d\u0430\u0447\u0435\u043d\u0438\u0435 \u0432 \u0447\u0435\u043b\u043e\u0432\u0435\u0447\u0435\u0441\u043a\u043e\u043c \u0432\u0438\u0434\u0435: true/false, \u0447\u0438\u0441\u043b\u043e, \u0438\u043c\u044f \u0440\u0435\u0436\u0438\u043c\u0430, \u043c\u0430\u0441\u0441\u0438\u0432 \u043f\u0443\u043d\u043a\u0442\u043e\u0432 \u0434\u043b\u044f \u043c\u0443\u043b\u044c\u0442\u0438\u0432\u044b\u0431\u043e\u0440\u0430, #rrggbbaa \u0434\u043b\u044f \u0446\u0432\u0435\u0442\u0430, \u0438\u043c\u044f \u043a\u043b\u0430\u0432\u0438\u0448\u0438 \u0434\u043b\u044f \u0431\u0438\u043d\u0434\u0430, \"click\" \u0434\u043b\u044f \u043a\u043d\u043e\u043f\u043a\u0438.",
         internalMethod01523()
            .internalMethod04070("module", "\u0418\u043c\u044f \u043c\u043e\u0434\u0443\u043b\u044f")
            .internalMethod01022("module")
            .internalMethod04070("setting", "\u0418\u043c\u044f \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438 \u0438\u0437 module_info")
            .internalMethod01022("setting")
            .internalMethod08319("value", "\u041d\u043e\u0432\u043e\u0435 \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u0435")
            .internalMethod01022("value")
            .internalMethod06713(),
         true,
         localValue0 -> McpActionService.internalMethod00575(internalMethod02760(localValue0, "module", null), internalMethod02760(localValue0, "setting", null), localValue0.get("value"))
      );
      internalMethod01530(
         "binds_list",
         "\u0412\u0441\u0435 \u043d\u0430\u0437\u043d\u0430\u0447\u0435\u043d\u043d\u044b\u0435 \u043a\u043b\u0430\u0432\u0438\u0448\u0438: \u0431\u0438\u043d\u0434\u044b \u043c\u043e\u0434\u0443\u043b\u0435\u0439, \u043d\u0430\u0441\u0442\u0440\u043e\u0435\u043a, \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432 Assist \u0438 \u043c\u0430\u043a\u0440\u043e\u0441\u044b.",
         internalMethod01523().internalMethod06713(),
         true,
         localValue0 -> GameStateSnapshotter.internalMethod09119()
      );
      internalMethod01530(
         "macros",
         "\u041c\u0430\u043a\u0440\u043e\u0441\u044b: \u043a\u043e\u043c\u0430\u043d\u0434\u0430 \u0447\u0430\u0442\u0430 \u043d\u0430 \u043a\u043b\u0430\u0432\u0438\u0448\u0435. \u0414\u0435\u0439\u0441\u0442\u0432\u0438\u044f list, add, remove.",
         internalMethod01523()
            .internalMethod04070("action", "list, add \u0438\u043b\u0438 remove (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e list)")
            .internalMethod04070(
               "command", "\u041a\u043e\u043c\u0430\u043d\u0434\u0430, \u043d\u0430\u043f\u0440\u0438\u043c\u0435\u0440 /spawn \u0438\u043b\u0438 .toggle aura"
            )
            .internalMethod08319("key", "\u041a\u043b\u0430\u0432\u0438\u0448\u0430: \u0438\u043c\u044f (\"G\") \u0438\u043b\u0438 GLFW-\u043a\u043e\u0434")
            .internalMethod06713(),
         true,
         localValue0 -> {
            String localValue1 = internalMethod02760(localValue0, "action", "list").toLowerCase(Locale.ROOT);

            return switch (localValue1) {
               case "add" -> GameStateSnapshotter.internalMethod05987(
                  "macros", McpActionService.internalMethod05607(internalMethod02760(localValue0, "command", null), localValue0.get("key"))
               );
               case "remove" -> GameStateSnapshotter.internalMethod05987(
                  "macros", McpActionService.internalMethod06365(internalMethod02760(localValue0, "command", null), localValue0.get("key"))
               );
               default -> GameStateSnapshotter.internalMethod05987("macros", GameStateSnapshotter.internalMethod02087());
            };
         }
      );
      internalMethod01530(
         "configs",
         "\u041b\u043e\u043a\u0430\u043b\u044c\u043d\u044b\u0435 \u043a\u043e\u043d\u0444\u0438\u0433\u0438 \u043a\u043b\u0438\u0435\u043d\u0442\u0430: list, save, load, delete, rename, duplicate, undo, reset.",
         internalMethod01523()
            .internalMethod04070(
               "action",
               "\u0427\u0442\u043e \u0441\u0434\u0435\u043b\u0430\u0442\u044c (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e list)"
            )
            .internalMethod04070("name", "\u0418\u043c\u044f \u043a\u043e\u043d\u0444\u0438\u0433\u0430")
            .internalMethod04070("new_name", "\u041d\u043e\u0432\u043e\u0435 \u0438\u043c\u044f \u0434\u043b\u044f rename")
            .internalMethod06713(),
         true,
         localValue0 -> McpActionService.internalMethod02860(
            internalMethod02760(localValue0, "action", "list"), internalMethod02760(localValue0, "name", null), internalMethod02760(localValue0, "new_name", null)
         )
      );
      internalMethod01530(
         "script_api",
         "\u0421\u043f\u0440\u0430\u0432\u043e\u0447\u043d\u0438\u043a \u043f\u043e Python-API \u0441\u043a\u0440\u0438\u043f\u0442\u043e\u0432 \u043a\u043b\u0438\u0435\u043d\u0442\u0430: \u043c\u043e\u0434\u0443\u043b\u0438, \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438, \u0441\u043e\u0431\u044b\u0442\u0438\u044f, HUD, \u043c\u0435\u043d\u044e, \u0440\u0435\u043d\u0434\u0435\u0440, \u0433\u043e\u0442\u043e\u0432\u044b\u0435 \u043f\u0440\u0438\u043c\u0435\u0440\u044b. \u0427\u0438\u0442\u0430\u0439 \u0415\u0413\u041e, \u0430 \u043d\u0435 \u043f\u0430\u043c\u044f\u0442\u044c: \u043a\u043b\u0438\u0435\u043d\u0442\u0441\u043a\u043e\u0433\u043e API \u043d\u0435\u0442 \u0432 \u043e\u0442\u043a\u0440\u044b\u0442\u044b\u0445 \u0438\u0441\u0442\u043e\u0447\u043d\u0438\u043a\u0430\u0445, \u0438 \u0432\u044b\u0434\u0443\u043c\u0430\u043d\u043d\u044b\u0435 \u043c\u0435\u0442\u043e\u0434\u044b \u0438 \u0441\u043e\u0431\u044b\u0442\u0438\u044f \u043f\u0440\u043e\u0441\u0442\u043e \u043d\u0435 \u0440\u0430\u0431\u043e\u0442\u0430\u044e\u0442. \u0420\u0430\u0437\u0434\u0435\u043b\u044b: basics (\u043d\u0430\u0447\u043d\u0438 \u0441 \u043d\u0435\u0433\u043e), api, events, ui, recipes.",
         internalMethod01523()
            .internalMethod04070(
               "section",
               "\u0420\u0430\u0437\u0434\u0435\u043b: basics (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e), api, events, ui, recipes"
            )
            .internalMethod04070(
               "query",
               "\u041d\u0435\u043e\u0431\u044f\u0437\u0430\u0442\u0435\u043b\u044c\u043d\u043e: \u0441\u043b\u043e\u0432\u043e \u0438\u043b\u0438 \u0438\u043c\u044f \u043c\u0435\u0442\u043e\u0434\u0430 \u2014 \u0442\u043e\u0433\u0434\u0430 \u043f\u0440\u0438\u0434\u0443\u0442 \u0442\u043e\u043b\u044c\u043a\u043e \u043f\u043e\u0434\u0440\u0430\u0437\u0434\u0435\u043b\u044b \u0441 \u043d\u0438\u043c"
            )
            .internalMethod06713(),
         false,
         localValue0 -> ScriptDocsProvider.internalMethod06334(internalMethod02760(localValue0, "section", "basics"), internalMethod02760(localValue0, "query", null))
      );
      internalMethod01530(
         "scripts",
         "Python-\u0441\u043a\u0440\u0438\u043f\u0442\u044b \u043a\u043b\u0438\u0435\u043d\u0442\u0430: list, read, write (\u0441\u043e\u0437\u0434\u0430\u0451\u0442 \u0438\u043b\u0438 \u043f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u044b\u0432\u0430\u0435\u0442), delete, enable, disable, reload. \u041f\u0435\u0440\u0435\u0434 write \u0432\u043e\u0437\u044c\u043c\u0438 API \u0438\u0437 script_api. \u041e\u0442\u0432\u0435\u0442 \u043d\u0430 write \u0433\u043e\u0432\u043e\u0440\u0438\u0442, \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u043b\u0441\u044f \u043b\u0438 \u0441\u043a\u0440\u0438\u043f\u0442, \u0438 \u043f\u0440\u0438\u043d\u043e\u0441\u0438\u0442 \u043e\u0448\u0438\u0431\u043a\u0443 \u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0438, \u0435\u0441\u043b\u0438 \u0443\u043f\u0430\u043b. \u041a\u0443\u043f\u043b\u0435\u043d\u043d\u044b\u0435 \u0437\u0430\u0449\u0438\u0449\u0451\u043d\u043d\u044b\u0435 \u0441\u043a\u0440\u0438\u043f\u0442\u044b \u0447\u0438\u0442\u0430\u0442\u044c \u043d\u0435\u043b\u044c\u0437\u044f \u2014 \u0438\u0441\u0445\u043e\u0434\u043d\u0438\u043a\u0430 \u043d\u0430 \u0434\u0438\u0441\u043a\u0435 \u043d\u0435\u0442.",
         internalMethod01523()
            .internalMethod04070(
               "action",
               "\u0427\u0442\u043e \u0441\u0434\u0435\u043b\u0430\u0442\u044c (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e list)"
            )
            .internalMethod04070("name", "\u0418\u043c\u044f \u0441\u043a\u0440\u0438\u043f\u0442\u0430 \u0431\u0435\u0437 .py")
            .internalMethod04070("content", "\u0421\u043e\u0434\u0435\u0440\u0436\u0438\u043c\u043e\u0435 \u0434\u043b\u044f write")
            .internalMethod06713(),
         true,
         localValue0 -> McpActionService.internalMethod02783(
            internalMethod02760(localValue0, "action", "list"), internalMethod02760(localValue0, "name", null), internalMethod02760(localValue0, "content", null)
         )
      );
      internalMethod01530(
         "swing",
         "\u0410\u043d\u0438\u043c\u0430\u0446\u0438\u044f \u0437\u0430\u043c\u0430\u0445\u0430: state (\u0442\u0435\u043a\u0443\u0449\u0438\u0435 \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u044f \u0438 \u0441\u043f\u0438\u0441\u043e\u043a \u043f\u0440\u0435\u0441\u0435\u0442\u043e\u0432), apply (\u0432\u043a\u043b\u044e\u0447\u0438\u0442\u044c \u043f\u0440\u0435\u0441\u0435\u0442), set (\u043f\u043e\u043c\u0435\u043d\u044f\u0442\u044c \u043e\u0434\u043d\u0443 \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0443 \u0444\u0430\u0437\u044b).",
         internalMethod01523()
            .internalMethod04070("action", "state, apply \u0438\u043b\u0438 set (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e state)")
            .internalMethod04070("preset", "\u0418\u043c\u044f \u043f\u0440\u0435\u0441\u0435\u0442\u0430 \u0434\u043b\u044f apply")
            .internalMethod04070("phase", "\u0414\u043b\u044f set: shared, start \u0438\u043b\u0438 end")
            .internalMethod04070(
               "setting",
               "\u0414\u043b\u044f set: \u0438\u043c\u044f \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438, \u043d\u0430\u043f\u0440\u0438\u043c\u0435\u0440 swing.rotateX"
            )
            .internalMethod08319("value", "\u0414\u043b\u044f set: \u043d\u043e\u0432\u043e\u0435 \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u0435")
            .internalMethod06713(),
         true,
         localValue0 -> McpActionService.internalMethod05884(
            internalMethod02760(localValue0, "action", "state"),
            internalMethod02760(localValue0, "preset", null),
            internalMethod02760(localValue0, "phase", null),
            internalMethod02760(localValue0, "setting", null),
            localValue0.get("value")
         )
      );
      internalMethod01530(
         "menu",
         "\u041c\u0435\u043d\u044e \u043a\u043b\u0438\u0435\u043d\u0442\u0430: open, close, toggle. \u041e\u0442\u043a\u0440\u044b\u0442\u043e\u0435 \u043c\u0435\u043d\u044e \u2014 \u043e\u0431\u044b\u0447\u043d\u044b\u0439 \u044d\u043a\u0440\u0430\u043d, \u043f\u043e \u043d\u0435\u043c\u0443 \u043c\u043e\u0436\u043d\u043e \u043a\u043b\u0438\u043a\u0430\u0442\u044c.",
         internalMethod01523()
            .internalMethod04070("action", "open, close \u0438\u043b\u0438 toggle (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e open)")
            .internalMethod06713(),
         true,
         localValue0 -> McpActionService.internalMethod05968(internalMethod02760(localValue0, "action", "open"))
      );
      internalMethod01530(
         "resource_reload",
         "\u041f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u0440\u0435\u0441\u0443\u0440\u0441\u044b Minecraft \u0442\u0430\u043a \u0436\u0435, \u043a\u0430\u043a F3+T. \u0418\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0435\u0442\u0441\u044f \u0434\u043b\u044f \u043f\u0440\u043e\u0432\u0435\u0440\u043a\u0438 \u0448\u0440\u0438\u0444\u0442\u043e\u0432, \u0430\u0442\u043b\u0430\u0441\u043e\u0432 \u0438 \u0448\u0435\u0439\u0434\u0435\u0440\u043e\u0432 \u043f\u043e\u0441\u043b\u0435 reload.",
         internalMethod01523().internalMethod06713(),
         false,
         localValue0 -> McpActionService.reloadResourcesForDiagnostics()
      );
      internalMethod01530(
         "screenshot",
         "\u041a\u0430\u0434\u0440 \u0438\u0433\u0440\u044b \u043a\u0430\u043a \u043a\u0430\u0440\u0442\u0438\u043d\u043a\u0430. \u0412\u043e\u0437\u0432\u0440\u0430\u0449\u0430\u0435\u0442 \u0435\u0449\u0451 \u0438 \u0440\u0430\u0437\u043c\u0435\u0440\u044b: \u043a\u043b\u0438\u043a\u0438 \u0438\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u043e\u043c mouse \u0441\u0447\u0438\u0442\u0430\u044e\u0442\u0441\u044f \u0432 \u043f\u0438\u043a\u0441\u0435\u043b\u044f\u0445 \u044d\u0442\u043e\u0433\u043e \u043a\u0430\u0434\u0440\u0430.",
         internalMethod01523()
            .internalMethod09012(
               "max_width",
               "\u0428\u0438\u0440\u0438\u043d\u0430 \u043a\u0430\u0440\u0442\u0438\u043d\u043a\u0438, \u043a\u0430\u0434\u0440 \u0443\u0436\u0438\u043c\u0430\u0435\u0442\u0441\u044f \u043f\u0440\u043e\u043f\u043e\u0440\u0446\u0438\u043e\u043d\u0430\u043b\u044c\u043d\u043e (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e 1280)"
            )
            .internalMethod04070("format", "jpeg (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e) \u0438\u043b\u0438 png")
            .internalMethod02966(
               "quality",
               "\u041a\u0430\u0447\u0435\u0441\u0442\u0432\u043e JPEG \u043e\u0442 0.1 \u0434\u043e 1.0 (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e 0.75)"
            )
            .internalMethod06713(),
         false,
         localValue0 -> McpInputController.internalMethod01136(
            internalMethod00324(localValue0, "max_width", 1280), internalMethod02760(localValue0, "format", "jpeg"), (float)internalMethod00323(localValue0, "quality", 0.75)
         )
      );
      internalMethod01530(
         "mouse",
         "\u041c\u044b\u0448\u044c: move, click, drag, scroll. \u041a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b \u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e \u2014 \u043f\u0438\u043a\u0441\u0435\u043b\u0438 \u043f\u043e\u0441\u043b\u0435\u0434\u043d\u0435\u0433\u043e \u0441\u043a\u0440\u0438\u043d\u0448\u043e\u0442\u0430. \u0415\u0441\u043b\u0438 \u043e\u0442\u043a\u0440\u044b\u0442\u043e\u0433\u043e \u044d\u043a\u0440\u0430\u043d\u0430 \u043d\u0435\u0442, \u043a\u043b\u0438\u043a \u0443\u0445\u043e\u0434\u0438\u0442 \u0432 \u0438\u0433\u0440\u0443 (\u043b\u0435\u0432\u0430\u044f \u2014 \u0443\u0434\u0430\u0440, \u043f\u0440\u0430\u0432\u0430\u044f \u2014 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435).",
         internalMethod01523()
            .internalMethod04070(
               "action", "move, click, drag \u0438\u043b\u0438 scroll (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e click)"
            )
            .internalMethod02966("x", "\u041a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u0430 X")
            .internalMethod02966("y", "\u041a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u0430 Y")
            .internalMethod02966("to_x", "\u041a\u0443\u0434\u0430 \u0442\u044f\u043d\u0443\u0442\u044c \u0434\u043b\u044f drag")
            .internalMethod02966("to_y", "\u041a\u0443\u0434\u0430 \u0442\u044f\u043d\u0443\u0442\u044c \u0434\u043b\u044f drag")
            .internalMethod04070("button", "left, right \u0438\u043b\u0438 middle (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e left)")
            .internalMethod09012(
               "clicks",
               "\u0421\u043a\u043e\u043b\u044c\u043a\u043e \u043a\u043b\u0438\u043a\u043e\u0432 \u043f\u043e\u0434\u0440\u044f\u0434 (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e 1)"
            )
            .internalMethod02966(
               "amount",
               "\u0412\u0435\u043b\u0438\u0447\u0438\u043d\u0430 \u043f\u0440\u043e\u043a\u0440\u0443\u0442\u043a\u0438, \u043f\u043b\u044e\u0441 \u0432\u0432\u0435\u0440\u0445"
            )
            .internalMethod09012(
               "hold_ms",
               "\u0421\u043a\u043e\u043b\u044c\u043a\u043e \u0434\u0435\u0440\u0436\u0430\u0442\u044c \u043a\u043d\u043e\u043f\u043a\u0443, \u043c\u0441"
            )
            .internalMethod04070(
               "space",
               "image (\u043f\u0438\u043a\u0441\u0435\u043b\u0438 \u0441\u043a\u0440\u0438\u043d\u0448\u043e\u0442\u0430, \u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e), pixels (\u043f\u0438\u043a\u0441\u0435\u043b\u0438 \u043a\u0430\u0434\u0440\u0430) \u0438\u043b\u0438 gui"
            )
            .internalMethod06713(),
         true,
         localValue0 -> McpInputController.internalMethod00871(
            internalMethod02760(localValue0, "action", "click"),
            internalMethod00323(localValue0, "x", 0.0),
            internalMethod00323(localValue0, "y", 0.0),
            internalMethod02760(localValue0, "button", "left"),
            internalMethod00324(localValue0, "clicks", 1),
            internalMethod00323(localValue0, "amount", 0.0),
            internalMethod00324(localValue0, "hold_ms", 0),
            internalMethod02828(localValue0, "to_x"),
            internalMethod02828(localValue0, "to_y"),
            internalMethod02760(localValue0, "space", "image")
         )
      );
      internalMethod01530(
         "keyboard",
         "\u041a\u043b\u0430\u0432\u0438\u0430\u0442\u0443\u0440\u0430: press (\u043d\u0430\u0436\u0430\u0442\u044c \u043a\u043b\u0430\u0432\u0438\u0448\u0443, \u0432 \u0442\u043e\u043c \u0447\u0438\u0441\u043b\u0435 \u0431\u0438\u043d\u0434 \u043c\u043e\u0434\u0443\u043b\u044f) \u0438\u043b\u0438 type (\u043d\u0430\u043f\u0435\u0447\u0430\u0442\u0430\u0442\u044c \u0442\u0435\u043a\u0441\u0442 \u0432 \u043e\u0442\u043a\u0440\u044b\u0442\u043e\u0435 \u043f\u043e\u043b\u0435 \u0432\u0432\u043e\u0434\u0430).",
         internalMethod01523()
            .internalMethod04070("action", "press \u0438\u043b\u0438 type (\u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e press)")
            .internalMethod04070("key", "\u0418\u043c\u044f \u043a\u043b\u0430\u0432\u0438\u0448\u0438: R, ESCAPE, F3, LEFT_SHIFT")
            .internalMethod04070("text", "\u0422\u0435\u043a\u0441\u0442 \u0434\u043b\u044f type")
            .internalMethod02971("modifiers", "shift, ctrl, alt", "string")
            .internalMethod09012(
               "hold_ms",
               "\u0421\u043a\u043e\u043b\u044c\u043a\u043e \u0434\u0435\u0440\u0436\u0430\u0442\u044c \u043a\u043b\u0430\u0432\u0438\u0448\u0443, \u043c\u0441"
            )
            .internalMethod06713(),
         true,
         localValue0 -> McpInputController.internalMethod00576(
            internalMethod02760(localValue0, "action", "press"),
            internalMethod02760(localValue0, "key", null),
            internalMethod02760(localValue0, "text", null),
            internalMethod04138(localValue0, "modifiers"),
            internalMethod00324(localValue0, "hold_ms", 0)
         )
      );
   }

   static final class InternalType0089 {
      private final JsonObject internalField0539 = new JsonObject();
      private final JsonArray internalField0368 = new JsonArray();
      private String internalField0248;

      private McpToolRegistry.InternalType0089 internalMethod02905(String localValue1, String localValue2, String localValue3) {
         JsonObject localValue4 = new JsonObject();
         if (localValue2 != null) {
            localValue4.addProperty("type", localValue2);
         }

         localValue4.addProperty("description", localValue3);
         this.internalField0539.add(localValue1, localValue4);
         this.internalField0248 = localValue1;
         return this;
      }

      McpToolRegistry.InternalType0089 internalMethod04070(String localValue1, String localValue2) {
         return this.internalMethod02905(localValue1, "string", localValue2);
      }

      McpToolRegistry.InternalType0089 internalMethod02966(String localValue1, String localValue2) {
         return this.internalMethod02905(localValue1, "number", localValue2);
      }

      McpToolRegistry.InternalType0089 internalMethod09012(String localValue1, String localValue2) {
         return this.internalMethod02905(localValue1, "integer", localValue2);
      }

      McpToolRegistry.InternalType0089 internalMethod08184(String localValue1, String localValue2) {
         return this.internalMethod02905(localValue1, "boolean", localValue2);
      }

      McpToolRegistry.InternalType0089 internalMethod08319(String localValue1, String localValue2) {
         return this.internalMethod02905(localValue1, null, localValue2);
      }

      McpToolRegistry.InternalType0089 internalMethod02971(String localValue1, String localValue2, String localValue3) {
         this.internalMethod02905(localValue1, "array", localValue2);
         JsonObject localValue4 = new JsonObject();
         localValue4.addProperty("type", localValue3);
         this.internalField0539.getAsJsonObject(localValue1).add("items", localValue4);
         return this;
      }

      McpToolRegistry.InternalType0089 internalMethod01022(String localValue1) {
         this.internalField0368.add(localValue1 == null ? this.internalField0248 : localValue1);
         return this;
      }

      JsonObject internalMethod06713() {
         JsonObject localValue1 = new JsonObject();
         localValue1.addProperty("type", "object");
         localValue1.add("properties", this.internalField0539);
         if (!this.internalField0368.isEmpty()) {
            localValue1.add("required", this.internalField0368);
         }

         return localValue1;
      }
   }

   static final class InternalType0090 {
      private final String internalField0248;
      private final String internalField0247;
      private final JsonObject internalField0539;
      private final boolean internalField0277;
      private final Function<JsonObject, Object> internalField0571;

      InternalType0090(String localValue1, String localValue2, JsonObject localValue3, boolean localValue4, Function<JsonObject, Object> localValue5) {
         this.internalField0248 = localValue1;
         this.internalField0247 = localValue2;
         this.internalField0539 = localValue3;
         this.internalField0277 = localValue4;
         this.internalField0571 = localValue5;
      }

      @Override
      public final String toString() {
         return "InternalType0090[name=" + this.internalField0248 + ", description=" + this.internalField0247 + ", schema=" + this.internalField0539 + ", onClient=" + this.internalField0277 + ", handler=" + this.internalField0571 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0247);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0539);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0571);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         McpToolRegistry.InternalType0090 other = (McpToolRegistry.InternalType0090) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0247, other.internalField0247)
            && java.util.Objects.equals(this.internalField0539, other.internalField0539)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0571, other.internalField0571);
      }

      public String internalMethod02112() {
         return this.internalField0248;
      }

      public String internalMethod06768() {
         return this.internalField0247;
      }

      public JsonObject internalMethod04539() {
         return this.internalField0539;
      }

      public boolean internalMethod06622() {
         return this.internalField0277;
      }

      public Function<JsonObject, Object> internalMethod02796() {
         return this.internalField0571;
      }
   }
}

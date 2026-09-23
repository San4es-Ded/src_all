package rockstar.client.internal.script;










import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.bot.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class BotCommandHandler implements MinecraftClientAccess {
   private final Map<String, PatrolRouteTask> internalField0543 = new ConcurrentHashMap<>();
   private final AtomicReference<Thread> internalField0746 = new AtomicReference<>();
   private final AtomicBoolean internalField0020 = new AtomicBoolean(false);

   public CommandNode internalMethod07197() {
      return CommandBuilder.internalMethod00593("bot")
         .internalMethod05325("bots")
         .internalMethod06148("commands.bot.description")
         .internalMethod01539(
            "action",
            localValue0 -> {
               localValue0.internalMethod04818(
                  "help",
                  "join",
                  "connect",
                  "connectwithport",
                  "connectfast",
                  "start",
                  "stop",
                  "leave",
                  "clear",
                  "stopall",
                  "list",
                  "jump",
                  "sneak",
                  "attack",
                  "use",
                  "drop",
                  "slot",
                  "goto",
                  "follow",
                  "idle",
                  "patrol",
                  "tp",
                  "info",
                  "chat",
                  "say",
                  "cmd",
                  "password",
                  "pay",
                  "antiafk",
                  "rejoin",
                  "control",
                  "return",
                  "tapemouse",
                  "rebreak",
                  "instantrebreak",
                  "rebreakrejoin",
                  "mining",
                  "autojoingrief",
                  "proxy"
               );
               localValue0.internalMethod07138(
                  "help",
                  "join",
                  "connect",
                  "connectwithport",
                  "connectfast",
                  "start",
                  "stop",
                  "clear",
                  "list",
                  "jump",
                  "sneak",
                  "attack",
                  "use",
                  "drop",
                  "slot",
                  "goto",
                  "follow",
                  "idle",
                  "patrol",
                  "tp",
                  "info",
                  "chat",
                  "cmd",
                  "password",
                  "pay",
                  "antiafk",
                  "rejoin",
                  "control",
                  "return",
                  "tapemouse",
                  "rebreak",
                  "instantrebreak",
                  "rebreakrejoin",
                  "mining",
                  "autojoingrief",
                  "proxy"
               );
            }
         )
         .internalMethod01539("args", localValue0 -> localValue0.internalMethod06921().internalMethod00125().internalMethod00776(OperationResult::internalMethod00116))
         .internalMethod00262(this::internalMethod07630)
         .internalMethod04146();
   }

   private void internalMethod07630(ParsedCommand localValue1) {
      String localValue2 = ((String)localValue1.internalMethod02266().get(0)).toLowerCase(Locale.ROOT);
      List localValue3 = this.internalMethod06112(localValue1);
      BotRosterManager localValue4 = BotRosterManager.internalMethod03065();
      switch (localValue2) {
         case "help":
            this.internalMethod04726();
            break;
         case "join":
            this.internalMethod04232(localValue4, localValue3);
            break;
         case "connect":
            this.internalMethod00518(localValue3);
            break;
         case "connectwithport":
            this.internalMethod00092(localValue3);
            break;
         case "connectfast":
            this.internalMethod07896(localValue3);
            break;
         case "start":
            this.internalMethod07953(localValue3);
            break;
         case "leave":
         case "stop":
            this.internalMethod05053(localValue4, localValue3, localValue2);
            break;
         case "stopall":
         case "clear":
            this.internalMethod02694(localValue4);
            break;
         case "list":
            this.internalMethod03363(localValue4);
            break;
         case "jump":
            this.internalMethod08635(localValue4);
            break;
         case "sneak":
            this.internalMethod08780(localValue4);
            break;
         case "attack":
            this.internalMethod05739(localValue4, localValue3);
            break;
         case "use":
            this.internalMethod08047(localValue4);
            break;
         case "drop":
            this.internalMethod08172(localValue4);
            break;
         case "slot":
            this.internalMethod08797(localValue4, localValue3);
            break;
         case "goto":
            this.internalMethod09081(localValue4, localValue3);
            break;
         case "follow":
            this.internalMethod08302(localValue4, localValue3);
            break;
         case "idle":
            this.internalMethod08572(localValue4, localValue3);
            break;
         case "patrol":
            this.internalMethod09393(localValue4, localValue3);
            break;
         case "tp":
            this.internalMethod09594(localValue4);
            break;
         case "info":
            this.internalMethod09569(localValue4, localValue3);
            break;
         case "say":
         case "chat":
            this.internalMethod10130(localValue4, localValue3);
            break;
         case "cmd":
            this.internalMethod09955(localValue4, localValue3);
            break;
         case "password":
            this.internalMethod10050(localValue4, localValue3);
            break;
         case "pay":
            this.internalMethod10102(localValue4, localValue3);
            break;
         case "antiafk":
            this.internalMethod09963(localValue4, localValue3);
            break;
         case "rejoin":
            this.internalMethod09980(localValue4, localValue3);
            break;
         case "control":
            this.internalMethod10105(localValue4, localValue3);
            break;
         case "return":
            this.internalMethod09198(localValue4);
            break;
         case "mining":
            this.internalMethod09487(localValue4, localValue3);
            break;
         case "tapemouse":
            this.internalMethod09620(localValue4, localValue3);
            break;
         case "rebreak":
            this.internalMethod09211(localValue4, localValue3);
            break;
         case "instantrebreak":
            this.internalMethod09353(localValue4, localValue3);
            break;
         case "rebreakrejoin":
            this.internalMethod09642(localValue4, localValue3);
            break;
         case "autojoingrief":
            this.internalMethod09795(localValue4, localValue3);
            break;
         case "proxy":
            this.internalMethod06477(
               "\u042d\u0442\u0430 reborn-\u043a\u043e\u043c\u0430\u043d\u0434\u0430 \u0437\u0430\u0432\u044f\u0437\u0430\u043d\u0430 \u043d\u0430 BotWorld/BotPlayer \u0438\u0437 \u0441\u0442\u0430\u0440\u043e\u0433\u043e \u043a\u043b\u0438\u0435\u043d\u0442\u0430 \u0438 \u0432 1.21.4 headless-\u043f\u043e\u0440\u0442\u0435 \u043f\u043e\u043a\u0430 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u0430"
            );
            break;
         default:
            this.internalMethod06477(LanguageManager.internalMethod07214("commands.bot.unknown_action"));
      }
   }

   private List<String> internalMethod06112(ParsedCommand localValue1) {
      if (localValue1.internalMethod02266().size() >= 2 && localValue1.internalMethod02266().get(1) != null) {
         Object localValue2 = localValue1.internalMethod02266().get(1);
         if (localValue2 instanceof List localValue3) {
            ArrayList localValue4 = new ArrayList();

            for (Object localValue6 : localValue3) {
               if (localValue6 != null) {
                  localValue4.add(String.valueOf(localValue6));
               }
            }

            return localValue4;
         } else {
            return List.of(String.valueOf(localValue2));
         }
      } else {
         return List.of();
      }
   }

   private void internalMethod04232(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.isEmpty()) {
         this.internalMethod06477(LanguageManager.internalMethod07214("commands.bot.nickname_required"));
      } else {
         BotTargetManager localValue3;
         if (localValue2.size() >= 2) {
            localValue3 = BotSpawnCoordinator.internalMethod02098((String)localValue2.get(0), (String)localValue2.get(1));
         } else {
            localValue3 = localValue1.internalMethod05974((String)localValue2.get(0));
         }

         if (localValue3 == null) {
            this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.already_exists", localValue2.get(0)));
         } else {
            this.internalMethod04831(LanguageManager.internalMethod00160("commands.bot.joining", localValue2.get(0)));
         }
      }
   }

   private void internalMethod00518(List<String> localValue1) {
      if (localValue1.size() < 2) {
         this.internalMethod06477("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot connect <\u043d\u0438\u043a> <ip[:port]>");
      } else {
         BotTargetManager localValue2 = BotSpawnCoordinator.internalMethod02098((String)localValue1.get(0), (String)localValue1.get(1));
         if (localValue2 == null) {
            this.internalMethod06477(
               "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0437\u0430\u043f\u0443\u0441\u0442\u0438\u0442\u044c \u0431\u043e\u0442\u0430 "
                  + (String)localValue1.get(0)
            );
         } else {
            this.internalMethod04831(
               "\u0411\u043e\u0442 "
                  + (String)localValue1.get(0)
                  + " \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0430\u0435\u0442\u0441\u044f \u043a "
                  + (String)localValue1.get(1)
            );
         }
      }
   }

   private void internalMethod00092(List<String> localValue1) {
      if (localValue1.size() < 3) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot connectwithport <\u043d\u0438\u043a> <ip> <\u043f\u043e\u0440\u0442>"
         );
      } else {
         Integer localValue2 = this.internalMethod01892((String)localValue1.get(2));
         if (localValue2 != null && localValue2 >= 1 && localValue2 <= 65535) {
            BotTargetManager localValue3 = BotSpawnCoordinator.internalMethod02103((String)localValue1.get(0), (String)localValue1.get(1), localValue2);
            if (localValue3 == null) {
               this.internalMethod06477(
                  "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0437\u0430\u043f\u0443\u0441\u0442\u0438\u0442\u044c \u0431\u043e\u0442\u0430 "
                     + (String)localValue1.get(0)
               );
            } else {
               this.internalMethod04831(
                  "\u0411\u043e\u0442 "
                     + (String)localValue1.get(0)
                     + " \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0430\u0435\u0442\u0441\u044f \u043a "
                     + (String)localValue1.get(1)
                     + ":"
                     + localValue2
               );
            }
         } else {
            this.internalMethod06477("\u041d\u0435\u0432\u0435\u0440\u043d\u044b\u0439 \u043f\u043e\u0440\u0442: " + (String)localValue1.get(2));
         }
      }
   }

   private void internalMethod07953(List<String> localValue1) {
      if (localValue1.isEmpty()) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot start <\u043a\u043e\u043b-\u0432\u043e> <ip[:port]> \u0438\u043b\u0438 .bot start stop|pause|unpause"
         );
      } else {
         String localValue2 = ((String)localValue1.get(0)).toLowerCase(Locale.ROOT);
         switch (localValue2) {
            case "stop":
               BotSpawnCoordinator.internalMethod01149();
               this.internalMethod04831(
                  "\u041e\u0447\u0435\u0440\u0435\u0434\u044c \u0437\u0430\u043f\u0443\u0441\u043a\u0430 \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0430"
               );
               break;
            case "pause":
               this.internalMethod04831(
                  BotSpawnCoordinator.internalMethod01150()
                     ? "\u041e\u0447\u0435\u0440\u0435\u0434\u044c \u0437\u0430\u043f\u0443\u0441\u043a\u0430 \u043f\u043e\u0441\u0442\u0430\u0432\u043b\u0435\u043d\u0430 \u043d\u0430 \u043f\u0430\u0443\u0437\u0443"
                     : "\u041e\u0447\u0435\u0440\u0435\u0434\u044c \u0443\u0436\u0435 \u043d\u0430 \u043f\u0430\u0443\u0437\u0435"
               );
               break;
            case "unpause":
               this.internalMethod04831(
                  BotSpawnCoordinator.internalMethod01152()
                     ? "\u041e\u0447\u0435\u0440\u0435\u0434\u044c \u0437\u0430\u043f\u0443\u0441\u043a\u0430 \u0432\u043e\u0437\u043e\u0431\u043d\u043e\u0432\u043b\u0435\u043d\u0430"
                     : "\u041e\u0447\u0435\u0440\u0435\u0434\u044c \u043d\u0435 \u0431\u044b\u043b\u0430 \u043d\u0430 \u043f\u0430\u0443\u0437\u0435"
               );
               break;
            default:
               if (localValue1.size() < 2) {
                  this.internalMethod06477(
                     "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot start <\u043a\u043e\u043b-\u0432\u043e> <ip[:port]>"
                  );
                  return;
               }

               Integer localValue4 = this.internalMethod01892((String)localValue1.get(0));
               if (localValue4 == null || localValue4 < 1) {
                  this.internalMethod06477(
                     "\u041d\u0435\u0432\u0435\u0440\u043d\u043e\u0435 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u0431\u043e\u0442\u043e\u0432"
                  );
                  return;
               }

               BotSpawnCoordinator.internalMethod03544(localValue4, (String)localValue1.get(1));
               this.internalMethod04831(
                  "\u0417\u0430\u043f\u0443\u0441\u043a\u0430\u044e " + localValue4 + " \u0431\u043e\u0442\u043e\u0432 \u043d\u0430 " + (String)localValue1.get(1)
               );
         }
      }
   }

   private void internalMethod07896(List<String> localValue1) {
      if (localValue1.size() == 1 && ((String)localValue1.get(0)).equalsIgnoreCase("stop")) {
         this.internalMethod04719();
      } else if (localValue1.size() < 3) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot connectfast <\u0444\u0430\u0439\u043b> <ip[:port]> <\u0438\u043d\u0442\u0435\u0440\u0432\u0430\u043b_\u0441\u0435\u043a>"
         );
      } else if (this.internalField0746.get() != null && this.internalField0746.get().isAlive()) {
         this.internalMethod06477(
            "\u041c\u0430\u0441\u0441\u043e\u0432\u044b\u0439 \u0437\u0430\u043f\u0443\u0441\u043a \u0443\u0436\u0435 \u0430\u043a\u0442\u0438\u0432\u0435\u043d. \u0418\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0439\u0442\u0435 .bot connectfast stop"
         );
      } else {
         Path localValue2 = Path.of((String)localValue1.get(0));
         Integer localValue3 = this.internalMethod01892((String)localValue1.get(2));
         if (localValue3 != null && localValue3 >= 1) {
            List localValue4;
            try {
               localValue4 = Files.readAllLines(localValue2, StandardCharsets.UTF_8)
                  .stream()
                  .map(String::trim)
                  .filter(localValue0 -> !localValue0.isEmpty() && !localValue0.startsWith("#"))
                  .toList();
            } catch (Exception localValue6) {
               this.internalMethod06477(
                  "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u0440\u043e\u0447\u0438\u0442\u0430\u0442\u044c \u0444\u0430\u0439\u043b: "
                     + localValue6.getMessage()
               );
               return;
            }

            if (localValue4.isEmpty()) {
               this.internalMethod06477(
                  "\u0424\u0430\u0439\u043b \u043d\u0435 \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u0442 \u043d\u0438\u043a\u0438 \u0431\u043e\u0442\u043e\u0432"
               );
            } else {
               this.internalField0020.set(false);
               Thread localValue5 = new Thread(() -> this.internalMethod04047(localValue4, (String)localValue1.get(1), localValue3), "Rockstar-BotConnectFast");
               localValue5.setDaemon(true);
               this.internalField0746.set(localValue5);
               localValue5.start();
               this.internalMethod04831(
                  "\u0417\u0430\u043f\u0443\u0441\u043a "
                     + localValue4.size()
                     + " \u0431\u043e\u0442\u043e\u0432 \u0441 \u0438\u043d\u0442\u0435\u0440\u0432\u0430\u043b\u043e\u043c "
                     + localValue3
                     + " \u0441\u0435\u043a"
               );
            }
         } else {
            this.internalMethod06477("\u041d\u0435\u0432\u0435\u0440\u043d\u044b\u0439 \u0438\u043d\u0442\u0435\u0440\u0432\u0430\u043b");
         }
      }
   }

   private void internalMethod04047(List<String> localValue1, String localValue2, int localValue3) {
      int localValue4 = 0;

      try {
         for (String localValue6 : localValue1) {
            if (this.internalField0020.get()) {
               this.internalMethod04831(
                  "\u041c\u0430\u0441\u0441\u043e\u0432\u044b\u0439 \u0437\u0430\u043f\u0443\u0441\u043a \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d. \u0417\u0430\u043f\u0443\u0449\u0435\u043d\u043e: "
                     + localValue4
               );
               return;
            }

            BotSpawnCoordinator.internalMethod02098(localValue6, localValue2);
            this.internalMethod04831("\u0417\u0430\u043f\u0443\u0449\u0435\u043d \u0431\u043e\u0442 " + localValue6 + " (" + ++localValue4 + "/" + localValue1.size() + ")");
            Thread.sleep(localValue3 * 1000L);
         }

         this.internalMethod04831(
            "\u0412\u0441\u0435 \u0431\u043e\u0442\u044b \u0438\u0437 \u0444\u0430\u0439\u043b\u0430 \u0437\u0430\u043f\u0443\u0449\u0435\u043d\u044b: " + localValue4
         );
      } catch (InterruptedException localValue10) {
         Thread.currentThread().interrupt();
         this.internalMethod04831(
            "\u041c\u0430\u0441\u0441\u043e\u0432\u044b\u0439 \u0437\u0430\u043f\u0443\u0441\u043a \u043f\u0440\u0435\u0440\u0432\u0430\u043d. \u0417\u0430\u043f\u0443\u0449\u0435\u043d\u043e: "
               + localValue4
         );
      } finally {
         this.internalField0746.set(null);
         this.internalField0020.set(false);
      }
   }

   private void internalMethod04719() {
      Thread localValue1 = this.internalField0746.get();
      if (localValue1 != null && localValue1.isAlive()) {
         this.internalField0020.set(true);
         localValue1.interrupt();
         this.internalMethod04831(
            "\u041e\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0430 \u043c\u0430\u0441\u0441\u043e\u0432\u043e\u0433\u043e \u0437\u0430\u043f\u0443\u0441\u043a\u0430..."
         );
      } else {
         this.internalMethod06477(
            "\u041c\u0430\u0441\u0441\u043e\u0432\u044b\u0439 \u0437\u0430\u043f\u0443\u0441\u043a \u043d\u0435 \u0430\u043a\u0442\u0438\u0432\u0435\u043d"
         );
      }
   }

   private void internalMethod05053(BotRosterManager localValue1, List<String> localValue2, String localValue3) {
      if (localValue2.isEmpty()) {
         localValue1.internalMethod01823(BotTargetManager::internalMethod09732);
         this.internalMethod04831(LanguageManager.internalMethod07214("commands.bot.action.stop"));
      } else {
         String localValue4 = (String)localValue2.get(0);
         Optional localValue5 = localValue1.internalMethod05101(localValue4);
         if (localValue5.isEmpty()) {
            this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue4));
         } else {
            localValue1.internalMethod00543(localValue4);
            this.internalMethod04831(
               localValue3.equals("stop")
                  ? "\u0411\u043e\u0442 " + localValue4 + " \u0431\u044b\u043b \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d"
                  : LanguageManager.internalMethod00160("commands.bot.left", localValue4)
            );
         }
      }
   }

   private void internalMethod02694(BotRosterManager localValue1) {
      int localValue2 = localValue1.internalMethod02921();
      localValue1.internalMethod02922();
      this.internalMethod04831(LanguageManager.internalMethod00160("commands.bot.cleared", localValue2));
   }

   private void internalMethod03363(BotRosterManager localValue1) {
      Collection localValue2 = localValue1.internalMethod01257();
      if (localValue2.isEmpty()) {
         this.internalMethod04831(LanguageManager.internalMethod07214("commands.bot.no_bots"));
      } else {
         ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.bot.list_header", localValue2.size())));

         for (BotTargetManager localValue4 : (Iterable<BotTargetManager>)(Iterable<?>)localValue2) {
            String localValue5 = localValue4.internalMethod07697()
               ? "\u00a7a" + LanguageManager.internalMethod07214("commands.bot.status.connected")
               : "\u00a7c" + localValue4.internalMethod04051().name();
            String localValue6 = localValue4.internalMethod06738() == null ? "Idle" : localValue4.internalMethod06738().internalMethod06553();
            String localValue7 = localValue1.internalMethod09048(localValue4.internalMethod03426()) ? " \u00a7a[TapeMouse]" : "";
            ClientMessages.internalMethod01809(Text.of("\u00a77- \u00a7f" + localValue4.internalMethod03426() + " \u00a77[" + localValue5 + "\u00a77] \u00a78" + localValue6 + localValue7));
         }
      }
   }

   private void internalMethod08635(BotRosterManager localValue1) {
      localValue1.internalMethod01823(BotTargetManager::internalMethod07692);
      this.internalMethod04831(LanguageManager.internalMethod07214("commands.bot.action.jump"));
   }

   private void internalMethod08780(BotRosterManager localValue1) {
      localValue1.internalMethod01823(BotTargetManager::internalMethod07730);
      this.internalMethod04831(LanguageManager.internalMethod07214("commands.bot.action.sneak"));
   }

   private void internalMethod05739(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.isEmpty()) {
         localValue1.internalMethod01823(BotTargetManager::internalMethod07733);
         this.internalMethod04831(LanguageManager.internalMethod07214("commands.bot.action.attack"));
      } else {
         String localValue3 = (String)localValue2.get(0);
         if (localValue3.equalsIgnoreCase("stopall")) {
            localValue1.internalMethod01823(localValue0 -> localValue0.internalMethod05456(new VelocityDecayBehavior()));
            this.internalMethod04831(
               "\u0410\u0442\u0430\u043a\u0430 \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0430 \u0443 \u0432\u0441\u0435\u0445 \u0431\u043e\u0442\u043e\u0432"
            );
         } else if (localValue3.equalsIgnoreCase("stop") && localValue2.size() >= 2) {
            localValue1.internalMethod05101((String)localValue2.get(1)).ifPresent(localValue0 -> localValue0.internalMethod05456(new VelocityDecayBehavior()));
            this.internalMethod04831(
               "\u0410\u0442\u0430\u043a\u0430 \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0430 \u0443 " + (String)localValue2.get(1)
            );
         } else if (localValue2.size() < 2) {
            this.internalMethod06477(
               "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot attack <\u0431\u043e\u0442|all|stop|stopall> <\u0446\u0435\u043b\u044c>"
            );
         } else {
            String localValue4 = (String)localValue2.get(1);
            if (localValue3.equalsIgnoreCase("all")) {
               localValue1.internalMethod01823(localValue1x -> localValue1x.internalMethod05456(new AttackPlayerTask(localValue4, localValue1x.internalMethod06687().internalMethod09843())));
               this.internalMethod04831("\u0412\u0441\u0435 \u0431\u043e\u0442\u044b \u0430\u0442\u0430\u043a\u0443\u044e\u0442 " + localValue4);
            } else {
               Optional localValue5 = localValue1.internalMethod05101(localValue3);
               if (localValue5.isEmpty()) {
                  this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue3));
               } else {
                  ((BotTargetManager)localValue5.get())
                     .internalMethod05456(new AttackPlayerTask(localValue4, ((BotTargetManager)localValue5.get()).internalMethod06687().internalMethod09843()));
                  this.internalMethod04831("\u0411\u043e\u0442 " + localValue3 + " \u0430\u0442\u0430\u043a\u0443\u0435\u0442 " + localValue4);
               }
            }
         }
      }
   }

   private void internalMethod08047(BotRosterManager localValue1) {
      localValue1.internalMethod01823(BotTargetManager::internalMethod09725);
      this.internalMethod04831(LanguageManager.internalMethod07214("commands.bot.action.use"));
   }

   private void internalMethod08172(BotRosterManager localValue1) {
      localValue1.internalMethod01823(localValue0 -> localValue0.internalMethod08814(false));
      this.internalMethod04831(LanguageManager.internalMethod07214("commands.bot.action.drop"));
   }

   private void internalMethod08797(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.isEmpty()) {
         this.internalMethod06477(LanguageManager.internalMethod07214("commands.bot.slot_required"));
      } else {
         Integer localValue3 = this.internalMethod01892((String)localValue2.get(0));
         if (localValue3 != null && localValue3 >= 0 && localValue3 <= 8) {
            localValue1.internalMethod01823(localValue1x -> localValue1x.internalMethod00338(localValue3));
            this.internalMethod04831(LanguageManager.internalMethod00160("commands.bot.action.slot", localValue3));
         } else {
            this.internalMethod06477(LanguageManager.internalMethod07214("commands.bot.invalid_slot"));
         }
      }
   }

   private void internalMethod09081(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.size() == 3) {
         this.internalMethod05264(localValue1, (String)localValue2.get(0), (String)localValue2.get(1), (String)localValue2.get(2));
      } else if (localValue2.size() < 4) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot goto <\u0431\u043e\u0442|all> <x> <y> <z>"
         );
      } else {
         String localValue3 = (String)localValue2.get(0);
         Integer localValue4 = this.internalMethod01892((String)localValue2.get(1));
         Integer localValue5 = this.internalMethod01892((String)localValue2.get(2));
         Integer localValue6 = this.internalMethod01892((String)localValue2.get(3));
         if (localValue4 == null || localValue5 == null || localValue6 == null) {
            this.internalMethod06477(LanguageManager.internalMethod07214("commands.bot.invalid_coords"));
         } else if (localValue3.equalsIgnoreCase("all")) {
            localValue1.internalMethod01823(localValue3x -> localValue3x.internalMethod05456(new GotoPositionTask(localValue4.intValue(), localValue5.intValue(), localValue6.intValue())));
            this.internalMethod04831(LanguageManager.internalMethod00160("commands.bot.action.goto", localValue4, localValue5, localValue6));
         } else {
            Optional localValue7 = localValue1.internalMethod05101(localValue3);
            if (localValue7.isEmpty()) {
               this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue3));
            } else {
               ((BotTargetManager)localValue7.get()).internalMethod05456(new GotoPositionTask(localValue4.intValue(), localValue5.intValue(), localValue6.intValue()));
               this.internalMethod04831("\u0411\u043e\u0442 " + localValue3 + " \u0438\u0434\u0451\u0442 \u043a " + localValue4 + ", " + localValue5 + ", " + localValue6);
            }
         }
      }
   }

   private void internalMethod05264(BotRosterManager localValue1, String localValue2, String localValue3, String localValue4) {
      Integer localValue5 = this.internalMethod01892(localValue2);
      Integer localValue6 = this.internalMethod01892(localValue3);
      Integer localValue7 = this.internalMethod01892(localValue4);
      if (localValue5 != null && localValue6 != null && localValue7 != null) {
         localValue1.internalMethod01823(localValue3x -> localValue3x.internalMethod05456(new GotoPositionTask(localValue5.intValue(), localValue6.intValue(), localValue7.intValue())));
         this.internalMethod04831(LanguageManager.internalMethod00160("commands.bot.action.goto", localValue5, localValue6, localValue7));
      } else {
         this.internalMethod06477(LanguageManager.internalMethod07214("commands.bot.invalid_coords"));
      }
   }

   private void internalMethod08302(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.isEmpty()) {
         if (internalField0149.player != null) {
            localValue1.internalMethod01823(localValue0 -> localValue0.internalMethod05456(new FollowEntityTask(internalField0149.player, localValue0.internalMethod06687().internalMethod04127())));
            this.internalMethod04831(LanguageManager.internalMethod07214("commands.bot.action.follow"));
         }
      } else {
         String localValue3 = (String)localValue2.get(0);
         if (localValue3.equalsIgnoreCase("stopall")) {
            localValue1.internalMethod01823(localValue0 -> localValue0.internalMethod05456(new VelocityDecayBehavior()));
            this.internalMethod04831(
               "\u0421\u043b\u0435\u0434\u043e\u0432\u0430\u043d\u0438\u0435 \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u043e \u0443 \u0432\u0441\u0435\u0445 \u0431\u043e\u0442\u043e\u0432"
            );
         } else if (localValue3.equalsIgnoreCase("stop") && localValue2.size() >= 2) {
            localValue1.internalMethod05101((String)localValue2.get(1)).ifPresent(localValue0 -> localValue0.internalMethod05456(new VelocityDecayBehavior()));
            this.internalMethod04831(
               "\u0421\u043b\u0435\u0434\u043e\u0432\u0430\u043d\u0438\u0435 \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u043e \u0443 "
                  + (String)localValue2.get(1)
            );
         } else if (localValue2.size() < 2) {
            this.internalMethod06477(
               "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot follow <\u0431\u043e\u0442|all|stop|stopall> <\u0446\u0435\u043b\u044c>"
            );
         } else {
            String localValue4 = (String)localValue2.get(1);
            if (localValue3.equalsIgnoreCase("all")) {
               localValue1.internalMethod01823(localValue1x -> localValue1x.internalMethod05456(new FollowEntityTask(localValue4, localValue1x.internalMethod06687().internalMethod04127())));
               this.internalMethod04831("\u0412\u0441\u0435 \u0431\u043e\u0442\u044b \u0441\u043b\u0435\u0434\u0443\u044e\u0442 \u0437\u0430 " + localValue4);
            } else {
               Optional localValue5 = localValue1.internalMethod05101(localValue3);
               if (localValue5.isEmpty()) {
                  this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue3));
               } else {
                  ((BotTargetManager)localValue5.get())
                     .internalMethod05456(new FollowEntityTask(localValue4, ((BotTargetManager)localValue5.get()).internalMethod06687().internalMethod04127()));
                  this.internalMethod04831("\u0411\u043e\u0442 " + localValue3 + " \u0441\u043b\u0435\u0434\u0443\u0435\u0442 \u0437\u0430 " + localValue4);
               }
            }
         }
      }
   }

   private void internalMethod08572(BotRosterManager localValue1, List<String> localValue2) {
      if (!localValue2.isEmpty() && !((String)localValue2.get(0)).equalsIgnoreCase("all")) {
         Optional localValue3 = localValue1.internalMethod05101((String)localValue2.get(0));
         if (localValue3.isEmpty()) {
            this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue2.get(0)));
         } else {
            ((BotTargetManager)localValue3.get()).internalMethod05456(new VelocityDecayBehavior());
            this.internalMethod04831("\u0411\u043e\u0442 " + (String)localValue2.get(0) + " \u043f\u0435\u0440\u0435\u0432\u0435\u0434\u0451\u043d \u0432 idle");
         }
      } else {
         localValue1.internalMethod01823(localValue0 -> localValue0.internalMethod05456(new VelocityDecayBehavior()));
         this.internalMethod04831("\u0412\u0441\u0435 \u0431\u043e\u0442\u044b \u043f\u0435\u0440\u0435\u0432\u0435\u0434\u0435\u043d\u044b \u0432 idle");
      }
   }

   private void internalMethod09487(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.isEmpty()) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot mining <\u0431\u043e\u0442|all|stop|stopall> <minecraft:block>"
         );
      } else {
         String localValue3 = (String)localValue2.get(0);
         if (localValue3.equalsIgnoreCase("stopall")) {
            localValue1.internalMethod01823(localValue0 -> localValue0.internalMethod05456(new VelocityDecayBehavior()));
            this.internalMethod04831(
               "Mining \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u0443 \u0432\u0441\u0435\u0445 \u0431\u043e\u0442\u043e\u0432"
            );
         } else if (localValue3.equalsIgnoreCase("stop") && localValue2.size() >= 2) {
            localValue1.internalMethod05101((String)localValue2.get(1)).ifPresent(localValue0 -> localValue0.internalMethod05456(new VelocityDecayBehavior()));
            this.internalMethod04831("Mining \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u0443 " + (String)localValue2.get(1));
         } else if (localValue2.size() < 2) {
            this.internalMethod06477(
               "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot mining <\u0431\u043e\u0442|all|stop|stopall> <minecraft:block>"
            );
         } else {
            Block localValue4 = this.internalMethod01568((String)localValue2.get(1));
            if (localValue4 == null || localValue4 == Blocks.AIR) {
               this.internalMethod06477("\u0411\u043b\u043e\u043a " + (String)localValue2.get(1) + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d");
            } else if (localValue3.equalsIgnoreCase("all")) {
               localValue1.internalMethod01823(localValue2x -> localValue2x.internalMethod05456(this.internalMethod02974(localValue4)));
               this.internalMethod04831(
                  "\u0412\u0441\u0435 \u0431\u043e\u0442\u044b \u0434\u043e\u0431\u044b\u0432\u0430\u044e\u0442 " + Registries.BLOCK.getId(localValue4)
               );
            } else {
               Optional localValue5 = localValue1.internalMethod05101(localValue3);
               if (localValue5.isEmpty()) {
                  this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue3));
               } else {
                  ((BotTargetManager)localValue5.get()).internalMethod05456(this.internalMethod02974(localValue4));
                  this.internalMethod04831("\u0411\u043e\u0442 " + localValue3 + " \u0434\u043e\u0431\u044b\u0432\u0430\u0435\u0442 " + Registries.BLOCK.getId(localValue4));
               }
            }
         }
      }
   }

   private BlockMinerTask internalMethod02974(Block localValue1) {
      BlockMinerTask localValue2 = new BlockMinerTask();
      localValue2.internalMethod05516(localValue1);
      return localValue2;
   }

   private Block internalMethod01568(String localValue1) {
      String localValue2 = localValue1 == null ? "" : localValue1.trim().toLowerCase(Locale.ROOT);
      if (localValue2.isBlank()) {
         return null;
      } else {
         if (!localValue2.contains(":")) {
            localValue2 = "minecraft:" + localValue2;
         }

         Identifier localValue3 = Identifier.tryParse(localValue2);
         return localValue3 != null && Registries.BLOCK.containsId(localValue3) ? (Block)Registries.BLOCK.get(localValue3) : null;
      }
   }

   private void internalMethod09620(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.size() < 2) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot tapemouse add|remove <\u0431\u043e\u0442>"
         );
      } else {
         String localValue3 = ((String)localValue2.get(0)).toLowerCase(Locale.ROOT);
         String localValue4 = (String)localValue2.get(1);
         switch (localValue3) {
            case "add":
               if (localValue1.internalMethod07133(localValue4)) {
                  this.internalMethod04831("\u0411\u043e\u0442 " + localValue4 + " \u0434\u043e\u0431\u0430\u0432\u043b\u0435\u043d \u0432 TapeMouse");
               } else {
                  this.internalMethod06477(
                     "\u0411\u043e\u0442 "
                        + localValue4
                        + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d, \u043d\u0435 \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d \u0438\u043b\u0438 \u0443\u0436\u0435 \u0432 TapeMouse"
                  );
               }
               break;
            case "remove":
               if (localValue1.internalMethod07761(localValue4)) {
                  this.internalMethod04831("\u0411\u043e\u0442 " + localValue4 + " \u0443\u0434\u0430\u043b\u0451\u043d \u0438\u0437 TapeMouse");
               } else {
                  this.internalMethod06477("\u0411\u043e\u0442 " + localValue4 + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d \u0432 TapeMouse");
               }
               break;
            default:
               this.internalMethod06477(
                  "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot tapemouse add|remove <\u0431\u043e\u0442>"
               );
         }
      }
   }

   private void internalMethod09211(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.isEmpty()) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot rebreak <\u0431\u043e\u0442|all|stop|stopall>"
         );
      } else {
         String localValue3 = (String)localValue2.get(0);
         if (localValue3.equalsIgnoreCase("stopall")) {
            localValue1.internalMethod01823(localValue1x -> {
               if (this.internalMethod00306(localValue1x)) {
                  localValue1x.internalMethod05456(new VelocityDecayBehavior());
               }
            });
            this.internalMethod04831(
               "Rebreak \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u0443 \u0432\u0441\u0435\u0445 \u0431\u043e\u0442\u043e\u0432"
            );
         } else if (localValue3.equalsIgnoreCase("stop") && localValue2.size() >= 2) {
            localValue1.internalMethod05101((String)localValue2.get(1)).ifPresent(localValue1x -> {
               if (this.internalMethod00306(localValue1x)) {
                  localValue1x.internalMethod05456(new VelocityDecayBehavior());
               }
            });
            this.internalMethod04831("Rebreak \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u0443 " + (String)localValue2.get(1));
         } else if (localValue3.equalsIgnoreCase("all")) {
            localValue1.internalMethod01823(localValue0 -> localValue0.internalMethod05456(new CyclicRebreakTask()));
            this.internalMethod04831("Rebreak \u0432\u043a\u043b\u044e\u0447\u0435\u043d \u0443 \u0432\u0441\u0435\u0445 \u0431\u043e\u0442\u043e\u0432");
         } else {
            Optional localValue4 = localValue1.internalMethod05101(localValue3);
            if (localValue4.isEmpty()) {
               this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue3));
            } else {
               ((BotTargetManager)localValue4.get()).internalMethod05456(new CyclicRebreakTask());
               this.internalMethod04831("Rebreak \u0432\u043a\u043b\u044e\u0447\u0435\u043d \u0443 " + localValue3);
            }
         }
      }
   }

   private void internalMethod09353(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.isEmpty()) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot instantrebreak <\u0431\u043e\u0442|all|stop|stopall>"
         );
      } else {
         String localValue3 = (String)localValue2.get(0);
         if (localValue3.equalsIgnoreCase("stopall")) {
            localValue1.internalMethod01823(localValue0 -> {
               if (localValue0.internalMethod06738() instanceof InstantRebreakTask) {
                  localValue0.internalMethod05456(new VelocityDecayBehavior());
               }
            });
            this.internalMethod04831(
               "InstantRebreak \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u0443 \u0432\u0441\u0435\u0445 \u0431\u043e\u0442\u043e\u0432"
            );
         } else if (localValue3.equalsIgnoreCase("stop") && localValue2.size() >= 2) {
            localValue1.internalMethod05101((String)localValue2.get(1)).ifPresent(localValue0 -> {
               if (localValue0.internalMethod06738() instanceof InstantRebreakTask) {
                  localValue0.internalMethod05456(new VelocityDecayBehavior());
               }
            });
            this.internalMethod04831("InstantRebreak \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u0443 " + (String)localValue2.get(1));
         } else if (localValue3.equalsIgnoreCase("all")) {
            localValue1.internalMethod01823(localValue0 -> localValue0.internalMethod05456(new InstantRebreakTask()));
            this.internalMethod04831("InstantRebreak \u0432\u043a\u043b\u044e\u0447\u0435\u043d \u0443 \u0432\u0441\u0435\u0445 \u0431\u043e\u0442\u043e\u0432");
         } else {
            Optional localValue4 = localValue1.internalMethod05101(localValue3);
            if (localValue4.isEmpty()) {
               this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue3));
            } else {
               ((BotTargetManager)localValue4.get()).internalMethod05456(new InstantRebreakTask());
               this.internalMethod04831("InstantRebreak \u0432\u043a\u043b\u044e\u0447\u0435\u043d \u0443 " + localValue3);
            }
         }
      }
   }

   private void internalMethod09642(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.isEmpty()) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot rebreakrejoin <\u0431\u043e\u0442|all|stop|stopall> <\u043d\u043e\u043c\u0435\u0440_\u0433\u0440\u0438\u0444\u0430>"
         );
      } else {
         String localValue3 = (String)localValue2.get(0);
         if (localValue3.equalsIgnoreCase("stopall")) {
            localValue1.internalMethod01823(localValue0 -> {
               if (localValue0.internalMethod06738() instanceof CyclicRebreakRejoinBehavior) {
                  localValue0.internalMethod05456(new VelocityDecayBehavior());
               }
            });
            this.internalMethod04831(
               "RebreakRejoin \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u0443 \u0432\u0441\u0435\u0445 \u0431\u043e\u0442\u043e\u0432"
            );
         } else if (localValue3.equalsIgnoreCase("stop") && localValue2.size() >= 2) {
            localValue1.internalMethod05101((String)localValue2.get(1)).ifPresent(localValue0 -> {
               if (localValue0.internalMethod06738() instanceof CyclicRebreakRejoinBehavior) {
                  localValue0.internalMethod05456(new VelocityDecayBehavior());
               }
            });
            this.internalMethod04831("RebreakRejoin \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u0443 " + (String)localValue2.get(1));
         } else if (localValue2.size() < 2) {
            this.internalMethod06477(
               "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot rebreakrejoin <\u0431\u043e\u0442|all|stop|stopall> <\u043d\u043e\u043c\u0435\u0440_\u0433\u0440\u0438\u0444\u0430>"
            );
         } else {
            Integer localValue4 = this.internalMethod01892((String)localValue2.get(1));
            if (localValue4 == null || localValue4 < 1) {
               this.internalMethod06477("\u041d\u0435\u0432\u0435\u0440\u043d\u044b\u0439 \u043d\u043e\u043c\u0435\u0440 \u0433\u0440\u0438\u0444\u0430");
            } else if (localValue3.equalsIgnoreCase("all")) {
               localValue1.internalMethod01823(localValue1x -> localValue1x.internalMethod05456(new CyclicRebreakRejoinBehavior(localValue4)));
               this.internalMethod04831(
                  "RebreakRejoin \u0432\u043a\u043b\u044e\u0447\u0435\u043d \u0443 \u0432\u0441\u0435\u0445 \u0431\u043e\u0442\u043e\u0432, \u0433\u0440\u0438\u0444 #"
                     + localValue4
               );
            } else {
               Optional localValue5 = localValue1.internalMethod05101(localValue3);
               if (localValue5.isEmpty()) {
                  this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue3));
               } else {
                  ((BotTargetManager)localValue5.get()).internalMethod05456(new CyclicRebreakRejoinBehavior(localValue4));
                  this.internalMethod04831("RebreakRejoin \u0432\u043a\u043b\u044e\u0447\u0435\u043d \u0443 " + localValue3 + ", \u0433\u0440\u0438\u0444 #" + localValue4);
               }
            }
         }
      }
   }

   private boolean internalMethod00306(BotTargetManager localValue1) {
      return localValue1 != null
         && (
            localValue1.internalMethod06738() instanceof CyclicRebreakTask
               || localValue1.internalMethod06738() instanceof CyclicRebreakRejoinBehavior
               || localValue1.internalMethod06738() instanceof InstantRebreakTask
         );
   }

   private void internalMethod09795(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.isEmpty()) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot autojoingrief <\u0431\u043e\u0442|all|stop|stopall> <\u043d\u043e\u043c\u0435\u0440_\u0433\u0440\u0438\u0444\u0430> [\u0438\u043d\u0442\u0435\u0440\u0432\u0430\u043b_\u0441\u0435\u043a]"
         );
      } else {
         String localValue3 = (String)localValue2.get(0);
         if (localValue3.equalsIgnoreCase("stopall")) {
            localValue1.internalMethod01823(localValue0 -> {
               if (localValue0.internalMethod06738() instanceof AutoJoinGriefTask) {
                  localValue0.internalMethod05456(new VelocityDecayBehavior());
               }
            });
            this.internalMethod04831(
               "AutoJoinGrief \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u0443 \u0432\u0441\u0435\u0445 \u0431\u043e\u0442\u043e\u0432"
            );
         } else if (localValue3.equalsIgnoreCase("stop") && localValue2.size() >= 2) {
            localValue1.internalMethod05101((String)localValue2.get(1)).ifPresent(localValue0 -> {
               if (localValue0.internalMethod06738() instanceof AutoJoinGriefTask) {
                  localValue0.internalMethod05456(new VelocityDecayBehavior());
               }
            });
            this.internalMethod04831("AutoJoinGrief \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u0443 " + (String)localValue2.get(1));
         } else if (localValue2.size() < 2) {
            this.internalMethod06477(
               "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot autojoingrief <\u0431\u043e\u0442|all|stop|stopall> <\u043d\u043e\u043c\u0435\u0440_\u0433\u0440\u0438\u0444\u0430> [\u0438\u043d\u0442\u0435\u0440\u0432\u0430\u043b_\u0441\u0435\u043a]"
            );
         } else {
            Integer localValue4 = this.internalMethod01892((String)localValue2.get(1));
            Integer localValue5 = localValue2.size() >= 3 ? this.internalMethod01892((String)localValue2.get(2)) : 1;
            if (localValue4 != null && localValue4 >= 1 && localValue5 != null && localValue5 >= 1) {
               if (localValue3.equalsIgnoreCase("all")) {
                  int localValue11 = 0;
                  long localValue7 = Math.max(250L, Math.min(2000L, localValue5.intValue() * 250L));

                  for (BotTargetManager localValue10 : localValue1.internalMethod01257()) {
                     if (localValue10.internalMethod07697()) {
                        localValue10.internalMethod05456(new AutoJoinGriefTask(localValue4, localValue5, localValue11 * localValue7));
                        localValue11++;
                     }
                  }

                  this.internalMethod04831(
                     "AutoJoinGrief \u0432\u043a\u043b\u044e\u0447\u0435\u043d \u0443 "
                        + localValue11
                        + " \u0431\u043e\u0442\u043e\u0432, \u0433\u0440\u0438\u0444 #"
                        + localValue4
                  );
               } else {
                  Optional localValue6 = localValue1.internalMethod05101(localValue3);
                  if (localValue6.isEmpty()) {
                     this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue3));
                  } else {
                     ((BotTargetManager)localValue6.get()).internalMethod05456(new AutoJoinGriefTask(localValue4, localValue5));
                     this.internalMethod04831("AutoJoinGrief \u0432\u043a\u043b\u044e\u0447\u0435\u043d \u0443 " + localValue3 + ", \u0433\u0440\u0438\u0444 #" + localValue4);
                  }
               }
            } else {
               this.internalMethod06477(
                  "\u041d\u0435\u0432\u0435\u0440\u043d\u044b\u0439 \u043d\u043e\u043c\u0435\u0440 \u0433\u0440\u0438\u0444\u0430 \u0438\u043b\u0438 \u0438\u043d\u0442\u0435\u0440\u0432\u0430\u043b"
               );
            }
         }
      }
   }

   private void internalMethod09393(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.size() < 2) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot patrol add|clear|start <\u0431\u043e\u0442>"
         );
      } else if (internalField0149.player != null) {
         String localValue3 = ((String)localValue2.get(0)).toLowerCase(Locale.ROOT);
         String localValue4 = (String)localValue2.get(1);
         Optional localValue5 = localValue1.internalMethod05101(localValue4);
         if (localValue5.isEmpty()) {
            this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue4));
         } else {
            PatrolRouteTask localValue6 = this.internalField0543.computeIfAbsent(localValue4.toLowerCase(Locale.ROOT), localValue0 -> new PatrolRouteTask());
            switch (localValue3) {
               case "add":
                  localValue6.internalMethod05146(internalField0149.player.getEntityPos());
                  this.internalMethod04831(
                     "\u0422\u043e\u0447\u043a\u0430 \u043f\u0430\u0442\u0440\u0443\u043b\u044f \u0434\u043e\u0431\u0430\u0432\u043b\u0435\u043d\u0430 \u0434\u043b\u044f "
                        + localValue4
                  );
                  break;
               case "clear":
                  localValue6.internalMethod05642();
                  this.internalMethod04831(
                     "\u0422\u043e\u0447\u043a\u0438 \u043f\u0430\u0442\u0440\u0443\u043b\u044f \u043e\u0447\u0438\u0449\u0435\u043d\u044b \u0434\u043b\u044f "
                        + localValue4
                  );
                  break;
               case "start":
                  if (localValue6.internalMethod05643()) {
                     this.internalMethod06477(
                        "\u0423 \u0431\u043e\u0442\u0430 "
                           + localValue4
                           + " \u043d\u0435\u0442 \u0442\u043e\u0447\u0435\u043a \u043f\u0430\u0442\u0440\u0443\u043b\u044f"
                     );
                     return;
                  }

                  ((BotTargetManager)localValue5.get()).internalMethod05456(localValue6);
                  this.internalMethod04831("\u0411\u043e\u0442 " + localValue4 + " \u043d\u0430\u0447\u0430\u043b \u043f\u0430\u0442\u0440\u0443\u043b\u044c");
                  break;
               default:
                  this.internalMethod06477(
                     "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot patrol add|clear|start <\u0431\u043e\u0442>"
                  );
            }
         }
      }
   }

   private void internalMethod09594(BotRosterManager localValue1) {
      if (internalField0149.player != null) {
         Vec3d localValue2 = internalField0149.player.getEntityPos();
         localValue1.internalMethod01823(localValue1x -> localValue1x.internalMethod02198(localValue2.x, localValue2.y, localValue2.z));
         this.internalMethod04831(LanguageManager.internalMethod07214("commands.bot.action.tp"));
      }
   }

   private void internalMethod09569(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.isEmpty()) {
         this.internalMethod06477(LanguageManager.internalMethod07214("commands.bot.nickname_required"));
      } else {
         Optional localValue3 = localValue1.internalMethod05101((String)localValue2.get(0));
         if (localValue3.isEmpty()) {
            this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue2.get(0)));
         } else {
            BotTargetManager localValue4 = (BotTargetManager)localValue3.get();
            ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.bot.info.header", localValue4.internalMethod03426())));
            ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.bot.info.status", localValue4.internalMethod04051().name())));
            ClientMessages.internalMethod01809(
               Text.of("Behavior: " + (localValue4.internalMethod06738() == null ? "Idle" : localValue4.internalMethod06738().internalMethod06553()))
            );
            ClientMessages.internalMethod01809(
               Text.of(
                  "World: chunks="
                     + localValue4.internalMethod00273().internalMethod08534().size()
                     + ", entities="
                     + localValue4.internalMethod00273().internalMethod02136().size()
                     + ", players="
                     + localValue4.internalMethod00273().internalMethod06766().size()
                     + ", blocks="
                     + localValue4.internalMethod00273().internalMethod04191()
               )
            );
            ClientMessages.internalMethod01809(
               Text.of(LanguageManager.internalMethod00160("commands.bot.info.health", localValue4.internalMethod06688().internalMethod08591()))
            );
            ClientMessages.internalMethod01809(
               Text.of(LanguageManager.internalMethod00160("commands.bot.info.food", localValue4.internalMethod06688().internalMethod02809()))
            );
            Vec3d localValue5 = localValue4.internalMethod06688().internalMethod06297();
            ClientMessages.internalMethod01809(
               Text.of(
                  LanguageManager.internalMethod00160(
                     "commands.bot.info.position", String.format("%.1f", localValue5.x), String.format("%.1f", localValue5.y), String.format("%.1f", localValue5.z)
                  )
               )
            );
         }
      }
   }

   private void internalMethod10130(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.isEmpty()) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot chat [\u043d\u0438\u043a|all] <\u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0435>"
         );
      } else {
         String localValue3 = (String)localValue2.get(0);
         Optional localValue4 = localValue1.internalMethod05101(localValue3);
         if (localValue3.equalsIgnoreCase("all") && localValue2.size() >= 2) {
            String localValue8 = String.join(" ", localValue2.subList(1, localValue2.size()));
            localValue1.internalMethod01823(localValue1x -> localValue1x.internalMethod04382(localValue8));
            this.internalMethod04831(
               "\u0421\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0435 \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u043e \u043e\u0442 \u0432\u0441\u0435\u0445 \u0431\u043e\u0442\u043e\u0432"
            );
         } else if (localValue4.isPresent() && localValue2.size() >= 2) {
            String localValue7 = String.join(" ", localValue2.subList(1, localValue2.size()));
            ((BotTargetManager)localValue4.get()).internalMethod04382(localValue7);
            this.internalMethod04831(LanguageManager.internalMethod00160("commands.bot.action.say", localValue3));
         } else {
            String localValue5 = String.join(" ", localValue2);
            Optional localValue6 = localValue1.internalMethod05204();
            if (localValue6.isPresent() && ((BotTargetManager)localValue6.get()).internalMethod07697()) {
               ((BotTargetManager)localValue6.get()).internalMethod04382(localValue5);
               this.internalMethod04831(
                  "\u0421\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0435 \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u043e \u043e\u0442 \u0443\u043f\u0440\u0430\u0432\u043b\u044f\u0435\u043c\u043e\u0433\u043e \u0431\u043e\u0442\u0430 "
                     + ((BotTargetManager)localValue6.get()).internalMethod03426()
               );
            } else {
               localValue1.internalMethod01823(localValue1x -> localValue1x.internalMethod04382(localValue5));
               this.internalMethod04831(
                  "\u0421\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0435 \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u043e \u043e\u0442 \u0432\u0441\u0435\u0445 \u0431\u043e\u0442\u043e\u0432"
               );
            }
         }
      }
   }

   private void internalMethod09955(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.size() < 2) {
         this.internalMethod06477(LanguageManager.internalMethod07214("commands.bot.cmd_usage"));
      } else {
         String localValue3 = (String)localValue2.get(0);
         String localValue4 = String.join(" ", localValue2.subList(1, localValue2.size()));
         if (localValue3.equalsIgnoreCase("all")) {
            localValue1.internalMethod01823(localValue1x -> localValue1x.internalMethod02827(localValue4.startsWith("/") ? localValue4.substring(1) : localValue4));
            this.internalMethod04831(
               "\u041a\u043e\u043c\u0430\u043d\u0434\u0430 \u0432\u044b\u043f\u043e\u043b\u043d\u0435\u043d\u0430 \u043e\u0442 \u0432\u0441\u0435\u0445 \u0431\u043e\u0442\u043e\u0432"
            );
         } else {
            Optional localValue5 = localValue1.internalMethod05101(localValue3);
            if (localValue5.isEmpty()) {
               this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue3));
            } else {
               ((BotTargetManager)localValue5.get()).internalMethod02827(localValue4.startsWith("/") ? localValue4.substring(1) : localValue4);
               this.internalMethod04831(LanguageManager.internalMethod00160("commands.bot.action.cmd", localValue3));
            }
         }
      }
   }

   private void internalMethod10050(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.isEmpty()) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot password <\u043f\u0430\u0440\u043e\u043b\u044c>"
         );
      } else {
         localValue1.internalMethod07760((String)localValue2.get(0));
         this.internalMethod04831(
            "\u041f\u0430\u0440\u043e\u043b\u044c \u0434\u043b\u044f \u0431\u043e\u0442\u043e\u0432 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d: "
               + (String)localValue2.get(0)
         );
      }
   }

   private void internalMethod10102(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.size() < 2) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot pay <\u043d\u0438\u043a|all> <\u043a\u043e\u043c\u0443>"
         );
      } else {
         String localValue3 = (String)localValue2.get(0);
         String localValue4 = (String)localValue2.get(1);
         if (localValue3.equalsIgnoreCase("all")) {
            localValue1.internalMethod01823(localValue2x -> {
               localValue1.internalMethod03661(localValue2x.internalMethod03426(), localValue4);
               localValue2x.internalMethod04382("/balance");
            });
            this.internalMethod04831(
               "\u0417\u0430\u043f\u0440\u043e\u0441 \u0431\u0430\u043b\u0430\u043d\u0441\u0430 \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d \u0432\u0441\u0435\u043c \u0431\u043e\u0442\u0430\u043c"
            );
         } else {
            Optional localValue5 = localValue1.internalMethod05101(localValue3);
            if (localValue5.isEmpty()) {
               this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue3));
            } else {
               localValue1.internalMethod03661(((BotTargetManager)localValue5.get()).internalMethod03426(), localValue4);
               ((BotTargetManager)localValue5.get()).internalMethod04382("/balance");
               this.internalMethod04831(
                  "\u0417\u0430\u043f\u0440\u043e\u0441 \u0431\u0430\u043b\u0430\u043d\u0441\u0430 \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d \u0431\u043e\u0442\u0443 "
                     + localValue3
               );
            }
         }
      }
   }

   private void internalMethod09963(BotRosterManager localValue1, List<String> localValue2) {
      if (!localValue2.isEmpty() && !((String)localValue2.get(0)).equalsIgnoreCase("status")) {
         String localValue3 = ((String)localValue2.get(0)).toLowerCase(Locale.ROOT);
         switch (localValue3) {
            case "enable":
               localValue1.internalMethod00445(true);
               localValue1.internalMethod00497(true);
               this.internalMethod04831("Anti-AFK \u0432\u043a\u043b\u044e\u0447\u0435\u043d");
               break;
            case "disable":
               localValue1.internalMethod00445(false);
               localValue1.internalMethod00497(false);
               this.internalMethod04831("Anti-AFK \u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d");
               break;
            case "setcommand":
               if (localValue2.size() < 2) {
                  this.internalMethod06477(
                     "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot antiafk setcommand <\u043a\u043e\u043c\u0430\u043d\u0434\u0430>"
                  );
                  return;
               }

               localValue1.internalMethod07132(String.join(" ", localValue2.subList(1, localValue2.size())));
               this.internalMethod04831("Anti-AFK \u043a\u043e\u043c\u0430\u043d\u0434\u0430 \u043e\u0431\u043d\u043e\u0432\u043b\u0435\u043d\u0430");
               break;
            case "setinterval":
               if (localValue2.size() < 2) {
                  this.internalMethod06477(
                     "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot antiafk setinterval <\u043c\u0438\u043d\u0443\u0442\u044b>"
                  );
                  return;
               }

               Integer localValue6 = this.internalMethod01892((String)localValue2.get(1));
               if (localValue6 == null || localValue6 < 1) {
                  this.internalMethod06477("\u041d\u0435\u0432\u0435\u0440\u043d\u044b\u0439 \u0438\u043d\u0442\u0435\u0440\u0432\u0430\u043b");
                  return;
               }

               localValue1.internalMethod00444(localValue6);
               this.internalMethod04831("Anti-AFK \u0438\u043d\u0442\u0435\u0440\u0432\u0430\u043b: " + localValue6 + " \u043c\u0438\u043d");
               break;
            case "walk":
               if (localValue2.size() < 2) {
                  this.internalMethod06477("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot antiafk walk enable|disable");
                  return;
               }

               boolean localValue5 = ((String)localValue2.get(1)).equalsIgnoreCase("enable") || ((String)localValue2.get(1)).equalsIgnoreCase("on");
               localValue1.internalMethod00497(localValue5);
               this.internalMethod04831(
                  "Anti-AFK walk " + (localValue5 ? "\u0432\u043a\u043b\u044e\u0447\u0435\u043d" : "\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d")
               );
               break;
            default:
               this.internalMethod06477(
                  "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot antiafk enable|disable|status|setcommand|setinterval|walk"
               );
         }
      } else {
         this.internalMethod04831(
            "Anti-AFK: "
               + (localValue1.internalMethod02923() ? "\u0432\u043a\u043b\u044e\u0447\u0435\u043d" : "\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d")
               + ", walk: "
               + (localValue1.internalMethod02925() ? "\u0432\u043a\u043b\u044e\u0447\u0435\u043d" : "\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d")
               + ", command: "
               + localValue1.internalMethod03066().internalMethod08526()
               + ", interval: "
               + localValue1.internalMethod03066().internalMethod09195() / 60000L
               + " \u043c\u0438\u043d"
         );
      }
   }

   private void internalMethod09980(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.size() < 2) {
         this.internalMethod06477(
            "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot rejoin enable|disable <\u043d\u0438\u043a|all> [\u0442\u0438\u043a\u0438]"
         );
      } else {
         boolean localValue3 = ((String)localValue2.get(0)).equalsIgnoreCase("enable");
         if (!localValue3 && !((String)localValue2.get(0)).equalsIgnoreCase("disable")) {
            this.internalMethod06477(
               "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot rejoin enable|disable <\u043d\u0438\u043a|all> [\u0442\u0438\u043a\u0438]"
            );
         } else {
            String localValue4 = (String)localValue2.get(1);
            Integer localValue5 = localValue2.size() >= 3 ? this.internalMethod01892((String)localValue2.get(2)) : localValue1.internalMethod03066().internalMethod09194();
            if (localValue5 == null || localValue5 < 1) {
               this.internalMethod06477("\u041d\u0435\u0432\u0435\u0440\u043d\u0430\u044f \u0437\u0430\u0434\u0435\u0440\u0436\u043a\u0430 rejoin");
            } else if (localValue4.equalsIgnoreCase("all")) {
               localValue1.internalMethod03985(localValue2x -> {
                  localValue2x.internalMethod09131(localValue3);
                  localValue2x.internalMethod08813(localValue5);
               });
               this.internalMethod04831(
                  "Rejoin "
                     + (localValue3 ? "\u0432\u043a\u043b\u044e\u0447\u0435\u043d" : "\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d")
                     + " \u0434\u043b\u044f \u0432\u0441\u0435\u0445 \u0431\u043e\u0442\u043e\u0432"
               );
            } else {
               Optional localValue6 = localValue1.internalMethod05101(localValue4);
               if (localValue6.isEmpty()) {
                  this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue4));
               } else {
                  ((BotTargetManager)localValue6.get()).internalMethod09131(localValue3);
                  ((BotTargetManager)localValue6.get()).internalMethod08813(localValue5);
                  this.internalMethod04831(
                     "Rejoin "
                        + (localValue3 ? "\u0432\u043a\u043b\u044e\u0447\u0435\u043d" : "\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d")
                        + " \u0434\u043b\u044f "
                        + localValue4
                  );
               }
            }
         }
      }
   }

   private void internalMethod10105(BotRosterManager localValue1, List<String> localValue2) {
      if (localValue2.isEmpty()) {
         this.internalMethod06477("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .bot control <\u043d\u0438\u043a>");
      } else {
         String localValue3 = (String)localValue2.get(0);
         if (!localValue1.internalMethod00544(localValue3)) {
            this.internalMethod06477(LanguageManager.internalMethod00160("commands.bot.not_found", localValue3));
         } else {
            this.internalMethod04831(
               "Packet-control \u0432\u043a\u043b\u044e\u0447\u0435\u043d \u0434\u043b\u044f "
                  + localValue3
                  + ". WASD/\u043c\u044b\u0448\u044c/attack/use/drop \u0443\u043f\u0440\u0430\u0432\u043b\u044f\u044e\u0442 \u0431\u043e\u0442\u043e\u043c; .bot return \u0432\u0435\u0440\u043d\u0451\u0442 \u043e\u0431\u044b\u0447\u043d\u044b\u0439 \u0440\u0435\u0436\u0438\u043c"
            );
         }
      }
   }

   private void internalMethod09198(BotRosterManager localValue1) {
      if (localValue1.internalMethod07764()) {
         this.internalMethod04831(
            "\u0423\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u0435 \u0431\u043e\u0442\u043e\u043c \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u043e"
         );
      } else {
         this.internalMethod04831(
            "\u0421\u0435\u0439\u0447\u0430\u0441 \u043d\u0438 \u043e\u0434\u0438\u043d \u0431\u043e\u0442 \u043d\u0435 \u043d\u0430\u0445\u043e\u0434\u0438\u0442\u0441\u044f \u0432 \u0440\u0443\u0447\u043d\u043e\u043c \u0443\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u0438"
         );
      }
   }

   private void internalMethod04726() {
      ClientMessages.internalMethod01809(
         Text.of(
            "\u0411\u043e\u0442\u044b: .bot connect <\u043d\u0438\u043a> <ip[:port]>, .bot start <count> <ip[:port]>, .bot stop <\u043d\u0438\u043a>, .bot clear, .bot list"
         )
      );
      ClientMessages.internalMethod01809(
         Text.of(
            "\u041f\u043e\u0432\u0435\u0434\u0435\u043d\u0438\u0435: .bot follow <\u0431\u043e\u0442|all> <\u0446\u0435\u043b\u044c>, .bot attack <\u0431\u043e\u0442|all> <\u0446\u0435\u043b\u044c>, .bot mining <\u0431\u043e\u0442|all> <block>, .bot rebreak <\u0431\u043e\u0442|all>, .bot instantrebreak <\u0431\u043e\u0442|all>, .bot goto <\u0431\u043e\u0442|all> <x> <y> <z>"
         )
      );
      ClientMessages.internalMethod01809(
         Text.of(
            "\u041f\u0440\u043e\u0447\u0435\u0435: .bot autojoingrief <\u0431\u043e\u0442|all> <\u0433\u0440\u0438\u0444>, .bot tapemouse add|remove <\u0431\u043e\u0442>, .bot control <\u043d\u0438\u043a>, .bot return, .bot chat <\u043d\u0438\u043a|all> <msg>, .bot antiafk enable|disable|status"
         )
      );
   }

   private Integer internalMethod01892(String localValue1) {
      try {
         return Integer.parseInt(localValue1);
      } catch (NumberFormatException localValue3) {
         return null;
      }
   }

   private void internalMethod04831(String localValue1) {
      MinecraftClient localValue2 = MinecraftClient.getInstance();
      Runnable localValue3 = () -> ClientMessages.internalMethod01809(Text.of(localValue1));
      if (localValue2.isOnThread()) {
         localValue3.run();
      } else {
         localValue2.execute(localValue3);
      }
   }

   private void internalMethod06477(String localValue1) {
      MinecraftClient localValue2 = MinecraftClient.getInstance();
      Runnable localValue3 = () -> ClientMessages.internalMethod09025(Text.of(localValue1));
      if (localValue2.isOnThread()) {
         localValue3.run();
      } else {
         localValue2.execute(localValue3);
      }
   }
}

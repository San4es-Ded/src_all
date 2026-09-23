package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.util.Formatting;

public final class PythonScriptCommand {
   public CommandNode internalMethod01089() {
      List localValue1 = RockstarClient.getInstance().internalMethod04979().internalMethod02641().stream().map(PythonScript::internalMethod01198).toList();
      return CommandBuilder.internalMethod07482(
            "script",
            localValue2 -> localValue2.internalMethod05325("py", "python")
               .internalMethod06148("commands.lua.description")
               .internalMethod01539(
                  "action",
                  localValue0 -> localValue0.internalMethod00776(
                        localValue0x -> PythonScriptCommand.InternalType0193.internalMethod06329(localValue0x)
                            .map(localValue1x -> (OperationResult)OperationResult.internalMethod00116(localValue1x))
                           .orElseGet(() -> OperationResult.internalMethod05941(LanguageManager.internalMethod07214("commands.config.invalid_action")))
                     )
                     .internalMethod05362(PythonScriptCommand.InternalType0193.internalMethod02412())
               )
               .internalMethod01539("id", localValue1xx -> localValue1xx.internalMethod06921().internalMethod00776(OperationResult::internalMethod00116).internalMethod05362(localValue1))
               .internalMethod00262(this::internalMethod03609)
         )
         .internalMethod04146();
   }

   private void internalMethod03609(ParsedCommand localValue1) {
      PythonScriptCommand.InternalType0193 localValue2 = (PythonScriptCommand.InternalType0193)localValue1.internalMethod02266().get(0);
      String localValue3 = (String)localValue1.internalMethod02266().get(1);
      localValue2.internalMethod02704().accept(localValue3);
   }

   static enum InternalType0193 {
      internalField0065("save", "\u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c", "\u044b\u0444\u043c\u0443"),
      internalField0064("create", "add"),
      internalField0974("remove", "delete", "del", "\u0443\u0434\u0430\u043b\u0438\u0442\u044c", "\u0432\u0443\u0434\u0443\u0435\u0443"),
      internalField0973("list", "\u0434\u0448\u044b\u0435"),
      internalField0972("load", "use", "enable", "\u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c", "true", "\u0434\u0449\u0444\u0432"),
      internalField0971("unload", "disable", "off", "false"),
      internalField1402("reload", "update"),
      internalField1405("toggle"),
      internalField1404("install", "pip", "i"),
      internalField1403("dir", "direction");

      private final List<String> internalField0416;

      private InternalType0193(String... localValue3) {
         this.internalField0416 = Arrays.stream(localValue3).map(String::toLowerCase).toList();
      }

      Consumer<String> internalMethod02704() {
         return switch (this) {
            case internalField0065 -> this::internalMethod02757;
            case internalField0064 -> this::internalMethod01358;
            case internalField0974 -> this::internalMethod08489;
            case internalField0973 -> localValue1 -> this.internalMethod00859();
            case internalField0972 -> this::internalMethod08656;
            case internalField0971 -> this::internalMethod08934;
            case internalField1402 -> localValue1 -> this.internalMethod00862();
            case internalField1405 -> this::internalMethod08809;
            case internalField1404 -> this::internalMethod09229;
            case internalField1403 -> localValue0 -> {
               try {
                  File localValue1 = new File(ConfigManager.internalField0148, "scripts");
                  String[] localValue2 = new String[]{"explorer", localValue1.getAbsolutePath()};
                  Runtime.getRuntime().exec(localValue2);
               } catch (Exception localValue3) {
                  RockstarClient.internalField0572.error(LanguageManager.internalMethod00160("commands.lua.dir.error", localValue3.getMessage()));
               }
            };
         };
      }

      private void internalMethod02757(String localValue1) {
         ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("commands.lua.save.edit_directly")));
      }

      private void internalMethod01358(String localValue1) {
         if (localValue1 != null) {
            PythonScriptManager localValue2 = RockstarClient.getInstance().internalMethod04979();
            Optional localValue3 = localValue2.internalMethod02641().stream().filter(localValue1x -> localValue1x.internalMethod01198().equals(localValue1)).findFirst();
            if (localValue3.isPresent()) {
               ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.lua.create.exists", localValue1)));
            } else {
               PythonScript localValue4 = new PythonScript(localValue1);
               if (localValue4.internalMethod05902().exists()) {
                  ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.lua.create.exists", localValue1)));
                  return;
               }

               localValue2.internalMethod02641().add(localValue4.internalMethod01300());
               ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.lua.create.success", localValue1)));
            }
         }
      }

      private void internalMethod00859() {
         PythonScriptManager localValue1 = RockstarClient.getInstance().internalMethod04979();
         if (localValue1.internalMethod02641().isEmpty()) {
            ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("commands.lua.not_found_list")));
         } else {
            ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("commands.lua.list")));
            int localValue2 = 1;

            for (PythonScript localValue4 : localValue1.internalMethod02641()) {
               String localValue5 = localValue4.internalMethod01198();
               String localValue6 = ".py toggle \"" + localValue5.replace("\"", "\\\"") + "\"";
               MutableText localValue7 = Text.literal("[" + localValue2++ + "] ")
                  .setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY)))
                  .append(Text.literal(localValue5).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.WHITE))));
               if (localValue4.internalMethod08681()) {
                  localValue7.append(
                     Text.literal(" " + LanguageManager.internalMethod07214("status.enabled"))
                        .setStyle(
                           Style.EMPTY
                              .withColor(TextColor.fromFormatting(Formatting.GREEN))
                              .withClickEvent(new ClickEvent.RunCommand(localValue6))
                              .withHoverEvent(new HoverEvent.ShowText(Text.literal(LanguageManager.internalMethod07214("python.hover_disable"))))
                        )
                  );
               } else {
                  localValue7.append(
                     Text.literal(" " + LanguageManager.internalMethod07214("status.disabled"))
                        .setStyle(
                           Style.EMPTY
                              .withColor(TextColor.fromFormatting(Formatting.RED))
                              .withClickEvent(new ClickEvent.RunCommand(localValue6))
                              .withHoverEvent(new HoverEvent.ShowText(Text.literal(LanguageManager.internalMethod07214("python.hover_enable"))))
                        )
                  );
               }

               ClientMessages.internalMethod07664(localValue7);
            }
         }
      }

      private void internalMethod08809(String localValue1) {
         if (localValue1 != null) {
            PythonScriptManager localValue2 = RockstarClient.getInstance().internalMethod04979();
            Optional localValue3 = localValue2.internalMethod02641().stream().filter(localValue1x -> localValue1x.internalMethod01198().equals(localValue1)).findFirst();
            if (localValue3.isPresent()) {
               localValue2.internalMethod03942(localValue1, !((PythonScript)localValue3.get()).internalMethod08681());
               this.internalMethod00859();
            } else {
               ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.lua.not_found", localValue1)));
            }
         }
      }

      private void internalMethod08489(String localValue1) {
         if (localValue1 != null) {
            PythonScriptManager localValue2 = RockstarClient.getInstance().internalMethod04979();
            Optional localValue3 = localValue2.internalMethod02641().stream().filter(localValue1x -> localValue1x.internalMethod01198().equals(localValue1)).findFirst();
            if (localValue3.isPresent()) {
               if (((PythonScript)localValue3.get()).internalMethod06690()) {
                  localValue2.internalMethod03942(localValue1, false);
                  ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.lua.delete.success", localValue1)));
               } else {
                  ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.lua.delete.error", localValue1)));
               }
            } else {
               ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.lua.not_found", localValue1)));
            }
         }
      }

      private void internalMethod08656(String localValue1) {
         if (localValue1 != null) {
            PythonScriptManager localValue2 = RockstarClient.getInstance().internalMethod04979();
            Optional localValue3 = localValue2.internalMethod02641().stream().filter(localValue1x -> localValue1x.internalMethod01198().equals(localValue1)).findFirst();
            if (localValue3.isPresent()) {
               localValue2.internalMethod03942(localValue1, true);
               ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.lua.load.success", localValue1)));
            } else {
               ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.lua.not_found", localValue1)));
            }
         }
      }

      private void internalMethod08934(String localValue1) {
         if (localValue1 != null) {
            PythonScriptManager localValue2 = RockstarClient.getInstance().internalMethod04979();
            Optional localValue3 = localValue2.internalMethod02641().stream().filter(localValue1x -> localValue1x.internalMethod01198().equals(localValue1)).findFirst();
            if (localValue3.isPresent()) {
               if (((PythonScript)localValue3.get()).internalMethod08681()) {
                  localValue2.internalMethod03942(localValue1, false);
                  ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.lua.unload.success", localValue1)));
               } else {
                  ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.lua.unload.not_loaded", localValue1)));
               }
            } else {
               ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.lua.not_found", localValue1)));
            }
         }
      }

      private void internalMethod00862() {
         PythonScriptManager localValue1 = RockstarClient.getInstance().internalMethod04979();
         localValue1.internalMethod07673();
      }

      private void internalMethod09229(String localValue1) {
         if (localValue1 == null) {
            ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("python.pip.usage")));
         } else {
            File localValue2 = new File(EmbeddedPythonRuntime.internalMethod00088(), "python.exe");
            if (!localValue2.exists()) {
               ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("python.runtime_missing")));
            } else {
               ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("python.pip.installing", localValue1)));
               Thread localValue3 = new Thread(
                  () -> {
                     String localValue2x = "";
                     int localValue3x = -1;

                     try {
                        Process localValue4 = new ProcessBuilder(localValue2.getAbsolutePath(), "-m", "pip", "install", localValue1, "--no-warn-script-location")
                           .redirectErrorStream(true)
                           .start();

                        String localValue6;
                        try (BufferedReader localValue5 = new BufferedReader(new InputStreamReader(localValue4.getInputStream(), StandardCharsets.UTF_8))) {
                           while ((localValue6 = localValue5.readLine()) != null) {
                              RockstarClient.internalField0572.info("[pip] {}", localValue6);
                              if (localValue6.startsWith("Successfully installed") || localValue6.startsWith("ERROR")) {
                                 localValue2x = localValue6;
                              }
                           }
                        }

                        localValue3x = localValue4.waitFor();
                     } catch (Exception localValue10) {
                        localValue2x = localValue10.getMessage() != null ? localValue10.getMessage() : localValue10.getClass().getSimpleName();
                     }

                     int localValue11 = localValue3x;
                     String localValue12 = localValue2x;
                     MinecraftClientAccess.internalField0149.execute(() -> {
                        if (localValue11 == 0) {
                           ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("python.pip.done", localValue1)));
                        } else {
                           ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod00160("python.pip.fail", localValue1, localValue12)));
                        }
                     });
                  },
                  "pip-install-" + localValue1
               );
               localValue3.setDaemon(true);
               localValue3.start();
            }
         }
      }

      static Optional<PythonScriptCommand.InternalType0193> internalMethod06329(String localValue0) {
         String localValue1 = localValue0.toLowerCase();
         return Arrays.stream(values()).filter(localValue1x -> localValue1x.internalField0416.contains(localValue1)).findFirst();
      }

      static List<String> internalMethod02412() {
         return Arrays.stream(values()).map(localValue0 -> localValue0.internalField0416.getFirst()).toList();
      }
   }
}

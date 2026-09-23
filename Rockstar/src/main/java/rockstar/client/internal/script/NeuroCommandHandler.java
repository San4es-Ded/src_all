package rockstar.client.internal.script;








import rockstar.client.util.*;
import rockstar.client.rotation.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.text.Text;

public class NeuroCommandHandler implements MinecraftClientAccess {
   private static volatile boolean internalField0277;

   public CommandNode internalMethod03205() {
      return CommandBuilder.internalMethod07482(
            "neuro",
            localValue1 -> localValue1.internalMethod05325("nr", "neurorotation")
               .internalMethod06148("commands.neuro.description")
               .internalMethod01539(
                  "action",
                  localValue0 -> localValue0.internalMethod06921()
                     .internalMethod00776(OperationResult::internalMethod00116)
                     .internalMethod07138("record", "stop", "data", "train", "load", "list", "dir", "why")
               )
               .internalMethod01539("name", localValue0 -> localValue0.internalMethod06921().internalMethod00776(OperationResult::internalMethod00116))
               .internalMethod01539(
                  "epochs", localValue0 -> localValue0.internalMethod06921().internalMethod00776(OperationResult::internalMethod00116).internalMethod07138("auto", "400", "800")
               )
               .internalMethod00262(this::internalMethod06559)
         )
         .internalMethod04146();
   }

   private void internalMethod06559(ParsedCommand localValue1) {
      String localValue2 = (String)localValue1.internalMethod02266().get(0);
      String localValue3 = (String)localValue1.internalMethod02266().get(1);
      String localValue4 = (String)localValue1.internalMethod02266().get(2);
      String localValue5 = localValue2 == null ? "status" : localValue2.toLowerCase(Locale.ROOT);
      switch (localValue5) {
         case "status":
            this.internalMethod07414();
            break;
         case "list":
            this.internalMethod07416();
            break;
         case "load":
            this.internalMethod02837(localValue3);
            break;
         case "dir":
            this.internalMethod08968();
            break;
         case "train":
            this.internalMethod00447(localValue3, localValue4);
            break;
         case "record":
         case "rec":
            this.internalMethod01423(localValue3);
            break;
         case "stop":
            this.internalMethod09133();
            break;
         case "data":
            this.internalMethod09143();
            break;
         case "why":
            this.internalMethod08973();
            break;
         default:
            internalMethod08130(
               "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .neuro <list|load|dir|train|record|stop|data|why> [\u0438\u043c\u044f] [\u044d\u043f\u043e\u0445\u0438|auto]"
            );
      }
   }

   private void internalMethod07414() {
      NeuroModelConfig localValue1 = NeuroModelConfig.internalMethod03469();
      internalMethod08971(
         "\u041c\u043e\u0434\u0435\u043b\u044c \u00a7b"
            + NeuroModelConfig.internalMethod07182()
            + "\u00a7r \u2014 "
            + (
               localValue1 == null
                  ? "\u00a7c\u043d\u0435 \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u043b\u0430\u0441\u044c\u00a7r"
                  : "\u0433\u043e\u0442\u043e\u0432\u0430"
            )
            + ", \u0432\u0441\u0435\u0433\u043e "
            + NeuroModelConfig.internalMethod01137().size()
            + "."
      );
      int localValue2 = AuraDatasetRecorder.internalMethod05632();
      AuraDatasetRecorder localValue3 = this.internalMethod02592();
      String localValue4 = AuraDatasetRecorder.internalMethod04422(localValue2);
      internalMethod08971(
         "\u0417\u0430\u043f\u0438\u0441\u0430\u043d\u043e: \u00a7b"
            + AuraDatasetRecorder.internalMethod05892(localValue2)
            + "\u00a7r \u0431\u043e\u044f ("
            + AuraDatasetRecorder.internalMethod08000(localValue2)
            + ")"
            + (
               localValue3 != null && localValue3.internalMethod05629()
                  ? " \u00a7c\u25cf \u043f\u0438\u0448\u0435\u0442\u0441\u044f " + localValue3.internalMethod03527() + "\u00a7r"
                  : ""
            )
      );
      if (localValue4 != null) {
         internalMethod08971(
            "\u00a77\u0414\u043e \u043e\u0431\u0443\u0447\u0435\u043d\u0438\u044f \u043d\u0435 \u0445\u0432\u0430\u0442\u0430\u0435\u0442 \u0435\u0449\u0451 "
               + localValue4
               + " \u0437\u0430\u043f\u0438\u0441\u0438.\u00a7r"
         );
      }

      internalMethod08971("\u0422\u0440\u0435\u043d\u0435\u0440: " + this.internalMethod04489());
   }

   private String internalMethod04489() {
      if (PythonTrainerService.internalMethod04801() == null) {
         return "\u00a77\u043d\u0435\u0442 \u043f\u0438\u0442\u043e\u043d-\u0440\u0430\u043d\u0442\u0430\u0439\u043c\u0430\u00a7r";
      } else if (PythonTrainerService.internalMethod07297()) {
         return "\u0433\u043e\u0442\u043e\u0432";
      } else {
         String localValue1 = PythonTrainerService.internalMethod08585() ? "torch" : "numpy \u0438 torch";
         if (PythonTrainerService.internalMethod07294()) {
            return "\u00a7e\u043a\u0430\u0447\u0430\u0435\u0442\u0441\u044f " + localValue1 + " (~200 \u041c\u0411, \u0440\u0430\u0437\u043e\u0432\u043e)\u00a7r";
         } else {
            String localValue2 = PythonTrainerService.internalMethod01147();
            return "\u00a7c\u043d\u0435\u0442 " + localValue1 + (localValue2 == null ? "" : ": " + localValue2) + "\u00a7r";
         }
      }
   }

   private void internalMethod07416() {
      List localValue1 = NeuroModelConfig.internalMethod01137();
      internalMethod08971("\u041c\u043e\u0434\u0435\u043b\u0435\u0439: \u00a7b" + localValue1.size() + "\u00a7r");

      for (String localValue3 : (Iterable<String>)(Iterable<?>)localValue1) {
         boolean localValue4 = localValue3.equals(NeuroModelConfig.internalMethod07182());
         boolean localValue5 = !Files.isRegularFile(NeuroModelConfig.internalMethod04529(localValue3));
         internalMethod08971(
            (localValue4 ? "\u00a7b > " : "\u00a77 \u00b7 ")
               + localValue3
               + (localValue5 ? " \u00a78(\u0432\u0441\u0442\u0440\u043e\u0435\u043d\u043d\u0430\u044f)" : "")
               + (localValue4 ? " \u00a78\u0430\u043a\u0442\u0438\u0432\u043d\u0430" : "")
               + "\u00a7r"
         );
      }
   }

   private void internalMethod02837(String localValue1) {
      if (internalMethod02838(localValue1)) {
         internalMethod08130("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .neuro load <\u0438\u043c\u044f>");
      } else if (!NeuroModelConfig.internalMethod06694(localValue1)) {
         internalMethod08130("\u041c\u043e\u0434\u0435\u043b\u0438 " + localValue1 + " \u043d\u0435\u0442. \u0421\u043f\u0438\u0441\u043e\u043a: .neuro list");
      } else {
         NeuroModelConfig.internalMethod05242(localValue1);
         internalMethod08971(
            NeuroModelConfig.internalMethod03469() == null
               ? "\u00a7c\u041c\u043e\u0434\u0435\u043b\u044c "
                  + localValue1
                  + " \u0432\u044b\u0431\u0440\u0430\u043d\u0430, \u043d\u043e \u043d\u0435 \u0447\u0438\u0442\u0430\u0435\u0442\u0441\u044f\u00a7r"
               : "\u041c\u043e\u0434\u0435\u043b\u044c \u00a7b" + localValue1 + "\u00a7r \u0437\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u0430."
         );
      }
   }

   private void internalMethod08968() {
      Path localValue1 = NeuroModelConfig.internalMethod06488();

      try {
         Files.createDirectories(localValue1);
      } catch (Exception localValue3) {
      }

      if (internalField0149.keyboard != null) {
         internalField0149.keyboard.setClipboard(localValue1.toString());
      }

      if (this.internalMethod00972(localValue1)) {
         internalMethod08971("\u041f\u0430\u043f\u043a\u0430 \u043c\u043e\u0434\u0435\u043b\u0435\u0439 \u043e\u0442\u043a\u0440\u044b\u0442\u0430: " + localValue1);
      } else {
         internalMethod08971(
            "\u041f\u0430\u043f\u043a\u0430 \u043c\u043e\u0434\u0435\u043b\u0435\u0439: "
               + localValue1
               + " \u00a78(\u043f\u0443\u0442\u044c \u0432 \u0431\u0443\u0444\u0435\u0440\u0435)\u00a7r"
         );
      }
   }

   private boolean internalMethod00972(Path localValue1) {
      try {
         if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.OPEN)) {
            Desktop.getDesktop().open(localValue1.toFile());
            return true;
         }
      } catch (Exception localValue4) {
      }

      try {
         new ProcessBuilder("explorer.exe", localValue1.toString()).start();
         return true;
      } catch (Exception localValue3) {
         return false;
      }
   }

   private void internalMethod01423(String localValue1) {
      AuraDatasetRecorder localValue2 = this.internalMethod02592();
      if (localValue2 == null) {
         internalMethod08130("\u041c\u043e\u0434\u0443\u043b\u044c Aura \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d");
      } else if (localValue2.internalMethod05629()) {
         internalMethod08130(
            "\u0423\u0436\u0435 \u043f\u0438\u0448\u0435\u0442\u0441\u044f "
               + localValue2.internalMethod03527()
               + ". \u041e\u0441\u0442\u0430\u043d\u043e\u0432\u0438: .neuro stop"
         );
      } else if (internalMethod02838(localValue1)) {
         internalMethod08130("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .neuro record <\u0438\u043c\u044f>");
      } else {
         String localValue3 = localValue1.replaceAll("[^\\p{L}\\p{N}_.-]", "");
         if (localValue3.isEmpty()) {
            internalMethod08130("\u041f\u043b\u043e\u0445\u043e\u0435 \u0438\u043c\u044f \u0434\u0430\u0442\u0430\u0441\u0435\u0442\u0430.");
         } else {
            int localValue4 = AuraDatasetRecorder.internalMethod00951(localValue3);
            String localValue5 = localValue2.internalMethod02121(localValue3);
            if (localValue5 != null) {
               internalMethod08130(localValue5);
            } else {
               internalMethod08971(
                  "\u041f\u0438\u0448\u0443 \u00a7b"
                     + localValue3
                     + "\u00a7r"
                     + (
                        localValue4 > 0
                           ? " (\u0434\u043e\u0437\u0430\u043f\u0438\u0441\u044c, \u0442\u0430\u043c \u0443\u0436\u0435 "
                              + AuraDatasetRecorder.internalMethod05892(localValue4)
                              + ")"
                           : ""
                     )
                     + ". \u0411\u0435\u0439 \u0440\u0443\u043a\u0430\u043c\u0438, \u0430\u0443\u0440\u0443 \u0438 \u0430\u0438\u043c-\u0430\u0441\u0441\u0438\u0441\u0442 \u0432\u044b\u043a\u043b\u044e\u0447\u0438. \u0421\u0442\u043e\u043f \u2014 .neuro stop"
               );
               String localValue6 = AuraDatasetRecorder.internalMethod04422(AuraDatasetRecorder.internalMethod05632());
               if (localValue6 != null) {
                  internalMethod08971(
                     "\u00a77\u0414\u043e \u043e\u0431\u0443\u0447\u0435\u043d\u0438\u044f \u043d\u0443\u0436\u043d\u043e \u0435\u0449\u0451 "
                        + localValue6
                        + " \u0431\u043e\u044f.\u00a7r"
                  );
               }
            }
         }
      }
   }

   private void internalMethod08973() {
      AuraModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
      if (localValue1 == null) {
         internalMethod08130("\u041c\u043e\u0434\u0443\u043b\u044c Aura \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d");
      } else {
         Map localValue2 = localValue1.internalMethod05372();
         if (localValue2.isEmpty()) {
            internalMethod08971(
               "\u041f\u043e\u043a\u0430 \u043d\u0435\u0447\u0435\u0433\u043e \u043f\u043e\u043a\u0430\u0437\u0430\u0442\u044c \u2014 \u043f\u043e\u0434\u0435\u0440\u0438\u0441\u044c \u043d\u0435\u043c\u043d\u043e\u0433\u043e \u0441 \u0432\u043a\u043b\u044e\u0447\u0451\u043d\u043d\u043e\u0439 \u0430\u0443\u0440\u043e\u0439."
            );
         } else {
            internalMethod08971(
               "\u00a7b\u041f\u043e\u0447\u0435\u043c\u0443 \u0443\u0434\u0430\u0440\u044b \u043d\u0435 \u043f\u0440\u043e\u0445\u043e\u0434\u044f\u0442\u00a7r (\u0441 \u043f\u043e\u0441\u043b\u0435\u0434\u043d\u0435\u0433\u043e \u0441\u0431\u0440\u043e\u0441\u0430):"
            );
            localValue2.entrySet()
               .stream()
               .sorted((localValue0, localValue1x) -> (Integer)((java.util.Map.Entry)localValue1x).getValue() - (Integer)((java.util.Map.Entry)localValue0).getValue())
               .limit(8L)
               .forEach(localValue0 -> internalMethod08971("  " + (String)((java.util.Map.Entry)localValue0).getKey() + ": " + ((java.util.Map.Entry)localValue0).getValue()));
            localValue2.clear();
            internalMethod08971("\u00a77\u0441\u0447\u0451\u0442\u0447\u0438\u043a\u0438 \u0441\u0431\u0440\u043e\u0448\u0435\u043d\u044b\u00a7r");
         }
      }
   }

   private void internalMethod09133() {
      AuraDatasetRecorder localValue1 = this.internalMethod02592();
      internalMethod08971(
         localValue1 == null ? "\u041c\u043e\u0434\u0443\u043b\u044c Aura \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d" : localValue1.internalMethod06997()
      );
   }

   private void internalMethod09143() {
      List localValue1 = AuraDatasetRecorder.internalMethod05562();
      if (localValue1.isEmpty()) {
         internalMethod08971(
            "\u0414\u0430\u0442\u0430\u0441\u0435\u0442\u043e\u0432 \u043d\u0435\u0442. \u0417\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u2014 .neuro record <\u0438\u043c\u044f>"
         );
      } else {
         int localValue2 = AuraDatasetRecorder.internalMethod05632();
         internalMethod08971(
            "\u0414\u0430\u0442\u0430\u0441\u0435\u0442\u043e\u0432 \u00a7b"
               + localValue1.size()
               + "\u00a7r, \u0432\u0441\u0435\u0433\u043e \u00a7b"
               + AuraDatasetRecorder.internalMethod05892(localValue2)
               + "\u00a7r \u0431\u043e\u044f ("
               + AuraDatasetRecorder.internalMethod08000(localValue2)
               + ")"
         );

         for (String localValue4 : (Iterable<String>)(Iterable<?>)localValue1) {
            internalMethod08971("\u00a78 \u00b7 \u00a77" + localValue4 + "\u00a7r");
         }

         int localValue5 = AuraDatasetRecorder.internalMethod05627();
         if (localValue5 > 0) {
            internalMethod08971(
               "\u00a77\u0411\u0438\u0442\u044b\u0435 \u0441\u0442\u0440\u043e\u043a\u0438 (\u0441\u043b\u0435\u0434\u044b \u0432\u044b\u043b\u0435\u0442\u043e\u0432 \u0432\u043e \u0432\u0440\u0435\u043c\u044f \u0437\u0430\u043f\u0438\u0441\u0438) \u0442\u0440\u0435\u043d\u0435\u0440 \u043f\u0440\u043e\u043f\u0443\u0441\u043a\u0430\u0435\u0442 \u0441\u0430\u043c.\u00a7r"
            );
         }

         String localValue6 = AuraDatasetRecorder.internalMethod04422(localValue2);
         if (localValue6 != null) {
            internalMethod08971(
               "\u00a77\u0414\u043e \u043e\u0431\u0443\u0447\u0435\u043d\u0438\u044f \u043d\u0435 \u0445\u0432\u0430\u0442\u0430\u0435\u0442 \u0435\u0449\u0451 "
                  + localValue6
                  + " \u0437\u0430\u043f\u0438\u0441\u0438.\u00a7r"
            );
         }
      }
   }

   private void internalMethod00447(String localValue1, String localValue2) {
      if (internalField0277) {
         internalMethod08130("\u041e\u0431\u0443\u0447\u0435\u043d\u0438\u0435 \u0443\u0436\u0435 \u0438\u0434\u0451\u0442.");
      } else {
         String localValue3 = internalMethod02838(localValue1) ? NeuroModelConfig.internalMethod07182() : localValue1;
         String localValue4 = localValue2 == null ? null : (localValue2.equalsIgnoreCase("auto") ? "0" : (localValue2.matches("\\d+") ? localValue2 : null));
         if (localValue2 != null && localValue4 == null) {
            internalMethod08130("\u042d\u043f\u043e\u0445\u0438 \u2014 \u0447\u0438\u0441\u043b\u043e \u0438\u043b\u0438 auto.");
         } else {
            File localValue5 = PythonTrainerService.internalMethod04801();
            if (localValue5 == null) {
               internalMethod08130(
                  "\u041f\u0438\u0442\u043e\u043d-\u0440\u0430\u043d\u0442\u0430\u0439\u043c \u0435\u0449\u0451 \u043d\u0435 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d \u2014 \u043e\u0431\u0443\u0447\u0430\u0442\u044c \u043d\u0435\u0447\u0435\u043c."
               );
               internalMethod08971(
                  "\u00a77\u041e\u043d \u043a\u0430\u0447\u0430\u0435\u0442\u0441\u044f \u043f\u0440\u0438 \u0437\u0430\u043f\u0443\u0441\u043a\u0435 \u0432\u043c\u0435\u0441\u0442\u0435 \u0441\u043e \u0441\u043a\u0440\u0438\u043f\u0442\u0430\u043c\u0438; \u0437\u0430\u0439\u0434\u0438 \u043f\u043e\u0437\u0436\u0435.\u00a7r"
               );
            } else if (!PythonTrainerService.internalMethod07297()) {
               if (PythonTrainerService.internalMethod07294()) {
                  internalMethod08130(
                     "\u0415\u0449\u0451 \u043a\u0430\u0447\u0430\u044e\u0442\u0441\u044f numpy \u0438 torch \u0434\u043b\u044f \u043e\u0431\u0443\u0447\u0435\u043d\u0438\u044f (~200 \u041c\u0411, \u0440\u0430\u0437\u043e\u0432\u043e). \u041f\u043e\u043f\u0440\u043e\u0431\u0443\u0439 \u0447\u0435\u0440\u0435\u0437 \u043f\u0430\u0440\u0443 \u043c\u0438\u043d\u0443\u0442."
                  );
               } else {
                  internalMethod08130(
                     "\u0414\u043b\u044f \u043e\u0431\u0443\u0447\u0435\u043d\u0438\u044f \u043d\u0443\u0436\u043d\u044b numpy \u0438 torch, \u0430 \u043e\u043d\u0438 \u043d\u0435 \u043f\u043e\u0441\u0442\u0430\u0432\u0438\u043b\u0438\u0441\u044c"
                        + (PythonTrainerService.internalMethod01147() == null ? "" : ": " + PythonTrainerService.internalMethod01147())
                  );
                  internalMethod08971(
                     "\u00a77\u041f\u0435\u0440\u0435\u0437\u0430\u0439\u0434\u0438 \u0432 \u0438\u0433\u0440\u0443 \u2014 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0430 \u0441\u0442\u0430\u0440\u0442\u0443\u0435\u0442 \u0441\u0430\u043c\u0430, \u043b\u043e\u0433 \u0432 latest.log.\u00a7r"
                  );
               }
            } else {
               int localValue6 = AuraDatasetRecorder.internalMethod05632();
               String localValue7 = AuraDatasetRecorder.internalMethod04422(localValue6);
               if (localValue7 != null) {
                  internalMethod08130(
                     "\u041c\u0430\u043b\u043e \u0437\u0430\u043f\u0438\u0441\u0438: "
                        + AuraDatasetRecorder.internalMethod05892(localValue6)
                        + " \u0431\u043e\u044f, \u043d\u0443\u0436\u043d\u043e \u0445\u043e\u0442\u044f \u0431\u044b "
                        + AuraDatasetRecorder.internalMethod05892(12000)
                        + "."
                  );
                  internalMethod08971("\u00a77\u0417\u0430\u043f\u0438\u0448\u0438 \u0435\u0449\u0451 " + localValue7 + ": .neuro record <\u0438\u043c\u044f>\u00a7r");
               } else {
                  internalField0277 = true;
                  internalMethod08971(
                     "\u041e\u0431\u0443\u0447\u0435\u043d\u0438\u0435 \u043c\u043e\u0434\u0435\u043b\u0438 \u00a7b"
                        + localValue3
                        + "\u00a7r \u043f\u043e\u0448\u043b\u043e ("
                        + (
                           "0".equals(localValue4)
                              ? "\u0430\u0432\u0442\u043e, \u043f\u043e\u043a\u0430 \u043f\u0430\u0434\u0430\u0435\u0442 val"
                              : (localValue4 == null ? "400" : localValue4) + " \u044d\u043f\u043e\u0445"
                        )
                        + "). \u0418\u0433\u0440\u0430 \u043d\u0435 \u0437\u0430\u043c\u0440\u0451\u0442, \u043f\u0440\u043e\u0433\u0440\u0435\u0441\u0441 \u0431\u0443\u0434\u0435\u0442 \u0442\u0443\u0442."
                  );
                  Thread localValue8 = new Thread(
                     () -> this.internalMethod02574(localValue5, NeuroModelConfig.internalMethod00627(), NeuroModelConfig.internalMethod04529(localValue3), localValue3, localValue4),
                     "neuro-train"
                  );
                  localValue8.setDaemon(true);
                  localValue8.start();
               }
            }
         }
      }
   }

   private void internalMethod02574(File localValue1, Path localValue2, Path localValue3, String localValue4, String localValue5) {
      try {
         Files.createDirectories(localValue3.getParent());
         Process localValue6 = PythonTrainerService.internalMethod02657(localValue1, localValue2, localValue3, localValue5);
         BufferedReader localValue7 = new BufferedReader(new InputStreamReader(localValue6.getInputStream(), StandardCharsets.UTF_8));

         String localValue8;
         try {
            while ((localValue8 = localValue7.readLine()) != null) {
               String localValue9 = localValue8.strip();
               if (!localValue9.isEmpty()) {
                  if (localValue9.startsWith("!! ")) {
                     this.internalMethod07657("\u00a7c" + localValue9.substring(3) + "\u00a7r");
                  } else if (localValue9.startsWith("! ")) {
                     this.internalMethod07657("\u00a7e" + localValue9.substring(2) + "\u00a7r");
                  } else {
                     this.internalMethod07657("\u00a77" + localValue9 + "\u00a7r");
                  }
               }
            }
         } catch (Throwable localValue16) {
            try {
               localValue7.close();
            } catch (Throwable localValue15) {
               localValue16.addSuppressed(localValue15);
            }

            throw localValue16;
         }

         localValue7.close();
         int localValue19 = localValue6.waitFor();
         if (localValue19 == 2) {
            return;
         }

         if (localValue19 == 0) {
            this.internalMethod07657("\u00a7a\u0413\u043e\u0442\u043e\u0432\u043e. \u0412\u043a\u043b\u044e\u0447\u0430\u044e " + localValue4 + ".\u00a7r");
            internalField0149.execute(() -> NeuroModelConfig.internalMethod05242(localValue4));
            return;
         }

         this.internalMethod07657("\u00a7c\u0422\u0440\u0435\u043d\u0435\u0440 \u0443\u043f\u0430\u043b, \u043a\u043e\u0434 " + localValue19 + ".\u00a7r");
      } catch (Exception localValue17) {
         this.internalMethod07657(
            "\u00a7c\u041e\u0431\u0443\u0447\u0435\u043d\u0438\u0435 \u0441\u043e\u0440\u0432\u0430\u043b\u043e\u0441\u044c: " + localValue17.getMessage() + "\u00a7r"
         );
         return;
      } finally {
         internalField0277 = false;
      }
   }

   private void internalMethod07657(String localValue1) {
      internalField0149.execute(() -> internalMethod08971(localValue1));
   }

   private AuraDatasetRecorder internalMethod02592() {
      NeuralAuraRotation localValue1 = this.internalMethod02534();
      return localValue1 == null ? null : localValue1.internalMethod03449();
   }

   private NeuralAuraRotation internalMethod02534() {
      AuraModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
      return localValue1 == null ? null : localValue1.internalMethod05950();
   }

   private static boolean internalMethod02838(String localValue0) {
      return localValue0 == null || localValue0.isBlank();
   }

   private static void internalMethod08971(String localValue0) {
      ClientMessages.internalMethod01809(Text.of(localValue0));
   }

   private static void internalMethod08130(String localValue0) {
      ClientMessages.internalMethod09025(Text.of(localValue0));
   }
}

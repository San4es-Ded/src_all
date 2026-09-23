package rockstar.client.internal.script;



import rockstar.client.event.*;
import rockstar.client.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import lombok.Generated;

public class PythonScriptManager {
   private final List<PythonScript> internalField0416 = new ArrayList<>();
   private final Set<String> internalField0546 = ConcurrentHashMap.newKeySet();
   private final AtomicInteger internalField0678 = new AtomicInteger();

   public PythonScriptManager() {
      EmbeddedPythonRuntime.internalMethod07576();
      this.internalMethod07673();
   }

   public void internalMethod06367() {
      this.internalField0678.incrementAndGet();
   }

   public void internalMethod06369() {
      this.internalField0678.updateAndGet(localValue0 -> localValue0 > 0 ? localValue0 - 1 : 0);
   }

   public final void internalMethod07315(ClientEvent localValue1) {
      if (this.internalField0678.get() != 0) {
         for (PythonScript localValue3 : this.internalField0416) {
            if (localValue3.internalMethod04479() != null) {
               localValue3.internalMethod04479().fire(localValue1);
            }
         }
      }
   }

   public final void internalMethod07673() {
      this.internalField0416.forEach(PythonScript::internalMethod06657);
      this.internalField0416.clear();
      Path localValue1 = Paths.get(ConfigManager.internalField0148.getPath(), "scripts");
      if (!Files.exists(localValue1)) {
         try {
            Files.createDirectories(localValue1);
         } catch (IOException localValue7) {
            RockstarClient.internalField0572
               .error(
                  "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043e\u0437\u0434\u0430\u0442\u044c \u0434\u0438\u0440\u0435\u043a\u0442\u043e\u0440\u0438\u044e \u0441\u043a\u0440\u0438\u043f\u0442\u043e\u0432: {}",
                  localValue7.getMessage()
               );
         }
      } else {
         try (Stream<Path> localValue2 = Files.list(localValue1)) {
            localValue2.filter(localValue0 -> Files.isRegularFile(localValue0)).filter(localValue0 -> ((java.nio.file.Path)localValue0).getFileName().toString().endsWith(".py")).forEach(localValue1x -> {
               String localValue2x = ((java.nio.file.Path)localValue1x).getFileName().toString();
               this.internalField0416.add(new PythonScript(localValue2x.substring(0, localValue2x.length() - 3)));
            });
         } catch (IOException localValue9) {
            RockstarClient.internalField0572
               .error(
                  "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u0441\u043a\u0430\u043d\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0438 \u0434\u0438\u0440\u0435\u043a\u0442\u043e\u0440\u0438\u0438 \u0441\u043a\u0440\u0438\u043f\u0442\u043e\u0432: {}",
                  localValue9.getMessage()
               );
         }
      }

      try {
         ScriptSyncService localValue10 = RockstarClient.getInstance().internalMethod05030();
         if (localValue10 != null) {
            localValue10.internalMethod06391().forEach((localValue1x, localValue2x) -> this.internalField0416.add(new PythonScript(localValue1x, localValue2x)));
         }
      } catch (Exception localValue5) {
      }

      this.internalMethod07674();
   }

   public void internalMethod03942(String localValue1, boolean localValue2) {
      String localValue3 = internalMethod07617(localValue1);
      if (!localValue3.isEmpty()) {
         if (localValue2) {
            this.internalField0546.add(localValue3);
         } else {
            this.internalField0546.remove(localValue3);
         }

         PythonScript localValue4 = this.internalMethod03385(localValue1);
         if (localValue4 != null) {
            if (localValue2) {
               if (!localValue4.internalMethod08681()) {
                  localValue4.internalMethod06656();
               }
            } else if (localValue4.internalMethod08681()) {
               localValue4.internalMethod06657();
            }
         }
      }
   }

   public List<String> internalMethod05060() {
      return new ArrayList<>(this.internalField0546);
   }

   public void internalMethod04796(Collection<String> localValue1) {
      this.internalField0546.clear();
      if (localValue1 != null) {
         for (String localValue3 : localValue1) {
            String localValue4 = internalMethod07617(localValue3);
            if (!localValue4.isEmpty()) {
               this.internalField0546.add(localValue4);
            }
         }
      }

      for (PythonScript localValue6 : this.internalField0416) {
         boolean localValue7 = this.internalField0546.contains(internalMethod07617(localValue6.internalMethod01198()));
         if (localValue7 && !localValue6.internalMethod08681()) {
            localValue6.internalMethod06656();
         } else if (!localValue7 && localValue6.internalMethod08681()) {
            localValue6.internalMethod06657();
         }
      }
   }

   private void internalMethod07674() {
      if (!this.internalField0546.isEmpty()) {
         for (PythonScript localValue2 : this.internalField0416) {
            if (!localValue2.internalMethod08681() && this.internalField0546.contains(internalMethod07617(localValue2.internalMethod01198()))) {
               localValue2.internalMethod06656();
            }
         }
      }
   }

   private PythonScript internalMethod03385(String localValue1) {
      String localValue2 = internalMethod07617(localValue1);

      for (PythonScript localValue4 : this.internalField0416) {
         if (internalMethod07617(localValue4.internalMethod01198()).equals(localValue2)) {
            return localValue4;
         }
      }

      return null;
   }

   private static String internalMethod07617(String localValue0) {
      return localValue0 == null ? "" : localValue0.trim().toLowerCase(Locale.ROOT);
   }

   @Generated
   public List<PythonScript> internalMethod02641() {
      return this.internalField0416;
   }
}

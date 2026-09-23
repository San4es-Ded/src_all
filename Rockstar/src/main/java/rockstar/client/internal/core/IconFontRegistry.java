package rockstar.client.internal.core;



import rockstar.client.render.*;
import rockstar.client.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public final class IconFontRegistry {
   private static final FontFamily internalField0450 = FontFamily.internalMethod05676("icons");
   private static Map<String, Integer> internalField0543 = Collections.emptyMap();

   private IconFontRegistry() {
   }

   public static FontFamily internalMethod05006() {
      return internalField0450;
   }

   public static boolean internalMethod02915() {
      return !internalField0543.isEmpty();
   }

   public static Integer internalMethod06164(String localValue0) {
      return internalField0543.get(localValue0);
   }

   public static boolean internalMethod06859(String localValue0) {
      return internalField0543.containsKey(localValue0);
   }

   public static Set<String> internalMethod02667() {
      return internalField0543.keySet();
   }

   public static synchronized void internalMethod02914() {
      try {
         Map localValue0 = MinecraftClient.getInstance().getResourceManager().findResources("icons/font", localValue0x -> localValue0x.getPath().endsWith(".svg"));
         TreeMap localValue1 = new TreeMap();
         localValue0.forEach((localValue1x, localValue2x) -> {
            if (((net.minecraft.util.Identifier)localValue1x).getNamespace().equals(RockstarClient.internalField1077)) {
               String localValue3x = ((net.minecraft.util.Identifier)localValue1x).getPath();
               localValue1.put(localValue3x.substring("icons/font".length() + 1, localValue3x.length() - 4), localValue1x);
            }
         });
         HashMap localValue2 = new HashMap();
         int localValue3 = 57344;

         for (Entry localValue5 : (Iterable<Entry>)(Iterable<?>)localValue1.entrySet()) {
            try (InputStream localValue6 = MinecraftClient.getInstance().getResourceManager().open((Identifier)localValue5.getValue())) {
               String localValue7 = new String(localValue6.readAllBytes(), StandardCharsets.UTF_8);
               internalField0450.internalMethod01906(localValue3, SvgPathParser.internalMethod03662(localValue7));
               localValue2.put((String)localValue5.getKey(), localValue3++);
            } catch (Exception localValue11) {
               RockstarClient.internalField0572
                  .warn("[icons] \u043d\u0435 \u0440\u0430\u0437\u0431\u0438\u0440\u0430\u0435\u0442\u0441\u044f {}: {}", localValue5.getValue(), localValue11.toString());
            }
         }

         internalField0543 = localValue2;
         RockstarClient.internalField0572
            .info("[icons] \u0440\u0430\u0437\u043e\u0431\u0440\u0430\u043d\u043e \u0437\u043d\u0430\u0447\u043a\u043e\u0432: {}", localValue2.size());
      } catch (Throwable localValue12) {
         RockstarClient.internalField0572
            .error(
               "[icons] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u0437\u043d\u0430\u0447\u043a\u0438: {}",
               localValue12.toString()
            );
      }
   }
}

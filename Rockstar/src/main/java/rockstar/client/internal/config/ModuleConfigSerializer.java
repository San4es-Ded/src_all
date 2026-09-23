package rockstar.client.internal.config;








import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.esp.*;
import rockstar.client.module.Module;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public final class ModuleConfigSerializer {
   private static final Map<String, JsonObject> internalField0543 = new ConcurrentHashMap<>();
   private static final Map<String, JsonObject> internalField0544 = new ConcurrentHashMap<>();

   private ModuleConfigSerializer() {
   }

   private static Collection<ModuleEntry> internalMethod00295() {
      return RockstarClient.getInstance().getModuleManager().getModules();
   }

   public static JsonObject internalMethod02114() {
      JsonObject localValue0 = new JsonObject();
      localValue0.add("modules", internalMethod01572());
      localValue0.add("espElements", EspManager.internalMethod06726().internalMethod00487());
      localValue0.add("macroMenu", internalMethod02220());
      return localValue0;
   }

   private static JsonArray internalMethod01572() {
      JsonArray localValue0 = new JsonArray();
      HashSet localValue1 = new HashSet();

      for (ModuleEntry localValue3 : internalMethod00295()) {
         localValue1.add(localValue3.getName());
         localValue0.add(internalMethod03948(localValue3));
      }

      for (Entry localValue5 : internalField0543.entrySet()) {
         if (!localValue1.contains(localValue5.getKey())) {
            localValue0.add(((JsonObject)localValue5.getValue()).deepCopy());
         }
      }

      return localValue0;
   }

   private static JsonObject internalMethod03948(ModuleEntry localValue0) {
      JsonObject localValue1 = new JsonObject();
      localValue1.addProperty("name", localValue0.getName());
      localValue1.addProperty("enabled", localValue0.isEnabled() && !internalMethod01796(localValue0));
      localValue1.addProperty("key", localValue0.getKeybind());
      localValue1.add("settings", internalMethod02611(localValue0.getSettings(), localValue0.getName()));
      return localValue1;
   }

   public static void internalMethod01099(ModuleEntry localValue0) {
      if (localValue0 != null) {
         internalField0543.put(localValue0.getName(), internalMethod03948(localValue0));
      }
   }

   public static void internalMethod05881(ModuleEntry localValue0, Setting localValue1) {
      if (localValue0 != null && localValue1 != null) {
         try {
            internalMethod04813(localValue0.getName(), localValue1.getName(), localValue1.toJson());
         } catch (Exception localValue3) {
            RockstarClient.internalField0572
               .warn(
                  "Config: \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0443 {} \u043c\u043e\u0434\u0443\u043b\u044f {}",
                  new Object[]{localValue1.getName(), localValue0.getName(), localValue3}
               );
         }
      }
   }

   private static JsonObject internalMethod02611(List<Setting> localValue0, String localValue1) {
      JsonObject localValue2 = new JsonObject();

      for (Setting localValue4 : localValue0) {
         localValue2.add(localValue4.getName(), localValue4.toJson());
      }

      JsonObject localValue9 = internalField0544.get(localValue1);
      if (localValue9 != null) {
         synchronized (localValue9) {
            for (Entry localValue6 : localValue9.entrySet()) {
               if (!localValue2.has((String)localValue6.getKey())) {
                  localValue2.add((String)localValue6.getKey(), ((JsonElement)localValue6.getValue()).deepCopy());
               }
            }
         }
      }

      return localValue2;
   }

   private static JsonArray internalMethod02220() {
      JsonArray localValue0 = new JsonArray();
      AssistModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(AssistModule.class);
      if (localValue1 == null) {
         return localValue0;
      } else {
         for (AssistSwapItem localValue3 : localValue1.internalMethod08262()) {
            JsonObject localValue4 = new JsonObject();
            localValue4.addProperty("name", localValue3.internalMethod06026());
            localValue4.addProperty("key", localValue3.internalMethod03234());
            localValue4.add("settings", internalMethod02611(localValue3.getSettings(), localValue3.internalMethod06026()));
            localValue0.add(localValue4);
         }

         return localValue0;
      }
   }

   public static ModuleConfigSerializer.InternalType0358 internalMethod07241(JsonObject localValue0) {
      if (localValue0 == null) {
         return ModuleConfigSerializer.InternalType0358.internalField1277;
      } else {
         JsonArray localValue1 = internalMethod00040(localValue0);
         if (localValue1 == null) {
            RockstarClient.internalField0572
               .warn(
                  "Config: \u043e\u0442\u0441\u0443\u0442\u0441\u0442\u0432\u0443\u0435\u0442 \u043c\u0430\u0441\u0441\u0438\u0432 \u043c\u043e\u0434\u0443\u043b\u0435\u0439"
               );
            return ModuleConfigSerializer.InternalType0358.internalField1277;
         } else {
            internalField0543.clear();
            internalField0544.clear();

            try {
               internalMethod00250();
               boolean localValue2 = true;

               for (JsonElement localValue4 : localValue1) {
                  if (localValue4.isJsonObject()) {
                     if (!internalMethod06514(localValue4.getAsJsonObject())) {
                        localValue2 = false;
                     }
                  } else {
                     localValue2 = false;
                  }
               }

               if (localValue0.has("espElements")) {
                  if (localValue0.get("espElements").isJsonArray()) {
                     try {
                        EspManager.internalMethod06726().internalMethod02428(localValue0.getAsJsonArray("espElements"));
                     } catch (Exception localValue5) {
                        RockstarClient.internalField0572
                           .warn(
                              "Config: \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u044d\u043b\u0435\u043c\u0435\u043d\u0442\u044b ESP",
                              localValue5
                           );
                        localValue2 = false;
                     }
                  } else {
                     localValue2 = false;
                  }
               }

               if (localValue0.has("macroMenu")) {
                  if (localValue0.get("macroMenu").isJsonArray()) {
                     if (!internalMethod02149(localValue0.getAsJsonArray("macroMenu"))) {
                        localValue2 = false;
                     }
                  } else {
                     localValue2 = false;
                  }
               }

               return localValue2 ? ModuleConfigSerializer.InternalType0358.internalField0683 : ModuleConfigSerializer.InternalType0358.internalField0684;
            } catch (Exception localValue6) {
               RockstarClient.internalField0572
                  .error(
                     "Config: \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u0440\u0438\u043c\u0435\u043d\u0438\u0442\u044c \u043a\u043e\u043d\u0444\u0438\u0433",
                     localValue6
                  );
               internalField0543.clear();
               internalField0544.clear();
               return ModuleConfigSerializer.InternalType0358.internalField1277;
            }
         }
      }
   }

   public static boolean internalMethod01100(ModuleEntry localValue0) {
      if (localValue0 == null) {
         return false;
      } else {
         JsonObject localValue1 = internalField0543.remove(localValue0.getName());
         return localValue1 == null ? false : internalMethod06514(localValue1);
      }
   }

   public static boolean internalMethod05882(ModuleEntry localValue0, Setting localValue1) {
      if (localValue0 != null && localValue1 != null) {
         JsonObject localValue2 = internalField0544.get(localValue0.getName());
         if (localValue2 == null) {
            return false;
         } else {
            JsonElement localValue3;
            synchronized (localValue2) {
               localValue3 = localValue2.remove(localValue1.getName());
            }

            if (localValue3 != null && localValue1.isValidJson(localValue3)) {
               try {
                  localValue1.fromJson(localValue3);
                  return true;
               } catch (Exception localValue6) {
                  RockstarClient.internalField0572
                     .warn(
                        "Config: \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0443 {} \u043c\u043e\u0434\u0443\u043b\u044f {}",
                        new Object[]{localValue1.getName(), localValue0.getName(), localValue6}
                     );
                  return false;
               }
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   public static List<String> internalMethod02659() {
      return new ArrayList<>(internalField0543.keySet());
   }

   public static void internalMethod01131(JsonObject localValue0, ModuleEntry localValue1) {
      if (localValue0 != null && localValue1 != null) {
         JsonArray localValue2 = internalMethod00040(localValue0);
         if (localValue2 != null) {
            for (JsonElement localValue4 : localValue2) {
               if (localValue4.isJsonObject()) {
                  JsonObject localValue5 = localValue4.getAsJsonObject();
                  if (localValue5.has("name")) {
                     String localValue6 = localValue5.get("name").getAsString();
                     if (localValue6.equalsIgnoreCase(localValue1.getName()) || localValue6.replace(" ", "").equalsIgnoreCase(localValue1.getName().replace(" ", ""))) {
                        if (!(localValue1 instanceof MenuModule) && localValue1 instanceof Module localValue7) {
                           localValue7.internalMethod09923();
                        }

                        internalMethod06514(localValue5);
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean internalMethod06514(JsonObject localValue0) {
      if (localValue0.has("name") && localValue0.get("name").isJsonPrimitive()) {
         String localValue1 = localValue0.get("name").getAsString();

         try {
            ModuleEntry localValue2 = RockstarClient.getInstance().getModuleManager().getModuleByName(localValue1);
            boolean localValue3 = false;
            if (localValue0.has("enabled")) {
               JsonElement localValue4 = localValue0.get("enabled");
               if (!localValue4.isJsonPrimitive() || !localValue4.getAsJsonPrimitive().isBoolean()) {
                  return false;
               }

               localValue3 = localValue4.getAsBoolean() && !internalMethod01796(localValue2);
            }

            int localValue10 = -1;
            if (localValue0.has("key")) {
               JsonElement localValue5 = localValue0.get("key");
               if (!localValue5.isJsonPrimitive() || !localValue5.getAsJsonPrimitive().isNumber()) {
                  return false;
               }

               double localValue6 = localValue5.getAsDouble();
               if (!Double.isFinite(localValue6) || localValue6 < -2.1474836E9F || localValue6 > 2.147483647E9) {
                  return false;
               }

               localValue10 = localValue5.getAsInt();
            }

            if (!(localValue2 instanceof MenuModule)) {
               localValue2.setEnabled(localValue3, true);
            }

            localValue2.setKeybind(localValue10);
            if (localValue0.has("settings") && localValue0.get("settings").isJsonObject()) {
               JsonObject localValue11 = localValue0.getAsJsonObject("settings");
               return internalMethod03878(localValue2.getSettings(), localValue11, localValue2.getName());
            } else {
               return !localValue0.has("settings");
            }
         } catch (ElementLookupException localValue8) {
            internalField0543.put(localValue1, localValue0.deepCopy());
            RockstarClient.internalField0572
               .warn("Config: \u043d\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043d\u044b\u0439 \u043c\u043e\u0434\u0443\u043b\u044c {}", localValue1);
            return false;
         } catch (Exception localValue9) {
            RockstarClient.internalField0572
               .warn(
                  "Config: \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u043c\u043e\u0434\u0443\u043b\u044c {}",
                  localValue1,
                  localValue9
               );
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean internalMethod03878(List<Setting> localValue0, JsonObject localValue1, String localValue2) {
      boolean localValue3 = true;
      HashSet localValue4 = new HashSet();

      for (Setting localValue6 : localValue0) {
         localValue4.add(localValue6.getName());
      }

      for (Entry localValue11 : localValue1.entrySet()) {
         if (!localValue4.contains(localValue11.getKey())) {
            internalMethod04813(localValue2, (String)localValue11.getKey(), (JsonElement)localValue11.getValue());
         }
      }

      for (Setting localValue12 : localValue0) {
         if (localValue1.has(localValue12.getName())) {
            if (!localValue12.isValidJson(localValue1.get(localValue12.getName()))) {
               RockstarClient.internalField0572
                  .warn(
                     "Config: \u043f\u0440\u043e\u043f\u0443\u0449\u0435\u043d\u0430 \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0430 {} \u043c\u043e\u0434\u0443\u043b\u044f {} \u0438\u0437-\u0437\u0430 \u043d\u0435\u0432\u0435\u0440\u043d\u043e\u0433\u043e \u0444\u043e\u0440\u043c\u0430\u0442\u0430",
                     localValue12.getName(),
                     localValue2
                  );
               internalMethod04813(localValue2, localValue12.getName(), localValue1.get(localValue12.getName()));
               localValue3 = false;
            } else {
               try {
                  localValue12.fromJson(localValue1.get(localValue12.getName()));
               } catch (Exception localValue8) {
                  RockstarClient.internalField0572
                     .warn(
                        "Config: \u043f\u0440\u043e\u043f\u0443\u0449\u0435\u043d\u0430 \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0430 {} \u043c\u043e\u0434\u0443\u043b\u044f {}",
                        new Object[]{localValue12.getName(), localValue2, localValue8}
                     );
                  internalMethod04813(localValue2, localValue12.getName(), localValue1.get(localValue12.getName()));
                  localValue3 = false;
               }
            }
         }
      }

      return localValue3;
   }

   private static void internalMethod04813(String localValue0, String localValue1, JsonElement localValue2) {
      if (localValue0 != null && localValue2 != null) {
         JsonObject localValue3 = internalField0544.computeIfAbsent(localValue0, localValue0x -> new JsonObject());
         synchronized (localValue3) {
            localValue3.add(localValue1, localValue2.deepCopy());
         }
      }
   }

   public static void internalMethod00250() {
      for (ModuleEntry localValue1 : internalMethod00295()) {
         if (!(localValue1 instanceof MenuModule) && localValue1 instanceof Module localValue2) {
            localValue2.internalMethod09923();
         }
      }
   }

   private static boolean internalMethod02149(JsonArray localValue0) {
      AssistModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(AssistModule.class);
      if (localValue1 != null && localValue0 != null) {
         HashMap localValue2 = new HashMap();

         for (AssistSwapItem localValue4 : localValue1.internalMethod03313()) {
            localValue2.put(localValue4.internalMethod06026(), localValue4);
         }

         ArrayList localValue12 = new ArrayList();
         boolean localValue13 = true;

         for (JsonElement localValue6 : localValue0) {
            if (!localValue6.isJsonObject()) {
               localValue13 = false;
            } else {
               JsonObject localValue7 = localValue6.getAsJsonObject();
               if (localValue7.has("name") && localValue7.get("name").isJsonPrimitive()) {
                  AssistSwapItem localValue8 = (AssistSwapItem)localValue2.get(localValue7.get("name").getAsString());
                  if (localValue8 == null) {
                     localValue13 = false;
                  } else {
                     if (localValue7.has("key")) {
                        JsonElement localValue9 = localValue7.get("key");
                        if (!localValue9.isJsonPrimitive() || !localValue9.getAsJsonPrimitive().isNumber()) {
                           localValue13 = false;
                           continue;
                        }

                        double localValue10 = localValue9.getAsDouble();
                        if (!Double.isFinite(localValue10) || localValue10 < -2.1474836E9F || localValue10 > 2.147483647E9) {
                           localValue13 = false;
                           continue;
                        }

                        localValue8.internalMethod01910(localValue9.getAsInt());
                     }

                     if (localValue7.has("settings")) {
                        if (!localValue7.get("settings").isJsonObject()) {
                           localValue13 = false;
                           continue;
                        }

                        if (!internalMethod03878(localValue8.getSettings(), localValue7.getAsJsonObject("settings"), localValue8.internalMethod06026())) {
                           localValue13 = false;
                        }
                     }

                     localValue12.add(localValue8);
                  }
               } else {
                  localValue13 = false;
               }
            }
         }

         localValue1.internalMethod03137(localValue12);
         return localValue13;
      } else {
         return false;
      }
   }

   private static JsonArray internalMethod00040(JsonObject localValue0) {
      return localValue0.has("modules") && localValue0.get("modules").isJsonArray() ? localValue0.getAsJsonArray("modules") : null;
   }

   private static boolean internalMethod01796(ModuleEntry localValue0) {
      ModuleInfo localValue1 = localValue0.getClass().getAnnotation(ModuleInfo.class);
      return localValue1 != null && localValue1.enabledByDefault();
   }

   public static enum InternalType0358 {
      internalField0683,
      internalField0684,
      internalField1277;
   }
}

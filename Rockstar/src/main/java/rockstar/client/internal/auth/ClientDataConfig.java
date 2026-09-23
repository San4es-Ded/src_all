package rockstar.client.internal.auth;











import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.framework.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import net.minecraft.util.math.Vec3d;
import pyrock.utility.render.ColorRGBA;

@ConfigFileAnnotation(
   internalMethod03654 = "client"
)
public class ClientDataConfig extends AbstractClientConfig implements MinecraftClientAccess {
   private static final Map<String, JsonObject> internalField0543 = new ConcurrentHashMap<>();
   private int internalField0227;
   private int internalField0228;
   private volatile boolean internalField0277;
   private volatile boolean internalField0276;

   @Override
   public void internalMethod07509() {
      try {
         ConfigManager.internalMethod01467(this.internalField0148, this.internalField0276 ? new JsonObject() : this.internalMethod06643());
      } catch (Exception localValue2) {
         localValue2.printStackTrace();
      }
   }

   public JsonObject internalMethod06643() {
      JsonObject localValue1 = new JsonObject();
      localValue1.addProperty("username", internalField0149.getSession().getUsername());
      localValue1.addProperty("language", LanguageManager.internalMethod00625().name());
      localValue1.addProperty("themeMode", RockstarClient.getInstance().internalMethod04467().internalMethod05065().name());
      localValue1.add("themeData", ThemeColors.internalMethod02435().internalMethod00421());
      localValue1.addProperty("swing", RockstarClient.getInstance().internalMethod00061().internalMethod02484());
      localValue1.add("hudElements", this.internalMethod03456());
      localValue1.add("friends", this.internalMethod08323());
      localValue1.add("staff", this.internalMethod04189());
      localValue1.add("colorPickerPresets", this.internalMethod09398());
      localValue1.add("password", this.internalMethod09319());
      localValue1.add("waypoints", this.internalMethod08801());
      localValue1.add("macros", this.internalMethod08187());
      localValue1.add("enabledScripts", this.internalMethod08921());
      String localValue2 = RockstarClient.getInstance().internalMethod02152().internalMethod02481();
      if (localValue2 != null) {
         localValue1.addProperty("lastConfig", localValue2);
      }

      localValue1.addProperty("autoSaveConfigs", RockstarClient.getInstance().internalMethod02152().internalMethod03463());
      localValue1.addProperty("hitIslandBestScore", this.internalField0227);
      localValue1.addProperty("hitIslandGamesPlayed", this.internalField0228);
      return localValue1;
   }

   public JsonObject internalMethod03450() {
      return this.internalMethod06643();
   }

   @Override
   public void internalMethod07512() {
      try {
         try (FileReader localValue1 = new FileReader(this.internalMethod05023())) {
            JsonObject localValue2 = (JsonObject)ConfigManager.internalField0931.fromJson(localValue1, JsonObject.class);
            if (localValue2 != null) {
               ClientDataConfig.InternalType0291 localValue3 = this.internalMethod00424(localValue2);
               if (localValue3.internalMethod05250() && !localValue3.internalMethod05255()) {
                  this.internalMethod07509();
               }

               return;
            }

            this.internalMethod08892();
         }
      } catch (Exception localValue6) {
         this.internalMethod08892();
         RockstarClient.internalField0572.error("Failed to read client data", localValue6);
      }
   }

   public ClientDataConfig.InternalType0291 internalMethod01758(JsonObject localValue1) {
      return this.internalMethod00424(localValue1);
   }

   public ClientDataConfig.InternalType0291 internalMethod00424(JsonObject localValue1) {
      if (localValue1 == null) {
         this.internalField0277 = true;
         return new ClientDataConfig.InternalType0291(false, true);
      } else {
         boolean localValue2 = false;
         boolean localValue3 = false;
         if (localValue1.has("password")) {
            try {
               this.internalMethod08222(localValue1.getAsJsonArray("password"));
            } catch (Exception localValue22) {
               localValue3 = true;
               this.internalMethod06463("password", localValue1, localValue22);
            }
         }

         if (localValue1.has("swing")) {
            try {
               String localValue4 = localValue1.get("swing").getAsString();
               SwingAnimationManager localValue5 = RockstarClient.getInstance().internalMethod00061();
               SwingPresetConfig localValue6 = RockstarClient.getInstance().internalMethod01001().internalMethod04541(localValue4, true);
               if (localValue6 != null) {
                  localValue6.internalMethod03765();
               } else {
                  for (SwingPreset localValue8 : localValue5.internalMethod05754()) {
                     if (localValue8.internalMethod02665().equals(localValue4)) {
                        localValue5.internalMethod01403(localValue8);
                     }
                  }
               }
            } catch (Exception localValue27) {
               localValue3 = true;
               this.internalMethod06463("swing", localValue1, localValue27);
            }
         }

         if (localValue1.has("language")) {
            try {
               try {
                  LanguageManager.internalMethod04491(Language.valueOf(localValue1.get("language").getAsString()));
               } catch (IllegalArgumentException localValue20) {
               }
            } catch (Exception localValue21) {
               localValue3 = true;
               this.internalMethod06463("language", localValue1, localValue21);
            }
         }

         if (localValue1.has("themeMode") || localValue1.has("theme")) {
            try {
               String localValue28 = localValue1.has("themeMode") ? localValue1.get("themeMode").getAsString() : null;
               if (localValue28 != null) {
                  try {
                     RockstarClient.getInstance().internalMethod04467().internalMethod05953(ThemeVariant.valueOf(localValue28));
                  } catch (IllegalArgumentException localValue18) {
                     RockstarClient.internalField0572.warn("Unknown theme mode in client data: {}", localValue28);
                  }
               } else {
                  try {
                     RockstarClient.getInstance().internalMethod04467().internalMethod05953(ThemeVariant.valueOf(localValue1.get("theme").getAsString()));
                  } catch (IllegalArgumentException localValue17) {
                  }
               }
            } catch (Exception localValue19) {
               localValue3 = true;
               this.internalMethod06463("theme", localValue1, localValue19);
            }
         }

         if (localValue1.has("friends")) {
            try {
               JsonArray localValue29 = localValue1.getAsJsonArray("friends");
               ArrayList localValue33 = new ArrayList();

               for (JsonElement localValue42 : localValue29) {
                  localValue33.add(localValue42 != null && !localValue42.isJsonNull() ? localValue42.getAsString() : null);
               }

               localValue2 = RockstarClient.getInstance().internalMethod03375().internalMethod03473(localValue33);
            } catch (Exception localValue26) {
               localValue3 = true;
               this.internalMethod06463("friends", localValue1, localValue26);
            }
         }

         if (localValue1.has("staff")) {
            try {
               JsonArray localValue30 = localValue1.getAsJsonArray("staff");
               AbstractClientConfig localValue34 = RockstarClient.getInstance().internalMethod03371().internalMethod01175("staff");
               if (localValue34 == null || !localValue34.internalMethod05023().exists()) {
                  ArrayList localValue39 = new ArrayList();

                  for (JsonElement localValue44 : localValue30) {
                     if (localValue44.isJsonObject()) {
                        JsonObject localValue9 = localValue44.getAsJsonObject();
                        localValue39.add(
                           new StaffListStore.InternalType0181(
                              localValue9.has("name") ? localValue9.get("name").getAsString() : "", localValue9.has("prefix") ? localValue9.get("prefix").getAsString() : ""
                           )
                        );
                     } else if (localValue44.isJsonPrimitive()) {
                        localValue39.add(new StaffListStore.InternalType0181(localValue44.getAsString(), "MODER"));
                     }
                  }

                  RockstarClient.getInstance().internalMethod04407().internalMethod03199(localValue39);
                  if (localValue34 != null) {
                     RockstarClient.getInstance().internalMethod03371().internalMethod02765(localValue34);
                  }
               }
            } catch (Exception localValue25) {
               localValue3 = true;
               this.internalMethod06463("staff", localValue1, localValue25);
            }
         }

         if (localValue1.has("colorPickerPresets")) {
            try {
               this.internalMethod03481(localValue1.getAsJsonArray("colorPickerPresets"));
            } catch (Exception localValue16) {
               localValue3 = true;
               this.internalMethod06463("colorPickerPresets", localValue1, localValue16);
            }
         }

         if (localValue1.has("waypoints")) {
            try {
               this.internalMethod06677(localValue1.getAsJsonArray("waypoints"));
            } catch (Exception localValue15) {
               localValue3 = true;
               this.internalMethod06463("waypoints", localValue1, localValue15);
            }
         }

         if (localValue1.has("macros")) {
            try {
               this.internalMethod07867(localValue1.getAsJsonArray("macros"));
            } catch (Exception localValue14) {
               localValue3 = true;
               this.internalMethod06463("macros", localValue1, localValue14);
            }
         }

         if (localValue1.has("enabledScripts")) {
            try {
               ArrayList localValue31 = new ArrayList();

               for (JsonElement localValue40 : localValue1.getAsJsonArray("enabledScripts")) {
                  if (localValue40 != null && localValue40.isJsonPrimitive()) {
                     localValue31.add(localValue40.getAsString());
                  }
               }

               PythonScriptManager localValue36 = RockstarClient.getInstance().internalMethod04979();
               if (localValue36 != null) {
                  localValue36.internalMethod04796(localValue31);
               }
            } catch (Exception localValue24) {
               localValue3 = true;
               this.internalMethod06463("enabledScripts", localValue1, localValue24);
            }
         }

         if (localValue1.has("hudElements")) {
            try {
               for (JsonElement localValue41 : localValue1.getAsJsonArray("hudElements")) {
                  this.internalMethod00720(localValue41.getAsJsonObject());
               }
            } catch (Exception localValue23) {
               localValue3 = true;
               this.internalMethod06463("hudElements", localValue1, localValue23);
            }
         }

         if (localValue1.has("hitIslandBestScore")) {
            try {
               this.internalField0227 = localValue1.get("hitIslandBestScore").getAsInt();
            } catch (Exception localValue13) {
               localValue3 = true;
               this.internalMethod06463("hitIslandBestScore", localValue1, localValue13);
            }
         }

         if (localValue1.has("hitIslandGamesPlayed")) {
            try {
               this.internalField0228 = localValue1.get("hitIslandGamesPlayed").getAsInt();
            } catch (Exception localValue12) {
               localValue3 = true;
               this.internalMethod06463("hitIslandGamesPlayed", localValue1, localValue12);
            }
         }

         if (localValue1.has("themeData")) {
            try {
               ThemeColors.internalMethod00247(ThemeStyleModel.internalMethod01615(localValue1.getAsJsonObject("themeData")));
            } catch (Exception localValue11) {
               localValue3 = true;
               this.internalMethod06463("themeData", localValue1, localValue11);
            }
         }

         if (localValue1.has("autoSaveConfigs")) {
            try {
               RockstarClient.getInstance().internalMethod02152().internalMethod00563(localValue1.get("autoSaveConfigs").getAsBoolean());
            } catch (Exception localValue10) {
               localValue3 = true;
               this.internalMethod06463("autoSaveConfigs", localValue1, localValue10);
            }
         }

         return new ClientDataConfig.InternalType0291(localValue2, localValue3);
      }
   }

   public void internalMethod08891() {
      this.internalField0277 = false;
   }

   private void internalMethod06463(String localValue1, JsonObject localValue2, Exception localValue3) {
      this.internalField0277 = true;
      this.internalMethod00719(localValue2);
      RockstarClient.internalField0572.error("Failed to load {} from client data", localValue1, localValue3);
   }

   private void internalMethod00719(JsonObject localValue1) {
      try {
         ConfigManager.internalMethod01467(new File(this.internalMethod05023().getPath() + ".broken"), localValue1);
      } catch (IOException localValue3) {
         RockstarClient.internalField0572.error("Failed to preserve broken client data", localValue3);
      }
   }

   private void internalMethod08892() {
      this.internalField0277 = true;

      try {
         Files.copy(this.internalMethod05023().toPath(), new File(this.internalMethod05023().getPath() + ".broken").toPath(), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException localValue2) {
         RockstarClient.internalField0572.error("Failed to preserve broken client data", localValue2);
      }
   }

   private JsonArray internalMethod03456() {
      JsonArray localValue1 = new JsonArray();
      HashSet localValue2 = new HashSet();

      for (AbstractHudWidget localValue4 : RockstarClient.getInstance().internalMethod01271().internalMethod09520()) {
         localValue2.add(localValue4.getName());
         localValue1.add(this.internalMethod04234(localValue4));
      }

      for (Entry localValue6 : internalField0543.entrySet()) {
         if (!localValue2.contains(localValue6.getKey())) {
            localValue1.add(((JsonObject)localValue6.getValue()).deepCopy());
         }
      }

      return localValue1;
   }

   public static void internalMethod02698(String localValue0) {
      if (localValue0 != null) {
         internalField0543.remove(localValue0);
      }
   }

   private JsonObject internalMethod04234(AbstractHudWidget localValue1) {
      JsonObject localValue2 = new JsonObject();
      localValue2.addProperty("name", localValue1.getName());
      localValue2.addProperty("x", localValue1.getX());
      localValue2.addProperty("y", localValue1.getY());
      localValue2.addProperty("showing", localValue1.isShowing());
      localValue2.add("settings", this.internalMethod01133(localValue1));
      return localValue2;
   }

   public static void internalMethod02870(AbstractHudWidget localValue0) {
      if (localValue0 != null) {
         if (RockstarClient.getInstance().internalMethod03371().internalMethod01175("client") instanceof ClientDataConfig localValue1) {
            try {
               internalField0543.put(localValue0.getName(), localValue1.internalMethod04234(localValue0));
            } catch (Exception localValue3) {
               RockstarClient.internalField0572
                  .warn(
                     "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0441\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u0435 hud-\u044d\u043b\u0435\u043c\u0435\u043d\u0442\u0430 {}",
                     localValue0.getName(),
                     localValue3
                  );
            }
         }
      }
   }

   public boolean internalMethod02871(AbstractHudWidget localValue1) {
      if (localValue1 == null) {
         return false;
      } else {
         JsonObject localValue2 = internalField0543.get(localValue1.getName());
         if (localValue2 != null) {
            return this.internalMethod04757(localValue2, localValue1);
         } else if (this.internalMethod05023() != null && this.internalMethod05023().exists()) {
            try (FileReader localValue3 = new FileReader(this.internalMethod05023())) {
               JsonObject localValue4 = (JsonObject)ConfigManager.internalField0931.fromJson(localValue3, JsonObject.class);
               if (localValue4 == null || !localValue4.has("hudElements")) {
                  return false;
               } else {
                  for (JsonElement localValue7 : localValue4.getAsJsonArray("hudElements")) {
                     JsonObject localValue8 = localValue7.getAsJsonObject();
                     if (localValue8.has("name")) {
                        String localValue9 = localValue8.get("name").getAsString();
                        if (localValue1.getName().equalsIgnoreCase(localValue9)) {
                           return this.internalMethod04757(localValue8, localValue1);
                        }
                     }
                  }

                  return false;
               }
            } catch (Exception localValue13) {
               RockstarClient.internalField0572.error("Failed to apply saved HUD state for {}", localValue1.getName(), localValue13);
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public void internalMethod03216(Iterable<? extends AbstractHudWidget> localValue1) {
      if (localValue1 != null) {
         ArrayList localValue2 = new ArrayList();

         for (AbstractHudWidget localValue4 : localValue1) {
            if (localValue4 != null) {
               JsonObject localValue5 = internalField0543.get(localValue4.getName());
               if (localValue5 != null) {
                  this.internalMethod04757(localValue5, localValue4);
               } else {
                  localValue2.add(localValue4);
               }
            }
         }

         if (!localValue2.isEmpty() && this.internalMethod05023() != null && this.internalMethod05023().exists()) {
            try {
               try (FileReader localValue14 = new FileReader(this.internalMethod05023())) {
                  JsonObject localValue15 = (JsonObject)ConfigManager.internalField0931.fromJson(localValue14, JsonObject.class);
                  if (localValue15 != null && localValue15.has("hudElements")) {
                     JsonArray localValue16 = localValue15.getAsJsonArray("hudElements");

                     for (AbstractHudWidget localValue7 : (Iterable<AbstractHudWidget>)(Iterable<?>)localValue2) {
                        for (JsonElement localValue9 : localValue16) {
                           JsonObject localValue10 = localValue9.getAsJsonObject();
                           if (localValue10.has("name") && localValue7.getName().equalsIgnoreCase(localValue10.get("name").getAsString())) {
                              this.internalMethod04757(localValue10, localValue7);
                              break;
                           }
                        }
                     }

                     return;
                  }
               }
            } catch (Exception localValue13) {
               RockstarClient.internalField0572.error("Failed to apply saved HUD settings", localValue13);
            }
         }
      }
   }

   private boolean internalMethod00720(JsonObject localValue1) {
      if (localValue1 != null && localValue1.has("name")) {
         String localValue2 = localValue1.get("name").getAsString();
         internalField0543.put(localValue2, localValue1.deepCopy());
         AbstractHudWidget localValue3 = RockstarClient.getInstance().internalMethod01271().internalMethod06084(localValue2);
         return localValue3 != null && this.internalMethod04757(localValue1, localValue3);
      } else {
         return false;
      }
   }

   private boolean internalMethod04757(JsonObject localValue1, AbstractHudWidget localValue2) {
      if (localValue1 != null && localValue2 != null) {
         if (localValue1.has("x")) {
            localValue2.setX(localValue1.get("x").getAsFloat());
         }

         if (localValue1.has("y")) {
            localValue2.setY(localValue1.get("y").getAsFloat());
         }

         if (localValue1.has("showing")) {
            localValue2.setShowing(localValue1.get("showing").getAsBoolean());
         }

         if (localValue1.has("settings")) {
            JsonObject localValue3 = localValue1.getAsJsonObject("settings");

            for (Setting localValue5 : localValue2.getSettings()) {
               if (localValue3.has(localValue5.getName())) {
                  localValue5.fromJson(localValue3.get(localValue5.getName()));
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private JsonObject internalMethod01133(AbstractHudWidget localValue1) {
      JsonObject localValue2 = new JsonObject();

      for (Setting localValue4 : localValue1.getSettings()) {
         localValue2.add(localValue4.getName(), localValue4.toJson());
      }

      return localValue2;
   }

   private JsonArray internalMethod04189() {
      JsonArray localValue1 = new JsonArray();

      for (StaffListStore.InternalType0181 localValue3 : RockstarClient.getInstance().internalMethod04407().internalMethod07279()) {
         if (localValue3.internalMethod00138() != null && !localValue3.internalMethod00138().isBlank()) {
            JsonObject localValue4 = new JsonObject();
            localValue4.addProperty("name", localValue3.internalMethod00138());
            localValue4.addProperty("prefix", localValue3.internalMethod04906());
            localValue1.add(localValue4);
         }
      }

      return localValue1;
   }

   private JsonArray internalMethod08801() {
      JsonArray localValue1 = new JsonArray();
      WaypointStore localValue2 = RockstarClient.getInstance().internalMethod06121();
      if (localValue2 == null) {
         return localValue1;
      } else {
         for (Entry localValue4 : localValue2.internalMethod00276()) {
            JsonObject localValue5 = new JsonObject();
            localValue5.addProperty("name", (String)localValue4.getKey());
            localValue5.addProperty("x", ((Vec3d)localValue4.getValue()).x);
            localValue5.addProperty("y", ((Vec3d)localValue4.getValue()).y);
            localValue5.addProperty("z", ((Vec3d)localValue4.getValue()).z);
            localValue1.add(localValue5);
         }

         return localValue1;
      }
   }

   private JsonArray internalMethod08921() {
      JsonArray localValue1 = new JsonArray();
      PythonScriptManager localValue2 = RockstarClient.getInstance().internalMethod04979();
      if (localValue2 != null) {
         for (String localValue4 : localValue2.internalMethod05060()) {
            localValue1.add(localValue4);
         }
      }

      return localValue1;
   }

   private JsonArray internalMethod08187() {
      JsonArray localValue1 = new JsonArray();
      if (RockstarClient.getInstance().internalMethod05155() == null) {
         return localValue1;
      } else {
         for (ChatMacroEntry localValue3 : RockstarClient.getInstance().internalMethod05155().internalMethod06721()) {
            JsonObject localValue4 = new JsonObject();
            localValue4.addProperty("key", localValue3.internalMethod03890());
            localValue4.addProperty("command", localValue3.internalMethod07169());
            localValue1.add(localValue4);
         }

         return localValue1;
      }
   }

   private void internalMethod06677(JsonArray localValue1) {
      WaypointStore localValue2 = RockstarClient.getInstance().internalMethod06121();
      if (localValue2 != null) {
         HashMap localValue3 = new HashMap();

         for (JsonElement localValue5 : localValue1) {
            JsonObject localValue6 = localValue5.getAsJsonObject();
            String localValue7 = localValue6.get("name").getAsString();
            double localValue8 = localValue6.get("x").getAsDouble();
            double localValue10 = localValue6.get("y").getAsDouble();
            double localValue12 = localValue6.get("z").getAsDouble();
            localValue3.put(localValue7, new Vec3d(localValue8, localValue10, localValue12));
         }

         localValue2.internalMethod05883(localValue3);
      }
   }

   private JsonArray internalMethod08323() {
      JsonArray localValue1 = new JsonArray();

      for (String localValue3 : RockstarClient.getInstance().internalMethod03375().internalMethod06515()) {
         if (localValue3 != null && !localValue3.isBlank()) {
            localValue1.add(localValue3);
         }
      }

      return localValue1;
   }

   private JsonArray internalMethod09319() {
      JsonArray localValue1 = new JsonArray();

      for (Entry localValue3 : RockstarClient.getInstance().getModuleManager().getModule(AutoAuthModule.class).internalMethod06147().entrySet()) {
         JsonObject localValue4 = new JsonObject();
         localValue4.addProperty("nick", (String)localValue3.getKey());
         localValue4.addProperty("pass", (String)localValue3.getValue());
         localValue1.add(localValue4);
      }

      return localValue1;
   }

   private JsonArray internalMethod09398() {
      JsonArray localValue1 = new JsonArray();

      for (ColorPickerPanel.InternalType0004 localValue4 : ColorPickerPanel.internalField0416) {
         if (localValue4.internalMethod06825()) {
            JsonObject localValue5 = new JsonObject();
            ColorRGBA localValue6 = localValue4.internalMethod03353();
            localValue5.addProperty("red", localValue6.getRed());
            localValue5.addProperty("green", localValue6.getGreen());
            localValue5.addProperty("blue", localValue6.getBlue());
            localValue5.addProperty("alpha", localValue6.getAlpha());
            localValue1.add(localValue5);
         }
      }

      return localValue1;
   }

   private void internalMethod03481(JsonArray localValue1) {
      ArrayList localValue2 = new ArrayList();

      for (JsonElement localValue4 : localValue1) {
         JsonObject localValue5 = localValue4.getAsJsonObject();
         float localValue6 = localValue5.get("red").getAsFloat();
         float localValue7 = localValue5.get("green").getAsFloat();
         float localValue8 = localValue5.get("blue").getAsFloat();
         float localValue9 = localValue5.get("alpha").getAsFloat();
         ColorRGBA localValue10 = new ColorRGBA(localValue6, localValue7, localValue8, localValue9);
         localValue2.add(new ColorPickerPanel.InternalType0004(localValue10));
      }

      ColorPickerPanel.internalMethod04224(localValue2);
   }

   private void internalMethod08222(JsonArray localValue1) {
      for (JsonElement localValue3 : localValue1) {
         JsonObject localValue4 = localValue3.getAsJsonObject();
         String localValue5 = localValue4.get("nick").getAsString();
         String localValue6 = localValue4.get("pass").getAsString();
         RockstarClient.getInstance().getModuleManager().getModule(AutoAuthModule.class).internalMethod07079(localValue5, localValue6);
      }
   }

   private void internalMethod07867(JsonArray localValue1) {
      if (RockstarClient.getInstance().internalMethod05155() != null) {
         ArrayList localValue2 = new ArrayList();

         for (JsonElement localValue4 : localValue1) {
            JsonObject localValue5 = localValue4.getAsJsonObject();
            int localValue6 = localValue5.get("key").getAsInt();
            String localValue7 = localValue5.get("command").getAsString();
            localValue2.add(new ChatMacroEntry(localValue6, localValue7));
         }

         RockstarClient.getInstance().internalMethod05155().internalMethod01171(localValue2);
      }
   }

   @Generated
   public int internalMethod02133() {
      return this.internalField0227;
   }

   @Generated
   public int internalMethod02138() {
      return this.internalField0228;
   }

   @Generated
   public boolean internalMethod02134() {
      return this.internalField0277;
   }

   @Generated
   public boolean internalMethod02139() {
      return this.internalField0276;
   }

   @Generated
   public void internalMethod06263(int localValue1) {
      this.internalField0227 = localValue1;
   }

   @Generated
   public void internalMethod06332(int localValue1) {
      this.internalField0228 = localValue1;
   }

   @Generated
   public void internalMethod06264(boolean localValue1) {
      this.internalField0277 = localValue1;
   }

   @Generated
   public void internalMethod06333(boolean localValue1) {
      this.internalField0276 = localValue1;
   }

   public static final class InternalType0291 {
      private final boolean internalField0277;
      private final boolean internalField0276;

      public InternalType0291(boolean localValue1, boolean localValue2) {
         this.internalField0277 = localValue1;
         this.internalField0276 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0291[cleanedFriends=" + this.internalField0277 + ", failed=" + this.internalField0276 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0276);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ClientDataConfig.InternalType0291 other = (ClientDataConfig.InternalType0291) localValue1;
         return java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0276, other.internalField0276);
      }

      public boolean internalMethod05250() {
         return this.internalField0277;
      }

      public boolean internalMethod05255() {
         return this.internalField0276;
      }
   }
}

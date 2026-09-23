package wtf.wyvern.core.config;

import wtf.wyvern.core.eventbus.EventManager;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.Generated;
import wtf.wyvern.Wyvern;
import wtf.wyvern.utility.crypt.CryptUtility;

public class ConfigManager {
   public static final File configDirectory;
   ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
   private final String shifr = "config";
   private volatile List<String> configNamesCache = List.of();
   private volatile long configDirectoryStamp = Long.MIN_VALUE;

   public ConfigManager() {
      configDirectory.mkdirs();
      this.loadConfig("current_config");
      EventManager.register(this);
      this.scheduler.scheduleAtFixedRate(() -> {
         try {
            this.saveConfig("current_config");
         } catch (Exception var2) {
         }

      }, 5L, 5L, TimeUnit.MINUTES);
   }

   public boolean loadConfig(String configName) {
      if (configName == null) {
         return false;
      } else {
         Config config = this.findConfig(configName);
         if (config == null) {
            return false;
         } else {
            try {
               BufferedReader reader = new BufferedReader(new FileReader(config.getFile()));

               boolean var10;
               try {
                  JsonParser parser = new JsonParser();
                  String encryptedDataBase64 = reader.readLine();
                  byte[] encryptedData = Base64.getDecoder().decode(encryptedDataBase64);
                  byte[] decryptedData = CryptUtility.decryptData(encryptedData, "config");
                  String json = new String(decryptedData, StandardCharsets.UTF_8);
                  JsonObject object = (JsonObject)parser.parse(json);
                  config.load(object);
                  var10 = true;
               } catch (Throwable var12) {
                  try {
                     reader.close();
                  } catch (Throwable var11) {
                     var12.addSuppressed(var11);
                  }

                  throw var12;
               }

               reader.close();
               return var10;
            } catch (Exception var13) {

               return false;
            }
         }
      }
   }

   public boolean saveConfig(String configName) {
      try {
         if (configName == null) {
            return false;
         } else {
            Config config;
            if ((config = this.findConfig(configName)) == null) {
               config = new Config(configName);
            }

            String contentPrettyPrint = (new GsonBuilder()).setPrettyPrinting().create().toJson(config.save());
            contentPrettyPrint = Base64.getEncoder().encodeToString(CryptUtility.encryptData(contentPrettyPrint.getBytes(), "config"));

            try {
               FileWriter writer = new FileWriter(config.getFile());
               writer.write(contentPrettyPrint);
               writer.close();
               return true;
            } catch (IOException var5) {
               return false;
            }
         }
      } catch (Exception var6) {
         return false;
      }
   }

   public Config findConfig(String configName) {
      if (configName == null) {
         return null;
      } else {
         return (new File(configDirectory, configName + "." + "Wyvern".toLowerCase())).exists() ? new Config(configName) : null;
      }
   }

   public List<String> configNames() {
      long directoryStamp = configDirectory.lastModified();
      if (directoryStamp == this.configDirectoryStamp) {
         return this.configNamesCache;
      }

      synchronized (this) {
         directoryStamp = configDirectory.lastModified();
         if (directoryStamp != this.configDirectoryStamp) {
            File[] files = configDirectory.listFiles(File::isFile);
            ArrayList<String> names = new ArrayList<>(files == null ? 0 : files.length);
            if (files != null) {
               for (File file : files) {
                  names.add(file.getName());
               }
            }
            this.configNamesCache = List.copyOf(names);
            this.configDirectoryStamp = directoryStamp;
         }
         return this.configNamesCache;
      }
   }

   public boolean deleteConfig(String configName) {
      if (configName == null) {
         return false;
      } else {
         Config config;
         if ((config = this.findConfig(configName)) == null) {
            return false;
         } else {
            File f = config.getFile();
            return f.exists() && f.delete();
         }
      }
   }

   public void save() {
      this.scheduler.shutdown();
      this.saveConfig("current_config");
   }

   @Generated
   public ScheduledExecutorService getScheduler() {
      return this.scheduler;
   }

   @Generated
   public String getShifr() {
      Objects.requireNonNull(this);
      return "config";
   }

   static {
      configDirectory = new File(Wyvern.DIRECTORY, "configs");
   }
}

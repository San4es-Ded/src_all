package rockstar.client.internal.script;










import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.esp.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.config.*;
import rockstar.client.internal.auth.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import jep.SharedInterpreter;
import lombok.Generated;
import net.minecraft.text.Text;
import pyrock.Client;
import pyrock.GlobalVars;
import pyrock.classes.PyCommand;
import pyrock.classes.PyHudElement;
import pyrock.classes.PyIslandStatus;
import pyrock.classes.PyRotations;
import pyrock.classes.aura.PyAura;
import pyrock.events.PyEvents;
import pyrock.utility.render.PyShader;
import pyrock.utility.render.PyShaders;

public class PythonScript {
   private static final AtomicInteger internalField0678 = new AtomicInteger();
   private final File internalField0148;
   private final String internalField0248;
   private final String internalField0247;
   private final byte[] internalField0609;
   private volatile PyEvents internalField0062;
   private volatile boolean internalField0277;
   private volatile String internalField1077;
   private WatchService internalField0049;
   private WatchKey internalField0700;
   private volatile boolean internalField0276;
   private long internalField0229;
   private final List<ScriptModule> internalField0416 = new ArrayList<>();
   private final List<PyHudElement> internalField0417 = new ArrayList<>();
   private final List<PyCommand> internalField1145 = new ArrayList<>();
   private final List<PyShader> internalField1146 = new ArrayList<>();
   private final Map<ModuleEntry, List<Setting>> internalField0543 = new HashMap<>();
   private final Map<Setting, JsonElement> internalField0544 = new HashMap<>();
   private final Map<ModeSetting, List<ModeSetting.InternalType0088>> internalField1197 = new HashMap<>();
   private final Map<MultiSelectSetting, List<MultiSelectSetting.InternalType0091>> internalField1196 = new HashMap<>();
   private static PythonScript internalField0435;

   public static AutoCloseable internalMethod02561(PythonScript localValue0) {
      PythonScript localValue1 = internalField0435;
      internalField0435 = localValue0;
      return () -> internalField0435 = localValue1;
   }

   public static PythonScript internalMethod00581() {
      return internalField0435;
   }

   public PythonScript(String localValue1) {
      this.internalField0248 = localValue1;
      this.internalField0609 = null;
      this.internalField0247 = internalMethod04215(localValue1);
      File localValue2 = new File(ConfigManager.internalField0148, "scripts");
      if (!localValue2.exists()) {
         localValue2.mkdirs();
      }

      this.internalField0148 = new File(localValue2, localValue1 + ".py");
   }

   public PythonScript(String localValue1, byte[] localValue2) {
      this.internalField0248 = localValue1;
      this.internalField0609 = localValue2;
      this.internalField0247 = internalMethod04215(localValue1);
      this.internalField0148 = null;
   }

   private static String internalMethod04215(String localValue0) {
      return "__ns_" + localValue0.replaceAll("[^A-Za-z0-9_]", "_") + "_" + internalField0678.incrementAndGet();
   }

   public static void internalMethod06022(ScriptModule localValue0) {
      if (internalField0435 != null) {
         internalField0435.internalMethod01625().add(localValue0);
      }
   }

   public static void internalMethod06107(PyHudElement localValue0) {
      if (internalField0435 != null && localValue0 != null) {
         internalField0435.internalField0417.add(localValue0);
      }
   }

   public static void internalMethod02692(PyShader localValue0) {
      if (internalField0435 != null && localValue0 != null && !internalField0435.internalField1146.contains(localValue0)) {
         if (internalField0435.internalField1146.size() >= 32) {
            throw new IllegalStateException(
               "\u0441\u043a\u0440\u0438\u043f\u0442 \u0441\u043e\u0437\u0434\u0430\u043b \u0431\u043e\u043b\u044c\u0448\u0435 32 \u0448\u0435\u0439\u0434\u0435\u0440\u043e\u0432: \u0432\u0435\u0440\u043e\u044f\u0442\u043d\u043e, create() \u0437\u043e\u0432\u0451\u0442\u0441\u044f \u0432\u043d\u0443\u0442\u0440\u0438 \u043e\u0442\u0440\u0438\u0441\u043e\u0432\u043a\u0438"
            );
         } else {
            internalField0435.internalField1146.add(localValue0);
         }
      }
   }

   public static void internalMethod02562(PyCommand localValue0) {
      if (internalField0435 != null && localValue0 != null && !internalField0435.internalField1145.contains(localValue0)) {
         internalField0435.internalField1145.add(localValue0);
      }
   }

   public static void internalMethod04663(ModuleEntry localValue0, Setting localValue1) {
      if (internalField0435 != null) {
         internalField0435.internalField0543.computeIfAbsent(localValue0, localValue0x -> new ArrayList<>()).add(localValue1);
      }
   }

   public static void internalMethod06468(Setting localValue0) {
      if (internalField0435 != null && !internalField0435.internalField0544.containsKey(localValue0)) {
         internalField0435.internalField0544.put(localValue0, localValue0.toJson());
      }
   }

   public static void internalMethod06874(ModeSetting localValue0, ModeSetting.InternalType0088 localValue1) {
      if (internalField0435 != null) {
         internalField0435.internalField1197.computeIfAbsent(localValue0, localValue0x -> new ArrayList<>()).add(localValue1);
      }
   }

   public static boolean internalMethod06875(ModeSetting localValue0, ModeSetting.InternalType0088 localValue1) {
      if (internalField0435 != null && localValue0 != null && localValue1 != null) {
         List localValue2 = internalField0435.internalField1197.get(localValue0);
         return localValue2 != null && localValue2.contains(localValue1);
      } else {
         return false;
      }
   }

   public static void internalMethod04950(ModeSetting localValue0, ModeSetting.InternalType0088 localValue1) {
      if (internalField0435 != null && localValue0 != null && localValue1 != null) {
         List localValue2 = internalField0435.internalField1197.get(localValue0);
         if (localValue2 != null) {
            localValue2.remove(localValue1);
            if (localValue2.isEmpty()) {
               internalField0435.internalField1197.remove(localValue0);
            }
         }
      }
   }

   public static void internalMethod00383(MultiSelectSetting localValue0, MultiSelectSetting.InternalType0091 localValue1) {
      if (internalField0435 != null) {
         internalField0435.internalField1196.computeIfAbsent(localValue0, localValue0x -> new ArrayList<>()).add(localValue1);
      }
   }

   public final void internalMethod06656() {
      if (this.internalField0277) {
         this.internalMethod06657();
      }

      if (!EmbeddedPythonRuntime.internalMethod07577()) {
         ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("python.runtime_missing")));
      } else {
         EmbeddedPythonRuntime.internalMethod01721(this::internalMethod06689);
      }
   }

   private void internalMethod06689() {
      try {
         try (AutoCloseable localValue1 = internalMethod02561(this)) {
            boolean localValue14 = this.internalField0609 != null;
            if (!localValue14 && !this.internalField0148.exists()) {
               RockstarClient.internalField0572.warn(LanguageManager.internalMethod00160("lua.script.file_missing", this.internalField0248));
               this.internalMethod01300();
            }

            this.internalField1077 = null;
            PyEvents localValue3 = new PyEvents(this);
            localValue3.setErrorHandler(localValue1x -> {
               this.internalField1077 = localValue1x.getMessage() != null ? localValue1x.getMessage() : localValue1x.getClass().getSimpleName();
               ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod00160("lua.script_error", this.internalField0248)));
               this.internalMethod06657();
            });
            this.internalField0062 = localValue3;
            RockstarClient.internalField0572.info(LanguageManager.internalMethod00160("lua.script.execution.running", this.internalField0248));
            SharedInterpreter localValue4 = EmbeddedPythonRuntime.internalMethod07274();
            if (localValue4 != null && EmbeddedPythonRuntime.internalMethod07579()) {
               localValue4.set("__ev", localValue3);
               localValue4.set("__cl", new Client());
               localValue4.set("__mc", MinecraftClientAccess.internalField0149);
               localValue4.set("__gv", new GlobalVars());
               localValue4.set("__pa", new PyAura());
               localValue4.set("__pr", new PyRotations());
               localValue4.exec(this.internalField0247 + " = __rockstar_make_ns(__ev, __cl, __mc, __gv, __pa, __pr)");
               if (localValue14) {
                  localValue4.set("__b64", Base64.getEncoder().encodeToString(this.internalField0609));
                  localValue4.exec("import marshal, base64");
                  localValue4.exec("exec(marshal.loads(base64.b64decode(__b64)), " + this.internalField0247 + ")");
                  localValue4.exec("del __ev, __cl, __mc, __gv, __pa, __pr, __b64");
               } else {
                  String localValue5 = Files.readString(this.internalField0148.toPath());
                  localValue4.set("__code", localValue5);
                  localValue4.exec("exec(compile(__code, " + internalMethod05295(this.internalField0248) + ", 'exec'), " + this.internalField0247 + ")");
                  localValue4.exec("del __ev, __cl, __mc, __gv, __pa, __pr, __code");
               }

               this.internalMethod08680();

               for (ScriptModule localValue6 : this.internalField0416) {
                  localValue6.internalMethod09922();
                  ModuleConfigSerializer.internalMethod01100(localValue6);
               }

               for (Entry localValue17 : this.internalField0543.entrySet()) {
                  for (Setting localValue8 : (Iterable<Setting>)(Iterable<?>)(List)localValue17.getValue()) {
                     ModuleConfigSerializer.internalMethod05882((ModuleEntry)localValue17.getKey(), localValue8);
                  }
               }

               EspManager.internalMethod06726().internalMethod02772(this);
               this.internalField0277 = true;
               RockstarClient.getInstance().internalMethod04979().internalMethod06367();
               if (!localValue14) {
                  this.internalField0229 = this.internalField0148.lastModified();
                  this.internalMethod09522();
               }

               RockstarClient.internalField0572.info(LanguageManager.internalMethod00160("lua.script.load.success_log", this.internalField0248));
               ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("lua.script.load.success", this.internalField0248)));
               return;
            }

            ClientMessages.internalMethod09025(
               Text.of(LanguageManager.internalMethod00160("python.init_failed", String.valueOf(EmbeddedPythonRuntime.internalMethod02793())))
            );
            this.internalMethod06657();
         }

         return;
      } catch (Exception localValue11) {
         String localValue13 = localValue11.getMessage() != null ? localValue11.getMessage() : localValue11.getClass().getSimpleName();
         this.internalField1077 = localValue13;
         ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod00160("lua.script.load.error", this.internalField0248, localValue13)));
         RockstarClient.internalField0572.error(LanguageManager.internalMethod00160("lua.script.load.error_log", this.internalField0248), localValue11);
         this.internalMethod06657();
      } catch (Throwable localValue12) {
         String localValue2 = localValue12.getMessage() != null ? localValue12.getMessage() : localValue12.getClass().getSimpleName();
         this.internalField1077 = localValue2;
         ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod00160("lua.script.load.error", this.internalField0248, localValue2)));
         RockstarClient.internalField0572.error(LanguageManager.internalMethod00160("lua.script.load.error_log", this.internalField0248), localValue12);
         this.internalMethod06657();
      }
   }

   private void internalMethod08680() {
      if (!this.internalField0417.isEmpty() && RockstarClient.getInstance().internalMethod01271() != null) {
         if (RockstarClient.getInstance().internalMethod03371().internalMethod01175("client") instanceof ClientDataConfig localValue1) {
            localValue1.internalMethod03216(this.internalField0417);
         }
      }
   }

   public final boolean internalMethod06657() {
      if (!this.internalField0277 && this.internalField0062 == null) {
         return false;
      } else {
         boolean localValue1 = this.internalField0277;
         this.internalField0277 = false;
         if (localValue1) {
            RockstarClient.getInstance().internalMethod04979().internalMethod06369();
         }

         for (ModuleEntry localValue3 : this.internalField0416) {
            ModuleConfigSerializer.internalMethod01099(localValue3);
         }

         for (ModuleEntry localValue9 : this.internalField0416) {
            if (localValue9.isEnabled()) {
               localValue9.setEnabled(false, true);
            }
         }

         if (RockstarClient.getInstance().getModuleManager().getModules().removeAll(this.internalField0416)) {
            ModuleManager.internalMethod03046();
         }

         this.internalField0416.clear();
         this.internalMethod09513();
         this.internalMethod08701();
         this.internalMethod08702();
         SkinOverrideStore.internalMethod02767(this);
         TranslationOverrideStore.internalMethod00186(this);
         EspManager.internalMethod06726().internalMethod04326(this);

         for (Entry localValue10 : this.internalField0543.entrySet()) {
            for (Setting localValue5 : (Iterable<Setting>)(Iterable<?>)(List)localValue10.getValue()) {
               ModuleConfigSerializer.internalMethod05881((ModuleEntry)localValue10.getKey(), localValue5);
            }

            ((ModuleEntry)localValue10.getKey()).getSettings().removeAll((Collection<?>)localValue10.getValue());
         }

         this.internalField0543.clear();
         this.internalMethod08682();
         this.internalField1197.clear();
         this.internalMethod09514();
         this.internalField1196.clear();

         for (Entry localValue11 : this.internalField0544.entrySet()) {
            ((Setting)localValue11.getKey()).fromJson((JsonElement)localValue11.getValue());
         }

         this.internalField0544.clear();
         if (this.internalField0062 != null) {
            this.internalField0062.dispose();
         }

         this.internalField0062 = null;
         this.internalMethod09523();
         if (EmbeddedPythonRuntime.internalMethod07579()) {
            EmbeddedPythonRuntime.internalMethod01721(() -> {
               try {
                  EmbeddedPythonRuntime.internalMethod07274().exec("globals().pop('" + this.internalField0247 + "', None)");
               } catch (Exception localValue2) {
               }
            });
         }

         RockstarClient.internalField0572.info(LanguageManager.internalMethod00160("lua.script.unload", this.internalField0248));
         return true;
      }
   }

   private void internalMethod08682() {
      for (Entry localValue2 : this.internalField1197.entrySet()) {
         ModeSetting localValue3 = (ModeSetting)localValue2.getKey();
         List localValue4 = (List)localValue2.getValue();
         ModeSetting.InternalType0088 localValue5 = localValue3.internalMethod07418();
         boolean localValue6 = localValue5 != null && localValue4.contains(localValue5);
         localValue3.internalMethod06723().removeIf(localValue4::contains);
         if (localValue6) {
            localValue3.internalMethod03917(localValue3.internalMethod06723().isEmpty() ? null : localValue3.internalMethod06723().getFirst());
         }
      }
   }

   private void internalMethod08701() {
      for (PyCommand localValue2 : new ArrayList<>(this.internalField1145)) {
         try {
            localValue2.remove();
         } catch (Throwable localValue4) {
         }
      }

      this.internalField1145.clear();
   }

   private void internalMethod08702() {
      for (PyShader localValue2 : new ArrayList<>(this.internalField1146)) {
         try {
            PyShaders.release(localValue2);
         } catch (Throwable localValue4) {
         }
      }

      this.internalField1146.clear();
   }

   private void internalMethod09513() {
      if (!this.internalField0417.isEmpty() && RockstarClient.getInstance().internalMethod01271() != null) {
         for (PyHudElement localValue2 : new ArrayList<>(this.internalField0417)) {
            ClientDataConfig.internalMethod02870(localValue2);
            localValue2.dispose();
            RockstarClient.getInstance().internalMethod01271().internalMethod09520().remove(localValue2);
         }

         this.internalField0417.clear();
      } else {
         this.internalField0417.clear();
      }
   }

   private void internalMethod09514() {
      for (Entry localValue2 : this.internalField1196.entrySet()) {
         MultiSelectSetting localValue3 = (MultiSelectSetting)localValue2.getKey();
         List localValue4 = (List)localValue2.getValue();

         for (MultiSelectSetting.InternalType0091 localValue6 : (Iterable<MultiSelectSetting.InternalType0091>)(Iterable<?>)localValue4) {
            if (localValue6 instanceof PyIslandStatus localValue7) {
               if (RockstarClient.getInstance().internalMethod01271() != null && RockstarClient.getInstance().internalMethod01271().internalMethod01259() != null) {
                  RockstarClient.getInstance().internalMethod01271().internalMethod01259().internalMethod05741(localValue7);
               }

               localValue7.dispose();
            }
         }

         localValue3.internalMethod07492().removeIf(localValue4::contains);
         localValue3.internalMethod01792().removeIf(localValue4::contains);
      }
   }

   public final PythonScript internalMethod01300() {
      if (this.internalField0148 != null && !this.internalField0148.exists()) {
         try {
            if (!this.internalField0148.createNewFile()) {
               throw new IOException(LanguageManager.internalMethod00160("lua.script.create_file_error", this.internalField0148.getAbsolutePath()));
            }

            try (FileWriter localValue1 = new FileWriter(this.internalField0148)) {
               localValue1.write(LanguageManager.internalMethod07214("lua.script.template"));
            }
         } catch (IOException localValue6) {
            RockstarClient.internalField0572.error(LanguageManager.internalMethod07214("lua.script.save_error"), localValue6);
         }

         return this;
      } else {
         return this;
      }
   }

   public final boolean internalMethod06690() {
      this.internalMethod06657();
      if (this.internalField0148 == null) {
         RockstarClient.getInstance().internalMethod04979().internalMethod02641().remove(this);
         return true;
      } else if (this.internalField0148.exists() && this.internalField0148.delete()) {
         RockstarClient.getInstance().internalMethod04979().internalMethod02641().remove(this);
         RockstarClient.internalField0572.info(LanguageManager.internalMethod00160("lua.script.delete.success", this.internalField0148.getAbsolutePath()));
         return true;
      } else {
         RockstarClient.internalField0572.warn(LanguageManager.internalMethod00160("lua.script.delete.error", this.internalField0148.getAbsolutePath()));
         return false;
      }
   }

   private static String internalMethod05295(String localValue0) {
      return "'" + localValue0.replace("\\", "\\\\").replace("'", "\\'") + "'";
   }

   private void internalMethod09522() {
      try {
         this.internalField0049 = FileSystems.getDefault().newWatchService();
         Path localValue1 = this.internalField0148.toPath().getParent();
         this.internalField0700 = localValue1.register(this.internalField0049, StandardWatchEventKinds.ENTRY_MODIFY);
         this.internalField0276 = true;
         Thread localValue2 = new Thread(() -> {
            try {
               while (this.internalField0276) {
                  WatchKey localValue1x = this.internalField0049.poll(500L, TimeUnit.MILLISECONDS);
                  if (localValue1x != null) {
                     for (WatchEvent localValue3x : localValue1x.pollEvents()) {
                        Path localValue4 = (Path)localValue3x.context();
                        if (localValue4.toString().equals(this.internalField0148.getName())) {
                           long localValue5 = this.internalField0148.lastModified();
                           if (localValue5 != this.internalField0229) {
                              this.internalField0229 = localValue5;
                              Thread.sleep(100L);
                              MinecraftClientAccess.internalField0149.execute(this::internalMethod09673);
                           }
                        }
                     }

                     localValue1x.reset();
                  }
               }
            } catch (Exception localValue7) {
               if (this.internalField0276) {
                  RockstarClient.internalField0572.error("File watcher error: {}", localValue7.getMessage());
               }
            }
         }, "Python-Watcher-" + this.internalField0248);
         localValue2.setDaemon(true);
         localValue2.start();
      } catch (Exception localValue3) {
         RockstarClient.internalField0572.error("Failed to start file watcher: {}", localValue3.getMessage());
      }
   }

   private void internalMethod09523() {
      this.internalField0276 = false;

      try {
         if (this.internalField0700 != null) {
            this.internalField0700.cancel();
            this.internalField0700 = null;
         }

         if (this.internalField0049 != null) {
            this.internalField0049.close();
            this.internalField0049 = null;
         }
      } catch (Exception localValue2) {
         RockstarClient.internalField0572.error("Failed to stop file watcher: {}", localValue2.getMessage());
      }
   }

   private void internalMethod09673() {
      this.internalMethod06657();
      this.internalMethod06656();
   }

   @Generated
   public File internalMethod05902() {
      return this.internalField0148;
   }

   @Generated
   public String internalMethod01198() {
      return this.internalField0248;
   }

   @Generated
   public String internalMethod05832() {
      return this.internalField0247;
   }

   @Generated
   public byte[] internalMethod05235() {
      return this.internalField0609;
   }

   @Generated
   public PyEvents internalMethod04479() {
      return this.internalField0062;
   }

   @Generated
   public boolean internalMethod08681() {
      return this.internalField0277;
   }

   @Generated
   public String internalMethod07951() {
      return this.internalField1077;
   }

   @Generated
   public WatchService internalMethod00153() {
      return this.internalField0049;
   }

   @Generated
   public WatchKey internalMethod01377() {
      return this.internalField0700;
   }

   @Generated
   public boolean internalMethod08683() {
      return this.internalField0276;
   }

   @Generated
   public long internalMethod06655() {
      return this.internalField0229;
   }

   @Generated
   public List<ScriptModule> internalMethod01625() {
      return this.internalField0416;
   }

   @Generated
   public List<PyHudElement> internalMethod07291() {
      return this.internalField0417;
   }

   @Generated
   public List<PyCommand> internalMethod08326() {
      return this.internalField1145;
   }

   @Generated
   public List<PyShader> internalMethod07892() {
      return this.internalField1146;
   }

   @Generated
   public Map<ModuleEntry, List<Setting>> internalMethod03801() {
      return this.internalField0543;
   }

   @Generated
   public Map<Setting, JsonElement> internalMethod00331() {
      return this.internalField0544;
   }

   @Generated
   public Map<ModeSetting, List<ModeSetting.InternalType0088>> internalMethod07985() {
      return this.internalField1197;
   }

   @Generated
   public Map<MultiSelectSetting, List<MultiSelectSetting.InternalType0091>> internalMethod08499() {
      return this.internalField1196;
   }
}

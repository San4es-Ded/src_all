package rockstar.client.internal.script;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import globals.shared.proto.Packets;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import rockstar.client.RockstarClient;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.internal.config.ModuleConfigSerializer;
import rockstar.client.internal.core.ClientSoundEvents;
import rockstar.client.module.ModuleEntry;
import rockstar.client.notification.NotificationType;
import rockstar.client.util.ClientMessages;
import rockstar.modules.other.SoundsModule;

/**
 * Local configuration storage.
 *
 * <p>The original client delegated every .cfg operation to RockNet. That made the
 * commands silently stop working when the service was unavailable and also let a
 * server-provided "default" configuration replace the user's current state. The
 * public method names are intentionally kept intact because the command parser,
 * UI and local MCP API call this class directly.</p>
 */
public class LocalConfigStore {
   private static final long internalField0229 = 1500L;
   private static final long internalField0230 = 1200L;
   private static final String internalField0248 = "configs";
   private static final String internalField0249 = "active.txt";
   private final Object internalField0961 = new Object();
   private volatile List<Packets.InternalType0490> internalField0416 = List.of();
   private volatile Map<String, Long> internalField0543 = Map.of();
   private volatile Map<String, InternalType0001> internalField0544 = Map.of();
   private volatile Long internalField0361;
   private volatile String internalField0247;
   private final AtomicBoolean internalField0020 = new AtomicBoolean(false);
   private final AtomicBoolean internalField0019 = new AtomicBoolean(false);
   private volatile boolean internalField0277 = true;
   private final ScheduledExecutorService internalField0220 = Executors.newSingleThreadScheduledExecutor(localValue0 -> {
      Thread localValue1 = new Thread(localValue0, "Config-Autosave");
      localValue1.setDaemon(true);
      return localValue1;
   });
   private volatile ScheduledFuture<?> internalField0825;
   private volatile ScheduledFuture<?> internalField0826;

   public void internalMethod03462() {
      this.internalMethod09777();
      String localValue1 = this.internalField0247;
      if (localValue1 != null) {
         this.internalField0019.set(true);
         this.internalField0826 = this.internalField0220.schedule(() -> {
            InternalType0001 localValue2 = this.internalMethod02543(localValue1);
            if (localValue2 == null) {
               this.internalField0019.set(false);
               this.internalMethod09777();
               return;
            }

            this.internalMethod00117(localValue2.name(), localValue2.data(), true);
         }, internalField0230, TimeUnit.MILLISECONDS);
      }
   }

   /** Refreshes the local index. RockNet connectivity is no longer required. */
   public void internalMethod03465() {
      this.internalMethod09777();
   }

   /** Ignore legacy cloud config lists so they cannot replace the local index. */
   public void internalMethod06954(List<Packets.InternalType0490> localValue1) {
      RockstarClient.internalField0572.debug("Config: ignored legacy RockNet config list");
   }

   /** Ignore legacy cloud config payloads so no remote/default config is auto-loaded. */
   public void internalMethod03402(Packets.InternalType0487 localValue1) {
      RockstarClient.internalField0572.debug("Config: ignored legacy RockNet config payload");
   }

   private void internalMethod00117(String localValue1, JsonObject localValue2, boolean localValue3) {
      this.internalField0019.set(true);
      this.internalMethod09787();
      ScheduledFuture<?> localValue4 = this.internalField0826;
      if (localValue4 != null) {
         localValue4.cancel(false);
         this.internalField0826 = null;
      }

      Runnable localValue5 = () -> {
         this.internalField0020.set(true);
         ModuleConfigSerializer.InternalType0358 localValue6 = ModuleConfigSerializer.InternalType0358.internalField1277;
         JsonObject localValue7 = null;

         try {
            localValue7 = ModuleConfigSerializer.internalMethod02114();
            localValue6 = ModuleConfigSerializer.internalMethod07241(localValue2);
            if (localValue6 == ModuleConfigSerializer.InternalType0358.internalField1277 && localValue7 != null) {
               ModuleConfigSerializer.internalMethod07241(localValue7);
            }
         } catch (Exception localValue12) {
            RockstarClient.internalField0572.error("Config: failed to apply local config {}", localValue1, localValue12);
            if (localValue7 != null) {
               try {
                  ModuleConfigSerializer.internalMethod07241(localValue7);
               } catch (Exception localValue13) {
                  RockstarClient.internalField0572.error("Config: failed to roll back after load error", localValue13);
               }
            }
         } finally {
            this.internalField0020.set(false);
         }

         if (localValue6 == ModuleConfigSerializer.InternalType0358.internalField1277) {
            this.internalField0019.set(false);
            this.internalMethod09001("Не удалось загрузить конфиг \"" + localValue1 + "\". Текущие настройки восстановлены.");
            return;
         }

         if (localValue6 == ModuleConfigSerializer.InternalType0358.internalField0684) {
            List<String> localValue8 = ModuleConfigSerializer.internalMethod02659();
            if (localValue8.isEmpty()) {
               RockstarClient.internalField0572.warn("Config: {} loaded partially; incompatible settings were skipped", localValue1);
            } else {
               RockstarClient.internalField0572.warn(
                  "Config: {} loaded partially; unknown module sections were preserved: {}",
                  localValue1,
                  String.join(", ", localValue8)
               );
            }
         }

         try {
            this.internalMethod09005(localValue1);
            this.internalMethod09777();
         } catch (IOException localValue11) {
            RockstarClient.internalField0572.error("Config: failed to remember active config {}", localValue1, localValue11);
         }

         this.internalField0019.set(false);
         if (localValue3) {
            this.internalMethod09778();
         }
      };

      MinecraftClient localValue6 = MinecraftClient.getInstance();
      if (localValue6.isOnThread()) {
         localValue5.run();
      } else {
         localValue6.execute(localValue5);
      }
   }

   private void internalMethod09778() {
      try {
         SoundsModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(SoundsModule.class);
         if (localValue1 != null) {
            ClientSoundEvents.internalField0131.internalMethod03132(localValue1.internalMethod01798(), 1.0F);
         }

         RockstarClient.getInstance()
            .internalMethod02503()
            .internalMethod04075(NotificationType.internalField0704, LanguageManager.internalMethod07214("configs.loaded"));
      } catch (Exception ignored) {
      }
   }

   public boolean internalMethod03463() {
      return this.internalField0277;
   }

   public void internalMethod00563(boolean localValue1) {
      this.internalField0277 = localValue1;
      if (!localValue1) {
         this.internalMethod09787();
      }
   }

   public void internalMethod07804() {
      if (!this.internalField0277 || this.internalField0020.get() || this.internalField0019.get() || this.internalField0247 == null) {
         return;
      }

      this.internalMethod09787();
      this.internalField0825 = this.internalField0220.schedule(this::internalMethod09786, internalField0229, TimeUnit.MILLISECONDS);
   }

   public void internalMethod07805() {
      if (!this.internalField0277) {
         return;
      }

      this.internalMethod09787();
      this.internalMethod09786();
   }

   private void internalMethod09786() {
      if (this.internalField0020.get() || this.internalField0019.get()) {
         return;
      }

      String localValue1 = this.internalField0247;
      if (localValue1 == null) {
         return;
      }

      Runnable localValue2 = () -> {
         if (this.internalField0020.get() || this.internalField0019.get()) {
            return;
         }

         try {
            this.internalMethod09003(localValue1, ModuleConfigSerializer.internalMethod02114(), true, true);
         } catch (Exception localValue4) {
            RockstarClient.internalField0572.error("Config: autosave failed for {}", localValue1, localValue4);
         }
      };

      MinecraftClient localValue3 = MinecraftClient.getInstance();
      if (localValue3.isOnThread()) {
         localValue2.run();
      } else {
         localValue3.execute(localValue2);
      }
   }

   public void internalMethod06089(String localValue1) {
      if (internalMethod04633(localValue1)) {
         return;
      }

      String localValue2 = localValue1.trim();
      this.internalField0019.set(false);
      try {
         this.internalMethod09003(localValue2, ModuleConfigSerializer.internalMethod02114(), true, true);
         ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.config.saved", localValue2)));
      } catch (Exception localValue4) {
         RockstarClient.internalField0572.error("Config: failed to save {}", localValue2, localValue4);
         this.internalMethod09001("Не удалось сохранить конфиг \"" + localValue2 + "\".");
      }
   }

   public void internalMethod04632(String localValue1) {
      InternalType0001 localValue2 = this.internalMethod02543(localValue1);
      if (localValue2 == null) {
         this.internalMethod08257(localValue1);
         return;
      }

      this.internalMethod00117(localValue2.name(), localValue2.data(), true);
   }

   public void internalMethod08118(String localValue1) {
      String localValue2 = internalMethod04633(localValue1) ? this.internalField0247 : localValue1.trim();
      InternalType0001 localValue3 = this.internalMethod02543(localValue2);
      if (localValue3 == null) {
         this.internalMethod08257(localValue2);
         return;
      }

      Path localValue4 = this.internalMethod09009(localValue3.path());
      if (!Files.isRegularFile(localValue4)) {
         ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("config.undo.empty")));
         return;
      }

      try {
         JsonObject localValue5 = this.internalMethod09011(localValue4);
         JsonObject localValue6 = this.internalMethod09010(localValue3.id(), localValue3.name(), localValue3.data());
         JsonObject localValue7 = this.internalMethod09010(
            localValue3.id(),
            localValue3.name(),
            this.internalMethod09012(localValue5)
         );
         ConfigManager.internalMethod01467(localValue3.path().toFile(), localValue7);
         ConfigManager.internalMethod01467(localValue4.toFile(), localValue6);
         this.internalMethod09777();
         InternalType0001 localValue8 = this.internalMethod02543(localValue3.name());
         if (localValue8 != null) {
            this.internalMethod00117(localValue8.name(), localValue8.data(), true);
         }
      } catch (Exception localValue9) {
         RockstarClient.internalField0572.error("Config: undo failed for {}", localValue3.name(), localValue9);
         this.internalMethod09001("Не удалось откатить конфиг \"" + localValue3.name() + "\".");
      }
   }

   public void internalMethod07852(String localValue1) {
      InternalType0001 localValue2 = this.internalMethod02543(localValue1);
      if (localValue2 == null) {
         this.internalMethod08257(localValue1);
         return;
      }

      try {
         Files.deleteIfExists(localValue2.path());
         Files.deleteIfExists(this.internalMethod09009(localValue2.path()));
         if (localValue2.name().equalsIgnoreCase(this.internalField0247)) {
            this.internalMethod09006();
         }
         this.internalMethod09777();
         ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("config.deleted", localValue2.name())));
      } catch (Exception localValue4) {
         RockstarClient.internalField0572.error("Config: failed to delete {}", localValue2.name(), localValue4);
         this.internalMethod09001("Не удалось удалить конфиг \"" + localValue2.name() + "\".");
      }
   }

   public void internalMethod06062(String localValue1, String localValue2) {
      InternalType0001 localValue3 = this.internalMethod02543(localValue1);
      if (localValue3 == null || internalMethod04633(localValue2)) {
         this.internalMethod08257(localValue1);
         return;
      }

      String localValue4 = localValue2.trim();
      InternalType0001 localValue5 = this.internalMethod02543(localValue4);
      if (localValue5 != null && localValue5.id() != localValue3.id()) {
         this.internalMethod09001("Конфиг \"" + localValue4 + "\" уже существует.");
         return;
      }

      try {
         Path localValue6 = this.internalMethod09008(localValue4);
         JsonObject localValue7 = this.internalMethod09010(localValue3.id(), localValue4, localValue3.data());
         ConfigManager.internalMethod01467(localValue6.toFile(), localValue7);
         Path localValue8 = this.internalMethod09009(localValue3.path());
         if (Files.isRegularFile(localValue8)) {
            Files.move(localValue8, this.internalMethod09009(localValue6), StandardCopyOption.REPLACE_EXISTING);
         }
         if (!localValue3.path().equals(localValue6)) {
            Files.deleteIfExists(localValue3.path());
         }
         if (localValue3.name().equalsIgnoreCase(this.internalField0247)) {
            this.internalMethod09005(localValue4);
         }
         this.internalMethod09777();
         ClientMessages.internalMethod01809(Text.literal("Конфиг переименован: " + localValue3.name() + " → " + localValue4));
      } catch (Exception localValue9) {
         RockstarClient.internalField0572.error("Config: failed to rename {} to {}", localValue3.name(), localValue4, localValue9);
         this.internalMethod09001("Не удалось переименовать конфиг.");
      }
   }

   public void internalMethod08544(String localValue1) {
      InternalType0001 localValue2 = this.internalMethod02543(localValue1);
      if (localValue2 == null) {
         this.internalMethod08257(localValue1);
         return;
      }

      String localValue3 = localValue2.name() + " copy";
      int localValue4 = 2;
      while (this.internalMethod02543(localValue3) != null) {
         localValue3 = localValue2.name() + " copy (" + localValue4++ + ")";
      }

      try {
         this.internalMethod09003(localValue3, localValue2.data(), false, false);
         ClientMessages.internalMethod01809(Text.literal("Создана копия конфига: " + localValue3));
      } catch (Exception localValue6) {
         RockstarClient.internalField0572.error("Config: failed to duplicate {}", localValue2.name(), localValue6);
         this.internalMethod09001("Не удалось создать копию конфига.");
      }
   }

   public void internalMethod04975(String localValue1, JsonObject localValue2) {
      if (internalMethod04633(localValue1) || localValue2 == null) {
         return;
      }

      String localValue3 = localValue1.trim();
      if (this.internalMethod02543(localValue3) != null) {
         int localValue4 = 2;
         String localValue5 = localValue3 + " imported";
         while (this.internalMethod02543(localValue5) != null) {
            localValue5 = localValue3 + " imported (" + localValue4++ + ")";
         }
         localValue3 = localValue5;
      }

      try {
         this.internalMethod09003(localValue3, localValue2, false, false);
         ClientMessages.internalMethod01809(Text.literal("Импортирован конфиг: " + localValue3));
      } catch (Exception localValue7) {
         RockstarClient.internalField0572.error("Config: failed to import {}", localValue3, localValue7);
         this.internalMethod09001("Не удалось импортировать конфиг.");
      }
   }

   public void internalMethod07825() {
      this.internalField0020.set(true);

      try {
         ModuleConfigSerializer.internalMethod00250();
         for (ModuleEntry localValue2 : RockstarClient.getInstance().getModuleManager().getModules()) {
            localValue2.setEnabled(localValue2.isEnabledByDefault(), true);
         }
      } finally {
         this.internalField0020.set(false);
      }

      this.internalField0019.set(false);
      this.internalMethod09786();
      ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("commands.config.reset")));
   }

   public void internalMethod07826() {
      this.internalMethod09777();
      List<Packets.InternalType0490> localValue1 = this.internalField0416;
      if (localValue1.isEmpty()) {
         ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("config.not_found")));
         return;
      }

      ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("config.list")));
      int localValue2 = 1;
      for (Packets.InternalType0490 localValue4 : localValue1) {
         String localValue5 = localValue4.name();
         String localValue6 = ".cfg load \"" + localValue5.replace("\"", "\\\"") + "\"";
         MutableText localValue7 = Text.literal("[" + localValue2++ + "] ")
            .setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY)))
            .append(
               Text.literal(localValue5).setStyle(
                  Style.EMPTY
                     .withColor(TextColor.fromFormatting(localValue4.active() ? Formatting.GREEN : Formatting.AQUA))
                     .withClickEvent(new ClickEvent.RunCommand(localValue6))
                     .withHoverEvent(new HoverEvent.ShowText(Text.literal(LanguageManager.internalMethod07214("config.hover_load"))))
               )
            );
         if (localValue4.active()) {
            localValue7.append(Text.literal(" ✓").setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GREEN))));
         }
         ClientMessages.internalMethod07664(localValue7);
      }
   }

   public List<Packets.InternalType0490> internalMethod01753() {
      return this.internalField0416;
   }

   public List<String> internalMethod07433() {
      ArrayList<String> localValue1 = new ArrayList<>();
      for (Packets.InternalType0490 localValue3 : this.internalField0416) {
         localValue1.add(localValue3.name());
      }
      return localValue1;
   }

   public String internalMethod02481() {
      return this.internalField0247;
   }

   public boolean internalMethod06090(String localValue1) {
      return this.internalMethod02543(localValue1) != null;
   }

   public void internalMethod09777() {
      synchronized (this.internalField0961) {
         try {
            Path localValue1 = this.internalMethod09007();
            Files.createDirectories(localValue1);
            String localValue2 = this.internalMethod09004();
            HashMap<String, InternalType0001> localValue3 = new HashMap<>();

            try (Stream<Path> localValue4 = Files.list(localValue1)) {
               for (Path localValue5 : localValue4.filter(Files::isRegularFile)
                  .filter(localValue0 -> localValue0.getFileName().toString().endsWith(".json"))
                  .sorted()
                  .toList()) {
                  try {
                     JsonObject localValue6 = this.internalMethod09011(localValue5);
                     String localValue7 = localValue6.has("name") ? localValue6.get("name").getAsString().trim() : "";
                     if (localValue7.isBlank()) {
                        RockstarClient.internalField0572.warn("Config: skipped nameless local file {}", localValue5.getFileName());
                        continue;
                     }
                     JsonObject localValue8 = this.internalMethod09012(localValue6);
                     long localValue9 = localValue6.has("id") ? localValue6.get("id").getAsLong() : this.internalMethod09013();
                     localValue3.putIfAbsent(
                        localValue7.toLowerCase(Locale.ROOT),
                        new InternalType0001(localValue9, localValue7, localValue5, localValue8)
                     );
                  } catch (Exception localValue11) {
                     RockstarClient.internalField0572.warn(
                        "Config: skipped unreadable local file {}: {}",
                        localValue5.getFileName(),
                        localValue11.getMessage()
                     );
                  }
               }
            }

            ArrayList<InternalType0001> localValue12 = new ArrayList<>(localValue3.values());
            localValue12.sort(Comparator.comparing(InternalType0001::name, String.CASE_INSENSITIVE_ORDER));
            InternalType0001 localValue13 = localValue2 == null ? null : localValue3.get(localValue2.toLowerCase(Locale.ROOT));
            if (localValue2 != null && localValue13 == null) {
               this.internalMethod09006();
            }

            ArrayList<Packets.InternalType0490> localValue14 = new ArrayList<>();
            HashMap<String, Long> localValue15 = new HashMap<>();
            for (InternalType0001 localValue16 : localValue12) {
               boolean localValue17 = localValue13 != null && localValue16.id() == localValue13.id();
               localValue14.add(new Packets.InternalType0490(localValue16.id(), localValue16.name(), localValue17));
               localValue15.put(localValue16.name().toLowerCase(Locale.ROOT), localValue16.id());
            }

            this.internalField0416 = List.copyOf(localValue14);
            this.internalField0543 = Map.copyOf(localValue15);
            this.internalField0544 = Map.copyOf(localValue3);
            this.internalField0361 = localValue13 == null ? null : localValue13.id();
            this.internalField0247 = localValue13 == null ? null : localValue13.name();
         } catch (Exception localValue18) {
            RockstarClient.internalField0572.error("Config: failed to refresh local config index", localValue18);
         }
      }
   }

   private InternalType0001 internalMethod02543(String localValue1) {
      return internalMethod04633(localValue1) ? null : this.internalField0544.get(localValue1.trim().toLowerCase(Locale.ROOT));
   }

   private void internalMethod09003(String localValue1, JsonObject localValue2, boolean localValue3, boolean localValue4) throws IOException {
      synchronized (this.internalField0961) {
         this.internalMethod09777();
         InternalType0001 localValue5 = this.internalMethod02543(localValue1);
         long localValue6 = localValue5 == null ? this.internalMethod09013() : localValue5.id();
         Path localValue7 = this.internalMethod09008(localValue1);
         if (localValue3 && Files.isRegularFile(localValue7)) {
            Files.copy(localValue7, this.internalMethod09009(localValue7), StandardCopyOption.REPLACE_EXISTING);
         }
         ConfigManager.internalMethod01467(localValue7.toFile(), this.internalMethod09010(localValue6, localValue1, localValue2));
         if (localValue4) {
            this.internalMethod09005(localValue1);
         }
         this.internalMethod09777();
      }
   }

   private String internalMethod09004() {
      Path localValue1 = this.internalMethod09007().resolve(internalField0249);
      if (!Files.isRegularFile(localValue1)) {
         return null;
      }
      try {
         String localValue2 = Files.readString(localValue1, StandardCharsets.UTF_8).trim();
         return localValue2.isBlank() ? null : localValue2;
      } catch (IOException localValue3) {
         RockstarClient.internalField0572.warn("Config: failed to read active config marker: {}", localValue3.getMessage());
         return null;
      }
   }

   private void internalMethod09005(String localValue1) throws IOException {
      ConfigManager.internalMethod04682(this.internalMethod09007().resolve(internalField0249).toFile(), localValue1);
   }

   private void internalMethod09006() throws IOException {
      Files.deleteIfExists(this.internalMethod09007().resolve(internalField0249));
      this.internalField0361 = null;
      this.internalField0247 = null;
   }

   private Path internalMethod09007() {
      return ConfigManager.internalField0148.toPath().resolve(internalField0248);
   }

   private Path internalMethod09008(String localValue1) {
      String localValue2 = localValue1.trim().toLowerCase(Locale.ROOT);
      String localValue3 = Base64.getUrlEncoder().withoutPadding().encodeToString(localValue2.getBytes(StandardCharsets.UTF_8));
      return this.internalMethod09007().resolve(localValue3 + ".json");
   }

   private Path internalMethod09009(Path localValue1) {
      return localValue1.resolveSibling(localValue1.getFileName().toString() + ".bak");
   }

   private JsonObject internalMethod09010(long localValue1, String localValue2, JsonObject localValue3) {
      JsonObject localValue4 = new JsonObject();
      localValue4.addProperty("format", 1);
      localValue4.addProperty("id", localValue1);
      localValue4.addProperty("name", localValue2.trim());
      localValue4.add("data", localValue3.deepCopy());
      return localValue4;
   }

   private JsonObject internalMethod09011(Path localValue1) throws IOException {
      JsonElement localValue2 = JsonParser.parseString(Files.readString(localValue1, StandardCharsets.UTF_8));
      if (!localValue2.isJsonObject()) {
         throw new IOException("root is not a JSON object");
      }
      return localValue2.getAsJsonObject();
   }

   private JsonObject internalMethod09012(JsonObject localValue1) throws IOException {
      JsonElement localValue2 = localValue1.get("data");
      if (localValue2 == null || !localValue2.isJsonObject()) {
         throw new IOException("missing config data object");
      }
      return localValue2.getAsJsonObject().deepCopy();
   }

   private long internalMethod09013() {
      return ThreadLocalRandom.current().nextLong(1L, Long.MAX_VALUE);
   }

   private void internalMethod09787() {
      ScheduledFuture<?> localValue1 = this.internalField0825;
      if (localValue1 != null) {
         localValue1.cancel(false);
      }
      this.internalField0825 = null;
   }

   private static boolean internalMethod04633(String localValue0) {
      return localValue0 == null || localValue0.isBlank();
   }

   private void internalMethod08257(String localValue1) {
      ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("config.not_found")));
   }

   private void internalMethod09001(String localValue1) {
      ClientMessages.internalMethod09025(Text.literal(localValue1));
   }

   private record InternalType0001(long id, String name, Path path, JsonObject data) {
   }
}

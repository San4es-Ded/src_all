package rockstar.client.internal.script;







import rockstar.client.notification.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.bot.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import pyrock.events.game.WorldChangeEvent;

public class BotRosterManager {
   private static BotRosterManager internalField0169;
   private final Map<String, BotTargetManager> internalField0543 = new ConcurrentHashMap<>();
   private final BotConfig internalField0171 = new BotConfig();
   private final Map<String, BotRosterManager.InternalType0456> internalField0544 = new ConcurrentHashMap<>();
   private final Set<String> internalField0546 = ConcurrentHashMap.newKeySet();
   private final Map<String, Long> internalField1197 = new ConcurrentHashMap<>();
   private final Pattern internalField0293 = Pattern.compile("(?:(?:\u0431\u0430\u043b\u0430\u043d\u0441)|balance)\\s*[:=]?\\s*\\$?\\s*([\\d\\s.,]+)", 66);
   private final ScheduledExecutorService internalField0220 = Executors.newScheduledThreadPool(2, new ThreadFactory() {
      private int internalField0227;

      @Override
      public synchronized Thread newThread(Runnable localValue1) {
         Thread localValue2 = new Thread(localValue1, "Rockstar-BotManager-" + ++this.internalField0227);
         localValue2.setDaemon(true);
         return localValue2;
      }
   });
   private long internalField0229;
   private boolean internalField0277 = true;
   private volatile BotTargetManager internalField0716;
   private final EventListener<WorldChangeEvent> internalField0157 = localValue1 -> this.internalMethod07763();

   private BotRosterManager() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   public static BotRosterManager internalMethod03065() {
      if (internalField0169 == null) {
         internalField0169 = new BotRosterManager();
      }

      return internalField0169;
   }

   public BotTargetManager internalMethod05974(String localValue1) {
      BotRosterManager.InternalType0457 localValue2 = this.internalMethod00130();
      return localValue2 == null ? null : this.internalMethod04594(localValue1, localValue2.internalMethod05503(), localValue2.internalMethod03719());
   }

   public BotTargetManager internalMethod04594(String localValue1, String localValue2, int localValue3) {
      if (localValue1 != null && !localValue1.isBlank() && localValue2 != null && !localValue2.isBlank()) {
         String localValue4 = this.internalMethod03699(localValue1);
         if (this.internalField0543.containsKey(localValue4)) {
            return null;
         } else {
            BotConfig localValue5 = new BotConfig();
            localValue5.internalMethod02008(this.internalField0171);
            BotTargetManager localValue6 = new BotTargetManager(localValue1, localValue5);
            String localValue7 = localValue2.trim();
            localValue6.internalMethod04050()
               .internalMethod01814(
                  localValue2x -> {
                     if (this.internalField0171.internalMethod07716()) {
                        RockstarClient.getInstance()
                           .internalMethod02503()
                           .internalMethod02784(
                              new DetailedNotification(
                                 NotificationType.internalField0704,
                                 LanguageManager.internalMethod07214("bot.notification.title"),
                                 LanguageManager.internalMethod00160("bot.notification.connected", localValue1)
                              )
                           );
                     }
                  }
               );
            localValue6.internalMethod04050()
               .internalMethod03974(
                  localValue3x -> {
                     RockstarClient.internalField0572.error("Bot {} error: {}", localValue1, localValue3x);
                     this.internalField0543.remove(localValue4);
                     if (this.internalField0171.internalMethod07716()) {
                        RockstarClient.getInstance()
                           .internalMethod02503()
                           .internalMethod02784(
                              new DetailedNotification(
                                 NotificationType.internalField0705,
                                 LanguageManager.internalMethod07214("bot.notification.title"),
                                 LanguageManager.internalMethod00160("bot.notification.error", localValue1, localValue3x)
                              )
                           );
                     }
                  }
               );
            localValue6.internalMethod04050()
               .internalMethod08394(
                  localValue4x -> {
                     this.internalField0543.remove(localValue4);
                     if (localValue6.internalMethod07731() && !localValue6.internalMethod07734()) {
                        this.internalMethod06111(localValue6);
                     }

                     if (this.internalField0171.internalMethod07716()) {
                        RockstarClient.getInstance()
                           .internalMethod02503()
                           .internalMethod02784(
                              new DetailedNotification(
                                 NotificationType.internalField0705,
                                 LanguageManager.internalMethod07214("bot.notification.title"),
                                 LanguageManager.internalMethod00160("bot.notification.disconnected", localValue1)
                              )
                           );
                     }
                  }
               );
            this.internalField0543.put(localValue4, localValue6);
            localValue6.internalMethod02830(localValue7, localValue3);
            return localValue6;
         }
      } else {
         return null;
      }
   }

   public void internalMethod00543(String localValue1) {
      String localValue2 = this.internalMethod03699(localValue1);
      this.internalField0546.remove(localValue2);
      this.internalField1197.remove(localValue2);
      BotTargetManager localValue3 = this.internalField0543.remove(localValue2);
      if (localValue3 != null) {
         localValue3.internalMethod04572();
      }
   }

   public void internalMethod02922() {
      for (BotTargetManager localValue2 : this.internalField0543.values()) {
         localValue2.internalMethod04572();
      }

      this.internalField0543.clear();
   }

   public Optional<BotTargetManager> internalMethod05101(String localValue1) {
      return Optional.ofNullable(this.internalField0543.get(this.internalMethod03699(localValue1)));
   }

   public Collection<BotTargetManager> internalMethod01257() {
      return Collections.unmodifiableCollection(this.internalField0543.values());
   }

   public int internalMethod02921() {
      return this.internalField0543.size();
   }

   public void internalMethod02924() {
      if (this.internalField0716 != null && !this.internalField0716.internalMethod07697()) {
         this.internalMethod07764();
      }

      for (BotTargetManager localValue2 : this.internalField0543.values()) {
         localValue2.internalMethod04579();
      }

      this.internalMethod07779();
      this.internalMethod07765();
      this.internalMethod07777();
   }

   public void internalMethod07763() {
      this.internalMethod07764();
      this.internalMethod02922();
   }

   public void internalMethod03985(Consumer<BotTargetManager> localValue1) {
      this.internalField0543.values().forEach(localValue1);
   }

   public void internalMethod01823(Consumer<BotTargetManager> localValue1) {
      this.internalField0543.values().stream().filter(BotTargetManager::internalMethod07697).forEach(localValue1);
   }

   public void internalMethod00445(boolean localValue1) {
      this.internalField0171.internalMethod09032(localValue1);
   }

   public boolean internalMethod02923() {
      return this.internalField0171.internalMethod07720();
   }

   public void internalMethod00497(boolean localValue1) {
      this.internalField0171.internalMethod07787(localValue1);
      this.internalField0229 = System.currentTimeMillis();
      this.internalField0277 = true;
   }

   public boolean internalMethod02925() {
      return this.internalField0171.internalMethod07748();
   }

   public void internalMethod07132(String localValue1) {
      if (localValue1 != null && !localValue1.isBlank()) {
         this.internalField0171.internalMethod07870(localValue1.trim());
      }
   }

   public void internalMethod00444(int localValue1) {
      if (localValue1 > 0) {
         this.internalField0171.internalMethod09667(TimeUnit.MINUTES.toMillis(localValue1));
      }
   }

   public void internalMethod07760(String localValue1) {
      if (localValue1 != null && !localValue1.isBlank()) {
         this.internalField0171.internalMethod08131(localValue1);
      }
   }

   public void internalMethod09047(String localValue1) {
      this.internalMethod05101(localValue1).ifPresent(localValue0 -> localValue0.internalMethod05456(new VelocityDecayBehavior()));
   }

   public boolean internalMethod00544(String localValue1) {
      Optional localValue2 = this.internalMethod05101(localValue1);
      if (!localValue2.isEmpty() && ((BotTargetManager)localValue2.get()).internalMethod07697()) {
         this.internalMethod07764();
         this.internalField0716 = (BotTargetManager)localValue2.get();
         this.internalField0716.internalMethod05456(new VelocityDecayBehavior());
         if (!this.internalField0716.internalMethod00272().internalMethod02294(MinecraftClient.getInstance())) {
            this.internalField0716 = null;
            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean internalMethod07764() {
      if (this.internalField0716 == null) {
         return false;
      } else {
         this.internalField0716.internalMethod00272().internalMethod02293(MinecraftClient.getInstance());
         this.internalField0716 = null;
         return true;
      }
   }

   public Optional<BotTargetManager> internalMethod05204() {
      return Optional.ofNullable(this.internalField0716);
   }

   public boolean internalMethod07133(String localValue1) {
      Optional localValue2 = this.internalMethod05101(localValue1);
      return !localValue2.isEmpty() && ((BotTargetManager)localValue2.get()).internalMethod07697() ? this.internalField0546.add(this.internalMethod03699(localValue1)) : false;
   }

   public boolean internalMethod07761(String localValue1) {
      String localValue2 = this.internalMethod03699(localValue1);
      this.internalField1197.remove(localValue2);
      return this.internalField0546.remove(localValue2);
   }

   public boolean internalMethod09048(String localValue1) {
      return this.internalField0546.contains(this.internalMethod03699(localValue1));
   }

   public void internalMethod03661(String localValue1, String localValue2) {
      if (localValue1 != null && localValue2 != null && !localValue2.isBlank()) {
         this.internalField0544.put(this.internalMethod03699(localValue1), new BotRosterManager.InternalType0456(localValue1, localValue2));
      }
   }

   public boolean internalMethod07003(BotTargetManager localValue1, String localValue2) {
      if (localValue1 != null && localValue2 != null) {
         BotRosterManager.InternalType0456 localValue3 = this.internalField0544.get(this.internalMethod03699(localValue1.internalMethod03426()));
         if (localValue3 == null) {
            return false;
         } else {
            Long localValue4 = this.internalMethod00179(localValue2);
            if (localValue4 == null) {
               return false;
            } else {
               this.internalField0544.remove(this.internalMethod03699(localValue1.internalMethod03426()));
               if (localValue4 > 0L && !localValue3.internalMethod03003().equalsIgnoreCase(localValue1.internalMethod03426())) {
                  this.internalField0220.schedule(() -> localValue1.internalMethod04382("/pay " + localValue3.internalMethod03003() + " " + localValue4), 5L, TimeUnit.SECONDS);
                  return true;
               } else {
                  return true;
               }
            }
         }
      } else {
         return false;
      }
   }

   public static BotRosterManager.InternalType0457 internalMethod00405(String localValue0) {
      if (localValue0 != null && !localValue0.isBlank()) {
         String localValue1 = localValue0.trim();
         int localValue2 = 25565;
         int localValue3 = localValue1.lastIndexOf(58);
         if (localValue3 > 0 && localValue3 < localValue1.length() - 1) {
            try {
               localValue2 = Integer.parseInt(localValue1.substring(localValue3 + 1));
               localValue1 = localValue1.substring(0, localValue3);
            } catch (NumberFormatException localValue5) {
            }
         }

         return !localValue1.isBlank() && localValue2 >= 1 && localValue2 <= 65535 ? new BotRosterManager.InternalType0457(localValue1, localValue2) : null;
      } else {
         return null;
      }
   }

   private void internalMethod07765() {
      if (this.internalField0171.internalMethod07720()) {
         long localValue1 = System.currentTimeMillis();
         this.internalMethod01823(localValue3 -> {
            if (localValue3.internalMethod00259(localValue1)) {
               localValue3.internalMethod04382(this.internalField0171.internalMethod08526());
               localValue3.internalMethod00258(localValue1);
            }
         });
      }
   }

   private void internalMethod07777() {
      if (this.internalField0171.internalMethod07748()) {
         long localValue1 = System.currentTimeMillis();
         if (localValue1 - this.internalField0229 >= this.internalField0171.internalMethod09201()) {
            this.internalField0229 = localValue1;
            double localValue3 = this.internalField0171.internalMethod10143() * (this.internalField0277 ? 1.0 : -1.0);
            this.internalMethod01823(localValue2 -> localValue2.internalMethod00255(localValue3));
            this.internalField0277 = !this.internalField0277;
         }
      }
   }

   private void internalMethod07779() {
      if (!this.internalField0546.isEmpty()) {
         long localValue1 = System.currentTimeMillis();
         this.internalField0546.removeIf(localValue3 -> {
            BotTargetManager localValue4 = this.internalField0543.get(localValue3);
            if (localValue4 != null && localValue4.internalMethod07697()) {
               long localValue5 = this.internalField1197.getOrDefault(localValue3, 0L);
               if (localValue1 - localValue5 >= localValue4.internalMethod06687().internalMethod07747()) {
                  localValue4.internalMethod04573();
                  this.internalField1197.put(localValue3, localValue1);
               }

               return false;
            } else {
               this.internalField1197.remove(localValue3);
               return true;
            }
         });
      }
   }

   private void internalMethod06111(BotTargetManager localValue1) {
      if (localValue1.internalMethod06925() != null) {
         long localValue2 = Math.max(50L, localValue1.internalMethod04570() * 50L);
         this.internalField0220.schedule(() -> {
            if (!this.internalField0543.containsKey(this.internalMethod03699(localValue1.internalMethod03426()))) {
               BotTargetManager localValue2x = this.internalMethod04594(localValue1.internalMethod03426(), localValue1.internalMethod06925(), localValue1.internalMethod04577());
               if (localValue2x != null) {
                  localValue2x.internalMethod09131(true);
                  localValue2x.internalMethod08813(localValue1.internalMethod04570());
               }
            }
         }, localValue2, TimeUnit.MILLISECONDS);
      }
   }

   private BotRosterManager.InternalType0457 internalMethod00130() {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      return localValue1.getNetworkHandler() != null && localValue1.getCurrentServerEntry() != null ? internalMethod00405(localValue1.getCurrentServerEntry().address) : null;
   }

   private Long internalMethod00179(String localValue1) {
      Matcher localValue2 = this.internalField0293.matcher(localValue1);
      if (!localValue2.find()) {
         return null;
      } else {
         String localValue3 = localValue2.group(1);
         if (localValue3 == null) {
            return null;
         } else {
            localValue3 = localValue3.replaceAll("[^0-9]", "");
            if (localValue3.isEmpty()) {
               return null;
            } else {
               try {
                  return Long.parseLong(localValue3);
               } catch (NumberFormatException localValue5) {
                  return null;
               }
            }
         }
      }
   }

   private String internalMethod03699(String localValue1) {
      return localValue1 == null ? "" : localValue1.trim().toLowerCase(Locale.ROOT);
   }

   @Generated
   public Map<String, BotTargetManager> internalMethod01560() {
      return this.internalField0543;
   }

   @Generated
   public BotConfig internalMethod03066() {
      return this.internalField0171;
   }

   static final class InternalType0456 {
      private final String internalField0248;
      private final String internalField0247;

      InternalType0456(String localValue1, String localValue2) {
         this.internalField0248 = localValue1;
         this.internalField0247 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0456[botName=" + this.internalField0248 + ", targetPlayer=" + this.internalField0247 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0247);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         BotRosterManager.InternalType0456 other = (BotRosterManager.InternalType0456) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0247, other.internalField0247);
      }

      public String internalMethod03163() {
         return this.internalField0248;
      }

      public String internalMethod03003() {
         return this.internalField0247;
      }
   }

   public static final class InternalType0457 {
      private final String internalField0248;
      private final int internalField0227;

      public InternalType0457(String localValue1, int localValue2) {
         this.internalField0248 = localValue1;
         this.internalField0227 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0457[address=" + this.internalField0248 + ", port=" + this.internalField0227 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         BotRosterManager.InternalType0457 other = (BotRosterManager.InternalType0457) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public String internalMethod05503() {
         return this.internalField0248;
      }

      public int internalMethod03719() {
         return this.internalField0227;
      }
   }
}

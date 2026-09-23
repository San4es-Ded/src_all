package rockstar.client.internal.ui;



import rockstar.client.setting.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public final class SettingHistoryManager {
   static final Deque<SettingHistoryManager.InternalType0278> internalField0796 = new ArrayDeque<>();
   static final Deque<SettingHistoryManager.InternalType0278> internalField0797 = new ArrayDeque<>();
   static SettingHistoryManager.InternalType0070 internalField0051;
   private static boolean internalField0277;

   private SettingHistoryManager() {
   }

   public static void internalMethod06169(Setting localValue0) {
      if (!internalField0277 && localValue0 != null) {
         Screen localValue1 = internalMethod05691();
         if (localValue1 != null) {
            JsonElement localValue2;
            try {
               localValue2 = localValue0.toJson().deepCopy();
            } catch (RuntimeException localValue6) {
               return;
            }

            if (internalField0051 != null) {
               internalField0051.internalMethod04471(localValue1, localValue0, localValue2);
            } else {
               long localValue3 = System.currentTimeMillis();
               SettingHistoryManager.InternalType0278 localValue5 = internalField0796.peekFirst();
               if (localValue5 == null
                  || localValue5.internalMethod03265() != localValue1
                  || localValue5.internalMethod04121().size() != 1
                  || localValue5.internalMethod04121().get(0).internalMethod05440() != localValue0
                  || localValue3 - localValue5.internalMethod02166() >= 350L) {
                  internalMethod03286(
                     internalField0796, new SettingHistoryManager.InternalType0278(localValue1, List.of(new SettingHistoryManager.InternalType0279(localValue0, localValue2)), localValue3)
                  );
                  internalField0797.clear();
               }
            }
         }
      }
   }

   public static SettingHistoryManager.InternalType0070 internalMethod07567() {
      return internalField0051 == null ? new SettingHistoryManager.InternalType0070() : SettingHistoryManager.InternalType0070.internalField0051;
   }

   public static boolean internalMethod07603() {
      return internalMethod07480(internalField0796, internalField0797, null);
   }

   public static boolean internalMethod07605() {
      return internalMethod07480(internalField0797, internalField0796, null);
   }

   public static boolean internalMethod06205(SettingOwner localValue0) {
      return internalMethod07480(internalField0796, internalField0797, localValue0 == null ? null : List.of(localValue0));
   }

   public static boolean internalMethod03022(SettingOwner localValue0) {
      return internalMethod07480(internalField0797, internalField0796, localValue0 == null ? null : List.of(localValue0));
   }

   public static boolean internalMethod03741(Collection<? extends SettingOwner> localValue0) {
      return internalMethod07480(internalField0796, internalField0797, localValue0);
   }

   public static boolean internalMethod05652(Collection<? extends SettingOwner> localValue0) {
      return internalMethod07480(internalField0797, internalField0796, localValue0);
   }

   public static boolean internalMethod08342(SettingOwner localValue0) {
      return internalMethod00704(internalField0796, localValue0 == null ? null : List.of(localValue0));
   }

   public static boolean internalMethod07684(SettingOwner localValue0) {
      return internalMethod00704(internalField0797, localValue0 == null ? null : List.of(localValue0));
   }

   public static boolean internalMethod08138(Collection<? extends SettingOwner> localValue0) {
      return internalMethod00704(internalField0796, localValue0);
   }

   public static boolean internalMethod08476(Collection<? extends SettingOwner> localValue0) {
      return internalMethod00704(internalField0797, localValue0);
   }

   public static long internalMethod06204(SettingOwner localValue0) {
      return internalMethod00703(internalField0796, localValue0 == null ? null : List.of(localValue0));
   }

   public static long internalMethod03021(SettingOwner localValue0) {
      return internalMethod00703(internalField0797, localValue0 == null ? null : List.of(localValue0));
   }

   private static boolean internalMethod00704(Deque<SettingHistoryManager.InternalType0278> localValue0, Collection<? extends SettingOwner> localValue1) {
      Screen localValue2 = internalMethod05691();
      if (localValue2 == null) {
         return false;
      } else {
         for (SettingHistoryManager.InternalType0278 localValue4 : localValue0) {
            if (localValue4.internalMethod03265() == localValue2 && internalMethod06508(localValue4, localValue1)) {
               return true;
            }
         }

         return false;
      }
   }

   private static long internalMethod00703(Deque<SettingHistoryManager.InternalType0278> localValue0, Collection<? extends SettingOwner> localValue1) {
      Screen localValue2 = internalMethod05691();
      if (localValue2 == null) {
         return Long.MIN_VALUE;
      } else {
         for (SettingHistoryManager.InternalType0278 localValue4 : localValue0) {
            if (localValue4.internalMethod03265() == localValue2 && internalMethod06508(localValue4, localValue1)) {
               return localValue4.internalMethod02166();
            }
         }

         return Long.MIN_VALUE;
      }
   }

   private static boolean internalMethod07480(
      Deque<SettingHistoryManager.InternalType0278> localValue0, Deque<SettingHistoryManager.InternalType0278> localValue1, Collection<? extends SettingOwner> localValue2
   ) {
      Screen localValue3 = internalMethod05691();
      if (localValue3 == null) {
         return false;
      } else {
         Iterator localValue4 = localValue0.iterator();

         while (localValue4.hasNext()) {
            SettingHistoryManager.InternalType0278 localValue5 = (SettingHistoryManager.InternalType0278)localValue4.next();
            if (localValue5.internalMethod03265() == localValue3 && internalMethod06508(localValue5, localValue2)) {
               localValue4.remove();
               ArrayList localValue6 = new ArrayList(localValue5.internalMethod04121().size());

               for (SettingHistoryManager.InternalType0279 localValue8 : localValue5.internalMethod04121()) {
                  try {
                     localValue6.add(new SettingHistoryManager.InternalType0279(localValue8.internalMethod05440(), localValue8.internalMethod05440().toJson().deepCopy()));
                  } catch (RuntimeException localValue13) {
                  }
               }

               boolean localValue15 = localValue6.size() != localValue5.internalMethod04121().size();

               for (int localValue16 = 0; !localValue15 && localValue16 < localValue6.size(); localValue16++) {
                  localValue15 = !((SettingHistoryManager.InternalType0279)localValue6.get(localValue16))
                     .internalMethod07520()
                     .equals(localValue5.internalMethod04121().get(localValue16).internalMethod07520());
               }

               if (localValue15) {
                  internalField0277 = true;

                  try {
                     for (SettingHistoryManager.InternalType0279 localValue9 : localValue5.internalMethod04121()) {
                        localValue9.internalMethod05440().fromJson(localValue9.internalMethod07520().deepCopy());
                     }
                  } finally {
                     internalField0277 = false;
                  }

                  if (!localValue6.isEmpty()) {
                     internalMethod03286(localValue1, new SettingHistoryManager.InternalType0278(localValue3, localValue6, localValue5.internalMethod02166()));
                  }

                  return true;
               }
            }
         }

         return false;
      }
   }

   private static boolean internalMethod06508(SettingHistoryManager.InternalType0278 localValue0, Collection<? extends SettingOwner> localValue1) {
      if (localValue1 == null) {
         return true;
      } else if (localValue1.isEmpty()) {
         return false;
      } else {
         for (SettingHistoryManager.InternalType0279 localValue3 : localValue0.internalMethod04121()) {
            if (!(localValue3.internalMethod05440() instanceof AbstractSetting localValue4) || !localValue1.contains(localValue4.internalMethod01453())) {
               return false;
            }
         }

         return true;
      }
   }

   public static void internalMethod03286(Deque<SettingHistoryManager.InternalType0278> localValue0, SettingHistoryManager.InternalType0278 localValue1) {
      localValue0.addFirst(localValue1);

      while (localValue0.size() > 100) {
         localValue0.removeLast();
      }
   }

   private static Screen internalMethod05691() {
      return MinecraftClient.getInstance().currentScreen;
   }

   public static final class InternalType0070 implements AutoCloseable {
      static final SettingHistoryManager.InternalType0070 internalField0051 = new SettingHistoryManager.InternalType0070(true);
      private final IdentityHashMap<Setting, JsonElement> internalField0594 = new IdentityHashMap<>();
      private Screen internalField0691;
      private final boolean internalField0277;
      private boolean internalField0276;

      InternalType0070() {
         this(false);
         SettingHistoryManager.internalField0051 = this;
      }

      private InternalType0070(boolean localValue1) {
         this.internalField0277 = localValue1;
      }

      void internalMethod04471(Screen localValue1, Setting localValue2, JsonElement localValue3) {
         if (!this.internalField0277 && !this.internalField0276 && (this.internalField0691 == null || this.internalField0691 == localValue1)) {
            this.internalField0691 = localValue1;
            this.internalField0594.putIfAbsent(localValue2, localValue3);
         }
      }

      @Override
      public void close() {
         if (!this.internalField0277 && !this.internalField0276) {
            this.internalField0276 = true;
            SettingHistoryManager.internalField0051 = null;
            if (this.internalField0691 != null && !this.internalField0594.isEmpty()) {
               ArrayList localValue1 = new ArrayList(this.internalField0594.size());

               for (Entry localValue3 : this.internalField0594.entrySet()) {
                  localValue1.add(new SettingHistoryManager.InternalType0279((Setting)localValue3.getKey(), (JsonElement)localValue3.getValue()));
               }

               SettingHistoryManager.internalMethod03286(
                  SettingHistoryManager.internalField0796, new SettingHistoryManager.InternalType0278(this.internalField0691, localValue1, System.currentTimeMillis())
               );
               SettingHistoryManager.internalField0797.clear();
            }
         }
      }
   }

   static final class InternalType0278 {
      private final Screen internalField0691;
      private final List<SettingHistoryManager.InternalType0279> internalField0416;
      private final long internalField0229;

      InternalType0278(Screen localValue1, List<SettingHistoryManager.InternalType0279> localValue2, long localValue3) {
         this.internalField0691 = localValue1;
         this.internalField0416 = localValue2;
         this.internalField0229 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0278[screen=" + this.internalField0691 + ", snapshots=" + this.internalField0416 + ", time=" + this.internalField0229 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0691);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         SettingHistoryManager.InternalType0278 other = (SettingHistoryManager.InternalType0278) localValue1;
         return java.util.Objects.equals(this.internalField0691, other.internalField0691)
            && java.util.Objects.equals(this.internalField0416, other.internalField0416)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229);
      }

      public Screen internalMethod03265() {
         return this.internalField0691;
      }

      public List<SettingHistoryManager.InternalType0279> internalMethod04121() {
         return this.internalField0416;
      }

      public long internalMethod02166() {
         return this.internalField0229;
      }
   }

   static final class InternalType0279 {
      private final Setting internalField0644;
      private final JsonElement internalField0469;

      InternalType0279(Setting localValue1, JsonElement localValue2) {
         this.internalField0644 = localValue1;
         this.internalField0469 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0279[setting=" + this.internalField0644 + ", value=" + this.internalField0469 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0644);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0469);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         SettingHistoryManager.InternalType0279 other = (SettingHistoryManager.InternalType0279) localValue1;
         return java.util.Objects.equals(this.internalField0644, other.internalField0644)
            && java.util.Objects.equals(this.internalField0469, other.internalField0469);
      }

      public Setting internalMethod05440() {
         return this.internalField0644;
      }

      public JsonElement internalMethod07520() {
         return this.internalField0469;
      }
   }
}

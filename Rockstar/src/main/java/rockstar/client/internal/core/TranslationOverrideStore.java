package rockstar.client.internal.core;



import rockstar.client.i18n.*;
import rockstar.client.*;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TranslationOverrideStore {
   private static final Map<Language, Map<String, TranslationOverrideStore.InternalType0384>> internalField0543 = new ConcurrentHashMap<>();

   private TranslationOverrideStore() {
   }

   public static void internalMethod01132(Object localValue0, Language localValue1, String localValue2, String localValue3) {
      if (localValue1 != null && localValue2 != null && !localValue2.isBlank() && localValue3 != null) {
         internalField0543.computeIfAbsent(localValue1, localValue0x -> new ConcurrentHashMap<>()).put(localValue2, new TranslationOverrideStore.InternalType0384(localValue0, localValue3));
         LanguageManager.internalMethod05357();
      }
   }

   public static String internalMethod03810(String localValue0) {
      if (!internalField0543.isEmpty() && localValue0 != null) {
         String localValue1 = internalMethod03711(LanguageManager.internalMethod00625(), localValue0);
         if (localValue1 != null) {
            return localValue1;
         } else {
            String localValue2 = internalMethod03711(Language.internalField0165, localValue0);
            if (localValue2 != null) {
               return localValue2;
            } else {
               String localValue3 = internalMethod03711(Language.internalField0164, localValue0);
               if (localValue3 != null) {
                  return localValue3;
               } else {
                  for (Map localValue5 : internalField0543.values()) {
                     TranslationOverrideStore.InternalType0384 localValue6 = (TranslationOverrideStore.InternalType0384)localValue5.get(localValue0);
                     if (localValue6 != null) {
                        return localValue6.internalMethod01458();
                     }
                  }

                  return null;
               }
            }
         }
      } else {
         return null;
      }
   }

   public static boolean internalMethod02060(String localValue0) {
      return internalMethod03810(localValue0) != null;
   }

   public static void internalMethod00186(Object localValue0) {
      boolean localValue1 = false;

      for (Map<String, TranslationOverrideStore.InternalType0384> localValue3 : internalField0543.values()) {
         localValue1 |= localValue3.values().removeIf(localValue1x -> localValue1x.internalMethod01852() == localValue0);
      }

      if (localValue1) {
         LanguageManager.internalMethod05357();
      }
   }

   public static boolean internalMethod06998() {
      return internalField0543.isEmpty();
   }

   public static Language internalMethod00068(String localValue0) {
      if (localValue0 != null && !localValue0.isBlank()) {
         String localValue1 = localValue0.trim().toLowerCase(Locale.ROOT).replace('-', '_');

         for (Language localValue5 : Language.values()) {
            if (localValue5.internalMethod03875().equalsIgnoreCase(localValue1) || localValue5.name().equalsIgnoreCase(localValue1)) {
               return localValue5;
            }
         }

         for (Language localValue9 : Language.values()) {
            if (localValue9.internalMethod03875().startsWith(localValue1 + "_") || localValue9.internalMethod03875().equals(localValue1 + "_" + localValue1)) {
               return localValue9;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private static String internalMethod03711(Language localValue0, String localValue1) {
      Map localValue2 = internalField0543.get(localValue0);
      if (localValue2 == null) {
         return null;
      } else {
         TranslationOverrideStore.InternalType0384 localValue3 = (TranslationOverrideStore.InternalType0384)localValue2.get(localValue1);
         return localValue3 == null ? null : localValue3.internalMethod01458();
      }
   }

   static final class InternalType0384 {
      private final Object internalField0290;
      private final String internalField0248;

      InternalType0384(Object localValue1, String localValue2) {
         this.internalField0290 = localValue1;
         this.internalField0248 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0384[owner=" + this.internalField0290 + ", value=" + this.internalField0248 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0290);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         TranslationOverrideStore.InternalType0384 other = (TranslationOverrideStore.InternalType0384) localValue1;
         return java.util.Objects.equals(this.internalField0290, other.internalField0290)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248);
      }

      public Object internalMethod01852() {
         return this.internalField0290;
      }

      public String internalMethod01458() {
         return this.internalField0248;
      }
   }
}

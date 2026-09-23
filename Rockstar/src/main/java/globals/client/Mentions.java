package globals.client;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Mentions {
   private static final Pattern MENTION = Pattern.compile("(?<![A-Za-z0-9_])@([A-Za-z0-9_]{3,20})");
   private static final Pattern TYPING = Pattern.compile("(?<![A-Za-z0-9_])@([A-Za-z0-9_]{0,20})$");

   private Mentions() {
   }

   public static boolean mentions(String localValue0, String localValue1) {
      if (localValue0 != null && localValue1 != null && !localValue1.isBlank() && localValue0.indexOf(64) >= 0) {
         Matcher localValue2 = MENTION.matcher(localValue0);

         while (localValue2.find()) {
            if (localValue2.group(1).equalsIgnoreCase(localValue1)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean mentionsMe(String localValue0) {
      GlobalsUser localValue1 = Information.getPreferUser();
      return localValue1 != null && mentions(localValue0, localValue1.username());
   }

   public static boolean isMe(String localValue0) {
      GlobalsUser localValue1 = Information.getPreferUser();
      return localValue1 != null && localValue1.username().equalsIgnoreCase(localValue0);
   }

   public static List<Mentions.InternalType0355> split(String localValue0) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         if (localValue0.indexOf(64) < 0) {
            return List.of(new Mentions.InternalType0355(localValue0, null));
         } else {
            ArrayList localValue1 = new ArrayList();
            Matcher localValue2 = MENTION.matcher(localValue0);

            int localValue3;
            for (localValue3 = 0; localValue2.find(); localValue3 = localValue2.end()) {
               if (localValue2.start() > localValue3) {
                  localValue1.add(new Mentions.InternalType0355(localValue0.substring(localValue3, localValue2.start()), null));
               }

               localValue1.add(new Mentions.InternalType0355(localValue2.group(), localValue2.group(1)));
            }

            if (localValue3 < localValue0.length()) {
               localValue1.add(new Mentions.InternalType0355(localValue0.substring(localValue3), null));
            }

            return localValue1;
         }
      } else {
         return List.of();
      }
   }

   public static boolean possible(String localValue0) {
      return localValue0 != null && localValue0.indexOf(64) >= 0;
   }

   public static String typed(String localValue0) {
      if (localValue0 != null && localValue0.indexOf(64) >= 0) {
         Matcher localValue1 = TYPING.matcher(localValue0);
         return localValue1.find() ? localValue1.group(1) : null;
      } else {
         return null;
      }
   }

   public static String complete(String localValue0, String localValue1) {
      if (localValue0 != null && localValue1 != null) {
         Matcher localValue2 = TYPING.matcher(localValue0);
         return !localValue2.find() ? localValue0 : localValue0.substring(0, localValue2.start()) + "@" + localValue1 + " ";
      } else {
         return localValue0;
      }
   }

   public static final class InternalType0355 {
      private final String text;
      private final String mention;

      public InternalType0355(String localValue1, String localValue2) {
         this.text = localValue1;
         this.mention = localValue2;
      }

      public boolean isMention() {
         return this.mention != null;
      }

      @Override
      public final String toString() {
         return "InternalType0355[text=" + this.text() + ", mention=" + this.mention() + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.text());
         result = 31 * result + java.util.Objects.hashCode(this.mention());
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         Mentions.InternalType0355 other = (Mentions.InternalType0355) localValue1;
         return java.util.Objects.equals(this.text(), other.text())
            && java.util.Objects.equals(this.mention(), other.mention());
      }

      public String text() {
         return this.text;
      }

      public String mention() {
         return this.mention;
      }
   }
}

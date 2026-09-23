package rockstar.client.internal.script;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;

public class LyricsTrack {
   private static final Pattern internalField0293 = Pattern.compile("\\[(\\d{1,3}):(\\d{2})[.,](\\d{2,3})]");
   private static final Pattern internalField0294 = Pattern.compile("<(\\d{1,3}):(\\d{2})[.,](\\d{2,3})>");
   private final List<LyricsTrack.InternalType0316> internalField0416;
   private final boolean internalField0277;

   private LyricsTrack(List<LyricsTrack.InternalType0316> localValue1, boolean localValue2) {
      this.internalField0416 = Collections.unmodifiableList(localValue1);
      this.internalField0277 = localValue2;
   }

   public static LyricsTrack internalMethod01213(String localValue0) {
      if (localValue0 != null && !localValue0.isBlank()) {
         ArrayList localValue1 = new ArrayList();

         for (String localValue5 : localValue0.split("\n")) {
            String localValue6 = localValue5.trim();
            Matcher localValue7 = internalField0293.matcher(localValue6);
            ArrayList<Long> localValue8 = new ArrayList<>();

            int localValue9;
            for (localValue9 = 0; localValue7.find() && localValue7.start() == localValue9; localValue9 = localValue7.end()) {
               localValue8.add(internalMethod06039(localValue7));
            }

            if (!localValue8.isEmpty()) {
               String localValue10 = localValue6.substring(localValue9).strip();
               Matcher localValue11 = internalField0294.matcher(localValue10);
               ArrayList localValue12 = new ArrayList();
               StringBuilder localValue13 = new StringBuilder();

               int localValue14;
               for (localValue14 = 0; localValue11.find(); localValue14 = localValue11.end()) {
                  localValue13.append(localValue10, localValue14, localValue11.start());
                  localValue12.add(new LyricsTrack.InternalType0315(internalMethod06039(localValue11), Character.codePointCount(localValue13, 0, localValue13.length())));
               }

               localValue13.append(localValue10, localValue14, localValue10.length());
               String localValue15 = localValue13.toString().strip();
               if (!localValue15.isEmpty()) {
                   for (long localValue17 : localValue8) {
                     localValue1.add(new LyricsTrack.InternalType0316(localValue17, localValue15, localValue12));
                  }
               }
            }
         }

         Collections.sort(localValue1);
         return new LyricsTrack(localValue1, true);
      } else {
         return internalMethod02903();
      }
   }

   public static LyricsTrack internalMethod02937(String localValue0) {
      if (localValue0 != null && !localValue0.isBlank()) {
         ArrayList localValue1 = new ArrayList();

         for (String localValue5 : localValue0.split("\n")) {
            String localValue6 = localValue5.trim();
            if (!localValue6.isEmpty()) {
               localValue1.add(new LyricsTrack.InternalType0316(-1L, localValue6, List.of()));
            }
         }

         return new LyricsTrack(localValue1, false);
      } else {
         return internalMethod02903();
      }
   }

   public static LyricsTrack internalMethod02903() {
      return new LyricsTrack(List.of(), false);
   }

   public boolean internalMethod00819() {
      return this.internalField0416.isEmpty();
   }

   public int internalMethod06143(long localValue1) {
      if (this.internalField0277 && !this.internalField0416.isEmpty()) {
         int localValue3 = 0;
         int localValue4 = this.internalField0416.size() - 1;
         int localValue5 = -1;

         while (localValue3 <= localValue4) {
            int localValue6 = localValue3 + localValue4 >>> 1;
            if (this.internalField0416.get(localValue6).internalMethod01998() <= localValue1) {
               localValue5 = localValue6;
               localValue3 = localValue6 + 1;
            } else {
               localValue4 = localValue6 - 1;
            }
         }

         return localValue5;
      } else {
         return -1;
      }
   }

   public int internalMethod05414(double localValue1, long localValue3, long localValue5) {
      int localValue7 = this.internalMethod06143((long)localValue1);
      if (localValue7 >= 0 && !internalMethod00879(this.internalField0416.get(localValue7).internalMethod00756())) {
         long localValue8 = this.internalMethod02522(localValue7, localValue3);
         if (localValue1 <= localValue8) {
            return localValue7;
         } else {
            long localValue10 = this.internalMethod06142(localValue7);
            if (localValue10 != Long.MAX_VALUE && localValue10 - localValue8 <= localValue5 * 2L) {
               return localValue7;
            } else {
               return localValue1 - localValue8 <= localValue5 ? localValue7 : -1;
            }
         }
      } else {
         return -1;
      }
   }

   public float internalMethod05423(int localValue1, double localValue2, long localValue4) {
      return this.internalField0277 && localValue1 >= 0 && localValue1 < this.internalField0416.size()
         ? this.internalMethod08855(localValue1, localValue2, this.internalMethod00795(localValue1, localValue4))
         : 0.0F;
   }

   public float internalMethod02234(int localValue1, double localValue2, long localValue4) {
      return this.internalField0277 && localValue1 >= 0 && localValue1 < this.internalField0416.size()
         ? this.internalMethod08855(localValue1, localValue2, this.internalMethod02522(localValue1, localValue4))
         : 0.0F;
   }

   private float internalMethod08855(int localValue1, double localValue2, long localValue4) {
      LyricsTrack.InternalType0316 localValue6 = this.internalField0416.get(localValue1);
      long localValue7 = localValue6.internalMethod01998();
      if (localValue2 <= localValue7) {
         return 0.0F;
      } else if (localValue2 >= localValue4) {
         return 1.0F;
      } else if (localValue6.internalMethod00629().isEmpty()) {
         return (float)((localValue2 - localValue7) / ((double)localValue4 - localValue7));
      } else {
         long localValue9 = localValue7;
         int localValue11 = 0;
         int localValue12 = localValue6.internalMethod00756().codePointCount(0, localValue6.internalMethod00756().length());

         for (LyricsTrack.InternalType0315 localValue14 : localValue6.internalMethod00629()) {
            if (localValue14.internalMethod00233() > localValue2) {
               return internalMethod03636(localValue12, localValue2, localValue9, localValue14.internalMethod00233(), localValue11, localValue14.internalMethod00232());
            }

            localValue9 = Math.max(localValue7, localValue14.internalMethod00233());
            localValue11 = Math.clamp((long)localValue14.internalMethod00232(), 0, localValue12);
         }

         return internalMethod03636(localValue12, localValue2, localValue9, localValue4, localValue11, localValue12);
      }
   }

   private long internalMethod00795(int localValue1, long localValue2) {
      long localValue4 = this.internalField0416.get(localValue1).internalMethod01998();
      long localValue6 = this.internalMethod06142(localValue1);
      if (localValue6 != Long.MAX_VALUE) {
         return localValue6;
      } else {
         long localValue8 = localValue4 + this.internalMethod06186(localValue1);
         return localValue2 > localValue4 ? Math.min(localValue2, localValue8) : localValue8;
      }
   }

   private long internalMethod02522(int localValue1, long localValue2) {
      long localValue4 = this.internalField0416.get(localValue1).internalMethod01998();
      long localValue6 = Math.min(this.internalMethod06142(localValue1), localValue4 + this.internalMethod06186(localValue1));
      return localValue2 > localValue4 ? Math.min(localValue2, localValue6) : localValue6;
   }

   private long internalMethod06142(int localValue1) {
      long localValue2 = this.internalField0416.get(localValue1).internalMethod01998();

      for (int localValue4 = localValue1 + 1; localValue4 < this.internalField0416.size(); localValue4++) {
         if (this.internalField0416.get(localValue4).internalMethod01998() > localValue2) {
            return this.internalField0416.get(localValue4).internalMethod01998();
         }
      }

      return Long.MAX_VALUE;
   }

   private long internalMethod06186(int localValue1) {
      String localValue2 = this.internalField0416.get(localValue1).internalMethod00756();
      return Math.clamp(localValue2.codePointCount(0, localValue2.length()) * 150L, 1500L, 8000L);
   }

   private static boolean internalMethod00879(String localValue0) {
      for (int localValue1 = 0; localValue1 < localValue0.length(); localValue1++) {
         if (Character.isLetterOrDigit(localValue0.charAt(localValue1))) {
            return false;
         }
      }

      return true;
   }

   private static float internalMethod03636(int localValue0, double localValue1, long localValue3, long localValue5, int localValue7, int localValue8) {
      if (localValue5 > localValue3 && localValue0 != 0) {
         double localValue9 = Math.clamp((localValue1 - localValue3) / ((double)localValue5 - localValue3), 0.0, 1.0);
         return (float)((localValue7 + (localValue8 - localValue7) * localValue9) / localValue0);
      } else {
         return (float)localValue8 / Math.max(1, localValue0);
      }
   }

   private static long internalMethod06039(Matcher localValue0) {
      int localValue1 = Integer.parseInt(localValue0.group(3));
      if (localValue0.group(3).length() == 2) {
         localValue1 *= 10;
      }

      return Integer.parseInt(localValue0.group(1)) * 60000L + Integer.parseInt(localValue0.group(2)) * 1000L + localValue1;
   }

   @Generated
   public List<LyricsTrack.InternalType0316> internalMethod00129() {
      return this.internalField0416;
   }

   @Generated
   public boolean internalMethod00823() {
      return this.internalField0277;
   }

   public static final class InternalType0315 {
      private final long internalField0229;
      private final int internalField0227;

      public InternalType0315(long localValue1, int localValue3) {
         this.internalField0229 = localValue1;
         this.internalField0227 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0315[timeMs=" + this.internalField0229 + ", charIndex=" + this.internalField0227 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         LyricsTrack.InternalType0315 other = (LyricsTrack.InternalType0315) localValue1;
         return java.util.Objects.equals(this.internalField0229, other.internalField0229)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public long internalMethod00233() {
         return this.internalField0229;
      }

      public int internalMethod00232() {
         return this.internalField0227;
      }
   }

   public static final class InternalType0316 implements Comparable<LyricsTrack.InternalType0316> {
      private final long internalField0229;
      private final String internalField0248;
      private final List<LyricsTrack.InternalType0315> internalField0416;

      public InternalType0316(long localValue1, String localValue3, List<LyricsTrack.InternalType0315> localValue4) {
         localValue4 = List.copyOf(localValue4);
         this.internalField0229 = localValue1;
         this.internalField0248 = localValue3;
         this.internalField0416 = localValue4;
      }

      @Override
      public int compareTo(LyricsTrack.InternalType0316 localValue1) {
         return Long.compare(this.internalField0229, localValue1.internalField0229);
      }

      @Override
      public final String toString() {
         return "InternalType0316[timeMs=" + this.internalField0229 + ", text=" + this.internalField0248 + ", cues=" + this.internalField0416 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         LyricsTrack.InternalType0316 other = (LyricsTrack.InternalType0316) localValue1;
         return java.util.Objects.equals(this.internalField0229, other.internalField0229)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0416, other.internalField0416);
      }

      public long internalMethod01998() {
         return this.internalField0229;
      }

      public String internalMethod00756() {
         return this.internalField0248;
      }

      public List<LyricsTrack.InternalType0315> internalMethod00629() {
         return this.internalField0416;
      }
   }
}

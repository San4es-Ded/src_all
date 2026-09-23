package rockstar.client.internal.network;


import rockstar.client.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

public final class HttpDownloadUtils {
   private static final int internalField0227 = 5000;
   private HttpDownloadUtils() {
   }

   public static HttpDownloadUtils.InternalType0449 internalMethod02390(String localValue0, String... localValue1) throws IOException {
      HttpURLConnection localValue2 = (HttpURLConnection)URI.create(localValue0).toURL().openConnection();

      HttpDownloadUtils.InternalType0449 localValue5;
      try {
         localValue2.setRequestMethod("GET");
         localValue2.setConnectTimeout(5000);
         localValue2.setReadTimeout(6000);
         localValue2.setInstanceFollowRedirects(true);

         for (int localValue3 = 0; localValue3 + 1 < localValue1.length; localValue3 += 2) {
            localValue2.setRequestProperty(localValue1[localValue3], localValue1[localValue3 + 1]);
         }

         int localValue15 = localValue2.getResponseCode();
         InputStream localValue4 = localValue15 >= 400 ? localValue2.getErrorStream() : localValue2.getInputStream();
         if (localValue4 != null) {
            InputStream localValue16 = localValue4;

            HttpDownloadUtils.InternalType0449 localValue6;
            try {
               localValue6 = new HttpDownloadUtils.InternalType0449(localValue15, new String(localValue4.readAllBytes(), internalMethod05180(localValue2)));
            } catch (Throwable localValue13) {
               if (localValue4 != null) {
                  try {
                     localValue16.close();
                  } catch (Throwable localValue12) {
                     localValue13.addSuppressed(localValue12);
                  }
               }

               throw localValue13;
            }

            if (localValue4 != null) {
               localValue4.close();
            }

            return localValue6;
         }

         localValue5 = new HttpDownloadUtils.InternalType0449(localValue15, "");
      } finally {
         localValue2.disconnect();
      }

      return localValue5;
   }

   public static byte[] internalMethod05458(String localValue0, String... localValue1) throws IOException {
      return internalMethod07379(localValue0, 33554432, localValue1);
   }

   public static byte[] internalMethod07379(String localValue0, int localValue1, String... localValue2) throws IOException {
      HttpURLConnection localValue3 = (HttpURLConnection)URI.create(localValue0).toURL().openConnection();

      byte[] localValue6;
      try {
         localValue3.setRequestMethod("GET");
         localValue3.setConnectTimeout(5000);
         localValue3.setReadTimeout(6000);
         localValue3.setInstanceFollowRedirects(true);

         for (int localValue4 = 0; localValue4 + 1 < localValue2.length; localValue4 += 2) {
            localValue3.setRequestProperty(localValue2[localValue4], localValue2[localValue4 + 1]);
         }

         int localValue15 = localValue3.getResponseCode();
         if (localValue15 != 200) {
            throw new IOException("HTTP " + localValue15 + " \u043e\u0442 " + localValue0);
         }

         try (InputStream localValue5 = localValue3.getInputStream()) {
            localValue6 = internalMethod01183(localValue5, localValue1, localValue0);
         }
      } finally {
         localValue3.disconnect();
      }

      return localValue6;
   }

   private static byte[] internalMethod01183(InputStream localValue0, int localValue1, String localValue2) throws IOException {
      ByteArrayOutputStream localValue3 = new ByteArrayOutputStream();
      byte[] localValue4 = new byte[16384];

      int localValue5;
      while ((localValue5 = localValue0.read(localValue4)) != -1) {
         if (localValue3.size() + localValue5 > localValue1) {
            throw new IOException("\u043e\u0442\u0432\u0435\u0442 \u0431\u043e\u043b\u044c\u0448\u0435 " + localValue1 / 1048576 + " \u041c\u0411: " + localValue2);
         }

         localValue3.write(localValue4, 0, localValue5);
      }

      return localValue3.toByteArray();
   }

   private static Charset internalMethod05180(HttpURLConnection localValue0) {
      String localValue1 = localValue0.getContentType();
      if (localValue1 == null) {
         return StandardCharsets.UTF_8;
      } else {
         for (String localValue5 : localValue1.split(";")) {
            String localValue6 = localValue5.trim();
            if (localValue6.regionMatches(true, 0, "charset=", 0, 8)) {
               String localValue7 = localValue6.substring(8).replace("\"", "").trim();

               try {
                  return Charset.forName(localValue7);
               } catch (UnsupportedCharsetException | IllegalCharsetNameException localValue9) {
                  return StandardCharsets.UTF_8;
               }
            }
         }

         return StandardCharsets.UTF_8;
      }
   }

   public static final class InternalType0449 {
      private final int internalField0227;
      private final String internalField0248;

      public InternalType0449(int localValue1, String localValue2) {
         this.internalField0227 = localValue1;
         this.internalField0248 = localValue2;
      }

      public boolean internalMethod02740() {
         return this.internalField0227 == 200;
      }

      @Override
      public final String toString() {
         return "InternalType0449[status=" + this.internalField0227 + ", body=" + this.internalField0248 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         HttpDownloadUtils.InternalType0449 other = (HttpDownloadUtils.InternalType0449) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248);
      }

      public int internalMethod02739() {
         return this.internalField0227;
      }

      public String internalMethod00694() {
         return this.internalField0248;
      }
   }
}

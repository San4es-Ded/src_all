package globals.client.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class Gz {
   private Gz() {
   }

   public static String deflateBase64(String localValue0) {
      if (localValue0 == null) {
         return null;
      } else {
         try {
            String localValue10;
            try (ByteArrayOutputStream localValue1 = new ByteArrayOutputStream()) {
               try (GZIPOutputStream localValue2 = new GZIPOutputStream(localValue1)) {
                  localValue2.write(localValue0.getBytes(StandardCharsets.UTF_8));
               }

               localValue10 = Base64.getEncoder().encodeToString(localValue1.toByteArray());
            }

            return localValue10;
         } catch (Exception localValue9) {
            return null;
         }
      }
   }

   public static String inflateBase64(String localValue0, int localValue1) {
      if (localValue0 != null && !localValue0.isBlank()) {
         try {
            byte[] localValue2 = Base64.getDecoder().decode(localValue0);

            String localValue13;
            try (
               GZIPInputStream localValue3 = new GZIPInputStream(new ByteArrayInputStream(localValue2));
               ByteArrayOutputStream localValue4 = new ByteArrayOutputStream();
            ) {
               byte[] localValue5 = new byte[8192];

               int localValue6;
               while ((localValue6 = localValue3.read(localValue5)) != -1) {
                  if (localValue4.size() + localValue6 > localValue1) {
                     return null;
                  }

                  localValue4.write(localValue5, 0, localValue6);
               }

               localValue13 = localValue4.toString(StandardCharsets.UTF_8);
            }

            return localValue13;
         } catch (Exception localValue12) {
            return null;
         }
      } else {
         return null;
      }
   }
}

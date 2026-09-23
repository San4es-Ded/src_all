package rockstar.client.network;


import rockstar.client.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import lombok.SneakyThrows;
import java.util.zip.InflaterInputStream;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public class RockstarHttpResponse extends FluentObject<RockstarHttpResponse> {
   private static final Map<String, RockstarHttpResponse.InternalType0331> internalField0543 = new HashMap<>();
   private final URL internalField0360;
   private final int internalField0227;
   private final RequestBody internalField0061;

   @Nullable
   private static RockstarHttpResponse.InternalType0331 internalMethod02422(String... localValue0) {
      for (String localValue4 : localValue0) {
         try {
            Class localValue5 = Class.forName(localValue4).asSubclass(InputStream.class);
            Constructor localValue6 = localValue5.getDeclaredConstructor(InputStream.class);
            return localValue2 -> {
               try {
                  return (InputStream)localValue6.newInstance(localValue2);
               } catch (Throwable localValue4x) {
                  throw new IOException("Failed to create decoder input stream of type " + localValue4, localValue4x);
               }
            };
         } catch (Throwable localValue7) {
         }
      }

      return null;
   }

   public RockstarHttpResponse(URL localValue1, int localValue2, byte[] localValue3, Map<String, List<String>> localValue4) {
      super(localValue4);
      this.internalField0360 = localValue1;
      this.internalField0227 = localValue2;
      this.internalField0061 = new ByteArrayRequestBody(
         this.internalMethod04855("Content-Type").map(MediaType::internalMethod06844).orElse(MediaTypes.internalField1097), localValue3
      );
   }

   public RockstarHttpResponse(URL localValue1, int localValue2, InputStream localValue3, Map<String, List<String>> localValue4) {
      super(localValue4);
      this.internalField0360 = localValue1;
      this.internalField0227 = localValue2;
      this.internalField0061 = new InputStreamRequestBody(
         this.internalMethod04855("Content-Type").map(MediaType::internalMethod06844).orElse(MediaTypes.internalField1097),
         localValue3,
         this.internalMethod04855("Content-Length").map(localValue0 -> {
            try {
               return Integer.valueOf(localValue0);
            } catch (NumberFormatException localValue2x) {
               return -1;
            }
         }).orElse(-1)
      );
   }

   public RockstarHttpResponse(URL localValue1, int localValue2, RequestBody localValue3, Map<String, List<String>> localValue4) {
      super(localValue4);
      this.internalField0360 = localValue1;
      this.internalField0227 = localValue2;
      this.internalField0061 = localValue3;
   }

   public URL internalMethod00641() {
      return this.internalField0360;
   }

   public int internalMethod00588() {
      return this.internalField0227;
   }

   public String internalMethod06484() {
      return HttpStatus.internalField0543.getOrDefault(this.internalField0227, "Unknown");
   }

   public RequestBody internalMethod02509() {
      return this.internalField0061;
   }

   public RequestBody internalMethod03839() {
      return this.internalMethod01512(localValue0 -> internalField0543.get(localValue0.toLowerCase(Locale.ROOT)));
   }

   public RequestBody internalMethod01512(RockstarHttpResponse.InternalType0330 localValue1) {
      String localValue2 = this.internalMethod04855("Content-Encoding").orElse(null);
      if (localValue2 == null) {
         return this.internalField0061;
      } else {
         String[] localValue3 = localValue2.split(",\\s*");
         final ArrayList localValue4 = new ArrayList(localValue3.length);

         for (String localValue8 : localValue3) {
            RockstarHttpResponse.InternalType0331 localValue9 = localValue1.get(localValue8);
            if (localValue9 == null) {
               return this.internalField0061;
            }

            localValue4.add(localValue9);
         }

         this.internalMethod02805("Content-Encoding");
         this.internalMethod01193("Original-Content-Encoding", localValue2);
         return new DelegatingRequestBody(this.internalField0061) {
            @Override
            protected InputStream internalMethod04357(InputStream localValue1) throws IOException {
               localValue1 = super.internalMethod04357(localValue1);

               for (int localValue2x = localValue4.size() - 1; localValue2x >= 0; localValue2x--) {
                  localValue1 = ((RockstarHttpResponse.InternalType0331)localValue4.get(localValue2x)).map(localValue1);
               }

               return localValue1;
            }
         };
      }
   }

   @Deprecated
   @ScheduledForRemoval
   @SneakyThrows(IOException.class)
   public InputStream internalMethod06088() {
      return this.internalField0061.internalMethod02044();
   }

   @Deprecated
   @ScheduledForRemoval
   @SneakyThrows(IOException.class)
   public String internalMethod02993() {
      return this.internalField0061.internalMethod04320();
   }

   @Deprecated
   @ScheduledForRemoval
   @SneakyThrows(IOException.class)
   public String internalMethod02559(Charset localValue1) {
      return this.internalField0061.internalMethod06795(localValue1);
   }

   @Deprecated
   @ScheduledForRemoval
   public Optional<MediaType> internalMethod07399() {
      return this.internalMethod04855("Content-Type").map(MediaType::internalMethod06844);
   }

   static {
      internalField0543.put("identity", localValue0 -> localValue0);
      internalField0543.put("gzip", GZIPInputStream::new);
      internalField0543.put("x-gzip", GZIPInputStream::new);
      internalField0543.put("deflate", InflaterInputStream::new);
      internalField0543.put("br", internalMethod02422("com.aayushatharva.brotli4j.decoder.BrotliInputStream", "org.brotli.dec.BrotliInputStream"));
      internalField0543.put(
         "zstd",
         internalMethod02422("io.airlift.compress.zstd.ZstdInputStream", "io.airlift.compress.v3.zstd.ZstdInputStream", "com.github.luben.zstd.ZstdInputStream")
      );
   }

   @FunctionalInterface
   public interface InternalType0330 {
      @Nullable
      RockstarHttpResponse.InternalType0331 get(String localValue1);
   }

   @FunctionalInterface
   public interface InternalType0331 {
      InputStream map(InputStream localValue1) throws IOException;
   }
}

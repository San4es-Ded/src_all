package rockstar.client.network;


import rockstar.client.*;
import java.io.IOException;
import java.net.CookieManager;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class HttpExecutor {
   @Nonnull
   protected final RockstarHttpClient internalField0058;

   public HttpExecutor(@Nonnull RockstarHttpClient localValue1) {
      this.internalField0058 = localValue1;
   }

   @Nonnull
   public abstract RockstarHttpResponse internalMethod03072(@Nonnull RockstarHttpRequest localValue1) throws IOException, InterruptedException;

   protected final boolean internalMethod00904(@Nonnull RockstarHttpRequest localValue1) {
      switch (localValue1.internalMethod03460()) {
         case internalField0530:
            return this.internalField0058.internalMethod03723();
         case internalField0531:
            return true;
         case internalField1191:
            return false;
         default:
            throw new IllegalStateException("Unexpected value: " + localValue1.internalMethod03460());
      }
   }

   @Nullable
   protected final CookieManager internalMethod04792(@Nonnull RockstarHttpRequest localValue1) {
      return localValue1.internalMethod07966() ? localValue1.internalMethod02997() : this.internalField0058.internalMethod06177();
   }

   protected final boolean internalMethod01771(@Nonnull RockstarHttpRequest localValue1) {
      return localValue1.internalMethod07978() ? localValue1.internalMethod09158() : this.internalField0058.internalMethod03730();
   }

   protected final Map<String, List<String>> internalMethod00896(@Nonnull RockstarHttpRequest localValue1, @Nullable CookieManager localValue2) throws IOException {
      return this.internalMethod03587(localValue1, localValue2, true);
   }

   protected final Map<String, List<String>> internalMethod03587(@Nonnull RockstarHttpRequest localValue1, @Nullable CookieManager localValue2, boolean localValue3) throws IOException {
      HashMap localValue4 = new HashMap();
      if (localValue2 != null) {
         try {
            Map localValue5 = localValue2.get(localValue1.internalMethod03635().toURI(), Collections.emptyMap());

            for (Entry localValue7 : (Iterable<Entry>)(Iterable<?>)localValue5.entrySet()) {
               if (!((List)localValue7.getValue()).isEmpty()) {
                  localValue4.put(((String)localValue7.getKey()).toLowerCase(), (List)localValue7.getValue());
               }
            }
         } catch (URISyntaxException localValue8) {
            throw new IOException("Failed to parse URL as URI", localValue8);
         }
      }

      if (localValue1 instanceof CustomHttpRequest && localValue3) {
         RequestBody localValue9 = ((CustomHttpRequest)localValue1).internalMethod05540();
         if (localValue9 != null) {
            localValue4.put("Content-Type".toLowerCase(), Collections.singletonList(localValue9.internalMethod05122().toString()));
            if (localValue9.internalMethod02203() < 0) {
               localValue4.put("Content-Length".toLowerCase(), Collections.singletonList(String.valueOf(localValue9.internalMethod02203())));
            }
         }
      }

      for (Entry localValue12 : this.internalField0058.internalMethod02866().entrySet()) {
         if (!((List)localValue12.getValue()).isEmpty()) {
            localValue4.put(((String)localValue12.getKey()).toLowerCase(), (List)localValue12.getValue());
         }
      }

      for (Entry localValue13 : localValue1.internalMethod02866().entrySet()) {
         if (!((List)localValue13.getValue()).isEmpty()) {
            localValue4.put(((String)localValue13.getKey()).toLowerCase(), (List)localValue13.getValue());
         }
      }

      return localValue4;
   }

   protected final void internalMethod05879(@Nullable CookieManager localValue1, URL localValue2, Map<String, List<String>> localValue3) throws IOException {
      if (localValue1 != null) {
         try {
            localValue1.put(localValue2.toURI(), localValue3);
         } catch (URISyntaxException localValue5) {
            throw new IOException("Failed to parse URL as URI", localValue5);
         }
      }
   }

   protected final void internalMethod01949(Map<String, List<String>> localValue1, BiConsumer<String, String> localValue2, BiConsumer<String, String> localValue3) {
      for (Entry localValue5 : localValue1.entrySet()) {
         if ("Cookie".equalsIgnoreCase((String)localValue5.getKey())) {
            localValue2.accept((String)localValue5.getKey(), String.join("; ", (Iterable<? extends CharSequence>)localValue5.getValue()));
         } else {
            boolean localValue6 = true;

            for (String localValue8 : (Iterable<String>)(Iterable<?>)(List)localValue5.getValue()) {
               if (localValue6) {
                  localValue6 = false;
                  localValue2.accept((String)localValue5.getKey(), localValue8);
               } else {
                  localValue3.accept((String)localValue5.getKey(), localValue8);
               }
            }
         }
      }
   }
}

package rockstar.client.network;


import rockstar.client.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;

public class UrlConnectionHttpExecutor extends HttpExecutor {
   public UrlConnectionHttpExecutor(RockstarHttpClient localValue1) {
      super(localValue1);
   }

   @Nonnull
   @Override
   public RockstarHttpResponse internalMethod03072(@Nonnull RockstarHttpRequest localValue1) throws IOException {
      CookieManager localValue2 = this.internalMethod04792(localValue1);
      ProxyConfig localValue3 = this.internalField0058.internalMethod03513();
      AuthenticatedProxySelector localValue4 = null;
      if (localValue3.internalMethod01161()) {
         localValue4 = localValue3.internalMethod02248();
      }

      RockstarHttpResponse localValue6;
      try {
         if (localValue4 != null) {
            localValue4.internalMethod06604(false);
         }

         HttpURLConnection localValue5 = this.internalMethod01732(localValue1, localValue2, localValue4 == null ? null : localValue3);
         localValue6 = this.internalMethod05257(localValue5, localValue2, localValue1);
      } finally {
         if (localValue4 != null) {
            localValue4.internalMethod07409(false);
         }
      }

      return localValue6;
   }

   private HttpURLConnection internalMethod01732(RockstarHttpRequest localValue1, CookieManager localValue2, ProxyConfig localValue3) throws IOException {
      URL localValue4 = localValue1.internalMethod03635();
      HttpURLConnection localValue5;
      if (localValue3 == null) {
         localValue5 = (HttpURLConnection)localValue4.openConnection();
      } else {
         localValue5 = (HttpURLConnection)localValue4.openConnection(localValue3.internalMethod07281());
      }

      if (this.internalMethod01771(localValue1) && localValue5 instanceof HttpsURLConnection) {
         HttpsURLConnection localValue6 = (HttpsURLConnection)localValue5;
         localValue6.setSSLSocketFactory(InsecureSslContext.internalMethod03802().getSocketFactory());
      }

      this.internalMethod05762(localValue5, localValue2, localValue1);
      localValue5.connect();
      return localValue5;
   }

   private void internalMethod05762(HttpURLConnection localValue1, @Nullable CookieManager localValue2, RockstarHttpRequest localValue3) throws IOException {
      this.internalMethod01949(this.internalMethod00896(localValue3, localValue2), localValue1::setRequestProperty, localValue1::addRequestProperty);
      CustomHttpRequest localValue4 = localValue3 instanceof CustomHttpRequest ? (CustomHttpRequest)localValue3 : null;
      RequestBody localValue5 = localValue4 != null ? localValue4.internalMethod05540() : null;
      localValue1.setConnectTimeout(this.internalField0058.internalMethod03722());
      localValue1.setReadTimeout(this.internalField0058.internalMethod03729());
      localValue1.setRequestMethod(localValue3.internalMethod06034());
      localValue1.setDoInput(true);
      if (localValue4 != null && localValue5 != null) {
         localValue1.setDoOutput(true);
         if (localValue3.internalMethod02703()) {
            if (localValue5.internalMethod02203() >= 0) {
               localValue1.setFixedLengthStreamingMode(localValue5.internalMethod02203());
            } else {
               localValue1.setChunkedStreamingMode(0);
            }
         }
      } else {
         localValue1.setDoOutput(false);
      }

      switch (localValue3.internalMethod03460()) {
         case internalField0530:
            localValue1.setInstanceFollowRedirects(this.internalField0058.internalMethod03723());
            break;
         case internalField0531:
            localValue1.setInstanceFollowRedirects(true);
            break;
         case internalField1191:
            localValue1.setInstanceFollowRedirects(false);
      }
   }

   private RockstarHttpResponse internalMethod05257(HttpURLConnection localValue1, @Nullable CookieManager localValue2, RockstarHttpRequest localValue3) throws IOException {
      boolean localValue4 = true;

      RockstarHttpResponse localValue18;
      try {
         if (localValue1.getDoOutput()) {
            RequestBody localValue5 = ((CustomHttpRequest)localValue3).internalMethod05540();
            OutputStream localValue6 = localValue1.getOutputStream();

            try {
               localValue5.internalMethod07373(localValue6);
            } catch (Throwable localValue14) {
               if (localValue6 != null) {
                  try {
                     localValue6.close();
                  } catch (Throwable localValue13) {
                     localValue14.addSuppressed(localValue13);
                  }
               }

               throw localValue14;
            }

            if (localValue6 != null) {
               localValue6.close();
            }
         }

         HashMap localValue16 = new HashMap<>(localValue1.getHeaderFields());
         localValue16.remove(null);
         InputStream localValue7 = this.internalMethod06227(localValue1);
         RockstarHttpResponse localValue17;
         if (localValue3.internalMethod07965()) {
            localValue17 = new RockstarHttpResponse(localValue3.internalMethod03635(), localValue1.getResponseCode(), localValue7, localValue16);
            localValue4 = false;
         } else {
            byte[] localValue8 = this.internalMethod05487(localValue7, localValue1.getContentLength());
            localValue17 = new RockstarHttpResponse(localValue3.internalMethod03635(), localValue1.getResponseCode(), localValue8, localValue16);
         }

         this.internalMethod05879(localValue2, localValue3.internalMethod03635(), localValue1.getHeaderFields());
         localValue18 = localValue17;
      } finally {
         if (localValue4) {
            localValue1.disconnect();
         }
      }

      return localValue18;
   }

   private InputStream internalMethod06227(HttpURLConnection localValue1) throws IOException {
      Object localValue2;
      if (localValue1.getResponseCode() >= 400) {
         localValue2 = localValue1.getErrorStream();
      } else {
         localValue2 = localValue1.getInputStream();
      }

      if (localValue2 == null) {
         localValue2 = new ByteArrayInputStream(new byte[0]);
      }

      return (InputStream)localValue2;
   }

   private byte[] internalMethod05487(InputStream localValue1, int localValue2) throws IOException {
      ByteArrayOutputStream localValue3 = new ByteArrayOutputStream(localValue2 >= 0 ? localValue2 : 1024);
      byte[] localValue4 = new byte[1024];

      int localValue5;
      while ((localValue5 = localValue1.read(localValue4)) >= 0) {
         localValue3.write(localValue4, 0, localValue5);
      }

      return localValue3.toByteArray();
   }
}

package rockstar.client.network;


import rockstar.client.*;
import rockstar.client.internal.core.*;
import java.io.IOException;
import java.net.CookieManager;
import java.net.ProtocolException;
import java.net.UnknownHostException;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.SSLException;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public class RockstarHttpClient extends FluentObject<RockstarHttpClient> implements HttpRequestFactory {
   private HttpExecutor internalField0264;
   @Nullable
   private CookieManager internalField0576 = new CookieManager();
   private boolean internalField0277 = true;
   private int internalField0227 = 10000;
   private int internalField0228 = 10000;
   private HttpRequestOptions internalField0874 = new HttpRequestOptions();
   private ProxyConfig internalField0271 = new ProxyConfig();
   private boolean internalField0276 = false;

   public RockstarHttpClient() {
      this(HttpBackend.internalField0262);
   }

   public RockstarHttpClient(@Nonnull HttpBackend localValue1) {
      this(localValue1::internalMethod06959);
   }

   public RockstarHttpClient(@Nonnull Function<RockstarHttpClient, HttpExecutor> localValue1) {
      this.internalMethod06638(localValue1);
   }

   public RockstarHttpClient internalMethod06638(@Nonnull Function<RockstarHttpClient, HttpExecutor> localValue1) {
      HttpExecutor localValue2 = (HttpExecutor)localValue1.apply(this);
      if (localValue2 == null) {
         throw new NullPointerException("The executor supplier returned null");
      } else {
         this.internalField0264 = localValue2;
         return this;
      }
   }

   @Nullable
   public CookieManager internalMethod06177() {
      return this.internalField0576;
   }

   public RockstarHttpClient internalMethod07183(@Nullable CookieManager localValue1) {
      this.internalField0576 = localValue1;
      return this;
   }

   public boolean internalMethod03723() {
      return this.internalField0277;
   }

   public RockstarHttpClient internalMethod00928(boolean localValue1) {
      this.internalField0277 = localValue1;
      return this;
   }

   public int internalMethod03722() {
      return this.internalField0227;
   }

   public RockstarHttpClient internalMethod05495(int localValue1) {
      this.internalField0227 = localValue1;
      return this;
   }

   public int internalMethod03729() {
      return this.internalField0228;
   }

   public RockstarHttpClient internalMethod00765(int localValue1) {
      this.internalField0228 = localValue1;
      return this;
   }

   @Nonnull
   public HttpRequestOptions internalMethod03231() {
      return this.internalField0874;
   }

   public RockstarHttpClient internalMethod06958(@Nonnull HttpRequestOptions localValue1) {
      this.internalField0874 = localValue1;
      return this;
   }

   @Deprecated
   @ScheduledForRemoval
   public RockstarHttpClient internalMethod05937(@Nonnull HttpRequestOptions localValue1) {
      this.internalField0874 = localValue1;
      return this;
   }

   @Nonnull
   public ProxyConfig internalMethod03513() {
      return this.internalField0271;
   }

   public RockstarHttpClient internalMethod00790(@Nonnull ProxyConfig localValue1) {
      this.internalField0271 = localValue1;
      return this;
   }

   public boolean internalMethod03730() {
      return this.internalField0276;
   }

   public RockstarHttpClient internalMethod01784(boolean localValue1) {
      this.internalField0276 = localValue1;
      return this;
   }

   public <T extends RockstarHttpRequest & ResponseHandler<R>, R> R internalMethod07532(T localValue1) throws IOException {
      return this.internalMethod04616(localValue1, (ResponseHandler<R>)localValue1);
   }

   public <R> R internalMethod04616(RockstarHttpRequest localValue1, ResponseHandler<R> localValue2) throws IOException {
      return (R)localValue2.handle(this.internalMethod03397(localValue1));
   }

   public RockstarHttpResponse internalMethod03397(RockstarHttpRequest localValue1) throws IOException {
      HttpRequestOptions localValue2 = localValue1.internalMethod07977() ? localValue1.internalMethod04025() : this.internalField0874;

      for (int localValue3 = 0; localValue3 <= localValue2.internalMethod05746(); localValue3++) {
         try {
            RockstarHttpResponse localValue4 = null;

            for (int localValue5 = 0; localValue5 <= localValue2.internalMethod05749(); localValue5++) {
               localValue4 = this.internalField0264.internalMethod03072(localValue1);
               RetryDelay localValue6 = localValue2.internalMethod06818().internalMethod07102(localValue4);
               if (!localValue6.internalMethod02490()) {
                  return localValue4;
               }

               localValue6.internalMethod02489();
            }

            if (localValue4 == null) {
               throw new IllegalStateException("Response not received but no exception was thrown");
            }

            if (localValue2.internalMethod05749() == 0) {
               return localValue4;
            }

            throw new HttpRetryException(localValue4);
         } catch (InterruptedException localValue7) {
            throw new IOException(localValue7);
         } catch (SSLException | ProtocolException | UnknownHostException localValue8) {
            throw localValue8;
         } catch (IOException localValue9) {
            if (localValue3 >= localValue2.internalMethod05746()) {
               throw localValue9;
            }
         }
      }

      throw new IllegalStateException("Connect retry failed but no exception was thrown");
   }

   @Override
   public <T extends RockstarHttpRequest> T internalMethod05303(T localValue1) {
      localValue1.internalMethod03607(this);
      return (T)localValue1;
   }

   @Override
   public String toString() {
      return "RockstarHttpClient#" + this.internalField0264.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(this));
   }
}

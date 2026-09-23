package rockstar.client.network;


import rockstar.client.*;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.net.Authenticator;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;

public class AuthenticatedProxySelector extends ProxySelector {
   private static final MethodHandle internalField0781;
   private final Proxy internalField0695;
   private final String internalField0248;
   private final String internalField0247;
   private final ProxySelector internalField0231;
   private final Authenticator internalField0603;

   @SneakyThrows(Throwable.class)
   public AuthenticatedProxySelector(Proxy localValue1, String localValue2, String localValue3) {
      this.internalField0695 = localValue1;
      this.internalField0248 = localValue2;
      this.internalField0247 = localValue3;
      this.internalField0231 = ProxySelector.getDefault();
      this.internalField0603 = (Authenticator)internalField0781.invokeExact();
   }

   public AuthenticatedProxySelector internalMethod06604(boolean localValue1) {
      if (localValue1) {
         ProxySelector.setDefault(this);
      }

      if (this.internalField0248 != null && this.internalField0247 != null) {
         Authenticator.setDefault(new ProxyAuthenticator(this.internalField0248, this.internalField0247));
      }

      return this;
   }

   public AuthenticatedProxySelector internalMethod07409(boolean localValue1) {
      if (localValue1) {
         ProxySelector.setDefault(this.internalField0231);
      }

      if (this.internalField0248 != null && this.internalField0247 != null) {
         Authenticator.setDefault(this.internalField0603);
      }

      return this;
   }

   @Override
   public List<Proxy> select(URI localValue1) {
      return Collections.singletonList(this.internalField0695);
   }

   @Override
   public void connectFailed(URI localValue1, SocketAddress localValue2, IOException localValue3) {
   }

   static {
      MethodHandle localValue0;
      try {
         localValue0 = MethodHandles.lookup().findStatic(Authenticator.class, "getDefault", MethodType.methodType(Authenticator.class));
      } catch (Throwable localValue4) {
         try {
            Field localValue2 = Authenticator.class.getDeclaredField("theAuthenticator");
            localValue2.setAccessible(true);
            localValue0 = MethodHandles.lookup().unreflectGetter(localValue2);
         } catch (Throwable localValue3) {
            localValue0 = MethodHandles.constant(Authenticator.class, null);
         }
      }

      internalField0781 = localValue0;
   }
}

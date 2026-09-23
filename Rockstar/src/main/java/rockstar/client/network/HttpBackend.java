package rockstar.client.network;


import rockstar.client.*;
import java.lang.reflect.Constructor;

public enum HttpBackend {
   internalField0262 {
      @Override
      public HttpExecutor internalMethod01115(RockstarHttpClient localValue1) {
         for (int localValue2 = values().length - 1; localValue2 >= 0; localValue2--) {
            HttpBackend localValue3 = values()[localValue2];
            if (!internalField0262.equals(localValue3) && localValue3.internalMethod01748()) {
               HttpExecutor localValue4 = localValue3.internalMethod06959(localValue1);
               if (localValue4 != null) {
                  return localValue4;
               }
            }
         }

         throw new IllegalStateException("Failed to find a suitable executor. This should never happen. Please report this to the developer.");
      }
   },
   internalField0263 {
      @Override
      public HttpExecutor internalMethod01115(RockstarHttpClient localValue1) {
         return new UrlConnectionHttpExecutor(localValue1);
      }
   },
   internalField1092 {
      private Constructor<?> internalField0810;

      @Override
      protected void internalMethod00995() throws Throwable {
         Class.forName("reactor.netty.http.client.RockstarHttpClient");
         Class localValue1 = Class.forName("rockstar.client.iiIiIi");
         this.internalField0810 = localValue1.getDeclaredConstructor(RockstarHttpClient.class);
      }

      @Override
      protected HttpExecutor internalMethod01115(RockstarHttpClient localValue1) throws Throwable {
         return (HttpExecutor)this.internalField0810.newInstance(localValue1);
      }
   },
   internalField1093 {
      private Constructor<?> internalField0810;

      @Override
      protected void internalMethod00995() throws Throwable {
         Class.forName("java.net.http.RockstarHttpClient");
         Class localValue1 = Class.forName("rockstar.client.iiIIiI");
         this.internalField0810 = localValue1.getDeclaredConstructor(RockstarHttpClient.class);
      }

      @Override
      protected HttpExecutor internalMethod01115(RockstarHttpClient localValue1) throws Throwable {
         return (HttpExecutor)this.internalField0810.newInstance(localValue1);
      }
   };

   private boolean internalField0277;

   private HttpBackend() {
      try {
         this.internalMethod00995();
         this.internalField0277 = true;
      } catch (Throwable localValue4) {
         this.internalField0277 = false;
      }
   }

   public final boolean internalMethod01748() {
      return this.internalField0277;
   }

   public final HttpExecutor internalMethod06959(RockstarHttpClient localValue1) {
      try {
         return this.internalMethod01115(localValue1);
      } catch (Throwable localValue3) {
         return null;
      }
   }

   protected void internalMethod00995() throws Throwable {
   }

   protected abstract HttpExecutor internalMethod01115(RockstarHttpClient localValue1) throws Throwable;
}

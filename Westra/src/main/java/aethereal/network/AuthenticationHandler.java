package aethereal.network;

import aethereal.lib.misc.RequestAuthenticator;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;

public class AuthenticationHandler extends Authenticator {
   static final int a = 5;
   static AuthenticationHandler.a b;
   RequestAuthenticator c;
   int d = 0;

   AuthenticationHandler() {
   }

   AuthenticationHandler(RequestAuthenticator auth) {
      this.c = auth;
   }

   @Override
   public final PasswordAuthentication getPasswordAuthentication() {
      AuthenticationHandler delegate = b.a(this);
      if (delegate == null) {
         return null;
      } else {
         delegate.d++;
         if (delegate.d <= 5 && delegate.c != null) {
            RequestAuthenticator.a ctx = new RequestAuthenticator.a(this.getRequestingURL(), this.getRequestorType(), this.getRequestingPrompt());
            return delegate.c.a(ctx);
         } else {
            return null;
         }
      }
   }

   static {
      try {
         b = (AuthenticationHandler.a)Class.forName("org.jsoup.helper.RequestAuthHandler").getConstructor().newInstance();
      } catch (ClassNotFoundException var1) {
         b = new AuthenticationHandler.b();
      } catch (Exception var2) {
         throw new IllegalStateException(var2);
      }
   }

   interface a {
      void a(RequestAuthenticator var1, HttpURLConnection var2);

      void a();

      AuthenticationHandler a(AuthenticationHandler var1);
   }

   static class b implements AuthenticationHandler.a {
      static ThreadLocal<AuthenticationHandler> a = new ThreadLocal<>();

      @Override
      public void a(RequestAuthenticator auth, HttpURLConnection con) {
         a.set(new AuthenticationHandler(auth));
      }

      @Override
      public void a() {
         a.remove();
      }

      @Override
      public AuthenticationHandler a(AuthenticationHandler helper) {
         return a.get();
      }

      static {
         Authenticator.setDefault(new AuthenticationHandler());
      }
   }
}

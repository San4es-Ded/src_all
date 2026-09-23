package aethereal.lib.misc;

import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.Authenticator.RequestorType;

public interface RequestAuthenticator {
   PasswordAuthentication a(RequestAuthenticator.a var1);

   public static final class a {
      public a(URL url, RequestorType type, String prompt) {
      }
   }
}

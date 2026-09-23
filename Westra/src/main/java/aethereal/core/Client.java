package aethereal.core;

import aethereal.api.Compile;
import aethereal.lib.log4j.LoggerFactory;
import aethereal.lib.log4j.Logger_2;
import aethereal.lib.websocket.ServerHandshake;
import aethereal.lib.websocket.WebSocketClient;
import aethereal.network.PacketSecurity;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.Generated;

@Compile
public class Client extends WebSocketClient {
   @Generated
   private static Logger_2 b;
   private final ScheduledExecutorService c = Executors.newSingleThreadScheduledExecutor();
   private final List<Packet> d = new ArrayList<>();
   private final PacketSecurity e = new PacketSecurity();

   private SSLSocketFactory C() throws Exception {
      X509TrustManager x509TrustManager = new X509TrustManager() {
         @Override
         public void checkClientTrusted(X509Certificate[] chain, String authType) {
         }

         @Override
         public void checkServerTrusted(X509Certificate[] chain, String authType) {
            if (chain != null && chain.length != 0) {
               try {
                  String pin = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(chain[0].getPublicKey().getEncoded()));
                  boolean matched = Set.of("tjzKnQqXiG8qfKkHSOtckEHsKNtsONSU9NN+d8vZ1XQ=")
                     .stream()
                     .anyMatch(expected -> MessageDigest.isEqual(pin.getBytes(StandardCharsets.UTF_8), expected.getBytes(StandardCharsets.UTF_8)));
                  if (!matched) {
                     throw new Client.a();
                  }
               } catch (Client.a var5) {
                  throw var5;
               } catch (Exception var6) {
                  throw new Client.a();
               }
            } else {
               throw new Client.a();
            }
         }

         @Override
         public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
         }
      };
      SSLContext sSLContext = SSLContext.getInstance("TLS");
      sSLContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
      return sSLContext.getSocketFactory();
   }

   @Override
   public void a(ServerHandshake handshake) {
   }

   @Override
   public void c(String message) {
   }

   @Override
   public void b(int code, String reason, boolean remote) {
   }

   @Override
   public void a(Exception ex) {
   }

   public void a(boolean change, String packetId, Object... keyValues) {
   }

   public void A() {
   }

   @Generated
   public PacketSecurity B() {
      return this.e;
   }

   public static boolean a(String packetId, Packet p) {
      if (p == null) {
         throw new NullPointerException();
      } else {
         String strB = p.b();
         if (strB == null) {
            throw new NullPointerException();
         } else {
            return strB.equals(packetId);
         }
      }
   }

   public void D() {
   }

   private static void jc$clinit$() {
      b = LoggerFactory.a(Client.class);
   }

   public boolean g() {
      return this.isOpen();
   }

   public Client(boolean dev) {
      super(URI.create("ws://localhost:2002/"), Map.of("Sec-WebSocket-Protocol", Westra.h().g().f() + "-minecraft"));
   }

   static {
      NativeMethodLookup.lookup(Client.class, 2);
      jc$clinit$();
   }

   static final class a extends RuntimeException {
      a() {
         super(null, null, false, false);
      }
   }
}

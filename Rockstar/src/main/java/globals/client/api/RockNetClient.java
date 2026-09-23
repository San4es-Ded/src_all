package globals.client.api;



import rockstar.client.network.*;
import rockstar.client.i18n.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import globals.client.RocknetHandler;
import globals.client.auth.ProfileSync;
import globals.client.auth.SessionManager;
import globals.client.crypto.ClientCrypto;
import globals.client.net.PacketCodec;
import globals.client.net.StompConnection;
import globals.shared.proto.Packet;
import globals.shared.proto.Packets;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Generated;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request.Builder;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;

public final class RockNetClient {
   private static final MediaType JSON = MediaType.parse("application/json");
   private static volatile boolean serverVerified = false;
   private final String httpBase;
   private final String wsUrl;
   private String activity = "main_menu";
   private Packets.InternalType0031 gameInfo;
   private volatile RockNetClient.InternalType0515 listener;
   private final OkHttpClient http = new OkHttpClient();
   private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor(localValue0 -> {
      Thread localValue1x = new Thread(localValue0, "Globals-Net");
      localValue1x.setDaemon(true);
      return localValue1x;
   });
   private volatile boolean sessionAuth;
   private volatile String jwt;
   volatile ClientCrypto crypto;
   volatile StompConnection stomp;
   final AtomicBoolean handshakeDone = new AtomicBoolean(false);
   final AtomicInteger reconnectAttempts = new AtomicInteger();
   final AtomicBoolean initialSyncDone = new AtomicBoolean(false);

   public static boolean isServerVerified() {
      return serverVerified;
   }

   private RockNetClient(String localValue1, int localValue2, boolean localValue3) {
      String localValue4 = localValue2 == (localValue3 ? 443 : 80) ? localValue1 : localValue1 + ":" + localValue2;
      this.httpBase = (localValue3 ? "https://" : "http://") + localValue4 + "/api/v1";
      this.wsUrl = (localValue3 ? "wss://" : "ws://") + localValue4 + "/ws";
      new RocknetHandler();
   }

   public static RockNetClient init(String localValue0, int localValue1) {
      return new RockNetClient(localValue0, localValue1, false);
   }

   public static RockNetClient init(String localValue0, int localValue1, boolean localValue2) {
      return new RockNetClient(localValue0, localValue1, localValue2);
   }

   public void connect() {
      if (this.sessionAuth) {
         this.exec.submit(this::establish);
      }
   }

   public void loginWithSession() {
      this.sessionAuth = true;
      this.jwt = null;
      ProfileSync.invalidate();
      this.initialSyncDone.set(false);
      this.exec.submit(this::establish);
   }

   public void send(Packet localValue1) {
      if (!(localValue1 instanceof Packets.InternalType0235) && !(localValue1 instanceof Packets.InternalType0150)) {
         JsonObject localValue2 = PacketCodec.toOp(localValue1);
         if (localValue2 != null) {
            this.sendSecure(localValue2);
         }
      } else {
         this.authResult(LanguageManager.internalMethod07214("rocknet.auth.launcher_only"));
      }
   }

   public synchronized void update(String localValue1) {
      if (localValue1 != null && !localValue1.isBlank()) {
         this.activity = localValue1;
         this.send(new Packets.InternalType0289(localValue1));
      }
   }

   public synchronized void updateVisibility(String localValue1) {
      if (localValue1 != null && !localValue1.isBlank()) {
         this.send(new Packets.InternalType0419(localValue1));
      }
   }

   public synchronized void info(String localValue1, String localValue2, String localValue3, String localValue4, String localValue5, String localValue6, String localValue7) {
      this.gameInfo = new Packets.InternalType0031(localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7);
      this.send(new Packets.InternalType0388(localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7));
   }

   public void close() {
      StompConnection localValue1 = this.stomp;
      if (localValue1 != null) {
         localValue1.disconnect();
      }

      this.handshakeDone.set(false);
      this.initialSyncDone.set(false);
   }

   void sendSecure(JsonObject localValue1) {
      ClientCrypto localValue2 = this.crypto;
      StompConnection localValue3 = this.stomp;
      if (localValue2 != null && localValue3 != null && this.handshakeDone.get()) {
         try {
            ClientCrypto.InternalType0005 localValue4 = localValue2.encrypt(localValue1.toString().getBytes(StandardCharsets.UTF_8));
            JsonObject localValue5 = new JsonObject();
            localValue5.addProperty("n", localValue4.n());
            localValue5.addProperty("ct", localValue4.ct());
            localValue3.sendJson("/app/globals/secure", localValue5.toString());
         } catch (Exception localValue6) {
         }
      }
   }

   private void establish() {
      try {
         if (this.sessionAuth) {
            String localValue1 = SessionManager.accessToken();
            if (localValue1 == null) {
               this.sessionAuth = false;
               this.authResult("session_expired");
               return;
            }

            this.jwt = localValue1;
         } else if (this.jwt == null) {
            this.authResult(LanguageManager.internalMethod07214("rocknet.auth.launcher_only"));
            return;
         }

         ProfileSync.refresh(this.httpBase, this.jwt);
         String localValue5 = this.httpPost(this.httpBase + "/ws-ticket", "ticket");
         String localValue2 = this.httpGet(this.httpBase + "/globals/pubkey", "key");
         if (!"".isEmpty() && !"".equals(localValue2.trim())) {
            serverVerified = false;
            this.authResult("key_mismatch");
            this.scheduleReconnect();
            return;
         }

         serverVerified = !"".isEmpty();
         this.crypto = new ClientCrypto(localValue2);
         this.handshakeDone.set(false);
         StompConnection localValue3 = new StompConnection(new StompConnection.InternalType0258() {
            @Override
            public void onConnected() {
               try {
                  JsonObject localValue1 = new JsonObject();
                  localValue1.addProperty("wrapped", RockNetClient.this.crypto.wrapMaster());
                  RockNetClient.this.stomp.sendJson("/app/globals/key-exchange", localValue1.toString());
                  RockNetClient.this.stomp.subscribe("/user/queue/globals", "sub-globals");
                  RockNetClient.this.stomp.subscribe("/topic/globals.online", "sub-online");
                  RockNetClient.this.handshakeDone.set(true);
                  RockNetClient.this.reconnectAttempts.set(0);
                  JsonObject localValue2x = new JsonObject();
                  localValue2x.addProperty("op", "sync");
                  RockNetClient.this.sendSecure(localValue2x);
                  boolean localValue3x = RockNetClient.this.initialSyncDone.compareAndSet(false, true);
                  RockNetClient.this.authResult(localValue3x ? "success" : "reconnected");
               } catch (Exception localValue4x) {
                  this.onError(localValue4x);
               }
            }

            @Override
            public void onMessage(String localValue1, String localValue2x) {
               RockNetClient.this.handleIncoming(localValue1, localValue2x);
            }

            @Override
            public void onClosed() {
               RockNetClient.this.handshakeDone.set(false);
               RockNetClient.this.fireClosed();
               RockNetClient.this.scheduleReconnect();
            }

            @Override
            public void onError(Throwable localValue1) {
               RockNetClient.this.handshakeDone.set(false);
               RockNetClient.this.fireError(localValue1);
               RockNetClient.this.scheduleReconnect();
            }
         });
         this.stomp = localValue3;
         localValue3.connect(this.wsUrl, localValue5);
      } catch (Exception localValue4) {
         logFailure("\u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0435 \u043a " + this.wsUrl, localValue4);
         this.jwt = null;
         this.authResult("connection_error");
         this.scheduleReconnect();
      }
   }

   private static void logFailure(String localValue0, Exception localValue1) {
      System.out
         .println("[Globals] " + localValue0 + " \u043d\u0435 \u0443\u0434\u0430\u043b\u0441\u044f: " + localValue1.getClass().getSimpleName() + ": " + localValue1.getMessage());
   }

   void handleIncoming(String localValue1, String localValue2) {
      try {
         JsonObject localValue3;
         if (localValue1 != null && localValue1.contains("globals.online")) {
            localValue3 = JsonParser.parseString(localValue2).getAsJsonObject();
         } else {
            JsonObject localValue4 = JsonParser.parseString(localValue2).getAsJsonObject();
            byte[] localValue5 = this.crypto.decrypt(localValue4.get("n").getAsLong(), localValue4.get("ct").getAsString());
            if (localValue5 == null) {
               return;
            }

            localValue3 = JsonParser.parseString(new String(localValue5, StandardCharsets.UTF_8)).getAsJsonObject();
         }

         Packet localValue7 = PacketCodec.fromMessage(localValue3);
         if (localValue7 != null && this.listener != null) {
            this.listener.onPacket(localValue7);
         }
      } catch (Exception localValue6) {
         RockstarClient.internalField0572
            .warn(
               "[Globals] \u043d\u0435 \u0440\u0430\u0437\u043e\u0431\u0440\u0430\u0442\u044c \u0432\u0445\u043e\u0434\u044f\u0449\u0438\u0439 \u043f\u0430\u043a\u0435\u0442 ({})",
               localValue1,
               localValue6
            );
      }
   }

   void scheduleReconnect() {
      if (this.sessionAuth) {
         int localValue1 = Math.min(this.reconnectAttempts.incrementAndGet(), 4);
         long localValue2 = Math.min(30L, 3L << localValue1 - 1);
         long localValue4 = localValue2 + ThreadLocalRandom.current().nextLong(localValue2 / 2L + 1L);
         this.exec.schedule(this::establish, localValue4, TimeUnit.SECONDS);
      }
   }

   void authResult(String localValue1) {
      if (this.listener != null) {
         this.listener.onPacket(new Packets.InternalType0063(localValue1));
      }
   }

   void fireClosed() {
      if (this.listener != null) {
         this.listener.onClosed();
      }
   }

   void fireError(Throwable localValue1) {
      if (this.listener != null) {
         this.listener.onError((Exception)(localValue1 instanceof Exception localValue2 ? localValue2 : new RuntimeException(localValue1)));
      }
   }

   private String httpPost(String localValue1, String localValue2) throws Exception {
      Request localValue3 = new Builder().url(localValue1).addHeader("Authorization", "Bearer " + this.jwt).post(RequestBody.create(JSON, "{}")).build();
      return this.readField(localValue3, localValue2);
   }

   private String httpGet(String localValue1, String localValue2) throws Exception {
      Request localValue3 = new Builder().url(localValue1).addHeader("Authorization", "Bearer " + this.jwt).get().build();
      return this.readField(localValue3, localValue2);
   }

   private String readField(Request localValue1, String localValue2) throws Exception {
      Response localValue3 = this.http.newCall(localValue1).execute();

      String localValue5;
      try {
         if (!localValue3.isSuccessful() || localValue3.body() == null) {
            throw new RuntimeException("HTTP " + localValue3.code());
         }

         JsonObject localValue4 = JsonParser.parseString(localValue3.body().string()).getAsJsonObject();
         localValue5 = localValue4.get(localValue2).getAsString();
      } catch (Throwable localValue7) {
         if (localValue3 != null) {
            try {
               localValue3.close();
            } catch (Throwable localValue6) {
               localValue7.addSuppressed(localValue6);
            }
         }

         throw localValue7;
      }

      if (localValue3 != null) {
         localValue3.close();
      }

      return localValue5;
   }

   @Generated
   public String getHttpBase() {
      return this.httpBase;
   }

   @Generated
   public String getActivity() {
      return this.activity;
   }

   @Generated
   public Packets.InternalType0031 getGameInfo() {
      return this.gameInfo;
   }

   @Generated
   public void setListener(RockNetClient.InternalType0515 localValue1) {
      this.listener = localValue1;
   }

   public interface InternalType0515 {
      void onPacket(Packet localValue1);

      default void onError(Exception localValue1) {
      }

      default void onClosed() {
      }
   }
}

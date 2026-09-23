package aethereal.network;

import aethereal.command.CommandExecutor;
import aethereal.core.User_2;
import aethereal.discord.ConnectionException;
import aethereal.discord.ConnectionFactory;
import aethereal.discord.DiscordBuild;
import aethereal.discord.DiscordIPCConfig;
import aethereal.discord.EventDispatcher;
import aethereal.discord.FailureInfo;
import aethereal.discord.NoDiscordClientException;
import aethereal.discord.PipeLocator;
import aethereal.discord.PipePathProvider;
import aethereal.discord.Platform;
import aethereal.discord.UnixConnection;
import aethereal.discord.WindowsConnection;
import aethereal.lib.javassist.Frame;
import aethereal.lib.javassist.OpCode;
import aethereal.lib.jsoup.Connection;
import aethereal.lib.log4j.LogManager;
import aethereal.lib.log4j.Logger;
import aethereal.util.JsonUtils;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import lombok.Generated;

public class ConnectionManager {
   @Generated
   private static final Logger a = LogManager.b(ConnectionManager.class);
   private final AtomicReference<ConnectionState> b = new AtomicReference<>(new ConnectionState.d());
   private final AtomicLong c = new AtomicLong(-1L);
   private final AtomicLong d = new AtomicLong();
   private final ExecutorService e = Executors.newSingleThreadExecutor(r -> {
      Thread t = new Thread(r, "jDRPC-worker");
      t.setDaemon(true);
      return t;
   });
   private final DiscordIPCConfig f;
   private final CommandExecutor g;
   private final EventDispatcher h;
   private final PipePathProvider i;
   private final ConnectionFactory j;
   private volatile Connection k;
   private volatile User_2 l;
   private volatile DiscordBuild m;
   private volatile Future<?> n;
   private Consumer<ConnectionState> o;

   @Generated
   public AtomicReference<ConnectionState> f() {
      return this.b;
   }

   @Generated
   public AtomicLong g() {
      return this.c;
   }

   @Generated
   public AtomicLong h() {
      return this.d;
   }

   @Generated
   public ExecutorService i() {
      return this.e;
   }

   @Generated
   public DiscordIPCConfig j() {
      return this.f;
   }

   @Generated
   public CommandExecutor k() {
      return this.g;
   }

   @Generated
   public EventDispatcher l() {
      return this.h;
   }

   @Generated
   public PipePathProvider m() {
      return this.i;
   }

   @Generated
   public ConnectionFactory n() {
      return this.j;
   }

   @Generated
   public Connection o() {
      return this.k;
   }

   @Generated
   public User_2 p() {
      return this.l;
   }

   @Generated
   public DiscordBuild q() {
      return this.m;
   }

   @Generated
   public Future<?> r() {
      return this.n;
   }

   @Generated
   public Consumer<ConnectionState> s() {
      return this.o;
   }

   public ConnectionManager(DiscordIPCConfig config, CommandExecutor commandExecutor, EventDispatcher eventDispatcher) {
      this(config, commandExecutor, eventDispatcher, PipeLocator::locateAll, path -> {
         switch (Platform.d) {
            case WINDOWS:
               return new WindowsConnection(path);
            case MACOS:
            case LINUX:
               return new UnixConnection(path);
            default:
               throw new IncompatibleClassChangeError();
         }
      });
   }

   ConnectionManager(
      DiscordIPCConfig config,
      CommandExecutor commandExecutor,
      EventDispatcher eventDispatcher,
      PipePathProvider pipePathProvider,
      ConnectionFactory connectionFactory
   ) {
      this.f = config;
      this.g = commandExecutor;
      this.h = eventDispatcher;
      this.i = pipePathProvider;
      this.j = connectionFactory;
   }

   public ConnectionState a() {
      return this.b.get();
   }

   public void a(Consumer<ConnectionState> listener) {
      this.o = listener;
   }

   private void a(ConnectionState newState) {
      this.b.set(newState);
      Optional.ofNullable(this.o).ifPresent(listener -> {
         try {
            listener.accept(newState);
         } catch (Exception var3) {
            a.f("State listener failed for {}", newState, var3);
         }
      });
   }

   public void b() {
      ConnectionState currentState = this.b.get();
      if (!(currentState instanceof ConnectionState.b) && !(currentState instanceof ConnectionState.c) && !(currentState instanceof ConnectionState.f)) {
         long generationToken = this.d.incrementAndGet();
         this.c.set(-1L);
         this.t();
         this.g.b();
         this.a(new ConnectionState.c());

         try {
            ConnectionManager.a result = this.e();
            this.a(generationToken, result, false);
         } catch (Exception var5) {
            if (!this.a(generationToken)) {
               a.a("Discarding stale connect failure for generation {}", generationToken, var5);
            } else {
               this.u();
               this.g.b();
               this.a(new ConnectionState.e(FailureInfo.a(var5)));
               if (var5 instanceof NoDiscordClientException) {
                  throw (NoDiscordClientException)var5;
               } else {
                  throw new ConnectionException("Failed to connect", var5);
               }
            }
         }
      } else {
         a.a("Ignoring connect() in state {}", currentState);
      }
   }

   public void c() {
      long generationToken = this.d.incrementAndGet();
      this.c.set(-1L);
      this.t();
      this.g.b();
      Connection conn = this.k;
      this.u();
      if (conn != null) {
         try {
            JsonObject closeData = new JsonObject();
            closeData.addProperty("code", 1000);
            closeData.addProperty("message", "Client disconnecting");
            conn.a(new Frame(OpCode.CLOSE, closeData));
         } catch (Exception var5) {
            a.a("Failed to send CLOSE frame during disconnect for generation {}", generationToken, var5);
         }

         this.a(conn, "disconnect");
      }

      this.g.a(new ConnectionException("Disconnected"));
      this.a(new ConnectionState.a());
      this.h.a();
      a.d("Disconnected from Discord");
   }

   public void d() {
      this.d.incrementAndGet();
      this.c.set(-1L);
      this.t();
      Connection conn = this.k;
      this.u();
      if (conn != null) {
         this.a(conn, "shutdown");
      }

      this.e.shutdownNow();
      this.g.b();
      this.g.a(new ConnectionException("Shutdown"));
      this.a(new ConnectionState.a());
      this.h.a();
      a.d("Shut down Discord IPC");
   }

   private void a(long generationToken, Connection conn) {
      this.n = this.e.submit(() -> {
         a.a("Read loop started for generation {}", generationToken);

         while (this.a(generationToken) && conn.a() && !Thread.currentThread().isInterrupted()) {
            try {
               Frame frame = conn.b();
               if (!this.a(generationToken)) {
                  return;
               }

               if (a.isDebugEnabled()) {
                  String preview = frame.c() != null ? frame.c().toString() : "null";
                  if (preview.length() > 200) {
                     preview = preview.substring(0, 200) + "...";
                  }

                  a.a("Received frame: op={}, data={}", frame.b(), preview);
               }

               switch (frame.b()) {
                  case CLOSE:
                     int closeCode = JsonUtils.a(frame.c(), "code", 0);
                     String closeMsg = JsonUtils.a(frame.c(), "message", "Discord closed connection");
                     a.d("Received CLOSE frame from Discord: code={}, message={}", closeCode, closeMsg);
                     this.a(closeCode, closeMsg, new ConnectionException(closeMsg), generationToken);
                     return;
                  case PING:
                     conn.a(new Frame(OpCode.PONG, frame.c()));
                     break;
                  case PONG:
                     a.a("Received PONG");
                     break;
                  case FRAME:
                     this.a(frame.c());
                     break;
                  default:
                     a.g("Unexpected opcode in read loop: {}", frame.b());
               }
            } catch (Exception var7) {
               if (!Thread.currentThread().isInterrupted()) {
                  a.f("Read loop error: {}", var7.getMessage(), var7);
                  this.a(0, var7.getMessage(), var7, generationToken);
               }
            }
         }

         a.a("Read loop ended for generation {}", generationToken);
      });
   }

   private void a(JsonObject json) {
      if (json != null) {
         String evt = JsonUtils.a(json, "evt").orElse(null);
         if ("ERROR".equals(evt) && JsonUtils.a(json, "nonce").isPresent()) {
            JsonObject data = JsonUtils.b(json, "data").orElse(null);
            int code = JsonUtils.a(data, "code", 1000);
            String message = JsonUtils.a(data, "message", "Unknown error");
            this.h.a(code, message);
         }

         boolean handled = this.g.a(json);
         a.a("Frame handled by command executor: {}", handled);
         if (!handled) {
            String cmd = JsonUtils.a(json, "cmd", "");
            if ("DISPATCH".equals(cmd)) {
               JsonUtils.a(json, "evt").ifPresent(eventName -> {
                  JsonObject eventData = JsonUtils.b(json, "data").orElse(null);
                  this.h.a(eventName, eventData);
               });
            }
         }
      }
   }

   private void a(int errorCode, String errorMessage, Throwable cause, long generationToken) {
      if (!this.d.compareAndSet(generationToken, generationToken + 1L)) {
         a.a("Ignoring stale disconnect for generation {}", generationToken);
      } else {
         long reconnectToken = generationToken + 1L;
         this.t();
         Connection conn = this.k;
         this.u();
         this.a(conn, "disconnect");
         this.g.b();
         this.g.a(new ConnectionException("Disconnected", cause));
         EventDispatcher eventDispatcher = this.h;
         String message;
         if (errorMessage != null) {
            message = errorMessage;
         } else {
            message = cause != null ? cause.getMessage() : "Unknown";
         }

         eventDispatcher.b(errorCode, message);
         if (!this.f.d()) {
            this.c.set(-1L);
            this.a(new ConnectionState.e(FailureInfo.a(cause)));
         } else {
            this.b(cause, reconnectToken);
         }
      }
   }

   private void a(Throwable initialCause, long generationToken) {
      int attempt = 1;
      int maxAttempts = this.f.e();
      long delay = this.f.f();
      long maxDelay = this.f.g();
      Throwable lastFailure = initialCause;

      while (!Thread.currentThread().isInterrupted() && this.a(generationToken) && (maxAttempts == 0 || attempt <= maxAttempts)) {
         this.a(new ConnectionState.f(attempt, FailureInfo.a(lastFailure)));
         a.a("Reconnecting (attempt {})...", attempt);

         try {
            Thread.sleep(delay);
            if (!this.a(generationToken)) {
               this.c.compareAndSet(generationToken, -1L);
               return;
            }

            try {
               ConnectionManager.a result = this.e();
               if (this.a(generationToken, result, true)) {
                  return;
               }

               attempt++;
               delay = Math.min(delay * 2L, maxDelay);
            } catch (Exception var12) {
               lastFailure = var12;
               a.a("Reconnect attempt {} failed: {}", attempt, var12.getMessage(), var12);
            }
         } catch (InterruptedException var13) {
            Thread.currentThread().interrupt();
            this.c.compareAndSet(generationToken, -1L);
            return;
         }
      }

      this.c.compareAndSet(generationToken, -1L);
      if (this.a(generationToken)) {
         this.a(new ConnectionState.e(FailureInfo.a(lastFailure)));
         a.b("Failed to reconnect after {} attempts", attempt - 1);
      }
   }

   ConnectionManager.a e() {
      List<String> paths = this.i.locateAll();
      boolean acceptAnyPreferred = this.f.c().contains(DiscordBuild.ANY);

      for (String path : paths) {
         try {
            ConnectionManager.a result = this.a(path);
            if (acceptAnyPreferred || this.f.c().contains(result.c)) {
               return result;
            }

            this.a(result.a, "skipping non-preferred build from " + path);
         } catch (Exception var7) {
            a.a("Pipe {} unavailable: {}", path, var7.getMessage(), var7);
         }
      }

      for (String path2 : paths) {
         try {
            return this.a(path2);
         } catch (Exception var6) {
            a.a("Pipe {} failed during second pass: {}", path2, var6.getMessage(), var6);
         }
      }

      throw new NoDiscordClientException();
   }

   ConnectionManager.a a(String path) throws IOException {
      Connection conn = null;
      boolean keepOpen = false;

      ConnectionManager.a var6;
      try {
         Connection conn2 = this.j.create(path);
         ConnectionManager.a result = this.a(conn2);
         keepOpen = true;
         var6 = result;
      } finally {
         if (!keepOpen) {
            this.a(conn, "failed handshake on " + path);
         }
      }

      return var6;
   }

   ConnectionManager.a a(Connection conn) throws IOException {
      JsonObject payload = new JsonObject();
      payload.addProperty("v", 1);
      payload.addProperty("client_id", String.valueOf(this.f.b()));
      conn.a(new Frame(OpCode.HANDSHAKE, payload));
      Frame response = conn.b();
      if (response.b() == OpCode.CLOSE) {
         throw new ConnectionException("Discord rejected handshake");
      } else if (response.b() != OpCode.FRAME) {
         throw new ConnectionException("Unexpected opcode in handshake response: " + response.b());
      } else {
         JsonObject data = response.c();
         if (data != null && !data.entrySet().isEmpty()) {
            JsonUtils.a(data, "cmd").filter(cmd -> !"DISPATCH".equals(cmd)).ifPresent(cmd2 -> {
               throw new ConnectionException("Unexpected handshake command: " + cmd2);
            });
            JsonUtils.a(data, "evt").filter(evt -> !"READY".equals(evt)).ifPresent(evt2 -> {
               throw new ConnectionException("Unexpected handshake event: " + evt2);
            });
            JsonObject responseData = JsonUtils.b(data, "data")
               .filter(d -> !d.entrySet().isEmpty())
               .orElseThrow(() -> new ConnectionException("Malformed handshake response: missing data object"));
            JsonObject userJson = JsonUtils.b(responseData, "user")
               .filter(u -> !u.entrySet().isEmpty())
               .orElseThrow(() -> new ConnectionException("No user in handshake response"));
            User_2 user = this.b(userJson);
            String endpoint = JsonUtils.b(responseData, "config").flatMap(cfg -> JsonUtils.a(cfg, "api_endpoint")).orElse(null);
            DiscordBuild build = DiscordBuild.a(endpoint);
            return new ConnectionManager.a(conn, user, build);
         } else {
            throw new ConnectionException("Empty handshake response");
         }
      }
   }

   private boolean a(long generationToken, ConnectionManager.a result, boolean reconnecting) {
      if (!this.a(generationToken)) {
         this.a(result.a, "stale activation for generation " + generationToken);
         return false;
      } else {
         this.k = result.a;
         this.l = result.b;
         this.m = result.c;
         this.g.a();
         this.c.compareAndSet(generationToken, -1L);
         this.a(new ConnectionState.b(result.b, result.c));
         this.h.a(result.b);
         this.a(generationToken, result.a);
         return true;
      }
   }

   private void b(Throwable cause, long generationToken) {
      if (!this.c.compareAndSet(-1L, generationToken)) {
         a.a("Reconnect already scheduled for generation {}", this.c.get());
      } else {
         try {
            this.e.submit(() -> this.a(cause, generationToken));
         } catch (RejectedExecutionException var5) {
            this.c.compareAndSet(generationToken, -1L);
            if (this.a(generationToken)) {
               this.a(new ConnectionState.e(FailureInfo.a(cause)));
            }

            a.f("Failed to schedule reconnect", var5);
         }
      }
   }

   private void t() {
      Future<?> currentReadFuture = this.n;
      if (currentReadFuture != null) {
         currentReadFuture.cancel(true);
         this.n = null;
      }
   }

   private void u() {
      this.k = null;
      this.l = null;
      this.m = null;
   }

   private boolean a(long generationToken) {
      return this.d.get() == generationToken;
   }

   private User_2 b(JsonObject userJson) {
      try {
         User_2 user = User_2.a(userJson);
         if (user.k() == null || user.k().isBlank()) {
            throw new ConnectionException("Handshake user is missing id");
         } else if (user.l() != null && !user.l().isBlank()) {
            user.c();
            return user;
         } else {
            throw new ConnectionException("Handshake user is missing username");
         }
      } catch (ConnectionException var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw new ConnectionException("Invalid user in handshake response", var4);
      }
   }

   private void a(Connection conn, String context) {
      if (conn != null) {
         try {
            conn.close();
         } catch (Exception var4) {
            a.f("Failed to close connection ({})", context, var4);
         }
      }
   }

   static final class a {
      final Connection a;
      final User_2 b;
      final DiscordBuild c;

      a(Connection connection, User_2 user, DiscordBuild build) {
         this.a = connection;
         this.b = user;
         this.c = build;
      }

      public Connection a() {
         return this.a;
      }

      public User_2 b() {
         return this.b;
      }

      public DiscordBuild c() {
         return this.c;
      }
   }
}

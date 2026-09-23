package aethereal.command;

import aethereal.discord.RpcErrorCode;
import aethereal.lib.javassist.Frame;
import aethereal.lib.javassist.OpCode;
import aethereal.lib.jsoup.Connection;
import aethereal.lib.log4j.LogManager;
import aethereal.lib.log4j.Logger;
import aethereal.util.JsonUtils;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Generated;

public class CommandExecutor {
   @Generated
   private static final Logger a = LogManager.b(CommandExecutor.class);
   private final ConcurrentHashMap<String, CompletableFuture<JsonObject>> b = new ConcurrentHashMap<>();
   private final AtomicLong c = new AtomicLong();
   private final AtomicBoolean d = new AtomicBoolean(false);
   private final long e;
   private final CommandExecutor.a f;

   public CommandExecutor(long commandTimeoutMs, int maxCommandsPerSecond) {
      if (commandTimeoutMs <= 0L) {
         throw new IllegalArgumentException("commandTimeoutMs must be > 0");
      } else if (maxCommandsPerSecond < 0) {
         throw new IllegalArgumentException("maxCommandsPerSecond must be >= 0");
      } else {
         this.e = commandTimeoutMs;
         this.f = maxCommandsPerSecond > 0 ? new CommandExecutor.a(maxCommandsPerSecond) : null;
      }
   }

   public void a() {
      this.d.set(true);
   }

   public void b() {
      this.d.set(false);
   }

   public JsonObject a(Connection connection, String cmd, JsonObject args, String evt) throws IOException {
      this.c();
      String nonce = String.valueOf(this.c.incrementAndGet());
      CompletableFuture<JsonObject> future = new CompletableFuture<>();
      this.b.put(nonce, future);

      try {
         try {
            try {
               this.a(nonce, future);
               JsonObject payload = new JsonObject();
               payload.addProperty("cmd", cmd);
               if (args != null) {
                  payload.add("args", args);
               }

               if (evt != null) {
                  payload.addProperty("evt", evt);
               }

               payload.addProperty("nonce", nonce);
               a.a("Sending command: {} (nonce: {})", cmd, nonce);
               this.d();
               this.a(nonce, future);
               connection.a(new Frame(OpCode.FRAME, payload));
               JsonObject jsonObject = future.get(this.e, TimeUnit.MILLISECONDS);
               this.b.remove(nonce, future);
               return jsonObject;
            } catch (CommandException var10) {
               throw var10;
            } catch (TimeoutException var11) {
               throw new IOException("Command timed out after " + this.e + " ms", var11);
            }
         } catch (InterruptedException var12) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException("Interrupted while waiting for command response");
         } catch (ExecutionException var13) {
            if (var13.getCause() instanceof CommandException ce) {
               throw ce;
            } else {
               throw new IOException("Command failed", var13.getCause());
            }
         } catch (Exception var14) {
            throw new IOException("Command timeout or error", var14);
         }
      } catch (Throwable var15) {
         this.b.remove(nonce, future);
         throw var15;
      }
   }

   public boolean a(JsonObject json) {
      String nonce = JsonUtils.a(json, "nonce").orElse(null);
      CompletableFuture<JsonObject> future;
      if (nonce != null && (future = this.b.remove(nonce)) != null) {
         String evt = JsonUtils.a(json, "evt").orElse(null);
         JsonObject data = JsonUtils.b(json, "data").orElse(null);
         if ("ERROR".equals(evt)) {
            int code = JsonUtils.a(data, "code", 1000);
            String message = JsonUtils.a(data, "message", "Unknown error");
            future.completeExceptionally(new CommandException(RpcErrorCode.a(code), message));
            return true;
         } else {
            future.complete(data != null ? data : new JsonObject());
            return true;
         }
      } else {
         return false;
      }
   }

   public void a(Throwable cause) {
      this.d.set(false);
      this.b.forEach((nonce, future) -> {
         if (this.b.remove(nonce, future)) {
            a.a("Cancelling pending command: {}", nonce);
            future.completeExceptionally(cause);
         }
      });
   }

   private void c() throws IOException {
      if (!this.d.get()) {
         throw new IOException("Connection is not available");
      }
   }

   private void a(String nonce, CompletableFuture<JsonObject> future) throws IOException {
      if (!this.d.get()) {
         this.b.remove(nonce, future);
         throw new IOException("Connection is not available");
      }
   }

   private void d() throws IOException {
      if (this.f != null) {
         try {
            this.f.a();
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException("Interrupted while waiting for command rate limiter");
         }
      }
   }

   static final class a {
      private final int a;
      private double b;
      private long c = System.nanoTime();

      a(int capacity) {
         this.a = capacity;
         this.b = capacity;
      }

      synchronized void a() throws InterruptedException {
         while (true) {
            this.b();
            if (this.b >= 1.0) {
               this.b--;
               return;
            }

            long waitNanos = (long)Math.ceil((1.0 - this.b) / this.a * 1.0E9);
            long waitMillis = Math.max(1L, waitNanos / 1000000L);
            int nanosPart = (int)Math.max(0L, waitNanos % 1000000L);
            this.wait(waitMillis, nanosPart);
         }
      }

      private void b() {
         long now = System.nanoTime();
         double elapsedSeconds = (now - this.c) / 1.0E9;
         this.b = Math.min((double)this.a, this.b + elapsedSeconds * this.a);
         this.c = now;
      }
   }
}

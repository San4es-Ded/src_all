package globals.client.auth;


import rockstar.client.network.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import globals.client.GlobalsUser;
import globals.client.Information;
import globals.client.api.RockNetClient;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.OkHttpClient.Builder;

public final class SessionManager {
   private static final MediaType JSON = MediaType.parse("application/json");
   private static final OkHttpClient HTTP = new Builder().callTimeout(10L, TimeUnit.SECONDS).build();
   private static volatile String httpBase;
   private static volatile Session session;
   private static volatile boolean failed;
   private static volatile boolean handoffUsed;
   private static volatile Runnable passwordFallback;
   private static volatile Thread early;

   private SessionManager() {
   }

   public static boolean isAvailable() {
      return failed ? false : session != null || handoffToken() != null || SessionStore.exists();
   }

   public static synchronized void setPasswordFallback(Runnable localValue0) {
      if (failed) {
         localValue0.run();
      } else {
         passwordFallback = localValue0;
      }
   }

   private static synchronized void giveUp() {
      failed = true;
      Runnable localValue0 = passwordFallback;
      passwordFallback = null;
      if (localValue0 != null) {
         localValue0.run();
      }
   }

   public static boolean isActive() {
      return session != null;
   }

   public static String username() {
      Session localValue0 = session;
      return localValue0 == null ? null : localValue0.username();
   }

   public static void bootstrapEarly(String localValue0) {
      httpBase = localValue0;
      Thread localValue1 = new Thread(() -> {
         Session localValue0x = restoreWithRetry();
         if (localValue0x == null) {
            giveUp();
         } else {
            apply(localValue0x);
         }
      }, "Globals-Auth-Early");
      localValue1.setDaemon(true);
      early = localValue1;
      localValue1.start();
   }

   public static void bootstrapAsync(RockNetClient localValue0) {
      httpBase = localValue0.getHttpBase();
      Thread localValue1 = new Thread(() -> {
         Thread localValue1x = early;
         if (localValue1x != null) {
            try {
               localValue1x.join();
            } catch (InterruptedException localValue3) {
               Thread.currentThread().interrupt();
               return;
            }
         }

         if (session != null) {
            localValue0.loginWithSession();
         } else if (!failed) {
            Session localValue2 = restoreWithRetry();
            if (localValue2 == null) {
               giveUp();
            } else {
               apply(localValue2);
               localValue0.loginWithSession();
            }
         }
      }, "Globals-Auth");
      localValue1.setDaemon(true);
      localValue1.start();
   }

   public static synchronized String accessToken() {
      Session localValue0 = session;
      if (localValue0 == null) {
         return null;
      } else if (localValue0.access() != null && !localValue0.stale()) {
         return localValue0.access();
      } else {
         SessionManager.InternalType0069 localValue1 = refresh(localValue0.refresh());
         if (localValue1.session() != null) {
            apply(localValue1.session());
            return localValue1.session().access();
         } else if (localValue1.rejected()) {
            clear();
            return null;
         } else {
            return localValue0.access() != null && System.currentTimeMillis() < localValue0.expiresAt() ? localValue0.access() : null;
         }
      }
   }

   public static synchronized void clear() {
      session = null;
      SessionStore.clear();
   }

   private static Session restoreWithRetry() {
      int localValue0 = 1;

      while (true) {
         SessionManager.InternalType0069 localValue1 = restore();
         if (localValue1.session() != null) {
            return localValue1.session();
         }

         if (localValue1.rejected() || localValue0 >= 3) {
            return null;
         }

         try {
            Thread.sleep(4000L);
         } catch (InterruptedException localValue3) {
            Thread.currentThread().interrupt();
            return null;
         }

         localValue0++;
      }
   }

   private static SessionManager.InternalType0069 restore() {
      boolean localValue0 = false;
      String localValue1 = handoffToken();
      if (localValue1 != null) {
         SessionManager.InternalType0069 localValue2 = exchange(localValue1);
         if (localValue2.session() != null) {
            handoffUsed = true;
            return localValue2;
         }

         if (localValue2.rejected()) {
            handoffUsed = true;
         } else {
            localValue0 = true;
         }
      }

      Session localValue4 = SessionStore.load();
      if (localValue4 != null && localValue4.refresh() != null) {
         SessionManager.InternalType0069 localValue3 = refresh(localValue4.refresh());
         if (localValue3.session() != null) {
            return localValue3;
         }

         if (localValue3.rejected()) {
            SessionStore.clear();
         } else {
            localValue0 = true;
         }
      }

      return new SessionManager.InternalType0069(null, !localValue0);
   }

   private static String handoffToken() {
      if (handoffUsed) {
         return null;
      } else {
         String localValue0 = blankToNull(System.getProperty("rockstar.session"));
         return localValue0 != null ? localValue0 : blankToNull(System.getenv("ROCKSTAR_SESSION"));
      }
   }

   private static String blankToNull(String localValue0) {
      return localValue0 != null && !localValue0.isBlank() ? localValue0 : null;
   }

   private static void apply(Session localValue0) {
      session = localValue0;
      SessionStore.save(localValue0);
      if (localValue0.username() != null) {
         Information.setPreferUser(new GlobalsUser(localValue0.username(), null, "Rockstar".toLowerCase()));
      }
   }

   private static SessionManager.InternalType0069 exchange(String localValue0) {
      JsonObject localValue1 = new JsonObject();
      localValue1.addProperty("token", localValue0);
      return post("/auth/game/exchange", localValue1);
   }

   private static SessionManager.InternalType0069 refresh(String localValue0) {
      if (localValue0 == null) {
         return new SessionManager.InternalType0069(null, true);
      } else {
         JsonObject localValue1 = new JsonObject();
         localValue1.addProperty("refresh", localValue0);
         return post("/auth/game/refresh", localValue1);
      }
   }

   private static SessionManager.InternalType0069 post(String localValue0, JsonObject localValue1) {
      String localValue2 = httpBase;
      if (localValue2 == null) {
         return new SessionManager.InternalType0069(null, false);
      } else {
         Request localValue3 = new okhttp3.Request.Builder().url(localValue2 + localValue0).post(RequestBody.create(JSON, localValue1.toString())).build();

         try {
            Response localValue4 = HTTP.newCall(localValue3).execute();

            SessionManager.InternalType0069 localValue8;
            label96: {
               SessionManager.InternalType0069 localValue6;
               try {
                  if (localValue4.isSuccessful() && localValue4.body() != null) {
                     JsonObject localValue12 = JsonParser.parseString(localValue4.body().string()).getAsJsonObject();
                     long localValue13 = localValue12.has("expiresIn") ? localValue12.get("expiresIn").getAsLong() : 900L;
                     localValue8 = new SessionManager.InternalType0069(
                        new Session(
                           localValue12.get("access").getAsString(),
                           localValue12.has("refresh") && !localValue12.get("refresh").isJsonNull() ? localValue12.get("refresh").getAsString() : null,
                           System.currentTimeMillis() + localValue13 * 1000L,
                           localValue12.has("username") ? localValue12.get("username").getAsString() : null,
                           localValue12.has("uid") ? localValue12.get("uid").getAsInt() : null,
                           localValue12.has("role") ? localValue12.get("role").getAsString() : null
                        ),
                        false
                     );
                     break label96;
                  }

                  boolean localValue5 = localValue4.code() >= 400 && localValue4.code() < 500;
                  localValue6 = new SessionManager.InternalType0069(null, localValue5);
               } catch (Throwable localValue10) {
                  if (localValue4 != null) {
                     try {
                        localValue4.close();
                     } catch (Throwable localValue9) {
                        localValue10.addSuppressed(localValue9);
                     }
                  }

                  throw localValue10;
               }

               if (localValue4 != null) {
                  localValue4.close();
               }

               return localValue6;
            }

            if (localValue4 != null) {
               localValue4.close();
            }

            return localValue8;
         } catch (Exception localValue11) {
            return new SessionManager.InternalType0069(null, false);
         }
      }
   }

   static final class InternalType0069 {
      private final Session session;
      private final boolean rejected;

      InternalType0069(Session localValue1, boolean localValue2) {
         this.session = localValue1;
         this.rejected = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0069[session=" + this.session() + ", rejected=" + this.rejected() + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.session());
         result = 31 * result + java.util.Objects.hashCode(this.rejected());
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         SessionManager.InternalType0069 other = (SessionManager.InternalType0069) localValue1;
         return java.util.Objects.equals(this.session(), other.session())
            && java.util.Objects.equals(this.rejected(), other.rejected());
      }

      public Session session() {
         return this.session;
      }

      public boolean rejected() {
         return this.rejected;
      }
   }
}

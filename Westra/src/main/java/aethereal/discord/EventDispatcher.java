package aethereal.discord;

import aethereal.core.User_2;
import aethereal.lib.log4j.LogManager;
import aethereal.lib.log4j.Logger;
import aethereal.util.JsonUtils;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Generated;

public class EventDispatcher {
   @Generated
   private static final Logger a = LogManager.b(EventDispatcher.class);
   private final List<DiscordEventListener> b = new CopyOnWriteArrayList<>();

   public void a(DiscordEventListener listener) {
      this.b.add(listener);
   }

   public void b(DiscordEventListener listener) {
      this.b.remove(listener);
   }

   public void a(User_2 user) {
      this.a("READY", (EventDispatcher.a)(listener -> listener.a(user)));
   }

   public void a(int errorCode, String message) {
      this.a("ERROR", (EventDispatcher.a)(listener -> listener.a(errorCode, message)));
   }

   public void b(int errorCode, String message) {
      this.a("DISCONNECT", (EventDispatcher.a)(listener -> listener.b(errorCode, message)));
   }

   public void a() {
      this.a("CLOSE", (EventDispatcher.a)(v0 -> v0.a()));
   }

   public void a(String eventName, JsonObject data) {
      switch (eventName) {
         case "ACTIVITY_JOIN":
            JsonUtils.a(data, "secret").ifPresent(secret -> this.a(eventName, (EventDispatcher.a)(listener -> listener.a(secret))));
            break;
         case "ACTIVITY_SPECTATE":
            JsonUtils.a(data, "secret").ifPresent(secret2 -> this.a(eventName, (EventDispatcher.a)(listener -> listener.b(secret2))));
            break;
         case "ACTIVITY_JOIN_REQUEST":
            JsonUtils.b(data, "user").ifPresent(userJson -> {
               try {
                  User_2 user = User_2.a(userJson);
                  this.a(eventName, (EventDispatcher.a)(listener -> listener.b(user)));
               } catch (RuntimeException var4) {
                  a.f("Failed to parse user payload for event {}", eventName, var4);
               }
            });
            break;
         default:
            a.a("Unknown event type: {}", eventName);
      }
   }

   private void a(String eventName, EventDispatcher.a callback) {
      for (DiscordEventListener listener : this.b) {
         try {
            callback.accept(listener);
         } catch (Exception var6) {
            a.f("Error in listener for event {}", eventName, var6);
         }
      }
   }

   @FunctionalInterface
   interface a {
      void accept(DiscordEventListener var1) throws Exception;
   }
}

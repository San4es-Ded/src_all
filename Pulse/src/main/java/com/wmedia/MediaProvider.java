package com.wmedia;

import java.lang.reflect.Method;
import java.util.Base64;
import java.util.Optional;
import org.json.JSONObject;

public final class MediaProvider {
   private static Method getMedia;
   private static Method control;
   private static boolean available;
   private static boolean initialized;

   private MediaProvider() {
   }

   private static synchronized void init() {
      if (!initialized) {
         initialized = true;

         try {
            Class<?> bridge = Class.forName("NativeMediaInfo");
            available = (Boolean)bridge.getMethod("isLoaded").invoke(null);
            getMedia = bridge.getMethod("getCurrentMediaJson");
            control = bridge.getMethod("controlCurrentMedia", String.class);
         } catch (Throwable ignored) {
            available = false;
         }
      }
   }

   public static Optional<MediaInfo> getCurrentMedia() {
      init();
      if (!available) {
         return Optional.empty();
      }

      try {
         String raw = (String)getMedia.invoke(null);
         if (raw != null && !raw.isBlank()) {
            JSONObject json = new JSONObject(raw);
            if (json.optBoolean("ok", true) && json.optBoolean("hasSession", false)) {
               String cover = json.optString("coverBase64", "");
               byte[] artwork = cover.isBlank() ? new byte[0] : Base64.getDecoder().decode(cover);
               return Optional.of(
                  new MediaInfo(
                     json.optString("title", ""),
                     json.optString("artist", ""),
                     json.optBoolean("isPlaying", "playing".equalsIgnoreCase(json.optString("status", ""))),
                     (float)json.optDouble("positionSeconds", 0.0) * 1000.0F,
                     (float)json.optDouble("durationSeconds", 0.0) * 1000.0F,
                     artwork
                  )
               );
            } else {
               return Optional.empty();
            }
         } else {
            return Optional.empty();
         }
      } catch (Throwable ignored) {
         return Optional.empty();
      }
   }

   public static Optional<AudioLevels> getAudioLevels() {
      return Optional.empty();
   }

   public static boolean isAvailable() {
      init();
      return available;
   }

   public static void playPause() {
      send("toggle");
   }

   public static void next() {
      send("next");
   }

   public static void previous() {
      send("previous");
   }

   public static void seek(long positionMs) {
   }

   public static void shutdown() {
   }

   private static void send(String action) {
      init();
      if (available) {
         try {
            control.invoke(null, action);
         } catch (Throwable var2) {
         }
      }
   }
}

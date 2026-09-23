package aethereal.lib.javassist;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;

public final class FrameSupport {
   private static final Gson GSON = new Gson();
   private static final int MAX_FRAME_SIZE = 1048576;

   private FrameSupport() {
   }

   public static void a(int length) throws FrameSupport.a {
      if (length < 0 || length > 1048576) {
         throw new FrameSupport.a("Invalid frame length: " + length);
      }
   }

   public static JsonObject a(byte[] bytes) {
      return bytes.length == 0 ? null : (JsonObject)GSON.fromJson(new String(bytes, StandardCharsets.UTF_8), JsonObject.class);
   }

   public static class a extends Exception {
      public a(String message) {
         super(message);
      }
   }
}

package aethereal.util;

import com.google.gson.JsonObject;
import java.util.Optional;

public class JsonUtils {
   private JsonUtils() {
   }

   public static Optional<String> a(JsonObject json, String field) {
      return json != null && json.has(field) && !json.get(field).isJsonNull() ? Optional.of(json.get(field).getAsString()) : Optional.empty();
   }

   public static String a(JsonObject json, String field, String defaultValue) {
      return a(json, field).orElse(defaultValue);
   }

   public static Optional<JsonObject> b(JsonObject json, String field) {
      return json != null && json.has(field) && json.get(field).isJsonObject() ? Optional.of(json.getAsJsonObject(field)) : Optional.empty();
   }

   public static boolean a(JsonObject json, String field, boolean defaultValue) {
      return json != null && json.has(field) && !json.get(field).isJsonNull() ? json.get(field).getAsBoolean() : defaultValue;
   }

   public static int a(JsonObject json, String field, int defaultValue) {
      return json != null && json.has(field) && !json.get(field).isJsonNull() ? json.get(field).getAsInt() : defaultValue;
   }
}

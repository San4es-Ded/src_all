package aethereal.network;

import aethereal.api.Compile;
import aethereal.core.NativeMethodLookup;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Compile
public class PacketSecurity {
   private byte[] a;
   private Gson b;

   public String a(String payload, String key) {
      Gson gson = this.b;
      Object objFromJson;
      if (payload == null || payload.isBlank() || (objFromJson = gson.fromJson(payload, JsonObject.class)) == null) {
         return null;
      } else if (!(objFromJson instanceof JsonObject jsonObject)) {
         throw new ClassCastException();
      } else {
         return jsonObject.has(key) && !jsonObject.get(key).isJsonNull() ? jsonObject.get(key).getAsString() : null;
      }
   }

   public JsonElement b(String payload, String key) {
      Gson gson = this.b;
      if (payload == null || payload.isBlank()) {
         return null;
      } else if (gson == null) {
         throw new NullPointerException();
      } else {
         Object objFromJson = gson.fromJson(payload, JsonObject.class);
         if (objFromJson == null) {
            return null;
         } else if (!(objFromJson instanceof JsonObject jsonObject)) {
            throw new ClassCastException();
         } else if (!jsonObject.has(key)) {
            return null;
         } else {
            JsonElement jsonElement = jsonObject.get(key);
            if (jsonElement == null) {
               throw new NullPointerException();
            } else {
               return jsonElement.isJsonNull() ? null : jsonObject.get(key);
            }
         }
      }
   }

   public List<String> c(String payload, String key) {
      Gson gson = this.b;
      if (payload == null || payload.isBlank()) {
         return null;
      } else if (gson == null) {
         throw new NullPointerException();
      } else {
         Object objFromJson = gson.fromJson(payload, JsonObject.class);
         if (objFromJson == null) {
            return null;
         } else if (!(objFromJson instanceof JsonObject jsonObject)) {
            throw new ClassCastException();
         } else if (!jsonObject.has(key)) {
            return null;
         } else {
            JsonElement jsonElement = jsonObject.get(key);
            if (jsonElement == null) {
               throw new NullPointerException();
            } else if (!jsonElement.isJsonArray()) {
               return null;
            } else {
               JsonArray asJsonArray = jsonObject.getAsJsonArray(key);
               if (asJsonArray == null) {
                  throw new NullPointerException();
               } else {
                  Stream stream = StreamSupport.stream(asJsonArray.spliterator(), false);
                  Function function = new Function() {
                     @Override
                     public Object apply(Object obj) {
                        return PacketSecurity.a((JsonElement)obj);
                     }
                  };
                  if (stream == null) {
                     throw new NullPointerException();
                  } else {
                     Stream map = stream.map(function);
                     Collector list = Collectors.toList();
                     if (map == null) {
                        throw new NullPointerException();
                     } else {
                        Object objCollect = map.collect(list);
                        if (objCollect == null) {
                           return null;
                        } else if (objCollect instanceof List) {
                           return (List<String>)objCollect;
                        } else {
                           throw new ClassCastException();
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public String a(Object... keyValues) {
      Gson gson = this.b;
      if (keyValues != null && (keyValues.length & 1) == 0) {
         JsonObject jsonObject = new JsonObject();

         for (int i = 0; i < keyValues.length; i += 2) {
            String strValueOf = String.valueOf(keyValues[i]);
            Object obj = keyValues[i + 1];
            JsonElement jsonPrimitive;
            if (obj instanceof JsonNode) {
               jsonPrimitive = JsonParser.parseString(((JsonNode)obj).toString());
            } else if (obj instanceof Number) {
               jsonPrimitive = new JsonPrimitive((Number)obj);
            } else {
               jsonPrimitive = (JsonElement)(obj instanceof Boolean ? new JsonPrimitive((Boolean)obj) : gson.toJsonTree(obj));
            }

            jsonObject.add(strValueOf, jsonPrimitive);
         }

         return gson.toJson(jsonObject);
      } else {
         throw new IllegalArgumentException("keyValues must contain even count of elements");
      }
   }

   public String d(String packetId, String payload) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("id", packetId);
      jsonObject.add("payload", this.b.toJsonTree(this.b(payload)));
      return this.b.toJson(jsonObject);
   }

   public Optional<PacketSecurity.a> a(String message) {
      try {
         JsonObject jsonObject = (JsonObject)this.b.fromJson(message, JsonObject.class);
         return jsonObject.has("id") && jsonObject.has("payload") && !jsonObject.get("id").isJsonNull() && !jsonObject.get("id").getAsString().isBlank()
            ? Optional.of(
               new PacketSecurity.a(
                  jsonObject.get("id").isJsonNull() ? null : jsonObject.get("id").getAsString(), this.c(jsonObject.get("payload").getAsString())
               )
            )
            : Optional.empty();
      } catch (Exception var3) {
         return Optional.empty();
      }
   }

   private JsonElement a(Object value) {
      if (value instanceof JsonNode) {
         return JsonParser.parseString(((JsonNode)value).toString());
      } else if (value instanceof Number) {
         return new JsonPrimitive((Number)value);
      } else {
         return (JsonElement)(value instanceof Boolean ? new JsonPrimitive((Boolean)value) : this.b.toJsonTree(value));
      }
   }

   public String b(String plain) {
      try {
         byte[] bArr = this.a;
         byte[] bArr2 = new byte[12];
         new SecureRandom().nextBytes(bArr2);
         Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
         cipher.init(1, new SecretKeySpec(bArr, "AES"), new GCMParameterSpec(128, bArr2));
         byte[] bArrDoFinal = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
         byte[] bArr3 = new byte[bArr2.length + bArrDoFinal.length];
         System.arraycopy(bArr2, 0, bArr3, 0, bArr2.length);
         System.arraycopy(bArrDoFinal, 0, bArr3, bArr2.length, bArrDoFinal.length);
         return Base64.getEncoder().encodeToString(bArr3);
      } catch (Exception var7) {
         return null;
      }
   }

   public String c(String encoded) {
      try {
         byte[] bArr = this.a;
         byte[] bArrDecode = Base64.getDecoder().decode(encoded);
         byte[] bArrCopyOfRange = Arrays.copyOfRange(bArrDecode, 0, 12);
         byte[] bArrCopyOfRange2 = Arrays.copyOfRange(bArrDecode, 12, bArrDecode.length);
         Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
         cipher.init(2, new SecretKeySpec(bArr, "AES"), new GCMParameterSpec(128, bArrCopyOfRange));
         return new String(cipher.doFinal(bArrCopyOfRange2), StandardCharsets.UTF_8);
      } catch (Exception var7) {
         return null;
      }
   }

   public static String a(JsonElement e) {
      if (e == null) {
         throw new NullPointerException();
      } else {
         return e.isJsonNull() ? null : e.getAsString();
      }
   }

   private void jc$init$0() {
      this.a = HexFormat.of().parseHex("9f3b7c1e6a8d4205b1e7c9a2f4d60837");
      this.b = new Gson();
   }

   public PacketSecurity() {
      this.jc$init$0();
   }

   static {
      NativeMethodLookup.lookup(PacketSecurity.class, 3);
   }

   public static final class a {
      private final String a;
      private final String b;

      public a(String id, String payload) {
         this.a = id;
         this.b = payload;
      }

      public String a() {
         return this.a;
      }

      public String b() {
         return this.b;
      }
   }
}

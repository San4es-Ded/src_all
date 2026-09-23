package aethereal.discord;

import aethereal.util.UrlValidator;
import com.google.gson.JsonObject;

public final class ActivityButton {
   private final String a;
   private final String b;

   public String b() {
      return this.a;
   }

   public String c() {
      return this.b;
   }

   public ActivityButton(String label, String url) {
      if (label == null || label.isEmpty() || label.length() > 32) {
         throw new IllegalArgumentException("Button label must be 1-32 characters, got: " + (label == null ? "null" : label.length()));
      } else if (url != null && url.length() <= 256) {
         UrlValidator.a(url, "Button URL", 256);
         this.a = label;
         this.b = url;
      } else {
         throw new IllegalArgumentException("Button URL must be at most 256 characters");
      }
   }

   public JsonObject a() {
      JsonObject json = new JsonObject();
      json.addProperty("label", this.a);
      json.addProperty("url", this.b);
      return json;
   }
}

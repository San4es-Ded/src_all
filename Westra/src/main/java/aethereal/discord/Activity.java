package aethereal.discord;

import aethereal.util.UrlValidator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Activity {
   private final ActivityType a;
   private final String b;
   private final String c;
   private final String d;
   private final ActivityTimestamps e;
   private final ActivityAssets f;
   private final ActivityParty g;
   private final ActivitySecrets h;
   private final List<ActivityButton> i;
   private final Boolean j;

   public ActivityType k() {
      return this.a;
   }

   public String l() {
      return this.b;
   }

   public String m() {
      return this.c;
   }

   public String n() {
      return this.d;
   }

   public ActivityTimestamps o() {
      return this.e;
   }

   public ActivityAssets p() {
      return this.f;
   }

   public ActivityParty q() {
      return this.g;
   }

   public ActivitySecrets r() {
      return this.h;
   }

   public List<ActivityButton> s() {
      return this.i;
   }

   public Boolean t() {
      return this.j;
   }

   public Activity(
      ActivityType type,
      String state,
      String details,
      String url,
      ActivityTimestamps timestamps,
      ActivityAssets assets,
      ActivityParty party,
      ActivitySecrets secrets,
      List<ActivityButton> buttons,
      Boolean instance
   ) {
      this.a = type != null ? type : ActivityType.PLAYING;
      this.b = state;
      this.c = details;
      this.d = url;
      this.e = timestamps;
      this.f = assets;
      this.g = party;
      this.h = secrets;
      this.i = buttons != null ? List.copyOf(buttons) : null;
      this.j = instance;
      if (this.b == null || this.b.length() >= 2 && this.b.length() <= 128) {
         if (this.c == null || this.c.length() >= 2 && this.c.length() <= 128) {
            if (this.i != null && this.i.size() > 2) {
               throw new IllegalArgumentException("Activity supports a maximum of 2 buttons, got " + this.i.size());
            } else if (this.a == ActivityType.STREAMING && this.d == null) {
               throw new IllegalArgumentException("Streaming activity type requires a URL");
            } else {
               if (this.a == ActivityType.STREAMING) {
                  UrlValidator.a(this.d, "Streaming URL", -1);
               }
            }
         } else {
            throw new IllegalArgumentException("Activity details must be 2-128 characters, got " + this.c.length());
         }
      } else {
         throw new IllegalArgumentException("Activity state must be 2-128 characters, got " + this.b.length());
      }
   }

   public Optional<String> a() {
      return Optional.ofNullable(this.b);
   }

   public Optional<String> b() {
      return Optional.ofNullable(this.c);
   }

   public Optional<String> c() {
      return Optional.ofNullable(this.d);
   }

   public Optional<ActivityTimestamps> d() {
      return Optional.ofNullable(this.e);
   }

   public Optional<ActivityAssets> e() {
      return Optional.ofNullable(this.f);
   }

   public Optional<ActivityParty> f() {
      return Optional.ofNullable(this.g);
   }

   public Optional<ActivitySecrets> g() {
      return Optional.ofNullable(this.h);
   }

   public Optional<List<ActivityButton>> h() {
      return Optional.ofNullable(this.i);
   }

   public Optional<Boolean> i() {
      return Optional.ofNullable(this.j);
   }

   public JsonObject j() {
      JsonObject json = new JsonObject();
      json.addProperty("type", this.a.a());
      this.a().ifPresent(s -> json.addProperty("state", s));
      this.b().ifPresent(d -> json.addProperty("details", d));
      this.c().ifPresent(u -> json.addProperty("url", u));
      this.d().ifPresent(t -> json.add("timestamps", t.a()));
      this.e().ifPresent(a2 -> json.add("assets", a2.a()));
      this.f().ifPresent(p -> json.add("party", p.a()));
      this.g().ifPresent(s2 -> json.add("secrets", s2.a()));
      this.h().filter(b -> !b.isEmpty()).ifPresent(b2 -> {
         JsonArray arr = new JsonArray();
         b2.forEach(btn -> arr.add(btn.a()));
         json.add("buttons", arr);
      });
      this.i().ifPresent(i -> json.addProperty("instance", i));
      return json;
   }

   public static final class a {
      private String b;
      private String c;
      private String d;
      private ActivityTimestamps e;
      private ActivityAssets f;
      private ActivityParty g;
      private ActivitySecrets h;
      private Boolean j;
      private ActivityType a = ActivityType.PLAYING;
      private final List<ActivityButton> i = new ArrayList<>();

      public Activity.a a(ActivityType type) {
         this.a = type;
         return this;
      }

      public Activity.a a(String state) {
         this.b = state;
         return this;
      }

      public Activity.a b(String details) {
         this.c = details;
         return this;
      }

      public Activity.a c(String url) {
         this.d = url;
         return this;
      }

      public Activity.a a(long epochSeconds) {
         this.e = new ActivityTimestamps(epochSeconds, this.e != null ? this.e.c() : null);
         return this;
      }

      public Activity.a b(long epochSeconds) {
         this.e = new ActivityTimestamps(this.e != null ? this.e.b() : null, epochSeconds);
         return this;
      }

      public Activity.a a(ActivityTimestamps timestamps) {
         this.e = timestamps;
         return this;
      }

      public Activity.a d(String key) {
         this.f = new ActivityAssets(key, this.f != null ? this.f.c() : null, this.f != null ? this.f.d() : null, this.f != null ? this.f.e() : null);
         return this;
      }

      public Activity.a a(String key, String text) {
         this.f = new ActivityAssets(key, text, this.f != null ? this.f.d() : null, this.f != null ? this.f.e() : null);
         return this;
      }

      public Activity.a b(String key, String text) {
         this.f = new ActivityAssets(this.f != null ? this.f.b() : null, this.f != null ? this.f.c() : null, key, text);
         return this;
      }

      public Activity.a a(ActivityAssets assets) {
         this.f = assets;
         return this;
      }

      public Activity.a a(String id, int currentSize, int maxSize) {
         this.g = ActivityParty.a(id, currentSize, maxSize);
         return this;
      }

      public Activity.a a(String id, int currentSize, int maxSize, int privacy) {
         this.g = ActivityParty.a(id, currentSize, maxSize, privacy);
         return this;
      }

      public Activity.a a(ActivitySecrets secrets) {
         this.h = secrets;
         return this;
      }

      public Activity.a c(String label, String url) {
         this.i.add(new ActivityButton(label, url));
         return this;
      }

      public Activity.a a(boolean instance) {
         this.j = instance;
         return this;
      }

      public Activity a() {
         return new Activity(this.a, this.b, this.c, this.d, this.e, this.f, this.g, this.h, this.i.isEmpty() ? null : this.i, this.j);
      }
   }
}

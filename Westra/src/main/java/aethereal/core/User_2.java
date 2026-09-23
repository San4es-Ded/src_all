package aethereal.core;

import aethereal.util.JsonUtils;
import aethereal.util.ObjectUtils;
import aethereal.util.StringUtils;
import com.google.gson.JsonObject;
import java.util.Objects;
import java.util.Optional;

public final class User_2 {
   private final String a;
   private final String b;
   private final String c;
   private final String d;
   private final String e;
   private final boolean f;

   public String k() {
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

   public String o() {
      return this.e;
   }

   public boolean p() {
      return this.f;
   }

   public User_2(String id, String username, String discriminator, String globalName, String avatar, boolean bot) {
      this.a = id;
      this.b = username;
      this.c = StringUtils.e(discriminator, "0");
      this.d = globalName;
      this.e = avatar;
      this.f = bot;
   }

   public static User_2 a(JsonObject json) {
      return new User_2(
         JsonUtils.a(json, "id", "0"),
         JsonUtils.a(json, "username", "Unknown"),
         JsonUtils.a(json, "discriminator", "0"),
         JsonUtils.a(json, "global_name").orElse(null),
         JsonUtils.a(json, "avatar").orElse(null),
         JsonUtils.a(json, "bot", false)
      );
   }

   public Optional<String> a() {
      return Optional.ofNullable(this.d);
   }

   public Optional<String> b() {
      return Optional.ofNullable(this.e);
   }

   public long c() {
      try {
         return Long.parseLong(this.a);
      } catch (NumberFormatException var2) {
         throw new IllegalStateException("User ID is not a numeric Discord snowflake: " + this.a, var2);
      }
   }

   public String d() {
      return ObjectUtils.a(StringUtils.w(this.d), this.b);
   }

   public String e() {
      return this.b;
   }

   public String f() {
      return "0".equals(this.c) ? this.b : this.b + "#" + this.c;
   }

   public Optional<String> g() {
      return this.b().map(a -> {
         String ext = a.startsWith("a_") ? "gif" : "png";
         return "https://cdn.discordapp.com/avatars/" + this.a + "/" + a + "." + ext;
      });
   }

   public String h() {
      int index;
      try {
         if ("0".equals(this.c)) {
            index = (int)((this.c() >> 22) % 6L);
         } else {
            index = Integer.parseInt(this.c) % 5;
         }
      } catch (RuntimeException var3) {
         index = 0;
      }

      return "https://cdn.discordapp.com/embed/avatars/" + index + ".png";
   }

   public String i() {
      return this.g().orElseGet(this::h);
   }

   public String j() {
      return "<@" + this.a + ">";
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         return !(o instanceof User_2 u) ? false : Objects.equals(this.a, u.a);
      }
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.a);
   }

   @Override
   public String toString() {
      return "User:" + this.f() + "(" + this.a + ")";
   }
}

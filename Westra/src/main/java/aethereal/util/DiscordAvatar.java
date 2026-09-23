package aethereal.util;

import aethereal.core.Interface;
import aethereal.core.User_2;
import aethereal.core.Westra;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_2960;

public class DiscordAvatar implements Interface {
   private static final class_2960 a = class_2960.method_60655("westra", "pictures/avatar.png");
   private static final class_2960 b = class_2960.method_60655("westra", "discord_avatar");
   private static String c;
   private static boolean d;
   private static boolean e;

   @Generated
   private DiscordAvatar() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static class_2960 a() {
      b();
      return e ? b : a;
   }

   private static void b() {
      if (Westra.h() != null && Westra.h().d() != null && Westra.h().d().g() != null) {
         Optional<String> url = c();
         if (!url.isEmpty()) {
            String link = url.get();
            if (!link.equals(c)) {
               if (!d) {
                  c = link;
                  d = true;
                  new Thread(() -> a(link), "westra-discord-avatar").start();
               }
            }
         }
      }
   }

   private static Optional<String> c() {
      try {
         if (Westra.h().d().g().a() == null) {
            return Optional.empty();
         } else {
            Optional<User_2> user = Westra.h().d().g().a().f();
            return user.isEmpty() ? Optional.empty() : user.get().g();
         }
      } catch (Throwable var1) {
         return Optional.empty();
      }
   }

   private static void a(String link) {
      try {
         try (HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5L)).build()) {
            HttpRequest request = HttpRequest.newBuilder(URI.create(link + "?size=128")).timeout(Duration.ofSeconds(10L)).GET().build();
            HttpResponse<InputStream> response = client.send(request, BodyHandlers.ofInputStream());
            if (response.statusCode() == 200) {
               class_1011 image = class_1011.method_4309(response.body());
               aM_.execute(() -> {
                  try {
                     aM_.method_1531().method_4616(b, new class_1043(image));
                     e = true;
                  } catch (Throwable var5) {
                     image.close();
                  } finally {
                     d = false;
                  }
               });
               return;
            }

            d = false;
         }
      } catch (Throwable var7) {
         d = false;
      }
   }
}

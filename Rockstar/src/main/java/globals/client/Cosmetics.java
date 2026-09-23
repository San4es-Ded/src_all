package globals.client;


import rockstar.client.internal.network.*;
import globals.client.api.RockNetClient;
import globals.shared.proto.Packets;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import rockstar.client.RockstarClient;
import rockstar.client.internal.network.WebImageUtils;
import rockstar.client.MinecraftClientAccess;

public final class Cosmetics {
   public static final String NICK_PLAIN = "plain";
   private static final Map<String, Packets.InternalType0424> nickStyles = new ConcurrentHashMap<>();
   private static final Map<String, Identifier> badges = new ConcurrentHashMap<>();
   private static final Map<String, Boolean> badgeLoading = new ConcurrentHashMap<>();
   private static final Map<String, Long> badgeRetryAt = new ConcurrentHashMap<>();
   private static volatile String selfBadge = "";
   private static volatile String selfNick = "plain";

   private Cosmetics() {
   }

   public static void applySelf(String localValue0, String localValue1) {
      selfBadge = localValue0 == null ? "" : localValue0;
      selfNick = localValue1 != null && !localValue1.isEmpty() ? localValue1 : "plain";
   }

   public static String selfBadge() {
      return selfBadge;
   }

   public static String selfNick() {
      return selfNick;
   }

   public static void apply(List<Packets.InternalType0424> localValue0) {
      if (localValue0 != null) {
         nickStyles.clear();

         for (Packets.InternalType0424 localValue2 : localValue0) {
            if ("NICK".equals(localValue2.type()) && localValue2.key() != null) {
               nickStyles.put(localValue2.key(), localValue2);
            }
         }
      }
   }

   public static Packets.InternalType0424 nick(String localValue0) {
      if (localValue0 != null && !localValue0.isEmpty() && !"plain".equals(localValue0)) {
         Packets.InternalType0424 localValue1 = nickStyles.get(localValue0);
         return localValue1 != null && localValue1.stops() != null && localValue1.stops().size() >= 2 ? localValue1 : null;
      } else {
         return null;
      }
   }

   public static Identifier badge(String localValue0) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         Identifier localValue1 = badges.get(localValue0);
         if (localValue1 != null) {
            return localValue1;
         } else {
            long localValue2 = System.currentTimeMillis();
            Long localValue4 = badgeRetryAt.get(localValue0);
            if (localValue4 != null && localValue2 < localValue4) {
               return null;
            } else if (badgeLoading.putIfAbsent(localValue0, Boolean.TRUE) != null) {
               return null;
            } else {
               new Thread(() -> {
                  try {
                     RockNetClient localValue1x = RockstarClient.getInstance().internalMethod06050();
                     String localValue2x = localValue1x.getHttpBase() + "/cosmetics/badge/" + localValue0 + ".png";
                     BufferedImage localValue3 = ImageIO.read(URI.create(localValue2x).toURL());
                     if (localValue3 == null) {
                        throw new IllegalStateException("\u043f\u0443\u0441\u0442\u043e\u0439 \u043e\u0442\u0432\u0435\u0442");
                     }

                     NativeImage localValue4x = WebImageUtils.internalMethod04372(localValue3, false);
                     Identifier localValue5 = RockstarClient.id("temp/badge/" + UUID.randomUUID());
                     MinecraftClientAccess.internalField0149.getTextureManager().registerTexture(localValue5, new NativeImageBackedTexture(() -> "Rockstar cosmetic", localValue4x));
                     badges.put(localValue0, localValue5);
                     badgeRetryAt.remove(localValue0);
                  } catch (Exception localValue9) {
                     badgeRetryAt.put(localValue0, System.currentTimeMillis() + 30000L);
                  } finally {
                     badgeLoading.remove(localValue0);
                  }
               }, "rockstar-badge-" + localValue0).start();
               return null;
            }
         }
      } else {
         return null;
      }
   }
}

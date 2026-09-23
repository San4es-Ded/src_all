package rockstar.client.internal.script;



import rockstar.client.internal.network.*;
import rockstar.client.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public final class SkinTextureLoader {
   private static final Map<String, Identifier> internalField0543 = new ConcurrentHashMap<>();
   private static final AtomicInteger internalField0678 = new AtomicInteger();
   private static final Executor internalField0252 = localValue0 -> {
      Thread localValue1 = new Thread(localValue0, "rockstar-skin");
      localValue1.setDaemon(true);
      localValue1.start();
   };

   private SkinTextureLoader() {
   }

   public static CompletableFuture<SkinTextures> internalMethod06608(String localValue0) {
      return CompletableFuture.supplyAsync(() -> MinecraftClient.getInstance().getApiServices().profileResolver().getProfileByName(localValue0), internalField0252)
         .thenCompose(
            localValue0x -> localValue0x.<CompletionStage<SkinTextures>>map(
                  localValue0xx -> MinecraftClient.getInstance().getSkinProvider().fetchSkinTextures(localValue0xx).thenApply(localValue0xxx -> (SkinTextures)localValue0xxx.orElse(null))
               )
               .orElseGet(() -> CompletableFuture.completedFuture(null))
         );
   }

   public static CompletableFuture<Identifier> internalMethod00977(String localValue0) {
      return internalMethod05436(localValue0, "skin", SkinTextureLoader::internalMethod00277);
   }

   public static CompletableFuture<Identifier> internalMethod08540(String localValue0) {
      return internalMethod05436(localValue0, "cape", SkinTextureLoader::internalMethod06887);
   }

   public static CompletableFuture<Identifier> internalMethod09023(String localValue0) {
      return internalMethod05436("http://s.optifine.net/capes/" + localValue0 + ".png", "cape", SkinTextureLoader::internalMethod06887);
   }

   private static CompletableFuture<Identifier> internalMethod05436(String localValue0, String localValue1, SkinTextureLoader.InternalType0179 localValue2) {
      if (localValue0 != null && !localValue0.isBlank()) {
         String localValue3 = localValue1 + ":" + localValue0;
         Identifier localValue4 = internalField0543.get(localValue3);
         return localValue4 != null
            ? CompletableFuture.completedFuture(localValue4)
            : CompletableFuture.<NativeImage>supplyAsync(() -> localValue2.apply(internalMethod02547(localValue0)), internalField0252)
               .thenCompose(localValue1x -> internalMethod00340(localValue1x, localValue1))
               .thenApply(localValue1x -> {
                  internalField0543.put(localValue3, localValue1x);
                  return (Identifier)localValue1x;
               });
      } else {
         return CompletableFuture.completedFuture(null);
      }
   }

   private static NativeImage internalMethod02547(String localValue0) {
      try {
         String localValue1 = localValue0.toLowerCase(Locale.ROOT);
         if (!localValue1.startsWith("http://") && !localValue1.startsWith("https://")) {
            Path localValue2 = Path.of(localValue0).toAbsolutePath().normalize();
            if (!Files.isRegularFile(localValue2)) {
               throw new IllegalArgumentException("\u0444\u0430\u0439\u043b\u0430 \u043d\u0435\u0442: " + localValue2);
            } else {
               return NativeImage.read(Files.readAllBytes(localValue2));
            }
         } else {
            return NativeImage.read(HttpDownloadUtils.internalMethod05458(localValue0));
         }
      } catch (Exception localValue3) {
         throw new RuntimeException(
            "\u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c " + localValue0 + ": " + localValue3.getMessage(),
            localValue3
         );
      }
   }

   private static CompletableFuture<Identifier> internalMethod00340(NativeImage localValue0, String localValue1) {
      CompletableFuture localValue2 = new CompletableFuture();
      Identifier localValue3 = RockstarClient.id("skins/" + localValue1 + "/" + internalField0678.incrementAndGet());
      MinecraftClient.getInstance().execute(() -> {
         try {
            MinecraftClient.getInstance().getTextureManager().registerTexture(localValue3, new NativeImageBackedTexture(() -> "Rockstar script image", localValue0));
            localValue2.complete(localValue3);
         } catch (Throwable localValue4) {
            localValue0.close();
            localValue2.completeExceptionally(localValue4);
         }
      });
      return localValue2;
   }

   private static NativeImage internalMethod00277(NativeImage localValue0) {
      if (localValue0.getWidth() == localValue0.getHeight() && localValue0.getWidth() != 64) {
         return localValue0;
      } else if (localValue0.getWidth() == 64 && (localValue0.getHeight() == 32 || localValue0.getHeight() == 64)) {
         boolean localValue1 = localValue0.getHeight() == 32;
         NativeImage localValue2 = localValue0;
         if (localValue1) {
            localValue2 = new NativeImage(64, 64, true);
            localValue2.copyFrom(localValue0);
            localValue0.close();
            localValue2.fillRect(0, 32, 64, 32, 0);
            localValue2.copyRect(4, 16, 16, 32, 4, 4, true, false);
            localValue2.copyRect(8, 16, 16, 32, 4, 4, true, false);
            localValue2.copyRect(0, 20, 24, 32, 4, 12, true, false);
            localValue2.copyRect(4, 20, 16, 32, 4, 12, true, false);
            localValue2.copyRect(8, 20, 8, 32, 4, 12, true, false);
            localValue2.copyRect(12, 20, 16, 32, 4, 12, true, false);
            localValue2.copyRect(44, 16, -8, 32, 4, 4, true, false);
            localValue2.copyRect(48, 16, -8, 32, 4, 4, true, false);
            localValue2.copyRect(40, 20, 0, 32, 4, 12, true, false);
            localValue2.copyRect(44, 20, -8, 32, 4, 12, true, false);
            localValue2.copyRect(48, 20, -16, 32, 4, 12, true, false);
            localValue2.copyRect(52, 20, -8, 32, 4, 12, true, false);
         }

         internalMethod01385(localValue2, 0, 0, 32, 16);
         if (localValue1) {
            internalMethod02952(localValue2, 32, 0, 64, 32);
         }

         internalMethod01385(localValue2, 0, 16, 64, 32);
         internalMethod01385(localValue2, 16, 48, 48, 64);
         return localValue2;
      } else {
         throw new IllegalArgumentException(
            "\u0441\u043a\u0438\u043d \u0434\u043e\u043b\u0436\u0435\u043d \u0431\u044b\u0442\u044c 64x64 \u0438\u043b\u0438 64x32, \u0430 \u043d\u0435 "
               + localValue0.getWidth()
               + "x"
               + localValue0.getHeight()
         );
      }
   }

   private static NativeImage internalMethod06887(NativeImage localValue0) {
      if (localValue0.getWidth() == localValue0.getHeight() * 2) {
         return localValue0;
      } else {
         int localValue1 = Math.max(64, internalMethod00906(localValue0.getWidth()));
         int localValue2 = localValue1 / 2;
         NativeImage localValue3 = new NativeImage(localValue1, localValue2, true);
         localValue3.fillRect(0, 0, localValue1, localValue2, 0);
         localValue3.copyFrom(localValue0);
         localValue0.close();
         return localValue3;
      }
   }

   private static void internalMethod01385(NativeImage localValue0, int localValue1, int localValue2, int localValue3, int localValue4) {
      for (int localValue5 = localValue1; localValue5 < localValue3; localValue5++) {
         for (int localValue6 = localValue2; localValue6 < localValue4; localValue6++) {
            localValue0.setColorArgb(localValue5, localValue6, localValue0.getColorArgb(localValue5, localValue6) | 0xFF000000);
         }
      }
   }

   private static void internalMethod02952(NativeImage localValue0, int localValue1, int localValue2, int localValue3, int localValue4) {
      for (int localValue5 = localValue1; localValue5 < localValue3; localValue5++) {
         for (int localValue6 = localValue2; localValue6 < localValue4; localValue6++) {
            if (ColorHelper.getAlpha(localValue0.getColorArgb(localValue5, localValue6)) < 128) {
               return;
            }
         }
      }

      for (int localValue7 = localValue1; localValue7 < localValue3; localValue7++) {
         for (int localValue8 = localValue2; localValue8 < localValue4; localValue8++) {
            localValue0.setColorArgb(localValue7, localValue8, localValue0.getColorArgb(localValue7, localValue8) & 16777215);
         }
      }
   }

   private static int internalMethod00906(int localValue0) {
      byte localValue1 = 1;

      while (localValue1 < localValue0) {
         localValue1 <<= 1;
      }

      return localValue1;
   }

   interface InternalType0179 {
      NativeImage apply(NativeImage localValue1);
   }
}

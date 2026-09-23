package pyrock.utility.render;





import rockstar.client.render.*;
import rockstar.client.asset.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.network.*;
import com.google.gson.Gson;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import jep.python.PyCallable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.NativeImage.Format;
import net.minecraft.util.Identifier;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.internal.script.EmbeddedPythonRuntime;
import rockstar.client.render.FontFamily;
import rockstar.client.internal.script.ConfigManager;
import rockstar.client.RockstarClient;
import rockstar.client.internal.network.WebImageUtils;
import rockstar.client.asset.FileDownloader;

public class PyAssets {
   private static final Gson GSON = new Gson();
   private static final Map<String, PyAssets.InternalType0143> TEXTURES = new ConcurrentHashMap<>();
   private static final Map<String, FontFamily> FONTS = new ConcurrentHashMap<>();
   private static final Map<String, Identifier> WEB_TEXTURES = new ConcurrentHashMap<>();

   public Identifier resource(String localValue1) {
      return RockstarClient.id(normalizeIdentifierPath(localValue1));
   }

   public Identifier image(String localValue1) {
      return this.image(null, localValue1);
   }

   public Identifier texture(String localValue1) {
      return this.image(localValue1);
   }

   public PyDynamicTexture dynamicTexture(String localValue1, int localValue2, int localValue3) {
      return new PyDynamicTexture(localValue1, localValue2, localValue3);
   }

   public PyPcmStream pcmStream(float localValue1, int localValue2) {
      return new PyPcmStream(localValue1, localValue2);
   }

   public Identifier image(String localValue1, String localValue2) {
      if (FileDownloader.internalMethod06346(localValue2)) {
         return this.webImage(localValue1, localValue2);
      } else {
         Path localValue3 = resolve(localValue2);
         if (!Files.isRegularFile(localValue3)) {
            throw new IllegalArgumentException("image file not found: " + localValue3);
         } else {
            try {
               Path localValue4 = localValue3.toAbsolutePath().normalize();
               long localValue5 = Files.getLastModifiedTime(localValue4).toMillis();
               String localValue7 = localValue4.toString();
               PyAssets.InternalType0143 localValue8 = TEXTURES.get(localValue7);
               if (localValue8 != null && localValue8.modified == localValue5) {
                  return localValue8.id;
               } else {
                  BufferedImage localValue9 = ImageIO.read(localValue4.toFile());
                  if (localValue9 == null) {
                     throw new IllegalArgumentException("unsupported image file: " + localValue4);
                  } else {
                     NativeImage localValue10 = WebImageUtils.internalMethod04372(localValue9, false);
                     Identifier localValue11 = RockstarClient.id("scripts/images/" + textureName(localValue1, localValue4));
                     MinecraftClient localValue12 = MinecraftClient.getInstance();
                     if (localValue8 != null && !localValue8.id.equals(localValue11)) {
                        localValue12.getTextureManager().destroyTexture(localValue8.id);
                     }

                     localValue12.getTextureManager().registerTexture(localValue11, new NativeImageBackedTexture(() -> "Rockstar PyAssets", localValue10));
                     TEXTURES.put(localValue7, new PyAssets.InternalType0143(localValue11, localValue5));
                     return localValue11;
                  }
               }
            } catch (IOException localValue13) {
               throw new RuntimeException("failed to load image: " + localValue3, localValue13);
            }
         }
      }
   }

   private Identifier webImage(String localValue1, String localValue2) {
      Identifier localValue3 = WEB_TEXTURES.get(localValue2);
      if (localValue3 != null) {
         return localValue3;
      } else {
         Identifier localValue4 = RockstarClient.id(
            "scripts/web/" + sanitize(localValue1 != null && !localValue1.isBlank() ? localValue1 : "image") + "_" + Integer.toHexString(localValue2.hashCode()) + ".png"
         );
         Identifier localValue5 = WEB_TEXTURES.putIfAbsent(localValue2, localValue4);
         if (localValue5 != null) {
            return localValue5;
         } else {
            MinecraftClient localValue6 = MinecraftClient.getInstance();
            localValue6.getTextureManager().registerTexture(localValue4, new NativeImageBackedTexture(() -> "Rockstar PyAssets placeholder", new NativeImage(Format.RGBA, 1, 1, false)));
            FileDownloader.internalMethod07340(
               localValue2,
               localValue3x -> {
                  try {
                     BufferedImage localValue4x = ImageIO.read(localValue3x.toFile());
                     if (localValue4x == null) {
                        throw new IllegalArgumentException("\u044d\u0442\u043e \u043d\u0435 \u043a\u0430\u0440\u0442\u0438\u043d\u043a\u0430");
                     }

                     localValue6.getTextureManager().registerTexture(localValue4, new NativeImageBackedTexture(() -> "Rockstar PyAssets download", WebImageUtils.internalMethod04372(localValue4x, false)));
                  } catch (Exception localValue5x) {
                     RockstarClient.internalField0572
                        .warn(
                           "\u0410\u0441\u0441\u0435\u0442\u044b: \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u0440\u043e\u0447\u0438\u0442\u0430\u0442\u044c \u043a\u0430\u0440\u0442\u0438\u043d\u043a\u0443 {}: {}",
                           localValue2,
                           localValue5x.getMessage()
                        );
                  }
               }
            );
            return localValue4;
         }
      }
   }

   public String download(String localValue1, PyCallable localValue2) {
      if (!FileDownloader.internalMethod06346(localValue1)) {
         Path localValue5 = resolve(localValue1);
         if (!Files.isRegularFile(localValue5)) {
            throw new IllegalArgumentException("file not found: " + localValue5);
         } else {
            String localValue6 = localValue5.toString();
            deliver(localValue2, localValue6);
            return localValue6;
         }
      } else {
         Path localValue3 = FileDownloader.internalMethod03010(localValue1);
         if (localValue3 != null) {
            String localValue4 = localValue3.toString();
            deliver(localValue2, localValue4);
            return localValue4;
         } else {
            FileDownloader.internalMethod07340(localValue1, localValue1x -> deliver(localValue2, localValue1x.toString()));
            return null;
         }
      }
   }

   private static void deliver(PyCallable localValue0, String localValue1) {
      if (localValue0 != null) {
         PythonScript localValue2 = PythonScript.internalMethod00581();
         MinecraftClient.getInstance().execute(() -> {
            if (localValue2 == null || localValue2.internalMethod08681()) {
               if (EmbeddedPythonRuntime.internalMethod08724()) {
                  try (AutoCloseable localValue3 = PythonScript.internalMethod02561(localValue2)) {
                     localValue0.call(new Object[]{localValue1});
                  } catch (Exception localValue8) {
                     RockstarClient.internalField0572.error("Python error in download callback:", localValue8);
                  }
               }
            }
         });
      }
   }

   public SizedFont font(String localValue1, float localValue2) {
      return slugFont(localValue1).internalMethod01432(localValue2);
   }

   public static FontFamily slugFont(String localValue0) {
      FontFamily localValue1 = FONTS.get(fontName(localValue0));
      return localValue1 != null ? localValue1 : Fonts.internalMethod00574(localValue0);
   }

   public FontFamily ttfFamily(String localValue1, String localValue2) {
      String localValue3 = fontName(localValue1);
      FontFamily localValue4 = FONTS.get(localValue3);
      if (localValue4 != null) {
         return localValue4;
      } else if (FileDownloader.internalMethod06346(localValue2)) {
         return this.webFont(localValue3, localValue2);
      } else {
         Path localValue5 = resolve(localValue2);
         if (!Files.isRegularFile(localValue5)) {
            throw new IllegalArgumentException("font file not found: " + localValue5);
         } else {
            try {
               Font localValue6 = Font.createFont(0, localValue5.toFile());
               FontFamily localValue7 = FontFamily.internalMethod04605(localValue3, localValue6);
               FONTS.put(localValue3, localValue7);
               return localValue7;
            } catch (Exception localValue8) {
               throw new RuntimeException("failed to load font: " + localValue1, localValue8);
            }
         }
      }
   }

   private FontFamily webFont(String localValue1, String localValue2) {
      Path localValue3 = FileDownloader.internalMethod03010(localValue2);
      if (localValue3 != null) {
         FontFamily localValue4 = this.readFont(localValue1, localValue3);
         if (localValue4 != null) {
            FONTS.put(localValue1, localValue4);
            return localValue4;
         }
      }

      FontFamily localValue5 = FontFamily.internalMethod00176(localValue1);
      FONTS.put(localValue1, localValue5);
      FileDownloader.internalMethod07340(
         localValue2,
         localValue2x -> {
            try {
               localValue5.internalMethod00602(Font.createFont(0, localValue2x.toFile()));
            } catch (Exception localValue4x) {
               RockstarClient.internalField0572
                  .warn(
                     "\u0410\u0441\u0441\u0435\u0442\u044b: \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u0440\u043e\u0447\u0438\u0442\u0430\u0442\u044c \u0448\u0440\u0438\u0444\u0442 {}: {}",
                     localValue2,
                     localValue4x.getMessage()
                  );
            }
         }
      );
      return localValue5;
   }

   private FontFamily readFont(String localValue1, Path localValue2) {
      try {
         return FontFamily.internalMethod04605(localValue1, Font.createFont(0, localValue2.toFile()));
      } catch (Exception localValue4) {
         RockstarClient.internalField0572
            .warn(
               "\u0410\u0441\u0441\u0435\u0442\u044b: \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u0440\u043e\u0447\u0438\u0442\u0430\u0442\u044c \u0448\u0440\u0438\u0444\u0442 {}: {}",
               localValue2,
               localValue4.getMessage()
            );
         return null;
      }
   }

   public String scriptsDir() {
      return scriptsRoot().toString();
   }

   public String assetsDir() {
      return scriptsRoot().toString();
   }

   public static Path resolve(String localValue0) {
      Path localValue1 = Path.of(localValue0);
      if (localValue1.isAbsolute()) {
         return localValue1.normalize();
      } else {
         Path localValue2 = scriptsRoot().resolve(localValue1).normalize();
         return Files.exists(localValue2) ? localValue2 : Path.of(ConfigManager.internalField0148.toURI()).resolve(localValue1).normalize();
      }
   }

   private static Path scriptsRoot() {
      return Path.of(ConfigManager.internalField0148.toURI()).resolve("scripts");
   }

   private static String fontName(String localValue0) {
      return sanitize(localValue0 != null && !localValue0.isBlank() ? localValue0 : "custom");
   }

   private static String textureName(String localValue0, Path localValue1) {
      String localValue2 = localValue0 != null && !localValue0.isBlank() ? localValue0 : stripExtension(localValue1.getFileName().toString());
      String localValue3 = extension(localValue1.getFileName().toString());
      String localValue4 = Integer.toHexString(localValue1.toString().hashCode());
      return sanitize(localValue2) + "_" + localValue4 + (localValue3.isBlank() ? ".png" : localValue3);
   }

   private static String stripExtension(String localValue0) {
      int localValue1 = localValue0.lastIndexOf(46);
      return localValue1 <= 0 ? localValue0 : localValue0.substring(0, localValue1);
   }

   private static String extension(String localValue0) {
      int localValue1 = localValue0.lastIndexOf(46);
      return localValue1 <= 0 ? "" : localValue0.substring(localValue1).toLowerCase(Locale.ROOT);
   }

   private static String normalizeIdentifierPath(String localValue0) {
      return localValue0.replace('\\', '/').replaceAll("^/+", "");
   }

   private static String sanitize(String localValue0) {
      String localValue1 = localValue0.toLowerCase(Locale.ROOT).replace('\\', '/').replaceAll("[^a-z0-9/._-]", "_").replaceAll("_+", "_").replaceAll("^_+|_+$", "");
      return localValue1.isBlank() ? "asset" : localValue1;
   }

   static final class InternalType0143 {
      final Identifier id;
      final long modified;

      InternalType0143(Identifier localValue1, long localValue2) {
         this.id = localValue1;
         this.modified = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0143[id=" + this.id() + ", modified=" + this.modified() + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.id());
         result = 31 * result + java.util.Objects.hashCode(this.modified());
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         PyAssets.InternalType0143 other = (PyAssets.InternalType0143) localValue1;
         return java.util.Objects.equals(this.id(), other.id())
            && java.util.Objects.equals(this.modified(), other.modified());
      }

      public Identifier id() {
         return this.id;
      }

      public long modified() {
         return this.modified;
      }
   }
}

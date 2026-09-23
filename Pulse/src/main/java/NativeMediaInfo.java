import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class NativeMediaInfo {
   private static boolean loaded;

   private NativeMediaInfo() {
   }

   public static boolean isLoaded() {
      return loaded;
   }

   public static native String getCurrentMediaJson();

   public static native boolean controlCurrentMedia(String var0);

   static {
      try {
         Path dir = Path.of(System.getProperty("java.io.tmpdir"), "pulse-native");
         Files.createDirectories(dir);
         Path dll = dir.resolve("MediaInfoNative.dll");

         try (InputStream in = NativeMediaInfo.class.getResourceAsStream("/native/MediaInfoNative.dll")) {
            if (in == null) {
               throw new IllegalStateException("Missing MediaInfoNative.dll");
            }

            Files.copy(in, dll, StandardCopyOption.REPLACE_EXISTING);
         }

         System.load(dll.toAbsolutePath().toString());
         loaded = true;
      } catch (Throwable error) {
         loaded = false;
         System.err.println("[Pulse] MediaInfoNative: " + error.getMessage());
      }
   }
}

package aethereal.cosmetic.figura;

import aethereal.core.Interface;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_2960;

public final class CosmeticTextureCache {
   private static final Map<Path, class_2960> CACHE = new HashMap<>();
   private static final Map<Path, int[]> SIZE = new HashMap<>();

   private CosmeticTextureCache() {
   }

   public static class_2960 get(Path path) {
      return path == null ? null : CACHE.computeIfAbsent(path.toAbsolutePath().normalize(), CosmeticTextureCache::load);
   }

   public static int[] size(Path path) {
      get(path);
      return SIZE.getOrDefault(path.toAbsolutePath().normalize(), new int[]{1, 1});
   }

   private static class_2960 load(Path path) {
      try {
         class_2960 var4;
         try (InputStream in = Files.newInputStream(path)) {
            class_2960 id = class_2960.method_60655("westra", "cosmetics/" + Integer.toHexString(path.toString().hashCode()));
            class_1011 image = class_1011.method_4309(in);
            SIZE.put(path, new int[]{image.method_4307(), image.method_4323()});
            Interface.aM_.method_1531().method_4616(id, new class_1043(image));
            var4 = id;
         }

         return var4;
      } catch (Exception var7) {
         System.err.println("[Westra] Preview load failed for " + path + ": " + var7.getMessage());
         return null;
      }
   }
}

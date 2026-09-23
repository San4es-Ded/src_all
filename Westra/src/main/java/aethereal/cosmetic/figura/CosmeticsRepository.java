package aethereal.cosmetic.figura;

import aethereal.core.Interface;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public final class CosmeticsRepository {
   private static final String RESOURCE = "assets/westra/cosmetics";
   private final Path root = Interface.aM_.field_1697.toPath().resolve("Westra/cosmetics");
   private List<CosmeticEntry> entries;

   public Path root() {
      return this.root;
   }

   public synchronized List<CosmeticEntry> entries() {
      if (this.entries != null) {
         return this.entries;
      } else {
         this.extractBundled();
         ArrayList<CosmeticEntry> result = new ArrayList<>();

         try (Stream<Path> paths = Files.walk(this.root)) {
            paths.filter(p -> Files.isRegularFile(p.resolve("avatar.json")))
               .filter(p -> !p.getFileName().toString().startsWith("."))
               .forEach(p -> result.add(this.entry(p)));
         } catch (IOException var7) {
            System.err.println("[Westra] Failed to scan cosmetics: " + var7.getMessage());
         }

         result.sort(Comparator.comparing(CosmeticEntry::name, String.CASE_INSENSITIVE_ORDER));
         return this.entries = List.copyOf(result);
      }
   }

   private CosmeticEntry entry(Path directory) {
      String id = this.root.relativize(directory).toString().replace('\\', '/');
      String raw = directory.getFileName().toString();
      String lower = raw.toLowerCase(Locale.ROOT);
      CosmeticCategory category = lower.startsWith("head-")
         ? CosmeticCategory.HEAD
         : (lower.startsWith("weapon-") ? CosmeticCategory.WEAPONS : CosmeticCategory.MODELS);
      String name = raw.replaceFirst("(?i)^(head|weapon)-", "").replace('_', ' ');
      Path preview = directory.resolve("avatar.png");
      return new CosmeticEntry(id, name, category, directory, Files.isRegularFile(preview) ? preview : null);
   }

   private void extractBundled() {
      try {
         Files.createDirectories(this.root);
         URI uri = this.getClass().getClassLoader().getResource("assets/westra/cosmetics").toURI();
         FileSystem fs = null;
         Path source;
         if ("jar".equals(uri.getScheme())) {
            try {
               fs = FileSystems.newFileSystem(uri, Map.of());
            } catch (FileSystemAlreadyExistsException var10) {
               fs = FileSystems.getFileSystem(uri);
            }

            source = fs.getPath("/assets/westra/cosmetics");
         } else {
            source = Paths.get(uri);
         }

         try (Stream<Path> files = Files.walk(source)) {
            for (Path file : files.toList()) {
               Path relative = source.relativize(file);
               Path target = this.root.resolve(relative.toString());
               if (Files.isDirectory(file)) {
                  Files.createDirectories(target);
               } else if (!Files.exists(target)) {
                  Files.copy(file, target);
               }
            }
         }
      } catch (Exception var12) {
         System.err.println("[Westra] Failed to extract cosmetics: " + var12.getMessage());
      }
   }
}

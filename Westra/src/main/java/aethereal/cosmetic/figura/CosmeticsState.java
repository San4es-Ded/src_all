package aethereal.cosmetic.figura;

import aethereal.core.Interface;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Stream;

public final class CosmeticsState {
   private static String modelId = "";
   private static String weaponId = "";
   private static boolean restorePending;
   private static CosmeticsRepository autoRepository;
   private static long nextRestoreAttempt;
   private static String lastAppliedId = "";

   private CosmeticsState() {
   }

   public static String selected() {
      if (modelId.isEmpty()) {
         return weaponId.isEmpty() ? "" : "|" + weaponId;
      } else {
         return weaponId.isEmpty() ? modelId : modelId + "|" + weaponId;
      }
   }

   public static String selected(CosmeticCategory category) {
      if (category == CosmeticCategory.WEAPONS) {
         return weaponId;
      } else {
         return modelId.isEmpty() ? "" : (categoryOfId(modelId) == category ? modelId : "");
      }
   }

   public static void load(String id) {
      String raw = id == null ? "" : id.trim();
      int split = raw.indexOf(124);
      if (split >= 0) {
         modelId = raw.substring(0, split).trim();
         weaponId = raw.substring(split + 1).trim();
      } else if (!raw.isEmpty() && categoryOfId(raw) == CosmeticCategory.WEAPONS) {
         weaponId = raw;
         modelId = "";
      } else {
         modelId = raw;
         weaponId = "";
      }

      lastAppliedId = "";
      restorePending = !modelId.isEmpty() || !weaponId.isEmpty();
   }

   public static boolean apply(CosmeticEntry entry) {
      if (entry == null) {
         return false;
      } else {
         boolean weapon = entry.category() == CosmeticCategory.WEAPONS;
         String previousModel = modelId;
         String previousWeapon = weaponId;
         if (weapon) {
            weaponId = entry.id();
         } else {
            modelId = entry.id();
         }

         if (!applyComposite(null)) {
            modelId = previousModel;
            weaponId = previousWeapon;
            return false;
         } else {
            restorePending = false;
            return true;
         }
      }
   }

   public static void clear(CosmeticCategory category) {
      if (category != null) {
         if (category == CosmeticCategory.WEAPONS) {
            if (weaponId.isEmpty()) {
               return;
            }

            weaponId = "";
         } else {
            if (modelId.isEmpty() || categoryOfId(modelId) != category) {
               return;
            }

            modelId = "";
         }

         lastAppliedId = "";
         applyComposite(null);
      }
   }

   public static void clear() {
      FiguraBridge.clear();
      modelId = "";
      weaponId = "";
      lastAppliedId = "";
      restorePending = false;
   }

   public static void restore(CosmeticsRepository repository) {
      if (repository != null) {
         autoRepository = repository;
      }

      if (restorePending && Interface.aM_.field_1724 != null) {
         if (repository != null) {
            applyComposite(repository);
            restorePending = false;
         }
      }
   }

   public static void tickRestore() {
      if (restorePending) {
         if (Interface.aM_.field_1724 != null) {
            long now = System.currentTimeMillis();
            if (now >= nextRestoreAttempt) {
               nextRestoreAttempt = now + 1000L;
               if (autoRepository == null) {
                  autoRepository = new CosmeticsRepository();
               }

               restore(autoRepository);
            }
         }
      }
   }

   public static void onWorldLeave() {
      restorePending = !modelId.isEmpty() || !weaponId.isEmpty();
      nextRestoreAttempt = 0L;
      lastAppliedId = "";
   }

   public static CosmeticCategory[] renderableCategories() {
      return new CosmeticCategory[]{CosmeticCategory.MODELS, CosmeticCategory.HEAD, CosmeticCategory.WEAPONS};
   }

   public static String activeRenderId() {
      return selected();
   }

   public static CosmeticCategory categoryOfId(String id) {
      if (id == null) {
         return CosmeticCategory.MODELS;
      } else {
         String leaf = id;
         int slash = id.lastIndexOf(47);
         if (slash >= 0 && slash + 1 < id.length()) {
            leaf = id.substring(slash + 1);
         }

         String lower = leaf.toLowerCase(Locale.ROOT);
         if (lower.startsWith("head-")
            || lower.startsWith("head_")
            || lower.contains("glasses")
            || lower.contains("hat")
            || lower.contains("ears")
            || lower.contains("crown")
            || lower.contains("helmet")
            || lower.contains("halo")
            || lower.contains("ushanka")) {
            return CosmeticCategory.HEAD;
         } else {
            return !lower.startsWith("weapon-")
                  && !lower.startsWith("weapon_")
                  && !lower.contains("sword")
                  && !lower.contains("scythe")
                  && !lower.contains("axe")
                  && !lower.contains("halberd")
                  && !lower.contains("staff")
                  && !lower.contains("оружие")
                  && !lower.contains("меч")
                  && !lower.contains("коса")
                  && !lower.contains("молоток")
               ? CosmeticCategory.MODELS
               : CosmeticCategory.WEAPONS;
         }
      }
   }

   private static boolean applyComposite(CosmeticsRepository provided) {
      CosmeticsRepository repository = provided != null ? provided : (autoRepository != null ? autoRepository : (autoRepository = new CosmeticsRepository()));
      if (modelId.isEmpty() && weaponId.isEmpty()) {
         lastAppliedId = "";
         return FiguraBridge.clear();
      } else {
         String composite = selected();
         if (composite.equals(lastAppliedId) && FiguraBridge.available()) {
            return true;
         } else {
            CosmeticEntry model = find(repository, modelId);
            CosmeticEntry weapon = find(repository, weaponId);
            if (model == null && weapon == null) {
               return false;
            } else {
               Path avatar;
               if (model == null) {
                  avatar = weapon.directory();
               } else if (weapon == null) {
                  avatar = model.directory();
               } else {
                  avatar = merge(repository, model, weapon);
                  if (avatar == null) {
                     return false;
                  }
               }

               boolean ok = FiguraBridge.apply(avatar);
               if (ok) {
                  lastAppliedId = composite;
               }

               return ok;
            }
         }
      }
   }

   private static CosmeticEntry find(CosmeticsRepository repository, String id) {
      if (id != null && !id.isEmpty()) {
         for (CosmeticEntry entry : repository.entries()) {
            if (entry.id().equals(id)) {
               return entry;
            }
         }

         String leaf = leafOf(id);

         for (CosmeticEntry entryx : repository.entries()) {
            if (leafOf(entryx.id()).equalsIgnoreCase(leaf)) {
               return entryx;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private static Path merge(CosmeticsRepository repository, CosmeticEntry model, CosmeticEntry weapon) {
      try {
         Path target = repository.root().resolve(".westra-merged");
         if (Files.exists(target)) {
            try (Stream<Path> walk = Files.walk(target)) {
               for (Path path : walk.sorted(Comparator.reverseOrder()).toList()) {
                  Files.deleteIfExists(path);
               }
            }
         }

         Files.createDirectories(target);
         copy(model.directory(), target, "");
         copy(weapon.directory(), target, "weapon_");
         return target;
      } catch (Exception var9) {
         System.err.println("[Westra] Cosmetic merge failed: " + var9.getMessage());
         return null;
      }
   }

   private static void copy(Path source, Path target, String prefix) throws IOException {
      try (Stream<Path> walk = Files.walk(source)) {
         for (Path path : walk.toList()) {
            Path relative = source.relativize(path);
            if (!relative.toString().isEmpty()) {
               Path destination = target.resolve(relative.toString());
               if (Files.isDirectory(path)) {
                  Files.createDirectories(destination);
               } else {
                  String name = path.getFileName().toString();
                  if (prefix.isEmpty() || !name.equals("avatar.json") && !name.equals("avatar.png")) {
                     if (!prefix.isEmpty() && Files.exists(destination)) {
                        destination = destination.resolveSibling(prefix + name);
                     }

                     Files.createDirectories(destination.getParent());
                     Files.copy(path, destination, StandardCopyOption.REPLACE_EXISTING);
                  }
               }
            }
         }
      }
   }

   private static String leafOf(String id) {
      if (id == null) {
         return "";
      } else {
         int slash = Math.max(id.lastIndexOf(47), id.lastIndexOf(92));
         return slash >= 0 ? id.substring(slash + 1) : id;
      }
   }
}

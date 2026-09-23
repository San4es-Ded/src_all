package wtf.wyvern.integration.figura;

import org.figuramc.figura.avatar.local.LocalAvatarFetcher;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/** Installs and indexes the full avatar collection bundled with Wyvern. */
public final class FiguraAvatarLibrary {
    private static final String ARCHIVE_RESOURCE = "assets/wyvern/figura_avatars.bin";
    private static final String COLLECTION = "Wyvern Figura Library";
    private static final String LIBRARY_VERSION = "4";
    private static final String VERSION_FILE = ".wyvern-library-version";
    private static final List<String> LEGACY_PRESETS = List.of(
            "Wyvern Ultimate", "Dragon Wings", "Fox Spirit",
            "Royal Accessories", "Crystal Arsenal"
    );
    private static final Set<String> OBSOLETE_BUNDLED_AVATARS = Set.of(
            "01a_shizuko", "01b_shizuko_swimsuit", "02a_izuna", "02b_izuna_swimsuit",
            "03a_mari", "03b_mari_tracksuit", "03c_mari_idol", "04a_momoi", "04b_momoi_maid",
            "05a_midori", "05b_midori_maid", "06a_shiroko", "06b_shiroko_swimsuit",
            "06c_shiroko_riding", "07a_hoshino", "07b_hoshino_swimsuit", "07c_hoshino_battle",
            "08a_umika", "09a_serina", "09b_serina_christmas", "10a_iroha", "11a_ibuki",
            "12a_seia", "12b_seia_swimsuit", "13a_aris", "13b_aris_maid", "13c_aris_battle",
            "14a_yuzu", "14b_yuzu_maid", "14c_yuzu_battle", "15a_hikari", "16a_nozomi",
            "17a_reisa", "17b_reisa_magical", "18a_michiru", "18b_michiru_dress", "19a_hifumi",
            "19b_hifumi_swimsuit", "20a_hina", "20b_hina_swimsuit", "20c_hina_dress",
            "21a_hanae", "21b_hanae_christmas", "22a_kei", "atreus", "axe", "axolotl!",
            "beret", "bunny-asset", "castorice", "computer head", "cool coat",
            "cool coat - slim body", "cool scythe", "crazy rabbit_1", "fall crown", "flower hat",
            "leaf", "mikie", "mushroom cap", "mushroom cap (crimson)", "oversized leather hat",
            "peter griffin", "pixelar_tung_sahur", "pumpkin hat", "simplewings", "steorra",
            "tsumiki miniwa", "turtle pet", "vampire", "wynn-extra-bends-1.3b"
    );
    private static CompletableFuture<List<AvatarEntry>> installTask;

    private FiguraAvatarLibrary() {
    }

    public static synchronized CompletableFuture<List<AvatarEntry>> ensureInstalled() {
        if (installTask == null) {
            installTask = CompletableFuture.supplyAsync(() -> {
                try {
                    removeLegacyGeneratedPresets();
                    Path collection = LocalAvatarFetcher.getLocalAvatarDirectory().resolve(COLLECTION).normalize();
                    Files.createDirectories(collection);
                    Path marker = collection.resolve(VERSION_FILE);
                    String installedVersion = Files.isRegularFile(marker) ? Files.readString(marker).trim() : "";
                    if (!LIBRARY_VERSION.equals(installedVersion)) {
                        Files.createDirectories(collection);
                        installResources(collection);
                        Files.writeString(marker, LIBRARY_VERSION);
                    }
                    return scan(LocalAvatarFetcher.getLocalAvatarDirectory().normalize());
                } catch (Exception exception) {
                    throw new RuntimeException("Не удалось установить библиотеку Figura", exception);
                }
            });
        }
        return installTask;
    }

    public static Path collectionDirectory() {
        return LocalAvatarFetcher.getLocalAvatarDirectory().resolve(COLLECTION).normalize();
    }

    public static CompletableFuture<List<AvatarEntry>> refreshIndex() {
        return ensureInstalled().thenCompose(ignored -> CompletableFuture.supplyAsync(() -> {
            try {
                Path avatarsRoot = LocalAvatarFetcher.getLocalAvatarDirectory().normalize();
                return scan(avatarsRoot);
            } catch (IOException exception) {
                throw new RuntimeException("Не удалось обновить список аватаров Figura", exception);
            }
        }));
    }

    private static void removeLegacyGeneratedPresets() throws IOException {
        Path avatars = LocalAvatarFetcher.getLocalAvatarDirectory().normalize();
        Path legacyCollection = avatars.resolve("Wyvern Collection").normalize();
        if (!legacyCollection.startsWith(avatars) || !Files.isDirectory(legacyCollection)) return;
        for (String preset : LEGACY_PRESETS) {
            Path target = legacyCollection.resolve(preset).normalize();
            if (!target.startsWith(legacyCollection) || !Files.exists(target)) continue;
            try (var paths = Files.walk(target)) {
                for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                    Files.deleteIfExists(path);
                }
            }
        }
        try (var remaining = Files.list(legacyCollection)) {
            if (remaining.findAny().isEmpty()) Files.deleteIfExists(legacyCollection);
        }
    }

    private static void installResources(Path target) throws Exception {
        InputStream resource = FiguraAvatarLibrary.class.getClassLoader().getResourceAsStream(ARCHIVE_RESOURCE);
        if (resource == null) throw new IOException("Архив моделей Figura не найден");
        try (ZipInputStream zip = new ZipInputStream(new BufferedInputStream(resource))) {
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.isDirectory() || entry.getName().isBlank() || entry.getName().contains("\\")) continue;
                Path relative = Path.of(entry.getName()).normalize();
                Path output = safeResolve(target, relative);
                Files.createDirectories(output.getParent());
                if (Files.exists(output) && entry.getSize() >= 0 && Files.size(output) == entry.getSize()) continue;
                Files.copy(zip, output, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private static void removeObsoleteBundledAvatars(Path collection) throws IOException {
        Path avatars = LocalAvatarFetcher.getLocalAvatarDirectory().normalize();
        if (!collection.startsWith(avatars) || collection.equals(avatars) || !Files.exists(collection)) return;
        try (var children = Files.list(collection)) {
            for (Path folder : children.filter(Files::isDirectory).toList()) {
                if (!OBSOLETE_BUNDLED_AVATARS.contains(folder.getFileName().toString().toLowerCase(Locale.ROOT))) continue;
                try (var paths = Files.walk(folder)) {
                    for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) Files.deleteIfExists(path);
                }
            }
        }
    }

    private static Path safeResolve(Path root, Path relative) throws IOException {
        Path output = root.resolve(relative).normalize();
        if (relative.isAbsolute() || !output.startsWith(root)) {
            throw new IOException("Небезопасный путь в библиотеке Figura: " + relative);
        }
        return output;
    }

    private static List<AvatarEntry> scan(Path avatarsRoot) throws IOException {
        List<AvatarEntry> result = new ArrayList<>();
        try (var files = Files.walk(avatarsRoot)) {
            files.filter(path -> path.getFileName().toString().equalsIgnoreCase("avatar.json"))
                    .map(Path::getParent)
                    .distinct()
                    .forEach(folder -> {
                String folderName = folder.getFileName().toString();
                String name = displayName(folderName);
                Path thumbnail = Files.isRegularFile(folder.resolve("avatar.png")) ? folder.resolve("avatar.png") : null;
                String key = avatarsRoot.relativize(folder).toString().replace('\\', '/');
                result.add(new AvatarEntry(key, name, Section.AVATARS, folder, thumbnail));
            });
        }
        // Keep freshly copied/edited avatars at the top of the catalog.  The
        // GUI preserves this order, so a manual refresh immediately surfaces
        // the newest model instead of burying it in alphabetical order.
        result.sort(Comparator
                .comparingLong((AvatarEntry entry) -> modifiedTime(entry.path()))
                .reversed()
                .thenComparing(AvatarEntry::name, String.CASE_INSENSITIVE_ORDER));
        return List.copyOf(result);
    }

    private static long modifiedTime(Path path) {
        try {
            return Files.getLastModifiedTime(path).toMillis();
        } catch (IOException ignored) {
            return 0L;
        }
    }

    private static String displayName(String folder) {
        if (folder.equalsIgnoreCase("Hornet - Full Pack")) return "Hornet";
        String clean = folder.replaceFirst("^\\d+[a-zA-Z]?_", "").replace('_', ' ').trim();
        return clean.isBlank() ? folder : clean;
    }

    public enum Section {
        AVATARS("Аватар");

        private final String title;

        Section(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }
    }

    public record AvatarEntry(String key, String name, Section section, Path path, Path thumbnail) {
    }
}

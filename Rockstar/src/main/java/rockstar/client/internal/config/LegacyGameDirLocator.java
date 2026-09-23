package rockstar.client.internal.config;


import rockstar.client.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import rockstar.client.RockstarClient;

public final class LegacyGameDirLocator {
    private LegacyGameDirLocator() {
    }

    public static Optional<Path> internalMethod02256() {
        Optional<Path> optional = LegacyGameDirLocator.internalMethod04501();
        if (optional.isPresent()) {
            return optional;
        }
        if (LegacyGameDirLocator.internalMethod03749()) {
            return LegacyGameDirLocator.internalMethod08284().filter(LegacyGameDirLocator::internalMethod03222);
        }
        return Optional.empty();
    }

    private static Optional<Path> internalMethod04501() {
        Optional<Path> optional = LegacyGameDirLocator.internalMethod00812(LegacyGameDirLocator.internalMethod00394());
        if (optional.isPresent()) {
            return optional;
        }
        return LegacyGameDirLocator.internalMethod07874().flatMap(LegacyGameDirLocator::internalMethod02168).filter(path -> Files.isDirectory(path, new LinkOption[0]));
    }

    private static Optional<Path> internalMethod00812(Set<Path> set) {
        return set.stream().map(LegacyGameDirLocator::internalMethod02168).flatMap(Optional::stream).filter(path -> Files.isDirectory(path, new LinkOption[0])).max(Comparator.comparingInt(LegacyGameDirLocator::internalMethod03221));
    }

    private static Set<Path> internalMethod00394() {
        LinkedHashSet<Path> linkedHashSet = new LinkedHashSet<Path>();
        for (Path path : LegacyGameDirLocator.internalMethod00916()) {
            linkedHashSet.add(path.resolve("legacy.properties"));
        }
        return linkedHashSet;
    }

    private static Optional<Path> internalMethod07874() {
        final AtomicReference atomicReference = new AtomicReference();
        for (final Path path : LegacyGameDirLocator.internalMethod06675()) {
            if (!Files.isDirectory(path, new LinkOption[0])) continue;
            try {
                Files.walkFileTree(path, Set.of(), 6, (FileVisitor<? super Path>)new SimpleFileVisitor<Path>(){

                    public FileVisitResult internalMethod05681(Path path2, BasicFileAttributes basicFileAttributes) {
                        if (!path.equals(path2) && LegacyGameDirLocator.internalMethod07343(path2)) {
                            return FileVisitResult.SKIP_SUBTREE;
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    public FileVisitResult internalMethod04373(Path path2, BasicFileAttributes basicFileAttributes) {
                        String string = path2.getFileName().toString();
                        if (string.equalsIgnoreCase("legacy.properties") && LegacyGameDirLocator.internalMethod02168(path2).filter(path -> Files.isDirectory(path, new LinkOption[0])).isPresent()) {
                            atomicReference.set(path2);
                            return FileVisitResult.TERMINATE;
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    public FileVisitResult internalMethod00025(Path path2, IOException iOException) {
                        return FileVisitResult.CONTINUE;
                    }

                });
            }
            catch (IOException iOException) {
                // empty catch block
            }
            if (atomicReference.get() == null) continue;
            return Optional.of((Path)atomicReference.get());
        }
        return Optional.empty();
    }

    public static Optional<Path> internalMethod02168(Path path) {
        Object object;
        if (!Files.isRegularFile(path, new LinkOption[0])) {
            return Optional.empty();
        }
        Properties properties = new Properties();
        try {
            object = Files.newInputStream(path, new OpenOption[0]);
            try {
                properties.load((InputStream)object);
            }
            finally {
                if (object != null) {
                    ((InputStream)object).close();
                }
            }
        }
        catch (IOException iOException) {
            RockstarClient.internalField0572.warn("Failed to read Legacy Launcher properties: {}", (Object)path, (Object)iOException);
            return Optional.empty();
        }
        object = properties.getProperty("minecraft.gamedir");
        if (object == null || ((String)object).isBlank()) {
            return Optional.empty();
        }
        try {
            Path path2 = path.toAbsolutePath().getParent();
            Path path3 = LegacyGameDirLocator.internalMethod06624(((String)object).trim(), path2);
            return Optional.of(path3.toAbsolutePath().normalize());
        }
        catch (RuntimeException runtimeException) {
            RockstarClient.internalField0572.warn("Invalid Legacy Launcher minecraft.gamedir in {}", (Object)path, (Object)runtimeException);
            return Optional.empty();
        }
    }

    private static List<Path> internalMethod00916() {
        ArrayList<Path> arrayList = new ArrayList<Path>();
        Optional<Path> optional = LegacyGameDirLocator.internalMethod08626();
        LegacyGameDirLocator.internalMethod01761("APPDATA").ifPresent(path -> {
            arrayList.add(path.resolve("tlauncher"));
            arrayList.add(path.resolve(".tlauncher"));
            arrayList.add(path.resolve("Legacy Launcher"));
            arrayList.add(path.resolve("legacylauncher"));
        });
        LegacyGameDirLocator.internalMethod01761("LOCALAPPDATA").ifPresent(path -> {
            arrayList.add(path.resolve("tlauncher"));
            arrayList.add(path.resolve(".tlauncher"));
            arrayList.add(path.resolve("Legacy Launcher"));
            arrayList.add(path.resolve("legacylauncher"));
        });
        LegacyGameDirLocator.internalMethod01761("XDG_DATA_HOME").ifPresent(path -> arrayList.add(path.resolve("tlauncher")));
        LegacyGameDirLocator.internalMethod01761("XDG_CONFIG_HOME").ifPresent(path -> arrayList.add(path.resolve("tlauncher")));
        optional.ifPresent(path -> {
            arrayList.add(path.resolve(".tlauncher"));
            arrayList.add(path.resolve("tlauncher"));
            arrayList.add(path.resolve("Legacy Launcher"));
            arrayList.add(path.resolve(".legacylauncher"));
            arrayList.add(path.resolve(".local").resolve("share").resolve("tlauncher"));
            arrayList.add(path.resolve(".config").resolve("tlauncher"));
            arrayList.add(path.resolve("Library").resolve("Application Support").resolve("tlauncher"));
            arrayList.add(path.resolve("AppData").resolve("Roaming").resolve("tlauncher"));
            arrayList.add(path.resolve("AppData").resolve("Roaming").resolve(".tlauncher"));
            arrayList.add(path.resolve("AppData").resolve("Local").resolve("tlauncher"));
            arrayList.add(path.resolve("AppData").resolve("Local").resolve(".tlauncher"));
        });
        return arrayList;
    }

    private static List<Path> internalMethod06675() {
        ArrayList<Path> arrayList = new ArrayList<Path>();
        LegacyGameDirLocator.internalMethod01761("APPDATA").ifPresent(arrayList::add);
        LegacyGameDirLocator.internalMethod01761("LOCALAPPDATA").ifPresent(arrayList::add);
        LegacyGameDirLocator.internalMethod01761("XDG_DATA_HOME").ifPresent(arrayList::add);
        LegacyGameDirLocator.internalMethod01761("XDG_CONFIG_HOME").ifPresent(arrayList::add);
        LegacyGameDirLocator.internalMethod08626().ifPresent(path -> {
            arrayList.add(path.resolve("Library").resolve("Application Support"));
            arrayList.add(path.resolve("AppData").resolve("Roaming"));
            arrayList.add(path.resolve("AppData").resolve("Local"));
            arrayList.add(path.resolve(".local").resolve("share"));
            arrayList.add(path.resolve(".config"));
        });
        return arrayList;
    }

    private static boolean internalMethod03749() {
        for (Path path : LegacyGameDirLocator.internalMethod00916()) {
            if (!Files.isRegularFile(path.resolve("legacy.properties"), new LinkOption[0]) && !Files.isRegularFile(path.resolve("bin").resolve("legacy.jar"), new LinkOption[0])) continue;
            return true;
        }
        return false;
    }

    private static Optional<Path> internalMethod08284() {
        Optional<Path> optional = LegacyGameDirLocator.internalMethod08626();
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        String string = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (string.contains("win")) {
            Optional<Path> optional2 = LegacyGameDirLocator.internalMethod01761("APPDATA");
            return Optional.of(optional2.orElse(optional.get().resolve("AppData").resolve("Roaming")).resolve(".minecraft"));
        }
        if (string.contains("mac")) {
            return Optional.of(optional.get().resolve("Library").resolve("Application Support").resolve("minecraft"));
        }
        return Optional.of(optional.get().resolve(".minecraft"));
    }

    private static boolean internalMethod03222(Path path) {
        return Files.isDirectory(path, new LinkOption[0]) && LegacyGameDirLocator.internalMethod03221(path) > 0;
    }

    private static int internalMethod03221(Path path) {
        String string2;
        int n = 0;
        for (String directoryName : (Iterable<String>)(Iterable<?>)List.of("versions", "resourcepacks", "mods", "saves", "assets", "libraries")) {
            if (!Files.isDirectory(path.resolve(directoryName), new LinkOption[0])) continue;
            n += 2;
        }
        for (String profileFileName : (Iterable<String>)(Iterable<?>)List.of("options.txt", "launcher_profiles.json", "tlauncher_profiles.json")) {
            if (!Files.isRegularFile(path.resolve(profileFileName), new LinkOption[0])) continue;
            n += 3;
        }
        Path path2 = path.getFileName();
        if (path2 != null && ((string2 = path2.toString().toLowerCase(Locale.ROOT)).equals("minecraft") || string2.equals(".minecraft"))) {
            ++n;
        }
        return n;
    }

    public static boolean internalMethod07343(Path path) {
        Path path2 = path.getFileName();
        if (path2 == null) {
            return false;
        }
        String string = path2.toString().toLowerCase(Locale.ROOT);
        return string.equals(".gradle") || string.equals(".m2") || string.equals(".git") || string.equals("node_modules") || string.equals("caches") || string.equals("cache") || string.equals("logs") || string.equals("build") || string.equals("target");
    }

    private static Path internalMethod06624(String string, Path path) {
        String string2 = LegacyGameDirLocator.internalMethod03920(LegacyGameDirLocator.internalMethod02851(string));
        Path path2 = Path.of(string2, new String[0]);
        if (path2.isAbsolute()) {
            return path2;
        }
        return path.resolve(path2);
    }

    private static String internalMethod02851(String string) {
        if (string.equals("~")) {
            return LegacyGameDirLocator.internalMethod08626().map(Path::toString).orElse(string);
        }
        if (string.startsWith("~/") || string.startsWith("~\\")) {
            return LegacyGameDirLocator.internalMethod08626().map(path -> path.resolve(string.substring(2)).toString()).orElse(string);
        }
        return string;
    }

    private static String internalMethod03920(String string) {
        String string2 = string;
        for (String string3 : System.getenv().keySet()) {
            String string4 = System.getenv(string3);
            if (string4 == null || string4.isBlank()) continue;
            string2 = string2.replace("%" + string3 + "%", string4);
            string2 = string2.replace("${" + string3 + "}", string4);
        }
        return string2;
    }

    private static Optional<Path> internalMethod08626() {
        String string = System.getProperty("user.home");
        if (string == null || string.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(Path.of(string, new String[0]));
    }

    private static Optional<Path> internalMethod01761(String string) {
        String string2 = System.getenv(string);
        if (string2 == null || string2.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(Path.of(string2, new String[0]));
    }
}


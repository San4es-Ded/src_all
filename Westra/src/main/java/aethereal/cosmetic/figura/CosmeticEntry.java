package aethereal.cosmetic.figura;

import java.nio.file.Path;

public record CosmeticEntry(String id, String name, CosmeticCategory category, Path directory, Path preview) {
}

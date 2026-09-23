package rockstar.client.internal.core;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.util.Identifier;
import net.minecraft.util.AssetInfo;

public final class SkinOverrideStore {
    public static final String internalField0248 = "*";
    private static final Map<String, InternalType0180> internalField0543 = new ConcurrentHashMap<String, InternalType0180>();

    private SkinOverrideStore() {
    }

    public static InternalType0180 internalMethod00141(Object object, String string2) {
        return internalField0543.compute(SkinOverrideStore.internalMethod02399(string2), (string, nestedValue0076) -> nestedValue0076 != null && nestedValue0076.internalField0290 == object ? nestedValue0076 : new InternalType0180(object));
    }

    public static InternalType0180 internalMethod05128(String string) {
        if (internalField0543.isEmpty() || string == null || string.isEmpty()) {
            return null;
        }
        InternalType0180 nestedValue0076 = internalField0543.get(SkinOverrideStore.internalMethod02399(string));
        return nestedValue0076 != null ? nestedValue0076 : internalField0543.get(internalField0248);
    }

    public static void internalMethod05341(String string2) {
        internalField0543.computeIfPresent(SkinOverrideStore.internalMethod02399(string2), (string, nestedValue0076) -> nestedValue0076.internalMethod04688() ? null : nestedValue0076);
    }

    public static void internalMethod03776(String string) {
        internalField0543.remove(SkinOverrideStore.internalMethod02399(string));
    }

    public static void internalMethod02767(Object object) {
        internalField0543.values().removeIf(nestedValue0076 -> nestedValue0076.internalField0290 == object);
    }

    public static void internalMethod00188() {
        internalField0543.clear();
    }

    public static boolean internalMethod00189() {
        return internalField0543.isEmpty();
    }

    public static List<String> internalMethod02624() {
        return new ArrayList<String>(internalField0543.keySet());
    }

    public static SkinTextures internalMethod04550(String string, SkinTextures skinTextures) {
        if (skinTextures == null) {
            return null;
        }
        InternalType0180 nestedValue0076 = SkinOverrideStore.internalMethod05128(string);
        if (nestedValue0076 == null || nestedValue0076.internalMethod04688()) {
            return skinTextures;
        }
        AssetInfo.TextureAsset body = nestedValue0076.internalField0354 != null
            ? new AssetInfo.TextureAssetInfo(nestedValue0076.internalField0354, nestedValue0076.internalField0354)
            : skinTextures.body();
        AssetInfo.TextureAsset cape = nestedValue0076.internalField0277
            ? null
            : nestedValue0076.internalField0355 != null
                ? new AssetInfo.TextureAssetInfo(nestedValue0076.internalField0355, nestedValue0076.internalField0355)
                : skinTextures.cape();
        AssetInfo.TextureAsset elytra = nestedValue0076.internalField1133 != null
            ? new AssetInfo.TextureAssetInfo(nestedValue0076.internalField1133, nestedValue0076.internalField1133)
            : skinTextures.elytra();
        return new SkinTextures(
            body,
            cape,
            elytra,
            nestedValue0076.internalField0236 != null ? nestedValue0076.internalField0236 : skinTextures.model(),
            skinTextures.secure()
        );
    }

    public static boolean internalMethod05342(String string) {
        InternalType0180 nestedValue0076 = SkinOverrideStore.internalMethod05128(string);
        return nestedValue0076 != null && nestedValue0076.internalField0355 != null;
    }

    private static String internalMethod02399(String string) {
        if (string == null) {
            return internalField0248;
        }
        String string2 = string.trim();
        return string2.isEmpty() ? internalField0248 : string2.toLowerCase(Locale.ROOT);
    }

    public static final class InternalType0180 {
        final Object internalField0290;
        volatile Identifier internalField0354;
        volatile PlayerSkinType internalField0236;
        volatile Identifier internalField0355;
        volatile Identifier internalField1133;
        volatile boolean internalField0277;

        InternalType0180(Object object) {
            this.internalField0290 = object;
        }

        public void internalMethod06120(Identifier identifier) {
            this.internalField0354 = identifier;
        }

        public void internalMethod03414(PlayerSkinType model) {
            this.internalField0236 = model;
        }

        public void internalMethod05017(Identifier identifier) {
            this.internalField0355 = identifier;
            if (identifier != null) {
                this.internalField0277 = false;
            }
        }

        public void internalMethod08337(Identifier identifier) {
            this.internalField1133 = identifier;
        }

        public void internalMethod03225(boolean bl) {
            this.internalField0277 = bl;
            if (bl) {
                this.internalField0355 = null;
            }
        }

        public Identifier internalMethod03751() {
            return this.internalField0354;
        }

        public Identifier internalMethod00837() {
            return this.internalField0355;
        }

        public Identifier internalMethod08803() {
            return this.internalField1133;
        }

        public PlayerSkinType internalMethod05326() {
            return this.internalField0236;
        }

        public boolean internalMethod04687() {
            return this.internalField0277;
        }

        public boolean internalMethod04688() {
            return this.internalField0354 == null && this.internalField0236 == null && this.internalField0355 == null && this.internalField1133 == null && !this.internalField0277;
        }
    }
}

package rockstar.client.internal.rotation;



import rockstar.client.rotation.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public final class PlayerMotionPredictor {
    private static final int internalField0227 = 15;
    private static final double internalField0194 = -0.08;
    private static final long internalField0229 = 30000L;
    private static final Map<UUID, List<InternalType0027>> internalField0543 = new ConcurrentHashMap<UUID, List<InternalType0027>>();
    private static final Map<UUID, InternalType0028> internalField0544 = new ConcurrentHashMap<UUID, InternalType0028>();
    private static final Map<UUID, Integer> internalField1197 = new ConcurrentHashMap<UUID, Integer>();
    private static long internalField0230 = System.currentTimeMillis();

    public static Vec3d internalMethod03269(PlayerEntity playerEntity) {
        if (playerEntity == null) {
            return Vec3d.ZERO;
        }
        PlayerMotionPredictor.internalMethod00245(playerEntity);
        if (!PlayerMotionPredictor.internalMethod00246(playerEntity)) {
            return playerEntity.getEntityPos();
        }
        int n = PlayerMotionPredictor.internalMethod00244(playerEntity);
        return PlayerMotionPredictor.internalMethod01371(playerEntity, n);
    }

    public static void internalMethod00245(PlayerEntity playerEntity) {
        if (playerEntity == null) {
            return;
        }
        UUID uUID2 = playerEntity.getUuid();
        long l = System.currentTimeMillis();
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.player == null) {
            return;
        }
        double d = playerEntity.distanceTo((Entity)minecraftClient.player);
        InternalType0027 nestedValue2003 = new InternalType0027(playerEntity.getEntityPos(), playerEntity.getVelocity(), playerEntity.getPitch(), playerEntity.getYaw(), playerEntity.isGliding(), l, d);
        List list = internalField0543.computeIfAbsent(uUID2, uUID -> new ArrayList());
        list.add(nestedValue2003);
        if (list.size() > 15) {
            list.removeFirst();
        }
        PlayerMotionPredictor.internalMethod00658(uUID2, nestedValue2003);
        PlayerMotionPredictor.internalMethod01438(l);
    }

    public static boolean internalMethod00246(PlayerEntity playerEntity) {
        if (!playerEntity.isGliding()) {
            return false;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.player == null) {
            return false;
        }
        UUID uUID = playerEntity.getUuid();
        List<InternalType0027> list = internalField0543.get(uUID);
        if (list == null || list.size() < 3) {
            return false;
        }
        boolean bl = PlayerMotionPredictor.internalMethod00206(list);
        boolean bl2 = PlayerMotionPredictor.internalMethod06908(playerEntity, (PlayerEntity)minecraftClient.player);
        boolean bl3 = PlayerMotionPredictor.internalMethod01105(playerEntity);
        int n = 0;
        if (bl) {
            ++n;
        }
        if (bl2) {
            ++n;
        }
        if (bl3) {
            ++n;
        }
        return n >= 2;
    }

    private static boolean internalMethod00206(List<InternalType0027> list) {
        if (list.size() < 3) {
            return false;
        }
        int n = Math.min(5, list.size());
        List<InternalType0027> list2 = list.subList(list.size() - n, list.size());
        int n2 = 0;
        for (int i = 1; i < list2.size(); ++i) {
            if (!(list2.get((int)i).internalField0194 > list2.get((int)(i - 1)).internalField0194)) continue;
            ++n2;
        }
        return n2 >= (list2.size() - 1) / 2;
    }

    private static boolean internalMethod06908(PlayerEntity playerEntity, PlayerEntity playerEntity2) {
        Vec3d vec3d;
        Vec3d vec3d2 = playerEntity.getEntityPos();
        Vec3d vec3d3 = playerEntity2.getEntityPos();
        Vec3d vec3d4 = playerEntity.getVelocity();
        Vec3d vec3d5 = vec3d2.subtract(vec3d3).normalize();
        double d = vec3d5.dotProduct(vec3d = vec3d4.normalize());
        return d > 0.3;
    }

    private static boolean internalMethod01105(PlayerEntity playerEntity) {
        double d = playerEntity.getVelocity().length();
        return d > 0.8;
    }

    private static Vec3d internalMethod01371(PlayerEntity playerEntity, int n) {
        Vec3d vec3d = playerEntity.getEntityPos();
        Vec3d vec3d2 = playerEntity.getVelocity();
        float f = playerEntity.getPitch();
        float f2 = playerEntity.getYaw();
        boolean bl = playerEntity.isGliding();
        for (int i = 0; i < n; ++i) {
            if (bl) {
                vec3d = PlayerMotionPredictor.internalMethod04166(vec3d, vec3d2, f, f2);
                vec3d2 = PlayerMotionPredictor.internalMethod05590(vec3d2, f, f2);
                continue;
            }
            vec3d2 = vec3d2.add(0.0, -0.08, 0.0).multiply(0.98);
            vec3d = vec3d.add(vec3d2);
        }
        return vec3d;
    }

    private static Vec3d internalMethod04166(Vec3d vec3d, Vec3d vec3d2, float f, float f2) {
        return vec3d.add(vec3d2);
    }

    private static Vec3d internalMethod05590(Vec3d vec3d, float f, float f2) {
        double d;
        double d2 = vec3d.x;
        double d3 = vec3d.y;
        double d4 = vec3d.z;
        float f3 = (float)Math.toRadians(f);
        float f4 = (float)Math.toRadians(f2);
        Vec3d vec3d2 = new Vec3d(-Math.sin(f4) * Math.cos(f3), -Math.sin(f3), Math.cos(f4) * Math.cos(f3));
        double d5 = Math.sqrt(d2 * d2 + d4 * d4);
        double d6 = Math.sqrt(vec3d2.x * vec3d2.x + vec3d2.z * vec3d2.z);
        float f5 = (float)Math.cos(f3);
        float f6 = f5 * f5;
        if ((d3 += -0.08 + (double)f6 * 0.06) < 0.0 && d6 > 0.0) {
            d = d3 * -0.1 * (double)f6;
            d3 += d;
            d2 += vec3d2.x * d / d6;
            d4 += vec3d2.z * d / d6;
        }
        if (f < 0.0f && d6 > 0.0) {
            d = d5 * -Math.sin(f3) * 0.04;
            d3 += d * 3.2;
            d2 -= vec3d2.x * d / d6;
            d4 -= vec3d2.z * d / d6;
        }
        if (d6 > 0.0) {
            d2 += (vec3d2.x / d6 * d5 - d2) * 0.1;
            d4 += (vec3d2.z / d6 * d5 - d4) * 0.1;
        }
        return new Vec3d(d2 *= 0.99, d3 *= 0.98, d4 *= 0.99);
    }

    private static int internalMethod00244(PlayerEntity playerEntity) {
        UUID uUID = playerEntity.getUuid();
        if (internalField1197.containsKey(uUID)) {
            return internalField1197.get(uUID);
        }
        int n = PlayerMotionPredictor.internalMethod01104(playerEntity);
        List<InternalType0027> list = internalField0543.get(uUID);
        if (list == null || list.size() < 3) {
            return n;
        }
        double d = PlayerMotionPredictor.internalMethod00205(list);
        double d2 = PlayerMotionPredictor.internalMethod04509(list);
        if (playerEntity.isGliding()) {
            double d3 = playerEntity.getVelocity().length();
            if (d3 > 2.0) {
                n += Math.min(4, (int)(d3 * 1.2));
            }
            if (d2 > 30.0) {
                n += 2;
            }
        }
        return Math.max(1, Math.min(15, n));
    }

    private static int internalMethod01104(PlayerEntity playerEntity) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        int n = 100;
        if (minecraftClient.getNetworkHandler() != null) {
            try {
                PlayerListEntry playerListEntry = minecraftClient.getNetworkHandler().getPlayerListEntry(playerEntity.getUuid());
                if (playerListEntry != null) {
                    n = playerListEntry.getLatency();
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        int n2 = Math.max(1, n / 50);
        int n3 = 2;
        return n2 + n3;
    }

    private static double internalMethod00205(List<InternalType0027> list) {
        if (list.size() < 2) {
            return 0.0;
        }
        double[] dArray = list.stream().mapToDouble(nestedValue2003 -> nestedValue2003.internalField0282.length()).toArray();
        double d = Arrays.stream(dArray).average().orElse(0.0);
        double d3 = Arrays.stream(dArray).map(d2 -> Math.pow(d2 - d, 2.0)).average().orElse(0.0);
        return Math.sqrt(d3);
    }

    private static double internalMethod04509(List<InternalType0027> list) {
        if (list.size() < 2) {
            return 0.0;
        }
        double d = 0.0;
        for (int i = 1; i < list.size(); ++i) {
            InternalType0027 nestedValue2003 = list.get(i - 1);
            InternalType0027 nestedValue2004 = list.get(i);
            double d2 = Math.abs(nestedValue2004.internalField0206 - nestedValue2003.internalField0206);
            double d3 = Math.abs(nestedValue2004.internalField0205 - nestedValue2003.internalField0205);
            if (d2 > 180.0) {
                d2 = 360.0 - d2;
            }
            d += Math.sqrt(d2 * d2 + d3 * d3);
        }
        return d / (double)(list.size() - 1);
    }

    private static void internalMethod00658(UUID uUID2, InternalType0027 nestedValue2003) {
        InternalType0028 nestedValue2005 = internalField0544.computeIfAbsent(uUID2, uUID -> new InternalType0028());
        nestedValue2005.internalMethod03014(nestedValue2003);
    }

    private static void internalMethod01438(long l) {
        if (l - internalField0230 < 30000L) {
            return;
        }
        internalField0230 = l;
        internalField0543.entrySet().removeIf(entry -> {
            List<InternalType0027> list = (List<InternalType0027>)entry.getValue();
            list.removeIf(nestedValue2003 -> l - nestedValue2003.internalField0229 > 30000L);
            return list.isEmpty();
        });
        internalField0544.entrySet().removeIf(entry -> l - ((InternalType0028)entry.getValue()).internalField0229 > 30000L);
    }

    @Generated
    private PlayerMotionPredictor() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static class InternalType0027 {
        public final Vec3d internalField0283;
        public final Vec3d internalField0282;
        public final float internalField0205;
        public final float internalField0206;
        public final boolean internalField0277;
        public final long internalField0229;
        public final double internalField0194;

        public InternalType0027(Vec3d vec3d, Vec3d vec3d2, float f, float f2, boolean bl, long l, double d) {
            this.internalField0283 = vec3d;
            this.internalField0282 = vec3d2;
            this.internalField0205 = f;
            this.internalField0206 = f2;
            this.internalField0277 = bl;
            this.internalField0229 = l;
            this.internalField0194 = d;
        }
    }

    static class InternalType0028 {
        private double internalField0194 = 0.0;
        private int internalField0227 = 0;
        long internalField0229 = System.currentTimeMillis();

        InternalType0028() {
        }

        public void internalMethod03014(InternalType0027 nestedValue2003) {
            double d = nestedValue2003.internalField0282.length();
            this.internalField0194 = (this.internalField0194 * (double)this.internalField0227 + d) / (double)(this.internalField0227 + 1);
            ++this.internalField0227;
            this.internalField0229 = System.currentTimeMillis();
        }
    }
}

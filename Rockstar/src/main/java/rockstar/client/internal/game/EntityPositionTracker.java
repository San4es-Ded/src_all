package rockstar.client.internal.game;


import rockstar.client.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public final class EntityPositionTracker {
    private static final Map<Integer, Vec3d> internalField0543 = new ConcurrentHashMap<Integer, Vec3d>();
    private static final Set<Integer> internalField0546 = ConcurrentHashMap.newKeySet();

    public static Vec3d internalMethod01255(Entity entity) {
        Vec3d vec3d = internalField0543.get(entity.getId());
        return vec3d != null ? vec3d : entity.getEntityPos();
    }

    public static void internalMethod03410(Entity entity, Vec3d vec3d) {
        internalField0543.put(entity.getId(), vec3d);
    }

    public static boolean internalMethod00897(Entity entity) {
        return internalField0546.contains(entity.getId());
    }

    public static boolean internalMethod06807(Entity entity) {
        return internalField0546.add(entity.getId());
    }

    public static void internalMethod03945(int n) {
        internalField0543.remove(n);
    }

    public static void internalMethod07263() {
        internalField0543.clear();
        internalField0546.clear();
    }

    @Generated
    private EntityPositionTracker() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}


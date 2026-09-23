package haron.modules.visuals;

import net.minecraft.util.math.Vec3d;

record TrailPoint(Vec3d position, long createdAtMs) {
    TrailPoint(Vec3d position) {
        this(position, System.currentTimeMillis());
    }

    boolean isOlderThan(long lifetimeMs) {
        return System.currentTimeMillis() - this.createdAtMs > lifetimeMs;
    }
}

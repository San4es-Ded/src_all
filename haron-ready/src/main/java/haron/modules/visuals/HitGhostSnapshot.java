package haron.modules.visuals;

import net.minecraft.util.math.Vec3d;

record HitGhostSnapshot(Vec3d pos, Vec3d drift, boolean slim, boolean sneaking, boolean baby,
              float bodyYaw, float headYaw, float pitch, float limbSwing,
              float limbSwingAmount, float swingProgress, long spawnAt) {
}

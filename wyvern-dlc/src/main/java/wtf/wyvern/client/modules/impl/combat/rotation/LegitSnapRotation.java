package wtf.wyvern.client.modules.impl.combat.rotation;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.astroguard.J2C.FastNative;
import wtf.wyvern.client.modules.impl.combat.Aura;
import wtf.wyvern.utility.component.FreeLookComponent;
import wtf.wyvern.utility.component.RotationComponent;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.wyvern.utility.math.MathUtil;
import wtf.wyvern.utility.math.MultipointUtils;

@FastNative
public final class LegitSnapRotation extends RotationBase {

    public void update(LivingEntity target, boolean attack) {
        if (mc.player == null || target == null) return;

        float distance = Aura.INSTANCE.distance.getCurrent();
        Vec3d aimPoint = MultipointUtils.getNearestPoint(target, distance);
        float[] angles = getAnglesTo(aimPoint);
        float targetYaw = angles[0];
        float targetPitch = angles[1];
        float baseYaw = FreeLookComponent.getFreeYaw();
        float basePitch = FreeLookComponent.getFreePitch();

        if (attack) {
            float fovDeg = Aura.INSTANCE.legitSnapFov.getCurrent();
            boolean inFov = isTargetInFov(target, fovDeg);
            float angleDist = getAngleDistance(targetYaw, targetPitch);

            if (inFov && angleDist <= fovDeg) {
                baseYaw = angles[0];
                basePitch = angles[1];
            }
        }

        lastYaw = baseYaw;
        lastPitch = basePitch;
        Rotation rotation = new Rotation(baseYaw, basePitch);
        RotationComponent.update(
                rotation,
                MathUtil.random(150, 180),
                MathUtil.random(150, 180),
                MathUtil.random(150, 180),
                MathUtil.random(150, 180),
                0,
                1,
                false
        );
    }

    private static float[] getAnglesTo(Vec3d point) {
        Vec3d eyes = mc.player.getEyePos();
        Vec3d dir = point.subtract(eyes);
        double dist = dir.length();
        if (dist < 1e-4) dist = 1e-4;

        double dx = dir.x / dist;
        double dy = dir.y / dist;
        double dz = dir.z / dist;

        float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        float pitch = (float) (-Math.toDegrees(Math.atan2(dy, Math.hypot(dx, dz))));
        return new float[]{yaw, pitch};
    }

    private static boolean isTargetInFov(LivingEntity target, float halfFov) {
        Vec3d eyes = mc.player.getEyePos();
        Box box = target.getBoundingBox();

        double cx = (box.minX + box.maxX) / 2.0;
        double cz = (box.minZ + box.maxZ) / 2.0;
        double[] checkY = {box.minY, (box.minY + box.maxY) / 2.0, box.maxY};

        for (double y : checkY) {
            Vec3d toPoint = new Vec3d(cx, y, cz).subtract(eyes);
            double len = toPoint.length();
            if (len < 1e-4) return true;

            Vec3d dir = toPoint.multiply(1.0 / len);
            float pYaw = (float) Math.toDegrees(Math.atan2(-dir.x, dir.z));
            float pPitch = (float) (-Math.toDegrees(Math.atan2(dir.y, Math.hypot(dir.x, dir.z))));

            float dYaw = Math.abs(MathHelper.wrapDegrees(pYaw - mc.player.getYaw()));
            float dPitch = Math.abs(MathHelper.wrapDegrees(pPitch - mc.player.getPitch()));

            if (dYaw <= halfFov && dPitch <= halfFov) {
                return true;
            }
        }
        return false;
    }

    private static float getAngleDistance(float targetYaw, float targetPitch) {
        float yawDiff = Math.abs(MathHelper.wrapDegrees(targetYaw - mc.player.getYaw()));
        float pitchDiff = Math.abs(MathHelper.wrapDegrees(targetPitch - mc.player.getPitch()));
        return (float) Math.hypot(yawDiff, pitchDiff);
    }

    public void reset() {
        if (mc.player != null) {
            lastYaw = mc.player.getYaw();
            lastPitch = mc.player.getPitch();
        }
    }

    @Override
    public void update(Rotation targetAngle, boolean elytraVisual) {
        // LegitSnap uses entity + attack state from Aura.
    }

    private int getPing() {
        if (mc.getNetworkHandler() != null && mc.player != null) {
            PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (entry != null) return entry.getLatency();
        }
        return 50;
    }
}

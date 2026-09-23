package wtf.wyvern.client.modules.impl.combat;

import net.minecraft.util.math.MathHelper;
import wtf.wyvern.Wyvern;
import wtf.astroguard.J2C.FastNative;

@FastNative
public final class TpsSync {

    private TpsSync() {
    }

    public static float getCurrentTPS() {
        if (Wyvern.getInstance().getServerHandler() == null) {
            return 20.0f;
        }
        float tps = Wyvern.getInstance().getServerHandler().getTPS();
        return MathHelper.clamp(tps, 0.1f, 20.0f);
    }

    public static long getAdjustedCooldown(long baseCooldown, boolean enabled) {
        if (!enabled) {
            return baseCooldown;
        }

        float tps = getCurrentTPS();
        if (tps >= 20.0f) {
            return baseCooldown;
        }

        float multiplier = 20.0f / tps;
        float additionalFactor = 1.0f + (20.0f - tps) * 0.05f;
        long adjusted = (long) (baseCooldown * multiplier * additionalFactor);

        return Math.min(adjusted, 3000L);
    }

    public static boolean canAttack(boolean enabled, long lastAttackTime, long baseCooldown, long currentTime) {
        if (!enabled) {
            return currentTime >= lastAttackTime + baseCooldown;
        }

        long adjustedCooldown = getAdjustedCooldown(baseCooldown, enabled);
        return currentTime >= lastAttackTime + adjustedCooldown;
    }
}

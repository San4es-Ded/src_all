package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;

public class KnockbackSimulator {
    public static void internalMethod06340(double[] dArray, double d, double d2, float f, LivingEntity livingEntity, boolean bl, float f2, int n) {
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8 = KnockbackSimulator.internalMethod03431(livingEntity);
        double d9 = livingEntity.getVelocity().x;
        double d10 = livingEntity.getVelocity().y;
        double d11 = livingEntity.getVelocity().z;
        boolean bl2 = livingEntity.isOnGround();
        double d12 = d - livingEntity.getX();
        double d13 = Math.sqrt(d12 * d12 + (d7 = d2 - livingEntity.getZ()) * d7);
        if (d13 > 0.001) {
            d12 /= d13;
            d7 /= d13;
        } else {
            d6 = Math.toRadians(f);
            d12 = -Math.sin(d6);
            d7 = Math.cos(d6);
        }
        d6 = 0.4 * (1.0 - d8);
        d9 = d9 / 2.0 - d12 * d6;
        d10 = bl2 ? Math.min(0.4, d10 / 2.0 + d6) : d10;
        d11 = d11 / 2.0 - d7 * d6;
        float f3 = f2 + (bl ? 1.0f : 0.0f);
        if (f3 > 0.0f) {
            d5 = (double)f3 * 0.5 * (1.0 - d8);
            d4 = Math.toRadians(f);
            d3 = Math.sin(d4);
            double d14 = -Math.cos(d4);
            d9 = d9 / 2.0 - d3 * d5;
            d10 = bl2 ? Math.min(0.4, d10 / 2.0 + d5) : d10;
            d11 = d11 / 2.0 - d14 * d5;
        }
        d5 = 0.0;
        d4 = 0.0;
        d3 = 0.0;
        for (int i = 0; i < n; ++i) {
            d5 += d9;
            d4 += (d10 -= 0.08);
            d3 += d11;
            d9 *= 0.91;
            d10 *= 0.98;
            d11 *= 0.91;
            if (!(d4 < 0.0)) continue;
            d4 = 0.0;
            d10 = 0.0;
        }
        dArray[0] = d5;
        dArray[1] = d4;
        dArray[2] = d3;
    }

    public static float internalMethod03432(LivingEntity livingEntity) {
        try {
            return (float)livingEntity.getAttributeValue(EntityAttributes.ATTACK_KNOCKBACK);
        }
        catch (Exception exception) {
            return 0.0f;
        }
    }

    private static double internalMethod03431(LivingEntity livingEntity) {
        try {
            return Math.min(livingEntity.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE), 1.0);
        }
        catch (Exception exception) {
            return 0.0;
        }
    }
}


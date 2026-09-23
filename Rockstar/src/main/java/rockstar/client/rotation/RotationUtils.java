package rockstar.client.rotation;



import rockstar.client.util.*;
import rockstar.client.*;
import rockstar.client.internal.game.*;
import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import rockstar.client.RockstarClient;
import rockstar.client.util.CombatUtils;
import rockstar.client.internal.game.EntityPositionTracker;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.MathUtils;
import rockstar.client.rotation.Rotation;
import rockstar.modules.combat.AuraModule;
import rockstar.modules.movement.SpeedModule;

public final class RotationUtils
implements MinecraftClientAccess {
    public static Vec3d internalMethod01412(Entity entity) {
        Vec3d vec3d = RotationUtils.internalField0149.player.getEyePos();
        return new Vec3d(MathHelper.clamp((double)vec3d.x, (double)entity.getBoundingBox().minX, (double)entity.getBoundingBox().maxX), MathHelper.clamp((double)vec3d.y, (double)entity.getBoundingBox().minY, (double)entity.getBoundingBox().maxY), MathHelper.clamp((double)vec3d.z, (double)entity.getBoundingBox().minZ, (double)entity.getBoundingBox().maxZ));
    }

    public static Vec3d internalMethod01839(LivingEntity livingEntity, Vec3d vec3d) {
        return RotationUtils.internalMethod01412((Entity)livingEntity).subtract(livingEntity.getEntityPos()).add(vec3d);
    }

    public static Rotation internalMethod05580(Vec3d vec3d) {
        double d = vec3d.getX();
        double d2 = vec3d.getY();
        double d3 = vec3d.getZ();
        double d4 = d - RotationUtils.internalField0149.player.getX();
        double d5 = d2 - (RotationUtils.internalField0149.player.getY() + (double)RotationUtils.internalField0149.player.getEyeHeight(RotationUtils.internalField0149.player.getPose()));
        double d6 = d3 - RotationUtils.internalField0149.player.getZ();
        double d7 = Math.sqrt(d4 * d4 + d6 * d6);
        float f = (float)Math.toDegrees(Math.atan2(d6, d4)) - 90.0f;
        float f2 = (float)(-Math.toDegrees(Math.atan2(d5, d7)));
        return new Rotation(f, f2);
    }

    public static Rotation internalMethod03921(Vec3d vec3d, Vec3d vec3d2) {
        double d = vec3d2.x - vec3d.x;
        double d2 = vec3d2.y - vec3d.y;
        double d3 = vec3d2.z - vec3d.z;
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)Math.toDegrees(Math.atan2(d3, d)) - 90.0f;
        float f2 = (float)(-Math.toDegrees(Math.atan2(d2, d4)));
        return new Rotation(f, f2);
    }

    public static float internalMethod03158() {
        double d = (Double)RotationUtils.internalField0149.options.getMouseSensitivity().getValue();
        double d2 = d * (double)0.6f + (double)0.2f;
        return (float)(d2 * d2 * d2 * (double)1.2f);
    }

    @NotNull
    public static Rotation internalMethod02284(@NotNull Rotation typedValue266, @NotNull Rotation typedValue267) {
        float f = RotationUtils.internalMethod03158();
        float f2 = MathHelper.wrapDegrees((float)(typedValue267.internalMethod00169() - typedValue266.internalMethod00169()));
        float f3 = typedValue267.internalMethod00171() - typedValue266.internalMethod00171();
        f2 = (float)Math.round(f2 / f) * f;
        f3 = (float)Math.round(f3 / f) * f;
        return new Rotation(typedValue266.internalMethod00169() + f2, MathHelper.clamp((float)(typedValue266.internalMethod00171() + f3), (float)-90.0f, (float)90.0f));
    }

    public static float internalMethod02018(float f, float f2) {
        return RotationUtils.internalMethod02284(new Rotation(f, 0.0f), new Rotation(f2, 0.0f)).internalMethod00169();
    }

    public static float internalMethod03739(float f, float f2) {
        return RotationUtils.internalMethod02284(new Rotation(0.0f, f), new Rotation(0.0f, f2)).internalMethod00171();
    }

    public static int internalMethod02019(float f, float f2) {
        float f3 = RotationUtils.internalMethod03158();
        return Math.round(MathHelper.wrapDegrees((float)(f2 - f)) / f3);
    }

    public static int internalMethod03740(float f, float f2) {
        float f3 = RotationUtils.internalMethod03158();
        return Math.round((f2 - f) / f3);
    }

    public static float internalMethod08495(float f, float f2) {
        float f3;
        for (f3 = f2 - f; f3 > 180.0f; f3 -= 360.0f) {
        }
        while (f3 < -180.0f) {
            f3 += 360.0f;
        }
        return f3;
    }

    public static float internalMethod02575(float f, float f2, float f3) {
        float f4;
        float f5 = f % 360.0f;
        if (f5 < 0.0f) {
            f5 += 360.0f;
        }
        if ((f4 = f2 % 360.0f) < 0.0f) {
            f4 += 360.0f;
        }
        int n = (int)(f / 360.0f);
        if (f < 0.0f && f % 360.0f != 0.0f) {
            --n;
        }
        float f6 = f4 + (float)(n * 360);
        float f7 = f4 - f5;
        if (f7 < 0.0f) {
            f7 += 360.0f;
        }
        if (f7 <= f3) {
            return f + f7;
        }
        float f8 = f6 - f;
        if (f8 > 180.0f) {
            f6 -= 360.0f;
        } else if (f8 < -180.0f) {
            f6 += 360.0f;
        }
        return f6;
    }

    public static float internalMethod08850(float f, float f2) {
        float f3;
        float f4;
        float f5;
        float f6 = f % 360.0f;
        if (f6 < 0.0f) {
            f6 += 360.0f;
        }
        if ((f5 = f2 % 360.0f) < 0.0f) {
            f5 += 360.0f;
        }
        int n = (int)(f / 360.0f);
        if (f < 0.0f && f % 360.0f != 0.0f) {
            --n;
        }
        if ((f4 = (f3 = f5 + (float)(n * 360)) - f) > 180.0f) {
            f3 -= 360.0f;
        } else if (f4 < -180.0f) {
            f3 += 360.0f;
        }
        return f3;
    }

    public static Rotation internalMethod04766(LivingEntity livingEntity, AuraModule internalValue0004) {
        Vec3d vec3d = CombatUtils.internalMethod01237((Entity)livingEntity, internalValue0004.internalMethod06377().isSelected());
        Rotation typedValue266 = RotationUtils.internalMethod05580(RotationUtils.internalMethod01839(livingEntity, vec3d));
        if (RotationUtils.internalField0149.player.getEyePos().distanceTo(livingEntity.getEyePos()) < 3.0) {
            vec3d = CombatUtils.internalMethod01237((Entity)livingEntity, internalValue0004.internalMethod06377().isSelected()).add(0.0, (double)(livingEntity.getHeight() / 2.0f), 0.0);
            typedValue266 = RotationUtils.internalMethod05580(vec3d);
            if (RockstarClient.getInstance().getModuleManager().getModule(SpeedModule.class).isEnabled()) {
                Vec3d vec3d2 = EntityPositionTracker.internalMethod01255((Entity)livingEntity);
            }
        }
        if (typedValue266.internalMethod00171() == (float)((int)typedValue266.internalMethod00171())) {
            typedValue266.internalMethod03289(Math.clamp(typedValue266.internalMethod00171() + MathUtils.internalMethod07919(-1.0f, 1.0f), -90.0f, 90.0f));
        }
        if (typedValue266.internalMethod00169() == (float)((int)typedValue266.internalMethod00169())) {
            typedValue266.internalMethod03239(typedValue266.internalMethod00169() + MathUtils.internalMethod07919(-1.0f, 1.0f));
        }
        return typedValue266;
    }

    @Generated
    private RotationUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

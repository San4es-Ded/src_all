package haron.entity;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public final class EntityInterpolation {
    public static Vec3d interpolatedPosition(Entity entity, float tickDelta) {
        return b(entity, tickDelta);
    }
    public static int a;
    public static boolean b;

    private EntityInterpolation() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Vec3d b(Entity entity, float f) {
        return EntityInterpolation.a(entity, f);
    }

    public static Vec3d a(Entity entity, float f) {
        Vec3d vec3d = EntityInterpolation.a(entity);
        return vec3d.add(entity.getPos().subtract(vec3d).multiply((double)f));
    }

    public static Vec3d a(Entity entity) {
        return new Vec3d(entity.prevX, entity.prevY, entity.prevZ);
    }

    public static boolean a(LivingEntity livingEntity) {
        if (!(livingEntity instanceof PlayerEntity)) {
            return false;
        }
        PlayerEntity playerEntity = (PlayerEntity)livingEntity;
        if (!playerEntity.isInvisible()) {
            return false;
        }
        Iterator iterator = playerEntity.getArmorItems().iterator();
        while (iterator.hasNext()) {
            if (((ItemStack)iterator.next()).isEmpty()) continue;
            return false;
        }
        return true;
    }
}

package rockstar.client.internal.inventory;


import rockstar.client.*;
import java.util.Comparator;
import java.util.function.Function;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import rockstar.client.MinecraftClientAccess;

public class EntityComparatorUtils
implements MinecraftClientAccess {
    public static final Comparator<Entity> internalField0757 = Comparator.comparingDouble(entity -> entity.distanceTo((Entity)EntityComparatorUtils.internalField0149.player));
    public static final Comparator<Entity> internalField0758 = Comparator.comparingDouble(entity -> {
        double d;
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            d = livingEntity.getHealth();
        } else {
            d = 0.0;
        }
        return d;
    });
    public static final Comparator<Entity> internalField1302 = Comparator.comparingDouble(entity -> {
        if (EntityComparatorUtils.internalField0149.player == null) {
            return Double.MAX_VALUE;
        }
        Vec3d vec3d = EntityComparatorUtils.internalField0149.player.getEntityPos();
        Vec3d vec3d2 = entity.getEntityPos();
        Vec3d vec3d3 = EntityComparatorUtils.internalField0149.player.getRotationVec(1.0f);
        Vec3d vec3d4 = vec3d2.subtract(vec3d).normalize();
        double d = vec3d3.dotProduct(vec3d4);
        return Math.acos(MathHelper.clamp((double)d, (double)-1.0, (double)1.0)) * 57.29577951308232;
    });
    public static final Comparator<Entity> internalField1304 = Comparator.comparingDouble(entity -> {
        if (!(entity instanceof PlayerEntity)) {
            return Double.MAX_VALUE;
        }
        PlayerEntity playerEntity = (PlayerEntity)entity;
        double d = 0.0;
        for (ItemStack itemStack : rockstar.client.util.LegacyItemTypes.armorItems(playerEntity)) {
            if (itemStack == null || itemStack.isEmpty()) continue;
            d += (double)itemStack.getItem().getDefaultStack().getCount();
        }
        return d;
    });
    public static final Comparator<Entity> internalField1303 = internalField1304.reversed();

    public static Comparator<Entity> internalMethod04791(Function<Entity, Double> function) {
        return Comparator.comparingDouble(function::apply);
    }

    public static Comparator<Entity> internalMethod06849(Function<Entity, Double> function) {
        return Comparator.comparingDouble(function::apply).reversed();
    }
}


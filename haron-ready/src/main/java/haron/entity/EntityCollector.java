package haron.entity;

import haron.client.MinecraftClientAccess;
import haron.core.BooleanCoercion;
import haron.entity.EntityCategory;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;

public final class EntityCollector
implements MinecraftClientAccess {
    public static int a;
    public static boolean b;

    private EntityCollector() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> CopyOnWriteArrayList<T> a(Predicate<Entity> predicate, EntityCategory ... kmb8x3Array) {
        CopyOnWriteArrayList<T> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        if (EntityCollector.c.world == null) {
            return copyOnWriteArrayList;
        }
        for (Entity entity : EntityCollector.c.world.getEntities()) {
            if (!predicate.test(entity) || entity instanceof ClientPlayerEntity) continue;
            if (entity instanceof PlayerEntity && Arrays.stream(kmb8x3Array).anyMatch(kmb8x32 -> {
                return BooleanCoercion.from(kmb8x32 != EntityCategory.PLAYER ? 0 : 1);
            })) {
                copyOnWriteArrayList.add((T)entity);
            }
            if (entity instanceof VillagerEntity && Arrays.stream(kmb8x3Array).anyMatch(kmb8x32 -> {
                return BooleanCoercion.from(kmb8x32 != EntityCategory.VILLAGERS ? 0 : 1);
            })) {
                copyOnWriteArrayList.add((T)entity);
            }
            if (entity instanceof AnimalEntity && Arrays.stream(kmb8x3Array).anyMatch(kmb8x32 -> {
                return BooleanCoercion.from(kmb8x32 != EntityCategory.ANIMAL ? 0 : 1);
            })) {
                copyOnWriteArrayList.add((T)entity);
            }
            if (entity instanceof MobEntity && Arrays.stream(kmb8x3Array).anyMatch(kmb8x32 -> {
                return BooleanCoercion.from(kmb8x32 != EntityCategory.MOBS ? 0 : 1);
            })) {
                copyOnWriteArrayList.add((T)entity);
            }
            if (entity instanceof ItemEntity && Arrays.stream(kmb8x3Array).anyMatch(kmb8x32 -> {
                return BooleanCoercion.from(kmb8x32 != EntityCategory.ITEMS ? 0 : 1);
            })) {
                copyOnWriteArrayList.add((T)entity);
            }
            if (!Arrays.stream(kmb8x3Array).anyMatch(kmb8x32 -> {
                int n2 = kmb8x32 != EntityCategory.ENTITY ? 0 : 1;
                return BooleanCoercion.from(n2);
            })) continue;
            copyOnWriteArrayList.add((T)entity);
        }
        return copyOnWriteArrayList;
    }

    public static boolean a(Entity entity) {
        if (EntityCollector.c.world == null) {
            return false;
        }
        Iterator iterator = EntityCollector.c.world.getEntities().iterator();
        while (iterator.hasNext()) {
            if ((Entity)iterator.next() != entity) continue;
            return true;
        }
        return false;
    }
}

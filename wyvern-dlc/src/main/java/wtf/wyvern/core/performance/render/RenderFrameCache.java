package wtf.wyvern.core.performance.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Shared, allocation-free views of the entities used by render modules in one frame. */
public final class RenderFrameCache {
    private static final MinecraftClient MC = MinecraftClient.getInstance();
    private static final ArrayList<Entity> ENTITIES = new ArrayList<>(256);
    private static final ArrayList<PlayerEntity> PLAYERS = new ArrayList<>(32);
    private static final ArrayList<ItemEntity> ITEMS = new ArrayList<>(64);
    private static final ArrayList<FireworkRocketEntity> FIREWORKS = new ArrayList<>(16);

    private static final List<Entity> ENTITIES_VIEW = Collections.unmodifiableList(ENTITIES);
    private static final List<PlayerEntity> PLAYERS_VIEW = Collections.unmodifiableList(PLAYERS);
    private static final List<ItemEntity> ITEMS_VIEW = Collections.unmodifiableList(ITEMS);
    private static final List<FireworkRocketEntity> FIREWORKS_VIEW = Collections.unmodifiableList(FIREWORKS);

    private static ClientWorld cachedWorld;
    private static boolean ready;

    private RenderFrameCache() {
    }

    public static void beginFrame() {
        ClientWorld world = MC.world;
        if (cachedWorld != world) {
            clear();
            cachedWorld = world;
        }
        ready = false;
    }

    public static List<Entity> entities() {
        ensureReady();
        return ENTITIES_VIEW;
    }

    public static List<PlayerEntity> players() {
        ensureReady();
        return PLAYERS_VIEW;
    }

    public static List<ItemEntity> items() {
        ensureReady();
        return ITEMS_VIEW;
    }

    public static List<FireworkRocketEntity> fireworks() {
        ensureReady();
        return FIREWORKS_VIEW;
    }

    private static void ensureReady() {
        if (!ready || cachedWorld != MC.world) {
            rebuild(MC.world);
        }
    }

    private static void rebuild(ClientWorld world) {
        clear();
        cachedWorld = world;
        ready = true;

        if (world == null) {
            return;
        }

        for (Entity entity : world.getEntities()) {
            ENTITIES.add(entity);
            if (entity instanceof PlayerEntity player) {
                PLAYERS.add(player);
            }
            if (entity instanceof ItemEntity item) {
                ITEMS.add(item);
            }
            if (entity instanceof FireworkRocketEntity firework) {
                FIREWORKS.add(firework);
            }
        }
    }

    private static void clear() {
        ENTITIES.clear();
        PLAYERS.clear();
        ITEMS.clear();
        FIREWORKS.clear();
    }
}

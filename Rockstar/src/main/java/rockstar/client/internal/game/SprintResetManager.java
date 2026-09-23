package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public final class SprintResetManager {
    private static PlayerEntity internalField0457;
    private static Runnable internalField0659;
    private static Runnable internalField0658;
    private static boolean internalField0277;

    private SprintResetManager() {
    }

    public static void internalMethod03031() {
        if (internalField0658 == null) {
            return;
        }
        Runnable runnable = internalField0658;
        internalField0658 = null;
        runnable.run();
    }

    public static void internalMethod04017(PlayerEntity playerEntity) {
        SprintResetManager.internalMethod03966(playerEntity, null);
    }

    public static void internalMethod03966(PlayerEntity playerEntity, Runnable runnable) {
        SprintResetManager.internalMethod06906(playerEntity, runnable, false);
    }

    public static void internalMethod06906(PlayerEntity playerEntity, Runnable runnable, boolean bl) {
        internalField0457 = playerEntity;
        internalField0659 = runnable;
        internalField0277 = bl;
    }

    public static boolean internalMethod06977(Entity entity) {
        return internalField0457 != null && entity == internalField0457;
    }

    public static void internalMethod06976(Entity entity) {
        if (!SprintResetManager.internalMethod06977(entity)) {
            return;
        }
        Runnable runnable = internalField0659;
        boolean bl = internalField0277;
        SprintResetManager.internalMethod04865(entity);
        if (runnable != null) {
            if (bl) {
                runnable.run();
            } else {
                internalField0658 = runnable;
            }
        }
    }

    public static boolean internalMethod03032() {
        return internalField0658 != null;
    }

    public static void internalMethod04865(Entity entity) {
        if (internalField0457 != null && entity == internalField0457) {
            internalField0457 = null;
            internalField0659 = null;
            internalField0277 = false;
        }
    }

    public static boolean internalMethod04866(Entity entity) {
        return SprintResetManager.internalMethod06977(entity) && internalField0277;
    }
}


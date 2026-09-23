package rockstar.client.internal.ui;


import rockstar.client.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.BlockPos;

public final class DeferredScreenTracker {
    public static volatile boolean internalField0277 = false;
    public static volatile BlockPos internalField0352 = null;
    public static volatile Class<? extends Screen> internalField0278 = null;
    public static volatile long internalField0229 = 0L;

    private DeferredScreenTracker() {
    }

    public static void internalMethod05914(BlockPos blockPos, Class<? extends Screen> clazz) {
        internalField0352 = blockPos;
        internalField0278 = clazz;
        internalField0229 = System.currentTimeMillis();
        internalField0277 = true;
    }

    public static void internalMethod03946() {
        internalField0277 = false;
        internalField0352 = null;
        internalField0278 = null;
        internalField0229 = 0L;
    }

    public static boolean internalMethod03735(Screen screen) {
        if (!internalField0277 || screen == null || internalField0278 == null) {
            return false;
        }
        return internalField0278.isInstance(screen);
    }
}


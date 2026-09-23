package rockstar.client.internal.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import org.joml.Matrix3x2fStack;

/** Queues scaled/faded item icons into the 1.21.11 GUI render state. */
public final class ScaledItemQueue {
    private ScaledItemQueue() {
    }

    public static void internalMethod02541(DrawContext context, ItemStack stack, float x, float y, float scale, float alpha) {
        if (stack.isEmpty() || alpha <= 0.01F) {
            return;
        }

        Matrix3x2fStack matrices = context.getMatrices();
        matrices.pushMatrix();
        matrices.translate(x, y);
        matrices.scale(scale, scale);
        RockstarGuiItemRenderer.enqueue(context, stack, 0, 0, 0, alpha);
        matrices.popMatrix();
    }
}

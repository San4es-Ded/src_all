package rockstar.client.internal.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.gui.render.state.ItemGuiElementRenderState;
import net.minecraft.client.render.item.KeyedItemRenderState;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.joml.Matrix3x2f;
import rockstar.client.render.ScissorStack;

/**
 * Owns the deferred GUI item layer used by Rockstar's immediate-mode UI.
 *
 * <p>Vanilla 1.21.11 builds one shared item atlas while extracting the HUD and
 * every open screen. Rockstar draws the rest of its UI immediately, so putting
 * its item states in that shared queue makes their ordering and atlas lifetime
 * depend on whichever custom screen happens to be open. A dedicated renderer
 * keeps the vanilla extraction model while preserving Rockstar's old ordering:
 * panels first, item sprites last.</p>
 */
public final class RockstarGuiItemRenderer {
    private static final GuiRenderState STATE = new GuiRenderState();
    private static GuiRenderer renderer;
    private static boolean hasItems;

    private RockstarGuiItemRenderer() {
    }

    public static void enqueue(DrawContext context, ItemStack stack, int x, int y, int seed, float alpha) {
        if (stack == null || stack.isEmpty() || alpha <= 0.01F) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        KeyedItemRenderState itemState = new KeyedItemRenderState();
        client.getItemModelManager().clearAndUpdate(
            itemState,
            stack,
            ItemDisplayContext.GUI,
            client.world,
            client.player,
            seed
        );
        if (itemState.isEmpty()) {
            return;
        }

        ScreenRect scissor = ScissorStack.currentScreenRect();
        if (scissor == null) {
            scissor = context.scissorStack.peekLast();
        }
        ItemGuiElementRenderState renderState = new ItemGuiElementRenderState(
            stack.getItem().getName().toString(),
            new Matrix3x2f(context.getMatrices()),
            itemState,
            x,
            y,
            scissor
        );
        ItemGuiAlphaCompat.register(renderState, alpha);
        STATE.addItem(renderState);
        hasItems = true;
    }

    public static void render() {
        if (!hasItems) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (renderer == null) {
            renderer = new GuiRenderer(
                STATE,
                client.getBufferBuilders().getEntityVertexConsumers(),
                client.gameRenderer.getEntityRenderCommandQueue(),
                client.gameRenderer.getEntityRenderDispatcher(),
                List.of()
            );
        }

        try {
            renderer.render(RenderSystem.getShaderFog());
            renderer.incrementFrame();
        } finally {
            hasItems = false;
        }
    }
}

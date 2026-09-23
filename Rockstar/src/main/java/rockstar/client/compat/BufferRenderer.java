package rockstar.client.compat;

import rockstar.client.render.compat.ImmediateRenderer;
import net.minecraft.client.render.BuiltBuffer;

/** Routes removed BufferRenderer calls through Rockstar's explicit GPU bridge. */
public final class BufferRenderer {
    private BufferRenderer() {
    }

    public static void drawWithGlobalProgram(BuiltBuffer buffer) {
        ImmediateRenderer.draw(buffer);
    }

    public static void draw(BuiltBuffer buffer) {
        ImmediateRenderer.draw(buffer);
    }
}



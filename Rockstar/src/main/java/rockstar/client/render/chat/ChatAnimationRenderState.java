package rockstar.client.render.chat;

/**
 * Per-line values shared by the chat traversal and Mojang's 1.21.11 chat backends.
 * Rendering is single-threaded, but keeping this state outside a mixin class avoids
 * references to mixin owners after those classes are merged into Minecraft classes.
 */
public final class ChatAnimationRenderState {
    private static float alpha = 1.0f;
    private static float shift;

    private ChatAnimationRenderState() {
    }

    public static void set(float lineAlpha, float lineShift) {
        alpha = lineAlpha;
        shift = lineShift;
    }

    public static void reset() {
        alpha = 1.0f;
        shift = 0.0f;
    }

    public static float alpha() {
        return alpha;
    }

    public static float shift() {
        return shift;
    }
}

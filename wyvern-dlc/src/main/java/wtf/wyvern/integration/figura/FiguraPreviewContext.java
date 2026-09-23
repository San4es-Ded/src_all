package wtf.wyvern.integration.figura;

import org.figuramc.figura.avatar.Avatar;

public final class FiguraPreviewContext {
    private static final ThreadLocal<Avatar> CURRENT = new ThreadLocal<>();

    private FiguraPreviewContext() {
    }

    public static Avatar current() {
        return CURRENT.get();
    }

    public static void renderWith(Avatar avatar, Runnable render) {
        CURRENT.set(avatar);
        try {
            render.run();
        } finally {
            CURRENT.remove();
        }
    }
}

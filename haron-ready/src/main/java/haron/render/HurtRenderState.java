package haron.render;

public final class HurtRenderState {
    private static boolean hurt;

    private HurtRenderState() {
    }

    public static void b() {
        hurt = false;
    }

    public static void a(boolean bl) {
        int n = 130;
        hurt = bl;
    }

    public static boolean a() {
        int n = 103;
        return hurt;
    }
}


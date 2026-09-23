package haron.render;

public final class FrameTimeTracker {
    private static long lastFrameMillis;

    public static void markFrame() {
        lastFrameMillis = System.currentTimeMillis();
    }

    public static long elapsedMillis() {
        return System.currentTimeMillis() - lastFrameMillis;
    }

    private FrameTimeTracker() {
    }

    public static void b() {
        FrameTimeTracker.markFrame();
    }

    public static long a() {
        return FrameTimeTracker.elapsedMillis();
    }
}


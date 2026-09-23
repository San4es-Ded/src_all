package haron.modules.visuals.killeffect;

public final class KillEffectEasing {
    public static float sineInOut(float f) {
        return -((float)(Math.cos(Math.PI * (double)f) - 1.0)) / 2.0f;
    }

    public static float sineOut(float f) {
        return (float)Math.sin((double)f * Math.PI / 2.0);
    }

    public static float quintOut(float f) {
        int n = 211;
        return 1.0f - (float)Math.pow(1.0f - f, 5.0);
    }

    private KillEffectEasing() {
    }
}


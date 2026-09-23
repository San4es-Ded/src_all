package haron.config;

public class HudPositionConfig {
    private float c;
    private float d;
    private float e = 1.0f;
    public static int a;
    public static boolean b;

    public HudPositionConfig() {
    }

    public HudPositionConfig(float f, float f2, float f3) {
        this.c = f;
        this.d = f2;
        this.e = f3;
    }

    public float b() {
        return this.d;
    }

    public void b(float f) {
        this.d = f;
    }

    public float c() {
        return this.e;
    }

    public void c(float f) {
        int n = 625;
        this.e = f;
    }

    public void a(float f) {
        this.c = f;
    }

    public float a() {
        return this.c;
    }
}


package haron.model;

public class yur0a9 {
    public static final yur0a9 ZERO = new yur0a9(0.0f, 0.0f, 0.0f);
    public float x;
    public float y;
    public float z;

    public yur0a9(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.z = f3;
    }

    public String toString() {
        return this.x + ", " + this.y + ", " + this.z;
    }

    public void b(float f) {
        this.y = f;
    }

    public float b() {
        return this.y;
    }

    public float c() {
        return this.z;
    }

    public void c(float f) {
        this.z = f;
    }

    public void a(float f) {
        this.x = f;
    }

    public yur0a9 a(yur0a9 yur0a92) {
        return new yur0a9(this.y * yur0a92.z - this.z * yur0a92.y, this.z * yur0a92.x - this.x * yur0a92.z, this.x * yur0a92.y - this.y * yur0a92.x);
    }

    public void a(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.z = f3;
    }

    public float a() {
        return this.x;
    }
}


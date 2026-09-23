package haron.render;

public enum PlayerModelPart {
    FREE(-1),
    BODY(1),
    HEAD(3),
    ABOVE_HEAD(-1),
    RIGHT_ARM(-1),
    LEFT_ARM(-1),
    RIGHT_LEG(2),
    LEFT_LEG(0);

    private final int modelPartIndex;

    private PlayerModelPart(int n2) {
        this.modelPartIndex = n2;
    }

    public int b() {
        return this.modelPartIndex;
    }

    public int a() {
        return this.ordinal();
    }

    public static PlayerModelPart a(int n) {
        return n < 0 || n >= PlayerModelPart.values().length ? BODY : PlayerModelPart.values()[n];
    }
}


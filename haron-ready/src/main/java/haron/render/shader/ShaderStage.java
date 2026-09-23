package haron.render.shader;

public enum ShaderStage {
    VERTEX(35633),
    FRAGMENT(35632);

    private final int glType;

    public int glType() {
        return this.glType;
    }

    private ShaderStage(int n2) {
        this.glType = n2;
    }

}

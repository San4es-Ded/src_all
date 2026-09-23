package rockstar.client.compat;

import rockstar.client.render.compat.LegacyShaderProgram;
import org.joml.Matrix4fc;

/** Compatibility view over a uniform stored in Rockstar's 1.21.11 UBO. */
public final class GlUniform {
    private final LegacyShaderProgram.UniformValue value;

    public GlUniform(LegacyShaderProgram.UniformValue value) {
        this.value = value;
    }

    public void set(float x) {
        value.set(x);
    }

    public void set(float x, float y) {
        value.set(x, y);
    }

    public void set(float x, float y, float z) {
        value.set(x, y, z);
    }

    public void set(float x, float y, float z, float w) {
        value.set(x, y, z, w);
    }

    public void set(int x) {
        value.set(x);
    }

    public void set(float[] values) {
        switch (values.length) {
            case 0 -> { }
            case 1 -> value.set(values[0]);
            case 2 -> value.set(values[0], values[1]);
            case 3 -> value.set(values[0], values[1], values[2]);
            default -> value.set(values[0], values[1], values[2], values[3]);
        }
    }

    public void set(Matrix4fc matrix) {
        value.set(matrix);
    }

    public void setAndFlip(float x, float y, float z, float w) {
        value.set(x, y, z, w);
    }
}



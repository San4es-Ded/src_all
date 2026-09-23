package ru.prism.utils.render;

import java.awt.Color;
import net.minecraft.client.render.VertexConsumer;

public class MaceHandConsumer implements VertexConsumer {

    private final VertexConsumer delegate;
    private final Color tint;

    public MaceHandConsumer(VertexConsumer delegate, Color tint) {
        this.delegate = delegate;
        this.tint = tint;
    }

    @Override
    public VertexConsumer vertex(float x, float y, float z) {
        return this.delegate.vertex(x, y, z);
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        return this.delegate.color(red, green, blue, alpha);
    }

    @Override
    public VertexConsumer color(int argb) {
        return this.delegate.color(argb);
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        return this.delegate.texture(u, v);
    }

    @Override
    public VertexConsumer overlay(int u, int v) {
        return this.delegate.overlay(u, v);
    }

    @Override
    public VertexConsumer light(int u, int v) {
        return this.delegate.light(u, v);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        return this.delegate.normal(x, y, z);
    }

    @Override
    public VertexConsumer lineWidth(float width) {
        return this.delegate.lineWidth(width);
    }

    @Override
    public void vertex(float x, float y, float z, int color, float u, float v, int overlay, int light, float nx, float ny, float nz) {
        int alpha = color >> 24 & 0xFF;
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;

        float factor = 0.95F;
        int nr = (int) (red * (1.0F - factor) + this.tint.getRed() * factor);
        int ng = (int) (green * (1.0F - factor) + this.tint.getGreen() * factor);
        int nb = (int) (blue * (1.0F - factor) + this.tint.getBlue() * factor);

        this.delegate.vertex(x, y, z, alpha << 24 | nr << 16 | ng << 8 | nb, u, v, overlay, light, nx, ny, nz);
    }
}

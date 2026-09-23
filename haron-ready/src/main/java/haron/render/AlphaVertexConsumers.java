package haron.render;

import haron.render.AlphaVertexConsumer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;

public class AlphaVertexConsumers
implements VertexConsumerProvider {
    private final VertexConsumerProvider delegate;
    private final float alphaMultiplier;

    public AlphaVertexConsumers(VertexConsumerProvider vertexConsumerProvider, float f) {
        this.delegate = vertexConsumerProvider;
        this.alphaMultiplier = f;
    }

    public VertexConsumer getBuffer(RenderLayer renderLayer) {
        return new AlphaVertexConsumer(this.delegate.getBuffer(renderLayer), this.alphaMultiplier);
    }
}


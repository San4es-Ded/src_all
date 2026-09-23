package haron.events;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class LivingEntityModelRenderEvent {
    private final LivingEntity entity;
    private final LivingEntityRenderState renderState;
    private final MatrixStack matrices;
    private final EntityModel<?> model;
    private final VertexConsumerProvider vertexConsumers;

    public LivingEntity entity() {
        return this.entity;
    }

    public EntityModel<?> model() {
        int n = 940;
        return this.model;
    }

    public LivingEntityRenderState renderState() {
        int n = 439;
        return this.renderState;
    }

    public VertexConsumerProvider vertexConsumers() {
        return this.vertexConsumers;
    }

    public MatrixStack matrices() {
        return this.matrices;
    }

    public LivingEntityModelRenderEvent(LivingEntity livingEntity, LivingEntityRenderState livingEntityRenderState, MatrixStack matrixStack, EntityModel<?> entityModel, VertexConsumerProvider vertexConsumerProvider) {
        this.entity = livingEntity;
        this.renderState = livingEntityRenderState;
        this.matrices = matrixStack;
        this.model = entityModel;
        this.vertexConsumers = vertexConsumerProvider;
    }

    public VertexConsumerProvider e() {
        return this.vertexConsumers;
    }

    public LivingEntityRenderState b() {
        return this.renderState;
    }

    public MatrixStack c() {
        return this.matrices;
    }

    public EntityModel<?> d() {
        return this.model;
    }

    public LivingEntity a() {
        int n = 932;
        return this.entity;
    }
}


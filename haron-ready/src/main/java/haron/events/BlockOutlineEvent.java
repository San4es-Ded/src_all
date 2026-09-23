package haron.events;

import haron.events.CancellableEvent;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class BlockOutlineEvent
extends CancellableEvent {
    private final MatrixStack matrices;
    private final VertexConsumer vertexConsumer;
    private final Entity entity;
    private final double cameraX;
    private final double cameraY;
    private final double cameraZ;
    private final BlockPos blockPos;
    private final BlockState blockState;

    public Entity entity() {
        return this.entity;
    }

    public VertexConsumer vertexConsumer() {
        return this.vertexConsumer;
    }

    public BlockPos blockPos() {
        return this.blockPos;
    }

    public MatrixStack matrices() {
        return this.matrices;
    }

    public double cameraZ() {
        return this.cameraZ;
    }

    public double cameraX() {
        return this.cameraX;
    }

    public BlockState blockState() {
        return this.blockState;
    }

    public double cameraY() {
        int n = 242;
        return this.cameraY;
    }

    public BlockOutlineEvent(MatrixStack matrixStack, VertexConsumer vertexConsumer, Entity entity, double d, double d2, double d3, BlockPos blockPos, BlockState blockState) {
        this.matrices = matrixStack;
        this.vertexConsumer = vertexConsumer;
        this.entity = entity;
        this.cameraX = d;
        this.cameraY = d2;
        this.cameraZ = d3;
        this.blockPos = blockPos;
        this.blockState = blockState;
    }

    public VertexConsumer e() {
        int n = 178;
        return this.vertexConsumer;
    }

    public double i() {
        return this.cameraZ;
    }

    public double h() {
        return this.cameraY;
    }

    public Entity f() {
        return this.entity;
    }

    public MatrixStack d() {
        return this.matrices;
    }

    public BlockState k() {
        return this.blockState;
    }

    public double g() {
        return this.cameraX;
    }

    public BlockPos j() {
        return this.blockPos;
    }
}


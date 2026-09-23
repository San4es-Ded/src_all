package ru.haron.mixin;

import haron.events.BlockOutlineEvent;
import haron.events.EventDispatcher;
import haron.module.ModuleManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={WorldRenderer.class})
public class BlockOutlineMixin {
    @Inject(method={"drawBlockOutline"}, at={@At(value="HEAD")}, cancellable=true)
    private void onDrawBlockOutline(MatrixStack MatrixStackVar, VertexConsumer VertexConsumerVar, Entity EntityVar, double d, double d2, double d3, BlockPos BlockPosVar, BlockState BlockStateVar, int i, CallbackInfo callbackInfo) {
        if (ModuleManager.BLOCK_OVERLAY.k()) {
            BlockOutlineEvent blockOutlineEvent = new BlockOutlineEvent(MatrixStackVar, VertexConsumerVar, EntityVar, d, d2, d3, BlockPosVar, BlockStateVar);
            EventDispatcher.EVENT_BUS.post((Object)blockOutlineEvent);
            if (blockOutlineEvent.c()) {
                callbackInfo.cancel();
            }
        }
    }
}


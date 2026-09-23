package ru.haron.mixin;

import haron.modules.utilities.Optimizations;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityRenderDispatcher.class})
public class EntityRenderDispatcherMixin {
    @Inject(method={"renderShadow"}, at={@At(value="HEAD")}, cancellable=true)
    private static void haron$hideEntityShadow(CallbackInfo ci) {
        if (Optimizations.hideEntityShadows()) {
            ci.cancel();
        }
    }

    @Inject(method={"shouldRender"}, at={@At(value="HEAD")}, cancellable=true)
    private <E extends Entity> void haron$cullDistantEntities(E entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (!Optimizations.cullDistantEntities()) {
            return;
        }
        if (entity == MinecraftClient.getInstance().player) {
            return;
        }
        if (entity.squaredDistanceTo(x, y, z) > Optimizations.maxEntityDistanceSq()) {
            cir.setReturnValue(false);
        }
    }
}


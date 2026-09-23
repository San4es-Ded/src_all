package ru.haron.mixin;

import haron.module.ModuleManager;
import haron.modules.utilities.FreeLook;
import haron.modules.utilities.Optimizations;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Entity.class})
public abstract class EntityMixin {
    @Shadow
    private boolean glowing;

    @Inject(method={"changeLookDirection"}, at={@At(value="HEAD")}, cancellable=true)
    public void changeLookDirection(double d, double d2, CallbackInfo callbackInfo) {
        if (FreeLook.active && (Object)this instanceof ClientPlayerEntity) {
            FreeLook.rotateTowards(d, d2);
            callbackInfo.cancel();
        }
    }

    @Shadow
    public abstract boolean isGlowing();

    @Inject(method={"isGlowing"}, at={@At(value="HEAD")}, cancellable=true)
    private void onIsGlowing(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (ModuleManager.RENDER_TWEAKS.v()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")}, cancellable=true)
    private void onEntityTickCulling(CallbackInfo callbackInfo) {
        boolean safeToCull;
        if (!Optimizations.cullDistantEntities()) {
            return;
        }
        Entity self = (Entity)(Object)this;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || self == client.player) {
            return;
        }
        if (self instanceof LivingEntity) {
            return;
        }
        if (self instanceof DisplayEntity) {
            return;
        }
        boolean bl = safeToCull = self instanceof ItemEntity || self instanceof ExperienceOrbEntity;
        if (!safeToCull) {
            return;
        }
        double maxSq = Optimizations.maxEntityDistanceSq();
        if (self.squaredDistanceTo((Entity)client.player) <= maxSq) {
            return;
        }
        if (self.age % 3 != 0) {
            callbackInfo.cancel();
        }
    }
}

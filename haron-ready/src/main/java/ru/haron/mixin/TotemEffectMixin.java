package ru.haron.mixin;

import haron.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={InGameHud.class})
public abstract class TotemEffectMixin {
    @Shadow
    @Final
    private MinecraftClient client;
    @Final
    private static Identifier TOTEM_OF_UNDYING_TEXTURE;
    @Shadow
    private int heldItemTooltipFade;
    @Shadow
    private ItemStack currentStack;

    @Inject(method={"renderHeldItemTooltip"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderHeldItemTooltip(DrawContext DrawContextVar, CallbackInfo callbackInfo) {
        if (!ModuleManager.RENDER_TWEAKS.q() || this.heldItemTooltipFade <= 0 || this.currentStack.isEmpty()) {
            return;
        }
        callbackInfo.cancel();
    }

    @Inject(method={"tick()V"}, at={@At(value="HEAD")})
    private void onTick(CallbackInfo callbackInfo) {
        if (!ModuleManager.RENDER_TWEAKS.q() || this.heldItemTooltipFade <= 0) {
            return;
        }
        this.heldItemTooltipFade = 0;
    }
}


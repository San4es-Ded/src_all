package ru.haron.mixin;

import haron.module.ModuleManager;
import haron.modules.visuals.RenderTweaks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={BossBarHud.class})
public class BossBarHudMixin {
    @Unique
    private static volatile boolean cachedHide = false;
    @Unique
    private static volatile long cachedTime = 0L;
    @Unique
    private static final long CACHE_TTL_MS = 500L;

    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRender(DrawContext DrawContextVar, CallbackInfo callbackInfo) {
        long now = System.currentTimeMillis();
        if (now - cachedTime > 500L) {
            cachedTime = now;
            RenderTweaks renderTweaks = ModuleManager.RENDER_TWEAKS;
            boolean bl = cachedHide = renderTweaks.s();
        }
        if (cachedHide) {
            callbackInfo.cancel();
        }
    }
}


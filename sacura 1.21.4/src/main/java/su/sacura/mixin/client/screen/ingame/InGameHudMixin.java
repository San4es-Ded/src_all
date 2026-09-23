package su.sacura.mixin.client.screen.ingame;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.function.Function;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.sacura.Sacura;
import su.sacura.events.render.DrawEvent;
import su.sacura.features.modules.impl.render.BetterMinecraftModule;
import su.sacura.util.impl.math.helper.AnimationUtil;
import su.sacura.util.impl.render.RenderUtils;
import su.sacura.util.type.MinecraftWrapper;

@Mixin({InGameHud.class})
public class InGameHudMixin implements MinecraftWrapper {
    @Shadow @Final private PlayerListHud field_2015;

    @Unique
    private float sacura$currentSlot = -1.0F;

    @Inject(method = {"render"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LayeredDrawer;render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", shift = At.Shift.AFTER)})
    public void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        System.out.println("[InGameHudMixin] posting DrawEvent");
        DrawEvent event = new DrawEvent(context, null, tickCounter.getTickDelta(false));
        Sacura.getInstance().getEventBus().post(event);
    }

    // Исправлено: @Redirect -> @WrapOperation
    @WrapOperation(method = {"renderHotbar"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V"))
    public void onDrawHotbarSelection(DrawContext context, Function<Identifier, RenderLayer> renderLayers, Identifier identifier, int x, int y, int width, int height, Operation<Void> original) {
        if (width == 24 && height == 23) {
            BetterMinecraftModule module = Sacura.getInstance().getModuleManager().getModule(BetterMinecraftModule.class);
            if (module != null && module.enable && ((Boolean)module.smoothHotBar.get()).booleanValue() && mc.player != null) {
                if (this.sacura$currentSlot == -1.0F)
                    this.sacura$currentSlot = mc.player.getInventory().selectedSlot;

                this.sacura$currentSlot = AnimationUtil.animate(mc.player.getInventory().selectedSlot, this.sacura$currentSlot, ((Float)module.speedHotBar.get()).floatValue());
                int newX = (int)((mc.getWindow().getScaledWidth() / 2 - 91 - 1) + this.sacura$currentSlot * 20.0F);
                original.call(context, renderLayers, identifier, newX, y, width, height);
                return;
            }
        }
        original.call(context, renderLayers, identifier, x, y, width, height);
    }

    @Inject(method = {"render"}, at = {@At("HEAD")})
    private void onRenderHead(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        BetterMinecraftModule module = Sacura.getInstance().getModuleManager().getModule(BetterMinecraftModule.class);
        if (module != null && module.enable && ((Boolean)module.smoothTab.get()).booleanValue())
            module.getTabOpenAnimation().update(mc.options.playerListKey.isPressed());
    }

    // Исправлено: @Redirect -> @WrapOperation (ordinal 0)
    @WrapOperation(method = {"renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 0))
    private void redirectScoreboardHeaderFill(DrawContext context, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
        BetterMinecraftModule betterMinecraft = Sacura.getInstance().getModuleManager().getModule(BetterMinecraftModule.class);
        if (betterMinecraft != null && betterMinecraft.enable && ((Boolean)betterMinecraft.improvedScoreboard.get()).booleanValue()) {
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), x1, y1, (x2 - x1), (y2 - y1 + 1), 6.0F, 0.0F, 0.0F, 0.0F, color, color, color, color);
        } else {
            original.call(context, x1, y1, x2, y2, color);
        }
    }

    // Исправлено: @Redirect -> @WrapOperation (ordinal 1)
    @WrapOperation(method = {"renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 1))
    private void redirectScoreboardScoresFill(DrawContext context, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
        BetterMinecraftModule betterMinecraft = Sacura.getInstance().getModuleManager().getModule(BetterMinecraftModule.class);
        if (betterMinecraft != null && betterMinecraft.enable && ((Boolean)betterMinecraft.improvedScoreboard.get()).booleanValue()) {
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), x1, y1, (x2 - x1), (y2 - y1), 0.0F, 6.0F, 0.0F, 0.0F, color, color, color, color);
        } else {
            original.call(context, x1, y1, x2, y2, color);
        }
    }
}   
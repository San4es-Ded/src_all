package ru.prism.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import ru.prism.manager.DragComponent;
import ru.prism.manager.event_impl.EventDisplay;
import ru.prism.module.impl.render.Animations;
import ru.prism.module.impl.render.CrossHair;
import ru.prism.module.impl.render.NoRender;
import ru.prism.utils.render.Render2D;
import ru.prism.utils.render.ScreenBlur;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.DrawContext;

import ru.prism.Client;
import ru.prism.module.impl.utils.StreamerMode;
import net.minecraft.client.MinecraftClient;

import static ru.prism.utils.annotation.IMinecraft.mc;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin  {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private PlayerListHud playerListHud;

    @Unique
    private boolean prism$prevTabShown;

    @Unique
    private boolean prism$deferHotbarHighlight;

    @Unique
    private int prism$deferredHotbarY;

    @Unique
    private Identifier prism$deferredHotbarTexture;

    @Unique
    private RenderPipeline prism$deferredHotbarPipeline;

    @Inject(method = "render", at = @At("TAIL"))
    public void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null) return;


        ScreenBlur.frame();

        Screen screen = mc.currentScreen;

        if (isLoadingScreen(screen)) return;

        context.createNewRootLayer();
        Render2D.beginOverlay();




        context.getMatrices().pushMatrix();




        EventDisplay event = new EventDisplay(context, tickCounter.getTickProgress(false));

        Client.get().componentManager().get(DragComponent.class).post(context.getMatrices());

        event.hook();

        Client.get().render2D().flushAll();

        context.getMatrices().popMatrix();
        Render2D.endOverlay();

    }

    @Inject(
            method = "renderStatusEffectOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {


        ci.cancel();
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void removeVanillaCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        try {
            CrossHair crosshairModule = CrossHair.getInstance();
            if ( crosshairModule.isEnabled()) {
                ci.cancel();
            }
        } catch (Exception e) {

        }
    }
    @Inject(method = "renderPlayerList", at = @At("HEAD"), cancellable = true)
    private void prism$renderPlayerList(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        Animations animations = Animations.get();
        if (animations == null || !animations.isEnabled() || !animations.tabList.getValue()) {
            this.prism$prevTabShown = false;
            return;
        }
        ci.cancel();
        if (this.client.world == null || this.client.player == null) {
            return;
        }
        Scoreboard scoreboard = this.client.world.getScoreboard();
        ScoreboardObjective objective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.LIST);
        boolean shown = this.client.options.playerListKey.isPressed()
                && (!this.client.isInSingleplayer()
                || this.client.player.networkHandler.getListedPlayerListEntries().size() > 1
                || objective != null);
        animations.updateTab(shown);
        if (!shown && !animations.tabRenderable()) {
            this.playerListHud.setVisible(false);
            return;
        }
        this.playerListHud.setVisible(true);
        float scale = animations.tabScale();
        if (scale < 0.001F) {
            this.playerListHud.setVisible(false);
            return;
        }
        context.createNewRootLayer();
        Matrix3x2fStack matrices = context.getMatrices();
        matrices.pushMatrix();
        float centerX = context.getScaledWindowWidth() / 2.0F;
        matrices.translate(centerX, 0.0F);
        matrices.scale(scale, scale);
        matrices.translate(-centerX, 0.0F);
        this.playerListHud.render(context, context.getScaledWindowWidth(), scoreboard, objective);
        matrices.popMatrix();
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    private void prism$updateHotbarAnimation(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        Animations animations = Animations.get();
        if (animations == null || !animations.isEnabled() || !animations.hotbar.getValue()) {
            return;
        }
        if (this.client.player != null) {
            animations.updateHotbar(this.client.player.getInventory().getSelectedSlot(), context.getScaledWindowWidth() / 2);
        }
    }

    @Redirect(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V",
                    ordinal = 1
            )
    )
    private void prism$deferHotbarHighlight(DrawContext context, RenderPipeline pipeline, Identifier texture, int x, int y, int width, int height) {
        Animations animations = Animations.get();
        if (animations == null || !animations.isEnabled() || !animations.hotbar.getValue()) {
            context.drawGuiTexture(pipeline, texture, x, y, width, height);
            return;
        }
        this.prism$deferredHotbarPipeline = pipeline;
        this.prism$deferredHotbarTexture = texture;
        this.prism$deferredHotbarY = y;
        this.prism$deferHotbarHighlight = true;
    }

    @Inject(method = "renderHotbar", at = @At("RETURN"))
    private void prism$renderHotbarHighlight(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!this.prism$deferHotbarHighlight) {
            return;
        }
        this.prism$deferHotbarHighlight = false;
        Animations animations = Animations.get();
        if (animations == null) {
            return;
        }
        context.drawGuiTexture(this.prism$deferredHotbarPipeline, this.prism$deferredHotbarTexture, Math.round(animations.hotbarX()), this.prism$deferredHotbarY, 24, 23);
    }

    @Unique
    private boolean isLoadingScreen(Screen screen) {
        if (screen == null) return false;
        String className = screen.getClass().getSimpleName().toLowerCase();
        String fullName = screen.getClass().getName().toLowerCase();
        if (className.contains("loading")) return true;
        if (className.contains("progress")) return true;
        if (className.contains("connecting")) return true;
        if (className.contains("downloading")) return true;
        if (className.contains("terrain")) return true;
        if (className.contains("generating")) return true;
        if (className.contains("saving")) return true;
        if (className.contains("reload")) return true;
        if (className.contains("resource")) return true;
        if (className.contains("pack")) return true;
        if (fullName.contains("mojang")) return true;
        return false;
    }

    @Inject(method = "renderNauseaOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderNauseaOverlay(DrawContext context, float nauseaStrength, CallbackInfo ci) {
        NoRender noRender = NoRender.getInstance();
        if (noRender != null && noRender.isEnabled() && noRender.ignoreZalupa.getValue()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", at = @At("HEAD"), cancellable = true)
    private void onRenderScoreboard(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        NoRender noRender = NoRender.getInstance();
        if (noRender != null && noRender.isEnabled() && noRender.ignoreScoreboard.getValue()) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/scoreboard/ScoreboardObjective;getDisplayName()Lnet/minecraft/text/Text;")
    )
    private Text prism$hideServerName(ScoreboardObjective objective) {
        StreamerMode streamerMode = StreamerMode.get();
        if (streamerMode != null && streamerMode.isEnabled() && streamerMode.hideServer.getValue()) {
            return Text.literal("prism");
        }
        return objective.getDisplayName();
    }

    @Inject(method = "renderBossBarHud", at = @At("HEAD"), cancellable = true)
    private void onRenderBossBar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        NoRender noRender = NoRender.getInstance();
        if (noRender != null && noRender.isEnabled() && noRender.ignoreBossBar.getValue()) {
            ci.cancel();
        }
    }
}

package moscow.rockstar.mixin.minecraft.client.gui.overlay;






import rockstar.client.server.*;
import rockstar.client.render.*;
import rockstar.client.animation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import rockstar.client.compat.RenderSystem;
import globals.client.snowball.FakeFrozenTicksAccess;
import moscow.rockstar.mixin.accessors.InGameHudAccessor;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import pyrock.events.render.HudRenderEvent;
import pyrock.events.render.PostHudRenderEvent;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.render.PostProcessRenderer;
import rockstar.modules.visual.BeautifullyModule;
import rockstar.client.render.SizedFont;
import rockstar.modules.visual.RemovalsModule;
import rockstar.client.render.Fonts;
import rockstar.client.internal.script.HudContextMenu;
import rockstar.client.internal.script.HotbarHud;
import rockstar.client.internal.script.EffectsHud;
import rockstar.client.RockstarClient;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.internal.core.DrawCallCounter;
import rockstar.client.render.RenderPipeline;
import rockstar.client.render.UiBatchRenderer;
import rockstar.client.internal.script.TextBatchRenderer;

@Mixin(value={InGameHud.class})
public class InGameHudMixin
implements MinecraftClientAccess {
    @Shadow
    @Final
    private static Identifier POWDER_SNOW_OUTLINE;
    @Shadow
    @Final
    private PlayerListHud playerListHud;
    @Unique
    private AnimatedValue rockstar$tabAnimation;
    @Unique
    private boolean rockstar$mainHudShifted;

    @Unique
    private AnimatedValue rockstar$tabAnimation() {
        if (this.rockstar$tabAnimation == null) {
            this.rockstar$tabAnimation = new AnimatedValue(200L, Easing.internalField1822);
        }
        return this.rockstar$tabAnimation;
    }

    @Inject(method={"renderCrosshair"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$hideCrosshairInScreens(DrawContext drawContext, RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        if (internalField0149.currentScreen != null) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderPlayerList"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$animatePlayerList(DrawContext drawContext, RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        if (!BeautifullyModule.internalMethod09647()) {
            return;
        }
        if (InGameHudMixin.internalField0149.world == null || InGameHudMixin.internalField0149.player == null || InGameHudMixin.internalField0149.player.networkHandler == null) {
            return;
        }
        Scoreboard scoreboard = InGameHudMixin.internalField0149.world.getScoreboard();
        ScoreboardObjective scoreboardObjective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.LIST);
        boolean bl = InGameHudMixin.internalField0149.options.playerListKey.isPressed() && (!internalField0149.isInSingleplayer() || InGameHudMixin.internalField0149.player.networkHandler.getListedPlayerListEntries().size() > 1 || scoreboardObjective != null);
        callbackInfo.cancel();
        AnimatedValue typedValue210 = this.rockstar$tabAnimation();
        typedValue210.internalMethod07061(200L);
        typedValue210.internalMethod06645(bl ? Easing.internalField0811 : Easing.internalField1629);
        float f = typedValue210.internalMethod07059(bl ? 1.0f : 0.0f);
        this.playerListHud.setVisible(bl);
        if (f <= 0.005f) {
            return;
        }
        float f2 = MathHelper.clamp((float)f, (float)0.0f, (float)1.0f);
        float f3 = drawContext.getScaledWindowWidth();
        float f4 = 0.96f + 0.04f * f;
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)f2);
        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(f3 / 2.0f, 0.0f);
        drawContext.getMatrices().scale(f4, f4);
        drawContext.getMatrices().translate(-f3 / 2.0f, (f - 1.0f) * 10.0f);
        this.playerListHud.render(drawContext, drawContext.getScaledWindowWidth(), scoreboard, scoreboardObjective);
        drawContext.getMatrices().popMatrix();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    @Redirect(method={"clear"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/ChatHud;clear(Z)V"))
    private void rockstar$keepChatHistory(ChatHud chatHud, boolean bl) {
        if (BeautifullyModule.internalMethod09826()) {
            return;
        }
        chatHud.clear(bl);
    }

    @Inject(method={"renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderScoreboardSidebarHook(DrawContext drawContext, ScoreboardObjective scoreboardObjective, CallbackInfo callbackInfo) {
        RemovalsModule typedValue322;
        if (scoreboardObjective.getDisplayName().getString().contains("\u0410\u043d\u0430\u0440\u0445\u0438\u044f") && (ServerUtils.internalMethod01786(KnownServer.internalField0578) || ServerUtils.internalMethod01786(KnownServer.internalField0579))) {
            try {
                ServerUtils.internalField0228 = Integer.parseInt(scoreboardObjective.getDisplayName().getString().split("-")[1].trim());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (scoreboardObjective.getDisplayName().getString().contains("\u0413\u0440\u0438\u0444\u0435\u0440\u0441\u043a\u0438\u0439") && ServerUtils.internalField0276) {
            try {
                ServerUtils.internalField1053 = Integer.parseInt(scoreboardObjective.getDisplayName().getString().split("-")[1].trim());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (scoreboardObjective.getDisplayName().getString().contains("\u0413\u0420\u0418\u0424") && ServerUtils.internalMethod01786(KnownServer.internalField1220)) {
            try {
                ServerUtils.internalField1055 = Integer.parseInt(scoreboardObjective.getDisplayName().getString().split("#")[1].trim());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if ((typedValue322 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class)).isEnabled() && typedValue322.internalMethod05450().isSelected()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderPortalOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderPortalOverlayHook(DrawContext drawContext, float f, CallbackInfo callbackInfo) {
        RemovalsModule typedValue322 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        if (typedValue322.isEnabled() && typedValue322.internalMethod08525().isSelected()) {
            callbackInfo.cancel();
        }
    }

    @ModifyArgs(method={"renderMiscOverlays"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V", ordinal=0))
    private void onRenderPumpkinOverlay(Args args) {
        RemovalsModule typedValue322 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        if (typedValue322.isEnabled() && typedValue322.internalMethod09196().isSelected()) {
            args.set(2, (Object)Float.valueOf(0.0f));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Inject(method={"render"}, at={@At(value="HEAD")})
    public void triggerPreHudRenderEvent(DrawContext drawContext, RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        PostProcessRenderer.internalMethod01672(0);
        CustomDrawContext customDrawContext = CustomDrawContext.of(drawContext);
        DrawCallCounter.internalMethod08091();
        try {
            RenderPipeline.internalField0314.internalMethod05186();
            RockstarClient.getInstance().internalMethod03317().internalMethod06883(new PreHudRenderEvent(customDrawContext, renderTickCounter.getTickProgress(false)));
        }
        finally {
            DrawCallCounter.internalMethod08092();
        }
    }

    @Inject(method={"render"}, at={@At(value="RETURN")})
    public void triggerPostHudRenderEvent(DrawContext drawContext, RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        CustomDrawContext customDrawContext = CustomDrawContext.of(drawContext);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new PostHudRenderEvent(customDrawContext, renderTickCounter.getTickProgress(false)));
        UiBatchRenderer.internalMethod02576();
        PostProcessRenderer.internalMethod01672(1);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void rockstar$renderDrawCalls(CustomDrawContext customDrawContext) {
        SizedFont typedValue020 = Fonts.internalField0449.internalMethod01432(8.0f);
        String string = "Draw calls: " + DrawCallCounter.internalMethod00120();
        float f = 4.0f;
        float f2 = typedValue020.internalMethod00965(string) + f * 2.0f;
        float f3 = typedValue020.internalMethod04890() + f * 2.0f;
        float f4 = 4.0f;
        float f5 = 4.0f;
        DrawCallCounter.internalMethod08091();
        try {
            TextBatchRenderer typedValue256 = new TextBatchRenderer(typedValue020.internalMethod01335(), 3.0f);
            typedValue256.internalMethod04362(rockstar.client.render.GuiMatrixCompat.toMatrix4f(customDrawContext.getMatrices()), f4, f5, f2, f3, new ColorRGBA(12.0f, 12.0f, 12.0f, 180.0f));
            typedValue256.internalMethod00978(rockstar.client.render.GuiMatrixCompat.toMatrix4f(customDrawContext.getMatrices()), string, typedValue020.internalMethod07850(), f4 + f, f5 + f, 0.0f, ColorRGBA.WHITE.getRGB());
            typedValue256.internalMethod03841();
        }
        finally {
            DrawCallCounter.internalMethod08092();
        }
    }

    @Inject(method={"renderMainHud"}, at={@At(value="HEAD")})
    private void rockstar$shiftVanillaMainHud(DrawContext drawContext, RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        float f = HotbarHud.internalMethod06380();
        if (f == 0.0f) {
            return;
        }
        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(0.0f, -f);
        this.rockstar$mainHudShifted = true;
    }

    @Inject(method={"renderMainHud"}, at={@At(value="TAIL")})
    private void triggerHudRenderEvent(DrawContext drawContext, RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        if (this.rockstar$mainHudShifted) {
            this.rockstar$mainHudShifted = false;
            drawContext.getMatrices().popMatrix();
        }
        if (RockstarClient.internalField0240.internalMethod06896()) {
            return;
        }
        CustomDrawContext customDrawContext = CustomDrawContext.of(drawContext);
        HudContextMenu.internalField0277 = false;
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new HudRenderEvent(customDrawContext, renderTickCounter.getTickProgress(false)));
    }

    @Inject(method={"renderHotbar"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$hideVanillaHotbar(DrawContext drawContext, RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        if (RockstarClient.internalField0240.internalMethod06896()) {
            return;
        }
        if (RockstarClient.getInstance().internalMethod01271() == null) {
            return;
        }
        HotbarHud typedValue200 = RockstarClient.getInstance().internalMethod01271().internalMethod01440();
        if (typedValue200 != null && typedValue200.isShowing() && typedValue200.show()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderStatusBars"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$hideVanillaStatusBars(DrawContext drawContext, CallbackInfo callbackInfo) {
        if (RockstarClient.internalField0240.internalMethod06896()) {
            return;
        }
        if (RockstarClient.getInstance().internalMethod01271() == null) {
            return;
        }
        HotbarHud typedValue200 = RockstarClient.getInstance().internalMethod01271().internalMethod01440();
        if (typedValue200 != null && typedValue200.isShowing() && typedValue200.show()) {
            callbackInfo.cancel();
        }
    }

    @Redirect(
        method={"renderMainHud"},
        at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/bar/Bar;renderBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V")
    )
    private void rockstar$renderCurrentBar(Bar bar, DrawContext drawContext, RenderTickCounter renderTickCounter) {
        if (this.rockstar$customInterfaceHidesCurrentBar(bar)) {
            return;
        }
        bar.renderBar(drawContext, renderTickCounter);
    }

    @Redirect(
        method={"renderMainHud"},
        at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/bar/Bar;renderAddons(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V")
    )
    private void rockstar$renderCurrentBarAddons(Bar bar, DrawContext drawContext, RenderTickCounter renderTickCounter) {
        if (this.rockstar$customInterfaceHidesCurrentBar(bar)) {
            return;
        }
        bar.renderAddons(drawContext, renderTickCounter);
    }

    @Redirect(
        method={"renderMainHud"},
        at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/bar/Bar;drawExperienceLevel(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;I)V")
    )
    private void rockstar$renderExperienceLevel(DrawContext drawContext, TextRenderer textRenderer, int level) {
        if (this.rockstar$customInterfaceHidesVanillaHud()) {
            return;
        }
        // renderMainHud is already translated as a single unit at HEAD. Applying
        // the same offset here again separated the level text from its bar when
        // the hotbar was dragged away from the bottom edge.
        Bar.drawExperienceLevel(drawContext, textRenderer, level);
    }

    @Unique
    private boolean rockstar$customInterfaceHidesVanillaHud() {
        if (RockstarClient.internalField0240.internalMethod06896() || RockstarClient.getInstance().internalMethod01271() == null) {
            return false;
        }
        HotbarHud interfaceElement = RockstarClient.getInstance().internalMethod01271().internalMethod01440();
        return interfaceElement != null && interfaceElement.isShowing() && interfaceElement.show();
    }

    @Unique
    private boolean rockstar$customInterfaceHidesCurrentBar(Bar bar) {
        return this.rockstar$customInterfaceHidesVanillaHud()
            && (bar instanceof ExperienceBar || bar instanceof LocatorBar);
    }

    @Inject(method={"renderStatusEffectOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$hideStatusEffects(DrawContext drawContext, RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        if (RockstarClient.internalField0240.internalMethod06896()) {
            return;
        }
        if (RockstarClient.getInstance().internalMethod01271() == null) {
            return;
        }
        boolean bl = RockstarClient.getInstance().internalMethod01271().internalMethod09520().stream().filter(typedValue197 -> typedValue197 instanceof EffectsHud).anyMatch(typedValue197 -> typedValue197.isShowing() && typedValue197.show());
        if (bl) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderMiscOverlays"}, at={@At(value="TAIL")})
    private void rockstar$renderFakeFrozenOverlay(DrawContext drawContext, RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        ClientPlayerEntity clientPlayerEntity = InGameHudMixin.internalField0149.player;
        if (!(clientPlayerEntity instanceof FakeFrozenTicksAccess)) {
            return;
        }
        FakeFrozenTicksAccess fakeFrozenTicksAccess = (FakeFrozenTicksAccess)clientPlayerEntity;
        int n = fakeFrozenTicksAccess.rockstar$getFakeFrozenTicks();
        if (n <= 0) {
            return;
        }
        float f = Math.min(1.0f, (float)n / (float)InGameHudMixin.internalField0149.player.getMinFreezeDamageTicks());
        ((InGameHudAccessor)((Object)this)).rockstar$renderOverlay(drawContext, POWDER_SNOW_OUTLINE, f);
    }
}

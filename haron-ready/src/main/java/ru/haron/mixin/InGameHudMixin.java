package ru.haron.mixin;

import haron.animation.HotbarSelectionAnimation;
import haron.animation.PlayerListAnimation;
import haron.events.HudRenderPostEvent;
import haron.events.HudRenderPreEvent;
import haron.events.EventDispatcher;
import haron.gui.core.GuiInput;
import haron.hud.core.HudManager;
import haron.inventory.fh7bgv;
import haron.inventory.nzsxbq;
import haron.module.ModuleManager;
import haron.modules.utilities.Cooldowns;
import haron.modules.utilities.FastSwap;
import haron.modules.visuals.RenderTweaks;
import haron.modules.visuals.Animations;
import haron.modules.visuals.Crosshair;
import haron.render.ScaledGuiProjection;
import haron.render.ScreenPoint;
import haron.util.effe6p;
import java.util.function.Function;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={InGameHud.class})
public abstract class InGameHudMixin {
    @Shadow
    @Final
    private static Identifier HOTBAR_SELECTION_TEXTURE;
    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    @Final
    private PlayerListHud playerListHud;
    @Unique
    private int deferredHighlightY;
    @Unique
    private boolean deferHighlight = false;
    @Unique
    private boolean prevTabShown = false;
    @Unique
    private static final long FLAGS_TTL_MS = 250L;
    @Unique
    private long flagsTime = 0L;
    @Unique
    private boolean fCrosshair = false;
    @Unique
    private boolean fHotbarAnim = false;
    @Unique
    private boolean fTabAnim = false;
    @Unique
    private boolean fArmorHud = false;
    @Unique
    private boolean fHideScoreboard = false;
    @Unique
    private boolean fFastSwapBinds = false;
    @Unique
    private boolean fCooldowns = false;

    @Unique
    private void refreshFlags() {
        long now = System.currentTimeMillis();
        if (now - this.flagsTime < 250L) {
            return;
        }
        this.flagsTime = now;
        try {
            Crosshair crosshair = ModuleManager.CROSSHAIR;
            this.fCrosshair = crosshair.k();
            Animations animations = ModuleManager.ANIMATIONS;
            boolean animOn = animations.k();
            this.fHotbarAnim = animOn && animations.hotbarSelectorEnabled.a();
            this.fTabAnim = animOn && animations.playerListEnabled.a();
            this.fArmorHud = ModuleManager.ARMOR_HUD.k();
            RenderTweaks renderTweaks = ModuleManager.RENDER_TWEAKS;
            this.fHideScoreboard = renderTweaks.r();
            FastSwap fastSwap = ModuleManager.FAST_SWAP;
            this.fFastSwapBinds = fastSwap.k() && (Boolean)fastSwap.E().k() != false;
            Cooldowns cooldowns = ModuleManager.COOLDOWNS;
            this.fCooldowns = cooldowns.k();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    @Shadow
    private void renderHotbarItem(DrawContext DrawContextVar, int i, int i2, RenderTickCounter RenderTickCounterVar, PlayerEntity PlayerEntityVar, ItemStack ItemStackVar, int i3) {
    }

    @Inject(method={"renderCrosshair"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderCrosshair(DrawContext DrawContextVar, RenderTickCounter RenderTickCounterVar, CallbackInfo callbackInfo) {
        this.refreshFlags();
        if (this.fCrosshair) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderHotbar"}, at={@At(value="HEAD")})
    private void onRenderHotbarHead(DrawContext DrawContextVar, RenderTickCounter RenderTickCounterVar, CallbackInfo callbackInfo) {
        this.refreshFlags();
        if (!this.fHotbarAnim || this.client.player == null) {
            return;
        }
        HotbarSelectionAnimation hotbarSelectionAnimationP = Animations.hotbarSelectionAnimation();
        hotbarSelectionAnimationP.a(this.client.player.getInventory().selectedSlot, DrawContextVar.getScaledWindowWidth() / 2, (long)ModuleManager.ANIMATIONS.hotbarDuration.get());
        hotbarSelectionAnimationP.a();
    }

    @Redirect(method={"renderHotbar"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V", ordinal=1))
    private void redirectHotbarSelection(DrawContext DrawContextVar, Function<Identifier, RenderLayer> function, Identifier IdentifierVar, int i, int i2, int i3, int i4) {
        if (!this.fHotbarAnim) {
            DrawContextVar.drawGuiTexture(function, IdentifierVar, i, i2, i3, i4);
        } else {
            this.deferredHighlightY = i2;
            this.deferHighlight = true;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Inject(method={"renderHotbar"}, at={@At(value="RETURN")})
    private void renderDeferredHighlight(DrawContext DrawContextVar, RenderTickCounter RenderTickCounterVar, CallbackInfo callbackInfo) {
        if (this.deferHighlight) {
            this.deferHighlight = false;
            int iRound = Math.round(Animations.hotbarSelectionAnimation().b());
            DrawContextVar.draw();
            MatrixStack MatrixStackVarGetMatrices = DrawContextVar.getMatrices();
            MatrixStackVarGetMatrices.push();
            try {
                MatrixStackVarGetMatrices.translate(0.0f, 0.0f, 200.0f);
                DrawContextVar.drawGuiTexture(RenderLayer::getGuiTextured, HOTBAR_SELECTION_TEXTURE, iRound, this.deferredHighlightY, 24, 23);
            }
            finally {
                MatrixStackVarGetMatrices.pop();
            }
        }
    }

    @Inject(method={"renderHotbar"}, at={@At(value="RETURN")})
    private void renderArmorHud(DrawContext DrawContextVar, RenderTickCounter RenderTickCounterVar, CallbackInfo callbackInfo) {
        ClientPlayerEntity ClientPlayerEntityVar;
        if (!this.fArmorHud || this.client.player == null || (ClientPlayerEntityVar = this.client.player) == null) {
            return;
        }
        int iGetScaledWindowWidth = DrawContextVar.getScaledWindowWidth() / 2;
        int i = ClientPlayerEntityVar.getMainArm().getOpposite() != Arm.RIGHT || ClientPlayerEntityVar.getOffHandStack().isEmpty() ? 0 : 24;
        for (int i2 = 0; i2 < 4; ++i2) {
            this.renderHotbarItem(DrawContextVar, iGetScaledWindowWidth + 92 + 10 + i2 * 20 + 2 + i, DrawContextVar.getScaledWindowHeight() - 16 - 3, RenderTickCounterVar, (PlayerEntity)ClientPlayerEntityVar, ClientPlayerEntityVar.getInventory().getArmorStack(3 - i2), i2 + 1);
        }
    }

    @Inject(method={"renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderScoreboardSidebar(DrawContext DrawContextVar, ScoreboardObjective ScoreboardObjectiveVar, CallbackInfo callbackInfo) {
        if (this.fHideScoreboard) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void onRenderPre(DrawContext DrawContextVar, RenderTickCounter RenderTickCounterVar, CallbackInfo callbackInfo) {
        ScreenPoint screenPoint = ScaledGuiProjection.a((int)GuiInput.e(), (int)GuiInput.f());
        EventDispatcher.EVENT_BUS.post((Object)new HudRenderPreEvent(DrawContextVar.getMatrices(), screenPoint.x(), screenPoint.y(), RenderTickCounterVar.getTickDelta(true)));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Inject(method={"render"}, at={@At(value="TAIL")})
    private void onRender(DrawContext DrawContextVar, RenderTickCounter RenderTickCounterVar, CallbackInfo callbackInfo) {
        try {
            ScreenPoint screenPoint = ScaledGuiProjection.a((int)GuiInput.e(), (int)GuiInput.f());
            ScaledGuiProjection.a(2.0);
            try {
                EventDispatcher.EVENT_BUS.post((Object)new HudRenderPostEvent(DrawContextVar.getMatrices(), screenPoint.x(), screenPoint.y(), RenderTickCounterVar.getTickDelta(true)));
            }
            finally {
                ScaledGuiProjection.a();
            }
            HudManager.a().a(DrawContextVar.getMatrices());
            HudManager.a().b(DrawContextVar.getMatrices());
        }
        catch (Throwable t) {
            LogManager.getLogger((String)"haron").error("[HARON] Exception in HUD render", t);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Inject(method={"renderPlayerList"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderPlayerList(DrawContext DrawContextVar, RenderTickCounter RenderTickCounterVar, CallbackInfo callbackInfo) {
        if (!this.fTabAnim) {
            this.prevTabShown = false;
            return;
        }
        callbackInfo.cancel();
        if (this.client.world == null || this.client.player == null) {
            return;
        }
        Scoreboard ScoreboardVarGetScoreboard = this.client.world.getScoreboard();
        ScoreboardObjective ScoreboardObjectiveVarGetObjectiveForSlot = ScoreboardVarGetScoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.LIST);
        boolean z = this.client.options.playerListKey.isPressed() && (!this.client.isInSingleplayer() || this.client.player.networkHandler.getListedPlayerListEntries().size() > 1 || ScoreboardObjectiveVarGetObjectiveForSlot != null);
        PlayerListAnimation playerListScaleAnimationN = Animations.playerListAnimation();
        long jFloatValue = (long)ModuleManager.ANIMATIONS.playerListDuration.get();
        if (z && !this.prevTabShown) {
            playerListScaleAnimationN.a(jFloatValue);
        } else if (!z && this.prevTabShown) {
            playerListScaleAnimationN.b(jFloatValue);
        }
        this.prevTabShown = z;
        playerListScaleAnimationN.a();
        if (!z && !playerListScaleAnimationN.d()) {
            this.playerListHud.setVisible(false);
            return;
        }
        this.playerListHud.setVisible(true);
        float fB = playerListScaleAnimationN.b();
        if (fB < 0.001f) {
            this.playerListHud.setVisible(false);
            return;
        }
        MatrixStack MatrixStackVarGetMatrices = DrawContextVar.getMatrices();
        MatrixStackVarGetMatrices.push();
        try {
            float fGetScaledWindowWidth = (float)DrawContextVar.getScaledWindowWidth() / 2.0f;
            MatrixStackVarGetMatrices.translate(fGetScaledWindowWidth, 0.0f, 0.0f);
            MatrixStackVarGetMatrices.scale(fB, fB, 1.0f);
            MatrixStackVarGetMatrices.translate(-fGetScaledWindowWidth, 0.0f, 0.0f);
            this.playerListHud.render(DrawContextVar, DrawContextVar.getScaledWindowWidth(), ScoreboardVarGetScoreboard, ScoreboardObjectiveVarGetObjectiveForSlot);
        }
        finally {
            MatrixStackVarGetMatrices.pop();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Inject(method={"renderHotbar"}, at={@At(value="TAIL")})
    private void renderHotbarItemBinds(DrawContext DrawContextVar, RenderTickCounter RenderTickCounterVar, CallbackInfo callbackInfo) {
        if (!this.fFastSwapBinds && !this.fCooldowns) {
            return;
        }
        MinecraftClient MinecraftClientVarGetInstance = MinecraftClient.getInstance();
        ClientPlayerEntity ClientPlayerEntityVar = MinecraftClientVarGetInstance.player;
        if (ClientPlayerEntityVar != null) {
            FastSwap fastSwap = ModuleManager.FAST_SWAP;
            boolean z = this.fFastSwapBinds;
            Cooldowns cooldowns = ModuleManager.COOLDOWNS;
            boolean z2 = this.fCooldowns;
            TextRenderer TextRendererVar = MinecraftClientVarGetInstance.textRenderer;
            int iGetScaledWindowWidth = DrawContextVar.getScaledWindowWidth();
            int iGetScaledWindowHeight = DrawContextVar.getScaledWindowHeight();
            for (int i2 = 0; i2 < 9; ++i2) {
                int i;
                int iCeil;
                boolean z4;
                ItemStack ItemStackVarGetStack = ClientPlayerEntityVar.getInventory().getStack(i2);
                if (ItemStackVarGetStack.isEmpty()) continue;
                int key = z ? this.getKey(ItemStackVarGetStack, fastSwap) : -1;
                boolean z3 = z2 && this.hasAnyCooldown(ItemStackVarGetStack, cooldowns);
                boolean bl = z4 = key != -1 && key != 0;
                if (!z3 && !z4) continue;
                int i3 = iGetScaledWindowWidth / 2 - 91 + i2 * 20 + 2;
                int i4 = iGetScaledWindowHeight - 16 - 3;
                if (z4 && !z3 && z) {
                    String strA = effe6p.a(key);
                    float fMin = Math.min(1.0f, 14.0f / (float)TextRendererVar.getWidth(strA)) * ((Float)fastSwap.F().k()).floatValue();
                    MatrixStack MatrixStackVarGetMatrices = DrawContextVar.getMatrices();
                    MatrixStackVarGetMatrices.push();
                    try {
                        MatrixStackVarGetMatrices.translate(0.0f, 0.0f, 400.0f);
                        MatrixStackVarGetMatrices.scale(fMin, fMin, 1.0f);
                        DrawContextVar.drawText(TextRendererVar, strA, (int)(((float)i3 + 2.0f) / fMin), (int)(((float)i4 + 1.0f) / fMin), 0xFFFFFF, true);
                    }
                    finally {
                        MatrixStackVarGetMatrices.pop();
                    }
                }
                if (!z3) continue;
                nzsxbq itemCooldownSnapshotA = fh7bgv.a(ClientPlayerEntityVar.getItemCooldownManager(), ItemStackVarGetStack.getItem());
                if (Cooldowns.a(ItemStackVarGetStack) && cooldowns.b(ItemStackVarGetStack.getItem())) {
                    float fA = cooldowns.a(ItemStackVarGetStack.getItem());
                    iCeil = (int)Math.ceil((double)fA * 18.5);
                    i = fA <= 0.33f ? 65280 : (fA <= 0.66f ? 0xFFFF00 : 0xFF0000);
                } else {
                    iCeil = (int)Math.ceil(itemCooldownSnapshotA.b());
                    float fC = itemCooldownSnapshotA.c();
                    i = fC <= 0.33f ? 65280 : (fC <= 0.66f ? 0xFFFF00 : 0xFF0000);
                }
                String strValueOf = String.valueOf(iCeil);
                float fMin2 = Math.min(1.0f, 14.0f / (float)TextRendererVar.getWidth(strValueOf)) * ((Float)cooldowns.q().k()).floatValue();
                MatrixStack MatrixStackVarMethod_514482 = DrawContextVar.getMatrices();
                MatrixStackVarMethod_514482.push();
                try {
                    MatrixStackVarMethod_514482.translate(0.0f, 0.0f, 400.0f);
                    MatrixStackVarMethod_514482.scale(fMin2, fMin2, 1.0f);
                    DrawContextVar.drawText(TextRendererVar, strValueOf, (int)(((float)i3 + 2.0f) / fMin2), (int)(((float)i4 + 1.0f) / fMin2), i, true);
                    continue;
                }
                finally {
                    MatrixStackVarMethod_514482.pop();
                }
            }
        }
    }

    @Unique
    private boolean hasAnyCooldown(ItemStack ItemStackVar, Cooldowns cooldowns) {
        return fh7bgv.a(MinecraftClient.getInstance().player.getItemCooldownManager(), ItemStackVar.getItem()).a() || Cooldowns.a(ItemStackVar) && cooldowns.b(ItemStackVar.getItem());
    }

    @Unique
    private int getKey(ItemStack ItemStackVar, FastSwap fastSwap) {
        if (fastSwap == null) {
            return -1;
        }
        Item ItemVarGetItem = ItemStackVar.getItem();
        if (ItemVarGetItem == Items.NETHERITE_SCRAP) {
            return this.getKeyOrDefault(fastSwap.q().a());
        }
        if (ItemVarGetItem == Items.DRIED_KELP) {
            return this.getKeyOrDefault(fastSwap.r().a());
        }
        if (ItemVarGetItem == Items.ENDER_EYE) {
            int iA = fastSwap.s().a();
            return iA > 0 ? iA : this.getKeyOrDefault(fastSwap.y().a());
        }
        if (ItemVarGetItem == Items.SUGAR) {
            return this.getKeyOrDefault(fastSwap.t().a());
        }
        if (ItemVarGetItem == Items.CHORUS_FRUIT) {
            return this.getKeyOrDefault(fastSwap.n().a());
        }
        if (ItemVarGetItem == Items.ENDER_PEARL) {
            return this.getKeyOrDefault(fastSwap.o().a());
        }
        if (ItemVarGetItem == Items.POTION && fastSwap.a(ItemStackVar)) {
            return this.getKeyOrDefault(fastSwap.p().a());
        }
        if (ItemVarGetItem == Items.NETHER_STAR) {
            return this.getKeyOrDefault(fastSwap.u().a());
        }
        if (ItemVarGetItem == Items.SLIME_BALL) {
            return this.getKeyOrDefault(fastSwap.v().a());
        }
        if (ItemVarGetItem == Items.TURTLE_SCUTE) {
            return this.getKeyOrDefault(fastSwap.w().a());
        }
        if (ItemVarGetItem == Items.COBWEB) {
            return this.getKeyOrDefault(fastSwap.x().a());
        }
        if (ItemVarGetItem == Items.FIREWORK_STAR) {
            return this.getKeyOrDefault(fastSwap.z().a());
        }
        return -1;
    }

    @Unique
    private int getKeyOrDefault(int i) {
        if (i <= 0) {
            return -1;
        }
        return i;
    }
}

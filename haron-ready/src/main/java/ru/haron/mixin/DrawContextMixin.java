package ru.haron.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.events.ItemTooltipEvent;
import haron.events.EventDispatcher;
import haron.inventory.fh7bgv;
import haron.module.ModuleManager;
import haron.modules.utilities.HealingHelper;
import haron.modules.utilities.AutoReissue;
import haron.modules.utilities.Cooldowns;
import haron.modules.utilities.ItemHighlighter;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={DrawContext.class})
public abstract class DrawContextMixin {
    @Shadow
    @Final
    private MatrixStack matrices;
    @Shadow
    @Final
    private MinecraftClient client;
    @Unique
    private static final long FLAGS_TTL_MS = 250L;
    @Unique
    private static volatile long flagsTime = 0L;
    @Unique
    private static volatile boolean fHighlighter = false;
    @Unique
    private static volatile boolean fCooldowns = false;
    @Unique
    private static volatile boolean fAutoReissue = false;
    @Unique
    private static volatile boolean fHealing = false;
    @Unique
    private static volatile int healingMaxPriority = 1;
    @Unique
    private static final int WHITE_HALF = new Color(255, 255, 255, 127).getRGB();

    @Shadow
    public abstract int drawText(TextRenderer var1, @Nullable String var2, int var3, int var4, int var5, boolean var6);

    @Shadow
    public abstract void fill(RenderLayer var1, int var2, int var3, int var4, int var5, int var6);

    @Shadow
    public abstract void fill(RenderLayer var1, int var2, int var3, int var4, int var5, int var6, int var7);

    @Unique
    private static void haron$refreshFlags() {
        long now = System.currentTimeMillis();
        if (now - flagsTime < 250L) {
            return;
        }
        flagsTime = now;
        try {
            ItemHighlighter itemHighlighter = ModuleManager.ITEM_HIGHLIGHTER;
            fHighlighter = itemHighlighter.k();
            Cooldowns cooldowns = ModuleManager.COOLDOWNS;
            fCooldowns = cooldowns.k();
            AutoReissue autoReissue = ModuleManager.AUTO_REISSUE;
            fAutoReissue = autoReissue.k();
            HealingHelper healingHelper = ModuleManager.HEALING_HELPER;
            boolean bl = fHealing = healingHelper.k();
            if (fHealing) {
                int max = 1;
                for (Item item : healingHelper.n()) {
                    int p = healingHelper.a(item);
                    if (p <= max) continue;
                    max = p;
                }
                healingMaxPriority = max;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    @Inject(method={"drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/util/math/MatrixStack;push()V", shift=At.Shift.AFTER)})
    private void renderItemHighlight(LivingEntity LivingEntityVar, World WorldVar, ItemStack ItemStackVar, int i, int i2, int i3, int i4, CallbackInfo callbackInfo) {
        DrawContextMixin.haron$refreshFlags();
        if (!fHighlighter) {
            return;
        }
        ItemHighlighter itemHighlighter = ModuleManager.ITEM_HIGHLIGHTER;
        if (itemHighlighter.a(ItemStackVar)) {
            Color colorA = itemHighlighter.a(ItemStackVar.getItem());
            int rgb = (itemHighlighter.n() & 0xFF) << 24 | colorA.getRGB() & 0xFFFFFF;
            this.matrices.push();
            this.matrices.translate(0.0f, 0.0f, 100.0f);
            this.fill(RenderLayer.getGuiOverlay(), i, i2, i + 16, i2 + 16, rgb);
            this.matrices.pop();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Inject(method={"drawStackOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void onDrawStackOverlay(TextRenderer TextRendererVar, ItemStack ItemStackVar, int i, int i2, @Nullable String str, CallbackInfo callbackInfo) {
        if (ItemStackVar.isEmpty()) {
            return;
        }
        DrawContextMixin.haron$refreshFlags();
        this.matrices.push();
        try {
            float fA;
            if (ItemStackVar.getCount() != 1 || str != null) {
                String strValueOf = str == null ? String.valueOf(ItemStackVar.getCount()) : str;
                this.matrices.translate(0.0f, 0.0f, 200.0f);
                this.drawText(TextRendererVar, strValueOf, i + 19 - 2 - TextRendererVar.getWidth(strValueOf), i2 + 6 + 3, -1, true);
            }
            if (ItemStackVar.isItemBarVisible()) {
                int iGetItemBarStep = ItemStackVar.getItemBarStep();
                int iGetItemBarColor = ItemStackVar.getItemBarColor();
                int i3 = i + 2;
                int i4 = i2 + 13;
                this.fill(RenderLayer.getGui(), i3, i4, i3 + 13, i4 + 2, 200, -16777216);
                this.fill(RenderLayer.getGui(), i3, i4, i3 + iGetItemBarStep, i4 + 1, 200, ColorHelper.fullAlpha((int)iGetItemBarColor));
            }
            ClientPlayerEntity ClientPlayerEntityVar = MinecraftClient.getInstance().player;
            Item ItemVarGetItem = ItemStackVar.getItem();
            float fC = 0.0f;
            if (ClientPlayerEntityVar != null) {
                fC = fh7bgv.a(ClientPlayerEntityVar.getItemCooldownManager(), ItemVarGetItem).c();
            }
            Cooldowns cooldowns = fCooldowns ? ModuleManager.COOLDOWNS : null;
            boolean zA = Cooldowns.a(ItemStackVar);
            if (cooldowns != null && zA && (fA = cooldowns.a(ItemStackVar.getItem())) > fC) {
                fC = fA;
            }
            if (fC > 0.0f) {
                boolean z = true;
                if (ItemStackVar.getItem() instanceof PotionItem) {
                    z = zA;
                }
                if (z) {
                    RenderSystem.disableDepthTest();
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    int iCeil = MathHelper.ceil((float)(16.0f * fC));
                    int iFloor = i2 + MathHelper.floor((float)(16.0f * (1.0f - fC)));
                    this.fill(RenderLayer.getGui(), i, iFloor, i + 16, iFloor + iCeil, 200, WHITE_HALF);
                    RenderSystem.enableDepthTest();
                }
            }
            if (fAutoReissue && ItemStackVar.getItem() == Items.CLOCK) {
                AutoReissue autoReissue = ModuleManager.AUTO_REISSUE;
                MinecraftClient MinecraftClientVarGetInstance = MinecraftClient.getInstance();
                if (MinecraftClientVarGetInstance.currentScreen != null && MinecraftClientVarGetInstance.player.currentScreenHandler != null && autoReissue.overlayActive) {
                    long j = autoReissue.timer.a(0L) ? (long)autoReissue.durationMs : 0L;
                    for (int i5 = 1; i5 <= autoReissue.durationMs / 1000 + 1; ++i5) {
                        if (autoReissue.timer.a(i5 * 1000)) continue;
                        j = (i5 - 1) * 1000;
                        break;
                    }
                    int iMax = Math.max(0, (autoReissue.durationMs - (int)j) / 1000);
                    float fMax = Math.max(0.0f, 1.0f - (float)(j / (long)autoReissue.durationMs));
                    if (iMax > 0) {
                        int iMethod_153862 = MathHelper.ceil((float)(16.0f * fMax));
                        int i6 = i2 + (16 - iMethod_153862);
                        this.fill(RenderLayer.getGui(), i, i6, i + 16, i6 + iMethod_153862, 200, WHITE_HALF);
                        String strValueOf2 = String.valueOf(iMax);
                        this.matrices.push();
                        this.matrices.translate(0.0f, 0.0f, 300.0f);
                        this.drawText(TextRendererVar, strValueOf2, i + 8 - TextRendererVar.getWidth(strValueOf2) / 2, i2 + 8 - 4, -1, true);
                        this.matrices.pop();
                    }
                }
            }
            if (fHealing) {
                HealingHelper healingHelper = ModuleManager.HEALING_HELPER;
                if (healingHelper.a(ItemStackVar)) {
                    float fClamp = MathHelper.clamp((float)((float)healingHelper.a(ItemStackVar.getItem()) / (float)healingMaxPriority), (float)0.0f, (float)1.0f);
                    int alpha = (int)((Math.sin((double)System.currentTimeMillis() / 100.0) * 0.5 + 0.5) * 100.0) + 50;
                    int rgb = (alpha & 0xFF) << 24 | ((int)(255.0f * (1.0f - fClamp)) & 0xFF) << 16 | ((int)(255.0f * fClamp) & 0xFF) << 8;
                    this.matrices.push();
                    this.matrices.translate(0.0f, 0.0f, 200.0f);
                    this.fill(RenderLayer.getGuiOverlay(), i, i2, i + 16, i2 + 16, rgb);
                    this.matrices.pop();
                }
            }
            EventDispatcher.EVENT_BUS.post((Object)new ItemTooltipEvent(ItemStackVar, i, i2));
        }
        finally {
            this.matrices.pop();
        }
        callbackInfo.cancel();
    }
}


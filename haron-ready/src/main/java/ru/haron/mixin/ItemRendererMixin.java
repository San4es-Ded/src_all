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
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={ItemRenderer.class})
public abstract class ItemRendererMixin {
    public void renderGuiItemOverlay(DrawContext DrawContextVar, TextRenderer TextRendererVar, ItemStack ItemStackVar, int i, int i2) {
        this.renderGuiItemOverlay(DrawContextVar, TextRendererVar, ItemStackVar, i, i2, null);
    }

    public void renderGuiItemOverlay(DrawContext DrawContextVar, TextRenderer TextRendererVar, ItemStack ItemStackVar, int i, int i2, @Nullable String str) {
        float fA;
        if (ItemStackVar.isEmpty()) {
            return;
        }
        Item ItemVarGetItem = ItemStackVar.getItem();
        ItemHighlighter itemHighlighter = ModuleManager.ITEM_HIGHLIGHTER;
        if (itemHighlighter.k() && itemHighlighter.a(ItemStackVar)) {
            Color colorB = itemHighlighter.b(ItemStackVar);
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            this.renderGuiQuad(Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR), i, i2, 16, 16, colorB.getRed(), colorB.getGreen(), colorB.getBlue(), itemHighlighter.n());
            RenderSystem.enableDepthTest();
        }
        if (ItemStackVar.getCount() != 1 || str != null) {
            String strValueOf = str == null ? String.valueOf(ItemStackVar.getCount()) : str;
            DrawContextVar.drawText(TextRendererVar, strValueOf, i + 19 - 2 - TextRendererVar.getWidth(strValueOf), i2 + 6 + 3, 0xFFFFFF, true);
        }
        if (ItemStackVar.isDamaged()) {
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            BufferBuilder BufferBuilderVarBegin = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            float fGetDamage = ItemStackVar.getDamage();
            float fGetMaxDamage = ItemStackVar.getMaxDamage();
            int iRound = Math.round(13.0f - fGetDamage * 13.0f / fGetMaxDamage);
            int iHsvToRgb = MathHelper.hsvToRgb((float)(Math.max(0.0f, (fGetMaxDamage - fGetDamage) / fGetMaxDamage) / 3.0f), (float)1.0f, (float)1.0f);
            this.renderGuiQuad(BufferBuilderVarBegin, i + 2, i2 + 13, 13, 2, 0, 0, 0, 255);
            this.renderGuiQuad(BufferBuilderVarBegin, i + 2, i2 + 13, iRound, 1, iHsvToRgb >> 16 & 0xFF, iHsvToRgb >> 8 & 0xFF, iHsvToRgb & 0xFF, 255);
            RenderSystem.enableDepthTest();
        }
        ClientPlayerEntity ClientPlayerEntityVar = MinecraftClient.getInstance().player;
        float fC = 0.0f;
        if (ClientPlayerEntityVar != null) {
            fC = fh7bgv.a(ClientPlayerEntityVar.getItemCooldownManager(), ItemVarGetItem).c();
        }
        Cooldowns cooldowns = ModuleManager.COOLDOWNS;
        if (cooldowns.k() && (fA = cooldowns.a(ItemVarGetItem)) > fC) {
            fC = fA;
        }
        if (fC > 0.0f) {
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            this.renderGuiQuad(Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR), i, i2 + MathHelper.floor((float)(16.0f * (1.0f - fC))), 16, MathHelper.ceil((float)(16.0f * fC)), 255, 255, 255, 127);
            RenderSystem.enableDepthTest();
        }
        HealingHelper healingHelper = ModuleManager.HEALING_HELPER;
        if (healingHelper.k() && healingHelper.a(ItemStackVar)) {
            Stream<Item> stream = healingHelper.n().stream();
            Objects.requireNonNull(healingHelper);
            int[] colorByPriority = this.getColorByPriority(healingHelper.a(ItemVarGetItem), stream.mapToInt(healingHelper::a).max().orElse(1));
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            this.renderGuiQuad(Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR), i, i2, 16, 16, colorByPriority[0], colorByPriority[1], colorByPriority[2], (int)((Math.sin((double)System.currentTimeMillis() / 100.0) * 0.5 + 0.5) * 100.0) + 50);
            RenderSystem.enableDepthTest();
        }
        if (ItemStackVar.getItem() == Items.CLOCK) {
            AutoReissue autoReissue = ModuleManager.AUTO_REISSUE;
            if (autoReissue.k()) {
                MinecraftClient MinecraftClientVarGetInstance = MinecraftClient.getInstance();
                if (MinecraftClientVarGetInstance.currentScreen != null && MinecraftClientVarGetInstance.player.currentScreenHandler != null) {
                    try {
                        MinecraftClientVarGetInstance.currentScreen.getTitle().getString();
                    }
                    catch (Exception colorByPriority) {
                        // empty catch block
                    }
                    if (autoReissue.overlayActive) {
                        long j = autoReissue.timer.a(0L) ? (long)autoReissue.durationMs : 0L;
                        for (int i3 = 1; i3 <= autoReissue.durationMs / 1000 + 1; ++i3) {
                            if (autoReissue.timer.a(i3 * 1000)) continue;
                            j = (i3 - 1) * 1000;
                            break;
                        }
                        int iMax = Math.max(0, (autoReissue.durationMs - (int)j) / 1000);
                        float fMax = Math.max(0.0f, 1.0f - (float)(j / (long)autoReissue.durationMs));
                        if (iMax > 0) {
                            RenderSystem.disableDepthTest();
                            RenderSystem.enableBlend();
                            RenderSystem.defaultBlendFunc();
                            BufferBuilder BufferBuilderVarMethod_608272 = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
                            int iCeil = MathHelper.ceil((float)(16.0f * fMax));
                            this.renderGuiQuad(BufferBuilderVarMethod_608272, i, i2 + (16 - iCeil), 16, iCeil, 255, 255, 255, 127);
                            RenderSystem.enableDepthTest();
                            String iMaxStr = String.valueOf(iMax);
                            DrawContextVar.drawText(TextRendererVar, iMaxStr, (int)((float)(i + 8) - (float)TextRendererVar.getWidth(iMaxStr) / 2.0f), (int)((float)(i2 + 8) - 4.5f), 0xFFFFFF, true);
                        }
                    }
                }
            }
        }
        EventDispatcher.EVENT_BUS.post((Object)new ItemTooltipEvent(ItemStackVar, i, i2));
    }

    private void renderGuiQuad(BufferBuilder BufferBuilderVar, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        BufferBuilderVar.vertex((float)(i + 0), (float)(i2 + 0), 0.0f).color(i5, i6, i7, i8);
        BufferBuilderVar.vertex((float)(i + 0), (float)(i2 + i4), 0.0f).color(i5, i6, i7, i8);
        BufferBuilderVar.vertex((float)(i + i3), (float)(i2 + i4), 0.0f).color(i5, i6, i7, i8);
        BufferBuilderVar.vertex((float)(i + i3), (float)(i2 + 0), 0.0f).color(i5, i6, i7, i8);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)BufferBuilderVar.end());
    }

    private int[] getColorByPriority(int i, int i2) {
        float f = i / i2;
        return new int[]{(int)(255.0f * (1.0f - f)), (int)(255.0f * f), 0};
    }
}

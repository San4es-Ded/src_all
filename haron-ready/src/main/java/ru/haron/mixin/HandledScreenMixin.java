package ru.haron.mixin;

import haron.inventory.fh7bgv;
import haron.inventory.nzsxbq;
import haron.module.ModuleManager;
import haron.modules.utilities.Cooldowns;
import haron.modules.utilities.FastSwap;
import haron.modules.visuals.ShulkerPreviewRenderer;
import haron.modules.visuals.ShulkerPreview;
import haron.util.effe6p;
import haron.util.jeooat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={HandledScreen.class})
public class HandledScreenMixin {
    @Shadow
    protected int x;
    @Shadow
    protected int y;
    @Shadow
    protected int backgroundWidth;
    @Shadow
    @Nullable
    protected Slot focusedSlot;
    @Unique
    private jeooat scrollTime;

    @Unique
    private jeooat getScrollTime() {
        if (this.scrollTime == null) {
            this.scrollTime = new jeooat();
        }
        return this.scrollTime;
    }

    @Inject(method={"render"}, at={@At(value="TAIL")})
    private void renderItemBindsOverlay(DrawContext DrawContextVar, int i, int i2, float f, CallbackInfo callbackInfo) {
        HandledScreen HandledScreenVar = (HandledScreen)(Object)this;
        FastSwap fastSwap = ModuleManager.FAST_SWAP;
        boolean z = (Boolean)fastSwap.E().k() != false && fastSwap.k();
        Cooldowns cooldowns = ModuleManager.COOLDOWNS;
        TextRenderer TextRendererVar = MinecraftClient.getInstance().textRenderer;
        for (Slot SlotVar : HandledScreenVar.getScreenHandler().slots) {
            int i3;
            int iCeil;
            boolean z3;
            ItemStack ItemStackVarGetStack = SlotVar.getStack();
            if (ItemStackVarGetStack.isEmpty()) continue;
            int key = this.getKey(ItemStackVarGetStack, fastSwap);
            boolean z2 = cooldowns.k() && this.hasAnyCooldown(ItemStackVarGetStack, cooldowns);
            boolean bl = z3 = key != -1 && key != 0;
            if (!z2 && !z3) continue;
            int i4 = SlotVar.x + this.x;
            int i5 = SlotVar.y + this.y;
            if (z3 && !z2 && z) {
                MatrixStack MatrixStackVarGetMatrices = DrawContextVar.getMatrices();
                MatrixStackVarGetMatrices.push();
                MatrixStackVarGetMatrices.translate(0.0f, 0.0f, 300.0f);
                String strA = effe6p.a(key);
                float fMin = Math.min(1.0f, 14.0f / (float)TextRendererVar.getWidth(strA)) * ((Float)fastSwap.F().k()).floatValue();
                MatrixStackVarGetMatrices.scale(fMin, fMin, 1.0f);
                DrawContextVar.drawText(TextRendererVar, strA, (int)(((float)i4 + 0.5f) / fMin), (int)(((float)i5 + 0.5f) / fMin), 0xFFFFFF, true);
                MatrixStackVarGetMatrices.pop();
            }
            if (!z2) continue;
            nzsxbq itemCooldownSnapshotA = fh7bgv.a(MinecraftClient.getInstance().player.getItemCooldownManager(), ItemStackVarGetStack.getItem());
            if (Cooldowns.a(ItemStackVarGetStack) && cooldowns.b(ItemStackVarGetStack.getItem())) {
                float fA = cooldowns.a(ItemStackVarGetStack.getItem());
                iCeil = (int)Math.ceil((double)fA * 18.5);
                i3 = fA <= 0.33f ? 65280 : (fA <= 0.66f ? 0xFFFF00 : 0xFF0000);
            } else {
                iCeil = (int)Math.ceil(itemCooldownSnapshotA.b());
                float fC = itemCooldownSnapshotA.c();
                i3 = fC <= 0.33f ? 65280 : (fC <= 0.66f ? 0xFFFF00 : 0xFF0000);
            }
            int i6 = i3;
            MatrixStack MatrixStackVarMethod_514482 = DrawContextVar.getMatrices();
            MatrixStackVarMethod_514482.push();
            MatrixStackVarMethod_514482.translate(0.0f, 0.0f, 300.0f);
            String strValueOf = String.valueOf(iCeil);
            float fMin2 = Math.min(1.0f, 14.0f / (float)TextRendererVar.getWidth(strValueOf)) * ((Float)cooldowns.q().k()).floatValue();
            MatrixStackVarMethod_514482.scale(fMin2, fMin2, 1.0f);
            DrawContextVar.drawText(TextRendererVar, strValueOf, (int)(((float)i4 + 0.5f) / fMin2), (int)(((float)i5 + 0.5f) / fMin2), i6, true);
            MatrixStackVarMethod_514482.pop();
        }
        this.handleItemScroller();
    }

    @Unique
    private void handleItemScroller() {
        if (ModuleManager.ITEM_SCROLLER.k()) {
            boolean z2;
            MinecraftClient MinecraftClientVarGetInstance = MinecraftClient.getInstance();
            if (MinecraftClientVarGetInstance.player == null || MinecraftClientVarGetInstance.currentScreen == null || this.focusedSlot == null || !this.focusedSlot.hasStack() || this.focusedSlot.getStack().getItem() == Items.AIR) {
                return;
            }
            long jGetHandle = MinecraftClientVarGetInstance.getWindow().getHandle();
            boolean z = GLFW.glfwGetMouseButton((long)jGetHandle, (int)0) == 1;
            boolean bl = z2 = GLFW.glfwGetKey((long)jGetHandle, (int)340) == 1;
            if (z && z2 && this.getScrollTime().a(((Float)ModuleManager.ITEM_SCROLLER.delay.k()).longValue())) {
                MinecraftClientVarGetInstance.interactionManager.clickSlot(((HandledScreen)(Object)this).getScreenHandler().syncId, this.focusedSlot.id, 0, SlotActionType.QUICK_MOVE, (PlayerEntity)MinecraftClientVarGetInstance.player);
                this.getScrollTime().b();
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

    @Inject(method={"drawMouseoverTooltip"}, at={@At(value="HEAD")}, cancellable=true)
    private void haron$cancelShulkerTooltip(DrawContext DrawContextVar, int i, int i2, CallbackInfo callbackInfo) {
        ShulkerPreview shulkerPreview = ModuleManager.SHULKER_PREVIEW;
        if (shulkerPreview.k() && shulkerPreview.inventoryPreview.a() && this.focusedSlot != null && this.focusedSlot.hasStack() && ShulkerPreviewRenderer.isShulkerBox(this.focusedSlot.getStack())) {
            callbackInfo.cancel();
            ShulkerPreviewRenderer.renderInventoryHover(this.focusedSlot, DrawContextVar, (int)((float)this.x - 170.0f - 6.0f), this.y, true);
        }
    }
}


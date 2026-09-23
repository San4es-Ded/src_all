package haron.modules.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.module.ModuleManager;
import haron.modules.visuals.ShulkerPreview;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import ru.haron.Haron;

public final class ShulkerPreviewRenderer {
    private static final float SLOT = 18.0f;
    private static final float PADDING = 6.0f;
    private static final int COLS = 9;
    private static final int ROWS = 3;
    private static final Color SLOT_BACKGROUND = new Color(45, 25, 15, 180);

    public static void renderInventoryHover(Slot slot, DrawContext drawContext, int n, int n2) {
        ShulkerPreviewRenderer.renderInventoryHover(slot, drawContext, n, n2, false);
    }

    public static void renderInventoryHover(Slot slot, DrawContext drawContext, int n, int n2, boolean bl) {
        ShulkerPreview igawp72 = ModuleManager.SHULKER_PREVIEW;
        if (!igawp72.k() || !igawp72.inventoryPreview.a() || slot == null) {
            return;
        }
        ItemStack itemStack = slot.getStack();
        if (ShulkerPreviewRenderer.isShulkerBox(itemStack)) {
            List<ItemStack> list = ShulkerPreviewRenderer.readContents(itemStack);
            float f = 174.0f;
            float f2 = 82.0f;
            float f3 = n;
            float f4 = n2;
            if (!bl) {
                f3 = n + 14;
                f4 = n2 + 6;
                MinecraftClient minecraftClient = MinecraftClient.getInstance();
                if (f3 + 174.0f > (float)minecraftClient.getWindow().getScaledWidth()) {
                    f3 = (float)n - 174.0f - 4.0f;
                }
                if (f4 + 82.0f > (float)minecraftClient.getWindow().getScaledHeight()) {
                    f4 = (float)n2 - 82.0f - 4.0f;
                }
            }
            ShulkerPreviewRenderer.renderPanel(drawContext, f3, f4, 174.0f, 82.0f, list, itemStack, 1.0f);
        }
    }

    private static Color getAccent() {
        return pryrvd.ACCENT;
    }

    private static void drawItem(MatrixStack matrixStack, DrawContext drawContext, ItemStack itemStack, float f, float f2, float f3) {
        if (itemStack.isEmpty()) {
            return;
        }
        MatrixStack matrixStack2 = drawContext.getMatrices();
        float f4 = f3 / 16.0f;
        matrixStack2.push();
        matrixStack2.translate(f, f2, 0.0f);
        matrixStack2.scale(f4, f4, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        drawContext.drawItem(itemStack, 0, 0);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.disableBlend();
        if (itemStack.getCount() > 1) {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            String string = String.valueOf(itemStack.getCount());
            matrixStack2.push();
            matrixStack2.translate(0.0f, 0.0f, 200.0f);
            matrixStack2.scale(0.5f, 0.5f, 1.0f);
            drawContext.drawTextWithShadow(textRenderer, (Text)Text.literal((String)string), (int)((16.0f - (float)textRenderer.getWidth(string) * 0.5f) / 0.5f), 22, 0xFFFFFF);
            matrixStack2.pop();
        }
        matrixStack2.pop();
    }

    public static boolean isShulkerBox(ItemStack itemStack) {
        Item item;
        return !itemStack.isEmpty() && (item = itemStack.getItem()) instanceof BlockItem && ((BlockItem)item).getBlock() instanceof ShulkerBoxBlock;
    }

    public static void renderWorldTarget(MinecraftClient minecraftClient, MatrixStack matrixStack, ShapeRenderer s7swsm2) {
        ItemEntity itemEntity;
        ItemStack itemStack;
        EntityHitResult entityHitResult;
        Entity entity;
        ShulkerPreview igawp72 = ModuleManager.SHULKER_PREVIEW;
        if (!igawp72.k() || !igawp72.worldPreview.a() || minecraftClient.player == null) {
            return;
        }
        if (minecraftClient.crosshairTarget instanceof EntityHitResult && (entity = (entityHitResult = (EntityHitResult)minecraftClient.crosshairTarget).getEntity()) instanceof ItemEntity && ShulkerPreviewRenderer.isShulkerBox(itemStack = (itemEntity = (ItemEntity)entity).getStack()) && minecraftClient.player.squaredDistanceTo((Entity)itemEntity) <= 36.0) {
            float f = 174.0f;
            ShulkerPreviewRenderer.renderPanel(new DrawContext(minecraftClient, minecraftClient.getBufferBuilders().getEntityVertexConsumers()), (float)minecraftClient.getWindow().getScaledWidth() / 2.0f - 87.0f, (float)minecraftClient.getWindow().getScaledHeight() / 2.0f + 20.0f, 174.0f, 82.0f, ShulkerPreviewRenderer.readContents(itemStack), itemStack, 1.0f);
        }
    }

    private static void renderPanel(DrawContext drawContext, float f, float f2, float f3, float f4, List<ItemStack> list, ItemStack itemStack, float f5) {
        MatrixStack matrixStack = drawContext.getMatrices();
        ShapeRenderer s7swsm2 = Haron.getInstance().getRender();
        if (s7swsm2 == null) {
            return;
        }
        s7swsm2.a(f, f2, f3, f4, 7.0f * f5, ShulkerPreviewRenderer.getAccent(), matrixStack);
        ClientFonts.a[Math.max(6, Math.min(14, Math.round(10.0f * f5)))].a(itemStack.get(DataComponentTypes.CUSTOM_NAME) != null ? itemStack.getName().getString() : "Shulker Box", f + 6.0f * f5, (double)(f2 + 4.0f * f5), Color.WHITE, matrixStack);
        float f6 = f + 6.0f * f5;
        float f7 = f2 + 16.0f * f5;
        float f8 = 18.0f * f5;
        for (int i = 0; i < 27; ++i) {
            float f9 = f6 + (float)(i % 9) * f8;
            float f10 = f7 + (float)(i / 9) * f8;
            s7swsm2.a(f9, f10, f8 - 1.0f, f8 - 1.0f, 3.0f * f5, SLOT_BACKGROUND, matrixStack);
            if (i >= list.size() || list.get(i).isEmpty()) continue;
            ShulkerPreviewRenderer.drawItem(matrixStack, drawContext, list.get(i), f9 + 1.0f * f5, f10 + 1.0f * f5, f8 - 2.0f * f5);
        }
    }

    private static List<ItemStack> readContents(ItemStack itemStack) {
        int n;
        ArrayList<ItemStack> arrayList = new ArrayList<ItemStack>();
        ContainerComponent containerComponent = (ContainerComponent)itemStack.get(DataComponentTypes.CONTAINER);
        if (containerComponent == null) {
            return arrayList;
        }
        for (n = 0; n < 27; ++n) {
            arrayList.add(ItemStack.EMPTY);
        }
        n = 0;
        for (ItemStack itemStack2 : containerComponent.iterateNonEmpty()) {
            if (n >= 27) continue;
            int n2 = n++;
            arrayList.set(n2, itemStack2.copy());
        }
        return arrayList;
    }

    private ShulkerPreviewRenderer() {
    }
}


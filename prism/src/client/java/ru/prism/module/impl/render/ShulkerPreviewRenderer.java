package ru.prism.module.impl.render;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.hit.EntityHitResult;
import ru.prism.manager.event_impl.EventDisplay;
import ru.prism.mixin.HandledScreenAccessor;
import ru.prism.utils.colors.ColorUtil;
import ru.prism.utils.render.Render2D;
import ru.prism.utils.render.RenderUtil;
import ru.prism.utils.render.font.Fonts;

import java.util.ArrayList;
import java.util.List;

public final class ShulkerPreviewRenderer {

    private static final float PANEL_WIDTH = 174F;
    private static final float PANEL_HEIGHT = 82F;
    private static final float SLOT_SIZE = 18F;

    private ShulkerPreviewRenderer() {
    }

    public static boolean hasInventoryPreview() {
        ShulkerPreview module = ShulkerPreview.getInstance();
        if (module == null || !module.isEnabled() || !module.inventoryPreview.getValue()) return false;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (!(mc.currentScreen instanceof HandledScreen<?> screen)) return false;

        Slot slot = ((HandledScreenAccessor) screen).getFocusedSlot();
        return slot != null && slot.hasStack() && isShulkerBox(slot.getStack());
    }

    public static void renderWorldTarget(EventDisplay event) {
        ShulkerPreview module = ShulkerPreview.getInstance();
        if (module == null || !module.isEnabled() || !module.worldPreview.getValue()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if (!(mc.crosshairTarget instanceof EntityHitResult hit) || !(hit.getEntity() instanceof ItemEntity itemEntity)) return;

        ItemStack stack = itemEntity.getStack();
        if (!isShulkerBox(stack) || mc.player.squaredDistanceTo(itemEntity) > 36.0) return;

        float scaleFix = 2F / (float) mc.getWindow().getScaleFactor();
        float screenWidth = mc.getWindow().getScaledWidth() / scaleFix;
        float screenHeight = mc.getWindow().getScaledHeight() / scaleFix;

        drawPanel(event.getDrawContext(), screenWidth / 2F - PANEL_WIDTH / 2F, screenHeight / 2F + 20F,
                readContents(stack), stack, scaleFix);
    }

    public static void renderInventoryHover(DrawContext context) {
        if (!hasInventoryPreview()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        HandledScreenAccessor accessor = (HandledScreenAccessor) mc.currentScreen;
        Slot slot = accessor.getFocusedSlot();
        ItemStack stack = slot.getStack();

        float scaleFix = 2F / (float) mc.getWindow().getScaleFactor();
        float panelX = (accessor.getScreenX() + accessor.getBackgroundWidth() + 6) / scaleFix;
        float panelY = accessor.getScreenY() / scaleFix;

        Render2D.beginOverlay();
        drawPanel(context, panelX, panelY, readContents(stack), stack, scaleFix);
        Render2D.endOverlay();
    }

    private static void drawPanel(DrawContext context, float x, float y, List<ItemStack> contents, ItemStack stack, float scaleFix) {
        RenderUtil.Render2D.rect(x, y, PANEL_WIDTH, PANEL_HEIGHT, ColorUtil.getColor(12, 12, 22, 220), 7F);
        RenderUtil.Render2D.outline(x, y, PANEL_WIDTH, PANEL_HEIGHT, 0.5F,
                ColorUtil.replAlpha(ColorUtil.client(), 0.35F), 7F);

        String title = stack.get(DataComponentTypes.CUSTOM_NAME) != null ? stack.getName().getString() : "Шалкер";
        Fonts.sf_regular.draw(title, x + 6F, y + 4.5F, 6.5F, ColorUtil.getColor(255, 255, 255, 235));

        for (int i = 0; i < 27; i++) {
            float slotX = x + 6F + (i % 9) * SLOT_SIZE;
            float slotY = y + 16F + (i / 9) * SLOT_SIZE;
            RenderUtil.Render2D.rect(slotX, slotY, SLOT_SIZE - 1F, SLOT_SIZE - 1F,
                    ColorUtil.getColor(24, 24, 36, 180), 3F);

            ItemStack content = contents.get(i);
            if (!content.isEmpty()) {
                drawItem(context, content, slotX + (SLOT_SIZE - 1F) / 2F, slotY + (SLOT_SIZE - 1F) / 2F, scaleFix);
            }
        }
    }

    private static void drawItem(DrawContext context, ItemStack stack, float centerX, float centerY, float scaleFix) {
        var matrices = context.getMatrices();
        matrices.pushMatrix();
        matrices.translate(centerX * scaleFix, centerY * scaleFix);
        matrices.scale(scaleFix, scaleFix);
        context.drawItem(stack, -8, -8);
        context.drawStackOverlay(MinecraftClient.getInstance().textRenderer, stack, -8, -8);
        matrices.popMatrix();
    }

    public static boolean isShulkerBox(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Item item = stack.getItem();
        return item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock;
    }

    private static List<ItemStack> readContents(ItemStack stack) {
        List<ItemStack> contents = new ArrayList<>(27);
        for (int i = 0; i < 27; i++) {
            contents.add(ItemStack.EMPTY);
        }

        ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
        if (container == null) return contents;

        int index = 0;
        for (ItemStack item : container.iterateNonEmpty()) {
            if (index >= 27) break;
            contents.set(index++, item.copy());
        }

        return contents;
    }
}

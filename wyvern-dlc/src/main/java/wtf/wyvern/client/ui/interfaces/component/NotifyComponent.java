package wtf.wyvern.client.ui.interfaces.component;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.core.font.Font;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.core.theme.Theme;
import wtf.wyvern.client.ui.interfaces.draggable.DraggableHudElement;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.ChatScreen;

public class NotifyComponent extends DraggableHudElement {
    private final Animation toggleAnimation;
    private final List<BaseNotification> notifications;
    private final TextNotification testNotification;

    public NotifyComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight, float offsetX, float offsetY, Align align) {
        super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
        this.toggleAnimation = new Animation(200L, Easing.CUBIC_OUT);
        this.notifications = new ArrayList<>();
        this.testNotification = new TextNotification("SD", Text.literal("Тестовое уведомление"));
    }

    public void addNotification(Module module, boolean enabled) {
        this.notifications.addLast(new ModuleNotification(module, enabled));
    }

    public void addTextNotification(String icon, Text text) {
        this.notifications.addLast(new TextNotification(icon, text));
    }

    public void addTotemNotification(String name, boolean enchanted) {
        this.notifications.addLast(new TotemNotification(name, enchanted));
    }

    public void addItemNotification(String itemName, String icon) {
        this.notifications.addLast(new ItemNotification(itemName, icon));
    }

    /**
     * Уведомление с настоящей иконкой предмета (зелья, глинт от чар и т.п.
     * рисуются самим майном, поэтому подходит для любого стака).
     */
    public void addUseNotification(ItemStack stack, Text text) {
        this.notifications.addLast(new UseNotification(stack.copy(), text));
    }

    @Override
    public void render(CustomDrawContext ctx) {
        Iterator<BaseNotification> iterator = this.notifications.iterator();
        while (iterator.hasNext()) {
            BaseNotification notification = iterator.next();
            if (!notification.fadingOut && System.currentTimeMillis() - notification.timestamp > 2500L) {
                notification.fadingOut = true;
                notification.alphaAnimation.update(0.0F);
            }

            if (notification.fadingOut && notification.alphaAnimation.getValue() < 0.01F) {
                iterator.remove();
            } else {
                notification.alphaAnimation.update(notification.fadingOut ? 0.0F : 1.0F);
            }
        }

        boolean editing = mc.currentScreen instanceof ChatScreen;
        this.toggleAnimation.update(editing || !this.notifications.isEmpty());
        Theme theme = Wyvern.getInstance().getThemeManager().getCurrentTheme();
        Font textFont = Fonts.MEDIUM.getFont(6.2F);
        float notificationHeight = 17.0F;

        // New notifications stay at the fixed top anchor. Older entries move
        // down and expire at the bottom, so removing one can no longer pull the
        // whole stack upward.
        List<BaseNotification> visible = new ArrayList<>(this.notifications);
        Collections.reverse(visible);
        if (editing) {
            if (this.testNotification.fadingOut) {
                this.testNotification.alphaAnimation.setValue(0.0F);
            }
            this.testNotification.fadingOut = false;
            this.testNotification.alphaAnimation.update(1.0F);
            visible.addFirst(this.testNotification);
        }

        float maxWidth = 0.0F;
        for (BaseNotification notification : visible) {
            maxWidth = Math.max(maxWidth, notification.getWidth(textFont, notificationHeight));
        }

        // Keep hitbox size stable so CENTER/BOTTOM align and screen clamping
        // cannot drag the whole stack upward as notifications accumulate.
        this.width = Math.max(maxWidth, 100.0F);
        this.height = notificationHeight;

        // Фиксированный центр для отрисовки (предотвращает горизонтальное дёрганье)
        float anchorCenterX = this.getX() + this.width / 2.0F;
        float y = this.getY();

        // Отрисовка в прямом порядке (новые элементы добавляются и уходят вниз)
        for (BaseNotification n : visible) {
            float anim = n.alphaAnimation.getValue();
            if (anim > 0.001F) {
                n.render(ctx, anchorCenterX, y, textFont, theme, notificationHeight, this);
                y += (notificationHeight + 4.0F) * anim;
            }
        }
    }

    private static class ModuleNotification extends BaseNotification {
        final Module module;
        final boolean enabled;

        ModuleNotification(Module module, boolean enabled) {
            this.module = module;
            this.enabled = enabled;
        }

        @Override
        void render(CustomDrawContext ctx, float x, float y, Font textFont, Theme theme, float height, NotifyComponent parent) {
            float alpha = this.alphaAnimation.getValue();
            int alphaInt = (int)(alpha * 255.0F);
            ColorRGBA themeColor = theme.getColor().withAlpha(alphaInt);

            ColorRGBA outlineColor = new ColorRGBA(23, 23, 23, alphaInt);
            ColorRGBA bodyColor = new ColorRGBA(11, 11, 11, alphaInt);
            ColorRGBA whiteColor = ColorRGBA.WHITE.withAlpha(alphaInt);
            ColorRGBA slashColor = ColorRGBA.WHITE.withAlpha((int)(110 * alpha));

            String iconChar = "SD";
            String moduleName = this.module.getName();
            String slash = "  /  ";
            String statusText = this.enabled ? " was enabled!" : " was disabled!";

            float iconBlockSize = height;
            float iconSize = 9.5F;
            float iconW = Fonts.WYVERN.getWidth(iconChar, iconSize);

            float nameW = textFont.width(moduleName);
            float slashW = textFont.width(slash);
            float statusW = textFont.width(statusText);

            float textPadding = 7.5F;
            float slashPadding = 1.0F;
            float textBlockWidth = nameW + slashW + statusW + textPadding * 2 + slashPadding;
            float spacing = 4.0F;

            float totalWidth = iconBlockSize + spacing + textBlockWidth;
            float drawX = x - totalWidth / 2.0F;
            float cornerRadius = 4.0F;
            float stroke = 1.0F;

            // --- BLOCK 1: ICON ---
            DrawUtil.drawRoundedRect(ctx.getMatrices(), drawX, y, iconBlockSize, iconBlockSize, BorderRadius.all(cornerRadius), outlineColor);
            DrawUtil.drawRoundedRect(ctx.getMatrices(), drawX + stroke, y + stroke, iconBlockSize - stroke * 2, iconBlockSize - stroke * 2, BorderRadius.all(cornerRadius - stroke), bodyColor);

            float iconX = drawX + (iconBlockSize - iconW) / 2.0F + 0.5F;
            float iconY = y + (iconBlockSize - 9.0F) / 2.0F + 0.5F;
            drawGlow(ctx, iconX + iconW / 2.0F, y + iconBlockSize / 2.0F,
                    34.0F, 22.0F, themeColor.withAlpha((int)(22 * alpha)));
            ctx.drawText(Fonts.WYVERN.getFont(iconSize), iconChar, iconX, iconY, themeColor);

            // --- BLOCK 2: TEXT ---
            float textBlockX = drawX + iconBlockSize + spacing;
            DrawUtil.drawRoundedRect(ctx.getMatrices(), textBlockX, y, textBlockWidth, height, BorderRadius.all(cornerRadius), outlineColor);
            DrawUtil.drawRoundedRect(ctx.getMatrices(), textBlockX + stroke, y + stroke, textBlockWidth - stroke * 2, height - stroke * 2, BorderRadius.all(cornerRadius - stroke), bodyColor);

            float currentX = textBlockX + textPadding;
            float textY = y + (height - textFont.height()) / 2.0F + 0.2F;
            drawGlow(ctx, textBlockX + textBlockWidth / 2.0F, y + height / 2.0F,
                    Math.min(textBlockWidth + 10.0F, 100.0F), 28.0F,
                    ColorRGBA.WHITE.withAlpha((int)(16 * alpha)));

            ctx.drawText(textFont, moduleName, currentX, textY, themeColor);
            currentX += nameW + slashPadding;
            ctx.drawText(textFont, slash, currentX, textY, slashColor);
            currentX += slashW;
            ctx.drawText(textFont, statusText, currentX, textY, whiteColor);
        }

        @Override
        float getWidth(Font textFont, float height) {
            return height + 4.0F + textFont.width(this.module.getName())
                    + textFont.width("  /  ")
                    + textFont.width(this.enabled ? " was enabled!" : " was disabled!")
                    + 16.0F;
        }
    }

    private static class TextNotification extends BaseNotification {
        final String icon;
        final Text text;

        TextNotification(String icon, Text text) {
            this.icon = icon;
            this.text = text;
        }

        @Override
        void render(CustomDrawContext ctx, float x, float y, Font textFont, Theme theme, float height, NotifyComponent parent) {
            float alpha = this.alphaAnimation.getValue();
            int alphaInt = (int)(alpha * 255.0F);
            ColorRGBA themeColor = theme.getColor().withAlpha(alphaInt);

            ColorRGBA outlineColor = new ColorRGBA(23, 23, 23, alphaInt);
            ColorRGBA bodyColor = new ColorRGBA(11, 11, 11, alphaInt);
            ColorRGBA whiteColor = ColorRGBA.WHITE.withAlpha(alphaInt);

            String displayText = this.text.getString();
            float displayTextWidth = textFont.width(displayText);

            float iconBlockSize = height;
            float iconSize = 9.5F;
            float iconW = Fonts.WYVERN.getWidth(this.icon, iconSize);

            float textPadding = 7.5F;
            float textBlockWidth = displayTextWidth + textPadding * 2;
            float spacing = 4.0F;

            float totalWidth = iconBlockSize + spacing + textBlockWidth;
            float drawX = x - totalWidth / 2.0F;
            float cornerRadius = 4.0F;
            float stroke = 1.0F;

            // --- BLOCK 1: ICON ---
            DrawUtil.drawRoundedRect(ctx.getMatrices(), drawX, y, iconBlockSize, iconBlockSize, BorderRadius.all(cornerRadius), outlineColor);
            DrawUtil.drawRoundedRect(ctx.getMatrices(), drawX + stroke, y + stroke, iconBlockSize - stroke * 2, iconBlockSize - stroke * 2, BorderRadius.all(cornerRadius - stroke), bodyColor);

            float iconX = drawX + (iconBlockSize - iconW) / 2.0F + 0.5F;
            float iconY = y + (iconBlockSize - 9.0F) / 2.0F + 0.5F;
            drawGlow(ctx, iconX + iconW / 2.0F, y + iconBlockSize / 2.0F,
                    34.0F, 22.0F, themeColor.withAlpha((int)(22 * alpha)));
            ctx.drawText(Fonts.WYVERN.getFont(iconSize), this.icon, iconX, iconY, themeColor);

            // --- BLOCK 2: TEXT ---
            float textBlockX = drawX + iconBlockSize + spacing;
            DrawUtil.drawRoundedRect(ctx.getMatrices(), textBlockX, y, textBlockWidth, height, BorderRadius.all(cornerRadius), outlineColor);
            DrawUtil.drawRoundedRect(ctx.getMatrices(), textBlockX + stroke, y + stroke, textBlockWidth - stroke * 2, height - stroke * 2, BorderRadius.all(cornerRadius - stroke), bodyColor);

            float textY = y + (height - textFont.height()) / 2.0F + 0.2F;
            drawGlow(ctx, textBlockX + textBlockWidth / 2.0F, y + height / 2.0F,
                    Math.min(textBlockWidth + 10.0F, 100.0F), 28.0F,
                    ColorRGBA.WHITE.withAlpha((int)(16 * alpha)));
            ctx.drawText(textFont, displayText, textBlockX + textPadding, textY, whiteColor);
        }

        @Override
        float getWidth(Font textFont, float height) {
            return height + 4.0F + textFont.width(this.text.getString()) + 15.0F;
        }
    }

    private static class TotemNotification extends BaseNotification {
        final String name;
        final boolean enchanted;

        TotemNotification(String icon, boolean enchanted) {
            this.name = icon;
            this.enchanted = enchanted;
        }

        @Override
        void render(CustomDrawContext ctx, float x, float y, Font textFont, Theme theme, float height, NotifyComponent parent) {
            float alpha = this.alphaAnimation.getValue();
            int alphaInt = (int)(alpha * 255.0F);
            ColorRGBA themeColor = theme.getColor().withAlpha(alphaInt);

            ColorRGBA outlineColor = new ColorRGBA(23, 23, 23, alphaInt);
            ColorRGBA bodyColor = new ColorRGBA(11, 11, 11, alphaInt);
            ColorRGBA whiteColor = ColorRGBA.WHITE.withAlpha(alphaInt);

            String displayText = this.name + " lost totem";
            float displayTextWidth = textFont.width(displayText);

            float iconBlockSize = height;
            float textPadding = 7.5F;
            float textBlockWidth = displayTextWidth + textPadding * 2;
            float spacing = 4.0F;

            float totalWidth = iconBlockSize + spacing + textBlockWidth;
            float drawX = x - totalWidth / 2.0F;
            float cornerRadius = 4.0F;
            float stroke = 1.0F;

            // --- BLOCK 1: ICON ---
            DrawUtil.drawRoundedRect(ctx.getMatrices(), drawX, y, iconBlockSize, iconBlockSize, BorderRadius.all(cornerRadius), outlineColor);
            DrawUtil.drawRoundedRect(ctx.getMatrices(), drawX + stroke, y + stroke, iconBlockSize - stroke * 2, iconBlockSize - stroke * 2, BorderRadius.all(cornerRadius - stroke), bodyColor);

            float itemSize = 9.0F;
            float iconX = drawX + (iconBlockSize - itemSize) / 2.0F + 0.5F;
            float iconY = y + (iconBlockSize - itemSize) / 2.0F + 0.5F;
            drawGlow(ctx, iconX + itemSize / 2.0F, iconY + itemSize / 2.0F,
                    34.0F, 22.0F, themeColor.withAlpha((int)(22 * alpha)));
            ctx.drawTexture(Identifier.of("minecraft", "textures/item/totem_of_undying.png"), iconX, iconY, itemSize, itemSize, whiteColor);

            // --- BLOCK 2: TEXT ---
            float textBlockX = drawX + iconBlockSize + spacing;
            DrawUtil.drawRoundedRect(ctx.getMatrices(), textBlockX, y, textBlockWidth, height, BorderRadius.all(cornerRadius), outlineColor);
            DrawUtil.drawRoundedRect(ctx.getMatrices(), textBlockX + stroke, y + stroke, textBlockWidth - stroke * 2, height - stroke * 2, BorderRadius.all(cornerRadius - stroke), bodyColor);

            float textY = y + (height - textFont.height()) / 2.0F - 0.3F;
            drawGlow(ctx, textBlockX + textBlockWidth / 2.0F, y + height / 2.0F,
                    Math.min(textBlockWidth + 10.0F, 100.0F), 28.0F,
                    ColorRGBA.WHITE.withAlpha((int)(16 * alpha)));
            ctx.drawText(textFont, displayText, textBlockX + textPadding, textY, whiteColor);
        }

        @Override
        float getWidth(Font textFont, float height) {
            return height + 4.0F + textFont.width(this.name + " lost totem") + 15.0F;
        }
    }

    private static class ItemNotification extends BaseNotification {
        final String itemName;
        final String icon;

        ItemNotification(String itemName, String icon) {
            this.itemName = itemName;
            this.icon = icon;
        }

        @Override
        void render(CustomDrawContext ctx, float x, float y, Font textFont, Theme theme, float height, NotifyComponent parent) {
            float alpha = this.alphaAnimation.getValue();
            int alphaInt = (int)(alpha * 255.0F);
            ColorRGBA themeColor = theme.getColor().withAlpha(alphaInt);

            ColorRGBA outlineColor = new ColorRGBA(23, 23, 23, alphaInt);
            ColorRGBA bodyColor = new ColorRGBA(11, 11, 11, alphaInt);
            ColorRGBA whiteColor = ColorRGBA.WHITE.withAlpha(alphaInt);

            String displayText = this.itemName;
            float displayTextWidth = textFont.width(displayText);

            float iconBlockSize = height;
            float iconSize = 9.5F;
            float iconW = Fonts.WYVERN.getWidth(this.icon, iconSize);

            float textPadding = 7.5F;
            float textBlockWidth = displayTextWidth + textPadding * 2;
            float spacing = 4.0F;

            float totalWidth = iconBlockSize + spacing + textBlockWidth;
            float drawX = x - totalWidth / 2.0F;
            float cornerRadius = 4.0F;
            float stroke = 1.0F;

            // --- BLOCK 1: ICON ---
            DrawUtil.drawRoundedRect(ctx.getMatrices(), drawX, y, iconBlockSize, iconBlockSize, BorderRadius.all(cornerRadius), outlineColor);
            DrawUtil.drawRoundedRect(ctx.getMatrices(), drawX + stroke, y + stroke, iconBlockSize - stroke * 2, iconBlockSize - stroke * 2, BorderRadius.all(cornerRadius - stroke), bodyColor);

            float iconX = drawX + (iconBlockSize - iconW) / 2.0F + 0.5F;
            float iconY = y + (iconBlockSize - 9.0F) / 2.0F + 0.5F;
            drawGlow(ctx, iconX + iconW / 2.0F, y + iconBlockSize / 2.0F,
                    34.0F, 22.0F, themeColor.withAlpha((int)(22 * alpha)));
            ctx.drawText(Fonts.WYVERN.getFont(iconSize), this.icon, iconX, iconY, themeColor);

            // --- BLOCK 2: TEXT ---
            float textBlockX = drawX + iconBlockSize + spacing;
            DrawUtil.drawRoundedRect(ctx.getMatrices(), textBlockX, y, textBlockWidth, height, BorderRadius.all(cornerRadius), outlineColor);
            DrawUtil.drawRoundedRect(ctx.getMatrices(), textBlockX + stroke, y + stroke, textBlockWidth - stroke * 2, height - stroke * 2, BorderRadius.all(cornerRadius - stroke), bodyColor);

            float textY = y + (height - textFont.height()) / 2.0F - 0.3F;
            drawGlow(ctx, textBlockX + textBlockWidth / 2.0F, y + height / 2.0F,
                    Math.min(textBlockWidth + 10.0F, 100.0F), 28.0F,
                    ColorRGBA.WHITE.withAlpha((int)(16 * alpha)));
            ctx.drawText(textFont, displayText, textBlockX + textPadding, textY, whiteColor);
        }

        @Override
        float getWidth(Font textFont, float height) {
            return height + 4.0F + textFont.width(this.itemName) + 15.0F;
        }
    }

    private static class UseNotification extends BaseNotification {
        private static final float ITEM_SIZE = 11.0F;
        private static final float CORNER_RADIUS = 4.0F;
        private static final float STROKE = 1.0F;
        private static final float TEXT_PADDING = 7.5F;
        private static final float BLOCK_SPACING = 4.0F;

        final ItemStack stack;
        final Text text;

        UseNotification(ItemStack stack, Text text) {
            this.stack = stack;
            this.text = text;
        }

        @Override
        void render(CustomDrawContext ctx, float x, float y, Font textFont, Theme theme, float height, NotifyComponent parent) {
            float alpha = this.alphaAnimation.getValue();
            int alphaInt = (int)(alpha * 255.0F);
            ColorRGBA themeColor = theme.getColor().withAlpha(alphaInt);

            ColorRGBA outlineColor = new ColorRGBA(23, 23, 23, alphaInt);
            ColorRGBA bodyColor = new ColorRGBA(11, 11, 11, alphaInt);

            float iconBlockSize = height;
            float textBlockWidth = textFont.width(this.text) + TEXT_PADDING * 2.0F;
            float totalWidth = iconBlockSize + BLOCK_SPACING + textBlockWidth;
            float drawX = x - totalWidth / 2.0F;

            // --- BLOCK 1: ITEM ICON ---
            DrawUtil.drawRoundedRect(ctx.getMatrices(), drawX, y, iconBlockSize, iconBlockSize,
                    BorderRadius.all(CORNER_RADIUS), outlineColor);
            DrawUtil.drawRoundedRect(ctx.getMatrices(), drawX + STROKE, y + STROKE,
                    iconBlockSize - STROKE * 2.0F, iconBlockSize - STROKE * 2.0F,
                    BorderRadius.all(CORNER_RADIUS - STROKE), bodyColor);

            drawGlow(ctx, drawX + iconBlockSize / 2.0F, y + iconBlockSize / 2.0F,
                    34.0F, 22.0F, themeColor.withAlpha((int)(22 * alpha)));

            // Иконка рисуется штатным рендером предметов (16x16), поэтому
            // масштабируем её под размер блока и центрируем по обеим осям.
            float itemX = drawX + (iconBlockSize - ITEM_SIZE) / 2.0F;
            float itemY = y + (iconBlockSize - ITEM_SIZE) / 2.0F;
            float itemScale = ITEM_SIZE / 16.0F;
            MatrixStack matrices = ctx.getMatrices();
            matrices.push();
            matrices.translate(itemX, itemY, 0.0F);
            matrices.scale(itemScale, itemScale, 1.0F);
            ctx.drawItem(this.stack, 0, 0);
            matrices.pop();

            // --- BLOCK 2: TEXT ---
            float textBlockX = drawX + iconBlockSize + BLOCK_SPACING;
            DrawUtil.drawRoundedRect(ctx.getMatrices(), textBlockX, y, textBlockWidth, height,
                    BorderRadius.all(CORNER_RADIUS), outlineColor);
            DrawUtil.drawRoundedRect(ctx.getMatrices(), textBlockX + STROKE, y + STROKE,
                    textBlockWidth - STROKE * 2.0F, height - STROKE * 2.0F,
                    BorderRadius.all(CORNER_RADIUS - STROKE), bodyColor);

            drawGlow(ctx, textBlockX + textBlockWidth / 2.0F, y + height / 2.0F,
                    Math.min(textBlockWidth + 10.0F, 100.0F), 28.0F,
                    ColorRGBA.WHITE.withAlpha((int)(16 * alpha)));

            float textY = y + (height - textFont.height()) / 2.0F + 0.2F;
            ctx.drawText(textFont, this.text, textBlockX + TEXT_PADDING, textY, alphaInt);
        }

        @Override
        float getWidth(Font textFont, float height) {
            return height + BLOCK_SPACING + textFont.width(this.text) + TEXT_PADDING * 2.0F;
        }
    }

    private static void drawGlow(CustomDrawContext ctx, float centerX, float centerY,
                                 float width, float height, ColorRGBA color) {
        ctx.drawTexture(Wyvern.id("icons/glow.png"), centerX - width / 2.0F,
                centerY - height / 2.0F, width, height, color);
    }

    private abstract static class BaseNotification {
        long timestamp;
        boolean fadingOut = false;
        final Animation alphaAnimation;

        private BaseNotification() {
            this.alphaAnimation = new Animation(360L, Easing.CUBIC_OUT);
            // Задаём стартовое значение 0.0f и целевое 1.0f для гарантированной анимации
            this.alphaAnimation.setValue(0.0F);
            this.alphaAnimation.update(1.0F);
            this.timestamp = System.currentTimeMillis();
        }

        abstract void render(CustomDrawContext var1, float var2, float var3, Font var4, Theme var5, float var6, NotifyComponent var7);

        abstract float getWidth(Font font, float height);
    }
}

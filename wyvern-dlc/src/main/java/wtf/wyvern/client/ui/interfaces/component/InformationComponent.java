package wtf.wyvern.client.ui.interfaces.component;

import net.minecraft.client.MinecraftClient;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.font.Font;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.client.ui.interfaces.draggable.DraggableHudElement;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.shader.DrawUtil;
import wtf.wyvern.core.theme.Theme;

import java.util.Locale;

public class InformationComponent extends DraggableHudElement {
    private final boolean isV2;
    private float customWidth = 145.0F;
    private float customHeight = 17.0F;

    // Конструктор для старого HUD1
    public InformationComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight, float offsetX, float offsetY, Align align) {
        super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
        this.isV2 = false;
    }

    // Конструктор для нового HUD2
    public InformationComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight, float offsetX, float offsetY, Align align, boolean isV2) {
        super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
        this.isV2 = isV2;
    }

    // ХАК ДЕРЖАТЕЛЯ МЫШИ
    public boolean isHovered(double mouseX, double mouseY) {
        float x = this.getX();
        float y = this.getY();
        return mouseX >= x && mouseX <= x + this.customWidth && mouseY >= y && mouseY <= y + this.customHeight;
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        float x = this.getX();
        float y = this.getY();
        return mouseX >= x && mouseX <= x + this.customWidth && mouseY >= y && mouseY <= y + this.customHeight;
    }

    @Override
    public void render(CustomDrawContext ctx) {
        if (mc.player == null) return;

        if (this.isV2) {
            this.renderV2(ctx);
        } else {
            this.renderV1(ctx);
        }
    }

    private void renderV1(CustomDrawContext ctx) {
        double deltaX = mc.player.getX() - mc.player.prevX;
        double deltaZ = mc.player.getZ() - mc.player.prevZ;
        double bps = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 20.0D;
        String bpsText = String.format(Locale.US, "%.2f", bps);

        float screenWidth = (float) mc.getWindow().getScaledWidth();
        float screenHeight = (float) mc.getWindow().getScaledHeight();
        float fontSize = 8.0F;
        float textWidth = Fonts.REGULAR.getWidth(bpsText, fontSize);
        float posX = screenWidth / 2.0F - textWidth / 2.0F;
        float posY = screenHeight / 2.0F - fontSize / 2.0F + 15.0F;

        ctx.drawText(Fonts.REGULAR.getFont(fontSize), bpsText, posX, posY, new ColorRGBA(255, 255, 255, 255));
        this.customWidth = textWidth;
        this.customHeight = fontSize;
    }

    private void renderV2(CustomDrawContext ctx) {
        float startX = this.getX();
        float y = this.getY();

        float notificationHeight = 17.0F;
        int alphaInt = 255;

        Theme theme = Wyvern.getInstance().getThemeManager().getCurrentTheme();

        // ВСЕ элементы теперь строго используют шрифт MEDIUM
        Font textFont = Fonts.MEDIUM.getFont(6.2F);

        ColorRGBA themeColor = theme.getColor().withAlpha(alphaInt);
        ColorRGBA outlineColor = new ColorRGBA(23, 23, 23, alphaInt);
        ColorRGBA bodyColor = new ColorRGBA(11, 11, 11, alphaInt);
        ColorRGBA whiteColor = ColorRGBA.WHITE.withAlpha(alphaInt);
        ColorRGBA slashColor = ColorRGBA.WHITE.withAlpha(110);

        // 1. Получение данных
        double deltaX = mc.player.getX() - mc.player.prevX;
        double deltaZ = mc.player.getZ() - mc.player.prevZ;
        double bps = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 20.0D;

        String xVal = String.valueOf((int) mc.player.getX());
        String yVal = String.valueOf((int) mc.player.getY());
        String zVal = String.valueOf((int) mc.player.getZ());
        String bpsVal = String.format(Locale.US, "%.0f", bps);

        String fpsVal = "0";
        if (mc.fpsDebugString != null && mc.fpsDebugString.contains(" ")) {
            fpsVal = mc.fpsDebugString.split(" ")[0];
        }

        // Делаем разделители шире визуально с помощью дополнительных пробелов
        String slash = "  /  ";
        String infoIcon = "SD";

        // Изменено на Fonts.MEDIUM
        float iconSize = 9.5F;
        float iconW = Fonts.WYVERN.getWidth(infoIcon, iconSize);

        // 2. Геометрия блоков
        float iconBlockSize = notificationHeight;
        float textPadding = 6.0F;
        float spacing = 4.0F;
        float cornerRadius = 4.0F;
        float stroke = 1.0F;

        // Переменные для расширенных отступов между блоками надписей
        // УМЕНЬШЕНО С 4.5F ДО 2.5F, ЧТОБЫ КООРДИНАТЫ БЫЛИ БЛИЖЕ ДРУГ К ДРУГУ
        float innerSpace = 2.5F;

        float letterW = textFont.width("x");
        float letterGap = textFont.width(" "); // Микро пробел между буквой оси и значением (например между 'x' и '170')

        float xValW = textFont.width(xVal);
        float yValW = textFont.width(yVal);
        float zValW = textFont.width(zVal);
        float slashW = textFont.width(slash);
        float bpsValW = textFont.width(bpsVal);
        float bpsLabelW = textFont.width("bps");
        float fpsValW = textFont.width(fpsVal);
        float fpsLabelW = textFont.width("fps");

        // Расчёт полной динамической ширины с учётом увеличенных зазоров
        float textBlockWidth = (letterW * 3) + (letterGap * 3) + xValW + yValW + zValW
                + (innerSpace * 2)
                + (slashW * 2)
                + bpsValW + bpsLabelW
                + fpsValW + fpsLabelW
                + (textPadding * 2);

        // Обновляем динамические размеры, которые теперь напрямую слушают методы проверки мыши выше
        this.customWidth = iconBlockSize + spacing + textBlockWidth;
        this.customHeight = notificationHeight;

        // --- БЛОК 1: ИКОНКА ---
        DrawUtil.drawRoundedRect(ctx.getMatrices(), startX, y, iconBlockSize, iconBlockSize, BorderRadius.all(cornerRadius), outlineColor);
        DrawUtil.drawRoundedRect(ctx.getMatrices(), startX + stroke, y + stroke, iconBlockSize - stroke * 2, iconBlockSize - stroke * 2, BorderRadius.all(cornerRadius - stroke), bodyColor);

        float iconX = startX + (iconBlockSize - iconW) / 2.0F + 0.5F;
        float iconY = y + (iconBlockSize - 9.0F) / 2.0F + 0.5F;
        drawGlow(ctx, iconX + iconW / 2.0F, y + iconBlockSize / 2.0F,
                34.0F, 22.0F, themeColor.withAlpha(22));
        ctx.drawText(Fonts.WYVERN.getFont(iconSize), infoIcon, iconX, iconY, themeColor);

        // --- БЛОК 2: ИНФОРМАЦИЯ ---
        float textBlockX = startX + iconBlockSize + spacing;
        DrawUtil.drawRoundedRect(ctx.getMatrices(), textBlockX, y, textBlockWidth, notificationHeight, BorderRadius.all(cornerRadius), outlineColor);
        DrawUtil.drawRoundedRect(ctx.getMatrices(), textBlockX + stroke, y + stroke, textBlockWidth - stroke * 2, notificationHeight - stroke * 2, BorderRadius.all(cornerRadius - stroke), bodyColor);

        // Текст
        float currentX = textBlockX + textPadding;
        float textY = y + (notificationHeight - textFont.height()) / 2.0F + 0.2F;
        drawGlow(ctx, textBlockX + textBlockWidth / 2.0F, y + notificationHeight / 2.0F,
                Math.min(textBlockWidth + 10.0F, 140.0F), 28.0F,
                ColorRGBA.WHITE.withAlpha(16));

        // Координата X
        ctx.drawText(textFont, "x", currentX, textY, themeColor);
        currentX += letterW + letterGap;
        ctx.drawText(textFont, xVal, currentX, textY, whiteColor);
        currentX += xValW + innerSpace;

        // Координата Y
        ctx.drawText(textFont, "y", currentX, textY, themeColor);
        currentX += letterW + letterGap;
        ctx.drawText(textFont, yVal, currentX, textY, whiteColor);
        currentX += yValW + innerSpace;

        // Координата Z
        ctx.drawText(textFont, "z", currentX, textY, themeColor);
        currentX += letterW + letterGap;
        ctx.drawText(textFont, zVal, currentX, textY, whiteColor);
        currentX += zValW;

        // Разделитель /
        ctx.drawText(textFont, slash, currentX, textY, slashColor);
        currentX += slashW;

        // BPS
        ctx.drawText(textFont, bpsVal, currentX, textY, whiteColor);
        currentX += bpsValW;
        ctx.drawText(textFont, "bps", currentX, textY, themeColor);
        currentX += bpsLabelW;

        // Разделитель /
        ctx.drawText(textFont, slash, currentX, textY, slashColor);
        currentX += slashW;

        // FPS
        ctx.drawText(textFont, fpsVal, currentX, textY, whiteColor);
        currentX += fpsValW;
        ctx.drawText(textFont, "fps", currentX, textY, themeColor);
    }

    private static void drawGlow(CustomDrawContext ctx, float centerX, float centerY,
                                 float width, float height, ColorRGBA color) {
        ctx.drawTexture(Wyvern.id("icons/glow.png"), centerX - width / 2.0F,
                centerY - height / 2.0F, width, height, color);
    }

    @Override
    public float getWidth() {
        return this.customWidth;
    }

    @Override
    public float getHeight() {
        return this.customHeight;
    }
}

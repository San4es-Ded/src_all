package wtf.wyvern.client.ui.interfaces.component;

import net.minecraft.client.gui.screen.ChatScreen;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.impl.render.WardenBoost;
import wtf.wyvern.client.ui.interfaces.draggable.DraggableHudElement;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.core.theme.Theme;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/** HUD-панель WardenBoost: сундуки с таймерами и ближайший варден. */
public final class WardenBoostComponent extends DraggableHudElement {
    private static final float MIN_WIDTH = 92.0F;
    private static final float HEADER_HEIGHT = 13.5F;
    private static final float ROW_HEIGHT = 12.0F;
    private static final ColorRGBA DIM_COLOR = new ColorRGBA(150, 150, 160, 255);

    private final Animation alpha = new Animation(200L, Easing.CUBIC_OUT);
    private final Animation widthAnimation = new Animation(200L, Easing.CUBIC_OUT);
    private final boolean v2;

    public WardenBoostComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight,
                                float offsetX, float offsetY, Align align, boolean v2) {
        super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
        this.v2 = v2;
    }

    @Override
    public void render(CustomDrawContext ctx) {
        WardenBoost module = WardenBoost.INSTANCE;
        boolean moduleActive = module.isEnabled();

        List<WardenBoost.ChestEntry> chests = new ArrayList<>();
        if (moduleActive) {
            chests.addAll(module.getChests().values());
            chests.sort(Comparator.comparingInt(WardenBoost.ChestEntry::getEffectiveTimer));
        }
        boolean wardenVisible = moduleActive && module.isWardenVisible() && module.getWardenDistance() >= 0.0;

        boolean hasContent = !chests.isEmpty() || wardenVisible;
        boolean show = moduleActive && (hasContent || mc.currentScreen instanceof ChatScreen);
        alpha.update(show ? 1.0F : 0.0F);
        float currentAlpha = alpha.getValue();
        if (currentAlpha < 0.01F) {
            width = 0.0F;
            height = 0.0F;
            return;
        }

        Theme theme = Wyvern.getInstance().getThemeManager().getCurrentTheme();
        ColorRGBA themeColor = theme.getColor();
        ColorRGBA darker = themeColor.darker(0.95F);

        float textSize = 7.0F;
        float maxWidth = Fonts.MEDIUM.getWidth("WardenBoost", 7.5F) + 24.0F;
        if (wardenVisible) {
            maxWidth = Math.max(maxWidth, Fonts.REGULAR.getWidth("Варден", textSize)
                    + Fonts.REGULAR.getWidth(distanceString(module.getWardenDistance()), textSize) + 26.0F);
        }
        for (WardenBoost.ChestEntry chest : chests) {
            maxWidth = Math.max(maxWidth, Fonts.REGULAR.getWidth(rowLabel(chest), textSize)
                    + Fonts.REGULAR.getWidth(chestDistance(chest), textSize) + 26.0F);
        }
        widthAnimation.update(Math.max(MIN_WIDTH, maxWidth));

        float panelWidth = widthAnimation.getValue();
        int rows = chests.size() + (wardenVisible ? 1 : 0);
        float totalHeight = HEADER_HEIGHT + rows * ROW_HEIGHT + 6.0F;

        BorderRadius headerRadius = v2 ? BorderRadius.all(4.0F) : new BorderRadius(4.0F, 4.0F, 0, 0);
        DrawUtil.drawBlur(ctx.getMatrices(), x, y, panelWidth, HEADER_HEIGHT, 30.0F,
                headerRadius, ColorRGBA.WHITE.withAlpha((int) (255 * currentAlpha)));
        DrawUtil.drawRoundedRect(ctx.getMatrices(), x, y, panelWidth, HEADER_HEIGHT,
                headerRadius, darker.withAlpha((int) (250 * currentAlpha)));

        ctx.drawText(Fonts.REGULAR.getFont(7.5F), "WardenBoost", x + 6.0F, y + 5.0F,
                ColorRGBA.WHITE.withAlpha((int) (255 * currentAlpha)));

        float currentY = y + HEADER_HEIGHT + 3.0F;

        if (wardenVisible) {
            double distance = module.getWardenDistance();
            ColorRGBA wardenColor = distance < 10.0 ? new ColorRGBA(255, 90, 90, 255)
                    : distance < 25.0 ? new ColorRGBA(255, 150, 64, 255)
                    : new ColorRGBA(170, 255, 170, 255);
            drawRowBackground(ctx, currentY, panelWidth, darker, currentAlpha);
            ctx.drawText(Fonts.REGULAR.getFont(textSize), "Варден", x + 6.0F, currentY + 2.5F,
                    ColorRGBA.WHITE.withAlpha((int) (255 * currentAlpha)));
            String distanceString = distanceString(distance);
            float distanceWidth = Fonts.REGULAR.getWidth(distanceString, textSize);
            ctx.drawText(Fonts.REGULAR.getFont(textSize), distanceString,
                    x + panelWidth - distanceWidth - 6.0F, currentY + 2.5F,
                    wardenColor.withAlpha((int) (255 * currentAlpha)));
            currentY += ROW_HEIGHT;
        }

        for (WardenBoost.ChestEntry chest : chests) {
            drawRowBackground(ctx, currentY, panelWidth, darker, currentAlpha);
            ctx.drawText(Fonts.REGULAR.getFont(textSize), rowLabel(chest), x + 6.0F, currentY + 2.5F,
                    chest.getColor().withAlpha((int) (255 * currentAlpha)));
            String distanceString = chestDistance(chest);
            float distanceWidth = Fonts.REGULAR.getWidth(distanceString, textSize);
            ctx.drawText(Fonts.REGULAR.getFont(textSize), distanceString,
                    x + panelWidth - distanceWidth - 6.0F, currentY + 2.5F,
                    DIM_COLOR.withAlpha((int) (255 * currentAlpha)));
            currentY += ROW_HEIGHT;
        }

        width = panelWidth;
        height = totalHeight;
    }

    private void drawRowBackground(CustomDrawContext ctx, float rowY, float panelWidth, ColorRGBA darker, float currentAlpha) {
        DrawUtil.drawBlur(ctx.getMatrices(), x + 3.0F, rowY, panelWidth - 6.0F, 10.0F, 20.0F,
                BorderRadius.all(3.0F), ColorRGBA.WHITE.withAlpha((int) (200 * currentAlpha)));
        DrawUtil.drawRoundedRect(ctx.getMatrices(), x + 3.0F, rowY, panelWidth - 6.0F, 10.0F,
                BorderRadius.all(3.0F), darker.withAlpha((int) (180 * currentAlpha)));
    }

    private String rowLabel(WardenBoost.ChestEntry chest) {
        return chest.getTimerString() + "  " + chest.pos.getX() + " " + chest.pos.getY() + " " + chest.pos.getZ();
    }

    private String chestDistance(WardenBoost.ChestEntry chest) {
        if (mc.player == null) return "";
        return distanceString(chest.distanceTo(mc.player.getX(), mc.player.getY(), mc.player.getZ()));
    }

    private String distanceString(double distance) {
        return String.format(Locale.US, "%.0fm", distance);
    }
}

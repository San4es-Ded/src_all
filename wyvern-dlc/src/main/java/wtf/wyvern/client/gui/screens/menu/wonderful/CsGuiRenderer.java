package wtf.wyvern.client.gui.screens.menu.wonderful;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.core.theme.Theme;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.setting.Setting;
import wtf.wyvern.client.modules.api.setting.impl.*;
import wtf.wyvern.utility.math.MathUtil;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;
import wtf.wyvern.client.gui.screens.menu.wonderful.figura.FiguraModelsPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * CS GUI layout adapted to Wyvern's Screen/DrawContext renderer.
 * Coordinates are the original GUI dimensions divided by Minecraft's default x2 GUI scale.
 */
public final class CsGuiRenderer {
    public static final float PANEL_WIDTH = 450.0F;
    public static final float PANEL_HEIGHT = 300.0F;
    public static final float SIDEBAR_WIDTH = 112.5F;
    public static final float HEADER_HEIGHT = 37.5F;
    public static final float CARD_HEADER_HEIGHT = 26.0F;
    public static final float CARD_GAP = 4.0F;
    public static final float CONTENT_PADDING = 6.0F;
    public static final float SCROLLBAR_GUTTER = 5.0F;
    public static final float ROUNDING = 6.0F;
    public static final int THEME_COLUMNS = 8;
    public static final float THEME_SWATCH_SIZE = 22.0F;
    public static final float THEME_SWATCH_GAP = 12.0F;
    public static final float THEME_GRID_TOP = 8.0F;
    public static final ColorRGBA ACCENT = new ColorRGBA(150, 121, 226);
    private ColorRGBA accent = ACCENT;
    private final ArrayList<Card> cardBuffer = new ArrayList<>();
    private final FiguraModelsPage figuraPage = new FiguraModelsPage();

    public FiguraModelsPage getFiguraPage() {
        return figuraPage;
    }

    public static ColorRGBA getCurrentAccent() {
        return Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor();
    }

    public static final class Card {
        public Module module;
        public float x, y, width, height;

        public Card(Module module, float x, float y, float width, float height) {
            this.module = module;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        private void set(Module module, float x, float y, float width, float height) {
            this.module = module;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean contains(double mouseX, double mouseY) {
            return MathUtil.isHovered(mouseX, mouseY, x, y, width, height);
        }

        public boolean headerContains(double mouseX, double mouseY) {
            return MathUtil.isHovered(mouseX, mouseY, x, y, width, CARD_HEADER_HEIGHT);
        }
    }

    public record Chip(float x, float y, float width, float height, ModeSetting.Value value) { }

    public void render(DrawContext context, int mouseX, int mouseY, Window window, float progress, ClickGuiState state) {
        if (window == null) return;

        float x = state.getX();
        float y = state.getY() + state.getRenderOffsetY();
        int alpha = (int) (255 * progress);
        CustomDrawContext draw = CustomDrawContext.of(context);
        accent = Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor();

        // Full-panel blur with a dense dark overlay to prevent excessive transparency.
        DrawUtil.drawBlur(draw.getMatrices(), x, y, PANEL_WIDTH, PANEL_HEIGHT, 30, BorderRadius.all(ROUNDING), ColorRGBA.WHITE.withAlpha(alpha));
        DrawUtil.drawRoundedRect(draw.getMatrices(), x, y, PANEL_WIDTH, PANEL_HEIGHT, BorderRadius.all(ROUNDING), new ColorRGBA(5, 6, 10, (int) (230 * progress)));

        // Sidebar and Header use a more opaque layer for better separation.
        DrawUtil.drawRoundedRect(draw.getMatrices(), x, y, SIDEBAR_WIDTH, PANEL_HEIGHT, new BorderRadius(ROUNDING, 0, 0, ROUNDING), new ColorRGBA(4, 5, 8, (int) (100 * progress)));
        DrawUtil.drawRoundedRect(draw.getMatrices(), x + SIDEBAR_WIDTH, y, PANEL_WIDTH - SIDEBAR_WIDTH, HEADER_HEIGHT, new BorderRadius(0, ROUNDING, 0, 0), new ColorRGBA(4, 5, 8, (int) (100 * progress)));

        renderSidebar(draw, x, y, alpha, mouseX, mouseY, state);
        renderHeader(draw, x, y, alpha, state);
        renderContent(context, draw, x, y, alpha, mouseX, mouseY, state);
    }

    private void renderSidebar(CustomDrawContext draw, float x, float y, int alpha, int mouseX, int mouseY, ClickGuiState state) {
        float searchX = x + 9F;
        float searchY = y + 42.5F;
        float searchW = SIDEBAR_WIDTH - 17.5F;
        boolean searching = state.isSearchActive();

        // WYVERN logo mark (glyph "A" of the wyvern font) instead of the old cube icon.
        float logoSize = 20.0F;
        float logoW = Fonts.WYVERN.getWidth("A", logoSize);
        draw.drawText(Fonts.WYVERN.getFont(logoSize), "A",
                x + (SIDEBAR_WIDTH - logoW) / 2.0F, y + 13, accent.withAlpha(alpha));
        DrawUtil.drawBlur(draw.getMatrices(), searchX, searchY, searchW, 16, 8.0F, BorderRadius.all(3.0F), new ColorRGBA(255, 255, 255, searching ? 26 : 16));
        draw.drawText(Fonts.LUPA.getFont(5.5F), "\uF002", searchX + 5, searchY + 6.0F, new ColorRGBA(156, 159, 175, alpha));
        String query = state.getSearchText();
        String shown = query.isEmpty() && !searching ? "Поиск" : query;
        draw.drawText(Fonts.MEDIUM.getFont(6.8F), shown, searchX + 14, searchY + 5.0F, query.isEmpty() && !searching ? new ColorRGBA(132, 135, 150, alpha) : ColorRGBA.WHITE.withAlpha(alpha));
        if (searching && (System.currentTimeMillis() / 500) % 2 == 0) {
            float cursor = searchX + 14 + Fonts.MEDIUM.getWidth(query.substring(0, Math.min(query.length(), state.getSearchCursor())), 6.8F);
            DrawUtil.drawRect(draw.getMatrices(), cursor, searchY + 4, 1, 8, ColorRGBA.WHITE.withAlpha(alpha));
        }

        // Group captions and their entries intentionally use separate anchors.
        // This keeps the caption above the list instead of moving both together.
        draw.drawText(Fonts.MEDIUM.getFont(7.5F), "Category", x + 7, y + 65.5F, new ColorRGBA(145, 148, 163, alpha));
        // One shared baseline/step keeps both sidebar groups perfectly aligned.
        final float categoryStep = 17.5F;
        float itemY = y + 77.5F;
        Category[] categories = Category.values();
        for (int i = 0; i < Category.THEMES.ordinal(); i++) {
            renderCategory(draw, x, itemY, i, categories[i], alpha, mouseX, mouseY, state);
            itemY += categoryStep;
        }

        draw.drawText(Fonts.MEDIUM.getFont(7.5F), "Client", x + 7, itemY + 0.5f, new ColorRGBA(145, 148, 163, alpha));
        itemY += 12.0f;
        renderCategory(draw, x, itemY, Category.THEMES.ordinal(), Category.THEMES, alpha, mouseX, mouseY, state);
        renderCategory(draw, x, itemY + categoryStep, Category.FIGURA.ordinal(), Category.FIGURA, alpha, mouseX, mouseY, state);
        renderConfigCategory(draw, x, itemY + categoryStep * 2.0F, alpha, mouseX, mouseY, state);
    }

    private void renderCategory(CustomDrawContext draw, float x, float y, int index, Category category, int alpha, int mouseX, int mouseY, ClickGuiState state) {
        boolean selected = !state.isConfigPage() && state.getSelectedCategory() == index;
        float selectionProgress = state.getCategorySelectionProgress(index, selected);
        boolean hovered = MathUtil.isHovered(mouseX, mouseY, x + 4, y - 4.5F, SIDEBAR_WIDTH - 12, 16.5F);
        if (selectionProgress > 0.01F) {
            DrawUtil.drawBlur(draw.getMatrices(), x + 4, y - 4.5F, SIDEBAR_WIDTH - 12, 16.5F, 8.0F, BorderRadius.all(3), new ColorRGBA(255, 255, 255, (int) (16 * selectionProgress)));
        } else if (hovered) {
            DrawUtil.drawBlur(draw.getMatrices(), x + 4, y - 4.5F, SIDEBAR_WIDTH - 12, 16.5F, 8.0F, BorderRadius.all(3), new ColorRGBA(255, 255, 255, 10));
        }
        ColorRGBA color = mix(new ColorRGBA(174, 177, 191, alpha), accent.withAlpha(alpha), selectionProgress);
        float iconX = x + 18.5f - Fonts.FONT.getWidth(category.getIcon(), 8.0F) / 2F;
        draw.drawText(Fonts.FONT.getFont(8.0F), category.getIcon(), iconX, y + 1.8F, color);
        draw.drawText(Fonts.MEDIUM.getFont(7.5F), category.getName(), x + 25, y + 1.3F, color);
    }

    private void renderUnavailableCategory(CustomDrawContext draw, float x, float y, String name, String icon, int alpha) {
        ColorRGBA color = new ColorRGBA(126, 129, 144, alpha);
        draw.drawText(Fonts.FONT.getFont(6.5F), icon, x + 16, y + 0.8F, color);
        draw.drawText(Fonts.MEDIUM.getFont(7.5F), name, x + 28, y + 0.6F, color);
    }

    private void renderConfigCategory(CustomDrawContext draw, float x, float y, int alpha, int mouseX, int mouseY, ClickGuiState state) {
        boolean selected = state.isConfigPage();
        boolean hovered = MathUtil.isHovered(mouseX, mouseY, x + 4, y - 4.5F, SIDEBAR_WIDTH - 12, 16.5F);
        float selectionProgress = state.getCategorySelectionProgress(100, selected);
        if (selectionProgress > 0.01F) {
            DrawUtil.drawBlur(draw.getMatrices(), x + 4, y - 4.5F, SIDEBAR_WIDTH - 12, 16.5F, 8.0F, BorderRadius.all(3), new ColorRGBA(255, 255, 255, (int) (16 * selectionProgress)));
        } else if (hovered) {
            DrawUtil.drawBlur(draw.getMatrices(), x + 4, y - 4.5F, SIDEBAR_WIDTH - 12, 16.5F, 8.0F, BorderRadius.all(3), new ColorRGBA(255, 255, 255, 10));
        }
        ColorRGBA color = mix(new ColorRGBA(174, 177, 191, alpha), accent.withAlpha(alpha), selectionProgress);
        draw.drawText(Fonts.FONT.getFont(8.0F), "C", x + 18.5f - Fonts.FONT.getWidth("C", 8.0F) / 2F, y + 1.8F, color);
        draw.drawText(Fonts.MEDIUM.getFont(7.5F), "Config", x + 26, y + 1.3F, color);
    }

    private void renderHeader(CustomDrawContext draw, float x, float y, int alpha, ClickGuiState state) {
        Category category = state.getSelectedCategoryValue();
        float transition = state.getCategoryContentProgress();
        float contentX = x + SIDEBAR_WIDTH + CONTENT_PADDING
                + state.getCategoryContentDirection() * (1.0F - transition) * 22.0F;
        alpha = (int)(alpha * transition);
        if (state.isConfigPage()) {
            draw.drawText(Fonts.FONT.getFont(11), "C", contentX + 6 - Fonts.FONT.getWidth("C", 12.5f) / 4F, y + 15.985F, accent.withAlpha(alpha));
            draw.drawText(Fonts.MEDIUM.getFont(10.0F), "Config", contentX + 16, y + 15.985F, ColorRGBA.WHITE.withAlpha(alpha));
        } else {
            draw.drawText(Fonts.FONT.getFont(11), category.getIcon(), contentX + 6 - Fonts.FONT.getWidth(category.getIcon(), 12.5f) / 4F, y + 15.985F, accent.withAlpha(alpha));
            draw.drawText(Fonts.MEDIUM.getFont(10.0F), category.getName(), contentX + 16, y + 15.985F, ColorRGBA.WHITE.withAlpha(alpha));
        }
    }

    private void renderContent(DrawContext context, CustomDrawContext draw, float x, float y, int alpha, int mouseX, int mouseY, ClickGuiState state) {
        float contentX = x + SIDEBAR_WIDTH + CONTENT_PADDING;
        float contentProgress = state.getCategoryContentProgress();
        contentX += state.getCategoryContentDirection() * (1.0F - contentProgress) * 22.0F;
        float contentY = y + HEADER_HEIGHT + CONTENT_PADDING;
        float contentW = PANEL_WIDTH - SIDEBAR_WIDTH - CONTENT_PADDING * 2;
        float contentH = PANEL_HEIGHT - HEADER_HEIGHT - CONTENT_PADDING * 2;
        alpha = (int) (alpha * contentProgress);
        if (state.isConfigPage()) {
            renderConfigs(context, draw, contentX, contentY, contentW, contentH, alpha, mouseX, mouseY, state);
            return;
        }
        if (state.getSelectedCategoryValue() == Category.THEMES) {
            renderThemes(context, draw, contentX, contentY, contentW, contentH, alpha, mouseX, mouseY, state);
            return;
        }
        if (state.getSelectedCategoryValue() == Category.FIGURA) {
            figuraPage.render(context, draw, contentX, contentY, contentW, contentH, alpha, mouseX, mouseY);
            return;
        }

        float gridWidth = contentW - SCROLLBAR_GUTTER;
        List<Card> cards = buildCards(state, contentX, contentY, gridWidth);
        float maxScroll = Math.max(0, getContentHeight(cards) - (contentY + contentH));
        Category category = state.getSelectedCategoryValue();
        state.setScrollTarget(category, Math.max(0, Math.min(maxScroll, state.getScrollTarget(category))));
        float scroll = state.getScroll(category);

        context.enableScissor((int) contentX, (int) contentY, (int) (contentX + contentW), (int) (contentY + contentH));
        for (Card card : cards) {
            float layoutY = card.y;
            card.y = layoutY - scroll;
            if (card.y + card.height >= contentY && card.y <= contentY + contentH) {
                renderCard(draw, card, alpha, mouseX, mouseY, state);
            }
            card.y = layoutY;
        }
        context.disableScissor();

        if (maxScroll > 0.5F) {
            float thumbH = Math.max(15, contentH * contentH / (maxScroll + contentH));
            float thumbY = contentY + (contentH - thumbH) * (scroll / maxScroll);
            DrawUtil.drawRoundedRect(draw.getMatrices(), contentX + contentW - 1.5F, contentY + 1, 1.5F, contentH - 2, BorderRadius.all(1), new ColorRGBA(255, 255, 255, 15));
            DrawUtil.drawRoundedRect(draw.getMatrices(), contentX + contentW - 1.5F, thumbY, 1.5F, thumbH, BorderRadius.all(1), new ColorRGBA(205, 205, 215, alpha));
        }
    }

    private void renderConfigs(DrawContext context, CustomDrawContext draw, float x, float y, float width, float height, int alpha, int mouseX, int mouseY, ClickGuiState state) {
        draw.drawText(Fonts.MEDIUM.getFont(7.2F), "Конфигурации", x, y + 1, ColorRGBA.WHITE.withAlpha(alpha));
        draw.drawText(Fonts.MEDIUM.getFont(5.2F), "Сохранение и загрузка настроек клиента", x, y + 9, new ColorRGBA(145, 148, 163, alpha));

        float fieldY = y + 16;
        float fieldW = width - 51;
        DrawUtil.drawRoundedRect(draw.getMatrices(), x, fieldY, fieldW, 16, BorderRadius.all(3), new ColorRGBA(255, 255, 255, state.isConfigNameActive() ? 25 : 15));
        String value = state.getConfigName();
        String shown = value.isBlank() && !state.isConfigNameActive() ? "Название конфига" : value;
        draw.drawText(Fonts.MEDIUM.getFont(5.8F), shown, x + 6, fieldY + 5.4F, value.isBlank() && !state.isConfigNameActive() ? new ColorRGBA(132, 135, 150, alpha) : ColorRGBA.WHITE.withAlpha(alpha));
        if (state.isConfigNameActive() && (System.currentTimeMillis() / 500) % 2 == 0) {
            float cursor = x + 6 + Fonts.MEDIUM.getWidth(value.substring(0, Math.min(value.length(), state.getConfigNameCursor())), 5.8F);
            DrawUtil.drawRect(draw.getMatrices(), cursor, fieldY + 4, 1, 8, ColorRGBA.WHITE.withAlpha(alpha));
        }
        DrawUtil.drawRoundedRect(draw.getMatrices(), x + fieldW + 4, fieldY, 47, 16, BorderRadius.all(3), accent.withAlpha(alpha));
        draw.drawText(Fonts.MEDIUM.getFont(5.8F), "Создать", x + fieldW + 15.5F, fieldY + 5.7F, ColorRGBA.WHITE.withAlpha(alpha));

        List<String> configs = getConfigNames();
        float listY = y + 44;
        context.enableScissor((int) x, (int) listY, (int) (x + width), (int) (y + height));
        for (int i = 0; i < configs.size(); i++) {
            String config = configs.get(i);
            float cy = listY + i * 32;
            
            boolean hovered = MathUtil.isHovered(mouseX, mouseY, x, cy, width, 28);
            var hoverAnimation = state.getConfigHoverAnimation(config);
            hoverAnimation.update(hovered);
            float hoverProgress = hoverAnimation.getValue();

            var entryAnimation = state.getConfigEntryAnimation(config);
            boolean isRemoved = state.isConfigRemoved(config);
            entryAnimation.update(isRemoved ? 0.0F : 1.0F);
            float entryProgress = entryAnimation.getValue();
            
            if (entryProgress <= 0.01F && isRemoved) continue;

            draw.getMatrices().push();
            draw.getMatrices().translate(x + width / 2F, cy + 14, 0);
            float scale = 0.95F + 0.05F * entryProgress;
            draw.getMatrices().scale(scale, scale, 1.0F);
            draw.getMatrices().translate(-(x + width / 2F), -(cy + 14), 0);

            int currentAlpha = (int) (alpha * entryProgress);

            // Config cards now use the same dense dark blur as theme/module cards.
            DrawUtil.drawBlur(draw.getMatrices(), x, cy, width, 28, 25.0F, BorderRadius.all(4), new ColorRGBA(255, 255, 255, currentAlpha));
            ColorRGBA configOverlay = mix(new ColorRGBA(10, 11, 18, (int) (225 * (currentAlpha / 255F))), new ColorRGBA(20, 21, 28, (int) (245 * (currentAlpha / 255F))), hoverProgress);
            DrawUtil.drawRoundedRect(draw.getMatrices(), x, cy, width, 28, BorderRadius.all(4), configOverlay);

            draw.drawText(Fonts.MEDIUM.getFont(7.5F), config, x + 7, cy + 7.8F, ColorRGBA.WHITE.withAlpha(currentAlpha));
            draw.drawText(Fonts.MEDIUM.getFont(5.8F), "Нажмите, чтобы загрузить", x + 7, cy + 17.5F, new ColorRGBA(142, 145, 160, currentAlpha));
            
            float loadW = 44;
            float deleteW = 40;
            float deleteX = x + width - deleteW - 6;
            float loadX = deleteX - loadW - 4;
            
            // Load Button
            DrawUtil.drawRoundedRect(draw.getMatrices(), loadX, cy + 8, loadW, 14, BorderRadius.all(3), accent.withAlpha((int) (160 * (currentAlpha / 255F))));
            draw.drawText(Fonts.MEDIUM.getFont(5.5F), "Загрузить", loadX + (loadW - Fonts.MEDIUM.getWidth("Загрузить", 5.5F)) / 2F, cy + 12.7F, ColorRGBA.WHITE.withAlpha(currentAlpha));
            
            // Delete Button - now in GUI style (accent-based but different from load)
            ColorRGBA deleteColor = mix(new ColorRGBA(45, 46, 54, (int) (180 * (currentAlpha / 255F))), accent.withAlpha((int) (120 * (currentAlpha / 255F))), hoverProgress);
            DrawUtil.drawRoundedRect(draw.getMatrices(), deleteX, cy + 8, deleteW, 14, BorderRadius.all(3), deleteColor);
            draw.drawText(Fonts.MEDIUM.getFont(5.5F), "Удалить", deleteX + (deleteW - Fonts.MEDIUM.getWidth("Удалить", 5.5F)) / 2F, cy + 12.7F, ColorRGBA.WHITE.withAlpha(currentAlpha));
            
            draw.getMatrices().pop();
        }
        context.disableScissor();
    }

    public static List<String> getConfigNames() {
        List<String> result = new ArrayList<>();
        for (String fileName : Wyvern.getInstance().getConfigManager().configNames()) {
            int dot = fileName.lastIndexOf('.');
            result.add(dot > 0 ? fileName.substring(0, dot) : fileName);
        }
        return result;
    }

    private void renderThemes(DrawContext context, CustomDrawContext draw, float x, float y, float width, float height, int alpha, int mouseX, int mouseY, ClickGuiState state) {
        List<Theme> themes = Wyvern.getInstance().getThemeManager().getThemes();
        int columns = Math.max(1, Math.min(THEME_COLUMNS, themes.size()));
        float gridWidth = columns * THEME_SWATCH_SIZE + (columns - 1) * THEME_SWATCH_GAP;
        float startX = x + (width - gridWidth) / 2.0F;
        float rowStep = THEME_SWATCH_SIZE + THEME_SWATCH_GAP;
        float totalH = THEME_GRID_TOP + (float)Math.ceil(themes.size() / (float)columns) * rowStep - THEME_SWATCH_GAP;
        float maxScroll = Math.max(0, totalH - height);
        state.setScrollTarget(Category.THEMES, Math.max(0, Math.min(maxScroll, state.getScrollTarget(Category.THEMES))));
        float scroll = state.getScroll(Category.THEMES);
        context.enableScissor((int) x, (int) y, (int) (x + width), (int) (y + height));
        for (int i = 0; i < themes.size(); i++) {
            Theme theme = themes.get(i);
            float tx = startX + (i % columns) * rowStep;
            float ty = y + THEME_GRID_TOP + (i / columns) * rowStep - scroll;
            boolean selected = theme == Wyvern.getInstance().getThemeManager().getCurrentTheme();
            boolean hovered = MathUtil.isHovered(mouseX, mouseY, tx, ty, THEME_SWATCH_SIZE, THEME_SWATCH_SIZE);
            var hoverAnimation = state.getThemeAnimation(theme);
            hoverAnimation.update(hovered);
            float hoverProgress = hoverAnimation.getValue();

            float lift = hoverProgress * 1.5F;
            float cubeX = tx - lift / 2.0F;
            float cubeY = ty - lift / 2.0F;
            float cubeSize = THEME_SWATCH_SIZE + lift;
            if (selected) {
                DrawUtil.drawBlur(draw.getMatrices(), cubeX - 3, cubeY - 3, cubeSize + 6, cubeSize + 6,
                        12.0F, BorderRadius.all(6), theme.getColor().withAlpha((int)(150 * alpha / 255F)));
                DrawUtil.drawRoundedRect(draw.getMatrices(), cubeX - 2, cubeY - 2, cubeSize + 4, cubeSize + 4,
                        BorderRadius.all(5), ColorRGBA.WHITE.withAlpha(alpha));
            } else if (hoverProgress > 0.01F) {
                DrawUtil.drawRoundedRect(draw.getMatrices(), cubeX - 1, cubeY - 1, cubeSize + 2, cubeSize + 2,
                        BorderRadius.all(5), ColorRGBA.WHITE.withAlpha((int)(45 * hoverProgress * alpha / 255F)));
            }
            ColorRGBA themePrimary = theme.getColor().withAlpha(alpha);
            ColorRGBA themeSecondary = theme.getSecondColor().withAlpha(alpha);
            DrawUtil.drawRoundedRect(draw.getMatrices(), cubeX, cubeY, cubeSize, cubeSize, BorderRadius.all(4),
                    themePrimary, themeSecondary, themeSecondary, themePrimary);
        }
        context.disableScissor();
    }

    public List<Card> buildCards(ClickGuiState state, float contentX, float contentY, float contentW) {
        List<Module> modules = state.getModules(state.getSelectedCategoryValue());
        float cardW = (contentW - CARD_GAP) / 2F;
        float leftColumn = 0;
        float rightColumn = 0;
        int cardIndex = 0;
        for (Module module : modules) {
            int column = leftColumn <= rightColumn ? 0 : 1;
            float height = getCardHeight(module, cardW, state);
            float cardX = contentX + column * (cardW + CARD_GAP);
            float cardY = contentY + (column == 0 ? leftColumn : rightColumn);
            if (cardIndex < cardBuffer.size()) {
                cardBuffer.get(cardIndex).set(module, cardX, cardY, cardW, height);
            } else {
                cardBuffer.add(new Card(module, cardX, cardY, cardW, height));
            }
            cardIndex++;
            if (column == 0) {
                leftColumn += height + CARD_GAP;
            } else {
                rightColumn += height + CARD_GAP;
            }
        }
        if (cardBuffer.size() > cardIndex) {
            cardBuffer.subList(cardIndex, cardBuffer.size()).clear();
        }
        return cardBuffer;
    }

    private float getCardHeight(Module module, float width, ClickGuiState state) {
        float height = CARD_HEADER_HEIGHT;
        for (Setting setting : module.getSettings()) {
            if (setting == null) continue;
            var animation = state.getSettingVisibilityAnimation(setting);
            animation.update(setting.isVisible() ? 1F : 0F);
            if (animation.getValue() > 0.01F) height += getSettingHeight(setting, width) * animation.getValue();
        }
        return height + (height > CARD_HEADER_HEIGHT ? 2 : 0);
    }

    public static float getContentHeight(List<Card> cards) {
        float height = 0;
        for (Card card : cards) height = Math.max(height, card.y + card.height);
        return height;
    }

    public static float getSettingHeight(Setting setting, float width) {
        if (setting instanceof SliderSetting) return 26.0F;
        if (setting instanceof StringSetting) return 26.0F;
        if (setting instanceof ModeSetting mode) return getModeHeight(mode, width);
        if (setting instanceof MultiBooleanSetting multi) return getMultiHeight(multi, width);
        return 16.0F;
    }

    /**
     * Chip geometry only depends on the value names (static after init) and the card
     * width, yet it used to be recomputed - including per-glyph text measuring - twice
     * per setting per frame (height pass + draw pass). The relative layout is cached
     * per setting and re-derived only when the width changes; the resulting positions
     * are byte-for-byte the same as before.
     */
    private static final Map<Object, ChipLayout> CHIP_LAYOUTS = new java.util.IdentityHashMap<>();

    private record ChipLayout(float layoutWidth, int count, float[] xOffsets, float[] yOffsets, float[] widths, float height) { }

    private static ChipLayout getChipLayout(Object key, List<String> names, float width) {
        ChipLayout layout = CHIP_LAYOUTS.get(key);
        if (layout != null && layout.layoutWidth() == width && layout.count() == names.size()) {
            return layout;
        }

        int count = names.size();
        float[] xs = new float[count];
        float[] ys = new float[count];
        float[] ws = new float[count];
        float cx = 6;
        float cy = 12;
        float right = width - 6;
        for (int i = 0; i < count; i++) {
            float chipWidth = Math.max(24, Fonts.MEDIUM.getWidth(names.get(i), 6.2F) + 10);
            if (cx > 6 && cx + chipWidth > right) {
                cx = 6;
                cy += 13.0F;
            }
            xs[i] = cx;
            ys[i] = cy;
            ws[i] = chipWidth;
            cx += chipWidth + 3.0F;
        }
        layout = new ChipLayout(width, count, xs, ys, ws, 6 + cy + 10.5F);
        CHIP_LAYOUTS.put(key, layout);
        return layout;
    }

    private static ChipLayout getModeLayout(ModeSetting mode, float width) {
        List<ModeSetting.Value> values = mode.getValues();
        ChipLayout layout = CHIP_LAYOUTS.get(mode);
        if (layout != null && layout.layoutWidth() == width && layout.count() == values.size()) {
            return layout;
        }
        List<String> names = new ArrayList<>(values.size());
        for (ModeSetting.Value value : values) {
            names.add(value.getName());
        }
        return getChipLayout(mode, names, width);
    }

    private static ChipLayout getMultiLayout(MultiBooleanSetting multi, float width) {
        List<MultiBooleanSetting.Value> values = multi.getBooleanSettings();
        ChipLayout layout = CHIP_LAYOUTS.get(multi);
        if (layout != null && layout.layoutWidth() == width && layout.count() == values.size()) {
            return layout;
        }
        List<String> names = new ArrayList<>(values.size());
        for (MultiBooleanSetting.Value value : values) {
            names.add(value.getName());
        }
        return getChipLayout(multi, names, width);
    }

    public static float getModeHeight(ModeSetting mode, float width) {
        return getModeLayout(mode, width).height();
    }

    public static List<Chip> getModeChips(ModeSetting mode, float x, float y, float width) {
        ChipLayout layout = getModeLayout(mode, width);
        List<ModeSetting.Value> values = mode.getValues();
        List<Chip> chips = new ArrayList<>(layout.count());
        for (int i = 0; i < layout.count(); i++) {
            chips.add(new Chip(x + layout.xOffsets()[i], y + layout.yOffsets()[i], layout.widths()[i], 10.5F, values.get(i)));
        }
        return chips;
    }

    public record MultiChip(float x, float y, float width, float height, MultiBooleanSetting.Value value) { }

    public static float getMultiHeight(MultiBooleanSetting multi, float width) {
        return getMultiLayout(multi, width).height();
    }

    public static List<MultiChip> getMultiChips(MultiBooleanSetting multi, float x, float y, float width) {
        ChipLayout layout = getMultiLayout(multi, width);
        List<MultiBooleanSetting.Value> values = multi.getBooleanSettings();
        List<MultiChip> chips = new ArrayList<>(layout.count());
        for (int i = 0; i < layout.count(); i++) {
            chips.add(new MultiChip(x + layout.xOffsets()[i], y + layout.yOffsets()[i], layout.widths()[i], 10.5F, values.get(i)));
        }
        return chips;
    }

    private void renderCard(CustomDrawContext draw, Card card, int alpha, int mouseX, int mouseY, ClickGuiState state) {
        boolean hovered = card.contains(mouseX, mouseY);
        var hoverAnimation = state.getModuleDotAnimation(card.module);
        hoverAnimation.update(hovered);
        float hoverProgress = hoverAnimation.getValue();
        card.module.getAnimation().update(card.module.isEnabled());
        float enabledProgress = card.module.getAnimation().getValue();

        // Module cards use an extremely dense dark blur overlay for maximum readability and minimal transparency.
              DrawUtil.drawBlur(draw.getMatrices(), card.x, card.y, card.width, card.height, 25.0F, BorderRadius.all(4), new ColorRGBA(255, 255, 255, alpha));
              ColorRGBA cardOverlay = mix(new ColorRGBA(10, 11, 18, (int) (225 * (alpha / 255F))), new ColorRGBA(20, 21, 28, (int) (245 * (alpha / 255F))), hoverProgress);
              DrawUtil.drawRoundedRect(draw.getMatrices(), card.x, card.y, card.width, card.height, BorderRadius.all(4), cardOverlay);

        draw.drawText(Fonts.MEDIUM.getFont(7.5F), card.module.getName(), card.x + 6, card.y + 7.0F, ColorRGBA.WHITE.withAlpha(alpha));
        String description = card.module.getInfo().description();
        if (description != null && !description.isBlank()) {
            if (description.length() > 34) description = description.substring(0, 31) + "...";
            draw.drawText(Fonts.MEDIUM.getFont(5.8F), description, card.x + 6, card.y + 17.0F, new ColorRGBA(150, 153, 169, alpha));
        }
        String bind = state.getBindingModule() == card.module ? "..." : card.module.getKeyCode() == -1 ? "" : wtf.wyvern.render.display.Keyboard.getKeyName(card.module.getKeyCode());
        float switchX = card.x + card.width - 18;
        if (!bind.isEmpty()) {
            float bindW = Fonts.MEDIUM.getWidth(bind, 5.8F) + 8;
            float bindX = card.x + card.width - bindW - 6;
            switchX = bindX - 16;
            DrawUtil.drawRoundedRect(draw.getMatrices(), bindX, card.y + 7, bindW, 12, BorderRadius.all(2), new ColorRGBA(255, 255, 255, 16));
            draw.drawText(Fonts.MEDIUM.getFont(5.8F), bind, bindX + (bindW - Fonts.MEDIUM.getWidth(bind, 5.8F)) / 2F + 0.8F, card.y + 10.7F, new ColorRGBA(180, 183, 197, alpha));
        }
        drawModuleSwitch(draw, switchX, card.y + 7.5F, enabledProgress, alpha);
        float settingY = card.y + CARD_HEADER_HEIGHT - 1;
        for (Setting setting : card.module.getSettings()) {
            if (setting == null) continue;
            var visibility = state.getSettingVisibilityAnimation(setting);
            visibility.update(setting.isVisible() ? 1F : 0F);
            float visibilityProgress = visibility.getValue();
            if (visibilityProgress <= 0.01F) continue;
            renderSetting(draw, setting, card.x, settingY + (1F - visibilityProgress) * 2F, card.width,
                    (int) (alpha * visibilityProgress), mouseX, mouseY, state);
            settingY += getSettingHeight(setting, card.width) * visibilityProgress;
        }
    }

    private void renderSetting(CustomDrawContext draw, Setting setting, float x, float y, float width, int alpha, int mouseX, int mouseY, ClickGuiState state) {
        float rowX = x + 6;
        float rowW = width - 12;
        ColorRGBA name = new ColorRGBA(205, 207, 218, alpha);
        if (setting instanceof BooleanSetting bool) {
            var animation = state.getBooleanBackgroundAnimation(bool);
            animation.update(bool.isEnabled());
            float progress = animation.getValue();
            ColorRGBA label = mix(name, accent.withAlpha(alpha), progress);
            float switchX = x + width - 18;
            String bind = state.getBindingSetting() == bool
                    ? "..."
                    : bool.getKeyCode() == -1
                    ? ""
                    : wtf.wyvern.render.display.Keyboard.getKeyName(bool.getKeyCode());
            float bindX = switchX;
            float bindW = 0.0F;
            if (!bind.isEmpty()) {
                bindW = Fonts.MEDIUM.getWidth(bind, 5.5F) + 7;
                bindX = switchX - bindW - 3;
                DrawUtil.drawRoundedRect(draw.getMatrices(), bindX, y + 3.2F, bindW, 11,
                        BorderRadius.all(2), new ColorRGBA(255, 255, 255, 18));
                draw.drawText(Fonts.MEDIUM.getFont(5.5F), bind,
                        bindX + (bindW - Fonts.MEDIUM.getWidth(bind, 5.5F)) / 2F + 0.7F,
                        y + 6.5f, accent.withAlpha(alpha));
            }
            float maxLabelWidth = bind.isEmpty() ? switchX - rowX - 4.0F : bindX - rowX - 4.0F;
            draw.drawText(Fonts.MEDIUM.getFont(6.5F), fitText(setting.getName(), maxLabelWidth, 6.5F),
                    rowX, y + 5.5F, label);
            drawSwitch(draw, switchX, y + 3.0F, progress, alpha);
        } else if (setting instanceof SliderSetting number) {
            float target = (number.getCurrent() - number.getMin()) / Math.max(0.0001F, number.getMax() - number.getMin());
            var animation = state.getSliderAnimation(number);

            if (state.isDraggingSlider(number)) {
                // Use smooth follow (lerp) instead of fixed-duration animation to avoid stuttering
                float current = animation.getValue();
                animation.setValue(current + (target - current) * 0.5F);
            } else {
                animation.setDuration(200L);
                animation.update(target);
            }

            float p = animation.getValue();
            float animatedValue = number.getMin() + (number.getMax() - number.getMin()) * p;
            String valueText = formatNumber(animatedValue, number.getIncrement());

            draw.drawText(Fonts.MEDIUM.getFont(6.5F), setting.getName(), rowX, y + 4.5F, name);
            draw.drawText(Fonts.MEDIUM.getFont(6.2F), valueText, x + width - 6 - Fonts.MEDIUM.getWidth(valueText, 6.2F), y + 4.5F, accent.withAlpha(alpha));

            float barY = y + 12.5F;
            float barHeight = 4F;
            ColorRGBA railColor = new ColorRGBA(255, 255, 255, 30);
            DrawUtil.drawRoundedRect(draw.getMatrices(), rowX, barY, rowW, barHeight, BorderRadius.all(0.5F), railColor);
            if (p > 0.01F) {
                DrawUtil.drawRoundedRect(draw.getMatrices(), rowX, barY, Math.max(barHeight, rowW * p), barHeight, BorderRadius.all(0.5F), accent.withAlpha(alpha));
            }
            float knobSize = 5.5F;
            float knobCenterX = rowX + knobSize / 2F + (rowW - knobSize) * p;
            DrawUtil.drawRoundedRect(draw.getMatrices(), knobCenterX - knobSize / 2F, barY - 0.75F, knobSize, knobSize, BorderRadius.all(knobSize / 2F), ColorRGBA.WHITE.withAlpha(alpha));
            String min = formatNumber(number.getMin(), number.getIncrement());
            String mid = formatNumber((number.getMin() + number.getMax()) / 2F, number.getIncrement());
            String max = formatNumber(number.getMax(), number.getIncrement());
            ColorRGBA ticks = new ColorRGBA(145, 148, 164, (int) (alpha * 0.72F));
            float ticksY = y + 21.0F;
            draw.drawText(Fonts.MEDIUM.getFont(5.0F), min, rowX, ticksY, ticks);
            draw.drawText(Fonts.MEDIUM.getFont(5.0F), mid, rowX + rowW / 2F - Fonts.MEDIUM.getWidth(mid, 5.0F) / 2F, ticksY, ticks);
            draw.drawText(Fonts.MEDIUM.getFont(5.0F), max, rowX + rowW - Fonts.MEDIUM.getWidth(max, 5.0F), ticksY, ticks);
        } else if (setting instanceof ModeSetting mode) {
            draw.drawText(Fonts.MEDIUM.getFont(6.5F), setting.getName(), rowX, y + 4.1F, name);
            for (Chip chip : getModeChips(mode, x, y, width)) {
                boolean selected = chip.value() == mode.getValue();
                chip.value().getAnimation().update(selected);
                float progress = chip.value().getAnimation().getValue();
                boolean hovered = MathUtil.isHovered(mouseX, mouseY, chip.x(), chip.y(), chip.width(), chip.height());

                // Setting chips use a medium density colorless blur without a background rect.
                DrawUtil.drawBlur(draw.getMatrices(), chip.x(), chip.y(), chip.width(), chip.height(), 12.0F, BorderRadius.all(2.0F), new ColorRGBA(255, 255, 255, (int) (100 * (alpha / 255F))));
                if (progress > 0) {
                    DrawUtil.drawRoundedRect(draw.getMatrices(), chip.x(), chip.y(), chip.width(), chip.height(), BorderRadius.all(2.0F), accent.withAlpha((int) (progress * alpha)));
                } else if (hovered) {
                    DrawUtil.drawRoundedRect(draw.getMatrices(), chip.x(), chip.y(), chip.width(), chip.height(), BorderRadius.all(2.0F), new ColorRGBA(255, 255, 255, (int) (20 * (alpha / 255F))));
                }

                ColorRGBA text = mix(new ColorRGBA(190, 192, 205, alpha), ColorRGBA.WHITE.withAlpha(alpha), progress);
                draw.drawText(Fonts.MEDIUM.getFont(6.2F), chip.value().getName(), chip.x() + (chip.width() - Fonts.MEDIUM.getWidth(chip.value().getName(), 6.2F)) / 2F, chip.y() + 3.0F, text);
            }
        } else if (setting instanceof BindSetting key) {
            String value = state.getBindingSetting() == key ? "..." : key.getKeyCode() == -1 ? "None" : wtf.wyvern.render.display.Keyboard.getKeyName(key.getKeyCode());
            draw.drawText(Fonts.MEDIUM.getFont(6.5F), setting.getName(), rowX, y + 5.5F, name);
            float keyW = Fonts.MEDIUM.getWidth(value, 6.2F) + 9;
            float keyX = x + width - keyW - 6;
            DrawUtil.drawRoundedRect(draw.getMatrices(), keyX, y + 1.5F, keyW, 11, BorderRadius.all(2), new ColorRGBA(255, 255, 255, 20));
            draw.drawText(Fonts.MEDIUM.getFont(6.2F), value, keyX + (keyW - Fonts.MEDIUM.getWidth(value, 6.2F)) / 2F + 0.2F, y + 5.2F, accent.withAlpha(alpha));
        } else if (setting instanceof MultiBooleanSetting multi) {
            draw.drawText(Fonts.MEDIUM.getFont(6.5F), setting.getName(), rowX, y + 4.1F, name);
            for (MultiChip chip : getMultiChips(multi, x, y, width)) {
                chip.value().getAnimation().update(chip.value().isEnabled());
                float progress = chip.value().getAnimation().getValue();
                boolean hovered = MathUtil.isHovered(mouseX, mouseY, chip.x(), chip.y(), chip.width(), chip.height());

                // Setting chips use a medium density colorless blur without a background rect.
                DrawUtil.drawBlur(draw.getMatrices(), chip.x(), chip.y(), chip.width(), chip.height(), 12.0F, BorderRadius.all(2.0F), new ColorRGBA(255, 255, 255, (int) (100 * (alpha / 255F))));
                if (progress > 0) {
                    DrawUtil.drawRoundedRect(draw.getMatrices(), chip.x(), chip.y(), chip.width(), chip.height(), BorderRadius.all(2.0F), accent.withAlpha((int) (progress * alpha)));
                } else if (hovered) {
                    DrawUtil.drawRoundedRect(draw.getMatrices(), chip.x(), chip.y(), chip.width(), chip.height(), BorderRadius.all(2.0F), new ColorRGBA(255, 255, 255, (int) (20 * (alpha / 255F))));
                }

                ColorRGBA text = mix(new ColorRGBA(190, 192, 205, alpha), ColorRGBA.WHITE.withAlpha(alpha), progress);
                draw.drawText(Fonts.MEDIUM.getFont(6.2F), chip.value().getName(), chip.x() + (chip.width() - Fonts.MEDIUM.getWidth(chip.value().getName(), 6.2F)) / 2F, chip.y() + 3.0F, text);
            }
        } else if (setting instanceof StringSetting string) {
            draw.drawText(Fonts.MEDIUM.getFont(6.5F), setting.getName(), rowX, y + 5, name);
            boolean editing = state.getEditingStringSetting() == string;
            String value = string.getValue();
            float fieldX = rowX - 1;
            float fieldY = y + 12;
            DrawUtil.drawRoundedRect(draw.getMatrices(), fieldX, fieldY, rowW + 1, 13, BorderRadius.all(2), new ColorRGBA(255, 255, 255, editing ? 26 : 16));
            String shown = value.isBlank() && !editing ? "..." : value;
            if (editing) {
                int cursor = Math.min(value.length(), state.getStringCursor());
                shown = value.substring(0, cursor) + "|" + value.substring(cursor);
            }
            draw.drawText(Fonts.MEDIUM.getFont(5.8F), shown, fieldX + 4, fieldY + 4.2F, new ColorRGBA(210, 212, 222, alpha));
        } else if (setting instanceof ColorSetting color) {
            String value = String.format("#%06X", color.getIntColor() & 0xFFFFFF);
            draw.drawText(Fonts.MEDIUM.getFont(6.5F), setting.getName(), rowX, y + 5.5F, name);
            float valueW = Fonts.MEDIUM.getWidth(value, 5.8F) + 18;
            DrawUtil.drawRoundedRect(draw.getMatrices(), x + width - valueW - 6, y + 2.0F, valueW, 11, BorderRadius.all(2), new ColorRGBA(255, 255, 255, 20));
            DrawUtil.drawRoundedRect(draw.getMatrices(), x + width - valueW - 3.5F, y + 5.0F, 6, 6, BorderRadius.all(1), color.getColor().withAlpha(alpha));
            draw.drawText(Fonts.MEDIUM.getFont(5.5F), value, x + width - valueW + 2, y + 5.5F, new ColorRGBA(200, 202, 214, alpha));
        } else if (setting instanceof ButtonSetting) {
            DrawUtil.drawRoundedRect(draw.getMatrices(), rowX, y + 2.0F, rowW, 13, BorderRadius.all(2), accent.withAlpha(190));
            draw.drawText(Fonts.MEDIUM.getFont(6.2F), setting.getName(), rowX + (rowW - Fonts.MEDIUM.getWidth(setting.getName(), 6.2F)) / 2F, y + 5.5F, ColorRGBA.WHITE.withAlpha(alpha));
        } else if (setting instanceof ItemSelectSetting items) {
            String value = items.getItemsById().isEmpty() ? "Пусто" : items.getItemsById().size() + " выбрано";
            draw.drawText(Fonts.MEDIUM.getFont(6.5F), setting.getName(), rowX, y + 5.5F, name);
            draw.drawText(Fonts.MEDIUM.getFont(6.2F), value, x + width - 6 - Fonts.MEDIUM.getWidth(value, 6.2F), y + 5.5F, accent.withAlpha(alpha));
        } else {
            draw.drawText(Fonts.MEDIUM.getFont(6.5F), setting.getName(), rowX, y + 5.5F, name);
        }
    }

    private String formatNumber(float value, float increment) {
        if (increment >= 1.0F) {
            return String.valueOf((int) value);
        }
        int hundredths = Math.round(value * 100.0F);
        boolean negative = hundredths < 0;
        int absolute = Math.abs(hundredths);
        int whole = absolute / 100;
        int fraction = absolute % 100;
        String prefix = negative ? "-" : "";
        if (fraction == 0) {
            return prefix + whole;
        }
        if (fraction % 10 == 0) {
            return prefix + whole + "." + (fraction / 10);
        }
        return prefix + whole + "." + (fraction < 10 ? "0" : "") + fraction;
    }

    // Truncation results are pure functions of (text, maxWidth, size); cache them so the
    // per-character measuring loop doesn't run every frame for every visible label.
    private static final Map<String, FitTextEntry> FIT_TEXT_CACHE = new java.util.HashMap<>();

    private record FitTextEntry(float maxWidth, float size, String result) { }

    private String fitText(String value, float maxWidth, float size) {
        if (value == null || maxWidth <= 4.0F) return value;
        FitTextEntry cached = FIT_TEXT_CACHE.get(value);
        if (cached != null && cached.maxWidth() == maxWidth && cached.size() == size) {
            return cached.result();
        }

        String result;
        if (Fonts.MEDIUM.getWidth(value, size) <= maxWidth) {
            result = value;
        } else {
            String suffix = "...";
            float suffixWidth = Fonts.MEDIUM.getWidth(suffix, size);
            StringBuilder builder = new StringBuilder();
            float width = 0.0F;
            for (int i = 0; i < value.length(); i++) {
                String character = String.valueOf(value.charAt(i));
                float characterWidth = Fonts.MEDIUM.getWidth(character, size);
                if (width + characterWidth + suffixWidth > maxWidth) break;
                builder.append(character);
                width += characterWidth;
            }
            result = builder.isEmpty() ? suffix : builder + suffix;
        }

        if (FIT_TEXT_CACHE.size() > 512) {
            FIT_TEXT_CACHE.clear();
        }
        FIT_TEXT_CACHE.put(value, new FitTextEntry(maxWidth, size, result));
        return result;
    }

    private ColorRGBA mix(ColorRGBA from, ColorRGBA to, float progress) {
        return ColorRGBA.lerp(from, to, progress);
    }

    private void drawCheckbox(CustomDrawContext draw, float x, float y, float progress, int alpha) {
        float size = 11.0F;
        // Background square
        DrawUtil.drawRoundedRect(draw.getMatrices(), x, y, size, size, BorderRadius.all(2.5F), mix(new ColorRGBA(255, 255, 255, 20), accent.withAlpha(alpha), progress));
        
        // Checkmark (only visible if progress > 0)
        if (progress > 0.1F) {
            String checkmark = "S";
            float checkScale = 7.5F;
            float checkX = x + (size - Fonts.ICONS.getWidth(checkmark, checkScale)) / 2F + 1.0F;
            float checkY = y + (size - 6.5F) / 2F; // Manual vertical centering for icon fonts
            draw.drawText(Fonts.ICONS.getFont(checkScale), checkmark, checkX, checkY, ColorRGBA.WHITE.withAlpha((int) (alpha * progress)));
        }
    }

    private void drawSwitch(CustomDrawContext draw, float x, float y, float progress, int alpha) {
        drawCheckbox(draw, x, y, progress, alpha);
    }

    private void drawModuleSwitch(CustomDrawContext draw, float x, float y, float progress, int alpha) {
        drawCheckbox(draw, x, y, progress, alpha);
    }
}

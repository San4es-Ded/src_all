package su.sacura.display.csgui.helper;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import su.sacura.Sacura;
import su.sacura.display.csgui.components.ModuleComponent;
import su.sacura.display.csgui.components.ThemeComponent;
import su.sacura.display.csgui.components.other.SearchComponent;
import su.sacura.display.csgui.helper.ThemeStorage;
import su.sacura.display.csgui.themes.Theme;
import su.sacura.display.csgui.themes.ThemeManager;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.impl.Category;
import su.sacura.util.impl.math.helper.AnimationUtil;
import su.sacura.util.impl.render.RenderUtils;
import su.sacura.util.impl.render.providers.ColorProvider;
import su.sacura.util.impl.render.providers.FontProvider;

public class WindowRender {
    private float x;
    private float y;
    private float width;
    private float height;
    private float targetX;
    private float targetY;
    private boolean dragging;
    private float dragX;
    private float dragY;
    private Category selectedCategory = Category.MOVEMENT;
    private final List<ModuleComponent> moduleComponents = new ArrayList<ModuleComponent>();
    private final List<ThemeComponent> themeComponents = new ArrayList<ThemeComponent>();
    private float scrollY = 0.0f;
    private float targetScrollY = 0.0f;
    private float maxContentHeight = 0.0f;
    private final SearchComponent searchComponent;
    public Module selectedModule;
    private float backgroundFlashAlpha = 0.0f;
    private float alpha = 0.0f;
    private float targetAlpha = 0.0f;
    private boolean isOpening = false;
    private boolean isClosing = false;

    private float originalY;

    private static final float DRAG_LERP_60 = 0.080F;
    private static final float SCROLL_LERP_60 = 0.250F;
    private static final float OPEN_ALPHA_LERP_60 = 0.200F;
    private static final float CLOSE_ALPHA_LERP_60 = 0.300F;

    private static final float DRAG_RATE = rate(DRAG_LERP_60);
    private static final float SCROLL_RATE = rate(SCROLL_LERP_60);
    private static final float OPEN_ALPHA_RATE = rate(OPEN_ALPHA_LERP_60);
    private static final float CLOSE_ALPHA_RATE = rate(CLOSE_ALPHA_LERP_60);

    private static final float CATEGORY_ICON_SIZE = 10.0F;
    private static final float CATEGORY_ICON_GAP = 12.0F;
    private static final float CATEGORY_BAR_PADDING = 12.0F;
    private static final float CATEGORY_BAR_Y = 7.0F;
    private static final float CATEGORY_BAR_HEIGHT = 25.0F;
    private static final float CATEGORY_ICON_BASELINE = 15.0F;
    private static final float SEARCH_ICON_Y = 14.0F;
    private static final float CATEGORY_CLICK_SCALE = 1.6F;
    private static final float CATEGORY_CLICK_OFFSET_Y = 4.0F;
    private static final float CATEGORY_CLICK_OFFSET_X = 1.0F;

    private static final float SCROLLBAR_VISUAL_WIDTH = 3.0F;
    private static final float SCROLLBAR_CLICK_WIDTH = 12.0F;
    private static final float SCROLLBAR_RIGHT_OFFSET = 4.0F;
    private static final float SCROLLBAR_MIN_ALPHA = 120.0F;
    private static final float SCROLLBAR_HOVER_ALPHA = 255.0F;

    private long lastFrameNanos = 0L;
    private float frameDelta = 1.0F / 60.0F;

    private boolean scrollbarDragging = false;
    private float scrollbarDragOffset = 0.0F;

    private static float rate(float baseCoeffAt60) {
        if (baseCoeffAt60 >= 1.0F) return 1e9F;
        if (baseCoeffAt60 <= 0.0F) return 0.0F;
        return (float)(-Math.log(1.0 - baseCoeffAt60) * 60.0);
    }

    private float factor(float rate) {
        if (rate <= 0.0F) return 0.0F;
        float v = 1.0F - (float)Math.exp(-rate * this.frameDelta);
        if (v < 0.0F) v = 0.0F;
        if (v > 1.0F) v = 1.0F;
        return v;
    }

    public WindowRender(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.originalY = y;
        this.width = width;
        this.height = height;
        this.targetX = x;
        this.targetY = y;
        this.searchComponent = new SearchComponent(this);
        ThemeManager.init();
        this.refreshModules();
        this.refreshThemes();
    }

    public void refreshModules() {
        this.moduleComponents.clear();
        List<Module> modules = Sacura.getInstance().getModuleManager().module;
        if (modules != null) {
            for (Module module : modules) {
                this.moduleComponents.add(new ModuleComponent(module, this.width - 20.0f, 20.0f));
            }
        }
    }

    public void refreshThemes() {
        this.themeComponents.clear();
        for (Theme theme : ThemeManager.themes) {
            this.themeComponents.add(new ThemeComponent(theme, this.width - 20.0f, 20.0f));
        }
    }

    public void onOpen() {
        this.isOpening = true;
        this.isClosing = false;
        this.targetAlpha = 1.0f;
        this.y = this.originalY;
        this.targetY = this.originalY;
    }

    public void onClose() {
        this.isClosing = true;
        this.isOpening = false;
        this.targetAlpha = 0.0f;
        // Позицию не трогаем — окно остаётся на месте, гаснет только alpha
    }

    public void tick() {
        long now = System.nanoTime();
        if (this.lastFrameNanos != 0L) {
            float dt = (now - this.lastFrameNanos) / 1_000_000_000.0F;
            if (dt > 0.1F) dt = 0.1F;
            if (dt < 0.0F) dt = 0.0F;
            this.frameDelta = dt;
        }
        this.lastFrameNanos = now;

        float alphaRate = this.isClosing ? CLOSE_ALPHA_RATE : OPEN_ALPHA_RATE;
        this.alpha = AnimationUtil.lerp(this.alpha, this.targetAlpha, factor(alphaRate));
    }

    public float getAlpha() {
        return this.alpha;
    }

    public boolean isClosing() {
        return this.isClosing;
    }

    private float categoryIconWidth(Category category) {
        return FontProvider.category.getWidth(category.iconChar, CATEGORY_ICON_SIZE);
    }

    private float categoryIconHeight() {
        return FontProvider.category.getHeight(CATEGORY_ICON_SIZE);
    }

    private float categoryBarWidth() {
        float total = 0.0F;
        Category[] values = Category.values();
        for (int i = 0; i < values.length; i++) {
            total += categoryIconWidth(values[i]);
            if (i < values.length - 1) total += CATEGORY_ICON_GAP;
        }
        return total + CATEGORY_BAR_PADDING * 2.0F;
    }

    private float categoryBarX() {
        return this.x + (this.width - categoryBarWidth()) / 2.0F;
    }

    private float scrollbarThumbHeight(float viewportHeight) {
        if (this.maxContentHeight <= 0.0F) return viewportHeight;
        return Math.max(20.0F, viewportHeight / this.maxContentHeight * viewportHeight);
    }

    private float scrollbarThumbY(float viewportHeight) {
        float thumbHeight = scrollbarThumbHeight(viewportHeight);
        float trackTop = this.y + 45.0F;
        float travel = viewportHeight - thumbHeight;
        if (travel <= 0.0F) return trackTop;
        float denom = this.maxContentHeight - viewportHeight;
        float ratio = denom <= 0.0F ? 0.0F : -this.scrollY / denom;
        if (ratio < 0.0F) ratio = 0.0F;
        if (ratio > 1.0F) ratio = 1.0F;
        return trackTop + ratio * travel;
    }

    private float scrollbarVisualX() {
        return this.x + this.width - SCROLLBAR_RIGHT_OFFSET - SCROLLBAR_VISUAL_WIDTH;
    }

    private float scrollbarClickX() {
        return this.x + this.width - SCROLLBAR_RIGHT_OFFSET - SCROLLBAR_CLICK_WIDTH;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        float lowestY;
        boolean canScroll;
        if (this.alpha < 0.01f && !this.isOpening) {
            return;
        }
        int globalAlpha = (int)(this.alpha * 255.0f);
        if (this.dragging) {
            this.targetX = (float)mouseX - this.dragX;
            this.targetY = (float)mouseY - this.dragY;
        }
        float dragFactor = factor(DRAG_RATE);
        this.x = AnimationUtil.lerp(this.x, this.targetX, dragFactor);
        this.y = AnimationUtil.lerp(this.y, this.targetY, dragFactor);
        RenderUtils.blur(context.getMatrices().peek().getPositionMatrix(), this.x, this.y, this.width, this.height, 5.0f, 12.0f, ColorProvider.rgba(255, 255, 255, globalAlpha));
        Color bg = ThemeStorage.backgroundColor;
        RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), this.x, this.y, this.width, this.height, 5.0f, new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), globalAlpha).getRGB());
        if (this.backgroundFlashAlpha > 0.0f) {
            Color accent = ThemeStorage.accentColor;
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), this.x, this.y, this.width, this.height, 5.0f, new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), (int)(this.backgroundFlashAlpha * (float)globalAlpha)).getRGB());
            this.backgroundFlashAlpha -= this.frameDelta * 2.0f;
            if (this.backgroundFlashAlpha < 0.0f) this.backgroundFlashAlpha = 0.0f;
        }
        if (!this.searchComponent.isOpen()) {
            float barWidth = categoryBarWidth();
            float barX = categoryBarX();
            Color accent = ThemeStorage.accentColor;
            Color text = ThemeStorage.textColor;

            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), barX, this.y + CATEGORY_BAR_Y, barWidth, CATEGORY_BAR_HEIGHT, 8.0f, ColorProvider.rgba(255, 255, 255, (int)(5.0f * this.alpha)));
            RenderUtils.border(context.getMatrices().peek().getPositionMatrix(), barX, this.y + CATEGORY_BAR_Y, barWidth, CATEGORY_BAR_HEIGHT, 8.0f, 1.1f, 0.1f, 0.1f, ColorProvider.rgba(255, 255, 255, (int)(5.0f * this.alpha)));

            float catX = barX + CATEGORY_BAR_PADDING;
            float catY = this.y + CATEGORY_ICON_BASELINE;
            for (Category category : Category.values()) {
                boolean selected = category == this.selectedCategory;
                int color = selected ? new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), globalAlpha).getRGB() : new Color(text.getRed(), text.getGreen(), text.getBlue(), 100 * globalAlpha / 255).getRGB();
                FontProvider.category.draw(context.getMatrices().peek().getPositionMatrix(), category.iconChar, catX, catY, CATEGORY_ICON_SIZE, color);
                catX += this.categoryIconWidth(category) + CATEGORY_ICON_GAP;
            }

            FontProvider.monoton.draw(context.getMatrices().peek().getPositionMatrix(), "g", this.x + (this.width - 150.0f) / 2.0f + 200.0f, this.y + SEARCH_ICON_Y, 11.0f, new Color(text.getRed(), text.getGreen(), text.getBlue(), 100 * globalAlpha / 255).getRGB());
        }
        RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), this.x, this.y, this.width, 40.0f, 5.0f, 0.0f, 0.0f, 5.0f, ColorProvider.rgba(255, 255, 255, (int)(2.0f * this.alpha)), ColorProvider.rgba(255, 255, 255, (int)(2.0f * this.alpha)), ColorProvider.rgba(255, 255, 255, (int)(2.0f * this.alpha)), ColorProvider.rgba(255, 255, 255, (int)(2.0f * this.alpha)));
        Color border = ThemeStorage.borderColor;
        RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), this.x, this.y + 39.0f, this.width, 1.0f, 0.0f, new Color(border.getRed(), border.getGreen(), border.getBlue(), globalAlpha).getRGB());
        FontProvider.logo.draw(context.getMatrices().peek().getPositionMatrix(), "a", this.x + 12.0f, this.y + 6.0f, 24.0f, new Color(150, 150, 150, globalAlpha).getRGB());
        context.enableScissor((int)this.x, (int)(this.y + 45.0f), (int)(this.x + this.width), (int)(this.y + this.height));
        float viewportHeight = this.height - 45.0f;
        boolean bl = canScroll = this.maxContentHeight > viewportHeight;
        if (canScroll) {
            if (this.targetScrollY > 0.0F) this.targetScrollY = 0.0F;
            float minScroll = -(this.maxContentHeight - viewportHeight);
            if (this.targetScrollY < minScroll) this.targetScrollY = minScroll;
        } else {
            this.targetScrollY = 0.0F;
        }
        if (this.scrollbarDragging) {
            float thumbHeight = scrollbarThumbHeight(viewportHeight);
            float trackTop = this.y + 45.0F;
            float travel = viewportHeight - thumbHeight;
            if (travel > 0.0F) {
                float desiredThumbY = (float)mouseY - this.scrollbarDragOffset;
                float clamped = Math.max(trackTop, Math.min(trackTop + travel, desiredThumbY));
                float ratio = (clamped - trackTop) / travel;
                float newTarget = -ratio * (this.maxContentHeight - viewportHeight);
                this.targetScrollY = newTarget;
                this.scrollY = newTarget;
            }
        }
        float scrollFactor = factor(SCROLL_RATE);
        this.scrollY = AnimationUtil.lerp(this.scrollY, this.targetScrollY, scrollFactor);

        float startX = this.x + 10.0f;

        float[] columnOffsets = new float[3];
        float listStartY = this.y + 45.0f + this.scrollY;
        if (this.selectedCategory == Category.THEMES) {
            float btnY = this.y + 48.0f + this.scrollY;
            float btnCreateW = 80.0f;
            float btnH = 18.0f;
            Color accent = ThemeStorage.accentColor;
            int btnColor = new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), (int)((double)globalAlpha * 0.8)).getRGB();
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), startX + 400.0f, btnY, btnCreateW, btnH, 5.0f, btnColor);
            FontProvider.regular.drawCentered(context.getMatrices().peek().getPositionMatrix(), "Создать", 400.0f + startX + btnCreateW / 2.0f, btnY + 5.0f, 7.0f, new Color(255, 255, 255, globalAlpha).getRGB());
            listStartY += 25.0f;
        }
        for (int i = 0; i < 3; ++i) {
            columnOffsets[i] = listStartY;
        }
        float moduleWidth = (this.width - 20.0f - 10.0f) / 3.0f;
        int index = 0;
        if (this.selectedCategory == Category.THEMES) {
            for (ThemeComponent component : this.themeComponents) {
                int columnIndex = index % 3;
                float currentModuleX = startX + (float)columnIndex * (moduleWidth + 5.0f);
                float currentModuleY = columnOffsets[columnIndex];
                component.width = moduleWidth;
                component.render(context, currentModuleX, currentModuleY + 3.0f, mouseX, mouseY, globalAlpha);
                int n = columnIndex;
                columnOffsets[n] = columnOffsets[n] + (component.getHeight() + 3.0f);
                ++index;
            }
        } else {
            for (ModuleComponent component : this.moduleComponents) {
                if (component.module == this.selectedModule) {
                    component.flashAlpha = 1.0f;
                    this.selectedModule = null;
                }
                if (component.module.category != this.selectedCategory) continue;
                int columnIndex = index % 3;
                float currentModuleX = startX + (float)columnIndex * (moduleWidth + 5.0f);
                float currentModuleY = columnOffsets[columnIndex];
                component.width = moduleWidth;
                component.render(context, currentModuleX, currentModuleY, mouseX, mouseY, globalAlpha);
                int n = columnIndex;
                columnOffsets[n] = columnOffsets[n] + (component.getTotalHeight() + 3.0f);
                ++index;
            }
        }
        if (columnOffsets[1] > (lowestY = columnOffsets[0])) {
            lowestY = columnOffsets[1];
        }
        if (columnOffsets[2] > lowestY) {
            lowestY = columnOffsets[2];
        }
        this.maxContentHeight = lowestY - (this.y + 45.0f + this.scrollY);
        this.maxContentHeight += 10.0f;
        context.disableScissor();

        canScroll = this.maxContentHeight > viewportHeight;
        if (canScroll) {
            float thumbHeight = scrollbarThumbHeight(viewportHeight);
            float thumbY = scrollbarThumbY(viewportHeight);
            float visualX = scrollbarVisualX();
            float clickX = scrollbarClickX();
            boolean hovered = this.isHovered(mouseX, mouseY, clickX, thumbY, SCROLLBAR_CLICK_WIDTH, thumbHeight);
            float targetAlphaValue = (hovered || this.scrollbarDragging) ? SCROLLBAR_HOVER_ALPHA : SCROLLBAR_MIN_ALPHA;
            int scrollbarAlpha = (int)(targetAlphaValue * this.alpha);
            Color accent = ThemeStorage.accentColor;
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), visualX, thumbY, SCROLLBAR_VISUAL_WIDTH, thumbHeight - 4.0f, 1.5f, new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), scrollbarAlpha).getRGB());
        }

        for (ModuleComponent component : this.moduleComponents) {
            if (!component.isBinding()) continue;
            component.renderBindPopup(context, globalAlpha);
        }
        if (this.searchComponent.isOpen()) {
            RenderUtils.blur(context.getMatrices().peek().getPositionMatrix(), this.x, this.y, this.width, this.height, 5.0f, 5.0f, 5.0f, 5.0f, 10.0f, ColorProvider.rgba(255, 255, 255, globalAlpha));
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), this.x, this.y, this.width, this.height, 5.0f, ColorProvider.rgba(0, 0, 0, (int)(160.0f * this.alpha)));
            this.searchComponent.render(context, mouseX, mouseY, delta, this.x, this.y, this.width, this.height, globalAlpha);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        block21: {
            if (this.alpha < 0.1f) {
                return false;
            }
            if (this.searchComponent.isOpen()) {
                if (this.searchComponent.mouseClicked(mouseX, mouseY, button, this.x, this.y, this.width, this.height)) {
                    return true;
                }
            } else {
                float viewportHeight = this.height - 45.0f;
                boolean canScroll = this.maxContentHeight > viewportHeight;
                if (canScroll && button == 0) {
                    float thumbHeight = scrollbarThumbHeight(viewportHeight);
                    float thumbY = scrollbarThumbY(viewportHeight);
                    float clickX = scrollbarClickX();
                    if (this.isHovered(mouseX, mouseY, clickX, thumbY, SCROLLBAR_CLICK_WIDTH, thumbHeight)) {
                        this.scrollbarDragging = true;
                        this.scrollbarDragOffset = (float)mouseY - thumbY;
                        return true;
                    }
                    float trackTop = this.y + 45.0F;
                    if (this.isHovered(mouseX, mouseY, clickX, trackTop, SCROLLBAR_CLICK_WIDTH, viewportHeight)) {
                        float travel = viewportHeight - thumbHeight;
                        if (travel > 0.0F) {
                            float desiredThumbY = (float)mouseY - thumbHeight / 2.0F;
                            float clamped = Math.max(trackTop, Math.min(trackTop + travel, desiredThumbY));
                            float ratio = (clamped - trackTop) / travel;
                            this.targetScrollY = -ratio * (this.maxContentHeight - viewportHeight);
                            this.scrollY = this.targetScrollY;
                            this.scrollbarDragging = true;
                            this.scrollbarDragOffset = thumbHeight / 2.0F;
                            return true;
                        }
                    }
                }
                float barX = categoryBarX();
                float barY = this.y + CATEGORY_BAR_Y;
                float barW = categoryBarWidth();
                float barH = CATEGORY_BAR_HEIGHT;
                if (this.isHovered(mouseX, mouseY, barX, barY, barW, barH) && button == 0) {
                    float catX = barX + CATEGORY_BAR_PADDING;
                    float catY = this.y + CATEGORY_ICON_BASELINE;
                    for (Category category : Category.values()) {
                        float iconW = this.categoryIconWidth(category);
                        float iconH = this.categoryIconHeight();
                        float centerX = catX + iconW / 2.0F + CATEGORY_CLICK_OFFSET_X;
                        float centerY = catY + CATEGORY_CLICK_OFFSET_Y;
                        float clickW = iconW * CATEGORY_CLICK_SCALE;
                        float clickH = iconH * CATEGORY_CLICK_SCALE;
                        float clickX = centerX - clickW / 2.0F;
                        float clickY = centerY - clickH / 2.0F;
                        if (this.isHovered(mouseX, mouseY, clickX, clickY, clickW, clickH)) {
                            this.selectedCategory = category;
                            this.targetScrollY = 0.0f;
                            this.scrollY = 0.0f;
                            return true;
                        }
                        catX += iconW + CATEGORY_ICON_GAP;
                    }
                    return true;
                }
                float rectX = this.x + (this.width - 150.0f) / 2.0f;
                if (this.isHovered(mouseX, mouseY, rectX + 200.0f, this.y + SEARCH_ICON_Y, FontProvider.monoton.getWidth("B", 11.0f), 11.0f) && button == 0) {
                    this.searchComponent.setOpen(true);
                    return true;
                }
            }
            for (ModuleComponent component : this.moduleComponents) {
                if (!component.isBinding() || !component.mouseClicked(mouseX, mouseY, button)) continue;
                return true;
            }
            if (this.isHovered(mouseX, mouseY, this.x, this.y, this.width, 40.0f)) {
                if (this.selectedCategory == Category.THEMES) {
                    float startX = this.x + 10.0f;
                    float btnY = this.y + 50.0f + this.scrollY;
                    float btnCreateW = 80.0f;
                    float btnImportW = 70.0f;
                    float btnH = 18.0f;
                    if (this.isHovered(mouseX, mouseY, startX + 400.0f, btnY, btnCreateW, btnH)) {
                        int i = 1;
                        while (true) {
                            int fi = i++;
                            if (ThemeManager.themes.stream().noneMatch(t -> t.name.equals("Моя Тема " + fi))) break;
                        }
                        ThemeManager.createNewTheme("Моя Тема " + i, "User");
                        this.themeComponents.clear();
                        return true;
                    }
                    if (this.isHovered(mouseX, mouseY, startX + btnCreateW + 5.0f, btnY, btnImportW, btnH)) {
                        return true;
                    }
                }
                this.dragging = true;
                this.dragX = (float)(mouseX - (double)this.x);
                this.dragY = (float)(mouseY - (double)this.y);
                return true;
            }
            if (this.searchComponent.isOpen() || !this.isHovered(mouseX, mouseY, this.x, this.y + 45.0f, this.width, this.height - 45.0f)) break block21;
            if (this.selectedCategory == Category.THEMES) {
                float startX = this.x + 10.0f;
                float btnY = this.y + 50.0f + this.scrollY;
                for (ThemeComponent component : this.themeComponents) {
                    if (!component.mouseClicked(mouseX, mouseY, button)) continue;
                    return true;
                }
            } else {
                for (ModuleComponent component : this.moduleComponents) {
                    if (component.module.category != this.selectedCategory || !component.mouseClicked(mouseX, mouseY, button)) continue;
                    return true;
                }
            }
        }
        return this.isHovered(mouseX, mouseY, this.x, this.y, this.width, this.height);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.alpha < 0.1f) {
            return false;
        }
        if (button == 0 && this.scrollbarDragging) {
            this.scrollbarDragging = false;
            return true;
        }
        if (this.dragging) {
            this.originalY = this.targetY;
        }
        this.dragging = false;
        for (ModuleComponent moduleComponent : this.moduleComponents) {
            if (!moduleComponent.mouseReleased(button)) continue;
            return true;
        }
        if (this.selectedCategory == Category.THEMES) {
            for (ThemeComponent themeComponent : this.themeComponents) {
                themeComponent.mouseReleased(button);
            }
        }
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.alpha < 0.1f) {
            return false;
        }
        if (this.scrollbarDragging) {
            return true;
        }
        if (this.dragging) {
            return true;
        }
        if (this.searchComponent.isOpen()) {
            return false;
        }
        for (ModuleComponent component : this.moduleComponents) {
            if (!component.mouseDragged(mouseX, mouseY, button)) continue;
            return true;
        }
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.alpha < 0.1f) {
            return false;
        }
        if (this.scrollbarDragging) {
            return true;
        }
        if (this.searchComponent.isOpen()) {
            return false;
        }
        if (this.isHovered(mouseX, mouseY, this.x, this.y, this.width, this.height)) {
            this.targetScrollY = (float)((double)this.targetScrollY + verticalAmount * 20.0);
            return true;
        }
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.alpha < 0.1f) {
            return false;
        }
        if (this.searchComponent.isOpen()) {
            return this.searchComponent.keyPressed(keyCode, scanCode, modifiers);
        }
        for (ModuleComponent component : this.moduleComponents) {
            if (!component.keyPressed(keyCode, scanCode, modifiers)) continue;
            return true;
        }
        if (keyCode == 78 && (modifiers & 2) != 0) {
            this.searchComponent.setOpen(true);
            return true;
        }
        return false;
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (this.alpha < 0.1f) {
            return false;
        }
        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        if (this.alpha < 0.1f) {
            return false;
        }
        if (this.searchComponent.isOpen()) {
            return this.searchComponent.charTyped(chr, modifiers);
        }
        for (ModuleComponent component : this.moduleComponents) {
            if (component.module.category != this.selectedCategory || !component.charTyped(chr, modifiers)) continue;
            return true;
        }
        return false;
    }

    private boolean isHovered(double mouseX, double mouseY, float x, float y, float width, float height) {
        return mouseX >= (double)x && mouseX <= (double)(x + width) && mouseY >= (double)y && mouseY <= (double)(y + height);
    }

    public void switchToCategory(Category category) {
        this.selectedCategory = category;
        this.targetScrollY = 0.0f;
        this.scrollY = 0.0f;
    }

    public void setSelectedModule(Module module) {
        this.selectedModule = module;
        this.backgroundFlashAlpha = 1.0f;
    }
}
package wtf.wyvern.client.gui.screens.menu.wonderful;

import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.setting.Setting;
import wtf.wyvern.client.modules.api.setting.impl.*;
import wtf.wyvern.utility.math.MathUtil;

import java.util.List;

/** Click, bind, search and scroll handling for the adapted CS layout. */
public final class CsGuiInputHandler {
    private final ClickGuiState state;
    private final CsGuiRenderer renderer;
    private SliderSetting draggingSlider;
    private float draggingSliderX;
    private float draggingSliderWidth;

    public CsGuiInputHandler(ClickGuiState state, CsGuiRenderer renderer) {
        this.state = state;
        this.renderer = renderer;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, Window window) {
        if (window == null) return false;
        float x = state.getX();
        float y = state.getY() + state.getRenderOffsetY();

        if (state.getBindingModule() != null && button >= 1) {
            state.getBindingModule().setKeyCode(button);
            state.setBindingModule(null);
            return true;
        }
        if (state.getBindingSetting() != null && button >= 1) {
            if (state.getBindingSetting() instanceof BindSetting key) {
                key.setKeyCode(button);
            } else if (state.getBindingSetting() instanceof BooleanSetting bool) {
                bool.setKeyCode(button);
            }
            state.setBindingSetting(null);
            return true;
        }

        float searchX = x + 9.0F;
        float searchY = y + 42.5F;
        float searchW = CsGuiRenderer.SIDEBAR_WIDTH - 17.5F;

        if (button == 0 && MathUtil.isHovered(mouseX, mouseY, x, y, CsGuiRenderer.PANEL_WIDTH, CsGuiRenderer.HEADER_HEIGHT)) {
            if (!MathUtil.isHovered(mouseX, mouseY, searchX, searchY, searchW, 16)) {
                state.setDraggingGui(true);
                state.setGuiDragX((float) mouseX - x);
                state.setGuiDragY((float) mouseY - y);
            }
        }

        if (button == 0 && MathUtil.isHovered(mouseX, mouseY, searchX, searchY, searchW, 16)) {
            state.setSearchActive(true);
            state.setSearchCursor(state.getSearchText().length());
            return true;
        }
        if (button == 0 && state.isSearchActive()) state.setSearchActive(false);

        // Keep hit boxes on the exact same anchors as CsGuiRenderer.renderSidebar.
        float categoryY = y + 77.5F;
        final float step = 17.5F;
        for (int i = 0; i < Category.THEMES.ordinal(); i++) {
            if (button == 0 && MathUtil.isHovered(mouseX, mouseY, x + 4, categoryY - 5.0F, CsGuiRenderer.SIDEBAR_WIDTH - 12, 18.0F)) {
                selectCategory(i);
                return true;
            }
            categoryY += step;
        }
        float themesY = categoryY + 12.0F;
        if (button == 0 && MathUtil.isHovered(mouseX, mouseY, x + 4, themesY - 5.0F, CsGuiRenderer.SIDEBAR_WIDTH - 12, 18.0F)) {
            selectCategory(Category.THEMES.ordinal());
            return true;
        }
        float figuraY = themesY + step;
        if (button == 0 && MathUtil.isHovered(mouseX, mouseY, x + 4, figuraY - 5.0F, CsGuiRenderer.SIDEBAR_WIDTH - 12, 18.0F)) {
            selectCategory(Category.FIGURA.ordinal());
            return true;
        }
        float configY = figuraY + step;
        if (button == 0 && MathUtil.isHovered(mouseX, mouseY, x + 4, configY - 5.0F, CsGuiRenderer.SIDEBAR_WIDTH - 12, 18.0F)) {
            state.setConfigPage(true);
            state.setSearchText("");
            state.setSearchActive(false);
            return true;
        }

        float contentX = x + CsGuiRenderer.SIDEBAR_WIDTH + CsGuiRenderer.CONTENT_PADDING;
        float transition = state.getCategoryContentProgress();
        contentX += state.getCategoryContentDirection() * (1.0F - transition) * 22.0F;
        float contentY = y + CsGuiRenderer.HEADER_HEIGHT + CsGuiRenderer.CONTENT_PADDING;
        float contentW = CsGuiRenderer.PANEL_WIDTH - CsGuiRenderer.SIDEBAR_WIDTH - CsGuiRenderer.CONTENT_PADDING * 2;
        float contentH = CsGuiRenderer.PANEL_HEIGHT - CsGuiRenderer.HEADER_HEIGHT - CsGuiRenderer.CONTENT_PADDING * 2;
        if (state.isConfigPage()) {
            return handleConfigClick(mouseX, mouseY, button, contentX, contentY, contentW, contentH);
        }
        if (state.getSelectedCategoryValue() == Category.THEMES) {
            return handleThemeClick(mouseX, mouseY, button, contentX, contentY, contentW, contentH);
        }
        if (state.getSelectedCategoryValue() == Category.FIGURA) {
            return renderer.getFiguraPage().mouseClicked(mouseX, mouseY, button);
        }

        List<CsGuiRenderer.Card> cards = renderer.buildCards(state, contentX, contentY, contentW - CsGuiRenderer.SCROLLBAR_GUTTER);
        clampScroll(cards, contentY, contentH);
        float scroll = state.getScroll(state.getSelectedCategoryValue());
        for (CsGuiRenderer.Card base : cards) {
            CsGuiRenderer.Card card = new CsGuiRenderer.Card(base.module, base.x, base.y - scroll, base.width, base.height);
            if (!card.contains(mouseX, mouseY)) continue;
            if (card.headerContains(mouseX, mouseY)) {
                if (button == 0) card.module.toggle();
                else if (button == 2 || button == 1) state.setBindingModule(card.module);
                return true;
            }
            return handleSettingClick(card, mouseX, mouseY, button);
        }
        return false;
    }

    private void selectCategory(int index) {
        state.setConfigPage(false);
        if (state.getSelectedCategory() == index) return;
        state.setSelectedCategory(index);
        state.setSearchText("");
        state.setSearchActive(false);
    }

    private boolean handleConfigClick(double mouseX, double mouseY, int button, float x, float y, float width, float height) {
        float fieldY = y + 16;
        float fieldW = width - 51;
        if (button == 0 && MathUtil.isHovered(mouseX, mouseY, x, fieldY, fieldW, 16)) {
            state.setConfigNameActive(true);
            state.setConfigNameCursor(state.getConfigName().length());
            return true;
        }
        if (button == 0 && MathUtil.isHovered(mouseX, mouseY, x + fieldW + 4, fieldY, 47, 16)) {
            String name = sanitizeConfigName(state.getConfigName());
            if (!name.isEmpty()) {
                Wyvern.getInstance().getConfigManager().saveConfig(name);
                state.setConfigName("");
                state.setConfigNameActive(false);
            }
            return true;
        }
        if (button == 0 && state.isConfigNameActive()) state.setConfigNameActive(false);

        List<String> configs = CsGuiRenderer.getConfigNames();
        float listY = y + 44;
        for (int i = 0; i < configs.size(); i++) {
            float cy = listY + i * 32;
            if (!MathUtil.isHovered(mouseX, mouseY, x, cy, width, 28)) continue;
            String name = configs.get(i);
            if (mouseX >= x + width - 40) Wyvern.getInstance().getConfigManager().deleteConfig(name);
            else Wyvern.getInstance().getConfigManager().loadConfig(name);
            return true;
        }
        return MathUtil.isHovered(mouseX, mouseY, x, y, width, height);
    }

    private String sanitizeConfigName(String name) {
        if (name == null) return "";
        return name.trim().replaceAll("[^a-zA-Z0-9а-яА-ЯёЁ _-]", "");
    }

    private boolean handleThemeClick(double mouseX, double mouseY, int button, float x, float y, float width, float height) {
        if (button != 0) return MathUtil.isHovered(mouseX, mouseY, x, y, width, height);
        var themes = Wyvern.getInstance().getThemeManager().getThemes();
        int columns = Math.max(1, Math.min(CsGuiRenderer.THEME_COLUMNS, themes.size()));
        float gridWidth = columns * CsGuiRenderer.THEME_SWATCH_SIZE
                + (columns - 1) * CsGuiRenderer.THEME_SWATCH_GAP;
        float startX = x + (width - gridWidth) / 2.0F;
        float step = CsGuiRenderer.THEME_SWATCH_SIZE + CsGuiRenderer.THEME_SWATCH_GAP;
        float scroll = state.getScroll(Category.THEMES);
        for (int i = 0; i < themes.size(); i++) {
            float tx = startX + (i % columns) * step;
            float ty = y + CsGuiRenderer.THEME_GRID_TOP + (i / columns) * step - scroll;
            if (MathUtil.isHovered(mouseX, mouseY, tx, ty,
                    CsGuiRenderer.THEME_SWATCH_SIZE, CsGuiRenderer.THEME_SWATCH_SIZE)) {
                Wyvern.getInstance().getThemeManager().setCurrentTheme(themes.get(i));
                return true;
            }
        }
        return MathUtil.isHovered(mouseX, mouseY, x, y, width, height);
    }

    private boolean handleSettingClick(CsGuiRenderer.Card card, double mouseX, double mouseY, int button) {
        float sy = card.y + CsGuiRenderer.CARD_HEADER_HEIGHT - 1;
        for (Setting setting : card.module.getSettings()) {
            if (setting == null) continue;
            var visibility = state.getSettingVisibilityAnimation(setting);
            visibility.update(setting.isVisible() ? 1F : 0F);
            float progress = visibility.getValue();
            if (progress <= 0.01F) continue;

            float height = CsGuiRenderer.getSettingHeight(setting, card.width) * progress;
            float renderedY = sy + (1F - progress) * 2F;
            if (MathUtil.isHovered(mouseX, mouseY, card.x + 3, renderedY, card.width - 6, height)) {
                if (setting instanceof BooleanSetting bool) {
                    if (button == 0 && MathUtil.isHovered(mouseX, mouseY, card.x + card.width - 18, renderedY + 3.0F, 11, 11)) {
                        bool.toggle();
                        return true;
                    }
                    if (button == 2 || button == 1) {
                        state.setBindingSetting(bool);
                        return true;
                    }
                    return true;
                }
                if (setting instanceof SliderSetting number) {
                    float barX = card.x + 6;
                    float barW = card.width - 12;
                    // Updated slider hitbox (bar is at y + 12.5)
                    if (button == 0 && MathUtil.isHovered(mouseX, mouseY, barX, renderedY + 10, barW, 10)) {
                        setSlider(number, mouseX, barX, barW);
                        draggingSlider = number;
                        draggingSliderX = barX;
                        draggingSliderWidth = barW;
                        state.setDraggingSlider(number);
                    }
                    return true;
                }
                if (setting instanceof ModeSetting mode) {
                    if (button == 0) {
                        for (CsGuiRenderer.Chip chip : CsGuiRenderer.getModeChips(mode, card.x, renderedY, card.width)) {
                            if (MathUtil.isHovered(mouseX, mouseY, chip.x(), chip.y(), chip.width(), chip.height())) {
                                mode.setValue(chip.value());
                                return true;
                            }
                        }
                    }
                    return true;
                }
                if (setting instanceof BindSetting key && button == 0) {
                    state.setBindingSetting(key);
                    return true;
                }
                if (setting instanceof MultiBooleanSetting multi && button == 0 && !multi.getBooleanSettings().isEmpty()) {
                    for (CsGuiRenderer.MultiChip chip : CsGuiRenderer.getMultiChips(multi, card.x, renderedY, card.width)) {
                        if (MathUtil.isHovered(mouseX, mouseY, chip.x(), chip.y(), chip.width(), chip.height())) {
                            chip.value().toggle();
                            return true;
                        }
                    }
                    return true;
                }
                if (setting instanceof StringSetting string && button == 0) {
                    state.setEditingStringSetting(string);
                    return true;
                }
                if (setting instanceof ButtonSetting buttonSetting && button == 0) {
                    buttonSetting.toggle();
                    return true;
                }
                if (setting instanceof ColorSetting color) {
                    if (button == 1) color.reset();
                    else if (button == 0) color.setColor(wtf.wyvern.render.display.base.color.ColorRGBA.fromHSB((color.getColor().getHue() + 0.08F) % 1F, 0.72F, 1F));
                    return true;
                }
                return true;
            }
            sy += height;
        }
        return false;
    }

    private void setSlider(SliderSetting setting, double mouseX, float x, float width) {
        float percent = Math.max(0, Math.min(1, ((float) mouseX - x) / width));
        float value = setting.getMin() + (setting.getMax() - setting.getMin()) * percent;
        float increment = setting.getIncrement();
        setting.setCurrent(Math.max(setting.getMin(), Math.min(setting.getMax(), Math.round(value / increment) * increment)));
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button) {
        if (state.isDraggingGui()) {
            state.setPos((float) mouseX - state.getGuiDragX(), (float) mouseY - state.getGuiDragY());
            return true;
        }
        if (button != 0 || draggingSlider == null) return false;
        setSlider(draggingSlider, mouseX, draggingSliderX, draggingSliderWidth);
        return true;
    }

    public boolean mouseReleased(int button) {
        if (button == 0) {
            if (state.isDraggingGui()) {
                state.setDraggingGui(false);
            }
            if (draggingSlider != null) {
                draggingSlider = null;
                state.setDraggingSlider(null);
                return true;
            }
        }
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
        float x = state.getX() + CsGuiRenderer.SIDEBAR_WIDTH + CsGuiRenderer.CONTENT_PADDING;
        float y = state.getY() + state.getRenderOffsetY() + CsGuiRenderer.HEADER_HEIGHT + CsGuiRenderer.CONTENT_PADDING;
        float width = CsGuiRenderer.PANEL_WIDTH - CsGuiRenderer.SIDEBAR_WIDTH - CsGuiRenderer.CONTENT_PADDING * 2;
        float height = CsGuiRenderer.PANEL_HEIGHT - CsGuiRenderer.HEADER_HEIGHT - CsGuiRenderer.CONTENT_PADDING * 2;
        if (!MathUtil.isHovered(mouseX, mouseY, x, y, width, height)) return false;

        Category category = state.getSelectedCategoryValue();
        if (category == Category.FIGURA) {
            return renderer.getFiguraPage().mouseScrolled(mouseX, mouseY, verticalAmount);
        }
        float max;
        if (category == Category.THEMES) {
            int count = Wyvern.getInstance().getThemeManager().getThemes().size();
            int columns = Math.max(1, Math.min(CsGuiRenderer.THEME_COLUMNS, count));
            float rows = (float)Math.ceil(count / (float)columns);
            max = Math.max(0, CsGuiRenderer.THEME_GRID_TOP
                    + rows * (CsGuiRenderer.THEME_SWATCH_SIZE + CsGuiRenderer.THEME_SWATCH_GAP)
                    - CsGuiRenderer.THEME_SWATCH_GAP - height);
        } else {
            List<CsGuiRenderer.Card> cards = renderer.buildCards(state, x, y, width - CsGuiRenderer.SCROLLBAR_GUTTER);
            max = Math.max(0, CsGuiRenderer.getContentHeight(cards) - (y + height));
        }
        state.setScrollTarget(category, Math.max(0, Math.min(max, state.getScrollTarget(category) - (float) verticalAmount * 12)));
        return true;
    }

    public boolean charTyped(char chr, int modifiers) {
        if (!state.isConfigPage() && state.getSelectedCategoryValue() == Category.FIGURA
                && renderer.getFiguraPage().charTyped(chr)) {
            return true;
        }
        if (state.getEditingStringSetting() != null) {
            if (Character.isISOControl(chr)) return true;
            StringSetting setting = state.getEditingStringSetting();
            String text = setting.getValue();
            if (text.length() >= setting.getMaxLength()) return true;
            int cursor = state.getStringCursor();
            setting.setValue(text.substring(0, cursor) + chr + text.substring(cursor));
            state.setStringCursor(cursor + 1);
            return true;
        }
        if (state.isConfigNameActive()) {
            if (Character.isISOControl(chr) || state.getConfigName().length() >= 32) return true;
            String text = state.getConfigName();
            int cursor = state.getConfigNameCursor();
            state.setConfigName(text.substring(0, cursor) + chr + text.substring(cursor));
            state.setConfigNameCursor(cursor + 1);
            return true;
        }
        if (!state.isSearchActive() || Character.isISOControl(chr)) return false;
        String text = state.getSearchText();
        if (text.length() >= ClickGuiLayout.SEARCH_MAX_CHARS) return true;
        int cursor = Math.max(0, Math.min(state.getSearchCursor(), text.length()));
        state.setSearchText(text.substring(0, cursor) + chr + text.substring(cursor));
        state.setSearchCursor(cursor + 1);
        return true;
    }

    public boolean keyPressed(int keyCode, int modifiers) {
        if (!state.isConfigPage() && state.getSelectedCategoryValue() == Category.FIGURA
                && renderer.getFiguraPage().keyPressed(keyCode)) {
            return true;
        }
        if (state.getBindingModule() != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) state.setBindingModule(null);
            else if (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE) { state.getBindingModule().setKeyCode(-1); state.setBindingModule(null); }
            else { state.getBindingModule().setKeyCode(keyCode); state.setBindingModule(null); }
            return true;
        }
        if (state.getBindingSetting() != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                state.setBindingSetting(null);
            } else if (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE) {
                if (state.getBindingSetting() instanceof BindSetting key) {
                    key.setKeyCode(-1);
                } else if (state.getBindingSetting() instanceof BooleanSetting bool) {
                    bool.setKeyCode(-1);
                }
                state.setBindingSetting(null);
            } else {
                if (state.getBindingSetting() instanceof BindSetting key) {
                    key.setKeyCode(keyCode);
                } else if (state.getBindingSetting() instanceof BooleanSetting bool) {
                    bool.setKeyCode(keyCode);
                }
                state.setBindingSetting(null);
            }
            return true;
        }
        if (state.getEditingStringSetting() != null) {
            StringSetting setting = state.getEditingStringSetting();
            String text = setting.getValue();
            int cursor = state.getStringCursor();
            if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_ENTER) { state.setEditingStringSetting(null); return true; }
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE && cursor > 0) { setting.setValue(text.substring(0, cursor - 1) + text.substring(cursor)); state.setStringCursor(cursor - 1); return true; }
            if (keyCode == GLFW.GLFW_KEY_DELETE && cursor < text.length()) { setting.setValue(text.substring(0, cursor) + text.substring(cursor + 1)); return true; }
            if (keyCode == GLFW.GLFW_KEY_LEFT) { state.setStringCursor(cursor - 1); return true; }
            if (keyCode == GLFW.GLFW_KEY_RIGHT) { state.setStringCursor(cursor + 1); return true; }
            return true;
        }
        if (state.isConfigNameActive()) {
            String text = state.getConfigName();
            int cursor = state.getConfigNameCursor();
            if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_ENTER) { state.setConfigNameActive(false); return true; }
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE && cursor > 0) { state.setConfigName(text.substring(0, cursor - 1) + text.substring(cursor)); state.setConfigNameCursor(cursor - 1); return true; }
            if (keyCode == GLFW.GLFW_KEY_DELETE && cursor < text.length()) { state.setConfigName(text.substring(0, cursor) + text.substring(cursor + 1)); return true; }
            if (keyCode == GLFW.GLFW_KEY_LEFT) { state.setConfigNameCursor(cursor - 1); return true; }
            if (keyCode == GLFW.GLFW_KEY_RIGHT) { state.setConfigNameCursor(cursor + 1); return true; }
            return true;
        }
        if (!state.isSearchActive()) return false;
        String text = state.getSearchText();
        int cursor = Math.max(0, Math.min(state.getSearchCursor(), text.length()));
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_ENTER) { state.setSearchActive(false); return true; }
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE && cursor > 0) { state.setSearchText(text.substring(0, cursor - 1) + text.substring(cursor)); state.setSearchCursor(cursor - 1); return true; }
        if (keyCode == GLFW.GLFW_KEY_DELETE && cursor < text.length()) { state.setSearchText(text.substring(0, cursor) + text.substring(cursor + 1)); return true; }
        if (keyCode == GLFW.GLFW_KEY_LEFT) { state.setSearchCursor(Math.max(0, cursor - 1)); return true; }
        if (keyCode == GLFW.GLFW_KEY_RIGHT) { state.setSearchCursor(Math.min(text.length(), cursor + 1)); return true; }
        return true;
    }

    private void clampScroll(List<CsGuiRenderer.Card> cards, float contentY, float contentHeight) {
        Category category = state.getSelectedCategoryValue();
        float max = Math.max(0, CsGuiRenderer.getContentHeight(cards) - (contentY + contentHeight));
        state.setScrollTarget(category, Math.max(0, Math.min(max, state.getScrollTarget(category))));
    }
}

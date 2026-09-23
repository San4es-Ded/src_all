package wtf.wyvern.client.gui.screens.menu.wonderful;

import net.minecraft.client.util.Window;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.setting.Setting;
import wtf.wyvern.client.modules.api.setting.impl.*;

import java.util.*;

public class ClickGuiState {
    private static final Map<Character, Character> RU_TO_EN = new HashMap<>();

    static {
        String ru = "йцукенгшщзхъфывапролджэячсмитьбюЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ";
        String en = "qwertyuiop[]asdfghjkl;'zxcvbnm,.QWERTYUIOP[]ASDFGHJKL;'ZXCVBNM,.";
        int length = Math.min(ru.length(), en.length());
        for (int i = 0; i < length; i++) {
            RU_TO_EN.put(ru.charAt(i), en.charAt(i));
        }
    }

    private final Map<Module, Animation> moduleOpenAnimation = new IdentityHashMap<>();
    private final Map<Module, Boolean> moduleOpenState = new IdentityHashMap<>();
    private final Map<Module, Animation> moduleDotAnimation = new IdentityHashMap<>();
    private final Map<wtf.wyvern.core.theme.Theme, Animation> themeAnimation = new IdentityHashMap<>();
    private final Map<String, Animation> configHoverAnimation = new HashMap<>();
    private final Map<String, Animation> configEntryAnimation = new HashMap<>();
    private final Set<String> removedConfigs = new HashSet<>();

    private final Map<BooleanSetting, Animation> booleanBackgroundAnimation = new HashMap<>();
    private final Map<BooleanSetting, Animation> booleanCircleAnimation = new HashMap<>();
    private final Map<SliderSetting, Animation> sliderAnimation = new HashMap<>();
    private final Map<Setting, Animation> settingVisibilityAnimation = new IdentityHashMap<>();
    private final Map<String, Animation> modeAnimation = new HashMap<>();
    private final Animation themeDropdownAnimation = new Animation(220L, Easing.CUBIC_OUT);
    private final Map<Integer, Animation> categorySelectionAnimations = new HashMap<>();
    private final Animation categoryContentAnimation = new Animation(230L, Easing.CUBIC_OUT);

    private final Map<Category, Float> categoryScrollTarget = new EnumMap<>(Category.class);
    private final Map<Category, Animation> categoryScrollAnimation = new EnumMap<>(Category.class);
    private final Map<Category, List<Module>> modulesByCategory = new EnumMap<>(Category.class);
    private final List<Module> allModules = new ArrayList<>();

    private float x;
    private float y;
    private int selectedCategory;
    private int categoryContentDirection = 1;
    private Setting bindingSetting;
    private Module bindingModule;
    private float renderOffsetY;
    private boolean searchActive;
    private String searchText = "";
    private int searchCursor = 0;
    private StringSetting editingStringSetting;
    private int stringCursor = 0;
    private boolean themeDropdownOpen;
    private SliderSetting draggingSlider;
    private boolean configPage;
    private boolean configNameActive;
    private String configName = "";
    private int configNameCursor;

    private boolean draggingGui;
    private float guiDragX;
    private float guiDragY;
    private boolean initializedPosition;

    public ClickGuiState() {
        refreshModules();
    }

    public void refreshModules() {
        allModules.clear();
        allModules.addAll(Wyvern.getInstance().getModuleManager().getModules());
        for (Category category : Category.values()) {
            modulesByCategory.put(category, allModules.stream().filter(module -> module.getCategory() == category).toList());
            categoryScrollTarget.putIfAbsent(category, 0f);
            categoryScrollAnimation.putIfAbsent(category, new Animation(300L, Easing.CUBIC_OUT));
        }
    }

    public void updatePosition(Window window, int categoryCount) {
        if (!initializedPosition) {
            this.x = (window.getScaledWidth() - CsGuiRenderer.PANEL_WIDTH) / 2F;
            this.y = (window.getScaledHeight() - CsGuiRenderer.PANEL_HEIGHT) / 2F;
            initializedPosition = true;
        }
    }

    public boolean isDraggingGui() { return draggingGui; }
    public void setDraggingGui(boolean draggingGui) { this.draggingGui = draggingGui; }
    public float getGuiDragX() { return guiDragX; }
    public void setGuiDragX(float guiDragX) { this.guiDragX = guiDragX; }
    public float getGuiDragY() { return guiDragY; }
    public void setGuiDragY(float guiDragY) { this.guiDragY = guiDragY; }
    public void setPos(float x, float y) { this.x = x; this.y = y; }

    public float getX() { return x; }
    public float getY() { return y; }
    public int getSelectedCategory() { return selectedCategory; }
    public void setSelectedCategory(int selectedCategory) {
        int next = Math.max(0, Math.min(Category.values().length - 1, selectedCategory));
        if (this.selectedCategory != next) {
            this.categoryContentDirection = next > this.selectedCategory ? 1 : -1;
            this.selectedCategory = next;
            categoryContentAnimation.setValue(0.0F);
            categoryContentAnimation.setStartValue(0.0F);
        }
    }
    public Category getSelectedCategoryValue() {
        return Category.values()[selectedCategory];
    }
    public float getRenderOffsetY() { return renderOffsetY; }
    public void setRenderOffsetY(float renderOffsetY) { this.renderOffsetY = renderOffsetY; }

    // Module names never change at runtime, so the search result is a pure function
    // of the query. Cache it: getModules is called every frame while the GUI is open.
    private String cachedSearchText;
    private List<Module> cachedSearchResult;

    public List<Module> getModules(Category category) {
        List<Module> modules = modulesByCategory.getOrDefault(category, List.of());
        if (searchText.isBlank()) {
            return modules;
        }

        if (cachedSearchResult == null || !searchText.equals(cachedSearchText)) {
            String query = searchText.toLowerCase(Locale.ROOT);
            cachedSearchResult = allModules.stream()
                    .filter(module -> module.getName().toLowerCase(Locale.ROOT).contains(query))
                    .toList();
            cachedSearchText = searchText;
        }
        return cachedSearchResult;
    }

    public List<Module> getAllModules() { return allModules; }

    public String toEnglish(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            result.append(RU_TO_EN.getOrDefault(c, c));
        }
        return result.toString();
    }

    public float getSliderPos(SliderSetting setting) {
        float delta = setting.getMax() - setting.getMin();
        return (setting.getCurrent() - setting.getMin()) / delta;
    }

    public float getSliderValue(SliderSetting setting, float posX, double mouseX) {
        float delta = setting.getMax() - setting.getMin();
        float clickedX = (float) mouseX - posX;
        float value = Math.max(0f, Math.min(1f, clickedX / ClickGuiLayout.SLIDER_WIDTH));
        float outValue = setting.getMin() + delta * value;
        float increment = setting.getIncrement();
        outValue = Math.round(outValue / increment) * increment;
        return Math.max(setting.getMin(), Math.min(setting.getMax(), outValue));
    }

    public float getScroll(Category category) {
        Animation animation = categoryScrollAnimation.computeIfAbsent(category, key -> new Animation(250L, Easing.CUBIC_OUT));
        animation.update(categoryScrollTarget.getOrDefault(category, 0f));
        return animation.getValue();
    }

    public float getScrollTarget(Category category) {
        return categoryScrollTarget.getOrDefault(category, 0f);
    }

    public void setScrollTarget(Category category, float value) {
        categoryScrollTarget.put(category, value);
    }

    public void clampScroll(Category category, float contentHeight) {
        float totalHeight = getTotalModulesHeight(category);
        float maxScroll = Math.min(0f, contentHeight - totalHeight);
        float currentTarget = categoryScrollTarget.getOrDefault(category, 0f);
        if (currentTarget < maxScroll || currentTarget > 0f) {
            categoryScrollTarget.put(category, Math.max(maxScroll, Math.min(0f, currentTarget)));
        }
    }

    public void addScroll(Category category, double verticalAmount, float contentHeight) {
        float totalHeight = getTotalModulesHeight(category);
        float maxScroll = Math.min(0f, contentHeight - totalHeight);
        float currentTarget = categoryScrollTarget.getOrDefault(category, 0f);
        float newTarget = currentTarget + (float) (verticalAmount * 20);
        categoryScrollTarget.put(category, Math.max(maxScroll, Math.min(0f, newTarget)));
    }

    public float getTotalModulesHeight(Category category) {
        if (category == Category.THEMES) {
            int count = Wyvern.getInstance().getThemeManager().getThemes().size();
            int columns = Math.max(1, Math.min(CsGuiRenderer.THEME_COLUMNS, count));
            return CsGuiRenderer.THEME_GRID_TOP
                    + (float)Math.ceil(count / (float)columns)
                    * (CsGuiRenderer.THEME_SWATCH_SIZE + CsGuiRenderer.THEME_SWATCH_GAP)
                    - CsGuiRenderer.THEME_SWATCH_GAP;
        }

        float totalHeight = 0f;
        for (Module module : getModules(category)) {
            totalHeight += ClickGuiLayout.MODULE_GAP + ClickGuiLayout.getModuleHeight(module, getOpenProgress(module));
        }
        return totalHeight;
    }

    public float getOpenProgress(Module module) {
        Animation animation = moduleOpenAnimation.computeIfAbsent(
                module,
                key -> new Animation(250L, Easing.CUBIC_OUT)
        );
        animation.update(isModuleOpen(module) ? 1f : 0f);
        return animation.getValue();
    }

    public boolean isModuleOpen(Module module) {
        return moduleOpenState.getOrDefault(module, false);
    }

    public void toggleModuleOpen(Module module) {
        moduleOpenState.put(module, !isModuleOpen(module));
    }

    public Animation getBooleanBackgroundAnimation(BooleanSetting setting) {
        return booleanBackgroundAnimation.computeIfAbsent(
                setting,
                key -> new Animation(200L, Easing.CUBIC_OUT)
        );
    }

    public Animation getBooleanCircleAnimation(BooleanSetting setting) {
        return booleanCircleAnimation.computeIfAbsent(
                setting,
                key -> new Animation(200L, Easing.CUBIC_OUT)
        );
    }

    public Animation getSliderAnimation(SliderSetting setting) {
        return sliderAnimation.computeIfAbsent(
                setting,
                key -> new Animation(200L, Easing.CUBIC_OUT)
        );
    }

    public Animation getModeAnimation(String key, boolean selected) {
        return modeAnimation.computeIfAbsent(
                key,
                unused -> new Animation(200L, Easing.CUBIC_OUT)
        );
    }

    public Animation getModuleDotAnimation(Module module) {
        return moduleDotAnimation.computeIfAbsent(
                module,
                key -> new Animation(200L, Easing.CUBIC_OUT)
        );
    }

    public Animation getThemeAnimation(wtf.wyvern.core.theme.Theme theme) {
        return themeAnimation.computeIfAbsent(
                theme,
                key -> new Animation(200L, Easing.CUBIC_OUT)
        );
    }

    public Animation getConfigHoverAnimation(String configName) {
        return configHoverAnimation.computeIfAbsent(
                configName,
                key -> new Animation(200L, Easing.CUBIC_OUT)
        );
    }

    public Animation getConfigEntryAnimation(String configName) {
        return configEntryAnimation.computeIfAbsent(
                configName,
                key -> {
                    Animation anim = new Animation(250L, Easing.CUBIC_OUT);
                    anim.setValue(0.0F);
                    anim.setStartValue(0.0F);
                    return anim;
                }
        );
    }

    public void markConfigRemoved(String configName) {
        removedConfigs.add(configName);
    }

    public boolean isConfigRemoved(String configName) {
        return removedConfigs.contains(configName);
    }

    public Setting getBindingSetting() { return bindingSetting; }
    public void setBindingSetting(Setting bindingSetting) { this.bindingSetting = bindingSetting; }

    public Module getBindingModule() { return bindingModule; }
    public void setBindingModule(Module bindingModule) { this.bindingModule = bindingModule; }

    public boolean isSearchActive() { return searchActive; }
    public void setSearchActive(boolean searchActive) { this.searchActive = searchActive; }

    public String getSearchText() { return searchText; }
    public void setSearchText(String searchText) { this.searchText = searchText; }
    public int getSearchCursor() { return searchCursor; }
    public void setSearchCursor(int searchCursor) { this.searchCursor = searchCursor; }

    public StringSetting getEditingStringSetting() { return editingStringSetting; }
    public void setEditingStringSetting(StringSetting editingStringSetting) {
        this.editingStringSetting = editingStringSetting;
        this.stringCursor = editingStringSetting == null ? 0 : editingStringSetting.getValue().length();
    }

    public int getStringCursor() { return stringCursor; }
    public void setStringCursor(int stringCursor) { this.stringCursor = stringCursor; }

    public boolean isThemeDropdownOpen() { return themeDropdownOpen; }
    public void setThemeDropdownOpen(boolean themeDropdownOpen) { this.themeDropdownOpen = themeDropdownOpen; }

    public boolean isDraggingSlider(SliderSetting setting) { return draggingSlider == setting; }
    public void setDraggingSlider(SliderSetting setting) { this.draggingSlider = setting; }

    public boolean isConfigPage() { return configPage; }
    public void setConfigPage(boolean configPage) {
        if (this.configPage != configPage) {
            this.categoryContentDirection = configPage ? 1 : -1;
            this.configPage = configPage;
            categoryContentAnimation.setValue(0.0F);
            categoryContentAnimation.setStartValue(0.0F);
        }
    }

    public Animation getSettingVisibilityAnimation(Setting setting) {
        return settingVisibilityAnimation.computeIfAbsent(
                setting,
                key -> new Animation(180L, Easing.CUBIC_OUT)
        );
    }
    public boolean isConfigNameActive() { return configNameActive; }
    public void setConfigNameActive(boolean configNameActive) {
        this.configNameActive = configNameActive;
        this.configNameCursor = Math.min(configNameCursor, configName.length());
    }

    public float getCategorySelectionProgress(int categoryIndex, boolean selected) {
        Animation animation = categorySelectionAnimations.computeIfAbsent(categoryIndex, key -> new Animation(180L, Easing.CUBIC_OUT));
        animation.update(selected ? 1.0F : 0.0F);
        return animation.getValue();
    }

    public float getCategoryContentProgress() {
        categoryContentAnimation.update(1.0F);
        return categoryContentAnimation.getValue();
    }
    public int getCategoryContentDirection() { return categoryContentDirection; }
    public String getConfigName() { return configName; }
    public void setConfigName(String configName) { this.configName = configName == null ? "" : configName; }
    public int getConfigNameCursor() { return configNameCursor; }
    public void setConfigNameCursor(int configNameCursor) { this.configNameCursor = Math.max(0, Math.min(configNameCursor, configName.length())); }

    public float getThemeDropdownProgress() {
        themeDropdownAnimation.update(themeDropdownOpen);
        return themeDropdownAnimation.getValue();
    }
}

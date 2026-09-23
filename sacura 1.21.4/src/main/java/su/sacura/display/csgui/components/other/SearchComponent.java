package su.sacura.display.csgui.components.other;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.util.Identifier;
import su.sacura.Sacura;
import su.sacura.display.csgui.helper.ThemeStorage;
import su.sacura.display.csgui.helper.WindowRender;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.impl.Category;
import su.sacura.features.modules.settings.api.Setting;
import su.sacura.util.impl.render.RenderUtils;
import su.sacura.util.impl.render.providers.FontProvider;
import su.sacura.util.type.ISetting;

public class SearchComponent {
    private boolean open = false;
    private boolean opening = false;
    private float animationProgress = 0.0f;
    private String searchQuery = "";
    private final List<Object> searchResults = new ArrayList<Object>();
    private int selectedIndex = 0;
    private final WindowRender windowRender;
    private float selectionY = 0.0f;
    private float targetSelectionY = 0.0f;
    private boolean searchResultsDirty = true;
    private int cursorPosition = 0;
    private int selectionAnchor = 0;
    private static final Map<String, String> TRANSLIT_MAP = new HashMap<String, String>();

    public SearchComponent(WindowRender windowRender) {
        this.windowRender = windowRender;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.opening = open;
        if (open) {
            this.open = true;
            this.searchResultsDirty = true;
            this.selectionAnchor = this.cursorPosition = this.searchQuery.length();
        } else {
            this.searchQuery = "";
            this.searchResults.clear();
            this.selectedIndex = 0;
            this.cursorPosition = 0;
            this.selectionAnchor = 0;
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta, float windowX, float windowY, float windowWidth, float windowHeight, int globalAlpha) {
        float baseSearchInputHeight;
        if (this.opening && this.animationProgress < 1.0f) {
            this.animationProgress += delta * 0.3f;
            if (this.animationProgress > 1.0f) {
                this.animationProgress = 1.0f;
            }
        } else if (!this.opening && this.animationProgress > 0.0f) {
            this.animationProgress -= delta * 0.3f;
            if (this.animationProgress < 0.0f) {
                this.animationProgress = 0.0f;
                this.open = false;
            }
        }
        if (!this.open) {
            return;
        }
        float searchInputX = windowX + (windowWidth - 270.0f) / 2.0f;
        float searchInputY = windowY + 25.0f;
        float slideOffset = 30.0f;
        float animatedY = searchInputY - slideOffset * (1.0f - this.animationProgress);
        float searchInputWidth = 270.0f;
        float totalHeight = baseSearchInputHeight = 22.0f;

        // Исправлено: добавлена типизация List<Module>
        List<Module> moduleResults = this.searchResults.stream().filter(Module.class::isInstance).map(Module.class::cast).collect(Collectors.toList());
        // Исправлено: добавлена типизация List<Setting>
        List<Setting> settingResults = this.searchResults.stream().filter(Setting.class::isInstance).map(s -> (Setting)s).collect(Collectors.toList());

        float resultsRenderHeight = 0.0f;
        float headerHeight = 15.0f;
        float itemHeight = 12.0f;
        float itemFullHeight = itemHeight + 5.0f;
        float topPadding = 5.0f;
        if (!moduleResults.isEmpty()) {
            resultsRenderHeight += headerHeight + (float)moduleResults.size() * itemFullHeight;
        }
        if (!settingResults.isEmpty()) {
            resultsRenderHeight += headerHeight + (float)settingResults.size() * itemFullHeight;
        }
        if (resultsRenderHeight > 0.0f) {
            totalHeight += (resultsRenderHeight += topPadding);
        }
        float animatedHeight = totalHeight * this.animationProgress;
        int animatedAlpha = (int)((float)globalAlpha * this.animationProgress);
        Color bg = ThemeStorage.backgroundColor;
        Color accent = ThemeStorage.accentColor;
        Color borderC = ThemeStorage.borderColor;
        Color textC = ThemeStorage.textColor;
        int r = bg.getRed();
        int g = bg.getGreen();
        int b = bg.getBlue();
        int a = (int)(200.0f * ((float)animatedAlpha / 255.0f));
        int borderColor = (a & 0xFF) << 24 | (borderC.getRed() & 0xFF) << 16 | (borderC.getGreen() & 0xFF) << 8 | borderC.getBlue() & 0xFF;
        int bgColor = new Color(r, g, b, animatedAlpha).getRGB();
        RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), searchInputX, animatedY, searchInputWidth, animatedHeight, 4.0f, bgColor);
        RenderUtils.border(context.getMatrices().peek().getPositionMatrix(), searchInputX, animatedY, searchInputWidth, animatedHeight, 4.0f, 0.2f, 0.1f, 1.0f, borderColor);
        FontProvider.monoton.draw(context.getMatrices().peek().getPositionMatrix(), "C", searchInputX - 105.0f, animatedY - 15.0f, 20.0f, new Color(150, 150, 150, animatedAlpha).getRGB());
        float textY = animatedY + baseSearchInputHeight / 2.0f - 4.0f;
        if (this.cursorPosition != this.selectionAnchor) {
            int start = Math.min(this.cursorPosition, this.selectionAnchor);
            int end = Math.max(this.cursorPosition, this.selectionAnchor);
            String preSelection = this.searchQuery.substring(0, start);
            String selection = this.searchQuery.substring(start, end);
            float preWidth = FontProvider.regular.getWidth(preSelection, 8.0f);
            float selWidth = FontProvider.regular.getWidth(selection, 8.0f);
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), searchInputX + 19.0f + preWidth, textY, selWidth + 3.0f, 10.0f, 0.0f, new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), animatedAlpha).getRGB());
        }
        if (this.searchQuery != null && !this.searchQuery.isEmpty()) {
            FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.searchQuery, searchInputX + 19.0f, textY, 8.0f, new Color(textC.getRed(), textC.getGreen(), textC.getBlue(), animatedAlpha).getRGB());
        }
        if (System.currentTimeMillis() / 500L % 2L == 0L) {
            String preCursor = this.searchQuery.substring(0, this.cursorPosition);
            float preCursorWidth = FontProvider.regular.getWidth(preCursor, 8.0f);
            float cursorX = searchInputX + 19.0f + preCursorWidth + 1.0f;
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), cursorX, textY - 1.0f, 1.0f, 10.0f, 0.0f, new Color(textC.getRed(), textC.getGreen(), textC.getBlue(), animatedAlpha).getRGB());
        }
        if (resultsRenderHeight > 0.0f && (double)this.animationProgress > 0.5) {
            float currentY = animatedY + baseSearchInputHeight + topPadding;
            context.enableScissor((int)searchInputX, (int)(animatedY + baseSearchInputHeight), (int)(searchInputX + searchInputWidth), (int)(animatedY + animatedHeight));
            int currentIndex = 0;
            float targetY = -1.0f;
            if (!moduleResults.isEmpty()) {
                FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), "Модули", searchInputX + 5.0f, currentY, 7.0f, new Color(180, 180, 180, animatedAlpha).getRGB());
                currentY += headerHeight;
                for (Module module : moduleResults) {
                    if (currentIndex == this.selectedIndex) {
                        targetY = currentY - 2.0f;
                    }
                    FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), module.name, searchInputX + 20.0f, currentY + 2.0f, 8.0f, new Color(textC.getRed(), textC.getGreen(), textC.getBlue(), animatedAlpha).getRGB());
                    Category category = module.category;
                    if (category != null) {
                        String categoryInfo = category.name;
                        float categoryWidth = FontProvider.regular.getWidth(categoryInfo, 7.0f);
                        FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), category.iconChar, searchInputX + searchInputWidth - categoryWidth - 20.0f, currentY + 1.0f, 8.0f, new Color(textC.getRed(), textC.getGreen(), textC.getBlue(), animatedAlpha).getRGB());
                        FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), categoryInfo, searchInputX + searchInputWidth - categoryWidth - 10.0f, currentY + 2.0f, 7.0f, new Color(180, 180, 180, animatedAlpha).getRGB());
                    }
                    AbstractTexture abstractTexture = MinecraftClient.getInstance().getTextureManager().getTexture(Identifier.ofVanilla((String)"sacura/images/ico.png"));
                    int ico = abstractTexture.getGlId();
                    RenderUtils.texture(context.getMatrices().peek().getPositionMatrix(), searchInputX + 7.0f, currentY + 1.5f, 10.0f, 10.0f, ico, new Color(textC.getRed(), textC.getGreen(), textC.getBlue(), animatedAlpha).getRGB());
                    currentY += itemFullHeight;
                    ++currentIndex;
                }
            }
            if (!settingResults.isEmpty()) {
                FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), "Настройки", searchInputX + 5.0f, currentY, 7.0f, new Color(180, 180, 180, animatedAlpha).getRGB());
                currentY += headerHeight;
                for (Setting setting : settingResults) {
                    if (currentIndex == this.selectedIndex) {
                        targetY = currentY - 2.0f;
                    }
                    String moduleName = Sacura.getInstance().getModuleManager().module.stream().filter(m -> m.getSettings().contains(setting)).findFirst().map(m -> m.name).orElse("");
                    String settingName = setting.getName();
                    FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), settingName, searchInputX + 10.0f, currentY + 2.0f, 8.0f, new Color(textC.getRed(), textC.getGreen(), textC.getBlue(), animatedAlpha).getRGB());
                    if (!moduleName.isEmpty()) {
                        float moduleNameWidth = FontProvider.regular.getWidth(moduleName, 7.0f);
                        FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), moduleName, searchInputX + searchInputWidth - moduleNameWidth - 10.0f, currentY + 2.0f, 7.0f, new Color(180, 180, 180, animatedAlpha).getRGB());
                    }
                    currentY += itemFullHeight;
                    ++currentIndex;
                }
            }
            if (targetY != -1.0f) {
                this.targetSelectionY = targetY;
            }
            if (this.searchResultsDirty) {
                this.selectionY = this.targetSelectionY;
                this.searchResultsDirty = false;
            } else {
                this.selectionY += (this.targetSelectionY - this.selectionY) * delta * 5.0f;
            }
            if (this.selectedIndex >= 0 && this.selectedIndex < this.searchResults.size()) {
                RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), searchInputX + 2.0f, this.selectionY, searchInputWidth - 4.0f, itemFullHeight, 4.0f, new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), (int)(100.0f * ((float)animatedAlpha / 255.0f))).getRGB());
            }
            context.disableScissor();
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, float windowX, float windowY, float windowWidth, float windowHeight) {
        if (!this.open) {
            return false;
        }
        float searchInputX = windowX + (windowWidth - 270.0f) / 2.0f;
        float searchInputY = windowY + 25.0f;
        float slideOffset = 30.0f;
        float animatedY = searchInputY - slideOffset * (1.0f - this.animationProgress);
        float searchInputWidth = 270.0f;
        float baseSearchInputHeight = 22.0f;
        if (mouseX >= (double)(searchInputX - 105.0f) && mouseX <= (double)(searchInputX - 85.0f) && mouseY >= (double)(animatedY - 15.0f) && mouseY <= (double)(animatedY + 5.0f)) {
            this.opening = false;
            return true;
        }
        float currentY = animatedY + baseSearchInputHeight + 5.0f;
        float headerHeight = 15.0f;
        float itemFullHeight = 17.0f;

        // Исправлено: добавлена типизация List<Module>
        List<Module> moduleResults = this.searchResults.stream().filter(Module.class::isInstance).map(Module.class::cast).collect(Collectors.toList());

        if (!moduleResults.isEmpty()) {
            currentY += headerHeight;
            for (Module module : moduleResults) {
                if (mouseX >= (double)searchInputX && mouseX <= (double)(searchInputX + searchInputWidth) && mouseY >= (double)(currentY - 2.0f) && mouseY <= (double)(currentY + itemFullHeight - 2.0f)) {
                    this.opening = false;
                    this.windowRender.switchToCategory(module.category);
                    this.windowRender.setSelectedModule(module);
                    return true;
                }
                currentY += itemFullHeight;
            }
        }
        float totalHeight = baseSearchInputHeight;

        // Исправлено: добавлена типизация List<Setting>
        List<Setting> settingResults = this.searchResults.stream().filter(Setting.class::isInstance).map(s -> (Setting)s).collect(Collectors.toList());

        float resultsRenderHeight = 0.0f;
        float topPadding = 5.0f;
        if (!moduleResults.isEmpty()) {
            resultsRenderHeight += headerHeight + (float)moduleResults.size() * itemFullHeight;
        }
        if (!settingResults.isEmpty()) {
            resultsRenderHeight += headerHeight + (float)settingResults.size() * itemFullHeight;
        }
        if (resultsRenderHeight > 0.0f) {
            totalHeight += (resultsRenderHeight += topPadding);
        }
        float animatedHeight = totalHeight * this.animationProgress;
        return mouseX >= (double)searchInputX && mouseX <= (double)(searchInputX + searchInputWidth) && mouseY >= (double)animatedY && mouseY <= (double)(animatedY + animatedHeight);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean shift;
        if (!this.open) {
            return false;
        }
        boolean ctrl = (modifiers & 2) != 0;
        boolean bl = shift = (modifiers & 1) != 0;
        if (keyCode == 256) {
            this.opening = false;
            return true;
        }
        if (keyCode == 65 && ctrl) {
            this.selectionAnchor = 0;
            this.cursorPosition = this.searchQuery.length();
            return true;
        }
        if (keyCode == 263) {
            if (this.cursorPosition > 0) {
                --this.cursorPosition;
            }
            if (!shift) {
                this.selectionAnchor = this.cursorPosition;
            }
            return true;
        }
        if (keyCode == 262) {
            if (this.cursorPosition < this.searchQuery.length()) {
                ++this.cursorPosition;
            }
            if (!shift) {
                this.selectionAnchor = this.cursorPosition;
            }
            return true;
        }
        if (keyCode == 264) {
            if (this.selectedIndex < this.searchResults.size() - 1) {
                ++this.selectedIndex;
            }
            return true;
        }
        if (keyCode == 265) {
            if (this.selectedIndex > 0) {
                --this.selectedIndex;
            }
            return true;
        }
        if (keyCode == 257) {
            if (this.selectedIndex >= 0 && this.selectedIndex < this.searchResults.size()) {
                Setting s;
                Object result = this.searchResults.get(this.selectedIndex);
                if (result instanceof Module) {
                    this.opening = false;
                    this.windowRender.switchToCategory(((Module)result).category);
                    this.windowRender.setSelectedModule((Module)result);
                } else if (result instanceof Setting && (s = (Setting)result).get() instanceof Boolean) {
                    s.set((Boolean)s.get() == false);
                }
            }
            return true;
        }
        if (keyCode == 259) {
            if (this.cursorPosition != this.selectionAnchor) {
                this.deleteSelection();
            } else if (this.cursorPosition > 0) {
                StringBuilder sb = new StringBuilder(this.searchQuery);
                sb.deleteCharAt(this.cursorPosition - 1);
                this.searchQuery = sb.toString();
                --this.cursorPosition;
                this.selectionAnchor = this.cursorPosition;
                this.updateSearchResults();
            }
            return true;
        }
        return true;
    }

    public boolean charTyped(char chr, int modifiers) {
        if (!this.open) {
            return false;
        }
        if (chr >= ' ') {
            if (this.cursorPosition != this.selectionAnchor) {
                this.deleteSelection();
            }
            StringBuilder sb = new StringBuilder(this.searchQuery);
            sb.insert(this.cursorPosition, chr);
            this.searchQuery = sb.toString();
            ++this.cursorPosition;
            this.selectionAnchor = this.cursorPosition;
            this.updateSearchResults();
        }
        return true;
    }

    private void deleteSelection() {
        int end;
        int start = Math.min(this.cursorPosition, this.selectionAnchor);
        if (start == (end = Math.max(this.cursorPosition, this.selectionAnchor))) {
            return;
        }
        StringBuilder sb = new StringBuilder(this.searchQuery);
        sb.delete(start, end);
        this.searchQuery = sb.toString();
        this.cursorPosition = start;
        this.selectionAnchor = start;
        this.updateSearchResults();
    }

    private void updateSearchResults() {
        this.searchResults.clear();
        this.selectedIndex = 0;
        if (this.searchQuery.isEmpty()) {
            this.searchResultsDirty = true;
            return;
        }
        String lowerCaseQuery = this.searchQuery.toLowerCase();
        String translitQuery = this.translit(lowerCaseQuery);
        List<Module> modules = Sacura.getInstance().getModuleManager().module;
        for (Module module : modules) {
            String lowerCaseModuleName = module.name.toLowerCase();
            if ((lowerCaseModuleName.contains(lowerCaseQuery) || lowerCaseModuleName.contains(translitQuery)) && !this.searchResults.contains(module)) {
                this.searchResults.add(module);
            }
            for (ISetting setting : module.getSettings()) {
                Setting s;
                String lowerCaseSettingName;
                if (!(setting instanceof Setting) || !(lowerCaseSettingName = (s = (Setting)setting).getName().toLowerCase()).contains(lowerCaseQuery) && !lowerCaseSettingName.contains(translitQuery) || this.searchResults.contains(s)) continue;
                this.searchResults.add(s);
            }
        }
        this.searchResults.sort((o1, o2) -> {
            if (o1 instanceof Module && o2 instanceof Setting) {
                return -1;
            }
            if (o1 instanceof Setting && o2 instanceof Module) {
                return 1;
            }
            return 0;
        });
        this.searchResultsDirty = true;
    }

    private String translit(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); ++i) {
            String ch = String.valueOf(text.charAt(i));
            sb.append(TRANSLIT_MAP.getOrDefault(ch, ch));
        }
        return sb.toString();
    }

    static {
        TRANSLIT_MAP.put("а", "a");
        TRANSLIT_MAP.put("б", "b");
        TRANSLIT_MAP.put("в", "v");
        TRANSLIT_MAP.put("г", "g");
        TRANSLIT_MAP.put("д", "d");
        TRANSLIT_MAP.put("е", "e");
        TRANSLIT_MAP.put("ё", "e");
        TRANSLIT_MAP.put("ж", "zh");
        TRANSLIT_MAP.put("з", "z");
        TRANSLIT_MAP.put("и", "i");
        TRANSLIT_MAP.put("й", "y");
        TRANSLIT_MAP.put("к", "k");
        TRANSLIT_MAP.put("л", "l");
        TRANSLIT_MAP.put("м", "m");
        TRANSLIT_MAP.put("н", "n");
        TRANSLIT_MAP.put("о", "o");
        TRANSLIT_MAP.put("п", "p");
        TRANSLIT_MAP.put("р", "r");
        TRANSLIT_MAP.put("с", "s");
        TRANSLIT_MAP.put("т", "t");
        TRANSLIT_MAP.put("у", "u");
        TRANSLIT_MAP.put("ф", "f");
        TRANSLIT_MAP.put("х", "h");
        TRANSLIT_MAP.put("ц", "ts");
        TRANSLIT_MAP.put("ч", "ch");
        TRANSLIT_MAP.put("ш", "sh");
        TRANSLIT_MAP.put("щ", "sch");
        TRANSLIT_MAP.put("ъ", "");
        TRANSLIT_MAP.put("ы", "y");
        TRANSLIT_MAP.put("ь", "");
        TRANSLIT_MAP.put("э", "e");
        TRANSLIT_MAP.put("ю", "yu");
        TRANSLIT_MAP.put("я", "ya");
    }
}
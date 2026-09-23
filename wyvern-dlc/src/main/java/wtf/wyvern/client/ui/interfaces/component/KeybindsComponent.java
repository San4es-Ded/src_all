package wtf.wyvern.client.ui.interfaces.component;

import java.util.Iterator;
import java.util.List;
import org.joml.Vector4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.core.theme.Theme;
import wtf.wyvern.client.ui.interfaces.draggable.DraggableHudElement;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.setting.Setting;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.render.display.Keyboard;
import wtf.wyvern.render.display.StencilUtil;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;
import net.minecraft.client.gui.screen.ChatScreen;

public class KeybindsComponent extends DraggableHudElement {
    private static final float V2_MIN_WIDTH = 82.0F;
    private static final float V2_MAX_WIDTH = 112.0F;
    private static final float V2_TEXT_SIZE = 7.2F;
    private final Animation widthAnimation;
    private final Animation xLine;
    private final Animation alpha;
    private final boolean v2;

    public KeybindsComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight, float offsetX, float offsetY, Align align, boolean v2) {
        super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
        this.widthAnimation = new Animation(200L, Easing.CUBIC_OUT);
        this.xLine = new Animation(170L, Easing.SINE_OUT);
        this.alpha = new Animation(200L, Easing.CUBIC_OUT);
        this.v2 = v2;
    }

    public void render(CustomDrawContext ctx) {
        float posX = this.getX();
        float posY = this.getY();
        Theme theme = Wyvern.getInstance().getThemeManager().getCurrentTheme();
        ColorRGBA themeColor = theme.getColor();

        if (v2) {
            renderV2(ctx, posX, posY, themeColor);
        } else {
            renderClassic(ctx, posX, posY, themeColor);
        }
    }

    private void renderV2(CustomDrawContext ctx, float posX, float posY, ColorRGBA themeColor) {
        boolean isFound = hasActiveBind();

        if (!isFound && !(mc.currentScreen instanceof ChatScreen)) {
            this.alpha.update(0.0F);
        } else {
            this.alpha.update(1.0F);
        }

        if (this.alpha.getValue() < 0.01F) return;

        float modulesHeight = 0.0F;
        float maxWidth = 0.0F;
        List<String[]> activeBinds = new java.util.ArrayList<>();

        // Считаем размеры элементов
        for (Module module : Wyvern.getInstance().getModuleManager().getModules()) {
            if (module.getKeyCode() != -1 && module.getAnimation().getValue() > 0.01F) {
                String bind = Keyboard.getKeyName(module.getKeyCode());
                activeBinds.add(new String[]{module.getName(), bind, String.valueOf(module.getAnimation().getValue())});
                float w = rowWidth(module.getName(), bind);
                maxWidth = Math.max(maxWidth, w);
                modulesHeight += 12.0F * module.getAnimation().getValue();
            }
            for (Setting setting : module.getSettings()) {
                if (module.isEnabled() && setting instanceof BooleanSetting bool && bool.getKeyCode() != -1) {
                    float sAnim = bool.getAnimation().getValue();
                    if (sAnim > 0.01F) {
                        String bind = Keyboard.getKeyName(bool.getKeyCode());
                        activeBinds.add(new String[]{bool.getName(), bind, String.valueOf(sAnim)});
                        float w = rowWidth(bool.getName(), bind);
                        maxWidth = Math.max(maxWidth, w);
                        modulesHeight += 12.0F * sAnim;
                    }
                }
            }
        }

        // --- ГЕОМЕТРИЯ ОКНА ---
        float headerHeight = 18.9F;
        float bodyHeight = modulesHeight + 6.0F;
        float totalHeight = headerHeight + bodyHeight;

        float targetWidth = Math.max(V2_MIN_WIDTH, Math.min(V2_MAX_WIDTH, maxWidth));
        this.widthAnimation.update(targetWidth);
        float animWidth = this.widthAnimation.getValue();

        float cornerRadius = 5.5F;
        float currentAlpha = this.alpha.getValue();

        // --- НАСТРОЙКА ИДЕАЛЬНОЙ ОБВОДКИ (#171717) ---
        Vector4f outlineVec = new Vector4f(0.09F, 0.09F, 0.09F, 1F * currentAlpha);
        ColorRGBA figmaOutlineColor = new ColorRGBA((int)(outlineVec.x * 255), (int)(outlineVec.y * 255), (int)(outlineVec.z * 255), (int)(outlineVec.w * 255));
        float strokeThickness = 1.0F;

        // Отрисовываем внешнюю плашку-обводку
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX, posY, animWidth, totalHeight, BorderRadius.all(cornerRadius), figmaOutlineColor);

        // --- ЦВЕТ ДЛЯ РАЗДЕЛИТЕЛЕЙ (#535353) ---
        ColorRGBA separatorColor = ColorRGBA.fromHex("#535353");

        // Запускаем маску стенсиля, сдвинутую внутрь на толщину обводки
        StencilUtil.push();
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX + strokeThickness, posY + strokeThickness, animWidth - (strokeThickness * 2), totalHeight - (strokeThickness * 2), BorderRadius.all(cornerRadius - strokeThickness), ColorRGBA.BLACK);

        StencilUtil.read(1);

        // --- ВЕРХНИЙ РЕКТ (ШАПКА #0B0B0B / 11 11 11) ---
        Vector4f topVec = new Vector4f(0.043F, 0.043F, 0.043F, 1.0F * currentAlpha);
        ColorRGBA topRectColor = new ColorRGBA((int)(topVec.x * 255), (int)(topVec.y * 255), (int)(topVec.z * 255), (int)(topVec.w * 255));
        BorderRadius topRounding = new BorderRadius(cornerRadius - strokeThickness, cornerRadius - strokeThickness, 0.0F, 0.0F);

        // Отрисовка шапки с учетом сдвига
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX + strokeThickness, posY + strokeThickness, animWidth - (strokeThickness * 2), headerHeight - strokeThickness, topRounding, topRectColor);

        // --- НИЖНИЙ РЕКТ (ТЕЛО С МОДУЛЯМИ #111111) ---
        float bodyY = posY + headerHeight;
        Vector4f bottomVec = new Vector4f(0.066F, 0.066F, 0.066F, 1.0F * currentAlpha);
        ColorRGBA bottomRectColor = new ColorRGBA((int)(bottomVec.x * 255), (int)(bottomVec.y * 255), (int)(bottomVec.z * 255), (int)(bottomVec.w * 255));
        BorderRadius bottomRounding = new BorderRadius(0.0F, 0.0F, cornerRadius - strokeThickness, cornerRadius - strokeThickness);

        // Отрисовка тела с учетом сдвига
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX + strokeThickness, bodyY, animWidth - (strokeThickness * 2), bodyHeight - strokeThickness, bottomRounding, bottomRectColor);

        StencilUtil.pop();

        // --- СОДЕРЖИМОЕ ШАПКИ ---
        float iconSize = 11.0F;
        float textX = posX + 6.5F;

        // Иконка клавиатуры "t"
        ctx.drawText(Fonts.WYVERN.getFont(iconSize), "QWERTYUIOP", textX, posY + 6.0F, themeColor.withAlpha((int)(255 * currentAlpha)));

        // Слэш "/"
        float slashX = textX + Fonts.WYVERN.getWidth("QWERTYUIOP", iconSize) + 1.5F;
        ctx.drawText(Fonts.BOLD.getFont(7.5F), "/", slashX, posY + 6.6F, ColorRGBA.WHITE.withAlpha((int)(110 * currentAlpha)));

        // Текст "Binds"
        float titleX = slashX + Fonts.BOLD.getWidth("/", 7.5F) + 3.0F;
        float titleW = Fonts.MEDIUM.getWidth("Binds", 7.5F);
        float glowWidth = 60.0F;
         float glowHeight = 35.0F;
         ctx.drawTexture(Wyvern.id("icons/glow.png"), titleX + (titleW / 2.0F) - (glowWidth / 2.0F), posY + 7.0F + (7.5F / 2.0F) - (glowHeight / 2.0F), glowWidth, glowHeight, ColorRGBA.WHITE.withAlpha((int)(18 * currentAlpha)));
        ctx.drawText(Fonts.MEDIUM.getFont(7.5F), "Binds", titleX, posY + 7.0F, ColorRGBA.WHITE.withAlpha((int)(230 * currentAlpha)));

        drawHeaderDots(ctx, posX, posY, animWidth, themeColor, currentAlpha);

        // --- СПИСОК БИНДОВ ---
        float currentY = bodyY + 1.5f;
        for (String[] bindInfo : activeBinds) {
            float anim = Float.parseFloat(bindInfo[2]);
            String key = fitMedium(bindInfo[1], 31.0F, V2_TEXT_SIZE);
            float keyW = Fonts.MEDIUM.getWidth(key, V2_TEXT_SIZE);
            float separatorW = Fonts.MEDIUM.getWidth("|", 6.5F);
            float keyX = posX + animWidth - keyW - 6.5F;
            float separatorX = keyX - separatorW - 4.0F;
            float nameX = posX + 6.5F;
            float maxNameW = Math.max(0.0F, separatorX - nameX - 4.0F);
            String text = fitMedium(bindInfo[0], maxNameW, V2_TEXT_SIZE);

            // Имя модуля
            ctx.drawText(Fonts.MEDIUM.getFont(V2_TEXT_SIZE), text, nameX, currentY + 3.4F, ColorRGBA.WHITE.withAlpha((int)(255 * currentAlpha * anim)));

            // Разделитель "|"
            ctx.drawText(Fonts.MEDIUM.getFont(6.5F), "|", separatorX, currentY + 3.2F, separatorColor.withAlpha((int)(255 * currentAlpha * anim)));

            // Кнопка бинда
            ctx.drawText(Fonts.MEDIUM.getFont(V2_TEXT_SIZE), key, keyX, currentY + 3.4F, themeColor.withAlpha((int)(255 * currentAlpha * anim)));

            currentY += 12.0F * anim;
        }

        this.width = animWidth;
        this.height = totalHeight;
    }

    private void renderClassic(CustomDrawContext ctx, float posX, float posY, ColorRGBA themeColor) {
        boolean isFound = hasActiveBind();

        if (!isFound && !(mc.currentScreen instanceof ChatScreen)) {
            this.alpha.update(0.0F);
        } else {
            this.alpha.update(1.0F);
        }

        if (this.alpha.getValue() < 0.01F) return;

        float modulesHeight = 0.0F;
        float maxWidth = 0.0F;
        List<String[]> activeBinds = new java.util.ArrayList<>();

        for (Module module : Wyvern.getInstance().getModuleManager().getModules()) {
            if (module.getKeyCode() != -1 && module.getAnimation().getValue() > 0.01F) {
                String bind = Keyboard.getKeyName(module.getKeyCode());
                float anim = module.getAnimation().getValue();
                activeBinds.add(new String[]{module.getName(), bind, String.valueOf(anim)});
                float nameWidth = Fonts.MEDIUM.getWidth(module.getName(), 7.0F);
                float bindWidth = Fonts.MEDIUM.getWidth(bind, 7.0F);
                float totalWidth = nameWidth + bindWidth + 16.0F;
                maxWidth = Math.max(maxWidth, totalWidth);
                modulesHeight += 12.0F * anim;
            }
            for (Setting setting : module.getSettings()) {
                if (module.isEnabled() && setting instanceof BooleanSetting bool && bool.getKeyCode() != -1) {
                    float sAnim = bool.getAnimation().getValue();
                    if (sAnim > 0.01F) {
                        String bind = Keyboard.getKeyName(bool.getKeyCode());
                        activeBinds.add(new String[]{bool.getName(), bind, String.valueOf(sAnim)});
                        float nameWidth = Fonts.MEDIUM.getWidth(bool.getName(), 7.0F);
                        float bindWidth = Fonts.MEDIUM.getWidth(bind, 7.0F);
                        float totalWidth = nameWidth + bindWidth + 16.0F;
                        maxWidth = Math.max(maxWidth, totalWidth);
                        modulesHeight += 12.0F * sAnim;
                    }
                }
            }
        }

        float headerHeight = 13.5F;
        float currentWidth = Math.max(maxWidth, 82.0F);
        this.widthAnimation.update(currentWidth);
        float animWidth = this.widthAnimation.getValue();
        float totalHeight = headerHeight + modulesHeight + 6.0F;
        float rounding = 4.0F;

        ColorRGBA darkerColor = themeColor.darker(0.95F);

        DrawUtil.drawBlur(ctx.getMatrices(), posX, posY, animWidth, headerHeight, 30.0F,
                new BorderRadius(rounding, rounding, 0, 0),
                ColorRGBA.WHITE.withAlpha((int)(255 * this.alpha.getValue())));

        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX, posY, animWidth, headerHeight,
                new BorderRadius(rounding, rounding, 0, 0), darkerColor.withAlpha((int)(250 * this.alpha.getValue())));

        float iconSize = 6.5F;
        float textSize = 7.5F;
        String icon = "L";
        float iconWidth = Fonts.ICONS.getWidth(icon, iconSize);
        float headerTextWidth = Fonts.REGULAR.getWidth("Keybinds", textSize);
        float spacing = 3.0F;
        float totalHeaderWidth = iconWidth + spacing + headerTextWidth;
        float headerX = posX + (animWidth - totalHeaderWidth) / 2.0F;

        ctx.drawText(Fonts.ICONS.getFont(iconSize), icon, headerX - 15f, posY + 5f,
                themeColor.withAlpha((int)(255 * this.alpha.getValue())));
        ctx.drawText(Fonts.REGULAR.getFont(textSize), "Keybinds", headerX - 16f + iconWidth + spacing, posY + 5f,
                ColorRGBA.WHITE.withAlpha((int)(255 * this.alpha.getValue())));

        float currentY = posY + headerHeight + 3.0F;
        float itemHeight = 10.0F;
        float itemPadding = 2.0F;

        for (String[] bindInfo : activeBinds) {
            String name = bindInfo[0];
            String bind = bindInfo[1];
            float anim = Float.parseFloat(bindInfo[2]);

            float bindWidth = Fonts.REGULAR.getWidth(bind, 7.0F);

            DrawUtil.drawBlur(ctx.getMatrices(), posX + 3.0F, currentY, animWidth - 6.0F, itemHeight, 20.0F,
                    BorderRadius.all(3.0F), ColorRGBA.WHITE.withAlpha((int)(200 * this.alpha.getValue() * anim)));

            DrawUtil.drawRoundedRect(ctx.getMatrices(), posX + 3.0F, currentY, animWidth - 6.0F, itemHeight,
                    BorderRadius.all(3.0F), darkerColor.withAlpha((int)(180 * this.alpha.getValue() * anim)));

            float maxNameWidth = Math.max(0.0F, animWidth - bindWidth - 15.0F);
            String visibleName = fitRegular(name, maxNameWidth, 7.0F);
            ctx.drawText(Fonts.REGULAR.getFont(7.0F), visibleName, posX + 6.0F, currentY + 2.5F,
                    ColorRGBA.WHITE.withAlpha((int)(255 * this.alpha.getValue() * anim)));
            ctx.drawText(Fonts.REGULAR.getFont(7.0F), bind, posX + animWidth - bindWidth - 6.0F, currentY + 2.5F,
                    themeColor.withAlpha((int)(255 * this.alpha.getValue() * anim)));

            currentY += (itemHeight + itemPadding) * anim;
        }

        this.width = animWidth;
        this.height = totalHeight;
    }

    private float rowWidth(String name, String key) {
        return Fonts.MEDIUM.getWidth(name, V2_TEXT_SIZE)
                + Fonts.MEDIUM.getWidth(key, V2_TEXT_SIZE)
                + Fonts.MEDIUM.getWidth("|", 6.5F) + 25.0F;
    }

    private void drawHeaderDots(CustomDrawContext ctx, float posX, float posY, float panelWidth,
                                ColorRGBA color, float alphaValue) {
        String dots = "...";
        float size = 11.0F;
        float dotsWidth = Fonts.BOLD.getWidth(dots, size);
        ctx.drawText(Fonts.BOLD.getFont(size), dots, posX + panelWidth - dotsWidth - 6.0F,
                posY + 3.3F, color.withAlpha((int) (255 * alphaValue)));
    }

    private String fitMedium(String text, float maxWidth, float size) {
        return fitText(text, maxWidth, size, true);
    }

    private String fitRegular(String text, float maxWidth, float size) {
        return fitText(text, maxWidth, size, false);
    }

    private String fitText(String text, float maxWidth, float size, boolean medium) {
        if (maxWidth <= 0.0F) return "";
        float textWidth = medium ? Fonts.MEDIUM.getWidth(text, size) : Fonts.REGULAR.getWidth(text, size);
        if (textWidth <= maxWidth) return text;
        String dots = "..";
        float dotsWidth = medium ? Fonts.MEDIUM.getWidth(dots, size) : Fonts.REGULAR.getWidth(dots, size);
        if (dotsWidth > maxWidth) return "";

        StringBuilder result = new StringBuilder();
        float width = 0.0F;
        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));
            float characterWidth = medium
                    ? Fonts.MEDIUM.getWidth(character, size)
                    : Fonts.REGULAR.getWidth(character, size);
            if (width + characterWidth + dotsWidth > maxWidth) break;
            result.append(character);
            width += characterWidth;
        }
        return result + dots;
    }

    /** A boolean setting is a real bind only while its parent module is active. */
    private boolean hasActiveBind() {
        for (Module module : Wyvern.getInstance().getModuleManager().getModules()) {
            if (!module.isEnabled()) continue;
            if (module.getKeyCode() != -1) return true;
            for (Setting setting : module.getSettings()) {
                if (setting instanceof BooleanSetting bool && bool.getKeyCode() != -1) return true;
            }
        }
        return false;
    }
}

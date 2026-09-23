package wtf.wyvern.client.ui.interfaces.component;

import wtf.wyvern.Wyvern;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.core.theme.Theme;
import wtf.wyvern.client.ui.interfaces.draggable.DraggableHudElement;
import wtf.wyvern.render.display.StencilUtil;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.screen.ChatScreen;
import org.joml.Vector4f;

public class PotionsComponent extends DraggableHudElement {
    private static final Map<StatusEffect, Identifier> EFFECT_ICON_CACHE = new java.util.IdentityHashMap<>();
    private final Animation widthAnimation;
    private final Animation xLine;
    private final Animation alpha;
    private final List<PotionItem> potionItems;
    private final boolean v2;
    private int lastPotionsTick = -1;
    private int lastEffectCount = -1;

    public PotionsComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight, float offsetX, float offsetY, Align align, boolean v2) {
        super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
        this.widthAnimation = new Animation(200L, Easing.CUBIC_OUT);
        this.xLine = new Animation(170L, Easing.SINE_OUT);
        this.alpha = new Animation(200L, Easing.CUBIC_OUT);
        this.potionItems = new CopyOnWriteArrayList<>();
        this.v2 = v2;
    }

    public void render(CustomDrawContext ctx) {
        if (mc.player != null) {
            // Durations only change once per tick, so rebuilding the effect map (streams +
            // Text.translatable per effect) every frame was wasted work. Refresh on tick
            // change or when the effect count changes (instant pickup of new effects).
            int tick = mc.player.age;
            int effectCount = mc.player.getStatusEffects().size();
            if (tick != this.lastPotionsTick || effectCount != this.lastEffectCount) {
                this.lastPotionsTick = tick;
                this.lastEffectCount = effectCount;
                this.updatePotions();
            }
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
    }

    private void renderV2(CustomDrawContext ctx, float posX, float posY, ColorRGBA themeColor) {
        boolean isFound = false;
        for (PotionItem item : this.potionItems) {
            item.animation.update(item.active);
            if (item.animation.getValue() > 0.01F) {
                isFound = true;
            }
        }

        if (!isFound && !(mc.currentScreen instanceof ChatScreen)) {
            this.alpha.update(0.0F);
        } else {
            this.alpha.update(1.0F);
        }

        if (this.alpha.getValue() < 0.01F) return;

        float potionsHeight = 0.0F;
        float maxWidth = 0.0F;
        List<PotionItem> activePotions = new java.util.ArrayList<>();

        // Считаем размеры элементов
        for (PotionItem item : this.potionItems) {
            float anim = item.animation.getValue();
            if (anim > 0.01F) {
                String name = I18n.translate(item.name, new Object[0]);
                if (item.amplifier > 0) name += " " + (item.amplifier + 1);
                String duration = this.formatDuration(item.durationTicks);

                // Считаем ширину строки, заложив место под увеличенную иконку
                float w = Fonts.MEDIUM.getWidth(name + "  | " + duration, 7.2F) + 13.0F;
                maxWidth = Math.max(maxWidth, w);
                potionsHeight += 12.0F * anim;
                activePotions.add(item);
            }
        }

        // --- ГЕОМЕТРИЯ ОКНА ---
        float headerHeight = 18.9F;
        float bodyHeight = potionsHeight + 6.0F;
        float totalHeight = headerHeight + bodyHeight;

        float targetWidth = Math.max(maxWidth + 16.0F, 82.0F);
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

        // --- НИЖНИЙ РЕКТ (ТЕЛО С ЭФФЕКТАМИ #111111) ---
        float bodyY = posY + headerHeight;
        Vector4f bottomVec = new Vector4f(0.066F, 0.066F, 0.066F, 1.0F * currentAlpha);
        ColorRGBA bottomRectColor = new ColorRGBA((int)(bottomVec.x * 255), (int)(bottomVec.y * 255), (int)(bottomVec.z * 255), (int)(bottomVec.w * 255));
        BorderRadius bottomRounding = new BorderRadius(0.0F, 0.0F, cornerRadius - strokeThickness, cornerRadius - strokeThickness);

        // Отрисовка тела с учетом сдвига
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX + strokeThickness, bodyY, animWidth - (strokeThickness * 2), bodyHeight - strokeThickness, bottomRounding, bottomRectColor);

        StencilUtil.pop();

        // --- СОДЕРЖИМОЕ ШАПКИ ---
        float iconSize = 11F;
        float textX = posX + 6.5F;

        // Иконка зелий "u"
        ctx.drawText(Fonts.WYVERN.getFont(iconSize), "HJKLZX", textX, posY + 5.5f, themeColor.withAlpha((int)(255 * currentAlpha)));

        // Слэш "/"
        float slashX = textX + Fonts.WYVERN.getWidth("HJKLZX", iconSize) + 2f;
        ctx.drawText(Fonts.BOLD.getFont(7.5F), "/", slashX, posY + 6.6F, ColorRGBA.WHITE.withAlpha((int)(110 * currentAlpha)));

        // Текст "Potions"
        float titleX = slashX + Fonts.BOLD.getWidth("/", 7.5F) + 3.0F;
        float titleW = Fonts.MEDIUM.getWidth("Potions", 7.5F);
        float glowWidth = 60.0F;
        float glowHeight = 35.0F;
        ctx.drawTexture(Wyvern.id("icons/glow.png"), titleX + (titleW / 2.0F) - (glowWidth / 2.0F), posY + 7.0F + (7.5F / 2.0F) - (glowHeight / 2.0F), glowWidth, glowHeight, ColorRGBA.WHITE.withAlpha((int)(18 * currentAlpha)));
        ctx.drawText(Fonts.MEDIUM.getFont(7.5F), "Potions", titleX, posY + 7.0F, ColorRGBA.WHITE.withAlpha((int)(230 * currentAlpha)));

        // Крупные три точки "..." справа
        String dotsMenu = "...";
        float dotsW = Fonts.WYVERN.getWidth(dotsMenu, 10.5F);
        ctx.drawText(Fonts.WYVERN.getFont(10.5F), dotsMenu, posX + animWidth - dotsW - 3.5F, posY + 5.0F, themeColor.withAlpha((int)(255 * currentAlpha)));

        // --- ТРИ ЖИРНЫЕ ТОЧКИ ПО ЦЕНТРУ ---
        String topDots = "...";
        float topDotsSize = 11.0F;
        float topDotsW = Fonts.BOLD.getWidth(topDots, topDotsSize);
        float topDotsX = posX + animWidth - topDotsW - 6.5F;
        float topDotsY = posY + 3.3F;
        ctx.drawText(Fonts.BOLD.getFont(topDotsSize), topDots, topDotsX, topDotsY, themeColor.withAlpha((int)(255 * currentAlpha)));

        // --- СПИСОК ЭФФЕКТОВ ---
        float currentY = bodyY + 1.5F;
        for (PotionItem item : activePotions) {
            float anim = item.animation.getValue();
            String name = I18n.translate(item.name, new Object[0]);
            if (item.amplifier > 0) name += " " + (item.amplifier + 1);
            String duration = this.formatDuration(item.durationTicks);
            Identifier icon = this.getEffectIcon((StatusEffect)item.effect.getEffectType().value());

            int finalAlpha = (int)(255 * currentAlpha * anim);
            float durW = Fonts.MEDIUM.getWidth(duration, 7.2F);
            float separatorW = Fonts.MEDIUM.getWidth("| ", 6.5F);

            // Расчет лимита под название
            float maxNameW = animWidth - durW - separatorW - 22.0F;
            float textW = Fonts.MEDIUM.getWidth(name, 7.2F);

            if (textW > maxNameW && maxNameW > 0) {
                String dots = "..";
                float dotsCharW = Fonts.MEDIUM.getWidth(dots, 7.2F);
                StringBuilder truncated = new StringBuilder();
                float cw = 0;
                for (int ci = 0; ci < name.length(); ci++) {
                    float charW = Fonts.MEDIUM.getWidth(String.valueOf(name.charAt(ci)), 7.2F);
                    if (cw + charW + dotsCharW > maxNameW) break;
                    truncated.append(name.charAt(ci));
                    cw += charW;
                }
                name = truncated + dots;
            }

            // Отрисовка иконки зелья
            ctx.drawTexture(icon, posX + 6.0F, currentY + 1.4F, 8.5F, 8.5F, ColorRGBA.WHITE.withAlpha(finalAlpha));

            // Название эффекта
            ctx.drawText(Fonts.MEDIUM.getFont(7.2F), name, posX + 18.0F, currentY + 3.4F, ColorRGBA.WHITE.withAlpha(finalAlpha));

            // Разделитель "|"
            float sepX = posX + animWidth - durW - separatorW - 6.5F - 0.5F;
            ctx.drawText(Fonts.MEDIUM.getFont(6.5F), "|", sepX, currentY + 3.2F, separatorColor.withAlpha((int)(255 * currentAlpha * anim)));

            // Таймер действия справа
            float durX = posX + animWidth - durW - 6.5F;
            ctx.drawText(Fonts.MEDIUM.getFont(7.2F), duration, durX, currentY + 3.4F, themeColor.withAlpha(finalAlpha));

            currentY += 12.0F * anim;
        }

        this.width = animWidth;
        this.height = totalHeight;
    }

    private void renderClassic(CustomDrawContext ctx, float posX, float posY, ColorRGBA themeColor) {
        boolean isFound = false;
        float maxNameWidth = 0.0F;
        float maxDurationWidth = 0.0F;
        float potionsHeight = 0.0F;

        for(PotionItem item : this.potionItems) {
            item.animation.update(item.active);
            float anim = item.animation.getValue();
            if (anim > 0.01F) {
                String name = I18n.translate(item.name, new Object[0]);
                if (item.amplifier > 0) {
                    name = name + " " + (item.amplifier + 1);
                }
                String duration = this.formatDuration(item.durationTicks);
                float nameW = Fonts.REGULAR.getWidth(name, 7.2F);
                float durW = Fonts.REGULAR.getWidth(duration, 7.2F);

                maxNameWidth = Math.max(maxNameWidth, nameW * anim + 10.0F);
                maxDurationWidth = Math.max(maxDurationWidth, durW * anim);
                potionsHeight += 11.0F * anim;
                isFound = true;
            }
        }

        if (!isFound && !(mc.currentScreen instanceof ChatScreen)) {
            this.alpha.update(0.0F);
        } else {
            this.alpha.update(1.0F);
        }

        if (mc.currentScreen instanceof ChatScreen) {
            this.alpha.update(1.0F);
        }

        float headerHeight = 15.0F;
        float footerHeight = 4.0F;
        float bodyHeight = potionsHeight + footerHeight;
        float totalHeight = headerHeight + bodyHeight;
        float targetWidth = Math.max(maxNameWidth + maxDurationWidth + 25.0F, 80.0F);
        this.widthAnimation.update(targetWidth);
        float currentWidth = this.widthAnimation.getValue();

        if (this.alpha.getValue() > 0.01F) {
            float rounding = 4.0F;
            ColorRGBA headerColor = new ColorRGBA(0, 0, 0, (int)(255 * this.alpha.getValue()));
            ColorRGBA bodyColor = new ColorRGBA(0, 0, 0, (int)(125 * this.alpha.getValue()));

            DrawUtil.drawBlur(ctx.getMatrices(), posX, posY, currentWidth, totalHeight, 15.0F, BorderRadius.all(rounding), ColorRGBA.WHITE.withAlpha((int)(255 * this.alpha.getValue())));
            DrawUtil.drawRoundedRect(ctx.getMatrices(), posX, posY, currentWidth, totalHeight, BorderRadius.all(rounding), bodyColor);
            DrawUtil.drawRoundedRect(ctx.getMatrices(), posX, posY, currentWidth, headerHeight, new BorderRadius(rounding, rounding, 0, 0), headerColor);

            ctx.drawText(Fonts.REGULAR.getFont(8.0F), "Potions", posX + 7.0F, posY + 4.5F, ColorRGBA.WHITE.withAlpha((int)(255 * this.alpha.getValue())));
            ctx.drawText(Fonts.NURIKI.getFont(9.5f), "E", posX - 1f + currentWidth - 14.0F, posY + 5.5F, themeColor.withAlpha((int)(255 * this.alpha.getValue())));

            float potionY = posY + headerHeight + 2.0F;
            float durEndX = posX + currentWidth - 8.0F;

            for(PotionItem item : this.potionItems) {
                float anim = item.animation.getValue();
                if (anim > 0.01F) {
                    String name = I18n.translate(item.name, new Object[0]);
                    if (item.amplifier > 0) {
                        name = name + " " + (item.amplifier + 1);
                    }
                    String duration = this.formatDuration(item.durationTicks);
                    Identifier icon = this.getEffectIcon((StatusEffect)item.effect.getEffectType().value());

                    ColorRGBA iconC = themeColor.withAlpha((int)(255 * this.alpha.getValue() * anim));
                    ColorRGBA textC = ColorRGBA.WHITE.withAlpha((int)(255 * this.alpha.getValue() * anim));

                    ctx.drawTexture(icon, posX + 6.0F, potionY + 1.0F, 8.0F, 8.0F, ColorRGBA.WHITE.withAlpha((int)(255 * this.alpha.getValue() * anim)));
                    ctx.drawText(Fonts.REGULAR.getFont(7.2F), name, posX + 16.5F, potionY + 2.5F, textC);
                    ctx.drawText(Fonts.REGULAR.getFont(7.2F), duration, durEndX - Fonts.REGULAR.getWidth(duration, 7.2F) + 1.0F, potionY + 2.5F, iconC);

                    potionY += 11.0F * anim;
                }
            }
        }

        this.width = currentWidth;
        this.height = totalHeight;
    }

    private String formatDuration(int durationTicks) {
        int totalSeconds = durationTicks / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private Identifier getEffectIcon(StatusEffect effect) {
        Identifier cached = EFFECT_ICON_CACHE.get(effect);
        if (cached == null) {
            String id = effect.getTranslationKey().replace("effect.minecraft.", "").replace("effect.", "");
            cached = Identifier.of("minecraft", "textures/mob_effect/" + id + ".png");
            EFFECT_ICON_CACHE.put(effect, cached);
        }
        return cached;
    }

    public void updatePotions() {
        if (mc.player != null) {
            Map<String, StatusEffectInstance> currentEffects = (Map)mc.player.getStatusEffects().stream().collect(Collectors.toMap((e) -> {
                String var10000 = Text.translatable(e.getTranslationKey()).getString();
                return var10000 + ":" + e.getAmplifier();
            }, (e) -> e, (e1, e2) -> e1));
            this.potionItems.forEach((item) -> {
                String key = item.name + ":" + item.amplifier;
                StatusEffectInstance effect = (StatusEffectInstance)currentEffects.get(key);
                if (effect != null) {
                    item.durationTicks = effect.getDuration();
                    if (!item.active) {
                        item.animation.setValue(1.0F);
                    }

                    item.active = true;
                    currentEffects.remove(key);
                } else {
                    item.active = false;
                }

            });
            currentEffects.forEach((key, effect) -> this.potionItems.add(new PotionItem(Text.translatable(effect.getTranslationKey()).getString(), effect.getAmplifier(), effect.getDuration(), effect)));
            this.potionItems.removeIf((item) -> !item.active && item.animation.getValue() == 0.0F);
        }
    }

    private static class PotionItem {
        String name;
        int amplifier;
        int durationTicks;
        boolean active;
        StatusEffectInstance effect;
        Animation animation;

        PotionItem(String name, int amplifier, int durationTicks, StatusEffectInstance effect) {
            this.animation = new Animation(250L, Easing.CUBIC_OUT);
            this.name = name;
            this.amplifier = amplifier;
            this.durationTicks = durationTicks;
            this.active = true;
            this.effect = effect;
        }
    }
}
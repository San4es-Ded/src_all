package wtf.wyvern.client.ui.interfaces.component;

import java.util.List;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.core.theme.Theme;
import wtf.wyvern.client.ui.interfaces.draggable.DraggableHudElement;
import wtf.wyvern.client.modules.impl.combat.Aura;
import wtf.wyvern.client.modules.impl.misc.NameProtect;
import wtf.wyvern.client.modules.impl.misc.ScoreboardHealth;
import wtf.wyvern.utility.game.player.PlayerIntersectionUtil;
import wtf.wyvern.mixin.accessors.DrawContextAccessor;
import wtf.wyvern.render.display.StencilUtil;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;

public class TargetHudComponent extends DraggableHudElement {
    private final Animation healthAnimation;
    private final Animation outdatedHealthAnimation;
    private final Animation gappleAnimation;
    private final Animation toggleAnimation;
    private final Animation toggleAnimationMetanoise;
    private LivingEntity target;
    private final boolean v2;

    public TargetHudComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight, float offsetX, float offsetY, Align align, boolean v2) {
        super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
        this.healthAnimation = new Animation(320L, Easing.CUBIC_OUT);
        this.outdatedHealthAnimation = new Animation(760L, Easing.CUBIC_OUT);
        this.gappleAnimation = new Animation(320L, Easing.CUBIC_OUT);
        this.toggleAnimation = new Animation(250L, Easing.CUBIC_OUT);
        this.toggleAnimationMetanoise = new Animation(1850L, Easing.CUBIC_OUT);
        this.v2 = v2;
    }

    public TargetHudComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight, float offsetX, float offsetY, Align align) {
        this(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align, false);
    }

    public void render(CustomDrawContext ctx) {
        Aura aura = Aura.INSTANCE;
        LivingEntity target = mc.currentScreen instanceof ChatScreen ? mc.player : aura.getTarget();
        this.setTarget((LivingEntity)target);
        if (this.toggleAnimationMetanoise.getValue() != 0.0F && this.target != null) {
            if (v2) {
                renderV2(ctx, this.target, this.toggleAnimation.getValue());
            } else {
                renderClassic(ctx, this.target, this.toggleAnimation.getValue());
            }
        }
    }
    private void renderV2(CustomDrawContext ctx, LivingEntity target, float animation) {
        float posX = this.getX();
        float posY = this.getY();

        float height = 36.0F;
        float leftBlockSize = 36.0F;
        float cornerRadius = 5.5F;
        float stroke = 1.0F;
        // GAP УБРАН, ЧТОБЫ БЛОКИ БЫЛИ ПРИТЫКНУТЫ

        // Расчет данных здоровья
        float hp = ScoreboardHealth.INSTANCE.isEnabled() ? PlayerIntersectionUtil.getHealth(target) : target.getHealth();
        this.healthAnimation.update(hp / target.getMaxHealth());
        if (this.outdatedHealthAnimation.getValue() < this.healthAnimation.getValue()) {
            this.outdatedHealthAnimation.setValue(this.healthAnimation.getValue());
            this.outdatedHealthAnimation.setStartValue(this.healthAnimation.getValue());
        } else {
            this.outdatedHealthAnimation.update(hp / target.getMaxHealth());
        }
        this.gappleAnimation.update(target.getAbsorptionAmount() / target.getMaxHealth());

        float currentAlpha = animation;
        int alphaInt = (int)(255 * currentAlpha);

        Theme theme = Wyvern.getInstance().getThemeManager().getCurrentTheme();
        ColorRGBA themeColor = theme.getColor().withAlpha(alphaInt);
        ColorRGBA whiteColor = ColorRGBA.WHITE.withAlpha(alphaInt);

        // --- ЦВЕТА ИЗ KeybindsComponent Renderv2 ---
        ColorRGBA outlineColor = new ColorRGBA(23, 23, 23, alphaInt);
        ColorRGBA headBgColor = new ColorRGBA(17, 17, 17, alphaInt);
        ColorRGBA infoBgColor = new ColorRGBA(11, 11, 11, alphaInt);

        // --- ПОДГОТОВКА ДАННЫХ СПРАВА ---
        String name = target == mc.player ? NameProtect.getCustomName() : target.getNameForScoreboard();
        if (name.length() > 14) name = name.substring(0, 14);

        String hpText = (int) hp + " hp";
        if (target.getAbsorptionAmount() > 0.0F) hpText += " + " + (int) target.getAbsorptionAmount();

        float nameW = Fonts.MEDIUM.getWidth(name, 8.5F);
        float hpW = Fonts.MEDIUM.getWidth(hpText, 7.0F);
        float barWidth = 65F;

        // УМЕНЬШИЛИ ОТСТУП С 16.0F ДО 12.0F, ЧТОБЫ УБРАТЬ ЛИШНИЙ ФОН СПРАВА
        float rightBlockWidth = Math.max(nameW, Math.max(hpW, barWidth)) + 8F;

        // --- ОТРИСОВКА ОДНОЙ ЕДИНОЙ ОБВОДКИ (Outline Rect) ---
        float totalWidth = leftBlockSize + rightBlockWidth;
        // Скругления: лево-верх, право-верх, право-низ, лево-низ
        BorderRadius totalOuterRounding = new BorderRadius(cornerRadius, cornerRadius, cornerRadius, cornerRadius);
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX, posY, totalWidth, height, totalOuterRounding, outlineColor);


        // --- ОТРИСОВКА ВНУТРЕННИХ ФОНОВ ---

        StencilUtil.push();
        // Создаем маску, сдвинутую внутрь на толщину обводки
        BorderRadius totalInnerRounding = new BorderRadius(cornerRadius - stroke, cornerRadius - stroke, cornerRadius - stroke, cornerRadius - stroke);
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX + stroke, posY + stroke, totalWidth - stroke * 2, height - stroke * 2, totalInnerRounding, ColorRGBA.BLACK);
        StencilUtil.read(1);

        // 1. Рисуем фон для головы (левый, #111111)
        BorderRadius headInnerRounding = new BorderRadius(cornerRadius - stroke, 0.0F, 0.0F, cornerRadius - stroke);
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX + stroke, posY + stroke, leftBlockSize - stroke, height - stroke * 2, headInnerRounding, headBgColor);

        // 2. Рисуем фон для инфо (правый, #0B0B0B)
        float posX2Inner = posX + leftBlockSize;
        BorderRadius infoInnerRounding = new BorderRadius(0.0F, cornerRadius - stroke, cornerRadius - stroke, 0.0F);
        // Обратите внимание: ширина правого блока чуть больше, чтобы перекрыть стык
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX2Inner, posY + stroke, rightBlockWidth - stroke, height - stroke * 2, infoInnerRounding, infoBgColor);

        StencilUtil.pop();


        // --- СОДЕРЖИМОЕ ---

        // 1. Голова
        Identifier skinTextures = resolveSkinTexture(target);

        float headSize = 27.5F;
        float hX = posX + (leftBlockSize - headSize) / 2.0F;
        float hY = posY + (height - headSize) / 2.0F;
        DrawUtil.drawPlayerHeadWithRoundedShader(ctx.getMatrices(), skinTextures, hX, hY, headSize, BorderRadius.all(cornerRadius), whiteColor);


        // 2. Текст и Полоска
        float posX2Text = posX + leftBlockSize;
        float contentX = posX2Text + 4.35F;

        // Никнейм
        ctx.drawText(Fonts.MEDIUM.getFont(8.5F), name, contentX, posY + 6.0F, whiteColor);

        // Текст ХП
        ctx.drawText(Fonts.MEDIUM.getFont(6.0F), hpText, contentX, posY + 16.5F,
                ColorRGBA.WHITE.withAlpha((int)(140 * currentAlpha)));

        // Полоска здоровья
        float barX = contentX - 1.5F;
        float barY = posY + height - 9.85F;
        float barHeight = 3.9F;
        float barRounding = 1.0F;

        ColorRGBA barBgColor = themeColor.darker(0.75F);
        DrawUtil.drawRoundedRect(ctx.getMatrices(), barX, barY, barWidth, barHeight, BorderRadius.all(barRounding), barBgColor);

        float healthFillWidth = MathHelper.clamp(barWidth * this.healthAnimation.getValue(), 0.0F, barWidth);
        if (healthFillWidth > 0.5F) {
            float glowWidth = Math.max(healthFillWidth + 18.0F, 26.0F);
            float glowHeight = 24.0F;
            float glowCenterX = barX + healthFillWidth / 2.0F;
            float glowCenterY = barY + barHeight / 2.0F;
            ctx.drawTexture(Wyvern.id("icons/glow.png"), glowCenterX - glowWidth / 2.0F,
                    glowCenterY - glowHeight / 2.0F, glowWidth, glowHeight,
                    themeColor.withAlpha((int)(28 * currentAlpha)));
        }

        ColorRGBA barDelayedColor = themeColor.darker(0.3F);
        DrawUtil.drawRoundedRect(ctx.getMatrices(), barX, barY, MathHelper.clamp(barWidth * this.outdatedHealthAnimation.getValue(), 0.0F, barWidth), barHeight, BorderRadius.all(barRounding), barDelayedColor);

        if (this.gappleAnimation.getValue() < this.healthAnimation.getValue()) {
            DrawUtil.drawRoundedRect(ctx.getMatrices(), barX, barY, healthFillWidth, barHeight, BorderRadius.all(barRounding), themeColor);
        }

        if (target.getAbsorptionAmount() > 0.0F) {
            DrawUtil.drawRoundedRect(ctx.getMatrices(), barX, barY, MathHelper.clamp(barWidth * this.gappleAnimation.getValue(), 0.0F, barWidth), barHeight, BorderRadius.all(barRounding),
                    new ColorRGBA(255, 209, 0, alphaInt), new ColorRGBA(255, 209, 0, alphaInt),
                    new ColorRGBA(255, 246, 20, alphaInt), new ColorRGBA(255, 246, 20, alphaInt));
        }

        // Три точки
        String dots = "...";
        float dotsW = Fonts.WYVERN.getWidth(dots, 10.5F);
        ctx.drawText(Fonts.WYVERN.getFont(10.5F), dots, posX + totalWidth - dotsW - 3.5F, posY + 5.0F, themeColor);

        // Отрисовка брони
        if (target instanceof PlayerEntity) {
            // Центрируем броню над объединенным ректом
            this.drawArmor(ctx, (PlayerEntity)target, posX + (totalWidth - 100.0F) / 2.0F, posY - 14.0F, animation);
        }

        this.width = totalWidth;
        this.height = height;
    }

    private void renderClassic(CustomDrawContext ctx, LivingEntity target, float animation) {
        float posX = this.getX();
        float posY = this.getY();
        float width = 100.0F;
        float height = 36.0F;
        Theme theme = Wyvern.getInstance().getThemeManager().getCurrentTheme();
        float hp = ScoreboardHealth.INSTANCE.isEnabled() ? PlayerIntersectionUtil.getHealth(target) : target.getHealth();
        this.healthAnimation.update(hp / target.getMaxHealth());
        if (this.outdatedHealthAnimation.getValue() < this.healthAnimation.getValue()) {
            this.outdatedHealthAnimation.setValue(this.healthAnimation.getValue());
            this.outdatedHealthAnimation.setStartValue(this.healthAnimation.getValue());
        } else {
            this.outdatedHealthAnimation.update(hp / target.getMaxHealth());
        }

        this.gappleAnimation.update(target.getAbsorptionAmount() / target.getMaxHealth());

        Vector4f rectRounding = new Vector4f(4.0F, 4.0F, 4.0F, 4.0F);
        ColorRGBA bgColor = new ColorRGBA(0, 0, 0, (int)(125 * animation));

        DrawUtil.drawBlur(ctx.getMatrices(), posX, posY, width, height, 15.0F, BorderRadius.all(rectRounding.x), ColorRGBA.WHITE.withAlpha((int)(255 * animation)));

        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX, posY, width, height, BorderRadius.all(rectRounding.x), bgColor);

        Identifier skinTextures = resolveSkinTexture(target);

        float headSize = 24.5F;
        DrawUtil.drawPlayerHeadWithRoundedShader(ctx.getMatrices(), skinTextures, posX + 4.5F, posY + 2.0F, headSize, BorderRadius.all(10.0F), ColorRGBA.WHITE.withAlpha(animation * 255.0F));

        float nameX = posX + 4.5F + headSize + 3.2F;
        String name = target == mc.player ? NameProtect.getCustomName() : target.getNameForScoreboard();
        if (name.length() > 8) {
            name = name.substring(0, 8);
        }
        ctx.drawText(Fonts.MEDIUM.getFont(8.5F), name, nameX, posY + 6.7F, ColorRGBA.WHITE.withAlpha(animation * 255.0F));

        String hpText = String.format("%.1f", hp).replace(",", ".") + "hp";
        float hpTextWidth = Fonts.REGULAR.getWidth(hpText, 7.0F);
        float dotsWidth = Fonts.BOLD.getWidth("...", 10.5F);
        ctx.drawText(Fonts.REGULAR.getFont(7.0F), hpText, posX + width - hpTextWidth - dotsWidth - 10.0F,
                posY + 7.2f, new ColorRGBA(255, 255, 255, 180.0F * animation));

        float barPadding = 5.0F;
        float barX = posX + barPadding;
        float barY = posY + height - 9.0F;
        float barWidth = width - barPadding * 2.0F;
        float barHeight = 5.0F;

        float barRounding = 0.35F;
        DrawUtil.drawRoundedRect(ctx.getMatrices(), barX, barY, barWidth, barHeight, BorderRadius.all(barRounding), theme.getSecondColor().darker(0.5F).withAlpha(animation * 255.0F), theme.getSecondColor().darker(0.5F).withAlpha(animation * 255.0F), theme.getColor().darker(0.5F).withAlpha(animation * 255.0F), theme.getColor().darker(0.5F).withAlpha(animation * 255.0F));

        DrawUtil.drawRoundedRect(ctx.getMatrices(), barX, barY, MathHelper.clamp(barWidth * this.outdatedHealthAnimation.getValue(), 0.0F, barWidth), barHeight, BorderRadius.all(barRounding), theme.getSecondColor().darker(0.35F).withAlpha(animation * 255.0F), theme.getSecondColor().darker(0.35F).withAlpha(animation * 255.0F), theme.getColor().darker(0.35F).withAlpha(animation * 255.0F), theme.getColor().darker(0.35F).withAlpha(animation * 255.0F));

        if (this.gappleAnimation.getValue() < this.healthAnimation.getValue()) {
            DrawUtil.drawRoundedRect(ctx.getMatrices(), barX, barY, MathHelper.clamp(barWidth * this.healthAnimation.getValue(), 0.0F, barWidth), barHeight, BorderRadius.all(barRounding), theme.getSecondColor().withAlpha(animation * 255.0F), theme.getSecondColor().withAlpha(animation * 255.0F), theme.getColor().withAlpha(animation * 255.0F), theme.getColor().withAlpha(animation * 255.0F));
        }

        DrawUtil.drawRoundedRect(ctx.getMatrices(), barX, barY, MathHelper.clamp(barWidth * this.gappleAnimation.getValue(), 0.0F, barWidth), barHeight, BorderRadius.all(barRounding), new ColorRGBA(255, 209, 0, animation * 255.0F), new ColorRGBA(255, 209, 0, animation * 255.0F), new ColorRGBA(255, 246, 20, animation * 255.0F), new ColorRGBA(255, 246, 20, animation * 255.0F));

        String dots = "...";
        float dotsSize = 10.5F;
        dotsWidth = Fonts.BOLD.getWidth(dots, dotsSize);
        ctx.drawText(Fonts.BOLD.getFont(dotsSize), dots, posX + width - dotsWidth - 5.0F,
                posY + 3.2F, theme.getColor().withAlpha((int) (255 * animation)));

        if (target instanceof PlayerEntity && animation > 0.01F) {
            this.drawArmor(ctx, (PlayerEntity)target, nameX - 1.0F, posY + 16.5F, animation);
        }

        this.width = width;
        this.height = height;
    }

    private void drawArmor(CustomDrawContext ctx, PlayerEntity player, float posX, float posY, float animation) {
        float boxSizeItem = 10.0F;
        float paddingItem = 0.0F;
        float iconX = posX + (5.0F - animation * 5.0F);
        float iconY = posY + 1.0F + (5.0F - animation * 5.0F);
        List<ItemStack> armor = player.getInventory().armor;
        ItemStack[] items = new ItemStack[]{player.getMainHandStack(), player.getOffHandStack(), (ItemStack)armor.get(3), (ItemStack)armor.get(2), (ItemStack)armor.get(1), (ItemStack)armor.get(0)};

        for(ItemStack stack : items) {
            if (!stack.isEmpty()) {
                ctx.getMatrices().push();
                ctx.getMatrices().translate((double)iconX + ((double)boxSizeItem - 9.6D) / 2.0D, (double)iconY + ((double)boxSizeItem - 9.6D) / 2.0D, 0.0D);
                ctx.getMatrices().scale(0.6F * animation, 0.6F * animation, 0.6F * animation);
                ctx.drawItem(stack, 0, 0);
                ((DrawContextAccessor)ctx).callDrawItemBar(stack, 0, 0);
                ((DrawContextAccessor)ctx).callDrawCooldownProgress(stack, 0, 0);
                ctx.getMatrices().pop();
                iconX += boxSizeItem + paddingItem;
            }
        }
    }

    /**
     * O(1) UUID lookup first; fall back to the old name-based tab scan for
     * NPC/disguise servers where the entity UUID differs from the tab-entry UUID.
     */
    private static Identifier resolveSkinTexture(LivingEntity target) {
        PlayerListEntry listEntry = mc.getNetworkHandler().getPlayerListEntry(target.getUuid());
        if (listEntry != null) {
            return listEntry.getSkinTextures().texture();
        }
        String name = target.getNameForScoreboard();
        for (PlayerListEntry entry : mc.getNetworkHandler().getPlayerList()) {
            if (entry.getProfile().getName().equals(name)) {
                return entry.getSkinTextures().texture();
            }
        }
        return DefaultSkinHelper.getSteve().texture();
    }

    public void setTarget(LivingEntity target) {
        if (target == null) {
            this.toggleAnimation.update(0.0F);
            this.toggleAnimationMetanoise.update(0.0F);
            this.toggleAnimationMetanoise.setDuration(2200L);
            this.toggleAnimationMetanoise.setEasing(Easing.CIRC_OUT);
            if (this.toggleAnimationMetanoise.getValue() == 0.0F) {
                this.target = null;
            }
        } else {
            this.target = target;
            this.toggleAnimationMetanoise.update(1.0F);
            this.toggleAnimationMetanoise.setDuration(1300L);
            this.toggleAnimationMetanoise.setEasing(Easing.CIRC_OUT);
            this.toggleAnimation.update(1.0F);
        }
    }

}

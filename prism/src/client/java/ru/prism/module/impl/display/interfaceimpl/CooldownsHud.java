package ru.prism.module.impl.display.interfaceimpl;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import ru.prism.manager.event_impl.EventUpdate;
import ru.prism.module.api.settings.impl.DragSetting;
import ru.prism.module.impl.display.InterFace;
import ru.prism.utils.animation.Animation;
import ru.prism.utils.animation.Easings;
import ru.prism.utils.animation.satoshi.Direction;
import ru.prism.utils.animation.satoshi.EaseInOutQuad;
import ru.prism.utils.colors.ColorUtil;
import ru.prism.utils.other.UseCooldowns;
import ru.prism.utils.render.RenderUtil;
import ru.prism.utils.render.RollingText;
import ru.prism.utils.render.font.Font;
import ru.prism.utils.render.font.Fonts;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CooldownsHud implements element {

    private static final long FORGET_MS = 60000L;

    private static float S = 1.0F;

    private static float H = 16F * S;
    private static float MIN_W = 60F * S;
    private static float RADIUS = 5F * S;
    private static float BASE_H = 4F * S;

    private static float TITLE_TEXT = 7F * S;
    private static float TITLE_ICON = 5F * S;
    private static float ROW_TEXT = 6.5F * S;

    private static float TITLE_ICON_X = 5F * S;
    private static float TITLE_ICON_Y = 5.5F * S;
    private static float TITLE_TEXT_X = 12.5F * S;
    private static float TITLE_TEXT_Y = 3.4F * S;

    private static float ROW_HEIGHT = 14F * S;
    private static float ROW_BASE_W = 22F * S;
    private static float ROW_PADDING_X = 5F * S;
    private static float ROW_START_Y = 5F * S;
    private static float ROW_ANIM_OFFSET = 15F * S;

    private static float HEAD_TEXT_X = 15F * S;
    private static float HEAD_ICON_Y = 0.9F * S;

    private static float SEP_PADDING_X = 4F * S;
    private static float SEP_OFFSET_Y = 3.2F * S;

    private final ru.prism.utils.animation.satoshi.Animation animation1 = new EaseInOutQuad(300, 1);
    private final ru.prism.utils.animation.satoshi.Animation animation2 = new EaseInOutQuad(300, 1);

    private float widthAnim = 0;

    private final Animation headAnimation = new Animation();
    private String head = "";

    private final Map<String, Row> rows = new LinkedHashMap<>();

    private UUID target;
    private String targetName = "";
    private long lastAttack;
    private boolean wasNausea;
    private Object lastWorld;

    private static class Row {
        final RollingText time = new RollingText();
        final Animation animation = new Animation();
        boolean active;
        String label = "";
        int color;
    }

    public void onAttack(Entity entity) {
        if (!(entity instanceof PlayerEntity player) || player == mc.player) return;

        target = player.getUuid();
        targetName = player.getName().getString();
        lastAttack = System.currentTimeMillis();
    }

    public void onTick(EventUpdate event) {
        UseCooldowns.tick(event);

        if (mc.world != lastWorld) {
            lastWorld = mc.world;
            target = null;
            targetName = "";
            lastAttack = 0;
            wasNausea = false;
            UseCooldowns.clear();
        }

        if (mc.player == null || mc.world == null) return;

        boolean nausea = mc.player.hasStatusEffect(StatusEffects.NAUSEA);

        if (nausea && !wasNausea && hasTarget()) {
            PlayerEntity player = getTargetPlayer();
            if (player != null) UseCooldowns.trigger(player, UseCooldowns.Item.NAUSEA);
        }

        wasNausea = nausea;
    }

    private boolean hasTarget() {
        return target != null && System.currentTimeMillis() - lastAttack < FORGET_MS;
    }

    private PlayerEntity getTargetPlayer() {
        return target == null || mc.world == null ? null : mc.world.getPlayerByUuid(target);
    }

    @Override
    public void onRender(DragSetting dragSetting, InterFace interFace) {
        S = InterFace.getInstance().sizeHud.getValue();
        H = 16F * S;
        MIN_W = 60F * S;
        RADIUS = 5F * S;
        BASE_H = 4F * S;
        TITLE_TEXT = 7F * S;
        TITLE_ICON = 5F * S;
        ROW_TEXT = 6.5F * S;
        TITLE_ICON_X = 5F * S;
        TITLE_ICON_Y = 5.5F * S;
        TITLE_TEXT_X = 12.5F * S;
        TITLE_TEXT_Y = 3.4F * S;

        ROW_HEIGHT = 14F * S;
        ROW_BASE_W = 22F * S;
        ROW_PADDING_X = 5F * S;
        ROW_START_Y = 5F * S;
        ROW_ANIM_OFFSET = 15F * S;

        HEAD_TEXT_X = 15F * S;
        HEAD_ICON_Y = 0.9F * S;
        SEP_PADDING_X = 4F * S;
        SEP_OFFSET_Y = 3.2F * S;
        rows.values().forEach(row -> row.active = false);

        boolean hasTarget = hasTarget();

        if (hasTarget) {
            for (UseCooldowns.Item item : UseCooldowns.Item.values()) {
                int left = UseCooldowns.remaining(target, item);
                if (left <= 0) continue;

                Row row = rows.computeIfAbsent(item.name(), k -> new Row());
                row.label = item.label;
                row.color = ColorUtil.getColor(240);
                row.time.set(time(left));
                row.active = true;
            }

            PlayerEntity player = getTargetPlayer();

            if (player != null) {
                for (UseCooldowns.Buff buff : UseCooldowns.Buff.values()) {
                    StatusEffectInstance effect = player.getStatusEffect(buff.effect);
                    if (effect == null) continue;

                    Row row = rows.computeIfAbsent(buff.name(), k -> new Row());
                    row.label = buff.label + roman(effect.getAmplifier() + 1);
                    row.color = buff.color;
                    row.time.set(time(effect.getDuration() / 20));
                    row.active = true;
                }
            }
        }

        String targetName = hasTarget ? this.targetName : "";

        if (!targetName.isEmpty()) head = targetName;

        headAnimation.update();
        headAnimation.run(targetName.isEmpty() ? 0F : 1F, 0.12F, Easings.QUAD_OUT);

        float ha = headAnimation.get();

        boolean isEmpty = ha <= 0.01F && rows.values().stream().noneMatch(row -> row.active);

        float x = dragSetting.position.x;
        float y = dragSetting.position.y;

        boolean closeCondition = isEmpty && !(mc.currentScreen instanceof ChatScreen);

        animation1.setDirection(closeCondition ? Direction.BACKWARDS : Direction.FORWARDS);
        animation2.setDirection((mc.currentScreen instanceof ChatScreen) && isEmpty ? Direction.FORWARDS : Direction.BACKWARDS);

        dragSetting.active = !closeCondition;

        float alpha = animation1.getOutput();

        if (closeCondition && alpha == 0.0F) {
            rows.clear();
            head = "";
            return;
        }

        float alpha2 = animation2.getOutput();

        RenderUtil.Blur.blur(x, y, MIN_W, H, alpha2, RADIUS, ColorUtil.replAlpha(ColorUtil.background(), alpha2 * InterFace.getInstance().alphaHUD.getValue()));

        Font font = Fonts.sf_regular;

        Fonts.prism_2.draw("N", x + TITLE_ICON_X, y + TITLE_ICON_Y, TITLE_ICON, ColorUtil.replAlpha(ColorUtil.client(), alpha2));
        font.draw("Cooldowns", x + TITLE_TEXT_X, y + TITLE_TEXT_Y, TITLE_TEXT, ColorUtil.multAlpha(ColorUtil.getColor(240), alpha2));

        float h = BASE_H;
        float w = 0;

        List<String> toRemove = new ArrayList<>();

        for (Map.Entry<String, Row> entry : rows.entrySet()) {
            Row row = entry.getValue();

            row.animation.update();
            row.animation.run(row.active ? 1F : 0F, 0.12F, Easings.QUAD_OUT);

            float a = row.animation.get();

            if (a <= 0.01F && !row.active) {
                toRemove.add(entry.getKey());
                continue;
            }

            float rowW = ROW_BASE_W + font.getWidth(row.label, ROW_TEXT) + row.time.width(font, ROW_TEXT);

            w = Math.max(w, rowW * a);
            h += ROW_HEIGHT * a;
        }

        toRemove.forEach(rows::remove);

        if (ha > 0.01F) {
            w = Math.max(w, (ROW_BASE_W + font.getWidth(head, ROW_TEXT)) * ha);
            h += ROW_HEIGHT * ha;
        }

        widthAnim += (w - widthAnim) * 0.2F;

        RenderUtil.Blur.blur(x, y, widthAnim, h, alpha, RADIUS, ColorUtil.replAlpha(ColorUtil.background(), alpha * InterFace.getInstance().alphaHUD.getValue()));

        float offsetY = y + ROW_START_Y;
        float offsetY2 = 0;

        if (ha > 0.01F) {
            float addX = 0;

            Fonts.category.draw("Q", x + ROW_PADDING_X - addX, offsetY + HEAD_ICON_Y, ROW_TEXT, ColorUtil.replAlpha(ColorUtil.client(), alpha * ha));
            font.draw(head, x + HEAD_TEXT_X - addX, offsetY, ROW_TEXT, ColorUtil.getColor(240, alpha * ha));

            offsetY += ROW_HEIGHT * ha;
            offsetY2 += ROW_HEIGHT * ha;
        }

        boolean firstRow = ha <= 0.01F;

        for (Row row : rows.values()) {

            float a = row.animation.get();
            if (a <= 0.01F && !row.active) continue;

            float addX = ROW_ANIM_OFFSET - ROW_ANIM_OFFSET * a;

            font.draw(row.label, x + ROW_PADDING_X - addX, offsetY, ROW_TEXT, ColorUtil.replAlpha(row.color, alpha * a));

            row.time.draw(font, x + addX - ROW_PADDING_X + widthAnim - row.time.width(font, ROW_TEXT), offsetY, ROW_TEXT,
                    ColorUtil.getColor(200, alpha * a));

            if (!firstRow) {
                RenderUtil.Render2D.rect(x + SEP_PADDING_X + addX, offsetY - SEP_OFFSET_Y, widthAnim - (SEP_PADDING_X * 2) - addX, 0.5F,
                        ColorUtil.getColor(255, 0.05F * alpha * a), 1);
            }

            firstRow = false;

            offsetY += ROW_HEIGHT * a;
            offsetY2 += ROW_HEIGHT * a;
        }

        dragSetting.size.set(ColorUtil.overCol((int) Math.max(widthAnim, 20 * S), (int) MIN_W, alpha2),
                ColorUtil.overCol((int) offsetY2, (int) H, alpha2));
    }

    private static String time(int seconds) {
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    private static String roman(int level) {
        return switch (level) {
            case 1 -> "";
            case 2 -> " II";
            case 3 -> " III";
            case 4 -> " IV";
            case 5 -> " V";
            default -> " " + level;
        };
    }
}

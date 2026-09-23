package wtf.wyvern.client.ui.interfaces.component;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.client.util.math.Vector2f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.client.modules.impl.misc.NameProtect;
import wtf.wyvern.client.modules.impl.render.Interface;
import wtf.wyvern.client.ui.interfaces.draggable.DraggableHudElement;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.core.theme.Theme;
import wtf.wyvern.render.display.StencilUtil;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.GuiUtil;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Список игроков в прогрузке: голова со скина, ник, надетая броня,
 * дистанция в блоках и стрелка направления.
 */
public class NearComponent extends DraggableHudElement {
    private static final Identifier ARROW_ICON = Wyvern.id("icons/arrow.png");

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    private static final int MAX_ROWS = 8;

    // --- Геометрия: все отступы в одном месте, чтобы список оставался ровным ---
    private static final float PADDING_X = 6.5F;
    private static final float HEADER_HEIGHT = 18.9F;
    private static final float ROW_HEIGHT = 14.0F;
    private static final float BODY_PADDING_Y = 3.0F;
    private static final float HEAD_SIZE = 8.0F;
    private static final float HEAD_TO_NAME_GAP = 4.0F;
    private static final float NAME_TO_ARMOR_GAP = 8.0F;
    private static final float ARMOR_ICON_SIZE = 9.0F;
    private static final float ARMOR_ICON_GAP = 1.0F;
    private static final float ARMOR_TO_DISTANCE_GAP = 5.0F;
    private static final float DISTANCE_TO_ARROW_GAP = 4.0F;
    private static final float ARROW_SIZE = 5.5F;
    private static final float NAME_SIZE = 7.2F;
    private static final float DISTANCE_SIZE = 6.8F;
    private static final float CORNER_RADIUS = 5.5F;
    private static final float STROKE = 1.0F;
    private static final float MIN_WIDTH = 118.0F;
    private static final float MAX_NAME_WIDTH = 62.0F;

    // --- Контекстное меню размера (ПКМ по элементу в режиме чата) ---
    private static final float MENU_WIDTH = 96.0F;
    private static final float MENU_PADDING = 6.0F;
    private static final float MENU_TRACK_HEIGHT = 3.0F;
    private static final float MENU_KNOB_SIZE = 5.5F;
    private static final float MENU_HEIGHT = MENU_PADDING + 10.0F + MENU_TRACK_HEIGHT + 4.0F + 6.0F + MENU_PADDING;

    /** Размер именно этого элемента в процентах; сохраняется вместе с позицией. */
    private final SliderSetting scaleSetting = new SliderSetting("Размер", 100.0F, 60.0F, 160.0F, 5.0F);

    private final Animation widthAnimation = new Animation(200L, Easing.CUBIC_OUT);
    private final Animation alphaAnimation = new Animation(200L, Easing.CUBIC_OUT);
    private final Animation menuAnimation = new Animation(180L, Easing.CUBIC_OUT);
    private final Map<Integer, Float> arrowAngles = new HashMap<>();
    private final List<NearPlayer> rows = new ArrayList<>();

    private boolean menuOpen;
    private boolean draggingSlider;
    private float menuX;
    private float menuY;

    public NearComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight,
                         float offsetX, float offsetY, Align align) {
        super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
    }

    @Override
    public void render(CustomDrawContext ctx) {
        boolean editing = mc.currentScreen instanceof ChatScreen;
        if (!editing) {
            this.menuOpen = false;
            this.draggingSlider = false;
        }
        collectPlayers(editing);

        float scale = this.scaleSetting.getCurrent() / 100.0F;

        this.alphaAnimation.update(rows.isEmpty() && !editing ? 0.0F : 1.0F);
        float alpha = this.alphaAnimation.getValue();
        if (alpha < 0.01F) {
            this.width = Math.max(this.width, MIN_WIDTH * scale);
            this.height = HEADER_HEIGHT * scale;
            return;
        }

        float posX = this.getX();
        float posY = this.getY();
        Theme theme = Wyvern.getInstance().getThemeManager().getCurrentTheme();
        ColorRGBA themeColor = theme.getColor();

        // --- Ширина по самой длинной строке ---
        float contentWidth = MIN_WIDTH;
        for (NearPlayer row : rows) {
            float rowWidth = PADDING_X + HEAD_SIZE + HEAD_TO_NAME_GAP
                    + Math.min(Fonts.MEDIUM.getWidth(row.name, NAME_SIZE), MAX_NAME_WIDTH)
                    + NAME_TO_ARMOR_GAP + armorWidth(row)
                    + ARMOR_TO_DISTANCE_GAP + Fonts.MEDIUM.getWidth(row.distanceText, DISTANCE_SIZE)
                    + DISTANCE_TO_ARROW_GAP + ARROW_SIZE + PADDING_X;
            contentWidth = Math.max(contentWidth, rowWidth);
        }
        this.widthAnimation.update(contentWidth);
        float animWidth = this.widthAnimation.getValue();

        float bodyHeight = rows.isEmpty()
                ? ROW_HEIGHT + BODY_PADDING_Y * 2.0F
                : rows.size() * ROW_HEIGHT + BODY_PADDING_Y * 2.0F;
        float totalHeight = HEADER_HEIGHT + bodyHeight;
        int alphaInt = (int)(255 * alpha);

        // Весь список рисуется в собственном масштабе: якорь — левый верхний
        // угол, поэтому позиция при ресайзе не уезжает.
        MatrixStack scaleMatrices = ctx.getMatrices();
        scaleMatrices.push();
        scaleMatrices.translate(posX, posY, 0.0F);
        scaleMatrices.scale(scale, scale, 1.0F);
        scaleMatrices.translate(-posX, -posY, 0.0F);

        // --- Подложка: обводка + шапка + тело ---
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX, posY, animWidth, totalHeight,
                BorderRadius.all(CORNER_RADIUS), new ColorRGBA(23, 23, 23, alphaInt));

        StencilUtil.push();
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX + STROKE, posY + STROKE,
                animWidth - STROKE * 2.0F, totalHeight - STROKE * 2.0F,
                BorderRadius.all(CORNER_RADIUS - STROKE), ColorRGBA.BLACK);
        StencilUtil.read(1);
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX + STROKE, posY + STROKE,
                animWidth - STROKE * 2.0F, HEADER_HEIGHT - STROKE,
                new BorderRadius(CORNER_RADIUS - STROKE, CORNER_RADIUS - STROKE, 0.0F, 0.0F),
                new ColorRGBA(11, 11, 11, alphaInt));
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX + STROKE, posY + HEADER_HEIGHT,
                animWidth - STROKE * 2.0F, bodyHeight - STROKE,
                new BorderRadius(0.0F, 0.0F, CORNER_RADIUS - STROKE, CORNER_RADIUS - STROKE),
                new ColorRGBA(17, 17, 17, alphaInt));
        StencilUtil.pop();

        drawHeader(ctx, posX, posY, animWidth, themeColor, alpha);

        float rowY = posY + HEADER_HEIGHT + BODY_PADDING_Y;
        for (NearPlayer row : rows) {
            drawRow(ctx, row, posX, rowY, animWidth, themeColor, alpha);
            rowY += ROW_HEIGHT;
        }

        scaleMatrices.pop();

        this.width = animWidth * scale;
        this.height = totalHeight * scale;

        renderMenu(ctx, themeColor);
    }

    // ------------------------------------------------------------------
    //  Меню размера
    // ------------------------------------------------------------------

    private void renderMenu(CustomDrawContext ctx, ColorRGBA themeColor) {
        this.menuAnimation.update(this.menuOpen ? 1.0F : 0.0F);
        float anim = this.menuAnimation.getValue();
        if (anim < 0.01F) {
            return;
        }

        if (this.draggingSlider) {
            Vector2f mouse = GuiUtil.getMouse(Interface.INSTANCE.getCustomScale());
            applySliderValue(mouse.getX());
        }

        int alphaInt = (int)(255 * anim);
        float x = this.menuX;
        float y = this.menuY;

        DrawUtil.drawRoundedRect(ctx.getMatrices(), x, y, MENU_WIDTH, MENU_HEIGHT,
                BorderRadius.all(CORNER_RADIUS), new ColorRGBA(23, 23, 23, alphaInt));
        DrawUtil.drawRoundedRect(ctx.getMatrices(), x + STROKE, y + STROKE,
                MENU_WIDTH - STROKE * 2.0F, MENU_HEIGHT - STROKE * 2.0F,
                BorderRadius.all(CORNER_RADIUS - STROKE), new ColorRGBA(11, 11, 11, alphaInt));

        float value = this.scaleSetting.getCurrent();
        String valueText = (int)value + "%";
        float valueWidth = Fonts.MEDIUM.getWidth(valueText, 7.0F);
        ctx.drawText(Fonts.MEDIUM.getFont(7.0F), "Размер", x + MENU_PADDING, y + MENU_PADDING,
                ColorRGBA.WHITE.withAlpha((int)(230 * anim)));
        ctx.drawText(Fonts.MEDIUM.getFont(7.0F), valueText, x + MENU_WIDTH - MENU_PADDING - valueWidth,
                y + MENU_PADDING, themeColor.withAlpha(alphaInt));

        float trackX = x + MENU_PADDING;
        float trackY = y + MENU_PADDING + 10.0F;
        float trackWidth = MENU_WIDTH - MENU_PADDING * 2.0F;
        float progress = (value - this.scaleSetting.getMin())
                / (this.scaleSetting.getMax() - this.scaleSetting.getMin());

        DrawUtil.drawRoundedRect(ctx.getMatrices(), trackX, trackY, trackWidth, MENU_TRACK_HEIGHT,
                BorderRadius.all(MENU_TRACK_HEIGHT / 2.0F), new ColorRGBA(42, 42, 42, alphaInt));
        DrawUtil.drawRoundedRect(ctx.getMatrices(), trackX, trackY, trackWidth * progress, MENU_TRACK_HEIGHT,
                BorderRadius.all(MENU_TRACK_HEIGHT / 2.0F), themeColor.withAlpha(alphaInt));
        DrawUtil.drawRoundedRect(ctx.getMatrices(),
                trackX + trackWidth * progress - MENU_KNOB_SIZE / 2.0F,
                trackY + MENU_TRACK_HEIGHT / 2.0F - MENU_KNOB_SIZE / 2.0F,
                MENU_KNOB_SIZE, MENU_KNOB_SIZE, BorderRadius.all(MENU_KNOB_SIZE / 2.0F),
                ColorRGBA.WHITE.withAlpha(alphaInt));

        float hintY = trackY + MENU_TRACK_HEIGHT + 4.0F;
        ColorRGBA hintColor = new ColorRGBA(120, 120, 120, alphaInt);
        String maxText = (int)this.scaleSetting.getMax() + "%";
        ctx.drawText(Fonts.MEDIUM.getFont(5.8F), (int)this.scaleSetting.getMin() + "%", trackX, hintY, hintColor);
        ctx.drawText(Fonts.MEDIUM.getFont(5.8F), maxText,
                trackX + trackWidth - Fonts.MEDIUM.getWidth(maxText, 5.8F), hintY, hintColor);
    }

    private void applySliderValue(float mouseX) {
        float trackX = this.menuX + MENU_PADDING;
        float trackWidth = MENU_WIDTH - MENU_PADDING * 2.0F;
        float min = this.scaleSetting.getMin();
        float max = this.scaleSetting.getMax();
        float progress = MathHelper.clamp((mouseX - trackX) / trackWidth, 0.0F, 1.0F);
        float increment = this.scaleSetting.getIncrement();
        float raw = min + (max - min) * progress;
        this.scaleSetting.setCurrent(MathHelper.clamp(Math.round(raw / increment) * increment, min, max));
    }

    /** ПКМ по элементу: открыть/закрыть меню у курсора. */
    public void toggleMenu(double mouseX, double mouseY) {
        if (this.menuOpen) {
            closeMenu();
            return;
        }
        this.menuOpen = true;
        this.menuX = (float)mouseX;
        this.menuY = (float)mouseY;
    }

    public void closeMenu() {
        this.menuOpen = false;
        this.draggingSlider = false;
    }

    public boolean isMenuOpen() {
        return this.menuOpen;
    }

    public boolean isMouseOverMenu(double mouseX, double mouseY) {
        return this.menuOpen
                && mouseX >= this.menuX && mouseX <= this.menuX + MENU_WIDTH
                && mouseY >= this.menuY && mouseY <= this.menuY + MENU_HEIGHT;
    }

    /** Возвращает true, если клик забрало меню и его не надо превращать в перетаскивание. */
    public boolean onMouseClicked(double mouseX, double mouseY) {
        if (!isMouseOverMenu(mouseX, mouseY)) {
            return false;
        }

        float trackY = this.menuY + MENU_PADDING + 10.0F;
        if (mouseY >= trackY - 4.0F && mouseY <= trackY + MENU_TRACK_HEIGHT + 4.0F) {
            this.draggingSlider = true;
            applySliderValue((float)mouseX);
        }
        return true;
    }

    public void onMouseReleased() {
        this.draggingSlider = false;
    }

    private void drawHeader(CustomDrawContext ctx, float posX, float posY, float animWidth,
                            ColorRGBA themeColor, float alpha) {
        int alphaInt = (int)(255 * alpha);
        float iconX = posX + PADDING_X;
        ctx.drawText(Fonts.WYVERN.getFont(10.0F), "F", iconX, posY + 6.0F, themeColor.withAlpha(alphaInt));

        float slashX = iconX + Fonts.WYVERN.getWidth("F", 10.0F) + 1.5F;
        ctx.drawText(Fonts.BOLD.getFont(7.5F), "/", slashX, posY + 6.6F,
                ColorRGBA.WHITE.withAlpha((int)(110 * alpha)));

        float titleX = slashX + Fonts.BOLD.getWidth("/", 7.5F) + 3.0F;
        float titleWidth = Fonts.MEDIUM.getWidth("Near", 7.5F);
        ctx.drawTexture(Wyvern.id("icons/glow.png"), titleX + titleWidth / 2.0F - 30.0F,
                posY + 7.0F + 7.5F / 2.0F - 17.5F, 60.0F, 35.0F,
                ColorRGBA.WHITE.withAlpha((int)(18 * alpha)));
        ctx.drawText(Fonts.MEDIUM.getFont(7.5F), "Near", titleX, posY + 7.0F,
                ColorRGBA.WHITE.withAlpha((int)(230 * alpha)));

        String counter = String.valueOf(rows.size());
        float counterWidth = Fonts.MEDIUM.getWidth(counter, 7.5F);
        ctx.drawText(Fonts.MEDIUM.getFont(7.5F), counter, posX + animWidth - counterWidth - PADDING_X,
                posY + 7.0F, themeColor.withAlpha(alphaInt));
    }

    private void drawRow(CustomDrawContext ctx, NearPlayer row, float posX, float rowY, float animWidth,
                         ColorRGBA themeColor, float alpha) {
        int alphaInt = (int)(255 * alpha);
        ColorRGBA whiteColor = ColorRGBA.WHITE.withAlpha(alphaInt);

        // Голова
        float headY = rowY + (ROW_HEIGHT - HEAD_SIZE) / 2.0F;
        DrawUtil.drawPlayerHeadWithRoundedShader(ctx.getMatrices(), row.skin,
                posX + PADDING_X, headY, HEAD_SIZE, BorderRadius.all(1.0F), whiteColor);

        // Стрелка направления — крайняя справа
        float arrowCenterX = posX + animWidth - PADDING_X - ARROW_SIZE / 2.0F;
        float arrowCenterY = rowY + ROW_HEIGHT / 2.0F;
        drawArrow(ctx, row, arrowCenterX, arrowCenterY, themeColor.withAlpha(alphaInt));

        // Дистанция — слева от стрелки
        float distanceWidth = Fonts.MEDIUM.getWidth(row.distanceText, DISTANCE_SIZE);
        float distanceX = arrowCenterX - ARROW_SIZE / 2.0F - DISTANCE_TO_ARROW_GAP - distanceWidth;
        ctx.drawText(Fonts.MEDIUM.getFont(DISTANCE_SIZE), row.distanceText, distanceX,
                rowY + (ROW_HEIGHT - Fonts.MEDIUM.getFont(DISTANCE_SIZE).height()) / 2.0F,
                new ColorRGBA(150, 150, 150, alphaInt));

        // Броня — прижата к дистанции справа налево
        float armorRight = distanceX - ARMOR_TO_DISTANCE_GAP;
        float armorX = armorRight - armorWidth(row);
        float armorY = rowY + (ROW_HEIGHT - ARMOR_ICON_SIZE) / 2.0F;
        MatrixStack matrices = ctx.getMatrices();
        float itemScale = ARMOR_ICON_SIZE / 16.0F;
        for (ItemStack stack : row.equipment) {
            matrices.push();
            matrices.translate(armorX, armorY, 0.0F);
            matrices.scale(itemScale, itemScale, 1.0F);
            ctx.drawItem(stack, 0, 0);
            matrices.pop();
            armorX += ARMOR_ICON_SIZE + ARMOR_ICON_GAP;
        }

        // Ник — между головой и бронёй, обрезается, чтобы не наехать
        float nameX = posX + PADDING_X + HEAD_SIZE + HEAD_TO_NAME_GAP;
        float nameLimit = Math.max(0.0F, armorRight - armorWidth(row) - NAME_TO_ARMOR_GAP - nameX);
        String name = fitText(row.name, Math.min(nameLimit, MAX_NAME_WIDTH));
        ctx.drawText(Fonts.MEDIUM.getFont(NAME_SIZE), name, nameX,
                rowY + (ROW_HEIGHT - Fonts.MEDIUM.getFont(NAME_SIZE).height()) / 2.0F,
                row.friend ? new ColorRGBA(90, 235, 120, alphaInt) : whiteColor);
    }

    private void drawArrow(CustomDrawContext ctx, NearPlayer row, float centerX, float centerY, ColorRGBA color) {
        float current = this.arrowAngles.getOrDefault(row.id, row.angle);
        current += MathHelper.wrapDegrees(row.angle - current) * 0.35F;
        this.arrowAngles.put(row.id, current);

        MatrixStack matrices = ctx.getMatrices();
        matrices.push();
        matrices.translate(centerX, centerY, 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(current));
        DrawUtil.drawTexture(matrices, ARROW_ICON, -ARROW_SIZE / 2.0F, -ARROW_SIZE / 2.0F,
                ARROW_SIZE, ARROW_SIZE, color);
        matrices.pop();
    }

    private void collectPlayers(boolean editing) {
        this.rows.clear();
        if (mc.world == null || mc.player == null) {
            return;
        }

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || !player.isAlive() || player.isRemoved()) {
                continue;
            }
            this.rows.add(createRow(player));
        }

        this.rows.sort((left, right) -> Float.compare(left.distance, right.distance));
        if (this.rows.size() > MAX_ROWS) {
            this.rows.subList(MAX_ROWS, this.rows.size()).clear();
        }

        // В режиме перетаскивания показываем себя, чтобы элемент было видно
        // даже когда рядом никого нет.
        if (this.rows.isEmpty() && editing) {
            this.rows.add(createRow(mc.player));
        }

        this.arrowAngles.keySet().removeIf(id -> {
            for (NearPlayer row : this.rows) {
                if (row.id == id) {
                    return false;
                }
            }
            return true;
        });
    }

    private NearPlayer createRow(PlayerEntity player) {
        List<ItemStack> equipment = new ArrayList<>(5);
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = player.getEquippedStack(slot);
            if (!stack.isEmpty()) {
                equipment.add(stack);
            }
        }
        ItemStack mainHand = player.getMainHandStack();
        if (!mainHand.isEmpty()) {
            equipment.add(mainHand);
        }

        float distance = mc.player.distanceTo(player);
        double diffX = player.getX() - mc.player.getX();
        double diffZ = player.getZ() - mc.player.getZ();
        float angle = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - mc.player.getYaw() - 90.0D);

        String name = player == mc.player && NameProtect.INSTANCE.isEnabled()
                ? NameProtect.getCustomName()
                : player.getNameForScoreboard();

        return new NearPlayer(
                player.getId(),
                name,
                player instanceof AbstractClientPlayerEntity clientPlayer
                        ? clientPlayer.getSkinTextures().texture()
                        : DefaultSkinHelper.getSteve().texture(),
                equipment,
                distance,
                (int)distance + "m",
                angle,
                Wyvern.getInstance().getFriendManager().isFriend(player.getNameForScoreboard())
        );
    }

    private float armorWidth(NearPlayer row) {
        if (row.equipment.isEmpty()) {
            return 0.0F;
        }
        return row.equipment.size() * ARMOR_ICON_SIZE + (row.equipment.size() - 1) * ARMOR_ICON_GAP;
    }

    private String fitText(String text, float maxWidth) {
        if (maxWidth <= 0.0F) {
            return "";
        }
        if (Fonts.MEDIUM.getWidth(text, NAME_SIZE) <= maxWidth) {
            return text;
        }

        String dots = "..";
        float dotsWidth = Fonts.MEDIUM.getWidth(dots, NAME_SIZE);
        if (dotsWidth > maxWidth) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        float width = 0.0F;
        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));
            float characterWidth = Fonts.MEDIUM.getWidth(character, NAME_SIZE);
            if (width + characterWidth + dotsWidth > maxWidth) {
                break;
            }
            result.append(character);
            width += characterWidth;
        }
        return result + dots;
    }

    private static final class NearPlayer {
        final int id;
        final String name;
        final Identifier skin;
        final List<ItemStack> equipment;
        final float distance;
        final String distanceText;
        final float angle;
        final boolean friend;

        NearPlayer(int id, String name, Identifier skin, List<ItemStack> equipment,
                   float distance, String distanceText, float angle, boolean friend) {
            this.id = id;
            this.name = name;
            this.skin = skin;
            this.equipment = equipment;
            this.distance = distance;
            this.distanceText = distanceText;
            this.angle = angle;
            this.friend = friend;
        }
    }
}

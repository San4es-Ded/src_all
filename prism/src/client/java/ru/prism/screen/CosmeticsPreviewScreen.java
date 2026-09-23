package ru.prism.screen;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import ru.prism.cosmetic.loader.CosmeticLoader;
import ru.prism.cosmetic.model.CosmeticModel;
import ru.prism.cosmetic.render.CosmeticPreviewRenderer;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.impl.render.Cosmetics;
import ru.prism.utils.animation.Animation;
import ru.prism.utils.animation.Easings;
import ru.prism.utils.animation.satoshi.Direction;
import ru.prism.utils.animation.satoshi.EaseInOutQuad;
import ru.prism.utils.annotation.IMinecraft;
import ru.prism.utils.colors.ColorUtil;
import ru.prism.utils.math.MathUtil;
import ru.prism.utils.other.GuiSounds;
import ru.prism.utils.render.DrawBatcher;
import ru.prism.utils.render.Render2D;
import ru.prism.utils.render.RenderUtil;
import ru.prism.utils.render.Scissor;
import ru.prism.utils.render.ScreenBlur;
import ru.prism.utils.render.font.Font;
import ru.prism.utils.render.font.Fonts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Большое окно визуализации косметики: тёмный заблюренный фон, счётчик выбранного
 * и скроллируемая сетка карточек с превью. Клик по карточке переключает
 * соответствующий тумблер в {@link Cosmetics}.
 */
public final class CosmeticsPreviewScreen extends Screen implements IMinecraft {

    private static final float GAP = 6F;
    private static final float PAD = 12F;
    private static final float HEADER_H = 34F;
    private static final float MIN_CARD_W = 54F;
    private static final float MAX_CARD_W = 84F;

    private final Screen parent;

    private final Animation openAnim = new Animation();
    private final Map<String, ru.prism.utils.animation.satoshi.Animation> hoverAnims = new HashMap<>();
    private final Map<String, ru.prism.utils.animation.satoshi.Animation> selectAnims = new HashMap<>();
    private final List<Card> cards = new ArrayList<>();
    private final CosmeticPreviewRenderer previewRenderer = new CosmeticPreviewRenderer();
    private final MatrixStack previewMatrices = new MatrixStack();
    private final float[] spinAngles = new float[Cosmetics.ITEM_NAMES.length];

    private float scaleFix = 1F;
    private float mouseX;
    private float mouseY;

    private float scroll;
    private float scrollTarget;
    private float maxScroll;

    public CosmeticsPreviewScreen(Screen parent) {
        super(Text.literal("Косметика"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        openAnim.set(0);
        openAnim.run(1, 0.25F, Easings.SINE_OUT);
        if (mc.getResourceManager() != null) {
            CosmeticLoader.getInstance().loadAll(mc.getResourceManager(), Cosmetics.ITEM_NAMES.length);
        }
    }

    @Override
    public void removed() {
        previewRenderer.close();
        super.removed();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    public void render(DrawContext context, int rawMouseX, int rawMouseY, float delta) {
        float currentScale = (float) mc.getWindow().getScaleFactor();
        scaleFix = currentScale <= 0F ? 1F : 2F / currentScale;

        float screenW = mc.getWindow().getScaledWidth() / scaleFix;
        float screenH = mc.getWindow().getScaledHeight() / scaleFix;

        mouseX = rawMouseX / scaleFix;
        mouseY = rawMouseY / scaleFix;

        openAnim.update();
        float alpha = openAnim.get();
        scroll += (scrollTarget - scroll) * 0.2F;

        Render2D.beginOverlay();

        ScreenBlur.capture(2);
        RenderUtil.Blur.blur(0, 0, screenW, screenH, alpha, ColorUtil.getColor(0, alpha * 0.2F));
        RenderUtil.Render2D.rect(0, 0, screenW, screenH, ColorUtil.getColor(0, 0.45F * alpha));

        Font font = Fonts.sf_regular;

        float panelW = Math.min(screenW - 32F, 900F);
        float panelH = Math.min(screenH - 32F, 620F);
        float panelX = screenW / 2F - panelW / 2F;
        float panelY = screenH / 2F - panelH / 2F + (1F - alpha) * 24F;

        RenderUtil.Blur.blur(panelX, panelY, panelW, panelH, alpha, 8,
                ColorUtil.multAlpha(ColorUtil.multDark(ColorUtil.background(), 0.6F), alpha));
        RenderUtil.Render2D.outline(panelX, panelY, panelW, panelH, 0.5F,
                ColorUtil.replAlpha(ColorUtil.client(), alpha * 0.35F), 8);

        Cosmetics cosmetics = Cosmetics.getInstance();
        int total = Cosmetics.ITEM_NAMES.length;
        int selected = 0;
        if (cosmetics != null) {
            for (BooleanSetting b : cosmetics.getItems().getValues()) {
                if (b.getValue()) selected++;
            }
        }

        font.draw("Косметика", panelX + PAD, panelY + 9F, 9F, ColorUtil.getColor(255, alpha));
        String counter = selected + " / " + total;
        font.draw(counter, panelX + panelW - PAD - font.getWidth(counter, 8F), panelY + 10F, 8F,
                ColorUtil.replAlpha(ColorUtil.client(), alpha));
        font.draw("ЛКМ — переключить   •   ESC — закрыть", panelX + PAD, panelY + 24F, 5.5F,
                ColorUtil.getColor(255, alpha * 0.35F));

        float gridX = panelX + PAD;
        float gridY = panelY + HEADER_H;
        float gridW = panelW - PAD * 2F;
        float gridH = panelH - HEADER_H - PAD;

        int columns = Math.max(3, Math.min(10, (int) ((gridW + GAP) / (MIN_CARD_W + GAP))));
        float cardW = MathUtil.clamp((gridW - (columns - 1) * GAP) / columns, MIN_CARD_W, MAX_CARD_W);
        float cardH = cardW + 14F;

        float usedW = columns * cardW + (columns - 1) * GAP;
        gridX += (gridW - usedW) / 2F;
        gridW = usedW;

        int rows = (total + columns - 1) / columns;
        float contentH = rows * cardH + Math.max(0, rows - 1) * GAP;
        maxScroll = Math.max(0F, contentH - gridH);
        scrollTarget = MathUtil.clamp(scrollTarget, 0F, maxScroll);

        cards.clear();
        Scissor.enable(gridX, gridY, gridW, gridH, 2);

        for (int i = 0; i < total; i++) {
            int col = i % columns;
            int row = i / columns;

            float cx = gridX + col * (cardW + GAP);
            float cy = gridY + row * (cardH + GAP) - scroll;

            if (cy + cardH < gridY || cy > gridY + gridH) {
                continue;
            }

            BooleanSetting setting = cosmetics == null ? null : cosmetics.getItems().get(Cosmetics.ITEM_NAMES[i]);
            boolean isSelected = setting != null && setting.getValue();
            boolean hovered = inside(mouseX, mouseY, cx, cy, cardW, cardH);

            ru.prism.utils.animation.satoshi.Animation hoverAnim = hoverAnims.computeIfAbsent("h:" + i, k -> new EaseInOutQuad(300, 1));
            hoverAnim.setDirection(hovered ? Direction.FORWARDS : Direction.BACKWARDS);
            float hov = hoverAnim.getOutput();

            ru.prism.utils.animation.satoshi.Animation selectAnim = selectAnims.computeIfAbsent("s:" + i, k -> new EaseInOutQuad(300, 1));
            selectAnim.setDirection(isSelected ? Direction.FORWARDS : Direction.BACKWARDS);
            float sel = selectAnim.getOutput();

            float accent = Math.max(sel, hov);

            RenderUtil.Render2D.rect(cx, cy, cardW, cardH, ColorUtil.overCol(
                    ColorUtil.getColor(0, 0.25F * alpha),
                    ColorUtil.replAlpha(ColorUtil.client(), 0.18F * alpha), accent), 6F);
            RenderUtil.Render2D.outline(cx, cy, cardW, cardH, 0.5F,
                    ColorUtil.replAlpha(ColorUtil.client(), alpha * accent), 6F);

            float imageSize = cardW - 8F;
            float contentAlpha = alpha * (0.55F + 0.45F * accent);
            boolean volumetric = false;

            CosmeticModel cosmetic = CosmeticLoader.getInstance().getCosmetic(i);
            if (cosmetic != null) {
                spinAngles[i] = (spinAngles[i] + delta * (3F + 7F * hov)) % 360F;
                volumetric = previewRenderer.render(cosmetic, previewMatrices,
                        cx + 4F + imageSize / 2F, cy + 4F + imageSize / 2F,
                        imageSize, spinAngles[i], contentAlpha);
            } else if (i < 14) {
                spinAngles[i] = (spinAngles[i] + delta * (3F + 7F * hov)) % 360F;
                volumetric = previewRenderer.renderCape(i, previewMatrices,
                        cx + 4F + imageSize / 2F, cy + 4F + imageSize / 2F,
                        imageSize, spinAngles[i], contentAlpha);
            }

            if (!volumetric) {
                RenderUtil.Images.texture(
                        Identifier.of("client", "textures/cosmetics/cosmetic_" + i + ".png"),
                        cx + 4F, cy + 4F, imageSize, imageSize,
                        ColorUtil.getColor(255, contentAlpha));
            }

            cards.add(new Card(i, cx, cy, cardW, cardH));
        }

        DrawBatcher.flushPending();
        previewRenderer.flush();

        for (Card card : cards) {
            int i = card.index;
            ru.prism.utils.animation.satoshi.Animation hoverAnim = hoverAnims.computeIfAbsent("h:" + i, k -> new EaseInOutQuad(300, 1));
            ru.prism.utils.animation.satoshi.Animation selectAnim = selectAnims.computeIfAbsent("s:" + i, k -> new EaseInOutQuad(300, 1));
            float accent = Math.max(selectAnim.getOutput(), hoverAnim.getOutput());

            String label = i + ". " + Cosmetics.ITEM_NAMES[i];
            font.drawFadingText(label, card.x + 4F, card.y + card.h - 12F, card.w - 8F,
                    ColorUtil.getColor(255, alpha * (0.55F + 0.45F * accent)), 5.5F);
        }

        Scissor.disable();

        Render2D.endOverlay();
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (click.button() != 0) {
            return super.mouseClicked(click, doubled);
        }

        float mx = (float) (click.x() / scaleFix);
        float my = (float) (click.y() / scaleFix);

        Cosmetics cosmetics = Cosmetics.getInstance();
        if (cosmetics == null) {
            return true;
        }

        for (Card card : cards) {
            if (!inside(mx, my, card.x, card.y, card.w, card.h)) {
                continue;
            }

            BooleanSetting setting = cosmetics.getItems().get(Cosmetics.ITEM_NAMES[card.index]);
            if (setting != null) {
                setting.set(!setting.getValue());
                GuiSounds.chipMulti(setting.getValue());
            }
            return true;
        }

        return true;
    }

    @Override
    public boolean mouseScrolled(double rawMouseX, double rawMouseY, double horizontal, double vertical) {
        scrollTarget = MathUtil.clamp((float) (scrollTarget - vertical * 24F), 0F, maxScroll);
        return true;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.key() == GLFW.GLFW_KEY_ESCAPE) {
            close();
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static boolean inside(float mouseX, float mouseY, float x, float y, float w, float h) {
        return MathUtil.isHovered(mouseX, mouseY, x, y, w, h);
    }

    private record Card(int index, float x, float y, float w, float h) {
    }
}

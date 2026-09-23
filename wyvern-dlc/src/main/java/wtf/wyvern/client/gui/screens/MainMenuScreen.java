package wtf.wyvern.client.gui.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;

/**
 * Reference-styled main menu: huge translucent wordmark, dark smoky shader
 * backdrop, centered dark buttons with left icons and an accent hover border,
 * greeting line and a theme-driven accent.
 */
public final class MainMenuScreen extends Screen {
    private static final float DESIGN_WIDTH = 884.0F;
    private static final float DESIGN_HEIGHT = 595.0F;
    private static final float BUTTON_WIDTH = 158.0F;
    private static final float BUTTON_HEIGHT = 26.0F;
    private static final float BUTTON_GAP = 7.0F;

    private static final ColorRGBA WHITE = new ColorRGBA(238, 239, 244, 248);
    private static final ColorRGBA DEFAULT_ACCENT = new ColorRGBA(157, 168, 237, 242);
    // Buttons are a plain neutral-dark frosted blur: the blur shader multiplies the
    // blurred backdrop by this tint, so a grey tint = darkened colorless glass.
    private static final ColorRGBA PANEL_TINT = new ColorRGBA(84, 86, 92, 235);
    private static final ColorRGBA PANEL_HOVER_TINT = new ColorRGBA(126, 128, 136, 245);

    /** Main buttons: label + iconpack1 glyph. Bottom row has no icons like the reference. */
    private static final String[] MAIN_LABELS = {"Singleplayer", "Multiplayer", "Accounts"};
    // f = lone player silhouette, G = group of players, o = person with ID card.
    private static final String[] MAIN_ICONS = {"f", "G", "o"};

    private final wtf.wyvern.core.animations.base.Animation viewAnim =
            new wtf.wyvern.core.animations.base.Animation(260L, wtf.wyvern.core.animations.base.Easing.CUBIC_OUT);
    private final java.util.Map<String, wtf.wyvern.core.animations.base.Animation> hoverAnims = new java.util.HashMap<>();
    private Screen pendingScreen;
    private boolean closing;

    public MainMenuScreen() {
        super(Text.literal("Wyvern"));
    }

    @Override
    protected void init() {
        // Re-entering the menu (back from Singleplayer/Accounts) replays the entrance.
        this.closing = false;
        this.pendingScreen = null;
    }

    /** Fades everything with the entrance/exit animation. */
    private ColorRGBA fade(ColorRGBA color) {
        float view = this.viewAnim.getValue();
        return view >= 0.999F ? color : color.withAlpha((int) (color.getAlpha() * view));
    }

    /** Smoothly animated 0..1 hover value per element key. */
    private float hoverValue(String key, boolean hovered) {
        wtf.wyvern.core.animations.base.Animation anim = this.hoverAnims.computeIfAbsent(key,
                k -> new wtf.wyvern.core.animations.base.Animation(160L, wtf.wyvern.core.animations.base.Easing.CUBIC_OUT));
        anim.update(hovered);
        return anim.getValue();
    }

    /** Plays the exit animation, then opens the target screen from render(). */
    private void beginTransition(Screen screen) {
        if (!this.closing) {
            this.pendingScreen = screen;
            this.closing = true;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        CustomDrawContext draw = CustomDrawContext.of(context);
        float scale = layoutScale();
        renderBackdrop(draw);

        this.viewAnim.update(!this.closing);
        float view = this.viewAnim.getValue();
        if (this.closing && view <= 0.02F) {
            Screen next = this.pendingScreen;
            this.pendingScreen = null;
            if (next != null) {
                open(next);
            } else {
                this.closing = false;
            }
            return;
        }
        draw.getMatrices().push();
        draw.getMatrices().translate(0.0F, (1.0F - view) * 14.0F * scale, 0.0F);

        float centerX = this.width * 0.5F;
        drawLogo(draw, centerX, this.height * 0.5F - 122.0F * scale, 46.0F * scale);
        float buttonX = centerX - BUTTON_WIDTH * scale * 0.5F;
        float firstY = firstButtonY(scale);
        float step = (BUTTON_HEIGHT + BUTTON_GAP) * scale;

        // Greeting: "Hello, <name>" with the name in accent.
        String hello = "Hello, ";
        String name = this.client != null ? this.client.getSession().getUsername() : "Player";
        float greetSize = 8.6F * scale;
        float helloW = Fonts.MEDIUM.getWidth(hello, greetSize);
        float nameW = Fonts.MEDIUM.getWidth(name, greetSize);
        float greetX = centerX - (helloW + nameW) * 0.5F;
        float greetY = firstY - 16.5F * scale;
        draw.drawText(Fonts.MEDIUM.getFont(greetSize), hello, greetX, greetY, fade(WHITE));
        draw.drawText(Fonts.MEDIUM.getFont(greetSize), name, greetX + helloW, greetY, fade(accent()));

        for (int i = 0; i < MAIN_LABELS.length; i++) {
            drawMainButton(draw, buttonX, firstY + step * i, BUTTON_WIDTH * scale,
                    MAIN_LABELS[i], MAIN_ICONS[i], mouseX, mouseY, scale);
        }

        float bottomY = firstY + step * MAIN_LABELS.length;
        float halfWidth = (BUTTON_WIDTH - BUTTON_GAP) * scale * 0.5F;
        drawSmallButton(draw, buttonX, bottomY, halfWidth, "Options", mouseX, mouseY, scale);
        drawSmallButton(draw, buttonX + halfWidth + BUTTON_GAP * scale, bottomY, halfWidth,
                "Exit", mouseX, mouseY, scale);

        draw.getMatrices().pop();
    }

    private static final long BACKDROP_START = System.currentTimeMillis();

    /** Monotonic session time: the old 600s modulo made the smoke pattern jump on wrap. */
    public static float backdropTime() {
        return (System.currentTimeMillis() - BACKDROP_START) / 1000.0F;
    }

    private void renderBackdrop(CustomDrawContext draw) {
        DrawUtil.drawMainMenuBackground(draw.getMatrices(), this.width, this.height, backdropTime());
    }

    private ColorRGBA accent() {
        try {
            ColorRGBA themeColor = Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor();
            return themeColor != null ? themeColor.withAlpha(242) : DEFAULT_ACCENT;
        } catch (RuntimeException ignored) {
            return DEFAULT_ACCENT;
        }
    }

    private static final net.minecraft.util.Identifier LOGO_GLOW =
            net.minecraft.util.Identifier.of("wyvern", "icons/glow.png");

    /** WYVERN logo glyph with a soft layered accent glow behind it. */
    private void drawLogo(CustomDrawContext draw, float centerX, float y, float size) {
        float glyphCenterY = y + size * 0.35F;
        float glowW = size * 3.4F;
        float glowH = size * 2.1F;
        draw.drawTexture(LOGO_GLOW, centerX - glowW * 0.5F, glyphCenterY - glowH * 0.5F,
                glowW, glowH, fade(accent().withAlpha(38)));
        draw.drawTexture(LOGO_GLOW, centerX - glowW * 0.3F, glyphCenterY - glowH * 0.3F,
                glowW * 0.6F, glowH * 0.6F, fade(accent().withAlpha(52)));
        float glyphW = Fonts.WYVERN.getWidth("A", size);
        draw.drawText(Fonts.WYVERN.getFont(size), "A", centerX - glyphW * 0.5F, y, fade(accent()));
    }

    private void drawMainButton(CustomDrawContext draw, float x, float y, float width,
                                String label, String icon, int mouseX, int mouseY, float scale) {
        float height = BUTTON_HEIGHT * scale;
        float hover = hoverValue("btn:" + label, hovered(x, y, width, height, mouseX, mouseY));
        drawPanel(draw, x, y, width, height, hover, 6.5F * scale);

        float iconSize = 10.6F * scale;
        draw.drawText(Fonts.MAINMENU.getFont(iconSize), icon,
                x + 11.0F * scale, y + (height - iconSize) * 0.5F + 2.1F * scale, fade(accent()));

        float labelSize = 8.4F * scale;
        float labelW = Fonts.MEDIUM.getWidth(label, labelSize);
        draw.drawText(Fonts.MEDIUM.getFont(labelSize), label,
                x + (width - labelW) * 0.5F, y + (height - labelSize) * 0.5F + 1.4F * scale, fade(WHITE));
    }

    private void drawSmallButton(CustomDrawContext draw, float x, float y, float width,
                                 String label, int mouseX, int mouseY, float scale) {
        float height = BUTTON_HEIGHT * scale;
        float hover = hoverValue("btn:" + label, hovered(x, y, width, height, mouseX, mouseY));
        drawPanel(draw, x, y, width, height, hover, 6.5F * scale);

        float labelSize = 8.4F * scale;
        float labelW = Fonts.MEDIUM.getWidth(label, labelSize);
        draw.drawText(Fonts.MEDIUM.getFont(labelSize), label,
                x + (width - labelW) * 0.5F, y + (height - labelSize) * 0.5F + 1.4F * scale, fade(WHITE));
    }

    /** Plain neutral-dark frosted blur, no outline; smoothly lighter when hovered. */
    private void drawPanel(CustomDrawContext draw, float x, float y, float width, float height,
                           float hoverProgress, float radius) {
        ColorRGBA tint = hoverProgress <= 0.001F ? PANEL_TINT
                : ColorRGBA.lerp(PANEL_TINT, PANEL_HOVER_TINT, hoverProgress);
        DrawUtil.drawBlur(draw.getMatrices(), x, y, width, height, 20.0F * layoutScale(),
                BorderRadius.all(radius), fade(tint));
    }

    private float firstButtonY(float scale) {
        return this.height * 0.5F + 8.0F * scale;
    }

    private float layoutScale() {
        return Math.max(0.65F, Math.min(this.width / DESIGN_WIDTH, this.height / DESIGN_HEIGHT));
    }

    private boolean hovered(float x, float y, float width, float height, double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private void open(Screen screen) {
        if (this.client != null) {
            this.client.setScreen(screen);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0 || this.closing) {
            return true;
        }

        float scale = layoutScale();
        float centerX = this.width * 0.5F;
        float buttonX = centerX - BUTTON_WIDTH * scale * 0.5F;
        float firstY = firstButtonY(scale);
        float step = (BUTTON_HEIGHT + BUTTON_GAP) * scale;
        float fullWidth = BUTTON_WIDTH * scale;
        float height = BUTTON_HEIGHT * scale;

        if (hovered(buttonX, firstY, fullWidth, height, mouseX, mouseY)) {
            beginTransition(new SelectWorldScreen(this));
            return true;
        }
        if (hovered(buttonX, firstY + step, fullWidth, height, mouseX, mouseY)) {
            beginTransition(new MultiplayerScreen(this));
            return true;
        }
        if (hovered(buttonX, firstY + step * 2.0F, fullWidth, height, mouseX, mouseY)) {
            beginTransition(new AccountManagerScreen(this));
            return true;
        }

        float bottomY = firstY + step * 3.0F;
        float halfWidth = (BUTTON_WIDTH - BUTTON_GAP) * scale * 0.5F;
        if (hovered(buttonX, bottomY, halfWidth, height, mouseX, mouseY)) {
            if (this.client != null) {
                beginTransition(new OptionsScreen(this, this.client.options));
            }
            return true;
        }
        if (hovered(buttonX + halfWidth + BUTTON_GAP * scale, bottomY, halfWidth, height, mouseX, mouseY)) {
            if (this.client != null) {
                this.client.scheduleStop();
            }
            return true;
        }

        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return keyCode == 256 || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}

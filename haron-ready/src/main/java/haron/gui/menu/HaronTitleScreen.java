package haron.gui.menu;

import haron.client.MinecraftClientAccess;
import haron.gui.menu.AltManagerScreen;
import haron.modules.hud.ClientColor;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ScaledGuiProjection;
import haron.render.icons.HaronIcons;
import haron.render.ScreenPoint;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.haron.Haron;

public class HaronTitleScreen
extends Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(HaronTitleScreen.class);
    private static final Identifier MENU_BG = Identifier.of((String)"haron", (String)"textures/menu.png");
    private static final Identifier BLOOM_TEX = Identifier.of((String)"haron", (String)"textures/bloom.png");
    private static final float LOGO_SIZE = 72.0f;
    private static final float BTN_W = 165.0f;
    private static final float BTN_H = 28.0f;
    private static final float BTN_GAP = 6.0f;
    private static final float BTN_RADIUS = 14.0f;
    private static final float SOC_SIZE = 26.0f;
    private static final float SOC_GAP = 8.0f;
    private static final float SOC_RADIUS = 8.0f;
    private static final float EXIT_W = 150.0f;
    private static final float EXIT_H = 32.0f;
    private static final float EXIT_SLIDER_SIZE = 26.0f;
    private float exitProgress = 0.0f;
    private boolean isDraggingExit = false;
    private final Color TEXT_WHITE = new Color(245, 245, 245);
    private final Color TEXT_IDLE = new Color(210, 205, 200);
    private final Color TEXT_MUTED = new Color(130, 120, 115);
    private final Color TEXT_DARK = new Color(20, 15, 12);
    private static final String[] MENU_BUTTONS = new String[]{"Singleplayer", "Multiplayer", "Accounts", "Options"};

    private float getCenterY() {
        return this.screenH() / 2.0f - 40.0f;
    }

    private Color getBtnHoverBg() {
        Color color = this.getAccent();
        return new Color(color.getRed() / 6, color.getGreen() / 6, color.getBlue() / 6, 230);
    }

    private Color getBtnIdleBg() {
        Color color = this.getAccent();
        return new Color(color.getRed() / 10, color.getGreen() / 10, color.getBlue() / 10, 210);
    }

    private Color getBtnIdleBorder() {
        Color color = this.getAccent();
        return new Color(color.getRed() / 5, color.getGreen() / 5, color.getBlue() / 5, 200);
    }

    private void drawExitIcon(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, Color color) {
        s7swsm2.a(f - 3.0f, f2 - 4.0f, 5.0f, 8.0f, 1.2f, color, color, color, color, matrixStack);
        s7swsm2.a(f + 1.0f, f2 - 1.0f, 3.0f, 2.0f, 0.5f, color, color, color, color, matrixStack);
    }

    private void drawDiscordIcon(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, Color color) {
        s7swsm2.a(f - 4.0f, f2 - 2.5f, 8.0f, 5.0f, 2.0f, color, color, color, color, matrixStack);
        s7swsm2.a(f - 2.5f, f2 + 1.5f, 2.0f, 1.5f, 0.5f, color, color, color, color, matrixStack);
        s7swsm2.a(f + 0.5f, f2 + 1.5f, 2.0f, 1.5f, 0.5f, color, color, color, color, matrixStack);
    }

    private void drawTelegramIcon(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, Color color, Color color2) {
        s7swsm2.a(f - 3.5f, f2 - 3.0f, 7.0f, 6.0f, 1.0f, color, color, color, color, matrixStack);
        s7swsm2.a(f - 1.5f, f2 - 1.0f, 3.0f, 3.0f, 0.5f, color2, color2, color2, color2, matrixStack);
    }

    private void drawButtonIcon(int n, ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, Color color) {
        switch (n) {
            case 0: {
                s7swsm2.a(f - 1.5f, f2 - 4.0f, 3.0f, 3.0f, 1.5f, color, color, color, color, matrixStack);
                s7swsm2.a(f - 3.0f, f2 + 0.0f, 6.0f, 4.0f, 1.5f, color, color, color, color, matrixStack);
                break;
            }
            case 1: {
                s7swsm2.a(f - 4.0f, f2 - 3.0f, 2.5f, 2.5f, 1.2f, color, color, color, color, matrixStack);
                s7swsm2.a(f - 5.5f, f2 + 0.5f, 5.0f, 3.5f, 1.2f, color, color, color, color, matrixStack);
                s7swsm2.a(f + 1.5f, f2 - 3.0f, 2.5f, 2.5f, 1.2f, color, color, color, color, matrixStack);
                s7swsm2.a(f + 0.0f, f2 + 0.5f, 5.0f, 3.5f, 1.2f, color, color, color, color, matrixStack);
                break;
            }
            case 2: {
                s7swsm2.a(f - 3.0f, f2 - 3.0f, 2.0f, 2.0f, 0.8f, color, color, color, color, matrixStack);
                s7swsm2.a(f + 1.0f, f2 - 3.0f, 2.0f, 2.0f, 0.8f, color, color, color, color, matrixStack);
                s7swsm2.a(f - 3.0f, f2 + 1.0f, 2.0f, 2.0f, 0.8f, color, color, color, color, matrixStack);
                s7swsm2.a(f + 1.0f, f2 + 1.0f, 2.0f, 2.0f, 0.8f, color, color, color, color, matrixStack);
                break;
            }
            case 3: {
                s7swsm2.a(f - 3.5f, f2 - 3.5f, 7.0f, 7.0f, 3.5f, color, color, color, color, matrixStack);
            }
        }
    }

    public void removed() {
        int n = 890;
        this.isDraggingExit = false;
        super.removed();
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.isDraggingExit = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button == 0 && this.isDraggingExit) {
            try {
                ScreenPoint screenPoint = ScaledGuiProjection.a(mouseX, mouseY);
                int n = screenPoint.x();
                float f = this.screenW();
                float f2 = this.screenH();
                float f3 = (f - 150.0f) / 2.0f;
                float f4 = 118.0f;
                float f5 = (float)n - (f3 + 3.0f + 13.0f);
                this.exitProgress = Math.max(0.0f, Math.min(1.0f, f5 / 118.0f));
                if (this.exitProgress >= 0.95f && this.client != null) {
                    this.client.scheduleStop();
                }
                return true;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return false;
        }
        try {
            float f;
            float f2;
            ScreenPoint screenPoint = ScaledGuiProjection.a(mouseX, mouseY);
            int n = screenPoint.x();
            int n2 = screenPoint.y();
            float f3 = this.screenW();
            float f4 = this.screenH();
            float f5 = this.getCenterY();
            float f6 = f5 - 140.0f + 72.0f + 44.0f + 16.0f;
            for (int i = 0; i < MENU_BUTTONS.length; ++i) {
                f2 = (f3 - 165.0f) / 2.0f;
                f = f6 + (float)i * 34.0f;
                if (!((float)n >= f2) || !((float)n <= f2 + 165.0f) || !((float)n2 >= f) || !((float)n2 <= f + 28.0f)) continue;
                this.onButton(i);
                return true;
            }
            float f7 = f6 + (float)MENU_BUTTONS.length * 34.0f + 6.0f;
            f2 = 60.0f;
            f = (f3 - 60.0f) / 2.0f;
            if ((float)n >= f && (float)n <= f + 26.0f && (float)n2 >= f7 && (float)n2 <= f7 + 26.0f) {
                Util.getOperatingSystem().open("https://t.me/nativevm");
                return true;
            }
            float f8 = f + 26.0f + 8.0f;
            if ((float)n >= f8 && (float)n <= f8 + 26.0f && (float)n2 >= f7 && (float)n2 <= f7 + 26.0f) {
                return true;
            }
            float f9 = (f3 - 150.0f) / 2.0f;
            float f10 = f4 - 32.0f - 18.0f;
            if ((float)n >= f9 && (float)n <= f9 + 150.0f && (float)n2 >= f10 && (float)n2 <= f10 + 32.0f) {
                this.isDraggingExit = true;
                return true;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    private float screenW() {
        int n = 839;
        return (float)MinecraftClientAccess.d.getWidth() / 2.0f;
    }

    private float screenH() {
        return (float)MinecraftClientAccess.d.getHeight() / 2.0f;
    }

    private Color getAccent() {
        return pryrvd.ACCENT;
    }

    private void onButton(int n) {
        if (this.client == null) {
            return;
        }
        try {
            switch (n) {
                case 0: {
                    this.client.setScreen((Screen)new SelectWorldScreen((Screen)this));
                    break;
                }
                case 1: {
                    this.client.setScreen((Screen)new MultiplayerScreen((Screen)this));
                    break;
                }
                case 2: {
                    this.client.setScreen((Screen)new AltManagerScreen(this));
                    break;
                }
                case 3: {
                    this.client.setScreen((Screen)new OptionsScreen((Screen)this, this.client.options));
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private void doRender(DrawContext drawContext, int n, int n2, float f) {
        Color color;
        Color color2;
        boolean bl;
        float f2;
        float f3;
        Object object;
        MatrixStack matrixStack = drawContext.getMatrices();
        ShapeRenderer s7swsm2 = Haron.getInstance().getRender();
        if (s7swsm2 == null) {
            super.render(drawContext, n, n2, f);
            return;
        }
        ScaledGuiProjection.a(2.0);
        ScreenPoint screenPoint = ScaledGuiProjection.a(n, n2);
        int n3 = screenPoint.x();
        int n4 = screenPoint.y();
        float f4 = this.screenW();
        float f5 = this.screenH();
        float f6 = this.getCenterY();
        Color color3 = ClientColor.currentColor();
        s7swsm2.a(MENU_BG, 0.0f, 0.0f, f4, f5, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, color3 != null ? color3 : new Color(255, 255, 255, 255), matrixStack);
        float f7 = (f4 - 72.0f) / 2.0f;
        float f8 = f6 - 140.0f;
        float f9 = 158.40001f;
        float f10 = f7 - 43.200005f;
        float f11 = f8 - 43.200005f;
        s7swsm2.a(BLOOM_TEX, f10, f11, 158.40001f, 158.40001f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, pryrvd.a(pryrvd.ACCENT, 180), matrixStack);
        Identifier identifier = HaronIcons.get("logo_menu");
        if (identifier != null) {
            object = ClientColor.currentColor();
            s7swsm2.a(identifier, f7, f8, 72.0f, 72.0f, 8.639999f, 0.0f, 0.0f, 1.0f, 1.0f, (Color)(object != null ? object : new Color(255, 255, 255, 255)), matrixStack);
        }
        object = ClientFonts.b[14];
        FontRenderer v6hnga2 = ClientFonts.a[10];
        String string = "src by";
        String string2 = " @nativevm";
        float f12 = ((FontRenderer)object).a(string);
        float f13 = ((FontRenderer)object).a(string2);
        float f14 = f12 + f13;
        float f15 = (f4 - f14) / 2.0f;
        float f16 = f8 + 72.0f + 6.0f;
        ((FontRenderer)object).a(string, f15, (double)f16, this.TEXT_WHITE, matrixStack);
        ((FontRenderer)object).a(string2, f15 + f12, (double)f16, pryrvd.ACCENT, matrixStack);
        String string3 = "1.21.4";
        v6hnga2.a(string3, (f4 - v6hnga2.a(string3)) / 2.0f, (double)(f16 + 13.0f), this.TEXT_MUTED, matrixStack);
        FontRenderer v6hnga3 = ClientFonts.a[12];
        String string4 = this.client != null && this.client.getSession() != null ? this.client.getSession().getUsername() : "User";
        String string5 = "Welcome back, ";
        float f17 = v6hnga3.a(string5);
        float f18 = v6hnga3.a(string4);
        float f19 = (f4 - (f17 + f18)) / 2.0f;
        float f20 = f16 + 38.0f;
        v6hnga3.a(string5, f19, (double)f20, this.TEXT_WHITE, matrixStack);
        v6hnga3.a(string4, f19 + f17 + 0.5f, (double)f20, new Color(255, 255, 255, 100), matrixStack);
        v6hnga3.a(string4, f19 + f17, (double)f20, pryrvd.ACCENT, matrixStack);
        float f21 = f20 + 16.0f;
        FontRenderer v6hnga4 = ClientFonts.b[12];
        for (int i = 0; i < MENU_BUTTONS.length; ++i) {
            f3 = (f4 - 165.0f) / 2.0f;
            f2 = f21 + (float)i * 34.0f;
            bl = (float)n3 >= f3 && (float)n3 <= f3 + 165.0f && (float)n4 >= f2 && (float)n4 <= f2 + 28.0f;
            color2 = bl ? this.getBtnHoverBg() : this.getBtnIdleBg();
            color = bl ? this.TEXT_WHITE : this.TEXT_IDLE;
            Color color4 = bl ? pryrvd.ACCENT : this.TEXT_MUTED;
            s7swsm2.a(f3 - 0.8f, f2 - 0.8f, 166.6f, 29.6f, 14.8f, this.getBtnIdleBorder(), this.getBtnIdleBorder(), this.getBtnIdleBorder(), this.getBtnIdleBorder(), matrixStack);
            s7swsm2.a(f3, f2, 165.0f, 28.0f, 14.0f, color2, color2, color2, color2, matrixStack);
            this.drawButtonIcon(i, s7swsm2, matrixStack, f3 + 12.0f, f2 + 14.0f, color4);
            String string6 = MENU_BUTTONS[i];
            float f22 = f3 + (165.0f - v6hnga4.a(string6)) / 2.0f;
            float f23 = f2 + (28.0f - v6hnga4.b(string6)) / 2.0f + 1.0f;
            v6hnga4.a(string6, f22, (double)f23, color, matrixStack);
        }
        float f24 = f21 + (float)MENU_BUTTONS.length * 34.0f + 6.0f;
        f3 = 60.0f;
        f2 = (f4 - 60.0f) / 2.0f;
        bl = (float)n3 >= f2 && (float)n3 <= f2 + 26.0f && (float)n4 >= f24 && (float)n4 <= f24 + 26.0f;
        color2 = bl ? this.getBtnHoverBg() : this.getBtnIdleBg();
        color = bl ? pryrvd.ACCENT : this.TEXT_MUTED;
        s7swsm2.a(f2 - 0.8f, f24 - 0.8f, 27.6f, 27.6f, 8.8f, this.getBtnIdleBorder(), this.getBtnIdleBorder(), this.getBtnIdleBorder(), this.getBtnIdleBorder(), matrixStack);
        s7swsm2.a(f2, f24, 26.0f, 26.0f, 8.0f, color2, color2, color2, color2, matrixStack);
        this.drawTelegramIcon(s7swsm2, matrixStack, f2 + 13.0f, f24 + 13.0f, color, color2);
        float f25 = f2 + 26.0f + 8.0f;
        boolean bl2 = (float)n3 >= f25 && (float)n3 <= f25 + 26.0f && (float)n4 >= f24 && (float)n4 <= f24 + 26.0f;
        Color color5 = bl2 ? this.getBtnHoverBg() : this.getBtnIdleBg();
        Color color6 = bl2 ? pryrvd.ACCENT : this.TEXT_MUTED;
        s7swsm2.a(f25 - 0.8f, f24 - 0.8f, 27.6f, 27.6f, 8.8f, this.getBtnIdleBorder(), this.getBtnIdleBorder(), this.getBtnIdleBorder(), this.getBtnIdleBorder(), matrixStack);
        s7swsm2.a(f25, f24, 26.0f, 26.0f, 8.0f, color5, color5, color5, color5, matrixStack);
        this.drawDiscordIcon(s7swsm2, matrixStack, f25 + 13.0f, f24 + 13.0f, color6);
        float f26 = (f4 - 150.0f) / 2.0f;
        float f27 = f5 - 32.0f - 18.0f;
        s7swsm2.a(f26 - 0.8f, f27 - 0.8f, 151.6f, 33.6f, 16.8f, this.getBtnIdleBorder(), this.getBtnIdleBorder(), this.getBtnIdleBorder(), this.getBtnIdleBorder(), matrixStack);
        s7swsm2.a(f26, f27, 150.0f, 32.0f, 16.0f, this.getBtnIdleBg(), this.getBtnIdleBg(), this.getBtnIdleBg(), this.getBtnIdleBg(), matrixStack);
        FontRenderer v6hnga5 = ClientFonts.a[11];
        String string7 = "Slide to exit";
        v6hnga5.a(string7, f26 + 50.0f, (double)(f27 + (32.0f - v6hnga5.b(string7)) / 2.0f + 1.0f), this.TEXT_MUTED, matrixStack);
        float f28 = 118.0f;
        float f29 = f26 + 3.0f + this.exitProgress * 118.0f;
        float f30 = f27 + 3.0f;
        s7swsm2.a(f29, f30, 26.0f, 26.0f, 13.0f, pryrvd.ACCENT, pryrvd.ACCENT, pryrvd.ACCENT, pryrvd.ACCENT, matrixStack);
        this.drawExitIcon(s7swsm2, matrixStack, f29 + 13.0f, f30 + 13.0f, this.TEXT_DARK);
        if (!this.isDraggingExit && this.exitProgress > 0.0f) {
            this.exitProgress = Math.max(0.0f, this.exitProgress - f * 0.15f);
        }
        ScaledGuiProjection.a();
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        try {
            this.doRender(context, mouseX, mouseY, delta);
        }
        catch (Throwable throwable) {
            try {
                super.render(context, mouseX, mouseY, delta);
            }
            catch (Throwable throwable2) {
                // empty catch block
            }
        }
    }

    public HaronTitleScreen() {
        super((Text)Text.literal((String)"src by @nativevm"));
    }
}


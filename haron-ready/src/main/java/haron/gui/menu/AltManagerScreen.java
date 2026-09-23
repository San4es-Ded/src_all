package haron.gui.menu;

import haron.client.MinecraftClientAccess;
import haron.modules.hud.ClientColor;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ScaledGuiProjection;
import haron.render.ScreenPoint;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.session.Session;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.haron.Haron;
import ru.haron.mixin.accessor.MinecraftClientAccessor;

public class AltManagerScreen
extends Screen {
    private static final Identifier MENU_BG = Identifier.of((String)"haron", (String)"textures/menu.png");
    private static final float CARD_W = 128.0f;
    private static final float CARD_H = 26.0f;
    private static final float CARD_GAP = 8.0f;
    private static final float CARD_RADIUS = 8.0f;
    private static final float HEAD_SIZE = 16.0f;
    private static final int COLUMNS = 2;
    private static final int MAX_ROWS = 6;
    private static final float BOX_PAD = 12.0f;
    private static final float BOX_RADIUS = 12.0f;
    private static final float PANEL_W = 132.0f;
    private static final float INPUT_H = 18.0f;
    private static final float BTN_H = 17.0f;
    private static final float PANEL_PAD = 7.0f;
    private static final int MAX_NICK_LEN = 16;
    private final Screen parent;
    private final List<String> alts = new ArrayList<String>();
    private String input = "";
    private boolean inputFocused = false;
    private int scroll = 0;
    private final Color TEXT_WHITE = new Color(245, 245, 245);
    private final Color TEXT_MUTED = new Color(130, 120, 115);
    private final Color TRASH_RED = new Color(205, 60, 60, 255);

    private String currentName() {
        try {
            return ((MinecraftClientAccessor)this.client).getSession().getUsername();
        }
        catch (Throwable throwable) {
            return "Player";
        }
    }

    private Color getBtnHoverBg() {
        Color color = pryrvd.ACCENT;
        return new Color(color.getRed() / 6, color.getGreen() / 6, color.getBlue() / 6, 230);
    }

    private void addCurrentInput() {
        String string = this.input.trim();
        if (string.length() >= 3 && string.length() <= 16 && !this.alts.contains(string)) {
            this.alts.add(string);
            this.save();
            this.input = "";
        }
    }

    private void addRandomAlt() {
        String string;
        int n = 0;
        while (this.alts.contains(string = this.generateRandomNick()) && ++n < 100) {
        }
        if (!this.alts.contains(string)) {
            this.alts.add(string);
            this.save();
        }
    }

    private Color getBtnIdleBg() {
        Color color = pryrvd.ACCENT;
        return new Color(color.getRed() / 10, color.getGreen() / 10, color.getBlue() / 10, 210);
    }

    private Color getBtnIdleBorder() {
        Color color = pryrvd.ACCENT;
        return new Color(color.getRed() / 5, color.getGreen() / 5, color.getBlue() / 5, 200);
    }

    private float totalContentH() {
        return this.boxH() + 8.0f + this.panelH();
    }

    private String generateRandomNick() {
        String string = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int n = 3 + (int)(Math.random() * 14.0);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            stringBuilder.append(string.charAt((int)(Math.random() * (double)string.length())));
        }
        return stringBuilder.toString();
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int n = (this.alts.size() + 2 - 1) / 2;
        if (n > 6) {
            this.scroll -= (int)Math.signum(verticalAmount);
            this.scroll = Math.max(0, Math.min(this.scroll, n - 6));
        }
        return true;
    }

    public boolean charTyped(char chr, int modifiers) {
        if (!this.inputFocused) {
            return false;
        }
        if ((Character.isLetterOrDigit(chr) || chr == '_') && this.input.length() < 16) {
            this.input = AltManagerScreen.$sf$3(this.input, chr);
            return true;
        }
        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float f;
        float f2;
        float f3;
        float f4;
        if (button != 0) {
            return false;
        }
        ScreenPoint screenPoint = ScaledGuiProjection.a(mouseX, mouseY);
        int n = screenPoint.x();
        int n2 = screenPoint.y();
        int n3 = Math.min(this.alts.size() - this.scroll * 2, 12);
        for (int i = 0; i < n3; ++i) {
            int n4 = this.scroll * 2 + i;
            int n5 = i % 2;
            int n6 = i / 2;
            f4 = this.cardX(n5);
            f3 = this.cardY(n6);
            f2 = 16.0f;
            f = f4 + 128.0f - 16.0f - 5.0f;
            float f5 = f3 + 5.0f;
            if ((float)n >= f && (float)n <= f + 16.0f && (float)n2 >= f5 && (float)n2 <= f5 + 16.0f) {
                this.alts.remove(n4);
                this.save();
                if (this.scroll > 0 && this.scroll * 2 >= this.alts.size()) {
                    --this.scroll;
                }
                return true;
            }
            if (!((float)n >= f4) || !((float)n <= f4 + 128.0f) || !((float)n2 >= f3) || !((float)n2 <= f3 + 26.0f)) continue;
            this.loginAs(this.alts.get(n4));
            return true;
        }
        float f6 = this.panelX();
        float f7 = this.panelY();
        float f8 = f7 + 7.0f + 12.0f;
        float f9 = 118.0f;
        this.inputFocused = (float)n >= f6 + 7.0f && (float)n <= f6 + 7.0f + 118.0f && (float)n2 >= f8 && (float)n2 <= f8 + 18.0f;
        f4 = f8 + 18.0f + 6.0f;
        f3 = 17.0f;
        f2 = 96.0f;
        if ((float)n >= f6 + 7.0f && (float)n <= f6 + 7.0f + 96.0f && (float)n2 >= f4 && (float)n2 <= f4 + 17.0f) {
            this.addCurrentInput();
            return true;
        }
        f = f6 + 7.0f + 96.0f + 5.0f;
        if ((float)n >= f && (float)n <= f + 17.0f && (float)n2 >= f4 && (float)n2 <= f4 + 17.0f) {
            this.addRandomAlt();
            return true;
        }
        return true;
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.client.setScreen(this.parent);
            return true;
        }
        if (this.inputFocused) {
            if (keyCode == 259 && !this.input.isEmpty()) {
                this.input = this.input.substring(0, this.input.length() - 1);
                return true;
            }
            if (keyCode == 257) {
                this.addCurrentInput();
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private Path altsFile() {
        String string = System.getenv("APPDATA");
        if (string == null) {
            string = System.getProperty("user.home");
        }
        return Paths.get(string, "Haron", "config", "alts.txt");
    }

    private float cardY(int n) {
        return this.gridY() + 12.0f + (float)n * 34.0f;
    }

    private float gridX() {
        int n = 837;
        return (this.screenW() - this.gridW()) / 2.0f;
    }

    private float boxH() {
        return 220.0f;
    }

    private float screenW() {
        return (float)MinecraftClientAccess.d.getWidth() / 2.0f;
    }

    private float boxX() {
        int n = 437;
        return this.gridX() - 12.0f;
    }

    private float panelX() {
        return (this.screenW() - 132.0f) / 2.0f;
    }

    private float panelY() {
        return this.boxY() + this.boxH() + 8.0f;
    }

    private float screenH() {
        return (float)MinecraftClientAccess.d.getHeight() / 2.0f;
    }

    private float panelH() {
        return 68.0f;
    }

    private float gridY() {
        return (this.screenH() - this.totalContentH()) / 2.0f;
    }

    private float cardX(int n) {
        return this.gridX() + (float)n * 136.0f;
    }

    private Color getAccent() {
        return pryrvd.ACCENT;
    }

    private float gridW() {
        return 264.0f;
    }

    private void loginAs(String string) {
        try {
            UUID uUID = UUID.nameUUIDFromBytes(AltManagerScreen.$sf$0(string).getBytes(StandardCharsets.UTF_8));
            Session session = new Session(string, uUID, "0", Optional.empty(), Optional.empty(), Session.AccountType.LEGACY);
            ((MinecraftClientAccessor)this.client).setSession(session);
        }
        catch (Throwable throwable) {
            Haron.getLOGGER().error("[Haron] login failed", throwable);
        }
    }

    private float boxY() {
        return this.gridY();
    }

    private float boxW() {
        return this.gridW() + 24.0f;
    }

    private void drawTrash(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, Color color) {
        float f3 = 6.0f;
        s7swsm2.a(f - 3.0f, f2 - 3.4f, 6.0f, 1.2f, 0.5f, color, color, color, color, matrixStack);
        s7swsm2.a(f - 1.4f, f2 - 4.6f, 2.8f, 1.2f, 0.5f, color, color, color, color, matrixStack);
        s7swsm2.a(f - 2.6f, f2 - 2.0f, 5.2f, 6.0f, 1.0f, color, color, color, color, matrixStack);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Object object;
        float f;
        float f2;
        MatrixStack matrixStack = context.getMatrices();
        ShapeRenderer s7swsm2 = Haron.getInstance().getRender();
        ScaledGuiProjection.a(2.0);
        ScreenPoint screenPoint = ScaledGuiProjection.a(mouseX, mouseY);
        int n = screenPoint.x();
        int n2 = screenPoint.y();
        float f3 = this.screenW();
        float f4 = this.screenH();
        Color color = ClientColor.currentColor();
        s7swsm2.a(MENU_BG, 0.0f, 0.0f, f3, f4, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, color != null ? color : new Color(255, 255, 255, 255), matrixStack);
        FontRenderer v6hnga2 = ClientFonts.a[14];
        FontRenderer v6hnga3 = ClientFonts.a[12];
        Identifier identifier = DefaultSkinHelper.getTexture();
        Color color2 = new Color(255, 255, 255, 255);
        s7swsm2.a(this.boxX() - 0.8f, this.boxY() - 0.8f, this.boxW() + 1.6f, this.boxH() + 1.6f, 12.8f, this.getBtnIdleBorder(), this.getBtnIdleBorder(), this.getBtnIdleBorder(), this.getBtnIdleBorder(), matrixStack);
        s7swsm2.a(this.boxX(), this.boxY(), this.boxW(), this.boxH(), 12.0f, this.getBtnIdleBg(), this.getBtnIdleBg(), this.getBtnIdleBg(), this.getBtnIdleBg(), matrixStack);
        int n3 = Math.min(this.alts.size() - this.scroll * 2, 12);
        for (int i = 0; i < n3; ++i) {
            int n4 = this.scroll * 2 + i;
            String string = this.alts.get(n4);
            int n5 = i % 2;
            int n6 = i / 2;
            f2 = this.cardX(n5);
            float f5 = this.cardY(n6);
            boolean bl = (float)n >= f2 && (float)n <= f2 + 128.0f && (float)n2 >= f5 && (float)n2 <= f5 + 26.0f;
            boolean bl2 = string.equals(this.currentName());
            Color color3 = bl2 ? pryrvd.ACCENT : this.getBtnIdleBorder();
            Color color4 = bl ? this.getBtnHoverBg() : this.getBtnIdleBg();
            s7swsm2.a(f2 - 0.8f, f5 - 0.8f, 129.6f, 27.6f, 8.8f, color3, color3, color3, color3, matrixStack);
            s7swsm2.a(f2, f5, 128.0f, 26.0f, 8.0f, color4, color4, color4, color4, matrixStack);
            float f6 = f2 + 5.0f;
            float f7 = f5 + 5.0f;
            s7swsm2.a(identifier, f6, f7, 16.0f, 16.0f, 3.0f, 0.125f, 0.125f, 0.125f, 0.125f, color2, matrixStack);
            s7swsm2.a(identifier, f6, f7, 16.0f, 16.0f, 3.0f, 0.625f, 0.125f, 0.125f, 0.125f, color2, matrixStack);
            v6hnga2.a(string, f6 + 16.0f + 6.0f, (double)(f5 + (26.0f - v6hnga2.b(string)) / 2.0f + 1.0f), this.TEXT_WHITE, matrixStack);
            f = 16.0f;
            float f8 = f2 + 128.0f - 16.0f - 5.0f;
            float f9 = f5 + 5.0f;
            boolean bl3 = (float)n >= f8 && (float)n <= f8 + 16.0f && (float)n2 >= f9 && (float)n2 <= f9 + 16.0f;
            object = bl3 ? this.TRASH_RED : this.getBtnIdleBg();
            s7swsm2.a(f8, f9, 16.0f, 16.0f, 5.0f, (Color)object, (Color)object, (Color)object, (Color)object, matrixStack);
            this.drawTrash(s7swsm2, matrixStack, f8 + 8.0f, f9 + 8.0f, bl3 ? color2 : this.TEXT_MUTED);
        }
        if (this.alts.isEmpty()) {
            String string = "Пусто — создай альта ниже";
            v6hnga3.a(string, (f3 - v6hnga3.a(string)) / 2.0f, (double)(this.boxY() + this.boxH() / 2.0f - 4.0f), this.TEXT_MUTED, matrixStack);
        }
        float f10 = this.panelX();
        float f11 = this.panelY();
        float f12 = this.panelH();
        s7swsm2.a(f10 - 0.8f, f11 - 0.8f, 133.6f, f12 + 1.6f, 9.0f, this.getBtnIdleBorder(), this.getBtnIdleBorder(), this.getBtnIdleBorder(), this.getBtnIdleBorder(), matrixStack);
        s7swsm2.a(f10, f11, 132.0f, f12, 8.0f, this.getBtnIdleBg(), this.getBtnIdleBg(), this.getBtnIdleBg(), this.getBtnIdleBg(), matrixStack);
        float f13 = f11 + 7.0f;
        s7swsm2.a(f10 + 7.0f, f13 + 2.0f, 4.0f, 4.0f, 2.0f, pryrvd.ACCENT, pryrvd.ACCENT, pryrvd.ACCENT, pryrvd.ACCENT, matrixStack);
        v6hnga3.a("AltManager", f10 + 7.0f + 8.0f, (double)(f13 - 1.0f), this.TEXT_MUTED, matrixStack);
        float f14 = f13 + 12.0f;
        f2 = 118.0f;
        s7swsm2.a(f10 + 7.0f, f14, 118.0f, 18.0f, 6.0f, this.getBtnIdleBg(), this.getBtnIdleBg(), this.getBtnIdleBg(), this.getBtnIdleBg(), matrixStack);
        if (this.inputFocused) {
            s7swsm2.a(f10 + 7.0f, f14, 2.0f, 18.0f, 1.0f, pryrvd.ACCENT, pryrvd.ACCENT, pryrvd.ACCENT, pryrvd.ACCENT, matrixStack);
        }
        String string = this.input.isEmpty() && !this.inputFocused ? "Name" : AltManagerScreen.$sf$1(this.input, this.inputFocused ? "_" : "");
        v6hnga2.a(string, f10 + 7.0f + 6.0f, (double)(f14 + (18.0f - v6hnga2.b(string)) / 2.0f + 1.0f), this.input.isEmpty() && !this.inputFocused ? this.TEXT_MUTED : this.TEXT_WHITE, matrixStack);
        float f15 = f14 + 18.0f + 6.0f;
        float f16 = 17.0f;
        float f17 = 96.0f;
        boolean bl = (float)n >= f10 + 7.0f && (float)n <= f10 + 7.0f + 96.0f && (float)n2 >= f15 && (float)n2 <= f15 + 17.0f;
        Color color5 = bl ? this.getBtnHoverBg() : this.getBtnIdleBg();
        s7swsm2.a(f10 + 7.0f, f15, 96.0f, 17.0f, 6.0f, color5, color5, color5, color5, matrixStack);
        String string2 = "ShaderProgramBuilder";
        v6hnga2.a(string2, f10 + 7.0f + (96.0f - v6hnga2.a(string2)) / 2.0f, (double)(f15 + (17.0f - v6hnga2.b(string2)) / 2.0f + 1.0f), this.TEXT_WHITE, matrixStack);
        f = f10 + 7.0f + 96.0f + 5.0f;
        boolean bl4 = (float)n >= f && (float)n <= f + 17.0f && (float)n2 >= f15 && (float)n2 <= f15 + 17.0f;
        Color color6 = bl4 ? this.getBtnHoverBg() : this.getBtnIdleBg();
        s7swsm2.a(f, f15, 17.0f, 17.0f, 8.5f, color6, color6, color6, color6, matrixStack);
        String string3 = "?";
        v6hnga2.a(string3, f + (17.0f - v6hnga2.a(string3)) / 2.0f, (double)(f15 + (17.0f - v6hnga2.b(string3)) / 2.0f + 1.0f), this.TEXT_WHITE, matrixStack);
        object = AltManagerScreen.$sf$2(this.currentName());
        v6hnga3.a((String)object, (f3 - v6hnga3.a((String)object)) / 2.0f, (double)(f4 - 16.0f), this.TEXT_MUTED, matrixStack);
        ScaledGuiProjection.a();
    }

    public AltManagerScreen(Screen screen) {
        super((Text)Text.literal((String)"AltManager"));
        this.parent = screen;
        this.load();
    }

    private void load() {
        this.alts.clear();
        try {
            Path path = this.altsFile();
            if (Files.exists(path, new LinkOption[0])) {
                for (String string : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                    String string2 = string.trim();
                    if (string2.isEmpty() || this.alts.contains(string2)) continue;
                    this.alts.add(string2);
                }
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    private void save() {
        try {
            Path path = this.altsFile();
            Files.createDirectories(path.getParent(), new FileAttribute[0]);
            Files.write(path, String.join((CharSequence)"\n", this.alts).getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    private static /* synthetic */ String $sf$0(String string) {
        return "OfflinePlayer:" + string;
    }

    private static /* synthetic */ String $sf$3(String string, char c) {
        return string + c;
    }

    private static /* synthetic */ String $sf$1(String string, String string2) {
        return string + string2;
    }

    private static /* synthetic */ String $sf$2(String string) {
        return "Ник: " + string + "  |  ESC — назад";
    }
}


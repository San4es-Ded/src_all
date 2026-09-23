package haron.gui.events;

import haron.client.MinecraftClientAccess;
import haron.gui.core.CategorySelectionModel;
import haron.gui.core.CategorySelectorOverlay;
import haron.gui.core.ScrollFadeOverlay;
import haron.gui.core.ClickGuiTab;
import haron.gui.core.ClickGuiScreen;
import haron.gui.core.ClickGuiTabType;
import haron.gui.events.CommandKeyBind;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class CommandBindsPanel
implements CategorySelectionModel,
MinecraftClientAccess,
ClickGuiTab {
    public static final List<CommandKeyBind> BINDS = new ArrayList<CommandKeyBind>();
    private static final Set<Integer> HELD = new HashSet<Integer>();
    private static final String[] CATS = new String[]{"Binds"};
    private static final float TOP_OFFSET = 20.0f;
    private static final float SIDE_PAD = 19.0f;
    private static final float TOGGLE_ROW_H = 22.0f;
    private static final float BTN_W = 75.0f;
    private static final float BTN_H = 18.0f;
    private static final float BTN_GAP = 6.0f;
    private static final float CARD_H = 32.0f;
    private static final float CARD_GAP = 6.0f;
    private final ScrollFadeOverlay p;
    private final CategorySelectorOverlay m;
    private String inputText = "";
    private boolean inputFocused = false;
    private final float[] inputRect = new float[4];
    private int q;
    private int r;
    private int k = 0;
    private float scroll = 0.0f;
    private float listVisibleH = 0.0f;
    private final float[] btnAdd = new float[4];
    private final float[] btnClear = new float[4];
    private final List<float[]> keyRects = new ArrayList<float[]>();
    private final List<float[]> delRects = new ArrayList<float[]>();
    private final List<CommandKeyBind> renderedBinds = new ArrayList<CommandKeyBind>();

    public static void tick() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null || minecraftClient.player == null || minecraftClient.getWindow() == null) {
            HELD.clear();
            return;
        }
        if (minecraftClient.currentScreen != null) {
            HELD.clear();
            return;
        }
        long l = minecraftClient.getWindow().getHandle();
        for (CommandKeyBind aprxg32 : BINDS) {
            boolean bl;
            int n = aprxg32.key;
            if (n <= 0 || aprxg32.command == null || aprxg32.command.trim().isEmpty()) continue;
            try {
                bl = GLFW.glfwGetKey((long)l, (int)n) == 1;
            }
            catch (Throwable throwable) {
                continue;
            }
            if (bl) {
                if (!HELD.add(n)) continue;
                CommandBindsPanel.execute(aprxg32.command.trim());
                continue;
            }
            HELD.remove(n);
        }
    }

    private void drawBindsList(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n, int n2) {
        this.keyRects.clear();
        this.delRects.clear();
        this.renderedBinds.clear();
        if (BINDS.isEmpty()) {
            return;
        }
        float f5 = f2 - this.scroll;
        int n3 = 0;
        for (CommandKeyBind aprxg32 : BINDS) {
            Color color;
            String string;
            float f6 = f5 + (float)n3 * 38.0f;
            ++n3;
            if (f6 + 32.0f < f2 || f6 > f2 + f4) continue;
            s7swsm2.a(f, f6, f3, 32.0f, 7.0f, pryrvd.CELL_BG_TOP, pryrvd.CELL_BG_TOP, pryrvd.CELL_BG_BOT, pryrvd.CELL_BG_BOT, matrixStack);
            FontRenderer v6hnga2 = ClientFonts.MEDIUM[14];
            v6hnga2.a(aprxg32.command, f + 10.0f, (double)(f6 + 11.5f), new Color(235, 215, 195, 230), matrixStack);
            float f7 = 80.0f;
            float f8 = f + f3 - 120.0f;
            float f9 = f6 + 7.0f;
            boolean bl = (float)n >= f8 && (float)n <= f8 + 80.0f && (float)n2 >= f9 && (float)n2 <= f9 + 18.0f;
            String string2 = string = aprxg32.binding ? "PRESS..." : this.getKeyName(aprxg32.key);
            color = aprxg32.binding ? pryrvd.ACCENT_SOFT : (bl ? pryrvd.ROW_HOVER_TOP : pryrvd.CELL_BG_TOP);
            Color color3 = aprxg32.binding ? pryrvd.ACCENT_DARK : (bl ? pryrvd.ROW_HOVER_TOP : pryrvd.CELL_BG_BOT);
            s7swsm2.a(f8, f9, 80.0f, 18.0f, 4.0f, color, color, color3, color3, matrixStack);
            FontRenderer v6hnga3 = ClientFonts.MEDIUM[11];
            float f10 = v6hnga3.a(string);
            v6hnga3.a(string, f8 + (80.0f - f10) / 2.0f, (double)(f9 + 5.0f), Color.WHITE, matrixStack);
            float f11 = 20.0f;
            float f12 = f + f3 - 30.0f;
            float f13 = f6 + 7.0f;
            boolean bl2 = (float)n >= f12 && (float)n <= f12 + 20.0f && (float)n2 >= f13 && (float)n2 <= f13 + 18.0f;
            Color color4 = bl2 ? new Color(180, 50, 50, 255) : new Color(110, 35, 35, 255);
            Color color5 = bl2 ? new Color(150, 40, 40, 255) : new Color(90, 25, 25, 255);
            s7swsm2.a(f12, f13, 20.0f, 18.0f, 4.0f, color4, color4, color5, color5, matrixStack);
            float f14 = v6hnga3.a("X");
            v6hnga3.a("X", f12 + (20.0f - f14) / 2.0f, (double)(f13 + 5.0f), Color.WHITE, matrixStack);
            this.keyRects.add(new float[]{f8, f9, 80.0f, 18.0f});
            this.delRects.add(new float[]{f12, f13, 20.0f, 18.0f});
            this.renderedBinds.add(aprxg32);
        }
    }

    private void addBind() {
        String string = this.inputText.trim();
        if (string.isEmpty()) {
            return;
        }
        BINDS.add(new CommandKeyBind(string, 0));
        this.inputText = "";
    }

    private String getKeyName(int n) {
        if (n <= 0 || n == 256) {
            return "NONE";
        }
        switch (n) {
            case 258: {
                return "TAB";
            }
            case 257: {
                return "ENTER";
            }
            case 340: {
                return "LSHIFT";
            }
            case 344: {
                return "RSHIFT";
            }
            case 341: {
                return "LCTRL";
            }
            case 345: {
                return "RCTRL";
            }
            case 342: {
                return "LALT";
            }
            case 346: {
                return "RALT";
            }
            case 32: {
                return "SPACE";
            }
            case 263: {
                return "LEFT";
            }
            case 262: {
                return "RIGHT";
            }
            case 265: {
                return "UP";
            }
            case 264: {
                return "DOWN";
            }
            case 266: {
                return "PGUP";
            }
            case 267: {
                return "PGDN";
            }
            case 268: {
                return "HOME";
            }
            case 269: {
                return "END";
            }
            case 260: {
                return "INSERT";
            }
            case 261: {
                return "DELETE";
            }
            case 290: {
                return "F1";
            }
            case 291: {
                return "F2";
            }
            case 292: {
                return "F3";
            }
            case 293: {
                return "F4";
            }
            case 294: {
                return "F5";
            }
            case 295: {
                return "F6";
            }
            case 296: {
                return "F7";
            }
            case 297: {
                return "F8";
            }
            case 298: {
                return "F9";
            }
            case 299: {
                return "F10";
            }
            case 300: {
                return "F11";
            }
            case 301: {
                return "F12";
            }
        }
        try {
            String string = GLFW.glfwGetKeyName((int)n, (int)0);
            if (string != null) {
                return string.toUpperCase(Locale.ROOT);
            }
        }
        catch (Throwable throwable) {
        }
        return CommandBindsPanel.$sf$1(n);
    }

    private void drawInput(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2) {
        Color color;
        String string;
        Color color2;
        boolean bl;
        boolean bl2 = bl = (float)n >= f && (float)n <= f + f3 && (float)n2 >= f2 && (float)n2 <= f2 + 18.0f;
        color2 = this.inputFocused ? pryrvd.ACCENT_SOFT : (bl ? pryrvd.ROW_HOVER_TOP : pryrvd.CELL_BG_TOP);
        Color color4 = this.inputFocused ? pryrvd.ACCENT_DARK : (bl ? pryrvd.ROW_HOVER_TOP : pryrvd.CELL_BG_BOT);
        s7swsm2.a(f, f2, f3, 18.0f, 5.0f, color2, color2, color4, color4, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.MEDIUM[12];
        if (this.inputText.isEmpty() && !this.inputFocused) {
            string = "Напиши бинд";
            color = new Color(160, 140, 120, 140);
        } else {
            string = this.inputText;
            if (this.inputFocused && System.currentTimeMillis() % 1000L < 500L) {
                string = CommandBindsPanel.$sf$0(string);
            }
            color = new Color(240, 220, 200, 230);
        }
        v6hnga2.a(string, f + 8.0f, (double)(f2 + 5.0f), color, matrixStack);
        this.inputRect[0] = f;
        this.inputRect[1] = f2;
        this.inputRect[2] = f3;
        this.inputRect[3] = 18.0f;
    }

    private void drawButton(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, String string, float[] fArray, int n, int n2, Color color) {
        boolean bl = (float)n >= f && (float)n <= f + 75.0f && (float)n2 >= f2 && (float)n2 <= f2 + 18.0f;
        Color color2 = bl ? color.brighter() : color;
        Color color3 = bl ? color : color.darker();
        s7swsm2.a(f, f2, 75.0f, 18.0f, 5.0f, color2, color2, color3, color3, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.MEDIUM[12];
        float f3 = v6hnga2.a(string);
        v6hnga2.a(string, f + (75.0f - f3) / 2.0f, (double)(f2 + 5.0f), Color.WHITE, matrixStack);
        fArray[0] = f;
        fArray[1] = f2;
        fArray[2] = 75.0f;
        fArray[3] = 18.0f;
    }

    public static void onKeyPress(int n) {
        if (n <= 0) {
            return;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null || minecraftClient.player == null) {
            return;
        }
        if (minecraftClient.currentScreen != null) {
            return;
        }
        for (CommandKeyBind aprxg32 : BINDS) {
            if (aprxg32.key != n || aprxg32.command == null || aprxg32.command.trim().isEmpty()) continue;
            CommandBindsPanel.execute(aprxg32.command.trim());
        }
    }

    public CommandBindsPanel() {
        this.m = new CategorySelectorOverlay(this);
        this.p = new ScrollFadeOverlay(25, 10.0f, 7.5f);
    }

    @Override
    public boolean b() {
        return this.inputFocused;
    }

    @Override
    public void b(float f, float f2, int n, int n2) {
        this.a(f, f2, n, n2);
    }

    @Override
    public void c(float f, float f2, int n, int n2) {
    }

    @Override
    public String[] c() {
        return CATS;
    }

    @Override
    public int d() {
        int n = 514;
        return this.k;
    }

    @Override
    public boolean a(int n, int n2, int n3) {
        for (CommandKeyBind aprxg32 : BINDS) {
            if (!aprxg32.binding) continue;
            aprxg32.key = n == 256 ? 0 : n;
            aprxg32.binding = false;
            return true;
        }
        if (this.inputFocused) {
            if (n == 259) {
                if (!this.inputText.isEmpty()) {
                    this.inputText = this.inputText.substring(0, this.inputText.length() - 1);
                }
                return true;
            }
            if (n == 257 || n == 335) {
                this.addBind();
                return true;
            }
            if (n == 256) {
                this.inputFocused = false;
                return true;
            }
            if (n == 86 && (n3 & 2) != 0) {
                try {
                    long l = MinecraftClient.getInstance().getWindow().getHandle();
                    String string = GLFW.glfwGetClipboardString((long)l);
                    if (string != null) {
                        String string2 = string.replace('\n', ' ').replace('\r', ' ');
                        if (this.inputText.length() + string2.length() <= 80) {
                            this.inputText = CommandBindsPanel.$sf$2(this.inputText, string2);
                        }
                    }
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                return true;
            }
            return true;
        }
        return false;
    }

    @Override
    public void a(int n) {
        if (n < 0 || n >= CATS.length || this.k == n) {
            return;
        }
        this.k = n;
        this.scroll = 0.0f;
    }

    public boolean a(char c, int n) {
        if (this.inputFocused) {
            if (c >= ' ' && c != '\u007f' && this.inputText.length() < 80) {
                this.inputText = CommandBindsPanel.$sf$3(this.inputText, c);
            }
            return true;
        }
        return false;
    }

    @Override
    public ClickGuiTabType a() {
        return ClickGuiTabType.EVENTS;
    }

    @Override
    public void a(float f, float f2, int n, int n2) {
        this.m.a(f, f2, n, n2);
        if (CommandBindsPanel.hit(this.inputRect, n, n2)) {
            this.inputFocused = true;
            return;
        }
        this.inputFocused = false;
        if (CommandBindsPanel.hit(this.btnAdd, n, n2)) {
            this.addBind();
            return;
        }
        if (CommandBindsPanel.hit(this.btnClear, n, n2)) {
            BINDS.clear();
            return;
        }
        for (int i = 0; i < this.renderedBinds.size(); ++i) {
            CommandKeyBind aprxg32 = this.renderedBinds.get(i);
            if (CommandBindsPanel.hit(this.keyRects.get(i), n, n2)) {
                for (CommandKeyBind aprxg33 : BINDS) {
                    if (aprxg33 == aprxg32) continue;
                    aprxg33.binding = false;
                }
                aprxg32.binding = !aprxg32.binding;
                return;
            }
            if (!CommandBindsPanel.hit(this.delRects.get(i), n, n2)) continue;
            BINDS.remove(aprxg32);
            return;
        }
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        float f3;
        this.q = n;
        this.r = n2;
        this.m.a(matrixStack, s7swsm2, f, f2, n, n2);
        float f4 = ClickGuiScreen.d();
        float f5 = ClickGuiScreen.e();
        float f6 = f + 19.0f;
        float f7 = f2 + 20.0f;
        float f8 = f4 - 38.0f;
        float f9 = f5 - 20.0f - 9.5f;
        float f10 = f7 + 2.0f;
        float f11 = f8 - 162.0f;
        this.drawInput(matrixStack, s7swsm2, f6, f10, f11, n, n2);
        this.drawButton(matrixStack, s7swsm2, f6 + f11 + 6.0f, f10, "Добавить бинд", this.btnAdd, n, n2, new Color(40, 110, 40, 240));
        this.drawButton(matrixStack, s7swsm2, f6 + f11 + 6.0f + 75.0f + 6.0f, f10, "Удалить все", this.btnClear, n, n2, new Color(130, 40, 40, 240));
        float f12 = f7 + 22.0f + 8.0f;
        this.listVisibleH = f3 = f9 - 38.0f;
        this.drawBindsList(matrixStack, s7swsm2, f6, f12, f8, f3, n, n2);
        this.p.a(matrixStack, s7swsm2, f, f2, n, n2);
    }

    @Override
    public void a(float f, float f2, int n, int n2, double d, double d2) {
    }

    @Override
    public void a(float f) {
        float f2;
        float f3;
        this.scroll -= f * 12.0f;
        if (this.scroll < 0.0f) {
            this.scroll = 0.0f;
        }
        if (this.scroll > (f3 = Math.max(0.0f, (f2 = (float)BINDS.size() * 38.0f) - this.listVisibleH))) {
            this.scroll = f3;
        }
    }

    private static void execute(String string) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null || minecraftClient.player == null) {
            return;
        }
        if (minecraftClient.player.networkHandler == null) {
            return;
        }
        try {
            if (string.startsWith("/")) {
                minecraftClient.player.networkHandler.sendChatCommand(string.substring(1));
            } else {
                minecraftClient.player.networkHandler.sendChatMessage(string);
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private static boolean hit(float[] fArray, int n, int n2) {
        return (float)n >= fArray[0] && (float)n <= fArray[0] + fArray[2] && (float)n2 >= fArray[1] && (float)n2 <= fArray[1] + fArray[3];
    }

    private static /* synthetic */ String $sf$0(String string) {
        return string + "_";
    }

    private static /* synthetic */ String $sf$3(String string, char c) {
        int n = 640;
        return string + c;
    }

    private static /* synthetic */ String $sf$1(int n) {
        return "KEY " + n;
    }

    private static /* synthetic */ String $sf$2(String string, String string2) {
        return string + string2;
    }
}

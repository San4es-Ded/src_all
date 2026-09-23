package haron.gui.friends;

import haron.gui.core.CategorySelectionModel;
import haron.gui.core.CategorySelectorOverlay;
import haron.gui.core.ScrollFadeOverlay;
import haron.gui.core.ClickGuiTab;
import haron.gui.core.ClickGuiScreen;
import haron.gui.core.ClickGuiTabType;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

public class FriendsTab
implements CategorySelectionModel,
ClickGuiTab {
    private static final Set<String> FRIENDS = new LinkedHashSet<String>();
    public static final Color FRIEND_COLOR = new Color(60, 220, 90);
    public static final int FRIEND_RGB = 3988570;
    public static boolean antiHit = true;
    public static boolean glow = true;
    private static boolean loaded = false;
    private static final String[] CATS = new String[]{"Список друзей"};
    private static final float TOP_OFFSET = 20.0f;
    private static final float SIDE_PAD = 19.0f;
    private static final float ROW_H = 22.0f;
    private static final float BTN_H = 18.0f;
    private static final float GAP = 6.0f;
    private static final float ADD_BTN_W = 24.0f;
    private static final float TOGGLE_W = 74.0f;
    private static final float SEARCH_W_MAX = 120.0f;
    private static final float SEARCH_W_MIN = 60.0f;
    private static final float ADD_W_MIN = 80.0f;
    private static final float CARD_H = 28.0f;
    private static final float CARD_GAP = 5.0f;
    private final CategorySelectorOverlay selector = new CategorySelectorOverlay(this);
    private final ScrollFadeOverlay fade = new ScrollFadeOverlay(25, 10.0f, 7.5f);
    private String addText = "";
    private String searchText = "";
    private boolean addFocused = false;
    private boolean searchFocused = false;
    private final float[] addRect = new float[4];
    private final float[] searchRect = new float[4];
    private final float[] btnAdd = new float[4];
    private final List<float[]> delRects = new ArrayList<float[]>();
    private final List<String> shown = new ArrayList<String>();
    private int mx;
    private int my;
    private float scroll = 0.0f;
    private float listVisibleH = 0.0f;

    public static boolean removeFriend(String string) {
        if (string == null) {
            return false;
        }
        FriendsTab.ensureLoaded();
        boolean bl = FRIENDS.remove(string.toLowerCase(Locale.ROOT));
        if (bl) {
            FriendsTab.save();
        }
        return bl;
    }

    private void drawSmallButton(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, String string, float[] fArray, Color color) {
        boolean bl = (float)this.mx >= f && (float)this.mx <= f + f3 && (float)this.my >= f2 && (float)this.my <= f2 + 18.0f;
        Color color2 = bl ? color.brighter() : color;
        Color color3 = bl ? color : color.darker();
        s7swsm2.a(f, f2, f3, 18.0f, 5.0f, color2, color2, color3, color3, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.MEDIUM[13];
        float f4 = v6hnga2.a(string);
        v6hnga2.a(string, f + (f3 - f4) / 2.0f, (double)(f2 + 5.0f), Color.WHITE, matrixStack);
        fArray[0] = f;
        fArray[1] = f2;
        fArray[2] = f3;
        fArray[3] = 18.0f;
    }

    private void drawInput(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, String string, boolean bl, String string2, float[] fArray) {
        Color color;
        String string3;
        Color color2;
        boolean bl2;
        boolean bl3 = bl2 = (float)this.mx >= f && (float)this.mx <= f + f3 && (float)this.my >= f2 && (float)this.my <= f2 + 18.0f;
        color2 = bl ? pryrvd.ACCENT_SOFT : (bl2 ? pryrvd.ROW_HOVER_TOP : pryrvd.CELL_BG_TOP);
        Color color4 = bl ? pryrvd.ACCENT_DARK : (bl2 ? pryrvd.ROW_HOVER_TOP : pryrvd.CELL_BG_BOT);
        s7swsm2.a(f, f2, f3, 18.0f, 5.0f, color2, color2, color4, color4, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.MEDIUM[12];
        if (string.isEmpty() && !bl) {
            string3 = string2;
            color = new Color(160, 140, 120, 140);
        } else {
            string3 = string;
            if (bl && System.currentTimeMillis() % 1000L < 500L) {
                string3 = FriendsTab.$sf$0(string3);
            }
            color = new Color(240, 220, 200, 230);
        }
        float f4 = f3 - 14.0f;
        while (string3.length() > 1 && v6hnga2.a(string3) > f4) {
            string3 = string3.substring(1);
        }
        v6hnga2.a(string3, f + 8.0f, (double)(f2 + 5.0f), color, matrixStack);
        fArray[0] = f;
        fArray[1] = f2;
        fArray[2] = f3;
        fArray[3] = 18.0f;
    }

    public static boolean canAttack(Entity entity) {
        if (!antiHit) {
            return true;
        }
        return !FriendsTab.isFriend(entity);
    }

    private void drawList(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4) {
        this.delRects.clear();
        this.shown.clear();
        List<String> list = FriendsTab.allFriends();
        String string = this.searchText.trim().toLowerCase(Locale.ROOT);
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string2 : list) {
            if (!string.isEmpty() && !string2.contains(string)) continue;
            arrayList.add(string2);
        }
        if (arrayList.isEmpty()) {
            return;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        float f5 = f2 - this.scroll;
        int n = 0;
        for (String string3 : arrayList) {
            float f6 = f5 + (float)n * 33.0f;
            ++n;
            if (f6 + 28.0f < f2 || f6 > f2 + f4) continue;
            boolean bl = (float)this.mx >= f && (float)this.mx <= f + f3 && (float)this.my >= f6 && (float)this.my <= f6 + 28.0f;
            s7swsm2.a(f, f6, f3, 28.0f, 6.0f, bl ? pryrvd.ROW_HOVER_TOP : pryrvd.CELL_BG_TOP, bl ? pryrvd.ROW_HOVER_TOP : pryrvd.CELL_BG_TOP, bl ? pryrvd.ROW_HOVER_BOT : pryrvd.CELL_BG_BOT, bl ? pryrvd.ROW_HOVER_BOT : pryrvd.CELL_BG_BOT, matrixStack);
            s7swsm2.a(f, f6, 3.0f, 28.0f, 2.0f, FRIEND_COLOR, FRIEND_COLOR, FRIEND_COLOR, FRIEND_COLOR, matrixStack);
            boolean bl2 = false;
            if (minecraftClient != null && minecraftClient.world != null) {
                for (PlayerEntity player : minecraftClient.world.getPlayers()) {
                    if (!player.getName().getString().equalsIgnoreCase(string3)) continue;
                    bl2 = true;
                    break;
                }
            }
            FontRenderer v6hnga2 = ClientFonts.MEDIUM[14];
            v6hnga2.a(string3, f + 12.0f, (double)(f6 + 9.5f), new Color(235, 215, 195, 235), matrixStack);
            FontRenderer statusFont = ClientFonts.MEDIUM[11];
            String string4 = bl2 ? "рядом" : "не в сети";
            Color color = bl2 ? new Color(120, 230, 140, 220) : new Color(150, 130, 115, 150);
            float f7 = statusFont.a(string4);
            statusFont.a(string4, f + f3 - f7 - 44.0f, (double)(f6 + 10.0f), color, matrixStack);
            float f8 = 20.0f;
            float f9 = f + f3 - 30.0f;
            float f10 = f6 + 5.0f;
            boolean bl3 = (float)this.mx >= f9 && (float)this.mx <= f9 + 20.0f && (float)this.my >= f10 && (float)this.my <= f10 + 18.0f;
            Color color2 = bl3 ? new Color(180, 50, 50, 255) : new Color(110, 35, 35, 255);
            Color color3 = bl3 ? new Color(150, 40, 40, 255) : new Color(90, 25, 25, 255);
            s7swsm2.a(f9, f10, 20.0f, 18.0f, 4.0f, color2, color2, color3, color3, matrixStack);
            float f11 = statusFont.a("X");
            statusFont.a("X", f9 + (20.0f - f11) / 2.0f, (double)(f10 + 5.0f), Color.WHITE, matrixStack);
            this.delRects.add(new float[]{f9, f10, 20.0f, 18.0f});
            this.shown.add(string3);
        }
    }

    public static boolean isFriend(String string) {
        if (string == null || string.isEmpty()) {
            return false;
        }
        FriendsTab.ensureLoaded();
        return FRIENDS.contains(string.toLowerCase(Locale.ROOT));
    }

    public static boolean isFriend(Entity entity) {
        if (!(entity instanceof PlayerEntity)) {
            return false;
        }
        return FriendsTab.isFriend(entity.getName().getString());
    }

    private void drawToggle(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, String string, boolean bl, float[] fArray) {
        Color color;
        Color color2;
        boolean bl2;
        boolean bl3 = bl2 = (float)this.mx >= f && (float)this.mx <= f + 74.0f && (float)this.my >= f2 && (float)this.my <= f2 + 18.0f;
        if (bl) {
            color2 = new Color(45, 130, 60, 235);
            color = new Color(32, 100, 45, 235);
        } else if (bl2) {
            color2 = new Color(70, 46, 30, 255);
            color = pryrvd.CELL_BG_BOT;
        } else {
            color2 = pryrvd.CELL_BG_TOP;
            color = pryrvd.CELL_BG_BOT;
        }
        s7swsm2.a(f, f2, 74.0f, 18.0f, 5.0f, color2, color2, color, color, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.MEDIUM[11];
        float f3 = v6hnga2.a(string);
        Color color3 = bl ? new Color(255, 255, 255, 240) : new Color(200, 180, 160, 200);
        v6hnga2.a(string, f + (74.0f - f3) / 2.0f, (double)(f2 + 5.0f), color3, matrixStack);
        fArray[0] = f;
        fArray[1] = f2;
        fArray[2] = 74.0f;
        fArray[3] = 18.0f;
    }

    public static boolean addFriend(String string) {
        if (string == null) {
            return false;
        }
        String string2 = string.trim();
        if (string2.isEmpty() || string2.length() > 16) {
            return false;
        }
        FriendsTab.ensureLoaded();
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient != null && minecraftClient.player != null && minecraftClient.player.getName().getString().equalsIgnoreCase(string2)) {
            return false;
        }
        boolean bl = FRIENDS.add(string2.toLowerCase(Locale.ROOT));
        if (bl) {
            FriendsTab.save();
        }
        return bl;
    }

    public static List<String> allFriends() {
        FriendsTab.ensureLoaded();
        return new ArrayList<String>(FRIENDS);
    }

    public static void clearFriends() {
        FriendsTab.ensureLoaded();
        FRIENDS.clear();
        FriendsTab.save();
    }

    public FriendsTab() {
        FriendsTab.ensureLoaded();
    }

    @Override
    public void b(float f, float f2, int n, int n2) {
        int n3 = 552;
        this.a(f, f2, n, n2);
    }

    @Override
    public boolean b() {
        return this.addFocused || this.searchFocused;
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
        return 0;
    }

    @Override
    public ClickGuiTabType a() {
        return ClickGuiTabType.FRIENDS;
    }

    @Override
    public void a(float f, float f2, int n, int n2, double d, double d2) {
    }

    public boolean a(char c, int n) {
        if (c < ' ' || c == '\u007f') {
            return false;
        }
        if (this.addFocused) {
            boolean bl;
            boolean bl2 = bl = c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '_';
            if (bl && this.addText.length() < 16) {
                this.addText = FriendsTab.$sf$2(this.addText, c);
            }
            return true;
        }
        if (this.searchFocused) {
            if (this.searchText.length() < 32) {
                this.searchText = FriendsTab.$sf$2(this.searchText, c);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean a(int n, int n2, int n3) {
        boolean bl = this.addFocused;
        boolean bl2 = this.searchFocused;
        if (!bl && !bl2) {
            return false;
        }
        if (n == 259) {
            if (bl && !this.addText.isEmpty()) {
                this.addText = this.addText.substring(0, this.addText.length() - 1);
            } else if (bl2 && !this.searchText.isEmpty()) {
                this.searchText = this.searchText.substring(0, this.searchText.length() - 1);
            }
            return true;
        }
        if (n == 257 || n == 335) {
            if (bl) {
                this.doAdd();
            }
            return true;
        }
        if (n == 256) {
            this.addFocused = false;
            this.searchFocused = false;
            return true;
        }
        if (n == 86 && (n3 & 2) != 0) {
            try {
                long l = MinecraftClient.getInstance().getWindow().getHandle();
                String string = GLFW.glfwGetClipboardString((long)l);
                if (string != null) {
                    String string2 = string.replace('\n', ' ').replace('\r', ' ').trim();
                    if (bl && this.addText.length() + string2.length() <= 16) {
                        this.addText = FriendsTab.$sf$1(this.addText, string2);
                    } else if (bl2 && this.searchText.length() + string2.length() <= 32) {
                        this.searchText = FriendsTab.$sf$1(this.searchText, string2);
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

    @Override
    public void a(float f) {
        float f2;
        float f3;
        this.scroll -= f * 12.0f;
        if (this.scroll < 0.0f) {
            this.scroll = 0.0f;
        }
        if (this.scroll > (f3 = Math.max(0.0f, (f2 = (float)FriendsTab.allFriends().size() * 33.0f) - this.listVisibleH))) {
            this.scroll = f3;
        }
    }

    @Override
    public void a(int n) {
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        float f3;
        float f4;
        this.mx = n;
        this.my = n2;
        this.selector.a(matrixStack, s7swsm2, f, f2, n, n2);
        float f5 = ClickGuiScreen.d();
        float f6 = ClickGuiScreen.e();
        float f7 = f + 19.0f;
        float f8 = f2 + 20.0f;
        float f9 = f5 - 38.0f;
        float f10 = f6 - 20.0f - 9.5f;
        float f11 = f8 + 2.0f;
        float f12 = f7 + f9;
        float f13 = f12 - 24.0f;
        float f14 = 120.0f;
        float f15 = f7 + 120.0f + 6.0f;
        float f16 = f13 - 6.0f - f15;
        if (f16 < 80.0f) {
            f4 = 80.0f - f16;
            f14 = Math.max(60.0f, 120.0f - f4);
            f15 = f7 + f14 + 6.0f;
            f16 = f13 - 6.0f - f15;
        }
        if (f16 < 20.0f) {
            f16 = 20.0f;
        }
        this.drawInput(matrixStack, s7swsm2, f7, f11, f14, this.searchText, this.searchFocused, "Поиск", this.searchRect);
        this.drawInput(matrixStack, s7swsm2, f15, f11, f16, this.addText, this.addFocused, "Добавить...", this.addRect);
        this.drawSmallButton(matrixStack, s7swsm2, f13, f11, 24.0f, "+", this.btnAdd, new Color(40, 110, 40, 240));
        f4 = f8 + 22.0f + 8.0f;
        this.listVisibleH = f3 = f10 - 38.0f;
        this.drawList(matrixStack, s7swsm2, f7, f4, f9, f3);
        this.fade.a(matrixStack, s7swsm2, f, f2, n, n2);
    }

    @Override
    public void a(float f, float f2, int n, int n2) {
        this.selector.a(f, f2, n, n2);
        if (FriendsTab.hit(this.addRect, n, n2)) {
            this.addFocused = true;
            this.searchFocused = false;
            return;
        }
        if (FriendsTab.hit(this.searchRect, n, n2)) {
            this.searchFocused = true;
            this.addFocused = false;
            return;
        }
        this.addFocused = false;
        this.searchFocused = false;
        if (FriendsTab.hit(this.btnAdd, n, n2)) {
            this.doAdd();
            return;
        }
        for (int i = 0; i < this.delRects.size(); ++i) {
            if (!FriendsTab.hit(this.delRects.get(i), n, n2)) continue;
            FriendsTab.removeFriend(this.shown.get(i));
            return;
        }
    }

    private static Path file() {
        try {
            String string = System.getenv("APPDATA");
            if (string != null && !string.isEmpty()) {
                return Paths.get(string, "haron", "config", "friends.txt");
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return Paths.get("haron", "friends.txt");
    }

    private static void save() {
        try {
            Path path = FriendsTab.file();
            Files.createDirectories(path.getParent(), new FileAttribute[0]);
            Files.write(path, new ArrayList<String>(FRIENDS), StandardCharsets.UTF_8, new OpenOption[0]);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    private static void ensureLoaded() {
        if (loaded) {
            return;
        }
        loaded = true;
        try {
            Path path = FriendsTab.file();
            if (Files.exists(path, new LinkOption[0])) {
                for (String string : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                    String string2 = string.trim();
                    if (string2.isEmpty() || string2.startsWith("#")) continue;
                    FRIENDS.add(string2.toLowerCase(Locale.ROOT));
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static boolean hit(float[] fArray, int n, int n2) {
        return (float)n >= fArray[0] && (float)n <= fArray[0] + fArray[2] && (float)n2 >= fArray[1] && (float)n2 <= fArray[1] + fArray[3];
    }

    private void doAdd() {
        String string = this.addText.trim();
        if (string.isEmpty()) {
            return;
        }
        if (FriendsTab.addFriend(string)) {
            this.addText = "";
        }
    }

    private static /* synthetic */ String $sf$0(String string) {
        return string + "_";
    }

    private static /* synthetic */ String $sf$1(String string, String string2) {
        return string + string2;
    }

    private static /* synthetic */ String $sf$2(String string, char c) {
        return string + c;
    }
}

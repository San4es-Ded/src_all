package haron.gui.friends;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.core.ScrollFadeOverlay;
import haron.gui.friends.FriendEntry;
import haron.gui.friends.FriendCard;
import haron.gui.widgets.ScrollBar;
import haron.gui.widgets.TextInputType;
import haron.gui.widgets.SearchBox;
import haron.gui.widgets.TextInput;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.client.util.math.MatrixStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FriendsPanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(FriendsPanel.class);
    private static final float a = 19.5f;
    private static final float b = 98.5f;
    private static final float c = 5.0f;
    private static final float d = 19.5f;
    private static final float e = 6.5f;
    private static final float f = 5.0f;
    private static final float g = 9.5f;
    private static final float h = 6.5f;
    private static final float i = 6.5f;
    private static final float j = 6.5f;
    private static final float k = 1.0f;
    private static final float l = 9.5f;
    private static final float m = 2.0f;
    private static final int n = 2;
    private static final Color o = pryrvd.I;
    private static final Color p = pryrvd.K;
    private static final Color q = pryrvd.O;
    private static final Color INPUT_BG_TOP = pryrvd.CELL_BG_TOP;
    private static final Color INPUT_BG_BOT = pryrvd.CELL_BG_BOT;
    private final TextInput u;
    private final ScrollBar v;
    private final ScrollFadeOverlay w;
    private Consumer<FriendEntry> B;
    private float C;
    private float D;
    private float E;
    private float F;
    private float G;
    private float H;
    private float I;
    private final List<FriendEntry> r = new ArrayList<FriendEntry>();
    private final List<FriendCard> s = new ArrayList<FriendCard>();
    private final AnimatedValue x = new AnimatedValue();
    private boolean y = false;
    private String z = "";
    private List<FriendCard> A = new ArrayList<FriendCard>();
    private final SearchBox t = new SearchBox();

    public FriendsPanel() {
        LOGGER.info("FriendsPanel initialized");
        this.t.a(this::a);
        this.u = new TextInput(TextInputType.PLAYER, "Добавить...");
        this.u.a(16);
        this.u.a(9.5f);
        this.u.a(INPUT_BG_TOP, INPUT_BG_BOT);
        this.v = new ScrollBar(2.0f, 20.0f);
        this.v.b(10.0f);
        this.v.a(pryrvd.b);
        this.v.b(pryrvd.d);
        this.w = new ScrollFadeOverlay(25, 5.0f, 9.0f);
        LOGGER.debug("FriendsPanel components initialized: TextInput, ScrollBar, PanelFadeOverlay");
    }

    private void e() {
        if (this.z.isEmpty()) {
            this.A = new ArrayList<FriendCard>(this.s);
            LOGGER.trace("Search cleared, showing all {} friends", (Object)this.s.size());
        } else {
            this.A = this.s.stream().filter(t2tp1d2 -> {
                return t2tp1d2.a().a().toLowerCase().contains(this.z);
            }).collect(Collectors.toList());
            LOGGER.trace("Search found {} matching friends for query '{}'", (Object)this.A.size(), (Object)this.z);
        }
    }

    public void b() {
        LOGGER.info("FriendsPanel refresh requested");
    }

    private void b(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n, int n2, float f5) {
        boolean bl;
        if (this.A.isEmpty()) {
            this.a(matrixStack, s7swsm2, f, f2, f3, f4);
            return;
        }
        float f6 = f + 1.0f;
        float f7 = f2 + 6.5f;
        float f8 = f3 - 2.0f;
        float f9 = f4 - 6.5f - 6.5f;
        float f10 = this.f();
        boolean bl2 = bl = f10 > f9;
        if (!bl) {
            this.v.e();
        }
        s7swsm2.b().a(f, f2 + 4.0f, f3, f4 - 8.0f, matrixStack);
        float f11 = (f8 - 9.5f) / 2.0f;
        float f12 = f7 - this.v.b();
        int n3 = 0;
        for (FriendCard t2tp1d2 : this.A) {
            float f13 = f6 + (float)(n3 % 2) * (f11 + 9.5f);
            float f14 = f12 + (float)(n3 / 2) * 43.5f;
            if (f14 + 37.0f >= f7 && f14 <= f7 + f9) {
                t2tp1d2.a(matrixStack, s7swsm2, f13, f14, f11, n, n2, f5);
            }
            ++n3;
        }
        s7swsm2.b().a(matrixStack);
        if (bl) {
            this.v.a(matrixStack, s7swsm2, f + f3 + 18.0f, f7, f9, f10, f9, n, n2, false);
        }
        this.w.a(matrixStack, s7swsm2, f, f2 + 1.5f, f3, f4, f5);
    }

    public void b(FriendEntry fghxgs2) {
        LOGGER.debug("History entry removed: {}", (Object)fghxgs2);
    }

    public boolean c() {
        return BooleanCoercion.from(this.t.c() || this.u.d() ? 1 : 0);
    }

    private float f() {
        if (this.A.isEmpty()) {
            return 0.0f;
        }
        float f = (float)((int)Math.ceil((double)this.A.size() / 2.0)) * 43.5f - 6.5f;
        LOGGER.trace("Scroll height calculated: {} for {} friends", (Object)Float.valueOf(f), (Object)this.A.size());
        return f;
    }

    public boolean d() {
        return this.r.isEmpty();
    }

    public boolean a(int n, int n2, int n3) {
        boolean handled = this.t.c() ? this.t.a(n, n2, n3) : (this.u.d() && this.u.b(n, n2, n3));
        if (handled) {
            LOGGER.trace("Key typed: char={}, keyCode={}", (Object)n, (Object)n2);
        }
        return handled;
    }

    public void a(float f, int n, int n2) {
        float f2 = this.D + 19.5f + 5.0f;
        float f3 = this.F - 19.5f - 5.0f;
        if (GuiInput.a(this.C, f2, this.E, f3, (double)n, (double)n2)) {
            this.v.a(f, this.f(), f3 - 6.5f - 6.5f);
            LOGGER.trace("Scroll wheel: delta={}", (Object)Float.valueOf(f));
        }
    }

    public void a(int n, int n2, double d, double d2) {
        if (this.v.c()) {
            float f = this.H - 6.5f - 6.5f;
            this.v.a(n2, this.f(), f);
            LOGGER.trace("Scroll drag: dx={}, dy={}", (Object)d, (Object)d2);
        }
    }

    public void a(FriendEntry fghxgs2) {
        LOGGER.debug("History entry added: {}", (Object)fghxgs2);
    }

    public boolean a(char c, int n) {
        boolean handled = this.t.c() ? this.t.a(c, n) : (this.u.d() && this.u.a(c, n));
        if (handled) {
            LOGGER.trace("Character typed: '{}', keyCode={}", (Object)Character.valueOf(c), (Object)n);
        }
        return handled;
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4) {
        FontRenderer v6hnga2 = ClientFonts.b[24];
        FontRenderer v6hnga3 = ClientFonts.a[15];
        String string = this.r.isEmpty() ? "Доюавь новых друзей" : "Ничего не найдено";
        String string2 = this.r.isEmpty() ? "Введи никнейм друга в поле “Добавить” и нажми на плюсик" : "Попробуй изменить запрос";
        float f5 = v6hnga2.a(string);
        float f6 = v6hnga3.a(string2);
        float f7 = f + (f3 - f5) / 2.0f;
        float f8 = f2 + f4 / 2.0f - 20.0f;
        float f9 = f + (f3 - f6) / 2.0f;
        v6hnga2.a(string, f7, (double)f8, pryrvd.a, matrixStack);
        v6hnga3.a(string2, f9, (double)(f8 + 15.0f), pryrvd.b, matrixStack);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5) {
        Color color = pryrvd.b(pryrvd.m, f5);
        Color color2 = pryrvd.b(pryrvd.n, f5);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, f4 + 1.0f, 9.5f, color, color, color2, color2, matrixStack);
        int n = (int)(255.0f * f5);
        Color color3 = pryrvd.a(pryrvd.CELL_BG_TOP, n);
        Color color4 = pryrvd.a(pryrvd.CELL_BG_BOT, n);
        s7swsm2.a(f, f2, f3, f4, 9.5f, color3, color3, color4, color4, matrixStack);
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n, int n2, float f5) {
        this.C = f;
        this.D = f2;
        this.E = f3;
        this.F = f4;
        this.v.a();
        this.x.a();
        float f6 = f3 - 98.5f - 5.0f - 19.5f - 5.0f;
        this.t.a(matrixStack, s7swsm2, f, f2, 98.5f, 19.5f, n, n2);
        float f7 = f + 98.5f + 5.0f;
        this.u.a(matrixStack, s7swsm2, f7, f2, f6, 19.5f, n, n2, f5);
        this.a(matrixStack, s7swsm2, f7 + f6 + 5.0f, f2, n, n2, f5);
        float f8 = f2 + 24.5f;
        float f9 = f4 - (f8 - f2);
        this.G = f8;
        this.H = f9;
        this.I = f3;
        this.b(matrixStack, s7swsm2, f, f8, f3, f9, n, n2, f5);
    }

    private void a(String string) {
        LOGGER.debug("Search query updated: '{}'", (Object)string);
        this.z = string.toLowerCase().trim();
        this.e();
        this.v.e();
        LOGGER.trace("Search results count: {}", (Object)this.A.size());
    }

    public void a(Consumer<FriendEntry> consumer) {
        LOGGER.debug("Setting history consumer");
        this.B = consumer;
    }

    public void a(int n, int n2) {
        this.v.d();
        LOGGER.trace("Scroll event at ({}, {})", (Object)n, (Object)n2);
    }

    public List<FriendEntry> a() {
        LOGGER.trace("Getting history entries, size: {}", (Object)this.r.size());
        return this.r;
    }

    public boolean a(float f, float f2, float f3, float f4, int n, int n2) {
        if (this.t.a(n, n2)) {
            this.u.a(false);
            LOGGER.debug("Search box focused");
            return true;
        }
        float f5 = f3 - 98.5f - 5.0f - 19.5f - 5.0f;
        float f6 = f + 98.5f + 5.0f;
        if (this.u.a(n, n2, 0)) {
            this.t.b(false);
            LOGGER.debug("Text input focused");
            return true;
        }
        if (GuiInput.a(f6 + f5 + 5.0f, f2, 19.5f, 19.5f, (double)n, (double)n2)) {
            this.g();
            LOGGER.info("Add friend button clicked");
            return true;
        }
        float f7 = f2 + 19.5f + 5.0f + 6.5f;
        float f8 = f4 - 19.5f - 5.0f - 6.5f - 6.5f;
        float f9 = f3 - 2.0f;
        float f10 = this.f();
        if (f10 > f8 && this.v.a(f + f3 + 18.0f, f7, f8, f10, f8, n, n2)) {
            LOGGER.trace("Scroll bar interaction");
            return true;
        }
        float f11 = f + 1.0f;
        float f12 = (f9 - 9.5f) / 2.0f;
        if (GuiInput.a(f11, f7, f9, f8, (double)n, (double)n2)) {
            float f13 = f7 - this.v.b();
            int n3 = 0;
            Iterator<FriendCard> iterator = this.A.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().a(f11 + (float)(n3 % 2) * (f12 + 9.5f), f13 + (float)(n3 / 2) * 43.5f, f12, n, n2)) {
                    LOGGER.debug("Friend card clicked at index: {}", (Object)n3);
                    return true;
                }
                ++n3;
            }
        }
        return false;
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2, float f3) {
        int n3 = (int)(255.0f * f3);
        boolean bl = GuiInput.a(f, f2, 19.5f, 19.5f, (double)n, (double)n2);
        if (bl != this.y) {
            this.x.a(bl ? 1.0 : 0.0, 0.15, Easings.h);
            this.y = bl;
            LOGGER.trace("Add friend button hover state changed: {}", (Object)bl);
        }
        float f4 = (float)this.x.j();
        Color color = pryrvd.b(pryrvd.q, f3);
        Color color2 = pryrvd.b(pryrvd.n, f3);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, 20.5f, 20.5f, 6.5f, color, color, color2, color2, matrixStack);
        Color color3 = ColorUtils.a(pryrvd.CELL_BG_TOP, o, f4);
        Color color4 = ColorUtils.a(pryrvd.CELL_BG_BOT, p, f4);
        Color color5 = pryrvd.a(color3, n3);
        Color color6 = pryrvd.a(color4, n3);
        s7swsm2.a(f, f2, 19.5f, 19.5f, 6.5f, color5, color5, color6, color6, matrixStack);
        Color color7 = pryrvd.a(ColorUtils.a(pryrvd.b, q, f4), n3);
        float f5 = f + 7.25f;
        float f6 = f2 + 7.25f;
        s7swsm2.a(f5, f6 + 2.5f - 0.75f, 5.0f, 1.5f, color7, matrixStack);
        s7swsm2.a(f5 + 2.5f - 0.75f, f6, 1.5f, 5.0f, color7, matrixStack);
        if (bl) {
            GuiInput.g();
        }
    }

    private void g() {
        LOGGER.info("Add friend action triggered");
    }
}

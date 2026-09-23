package haron.gui.config;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.client.MinecraftClientAccess;
import haron.config.ConfigProfileEntry;
import haron.core.BooleanCoercion;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.GuiInput;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.client.util.math.MatrixStack;

public class ConfigCard {
    public static final float a = 36.5f;
    private static final float d = 9.5f;
    private static final float e = 11.0f;
    private static final float f = 9.0f;
    private static final float g = 19.5f;
    private static final float h = 6.5f;
    private static final float i = 6.0f;
    private static final float j = 3.0f;
    private static final float k = 5.0f;
    private static final Color l;
    private static final Color m;
    private static final Color n;
    private static final Color o;
    private static final Color p;
    private static final Color q;
    private static final Color r;
    private static final Color s;
    private static final Color t;
    private static final Color u;
    private static final Color v;
    private static final Color w;
    private static final Color x;
    private static final Color y;
    private static final Color z;
    private static final Color A;
    private static final Color B;
    private static final Color C;
    private static final Color D;
    private static final long E = 400L;
    private final ConfigProfileEntry F;
    private Consumer<ConfigProfileEntry> O;
    private Consumer<ConfigProfileEntry> P;
    private Consumer<ConfigProfileEntry> Q;
    private BiConsumer<ConfigProfileEntry, Float[]> R;
    private BiConsumer<String, String> S;
    private float U;
    private float V;
    private float W;
    private static final String ModelCube;
    public static int b;
    public static boolean c;
    private final AnimatedValue G = new AnimatedValue();
    private final AnimatedValue H = new AnimatedValue();
    private final AnimatedValue I = new AnimatedValue();
    private final AnimatedValue J = new AnimatedValue();
    private boolean K = false;
    private boolean L = false;
    private boolean M = false;
    private boolean N = false;
    private long T = 0L;
    private boolean X = false;
    private String Y = "";
    private String Z = "";
    private int aa = 0;
    private int FriendCard = -1;
    private int CosmeticModelRenderer = -1;
    private final AnimatedValue CosmeticModelLoader = new AnimatedValue();
    private boolean ModelPart = true;

    public ConfigCard(ConfigProfileEntry lhvtx72) {
        this.F = lhvtx72;
    }

    static {
        ModelCube = "\\/:*?\"<>| ";
        l = new Color(13, 13, 17);
        m = new Color(17, 17, 23);
        n = new Color(18, 18, 23);
        o = new Color(19, 19, 25);
        p = new Color(26, 26, 46);
        q = new Color(18, 18, 33);
        r = new Color(36, 36, 66);
        s = new Color(18, 18, 33);
        t = new Color(20, 20, 27);
        u = new Color(24, 24, 32);
        v = new Color(36, 36, 60);
        w = new Color(26, 26, 45);
        x = new Color(30, 25, 15);
        y = new Color(25, 20, 12);
        z = new Color(80, 65, 30);
        A = new Color(55, 45, 20);
        B = new Color(40, 33, 18);
        C = new Color(35, 28, 15);
        D = new Color(255, 200, 80);
    }

    public float e() {
        return this.U + this.W - 11.0f - 19.5f - 6.0f - 19.5f;
    }

    private boolean i() {
        return this.FriendCard != -1 && this.CosmeticModelRenderer != -1 && this.FriendCard != this.CosmeticModelRenderer;
    }

    private void b(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, Color color) {
        FontRenderer v6hnga2 = ClientFonts.e[11];
        float f3 = v6hnga2.a("");
        float f4 = v6hnga2.b("");
        v6hnga2.a("", f + (19.5f - f3) / 2.0f, (double)(f2 + (19.5f - f4 / 2.0f) / 2.0f - 0.5f), color, matrixStack);
    }

    public void b() {
        this.X = true;
        this.Z = this.F.a();
        this.Y = this.F.a();
        this.aa = this.Y.length();
        this.FriendCard = -1;
        this.CosmeticModelRenderer = -1;
        this.CosmeticModelLoader.d(1.0);
        this.ModelPart = false;
        this.CosmeticModelLoader.a(0.3, 0.5, Easings.i);
    }

    public void b(BiConsumer<String, String> biConsumer) {
        this.S = biConsumer;
    }

    public void b(Consumer<ConfigProfileEntry> consumer) {
        this.P = consumer;
    }

    public void c(Consumer<ConfigProfileEntry> consumer) {
        this.Q = consumer;
    }

    public String c() {
        return this.Z;
    }

    private void h() {
        this.CosmeticModelLoader.d(1.0);
        this.ModelPart = false;
        this.CosmeticModelLoader.a(0.3, 0.5, Easings.i);
    }

    public float f() {
        return this.V + 8.5f + 19.5f;
    }

    private void l() {
        if (this.i()) {
            int n = Math.min(this.FriendCard, this.CosmeticModelRenderer);
            this.Y = ConfigCard.$sf$1(this.Y.substring(0, n), this.Y.substring(Math.max(this.FriendCard, this.CosmeticModelRenderer)));
            this.aa = n;
            this.j();
        }
    }

    public boolean d() {
        return this.X;
    }

    public boolean a(int n, int n2, int n3) {
        boolean bl;
        if (!this.X) {
            return false;
        }
        boolean bl2 = (~n3 | 2) - ~n3 != 0;
        boolean bl3 = bl = (~n3 | 1) - ~n3 != 0;
        if (n == 257 || n == 335) {
            this.a(true);
            return true;
        }
        if (n == 256) {
            this.a(false);
            return true;
        }
        if (bl2 && n == 65) {
            this.FriendCard = 0;
            this.CosmeticModelRenderer = this.Y.length();
            this.aa = this.Y.length();
            return true;
        }
        if (bl2 && n == 67) {
            if (this.i()) {
                MinecraftClientAccess.c.keyboard.setClipboard(this.k());
            }
            return true;
        }
        if (bl2 && n == 88) {
            if (this.i()) {
                MinecraftClientAccess.c.keyboard.setClipboard(this.k());
                this.l();
                this.h();
            }
            return true;
        }
        if (bl2 && n == 86) {
            String string = MinecraftClientAccess.c.keyboard.getClipboard();
            if (string != null && !string.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (char n4 : string.toCharArray()) {
                    if (!this.a(n4)) continue;
                    stringBuilder.append(n4);
                }
                Object object = stringBuilder.toString();
                if (!((String)object).isEmpty()) {
                    int n5;
                    if (this.i()) {
                        this.l();
                    }
                    if ((n5 = 32 - this.Y.length()) > 0) {
                        String string2 = ((String)object).substring(0, Math.min(((String)object).length(), n5));
                        this.Y = ConfigCard.$sf$0(this.Y.substring(0, this.aa), string2, this.Y.substring(this.aa));
                        int n4 = this.aa;
                        int n6 = string2.length();
                        this.aa = (n4 & ~n6) + (n6 & ~n4) + 2 * (n4 & n6);
                        this.h();
                    }
                }
            }
            return true;
        }
        if (n == 259) {
            if (this.i()) {
                this.l();
            } else if (this.aa > 0) {
                this.Y = ConfigCard.$sf$1(this.Y.substring(0, this.aa - 2 + 1), this.Y.substring(this.aa));
                int n7 = this.aa;
                this.aa = (n7 ^ 1) - 2 * (~n7 & 1);
            }
            this.h();
            return true;
        }
        if (n == 261) {
            if (this.i()) {
                this.l();
            } else if (this.aa < this.Y.length()) {
                String string = this.Y.substring(0, this.aa);
                int n8 = this.aa;
                this.Y = ConfigCard.$sf$1(string, this.Y.substring((n8 | 1) + (n8 & 1)));
            } else if (c) {
                // empty if block
            }
            this.h();
            return true;
        }
        if (n == 263) {
            if (bl) {
                if (!this.i()) {
                    this.FriendCard = this.aa;
                }
                if (this.aa > 0) {
                    --this.aa;
                    this.CosmeticModelRenderer = this.aa;
                }
            } else if (this.i()) {
                this.aa = Math.min(this.FriendCard, this.CosmeticModelRenderer);
                this.j();
            } else if (this.aa > 0) {
                int n9 = this.aa;
                this.aa = (n9 & 0xFFFFFFFE) - (~n9 & 1);
            }
            this.h();
            return true;
        }
        if (n == 262) {
            if (bl) {
                if (!this.i()) {
                    this.FriendCard = this.aa;
                }
                if (this.aa < this.Y.length()) {
                    int n10 = this.aa;
                    this.CosmeticModelRenderer = this.aa = (n10 | 1) + (n10 & 1);
                } else if (c) {
                    // empty if block
                }
            } else if (this.i()) {
                this.aa = Math.max(this.FriendCard, this.CosmeticModelRenderer);
                this.j();
            } else if (this.aa < this.Y.length()) {
                int n11 = this.aa;
                this.aa = (n11 | 1) + (n11 & 1);
            }
            this.h();
            return true;
        }
        if (n == 268) {
            if (bl) {
                if (!this.i()) {
                    this.FriendCard = this.aa;
                }
                this.aa = 0;
                this.CosmeticModelRenderer = 0;
            } else {
                this.aa = 0;
                this.j();
            }
            this.h();
            return true;
        }
        if (n != 269) {
            return true;
        }
        if (bl) {
            if (!this.i()) {
                this.FriendCard = this.aa;
            }
            this.aa = this.Y.length();
            this.CosmeticModelRenderer = this.Y.length();
        } else {
            this.aa = this.Y.length();
            this.j();
        }
        this.h();
        return true;
    }

    public ConfigProfileEntry a() {
        return this.F;
    }

    public void a(boolean bl) {
        if (this.X) {
            this.X = false;
            if (bl) {
                String string = this.Y.trim();
                if (string.isEmpty() || string.equals(this.Z)) {
                    return;
                }
                String string2 = this.Z;
                this.F.a(string);
                if (this.S != null) {
                    this.S.accept(string2, string);
                }
            }
        }
    }

    public boolean a(float f, float f2, float f3, int n, int n2) {
        if (this.X && !GuiInput.a(f, f2, f3, 36.5f, (double)n, (double)n2)) {
            this.a(true);
            return true;
        }
        if (this.F.f()) {
            if (!GuiInput.a(f + f3 - 11.0f - 19.5f, f2 + 8.5f, 19.5f, 19.5f, (double)n, (double)n2)) {
                return false;
            }
            if (this.Q != null) {
                this.Q.accept(this.F);
            }
            return true;
        }
        float f4 = f + f3 - 11.0f - 19.5f;
        float f5 = f4 - 6.0f - 19.5f;
        float f6 = f2 + 8.5f;
        if (GuiInput.a(f5, f6, 19.5f, 19.5f, (double)n, (double)n2)) {
            if (this.X) {
                this.a(true);
            }
            if (this.O != null) {
                this.O.accept(this.F);
            }
            if (this.R != null) {
                this.R.accept(this.F, new Float[]{Float.valueOf(f5), Float.valueOf(f6 + 19.5f), Float.valueOf(19.5f)});
            }
            return true;
        }
        if (GuiInput.a(f4, f6, 19.5f, 19.5f, (double)n, (double)n2)) {
            if (this.X) {
                this.a(true);
            }
            if (this.P != null) {
                this.P.accept(this.F);
            }
            return true;
        }
        if (GuiInput.a(f, f2, f3, 36.5f, (double)n, (double)n2)) {
            long l = System.currentTimeMillis();
            if (l - this.T < 400L) {
                if (!this.X && this.P != null) {
                    this.P.accept(this.F);
                }
                this.T = 0L;
                return true;
            }
            this.T = l;
        }
        return false;
    }

    public boolean a(char c, int n) {
        if (!this.X) {
            return false;
        }
        if (this.a(c)) {
            if (this.i()) {
                this.l();
            }
            if (this.Y.length() < 32) {
                this.Y = ConfigCard.$sf$2(this.Y.substring(0, this.aa), c, this.Y.substring(this.aa));
                int n2 = this.aa;
                this.aa = (n2 & 0xFFFFFFFE) + (1 & ~n2) + 2 * (n2 & 1);
                this.h();
            }
        }
        return true;
    }

    public void a(Consumer<ConfigProfileEntry> consumer) {
        this.O = consumer;
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2, int n3, float f3) {
        float f4;
        FontRenderer v6hnga2 = ClientFonts.a[12];
        FontRenderer v6hnga3 = ClientFonts.b[15];
        FontRenderer v6hnga4 = ClientFonts.b[15];
        float f5 = f + 11.0f;
        float f6 = f2 + 9.0f;
        float f7 = f6 + v6hnga2.b("A") - 6.0f;
        Color color = pryrvd.a(pryrvd.b, n3);
        Color color2 = pryrvd.a(pryrvd.a, n3);
        Color color3 = pryrvd.a(pryrvd.b, n3);
        v6hnga2.a(this.F.d(), f5, (double)f6, color, matrixStack);
        if (!this.X) {
            float f8 = v6hnga3.a(this.F.a());
            v6hnga3.a(this.F.a(), f5, (double)f7, color2, matrixStack);
            float f9 = f5 + f8 + 5.0f;
            s7swsm2.a(f9, f7 + v6hnga3.b("A") / 2.0f - 5.0f, 3.0f, 3.0f, 1.5f, color3, matrixStack);
            v6hnga4.a(this.F.b() != null ? this.F.b() : "", f9 + 3.0f + 5.0f, (double)f7, color2, matrixStack);
            return;
        }
        this.CosmeticModelLoader.a();
        if (this.CosmeticModelLoader.d()) {
            this.ModelPart = BooleanCoercion.from(this.ModelPart ? 0 : 1);
            AnimatedValue urhlup2 = this.CosmeticModelLoader;
            double d = this.ModelPart ? 1.0 : 0.3;
            urhlup2.a(d, 0.5, Easings.i);
        }
        float f10 = v6hnga3.a(this.Y);
        float f11 = v6hnga3.b("A");
        if (this.i()) {
            int n4 = Math.min(this.FriendCard, this.CosmeticModelRenderer);
            s7swsm2.a(f5 + v6hnga3.a(this.Y.substring(0, n4)), f7, v6hnga3.a(this.Y.substring(n4, Math.max(this.FriendCard, this.CosmeticModelRenderer))), f11 - 8.0f, 2.0f, pryrvd.a(new Color(pryrvd.y.getRed(), pryrvd.y.getGreen(), pryrvd.y.getBlue(), 80), n3), matrixStack);
        }
        v6hnga3.a(this.Y, f5, (double)f7, color2, matrixStack);
        if (!this.i() && (f4 = (float)this.CosmeticModelLoader.j()) > 0.1f) {
            s7swsm2.a(f5 + v6hnga3.a(this.Y.substring(0, this.aa)), f7 + 1.0f, 1.5f, f11 - 9.0f, 0.5f, pryrvd.a(pryrvd.y, (int)((float)n3 * f4)), matrixStack);
        }
        float f12 = f5 + f10 + 5.0f;
        s7swsm2.a(f12, f7 + v6hnga3.b("A") / 2.0f - 5.0f, 3.0f, 3.0f, 1.5f, color3, matrixStack);
        float f13 = f12 + 3.0f + 5.0f;
        String string = this.F.b() != null ? this.F.b() : "";
        v6hnga4.a(string, f13, (double)f7, color2, matrixStack);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, float f4, float f5) {
        Color color;
        Color color2;
        Color color3;
        Color color4;
        if (this.F.f()) {
            color4 = pryrvd.a(z, n);
            color3 = pryrvd.a(A, n);
            color2 = pryrvd.a(ColorUtils.a(x, B, f4), n);
            color = pryrvd.a(ColorUtils.a(y, C, f4), n);
        } else {
            Color color5 = ConfigCard.n;
            Color color6 = o;
            Color color7 = r;
            Color color8 = s;
            color4 = pryrvd.a(ColorUtils.a(color5, color7, f5), n);
            color3 = pryrvd.a(ColorUtils.a(color6, color8, f5), n);
            Color color9 = ColorUtils.a(l, t, f4);
            Color color10 = ColorUtils.a(m, u, f4);
            Color color11 = ColorUtils.a(p, v, f4);
            Color color12 = ColorUtils.a(q, w, f4);
            color2 = pryrvd.a(ColorUtils.a(color9, color11, f5), n);
            color = pryrvd.a(ColorUtils.a(color10, color12, f5), n);
        }
        s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, 37.5f, 9.5f, color4, color4, color3, color3, matrixStack);
        s7swsm2.a(f, f2, f3, 36.5f, 9.5f, color2, color2, color, color, matrixStack);
    }

    private void a(AnimatedValue urhlup2, boolean bl, boolean bl2, boolean bl3) {
        if (bl3 && bl2) {
            urhlup2.a(0.0, 0.15, Easings.h);
        } else {
            if (bl3 || bl == bl2) {
                return;
            }
            urhlup2.a(!bl ? 0.0 : 1.0, 0.15, Easings.h);
        }
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4) {
        float f5;
        this.U = f;
        this.V = f2;
        this.W = f3;
        this.G.a();
        this.H.a();
        this.I.a();
        this.J.a();
        int n3 = (int)(255.0f * f4);
        boolean bl = InteractionOverlayController.a().b();
        boolean bl2 = this.F.e();
        boolean bl3 = this.F.f();
        if (!bl3) {
            if (bl2 != this.L) {
                this.H.a(!bl2 ? 0.0 : 1.0, 0.2, Easings.h);
                this.L = bl2;
            } else if (c) {
                // empty if block
            }
        }
        float f6 = (float)this.G.j();
        float f7 = f5 = !bl3 ? (float)this.H.j() : 0.0f;
        if (bl3) {
            float f8 = f + f3 - 11.0f - 19.5f;
            float f9 = f2 + 8.5f;
            int n4 = bl ? 0 : (GuiInput.a(f8, f9, 19.5f, 19.5f, (double)n, (double)n2) ? 1 : 0);
            int n5 = n4;
            int n6 = !bl && GuiInput.a(f, f2, f3, 36.5f, (double)n, (double)n2) && n5 == 0 ? 1 : 0;
            this.a(this.G, BooleanCoercion.from(n6), this.K, bl);
            this.K = BooleanCoercion.from(bl || n6 == 0 ? 0 : 1);
            this.a(this.J, BooleanCoercion.from(n5), this.N, bl);
            this.N = BooleanCoercion.from(bl || n5 == 0 ? 0 : 1);
            this.a(matrixStack, s7swsm2, f, f2, f3, n3, f6, f5);
            this.a(matrixStack, s7swsm2, f, f2, n, n2, n3, f4);
            this.a(matrixStack, s7swsm2, f8, f9, n3);
            if (n5 == 0 || bl) {
                return;
            }
            GuiInput.g();
            return;
        }
        float f10 = f + f3 - 11.0f - 19.5f;
        float f11 = f10 - 6.0f - 19.5f;
        float f12 = f2 + 8.5f;
        int n7 = bl || !GuiInput.a(f11, f12, 19.5f, 19.5f, (double)n, (double)n2) ? 0 : 1;
        int n8 = n7;
        int n9 = bl || !GuiInput.a(f10, f12, 19.5f, 19.5f, (double)n, (double)n2) ? 0 : 1;
        int n10 = !bl && GuiInput.a(f, f2, f3, 36.5f, (double)n, (double)n2) && n8 == 0 && n9 == 0 ? 1 : 0;
        this.a(this.G, BooleanCoercion.from(n10), this.K, bl);
        int n11 = bl ? 0 : (n10 != 0 ? 1 : 0);
        this.K = BooleanCoercion.from(n11);
        this.a(this.I, BooleanCoercion.from(n8), this.M, bl);
        this.M = BooleanCoercion.from(bl || n8 == 0 ? 0 : 1);
        this.a(this.J, BooleanCoercion.from(n9), this.N, bl);
        this.N = BooleanCoercion.from(bl || n9 == 0 ? 0 : 1);
        this.a(matrixStack, s7swsm2, f, f2, f3, n3, f6, f5);
        this.a(matrixStack, s7swsm2, f, f2, n, n2, n3, f4);
        this.a(matrixStack, s7swsm2, f11, f10, f12, n3, f5);
        if (n8 == 0 && n9 == 0 || bl) {
            return;
        }
        GuiInput.g();
    }

    private boolean a(char c) {
        return BooleanCoercion.from("\\/:*?\"<>| ".indexOf(c) != -1 || Character.isISOControl(c) ? 0 : 1);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, float f3, float f4, boolean bl) {
        Color color = ColorUtils.a(l, t, f3);
        Color color2 = ColorUtils.a(m, u, f3);
        Color color3 = ColorUtils.a(ConfigCard.n, color, f3);
        Color color4 = ColorUtils.a(o, color2, f3);
        Color color5 = ColorUtils.a(p, v, f3);
        Color color6 = ColorUtils.a(q, w, f3);
        Color color7 = ColorUtils.a(r, color5, f3);
        Color color8 = ColorUtils.a(s, color6, f3);
        Color color9 = ColorUtils.a(color, color5, f4);
        Color color10 = ColorUtils.a(color2, color6, f4);
        Color color11 = ColorUtils.a(color3, color7, f4);
        Color color12 = ColorUtils.a(color4, color8, f4);
        Color color13 = pryrvd.a(color11, n);
        Color color14 = pryrvd.a(color12, n);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, 20.5f, 20.5f, 6.5f, color13, color13, color14, color14, matrixStack);
        Color color15 = pryrvd.a(color9, n);
        Color color16 = pryrvd.a(color10, n);
        s7swsm2.a(f, f2, 19.5f, 19.5f, 6.5f, color15, color15, color16, color16, matrixStack);
        Color color17 = pryrvd.a(ColorUtils.a(pryrvd.a, pryrvd.aa, f3), n);
        if (bl) {
            this.a(matrixStack, s7swsm2, f, f2, color17);
        } else {
            this.b(matrixStack, s7swsm2, f, f2, color17);
        }
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, Color color) {
        float f3 = 6.5f;
        float f4 = f + 9.0f;
        float f5 = f2 + 6.5f;
        for (int i = 0; i < 3; ++i) {
            s7swsm2.a(f4, f5 + (float)i * 2.5f, 1.5f, 1.5f, 0.75f, color, matrixStack);
        }
    }

    public void a(BiConsumer<ConfigProfileEntry, Float[]> biConsumer) {
        int n = 440;
        this.R = biConsumer;
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n) {
        float f3 = (float)this.J.j();
        Color color = x;
        Color color2 = B;
        Color color3 = z;
        Color color4 = new Color(100, 80, 35);
        Color color5 = pryrvd.a(ColorUtils.a(color, color2, f3), n);
        Color color6 = pryrvd.a(ColorUtils.a(color3, color4, f3), n);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, 20.5f, 20.5f, 6.5f, color6, color6, color6, color6, matrixStack);
        s7swsm2.a(f, f2, 19.5f, 19.5f, 6.5f, color5, color5, color5, color5, matrixStack);
        this.b(matrixStack, s7swsm2, f, f2, pryrvd.a(ColorUtils.a(pryrvd.a, pryrvd.aa, f3), n));
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, float f4) {
        float f5 = (float)this.I.j();
        float f6 = (float)this.J.j();
        this.a(matrixStack, s7swsm2, (float)((int)f), (float)((int)f3), n, f5, f4, true);
        this.a(matrixStack, s7swsm2, (float)((int)f2), (float)((int)f3), n, f6, f4, false);
    }

    private String k() {
        if (!this.i()) {
            return "";
        }
        return this.Y.substring(Math.min(this.FriendCard, this.CosmeticModelRenderer), Math.max(this.FriendCard, this.CosmeticModelRenderer));
    }

    public boolean g() {
        return this.X;
    }

    private void j() {
        this.FriendCard = -1;
        this.CosmeticModelRenderer = -1;
    }

    private static /* synthetic */ String $sf$0(String string, String string2, String string3) {
        return string + string2 + string3;
    }

    private static /* synthetic */ String $sf$1(String string, String string2) {
        return string + string2;
    }

    private static /* synthetic */ String $sf$2(String string, char c, String string2) {
        return string + c + string2;
    }
}


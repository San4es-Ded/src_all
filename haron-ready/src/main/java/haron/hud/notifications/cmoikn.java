package haron.hud.notifications;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.hud.core.HudServices;
import haron.hud.notifications.ijsfy3;
import haron.media.MediaPlayerHudElement;
import haron.media.MediaPlaybackState;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.icons.HaronIcons;
import haron.render.ShapeRenderer;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class cmoikn
extends ijsfy3 {
    private static final Color m = new Color(255, 255, 255);
    private static final Color n = new Color(223, 223, 243);
    private static final Color o = new Color(49, 49, 69);
    private static final Color p = new Color(49, 49, 69);
    private static final Color q = new Color(255, 255, 255);
    private static final Color r = new Color(15, 15, 20);
    private static final float s = 12.0f;
    private static final float t = 8.0f;
    private static final float u = 24.0f;
    private static final float v = 6.0f;
    private static final float w = 8.0f;
    private static final float x = 5.0f;
    private static final float y = 25.0f;
    private static final float z = 103.0f;
    private static final float A = 147.5f;
    private static final float B = 65.0f;
    private static final float C = 9.5f;
    private static final float D = 7.0f;
    private static final float E = 31.0f;
    private static final float F = 10.0f;
    private static final float G = 7.5f;
    private static final float H = 70.0f;
    private static final float I = 12.0f;
    private static final float J = 20.0f;
    private static final long K = 2500L;
    private static final long L = 1500L;
    private static final long M = 8000L;
    private static final int N = 6;
    private static final float O = 2.0f;
    private static final float P = 0.5f;
    private static final float Q = 12.0f;
    private static final float R = 16.0f;
    private static final float S = 2.0f;
    private static final float T = 1.0f;
    private static final float U = 4.0f;
    private static final float V = 3.0f;
    private static final float W = 32.0f;
    private static final float X = 32.0f;
    private static final float Y = 0.0f;
    private float ae;
    private float af;
    private float ag;
    private float ah;
    private float ai;
    private float aj;
    private float ak;
    private float al;
    private float am;
    private float an;
    private float ConfigState;
    private float ConfigProfile;
    private float ConfigJsonCodec;
    private float ConfigManager;
    private float at;
    private float au;
    private float CloudConfigRepository;
    private boolean aA;
    private boolean aB;
    private boolean aC;
    private static final double aT = 0.25;
    public static int a;
    public static boolean b;
    private byte[] Z = null;
    private Identifier aa = null;
    private NativeImageBackedTexture FriendCard = null;
    private boolean FriendsPanel = false;
    private final AnimatedValue ad = new AnimatedValue();
    private boolean ar = false;
    private boolean aw = false;
    private final AnimatedValue ax = new AnimatedValue();
    private final AnimatedValue ay = new AnimatedValue();
    private final AnimatedValue az = new AnimatedValue();
    private final AnimatedValue aD = new AnimatedValue();
    private final AnimatedValue aE = new AnimatedValue();
    private final AnimatedValue aF = new AnimatedValue();
    private final AnimatedValue aG = new AnimatedValue();
    private boolean aH = false;
    private final AnimatedValue aI = new AnimatedValue();
    private final AnimatedValue aJ = new AnimatedValue();
    private String aK = "";
    private final AnimatedValue aL = new AnimatedValue();
    private boolean aM = false;
    private double aN = 0.0;
    private double aO = 0.0;
    private long aP = 0L;
    private long aQ = 0L;
    private boolean aR = false;
    private boolean aS = false;

    public cmoikn() {
        this.g = 9.5f;
        this.h = 7.0f;
        this.j = Long.MAX_VALUE;
        this.ad.d(0.0);
        this.ax.d(0.0);
        this.ay.d(0.0);
        this.az.d(0.0);
        this.aD.d(0.0);
        this.aE.d(0.0);
        this.aF.d(0.0);
        this.aG.d(0.0);
        this.aI.d(0.0);
        this.aJ.d(1.0);
        this.aL.d(0.0);
    }

    @Override
    public void e() {
        super.e();
        this.ad.a();
        this.g = 9.5f;
        this.h = 7.0f;
        float f = (float)this.ad.j();
        MediaPlaybackState xukx3d2 = this.r();
        if (xukx3d2 != null) {
            boolean bl;
            boolean bl2;
            byte[] byArray = xukx3d2.p();
            if (xukx3d2.q()) {
                if (byArray == null || byArray.length <= 0) {
                    this.aa = null;
                    this.Z = null;
                } else {
                    this.a(byArray);
                }
                xukx3d2.f();
            } else if (this.aa == null && this.Z == null && byArray != null && byArray.length > 0) {
                this.a(byArray);
            }
            this.ax.a();
            this.ay.a();
            this.az.a();
            this.aD.a();
            this.aE.a();
            this.aF.a();
            this.aG.a();
            this.aI.a();
            this.aJ.a();
            this.aL.a();
            boolean bl3 = xukx3d2.c();
            if (bl3 != this.aH) {
                this.aH = bl3;
                this.aG.a(!bl3 ? 0.0 : 1.0, 0.2, Easings.k);
            }
            boolean bl4 = bl2 = !bl3;
            if (bl2 && this.aI.j() < 0.5) {
                this.aI.a(1.0, 0.2, Easings.k);
            } else if (!bl2 && this.aI.j() > 0.5) {
                this.aI.a(0.0, 0.2, Easings.k);
            }
            String string = xukx3d2.n();
            if (!string.equals(this.aK)) {
                this.aK = string;
                this.aJ.d(0.0);
                this.aJ.a(1.0, 0.25, Easings.k);
                this.aR = false;
                this.aS = false;
            }
            FontRenderer v6hnga2 = ClientFonts.b[14];
            FontRenderer v6hnga3 = ClientFonts.b[15];
            FontRenderer v6hnga4 = ClientFonts.a[13];
            boolean bl5 = !((f >= 0.2f ? v6hnga3 : v6hnga2).a(string) <= this.a(25.0f, 70.0f, f));
            boolean bl6 = bl5;
            if (bl6 && !this.aR) {
                this.aR = true;
                this.aP = System.currentTimeMillis();
            } else if (!bl6) {
                this.aR = false;
            }
            boolean bl7 = bl = v6hnga4.a(xukx3d2.o()) > 70.0f && f > 0.3f;
            if (bl && !this.aS) {
                this.aS = true;
                this.aQ = System.currentTimeMillis();
            } else {
                if (bl) {
                    return;
                }
                this.aS = false;
            }
        }
    }

    public boolean b(double d, double d2, int n) {
        MediaPlayerHudElement dvkv1a2 = this.q();
        MediaPlaybackState xukx3d2 = this.r();
        if (dvkv1a2 == null || xukx3d2 == null) {
            return false;
        }
        if (n != 0 || !xukx3d2.y()) {
            return false;
        }
        dvkv1a2.c(Math.max(0.0f, Math.min(1.0f, (float)(d - (double)this.ConfigManager) / this.au)));
        return true;
    }

    @Override
    public float b() {
        return this.a(12.0f, 65.0f, (float)this.ad.j());
    }

    private float b(float f) {
        float f2 = Math.max(0.0f, Math.min(1.0f, f));
        return f2 * f2 * (3.0f - 2.0f * f2);
    }

    public void b(double d, double d2) {
        MediaPlayerHudElement dvkv1a2 = this.q();
        MediaPlaybackState xukx3d2 = this.r();
        if (dvkv1a2 == null || xukx3d2 == null) {
            return;
        }
        if (xukx3d2.y() && this.au > 0.0f) {
            dvkv1a2.b(Math.max(0.0f, Math.min(1.0f, (float)(d - (double)this.ConfigManager) / this.au)));
        }
    }

    private void s() {
        MediaPlaybackState xukx3d2 = this.r();
        if (!this.ar || xukx3d2 == null) {
            this.aA = false;
            this.aB = false;
            this.aC = false;
            this.aM = false;
            return;
        }
        double d = this.aN;
        double d2 = this.aO;
        boolean bl = this.aA;
        this.aA = this.a(d, d2, this.ai, this.aj, this.ak, this.ak);
        if (this.aA != bl) {
            this.ax.a(!this.aA ? 0.0 : 1.0, 0.15, Easings.k);
        }
        boolean bl2 = this.aB;
        this.aB = this.a(d, d2, this.al, this.am, this.an, this.an);
        if (this.aB != bl2) {
            double d3 = this.aB ? 1.0 : 0.0;
            this.ay.a(d3, 0.15, Easings.k);
        }
        boolean bl3 = this.aC;
        this.aC = this.a(d, d2, this.ConfigState, this.ConfigProfile, this.ConfigJsonCodec, this.ConfigJsonCodec);
        if (this.aC != bl3) {
            this.az.a(!this.aC ? 0.0 : 1.0, 0.15, Easings.k);
        }
        if (this.aw) {
            boolean bl4 = this.aM;
            this.aM = BooleanCoercion.from(d < (double)(this.ConfigManager - 4.0f) || d > (double)(this.ConfigManager + this.au + 4.0f) || d2 < (double)(this.at - 4.0f) || d2 > (double)(this.at + this.CloudConfigRepository + 4.0f) ? 0 : 1);
            if (this.aM != bl4 || xukx3d2.y()) {
                this.aL.a(this.aM || xukx3d2.y() ? 1.0 : 0.0, 0.15, Easings.k);
            }
        }
    }

    public void n() {
        if (this.FriendsPanel) {
            this.FriendsPanel = false;
            this.ad.a(0.0, 0.25, Easings.k);
        }
        this.aA = false;
        this.aB = false;
        this.aC = false;
        this.aM = false;
    }

    private float a(float f) {
        return f >= 0.5f ? 1.0f - (float)Math.pow(-2.0f * f + 2.0f, 3.0) / 2.0f : 4.0f * f * f * f;
    }

    private float a(float f, float f2, long l, boolean bl) {
        if (!bl || f <= f2) {
            return 0.0f;
        }
        float f3 = f - f2;
        long l2 = (System.currentTimeMillis() - l) % 8000L;
        return -f3 * (l2 >= 1500L ? (l2 >= 4000L ? (l2 >= 5500L ? 1.0f - this.a((float)(l2 - 1500L - 2500L - 1500L) / 2500.0f) : 1.0f) : this.a((float)(l2 - 1500L) / 2500.0f)) : 0.0f);
    }

    private Color a(Color color, Color color2, float f) {
        return new Color((int)this.a(color.getRed(), color2.getRed(), f), (int)this.a(color.getGreen(), color2.getGreen(), f), (int)this.a(color.getBlue(), color2.getBlue(), f), (int)this.a(color.getAlpha(), color2.getAlpha(), f));
    }

    private boolean a(double d, double d2, float f, float f2, float f3, float f4) {
        return !(d < (double)f) && !(d > (double)(f + f3)) && d2 >= (double)f2 && d2 <= (double)(f2 + f4);
    }

    public boolean a(double d, double d2, int n) {
        if (n != 0) {
            return false;
        }
        MediaPlayerHudElement dvkv1a2 = this.q();
        MediaPlaybackState xukx3d2 = this.r();
        if (dvkv1a2 == null || xukx3d2 == null) {
            return false;
        }
        if (this.aw && xukx3d2.h() > 0.0f && d >= (double)(this.ConfigManager - 4.0f) && d <= (double)(this.ConfigManager + this.au + 4.0f) && d2 >= (double)(this.at - 4.0f) && d2 <= (double)(this.at + this.CloudConfigRepository + 4.0f)) {
            dvkv1a2.a(Math.max(0.0f, Math.min(1.0f, (float)(d - (double)this.ConfigManager) / this.au)));
            return true;
        }
        if (!this.ar) {
            return false;
        }
        if (this.a(d, d2, this.ai, this.aj, this.ak, this.ak)) {
            this.aD.d(1.0);
            this.aD.a(0.0, 0.2, Easings.k);
            dvkv1a2.f();
            return true;
        }
        if (this.a(d, d2, this.al, this.am, this.an, this.an)) {
            this.aE.d(1.0);
            this.aE.a(0.0, 0.2, Easings.k);
            dvkv1a2.d();
            return true;
        }
        if (!this.a(d, d2, this.ConfigState, this.ConfigProfile, this.ConfigJsonCodec, this.ConfigJsonCodec)) {
            return false;
        }
        this.aF.d(1.0);
        this.aF.a(0.0, 0.2, Easings.k);
        dvkv1a2.e();
        return true;
    }

    public void a(double d, double d2) {
        this.aN = d;
        this.aO = d2;
        this.t();
        this.s();
    }

    private void a(byte[] byArray) {
        try {
            BufferedImage bufferedImage;
            if (this.FriendCard != null) {
                this.FriendCard.close();
                this.FriendCard = null;
            }
            if ((bufferedImage = ImageIO.read(new ByteArrayInputStream(byArray))) == null) {
                return;
            }
            int n = bufferedImage.getWidth();
            int n2 = bufferedImage.getHeight();
            NativeImage nativeImage = new NativeImage(n, n2, false);
            for (int i = 0; i < n2; ++i) {
                for (int j = 0; j < n; ++j) {
                    nativeImage.setColorArgb(j, i, bufferedImage.getRGB(j, i));
                }
            }
            RenderSystem.recordRenderCall(() -> {
                this.FriendCard = new NativeImageBackedTexture(nativeImage);
                this.aa = Identifier.of((String)"crypt", (String)"crypt");
                c.getTextureManager().registerTexture(this.aa, (AbstractTexture)this.FriendCard);
            });
            this.Z = byArray;
        }
        catch (Exception exception) {
            this.aa = null;
        }
    }

    @Override
    public float a() {
        int n = 829;
        return this.a(103.0f, 147.5f, (float)this.ad.j());
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5) {
        this.ae = f - this.g;
        this.af = f2 - this.h;
        this.ag = f3 + this.g * 2.0f;
        this.ah = f4 + this.h * 2.0f;
        MediaPlaybackState xukx3d2 = this.r();
        if (xukx3d2 != null) {
            int n;
            float f6;
            Color color;
            float f7 = (float)this.ad.j();
            int n2 = (int)(f5 * 255.0f);
            FontRenderer v6hnga2 = ClientFonts.b[15];
            FontRenderer v6hnga3 = ClientFonts.b[14];
            FontRenderer v6hnga4 = ClientFonts.a[13];
            FontRenderer v6hnga5 = ClientFonts.a[11];
            String string = xukx3d2.n();
            String string2 = xukx3d2.o();
            boolean bl = xukx3d2.c();
            float f8 = xukx3d2.h();
            float f9 = this.a(12.0f, 31.0f, f7);
            float f10 = this.a(8.0f, 10.0f, f7);
            float f11 = (float)this.aI.j();
            float f12 = f11 * 0.08f * Math.max(0.0f, (f7 - 0.3f) / 0.7f);
            float f13 = 1.0f - f11 * 0.3f;
            float f14 = f9 * (1.0f - f12);
            float f15 = (f9 - f14) / 2.0f;
            float f16 = this.a(f2 + f4 / 2.0f - 6.0f, f2, f7) + f15;
            float f17 = f + f15;
            Color color2 = new Color(255, 255, 255, (int)((float)n2 * f13));
            if (this.aa != null) {
                s7swsm2.a(this.aa, f17, f16, f14, f14, f10, color2, matrixStack);
            } else {
                s7swsm2.a(HaronIcons.get("crypt"), f17, f16, f14, f14, f10, color2, matrixStack);
            }
            float f18 = this.a(f + 12.0f + 6.0f, f + 31.0f + 5.5f, f7);
            float f19 = this.a(f2 + f4 / 2.0f - v6hnga3.b(string) / 4.0f - 0.5f, f2 + 15.5f - 9.0f, f7);
            float f20 = (float)this.aJ.j();
            Color color3 = new Color(m.getRed(), m.getGreen(), m.getBlue(), (int)((float)n2 * f20));
            float f21 = this.a(25.0f, 70.0f, f7);
            float f22 = this.a(20.0f, 0.0f, f7);
            FontRenderer v6hnga6 = f7 >= 0.2f ? v6hnga2 : v6hnga3;
            float f23 = v6hnga6.a(string);
            boolean bl2 = !(f23 <= f21);
            boolean bl3 = bl2;
            float f24 = this.a(f23, f21 + f22, this.aP, this.aR);
            if (bl3) {
                s7swsm2.b().a(f18, f19 - 3.0f, f21 + f22, 16.0f, matrixStack);
            }
            v6hnga6.a(string, f18 + f24, (double)f19, color3, matrixStack);
            if (bl3) {
                s7swsm2.b().a(matrixStack);
                Color color4 = new Color(r.getRed(), r.getGreen(), r.getBlue(), 0);
                color = new Color(r.getRed(), r.getGreen(), r.getBlue(), n2);
                s7swsm2.a(f18 + f21 + f22 - 12.0f + 1.0f, f19 - 3.0f, 12.0f, 16.0f, 0.0f, color4, color, color4, color, matrixStack);
            }
            if (f7 > 0.01f) {
                int n3 = (int)(f5 * this.b(f7) * f20 * 255.0f);
                color = new Color(cmoikn.n.getRed(), cmoikn.n.getGreen(), cmoikn.n.getBlue(), n3);
                f6 = v6hnga4.a(string2);
                boolean bl4 = f6 > 70.0f;
                float f25 = this.a(f6, 70.0f, this.aQ, this.aS);
                if (bl4) {
                    s7swsm2.b().a(f18, f19 + 7.0f, 70.0f, 16.0f, matrixStack);
                }
                v6hnga4.a(string2, f18 + f25, (double)(f19 + 10.0f), color, matrixStack);
                if (bl4) {
                    s7swsm2.b().a(matrixStack);
                    Color color5 = new Color(r.getRed(), r.getGreen(), r.getBlue(), 0);
                    Color color6 = new Color(r.getRed(), r.getGreen(), r.getBlue(), n3);
                    s7swsm2.a(f18 + 70.0f - 12.0f, f19 + 7.0f, 12.0f, 16.0f, 0.0f, color5, color6, color5, color6, matrixStack);
                }
            }
            float f26 = this.a(12.0f, 16.0f, f7);
            float f27 = this.a(f + f3 - 14.5f - 4.0f, f + f3 - 14.5f - 4.0f, f7) + 4.0f;
            f6 = this.a(f2 + f4 / 2.0f, f2 + f26 / 2.0f, f7);
            float[] fArray = xukx3d2.H();
            for (n = 0; n < 6; ++n) {
                float f28 = fArray[n];
                float f29 = Math.max(2.0f, f28 * f26);
                float f30 = f27 + (float)n * 2.5f;
                float f31 = f6 - f29 / 2.0f;
                int n4 = (int)this.a(180.0f, 255.0f, f28);
                s7swsm2.a(f30, f31, 2.0f, f29, 1.0f, this.a(new Color(n4, n4, n4, n2), new Color((int)this.a(o.getRed(), n4, 0.5f), (int)this.a(o.getGreen(), n4, 0.5f), (int)this.a(o.getBlue(), n4, 0.5f), n2), f7), matrixStack);
            }
            if (f7 <= 0.01f) {
                this.ar = false;
                this.aw = false;
                return;
            }
            n = (int)(f5 * this.b(f7) * 255.0f);
            float f32 = f2 + 31.0f + 7.5f;
            float f33 = xukx3d2.b() * f8;
            String string3 = this.a((int)f33);
            String string4 = cmoikn.$sf$0(this.a((int)(f8 - f33)));
            float f34 = v6hnga5.a(string3);
            float f35 = v6hnga5.a(string4);
            Color color7 = new Color(cmoikn.n.getRed(), cmoikn.n.getGreen(), cmoikn.n.getBlue(), n);
            float f36 = f32 + 2.0f - 4.0f;
            v6hnga5.a(string3, f, (double)f36, color7, matrixStack);
            v6hnga5.a(string4, f + f3 - f35, (double)f36, color7, matrixStack);
            float f37 = f + f34 + 3.5f;
            float f38 = f3 - f34 - f35 - 7.0f;
            this.ConfigManager = f37;
            this.at = f32 - 0.5f;
            this.au = f38;
            this.CloudConfigRepository = 4.0f;
            this.aw = true;
            float f39 = (float)this.aL.j();
            float f40 = 4.0f + f39 * 2.0f;
            float f41 = f32 - 0.5f - f39 * 1.0f;
            float f42 = 3.0f + f39 * 1.0f;
            s7swsm2.a(f37, f41, f38, f40, f42, new Color(p.getRed(), p.getGreen(), p.getBlue(), n), matrixStack);
            float f43 = f38 * Math.max(0.04f, xukx3d2.b());
            Color color8 = new Color(q.getRed(), q.getGreen(), q.getBlue(), n);
            if (f43 > 0.0f) {
                s7swsm2.a(f37, f41, f43, f40, f42, 0.0f, f42, 0.0f, color8, color8, color8, color8, matrixStack);
            }
            float f44 = f32 + 4.0f + 7.5f - 10.0f;
            float f45 = f + f3 / 2.0f;
            float f46 = 0.9f + (float)this.ax.j() * 0.1f - (float)this.aD.j() * 0.2f;
            int n5 = (int)(255.0f * Math.min(1.0f, f46));
            int n6 = (int)((float)n * Math.min(1.0f, f46));
            float f47 = f45 - 16.0f - 0.0f - 32.0f;
            float f48 = f44 + 0.0f;
            Color color9 = new Color(n5, n5, n5, n6);
            FontRenderer v6hnga7 = ClientFonts.e[35];
            v6hnga7.a("crypt", f47 + (32.0f - v6hnga7.a("crypt")) / 2.0f, (double)(f48 + (32.0f - v6hnga7.b("crypt") / 2.0f) / 2.0f), color9, matrixStack);
            float f49 = 0.9f + (float)this.ay.j() * 0.1f - (float)this.aE.j() * 0.2f;
            int n7 = (int)(255.0f * Math.min(1.0f, f49));
            int n8 = (int)((float)n * Math.min(1.0f, f49));
            float f50 = f45 - 16.0f;
            Color color10 = new Color(n7, n7, n7, n8);
            FontRenderer v6hnga8 = ClientFonts.e[35];
            String string5 = !bl ? "crypt" : "crypt";
            v6hnga8.a(string5, f50 + (32.0f - v6hnga8.a(string5)) / 2.0f, (double)(f44 + (32.0f - v6hnga8.b(string5) / 2.0f) / 2.0f), color10, matrixStack);
            float f51 = 0.9f + (float)this.az.j() * 0.1f - (float)this.aF.j() * 0.2f;
            int n9 = (int)(255.0f * Math.min(1.0f, f51));
            int n10 = (int)((float)n * Math.min(1.0f, f51));
            float f52 = f45 + 16.0f + 0.0f;
            Color color11 = new Color(n9, n9, n9, n10);
            FontRenderer v6hnga9 = ClientFonts.e[35];
            v6hnga9.a("crypt", f52 + (32.0f - v6hnga9.a("crypt")) / 2.0f, (double)(f48 + (32.0f - v6hnga9.b("crypt") / 2.0f) / 2.0f), color11, matrixStack);
            this.ai = f47;
            this.aj = f48;
            this.ak = 32.0f;
            this.al = f50;
            this.am = f44;
            this.an = 32.0f;
            this.ConfigState = f52;
            this.ConfigProfile = f48;
            this.ConfigJsonCodec = 32.0f;
            this.ar = true;
        }
    }

    private String a(int n) {
        return cmoikn.$sf$1(n / 60, String.format("crypt", n % 60));
    }

    private float a(float f, float f2, float f3) {
        return f + (f2 - f) * f3;
    }

    public void m() {
        if (this.ar || this.aw) {
            double d = this.aN;
            double d2 = this.aO;
            boolean bl = false;
            if (this.ar) {
                if (this.a(d, d2, this.ai, this.aj, this.ak, this.ak)) {
                    bl = true;
                }
                if (this.a(d, d2, this.al, this.am, this.an, this.an)) {
                    bl = true;
                }
                if (this.a(d, d2, this.ConfigState, this.ConfigProfile, this.ConfigJsonCodec, this.ConfigJsonCodec)) {
                    bl = true;
                }
            }
            if (this.aw && !(d < (double)(this.ConfigManager - 4.0f)) && d <= (double)(this.ConfigManager + this.au + 4.0f) && d2 >= (double)(this.at - 4.0f) && d2 <= (double)(this.at + this.CloudConfigRepository + 4.0f)) {
                bl = true;
            }
            if (bl) {
                GuiInput.g();
            }
        }
    }

    public void o() {
        if (this.FriendCard != null) {
            this.FriendCard.close();
            this.FriendCard = null;
        }
        this.aa = null;
        this.Z = null;
    }

    public static boolean p() {
        return MediaPlayerHudElement.g();
    }

    private void t() {
        boolean bl = this.FriendsPanel;
        if (this.aN >= (double)this.ae) {
            int n = this.aN <= (double)(this.ae + this.ag) && this.aO >= (double)this.af && this.aO <= (double)(this.af + this.ah) ? 1 : 0;
            this.FriendsPanel = BooleanCoercion.from(n);
            if (this.FriendsPanel == bl) {
                double d = this.FriendsPanel ? 1.0 : 0.0;
                this.ad.a(d, 0.25, Easings.k);
                return;
            }
            return;
        }
        boolean bl2 = false;
        this.FriendsPanel = BooleanCoercion.from(0);
    }

    private MediaPlayerHudElement q() {
        return HudServices.MEDIA;
    }

    private MediaPlaybackState r() {
        MediaPlayerHudElement dvkv1a2 = this.q();
        if (dvkv1a2 != null) {
            return dvkv1a2.i();
        }
        return null;
    }

    private static /* synthetic */ String $sf$0(String string) {
        return "crypt" + string;
    }

    private static /* synthetic */ String $sf$1(int n, String string) {
        return n + "crypt" + string;
    }
}


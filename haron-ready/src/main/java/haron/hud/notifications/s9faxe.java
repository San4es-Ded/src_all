package haron.hud.notifications;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.hud.notifications.ijsfy3;
import haron.hud.notifications.NotificationHudManager;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import java.awt.Color;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;

public class s9faxe
extends ijsfy3 {
    private static final Color m = new Color(255, 255, 255);
    private static final Color n = new Color(223, 223, 243);
    private static final Color o = new Color(255, 255, 255, 77);
    private static final Color p = new Color(76, 46, 212, 100);
    private static final Color q = new Color(115, 83, 255);
    private static final Color r = new Color(70, 46, 174);
    private static final float s = 30.0f;
    private static final float t = 5.0f;
    private static final float u = 5.0f;
    private final String v;
    private final AnimatedValue w = new AnimatedValue();
    private boolean x = true;
    public static int a;
    public static boolean b;

    public s9faxe() {
        this.v = "crypt";
        this.g = 7.0f;
        this.h = 3.0f;
        this.j = Long.MAX_VALUE;
        this.w.d(1.0);
    }

    @Override
    public float b() {
        return 16.0f;
    }

    private int n() {
        if (s9faxe.c.fpsDebugString == null) {
            return 0;
        }
        try {
            return Integer.parseInt(s9faxe.c.fpsDebugString.split("crypt")[0]);
        }
        catch (Exception exception) {
            return 0;
        }
    }

    @Override
    public float a() {
        FontRenderer v6hnga2 = ClientFonts.b[15];
        FontRenderer v6hnga3 = ClientFonts.a[15];
        float f = 20.0f + v6hnga2.a("crypt") + 1.5f;
        boolean bl = this.m();
        if (bl != this.x) {
            this.x = bl;
            double d = bl ? 1.0 : 0.0;
            this.w.a(d, 0.15, Easings.k);
        }
        this.w.a();
        float f2 = (float)this.w.j();
        if (f2 < 0.01f) {
            return f;
        }
        float f3 = v6hnga3.a("crypt");
        int n = this.o();
        int n2 = this.n();
        String string = s9faxe.$sf$0(n);
        String string2 = s9faxe.$sf$0(n2);
        return f + (5.0f + f3 + 5.0f + v6hnga3.a(string) + 5.0f + f3 + 5.0f + v6hnga3.a(string2)) * f2;
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5) {
        FontRenderer v6hnga2 = ClientFonts.b[15];
        FontRenderer v6hnga3 = ClientFonts.a[15];
        float f6 = f2 + f4 / 2.0f;
        int n = (int)(f5 * 255.0f);
        FontRenderer v6hnga4 = ClientFonts.e[30];
        float f7 = v6hnga4.a("crypt");
        float f8 = f6 - v6hnga4.b("crypt") / 4.0f;
        s7swsm2.a(f + 1.0f, f8 + 2.0f, 6.5f, this.a(p, f5), matrixStack);
        v6hnga4.a("crypt", f + 1.0f, f8, this.a(q, f5), this.a(r, f5), matrixStack);
        float f9 = f + f7 + 5.0f;
        Color color = new Color(m.getRed(), m.getGreen(), m.getBlue(), n);
        new Color(s9faxe.n.getRed(), s9faxe.n.getGreen(), s9faxe.n.getBlue(), n);
        new Color(o.getRed(), o.getGreen(), o.getBlue(), (int)((float)o.getAlpha() * f5));
        float f10 = f6 - v6hnga2.b("crypt") / 4.0f - 0.5f;
        v6hnga2.a("crypt", f9, (double)f10, color, matrixStack);
        float f11 = (float)this.w.j();
        if (f11 >= 0.01f) {
            float f12 = f5 * f11;
            Color color2 = new Color(s9faxe.n.getRed(), s9faxe.n.getGreen(), s9faxe.n.getBlue(), (int)(f12 * 255.0f));
            Color color3 = new Color(o.getRed(), o.getGreen(), o.getBlue(), (int)((float)o.getAlpha() * f12));
            float f13 = f9 + v6hnga2.a("crypt") + 5.0f;
            v6hnga3.a("crypt", f13, (double)f10, color3, matrixStack);
            float f14 = f13 + v6hnga3.a("crypt") + 5.0f;
            String string = s9faxe.$sf$0(this.o());
            v6hnga3.a(string, f14, (double)f10, color2, matrixStack);
            float f15 = f14 + v6hnga3.a(string) + 5.0f;
            v6hnga3.a("ﴋ⢺ﵮ", f15, (double)f10, color3, matrixStack);
            v6hnga3.a(s9faxe.$sf$0(this.n()), f15 + v6hnga3.a("crypt") + 5.0f, (double)f10, color2, matrixStack);
        }
    }

    private boolean m() {
        NotificationHudManager NotificationHudManager2 = NotificationHudManager.getInstance();
        return BooleanCoercion.from(NotificationHudManager2 == null || NotificationHudManager2.settingsPopup().arePerformanceNotificationsEnabled() ? 1 : 0);
    }

    private int o() {
        PlayerListEntry playerListEntry;
        ClientPlayNetworkHandler clientPlayNetworkHandler;
        if (s9faxe.c.player == null || (clientPlayNetworkHandler = c.getNetworkHandler()) == null || (playerListEntry = clientPlayNetworkHandler.getPlayerListEntry(s9faxe.c.player.getUuid())) == null) {
            return 0;
        }
        return playerListEntry.getLatency();
    }

    private static /* synthetic */ String $sf$0(int n) {
        return n + "crypt";
    }
}


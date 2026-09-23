package haron.gui.friends;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.GuiInput;
import haron.gui.friends.FriendEntry;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FriendCard {
    public static final float a = 182.5f;
    public static final float b = 37.0f;
    private static final float e = 9.5f;
    private static final float f = 6.5f;
    private static final float g = 24.0f;
    private static final float h = 5.0f;
    private static final float i = 6.5f;
    private static final float j = -8.0f;
    private static final float k = 13.5f;
    private static final float l = 4.0f;
    private static final float m = 6.5f;
    private final FriendEntry n;
    private final AnimatedValue o = new AnimatedValue();
    private final AnimatedValue p = new AnimatedValue();
    private boolean q = false;
    private boolean r = false;
    private Consumer<FriendEntry> s;
    private float t;
    private float u;
    private float v;
    public static int c;

    public FriendCard(FriendEntry fghxgs2) {
        this.n = fghxgs2;
    }

    private Identifier b() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.getNetworkHandler() != null) {
            for (PlayerListEntry playerListEntry : minecraftClient.getNetworkHandler().getPlayerList()) {
                if (playerListEntry.getProfile() == null || playerListEntry.getProfile().getName() == null || !playerListEntry.getProfile().getName().equalsIgnoreCase(this.n.a())) continue;
                return playerListEntry.getSkinTextures().texture();
            }
        }
        return DefaultSkinHelper.getTexture();
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3) {
        int n = (int)(255.0f * f3);
        float f4 = (float)this.p.j();
        Color color = pryrvd.b(pryrvd.q, f3);
        Color color2 = pryrvd.b(pryrvd.n, f3);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, 14.5f, 14.5f, 4.0f, color, color, color2, color2, matrixStack);
        Color color3 = ColorUtils.a(pryrvd.f, pryrvd.L, f4);
        Color color4 = ColorUtils.a(pryrvd.e, pryrvd.M, f4);
        Color color5 = pryrvd.a(color3, n);
        Color color6 = pryrvd.a(color4, n);
        s7swsm2.a(f, f2, 13.5f, 13.5f, 4.0f, color5, color5, color6, color6, matrixStack);
        ClientFonts.e[15].a("ﻺ", f + 2.75f, (double)(f2 + 2.75f - 0.5f), pryrvd.a(ColorUtils.a(pryrvd.b, pryrvd.Z, f4), n), matrixStack);
    }

    public boolean a(float f, float f2, float f3, int n, int n2) {
        if (!GuiInput.a(f + f3 - 6.5f - 13.5f, f2 + 6.5f, 13.5f, 13.5f, (double)n, (double)n2)) {
            return false;
        }
        if (this.s != null) {
            this.s.accept(this.n);
        }
        return true;
    }

    public FriendEntry a() {
        return this.n;
    }

    public void a(Consumer<FriendEntry> consumer) {
        this.s = consumer;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4) {
        int n3;
        this.t = f;
        this.u = f2;
        this.v = f3;
        this.o.a();
        this.p.a();
        int n4 = (int)(255.0f * f4);
        boolean bl = InteractionOverlayController.a().b();
        int n5 = bl || !GuiInput.a(f, f2, f3, 37.0f, (double)n, (double)n2) ? 0 : 1;
        float f5 = f + f3 - 6.5f - 13.5f;
        float f6 = f2 + 6.5f;
        int n6 = n3 = bl || !GuiInput.a(f5, f6, 13.5f, 13.5f, (double)n, (double)n2) ? 0 : 1;
        if (bl && this.q) {
            this.o.a(0.0, 0.15, Easings.h);
            this.q = false;
        } else if (!bl && BooleanCoercion.from(n5) != this.q) {
            this.o.a(n5 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
            this.q = BooleanCoercion.from(n5);
        }
        if (bl && this.r) {
            this.p.a(0.0, 0.15, Easings.h);
            this.r = false;
        } else if (!bl && BooleanCoercion.from(n3) != this.r) {
            this.p.a(n3 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
            this.r = BooleanCoercion.from(n3);
        }
        float f7 = (float)this.o.j();
        Color color = pryrvd.a(pryrvd.m, n4);
        Color color2 = pryrvd.a(pryrvd.n, n4);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, 38.0f, 9.5f, color, color, color2, color2, matrixStack);
        Color color3 = ColorUtils.a(pryrvd.e, pryrvd.g, f7);
        Color color4 = ColorUtils.a(pryrvd.f, pryrvd.h, f7);
        Color color5 = pryrvd.a(color3, n4);
        Color color6 = pryrvd.a(color4, n4);
        s7swsm2.a(f, f2, f3, 37.0f, 9.5f, color5, color5, color6, color6, matrixStack);
        float f8 = f + 6.5f;
        this.a(matrixStack, s7swsm2, f8, f2 + 6.5f, n4);
        FontRenderer v6hnga2 = ClientFonts.a[15];
        FontRenderer v6hnga3 = ClientFonts.a[11];
        float f9 = f8 + 24.0f + 6.5f;
        String string = this.n.a();
        String string2 = this.n.c();
        float f10 = v6hnga2.b(string);
        float f11 = f2 + (37.0f - (f10 + -8.0f + v6hnga3.b(string2) - 6.0f)) / 2.0f;
        float f12 = f11 + f10 + -8.0f;
        Color color7 = pryrvd.a(pryrvd.a, n4);
        Color color8 = pryrvd.a(pryrvd.b, n4);
        v6hnga2.a(string, f9, (double)f11, color7, matrixStack);
        v6hnga3.a(string2, f9, (double)f12, color8, matrixStack);
        this.a(matrixStack, s7swsm2, f5, f6, f4);
        if (n3 == 0 || bl) {
            return;
        }
        GuiInput.g();
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n) {
        Color color = pryrvd.a(pryrvd.m, n);
        Color color2 = pryrvd.a(pryrvd.n, n);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, 25.0f, 25.0f, 5.0f, color, color, color2, color2, matrixStack);
        Identifier identifier = this.b();
        if (identifier == null) {
            s7swsm2.a(f, f2, 24.0f, 24.0f, 5.0f, pryrvd.a(pryrvd.FriendsPanel, n), matrixStack);
            return;
        }
        RenderSystem.setShaderTexture((int)0, (Identifier)identifier);
        Color color3 = pryrvd.a(pryrvd.aa, n);
        s7swsm2.a(identifier, f, f2, 24.0f, 24.0f, 5.0f, 0.125f, 0.125f, 0.125f, 0.125f, color3, matrixStack);
        s7swsm2.a(identifier, f, f2, 24.0f, 24.0f, 5.0f, 0.625f, 0.125f, 0.125f, 0.125f, color3, matrixStack);
    }
}

